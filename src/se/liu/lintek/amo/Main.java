package se.liu.lintek.amo;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;

public class Main {

    final static Object[] OPTIONS = {"Ja", "Nej"};
    final static String PATH = "students.json";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        JPanel panel = new JPanel(new MigLayout("", "[][fill, grow][fill, grow]", "[][][][][fill, grow]"));
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(true);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);

        StudentList students = new StudentList();


        try {
            students.importStudents(PATH);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Could not import database");
        }

        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        frame.getContentPane().add(BorderLayout.NORTH, mb);

        JLabel idLabel = new JLabel("LiUID:");
        panel.add(idLabel, "cell 0 0");
        JLabel clothesLabel = new JLabel("Kläder:");
        panel.add(clothesLabel, "cell 0 1");
        JLabel otherLabel = new JLabel("Övrigt:");
        panel.add(otherLabel, "cell 0 2");

        JLabel statusLabel = new JLabel("Status");
        panel.add(statusLabel, "cell 0 4, span 3 1");

        JTextField liuidTextField = new JTextField();
        panel.add(liuidTextField, "cell 1 0,");

        JFormattedTextField clothesTextField = new JFormattedTextField(formatter);
        clothesTextField.setValue(0);
        panel.add(clothesTextField, "cell 1 1 ");
        JFormattedTextField otherTextField = new JFormattedTextField(formatter);
        otherTextField.setValue(0);
        panel.add(otherTextField, "cell 1 2");

        Button addButton = new Button("Lägg till");
        panel.add(addButton, "cell 2 1");
        Button removeButton = new Button("Ta bort");
        panel.add(removeButton, "cell 2 2");

        JLabel buttonStatusLabel = new JLabel("");
        panel.add(buttonStatusLabel, "cell 0 3, span 3 1");

        liuidTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                buttonStatusLabel.setText("");
                setStatusText(liuidTextField, students, statusLabel);
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                buttonStatusLabel.setText("");
                setStatusText(liuidTextField, students, statusLabel);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {} // Not needed for plain-text fields
        });

        addButton.addActionListener(actionEvent -> modButtonPress(
                clothesTextField,
                otherTextField,
                liuidTextField,
                buttonStatusLabel,
                students,
                frame,
                statusLabel,
                "Lyckades lägga till",
                "Lyckades inte lägga till",
                false));

        removeButton.addActionListener(actionEvent -> modButtonPress(
                clothesTextField,
                otherTextField,
                liuidTextField,
                buttonStatusLabel,
                students,
                frame,
                statusLabel,
                "Lyckades ta bort",
                "Lyckades inte ta bort",
                true));

        frame.getContentPane().add(BorderLayout.CENTER, panel);

        frame.setVisible(true);

        /*
        for (int i = 0; i < 1000; i++) {
            String tempstudent = "aaaaa" + String.format("%03d", i);
            students.addStudent(tempstudent, i, 1000 - i);
        }
        */
    }

    private static void modButtonPress(
            JFormattedTextField clothesTextField,
            JFormattedTextField otherTextField,
            JTextField liuidTextField,
            JLabel buttonStatusLabel,
            StudentList students,
            JFrame frame,
            JLabel statusLabel,
            String success,
            String failure,
            boolean remove) {

        int clothes;
        int other;
        String student;
        try {
            clothes = Integer.parseInt(clothesTextField.getText());
            other = Integer.parseInt(otherTextField.getText());
            student = liuidTextField.getText();

        } catch (NumberFormatException e) {
            buttonStatusLabel.setText(failure);
            return;
        }
        if (remove && students.removeValues(student, clothes, other)){
            buttonStatusLabel.setText(success);
        }
        else if (!remove && students.addValues(student, clothes, other)) {
            buttonStatusLabel.setText(success);
        }
        else if (!students.contains(student) && StudentList.isLiuId(student)) {
            clothes = remove ? -clothes : clothes;
            other = remove ? -other : other;
            addStudentDialog(clothes, other, student, frame, students, buttonStatusLabel, success);
        }
        else {
            buttonStatusLabel.setText(failure);
            return;
        }
        String json = students.getJson();
        WriteJsonFile(json);
        setStatusText(liuidTextField, students, statusLabel);

    }

    private static void setStatusText(JTextField liuidTextField, StudentList students, JLabel statusLabel) {
        statusLabel.setText(convertToMultiline(students.getStatus(liuidTextField.getText())));
    }

    private static void WriteJsonFile(String json) {
        try {
            Files.write(Paths.get(PATH), json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addStudentDialog(int clothes, int other, String student, JFrame frame, StudentList students, JLabel buttonStatusLabel, String message) {
        int toAdd = JOptionPane.showOptionDialog(frame,
                "Vill du lägga till en ny student "
                        + student + "?",
                "Ny student",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                OPTIONS,
                OPTIONS[1]);
        if (toAdd == 0) {
            students.addStudent(student, clothes, other);
            buttonStatusLabel.setText(message);
        }
    }

    private static String convertToMultiline(String orig)
    {
        return "<html>" + orig.replaceAll("\n", "<br>");
    }
}
