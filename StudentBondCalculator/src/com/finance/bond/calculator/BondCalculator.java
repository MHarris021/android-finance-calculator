package com.finance.bond.calculator;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.finance.Financial_Instrument;
import com.finance.UserInterface;
import com.finance.bond.Bond;
import com.finance.bond.BondBundler;
import com.finance.bond.calculator.StudentBondCalculator.R;


public class BondCalculator extends Activity {
	public static final int DISPLAY_MODE = 0;
	public static final int NUMBER_OF_DAYS = 1;
	public static final int COMPOUNDING_TYPE = 2;
	public static final int MENU_ADVANCED = 1;
    public static final int MENU_STANDARD = 0;
    public static final int MENU_CALCULATE_YIELD = 2;
    public static final int MENU_CALCULATE_RATE = 3;
    public static final int MENU_CALCULATE_DV01= 4;
    public static final int MENU_PREFERENCES = 5;
    private static final String DEFAULT_FACE_VALUE = "Default_Face_Value";
    private static final String DEFAULT_RATE = "Default_Rate";
    private static final String DEFAULT_LENGTH = "Default_Length";
    private static final String DEFAULT_RF_RATE = "Default_RF_Rate";
    private static final String DEFAULT_PERIODS = "Default_Periods";
	private static final String PREF_MODE_START = "Pref_MODE_START";
	private static final String PREF_AUTO_CALCULATE = "Pref_Auto_Calculate";
	private static final String PREF_YIELD_CHANGE = "Pref_Yield_Change";
	private static final String PREF_COMPOUNDING_TYPE = "Pref_Compounding_Type";
	private static final String PREF_DAYS_IN_YEAR = "Pref_DAYS_IN_YEAR";
	private UserInterface myInterface;
	private int mode=0;
	private SharedPreferences myPrefs;
	private SharedPreferences.Editor myPrefsEditor;
	private Bundle myBundle;
    private com.finance.bond.Bond myBond;
	private com.finance.bond.BondBundler myBundler;
    
    private Locale [] defaultLocales;
    private ArrayList<Locale> availableLocales;
    private ArrayList<Currency> supportedCurrencies;
    private ArrayList<String> supportedCurrencySymbols;
    private Locale currentLocale;
    private Locale defaultLocale;
    private Currency currentCurrency;
    private NumberFormat currencyFormat;
    private NumberFormat percentageFormat;
    private NumberFormat integerFormat;
    private NumberFormat decimalFormat;
	
    private ArrayAdapter<String> currencyAdapter;
    private ArrayList<TableLayout> uiTables;
    private ArrayList<TableRow> uiRows;
    private ArrayList<TableRow> uiColumns;
    
    private TextView label1HeaderValue;
    
    private Spinner currencySpinner;
    private TextView bondHeader;
    private TextView localCurrencySymbol;
    private EditText parValue;
    private TextView atParText;
    private EditText faceValue;
    private EditText priceValue;
    private EditText paymentPeriodsValue;
    private EditText timeValue;
    private EditText bondLengthValue;
    private EditText rateValue;
    private EditText periodicRateValue;
    private EditText couponValue;
    private EditText yieldValue;        
    private EditText yieldToCallValue;
    private EditText durationValue;
    private EditText mDurationValue;
    private EditText rfrateValue;
    private EditText dv01Value;
    
    private Button calculateButton, couponButton;
    private RadioGroup calculateSelection;
    private boolean autocalculate;
    private int yield_calc_flag;
	private double default_face_value;
    private double default_rate;
    private double default_rf_rate;
    private double default_length;
    private int default_periods;
    private int default_days;
    private int default_compounding;
	private int prevmode;
	private int calculate_mode;
	
	/**
     * Invoked during init to give the Activity a chance to set up its Menu.
     * 
     * @param menu the Menu to which entries may be added
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_CALCULATE_DV01, MENU_CALCULATE_DV01, this.getResources().getStringArray(R.array.menu_items)[MENU_CALCULATE_DV01]);
        menu.add(0, MENU_PREFERENCES, MENU_PREFERENCES, this.getResources().getStringArray(R.array.menu_items)[MENU_PREFERENCES]);
        menu.add(0, MENU_CALCULATE_YIELD, MENU_CALCULATE_YIELD, this.getResources().getStringArray(R.array.menu_items)[MENU_CALCULATE_YIELD]);
        menu.add(0, MENU_CALCULATE_RATE, MENU_CALCULATE_RATE, this.getResources().getStringArray(R.array.menu_items)[MENU_CALCULATE_RATE]);
        menu.add(0, MENU_ADVANCED, MENU_ADVANCED, this.getResources().getStringArray(R.array.menu_items)[MENU_ADVANCED]);
        menu.add(0, MENU_STANDARD, MENU_STANDARD, this.getResources().getStringArray(R.array.menu_items)[MENU_STANDARD]);
        return true;
    }

    /**
     * Invoked when the user selects an item from the Menu.
     * 
     * @param item the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false
     *         otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	prevmode=mode;
    	mode = item.getItemId();
        if(displayMode())
        	return true;
        else return super.onOptionsItemSelected(item);
    }

    /**
	 * 
	 */
	public void displayPreferencesMode() {
		// TODO Auto-generated method stub
		Intent i = new Intent(this, calculatorPreferences.class);
		startActivity(i);
        
	}

