package com.flightpalfx;

import com.connection.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.DriverManager;

public class EditAirlineController extends JDBC {

	@FXML
	private TextField addAirlineName;
	@FXML private TextField addAirlineWeb;

	@FXML private TableView<?> airlineEditTable;
	@FXML private TableColumn<?, ?> editAirlineID;
	@FXML private TableColumn<?, ?> editAirlineNAME;
	@FXML private TableColumn<?, ?> editAirlineWEB;

	@FXML
	void addAirlineBtnOnClick(ActionEvent event) {
		String airlineName = addAirlineName.getText();
		String airlineWeb = addAirlineWeb.getText();
		boolean isAddAirline = addAirlineCheck(airlineName, airlineWeb);
		if(isAddAirline)
			addAirlineToDB(airlineName, airlineWeb);
	}

	private boolean addAirlineCheck(String airlineName, String airlineWeb) {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
			String sql = "SELECT * FROM airlines WHERE name = '" + airlineName + "'";
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				showMessageDialog("Failed", "Airlines alreaddy exist!");
				addAirlineName.setText("");
				addAirlineWeb.setText("");
				return false;
			}
			conn.close();
		} catch (Exception e) {e.printStackTrace();}
		if(airlineName.isEmpty() || airlineName.isEmpty()) {
			showMessageDialog("Failed", "Form can't be empty!");
			return false;
		}
		return true;
	}


	private void addAirlineToDB(String airlineName, String airlineWeb) {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			String sql = "INSERT INTO airlines (airline_id, name, website) VALUES (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			String uniqueId = (airlineName.substring(0,3) + RandomStringUtils.randomAlphanumeric(3)).toUpperCase();
			pstmt.setString(1, uniqueId);
			pstmt.setString(2, airlineName);
			pstmt.setString(3, airlineWeb);
			pstmt.execute();
			showMessageDialog("Success", "Airline has been added to Database");
			addAirlineName.setText("");
			addAirlineWeb.setText("");
			conn.close();
		} catch (Exception e) {e.printStackTrace();}
	}

	private void showMessageDialog(String tittle, String msg) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(tittle);
		alert.setHeaderText(null);
		alert.setContentText(msg);
		alert.showAndWait();
	}


	@FXML
	void addAirlineExitBtnOnClick(ActionEvent event) {

	}

}

