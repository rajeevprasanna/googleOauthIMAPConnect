package com.mailDownloader.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
 
public class Junk {
   
  public static void main(String[] args) {
	  postResponse();
	}
  
  private static final String YAHOO_API_CONSUMER_KEY = "d34j0yJmk9a3lKTW5DNXhSUWdrJmQ9WVdrOVJYVm9USE0zTXpZbWNHbzlNVE15T1RJeU1UZzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD1iNg--";
  private  static final String YAHOO_API_CONSUMER_SECRET = "a9d235b489412faa5a4c390bd00107b7bb7ec4dc96&";
  private  static final String YAHOO_REDIRECT_URL = "http://ec2-54-2-227-210.us-west-2.compute.amazonaws.com/";

  
  private static void postResponse() {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("https://api.login.yahoo.com/oauth/v2/get_token");
		
		long timeStamp = new Date().getTime()/1000;		 
		long nonce = System.currentTimeMillis() + new Random().nextInt();

		method.addParameter("oauth_signature_method", "plaintext");
		method.addParameter("oauth_signature", YAHOO_API_CONSUMER_SECRET+"1ae5f0bdcc39df4c297bef1a5cff76d6189edece");
		method.addParameter("oauth_consumer_key", YAHOO_API_CONSUMER_KEY);
		method.addParameter("oauth_version", "1.0");
		method.addParameter("oauth_timestamp", String.valueOf(timeStamp));
		method.addParameter("oauth_nonce",  String.valueOf(nonce));
		method.addParameter("oauth_token", "qcefkbu");
		method.addParameter("oauth_verifier", "v9fxsa");
		 		 
		String responseBody = "";

		try {
			int returnCode = client.executeMethod(method);
			if (returnCode == HttpStatus.SC_OK)
				responseBody = method.getResponseBodyAsString();
			System.out.println("return code => " + returnCode);
		} catch (Exception e) {
			System.out
					.println("Exception occurred while making get request to live "
							+ e.getStackTrace());
		} finally {
			method.releaseConnection();
		}
		System.out.println(responseBody);
	}
  
  
	private static void getResponse() {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod("https://api.login.yahoo.com/oauth/v2/get_request_token");

		//String oauth_signature = YAHOO_API_CONSUMER_SECRET";
		long timeStamp = new Date().getTime()/1000;		 
		long nonce = System.currentTimeMillis() + new Random().nextInt();

		NameValuePair[] array = new NameValuePair[7];
		array[0] = new NameValuePair("oauth_signature_method", "plaintext");
		array[1] = new NameValuePair("oauth_signature", YAHOO_API_CONSUMER_SECRET);
		array[2] = new NameValuePair("oauth_consumer_key",
				YAHOO_API_CONSUMER_KEY);
		array[3] = new NameValuePair("oauth_version", "1.0");
		array[4] = new NameValuePair("oauth_callback", YAHOO_REDIRECT_URL);
		array[5] = new NameValuePair("oauth_timestamp", timeStamp + "");
		array[6] = new NameValuePair("oauth_nonce", nonce + "");
		
		/*System.out.println("start=============>");
		for(NameValuePair n : array){
			System.out.println(n.getName() + "   "+ n.getValue());
		}
		System.out.println("end=============>");*/

		method.setQueryString(array);
		String responseBody = "";

		try {
			int returnCode = client.executeMethod(method);
			if (returnCode == HttpStatus.SC_OK)
				responseBody = method.getResponseBodyAsString();
			System.out.println("return code => " + returnCode);
		} catch (Exception e) {
			System.out
					.println("Exception occurred while making get request to live "
							+ e.getStackTrace());
		} finally {
			method.releaseConnection();
		}
		System.out.println(responseBody);
	}
  
  private static void getTest1(){
	  HttpClient client = new HttpClient(); 
	    PostMethod method = new PostMethod("https://login.live.com/oauth20_token.srf");
		method.addParameter("client_id", "0000000044111F04");
		method.addParameter("redirect_uri", "http://ec2-54-200-227-210.us-west-2.compute.amazonaws.com/nstar_2.10-0.1.0/oauth/live/receiver");
		method.addParameter("client_secret", "qdmcWWbz5zRuGOe7GMWtwIJJuAYvvs48");
		method.addParameter("grant_type", "authorization_code");
		method.addParameter("code", "7d4edfd5-9ecf-0432-a459-ff3c7dd72ca1");
		String responseBody = null;
	    try{
	      int returnCode = client.executeMethod(method);
	      if(returnCode == HttpStatus.SC_OK)
	      responseBody = method.getResponseBodyAsString();	       
	    } catch (Exception e) {
	      System.err.println(e);
	    } finally {
	      method.releaseConnection();	       
	    }
	    
	    Map<String, String> tokens= splitQuery(responseBody);
	    for(String key : tokens.keySet()){
	    	System.out.println(key + "  => "+ tokens.get(key));
	    }
  }
   
  private static Map<String, String> splitQuery(String query) {
		if(query == null || query.isEmpty()){
			System.out.println("query log is empty");
			return Collections.EMPTY_MAP;
		}
		
	    Map<String, String> query_pairs = new HashMap<String, String>();	     
	    String[] pairs = query.trim().split(",");
	    for (String pair : pairs) {
	        int idx = pair.indexOf(":");
	        try {
	        	String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
	        	String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
				query_pairs.put(trimDoubleQuotes(key), trimDoubleQuotes(value));
			} catch (UnsupportedEncodingException e) {
				System.out.println(e.getStackTrace());				
			}
	    }
	    return query_pairs;
	}
  
  	private static String trimDoubleQuotes(String input){
  		if(input != null && !input.isEmpty()){
  			return input.replaceAll("^\"|\"$", "").trim();
  		}else{
  			return "";
  		}
  	}
}
