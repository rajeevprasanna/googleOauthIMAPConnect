package com.mailDownloader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//Refer : http://www.mkyong.com/maven/how-to-create-a-web-application-project-with-maven/

@Controller
@RequestMapping("/")
public class BaseController {

	/*
	public static final String CLIENT_ID = "516271788433.apps.googleusercontent.com";
	public static final String CLIENT_SECRET = "gNX4n35KqGE2fBBAmUZ70cUT";
	
	
	//private static final String SERVER_URL ="http://ec2-54-200-227-210.us-west-2.compute.amazonaws.com";
	private static final String SERVER_URL ="http://localhost:8080";
	public static final String CALLBACK_URI = SERVER_URL+"/MailDownloader/gauthResponse";

	public static final Iterable<String> SCOPE = Arrays
			.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email;https://mail.google.com/"
					.split(";"));	 
	public static final JsonFactory JSON_FACTORY = new JacksonFactory();
	public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	private String stateToken;
	private final GoogleAuthorizationCodeFlow flow;

	public BaseController() {
		//for setting approval prompt to force to gernerate new refresh tocken, Refer : http://stackoverflow.com/questions/8942340/get-refresh-token-google-api
		flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
				JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).setAccessType("offline").setApprovalPrompt("force").build();
		
		generateStateToken();
	}

	private void generateStateToken() {
		SecureRandom sr1 = new SecureRandom();
		stateToken = "google;" + sr1.nextInt();
	}

	public String getStateToken() {
		return stateToken;
	}
	
	private static final String OUTLOOK_CLIENT_ID = "0000000044111F04";
	private static final String OUTLOOK_CLIENT_SECRET_ID = "qdmcWWbz5zRuGOe7GMWtwIJJuAYvvs48";
	private static final String OUTLOOK_CLIENT_REDIRECT_URL = SERVER_URL + "/MailDownloader/outlookReceiver";
	
	@RequestMapping(value = "/welcomeOutlook", method = RequestMethod.GET)
	public void welcomeOutlook(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Refer : http://www.limilabs.com/blog/oauth2-outlook-com-imap-web-applications
		
		System.out.println("I am trying to connct to outlook mail........................ =>  ");
		
		OAuthService service = new ServiceBuilder()
				.provider(YahooApi.class)
				.apiKey(OUTLOOK_CLIENT_ID)
				.apiSecret(OUTLOOK_CLIENT_SECRET_ID).callback(OUTLOOK_CLIENT_REDIRECT_URL).build();
		
		
		System.out.println("=== Yahoo's OAuth Workflow by Barry===");
		System.out.println();

		// Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		YAHOO_REQUEST_TOKEN = service.getRequestToken();//Setting for member variables to persist across requests. after make it local
		System.out.println("Got the Request Token!");
		System.out.println();
		 
		response.sendRedirect("https://api.login.yahoo.com/oauth/v2/request_auth?oauth_token="+ YAHOO_REQUEST_TOKEN.getToken());
		return;
	}
	
	
	@RequestMapping(value="/connectYahoo")
	public void connectToYahoo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String queryString = request.getQueryString();
		System.out.println("Query string from request is : "+ queryString);
		response.sendRedirect("/MailDownloader/finalPage");
		return;
	}
	
	
	@RequestMapping(value="/finalPage")
	public String finalPage(HttpServletRequest request, HttpServletResponse response){
		return "index";
	}
	
	private static String YAHOO_VERIFIER = "";
	private static String RESPONSE_YAHOO_O_AUTH_TOKEN="";
	private static Token YAHOO_ACCESS_TOKEN = null;
	@RequestMapping(value="/yahooReceiver")
	public String yahooReceiver(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		
		YAHOO_VERIFIER = request.getParameter("oauth_verifier");
		
		RESPONSE_YAHOO_O_AUTH_TOKEN = request.getParameter("oauth_token");
		
		System.out.println("YAHOO_VERIFIER => " + YAHOO_VERIFIER
				+ " RESPONSE_YAHOO_O_AUTH_TOKEN => "
				+ RESPONSE_YAHOO_O_AUTH_TOKEN);
		
		
		//Now get access token
		OAuthService service = new ServiceBuilder()
		.provider(YahooApi.class)
		.apiKey("dj0yJmk9a3lKTW5DNXhSUWdrJmQ9WVdrOVJYVm9USE0zTXpZbWNHbzlNVE15T1RJeU1UZzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD1iNg--")
		.apiSecret("a9d5b489412faa5a4c390bd00107b7bb7ec4dc96").build();
		
		YAHOO_ACCESS_TOKEN = service.getAccessToken(YAHOO_REQUEST_TOKEN, new Verifier(YAHOO_VERIFIER));
		
		System.out.println("(if your curious it looks like this: " + YAHOO_ACCESS_TOKEN + " )");
		
		System.out.println("====================================================================================================================");		
		System.out.println("====================================================================================================================");
		
		System.out.println("YAHOO_ACCESS_TOKEN.getSecret() => "+ YAHOO_ACCESS_TOKEN.getSecret());
		System.out.println("YAHOO_ACCESS_TOKEN.getToken() => "+ YAHOO_ACCESS_TOKEN.getToken());
		System.out.println("YAHOO_ACCESS_TOKEN.getRawResponse()  = > "+YAHOO_ACCESS_TOKEN.getRawResponse());
		
		 
		
		System.out.println("====================================================================================================================");		
		System.out.println("====================================================================================================================");
		
		Map<String, String> tokenMap = splitQuery(YAHOO_ACCESS_TOKEN.getRawResponse());
		//oauth_session_handle is like refresh token to renew access token
		System.out.println("session  => "+ tokenMap.get("oauth_session_handle"));
		
		System.out.println("Now we are going to refresh the token =>");
		//Refer this for refreshing token : http://stackoverflow.com/questions/7415279/refresh-yahoo-oauth-access-token-using-grails-oauth-plugin-based-on-sign-post-ap
		
		//refer:  https://github.com/fernandezpablo85/scribe-java/issues/83
		refreshToken(YAHOO_ACCESS_TOKEN, tokenMap);
		return "index";
	}
	
	 
	//TODO: make sure to use v2 url
	private static final String PROTECT_RESOURCE_V2_URL = "https://api.login.yahoo.com/oauth/v2/get_token?";
	//TODO : Integrate this API as it has code for refresh token
	public void refreshToken(Token accessToken, Map<String, String> tokenMap) {
		System.out.println("====================================================================================================================");		
		System.out.println("====================================================================================================================");
		
		
		
		OAuthRequest request = new OAuthRequest(Verb.GET, "https://api.login.yahoo.com/oauth/v2/get_token");
		request.addOAuthParameter("oauth_session_handle", tokenMap.get("oauth_session_handle"));
				
		OAuthService service = new ServiceBuilder()
		.provider(YahooApi.class)
		.apiKey("dj0yJmk9a3lKTW5DNXhSUWdrJmQ9WVdrOVJYVm9USE0zTXpZbWNHbzlNVE15T1RJeU1UZzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD1iNg--")
		.apiSecret("a9d5b489412faa5a4c390bd00107b7bb7ec4dc96").build();
		service.signRequest(accessToken, request);
		 		 
		Response response = request.send();
		System.out.printf("response code: %1s \n", response.getCode());
		System.out.println(response.getBody());
		
		System.out.println("====================================================================================================================");		
		System.out.println("====================================================================================================================");
		
	}

	
	public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException  {
	    Map<String, String> query_pairs = new HashMap<String, String>();	     
	    String[] pairs = query.split("&");
	    for (String pair : pairs) {
	        int idx = pair.indexOf("=");
	        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
	    }
	    return query_pairs;
	}
	
	private static Token YAHOO_REQUEST_TOKEN= null;
	private static final String PROTECTED_RESOURCE_URL = "http://social.yahooapis.com/v1/user/%1s/contacts?format=json";
	@RequestMapping(value = "/welcomeYahoo", method = RequestMethod.GET)
	public void welcomeYahoo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("I am trying to connct to Yahoo mail........................ =>  ");
		
		OAuthService service = new ServiceBuilder()
				.provider(YahooApi.class)
				.apiKey("dj0yJmk9a3lKTW5DNXhSUWdrJmQ9WVdrOVJYVm9USE0zTXpZbWNHbzlNVE15T1RJeU1UZzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD1iNg--")
				.apiSecret("a9d5b489412faa5a4c390bd00107b7bb7ec4dc96").callback("http://ec2-54-200-227-210.us-west-2.compute.amazonaws.com/MailDownloader/yahooReceiver").build();
		
		
		System.out.println("=== Yahoo's OAuth Workflow by Barry===");
		System.out.println();

		// Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		YAHOO_REQUEST_TOKEN = service.getRequestToken();//Setting for member variables to persist across requests. after make it local
		System.out.println("Got the Request Token!");
		System.out.println();
		 
		response.sendRedirect("https://api.login.yahoo.com/oauth/v2/request_auth?oauth_token="+ YAHOO_REQUEST_TOKEN.getToken());
		return;
	}

	
	
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public void welcome(HttpServletRequest request, HttpServletResponse response) throws IOException {

		System.out.println("I am trying to connct to google mail........................ =>  ");

		final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
		//url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
		url.setRedirectUri("http://localhost:8080/n_2.10-0.1.0/oauth/gmailListener").setState(stateToken).build();
		response.sendRedirect(url.build());
		return;
	}

	@RequestMapping(value = "/gauthResponse", method = RequestMethod.GET)
	public String gauthResponse(HttpServletRequest request, HttpServletResponse response) throws Exception {

		StringBuffer fullUrlBuf = request.getRequestURL();
		if (request.getQueryString() != null) {
			fullUrlBuf.append('?').append(request.getQueryString());
		}
		String authorizedToken = null;
		AuthorizationCodeResponseUrl authResponse = new AuthorizationCodeResponseUrl(fullUrlBuf.toString());
		// check for user-denied error
		if (authResponse.getError() != null) {			
			// authorization denied...
		} else {
			authorizedToken = request.getParameter("code");
  
			Map<String, String> tokens = requestAccessToken(authorizedToken);
			new OAuth2Authenticator().connectIMAP(tokens.get(ACCESS_TOKEN));
			// request access token using authResponse.getCode()...
		}

		System.out.println("Handling the response returned from Google OAuth API........................ =>  ");

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "index";
	}
	
	public static void main(String[] args) throws Exception{
		new OAuth2Authenticator().connectIMAP("");
	}
	
 		
	private static final String ACCESS_TOKEN = "access_token";
	private static final String REFRESH_TOKEN = "refresh_token";
	private static Map<String, String> requestAccessToken(String code) throws IOException {
		Map<String, String> map = new HashMap<String, String>();
		try {
			GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
					new NetHttpTransport(), new JacksonFactory(), CLIENT_ID,
					CLIENT_SECRET, code, CALLBACK_URI).execute();

			System.out.println("Access token: " + response.getAccessToken());
			System.out.println("Refresh token : " + response.getRefreshToken());

			map.put(ACCESS_TOKEN, response.getAccessToken());
			map.put(REFRESH_TOKEN, response.getRefreshToken());

		} catch (TokenResponseException e) {
			if (e.getDetails() != null) {
				System.err.println("Error: " + e.getDetails().getError());
				if (e.getDetails().getErrorDescription() != null) {
					System.err.println(e.getDetails().getErrorDescription());
				}
				if (e.getDetails().getErrorUri() != null) {
					System.err.println(e.getDetails().getErrorUri());
				}
			} else {
				System.err.println(e.getMessage());
			}
		}
		return map;
	}
	

	@RequestMapping(value = "/welcome/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) {

		model.addAttribute("message", "Maven Web Project + Spring 3 MVC - "
				+ name);
		return "index";
*/
	}

