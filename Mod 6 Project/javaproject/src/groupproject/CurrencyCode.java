package groupproject;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Simple JSON parser implementation since org.json might not be available
class JSONObject {
    private Map<String, Object> map = new HashMap<>();
    
    public JSONObject(String jsonString) {
        parseJSON(jsonString.trim());
    }
    
    private void parseJSON(String json) {
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new RuntimeException("Invalid JSON format");
        }
        
        // Remove the outer braces
        json = json.substring(1, json.length() - 1).trim();
        
        // Simple state machine to parse the JSON
        int i = 0;
        while (i < json.length()) {
            // Find the key (always in quotes)
            if (json.charAt(i) != '"') {
                i++;
                continue;
            }
            
            int keyStart = i + 1;
            i++; // Skip opening quote
            while (i < json.length() && json.charAt(i) != '"') {
                i++;
            }
            if (i >= json.length()) break;
            
            String key = json.substring(keyStart, i);
            i++; // Skip closing quote
            
            // Find the colon
            while (i < json.length() && json.charAt(i) != ':') {
                i++;
            }
            if (i >= json.length()) break;
            i++; // Skip colon
            
            // Skip whitespace
            while (i < json.length() && Character.isWhitespace(json.charAt(i))) {
                i++;
            }
            if (i >= json.length()) break;
            
            // Determine value type
            char valueStart = json.charAt(i);
            if (valueStart == '{') {
                // Parse nested object
                int braceCount = 1;
                int valueStartPos = i;
                i++; // Skip opening brace
                
                while (i < json.length() && braceCount > 0) {
                    if (json.charAt(i) == '{') braceCount++;
                    if (json.charAt(i) == '}') braceCount--;
                    i++;
                }
                
                String nestedJson = json.substring(valueStartPos, i);
                map.put(key, new JSONObject(nestedJson));
            } else if (valueStart == '[') {
                // Skip arrays for simplicity (we don't need them for this application)
                int bracketCount = 1;
                i++; // Skip opening bracket
                
                while (i < json.length() && bracketCount > 0) {
                    if (json.charAt(i) == '[') bracketCount++;
                    if (json.charAt(i) == ']') bracketCount--;
                    i++;
                }
            } else if (valueStart == '"') {
                // Parse string
                i++; // Skip opening quote
                int valueStartPos = i;
                
                while (i < json.length() && json.charAt(i) != '"') {
                    if (json.charAt(i) == '\\') i += 2; // Skip escaped characters
                    else i++;
                }
                
                String value = json.substring(valueStartPos, i);
                map.put(key, value);
                i++; // Skip closing quote
            } else {
                // Parse number or boolean
                int valueStartPos = i;
                
                while (i < json.length() && json.charAt(i) != ',' && json.charAt(i) != '}') {
                    i++;
                }
                
                String valueStr = json.substring(valueStartPos, i).trim();
                
                if (valueStr.equals("true")) {
                    map.put(key, Boolean.TRUE);
                } else if (valueStr.equals("false")) {
                    map.put(key, Boolean.FALSE);
                } else if (valueStr.equals("null")) {
                    map.put(key, null);
                } else {
                    try {
                        if (valueStr.contains(".")) {
                            map.put(key, Double.parseDouble(valueStr));
                        } else {
                            map.put(key, Integer.parseInt(valueStr));
                        }
                    } catch (NumberFormatException e) {
                        map.put(key, valueStr);
                    }
                }
            }
            
            // Skip to next key-value pair
            while (i < json.length() && json.charAt(i) != ',') {
                if (json.charAt(i) == '}') break;
                i++;
            }
            i++; // Skip comma
        }
    }
    
    public boolean has(String key) {
        return map.containsKey(key);
    }
    
    public JSONObject getJSONObject(String key) {
        Object obj = map.get(key);
        if (obj instanceof JSONObject) {
            return (JSONObject) obj;
        }
        throw new RuntimeException("Value for key '" + key + "' is not a JSONObject");
    }
    
    public double getDouble(String key) {
        Object obj = map.get(key);
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        throw new RuntimeException("Value for key '" + key + "' is not a number");
    }
    
    public Iterable<String> keySet() {
        return map.keySet();
    }
    
    @Override
    public String toString() {
        return map.toString();
    }
}

