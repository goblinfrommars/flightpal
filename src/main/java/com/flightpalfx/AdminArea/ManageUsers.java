package com.flightpalfx.AdminArea;

import com.connection.JDBC;
import com.flightpalfx.User;
import net.proteanit.sql.DbUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.DriverManager;

public class ManageUsers extends JDBC {
  private JTable manageUsersTable;
  private JTextField manageUserUsernameField;
  private JTextField manageUserPassField;
  private JTextField manageUserLevelField;
  private JTextField manageUserFullNameField;
  private JTextField manageUserEmailField;
  private JButton exitButton;
  private JButton addUserButton;
  private JButton editUserButton;
  private JButton removeUserButton;
  private JTextField manageUserBalanceField;
  private JPanel manageUsersPanel;
  JFrame manageUsersFrame;

  String userId, username, password, fullname, email;
  int level, balance;


  String loginUserId;

  // con
  public ManageUsers(String getUserId) {
    this.loginUserId = getUserId;
    manageUsersFrame = new JFrame("Users Manager");
    manageUsersFrame.setContentPane(manageUsersPanel);
    manageUsersFrame.setLocationRelativeTo(null);
    manageUsersFrame.setSize(900, 500);
    manageUsersFrame.setVisible(true);
    loadUsersTable();


    // add user
    addUserButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = manageUserUsernameField.getText();
        String password = manageUserPassField.getText();
        String level = manageUserLevelField.getText();
        String fullName = manageUserFullNameField.getText();
        String email = manageUserEmailField.getText();
        String balance = manageUserBalanceField.getText();
        User newUser = new User(username, password, level, fullName, email, balance);

        boolean isAddUser = addUserCheck(newUser);
        if (isAddUser) {
          addUserToDb(newUser);
          loadUsersTable();
        }
      }

      private boolean addUserCheck(User newUser) {
        boolean existUser = true;
        boolean levelBalanceIsNumber = true;
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          stmt = conn.createStatement();
          String sql = "SELECT username FROM users WHERE username = '" + newUser.username + "' ";
          rs = stmt.executeQuery(sql);
          if (rs.next())
            existUser = false;
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
        try {
          int valueLevel = Integer.parseInt(newUser.levelField);
          int valueBalance = Integer.parseInt(newUser.balanceField);
        } catch (NumberFormatException e) {levelBalanceIsNumber = false;}

        if (newUser.username.isEmpty() || newUser.password.isEmpty() || newUser.levelField.isEmpty() || newUser.fullname.isEmpty() || newUser.email.isEmpty() || newUser.balanceField.isEmpty()) {
          showMessageDialog("field cannot be empty");
          return false;
        }
        if (!existUser) {
          showMessageDialog("username has been registered");
          return false;
        }
        if (!levelBalanceIsNumber) {
          showMessageDialog("the level and balance areas must be numeric");
          return false;
        }
        if (newUser.password.length() < 8) {
          showMessageDialog("password character length of at least 8 characters");
          return false;
        }
        if (!((Integer.parseInt(newUser.levelField)) == 1) && !((Integer.parseInt(newUser.levelField)) == 2)) {
          showMessageDialog("area level must be number 1 (admin) or 2 (user)");
          return false;
        }
        return true;
      }
      private void addUserToDb(User newUser) {
        String unque = "US" + RandomStringUtils.randomNumeric(3);
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "INSERT INTO users (user_id, username, password, level, fullname, email, balance) " +
                  "VALUES(?, ?, ?, ?, ?, ?, ?)";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, unque);
          pstmt.setString(2, newUser.username);
          pstmt.setString(3, newUser.password);
          pstmt.setInt(4, Integer.parseInt(newUser.levelField));
          pstmt.setString(5, newUser.fullname);
          pstmt.setString(6, newUser.email);
          pstmt.setInt(7, Integer.parseInt(newUser.balanceField));
          pstmt.execute();
          showMessageDialog("user has been added successfully");
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
      }


    });

    // when table click
    manageUsersTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        DefaultTableModel table = (DefaultTableModel) manageUsersTable.getModel();
        userId = table.getValueAt(manageUsersTable.getSelectedRow(), 0).toString();
        username = table.getValueAt(manageUsersTable.getSelectedRow(), 1).toString();
        password = table.getValueAt(manageUsersTable.getSelectedRow(), 2).toString();
        level = Integer.parseInt(table.getValueAt(manageUsersTable.getSelectedRow(), 3).toString());
        fullname = table.getValueAt(manageUsersTable.getSelectedRow(), 4).toString();
        email = table.getValueAt(manageUsersTable.getSelectedRow(), 5).toString();
        balance = Integer.parseInt(table.getValueAt(manageUsersTable.getSelectedRow(), 6).toString());
      }
    });

    // remove user
    removeUserButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(userId == null)
          showMessageDialog("choose user first");
        else if (userId.equals(getUserId)) {
          showMessageDialog("you can't delete yourself");
        } else {
          int input = JOptionPane.showConfirmDialog(manageUsersFrame,
                  "are you sure to delete this user?", "delete confirmation",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
          if(input == 0) {
            removeUserFromDb();
            loadUsersTable();
          }
        }
      }

      private void removeUserFromDb() {
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "DELETE FROM users WHERE user_id = ?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, userId);
          pstmt.execute();
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
      }
    });

    // exit
    exitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        manageUsersFrame.dispose();
      }
    });

    // edit user
    editUserButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (userId == null)
          showMessageDialog("choose user first");
        else if (userId.equals(getUserId))
          showMessageDialog("you can't edit yourself");
        else {
          User updateUser = new User(userId, username, password, level, fullname, email, balance);
          new EditUser(updateUser, manageUsersTable);
          loadUsersTable();
        }
      }
    });
  }

  // Mesage Dialog
  private void showMessageDialog(String msg) {
    JOptionPane.showMessageDialog(manageUsersFrame, msg);
  }
  // Load Table
  private void loadUsersTable() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "SELECT * FROM users";
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      manageUsersTable.setModel(DbUtils.resultSetToTableModel(rs));
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }
}
