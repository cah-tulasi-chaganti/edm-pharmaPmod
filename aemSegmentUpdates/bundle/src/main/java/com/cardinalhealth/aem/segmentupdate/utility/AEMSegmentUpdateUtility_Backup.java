package com.cardinalhealth.aem.segmentupdate.utility;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.jcr.Node;
import javax.jcr.Session;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.day.cq.dam.api.Asset;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.search.Query;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class AEMSegmentUpdateUtility_Backup {
	private HashMap<String, String> outputfileMap = new HashMap<String, String>();
	private static final Logger LOGGER = LoggerFactory.getLogger(AEMSegmentUpdateUtility_Backup.class);
	
	public void updateOutputfileMap(String messageKey, String message){
		String tempMsgStr = this.outputfileMap.get(messageKey);
		if(tempMsgStr == null){
			this.outputfileMap.put(messageKey, message+Constants.newLine);
		}else{
			this.outputfileMap.put(messageKey, tempMsgStr+message+Constants.newLine);
		}
	}
	
	private static void logMessage(String methodName, String message) {
		LOGGER.info(methodName + " - " + message);
	}

	private void logErrorMessage(String methodName, String message) {
		LOGGER.error(methodName + " - " , message);
	}
		/**
	 * Create a query and read files from DAM.
	 * 
	 * @param absPath
	 * @param session
	 * @param resourceResolver
	 * @return
	 * @throws Exception
	 */
	public static Map<String, InputStream> retrieveContentFromCRXRepository(String absPath, Session session,
			ResourceResolver resourceResolver, QueryBuilder builder, AEMSegmentUpdateOutputFileFormat outlog) throws Exception {
		final String methodName = "retrieveContentFromCRXRepository";
		logMessage(methodName, " Start.");
		logMessage(methodName, " absPath:"+ absPath);

		InputStream data = null;
		Map<String, InputStream> dataMap = null;
		String currFileName = "";
		try {

			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "#Input Folder - CSV file status update as follows:");
			// create query description as hash map
			Map<String, String> map = new HashMap<String, String>();
			map.put("type", "dam:Asset");
			map.put("path", absPath); // "/content/dam/Search/Input"
			builder = resourceResolver.adaptTo(QueryBuilder.class);

			// INvoke the Search query
			Query query = builder.createQuery(PredicateGroup.create(map), session);
			logMessage(methodName, " query:"+ query.toString());
			SearchResult sr = query.getResult();
			logMessage(methodName, " sr qurty statement:"+ sr.getQueryStatement());
			logMessage(methodName, " sr qurty statement:"+ sr.toString());

			logMessage(methodName,"Search Results: " + sr.getTotalMatches());

			// Create a MAP to store results
			dataMap = new HashMap<String, InputStream>();
			
			Map<String, String> csvFilePathMap = new HashMap<String, String>();
			Map<String, String> searchdeleteFilePathMap = new HashMap<String, String>();
			Map<String, String> outputBufFileMap = new TreeMap<String, String>();
			// iterating over the results
			for (Hit hit : sr.getHits()) {
				String path = hit.getPath();
				Resource rs = resourceResolver.getResource(path);
				Asset asset = rs.adaptTo(Asset.class);
				
				// We have the File Name and the inputstream
				data = asset.getOriginal().getStream();
				logMessage(methodName,"after the query data: " + data);
				currFileName = asset.getName();
				logMessage(methodName, " currFileName:"+ currFileName);
				
				// Add to map: key is fileName and value is inputStream
				dataMap.put(currFileName, data);
			}

			if (dataMap.size() > 0) {
				logMessage(methodName, " successfully read files from CRXRepository. dataMap:" + dataMap);
			} else {
				logMessage(methodName, " Business Error: File does not exist in Input folder.");
				outlog.updateOutputfileMap(Constants.csvRetrievelMsg,
						"Business Error: File does not exist in Input folder.");
			}
			
		} catch (Exception ex) {
			logMessage(methodName, " Exception while reading a file from CRXRepository:" + ex.getMessage());
			outlog.updateOutputfileMap(Constants.csvRetrievelMsg,
					currFileName + " - Exception while reading the file.");
			ex.printStackTrace();
			throw ex;
		} finally {
			if (data != null)
				data.close();
		}

		return dataMap;
	}
	
	
	
	
	
	/**
	 * this method process CSV file and return segment and content map from PCI
	 * output file.
	 * 
	 * @param dataMap
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String>  getFileDetailsFromInputStrem(Map<String, InputStream> dataMap,
			AEMSegmentUpdateOutputFileFormat outlog) throws Exception {
		final String methodName = "getFileDetailsFromInputStrem";
		logMessage(methodName, " Start.");
		logMessage(methodName, " dataMap:" + dataMap);
		
		BufferedReader csvBuffReader = null;
		Map<String, String>  fileDetailsMap = new HashMap<String, String>();
		Map<String, Map<String, String>> fileSegmentDetailsMap = new HashMap<String, Map<String, String>>();
		
		try {
			for (Map.Entry<String, InputStream> dataMapEntry : dataMap.entrySet()) {
				String csvFileName = dataMapEntry.getKey();
				logMessage(methodName, " csvFileName:" + csvFileName);
				//if (csvFileName.startsWith(AEMConstant.SEARCH_SEGMENT_OUTPUT_FILEPATTERN)) {
					logMessage(methodName, "inside if csvFileName:" + csvFileName);
					csvBuffReader = new BufferedReader(new InputStreamReader(dataMapEntry.getValue()));
					int csvLineCount = 1;
					String line = null;
					String csvRetrievelMsg = csvFileName + " - Read Successfully.";
					//logMessage(methodName, " csvBuffReader.readLine():" + csvBuffReader.readLine() +"length is:"+csvBuffReader.readLine().length());
					while ((line = csvBuffReader.readLine()) != null) {
						logMessage(methodName, " CSV line " + csvLineCount + " - " + line);
						String[] tempStrArr = line.split(",");
						fileDetailsMap.put(tempStrArr[2] + "_" + tempStrArr[0], tempStrArr[2] + "_" + tempStrArr[1]);
						csvLineCount++;
					}
					//outlog.updateOutputfileMap(AEMConstant.csvRetrievelMsg, csvRetrievelMsg);
					logMessage(methodName, "Files Read successfully and set in map::csvFileName-" + csvFileName);
				//}// if loop for the cvs pattern
			}
			
			//fileDetailsMap.put(Constants.searchSegmentDetails, fileSegmentDetailsMap);

		} catch (Exception ex) {
			LOGGER.info(methodName, " Exception while converting inputstream in map:" + ex.getMessage());
			ex.printStackTrace();
		} finally {
			if (csvBuffReader != null)
				csvBuffReader.close();
		}
		// logMessage(methodName,"ALL CSV file/s successfully read:
		// fileDetailsMap" + fileDetailsMap);
		logMessage(methodName, "ALL CSV file/s successfully read. Segment Count:" + fileSegmentDetailsMap.size()
				+ "  , Content Count:" + fileDetailsMap.size());
		return fileDetailsMap;
	}
	
	
	/**
	 * read value from map and returns respective value;
	 * @param props
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getConfigurationProperty(Map<String, Object> props, String key, String defaultValue) {
		String returnValue = defaultValue;
		Object sysuserobj = props.get(key);
		if (sysuserobj != null && sysuserobj.toString().length() > 0) {
			returnValue = sysuserobj.toString();
		}
		return returnValue;
	}
	
	
	/**
	 * This method save a JCR session.
	 * 
	 * @param resourceResolver
	 * @param session
	 * @throws RepositoryException
	 */
	public static void saveSession(ResourceResolver resourceResolver, Session session) throws Exception {
		final String methodName = "saveSession";
		// resourceResolver.commit();
		try {
			session.save();
			logMessage(methodName, " - Session saved successfully.");
		} catch (Exception e) {
			session.refresh(false);
			LOGGER.info(methodName, " Exception while saving session" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * create CSV file
	 * 
	 * @param resourceResolver
	 * @param dataMap
	 */
	public static void createOutputFiles(ResourceResolver resourceResolver, long processingTime,
			String outputFolPath, AEMSegmentUpdateOutputFileFormat outlog) {

		final String methodName = "createOutputFiles";
		logMessage(methodName, " Start.");
		try {

			StringBuilder outputStrBuilder = outlog.getCsvRetrievalMsgForOutputFile();
			outputStrBuilder.append( Constants.newLine).append("#CSV file processing completed.");
			outputStrBuilder.append( Constants.newLine).append( "#Total processing time (in ms): - " + processingTime);
			
			logMessage(methodName, "outputStr:" + String.valueOf(outputStrBuilder));
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.outFileDateFormat);
			Date dt = new Date();
			String outputFileName = "output_" + formatter.format(dt) + ".txt";

			InputStream is = new ByteArrayInputStream(String.valueOf(outputStrBuilder).getBytes());
			com.day.cq.dam.api.AssetManager cqAssetManager = resourceResolver
					.adaptTo(com.day.cq.dam.api.AssetManager.class);
			String assetPath = outputFolPath + "/" + outputFileName;
			cqAssetManager.createAsset(assetPath, is, Constants.outputFileFormat, true);
			logMessage(methodName, " Successfully created output file. File Name- " + outputFileName);
		} catch (Exception ex) {
			LOGGER.info(methodName, " Exception while creating output file:" + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
    public static  HashMap<Integer, String>  allSegmentsforDCAccount(ResourceResolver resourceResolver, String inputPath, AEMSegmentUpdateOutputFileFormat outlog, String traitType) {
    	HashMap<Integer, String> segmentsMap = new HashMap<Integer, String>();
    	try {
			
			Resource segMentResource = resourceResolver.getResource(inputPath);
			Iterator<Resource> iterator = segMentResource.listChildren();
			int segCount =0;
			while (iterator.hasNext()) {
				Resource segMentResourceItem = iterator.next(); 
				String segPath = segMentResourceItem.getPath();
				if(segPath.indexOf("jcr:content")>0) {
					segPath = segPath.replace("/jcr:content", "");
				}
				String dcAccountNodePath = segPath + "/jcr:content/traits/andpar/"+traitType;				
				String dcAccountNodePath1 = segPath + "/jcr:content/traits/andpar/dcaccount";				
				
				Resource nodeResource = resourceResolver.getResource(dcAccountNodePath);
				Node myNode = null;		
							
				
				Resource nodeResource1 = resourceResolver.getResource(dcAccountNodePath1);
				Node myNode1 = null;
				if(nodeResource != null) {
					myNode = nodeResource.adaptTo(Node.class);
					if(myNode != null && myNode.hasProperty(traitType) && myNode.getProperty(traitType)!=null) {						
						segmentsMap.put(segCount, myNode.getPath());						
						segCount++;
					}
				} if(nodeResource1 != null) {
					myNode1 = nodeResource1.adaptTo(Node.class);
					if(myNode1 != null && myNode1.hasProperty(traitType) && myNode1.getProperty(traitType)!=null) {						
						segmentsMap.put(segCount, myNode1.getPath());						
						segCount++;
					}
				}
			}
			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "Total Nodes are:"+segCount);
		} catch (Exception e) {			
			LOGGER.info("Exception is due to:"+e.getMessage());
			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "Exception while fetching segments due to:"+e.getMessage());
		}
		
		return segmentsMap;
	}
    public static  HashMap<Integer, String>  allSegmentsforKeyword(ResourceResolver resourceResolver, String inputPath, AEMSegmentUpdateOutputFileFormat outlog, String traitType) {
    	HashMap<Integer, String> segmentsMap = new HashMap<Integer, String>();
    	try {
			
			Resource segMentResource = resourceResolver.getResource(inputPath);
			Iterator<Resource> iterator = segMentResource.listChildren();
			int segCount =0;
			while (iterator.hasNext()) {
				Resource segMentResourceItem = iterator.next(); 
				String segPath = segMentResourceItem.getPath();
				if(segPath.indexOf("jcr:content")>0) {
					segPath = segPath.replace("/jcr:content", "");
				}
				String dcAccountNodePath = segPath + "/jcr:content/traits/andpar/"+traitType;				
							
				
				Resource nodeResource = resourceResolver.getResource(dcAccountNodePath);
				Node myNode = null;				
				
				if(nodeResource != null) {
					myNode = nodeResource.adaptTo(Node.class);
					if(myNode != null && myNode.hasProperty(traitType) && myNode.getProperty(traitType)!=null) {						
						segmentsMap.put(segCount, myNode.getPath());						
						segCount++;
					}
				}
			}
			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "Total Nodes are:"+segCount);
		} catch (Exception e) {			
			LOGGER.info("Exception is due to:"+e.getMessage());
			outlog.updateOutputfileMap(Constants.csvRetrievelMsg, "Exception while fetching segments due to:"+e.getMessage());
		}
		
		return segmentsMap;
	}


}//Utility Class ends
