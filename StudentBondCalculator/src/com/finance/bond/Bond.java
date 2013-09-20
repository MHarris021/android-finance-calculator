/**
 * 
 */
package com.finance.bond;

import java.math.BigDecimal;
import java.util.Date;

import com.finance.Financial_Instrument;

/**
 * @author DarcWynd
 *
 */
public class Bond extends Financial_Instrument {

	/**
	 *  Constants
	 */
	
	public static final double PERPETUAL_BOND  = 0;
	public static final double THIRTY_DAY_BILL  = 1/12;
	public static final double NINETY_DAY_BILL  = 1/4;
	public static final double SIX_MONTH_BILL  = 1/2;
	public static final double NINE_MONTH_BILL  = 3/4;
	public static final double ONE_YEAR_BILL  = 1;
	public static final double TWO_YEAR_NOTE  = 2;
	public static final double FIVE_YEAR_NOTE  = 5;
	public static final double SEVEN_YEAR_NOTE  = 7;
	public static final double TEN_YEAR_NOTE  = 10;
	public static final double FIFTEEN_YEAR_BOND  = 15;
	public static final double TWENTY_YEAR_BOND  = 20;
	public static final double THIRTY_YEAR_BOND  = 30;
	public static final double HUNDRED_YEAR_BOND  = 100;
	
	public static final int PERPETUAL_BOND_INT  = 0;
	public static final int THIRTY_DAY_BILL_INT  = 1;
	public static final int NINETY_DAY_BILL_INT  = 3;
	public static final int SIX_MONTH_BILL_INT  = 6;
	public static final int NINE_MONTH_BILL_INT  = 9;
	public static final int ONE_YEAR_BILL_INT  = 12;
	public static final int TWO_YEAR_NOTE_INT  = 24;
	public static final int FIVE_YEAR_NOTE_INT  = 60;
	public static final int SEVEN_YEAR_NOTE_INT  = 84;
	public static final int TEN_YEAR_NOTE_INT  = 120;
	public static final int FIFTEEN_YEAR_BOND_INT  = 180;
	public static final int TWENTY_YEAR_BOND_INT  = 240;
	public static final int THIRTY_YEAR_BOND_INT  = 360;
	public static final int HUNDRED_YEAR_BOND_INT  = 1200;
	
	/**
	 *  Variables
	 */
	private String bondType;
	double rate;
	private double couponRate;
	double yieldtoCall;
	double yieldtoMaturity;
	private double bondLength;
	private double timetoCall;
	private double accruedInterest;
	private Date startDate;
	private Date callDate;
	private Date endDate;
	private boolean callable;
	private boolean datesSet;
	private double duration;
	private double mDuration;
	private double eRR;
	private double dV01;
	
	/**
	 * @param bondType the bondType to set
	 */
	public void setBondType(String bondType) {
		this.bondType = bondType;
	}

	/**
	 * @return the bondType
	 */
	public String getBondType() {
		return bondType;
	}

