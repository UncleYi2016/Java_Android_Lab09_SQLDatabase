package com.example.java_android_lab09_sqldatabase;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
public class ViewSimple extends TextView implements IModelListener {
	 private Bank model;
	 private ControllerSimple controller;
	 public ViewSimple(Context context, AttributeSet attrs) {
		 super(context, attrs);
	 }
	 public void setMVC(Bank m, ControllerSimple c) {
		 this.model = m;
		 this.controller = c;
		 // The setMVC method registers the view with the model and updates
		 // the text of the ViewSimple to display the total amount of money
		 // in all the bank accounts of the bank.
		 model.addListener(this);
		 notifyModelListener();
	 }
	 // The notifyModelListener method updates the text of the ViewSimple as
	 // necessary so that the ViewSimple always displays the current value of
	 // the total amount of money in all the bank accounts of the bank.
	 public void notifyModelListener() {
		 this.setText("total money: " + model.totalMoney());
	 }
}
