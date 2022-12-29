package com.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBC {
	public static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	public static final String DB_URL = "jdbc:mysql://localhost/flightpal_fix";
	public static final String USER = "root";
	public static final String PASS = "";

	// Menyiapkan objek yang diperlukan untuk mengelola database
	public static Connection conn;
	public static Statement stmt;
	public static ResultSet rs;
	public static PreparedStatement pstmt;
}