	public String calcBondType() {
		String type = "";
		switch(this.getPayoutPeriodsPerYear()){
		case PAYOUT_ZERO:
			type = "Zero Coupon";
			break;
		case PAYOUT_YEARLY:
			type = "Annual";
			break;
		case PAYOUT_SEMI_ANNUAL:
			type = "Semi-annual";
			break;
		case PAYOUT_QUARTERLY:
			type = "Quarterly";
			break;
		case PAYOUT_MONTHLY:
			type = "Annual";
			break;
		case PAYOUT_WEEKLY:
			type = "Semi-annual";
			break;
		case PAYOUT_DAILY_NO_LEAP:
		case PAYOUT_DAILY_LEAP:
		case PAYOUT_DAILY_360:
			type = "Daily";
			break;
		default: return "Not defined";
		}
		
		switch((int)(this.bondLength*MONTHS_IN_YEAR)){
		case PERPETUAL_BOND_INT:
			type+=(" Perpetual");
			break;
		case THIRTY_DAY_BILL_INT:
			type+=(" Thirty Day");
			break;
		case NINETY_DAY_BILL_INT:
			type+=(" Ninety Day");
			break;
		case SIX_MONTH_BILL_INT:
			type+=(" Six Month");
			break;
		case NINE_MONTH_BILL_INT:
			type+=(" Nine Month");
			break;
		case ONE_YEAR_BILL_INT:
			type+=(" One Year");
			break;
		case TWO_YEAR_NOTE_INT:
			type+=(" Two Year");
			break;
		case FIVE_YEAR_NOTE_INT:
			type+=(" Five Year");
			break;
		case SEVEN_YEAR_NOTE_INT:
			type+=(" Seven Year");
			break;
		case TEN_YEAR_NOTE_INT:
			type+=(" Ten Year");
			break;
		case FIFTEEN_YEAR_BOND_INT:
			type+=(" Fifteen Year");
			break;
		case TWENTY_YEAR_BOND_INT:
			type+=(" Twenty Year");
			break;
		case THIRTY_YEAR_BOND_INT:
			type+=(" Thirty Year");
			break;
		case HUNDRED_YEAR_BOND_INT:
			type+=(" One Hundred Year");
			break;
		default:type+=(" "+String.valueOf(this.bondLength)+ " Year");
		}
		
		if(this.bondLength<=ONE_YEAR_BILL)type+=(" Bill");
		else if(this.bondLength<FIFTEEN_YEAR_BOND) type+=(" Note");
		else type+=(" Bond");
		this.bondType = type;
		return this.bondType;
	}
	public Bond() {
		// TODO Auto-generated constructor stub
		this.setType("Bond");
		this.rate = 0.00;
		this.setTimeLeft(Bond.ONE_YEAR_BILL);
		this.bondLength = Bond.ONE_YEAR_BILL;
		this.timetoCall = Bond.ONE_YEAR_BILL;
		this.setPayoutPeriodsPerYear(Bond.PAYOUT_SEMI_ANNUAL);
		this.couponRate = 0;
		this.accruedInterest =0;
		this.startDate = new Date();
		this.endDate = new Date();
		this.callDate = new Date();
		this.endDate.setYear(startDate.getYear()+1);
		this.callable = false;
		this.datesSet = false;
		this.calcYieldM();
		this.calcYieldC();
		this.calcDuration();
		this.calcMDuration();
		this.eRR = this.yieldtoMaturity;
		this.dV01 = .25;
		this.calcBondType();
	}
	
	public Bond(Bond B) {
		// TODO Auto-generated constructor stub
		super(B);
		this.bondType = B.bondType;
		this.rate = B.rate;
		this.bondLength = B.bondLength;
		this.timetoCall = B.timetoCall;
		this.couponRate = B.couponRate;
		this.accruedInterest = B.accruedInterest;
		this.startDate = new Date(B.startDate.getTime());
		this.callDate = new Date(B.callDate.getTime());
		this.endDate = new Date(B.endDate.getTime());
		this.callable = B.callable;
		this.datesSet = B.datesSet;
		this.yieldtoMaturity = B.yieldtoMaturity;
		this.yieldtoCall = B.yieldtoCall;
		this.duration = B.duration;
		this.mDuration = B.mDuration;
		this.eRR = B.eRR;
		this.dV01 = B.dV01;
	}
	
