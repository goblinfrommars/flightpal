package com.flightpalfx.AdminArea;

import com.connection.JDBC;
import net.proteanit.sql.DbUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.Time;
import java.sql.Types;

public class ManageTickets extends JDBC {
  private JPanel manageTicketsPanel;
  private JTable manageTicketsTicketsTable;
  private JTable manageTicketsAirlineTable;
  private JComboBox airlineCB;
  private JTextField manageTicketsDateField;
  private JTextField manageDprtHourField;
  private JTextField manageDstHourField;
  private JComboBox classCB;
  private JTextField manageTicketsOriginField;
  private JTextField manageTicketsDstField;
  private JButton exitButton;
  private JButton removeButton;
  private JButton editButton;
  private JButton addButton;
  private JTextField manageTicketsPriceField;
  JFrame manageTicketsFrame;

  String getTicketIdOnClick;

  public ManageTickets() {
    manageTicketsFrame = new JFrame("Tickets Manager");
    manageTicketsFrame.setContentPane(manageTicketsPanel);
    manageTicketsFrame.pack();
    manageTicketsFrame.setLocationRelativeTo(null);
    manageTicketsFrame.setVisible(true);
    loadTicketsTable();
    loadAirlinesTable();
    loadAirlineComboBox();
    loadClassTypeComboBox();

    // add
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String addTicketId = "TI" + RandomStringUtils.randomNumeric(3);
        String addAirline = airlineCB.getSelectedItem().toString();
        String addDate = manageTicketsDateField.getText();
        String addHrsDprt = manageDprtHourField.getText();
        String addHrsDstn = manageDstHourField.getText();
        String addClass = classCB.getSelectedItem().toString();
        String addOrigin = manageTicketsOriginField.getText();
        String addDestination = manageTicketsDstField.getText();
        String addPrice = manageTicketsPriceField.getText();

        boolean isAddTicket = addTicketCheck(addTicketId, addAirline, addDate, addHrsDprt, addHrsDstn, addClass, addOrigin, addDestination, addPrice);
        if (isAddTicket) {
          insertTicketToDb(addTicketId, addAirline, addDate, addHrsDprt, addHrsDstn, addClass, addOrigin, addDestination, addPrice);
          loadTicketsTable();
        }
      }

      private void insertTicketToDb(String addTicketId, String addAirline, String addDate, String addHrsDprt, String addHrsDstn, String addClass, String addOrigin, String addDestination, String addPrice) {
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "INSERT INTO tickets(ticket_id, airline, date, hours_of_departure, hour_to_destination, class_type, origin, destination, price, user_id) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, addTicketId);
          pstmt.setString(2, addAirline);
          pstmt.setDate(3, Date.valueOf(addDate));
          pstmt.setTime(4, Time.valueOf(addHrsDprt));
          pstmt.setTime(5, Time.valueOf(addHrsDstn));
          pstmt.setString(6, addClass);
          pstmt.setString(7, addOrigin);
          pstmt.setString(8, addDestination);
          pstmt.setInt(9, Integer.parseInt(addPrice));
          pstmt.setNull(10, Types.INTEGER);
          pstmt.execute();
          showMessageDialog("ticket has been successfully added");
          conn.close();
        } catch (Exception e) {
          showMessageDialog("date or time format invalid");
        }
      }
      private boolean addTicketCheck(String addTicketId, String addAirline, String addDate, String addHrsDprt, String addHrsDstn, String addClass, String addOrigin, String addDestination, String addPrice) {
        boolean isPriceNumber = true;
        try {
          int number = Integer.parseInt(addPrice);
        } catch (NumberFormatException e) {
          isPriceNumber = false;
        }
        if (addTicketId.isEmpty() || addAirline.isEmpty() || addDate.isEmpty() || addHrsDprt.isEmpty() || addHrsDstn.isEmpty() ||addClass.isEmpty() || addOrigin.isEmpty() || addDestination.isEmpty() || addPrice.isEmpty()) {
          showMessageDialog("field cannot be empty");
          return false;
        }

        if (!isPriceNumber) {
          showMessageDialog("price area must in number");
          return false;
        }
        return true;
      }
    });


    exitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        manageTicketsFrame.dispose();
      }
    });

    // remove btn
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (getTicketIdOnClick == null) {
          showMessageDialog("choose ticket first");
        } else {
          int input = JOptionPane.showConfirmDialog(manageTicketsFrame,
                  "are you sure to delete this ticket?", "delete confirmation",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
          if(input == 0) {
            removeTicketFromDb();
            loadTicketsTable();
            getTicketIdOnClick = null;
          }
        }
      }

      private void removeTicketFromDb() {
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "DELETE FROM tickets WHERE ticket_id = ?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, getTicketIdOnClick);
          pstmt.execute();
          showMessageDialog("ticket delete successfully");
          conn.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    // table on click
    manageTicketsTicketsTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        DefaultTableModel table = (DefaultTableModel) manageTicketsTicketsTable.getModel();
        getTicketIdOnClick = table.getValueAt(manageTicketsTicketsTable.getSelectedRow(), 0).toString();
      }
    });

    // edit
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(getTicketIdOnClick == null) {
          showMessageDialog("please select ticket first");
        } else {
          new EditTicket(getTicketIdOnClick, manageTicketsTicketsTable);
        }
      }
    });
  }

  private void loadClassTypeComboBox() {
    classCB.addItem("Economy");
    classCB.addItem("Bussiness");
  }

  private void showMessageDialog(String msg) {
    JOptionPane.showMessageDialog(manageTicketsFrame, msg);
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
        airlineCB.addItem(id);
      }
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }

  // Load Table
  private void loadTicketsTable() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "SELECT ticket_id, airline, date, hours_of_departure, hour_to_destination, class_type, origin, destination, price FROM tickets WHERE user_id IS NULL";
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      manageTicketsTicketsTable.setModel(DbUtils.resultSetToTableModel(rs));
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }


  private void loadAirlinesTable() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "SELECT airline_id, airline_name FROM airlines";
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      manageTicketsAirlineTable.setModel(DbUtils.resultSetToTableModel(rs));
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }
}
