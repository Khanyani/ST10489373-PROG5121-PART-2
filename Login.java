

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp; 



/**
 *
 * @author Student
 */
public class Login {

    private String username;
    private String password;
    private String phoneNumber;

    // The username must contain an underscore and must not be longer than 5 characters
    public boolean checkUserName(String username) {
        // username.contains("_") checks for underscore
        // username.length() <= 5 ensures the username does not exceed 5 characters
        return username.contains("_") && username.length() <= 5;
    }

    // Checks whether the password is strong enough.
    // A valid password must have: one capital letter, one number,
    // one special character, and be at least 8 characters long.
    public boolean checkPasswordComplexity(String password) {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return password.length() >= 8 && hasCapital && hasNumber && hasSpecial;
    }

    // Validates that the phone number starts with +27 and has exactly 12 characters total
    public boolean checkCellPhoneNumber(String phone) {
        return phone.startsWith("+27") && phone.length() == 12;
    }

    // Registers a user after validating username, password, and phone number.
    // Returns a descriptive message for each outcome.
    public String registerUser(String username, String password, String phoneNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        return "User registered successfully.";
    }

    // Allows a user to log in using their registered credentials
    public boolean loginUser(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }
   
    public String returnLoginStatus(boolean success) {
        if (success) {
            return "Welcome " + username + ", it is great to see you.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
    

