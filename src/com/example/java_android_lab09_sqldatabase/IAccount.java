package com.example.java_android_lab09_sqldatabase;

public interface IAccount {
	 String getName();
	 int getMoney();
	 void withdraw(int amount) throws NotEnoughMoneyException;
}