	/**
	 * 
	 */
	public void displayRateMode() {
		// TODO Auto-generated method stub
		setAllFocusable(true);
		myInterface.showAll(uiRows, uiColumns);
		myInterface.setRadioButtonsVisibility(calculateSelection, View.VISIBLE);
		label1HeaderValue.setText(this.getResources().getStringArray(R.array.menu_items)[MENU_CALCULATE_RATE]);
		hideColumn(uiColumns,R.id.column_par_value);
		hideColumn(uiColumns,R.id.column_time_left);
		hideColumn(uiColumns,R.id.column_yield_call);
		hideRow(uiRows,R.id.durationrow);
		hideColumn(uiColumns,R.id.column_current_currency);
		hideColumn(uiColumns,R.id.column_rf_rate);
		hideColumn(uiColumns,R.id.column_dv01);
		rateValue.setFocusable(false);
		couponValue.setFocusable(false);
		myInterface.hideRadioButton(calculateSelection, R.id.Radio_Duration);
		myInterface.hideRadioButton(calculateSelection, R.id.Radio_DV01);
		if(findViewById(calculateSelection.getCheckedRadioButtonId()).getVisibility()!=View.VISIBLE)
			calculateSelection.check(R.id.Radio_Price);
	
	}

	/**
	 * 
	 */
	public void displayDV01Mode() {
		setAllFocusable(true);
		myInterface.showAll(uiRows, uiColumns);
		myInterface.setRadioButtonsVisibility(calculateSelection, View.VISIBLE);
		label1HeaderValue.setText(this.getResources().getStringArray(R.array.menu_items)[MENU_CALCULATE_DV01]);
		hideColumn(uiColumns,R.id.column_par_value);
		hideColumn(uiColumns,R.id.column_time_left);
		hideColumn(uiColumns,R.id.column_yield_call);
		hideColumn(uiColumns,R.id.column_m_duration);
		hideColumn(uiColumns,R.id.column_current_currency);
		hideColumn(uiColumns,R.id.column_rf_rate);
		myInterface.hideRadioButton(calculateSelection, R.id.Radio_Rate);
		myInterface.hideRadioButton(calculateSelection, R.id.Radio_Price);
		if(findViewById(calculateSelection.getCheckedRadioButtonId()).getVisibility()!=View.VISIBLE)
			calculateSelection.check(R.id.Radio_Yield);
	}

	/**
	 * 
	 */
	public void displayYieldMode() {
		setAllFocusable(true);
		myInterface.showAll(uiRows, uiColumns);
		myInterface.setRadioButtonsVisibility(calculateSelection, View.VISIBLE);
		label1HeaderValue.setText(this.getResources().getStringArray(R.array.menu_items)[MENU_CALCULATE_YIELD]);
		hideColumn(uiColumns,R.id.column_par_value);
		hideColumn(uiColumns,R.id.column_time_left);
		hideColumn(uiColumns,R.id.column_yield_call);
		hideColumn(uiColumns,R.id.column_m_duration);
		hideColumn(uiColumns,R.id.column_dv01);
		hideColumn(uiColumns,R.id.column_current_currency);
		hideColumn(uiColumns,R.id.column_rf_rate);
		myInterface.hideRadioButton(calculateSelection, R.id.Radio_Price);
		myInterface.hideRadioButton(calculateSelection, R.id.Radio_DV01);
		if(findViewById(calculateSelection.getCheckedRadioButtonId()).getVisibility()!=View.VISIBLE)
			calculateSelection.check(R.id.Radio_Rate);
	}

	/**
	 * 
	 */
	public void displayAdvancedMode() {
		setAllFocusable(true);
		myInterface.showAll(uiRows, uiColumns);
		hideColumn(uiColumns,R.id.column_yield_call);
		myInterface.setRadioButtonsVisibility(calculateSelection, View.VISIBLE);
		label1HeaderValue.setText(this.getResources().getStringArray(R.array.menu_items)[MENU_ADVANCED]);
		if(findViewById(calculateSelection.getCheckedRadioButtonId()).getVisibility()!=View.VISIBLE)
			calculateSelection.check(R.id.Radio_Price);
	}