public class CurrencyCode 
{
    // Class constants
    private static final Color THEME_COLOR = new Color(135, 206, 250); // Light blue theme
    private static final double BASE_FEE_PERCENTAGE = 2.5; // 2.5% base fee
    private static final double MIN_FEE_USD = 2.0; // Minimum fee in USD
    
    // Font constants - all using Courier New for consistency
    private static final Font HEADER_FONT = new Font("Courier New", Font.PLAIN, 16);
    private static final Font CONTENT_FONT = new Font("Courier New", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Courier New", Font.PLAIN, 12);
    private static final Font RESULT_FONT = new Font("Courier New", Font.PLAIN, 14);
    
    // API URL is now defined directly in the fetchExchangeRates method
    
    static Map<String, String> countryCurrencyMap = new HashMap<>();
    static Map<String, Double> currencyToUSDMap = new HashMap<>();
    static Map<String, String> currencyNameMap = new HashMap<>(); // To store full currency names

    static 
    {
        // Currency map for each country
        countryCurrencyMap.put("USA", "USD");
        countryCurrencyMap.put("Canada", "CAD");
        countryCurrencyMap.put("Mexico", "MXN");
        countryCurrencyMap.put("Belize", "BZD");
        countryCurrencyMap.put("Costa Rica", "CRC");
        countryCurrencyMap.put("El Salvador", "USD");
        countryCurrencyMap.put("Guatemala", "GTQ");
        countryCurrencyMap.put("Honduras", "HNL");
        countryCurrencyMap.put("Nicaragua", "NIO");
        countryCurrencyMap.put("Panama", "PAB");
        countryCurrencyMap.put("Antigua and Barbuda", "XCD");
        countryCurrencyMap.put("Bahamas", "BSD");
        countryCurrencyMap.put("Barbados", "BBD");
        countryCurrencyMap.put("Cuba", "CUP");
        countryCurrencyMap.put("Dominica", "XCD");
        countryCurrencyMap.put("Dominican Republic", "DOP");
        countryCurrencyMap.put("Grenada", "XCD");
        countryCurrencyMap.put("Haiti", "HTG");
        countryCurrencyMap.put("Jamaica", "JMD");
        countryCurrencyMap.put("Saint Kitts and Nevis", "XCD");
        countryCurrencyMap.put("Saint Lucia", "XCD");
        countryCurrencyMap.put("Saint Vincent and the Grenadines", "XCD");
        countryCurrencyMap.put("Trinidad and Tobago", "TTD");
        countryCurrencyMap.put("Argentina", "ARS");
        countryCurrencyMap.put("Bolivia", "BOB");
        countryCurrencyMap.put("Brazil", "BRL");
        countryCurrencyMap.put("Chile", "CLP");
        countryCurrencyMap.put("Colombia", "COP");
        countryCurrencyMap.put("Ecuador", "USD");
        countryCurrencyMap.put("Guyana", "GYD");
        countryCurrencyMap.put("Paraguay", "PYG");
        countryCurrencyMap.put("Peru", "PEN");
        countryCurrencyMap.put("Suriname", "SRD");
        countryCurrencyMap.put("Uruguay", "UYU");
        countryCurrencyMap.put("Venezuela", "VES");

        // Currency names for full name display
        currencyNameMap.put("USD", "US Dollar");
        currencyNameMap.put("CAD", "Canadian Dollar");
        currencyNameMap.put("MXN", "Mexican Peso");
        currencyNameMap.put("BZD", "Belize Dollar");
        currencyNameMap.put("CRC", "Costa Rican Colón");
        currencyNameMap.put("GTQ", "Guatemalan Quetzal");
        currencyNameMap.put("HNL", "Honduran Lempira");
        currencyNameMap.put("NIO", "Nicaraguan Córdoba");
        currencyNameMap.put("PAB", "Panamanian Balboa");
        currencyNameMap.put("XCD", "East Caribbean Dollar");
        currencyNameMap.put("BSD", "Bahamian Dollar");
        currencyNameMap.put("BBD", "Barbadian Dollar");
        currencyNameMap.put("CUP", "Cuban Peso");
        currencyNameMap.put("DOP", "Dominican Peso");
        currencyNameMap.put("HTG", "Haitian Gourde");
        currencyNameMap.put("JMD", "Jamaican Dollar");
        currencyNameMap.put("TTD", "Trinidad and Tobago Dollar");
        currencyNameMap.put("ARS", "Argentine Peso");
        currencyNameMap.put("BOB", "Bolivian Boliviano");
        currencyNameMap.put("BRL", "Brazilian Real");
        currencyNameMap.put("CLP", "Chilean Peso");
        currencyNameMap.put("COP", "Colombian Peso");
        currencyNameMap.put("GYD", "Guyanese Dollar");
        currencyNameMap.put("PYG", "Paraguayan Guarani");
        currencyNameMap.put("PEN", "Peruvian Nuevo Sol");
        currencyNameMap.put("SRD", "Surinamese Dollar");
        currencyNameMap.put("UYU", "Uruguayan Peso");
        currencyNameMap.put("VES", "Venezuelan Bolívar");
    }

    /**
     * Fetches the latest exchange rates from the API
     * @return Map containing currency codes and their exchange rates relative to USD
     */
    public static Map<String, Double> fetchExchangeRates() {
        Map<String, Double> rates = new HashMap<>();
        JDialog loadingDialog = null;
        
        try {
            // Show loading dialog
            loadingDialog = new JDialog();
            loadingDialog.setTitle("Loading Exchange Rates");
            JLabel loadingLabel = new JLabel("Fetching the latest exchange rates...");
            loadingLabel.setFont(CONTENT_FONT);
            loadingLabel.setBorder(new EmptyBorder(20, 30, 20, 30));
            loadingDialog.add(loadingLabel);
            loadingDialog.pack();
            loadingDialog.setLocationRelativeTo(null);
            loadingDialog.setModal(false);
            loadingDialog.setVisible(true);
            
            // Try an alternative API: ExchangeRate-API.com with free access key
            String apiUrl = "https://open.er-api.com/v6/latest/USD";
            
            // Create connection
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            
            // Check if the connection was successful
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("HTTP Error: " + responseCode);
            }
            
            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            // Debug output to console
            System.out.println("API Response: " + response.toString());
            
            try {
                // Parse JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                
                // Check if we have rates field
                if (jsonResponse.has("rates")) {
                    JSONObject ratesObject = jsonResponse.getJSONObject("rates");
                    
                    // Convert rates to our format (relative to USD)
                    for (String currencyCode : ratesObject.keySet()) {
                        double rate = ratesObject.getDouble(currencyCode);
                        // Store the USD value of 1 unit of this currency
                        rates.put(currencyCode, 1.0 / rate);
                    }
                    
                    // Add USD as base currency
                    rates.put("USD", 1.0);
                    
                    // Close loading dialog
                    if (loadingDialog != null) {
                        loadingDialog.setVisible(false);
                        loadingDialog.dispose();
                    }
                    
                    return rates;
                } else {
                    throw new Exception("API response missing 'rates' field");
                }
            } catch (Exception e) {
                System.out.println("JSON parsing error: " + e.getMessage());
                throw new Exception("Error parsing JSON: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            
            // Close loading dialog if it's still open
            if (loadingDialog != null && loadingDialog.isVisible()) {
                loadingDialog.setVisible(false);
                loadingDialog.dispose();
            }
            
            // Show more detailed error message
            JOptionPane.showMessageDialog(null, 
                "Could not fetch exchange rates. Using stored rates instead.\n" + 
                "Error: " + e.getMessage() + "\n" +
                "Please check your internet connection or try again later.",
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            
            // Fall back to stored rates if API call fails
            return getFallbackRates();
        } finally {
            // Ensure loading dialog is closed
            if (loadingDialog != null && loadingDialog.isVisible()) {
                loadingDialog.setVisible(false);
                loadingDialog.dispose();
            }
        }
    }
    
    /**
     * Provides fallback exchange rates if API call fails
     * @return Map containing fallback exchange rates
     */
    private static Map<String, Double> getFallbackRates() {
        Map<String, Double> fallbackRates = new HashMap<>();
        
        // Exchange rates relative to USD for all currencies
        fallbackRates.put("USD", 1.0); // USD is base
        fallbackRates.put("CAD", 0.75);
        fallbackRates.put("MXN", 0.052);
        fallbackRates.put("BZD", 0.5);
        fallbackRates.put("CRC", 0.0016);
        fallbackRates.put("GTQ", 0.13);
        fallbackRates.put("HNL", 0.04);
        fallbackRates.put("NIO", 0.028);
        fallbackRates.put("PAB", 1.0);
        fallbackRates.put("XCD", 0.37);
        fallbackRates.put("BSD", 1.0);
        fallbackRates.put("BBD", 0.5);
        fallbackRates.put("CUP", 0.041);
        fallbackRates.put("DOP", 0.018);
        fallbackRates.put("HTG", 0.010);
        fallbackRates.put("JMD", 0.0065);
        fallbackRates.put("TTD", 0.15);
        fallbackRates.put("ARS", 0.0085);
        fallbackRates.put("BOB", 0.14);
        fallbackRates.put("BRL", 0.19);
        fallbackRates.put("CLP", 0.0013);
        fallbackRates.put("COP", 0.00026);
        fallbackRates.put("GYD", 0.0048);
        fallbackRates.put("PYG", 0.00014);
        fallbackRates.put("PEN", 0.26);
        fallbackRates.put("SRD", 0.13);
        fallbackRates.put("UYU", 0.023);
        fallbackRates.put("VES", 0.00004);
        
        return fallbackRates;
    }

    public static void main(String[] args) 
    {
        // Set UI look and feel properties
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Apply custom colors to components
            UIManager.put("OptionPane.background", THEME_COLOR);
            UIManager.put("Panel.background", THEME_COLOR);
            UIManager.put("Button.background", THEME_COLOR.darker());
            
            // Apply custom fonts to UI components - consistent Verdana font
            UIManager.put("OptionPane.messageFont", CONTENT_FONT);
            UIManager.put("OptionPane.buttonFont", BUTTON_FONT);
            UIManager.put("TextField.font", CONTENT_FONT);
            UIManager.put("ComboBox.font", CONTENT_FONT);
            UIManager.put("Label.font", CONTENT_FONT);
            
            // Ensure dropdown box components also use Verdana font
            UIManager.put("ComboBox.listFont", CONTENT_FONT);           // For the dropdown list items
            UIManager.put("ComboBox.selectionFont", CONTENT_FONT);      // For the selected item
            UIManager.put("List.font", CONTENT_FONT);                   // For the list in the dropdown
        } catch (Exception e) {
            System.out.println("Error setting look and feel: " + e.getMessage());
        }

        runExchangeProgram();
    }
    
    public static void runExchangeProgram() {
        ImageIcon icon = null;
        try {
            // Try loading the image from the same directory where the Java file is located
            java.net.URL imageUrl = CurrencyCode.class.getResource("coinspin.gif");

            // If the image is found, proceed
            if (imageUrl != null) {
                icon = new ImageIcon(imageUrl);
                // Scale the icon for better display
                // for some reason this scaling gives a nondisplaying image sometimes - AN
                //icon = new ImageIcon(icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH));
                System.out.println("Icon loaded successfully from: " + imageUrl);
            } else {
                // If image is not found in the same directory, print an error
                System.err.println("GIF not found in the same directory as the Java file.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create welcome message with custom styling
        JLabel welcomeLabel = new JLabel("Welcome to the Currency Exchange Program!");
        welcomeLabel.setFont(HEADER_FONT);
        welcomeLabel.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Use the icon in your JOptionPane, or fallback to a default icon
        if (icon != null && icon.getIconWidth() > 0) {
            JOptionPane.showMessageDialog(null, welcomeLabel, 
                    "Currency Exchange", JOptionPane.PLAIN_MESSAGE, icon);
        } else {
            System.out.println("Using default icon because image couldn't be loaded");
            JOptionPane.showMessageDialog(null, welcomeLabel, 
                    "Currency Exchange", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Fetch the latest exchange rates
        Map<String, Double> currencyToUSDMap = fetchExchangeRates();

        String[] countries = {
            "USA", "Canada", "Mexico", "Belize", "Costa Rica", "El Salvador",
            "Guatemala", "Honduras", "Nicaragua", "Panama", "Antigua and Barbuda",
            "Bahamas", "Barbados", "Cuba", "Dominica", "Dominican Republic", "Grenada",
            "Haiti", "Jamaica", "Saint Kitts and Nevis", "Saint Lucia", 
            "Saint Vincent and the Grenadines", "Trinidad and Tobago", "Argentina", 
            "Bolivia", "Brazil", "Chile", "Colombia", "Ecuador", "Guyana", "Paraguay", 
            "Peru", "Suriname", "Uruguay", "Venezuela"
        };

        boolean continueExchanging = true;
        
        while (continueExchanging) {
            // Step 1: Country Selection with styled prompt
            JLabel countryPrompt = new JLabel("Select the country you are from:");
            countryPrompt.setFont(CONTENT_FONT);
            
            String selectedCountry = (String) JOptionPane.showInputDialog(
                null,
                countryPrompt,
                "Country Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                countries,
                countries[0]
            );

            if (selectedCountry == null) {
                JOptionPane.showMessageDialog(null, "Program has been cancelled.");
                return;
            }

            // Show the currency used in the selected country with styled text
            String fromCurrency = countryCurrencyMap.getOrDefault(selectedCountry, "(Unknown Currency)");
            String fromCurrencyFull = currencyNameMap.get(fromCurrency);
            
            JLabel currencyInfoLabel = new JLabel("<html>" + selectedCountry + " uses the currency:<br><b>" 
                + fromCurrencyFull + " (" + fromCurrency + ")</b></html>");
            currencyInfoLabel.setFont(CONTENT_FONT);
            
            JOptionPane.showMessageDialog(null, currencyInfoLabel);

            // Step 2: Destination Country with styled prompt
            JLabel destPrompt = new JLabel("Select the country you are visiting:");
            destPrompt.setFont(CONTENT_FONT);
            
            String destinationCountry = (String) JOptionPane.showInputDialog(
                null,
                destPrompt,
                "Destination Country",
                JOptionPane.QUESTION_MESSAGE,
                null,
                countries,
                countries[0]
            );

            if (destinationCountry == null) {
                JOptionPane.showMessageDialog(null, "Program has been cancelled.");
                return;
            }

            // Show the currency used in the destination country with styled text
            String toCurrency = countryCurrencyMap.getOrDefault(destinationCountry, "(Unknown Currency)");
            String toCurrencyFull = currencyNameMap.get(toCurrency);
            
            JLabel destCurrencyLabel = new JLabel("<html>" + destinationCountry + " uses the currency:<br><b>" 
                + toCurrencyFull + " (" + toCurrency + ")</b></html>");
            destCurrencyLabel.setFont(CONTENT_FONT);
            
            JOptionPane.showMessageDialog(null, destCurrencyLabel);

            // Step 3: Amount to exchange - with improved validation and styling
            String amountStr;
            double amount = 0;
            boolean validAmount = false;
            
            while (!validAmount) {
                // Create a styled input prompt
                JLabel amountPrompt = new JLabel("Enter the amount of " + fromCurrencyFull + " you are exchanging:");
                amountPrompt.setFont(CONTENT_FONT);
                
                amountStr = JOptionPane.showInputDialog(null, amountPrompt);

                if (amountStr == null) {
                    JOptionPane.showMessageDialog(null, "Program has been cancelled.");
                    return;
                }

                try {
                    amount = Double.parseDouble(amountStr);

                    if (amount <= 0) {
                        JLabel errorLabel = new JLabel("Amount must be greater than 0. Please try again.");
                        errorLabel.setFont(CONTENT_FONT);
                        errorLabel.setForeground(Color.RED);
                        JOptionPane.showMessageDialog(null, errorLabel, "Invalid Amount", JOptionPane.ERROR_MESSAGE);
                    } else {
                        validAmount = true;
                    }
                } catch (NumberFormatException e) {
                    JLabel errorLabel = new JLabel("Invalid number. Please enter a valid amount.");
                    errorLabel.setFont(CONTENT_FONT);
                    errorLabel.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(null, errorLabel, "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Step 4: Calculate exchange fee and the amount after exchange
            // Get conversion rates from the API response
            double fromCurrencyToUSD = currencyToUSDMap.getOrDefault(fromCurrency, 1.0);
            double toCurrencyToUSD = currencyToUSDMap.getOrDefault(toCurrency, 1.0);
            
            // First convert amount to USD
            double amountInUSD = amount * fromCurrencyToUSD;
            
            // Then convert USD amount to target currency
            double exchangeAmount = amountInUSD / toCurrencyToUSD;
            
            // Calculate the exchange rate for display
            double exchangeRate = fromCurrencyToUSD / toCurrencyToUSD;
            
            // Calculate fee in USD, then convert to target currency
            double feeInUSD = Math.max(amountInUSD * BASE_FEE_PERCENTAGE / 100, MIN_FEE_USD);
            double exchangeFee = feeInUSD / toCurrencyToUSD;
            
            // Calculate final amount after deducting fee
            double finalAmount = exchangeAmount - exchangeFee;
            
            // Display exchange rate source information
            String rateSourceInfo = "Using real-time exchange rates from Open Exchange Rate API";
            
            // Create a styled result message
            JLabel resultLabel = new JLabel("<html><div style='text-align: center;'>" +
                "You are exchanging <b>" + amount + " " + fromCurrency + "</b> (" + fromCurrencyFull + ").<br><br>" +
                "Exchange rate: <b>1 " + fromCurrency + " = " + String.format("%.4f", exchangeRate) + " " + toCurrency + "</b><br>" +
                "<span style='font-size: smaller;'>" + rateSourceInfo + "</span><br><br>" +
                "After an exchange fee of <b>" + String.format("%.2f", exchangeFee) + " " + toCurrency + "</b>, you will receive:<br><br>" +
                "<span style='font-size: larger;'><b>" + String.format("%.2f", finalAmount) + " " + toCurrency + "</b></span><br>" +
                "(" + toCurrencyFull + ")" +
                "</div></html>");
            resultLabel.setFont(RESULT_FONT);

            JOptionPane.showMessageDialog(null, resultLabel, "Exchange Result", JOptionPane.INFORMATION_MESSAGE);

            // Step 5: Continue or exit the program with styled options
            JLabel continuePrompt = new JLabel("Would you like to perform another exchange?");
            continuePrompt.setFont(CONTENT_FONT);
            
            int continueOption = JOptionPane.showConfirmDialog(null, continuePrompt, 
                "Another Exchange", JOptionPane.YES_NO_OPTION);
            if (continueOption == JOptionPane.NO_OPTION) {
                continueExchanging = false;
            }
        }

        Font farewellFont = new Font("Courier New", Font.PLAIN, 14);

        // Create a styled farewell message
        JLabel farewellLabel = new JLabel("<html><div style='text-align: center;'>" +
        "Thank you for using the<br>Currency Exchange Program!" +
        "</div></html>");
        farewellLabel.setFont(farewellFont);
        
        JOptionPane.showMessageDialog(null, farewellLabel, "Goodbye", JOptionPane.INFORMATION_MESSAGE);
    }
}