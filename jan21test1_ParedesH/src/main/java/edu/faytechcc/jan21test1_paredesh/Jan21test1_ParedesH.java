/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
// This has to go before imports!
package edu.faytechcc.jan21test1_paredesh;

import javax.swing.JOptionPane; // required

/**
 *
 * @author paredesh3418
 */
public class Jan21test1_ParedesH {

    public static void main(String[] args) {
        // The real lesson is:
        // learn how to put stuff into a joptionpane 
        // and learn how to get stuff out
        // basically input() and print() if you do python
        
        // Display a welcome message
        JOptionPane.showMessageDialog(null, "Welcome to the Input and Output Demo!");
        // Let's ask the user for their nickname
        // 
        String nickname = JOptionPane.showInputDialog("What should I call you?");
        
        // Now let's say hi
        String helloMessage = "Nice to meet you, " + nickname + "! ";
        
        JOptionPane.showMessageDialog(null, helloMessage);
        
        
        // Testing the copliot code with this one.
        // Get User input
        String input = JOptionPane.showInputDialog("Enter a prompt for the AI:");
        
        // Simulate AI response
        String aiResponse = generateAIResponse(input);
        
        // Display AI response 
        JOptionPane.showMessageDialog(null, "AI Response: " + aiResponse);
        
    }
    // Stimulated AI response generation
    private static String generateAIResponse(String input) {
        // For simplicity, we'll just echo the input with a message 
        return "You said: " + input + ". This is a simulated AI response.";
    }
}

/*
Copilot suggested this

import javax.swing.JOptionPane; // required

public static void Main {
    public static void main(String[] args) {
        // Display a welcome message
        JOptionPane.showMessageDialog(null, "Welcome to the Generative AI Demo!");
        
        // Get User input
        String input = JOptionPane.showInputDialog("Enter a prompt for the AI:");
        
        // Simulate AI response
        String aiResponse = generateAIResponse(input);
        
        // Display AI response 
        JOptionPane.showMessageDialog(null, "AI Response: " + aiResponse);
    }
    
    // Stimulated AI response generation
    private static String generateAIResponse(String input) {
        // For simplicity, we'll just echo the input with a message 
        return "You said: " + input + ". This is a simulated AI response.";
    }
}
*/