	public boolean equals(Bond B) {
		if(!super.equals(B)) return false;
		if(this.bondType != B.bondType) return false;
		if(this.rate != B.rate) return false;
		if(this.bondLength != B.bondLength) return false;
		if(this.timetoCall != B.timetoCall) return false;
		if(this.couponRate != B.couponRate) return false;
		if(this.accruedInterest != B.accruedInterest) return false;
		if(this.datesSet != B.datesSet) return false;
		if(!this.startDate.equals(B.startDate)) return false;
		if(!this.callDate.equals(B.callDate)) return false;
		if(!this.endDate.equals(B.endDate)) return false;
		if(this.callable != B.callable) return false;
		if(this.yieldtoMaturity != B.yieldtoMaturity) return false;
		if(this.yieldtoCall != B.yieldtoCall) return false;
		if(this.duration != B.duration) return false;
		if(this.mDuration != B.mDuration) return false;
		if(this.eRR != B.eRR) return false;
		if(this.dV01 != B.dV01) return false;
		return true;
	}
	
	@Override
	public double calcParValue()
	{
		double parvalue;
		parvalue = this.calcNPV(this.getRate(), this.getTimeLeft());
		this.setParValue(BigDecimal.valueOf(Double.valueOf(parvalue)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return this.getParValue();
	}
	
	public double calcPrice()
	{
		double price;
		price = this.calcNPV(this.getYieldtoMaturity(), this.getTimeLeft());
		this.setPrice(price);
		return this.getPrice();
	}
	
	public double calcAPrice(double yield, double time)
	{
		double price, x, y, s, time2;
		time2 = time *this.getPayoutPeriodsPerYear();
		switch(this.getCompoundingType())
		{
		case Financial_Instrument.CONTINUOUS:
			y = java.lang.Math.exp(yield/this.getPayoutPeriodsPerYear());
			x = java.lang.Math.pow(y, time2);
			s = ((1-(1/x))/(1-(1/y)));
			break;
		case Financial_Instrument.SIMPLE:
			y = yield/this.getPayoutPeriodsPerYear();
			x = java.lang.Math.pow((1+y), time2);
			s = (1-(1/x));
			break;
		default:
			y = java.lang.Math.exp(yield/this.getPayoutPeriodsPerYear());
			x = java.lang.Math.pow(y, time2);
			s = ((1-(1/x))/(1-(1/y)));
			break;
		}
		price = this.getFaceValue()/x + this.getCouponRate()/y *s;
		price = BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return price;
	}
	/**
	 * @param myRate the myRate to set
	 */
	public void setRate(double myRate) {
		this.rate = myRate;
	}

	/**
	 * @return the myRate
	 */
	public double getRate() {
		return rate;
	}

	public double calcRate() {
		if (this.getPayoutPeriodsPerYear() >= 1)
			this.rate = this.couponRate*this.getPayoutPeriodsPerYear()/(this.getFaceValue());
		else this.rate = (this.getFaceValue()-this.getPrice())/this.getFaceValue();
		return this.rate;
	}
	
	public double calcRateFromYield()
	{
		double FV, rate;
		FV = java.lang.Math.pow((1+this.yieldtoMaturity), this.getTimeLeft()) * this.getPrice();
		rate = java.lang.Math.pow((FV/this.getFaceValue()), 1.0/this.getTimeLeft()) - 1;
		this.setRate(rate);
		return this.getRate();
	}
	/**
	 * @param myLengthinYears the myLengthinYears to set
	 */
		public double calcTimeLeft() {
		double years = 0;
		if(this.datesSet)
		{
			if(this.getEndDate().after(getStartDate()))
			{
				years = this.getEndDate().getYear()-this.getStartDate().getYear();
				years += (this.getEndDate().getMonth()-this.getStartDate().getMonth())/MONTHS_IN_YEAR;
				years += (this.getEndDate().getDate()-this.getStartDate().getDate())/DAYS_IN_YEAR;
			}
			this.setTimeLeft(years);
		}else this.setTimeLeft(this.bondLength);
		this.calcBondType();
		return this.getTimeLeft();
	}
	/**
	 * @param bondLength the bondLength to set
	 */
	public void setBondLength(double bondLength) {
		this.bondLength = bondLength;
	}

	/**
	 * @return the bondLength
	 */
	public double getBondLength() {
		return bondLength;
	}

	public double calcBondLength() {
		if(this.datesSet)
			return this.bondLength;
		else
		{
			this.bondLength=this.getTimeLeft();
			return this.bondLength;
		}
	}

	/**
	 * @param timetoCall the timetoCall to set
	 */
	public void setTimetoCall(double timetoCall) {
		this.timetoCall = timetoCall;
	}

	/**
	 * @return the timetoCall
	 */
	public double getTimetoCall() {
		return timetoCall;
	}
	
	public double calcTimetoCall() {
		double ttc = 0;
		if(this.isCallable())
		{
			if(this.getCallDate().after(getStartDate()))
			{
				ttc = this.getCallDate().getYear()-this.getStartDate().getYear();
				ttc += (this.getCallDate().getMonth()-this.getStartDate().getMonth())/MONTHS_IN_YEAR;
				ttc += (this.getCallDate().getDate()-this.getStartDate().getDate())/DAYS_IN_YEAR;
			}
		}
		this.setTimetoCall(ttc);
		return this.getTimetoCall();
	}

	/**
	 * @param myPayoutPeriodsPerYear the myPayoutPeriodsPerYear to set
	 */
	
	/**
	 * @param myCouponRate the myCouponRate to set
	 */
	
	public void setCouponRate(double myCouponRate) {
		this.couponRate = myCouponRate;
	}
	
	/**
	 * @return the myCouponRate
	 */
	public double getCouponRate() {
		return couponRate;
	}
	
	
	@Override
	public double getPaymentAtTime(int time) {
		if(time!=Double.valueOf(this.getTimeLeft()*this.getPayoutPeriodsPerYear()).intValue())
			return this.couponRate;
		else return this.couponRate+this.getFaceValue();
	}
	
	public double calcCouponRate() {
		if (this.getPayoutPeriodsPerYear() >= 1)
		{
			switch(this.getCompoundingType())
			{
				case Financial_Instrument.SIMPLE:
					this.couponRate = this.rate*this.getFaceValue()/this.getPayoutPeriodsPerYear();
					break;
				case Financial_Instrument.COMPOUND:
					this.couponRate = (java.lang.Math.pow((1+this.rate),1.0/this.getPayoutPeriodsPerYear())-1)*this.getFaceValue();
					break;
				case Financial_Instrument.CONTINUOUS:
					this.couponRate = (java.lang.Math.exp(this.rate/this.getPayoutPeriodsPerYear())-1)*this.getFaceValue();
					break;
				default: this.couponRate = this.rate*this.getFaceValue()/this.getPayoutPeriodsPerYear();
			}
		}
		else this.couponRate= this.getFaceValue()-this.getPrice();
		return this.couponRate;
	}

	/**
	 * @param coupon the myLastCoupon to set
	 */
	public void setAInterest(double coupon) {
		this.accruedInterest = coupon;
	}

	/**
	 * @return the myLastCoupon
	 */
	public double getAInterest() {
		return accruedInterest;
	}
	
	public double calcAInterest() {
		// TODO Auto-generated method stub
		double years = this.getTimeLeft();
    	int periods = this.getPayoutPeriodsPerYear();
    	double coupon = this.getCouponRate();
		double remainder = java.lang.Math.IEEEremainder(years, 1.0);
		if(remainder == 0)
		{
			this.setAInterest(0);
			return this.accruedInterest;
		}else
		{
			remainder = java.lang.Math.IEEEremainder(remainder, 1.0/periods);
			if(remainder<0)
				remainder+=1.0/periods;
			if(remainder != 0)
			{
				coupon=remainder*periods*(coupon);
				this.setAInterest(coupon);
				return coupon;
			}
			else return 0;
		}
	}

	/**
	 * @param myStartDate the myStartDate to set
	 */
	public void setStartDate(Date myStartDate) {
		this.startDate = myStartDate;
	}

	/**
	 * @return the myStartDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param myCallDate the myCallDate to set
	 */
	public void setCallDate(Date myCallDate) {
		this.callDate = myCallDate;
	}

	/**
	 * @return the myCallDate
	 */
	public Date getCallDate() {
		return callDate;
	}

	/**
	 * @param myEndDate the myEndDate to set
	 */
	public void setEndDate(Date myEndDate) {
		this.endDate = myEndDate;
	}

	/**
	 * @return the myEndDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	public double calcPVFace() {
    	return this.calcPVFace(this.couponRate, this.getTimeLeft()); 
	}
	
	private double calcPVFace(double rate2, double years) {
		double pv, discount, face = this.getFaceValue();
		discount = java.lang.Math.pow(rate2, this.getPayoutPeriodsPerYear()*years);
		pv = face/discount;
    	return pv; 
	}
	
	public double calcPVCoupon() {
		double pv = 0;
		double years = this.getTimeLeft();
		double coupon = this.couponRate;
		double rate = this.getRFRate();
		pv = this.calcPVCoupon(rate, coupon, years); 
		return pv;
	}
	
	private double calcPVCoupon(double rate, double couponRate2, double years) {
		int periods = this.getPayoutPeriodsPerYear();
		double pv = 0;
		double discountRate, temp, totalPeriods = (years)*periods;
		for (int i = 1; i <= totalPeriods; i++)
		{
			discountRate =java.lang.Math.pow(rate, i);
			temp = couponRate2/discountRate;
			pv = pv+temp;
		}
		return pv;
	}
	
	@Override
	public double calcNPV() {
		this.setNPV(this.calcPVCoupon()+(this.calcPVFace()));
    	return this.getNPV();
    }
	
	protected double calcNPV(double rate, double years)
	{
		double npv;
		npv = this.calcNPV(rate, this.couponRate, years);
		return npv;
	}
	
	private double calcNPV(double rate, double couponRate2,double years) {
		double npv, discount, coupons, periods=this.getPayoutPeriodsPerYear();
		
		switch(this.getCompoundingType())
		{
			case Financial_Instrument.SIMPLE:
				discount = (rate/periods+1);
				coupons = couponRate2;
				break;
			case Financial_Instrument.COMPOUND:
				discount = java.lang.Math.pow((1+rate), 1.0/periods);
				coupons = couponRate2;
				break;
			case Financial_Instrument.CONTINUOUS:
				discount = java.lang.Math.exp(rate/periods);
				coupons = java.lang.Math.expm1(couponRate2/this.getFaceValue())*this.getFaceValue();
				break;
			default:
				discount=1.0;
				coupons = couponRate2;
		}
		npv = this.calcPVCoupon(discount,coupons,years)+(this.calcPVFace(discount,years));
		npv = BigDecimal.valueOf(npv).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return npv; 
	}
	
	/**
	 * @param myCallability the myCallability to set
	 */
	public void setCallable(boolean myCallability) {
		this.callable = myCallability;
	}

	/**
	 * @return the myCallability
	 */
	public boolean isCallable() {
		return callable;
	}

	/**
	 * @param datesSet the datesSet to set
	 */
	public void setDatesSet(boolean datesSet) {
		this.datesSet = datesSet;
	}

	/**
	 * @return the datesSet
	 */
	public boolean isDatesSet() {
		return datesSet;
	}

	/**
	 * @param bigDecimal the myYieldtoMaturity to set
	 */
	public void setYieldtoMaturity(double bigDecimal) {
		this.yieldtoMaturity = bigDecimal;
	}

	public double getYieldtoMaturity() {
		return yieldtoMaturity;
	}
	
	public double calcYieldM() {
		this.yieldtoMaturity=calcYield(this.getTimeLeft());
		return this.yieldtoMaturity;
	}
	
	private double calcYield(double time)
	{
		BigDecimal yield=BigDecimal.ZERO;
		int errorDigits=this.getErrorTolerance(), periods = this.getPayoutPeriodsPerYear();
		double  FV, price0, yield1, ratio;
		price0 = this.getPrice();
		//for(int i=0;i<100;i++)
		switch(this.getCompoundingType())
		{
			case Financial_Instrument.SIMPLE:
				FV = (this.getFaceValue()*java.lang.Math.pow((1+this.getRate()),time));
				ratio = FV/price0;
				yield1 = java.lang.Math.pow(ratio, 1.0/(time))-1;
				break;
			case Financial_Instrument.COMPOUND:
				FV = (this.getFaceValue()*java.lang.Math.pow((1+this.getRate()/periods),periods*time));
				ratio = FV/price0;
				yield1 = java.lang.Math.pow(ratio, 1.0/(time))-1;
				break;
			case Financial_Instrument.CONTINUOUS:
				FV = (this.getFaceValue()*java.lang.Math.exp(this.getRate()*time));
				ratio = FV/price0;
				yield1 = java.lang.Math.log(ratio)/time;
				break;
			default:
				FV = (this.getFaceValue()*java.lang.Math.pow((1+this.getRate()/periods),periods*time));
				ratio = FV/price0;
				yield1 = java.lang.Math.pow(ratio, 1.0/(time))-1;
		}
		yield = BigDecimal.valueOf(yield1).setScale(errorDigits, BigDecimal.ROUND_HALF_UP);
		return yield.doubleValue();
	}
	
	/**
	 * @param myYieldtoCall the myYieldtoCall to set
	 */
	public void setYieldtoCall(double myYieldtoCall) {
		this.yieldtoCall = myYieldtoCall;
	}

	/**
	 * @return the myYieldtoCall
	 */
	public double getYieldtoCall() {
		return yieldtoCall;
	}
	
	public double calcYieldC() {
		if(!this.isCallable()){
			if(this.yieldtoMaturity==0) this.yieldtoCall=this.calcYieldM();
			else this.yieldtoCall=this.yieldtoMaturity;
		}else this.yieldtoCall=this.calcYield(this.timetoCall);
		return this.yieldtoCall;
	}

	@Override
	public int calcPar() {
		this.setPar(NO_PAR_VALUE);
		if(this.getPrice()==this.getParValue())
		{
			this.setPar(AT_PAR);
			return this.getPar();
		}
		if(this.getPrice()>this.getParValue())
		{
			this.setPar(ABOVE_PAR);
			return this.getPar();
		}
		if(this.getPrice()<this.getParValue())
		{
			this.setPar(BELOW_PAR);
			return this.getPar();
		}
		return this.getPar();
	}
	
	/**
	 * @param myDuration the myDuration to set
	 */
	public void setDuration(double myDuration) {
		this.duration = myDuration;
	}

	/**
	 * @return the myDuration
	 */
	public double getDuration() {
		return duration;
	}

	public double calcDuration() {
		int i, periods = this.getPayoutPeriodsPerYear(), length = this.getTotalPeriods();
		double discount, totaldiscount, totalValue=0, sum=0, coupon, yield;
		yield = this.getYieldtoMaturity();
		this.duration=0;
		switch(this.getCompoundingType())
		{
	/*		case Financial_Instrument.SIMPLE:
				discount = (yield/periods+1);
				coupon = this.couponRate;
				break;
			case Financial_Instrument.COMPOUND:
				discount = java.lang.Math.pow((1+yield), 1.0/periods);
				coupon = this.couponRate;
				break;
			case Financial_Instrument.CONTINUOUS:
				discount = java.lang.Math.exp(yield/periods);
				//coupon = this.couponRate;
				coupon = java.lang.Math.expm1(this.couponRate/this.getFaceValue())*this.getFaceValue();
				break;*/
			default:
				discount=(yield/periods+1);
				coupon = this.couponRate;
		}
		
		for(i=1;i<=length;i++)
		{
			totaldiscount=java.lang.Math.pow(discount, i);
			sum+=(i/periods*coupon)/(totaldiscount);
		}
		totaldiscount = java.lang.Math.pow(discount, length);
		totalValue = sum + (this.getTimeLeft()*this.getFaceValue())/(totaldiscount);
		duration = totalValue/(this.getPrice());
		
		return duration;
	}
	/**
	 * @param myMDuration the myMDuration to set
	 */
	public void setMDuration(double myMDuration) {
		this.mDuration = myMDuration;
	}

	/**
	 * @return the myMDuration
	 */
	public double getMDuration() {
		return mDuration;
	}
	
	public double calcMDuration() {
		this.mDuration=this.duration/(1+this.yieldtoMaturity/this.getPayoutPeriodsPerYear());
		return mDuration;
	}
	/**
	 * @param myERR the myERR to set
	 */
	public void setERR(double myERR) {
		this.eRR = myERR;
	}

	/**
	 * @return the myERR
	 */
	public double getERR() {
		return eRR;
	}

	/**
	 * @param myDV01 the myDV01 to set
	 */
	public void setDV01(double myDV01) {
		this.dV01 = myDV01;
	}

	/**
	 * @return the myDV01
	 */
	public double getDV01() {
		return dV01;
	}

	public double calcDV01() {
		double yield= this.yieldtoMaturity, y1, y0, price0, price1, price;
		y1=yield+0.0001;
		y0=yield-0.0001;
		price0=this.calcNPV(y0, couponRate, this.getTimeLeft());
		price = this.getPrice();
		price1=this.calcNPV(y1, couponRate, this.getTimeLeft());
		if((price1<=price)&&(price<=price0))
			dV01=(price0-price1)/(2*price*.001);
		else dV01 = -1;
		return dV01;
	}

	/**
	 * 
	 */
	protected void updateMainDependentValues() {
		// TODO Auto-generated method stub
		this.calcAInterest();
		this.calcYieldM();
		this.calcYieldC();
		this.calcParValue();
		this.calcPar();
		this.calcDuration();
		this.calcMDuration();
		this.calcDV01();		
	}
	
	@Override
	public void priceUpdated() {
		// TODO Auto-generated method stub
		this.updateMainDependentValues();
	}
	
	public void parValueUpdated() {
		// TODO Auto-generated method stub
		this.updateMainDependentValues();
	}
	
	@Override
	public void faceValueUpdated() {
		// TODO Auto-generated method stub
		this.calcCouponRate();
		this.updateMainDependentValues();
	}
	
	public void bondLengthUpdated() {
		// TODO Auto-generated method stub
		this.calcTimeLeft();
		this.calcTimetoCall();
		this.updateMainDependentValues();
	}
	
	public void timeLeftUpdated() {
		// TODO Auto-generated method stub
		this.calcBondLength();
		this.calcTimetoCall();
		this.updateMainDependentValues();
	}
	
	public void periodsUpdated() {
		// TODO Auto-generated method stub
		this.calcCouponRate();
		this.updateMainDependentValues();
	}
	
	public void rateUpdated() {
		// TODO Auto-generated method stub
		this.calcCouponRate();
		this.updateMainDependentValues();
	}
	
	public void compoundingUpdated() {
		// TODO Auto-generated method stub
		this.calcCouponRate();
		this.updateMainDependentValues();
	}
	
	public void couponUpdated() {
		// TODO Auto-generated method stub
		this.calcRate();
		this.updateMainDependentValues();
	}

	/**
	 * 
	 */
	@Override
	public void rFRateUpdated() {
		// TODO Auto-generated method stub
		this.updateMainDependentValues();
	}
	
	public void yieldUpdated(int flag) {
		// TODO Auto-generated method stub
		if(flag==0)
			this.calcPrice();
		else{
			this.calcRateFromYield();
			this.calcCouponRate();
		}
		this.updateMainDependentValues();
	}
	
}
