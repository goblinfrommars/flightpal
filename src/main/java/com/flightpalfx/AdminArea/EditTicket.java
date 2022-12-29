package com.flightpalfx.AdminArea;

import com.connection.JDBC;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Time;
import java.sql.Types;

public class EditTicket extends JDBC {
  private JComboBox editAirlineAirlineCB;
  private JTextField editTicketDateField;
  private JTextField editTicketNewHrDprtField;
  private JTextField editTicketNewHrDstField;
  private JComboBox editAirlineClassCB;
  private JTextField editTicketNewOriginField;
  private JTextField editTicketNewDestField;
  private JTextField editTicketNewPriceField;
  private JButton updateButton;
  private JButton cancelButton;
  private JPanel editTicketsPanel;
  JFrame editTicketFrame;
  JTable getManageTicketTicketTable;
  String getTicketId;


  public EditTicket(String getTicketIdOnClick, JTable manageTicketsTicketsTable) {
    this.getManageTicketTicketTable = manageTicketsTicketsTable;
    this.getTicketId = getTicketIdOnClick;
    editTicketFrame = new JFrame("Edit Ticket");
    editTicketFrame.setContentPane(editTicketsPanel);
    editTicketFrame.pack();
    editTicketFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    editTicketFrame.setLocationRelativeTo(null);
    editTicketFrame.setVisible(true);
    loadAirlineComboBox();
    loadClassTypeComboBox();

    // Cancel
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editTicketFrame.dispose();
      }
    });
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        editTicketFrame.dispose();
      }
    });

    // update
    updateButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String newAirline = editAirlineAirlineCB.getSelectedItem().toString();
        String newDate = editTicketDateField.getText();
        String newHrDprt = editTicketNewHrDprtField.getText();
        String newHrDstn = editTicketNewHrDstField.getText();
        String newClass = editAirlineClassCB.getSelectedItem().toString();
        String newOrigin = editTicketNewOriginField.getText();
        String newDestination = editTicketNewDestField.getText();
        String newPrice = editTicketNewPriceField.getText();
        boolean isUpdateTicket = updateTicketCheck(newAirline, newDate, newHrDprt, newHrDstn, newClass, newOrigin, newDestination, newPrice);
        if (isUpdateTicket) {
          updateTicketToDb(newAirline, newDate, newHrDprt, newHrDstn, newClass, newOrigin, newDestination, newPrice);
          loadTicketsTable();
          editTicketFrame.dispose();
        }
      }

      private void updateTicketToDb(String newAirline, String newDate, String newHrDprt, String newHrDstn, String newClass, String newOrigin, String newDestination, String newPrice) {
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "UPDATE tickets SET airline=?, date =?, hours_of_departure =?, hour_to_destination =?, class_type =?, origin =?, destination =?, price =?, user_id =? WHERE ticket_id =?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, newAirline);
          pstmt.setDate(2, Date.valueOf(newDate));
          pstmt.setTime(3, Time.valueOf(newHrDprt));
          pstmt.setTime(4, Time.valueOf(newHrDstn));
          pstmt.setString(5, newClass);
          pstmt.setString(6, newOrigin);
          pstmt.setString(7, newDestination);
          pstmt.setInt(8, Integer.parseInt(newPrice));
          pstmt.setNull(9, Types.INTEGER);
          pstmt.setString(10, getTicketId);
          pstmt.execute();
          showMessageDialog("update ticket successfully");
          conn.close();
        } catch (Exception e) {
          e.printStackTrace();
          showMessageDialog("date or time format invalid");
        }
        System.out.println(newDate);
      }

      private boolean updateTicketCheck(String newAirline, String newDate, String newHrDprt, String newHrDstn, String newClass, String newOrigin, String newDestination, String newPrice) {
        boolean isPriceNumber = true;
        if(newAirline.isEmpty() || newDate.isEmpty() || newHrDprt.isEmpty() || newHrDstn.isEmpty() || newClass.isEmpty() ||newOrigin.isEmpty() || newDestination.isEmpty() || newPrice.isEmpty()) {
          showMessageDialog("field cannot be empty");
          return false;
        }
        try {
          int value = Integer.parseInt(newPrice);
        } catch (NumberFormatException e) {
          isPriceNumber = false;
        }
        if(!isPriceNumber) {
          showMessageDialog("price area must in number");
          return false;
        }
        return true;
      }
    });
  }

  private void loadAirlineComboBox() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      String sql = "SELECT airline_id FROM airlines";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String id = rs.getString("airline_id");
        editAirlineAirlineCB.addItem(id);
      }
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }

  private void loadClassTypeComboBox() {
    editAirlineClassCB.addItem("Economy");
    editAirlineClassCB.addItem("Bussiness");
  }

  private void showMessageDialog(String msg) {
    JOptionPane.showMessageDialog(editTicketFrame, msg);
  }

  private void loadTicketsTable() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "SELECT ticket_id, airline, date, hours_of_departure, hour_to_destination, class_type, origin, destination, price FROM tickets WHERE user_id IS NULL";
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      getManageTicketTicketTable.setModel(DbUtils.resultSetToTableModel(rs));
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }
}
