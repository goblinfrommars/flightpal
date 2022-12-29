package com.flightpalfx.AdminArea;

import com.connection.JDBC;
import com.flightpalfx.User;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;

public class EditUser extends JDBC {
  private JTextField editUserUsernameField;
  private JTextField editUserPasswordField;
  private JTextField editUserLevelField;
  private JTextField editUserFullNameField;
  private JTextField editUserEmailField;
  private JTextField editUserBalanceField;
  private JButton cancelButton;
  private JButton updateButton;
  private JPanel editUserPanel;
  JFrame editUserFrame;
  JTable getManageUsersTable;

  public EditUser(User updateUser, JTable manageUsersTable) {
    this.getManageUsersTable = manageUsersTable;
    editUserFrame = new JFrame("Edit User");
    editUserFrame.setContentPane(editUserPanel);
    editUserFrame.setLocationRelativeTo(null);
    editUserFrame.pack();
    editUserFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    editUserFrame.setVisible(true);
    setTextField(updateUser);

    // when update click
    updateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String newUsername = editUserUsernameField.getText();
        String newPassword = editUserPasswordField.getText();
        String newLevel = editUserLevelField.getText();
        String newFullName = editUserFullNameField.getText();
        String newEmail = editUserEmailField.getText();
        String newBalance = editUserBalanceField.getText();

        boolean isUpdateUser = updateUserCheck(newUsername, newPassword, newLevel, newFullName, newEmail, newBalance);
        if (isUpdateUser) {
          updateUserToDB(newUsername, newPassword, newLevel, newFullName, newEmail, newBalance);
        }
      }

      private void updateUserToDB(String newUsername, String newPassword, String newLevel, String newFullName,String newEmail, String newBalance) {
        boolean isUsernameEx = true;

        // remove old user
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "DELETE FROM users WHERE user_id = ?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, updateUser.id);
          pstmt.execute();
          conn.close();
        } catch (Exception e) {e.printStackTrace();}

        // check if user exist
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          stmt = conn.createStatement();
          String sql = "SELECT username FROM users WHERE username = '" + newUsername + "' ";
          rs = stmt.executeQuery(sql);
          if (rs.next()) {
            showMessageDialog("the user is already registered");
            isUsernameEx = false;
          }
          conn.close();
        } catch (Exception e) {e.printStackTrace();}

        // insert new user
        if(isUsernameEx) {
          try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String sql = "INSERT INTO users(user_id, username, password, level, fullname, email, balance) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, updateUser.id);
            pstmt.setString(2, newUsername);
            pstmt.setString(3, newPassword);
            pstmt.setInt(4, Integer.parseInt(newLevel));
            pstmt.setString(5, newFullName);
            pstmt.setString(6, newEmail);
            pstmt.setInt(7, Integer.parseInt(newBalance));
            pstmt.execute();
            showMessageDialog("user update successfully");
            editUserFrame.dispose();
            loadUsersTable();

            conn.close();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }

      private boolean updateUserCheck(String newUsername, String newPassword, String newLevel, String newFullName, String newEmail, String newBalance) {
        boolean isNumber = true;
        try {
          int numberLevel = Integer.parseInt(newLevel);
          int numberBalance = Integer.parseInt(newBalance);
        } catch (NumberFormatException e) {
          isNumber = false;
        }

        if (newUsername.isEmpty() || newPassword.isEmpty() || newLevel.isEmpty() || newFullName.isEmpty() || newEmail.isEmpty() || newBalance.isEmpty()) {
          showMessageDialog("field cannot be empty");
        }

        if(!isNumber) {
          showMessageDialog("the level and balance areas must be numeric");
          return false;
        }

        if (!((Integer.parseInt(newLevel)) == 1) && !((Integer.parseInt(newLevel)) == 2)) {
          showMessageDialog("area level must be number 1 (admin) or 2 (user)");
          return false;
        }

        return true;
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editUserFrame.dispose();
      }
    });
  }

  private void setTextField(User updateUser) {
    editUserUsernameField.setText(updateUser.username);
    editUserPasswordField.setText(updateUser.password);
    editUserLevelField.setText(String.valueOf(updateUser.level));
    editUserFullNameField.setText(updateUser.fullname);
    editUserEmailField.setText(updateUser.email);
    editUserBalanceField.setText(String.valueOf(updateUser.balance));
  }

  // Mesage Dialog
  private void showMessageDialog(String msg) {
    JOptionPane.showMessageDialog(editUserFrame, msg);
  }

  private void loadUsersTable() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "SELECT * FROM users";
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      getManageUsersTable.setModel(DbUtils.resultSetToTableModel(rs));
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }
}
