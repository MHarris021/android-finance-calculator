package com.finance.bond.calculator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.finance.PaymentSchedule;
import com.finance.bond.Bond;
import com.finance.bond.BondBundler;
import com.finance.bond.calculator.StudentBondCalculator.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CouponSchedule extends Activity {
    private com.finance.bond.Bond myBond;
    private com.finance.bond.BondBundler myBundler;
    private com.finance.PaymentSchedule mySchedule;
   private RelativeLayout myView;
    private ScrollView myScroll;
    private TableLayout myScrollTable, headerTable, footerTable;
    private LayoutInflater myLI;
    private ArrayList<TableRow> myRows;
    private TableRow header1, footer1, footer2;
    private ArrayList<TextView> myTexts;
    private ArrayList<Double> payments, npvs, rfrates, weights, durations;
    private int length;
    private Locale location;
    private NumberFormat currencyF, percentF, doubleF, intF;
    private Bundle myBundle;
    private Intent myIntent;
	    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	
        initUI();
        setContentView(myView);
        myIntent = getIntent();
        myBundle = myIntent.getExtras();
        myBundler = new BondBundler();
        myBond = new Bond();
    	myBundler.setBond(myBond);
    	myBundler.setBundle(myBundle);
        myBundler.loadBond();
    	location = Locale.getDefault();
    	setFormats(location);
        mySchedule = new PaymentSchedule(myBond);
        mySchedule.generateSchedule();
        length = mySchedule.getLength();
        payments = mySchedule.getPayments();
        npvs = mySchedule.getNpvs();
        rfrates = mySchedule.getrFRates();
        weights = mySchedule.getWeights();
        durations = mySchedule.getDurationpieces();
        buildHeader();
        buildBody();
        buildFooter();
    	myScrollTable.getHeight();
    	createUI();
    }

	/**
	 * 
	 */
	private void createUI() {
		headerTable.addView(header1);
        headerTable.setStretchAllColumns(true);
    	myScroll.setVerticalScrollBarEnabled(true);
    	myScrollTable.setStretchAllColumns(true);
    	footerTable.addView(footer1);
        footerTable.addView(footer2);
        footerTable.setStretchAllColumns(true);
    }

	/**
	 * 
	 */
	private void buildBody() {
		// TODO Auto-generated method stub
		for(int i=0;i<length;i++)
        {
    		myRows.add((TableRow)myLI.inflate(R.layout.couponrow, null));
    	    myTexts.clear();
    	    myTexts.add((TextView)myRows.get(i).findViewById(R.id.crt0));
           	myTexts.get(0).setText(intF.format(i+1));
           	myTexts.add((TextView)myRows.get(i).findViewById(R.id.crt1));
           	myTexts.get(1).setText(currencyF.format(payments.get(i)));
        	myTexts.add((TextView)myRows.get(i).findViewById(R.id.crt2));
            myTexts.get(2).setText(currencyF.format(npvs.get(i)));
        	myTexts.add((TextView)myRows.get(i).findViewById(R.id.crt3));
            myTexts.get(3).setText(percentF.format(rfrates.get(i)));
        	myTexts.add((TextView)myRows.get(i).findViewById(R.id.crt4));
            myTexts.get(4).setText(doubleF.format(weights.get(i)));
        	myTexts.add((TextView)myRows.get(i).findViewById(R.id.crt5));
            myTexts.get(5).setText(doubleF.format(durations.get(i)));
        	myScrollTable.addView(myRows.get(i));
        }
    }

	/**
	 * 
	 */
	private void buildFooter() {
		// TODO Auto-generated method stub
		myTexts.clear();
		myTexts.add((TextView)footer1.findViewById(R.id.crt0));
        myTexts.get(0).setText("Total");
    	myTexts.add((TextView)footer1.findViewById(R.id.crt1));
        myTexts.get(1).setText("Total");
        myTexts.add((TextView)footer1.findViewById(R.id.crt2));
        myTexts.get(2).setText("Total");
        myTexts.add((TextView)footer1.findViewById(R.id.crt3));
        myTexts.get(3).setText("Average");
        myTexts.add((TextView)footer1.findViewById(R.id.crt4));
        myTexts.get(4).setText("Total");
        myTexts.add((TextView)footer1.findViewById(R.id.crt5));
        myTexts.get(5).setText("Total");
		myTexts.clear();
		myTexts.add((TextView)footer2.findViewById(R.id.crt0));
        myTexts.get(0).setText(intF.format(this.length));
    	myTexts.add((TextView)footer2.findViewById(R.id.crt1));
        myTexts.get(1).setText(currencyF.format(mySchedule.getTotalPayment()));
        myTexts.add((TextView)footer2.findViewById(R.id.crt2));
        myTexts.get(2).setText(currencyF.format(mySchedule.getNpv()));
        myTexts.add((TextView)footer2.findViewById(R.id.crt3));
        myTexts.get(3).setText(percentF.format(mySchedule.getAvgRFRate()));
        myTexts.add((TextView)footer2.findViewById(R.id.crt4));
        myTexts.get(4).setText(doubleF.format(mySchedule.getTotalWeight()));
        myTexts.add((TextView)footer2.findViewById(R.id.crt5));
        myTexts.get(5).setText(doubleF.format(mySchedule.getDuration()));
    }

	/**
	 * 
	 */
	private void buildHeader() {
		// TODO Auto-generated method stub
		myTexts.clear();
		myTexts.add((TextView)header1.findViewById(R.id.crt0));
        myTexts.get(0).setText("Period");
    	myTexts.add((TextView)header1.findViewById(R.id.crt1));
        myTexts.get(1).setText("Payment");
    	myTexts.add((TextView)header1.findViewById(R.id.crt2));
        myTexts.get(2).setText("NPV");
    	myTexts.add((TextView)header1.findViewById(R.id.crt3));
        myTexts.get(3).setText("Discount");
    	myTexts.add((TextView)header1.findViewById(R.id.crt4));
        myTexts.get(4).setText("Weight");
    	myTexts.add((TextView)header1.findViewById(R.id.crt5));
        myTexts.get(5).setText("Duration");
    }

	/**
	 * @param location2
	 */
	private void setFormats(Locale location) {
		// TODO Auto-generated method stub
		currencyF = NumberFormat.getCurrencyInstance(location);
        percentF = NumberFormat.getPercentInstance(location);
        percentF.setMinimumFractionDigits(3);
        doubleF = NumberFormat.getNumberInstance(location);
        doubleF.setMinimumFractionDigits(3);
        intF = NumberFormat.getIntegerInstance(location);
	}

	/**
	 * 
	 */
	private void initUI() {
		// TODO Auto-generated method stub
		myLI =  getLayoutInflater();
        myView = (RelativeLayout) myLI.inflate(R.layout.couponschedule,null);
        myScroll = (ScrollView) myView.findViewById(R.id.coupon_schedule_couponschedule);
        myScrollTable = (TableLayout) myScroll.findViewById(R.id.coupon_schedule_scroll_table);
        headerTable = (TableLayout) myView.findViewById(R.id.coupon_schedule_header);
        footerTable = (TableLayout) myView.findViewById(R.id.coupon_schedule_footer);
        myRows = new ArrayList<TableRow>();
        myTexts = new ArrayList<TextView>();
        header1 = (TableRow)myLI.inflate(R.layout.couponrow, null);
        footer1 = (TableRow)myLI.inflate(R.layout.couponrow, null);
		footer2 = (TableRow)myLI.inflate(R.layout.couponrow, null);
    }
}