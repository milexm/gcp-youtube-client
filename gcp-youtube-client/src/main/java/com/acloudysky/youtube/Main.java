/** 
 * LEGAL: Use and Disclaimer. 
 * This software belongs to the owner of the http://www.acloudysky.com site and supports the
 * examples described there. 
 * Unless required by applicable law or agreed to in writing, this software is distributed on 
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. 
 * Please, use the software accordingly and provide the proper acknowledgement to the author.
 * @author milexm@gmail.com  
 **/
package com.acloudysky.youtube;

import com.acloudysky.auth.AuthenticateGoogleServiceClient;

import com.acloudysky.auth.IGoogleServiceClientAuthentication;

import com.acloudysky.utilities.Utility;
import com.acloudysky.ui.UserInterface;

import com.google.api.services.youtube.YouTube;



/**
 * Main class for the google-service-client console application. 
 * @author Michael
 *
 */
public class Main {
	
	
	/***
	 * Application entry point which displays the start greetings and performs the following main tasks:
	 * <ul>
	 *      <li>Gets the authenticated client object authorized to access the Google Youtube service REST API.</li> 
	 *		<li>Reads the default settings.</li>
	 * 		<li>Instantiates the operation classes.</li>
	 * 		<li>Delegates to the SimpleUI class the display of the selection menu and the processing of the user's input.</li>
	 * </ul>
	 * <b>Notes</b>
	 * <ul>
	 * 	<li>This client application assumes that you have a folder named <i>.googleservices</i> in your home directory. In the code this folder
	 *  name is assigned to the <i>parentDir</i> variable.</li>
	 *  <li>The <i>.googleservices</i> folder contains another folder named <i>Youtube</i>. In the code this folder
	 *  name is assigned to the <i>dataDir</i> variable.</li>
	 *  <li>The <i>Youtube</i> folder contains the following files: <i>client_defaults.json</i>, <i>client_secrets.json</i> and 
	 *  <i>StoredCredential</i>. The first file contains the defaults values for the client app; the second file contains crucial  info
	 *  to authenticate this client app to allow the use of the service service REST API. The last file, whose name cannot be changed because defined
	 *  by OAuth2, stores the OAuth2 evaluated credentials so you are not asked to allow access every time you run the client app. If you delete the 
	 *  file you will be asked again to allow access to the service REST API. By the way when you allow access, select the Google ID whose service you
	 *  want allow access to.  
	 * </ul>
	 * @see YoutubeeDefaultSettings#readSettings()  
	 * @see SimpleUI#SimpleUI(YoutubeDefaultSettings)
	 * @param args args[0] = "service"
	 * 
	 */
	public static void main(String[] args) {
		
		// The API client name
		String client = null;
		
		// Set DEBUG flag for testing. 
		Utility.setDEBUG(true);
		
		// Display greeting message.
		UserInterface.displayWelcomeMessage("Google YouTube Service");
		
		// Read input parameters.
		try {
			
			client = args[0];
		}
		catch (Exception e) {
			System.out.println("IO error trying to read application input! Assigning default value.");
			// Assign default values if none are passed.
			if (args.length==0) {
				client = "service";
			}
			else {
				System.out.println("IO error trying to read application input!");
				System.exit(1); 
			}
		}
		
		if (Utility.isDEBUG()) {
			String startGreetings = String.format("Start %s console application", client);
			System.out.println(startGreetings);	
		}
		
		
		
		if (Utility.isDEBUG())
			Utility.getAbsoluteFilePath(".googleservices", "youtube", "client_secrets.json");
		
		// Instantiate the AuthenticateGoogleServiceClient class.
		AuthenticateGoogleServiceClient serviceAuthentication = 
				new AuthenticateGoogleServiceClient(".googleservices", "youtube", "client_secrets.json"); 
		
		// Instantiate the YoutubeDefaultSettings class.
		
		// Instantiate the YoutubeDefaultSettings class.
		YoutubeDefaultSettings clientDefaultSettings = new YoutubeDefaultSettings();
		
		// Read application default values from the JSON file and store them in memory.
		YoutubeDefaultSettings defaultSettings = clientDefaultSettings.readSettings();
				
		// Create an authenticated client which is authorized to use Google service REST API.
		YouTube youtubeServiceClient = null;
		
		try {
			String selectedScope = 
				serviceAuthentication.getScope(IGoogleServiceClientAuthentication.youtubeScopes, defaultSettings.getDefaultScope());
			if (Utility.isDEBUG()) {
				// Display scopes.
				Utility.displayScopes(IGoogleServiceClientAuthentication.youtubeScopes);
				System.out.println("Selected scope: " + selectedScope);	
			}
			
			boolean useStoredCredential = true;
			String defaultScope = defaultSettings.getDefaultScope();
			String currentScope = defaultSettings.getDefaultScope();
			
			if (!defaultScope.equals(currentScope))   
				useStoredCredential = false;
			
			youtubeServiceClient = 
					serviceAuthentication.getAuthenticatedYouTubeClient(selectedScope, useStoredCredential);
			
			// Make current scope equals to the default scope. 
			defaultSettings.updateDefaultSettings("currentscope", defaultSettings.getDefaultScope());
			
			String service = youtubeServiceClient.getApplicationName();
			if (Utility.isDEBUG()) {
				System.out.println(String.format("App name is: %s", service));
			}
		}
		catch (Exception e) {
			System.out.println(String.format("Error %s during Youtube authentication.", e.toString()));
		}
		
		
		if (youtubeServiceClient != null) {
			
			// Instantiate the serviceDefaultSettings class.
			// YoutubeDefaultSettings defaultSettings = new YoutubeDefaultSettings();
			
			// Initializes the classes that contains methods to interact with Youtube service.
			VideoInformation.initVideoInformation(youtubeServiceClient);
			VideoActions.initVideoActions(youtubeServiceClient, serviceAuthentication, defaultSettings);
						
			
			// Instantiate SimpleUI class and display menu.
			SimpleUI sui = new SimpleUI();
			// Start loop to process user's input.
			sui.processUserInput();
		}
		else 
			String.format("Error %s", "Youtube object is null.");
		
		// Display goodbye message.
		UserInterface.displayGoodbyeMessage("Google Youtube Service");	
	}
	
}