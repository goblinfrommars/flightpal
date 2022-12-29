package com.flightpalfx;

import com.connection.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.DriverManager;

public class TopUpController extends JDBC {


	private static Stage stage, userMenuStage;
	private static Scene scene;
	private static Parent root;

	static String getUsername = null, getPassword = null, getFullName = null, getEmail = null, getId = null;
	static int getBalance, getLevel;

	@FXML private TextField topUpAddBalanceField;
	@FXML private Label topUpCurrentBalance;
	@FXML private Button topUpAddBalanceBtn;

	public static void topup(String id, String username, String password, int level, String fullname, String email, int balance, Stage getUserMenuStage) throws IOException {

		getId = id;
		getUsername = username;
		getPassword = password;
		getLevel = level;
		getFullName = fullname;
		getEmail = email;
		getBalance = balance;
		userMenuStage = getUserMenuStage;

		FXMLLoader loader = new FXMLLoader(TopUpController.class.getResource("topup.fxml"));
		root = loader.load();
		Stage stage = new Stage();


		TopUpController topUpController = loader.getController();
		topUpController.setTopUpHeader(balance);
		try {
			Parent root = FXMLLoader.load(TopUpController.class.getResource("topup.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);

			stage.show();
		} catch (Exception e) {e.printStackTrace();}
	}

	private void setTopUpHeader(int balance) {
		System.out.println(balance);
		String balanceConvert = String.valueOf(balance);
		System.out.println(balanceConvert);
	}

	@FXML
	void topUpAddBalanceBtnOnClick(ActionEvent event) {
		String balanceValue = topUpAddBalanceField.getText();
		try {
			int convert = Integer.parseInt(balanceValue);
			isTopUp(convert, event);
		} catch (Exception e) {
			System.out.println("Field must be number");
		}
	}

	private void isTopUp(int balance, ActionEvent event) throws IOException {
		int currentBalance = 0;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql = "SELECT balance FROM users WHERE username = '" + getUsername + "' AND password = '" + getPassword + "' ";
			rs = stmt.executeQuery(sql);
			while (rs.next())
				currentBalance += rs.getInt(1);
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int newBalance = currentBalance + balance;
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String sql = "UPDATE users SET balance = ? WHERE username = ? AND password = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, currentBalance + balance);
			pstmt.setString(2, getUsername);
			pstmt.setString(3, getPassword);
			pstmt.executeUpdate();
			topUpSuccessMsg();
			topUpAddBalanceField.setText("");

			conn.close();
		} catch (Exception e) {e.printStackTrace();}
		User user = new User(getId, getUsername, getPassword, getLevel, getFullName, getEmail, newBalance);
		UserMenuController.login(event, user);
		userMenuStage.close();
		topUpCurrentBalance.setText("");
	}

	private void topUpSuccessMsg() {
		Alert msg = new Alert(Alert.AlertType.INFORMATION);
		msg.setTitle("Top Up");
		msg.setHeaderText("Success");
		msg.setContentText("TopUp Successfully");
		msg.showAndWait();
	}
}
