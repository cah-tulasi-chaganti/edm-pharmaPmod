package com.cardinalhealth.aem.segmentupdate.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;



public class CSVReader {
	private final static Logger LOGGER = LoggerFactory.getLogger(CSVReader.class);
	public  static HashMap<String, String> cusotmerData(ResourceResolver resourceResolver, AEMSegmentUpdateOutputFileFormat outlog, 
			String inputFolder, String traitType) throws IOException {
		// 
		Date date = new Date();
		Resource res = resourceResolver.getResource(inputFolder);
	    Asset asset = res.adaptTo(Asset.class);
	    Rendition rendition = asset.getOriginal();
		InputStream inputStream = rendition.adaptTo(InputStream.class);
		//String csvFile = assetNode;
	    String line = "";
	    String cvsSplitBy =",";
	    String[] country = null ;
	    HashMap<String, String> hmap = new HashMap<String, String>();
	    try {
	    	//InputStreamReader ir = new InputStreamReader(assetNode.getRendition(Constants.ORIGINAL).getStream())
	    	BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
	    	//BufferedReader br = new BufferedReader(new FileReader(csvFile));
	    	br.readLine();
	    	//String nextLine = br.readLine();
	    	int count=0;
	    	LOGGER.info("traitType is  IN CSV FIle is:"+traitType);
	    	
	        while ((line = br.readLine()) != null) {
	        	try {
		            // use comma as separator
		            country = line.split(cvsSplitBy); 
		            if(StringUtils.isNumeric(country[1])) {
		            	//LOGGER.info("country NUMERICCC");
		            	if(traitType.equalsIgnoreCase("dcAccount")) {
		            		hmap.put(country[0] + "_" + country[1], country[0] + "_" + country[3]);
		            	}else if(traitType.equalsIgnoreCase("affiliation")) {
		            		if(country[1]!=null && country[1].length()>0) {
		            			hmap.put( country[0], country[1]);	
		            		}
		            	}else {
		            		//LOGGER.info("country NOTTTT NUMERICCC");
		            		hmap.put( country[1], country[0]);		            	 
		            	}
		            	
		 	            count++;
		            }
	        	}catch (Exception e) {
	        		LOGGER.info("exception is :"+e.getMessage());
					// TODO: handle exception
	        		outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "EXCEption occured while reading is:"+e.getMessage());
	        		
				}
	        }
	        // Now print the hasmap        
	        Iterator iterator = hmap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry me2 = (Map.Entry) iterator.next();
				outlog.updateOutputfileMap(Constants.csvRetrievelMsg,"OLD VALUE to NEW VALUE IS:"+me2.getKey() +" ," + me2.getValue());
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	        LOGGER.info("exception is :"+e.getMessage());
			// TODO: handle exception
    		outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "Exception  occured while priting  is:"+e.getMessage());
	    }
    
	    return hmap;

	}
}
