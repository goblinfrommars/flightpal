package com.flightpalfx.swing;

import com.connection.JDBC;
import com.flightpalfx.TicketDesign.TicketDesign;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.DriverManager;

public class TransactionsHistory extends JDBC {
  private JButton exitButton;
  private JButton ticketDetailButton;
  private JTable transactionsHistoryTable;
  private JPanel transactionHistoryPanel;
  JFrame transactionHistoryFrame;
  String id, getTicketId;

  public TransactionsHistory(String id) {
    this.id = id;
    transactionHistoryFrame = new JFrame("Transaction History");
    transactionHistoryFrame.setContentPane(transactionHistoryPanel);
    transactionHistoryFrame.setSize(500, 700);
    transactionHistoryFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    transactionHistoryFrame.setLocationRelativeTo(null);
    transactionHistoryFrame.setVisible(true);
    loadTransactionsHistoryTable();


    exitButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        transactionHistoryFrame.dispose();
      }
    });


    ticketDetailButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (getTicketId == null) {
          showMessageDialog("select ticket first");
        } else {
          new TicketDesign(getTicketId);
        }
      }
    });

    transactionsHistoryTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        DefaultTableModel table = (DefaultTableModel) transactionsHistoryTable.getModel();
        getTicketId = table.getValueAt(transactionsHistoryTable.getSelectedRow(), 1).toString();
      }
    });
  }

  private void loadTransactionsHistoryTable() {
    try {
      Class.forName(JDBC_DRIVER);
      conn = DriverManager.getConnection(DB_URL, USER, PASS);
      String sql = "SELECT transaction_id AS 'Transaction ID', ticket_id AS 'Ticket ID', full_name AS 'Customer Name', card_id AS 'Customuer Card(ID)', passport AS 'Customer  Passport', email AS 'Customer Email' FROM transactions WHERE user_id = '" + id + "' ";
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      transactionsHistoryTable.setModel(DbUtils.resultSetToTableModel(rs));
    } catch (Exception e) {e.printStackTrace();}
  }

  private void showMessageDialog(String msg) {
    JOptionPane.showMessageDialog(transactionHistoryFrame, msg);
  }
}
