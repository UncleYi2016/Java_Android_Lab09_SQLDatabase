package com.example.java_android_lab09_sqldatabase;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class ViewGetMoney extends Button implements IModelListener {
	 private Bank model;
	 private ControllerGetMoney controller;
	 public ViewGetMoney(Context context, AttributeSet attrs) {
		 super(context, attrs);
	 }
	 public void setMVC(Bank m, ControllerGetMoney c) {
		 this.model = m;
		 this.controller = c;
		 // The setMVC method registers the view with the model.
		 model.addListener(this);
		 // The user can type in the EditView the name of a bank account customer.
		 // When the user then clicks on the ViewGetMoney button, the action
		 // listener of the button should read the name of the customer that was
		 // typed in the EditView and calls the getMoney method of the controller
		 // with the name of that customer as argument. The getMoney method of
		 // the controller returns a string as result which should then be
		 // displayed back to the user using a Toast.
	 	 this.setOnClickListener(new OnClickListener() {
			 public void onClick(View v) {
				 Activity a = (Activity)v.getContext();
				 EditText et = (EditText)a.findViewById(R.id.editText1);
				 String name = et.getText().toString();
				 String result = controller.getMoney(name);
				 Toast.makeText(v.getContext(), result, Toast.LENGTH_SHORT).show();
			 }
	 	 });
	 }
	 // Does nothing, because the ViewGetMoney class does not graphically display
	 // any data from the bank (the model).
	 public void notifyModelListener() {
	 }
}

