package com.cardinalhealth.aem.segmentupdate.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * This class is used to make Query Builder API Calls using Rest frame work.
 * This will be used for AEM Extract process.
 * @author ankur.singh03
 *
 */
public class QueryBuilderRestUtility {
	private static final Logger LOGGER = LoggerFactory.getLogger(CSVReader.class);;
	private static final String COOKIE_NAME = "login-token";
	private static  String PROTOCOL = "http";
	private static  String HOST = "localhost";
	private static  String USER_NAME = "oeextractuser";
	private static  String PASSWD = "Cardinal1";
	private static  String PORT = "";

	/**
	 * executeExtractQuery- This method will form the URL and make REST call to Query Builder REST API.
	 * @param items
	 * @param extractPath
	 * @return String
	 * @throws IOException  
	 */
	public static String executeExtractQuery(final String extractPath,
			final String port,
			final String dirLocation,
			final String userId,
			final String psswrd,
			final String nodeDepth,
			final String nodeLimit,
			final String queryTimeOut,
			final String protocol,
			final String hostName,
			final String sortingParam) throws IOException {
		final String methodName = "executeExtractQuery";
		LOGGER.info(methodName);
		String finalOutPut= "";

		if(port != null) {
			QueryBuilderRestUtility.PORT =  port;
		}
		if(userId != null) {
			QueryBuilderRestUtility.USER_NAME =  userId;
		}
		if(psswrd != null) {
			QueryBuilderRestUtility.PASSWD =  psswrd;
		}
		if(protocol != null) {
			QueryBuilderRestUtility.PROTOCOL =  protocol;
		}
		if(hostName != null) {
			QueryBuilderRestUtility.HOST =  hostName;
		}

		LOGGER.info(methodName + "Port="+QueryBuilderRestUtility.PORT);		
		URL url = new URL(PROTOCOL+"://"+HOST+":"+port+"/bin/querybuilder.json?p.limit="+
				nodeLimit+"&path="+extractPath+"&type=cq:PageContent&p.hits=full&orderby="+sortingParam+"&p.nodedepth="+nodeDepth);
		LOGGER.info(methodName + "URL="+url.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");
		if(null!=queryTimeOut && !queryTimeOut.isEmpty()) {
			conn.setReadTimeout(Integer.valueOf(queryTimeOut)); //this is configurable value- from AEM Config manager
			conn.setConnectTimeout(Integer.valueOf(queryTimeOut)); //this is configurable value- from AEM Config manager
		}
		conn.setRequestProperty("j_username", QueryBuilderRestUtility.USER_NAME);
		conn.setRequestProperty("j_password", QueryBuilderRestUtility.PASSWD);
		HttpClient client = new HttpClient();
		String loginToken = getToken(client,QueryBuilderRestUtility.USER_NAME,QueryBuilderRestUtility.PASSWD,QueryBuilderRestUtility.PORT);
		if(null == loginToken || loginToken.isEmpty()) {
			LOGGER.error(methodName , "Token Service failed during login to AEM.");
		}
		conn.setRequestProperty("Cookie", loginToken); 
		LOGGER.info(methodName + "Going to hit GET Call for Query builder REST API.");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output;
			LOGGER.info(methodName + "Response from server is recieved successfully.");
			while ((output = br.readLine()) != null) {
				finalOutPut = finalOutPut + output;
			}
		}else {
			LOGGER.error(methodName , "Connection Failed with exception code = " + conn.getResponseCode());
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}
		conn.disconnect();
		LOGGER.info(methodName);
		return finalOutPut;
	}

	/**
	 * getToken- This method will call AEM token generation API and will create login token to pass for Query builder REST API
	 * @param client
	 * @param userId
	 * @param psswrd
	 * @param port
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	private static String getToken(HttpClient client, String userId, String psswrd, String port) throws IOException,
	HttpException {
		final String methodName = "getToken";
		LOGGER.info(methodName);
		String token = null;
		PostMethod authRequest = new PostMethod(String.format("%s://%s:%s/j_security_check", PROTOCOL, HOST, QueryBuilderRestUtility.PORT));
		authRequest.setParameter("j_username", QueryBuilderRestUtility.USER_NAME);
		authRequest.setParameter("j_password", QueryBuilderRestUtility.PASSWD);
		authRequest.setParameter("j_validate", "true");

		int status = client.executeMethod(authRequest);
		if (status == 200) {
			Header[] headers = authRequest.getResponseHeaders("Set-Cookie");
			for (Header header : headers) {
				String value = header.getValue();
				if (value.startsWith(COOKIE_NAME + "=")) {
					int endIdx = value.indexOf(';');
					if (endIdx > 0) {
						token = value.substring(COOKIE_NAME.length() + 1, endIdx);
					}
				}
			}
		} else {
			LOGGER.error(methodName, "Unexcepted response code " + status + "; msg: " + authRequest.getResponseBodyAsString());
		}
		LOGGER.error("login-token"+"login-token="+token);
		LOGGER.info(methodName);
		return "login-token="+token;
	}

}
