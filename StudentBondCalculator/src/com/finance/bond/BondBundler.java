/**
 * 
 */
package com.finance.bond;

import java.util.Date;


import android.os.Bundle;

/**
 * @author DarcWynd
 *
 */
public class BondBundler {
	private Bond myBond;
	private Bundle myBundle;
	/**
	 * @param myBond the myBond to set
	 */
	public void setBond(Bond myBond) {
		this.myBond = myBond;
	}
	/**
	 * @return the myBond
	 */
	public Bond getBond() {
		return myBond;
	}
	/**
	 * @param myBundle the myBundle to set
	 */
	public void setBundle(Bundle myBundle) {
		this.myBundle = myBundle;
	}
	/**
	 * @return the myBundle
	 */
	public Bundle getBundle() {
		return myBundle;
	}
	
	public boolean saveBond()
	{
		String pre, lookup;
		pre = myBond.getType();
		lookup = pre+myBond.getCUISP();
		myBundle.putString(lookup+"BondType", myBond.getBondType());
		myBundle.putDouble(lookup+".ParValue", myBond.getParValue());
		myBundle.putDouble(lookup+".FaceValue", myBond.getFaceValue());
		myBundle.putDouble(lookup+".Price", myBond.getPrice());
		myBundle.putDouble(lookup+".BondLength", myBond.getBondLength());
		myBundle.putLong(lookup+".StartDate", myBond.getStartDate().getTime());
		myBundle.putLong(lookup+".EndDate", myBond.getEndDate().getTime());
		myBundle.putBoolean(lookup+".isCallable", myBond.isCallable());
		if(myBond.isCallable()) myBundle.putLong(lookup+".CallDate", myBond.getCallDate().getTime());
		myBundle.putDouble(lookup+".TimeLeft", myBond.getTimeLeft());
		myBundle.putInt(lookup+".Periods", myBond.getPayoutPeriodsPerYear());
		myBundle.putDouble(lookup+".Rate", myBond.getRate());
		myBundle.putDouble(lookup+".RFRate", myBond.getRFRate());
		myBundle.putDouble(lookup+".CouponRate", myBond.getCouponRate());
		myBundle.putDouble(lookup+".YieldC", myBond.getYieldtoCall());
		myBundle.putDouble(lookup+".YieldM", myBond.getYieldtoMaturity());
		myBundle.putDouble(lookup+".Duration", myBond.getDuration());
		myBundle.putDouble(lookup+".MDuration", myBond.getMDuration());
		myBundle.putDouble(lookup+".DV01", myBond.getDV01());
		return true;
	}
	
	public boolean loadBond()
	{
		
		String pre, lookup;
		pre = myBond.getType();
		lookup = pre+myBond.getCUISP();
		myBond.setBondType(myBundle.getString(lookup+"BondType"));
		myBond.setParValue(myBundle.getDouble(lookup+".ParValue"));
		myBond.setFaceValue(myBundle.getDouble(lookup+".FaceValue"));
		myBond.setPrice(myBundle.getDouble(lookup+".Price"));
		myBond.setBondLength(myBundle.getDouble(lookup+".BondLength"));
		myBond.setStartDate(new Date(myBundle.getLong(lookup+".StartDate")));
		myBond.setEndDate(new Date(myBundle.getLong(lookup+".EndDate")));
		myBond.setCallable(myBundle.getBoolean(lookup+".isCallable"));
		if(myBond.isCallable()) myBond.setCallDate(new Date(myBundle.getLong(lookup+".CallDate")));
		myBond.setTimeLeft(myBundle.getDouble(lookup+".TimeLeft"));
		myBond.setPayoutPeriodsPerYear(myBundle.getInt(lookup+".Periods"));
		myBond.setRate(myBundle.getDouble(lookup+".Rate"));
		myBond.setRFRate(myBundle.getDouble(lookup+".RFRate"));
		myBond.setCouponRate(myBundle.getDouble(lookup+".CouponRate"));
		myBond.setYieldtoCall(myBundle.getDouble(lookup+".YieldC"));
		myBond.setYieldtoMaturity(myBundle.getDouble(lookup+".YieldM"));
		myBond.setDuration(myBundle.getDouble(lookup+".Duration"));
		myBond.setMDuration(myBundle.getDouble(lookup+".MDuration"));
		myBond.setDV01(myBundle.getDouble(lookup+".DV01"));
		return true;
	}
	

}