	/**
	 * 
	 */
	public void displayStandardMode() {
		setAllFocusable(true);
		myInterface.showAll(uiRows, uiColumns);
		
		myInterface.setRadioButtonsVisibility(calculateSelection, View.VISIBLE);
		label1HeaderValue.setText(this.getResources().getStringArray(R.array.menu_items)[MENU_STANDARD]);
		hideColumn(uiColumns,R.id.column_par_value);
		hideColumn(uiColumns,R.id.column_time_left);
		hideColumn(uiColumns,R.id.column_yield_call);
		hideRow(uiRows,R.id.durationrow);
		hideColumn(uiColumns,R.id.column_dv01);
		hideColumn(uiColumns,R.id.column_current_currency);
		hideColumn(uiColumns,R.id.column_rf_rate);
		myInterface.hideRadioButton(calculateSelection, R.id.Radio_Duration);
		myInterface.hideRadioButton(calculateSelection, R.id.Radio_DV01);
		if(findViewById(calculateSelection.getCheckedRadioButtonId()).getVisibility()!=View.VISIBLE)
			calculateSelection.check(R.id.Radio_Price);
	}
	
	
	public void hideColumn(ArrayList<TableRow> columns, int resId) {
			int columnId = columns.indexOf((TableRow)findViewById(resId));
		myInterface.hideColumn(columns, columnId);
	}
	public void showColumn(ArrayList<TableRow> columns, int resId) {
		int columnId = columns.indexOf((TableRow)findViewById(resId));
		myInterface.showColumn(columns, columnId);
	}
	public void hideRow(ArrayList<TableRow> rows, int resId) {
		int rowId = rows.indexOf((TableRow)findViewById(resId));
		myInterface.hideRow(rows, rowId);
	}
	public void showRow(ArrayList<TableRow> rows, int resId) {
		int rowId = rows.indexOf((TableRow)findViewById(resId));
		myInterface.showRow(rows, rowId);
	}

	public void initUI(Locale location){
    	label1HeaderValue =(TextView)findViewById(R.id.Label1_Value_Text);
    	uiTables = new ArrayList<TableLayout>();
    	initTables();
    	
    	uiRows = new ArrayList<TableRow>();
    	uiRows = myInterface.initTableRows(uiTables);
    	uiColumns = new ArrayList<TableRow>();
    	uiColumns = myInterface.initTableColumns(uiRows);
    	
    	bondHeader =(TextView)findViewById(R.id.bond_label_text);
    	currentLocale = location;
        currentCurrency = Currency.getInstance(location);
    	localCurrencySymbol =(TextView)findViewById(R.id.LCSymbol);
        localCurrencySymbol.setText(currentCurrency.getSymbol());
        
        currencySpinner = (Spinner) findViewById(R.id.CurrencyType);
        setFormats(location);
             
        initEditTexts();
        initButtons();
        calculateSelection =(RadioGroup)findViewById(R.id.Calculate_Selection);
        calculateSelection.setOnCheckedChangeListener(selection_listener);
        calculateSelection.clearCheck();
     }
    
    /**
	 * @param location
	 */
	private void setFormats(Locale location) {
        currencyFormat = NumberFormat.getCurrencyInstance(location);
        percentageFormat = NumberFormat.getPercentInstance(location);
        percentageFormat.setMinimumFractionDigits(3);
        integerFormat =NumberFormat.getIntegerInstance(location);
        decimalFormat =NumberFormat.getNumberInstance(location);
        decimalFormat.setMinimumFractionDigits(2);
	}

	/**
	 * 
	 */
	private void initButtons() {
		calculateButton =(Button)findViewById(R.id.calculate_button);
        calculateButton.setOnClickListener(calculate_listener);
        couponButton =(Button)findViewById(R.id.CouponButton);
        couponButton.setOnClickListener(coupon_listener);    
	}

	/**
	 * 
	 */
	
	private EditText initEditText(int resId)
	{
		EditText editbox = (EditText)findViewById(resId);
		
		editbox.setOnEditorActionListener(editText_listener);
        editbox.setOnClickListener(hide_buttons_listener);
        editbox.setOnFocusChangeListener(show_buttons_listener);
        return editbox;
	}
	private void initEditTexts() {
		parValue = initEditText(R.id.parvalue);
		atParText =(TextView)findViewById(R.id.atpartext);
		faceValue=initEditText( R.id.facevalue);
		priceValue=initEditText( R.id.pricevalue);
		paymentPeriodsValue=initEditText( R.id.payment_periods_value);
		timeValue=initEditText( R.id.timevalue);
		bondLengthValue=initEditText( R.id.bond_length_value);
		rateValue=initEditText( R.id.ratevalue);
		periodicRateValue=initEditText( R.id.periodicratevalue);
		couponValue=initEditText( R.id.couponvalue);
		yieldValue=initEditText( R.id.yieldvalue);
		yieldToCallValue=initEditText( R.id.yield_to_call_value);
		durationValue=initEditText( R.id.durationvalue);
		mDurationValue=initEditText( R.id.m_duration_value);
		rfrateValue=initEditText( R.id.rfratevalue);
		dv01Value=initEditText( R.id.dv01value);
	}

