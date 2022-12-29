package com.flightpalfx;

public class User {
	public String id, username, password, fullname, email;
	public int level, balance;
	public int newLevel, newBalance;

	// get from field
	public String levelField, balanceField;

	public User(String id, String username, String password, int level, String fullname, String email, int balance) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.level = level;
		this.balance = balance;
	}

	// overload to add user
	public User(String username, String password, String level, String fullname, String email, String balance) {
		this.username = username;
		this.password = password;
		this.fullname = fullname;
		this.email = email;
		this.levelField = level;
		this.balanceField = balance;
	}

}
