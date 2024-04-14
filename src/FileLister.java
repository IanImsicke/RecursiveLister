import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;

public class FileLister extends JFrame
{
    private static FileLister fileLister;
    private Path selectedDirectory;
    private JOptionPane optionPane;
    private JPanel mainPanel;
    private JTextArea outputArea;
    private JScrollPane outputScroller;
    private JButton chooseButton;
    private JButton runButton;
    private JButton quitButton;

    public static void main(String[] args)
    {
        fileLister = new FileLister();
    }
    public FileLister()
    {
        optionPane = new JOptionPane();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4,1));
        chooseButton = new JButton("Choose a directory");
        chooseButton.addActionListener(e ->
        {
            selectedDirectory = ChooseDirectory();
        });
        runButton = new JButton("Run");
        runButton.addActionListener(e ->
        {
            if (selectedDirectory != null)
            {
                try
                {
                    outputArea.setText("");
                    ListFiles(selectedDirectory);
                }
                catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
            else
            {
                optionPane.showInternalMessageDialog(null, "Missing Directory Selection",
                        "Missing Directory Selection", JOptionPane.INFORMATION_MESSAGE);
            }

        });
        quitButton = new JButton("Quit");
        quitButton.addActionListener(e ->
        {
            System.exit(0);
        });
        outputArea = new JTextArea(20, 20);
        outputScroller = new JScrollPane(outputArea);
        mainPanel.add(chooseButton);
        mainPanel.add(runButton);
        mainPanel.add(outputScroller);
        mainPanel.add(quitButton);
        add(mainPanel);
        BuildWindow();
    }
    private void BuildWindow()
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setTitle("RecursiveLister");
        setSize((screenWidth / 4) * 3, screenHeight);
        setLocation(screenWidth / 8, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    private Path ChooseDirectory()
    {
        Path chosenDirectory = null;
        JFileChooser chooser = new JFileChooser();
//        try
//        {
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            chosenDirectory = chooser.getSelectedFile().toPath();
            optionPane.showInternalMessageDialog(null, "Directory selected: " + chosenDirectory,
                    "File info", JOptionPane.INFORMATION_MESSAGE);
        }
//        }
//        catch (FileNotFoundException e)
//        {
//            optionPane.showInternalMessageDialog(null, "File not found!",
//                    "Error", JOptionPane.INFORMATION_MESSAGE);
//            e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
        return chosenDirectory;
    }
    private void ListFiles(Path selectedDirectory) throws IOException
    {
        for (Path item : Files.newDirectoryStream(selectedDirectory))
        {
            if (Files.isDirectory(item))
            {
                outputArea.append("dir: " + item.getFileName().toString() + "\n");
                ListFiles(item);
            }
            else
            {
                outputArea.append("file: " + item.getFileName().toString() + "\n");
            }
        }
    }
}