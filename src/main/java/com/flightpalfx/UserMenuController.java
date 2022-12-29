package com.flightpalfx;

import com.flightpalfx.swing.ChooseTicket;
import com.flightpalfx.swing.TransactionsHistory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class UserMenuController {

private static Stage userMenuStage, logOutStage;
private static Scene userMenuScene, logOutScene;
private static Parent userMenuRoot, logOutRoot;

static String id = null, username = null, password = null, fullname = null, email = null;
static int level, balance;

	@FXML private Label userMenuUserName;
	@FXML private Label userMneuUserId;
	@FXML private Label userMenuBalance;

	@FXML
	void userMenuBuyTicketOnClicked(ActionEvent event) throws IOException {
		System.out.println("Buy Tickets");
		ChooseTicket chooseTicket = new ChooseTicket(id, username, password, level, fullname, email, balance, userMenuStage, event);
	}

	@FXML
	void userMenuScheduleOnClicked(ActionEvent event) {
		TransactionsHistory transactionsHistory = new TransactionsHistory(id);
	}

	@FXML
	void userMenuTopUpOnClicked(ActionEvent event) throws IOException {
		TopUpController.topup(id, username, password, level, fullname, email, balance, userMenuStage);
	}

	@FXML
	void userMenuLogOutOnClicked(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("login.fxml"));
		logOutRoot = loader.load();
		logOutStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		logOutScene = new Scene(logOutRoot);
		logOutStage.setScene(logOutScene);
		logOutStage.show();
	}

	public static void login(ActionEvent event, User user) throws IOException {
		FXMLLoader loader = new FXMLLoader(AdminMenuController.class.getResource("usermenu.fxml"));
		userMenuRoot = loader.load();

		id = user.id;
		username = user.username;
		password = user.password;
		level = user.level;
		fullname = user.fullname;
		email = user.email;
		balance = user.balance;

		UserMenuController userMenuController = loader.getController();
		userMenuController.setUserMenuHeader(username, id, balance);

		userMenuStage = (Stage)((Node)event.getSource()).getScene().getWindow();
		userMenuScene = new Scene(userMenuRoot);
		userMenuStage.setScene(userMenuScene);
		userMenuStage.show();
		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		userMenuStage.setX((screenBounds.getWidth() - userMenuStage.getWidth()) / 2);
		userMenuStage.setX((screenBounds.getHeight() - userMenuStage.getHeight()) / 2);
	}

	public void setUserMenuHeader(String username, String id, int balance) {
		String balanceConvert = String.valueOf(balance);
		userMenuUserName.setText(username);
		userMneuUserId.setText("#" + id);
		userMenuBalance.setText("Rp. " + balanceConvert);
	}
}
