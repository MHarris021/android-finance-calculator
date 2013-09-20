package com.finance.bond.calculator;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.finance.bond.calculator.StudentBondCalculator.R;

public class calculatorPreferences extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	addPreferencesFromResource(R.xml.preferences);
	}
}
