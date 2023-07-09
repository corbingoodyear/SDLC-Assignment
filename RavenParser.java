import java.util.*;
import java.lang.*;
import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Class to parse the poem "The Raven" and provide a list of words and their frequencies
 *
 * @author Corbin Goodyear
 * @version 5/21/2023
 */
public class RavenParser
{
    private static String fetchWebContent(String url) throws IOException {
        String content = "";
        URL urlToRaven = new URL(url);
        URLConnection yc = urlToRaven.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) 
            content += inputLine;
        in.close();
        
        return content;
    }
    /**
     * 
     * @param args for main method
     * @throws IOException for opening file
     */
    public static void main(String[] args) throws IOException {
    	//Sets up UI
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);

        //Creates the MenuBar and adds components
        JMenuBar mb = new JMenuBar();
        

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JLabel label = new JLabel("Enter URL");
        JLabel beg = new JLabel("Enter beginning words");
        JLabel end = new JLabel("Enter last words");

        JTextField tf = new JTextField("Enter URL"); // accepts url
        JTextField tfbeg = new JTextField("Enter first words"); // accepts beginning tokens
        JTextField tfend = new JTextField("Enter last words"); // accepts end tokens

        JButton fetch = new JButton("Fetch Document");
        panel.add(label); // Components Added using Flow Layout
        panel.add(tf);
        panel.add(beg);
        panel.add(tfbeg);
        panel.add(end);
        panel.add(tfend);
        panel.add(fetch);
        //Main Text Area
        JTextArea ta = new JTextArea();

        //Adds Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.setVisible(true);
         //define string to contain full html of poem
        String poem = fetchWebContent("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
          
        //chop off extra material at the beginning (before the title) and after the poem ends,
        //as well as strip out HTML tags and remove a few other random extra words/characters that appear in the poem
        //that shouldn't be included in our word count algorithm
        int beginIndex = poem.indexOf("<h1>The Raven</h1>");
        int endIndex = poem.indexOf("<!--end chapter-->");
        
        poem = poem.substring(beginIndex,endIndex)
            .replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ")
            .replace("“","")
            .replace("&mdash","");
        
        //only uncomment this line for debugging purposes, in order to see the full poem before it is parsed
        //System.out.print(poem);
        
        //create an empty map used to store each word and its frequency
        Map<String, Integer> frequencies = new HashMap<>();
                
        //split the poem into separate words
        String[] words = poem.split("[\\s.;,?:!()\"]+");
        
    	//iterate the words and store frequencies
    	for (String word : words) {
    		word = word.trim();
    		if (word.length() > 0) {
    			if (frequencies.containsKey(word)) {
    				frequencies.put(word, frequencies.get(word) + 1);
    			} else {
    				frequencies.put(word, 1);
    			}
    		}
    	}
        
        //sort wordCounts by frequency
        Map<String, Integer> sortedWordCounts = frequencies.entrySet().stream()
        		.sorted(Collections.reverseOrder(Entry.comparingByValue()))
        		.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        		
        //printing words and frequencies
        System.out.printf("%-20s%15s\n", "WORD", "FREQUENCY");
        System.out.printf("%-20s%15s\n", "~~~~", "~~~~~~~~~");
        for (Map.Entry<String, Integer> entry : sortedWordCounts.entrySet()) {
        	System.out.printf("%-20s%10s\n", entry.getKey(), entry.getValue());
        }
    }
    
    /**
     * Constructor for objects of class RavenParser
     */
    public RavenParser()
    {
        // initialise instance variables
    }
}
