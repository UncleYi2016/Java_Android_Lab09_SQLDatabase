package com.example.java_android_lab09_sqldatabase;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class Bank extends SQLiteOpenHelper{
	private String name;
	private ArrayList<IModelListener> listeners;
	private final String tablename="AccountTable";
	private final String accountId = "id";
	private final String accountName="name";
	private final String accountBalance="balance";
	private final String accountType="type";
	public Bank(String name, Context context) {
		super(context, "bankDb.db", null, 1);
		this.name = name;
		// When a bank is created, it has an empty arraylist of listeners.
		this.listeners = new ArrayList<IModelListener>();
	}
	
	
	// The addListener method takes a listener as argument and adds the
	// listener to the arraylist of listeners for the bank.
	public void addListener(IModelListener listener) {
		this.listeners.add(listener);
	}
	// Notify all listeners that the bank data has changed. Once they
	// have been notified, each listener will then use the appropriate
	// methods of the bank to get all the new values that the listener
	// needs to display.
	private void notifyAllModelListeners(){
		for(IModelListener listener : listeners){
			listener.notifyModelListener();
		}
	}
	// The addAccount method takes an account as argument and adds the
	// account to the arraylist of accounts for the bank.
	public void addAccount(IAccount account) {
		ContentValues value = new ContentValues(); 
		value.put(this.accountName, account.getName());
		value.put(this.accountBalance, account.getMoney());
		if(account instanceof CreditAccount){
			value.put(this.accountType, "CreditAccount");
		}else if(account instanceof StudentAccount){
			value.put(this.accountType, "StudentAccount");
		}else{
			value.put(this.accountType, "Account");
		}
		SQLiteDatabase wdb = this.getWritableDatabase();
		wdb.insert(tablename, null, value);
		wdb.close();
		// Notify all the listeners that a change has occurred with the bank¡¯s data.
		notifyAllModelListeners();
	}
	// The totalMoney method returns as result the total amount of money in
	// all the bank accounts of the bank.
	public int totalMoney() {
		int total = 0;
		// We loop over all the accounts in the arraylist and add the amount of
		// money in each account to the total.
		SQLiteDatabase rdb = this.getReadableDatabase();
		Cursor cursor = rdb.rawQuery("SELECT * FROM " + tablename, null);
		cursor.moveToFirst();
		do{
			total += Integer.parseInt(cursor.getString(2));
		}while(cursor.moveToNext());
		return total;
	}
	// The getMoney method takes as argument the name of a customer and
	// returns as result the amount of money currently stored in the bank
	// account that belongs to that customer. If the customer does not have
	// a bank account in the bank then the getMoney method should throw an
	// UnknownCustomerException with the message "Customer XXX unknown", where
	// XXX is replaced with the name of the customer.
	public int getMoney(String name) throws UnknownCustomerException, NotEnoughMoneyException {
		// We loop over all the accounts in the arraylist, looking for the
		// account with the correct customer name.
		SQLiteDatabase rdb = this.getReadableDatabase();
		Cursor cursor = rdb.rawQuery("SELECT * FROM '" + this.tablename + "' WHERE '" + this.accountName + "' = '" + name + "'", null);
		if(cursor.moveToFirst()){
			String accName = cursor.getString(1);
			int accBalance = Integer.parseInt(cursor.getString(2));
			String accType = cursor.getString(3);
			if(accType.equals("CreditAccount")){
				IAccount cAcc = new CreditAccount(accName, accBalance);
				return cAcc.getMoney();
			}else if(accType.equals("StudentAccount")){
				IAccount sAcc = new StudentAccount(accName, accBalance);
				return sAcc.getMoney();
			}
		}
		// If we reach this point in the code, then it means we have looped
		// over all the accounts in the arraylist without finding an account
		// with the correct customer name. Therefore this customer does not
		// have an account in the bank and we throw an exception.
		throw new UnknownCustomerException("Customer " + name + " unknown");
	}
	 // The withdraw method takes as argument the name of a customer and an
	// amount of money and withdraws that amount of money from the amount of
	// money currently stored in the bank account that belongs to that
	// customer. If the customer does not have a bank account in the bank
	 // then the withdraw method should throw an UnknownCustomerException with
	// the message "Customer XXX unknown", where XXX is replaced with the
	 // name of the customer.
	 public void withdraw(String name, int amount) throws UnknownCustomerException, NotEnoughMoneyException {
		 // We loop over all the accounts in the arraylist, looking for the
		 // account with the correct customer name.
		 SQLiteDatabase rdb = this.getReadableDatabase();
		 Cursor cursor = rdb.rawQuery("SELECT * FROM '" + this.tablename + "' WHERE '" + this.accountName + "' = '" + name + "'", null);
		 if(cursor.moveToFirst()){
			String accName = cursor.getString(1);
			int accBalance = Integer.parseInt(cursor.getString(2));
			String accType = cursor.getString(3);
			IAccount acc = null;
			if(accType.equals("CreditAccount")){
				acc = new CreditAccount(accName, accBalance);
				acc.withdraw(amount);
			}else if(accType.equals("StudentAccount")){
				acc = new StudentAccount(accName, accBalance);
				acc.withdraw(amount);
			}
			
		}
	 throw new UnknownCustomerException("Customer " + name + " unknown");
	 }
	 public static void TestBank() {
		 Bank b = new Bank("UIC Bank", null);
		 System.out.println(b.totalMoney() == 0);
		 b.addAccount(new CreditAccount("Philippe", 1000));
		 try {
			 System.out.println(b.getMoney("Philippe") == 1000);
			 System.out.println(b.totalMoney() == 1000);
			 b.addAccount(new StudentAccount("Meunier", 1500));
			 System.out.println(b.getMoney("Philippe") == 1000);
			 System.out.println(b.getMoney("Meunier") == 1500);
			 System.out.println(b.totalMoney() == 2500);
			 b.getMoney("Ms. Park");
		 } catch(UnknownCustomerException ex) {
			 System.out.println(ex.getMessage().equals("Customer Ms. Park unknown"));
		 } catch(NotEnoughMoneyException ex) {
			 // This should never happen!
			 System.out.println(false);
		 }
		 try {
			 b.withdraw("Philippe", 500);
			 System.out.println(b.getMoney("Philippe") == 500);
			 System.out.println(b.getMoney("Meunier") == 1500);
			 b.withdraw("Ms. Park", 1);
		 } catch(UnknownCustomerException ex) {
			 System.out.println(ex.getMessage().equals("Customer Ms. Park unknown"));
		 } catch(NotEnoughMoneyException ex) {
			 // This should never happen!
			 System.out.println(false);
		 }
		 try {
			 b.withdraw("Meunier", 2000);
		 } catch(NotEnoughMoneyException ex) {
			 System.out.println(ex.getMessage().equals("Cannot withdraw 2000 from account,only 1500 is available"));
		 } catch(UnknownCustomerException ex) {
			 // This should never happen!
			 System.out.println(false);
		 }
		 try {
			 System.out.println(b.getMoney("Philippe") == 500);
			 System.out.println(b.getMoney("Meunier") == 1500);
		 } catch(UnknownCustomerException ex) {
			 // This should never happen!
			 System.out.println(false);
		 }
	 }
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}