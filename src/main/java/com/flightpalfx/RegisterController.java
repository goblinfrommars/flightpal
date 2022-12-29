package com.flightpalfx;

import com.connection.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;
import java.io.IOException;
import java.sql.DriverManager;

public class RegisterController extends JDBC {
  Parent logOutRoot;
  Stage logOutStage;
  Scene logOutScene;

  @FXML private Hyperlink bckToLogin;
  @FXML private TextField registEmail;
  @FXML private TextField registFullName;
  @FXML private PasswordField registPassword;
  @FXML private PasswordField registPasswordConf;
  @FXML private TextField registUserName;

  @FXML void bckToLoginOnClick(ActionEvent event) throws IOException {
    bckToLogin.getScene().getWindow().hide();
  }

  @FXML void registerBtnOnClick(ActionEvent event) {
    String fullname = registFullName.getText();
    String username = registUserName.getText();
    String password = registPassword.getText();
    String confpass = registPasswordConf.getText();
    String email = registEmail.getText();

    boolean isregist = registCheck(fullname, username, password, confpass, email);
    if(isregist)
      insertToDatabase(fullname, username, password, email);

  }

  private boolean registCheck(String fullname, String username, String password, String confpass, String email) {
    boolean usernameexist = true;
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      String sql = "SELECT * FROM users WHERE username = '" + username + "' ";
      rs = stmt.executeQuery(sql);

      if(rs.next())
        usernameexist = false;

      conn.close();
    } catch (Exception e) {e.printStackTrace();}

    if(fullname.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty()) {
      showMessageDialog("Field can't be empty");
      return false;
    }

    if(!usernameexist) {
      showMessageDialog("Username has been used");
      return false;
    }

    if(!password.equals(confpass) || password.length() < 8) {
      showMessageDialog("Password confirmation failed (Must be more or equal 8 characters");
      return false;
    }

    return true;
  }
  private void insertToDatabase(String fullname, String username, String password, String email) {

    String unique = "US" + RandomStringUtils.randomNumeric(3);
    System.out.println(unique);

    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "INSERT INTO users (user_id, username, password, level, fullname, email, balance) VALUES(?, ?, ?, ?, ?, ?, ?)";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, unique);
      pstmt.setString(2, username);
      pstmt.setString(3, password);
      pstmt.setInt(4, 2);
      pstmt.setString(5, fullname);
      pstmt.setString(6, email);
      pstmt.setInt(7, 0);
      pstmt.execute();

      showMessageDialog("Register Successfully. Please Login Now");

      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }

  void showMessageDialog(String msg) {
    JOptionPane.showMessageDialog(null, msg);
  }
}
