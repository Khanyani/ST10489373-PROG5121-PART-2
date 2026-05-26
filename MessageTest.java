/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

/**
 *
 * @author Student
 */
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * MessageTest - JUnit 5 unit tests for the Message class.
 * Tests all methods specified in the PROG5121 Part 2 POE requirements.
 *
 * POE test data:
 *   Message 1: recipient = +27718693002, text = "Hi Mike, can you join us for dinner tonight?"
 *   Message 2: recipient = 08575975889 (invalid - no international code)
 */
public class MessageTest {

    // Test message objects created fresh before each test
    private Message validMessage;
    private Message invalidRecipientMessage;

    /**
     * Sets up the test objects before each @Test method runs.
     * Uses the exact test data provided in the POE.
     * The test constructor accepts a specific messageID so the hash is predictable.
     */
    @BeforeEach
    public void setUp() {
        // Clear static session list so tests do not interfere with each other
        Message.clearMessages();

        // POE Test Message 1 - valid recipient, ID starts with "00" for predictable hash
        validMessage = new Message(
            "0000000000",                                        // messageID
            0,                                                   // messageNumber
            "+27718693002",                                      // recipient
            "Hi Mike, can you join us for dinner tonight?"       // messageText
        );

        // POE Test Message 2 - invalid recipient (no international code)
        invalidRecipientMessage = new Message(
            "0000000001",                                        // messageID
            1,                                                   // messageNumber
            "08575975889",                                       // recipient - no + prefix
            "Hi Keegan, did you receive the payment?"           // messageText
        );
    }

    // MESSAGE LENGTH TESTS

    /**
     * Test: message text that is within the 250 character limit.
     * Expected return: "Message ready to send."
     */
    @Test
    public void testMessageLengthValid() {
        String result = validMessage.checkMessageLength(
            "Hi Mike, can you join us for dinner tonight?"
        );
        assertEquals("Message ready to send.", result);
    }

    /**
     * Test: message text that exceeds the 200 character limit.
     * Expected return: failure message with the exact number of characters over the limit.
     */
    @Test
    public void testMessageLengthInvalid() {
        // Build a message that exceeds 200 characters
        String longMessage = "This message is intentionally too long. "
            + "It has been constructed to exceed the two hundred "
            + "character limit that is enforced by the checkMessageLength method "
            + "inside the Message class. Extra text added here to push it over.";

        int expectedOver = longMessage.length() - 200;
        String expectedResult = "Message exceeds 200 characters by " + expectedOver + "; please reduce the size.";

        String result = validMessage.checkMessageLength(longMessage);
        assertEquals(expectedResult, result);
    }

    // RECIPIENT CELL NUMBER TESTS

    /**
     * Test: recipient number that is correctly formatted.
     * +27718693002 starts with +27 and is exactly 12 characters.
     * Expected return: "Cell phone number successfully captured."
     */
    @Test
    public void testRecipientCellValid() {
        String result = validMessage.checkRecipientCell();
        assertEquals("Cell phone number successfully captured.", result);
    }

    /**
     * Test: recipient number that is incorrectly formatted.
     * 08575975889 does not contain an international code.
     * Expected return: the detailed failure message.
     */
    @Test
    public void testRecipientCellInvalid() {
        String result = invalidRecipientMessage.checkRecipientCell();
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            result
        );
    }

    // MESSAGE HASH TEST

    /**
     * Test: message hash is generated correctly using the POE's exact test data.
     * Message ID:  "0000000000" -> first 2 chars = "00"
     * Number:      0
     * First word:  "Hi"
     * Last word:   "tonight?" -> stripped to "tonight"
     * Expected:    "00:0:HITONIGHT"
     */
    @Test
    public void testMessageHashCorrect() {
        assertEquals("00:0:HITONIGHT", validMessage.getMessageHash());
    }

    // MESSAGE ID TESTS

    /**
     * Test: checkMessageID() returns true for a valid 10-character ID.
     */
    @Test
    public void testMessageIDCreated() {
        assertTrue(validMessage.checkMessageID(), "Message ID should be 10 characters or fewer");
    }

    /**
     * Test: message ID is exactly 10 characters long.
     */
    @Test
    public void testMessageIDLength() {
        assertEquals(10, validMessage.getMessageID().length());
    }
    // SENT MESSAGE ACTION TESTS
    /**
     * Test: user selects option 1 (Send Message).
     * Expected return: "Message successfully sent."
     */
    @Test
    public void testSentMessageSend() {
        String result = validMessage.sentMessage(1);
        assertEquals("Message successfully sent.", result);
    }

    /**
     * Test: user selects option 2 (Disregard Message).
     * Expected return: "Press 0 to delete the message."
     */
    @Test
    public void testSentMessageDisregard() {
        String result = validMessage.sentMessage(2);
        assertEquals("Press 0 to delete the message.", result);
    }

    /**
     * Test: user selects option 3 (Store Message).
     * Expected return: "Message successfully stored."
     */
    @Test
    public void testSentMessageStore() {
        String result = validMessage.sentMessage(3);
        assertEquals("Message successfully stored.", result);
    }
}
