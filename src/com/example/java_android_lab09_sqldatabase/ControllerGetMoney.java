package com.example.java_android_lab09_sqldatabase;

public class ControllerGetMoney {
	 private Bank model;
	 public ControllerGetMoney(Bank m) {
		 this.model = m;
	 }
	 // The getMoney method takes the name of a bank customer as argument.
	 public String getMoney(String name) {
		 String result;
	 try {
		 // The getMoney method of the controller calls the getMoney method
		 // of the bank to get the amount of money currently stored in the bank
		 // account that belongs to that customer. The getMoney method of the
		 // controller then transforms the integer result of the getMoney method
		 // of the bank into a string and returns that string as result (to the
		 // view which then shows it as a Toast).
		 result = Integer.toString(model.getMoney(name));
	 } catch(UnknownCustomerException ex) {
		 // If the getMoney method of the bank throws an UnknownCustomerException
		 // then the getMoney method of the controller should catch this exception
		 // and return as result the error message from the exception object.
		 result = ex.getMessage();
	 }
	 return result;
	 }
}

