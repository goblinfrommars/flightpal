package com.flightpalfx.swing;

import com.connection.JDBC;
import com.flightpalfx.Tickets;
import javafx.stage.Stage;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.DriverManager;

public class ChooseTicket extends JDBC {
  private JPanel chooseTicketPanel;
  private JComboBox originComboBox;
  private JComboBox destinationComboBox;
  private JButton searchTicketsButton;
  private JButton buyTicketButton;
  private JButton chooseTicketCancelBtn;
  private JScrollPane scrollPane;
  private JTable chooseTicketTable;
  private JButton loadAllTicketsButton;
  private JLabel chooseTicketBalanceLabel;
  private JLabel remainTicketsLabel;

  JFrame chooseTicketFrame;
  String id, username, password, fullname, email;
  int balance, level;
  Stage userMenuStage;
  javafx.event.ActionEvent event;

  // variable to store tickets data
  String idTicket, airlineTicket, originTicket, destinationTicket, dateTicket, departureTimeTicket, arrivesTimeTicket, classTypeTicket;
  int priceTicket;
  public ChooseTicket(String id, String username, String password, int level, String fullname, String email, int balance, Stage userMenuStage, javafx.event.ActionEvent event) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.level = level;
    this.fullname = fullname;
    this.email = email;
    this.balance = balance;
    this.userMenuStage = userMenuStage;
    this.event = event;

    chooseTicketFrame = new JFrame("Choose Tickets");
    chooseTicketFrame.setContentPane(chooseTicketPanel);
    chooseTicketFrame.setSize(600, 800);
    chooseTicketFrame.setLocationRelativeTo(null);
    chooseTicketFrame.setVisible(true);

    chooseTicketBalanceLabel.setText("Your balance: Rp. " + balance);
    setTicketRemain();
    tableLoadAllTickets();
    addToOriginCombboBox();
    addToDestinationCombboBox();


    searchTicketsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String selectedOrigin = (String) originComboBox.getSelectedItem();
        String selectedDestination = (String) destinationComboBox.getSelectedItem();
        loadSelectedRouteTable(selectedOrigin, selectedDestination);
      }
      private void loadSelectedRouteTable(String selectedOrigin, String selectedDestination) {
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "SELECT tickets.ticket_id AS 'Id', airlines.airline_name AS 'Airline', tickets.origin AS 'From', " +
                  "tickets.destination AS 'To', tickets.date AS 'Date', tickets.hours_of_departure AS 'Departure Time', tickets.hour_to_destination AS " +
                  "'Arrives Time', tickets.class_type AS 'Class', tickets.price AS 'Price' FROM tickets INNER JOIN airlines ON tickets.airline = airlines.airline_id WHERE " +
                  "tickets.origin = '" + selectedOrigin + "' AND tickets.destination = '" + selectedDestination +"' AND tickets.user_id IS NULL";
          pstmt = conn.prepareStatement(sql);
          rs = pstmt.executeQuery();
          chooseTicketTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {e.printStackTrace();}
      }
    });
    loadAllTicketsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        tableLoadAllTickets();
      }
    });
    chooseTicketCancelBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        chooseTicketFrame.dispose();
      }
    });


    buyTicketButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(idTicket == null)
          showMessageDialog("Please Select Ticket First");
        else if (priceTicket > balance)
          showMessageDialog("Your balance is not enough to buy this ticket");
        else {
          Tickets newTicket = new Tickets(idTicket, airlineTicket, originTicket, destinationTicket, dateTicket, departureTimeTicket, arrivesTimeTicket, classTypeTicket, priceTicket);
          TransactionsConfirmation transactionsConfirmation = new TransactionsConfirmation(id, username, password,level, fullname, email, balance, newTicket, chooseTicketFrame, userMenuStage, event);
        }
      }

      private void showMessageDialog(String msg) {
        JOptionPane.showMessageDialog(chooseTicketPanel, msg);
      }
    });

    chooseTicketTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        DefaultTableModel table = (DefaultTableModel)chooseTicketTable.getModel();

        idTicket = table.getValueAt(chooseTicketTable.getSelectedRow(), 0).toString();
        airlineTicket = table.getValueAt(chooseTicketTable.getSelectedRow(), 1).toString();
        originTicket = table.getValueAt(chooseTicketTable.getSelectedRow(), 2).toString();
        destinationTicket = table.getValueAt(chooseTicketTable.getSelectedRow(), 3).toString();
        dateTicket = table.getValueAt(chooseTicketTable.getSelectedRow(), 4).toString();
        departureTimeTicket = table.getValueAt(chooseTicketTable.getSelectedRow(), 5).toString();
        arrivesTimeTicket = table.getValueAt(chooseTicketTable.getSelectedRow(), 6).toString();
        classTypeTicket = table.getValueAt(chooseTicketTable.getSelectedRow(), 7).toString();
        priceTicket = (int) table.getValueAt(chooseTicketTable.getSelectedRow(), 8);
      }
    });
  }
  private void setTicketRemain() {
    int remainTickets = 0;
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      String sql = "SELECT COUNT(*) FROM tickets WHERE user_id IS NULL";
      rs = stmt.executeQuery(sql);

      rs.next();
      remainTickets = rs.getInt(1);

      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
      remainTickets = -1;
    }

    remainTicketsLabel.setText("Remaining Tickets: " + remainTickets);
  }
  void tableLoadAllTickets() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "SELECT tickets.ticket_id AS 'Id', airlines.airline_name AS 'Airline', tickets.origin AS 'From', tickets.destination AS 'To', tickets.date AS 'Date', tickets.hours_of_departure AS 'Departure Time', tickets.hour_to_destination AS 'Arrives Time', tickets.class_type AS 'Class', tickets.price AS 'Price' FROM tickets INNER JOIN airlines ON tickets.airline = airlines.airline_id WHERE tickets.user_id IS NULL";
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      chooseTicketTable.setModel(DbUtils.resultSetToTableModel(rs));
    } catch (Exception e) {e.printStackTrace();}
  }
  void addToOriginCombboBox() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      String sql = "SELECT DISTINCT origin FROM tickets";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String origin = rs.getString("origin");
        originComboBox.addItem(origin);
      }
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }
  void addToDestinationCombboBox() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();
      String sql = "SELECT DISTINCT destination FROM tickets";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        String destination = rs.getString("destination");
        destinationComboBox.addItem(destination);
      }
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }
}
