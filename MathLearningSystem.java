import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import javax.swing.*;

class MathLearningSystem extends JFrame implements ActionListener {
    private JTextField resultField;
    private MathOperation mathOperation;
    private String operation;
    private MathApp parent;
    private String userName;
    private Color answerFieldColor = Color.YELLOW;
    private int answerFontSize = 20;
    private String userAnswer = "";
    private boolean answerChecked = false;

    private JLabel questionLabel;

    public MathLearningSystem(MathApp parent, String operation, String userName) {
        this.parent = parent;
        this.operation = operation;
        this.userName = userName;

        // Initialize mathOperation based on the operation
        switch (operation) {
            case "Addition":
                mathOperation = new AdditionOperation();
                break;
            case "Subtraction":
                mathOperation = new SubtractionOperation();
                break;
            case "Multiplication":
                mathOperation = new MultiplicationOperation();
                break;
            case "Division":
                mathOperation = new DivisionOperation();
                break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + operation);
        }

        setTitle("Math Learning System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        init();
    }

    public void init() {
        // Create components
        JLabel enterAnswerLabel = new JLabel("Enter Your Answer Here:");
        JButton checkButton = new JButton("Check Answer");
        JButton nextButton = new JButton("Next Question");
        resultField = new JTextField(10);

        resultField.setBackground(answerFieldColor);

        resultField.setFont(new Font("Arial", Font.PLAIN, answerFontSize));

        Font questionFont = new Font("Arial", Font.BOLD, 20);
        questionLabel = new JLabel("Question " + parent.getQuestionCount() + ": " + mathOperation.getNum1() + " " + operation + " " + mathOperation.getNum2());
        questionLabel.setFont(questionFont);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        questionPanel.add(questionLabel);

        mainPanel.add(questionPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.add(enterAnswerLabel);
        inputPanel.add(resultField);

        mainPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(checkButton);
        buttonPanel.add(nextButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        checkButton.addActionListener(this);
        nextButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Check Answer")) {
            if (!answerChecked) {
                if (isValidInput(resultField.getText())) {
                    checkAnswer();
                    answerChecked = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "You have already checked the answer.");
            }
        } else if (e.getActionCommand().equals("Next Question")) {
            if (answerChecked && isValidInput(resultField.getText())) {
                answerChecked = false;
		userAnswer = "";
                if (parent.getQuestionCount() < 10) {
                    parent.incrementQuestionCount();
                    mathOperation.generateOperands();
                    updateQuestionLabel();
                } else {
                    JOptionPane.showMessageDialog(this, "You have completed 10 questions for " + operation + ".\nNumber of correct answers: " + parent.getCorrectAnswerCount() + "\nUser: " + userName);
                    parent.resetCounts();
		    saveResultToFile();
                    dispose();
                }
            } else if (!isValidInput(resultField.getText())) {
                JOptionPane.showMessageDialog(this, "Please check the answer with valid input before moving to the next question.");
            } else {
                JOptionPane.showMessageDialog(this, "Please check the answer with valid input before moving to the next question.");
            }
        }
    }

    private boolean isValidInput(String inputValue) {
        try {
            int number = Integer.parseInt(inputValue);
            return number >= 0; // Allow all integers as input
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void updateQuestionLabel() {
        questionLabel.setText("Question " + parent.getQuestionCount() + ": " + mathOperation.getNum1() + " " + operation + " " + mathOperation.getNum2());
        resultField.setText(userAnswer);
    }

    private void checkAnswer() {
        String inputValue = resultField.getText();
	userAnswer = inputValue;
        try {
            int userAnswer = Integer.parseInt(inputValue);
            int correctAnswer = mathOperation.calculateResult();

	    
            // Display in Command Prompt
            System.out.println("Debug: userAnswer = " + userAnswer + ", correctAnswer = " + correctAnswer);


            if (userAnswer == correctAnswer) {
                parent.incrementCorrectAnswerCount(); // Increment
                JOptionPane.showMessageDialog(this, "Correct!");
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect. The correct answer is " + correctAnswer);
            }
	
	    updateQuestionLabel();

            saveResultToFile();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }

    private void saveResultToFile() {
               if (parent.getQuestionCount() == 10) {
                       // Save the final result after completing 10 questions
                       try (BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt", true))) {
                               writer.write("User: " + userName + ", Operation: " + operation + ", Correct Answers: " + parent.getCorrectAnswerCount() + "\n");
                       } catch (IOException e) {
                               e.printStackTrace();
                       }
               }
       }


}