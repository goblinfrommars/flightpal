package com.flightpalfx;

import com.connection.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.DriverManager;

public class LoginController extends JDBC {


	@FXML private Button loginBtn;
	@FXML private PasswordField loginPassField;
	@FXML private TextField loginUserField;

	// whene login button on click
	@FXML void loginBtnOnClick(ActionEvent event) throws IOException {
		String getLoginUsername = loginUserField.getText();
		String getLoginPassword = loginPassField.getText();
		loginCheck(getLoginUsername, getLoginPassword, event);
	}

	private void loginCheck(String getLoginUsername, String getLoginPassword, ActionEvent event) throws IOException {
		boolean isAdmin = false;
		boolean isUser = false;
		String username = getLoginUsername;
		String getId = null, getUsername = null, getPassword = null, getFName = null, getEmail = null;
		int getBalance = 0, getLevel = 0;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sqlAdmin = "SELECT * FROM users WHERE username = '" + getLoginUsername + "' AND password = '" + getLoginPassword + "' AND level = 1";
			rs = stmt.executeQuery(sqlAdmin);
			if(rs.next()) {
				isAdmin = true;
				getId = rs.getString(1);


			}
			conn.close();
		} catch (Exception e) {e.printStackTrace();}
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sqlUser = "SELECT * FROM users WHERE username = '" + getLoginUsername + "' AND password = '" + getLoginPassword + "' AND level = 2";
			rs = stmt.executeQuery(sqlUser);

			if(rs.next()) {
				isUser = true;
				getId = rs.getString(1);
				getUsername = rs.getString(2);
				getPassword = rs.getString(3);
				getLevel = rs.getInt(4);
				getFName = rs.getString(5);
				getEmail = rs.getString(6);
				getBalance = rs.getInt(7);
			}

			conn.close();
		} catch (Exception e) {e.printStackTrace();}

		if(getLoginUsername.isEmpty() || getLoginPassword.isEmpty()) {
			System.out.println("Error, Empty field");
		} else if(isAdmin) {
			AdminMenuController.login(event, getLoginUsername, getLoginPassword, getId);
		} else if(isUser) {
			User user = new User(getId, getUsername, getPassword, getLevel, getFName, getEmail, getBalance);
			UserMenuController.login(event, user);
		} else {
			System.out.println("Account not found");
		}
	}


	@FXML void registerBtnOnClick(ActionEvent event) throws IOException {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("register.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("ABC");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (Exception e) {e.printStackTrace();}
	}
}
