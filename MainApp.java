/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */
import java.util.Scanner;
public class MainApp {
    public static void main(String[]args){
        
        // A scanner will allow the user to enter information
        Scanner input = new Scanner(System.in);
        
        // A method that will create an object of the login class
        Login login = new Login();
        
        // ---REGISTRATION SECTION ---
        System.out.println("=== USER REGISTRATION ===");

        boolean isRegistered = false;

        while (!isRegistered) {
        
            System.out.print("Enter a username:");
            String username = input.nextLine();
            
            System.out.print("Enter a password:");
            String password = input.nextLine();
            
            System.out.print("Enter your South African phone number (+27...):");
            String phone = input.nextLine();
            
            //call the resgiterUser
            //Store the message it returns
            String response = login.registerUser(username,password,phone);
            
            // Registration mehtod will show
            if (response.equals("User registered successfully.")) {
                System.out.println("\n" + response);
                isRegistered = true;
            } else {
                System.out.println("\nRegistration failed: " + response);
                System.out.println("Please try again.\n");
            }
        }
        
        // --- LOGIN SECTION ---
        System.out.println("\n=== USER LOGIN ===");

        boolean isLoggedIn = false;
        int attempts = 0;
        int maxAttempts = 5;

        while (!isLoggedIn && attempts < maxAttempts) {

            attempts++;
        
            System.out.print("Enter your username:");
            String loginUsername = input.nextLine();
            
            System.out.print("Enter your password:");
            String loginPassword = input.nextLine();
            
            // Call the LoginUser to check if the details match the ones stored
            boolean loggedIn = login.loginUser(loginUsername, loginPassword);
            
            //Print out the correct login message
            String loginMessage = login.returnLoginStatus(loggedIn);

            if (loggedIn) {
                System.out.println("\n" + loginMessage);
                isLoggedIn = true;
            } else {
                int remaining = maxAttempts - attempts;

                if (remaining > 0) {
                    System.out.println("\n" + loginMessage);
                    System.out.println("You have " + remaining + " attempt(s) remaining.\n");
                } else {
                    System.out.println("\nToo many failed attempts. Your account has been locked.");
                    System.out.println("Please contact support for assistance.");
                }
            }
        }
//  MESSAGING
        if (isLoggedIn) {
 
            // Welcome message displayed after successful login
            System.out.println("Welcome to ChatApp.");
 
            // Application loop - keeps the menu running until the user quits
            boolean running = true;
 
            while (running) {
 
                // Display the main menu options
                System.out.println("\n==============================");
                System.out.println("       CHATAPP MENU");
                System.out.println("==============================");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.print("Enter your choice: ");
 
                int choice = input.nextInt();
                input.nextLine(); // consume the leftover newline
 
                switch (choice) {
 
                    case 1 -> {
                        // SEND MESSAGES
                        System.out.print("\nHow many messages would you like to send? ");
                        int numMessages = input.nextInt();
                        input.nextLine(); // consume newline
 
                        // For loop runs exactly numMessages times
                        // Each iteration represents one message being composed and sent
                        for (int i = 0; i < numMessages; i++) {
 
                            // messageNumber is the human-readable loop counter (starts at 1)
                            int messageNumber = i + 1;
                            System.out.println("\n--- Message " + messageNumber + " ---");
 
                            // Collect and validate the recipient number
                            String recipient = "";
                            boolean validRecipient = false;
 
                            while (!validRecipient) {
                                System.out.print("Enter recipient cell number (+27...): ");
                                recipient = input.nextLine();
 
                                // Use a temporary Message to run checkRecipientCell()
                                Message tempMsg = new Message("0000000000", messageNumber, recipient, "placeholder");
                                String recipientCheck = tempMsg.checkRecipientCell();
                                System.out.println(recipientCheck);
 
                                if (recipientCheck.equals("Cell phone number successfully captured.")) {
                                    validRecipient = true;
                                }
                                // If invalid, loop repeats and asks again
                            }
                            
                            // Collect and validate the message text 
                            String messageText = "";
                            boolean validMessage = false;
 
                            while (!validMessage) {
                                System.out.print("Enter your message (max 250 characters): ");
                                messageText = input.nextLine();
 
                                // Use a temporary Message to run checkMessageLength()
                                Message tempMsg = new Message("0000000000", messageNumber, recipient, messageText);
                                String lengthCheck = tempMsg.checkMessageLength(messageText);
                                System.out.println(lengthCheck);
 
                                if (lengthCheck.equals("Message ready to send.")) {
                                    validMessage = true;
                                }
                                // If too long, loop repeats and asks again
                            }
 
                            //  Create the actual message with a real generated ID
                            Message message = new Message(messageNumber, recipient, messageText);
                            
                            // Ask the user what to do with the message 
                            System.out.println("\nWhat would you like to do with this message?");
                            System.out.println("1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println("3) Store Message to send later");
                            System.out.print("Enter your choice: ");
 
                            int sendOption = input.nextInt();
                            input.nextLine(); // consume newline
 
                            // Call sentMessage with the user's choice and print the result
                            String sendResult = message.sentMessage(sendOption);
                            System.out.println("\n" + sendResult);
 
                            // Display message details in the required order
                            if (sendOption == 1 || sendOption == 3) {
                                System.out.println("\nMessage Details:");
                                System.out.println("Message ID:   " + message.getMessageID());
                                System.out.println("Message Hash: " + message.getMessageHash());
                                System.out.println("Recipient:    " + message.getRecipient());
                                System.out.println("Message:      " + message.getMessageText());
                            }
                        }
 
                        // Display total messages after the for loop ends
                        System.out.println("\nTotal messages sent/stored this session: " + Message.returnTotalMessages());
                    }
 
                    case 2 -> // Feature not yet built - placeholder as per POE requirements
                        System.out.println("Coming Soon.");
 
                    case 3 -> {
                        // Quit - set running to false to exit the while loop
                        running = false;
                        System.out.println("Thank you for using ChatApp. Goodbye!");
                    }
 
                    default -> // Handle any input that is not 1, 2, or 3
                        System.out.println("Invalid option. Please enter 1, 2, or 3.");
                }
            }
 
        } else {
            // User could not log in after 3 attempts - exit the application
            System.out.println("\nAccess denied. Exiting ChatApp.");
        }
 
        input.close();
    }
 
}
