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
import java.sql.DriverManager;
import java.util.Set;

public class ManageAirline extends JDBC {
  private JTextField manageAirlineNameField;
  private JTextField manageAirlineWebField;
  private JButton addButton;
  private JTable manageAirlinesTable;
  private JButton editButton;
  private JButton exitButton;
  private JButton removeButton;
  private JPanel ManageAirlinePanel;

  public String airlineId, airlineName, airlineWeb;

  JFrame manageAirlineFrame;
  public ManageAirline() {
    manageAirlineFrame = new JFrame("Airline Manager");
    manageAirlineFrame.setContentPane(ManageAirlinePanel);
    manageAirlineFrame.pack();
    manageAirlineFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    manageAirlineFrame.setLocationRelativeTo(null);
    manageAirlineFrame.setVisible(true);
    loadAirlinesTable();

    // add airlines button
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String airlineName = manageAirlineNameField.getText();
        String airlineWeb = manageAirlineWebField.getText();
        boolean isAirlineAvailable = addAirlineCheck(airlineName, airlineWeb);
        if (isAirlineAvailable)
          addAirlineToDb(airlineName, airlineWeb);
        loadAirlinesTable();
      }
      private boolean addAirlineCheck(String airlineName, String airlineWeb) {
        boolean existAirline = true;
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          stmt = conn.createStatement();
          String sql = "SELECT airline_name FROM airlines WHERE airline_name = '" + airlineName + "' ";
          rs = stmt.executeQuery(sql);
          if(rs.next())
            existAirline = false;
          conn.close();
        } catch (Exception e) { e.printStackTrace();}

        if (airlineName.isEmpty() || airlineWeb.isEmpty()) {
          showMessageDialog("field cannot be empty");
          return false;
        }

        if (!existAirline) {
          showMessageDialog("the airline has been registered");
          return false;
        }

        return true;
      }
      private void addAirlineToDb(String airlineName, String airlineWeb) {
        String unique = "AI" + RandomStringUtils.randomNumeric(3);
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "INSERT INTO airlines (airline_id, airline_name, website) " +
                  "VALUES(?, ?, ?)";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, unique);
          pstmt.setString(2, airlineName);
          pstmt.setString(3, airlineWeb);
          pstmt.execute();

          showMessageDialog("airline added successfully");
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
      }
    });


    // remove airline button
    removeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(airlineId == null) {
          showMessageDialog("choose the airline first");
        } else {
          int input = JOptionPane.showConfirmDialog(manageAirlineFrame,
                  "are you sure to delete this airline?", "delete confirmation",
                  JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
          if(input == 0) {
            removeFromDatabase(airlineId);
            loadAirlinesTable();
          }
        }
      }
      private void removeFromDatabase(String airlineId) {
        try {
          Class.forName(JDBC_DRIVER);
          conn = DriverManager.getConnection(DB_URL, USER, PASS);
          String sql = "DELETE FROM airlines WHERE airline_id = ?";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1, airlineId);
          pstmt.execute();
          conn.close();
        } catch (Exception e) {e.printStackTrace();}
      }
    });

    // exit button
    exitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        manageAirlineFrame.dispose();
      }
    });

    manageAirlinesTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        DefaultTableModel table = (DefaultTableModel)manageAirlinesTable.getModel();
        airlineId = table.getValueAt(manageAirlinesTable.getSelectedRow(), 0).toString();
        airlineName = table.getValueAt(manageAirlinesTable.getSelectedRow(),1).toString();
        airlineWeb = table.getValueAt(manageAirlinesTable.getSelectedRow(),2).toString();
      }
    });
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(airlineId == null)
          showMessageDialog("choose the airline first");
        else
          new EditAirline(airlineId, manageAirlineFrame, airlineName, airlineWeb);
      }
    });
  }


  // Mesage Dialog
  private void showMessageDialog(String msg) {
    JOptionPane.showMessageDialog(manageAirlineFrame, msg);
  }

  // Load Table
  private void loadAirlinesTable() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "SELECT * FROM airlines";
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      manageAirlinesTable.setModel(DbUtils.resultSetToTableModel(rs));
      conn.close();
    } catch (Exception e) {e.printStackTrace();}
  }
}
