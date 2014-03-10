package com.mailDownloader.controller;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.YahooApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;


//Refer : http://agilesc.barryku.com/?p=175
public class YahooExample {
	private static final String PROTECTED_RESOURCE_URL = "http://social.yahooapis.com/v1/user/%1s/listMessages?format=json";
	private static final String PROTECTED_RESOURCE_URL_MAIL = "http://mail.yahooapis.com/ya/download";

	public static void main(String[] args) {
		OAuthService service = new ServiceBuilder()
				.provider(YahooApi.class)
				.apiKey("dj0yJmk9a3lKTW5DNXhSUWdrJmQ9WVdrOVJYVm9USE0zTXpZbWNHbzlNVE15T1RJeU1UZzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD1iNg--")
				.apiSecret("a9d5b489412faa5a4c390bd00107b7bb7ec4dc96").build();
		Scanner in = new Scanner(System.in);

		System.out.println("=== Yahoo's OAuth Workflow by Barry===");
		System.out.println();

		// Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		Token requestToken = service.getRequestToken();
		System.out.println("Got the Request Token!");
		System.out.println();

		System.out
				.println("Now open your browser to the following URL, and grant access when prompted."
						+ "You will get a response page with a verification code since there's no callback specified. "
						+ "Copy the verification code.");
		System.out.println(service.getAuthorizationUrl(requestToken));
		System.out.println("Paste the verification code here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();

		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: "
				+ accessToken + " )");
		System.out.println();

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, String.format(
				PROTECTED_RESOURCE_URL,
				getYahooGuid(accessToken.getRawResponse())));
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.printf("response code: %1s \n", response.getCode());
		System.out.println(response.getBody());

		System.out.println();
		System.out
				.println("Thats it man! Go and build something awesome with Scribe!");

	}

	private static final String YAHOO_GUID = "xoauth_yahoo_guid";
	private static final int GUID_LENGTH = 26;

	private static String getYahooGuid(String response) {
		String yahoo_guid = null;
		int yahoo_guid_location = response.indexOf(YAHOO_GUID);
		if (yahoo_guid_location > 0) {
			yahoo_guid = response
					.substring(yahoo_guid_location + YAHOO_GUID.length() + 1,
							yahoo_guid_location + YAHOO_GUID.length()
									+ GUID_LENGTH + 1);
		}
		return yahoo_guid;
	}
}
