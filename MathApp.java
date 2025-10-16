import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.swing.*;

abstract class MathOperation {
    protected int num1;
    protected int num2;
    protected Random random = new Random();

    public MathOperation() {
        generateOperands();
    }

    protected abstract void generateOperands();
    public abstract int calculateResult();

    public int getNum1() {
        return num1;
    }

    public int getNum2() {
        return num2;
    }
}

class AdditionOperation extends MathOperation {
    // Override
    protected void generateOperands() {
        num1 = random.nextInt(35);
        num2 = random.nextInt(35);
    }

    // Override
    public int calculateResult() {
        return num1 + num2;
    }
}

class SubtractionOperation extends MathOperation {
    // Override
    protected void generateOperands() {
        num1 = random.nextInt(30);
        num2 = random.nextInt(num1 + 1);
    }

    // Override
    public int calculateResult() {
        return num1 - num2;
    }
}

class MultiplicationOperation extends MathOperation {
    // Override
    protected void generateOperands() {
        num1 = random.nextInt(13);
        num2 = random.nextInt(13);
    }

    // Override
    public int calculateResult() {
        return num1 * num2;
    }
}

class DivisionOperation extends MathOperation {
    // Override
    protected void generateOperands() {
        num1 = random.nextInt(30);
        num2 = getRandomNonZeroForDivision();
    }

    private int getRandomNonZeroForDivision() {
        int randomNum;
        do {
            randomNum = random.nextInt(30);
        } while (randomNum == 0 || num1 % randomNum !=0);
        return randomNum;
    }

    // Override
    public int calculateResult() {
        return num1 / num2;
    }
}

public class MathApp extends JFrame implements ActionListener {
    private int questionCount;
    private int correctAnswerCount;
    private JButton addition, subtraction, multiplication, division;
    private String userName;
    private Color customBackgroundColor = Color.CYAN;
    private JTextField nameField;
    private JButton startButton;
    private String userAnswer;

    public MathApp() {
        // Initialize components
        getContentPane().setBackground(Color.BLACK);
        JLabel welcome1 = new JLabel("WELCOME TO");
        JLabel welcome2 = new JLabel("CHILDREN MATH LEARNING SYSTEM");
        JLabel pattern1 = new JLabel("+");
        JLabel pattern2 = new JLabel("-");
        JLabel pattern3 = new JLabel("*");
        JLabel pattern4 = new JLabel("/");
        JLabel nameLabel = new JLabel("Enter your name:");
        nameField = new JTextField(20);
        startButton = new JButton("Start");

        Font w1 = new Font("Georgia", Font.BOLD, 30);
        Font w2 = new Font("Georgia", Font.BOLD, 20);
        Font p1 = new Font("Arial", Font.BOLD, 120);
        Font p2 = new Font("Arial", Font.BOLD, 200);
        Font p3 = new Font("Arial", Font.BOLD, 150);
        Font p4 = new Font("Arial", Font.BOLD, 120);
        welcome1.setFont(w1);
        welcome1.setForeground(Color.PINK);
        welcome2.setFont(w2);
        welcome2.setForeground(Color.PINK);
        pattern1.setFont(p1);
        pattern1.setForeground(Color.yellow);
        pattern2.setFont(p2);
        pattern2.setForeground(Color.MAGENTA);
        pattern3.setFont(p3);
        pattern3.setForeground(Color.RED);
        pattern4.setFont(p4);
        pattern4.setForeground(Color.green);
        nameLabel.setForeground(Color.LIGHT_GRAY);

        welcome1.setBounds(135, 10, 1300, 30);
        welcome2.setBounds(40, 40, 600, 50);
        pattern1.setBounds(40, 150, 300, 120);
        pattern2.setBounds(160, 140, 300, 200);
        pattern3.setBounds(300, 140, 300, 150);
        pattern4.setBounds(400, 160, 300, 120);
        nameLabel.setBounds(50, 100, 100, 20);
        nameField.setBounds(160, 100, 200, 20);
        startButton.setBounds(160, 140, 100, 20);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                userName = nameField.getText();

                if (isValidUserName(userName)) {
                    saveUserName(userName);
                    showMathOperations();
                } else {
                    JOptionPane.showMessageDialog(MathApp.this, "Invalid name. Please enter only alphabets.");
                }
            }
        });

        startButton.setBackground(Color.CYAN);

        add(welcome1);
        add(welcome2);
        add(pattern1);
        add(pattern2);
        add(pattern3);
        add(pattern4);
        add(nameLabel);
        add(nameField);
        add(startButton);
        setLayout(null);
        questionCount = 0;
        correctAnswerCount = 0;

        setTitle("Math App");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen
        setResizable(false);
        setVisible(true);
    }

    private void showMathOperations() {
        getContentPane().setBackground(customBackgroundColor);
        // Remove the name input components
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JLabel || component instanceof JTextField || component instanceof JButton) {
                getContentPane().remove(component);
            }
        }
        getContentPane().validate();

        JLabel welcome3 = new JLabel("PLEASE CLICK ANY ONE !!!");
        Font w3 = new Font("Times New Roman", Font.BOLD, 20);
        welcome3.setFont(w3);
        welcome3.setBounds(120, 30, 300, 20);
        addition = new JButton("Addition");
        addition.setBounds(80, 100, 120, 20);
        subtraction = new JButton("Subtraction");
        subtraction.setBounds(290, 100, 120, 20);
        multiplication = new JButton("Multiplication");
        multiplication.setBounds(80, 200, 120, 20);
        division = new JButton("Division");
        division.setBounds(290, 200, 120, 20);

        addition.addActionListener(this);
        subtraction.addActionListener(this);
        multiplication.addActionListener(this);
        division.addActionListener(this);

        addition.setBackground(Color.YELLOW);
        subtraction.setBackground(Color.MAGENTA);
        subtraction.setForeground(Color.WHITE);
        multiplication.setBackground(Color.RED);
        multiplication.setForeground(Color.WHITE);
        division.setBackground(Color.GREEN);

        add(addition);
        add(subtraction);
        add(multiplication);
        add(division);
        setLayout(null);
        add(welcome3);

        questionCount = 1;
    }

    private void saveUserName(String name) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("username.txt"))) {
            writer.write(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserName() {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("username.txt");
            userName = new String(java.nio.file.Files.readAllBytes(path)).trim();
            showMathOperations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidUserName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addition || e.getSource() == subtraction || e.getSource() == multiplication || e.getSource() == division) {
            String operation = e.getActionCommand();

            MathLearningSystem mathLearningSystem = new MathLearningSystem(this, operation, userName);
            mathLearningSystem.setVisible(true);
        }
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void incrementQuestionCount() {
        questionCount++;
    }

    public int getCorrectAnswerCount() {
        return correctAnswerCount;
    }

    public void incrementCorrectAnswerCount() {
        correctAnswerCount++;
    }

    public void resetCounts() {
        questionCount = 1;
        correctAnswerCount = 0;
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MathApp mathApp = new MathApp();
            mathApp.loadUserName();
        });
    }
}

