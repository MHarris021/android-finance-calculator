/**
 * Financial Instruments Class
 * Parent Class for all Financial Instruments
 */
package com.finance;

import java.util.Currency;
import java.util.Locale;

/**
 * @author DarcWynd
 *
 */
public abstract class Financial_Instrument {

	/**
	 *  Constants
	 */
	public static final int DEFAULT_ERROR_TOLERANCE = 6;
	public static final int NO_PAR_VALUE = 0;
	public static final int AT_PAR = 1;
	public static final int ABOVE_PAR = 2;
	public static final int BELOW_PAR = 3;
	public static final int CONTINUOUS = 0;
	public static final int SIMPLE = 1;
	public static final int COMPOUND = 2;
	public static final int CULENGTH = 16;
	public static final int PAYOUT_ZERO = 0;
	public static final int PAYOUT_YEARLY = 1;
	public static final int PAYOUT_SEMI_ANNUAL = 2;
	public static final int PAYOUT_QUARTERLY = 4;
	public static final int PAYOUT_MONTHLY = 12;
	public static final int PAYOUT_WEEKLY = 52;
	public static final int PAYOUT_DAILY_NO_LEAP = 365;
	public static final int PAYOUT_DAILY_LEAP = 364;
	public static final int PAYOUT_DAILY_360 = 360;
	public static final int MONTHS_IN_YEAR = 12;
	public static final int DAYS_IN_YEAR = 365;
	public static final int DAYS_IN_MONTH = 30;
	public static final int DAYS_IN_YEAR_360 = 360;
	
	/**
	 *  Variables
	 */
	private Currency currency;
	private String type, cuISP;
	private int par;
	private double parValue, faceValue, price;
	private double timeLeft;
	private int payoutPeriodsPerYear;
	private double nPV;
	private double rFRate;
	private int errTol;
	private int compoundingType;
	
		
	public Financial_Instrument() {
		// TODO Auto-generated constructor stub
		this.currency = Currency.getInstance(Locale.getDefault());
		this.type = new String();
		this.type = "";
		this.cuISP = new String();
		this.generateCUISP();
		this.par = AT_PAR;
		this.parValue = 1000.00;
		this.faceValue = 1000.00;
		this.price = 1000.00;
		this.timeLeft=1;
		this.payoutPeriodsPerYear=1;
		this.nPV = 1000.00;
		this.rFRate = 0;
		this.errTol = DEFAULT_ERROR_TOLERANCE;
		this.compoundingType = SIMPLE;
	}
	
	public Financial_Instrument(Financial_Instrument FI) {
		// TODO Auto-generated constructor stub
		this.currency = Currency.getInstance(FI.currency.getCurrencyCode());
		this.type = new String();
		this.type = FI.type;
		this.cuISP = new String();
		this.cuISP = FI.cuISP;
		this.par = FI.par;
		this.parValue = FI.parValue;
		this.faceValue = FI.faceValue;
		this.price = FI.price;
		this.timeLeft=FI.timeLeft;
		this.payoutPeriodsPerYear=FI.payoutPeriodsPerYear;
		this.nPV = FI.nPV;
		this.rFRate = FI.rFRate;
		this.errTol = FI.errTol;
		this.compoundingType = FI.compoundingType;
	}

