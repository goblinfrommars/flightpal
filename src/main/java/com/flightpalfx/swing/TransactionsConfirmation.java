package com.flightpalfx.swing;

import com.connection.JDBC;
import com.flightpalfx.Tickets;
import com.flightpalfx.Transactions;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;

public class TransactionsConfirmation extends JDBC {
  private JTextField tcFullNameField;
  private JTextField tcIdCardField;
  private JTextField tcPassportField;
  private JTextField tcEmailField;
  private JPanel transactionConfirmationPanel;
  private JLabel transactionConfBalanceLabel;
  private JButton transactionConfirmBtn;
  private JButton transactionCancelBtn;

  String id, username, password, fullname, email;
  int balance, level;

  JFrame transactionsConfirmationFrame;
  Stage userMenuStage;
  javafx.event.ActionEvent event;


  public TransactionsConfirmation(String id, String username, String password, int level, String fullname, String email, int balance, Tickets newTicket, JFrame chooseTicketFrame, Stage userMenuStage, javafx.event.ActionEvent event) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.level = level;
    this.fullname = fullname;
    this.email = email;
    this.balance = balance;
    this.userMenuStage = userMenuStage;
    this.event = event;

    transactionsConfirmationFrame = new JFrame("Transaction Confirmation");
    transactionsConfirmationFrame.setContentPane(transactionConfirmationPanel);
    transactionsConfirmationFrame.setLocationRelativeTo(null);
    transactionsConfirmationFrame.setSize(600, 500);
    transactionsConfirmationFrame.setVisible(true);
    setTransactionBalance(balance);

    transactionCancelBtn.addActionListener(e -> transactionsConfirmationFrame.dispose());
    transactionConfirmBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String tcFullName = tcFullNameField.getText();
        String tcIdCard = tcIdCardField.getText();
        String tcPassport = tcPassportField.getText();
        String tcEmail = tcEmailField.getText();

        Transactions transactions = new Transactions(tcFullName, tcIdCard, tcPassport, tcEmail);

        if (tcFullName.isEmpty() || tcIdCard.isEmpty() || tcPassport.isEmpty() || tcEmail.isEmpty()) {
          showMessageDialog("Field can't be empty");
        } else {
          confirmationDioalog(newTicket, chooseTicketFrame, transactions);
        }
      }
      private void confirmationDioalog(Tickets newTicket, JFrame chooseTicketFrame, Transactions transactions) {
        int input = JOptionPane.showConfirmDialog(transactionsConfirmationFrame,
                "Buy this ticket?\n" +
                        "*Ticket id: " + newTicket.id + "\n" +
                        "*From:  " + newTicket.origin + " To: " + newTicket.destination + "\n" +
                        "*Date: " + newTicket.date + "\n" +
                        "*Departure Time: " + newTicket.departureTime + "\n" +
                        "*Arrives Time: " + newTicket.arrivesTime + "\n" +
                        "*Class: " + newTicket.classType + "\n" +
                        "*PRICE: " + newTicket.price, "Are you sure?",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (input == 0) {
          updateTicketFromDb(newTicket);
          updateUserBalance();
          updateTransaction(id, transactions, newTicket);
          String msg = "Your Ticket has been sent to: " + transactions.email;
          showMessageDialog(msg);
          chooseTicketFrame.dispose();
          transactionsConfirmationFrame.dispose();
        }
      }

      private void updateTransaction(String id, Transactions transactions, Tickets newTicket) {
        String unique = "TR" + RandomStringUtils.randomNumeric(3);
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "INSERT INTO transactions (transaction_id, user_id, ticket_id, full_name, card_id, passport, email)" +
                  "VALUES(?, ?, ?, ?, ?, ?, ?)";

          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, unique);
          pstmt.setString(2, id);
          pstmt.setString(3, newTicket.id);
          pstmt.setString(4, transactions.fullName);
          pstmt.setString(5, transactions.idCard);
          pstmt.setString(6, transactions.passport);
          pstmt.setString(7, transactions.email);

          pstmt.execute();
          conn.close();
        }catch (Exception e) {e.printStackTrace();}
      }

      private void updateUserBalance() {
        int newBalance = balance - newTicket.price;
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "UPDATE users SET balance=" + newBalance + " WHERE user_id='" + id + "' ";
          stmt = conn.createStatement();
          stmt.executeUpdate(sql);
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
      }
      private void updateTicketFromDb(Tickets newTicket) {
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "UPDATE tickets SET user_id='" + id + "' WHERE ticket_id='" + newTicket.id + "' ";
          stmt = conn.createStatement();
          stmt.executeUpdate(sql);
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
      }
      private void showMessageDialog(String msg) {
        JOptionPane.showMessageDialog(transactionsConfirmationFrame, msg);
      }
    });
  }

  private void setTransactionBalance(int balance) {
    transactionConfBalanceLabel.setText("Your Balance: Rp. " + balance);
  }
}
