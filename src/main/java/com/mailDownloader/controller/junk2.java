package com.mailDownloader.controller;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

public class junk2 {
	private static String url = "https://apis.live.net/v5.0/me?access_token=EwBAAq1DBAAUGCCXc8wU%2FzFu9QnLdZXy%2BYnElFkAARXUYyfVfvAn0m3wK8wRVSmUmgSOhXfxOc6UZv86bJ9bJ6qOeSLvHqm%2BxglUlSasuEqJ707a1tceAwZg31OUTVyAHpo2bcvM5zVyTxfkVBNzL5YpEUl%2FDolovPFz9y8grWO0%2FieyCP4Y4juinW%2BMytI5aZZVwqJ%2BxiCNvabd96v98nYxNYt7W8HjpwrkukFb7BX0eFWd3qR8B7HjbZsPBUKXWSyWUep5C76OqjbgJ55lovGL8T6jxR3o4XKU3XO%2FH4fbeBHir08Vu2%2FfFmLRfZaAyItA85yjCboMr3saJx%2Ft1qsNuqYmPpTSWFle4ONEzVBQqyXIiX5hZgZC1Qdw%2FnUDZgAACJ7lVcdofE6oEAGPmyMO42mmP3On%2Bo%2Ftw0tC1NM2IJcb%2BP5HztDJ1RlvPWa42pNTXgQ7VndOZgWY12%2FOSAOPT77SV%2BfZemDhp09ncz%2BTf1KyJo01UKwYsZLTGQLxtUksrhMgbnMYGdJpLmLV7z3CtaPSo73wiD5C1NyWcf4NVMBlyfCJNuilF9T5ed5S7tbUPrYYmhFPV8DfagfDSm3oB23o9lEiDDiG1lz1mVJEwjHD4ukjqJcLtx7OP2mR94rQXaUUazbdh7rZrYd4%2BLY04oWvcvtzA92JH2GAICG28N1e%2FexMDbBrBFWD3SA4hXtgz2JdYB9O9hjOdpo3xHaQ6261YmlmSGLB3tinQvxzkICtSmSkMIWCpQ38DAAA";

	  public static void main(String[] args) {	     
	    HttpClient client = new HttpClient();	     
	    GetMethod method = new GetMethod(url);
	    	     
	    try {	       
	      int statusCode = client.executeMethod(method);

	      if (statusCode != HttpStatus.SC_OK) {
	        System.err.println("Method failed: " + method.getStatusLine());
	      }
	     
	      System.out.println(method.getResponseBodyAsString());
	    } catch (HttpException e) {
	      System.err.println("Fatal protocol violation: " + e.getMessage());
	      e.printStackTrace();
	    } catch (IOException e) {
	      System.err.println("Fatal transport error: " + e.getMessage());
	      e.printStackTrace();
	    } finally {	       
	      method.releaseConnection();
	    }  
	  }
}
