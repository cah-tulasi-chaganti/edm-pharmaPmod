package com.cardinalhealth.aem.segmentupdate.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

import javax.jcr.SimpleCredentials;
import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PropertyIterator;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.jackrabbit.commons.JcrUtils;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.FrameworkUtil;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import com.day.cq.search.QueryBuilder;

import com.cardinalhealth.aem.segmentupdate.AEMSegmentUpdateService;
import com.cardinalhealth.aem.segmentupdate.utility.AEMSegmentUpdateOutputFileFormat;
import com.cardinalhealth.aem.segmentupdate.utility.AEMSegmentUpdateUtility;
import com.cardinalhealth.aem.segmentupdate.utility.CSVReader;
import com.cardinalhealth.aem.segmentupdate.utility.Constants;
import com.cardinalhealth.aem.segmentupdate.utility.QueryBuilderRestUtility;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

/**
 * One implementation of the {@link AEMSegmentUpdateService}. Note that
 * the repository is injected, not retrieved.
 */

@Component(immediate = true, metatype = true, label = "CAH AEM Segment Update Service for PMOD", description = "Segment Update service for PMOD")
@Service(AEMSegmentUpdateImpl_Backup.class)
@Properties({
	@Property(name = "aemsegmentmgtservice.sysuser", label = "System User", description = "System User having access on DAM, content and segment", value = "oesystemuser"),
	@Property(name = "aemsegmentmgtservice.damInputFolPath", label = "DAM Input Base Location", description = "Enter Input folder Location.", value = Constants.AEM_SEGMENT_INPUT_FOLDER),
	@Property(name = "aemsegmentmgtservice.damOutPutFolPath", label = "DAM Output Base Location", description = "Enter Output folder Location.", value = Constants.AEM_SEGMENT_OUTPUT_FOLDER),
	@Property(name = "aemsegmentmgtservice.segmentFolder", label = "Segmentation Folder", description = "Enter Segmentation path location.", value = Constants.AEM_SEGMENT_FOLDER),
	@Property(name = "aemsegmentmgtservice.taritValue", label = "Trait Value", description = "Enter trait value to be updated(Cins/dcAccount)", value = Constants.AEM_DEFAULT_TRAIT_VALUE),
	@Property(name = "aemsegmentmgtservice.isAuthInstance", label = "Is Author Instance", description = "Is Author Instance.", value = "1") })
