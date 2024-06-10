import java.io.Serializable;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Student implements Serializable {
    private String name;
    private String rollNumber;
    private String grade;

    public Student(String name, String rollNumber, String grade) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return  "Name='" + name + '\'' +
                ", RollNumber='" + rollNumber + '\'' +
                ", Grade='" + grade + '\'';
    }
}

public class StudentManagementSystem {
    private List<Student> students;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        loadStudents();
    }

    public void addStudent(Student student) {
        students.add(student);
        saveStudents();
    }

    public void removeStudent(String rollNumber) {
        students.removeIf(student -> student.getRollNumber().equals(rollNumber));
        saveStudents();
    }

    public Student searchStudent(String rollNumber) {
        for (Student student : students) {
            if (student.getRollNumber().equals(rollNumber)) {
                return student;
            }
        }
        return null;
    }

    public List<Student> getAllStudents() {
        return students;
    }

    private void saveStudents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("students.dat"))) {
            oos.writeObject(students);
        } catch (IOException e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
    }

    private void loadStudents() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("students.dat"))) {
            students = (List<Student>) ois.readObject();
        } catch (FileNotFoundException e) {
            // Ignore, file will be created when saving
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading student data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementSystem::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Student Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        StudentManagementSystem sms = new StudentManagementSystem();

        // Create the tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Add Student Panel
        JPanel addPanel = new JPanel(new GridLayout(4, 2));
        JTextField nameField = new JTextField();
        JTextField rollNumberField = new JTextField();
        JTextField gradeField = new JTextField();
        JButton addButton = new JButton("Add Student");

        addPanel.add(new JLabel("Name:"));
        addPanel.add(nameField);
        addPanel.add(new JLabel("Roll Number:"));
        addPanel.add(rollNumberField);
        addPanel.add(new JLabel("Grade:"));
        addPanel.add(gradeField);
        addPanel.add(new JLabel());
        addPanel.add(addButton);

        tabbedPane.addTab("Add Student", addPanel);

        // Remove Student Panel
        JPanel removePanel = new JPanel(new BorderLayout());
        JTextField removeField = new JTextField();
        JButton removeButton = new JButton("Remove Student");

        removePanel.add(new JLabel("Roll Number:"), BorderLayout.NORTH);
        removePanel.add(removeField, BorderLayout.CENTER);
        removePanel.add(removeButton, BorderLayout.SOUTH);

        tabbedPane.addTab("Remove Student", removePanel);

        // Search Student Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search Student");
        JTextArea searchResultArea = new JTextArea();
        searchResultArea.setEditable(false);

        searchPanel.add(new JLabel("Roll Number:"), BorderLayout.NORTH);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(new JScrollPane(searchResultArea), BorderLayout.EAST);
        searchPanel.add(searchButton, BorderLayout.SOUTH);
       

        tabbedPane.addTab("Search Student", searchPanel);

        // Display All Students Panel
        JPanel displayPanel = new JPanel(new BorderLayout());
        JTextArea displayArea = new JTextArea();
        displayArea.setEditable(false);
        JButton displayButton = new JButton("Display All Students");

        displayPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        displayPanel.add(displayButton, BorderLayout.SOUTH);

        tabbedPane.addTab("Display All Students", displayPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String rollNumber = rollNumberField.getText();
                String grade = gradeField.getText();
                if (name.isEmpty() || rollNumber.isEmpty() || grade.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    sms.addStudent(new Student(name, rollNumber, grade));
                    JOptionPane.showMessageDialog(frame, "Student added successfully.");
                    nameField.setText("");
                    rollNumberField.setText("");
                    gradeField.setText("");
                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rollNumber = removeField.getText();
                sms.removeStudent(rollNumber);
                JOptionPane.showMessageDialog(frame, "Student removed successfully.");
                removeField.setText("");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String rollNumber = searchField.getText();
                Student student = sms.searchStudent(rollNumber);
                if (student != null) {
                    searchResultArea.setText(student.toString());
                } else {
                    searchResultArea.setText("Student not found.");
                }
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Student> allStudents = sms.getAllStudents();
                displayArea.setText("");
                for (Student student : allStudents) {
                    displayArea.append(student.toString() + "\n");
                }
            }
        });

        frame.setVisible(true);
    }
}
