import javax.swing.*;

public class TagExtractor {
    JFileChooser fileChooser = new JFileChooser();
    int returnValue = fileChooser.showOpenDialog(null);
      if (returnValue == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        java.awt.Desktop.getDesktop().open(selectedFile);//<-- here
    }
}
