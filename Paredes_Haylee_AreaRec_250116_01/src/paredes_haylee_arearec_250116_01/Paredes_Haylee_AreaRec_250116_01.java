/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package paredes_haylee_arearec_250116_01;
// Imports 
// This is an import statement that gives access to JOptionPane class. 
// javax is the package namespace for the extensions. 
// swing is the sub-package(contains GUI components).
// JOptionPane is the class we are trying to use.

import javax.swing.JOptionPane;
// This provide dialog boxes to show messages to the user, get input from said user, show warnings or error messages, or ask yes/no questions.

public class Paredes_Haylee_AreaRec_250116_01 
{
// Global Items
    
    public static void main(String[] args) 
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
        
        // This calculates the area
        double area = length * width;
        
        // JOptionPane.showMessageDialog() is a method that creates a popup window with a message and OK buttton.
        // Null lets Java this dialog isn't attached to any parent window.
        // "The area of the rectangle is: " is the text.
        // + area adds the calculation to the end of the text. 
        JOptionPane.showMessageDialog(null, "The area of the rectangle is: " + area);
        
    }
    
}
