import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


public class MyFrame extends JFrame {
    JButton button1, button2, button3, button4;
    JTextField textField1, textField2, textFieldTrue, textFieldFalse, textField5;
    JProgressBar progressBar;
    JLabel label1, label2, label3;
    JTable textReport;

    int valueProgress = 0;

    Object[] header = {"GUID" , "OrgCode", "OrFKCode" , "Result", "Path file", "Error"};
    Object[][] data = {{}};
    File fileXsd = new File("C:\\Users\\Nika\\Desktop\\Valid\\REF_UBPandNUBP.xsd");
    File[] filesXml;

    public MyFrame() {
        super("Валидатор UBPandNUBP");
        setLayout(new FlowLayout(FlowLayout.LEFT));
        button1 = new JButton("XSD");
        button2 = new JButton("XML");
        button3 = new JButton("Validate start");
        button4 = new JButton("Open report");
        progressBar = new JProgressBar();
        progressBar.setVisible(true);
        progressBar.setStringPainted(true);
        progressBar.setString("0");
        progressBar.setPreferredSize(new Dimension(122, 20));
        textField1 = new JTextField(30);
        textField2 = new JTextField(30);
        textFieldTrue = new JTextField(6);
        textFieldFalse = new JTextField(6);
        textField5 = new JTextField(30);
        label1 = new JLabel("true");
        label2 = new JLabel("false");
        label3 = new JLabel("Report");
        button1.setPreferredSize(new Dimension(110, 25));
        button2.setPreferredSize(new Dimension(110, 25));
        button3.setPreferredSize(new Dimension(110, 25));
        button4.setPreferredSize(new Dimension(110, 25));
        textReport = new JTable(data, header);
        textReport.setVisible(false);
        textReport.setSize(new Dimension(10, 10));
        textField1.setEditable(false);
        textField1.setText(fileXsd.getAbsolutePath());

        add(button1);
        add(textField1);
        add(button2);
        add(textField2);
        add(button3);
        add(progressBar);
        add(label1);
        add(textFieldTrue);
        add(label2);
        add(textFieldFalse);
        add(button4);
        add(textField5);
        add(label3);
        add(textReport);
        add(new JScrollPane(textReport));

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int ret = fileChooser.showDialog(null, "Выбрать XSD схему");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    fileXsd = fileChooser.getSelectedFile();
                    textField1.setText(fileXsd.getAbsolutePath());
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int ret = fileChooser.showDialog(null, "Выбрать XML");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    filesXml = fileChooser.getSelectedFiles();
                    for (File file : filesXml) {
                        if (file.isFile()) {
                            textField2.setText(file.getAbsolutePath());
                        } else if (file.isDirectory())
                            textField2.setText(file.getAbsolutePath());
                    }

                }
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
           checkForXml(fileXsd, filesXml);

            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("report.txt");
                fileChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.getName().endsWith(".txt");
                    }

                    @Override
                    public String getDescription() {
                        return "TXT files";
                    }
                });
                int ret = fileChooser.showDialog(null, "Открыть отчет");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File fileReport = fileChooser.getSelectedFile();
                    try {
                        Runtime.getRuntime().exec("notepad.exe report.txt");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        });

    }
    public void checkForXml(File pathXsd, File... pathXml){
        File fileReport = new File("report.txt");
        int countsStr = 0;
        try {
            FileWriter writer = new FileWriter(fileReport);
            writer.write("GUID;OrgCode;OrgStatus;OrFKCode;Result;Path file;Error\r\n");
            String s = "";
            int countsTr = 0;
            int countsFs = 0;
            if (pathXml[0].isDirectory()) {
                for (File file : pathXml[0].listFiles()) {
                    s = new ValidXML().validXmlforXsd(file, pathXsd);
                    writer.write(s + "\r\n");
                    if (s.contains("true;")){
                        countsTr++;
                    }
                    else
                        countsFs++;
                    countsStr++;
                    valueProgress = pathXml.length;
                }
            }else {
                for (int i = 0; i < pathXml.length; i++) {
                    s = new ValidXML().validXmlforXsd(pathXml[i], pathXsd);
                    writer.write(s + "\r\n");
                    if (s.contains("true;")) {
                        countsTr++;
                    } else
                        countsFs++;
                    countsStr++;
                    valueProgress = pathXml.length;
                }
            }
            textFieldTrue.setText(String.valueOf(countsTr));
            textFieldFalse.setText(String.valueOf(countsFs));
            writer.flush();
            writer.close();
        } catch (Exception ex) {

        }
        textField5.setText(fileReport.getAbsolutePath());

    }
}