public class AEMSegmentUpdateImpl_Backup implements AEMSegmentUpdateService {
	@Override
	public String updateContent() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String updateAEMSegments() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getAEMSegmentsORContent(String aemType, String traitType) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
//    
//	
//	private String sysuser = "";	
//	private String inputFolPath = "";
//	private String outputFolPath = "";
//	private String segmentFolder ="";
//	private String defaultTriatType = "";
//    @Reference
//    private SlingRepository repository;
//        
//    @Reference
//	private Replicator replicator;
//      
//    
//    @Reference
// 	private ResourceResolverFactory resolverFactory;    
//    
//    @Reference
//    private QueryBuilder builder;
//	static HashMap<Integer, String> segmentsMap = new HashMap<Integer, String>();
//	private static final Logger LOGGER = LoggerFactory.getLogger(AEMSegmentUpdateImpl_Backup.class);
//	static CSVReader csvReader = new CSVReader();
//
//
//    /**
//	 * This method read configuration value. If value is blank or null then read
//	 * default value.
//	 * 
//	 * @param props
//	 */
//	@Activate
//	protected void activate(Map<String, Object> props) {
//		try {
//			
//			BundleContext bundleContext = FrameworkUtil.getBundle(AEMSegmentUpdateImpl_Backup.class).getBundleContext();
//			ServiceReference resolverFactoryRef = bundleContext.getServiceReference(ResourceResolverFactory.class.getName());			
//			resolverFactory = (ResourceResolverFactory) bundleContext.getService(resolverFactoryRef);
//			ServiceReference replicatorRef = bundleContext.getServiceReference(Replicator.class.getName());
//			replicator = (Replicator) bundleContext.getService(replicatorRef);
//			ServiceReference queryBuilderRef = bundleContext.getServiceReference(QueryBuilder.class.getName());
//			builder = (QueryBuilder) bundleContext.getService(queryBuilderRef);
//			sysuser = AEMSegmentUpdateUtility.getConfigurationProperty(props, "aemsegmentmgtservice.sysuser", Constants.systemUser);
//			inputFolPath = AEMSegmentUpdateUtility.getConfigurationProperty(props, "aemsegmentmgtservice.damInputFolPath", Constants.AEM_SEGMENT_INPUT_FOLDER);
//			outputFolPath = AEMSegmentUpdateUtility.getConfigurationProperty(props, "aemsegmentmgtservice.damOutPutFolPath", Constants.AEM_SEGMENT_OUTPUT_FOLDER);
//			segmentFolder = AEMSegmentUpdateUtility.getConfigurationProperty(props, "aemsegmentmgtservice.segmentFolder", Constants.AEM_SEGMENT_FOLDER);
//			defaultTriatType = AEMSegmentUpdateUtility.getConfigurationProperty(props, "aemsegmentmgtservice.taritValue", Constants.AEM_DEFAULT_TRAIT_VALUE);
//		} catch (Exception ex) {
//			 ex.printStackTrace();
//			LOGGER.info("Exception inside activate method"+ null+ ex.getMessage());
//		}
//	}
//    
//    public String updateAEMSegments() throws IOException{
//    	Date date = new Date() ;
//    	long startTime = System.currentTimeMillis();	
//    	String traitType = "";
//        ResourceResolver resourceResolver = null;     
//        LOGGER.info("StartTIme is:"+startTime);    
//        
//        if(this.defaultTriatType.equalsIgnoreCase("dcAccount")) {
//        	traitType= Constants.AEM_DC_ACCOUNT_TRAIT_VALUE;
//        }else if(this.defaultTriatType.equalsIgnoreCase("cins")) {
//        	traitType= Constants.AEM_CINS_TRAIT_VALUE;
//        } 
//
//        Session session = null;
//    	//HashMap<String , String> hmap = csvReader.cusotmerData();
//		Map<String, Object> serviceParams = new HashMap<String, Object>();
//		serviceParams.put(ResourceResolverFactory.SUBSERVICE, this.sysuser);
//		String methodName = "updateSegment";
//		AEMSegmentUpdateOutputFileFormat outlog = new AEMSegmentUpdateOutputFileFormat();
//		try {
//			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "after getting service param:" +serviceParams);
//			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "#CSV file processing start.");
//			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, Constants.newLine + "#Source-" );			
//			resourceResolver = resolverFactory.getServiceResourceResolver(serviceParams);			
//			session = resourceResolver.adaptTo(Session.class);
//			
//			
//			// read out file details and set in map.
//			Map<String, String> fileDetailsMap = CSVReader.cusotmerData(resourceResolver, outlog, this.inputFolPath);
////			if(traitType.equalsIgnoreCase(Constants.AEM_DC_ACCOUNT_TRAIT_VALUE)) {
////				segmentsMap = AEMSegmentUpdateUtility.allSegmentsforDCAccount(resourceResolver, this.segmentFolder,outlog, traitType);
////			}else if(traitType.equalsIgnoreCase(Constants.AEM_DC_ACCOUNT_TRAIT_VALUE)) {
////				segmentsMap  = AEMSegmentUpdateUtility.allSegmentsforKeyword(resourceResolver, this.segmentFolder,outlog, traitType);
////			}
//			
//			
//			//Iterator<Node> itr = allChild.iterator();
//			Iterator itr = segmentsMap.entrySet().iterator();
//			
//			//outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "Segmentation Path:"+this.segmentFolder);
//			
//			int count = 0, firstMatch = 0;
//			boolean foundMatch = false;			
//			while (itr.hasNext()) {
//				Map.Entry currentNode = (Map.Entry) itr.next();
//				outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"Segment Path is:"+currentNode.getKey()+" "+currentNode.getValue().toString());
//										
//				Resource segNodeResource = resourceResolver.getResource(currentNode.getValue().toString());
//				Node segMentNode = null;
//				if(segNodeResource != null) {
//					segMentNode = segNodeResource.adaptTo(Node.class);
//				if(segMentNode != null && segMentNode.getProperty(traitType)!=null) {
//						String[] nodeCurrentValue =  segMentNode.getProperty(traitType).getString().split(",");
//						outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"Current Node property :  " +segMentNode.getProperty(traitType).getString());
//						String newNodeValue = "" ;
//						for(int i =0; i<nodeCurrentValue.length; i++) {
//							
//							if(i<3000) {
//								String traitVal ="";
//								String dcAccountDC ="";
//								if(traitType.equalsIgnoreCase(Constants.AEM_DC_ACCOUNT_TRAIT_VALUE)) {
//									traitVal = nodeCurrentValue[i].substring(nodeCurrentValue[i].indexOf('_')+1,nodeCurrentValue[i].length());
//									dcAccountDC = nodeCurrentValue[i].substring(0,nodeCurrentValue[i].indexOf('_'));
//								}else if(traitType.equalsIgnoreCase(Constants.AEM_CINS_TRAIT_VALUE)) {
//									traitVal = nodeCurrentValue[i];
//								}
//								
//								Iterator iterator = fileDetailsMap.entrySet().iterator();
//								while (iterator.hasNext()) {
//									Map.Entry me2 = (Map.Entry) iterator.next();
//									if(traitVal.equals(me2.getKey())) {							
//										if(firstMatch == 0) {
//											foundMatch= true;
//										}
//										traitVal =  me2.getValue().toString();
//										firstMatch++;
//									}
//								}
//								if(traitType.equalsIgnoreCase(Constants.AEM_DC_ACCOUNT_TRAIT_VALUE)) {
//									newNodeValue+= dcAccountDC+"_" +traitVal+",";
//								}else if(traitType.equalsIgnoreCase(Constants.AEM_CINS_TRAIT_VALUE)) {
//									newNodeValue+= traitVal+",";
//									
//								}
//								
//							}//if less than 3000 value
//							
//							
//						}//for loop ends
//						if(foundMatch) {
//							outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"---Modified node is:  "+ newNodeValue );
//							segMentNode.setProperty(traitType, newNodeValue);
//							session.save();
//							outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"Segment to be activated: "+currentNode.getValue().toString());
//													
//						}				
//						count++;		
//						
//					}//segment node not null
//				}//segment node resource not null
//					
//					
//					
//					
//											
//				//}				
//			}
//			outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"Total AEM Segments to be updated are:"+String.valueOf(count));
//			
//		} catch (Exception e) {
//			outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"exception at the segments:"+e.getMessage());
//			e.printStackTrace();
//		}finally {
//			try {
//				long processingTime = (Calendar.getInstance().getTimeInMillis() - startTime);
//				AEMSegmentUpdateUtility.createOutputFiles(resourceResolver, processingTime, outputFolPath, outlog);
////				if (session != null) {
////					AEMSegmentUpdateUtility.saveSession(resourceResolver, session);
////					//session.logout();
////				}
//				if (resourceResolver != null && resourceResolver.isLive()) {
//					resourceResolver.commit();
//					resourceResolver.close();
//				}
//			} catch (Exception ex) {
//				LOGGER.error("",
//						" Exception while creating a output file and saving session" + ex.getMessage());
//				ex.printStackTrace();
//			}
//		}		
//
//	
//        return repository.getDescriptor(Repository.REP_NAME_DESC);
//    }//updateAEMSegment
//
//    
//    public String updateContent() throws IOException {
//    	AEMSegmentUpdateOutputFileFormat outlog = new AEMSegmentUpdateOutputFileFormat();
//    	Session session = null;
//    	ResourceResolver resourceResolver = null;
//    	long startTime = System.currentTimeMillis();	
//    	try {
//    		
//    		JSONObject aemExtractValues = null;
//    		JSONArray aemExtractValuesArray = null;
//    		String extractPath = "/content/oe";
//        	String methodName = "updateContent";
//        	String authorPort = "14502";  //Default value for local- this will be used unless property is overwritten in AEM env by JPM Team
//        	String authExtractOutputDIRPath = "/content/dam/oe/SegmentUpdate/Output";  //DEV/QA/STG Path:  /opt/OE/AEMExtract/Output
//        	String authUserId = "oeextractuser";
//        	String authPsswrd = "Cardinal1";
//        	String nodeDepth = "100";
//        	String nodeLimit = "-1";
//        	String queryTimeOut = "60000";
//        	String sortingParameter = "jcr:title";  
//        	String hostName = "localhost";  
//        	String protocol = "http";
//        	
//	    
//	    	Map<String, Object> serviceParams = new HashMap<String, Object>();
//			serviceParams.put(ResourceResolverFactory.SUBSERVICE, this.sysuser);
//			
//	    	resourceResolver = resolverFactory.getServiceResourceResolver(serviceParams);
//	    	session = resourceResolver.adaptTo(Session.class);
//	    	
//	    	// read out file details and set in map.
//        	Map<String, String> fileDetailsMap = CSVReader.cusotmerData(resourceResolver, outlog, this.inputFolPath);
//        	String queryBuilderResponse = QueryBuilderRestUtility.executeExtractQuery(extractPath,authorPort,
//        								authExtractOutputDIRPath,authUserId,authPsswrd,nodeDepth,nodeLimit,queryTimeOut,
//        								protocol,hostName,sortingParameter);
//        	outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "Response from QueryBuilderRestUtility is received.\n");
//        	
//    		if (null !=queryBuilderResponse && !queryBuilderResponse.isEmpty()) {
//    			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, " Processing Response as JSON and Going to create file.");
//    			aemExtractValues = new JSONObject(queryBuilderResponse);
//    			aemExtractValuesArray = aemExtractValues.getJSONArray("hits");
//    			//LOGGER.info("aemExtractValues are:"+aemExtractValues);
//    			//outputStrBuilder.append(AEMConstant.ALL_CONTENT_CSV_HEADER);
//    			JSONObject currentHit = null;
//    			JSONObject innerHit = null;
//    			JSONObject iteratableInnerJSON =null;
//    			String path =null;
//    			int countContent =0;
//    			int count = 0, firstMatch = 0;
//    			boolean foundMatch = false;	
//    			outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"TOTAL CONTENT FOUND ARE:"+ aemExtractValuesArray.length());
//    			for (int i = 0; i < aemExtractValuesArray.length(); i++) {
//    				currentHit = aemExtractValuesArray.getJSONObject(i);
//    				//outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"currentHit:"+ currentHit);
//    				String[] cinsArray = {};
//    				String nodePath = "";
//    				if(currentHit.opt("jcr:path") != null && currentHit.opt("cq:lastReplicationAction")!= null && currentHit.opt("cq:lastReplicationAction").equals("Activate")) {
//    					
//    					if(currentHit.has("cinForSearch") && currentHit.opt("cinForSearch")!= null ) {
//    						cinsArray = currentHit.opt("cinForSearch").toString().split(","); 
//    						nodePath =  currentHit.opt("jcr:path").toString();
//    					}else {
//    						nodePath =  currentHit.opt("jcr:path").toString();
//    						Iterator<?> keys = currentHit.keys();
//        						while( keys.hasNext() ) {
//        							String outerKey = (String)keys.next();
//        							String currentOuterKey = outerKey;
//        								if(currentOuterKey.contains("par") || currentOuterKey.equals("par")) {
//        									if(currentHit.optJSONObject(currentOuterKey) != null) {
//        										nodePath+="/par";
//        										iteratableInnerJSON = currentHit.optJSONObject(currentOuterKey);
//        										Iterator<?> innerkeys = iteratableInnerJSON.keys();
//        										
//        										while( innerkeys.hasNext() ) {
//        											String key = (String)innerkeys.next();
//        											//if ( keyArray instanceof JSONArray ) {
//        												String currentKey = key;
//        												//outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "innerKey JSON is:"+iteratableInnerJSON.optJSONObject(currentKey));
//        												if(iteratableInnerJSON.optJSONObject(currentKey) != null) {
//        													
//        													if(iteratableInnerJSON.optJSONObject(currentKey).opt("cinForSearch") != null) {
//        														//outputStrBuilder.append(iteratableInnerJSON.optJSONObject(currentKey).opt("cinForSearch")!= null ? handleLargeNumbersWithCommas(((JSONArray)iteratableInnerJSON.optJSONObject(currentKey).opt("cinForSearch")).toString()) :"") ; //Product Value;
//        														cinsArray = iteratableInnerJSON.optJSONObject(currentKey).opt("cinForSearch").toString().split(",");
//        														//nodePath+=  "/cinForSearch";
//        														nodePath+="/"+currentKey;
//        														break;
//        													}
//        													
//        												}
//        											//}
//        										}
//        									}
//        								}
//        							//}
//        						}
//        					}
//    					
//    				
//    						countContent++;
//    						outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"NODEPATH IS:"+nodePath );
//    						String cinArr = Arrays.toString(cinsArray); 
//    						outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"Current Node properties are:"+cinArr);
//    						String newNodeValue = "";
//    						for(int i1=0;i1<cinsArray.length;i1++) {    														
//    							Iterator iterator = fileDetailsMap.entrySet().iterator();
//    							while (iterator.hasNext()) {
//    								Map.Entry me2 = (Map.Entry) iterator.next();
//    								if(cinsArray[i1].equals(me2.getKey())) {							
//    									if(firstMatch == 0) {
//    										foundMatch= true;
//    									}
//    									cinsArray[i1] =  me2.getValue().toString();
//    									firstMatch++;
//    								}
//    							}//while filedetailmap ends
//    							newNodeValue+= cinsArray[i1]+",";
//    							if(foundMatch) {
//    								Resource segNodeResource = resourceResolver.getResource(nodePath);
//    								Node segMentNode = null;
//    								if(segNodeResource != null) {
//    									segMentNode = segNodeResource.adaptTo(Node.class);
//	    								if(segMentNode != null && segMentNode.getProperty("cinForSearch")!=null) {
//	    									segMentNode.setProperty("cinForSearch", newNodeValue);
//	    									session.save();
//	    								}
//	    							}
//    								outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"Segment to be activated: "+segMentNode.getProperty("cinForSearch").toString());
//    														
//    							}	
//    						}//cins node end
//    						
//    					}//current key
//    					
//    				
//    			}//for all content items
//    			
//    			LOGGER.info("TOTAL COUNT WITH CINS ARE:"+countContent);
//    			
//    		}//if ends
//    		
//    	}catch (Exception e) {
//			// TODO: handle exception
//    		outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"exception while looping:"+e.getMessage());
//		}finally {
//			try {
//				long processingTime = (Calendar.getInstance().getTimeInMillis() - startTime);
//				AEMSegmentUpdateUtility.createOutputFiles(resourceResolver, processingTime, outputFolPath, outlog);
////				if (session != null) {
////					AEMSegmentUpdateUtility.saveSession(resourceResolver, session);
////					//session.logout();
////				}
//				if (resourceResolver != null && resourceResolver.isLive()) {
//					resourceResolver.commit();
//					resourceResolver.close();
//				}
//			} catch (Exception ex) {
//				LOGGER.error("",
//						" Exception while creating a output file and saving session" + ex.getMessage());
//				ex.printStackTrace();
//			}
//		}
//    	
//    	return repository.getDescriptor(Repository.REP_NAME_DESC);
//    	
//    }//updatecontent ends
//   
//
//
}