	/**
	 * 
	 */
	private void initTables() {
		// TODO Auto-generated method stub
		uiTables.add((TableLayout)findViewById(R.id.Label_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Scroll_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Header_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Currency_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Par_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Price_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Time_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Rate_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Coupon_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Yield_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Duration_Table));
    	uiTables.add((TableLayout)findViewById(R.id.RFDV_Table));
    	uiTables.add((TableLayout)findViewById(R.id.Button_Table));
	}
	
	protected double getCurrencyValue(String text)
	{
		double value=0;
		try {
			if(text.contains(currentCurrency.getSymbol()))
				value = currencyFormat.parse(text).doubleValue();
			else value = decimalFormat.parse(text).doubleValue();
			}
		catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return value;
	}

	protected double getPercentageValue(String text)
	{
		double value=0;
		try {
			if(text.contains("%"))
				value = percentageFormat.parse(text).doubleValue();
			else 
			{
				value = decimalFormat.parse(text).doubleValue();
				if((!text.startsWith("0"))&&(!text.startsWith(".")))
				{
					value/=100.0;
				}
			}
		}
		catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return value;
	}

	protected void setBondHeader(Bond myBond) {
		String text = myBond.getBondType();
		bondHeader.setText(text);
   	}
	
	protected void setParValue(Bond myBond) {
		String text = currencyFormat.format(myBond.getParValue());
		InputFilter[] filter= parValue.getFilters();
		parValue.setFilters(filter);
		parValue.setText(text);
	}
	
	protected void setParText(Bond myBond) {
		String text =resolvePar(myBond.getPar());
		atParText.setText(text);
    }

	protected void setFaceValue(Bond myBond) {
		faceValue.setText(currencyFormat.format(myBond.getFaceValue()));
	}

	protected void setPrice(Bond myBond) {
		priceValue.setText(currencyFormat.format(myBond.getPrice()));
    }
	
	protected void setPeriods(Bond myBond) {
		paymentPeriodsValue.setText(integerFormat.format(myBond.getPayoutPeriodsPerYear()));
	}

	protected void setTimeLeft(Bond myBond) {
		timeValue.setText(decimalFormat.format(myBond.getTimeLeft()));
	}

	protected void setBondLength(Bond myBond) {
		bondLengthValue.setText(decimalFormat.format(myBond.getBondLength()));
	}

	protected void setRate(Bond myBond) {
	    rateValue.setText(percentageFormat.format(myBond.getRate()));
	}

	protected void setPeriodicRate(Bond myBond) {
	    periodicRateValue.setText(percentageFormat.format(myBond.getRate()/myBond.getPayoutPeriodsPerYear()));
	}

	protected void setCouponRate(Bond myBond) {
	    couponValue.setText(currencyFormat.format(myBond.getCouponRate()));
	}
	
	protected void setYield(Bond myBond2) {
    	yieldValue.setText(percentageFormat.format(myBond.getYieldtoMaturity()));
	}
	
	protected void setYieldToCall(Bond myBond) {
        yieldToCallValue.setText(percentageFormat.format(myBond.getYieldtoCall()));
	}

	protected void setDuration(Bond myBond) {
        durationValue.setText(decimalFormat.format(myBond.getDuration()));
	}
	
	
	protected void setMDuration(Bond myBond) {
        mDurationValue.setText(decimalFormat.format(myBond.getMDuration()));
	}

	protected void setDV01(Bond myBond) {
        dv01Value.setText(currencyFormat.format(myBond.getDV01()));
	}

	protected void setRFRate(Bond myBond) {
        rfrateValue.setText(percentageFormat.format(myBond.getRFRate()));
	}

	public void setUIValues(Bond myBond){
    	setBondHeader(myBond);
    	setParValue(myBond);
    	setParText(myBond);
    	setFaceValue(myBond);
    	setPrice(myBond);
    	setPeriods(myBond);
    	setTimeLeft(myBond);
    	setBondLength(myBond);
    	setRate(myBond);
    	setPeriodicRate(myBond);
    	setCouponRate(myBond);
        setYield(myBond);
        setYieldToCall(myBond);
        setDuration(myBond);
        setMDuration(myBond);
        setRFRate(myBond);
        setDV01(myBond);
        uiTables.get(uiTables.indexOf(findViewById(R.id.Button_Table))).setVisibility(View.VISIBLE);
        setAllFocusable(true);
    }
    
