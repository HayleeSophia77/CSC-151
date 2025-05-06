/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package febtest1_paredesh;

import javax.swing.JOptionPane;
import java.util.Scanner;

/**
 *
 * @author paredesh3418
 */
public class FebTest1_ParedesH 
{

    public static String nameOutput() // no parameters they are created inside
    {
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
        
        // JOptionPane.showMessageDialog(null, helloMessage);
        
        
        // Testing the copliot code with this one.
        // Get User input
        String input = JOptionPane.showInputDialog("Enter a prompt for the AI:");
        
        // Simulate AI response
        String aiResponse = generateAIResponse(input);
        
        // Display AI response 
        
        
        //JOptionPane.showMessageDialog(null, "AI Response: " + aiResponse);
        
        return helloMessage + aiResponse; 
    }
    
    
    
    // Stimulated AI response generation
    private static String generateAIResponse(String input) 
    {
        // For simplicity, we'll just echo the input with a message 
        return "You said: " + input + ". This is a simulated AI response.";
    }    
    
    
    public static String classSection()
    {
        JOptionPane.showMessageDialog(null, "Welcome to the Class Section Expaliner!");
        // Call nickname from the method nameOutput()
        String nickname = nameOutput();
        String hiMessage = "Hi, " + nickname + "!";
        
        // Find out what class they are taking
        String className = JOptionPane.showInputDialog("What class are you taking? (Ex/NOS-110)");
        
        
        JOptionPane.showMessageDialog(null, "To find out your class section, we need to know what session is your class.(Ex/NOS-110-0901). Take the first number from the last four digits of you class section to find the class session number.");
        
        // Find out what session their class is
        String classSessionnNum = JOptionPane.showInputDialog("What is you class session number? Enter a number from (1-9)");
        int firstDigit = Integer.parseInt(classSessionnNum);
        
        
        JOptionPane.showMessageDialog(null, "Now, we need to know what location/type that your class is at or the type your class is.(Ex/NOS-110-0901). Take the second number from the last four digits of you class section to find the class session number.");
        
        // Find out the location/type of the class
        String classTypeNum = JOptionPane.showInputDialog("What is you class session number? Enter a number from (0, 1, 3, 4, or 9) OR a letter from (B, C, D, F, H, L, P, R, V, X, or Y)");
        int secondDigit = Integer.parseInt(classTypeNum);
        
        
        JOptionPane.showMessageDialog(null, "Now, we need to know what number your class is.(Ex/NOS-110-0901). Take the third and fourth number from the last four digits of you class section to find the class session number.");
        
        
        // Find out number of course
        String classNum = JOptionPane.showInputDialog("What is you class number? Enter a number from (01-49, 51, or 61)");
        int threefourDigit = Integer.parseInt(classNum);
        
        return hiMessage;
    }
    
    
    
    public static double rectAreaCal() // no parameters they are created inside
    {
        // JOptionPane.showInputDialog() creates a popup dialog box.
        // This can allow the user to type, has an OK button, has an Cancel button, and display a message.
        String lengthInput = JOptionPane.showInputDialog("Enter the length of the rectangle: ");
        
        // If it cannot be partial then it will be an integer, but if it can be partial then it will have to be a double. 
        // length is new variable name.
        // Double.parseDouble(lengthInput) converts text into a number
        // Ex/ "5.5" to 5.5
        double length = Double.parseDouble(lengthInput);
        // Number is stored in length variable.
        
        String widthInput = JOptionPane.showInputDialog("Enter the width of the rectangle: ");
        double width = Double.parseDouble(widthInput);
        
        // JOptionPane.showMessageDialog() is a method that creates a popup window with a message and OK buttton.
        // Null lets Java this dialog isn't attached to any parent window.
        // "The area of the rectangle is: " is the text.
        // + area adds the calculation to the end of the text. 
        
        
        
        // JOptionPane.showMessageDialog(null, "The area of the rectangle is: " + area);
        
        return length * width;
    }
    
    
    
    public static void main(String[] args) 
    {
       // Scanner a = new Scanner(System.in);
       String wholeMess = nameOutput();
       
       // Not needed; make a string with both message dialogs
       // JOptionPane.showMessageDialog(null, helloMessage); 
       // JOptionPane.showMessageDialog(null, "AI Response: " + aiResponse);
       JOptionPane.showMessageDialog(null, wholeMess);
       
       double area = rectAreaCal();
       JOptionPane.showMessageDialog(null, "The area of the rectangle is: " + area);
    }
   
}
