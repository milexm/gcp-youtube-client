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

import java.io.BufferedReader;
import java.io.IOException;
import com.acloudysky.ui.UserInterface;



/*** 
 * Displays a selection menu for the user. Processes the  user's input and calls the proper 
 * method based on the user's selection. 
 * Each method calls the related Google Cloud [service] REST API.
 * @author Michael Miele.
 *
 */
public class SimpleUI extends UserInterface {
	
	
	/**
	 * Instantiates SimpleUI class along with its superclass.
	 */
	SimpleUI() {
		super();
		// Display menu.
		displayMenu(youtubeMenuEntries);
		
	}
	
	/*
	 * Reads user input.
	 */
	private static String readUserInput(String msg) {
		
		// Open standard input.
		BufferedReader br = new BufferedReader(new java.io.InputStreamReader(System.in));
		
		String selection = null;
		
		//  Read the selection from the command-line; need to use try/catch with the
		//  readLine() method
		try {
			if (msg == null)
				System.out.print("\n>>> ");
			else
				System.out.print("\n" + msg);
			selection = br.readLine();
		} catch (IOException e) {
			System.out.println("IO error trying to read your input!");
			System.out.println(String.format("%s", e.getMessage()));
			System.exit(1);
		}
		
		return selection;
		
	}
	
	/*
	 * Executes the selected operation.
	 */
	private void performOperation(String operation) {
		
		// Select operation to perform.
		switch(operation) {
			case "lv": {
				
				try {
					// List uploaded videos.
					VideoInformation.listVideos();
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}	
			case "uv": {
				
				try {
					// Upload video.
					VideoActions.uploadVideo();
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}	
			case "udv": {
				
				try {
					// Update video.
					VideoActions.updateVideo();
				}
				catch (Exception e){
					System.out.println(String.format("%s", e.getMessage()));
				}
				break;
			}	
			default: {
				System.out.println(String.format("%s is not allowed", operation));
				break;
			}
		}
	
	}
	
	
	/**
	 * 
	 */
	public void processUserInput() {
		
		while (true) {
			
			// Get user input.
			String userSelection = readUserInput(null).toLowerCase();	
			// Normalize user's input.
			String normalizedUserSelection = userSelection.trim().toLowerCase();
			
			
			try{
				// Exit the application.
				if ("x".equals(normalizedUserSelection)){
					break;
				}
				else
					if ("m".equals(normalizedUserSelection)) {
						// Display menu
						displayMenu(youtubeMenuEntries);
						continue;
					}
				
			}
			catch (Exception e){
				// System.out.println(e.toString());
				System.out.println(String.format("Input %s is not allowed%n", userSelection));
				continue;
			}
			
			performOperation(normalizedUserSelection);
		}
		
	}
	
}