	public boolean equals(Financial_Instrument FI) {
		if(this.currency.getCurrencyCode().compareTo(FI.currency.getCurrencyCode())!=0) return false;
		if(this.type.compareTo(FI.type)!=0) return false;
		if(this.cuISP.compareTo(FI.cuISP)!=0) return false;
		if(this.par != FI.par) return false;
		if(this.parValue != FI.parValue) return false;
		if(this.faceValue != FI.faceValue) return false;
		if(this.price != FI.price) return false;
		if(this.timeLeft != FI.timeLeft) return false;
		if(this.payoutPeriodsPerYear != FI.payoutPeriodsPerYear) return false;
		if(this.nPV != FI.nPV) return false;
		if(this.rFRate != FI.rFRate) return false;
		if(this.errTol != FI.errTol) return false;
		if(this.compoundingType != FI.compoundingType) return false;
		return true;
	}
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency newcurrency) {
		this.currency = Currency.getInstance(newcurrency.getCurrencyCode());
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setCUISP(String cuisp) {
		this.cuISP = cuisp;
	}

	/**
	 * @return the type
	 */
	public String getCUISP() {
		return this.cuISP;
	}
	
	private void generateCUISP() {
		int i, value;
		for(i=0; i< CULENGTH; i++)
		{
			value = Double.valueOf(java.lang.Math.random()*10).intValue();
			this.cuISP.concat( java.lang.Integer.toString(value));
		}
		 
	}
	

	public int getPar() {
		return par;
	}
	
	public void setPar(int par) {
		this.par = par;
	}
	
	public int calcPar() {
		if ((this.price > 0) && (this.parValue>0) && (this.nPV>0))
		{
			double temp = this.price-this.nPV;
			if (temp == 0.00) this.par = Financial_Instrument.AT_PAR;
			else if (temp > 0) this.par = Financial_Instrument.ABOVE_PAR;
				else this.par = Financial_Instrument.BELOW_PAR;
			return this.par;
		}
		else this.par = Financial_Instrument.NO_PAR_VALUE;
		return this.par;
	}

	/**
	 * @param myFaceValue the myFaceValue to set
	 */
	public void setFaceValue(double myFaceValue) {
		this.faceValue = myFaceValue;
	}

	/**
	 * @return the myFaceValue
	 */
	public double getFaceValue() {
		return faceValue;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double newprice) {
		this.price = newprice;
	}
	
	public void setTimeLeft(double myLengthinYears) {
		this.timeLeft = myLengthinYears;
	}

	/**
	 * @return the myLengthinYears
	 */
	
	public double getTimeLeft() {
		return timeLeft;
	}
	
	public int getTotalPeriods() {
		return Double.valueOf(this.timeLeft*this.payoutPeriodsPerYear).intValue();
	}
		
	public void setPayoutPeriodsPerYear(int myPayoutPeriodsPerYear) {
		this.payoutPeriodsPerYear = myPayoutPeriodsPerYear;
	}

	/**
	 * @return the myPayoutPeriodsPerYear
	 */
	public int getPayoutPeriodsPerYear() {
		return payoutPeriodsPerYear;
	}

	public double getPaymentAtTime(int time) {
		return 0;
	}
	
	public double getRFRateAtTime(int time) {
		return this.rFRate;
	}

	/**
	 * @param nPV the nPV to set
	 */
	public void setNPV(double nPV) {
		this.nPV = nPV;
	}

	/**
	 * @return the nPV
	 */
	public double getNPV() {
		return nPV;
	}
	
	public double calcNPV() {
		this.nPV = this.faceValue/(this.rFRate+1);
		return nPV;
	}

	public double getRFRate() {
		return rFRate;
	}

	public void setRFRate(double newRFRate) {
		this.rFRate = newRFRate;
	}

	/**
	 * @param myParValue the myParValue to set
	 */
	public void setParValue(double myParValue) {
		this.parValue = myParValue;
	}

	/**
	 * @return the myParValue
	 */
	public double getParValue() {
		return parValue;
	}
	
	public double calcParValue() {
		this.parValue = calcNPV();
		calcPar();
		return parValue;
	}
	
	/**
	 * @param myErrorTolerance the myErrorTolerance to set
	 */
	public void setErrorTolerance(int myErrorTolerance) {
		this.errTol = myErrorTolerance;
	}

	/**
	 * @return the myErrorTolerance
	 */
	public int getErrorTolerance() {
		return errTol;
	}

	/**
	 * @param compoundingType the compoundingType to set
	 */
	public void setCompoundingType(int compoundingType) {
		this.compoundingType = compoundingType;
	}

	/**
	 * @return the compoundingType
	 */
	public int getCompoundingType() {
		return compoundingType;
	}

	/**
	 * 
	 */
	public void rFRateUpdated() {
		// TODO Auto-generated method stub
		calcParValue();
	}

	/**
	 * 
	 */
	public void faceValueUpdated() {
		// TODO Auto-generated method stub
		calcParValue();
	}

	/**
	 * 
	 */
	public void priceUpdated() {
		// TODO Auto-generated method stub
		//calcParValue();
	}

}
