/**
 * 
 */
package com.finance;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import android.content.Context;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * @author DarcWynd
 *
 */
public class UserInterface extends View{
	
	private Context myContext;
	/**
	 * @param context
	 */
	public UserInterface(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.myContext = context;
	}

	/**
	 * @param myContext the myContext to set
	 */
	public void setMyContext(Context myContext) {
		this.myContext = myContext;
	}
	/**
	 * @return the myContext
	 */
	public Context getMyContext() {
		return myContext;
	}
	
	public void setRadioButtonsVisibility(RadioGroup rG, int visibility){
		int i,length;
		length = rG.getChildCount();
		for(i=0;i<length;i++)
		{
			try
			{
				rG.getChildAt(i).setVisibility(visibility);
			}
			catch (Exception e)
			{
			
			}
		}
	}
	
	public void showRadioButton(RadioGroup rG, int resId){
		try
		{
			rG.findViewById(resId).setVisibility(View.VISIBLE);
		}
		catch (Exception e)
		{
		
		}
	}
	
	public void hideRadioButton(RadioGroup rG, int resId){
		try
		{
			rG.findViewById(resId).setVisibility(View.GONE);
		}
		catch (Exception e)
		{
		
		}
	}
	
	public void setSameTextViews(ArrayList<TextView> textViews, String value){
    	for(int i =0; i< textViews.size(); i++)
    	{
    		textViews.get(i).setText(value);
    	}
    }
	public ArrayList<TableRow> initTableRows(ArrayList<TableLayout> tables) {
		// TODO Auto-generated method stub
		int i,j,length,depth;
		ArrayList<TableRow> tableRows = new ArrayList<TableRow>();
		length = tables.size();
		for(i=0; i<length;i++)
		{
			depth = tables.get(i).getChildCount();
			for(j = 0; j<depth;j++)
			{
				try
				{
					tableRows.add((TableRow)tables.get(i).getChildAt(j));
				}
				catch(Exception e)
				{}
			}
		}
		return tableRows;
	}

	/**
	 * @param uiTables2
	 * @return
	 */
	public ArrayList<TableRow> initTableColumns(ArrayList<TableRow> tableRows) {
		// TODO Auto-generated method stub
		int i,j,length,depth;
		ArrayList<TableRow> columns = new ArrayList<TableRow>();
		length = tableRows.size();
		for(i=0; i<length;i++)
		{
			depth = tableRows.get(i).getChildCount();
			for(j = 0; j<depth;j++)
			{
				try
				{
					columns.add((TableRow)tableRows.get(i).getChildAt(j));
				}
				catch(Exception e)
				{}
			}
		}
		return columns;
	}

	public ArrayList<Currency> getSupportedCurrencies(ArrayList<Locale> locales) {
		// TODO Auto-generated method stub
    	int len = locales.size();
        ArrayList<Currency> supportedCurrencies = new ArrayList<Currency>();
        supportedCurrencies.ensureCapacity(len);
        //= getResources().getStringArray(R.array.locales);
        Locale currentLocale;
        Currency currentCurrency;
        for(int i =0; i<len; i++)
        {
        	currentLocale = locales.get(i);
        	if(currentLocale.getISO3Country()!=""){
        		try{
        			currentCurrency = Currency.getInstance(currentLocale);
        			if(currentCurrency.getSymbol()!= ""){
            			if(!supportedCurrencies.contains(currentCurrency))
            				supportedCurrencies.add(currentCurrency);
            		}
        		}catch (Exception e)
        		{
        			
        		}
        	}
        }
        return supportedCurrencies;
	}
	
	public ArrayList<String> getSupportedCurrencySymbols(ArrayList<Currency> availableCurrencies) {
		// TODO Auto-generated method stub
    	int len = availableCurrencies.size();
    	ArrayList<String> supportedCurrencySymbols = new ArrayList<String>();
        //= getResources().getStringArray(R.array.locales);
        Currency currentCurrency;
        for(int i =0; i<len; i++)
        {
        	try
        	{
        		currentCurrency = availableCurrencies.get(i);
        		if(currentCurrency.getCurrencyCode()!= "")
        		{
            		if(!supportedCurrencySymbols.contains(currentCurrency.getCurrencyCode()))
            		{
            			supportedCurrencySymbols.add(currentCurrency.getCurrencyCode());
            		}
            	}
        	}catch (Exception e)
        	{
        	}
        	
        }
        return supportedCurrencySymbols;
	}
	
	public double decimalConverter(double rate, double converter) {
		// TODO Auto-generated method stub
		return rate*converter;
	}
	public void setTableFocus(TableLayout table)
	{
		int length = table.getChildCount();
		int i=0;
		for(i=0;i<length;i++)
		{
			setFocusByVisibility(table.getChildAt(i));
		}
	}
	
	public void setGroupFocusByVisibility(ArrayList<TableRow> group)
	{
		int length = group.size();
		int i,j, depth;
		TableRow child;
		for(i=0;i<length;i++)
		{
			child = group.get(i);
			depth=child.getChildCount();
			for(j=0;j<depth;j++)
			{
				setFocusByVisibility(child.getChildAt(j));
			}
		}
	}
	
	public void setFocusByVisibility(View view) {
		// TODO Auto-generated method stub
		if(view.getVisibility()==View.GONE)
		{
			view.setFocusable(false);
			view.setFocusableInTouchMode(false);
		}
		else if(view.getVisibility()==View.VISIBLE)
		{
			view.setFocusableInTouchMode(true);
			view.setFocusable(true);
		}
	}
	
	public void showColumn(ArrayList<TableRow> columns, int columnId) {
		// TODO Auto-generated method stub
		columns.get(columnId).setVisibility(View.VISIBLE);
   	}
    
	/**
	 * @param columnParValue
	 */
	public void hideColumn(ArrayList<TableRow> columns, int columnId) {
		// TODO Auto-generated method stub
		columns.get(columnId).setVisibility(View.GONE);
   	}

	/**
	 * 
	 */
    
	public void hideAllColumns(ArrayList<TableRow> columns) {
		// TODO Auto-generated method stub
		int length=columns.size();
		for(int i=0;i<length;i++)
		{
			columns.get(i).setVisibility(View.GONE);
		}
	}

	public void showAllColumns(ArrayList<TableRow> columns) {
		// TODO Auto-generated method stub
		int length=columns.size();
		for(int i=0;i<length;i++)
		{
			columns.get(i).setVisibility(View.VISIBLE);
		}
	}
	
	public void showAll(ArrayList<TableRow> rows, ArrayList<TableRow> columns) {
		// TODO Auto-generated method stub
		showAllRows(rows);
		showAllColumns(columns);
	}
	
	public void showRow(ArrayList<TableRow> rows, int rowId) {
		// TODO Auto-generated method stub
		rows.get(rowId).setVisibility(View.VISIBLE);
    }
    /**
	 * @param columnParValue
	 */
	public void hideRow(ArrayList<TableRow> rows, int rowId) {
		// TODO Auto-generated method stub
		rows.get(rowId).setVisibility(View.GONE);
    	
	}

	public void hideAllRows(ArrayList<TableRow> rows) {
		// TODO Auto-generated method stub
		int length=rows.size();
		for(int i=0;i<length;i++)
		{
			rows.get(i).setVisibility(View.GONE);
		}
	}

	public void showAllRows(ArrayList<TableRow> rows) {
		// TODO Auto-generated method stub
		int length=rows.size();
		for(int i=0;i<length;i++)
		{
			rows.get(i).setVisibility(View.VISIBLE);
		}
	}
}
