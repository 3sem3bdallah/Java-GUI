import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ATMGUI extends JFrame {
    private static final HashMap<String, Double> accounts = new HashMap<>();
    private static final HashMap<String, String> passwords = new HashMap<>();
    private String currentUser;
    private double currentBalance;
    private StringBuilder transactionHistory = new StringBuilder();
    private JTextArea transactionArea;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea outputArea;
    private JTextField amountField;
    private JPasswordField newPasswordField;

    public ATMGUI() {
        accounts.put("user1", 1000.0);
        passwords.put("user1", "password1");

        setTitle("ATM Simulation");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        amountField = new JTextField(20);
        newPasswordField = new JPasswordField(20);
        outputArea = new JTextArea(12, 30);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        transactionArea = new JTextArea(5, 30);
        transactionArea.setEditable(false);
        transactionArea.setLineWrap(true);
        transactionArea.setWrapStyleWord(true);
        
        JButton loginButton = new JButton("Login");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton changePasswordButton = new JButton("Change Password");
        JButton clearButton = new JButton("Clear Output");
        JButton logoutButton = new JButton("Logout"); 

        loginButton.addActionListener(new LoginAction());
        checkBalanceButton.addActionListener(new CheckBalanceAction());
        depositButton.addActionListener(new DepositAction());
        withdrawButton.addActionListener(new WithdrawAction());
        changePasswordButton.addActionListener(new ChangePasswordAction());
        clearButton.addActionListener(e -> outputArea.setText(""));
        logoutButton.addActionListener(e -> logout()); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Amount:"), gbc);
        
        gbc.gridx = 1;
        add(amountField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(depositButton, gbc);
        
        gbc.gridx = 1;
        add(withdrawButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("New Password:"), gbc);
        
        gbc.gridx = 1;
        add(newPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(changePasswordButton, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(new JScrollPane(outputArea), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        add(checkBalanceButton, gbc);
        
        gbc.gridx = 1;
        add(clearButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        add(logoutButton, gbc); 
        
        gbc.gridx = 0;
        gbc.gridy = 10;
        add(new JLabel("Transaction History:"), gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        add(new JScrollPane(transactionArea), gbc); 

        setVisible(true);
    }

    private void logout() {
        currentUser = null;
        currentBalance = 0;
        outputArea.setText("Logged out successfully.");
        transactionHistory.setLength(0); 
        transactionArea.setText("");
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (!accounts.containsKey(username)) {
                outputArea.setText("Account not found.");
                return;
            }

            if (!passwords.get(username).equals(password)) {
                outputArea.setText("Incorrect password.");
                return;
            }

            currentUser = username;
            currentBalance = accounts.get(username);
            outputArea.setText("Login successful. Your balance is: " + currentBalance);
        }
    }

    private class CheckBalanceAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentUser != null) {
                outputArea.setText("Your current balance is: " + currentBalance);
            } else {
                outputArea.setText("Please login first.");
            }
        }
    }

    private class DepositAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentUser != null) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    currentBalance += amount;
                    accounts.put(currentUser, currentBalance);
                    String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                    transactionHistory.append("Deposited: ").append(amount).append(" at ").append(timeStamp).append("\n");
                    transactionArea.setText(transactionHistory.toString());
                    outputArea.setText("Deposit successful. Your current balance is: " + currentBalance);
                } catch (NumberFormatException ex) {
                    outputArea.setText("Invalid amount. Please enter a number.");
                }
            } else {
                outputArea.setText("Please login first.");
            }
        }
    }

    private class WithdrawAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentUser != null) {
                try {
                    double amount = Double.parseDouble(amountField.getText());
                    if (currentBalance >= amount) {
                        currentBalance -= amount;
                        accounts.put(currentUser, currentBalance);
                        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                        transactionHistory.append("Withdrew: ").append(amount).append(" at ").append(timeStamp).append("\n");
                        transactionArea.setText(transactionHistory.toString());
                        outputArea.setText("Withdrawal successful. Your current balance is: " + currentBalance);
                    } else {
                        outputArea.setText("Insufficient balance.");
                    }
                } catch (NumberFormatException ex) {
                    outputArea.setText("Invalid amount. Please enter a number.");
                }
            } else {
                outputArea.setText("Please login first.");
            }
        }
    }

    private class ChangePasswordAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentUser != null) {
                String newPassword = new String(newPasswordField.getPassword());
                passwords.put(currentUser, newPassword);
                outputArea.setText("Password changed successfully.");
            } else {
                outputArea.setText("Please login first.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATMGUI::new);
    }
}