package com.finance.bond.calculator.StudentBondCalculator;

import com.finance.bond.calculator.BondCalculator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StudentBondActivity extends Activity {
    /** Called when the activity is first created. */
	private Bundle myBundle;
	private Intent calculator;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBundle = new Bundle();
        calculator = new Intent(this,BondCalculator.class);
     	calculator.putExtras(myBundle);
    	this.startActivity(calculator);
    	
    	finish();
    }
}