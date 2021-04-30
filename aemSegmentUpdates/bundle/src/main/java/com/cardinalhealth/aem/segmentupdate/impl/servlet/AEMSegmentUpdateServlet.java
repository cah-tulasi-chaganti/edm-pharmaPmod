package com.cardinalhealth.aem.segmentupdate.impl.servlet;

import java.io.IOException;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.sling.SlingServlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;


import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cardinalhealth.aem.segmentupdate.AEMSegmentUpdateService;
import com.cardinalhealth.aem.segmentupdate.impl.AEMSegmentUpdateImpl;




/**
 * SegmentMgtServlet is a wrapper servlet to call SegmentMgtService. 
 * This can be only used when SegmentMgtService needs to invoked manually by IT team.
 *
 */
@SlingServlet(
		methods = { "GET" },
		paths = { "/bin/AEMSegmentUpdateServletPMOD" }
)
public class AEMSegmentUpdateServlet extends SlingSafeMethodsServlet {
	private static final long serialVersionUID = 1L;
	private static final String CLASS_NAME = "AEMSegmentUpdateServlet";
	private static final Logger LOGGER = LoggerFactory.getLogger(AEMSegmentUpdateServlet.class);

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		try{
		LOGGER.info("-AEMSegmentUpdateServlet-----Processing Start--------"+ response.getWriter());
		response.getWriter().append("--AEMSegmentUpdateServlet-----Processing Started.................");
		BundleContext bundleContext = FrameworkUtil.getBundle(AEMSegmentUpdateServlet.class).getBundleContext();
		ServiceReference segmentMgtServiceRef = bundleContext.getServiceReference(AEMSegmentUpdateImpl.class.getName());
		AEMSegmentUpdateService segmentMgtService = (AEMSegmentUpdateService) bundleContext.getService(segmentMgtServiceRef);
		if(request.getParameter("updateAEMLayer")!=null && request.getParameter("updateAEMLayer").equalsIgnoreCase("segments")) {
			segmentMgtService.updateAEMSegments();
		}else if(request.getParameter("updateAEMLayer") !=null && request.getParameter("updateAEMLayer").equalsIgnoreCase("content")) {
			segmentMgtService.updateContent();
		}else if(request.getParameter("aemType")!=null && request.getParameter("aemType").equalsIgnoreCase("segments")) {
			if(request.getParameter("traitType")!=null && request.getParameter("traitType").equalsIgnoreCase("dcAccount")) {
				segmentMgtService.getAEMSegmentsORContent("segments", "dcAccount");
			}else if(request.getParameter("traitType")!=null && request.getParameter("traitType").equalsIgnoreCase("keyword")) {
			segmentMgtService.getAEMSegmentsORContent("segments", "keyword");
			}else if(request.getParameter("traitType")!=null && request.getParameter("traitType").equalsIgnoreCase("affiliation")) {
				segmentMgtService.getAEMSegmentsORContent("segments", "affiliation");
			}
			
		}else if(request.getParameter("aemType")!=null && request.getParameter("aemType").equalsIgnoreCase("content")) {
			segmentMgtService.getAEMSegmentsORContent("content", "none");
		}
		
		// Disable Dispatcher caching of this response.
		response.setHeader("Dispatcher", "no-cache");
		response.getWriter().append("--SegmentMgtServlet-----Processing End.................");
		}catch(Exception e){
			LOGGER.info("-SegmentMgtServlet------Exception-------:"+e.getMessage()+ response.getWriter());
			e.printStackTrace(response.getWriter());
		}
	}
	
	
	
}
