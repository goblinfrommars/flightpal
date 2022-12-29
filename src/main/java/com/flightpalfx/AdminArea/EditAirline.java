package com.flightpalfx.AdminArea;

import com.connection.JDBC;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;

public class EditAirline extends JDBC {
  private JTextField editAirlineNewNameField;
  private JTextField editAirlineNewWebField;
  private JButton cancelButton;
  private JButton updateButton;
  private JPanel editAirlinePanel;
  JFrame editAirlineFrame;
  JFrame manageAirlineFrame;

  public String airlineId;

  public EditAirline(String airlineId, JFrame manageAirlineFrame, String getAirlineName, String getAirlineWeb) {
    this.airlineId = airlineId;
    this.editAirlineFrame = manageAirlineFrame;
    editAirlineFrame = new JFrame("edit airline");
    editAirlineFrame.setContentPane(editAirlinePanel);
    editAirlineFrame.pack();
    editAirlineFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    editAirlineFrame.setLocationRelativeTo(null);
    editAirlineFrame.setVisible(true);
    editAirlineNewNameField.setText(getAirlineName);
    editAirlineNewWebField.setText(getAirlineWeb);

    // edit airline cancel button
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editAirlineFrame.dispose();
      }
    });

    // update button
    updateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String airlineNewName = editAirlineNewNameField.getText();
        String airlineNewWeb = editAirlineNewWebField.getText();
        boolean isAirlineUpdate = isAirlineUpdateCheck(airlineNewName, airlineNewWeb);

        if (isAirlineUpdate) {
          updateAirlineToDb(airlineNewName, airlineNewWeb);
          showMessageDialog("update successfully");
          editAirlineFrame.dispose();
          manageAirlineFrame.dispose();
          new ManageAirline();
        }

      }
      private boolean isAirlineUpdateCheck(String airlineNewName, String airlineNewWeb) {
        boolean existAirline = false;
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER,PASS);
          stmt = conn.createStatement();
          String sql = "SELECT airline_name FROM airlines WHERE airline_name = '" + airlineNewName + "' ";
          rs = stmt.executeQuery(sql);
          if(rs.next())
            existAirline = true;
          conn.close();
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
        if(airlineNewName.isEmpty() || airlineNewWeb.isEmpty()) {
          showMessageDialog("field cannot be empty");
          return false;
        }
        if(!existAirline) {
          showMessageDialog("the airline has been registered");
          return false;
        }
        return true;
      }
      private void updateAirlineToDb(String airlineNewName, String airlineNewWeb) {
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "UPDATE airlines SET airline_name = ?, website = ? WHERE airline_id = ?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, airlineNewName);
          pstmt.setString(2, airlineNewWeb);
          pstmt.setString(3, airlineId);
          pstmt.execute();
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
      }
    });
  }


  // Mesage Dialog
  private void showMessageDialog(String msg) {
    JOptionPane.showMessageDialog(editAirlineFrame, msg);
  }
}
