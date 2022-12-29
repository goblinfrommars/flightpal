package com.flightpalfx.TicketDesign;

import com.connection.JDBC;

import javax.swing.*;
import java.sql.DriverManager;

public class TicketDesign extends JDBC {
  private JLabel fromLabel;
  private JLabel toLabel;
  private JLabel airlineLabel;
  private JLabel dateLabel;
  private JLabel classLabel;
  private JPanel eTicketPanel;
  private JLabel ticketDesignIdLabel;
  JFrame ticketDesignFrame;

  public TicketDesign(String getTicketId) {
    ticketDesignFrame = new JFrame();
    ticketDesignFrame.setContentPane(eTicketPanel);
    ticketDesignFrame.setSize(700, 250);
    ticketDesignFrame.setLocationRelativeTo(null);
    ticketDesignFrame.setVisible(true);
    getAllTicketData(getTicketId);
  }

  private void getAllTicketData(String getTicketId) {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      stmt = conn.createStatement();

      String sql = "SELECT * FROM tickets WHERE ticket_id = '"+getTicketId+"'";
      // String sql = "SELECT * FROM tickets";
      rs = stmt.executeQuery(sql);
      while (rs.next()) {
        ticketDesignIdLabel.setText("ticket-id: " + rs.getString("ticket_id"));
        dateLabel.setText("date: " + rs.getString("date"));
        fromLabel.setText("from: " + rs.getString("origin"));
        toLabel.setText("to: " + rs.getString("destination"));
        classLabel.setText("type: " + rs.getString("class_type"));
      }


      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }
}