   	/**
	 * @param b
	 */
	public void setAllFocusable(boolean b) {
		// TODO Auto-generated method stub
		if(!b)
		{
			parValue.setFocusable(b);
			faceValue.setFocusable(b);
			priceValue.setFocusable(b);
			paymentPeriodsValue.setFocusable(b);
			timeValue.setFocusable(b);
			bondLengthValue.setFocusable(b);
			rateValue.setFocusable(b);
			periodicRateValue.setFocusable(b);
			couponValue.setFocusable(b);
			rfrateValue.setFocusable(b);
			currencySpinner.setFocusable(b);
		}
		else if(b)
		{
			parValue.setFocusableInTouchMode(b);
			faceValue.setFocusableInTouchMode(b);
			priceValue.setFocusableInTouchMode(b);
			paymentPeriodsValue.setFocusableInTouchMode(b);
			timeValue.setFocusableInTouchMode(b);
			bondLengthValue.setFocusableInTouchMode(b);
			rateValue.setFocusableInTouchMode(b);
			periodicRateValue.setFocusableInTouchMode(b);
			couponValue.setFocusableInTouchMode(b);
			rfrateValue.setFocusableInTouchMode(b);
			currencySpinner.setFocusableInTouchMode(b);
		}
	}

	public void createCurrencySpinner(Context context, ArrayAdapter<String> currencyAdapter2,
			Spinner currencySpinner2, ArrayList<String> supportedCurrencies2,
			int listItem) {
		// TODO Auto-generated method stub
		currencyAdapter2 =new ArrayAdapter<String>(context, listItem, supportedCurrencies2);
		currencyAdapter2.setDropDownViewResource(listItem);
        currencySpinner2.setAdapter(currencyAdapter2);
        currencySpinner2.setSelection(currencyAdapter2.getPosition(Currency.getInstance(Locale.getDefault()).getCurrencyCode()));
	}

