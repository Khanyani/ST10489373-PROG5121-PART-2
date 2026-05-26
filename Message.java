/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */
// Attribution: org.json library used for JSON file storage
// Source: https://mvnrepository.com/artifact/org.json/json
// Version used: 20231013
 
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
 
/**
 * Message class - represents a single chat message in ChatApp.
 * Handles message creation, validation, hashing, and JSON storage.
 */
public class Message {
    // Fields
    private String messageID;       // 10-digit randomly generated ID
    private int messageNumber;      // position in the send loop
    private String recipient;       // validated international cell number
    private String messageText;     // max 250 characters
    private String messageHash;     // auto-generated from ID, number, and message words
    private String sendStatus;      // Sent, Stored, or Disregarded
 
    // Static list to track all sent and stored messages in the current session
    private static ArrayList<Message> sentMessages = new ArrayList<>();
 
    // CONSTRUCTORS
 
 
    /**
     * Run time constructor - auto-generates the message ID and hash.
     * Used by MainApp when a user composes a message.
     * @param messageNumber the position of this message in the send loop
     * @param recipient     the recipient's cell number
     * @param messageText   the message content (max 250 characters)
     */
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }
 
    /**
     * Test constructor - accepts a specific message ID so unit tests can predict the hash.
     * Used by MessageTest.java to pass known values.
     * @param messageID     a specific 10-character ID for controlled testing
     * @param messageNumber the position of this message in the send loop
     * @param recipient     the recipient's cell number
     * @param messageText   the message content (max 250 characters)
     */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }
 
    // PRIVATE HELPERS
 
    /**
     * Generates a random 10-digit message ID using Java's Random class.
     * Ensures the result is always exactly 10 digits using string manipulation.
     * @return a String of exactly 10 digits
     */
    private String generateMessageID() {
        Random rand = new Random();
        // Generate a number between 1000000000 and 9999999999 (always 10 digits)
        long id = (long) (rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }
 

    // VALIDATION METHODS-
 
    /**
     * Checks that the message ID does not exceed 10 characters.
     * Used in unit tests to confirm correct ID generation.
     * @return true if the ID is 10 characters or fewer, false otherwise
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }
 
    /**
     * Validates the message text length against the 250 character limit.
     * Returns the exact String messages required by the unit tests.
     * @param message the message text entered by the user
     * @return "Message ready to send." on success, or a failure message with the character count
     */
    public String checkMessageLength(String message) {
        if (message.length() > 200) {
            int over = message.length() - 250;
            return "Message exceeds 200 characters by " + over + "; please reduce the size.";
        }
        return "Message ready to send.";
    }
 
    /**
     * Validates the recipient's cell number stored in this message.
     * Reuses the same South African phone number logic from Part 1:
     * must start with +27 and be exactly 12 characters in total.
     * @return "Cell phone number successfully captured." on success,
     *         or a detailed failure message on invalid format
     */
    public String checkRecipientCell() {
        if (recipient != null && recipient.startsWith("+27") && recipient.length() == 12) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }
 
    // HASH METHOD

    /**
     * Builds the message hash from the first 2 digits of the ID,
     * the message number, and the first and last words of the message text.
     * Format: XX:N:FIRSTWORDLASTWORD (all uppercase)
     * Example: 00:0:HITONIGHT
     * @return the generated hash string in uppercase
     */
    public String createMessageHash() {
        // Step 1: get the first 2 characters of the message ID
        String idPart = messageID.substring(0, 2);
 
        // Step 2: split the message text into individual words
        String[] words = messageText.split(" ");
 
        // Step 3: get first and last words, stripping any punctuation
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
 
        // Step 4: assemble the hash and return in uppercase
        String hash = idPart + ":" + messageNumber + ":" + firstWord + lastWord;
        return hash.toUpperCase();
    }
 
    // SEND / DISREGARD / STORE

    /**
     * Handles the user's decision to Send, Disregard, or Store the message.
     * Takes the user's menu choice as a parameter so this method is fully unit testable.
     * The menu prompt and input reading happen in MainApp before this is called.
     * @param option 1 = Send, 2 = Disregard, 3 = Store
     * @return a String describing the action taken
     */
    public String sentMessage(int option) {
        switch (option) {
            case 1 -> {
                // Send: mark as sent and add to session list
                sendStatus = "Sent";
                sentMessages.add(this);
                return "Message successfully sent.";
            }
 
            case 2 -> {
                // Disregard: mark as disregarded, do not store
                sendStatus = "Disregarded";
                return "Press 0 to delete the message.";
            }
 
            case 3 -> {
                // Store: save to JSON file and add to session list
                sendStatus = "Stored";
                sentMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            }
 
            default -> {
                return "Invalid option selected. Message not processed.";
            }
        }
    }
 
    // DISPLAY AND COUNT METHODS

    /**
     * Returns a formatted String listing all messages sent or stored in this session.
     * Displays Message ID, Hash, Recipient, Message, and Status for each entry.
     * @return a formatted String of all session messages, or a 'no messages' notice
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message msg : sentMessages) {
            sb.append("Message ID: ").append(msg.messageID).append("\n");
            sb.append("Message Hash: ").append(msg.messageHash).append("\n");
            sb.append("Recipient: ").append(msg.recipient).append("\n");
            sb.append("Message: ").append(msg.messageText).append("\n");
            sb.append("Status: ").append(msg.sendStatus).append("\n");
            sb.append("------------------------------\n");
        }
        return sb.toString();
    }
 
    /**
     * Returns the total number of messages sent or stored during the session.
     * Disregarded messages are not counted.
     * @return the integer count of sent and stored messages
     */
    public static int returnTotalMessages() {
        return sentMessages.size();
    }
 
    // JSON STORAGE
 
    /**
     * Saves this message to a JSON file (messages.json) using the org.json library.
     * Appends each stored message as a new line in the file.
     * Attribution: org.json library - https://mvnrepository.com/artifact/org.json/json
     */
    public void storeMessage() {
        // Build a JSON object with all message fields
        JSONObject obj = new JSONObject();
        obj.put("messageID", this.messageID);
        obj.put("messageNumber", this.messageNumber);
        obj.put("recipient", this.recipient);
        obj.put("message", this.messageText);
        obj.put("messageHash", this.messageHash);
        obj.put("status", this.sendStatus);
 
        // Write the JSON object to messages.json, appending to existing content
        try (FileWriter fw = new FileWriter("messages.json", true)) {
            fw.write(obj.toString() + "\n");
            System.out.println("Message saved to messages.json.");
        } catch (IOException e) {
            System.out.println("Error saving message: " + e.getMessage());
        }
    }
 
    // GETTERS
 
    /** @return the 10-digit message ID */
    public String getMessageID() { return messageID; }
 
    /** @return the message number (loop position) */
    public int getMessageNumber() { return messageNumber; }
 
    /** @return the recipient cell number */
    public String getRecipient() { return recipient; }
 
    /** @return the message text */
    public String getMessageText() { return messageText; }
 
    /** @return the generated message hash */
    public String getMessageHash() { return messageHash; }
 
    /** @return the send status (Sent, Stored, or Disregarded) */
    public String getSendStatus() { return sendStatus; }
 
    /**
     * Clears the static session message list.
     * Used in unit tests to reset state between test cases.
     */
    public static void clearMessages() {
        sentMessages.clear();
    }
}
    

