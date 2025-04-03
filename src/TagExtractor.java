import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TagExtractor extends JFrame {

    private JTextArea textArea;
    private JFileChooser fileChooser;
    private Map<String, Integer> tagFrequencyMap;
    private Set<String> stopWords;

    // Creates the GUI
    public TagExtractor() {
        // Load in stop words & initialize map
        tagFrequencyMap = new HashMap<>();
        stopWords = getStopWords("English Stop Words.txt");

        setTitle("Tag Extractor!");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Declaring GUI elements
        JPanel btnPnl;
        JButton getTagsBtn, saveTagsBtn;
        textArea = new JTextArea(20, 50);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        // Buttons
        getTagsBtn = new JButton("Get tags!");
        saveTagsBtn = new JButton("Save Tags!");

        // Stuff to make buttons do things
        getTagsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseFile();
                getTags();
            }
        });

        saveTagsBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveTags();
            }
        });

        // Add in buttons
        btnPnl = new JPanel();
        btnPnl.setLayout(new GridLayout(1,2));

        btnPnl.add(getTagsBtn);
        btnPnl.add(saveTagsBtn);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(btnPnl, BorderLayout.SOUTH);
    }

    // Reads file itself
    private void processTextFile(File file) {
        tagFrequencyMap.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
            textArea.setText("Tags extracted from: " + file.getName() + " successfully!" + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gets only letter characters, removes stop words, maps frequency
    private void processLine(String line) {
        String[] words = line.split("\\s+");
        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if (!word.isEmpty() && !stopWords.contains(word)) {
                tagFrequencyMap.put(word, tagFrequencyMap.getOrDefault(word, 0) + 1);
            }
        }
    }

    // Reads stop word file
    private Set<String> getStopWords(String fileName) {
        Set<String> stopwords = new HashSet<>();
        try (InputStream inputStream = getClass().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stopwords.add(line.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stopwords;
    }

    // Gets file choice from user
    private void chooseFile() {
        fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            processTextFile(selectedFile);
        }
    }

    // Extracts the tags
    private void getTags() {
        textArea.setText("");

        if (tagFrequencyMap.isEmpty()) {
            textArea.append("Nothing to extract. Open a text file!\n");
        } else {
            textArea.append("Tags and Frequencies:\n");
            for (Map.Entry<String, Integer> entry : tagFrequencyMap.entrySet()) {
                textArea.append(entry.getKey() + ": " + entry.getValue() + "\n");
            }
        }
    }

    // Allows you to save the mapped frequencies to a file
    private void saveTags() {
        if (tagFrequencyMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nothing to save!");
        } else {
            fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selected = fileChooser.getSelectedFile();

                try (PrintWriter writer = new PrintWriter(selected)) {
                    for (Map.Entry<String, Integer> entry : tagFrequencyMap.entrySet()) {
                        writer.println(entry.getKey() + ": " + entry.getValue());
                    }
                    JOptionPane.showMessageDialog(this, "Tags saved to " + selected.getAbsolutePath() + " successfully!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Main!
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TagExtractor gui = new TagExtractor();
            gui.setVisible(true);
        });
    }
}

