package com.cardinalhealth.aem.segmentupdate.utility;

import java.util.HashMap;



/**
 * This file is used to format output file.
 * @author alok.agrwal
 *
 */
public class AEMSegmentUpdateOutputFileFormat {
	private HashMap<String, String> outputfileMap = new HashMap<String, String>();
	
	/**
	 * This method is to update map
	 * @param messageKey
	 * @param message
	 */
	public void updateOutputfileMap(String messageKey, String message){
		String tempMsgStr = this.outputfileMap.get(messageKey);
		if(tempMsgStr == null){
			this.outputfileMap.put(messageKey, message+Constants.newLine);
		}else{
			this.outputfileMap.put(messageKey, tempMsgStr+message+Constants.newLine);
		}
	}
	
	/**
	 * This Method is to generate csv retrieval message
	 * @return
	 */
	public StringBuilder getCsvRetrievalMsgForOutputFile(){
		//final String methodName = "getCsvRetrievelMsgForOutputFile";
		StringBuilder csvRetrievalstr = new StringBuilder();
		if(this.outputfileMap != null && this.outputfileMap.get(Constants.csvRetrievelMsg) != null){
			csvRetrievalstr.append(this.outputfileMap.get(Constants.csvRetrievelMsg));
		}
		//logMessage(methodName, " csvRetrievelMsg:" + strBilder.toString());
		return csvRetrievalstr;
	}
	
	
	
	
	
	/**
	 * This Method is to generate a output file content for delete file
	 * @return
	 */
	public StringBuilder getOutputFileInfoforOutputFile(){
		//final String methodName = "getOutputFileInfoforOutputFile";
		StringBuilder strBilder = new StringBuilder();
		//logMessage(methodName, "this.outputfileMap:" + this.outputfileMap);
		
		String outSegCreateMsg =this.outputfileMap.get(Constants.outSegCreateMsg);
		String outSegUpdateMsg =this.outputfileMap.get(Constants.outSegUpdateMsg);
		String outSegExceptionMsg =this.outputfileMap.get(Constants.outSegExceptionMsg);
		if(outSegCreateMsg != null || outSegUpdateMsg != null || outSegExceptionMsg != null){
			strBilder.append(Constants.newLine).append(Constants.newLine);
			strBilder.append("#PCI Output File - Segments processing details are as follows:").append(Constants.newLine);
			if(outSegCreateMsg != null){
				strBilder.append("Segment Created Successfully:").append(Constants.newLine);
				strBilder.append(outSegCreateMsg);
			}
			if(outSegUpdateMsg != null){
				strBilder.append("Segment Updated Successfully:").append(Constants.newLine);
				strBilder.append(outSegUpdateMsg);
			}
			
			if(outSegExceptionMsg != null){
				strBilder.append("Segment Error:").append(Constants.newLine);
				strBilder.append(outSegExceptionMsg);
			}
		}
		
		String outConAssociateMsg =this.outputfileMap.get(Constants.outConAssociateMsg);
		String outConNotFountMsg =this.outputfileMap.get(Constants.outConNotFountMsg);
		String outConExceptionMsg =this.outputfileMap.get(Constants.outConExceptionMsg);
		
		if(outConAssociateMsg != null || outConNotFountMsg != null || outConExceptionMsg != null){
			strBilder.append(Constants.newLine).append(Constants.newLine);
			strBilder.append("#PCI Output File - Content processing details are as follows:").append(Constants.newLine);
			if(outConAssociateMsg != null){
				strBilder.append("Content Updated Successfully:").append(Constants.newLine); 
				strBilder.append(outConAssociateMsg);
			}
			if(outConNotFountMsg != null){
				strBilder.append("Content Not Found:").append(Constants.newLine);
				strBilder.append(outConNotFountMsg);
			}
			
			if(outConExceptionMsg != null){
				strBilder.append("Content Error:").append(Constants.newLine);
				strBilder.append(outConExceptionMsg);
			}
		}
		//logMessage(methodName, "delFileInfo:" + delFileInfo);
		
		return strBilder;
		
	}
	
	
	/**
	 * This Method is to generate a output file content for delete file
	 * @return
	 */
	public StringBuilder getConActivateInfoOutputFile(){
		StringBuilder strBilder = new StringBuilder();
		if(this.outputfileMap != null && this.outputfileMap.size() > 0){
			
			strBilder.append(Constants.newLine).append(Constants.newLine);
			strBilder.append("Content activation processing details are as follows:").append(Constants.newLine);
			if(this.outputfileMap.get(Constants.conActivateSuccesstMsg) != null){
				strBilder.append("Content Activated Successfully:").append(Constants.newLine);
				strBilder.append(this.outputfileMap.get(Constants.conActivateSuccesstMsg));
			}
			if(this.outputfileMap.get(Constants.conActivateExceptionMsg) != null){
				strBilder.append("Content Not Activated:").append(Constants.newLine);
				strBilder.append(this.outputfileMap.get(Constants.conActivateExceptionMsg));
			}
			
		}
		return strBilder;
		
	}
}
