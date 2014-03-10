package com.mailDownloader.controller;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;

public class IMAPMailConnector {

	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private static final String GMAIL_SCOPE = "https://mail.google.com/";

	public static void main(String[] args) throws GeneralSecurityException, IOException {
		new IMAPMailConnector().getAccessToken();
	}

	public String getAccessToken() throws GeneralSecurityException, IOException {
		GoogleCredential credential = new GoogleCredential.Builder()
				.setTransport(HTTP_TRANSPORT)
				.setJsonFactory(JSON_FACTORY)
				.setServiceAccountId("technomania.rajeev@gmail.com")
				.setServiceAccountId(BaseController.CLIENT_ID)
				.setServiceAccountScopes(GMAIL_SCOPE)
				.setServiceAccountPrivateKeyFromP12File(
						new File("/Users/rajeevprasanna/Desktop/scalatraTest/MailDownloader_SpringMaven/src/main/java/com/mailDownloader/d3f38ade8aa99aee0a08d3493b1ce16b5a0c40d7-privatekey.p12"))
				.build();
		credential.refreshToken();

		System.out.println("access token from IMAP Mail connector class => " + credential.getAccessToken());
		System.out.println("refresh token from IMAP Mail connector class => " + credential.getRefreshToken());

		return credential.getAccessToken();
	}
}
