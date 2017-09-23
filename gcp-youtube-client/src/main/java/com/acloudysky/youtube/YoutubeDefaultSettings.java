/** 
 * LEGAL: Use and Disclaimer. 
 * This software belongs to the owner of the http://www.acloudysky.com site and supports the
 * examples described there. 
 * Unless required by applicable law or agreed to in writing, this software is distributed on 
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * Please, use the software accordingly and provide the proper acknowledgement to the author.
 * @author milexm@gmail.com  
 **/

package com.acloudysky.youtube;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import com.acloudysky.utilities.Utility;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;


/*** 
 * Reads and writes the service client default settings from the related JSON file.
 * The file contains information such as project ID, default file names and so on.
 * It extends <a href="https://developers.google.com/api-client-library/java/google-http-java-client/reference/1.20.0/com/google/api/client/json/GenericJson" target="_blank">GenericJson class</a>
 * whose Subclasses can declare fields for known data keys using the Key annotation. 
 * Each field can be of any visibility (private, package private, protected, or public) and must not be static. 
 * The following is the JSON formatted information:
 * <pre>
 * "defaultemail" : "your e-mail",
 * "defaultprefix" : "your alias",
 * "defaultproject" : "your project ID",
 * "currentscope" : "default scope to use",
 * "defauldomain" : "your domain name"
 * </pre>
 * @author Michael
 * * <b>Notes</b>
 * <ul>
 * 	<li>The currentscope will be updated by the application in function of the Youtube operation to perform.</li>
 *  <li>If the scope requested by the operation is different from the currentscope, the currentscope will be updated with the requested one,
 *  and the StoredCredential file is deleted form the local computer location. </li>
 *  <li>An access permission request is issued again and a new StoredCredential file is created containing the new issued credentials.</li>
 * </ul>
 */
public class YoutubeDefaultSettings extends GenericJson {
	
	// Google services local directories.
	final static String SERVICESDIR = ".googleservices";
	final static String DATADIR = "youtube";
	// Client default settings file name. 
	final static String DEFAULTSFILE = "client_defaults.json";
		
	// Common defaults.
	@Key("defaultproject")
	private String project;
	
	@Key("defaultprefix")
	private String prefix;

	@Key("defaultemail")
	private String email;

	@Key("defaultdomain")
	private String domain;


	@Key("currentscope")
	private String currentscope;

	@Key("defaultscope")
	private String defaultscope;
	
	// Setters and getters. 
	public String getProject() {
		return project;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public String getEmail() {
		return email;
	}

	public String getDomain() {
		return domain;
	}
	
	public String getDefaultScope() {
		return defaultscope;
	}

	public void setDefaultScope(String newscope) {
		defaultscope = newscope;
	}
	
	public String getCurrentScope() {
		return currentscope;
	}

	public void setCurrentScope(String newscope) {
		currentscope = newscope;
	}

	/***
	 * Reads sample settings contained in the supporting <i>client_defaults.json</i> file.
	 * 	<b>Note</b>. This method uses {@link com.google.api.client.json.JsonFactory} to create
	 * a DriveDefaultSettings object to read the JSON formatted information.
	 * @return The DriveDefaultSettings object
	 */
	public YoutubeDefaultSettings readSettings() {
		
		// Instance of the JSON factory. 
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		YoutubeDefaultSettings settings = null;
		
		// Load application default settings from the "client_settings.json" file
		String filePath = Utility.getAbsoluteFilePath(SERVICESDIR, DATADIR, DEFAULTSFILE);
		
		try {
				InputStream inputStream = new FileInputStream(filePath);
				// Create settings object to access the default settings.
				settings = jsonFactory.fromInputStream(inputStream, YoutubeDefaultSettings.class);
				if (Utility.isDEBUG()) 
					System.out.println(settings.toPrettyString());
	      } catch (IOException e) {
	        String msg = String.format("Error occurred; %s", e.getMessage());
	        System.out.println(msg);
	      }
		if (settings.getProject().startsWith("Enter ")) {
			System.out.println("Enter sample settings info in "
					+ DEFAULTSFILE);
			System.exit(1);
		}
		return settings;
	}

	
	/**
	 * Update the value of the specified key in the client_defaults.json file. 
	 * Note: Remember to assign the default scope when you are done with the application. 
	 * @param key The key identifying the setting.
	 * @param value The value associated with the key.
	 */
	public void updateDefaultSettings(String key, String value) {
		
		// Instance of the JSON factory. 
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

		YoutubeDefaultSettings settings = null;
		
		// Load application default settings from the "client_settings.json" file
		String filePath = Utility.getAbsoluteFilePath(SERVICESDIR, DATADIR, DEFAULTSFILE);
		
		try {
			InputStream inputStream = new FileInputStream(filePath);
			// Create settings object to access the default settings.
			settings = jsonFactory.fromInputStream(inputStream, YoutubeDefaultSettings.class);
			settings.put(key, value);
		}
		catch (IOException e) {
			String msg = String.format("Error occurred; %s", e.getMessage());
			System.out.println(msg);
		}
		
		try (FileWriter file = new FileWriter(filePath)) {

            file.write(settings.toPrettyString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
	
	}

} 

