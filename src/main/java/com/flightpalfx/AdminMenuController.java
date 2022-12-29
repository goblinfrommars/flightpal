package com.flightpalfx;

import com.flightpalfx.AdminArea.ManageAirline;
import com.flightpalfx.AdminArea.ManageTickets;
import com.flightpalfx.AdminArea.ManageUsers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminMenuController {

	private static Stage stage;
	private static Scene scene;
	private static Parent root;

	String getUserId;

	@FXML private Label adminMenuAdminId;
	@FXML private Label adminMenuAdminUsername;

	@FXML private Button adminMenuLogoutBtn;

	@FXML void adminMenuLogoutBtnOnClick(ActionEvent event) throws IOException {
		System.out.println("LogOut");
		FXMLLoader loader = new FXMLLoader(AdminMenuController.class.getResource("login.fxml"));
		root = loader.load();
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}


	@FXML void airlinesManagerBtnOnClick(MouseEvent event) {
		new ManageAirline();
	}

	@FXML void ticketsManagerBtnOnClick(MouseEvent event) {
		new ManageTickets();
	}

	@FXML void usersManagerBtnOnClick(MouseEvent event) {
		new ManageUsers(getUserId);
	}

	public static void login(ActionEvent event, String getLoginUsername, String getLoginPassword, String id) throws IOException {
		FXMLLoader loader = new FXMLLoader(AdminMenuController.class.getResource("adminmenu.fxml"));
		root = loader.load();
		AdminMenuController adminMenuController = loader.getController();
		adminMenuController.setAdminMenuHeader(getLoginUsername, id);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void setAdminMenuHeader(String username, String id) {
		getUserId = id;
		adminMenuAdminUsername.setText(username);
		adminMenuAdminId.setText("#" + id);
	}
}
