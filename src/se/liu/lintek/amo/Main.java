package se.liu.lintek.amo;

import com.google.gson.Gson;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;

public class Main {

    final static Object[] options = {"Yes, please", "No, thanks"};

    public static void main(String[] args) {
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        JPanel panel = new JPanel(new MigLayout("", "[][fill, grow][fill, grow]", "[][][][][][fill, grow]"));
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(true);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);

        StudentsList students = new StudentsList();

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
        JLabel ipLabel = new JLabel("IP-adress:");
        panel.add(ipLabel, "cell 0 3");
        JLabel passwordLabel = new JLabel("Lösenord:");
        panel.add(passwordLabel, "cell 0 4");

        MultiLineLabel statusLabel = new MultiLineLabel("Status");
        panel.add(statusLabel, "cell 0 5, span 3 1");

        JTextField liuidTextField = new JTextField();
        panel.add(liuidTextField, "cell 1 0,");

        JFormattedTextField clothesTextField = new JFormattedTextField(formatter);
        clothesTextField.setValue(0);
        panel.add(clothesTextField, "cell 1 1 ");
        JFormattedTextField otherTextField = new JFormattedTextField(formatter);
        otherTextField.setValue(0);
        panel.add(otherTextField, "cell 1 2");
        JFormattedTextField ipTextField = new JFormattedTextField(formatter);
        otherTextField.setValue(0);
        panel.add(ipTextField, "cell 1 3");
        JFormattedTextField passwordTextField = new JFormattedTextField(formatter);
        otherTextField.setValue(0);
        panel.add(passwordTextField, "cell 1 4");

        Button addButton = new Button("Add");
        panel.add(addButton, "cell 2 1");
        Button removeButton = new Button("Remove");
        panel.add(removeButton, "cell 2 2");

        JLabel buttonStatusLabel = new JLabel("");
        panel.add(buttonStatusLabel, "cell 2 3");

        liuidTextField.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void removeUpdate(DocumentEvent e)
            {
                buttonStatusLabel.setText("");
                statusLabel.setText(students.getStatus(liuidTextField.getText()));
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                buttonStatusLabel.setText("");
                statusLabel.setText(students.getStatus(liuidTextField.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {} // Not needed for plain-text fields
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int clothes;
                int other;
                String student;
                try {
                    clothes = Integer.parseInt(clothesTextField.getText());
                    other = Integer.parseInt(otherTextField.getText());
                    student = liuidTextField.getText();

                } catch (NumberFormatException e) {
                    buttonStatusLabel.setText("Add failure");
                    return;
                }
                if (students.addValues(student, clothes, other)) {
                    buttonStatusLabel.setText("Add success");
                }
                else if (!students.contains(student) && StudentsList.isLiuId(student)) {
                    int toAdd = JOptionPane.showOptionDialog(frame,
                            "Do you want to add new student "
                                    + student + "?",
                            "New student",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[1]);
                    if (toAdd == 0) {
                        students.addStudent(student, clothes, other);
                        buttonStatusLabel.setText("Add success");
                    }
                }
                else {
                    buttonStatusLabel.setText("Add failure");
                }
                Gson obj = new Gson();
                String json = obj.toJson(students);
                System.out.println(json);
                try {
                    Files.write(Paths.get("students.json"), json.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                statusLabel.setText(students.getStatus(liuidTextField.getText()));
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int clothes;
                int other;
                String student;
                try {
                    clothes = Integer.parseInt(clothesTextField.getText());
                    other = Integer.parseInt(otherTextField.getText());
                    student = liuidTextField.getText();
                } catch (NumberFormatException e) {
                    buttonStatusLabel.setText("Remove failure");
                    return;
                }
                if (students.removeValues(student, clothes, other)) {
                    buttonStatusLabel.setText("Remove success");
                }
                else if (!students.contains(student) && StudentsList.isLiuId(student)) {
                    int toAdd = JOptionPane.showOptionDialog(frame,
                            "Do you want to add new student "
                                    + student + "?",
                            "New student",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[1]);
                    if (toAdd == 0) {
                        students.addStudent(student, -clothes, -other);
                        buttonStatusLabel.setText("Remove success");
                    }
                }
                else {
                    buttonStatusLabel.setText("Remove failure");
                }
                statusLabel.setText(students.getStatus(liuidTextField.getText()));
            }
        });

        frame.getContentPane().add(BorderLayout.CENTER, panel);

        frame.setVisible(true);
    }
}