	public void setBondValues(Bond myBond){
    	try {
			myBond.setParValue((currencyFormat.parse(parValue.getText().toString()).doubleValue()));
		} catch (ParseException e7) {
			// TODO Auto-generated catch block
			e7.printStackTrace();
		}
    	try {
			myBond.setFaceValue((currencyFormat.parse(faceValue.getText().toString()).doubleValue()));
		} catch (ParseException e6) {
			// TODO Auto-generated catch block
			e6.printStackTrace();
		}
    	try {
			myBond.setPrice((currencyFormat.parse(priceValue.getText().toString()).doubleValue()));
		} catch (ParseException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		}
    	myBond.setPayoutPeriodsPerYear(Integer.valueOf(paymentPeriodsValue.getText().toString()));
        myBond.setTimeLeft(Double.valueOf(timeValue.getText().toString()));
        myBond.setBondLength(Double.valueOf(bondLengthValue.getText().toString()));
        try {
			myBond.setRate((percentageFormat.parse(rateValue.getText().toString()).doubleValue()));
		} catch (ParseException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
        try {
			myBond.setCouponRate((currencyFormat.parse(couponValue.getText().toString()).doubleValue()));
		} catch (ParseException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        try {
			myBond.setYieldtoMaturity((percentageFormat.parse(yieldValue.getText().toString()).doubleValue()));
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        try {
			myBond.setYieldtoCall((percentageFormat.parse(yieldToCallValue.getText().toString()).doubleValue()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        myBond.setDuration(Double.valueOf(durationValue.getText().toString()));
        myBond.setMDuration(Double.valueOf(mDurationValue.getText().toString()));
        try {
			myBond.setRFRate((percentageFormat.parse(rfrateValue.getText().toString()).doubleValue()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        myBond.setDV01(Double.valueOf(dv01Value.getText().toString()));
        myBond.calcPar();
    }
	
	public OnFocusChangeListener show_buttons_listener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
		/*	if(v.hasFocus()==false)
				uiTables.get(uiTables.indexOf(findViewById(R.id.Button_Table))).setVisibility(View.VISIBLE);
			*/
		}
		
	};
	
	public OnClickListener hide_buttons_listener = new OnClickListener() {
		
		
		@Override
		public void onClick(View v) {
			
		/*	if(BondCalculator.this.getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
			{
				uiTables.get(uiTables.indexOf(findViewById(R.id.Button_Table))).setVisibility(View.GONE);
	    		
			}
			else uiTables.get(uiTables.indexOf(findViewById(R.id.Button_Table))).setVisibility(View.VISIBLE);*/	
		}
	};
    
	public OnEditorActionListener editText_listener = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			String textString =v.getText().toString();
			double textValue;
			boolean autoEnter=autocalculate;
			if(!textString.equals("")){
				try {
					switch(v.getId())
					{
					case R.id.parvalue:
						textValue = getCurrencyValue(textString);
						if(textValue!=myBond.getParValue())
						{
							myBond.setParValue(textValue);
							if(autoEnter)
							{
								myBond.parValueUpdated();
								setUIValues(myBond);
							}
						}
						else setParValue(myBond);
						break;
					case R.id.facevalue:
						textValue = getCurrencyValue(textString);
						if(textValue!=myBond.getFaceValue())
						{
							myBond.setFaceValue((textValue));
							setFaceValue(myBond);
							if(autoEnter)
							{
								myBond.faceValueUpdated();
								setUIValues(myBond);
							}
						}
						else setFaceValue(myBond);
						break;
					case R.id.pricevalue:
						textValue = getCurrencyValue(textString);
						if(textValue!=myBond.getPrice())
						{
							myBond.setPrice((textValue));
							setPrice(myBond);
							if(autoEnter){
								myBond.priceUpdated();
								setUIValues(myBond);
							}
						}
						else setPrice(myBond);
						break;
					case R.id.payment_periods_value:
						textValue = integerFormat.parse(textString).intValue();
						if(textValue!=myBond.getPayoutPeriodsPerYear())
						{
							myBond.setPayoutPeriodsPerYear(new Double(textValue).intValue());
							setPeriods(myBond);
							if(autoEnter){
								myBond.periodsUpdated();
								setUIValues(myBond);
							}
						}
						else setPeriods(myBond);
						break;
					case R.id.timevalue:
						textValue = decimalFormat.parse(textString).doubleValue();
						if(textValue!=myBond.getTimeLeft())
						{
							myBond.setTimeLeft(textValue);
							setTimeLeft(myBond);
							if(autoEnter){
								myBond.timeLeftUpdated();
								setUIValues(myBond);
							}
						}
						else setTimeLeft(myBond);
						break;
					case R.id.bond_length_value:
						textValue = decimalFormat.parse(textString).doubleValue();
						if(textValue!=myBond.getBondLength())
						{
							myBond.setBondLength(textValue);
							setBondLength(myBond);
							if(autoEnter){
								myBond.bondLengthUpdated();
								setUIValues(myBond);
							}
						}
						else setBondLength(myBond);
						break;
					case R.id.ratevalue:
						textValue = getPercentageValue(textString);
						if(textValue!=myBond.getRate())
						{
							myBond.setRate((textValue));
							setRate(myBond);
							if(autoEnter){
								myBond.rateUpdated();
								setUIValues(myBond);
							}
						}
						else setRate(myBond);
						break;
					case R.id.couponvalue:
						textValue = getCurrencyValue(textString);
						if(textValue!=myBond.getCouponRate())
						{
							myBond.setCouponRate((textValue));
							setCouponRate(myBond);
							if(autoEnter){
								myBond.couponUpdated();
								setUIValues(myBond);
							}
						}
						else setCouponRate(myBond);
						break;
					case R.id.periodicratevalue:
						textValue = getPercentageValue(textString);
						if(textValue!=(myBond.getRate()/myBond.getPayoutPeriodsPerYear()))
						{
							myBond.setRate((textValue*myBond.getPayoutPeriodsPerYear()));
							setPeriodicRate(myBond);
							setRate(myBond);
							if(autoEnter){
								myBond.rateUpdated();
								setUIValues(myBond);
							}
						}
						else setPeriodicRate(myBond);
						break;
					case R.id.yieldvalue:
						textValue = getPercentageValue(textString);
						if(textValue!=myBond.getYieldtoMaturity())
						{
							myBond.setYieldtoMaturity(textValue);
							setYield(myBond);
							if(autoEnter){
								myBond.yieldUpdated(yield_calc_flag);
								setUIValues(myBond);
							}
						}
						else setYield(myBond);
						break;
					case R.id.rfratevalue:
						textValue = getPercentageValue(textString);
						if(textValue!=myBond.getRFRate())
						{
							myBond.setRFRate((textValue));
							setRFRate(myBond);
							if(autoEnter){
								myBond.rFRateUpdated();
								setUIValues(myBond);
							}
						}
						else setRFRate(myBond);
						break;
					}
					v.clearFocus();
					if(event==null) v.requestFocus();
					else{
						v.focusSearch(View.FOCUS_DOWN).requestFocus();
						return true;
					}
					//uiTables.get(uiTables.indexOf(findViewById(R.id.Button_Table))).setVisibility(View.VISIBLE);
					return false;
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
	};
		
	public OnCheckedChangeListener selection_listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			setAllFocusable(true);
			setUIValues(myBond);
			switch(arg1)
			{
			case R.id.Radio_Price:
				calculate_mode =0; 
				break;
			case R.id.Radio_Rate:
				calculate_mode =1; 
				break;
			case R.id.Radio_Yield:
				calculate_mode =2; 
				break;
			case R.id.Radio_Duration:
				calculate_mode =3; 
				break;
			case R.id.Radio_DV01:
				calculate_mode =4; 
				break;
			}
		}
	};
	
	public OnClickListener calculate_listener = new OnClickListener() {
	    @Override
		public void onClick(View v) {
	        // Perform action on clicks
	    	//Button button = (Button)v;
	    	switch(calculate_mode)
	    	{
	    	case 0: myBond.calcPrice();
	    		myBond.priceUpdated();
	    		break;
	    	case 1: myBond.calcRateFromYield();
	    		myBond.rateUpdated();
	    		break;
	    	case 2: myBond.calcYieldM();
	    		myBond.calcYieldC();
	    		break;
	    	case 3: myBond.calcDuration();
	    		myBond.calcMDuration();
	    		break;
	    	case 4: myBond.calcDV01();
	    		break;
	    	}
	    	setUIValues(myBond);
	    }
	};
	
	public OnClickListener coupon_listener = new OnClickListener() {
	    @Override
		public void onClick(View v) {
	     	Intent coupon = new Intent(BondCalculator.this,CouponSchedule.class);
	     	myBundler.saveBond();
	    	coupon.putExtras(myBundle);
	    	BondCalculator.this.startActivity(coupon);
	    }
	};
	
        public OnItemSelectedListener currency_selector =  new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
	            int position, long id) {
	    	currencySpinner.setSelection(position);
	    	currentCurrency = Currency.getInstance(supportedCurrencySymbols.get(position));
	    	myBond.setCurrency(currentCurrency);
	    	currentLocale = availableLocales.get(supportedCurrencies.indexOf(currentCurrency));
	    	setFormats(currentLocale);
	       	setUIValues(myBond);
	    }
	
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	};
		public OnSharedPreferenceChangeListener pref_listener = new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub
				
				/*int prev_compounding = default_compounding;
				if(key.compareTo(PREF_COMPOUNDING_TYPE)==0)
				{
					default_compounding = Integer.parseInt(sharedPreferences.getString(PREF_COMPOUNDING_TYPE, "0"));
					if(prev_compounding!=default_compounding)
					{
						myBond.setCompoundingType(default_compounding);
						myBond.compoundingUpdated();
						setUIValues(myBond);
					}
			    }*/
				if(key.compareTo(PREF_YIELD_CHANGE)==0)
			    {
			    	yield_calc_flag = Integer.parseInt(sharedPreferences.getString(PREF_YIELD_CHANGE, "0"));
				}else if(key.compareTo(PREF_AUTO_CALCULATE)==0)
			    {
					autocalculate = myPrefs.getBoolean(PREF_AUTO_CALCULATE, true);
			    }
			}
			
		};
		
	protected void initPrefs(int myMode)
	{
		myPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	myPrefs.registerOnSharedPreferenceChangeListener(pref_listener );
    	myPrefsEditor = myPrefs.edit();
    	if(!myPrefs.contains(PREF_MODE_START))
    	{
    		Intent i = new Intent(this, calculatorPreferences.class);
    		startActivity(i);
    	}
    	if(myMode==-1)
    		mode = Integer.parseInt(myPrefs.getString(PREF_MODE_START, "0"));
    	else mode = myMode;
    	autocalculate = myPrefs.getBoolean(PREF_AUTO_CALCULATE, true);
    	yield_calc_flag = Integer.parseInt(myPrefs.getString(PREF_YIELD_CHANGE, "0"));
    	default_compounding = Financial_Instrument.SIMPLE;//Integer.parseInt(myPrefs.getString(PREF_COMPOUNDING_TYPE, "0"));
    	try {
			default_face_value = currencyFormat.parse(myPrefs.getString(DEFAULT_FACE_VALUE, "$1000")).doubleValue();
		} catch (ParseException e) {
			myPrefsEditor.putString(DEFAULT_FACE_VALUE, "$1000");
			myPrefsEditor.commit();
			e.printStackTrace();
		}
		try {
			default_rate = percentageFormat.parse(myPrefs.getString(DEFAULT_RATE, "5.00%")).doubleValue();
		} catch (ParseException e) {
			myPrefsEditor.putString(DEFAULT_RATE, "5.00%");
			myPrefsEditor.commit();
			e.printStackTrace();
		}
		try {
			default_rf_rate = percentageFormat.parse(myPrefs.getString(DEFAULT_RF_RATE, "3.00%")).doubleValue();
		} catch (ParseException e) {
			myPrefsEditor.putString(DEFAULT_RF_RATE, "3.00%");
			myPrefsEditor.commit();
			e.printStackTrace();
		}
		try {
			default_length = Double.parseDouble(myPrefs.getString(DEFAULT_LENGTH, "10.00"));
		} catch (Exception e) {
			myPrefsEditor.putString(DEFAULT_LENGTH, "10.00");
			myPrefsEditor.commit();
			e.printStackTrace();
		}
		try {
			default_periods = Integer.parseInt(myPrefs.getString(DEFAULT_PERIODS, "2"));
		} catch (Exception e) {
			myPrefsEditor.putString(DEFAULT_PERIODS, "2");
			myPrefsEditor.commit();
			e.printStackTrace();
		}
    	
	}
	
	public void createBond()
	{
		myBond.setFaceValue(default_face_value);
		myBond.setPrice(default_face_value);
		myBond.setFaceValue(default_face_value);
		myBond.setRate(default_rate);
		myBond.setBondLength(default_length);
		myBond.setTimeLeft(default_length);
		myBond.setPayoutPeriodsPerYear(default_periods);
    	myBond.setCompoundingType(default_compounding);
    	myBond.setRFRate(default_rf_rate);
    	myBond.compoundingUpdated();
    }
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	defaultLocale = currentLocale = Locale.getDefault();
        setFormats(defaultLocale);
        if(savedInstanceState==null)
        	initPrefs(-1);
        else initPrefs(savedInstanceState.getInt("Mode", 0));
    	myBundle=new Bundle();
    	calculate_mode=0;
    	setContentView(R.layout.calcview);
    	myBond = new Bond();
    	myBundler = new BondBundler();
    	myBundler.setBond(myBond);
    	myBundler.setBundle(myBundle);
    	createBond();
    	defaultLocales = Locale.getAvailableLocales();
        availableLocales =createAvailableLocals(defaultLocales);
        currentCurrency =Currency.getInstance(currentLocale);
        myInterface = new UserInterface(this);
        supportedCurrencies = myInterface.getSupportedCurrencies(availableLocales);
        supportedCurrencySymbols =myInterface.getSupportedCurrencySymbols(supportedCurrencies);
        initUI(defaultLocale);
    	createCurrencySpinner(this,currencyAdapter, currencySpinner, supportedCurrencySymbols, R.layout.list_item);
    	setUIValues(myBond);
    	calculateSelection.check(R.id.Radio_Rate);
    	displayMode();
        currencySpinner.setOnItemSelectedListener(currency_selector);
    }
		

	private boolean displayMode() {
		// TODO Auto-generated method stub
		switch (mode) {
        case MENU_CALCULATE_DV01:
            displayDV01Mode();
        	return true;
        case MENU_PREFERENCES:
        	displayPreferencesMode();
        	mode=prevmode;
    		return true;
        case MENU_CALCULATE_YIELD:
        	displayYieldMode();
        	return true;
        case MENU_CALCULATE_RATE:
        	displayRateMode();
        	return true;
        case MENU_ADVANCED:
        	displayAdvancedMode();
        	return true;
        case MENU_STANDARD:
        	displayStandardMode();
            return true;
		}
		return false;
	}

	/**
	 * @param defaultLocales
	 */
	private ArrayList<Locale> createAvailableLocals(Locale[] defaultLocales) {
		// TODO Auto-generated method stub
		int len = defaultLocales.length;
        ArrayList<Locale> supportedLocales = new ArrayList<Locale>();
        Locale currentLocale;
        for(int i =0; i<len; i++)
        {
        	currentLocale = defaultLocales[i];
        	if(currentLocale.getISO3Country()!="")
        	{
        		try
        		{
        			supportedLocales.add(currentLocale);
        		}
        		catch (Exception e)
        		{
           		}
        	}
        }
    return supportedLocales;
	}

	/**
     * Invoked when the Activity loses user focus.
     */
   @Override
    protected void onPause() {
        super.onPause();
    	myBundler.saveBond();
    }

    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state.
     * 
     * @param outState a Bundle into which this Activity should save its state
     */
   /*@Override
    protected void onResume() {
       super.onResume();
       myBundler.loadBond();
    }*/
   
   @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
       super.onRestoreInstanceState(savedInstanceState);
       mode=savedInstanceState.getInt("Mode", 0);
       BondBundler myBundler2 = new BondBundler();
       myBundler2.setBond(myBond);
       myBundler2.setBundle(savedInstanceState);
       myBundler2.loadBond();
       
       Log.w(this.getClass().getName(), "Restore called");
   }

   @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Mode", mode);
        BondBundler myBundler2 = new BondBundler();
        myBundler2.setBond(myBond);
        myBundler2.setBundle(outState);
        myBundler2.saveBond();
        Log.w(this.getClass().getName(), "SIS called");
    }
	
	public String resolvePar(int par) {
    	return(this.getResources().getStringArray(R.array.ParStrings)[par]);
    }

}
