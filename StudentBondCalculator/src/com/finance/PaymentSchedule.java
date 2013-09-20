/**
 * 
 */
package com.finance;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * @author DarcWynd
 *
 */
public class PaymentSchedule {
	private Financial_Instrument myFI;
	private ArrayList<Double> rFRates, payments, weights, npvs, durationpieces;
	private double totalPayment, totalWeight, npv, duration, avgRFRate;
	private int length;
	
	
	public PaymentSchedule()
	{
		//this.myFI.asSubclass(new Financial_Instrument());
		this.myFI=null;
		this.length=this.myFI.getTotalPeriods();
		this.rFRates=new ArrayList<Double>();
		this.avgRFRate=0;
		this.payments=new ArrayList<Double>();
		this.weights=new ArrayList<Double>();
		this.npvs=new ArrayList<Double>();
		this.durationpieces=new ArrayList<Double>();
		this.totalPayment=0;
		this.totalWeight=0;
		this.npv=0;
		this.duration=0;
	}
	
	public PaymentSchedule(PaymentSchedule PS)
	{
		Class <? extends Financial_Instrument> c=PS.myFI.getClass();
		Constructor<? extends Financial_Instrument> con;
		try {
			con = c.getDeclaredConstructor(c);
			this.myFI= con.newInstance(PS.myFI);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch(Exception e) {
			
		}
		this.length=PS.length;
		this.rFRates=new ArrayList<Double>(PS.rFRates);
		this.avgRFRate=PS.avgRFRate;
		this.payments=new ArrayList<Double>(PS.payments);
		this.weights=new ArrayList<Double>(PS.weights);
		this.npvs=new ArrayList<Double>(PS.npvs);
		this.durationpieces=new ArrayList<Double>(PS.durationpieces);
		this.totalPayment=PS.totalPayment;
		this.totalWeight=PS.totalWeight;
		this.npv=PS.npv;
		this.duration=PS.duration;
	}
	
	public PaymentSchedule(Financial_Instrument FI)
	{
		Class <? extends Financial_Instrument> c=FI.getClass();
		Constructor<? extends Financial_Instrument> con;
		try {
			con = c.getDeclaredConstructor(c);
			this.myFI= con.newInstance(FI);
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch(Exception e) {
			
		}
		this.length=this.myFI.getTotalPeriods();
		this.rFRates=new ArrayList<Double>();
		this.createRFRates();
		this.calcAvgRFRate();
		this.payments=new ArrayList<Double>();
		this.createPayments();
		this.sumPayments();
		this.npvs=new ArrayList<Double>();
		this.createNPVs();
		this.sumNPV();
		this.weights=new ArrayList<Double>();
		this.createWeights();
		this.sumWeight();
		this.durationpieces=new ArrayList<Double>();
		this.createDurations();
		this.sumDuration();
	}
	
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 
	 */
	private boolean createRFRates() {
		// TODO Auto-generated method stub
		int i;
		double rfrate;
		this.rFRates.clear();
		for(i=1;i<=this.length;i++)
		{
			rfrate = java.lang.Math.pow(1+this.myFI.getRFRateAtTime(i),(1.0/this.myFI.getPayoutPeriodsPerYear()));
			rfrate = java.lang.Math.pow(rfrate, i)-1;
			this.rFRates.add(rfrate);
		}
		return true;
	}
	
	public boolean setRFRateAt(int index, double value) {
		if(index>=this.length) return false;
		this.rFRates.set(index, java.lang.Math.pow(1+value/this.myFI.getPayoutPeriodsPerYear(), index+1)-1);
		if(this.rFRates.get(index)==value)
		{
			this.calcAvgRFRate();
			this.npvs.set(index, this.payments.get(index)/(1+this.rFRates.get(index)));
			this.sumNPV();
			if(!this.createWeights()) return false;
			this.sumWeight();
			if(!this.createDurations()) return false;
			this.sumDuration();
			return true;
		}
		else return false;
	}

	/**
	 * @param avgRFRate the avgRFRate to set
	 */
	public void setAvgRFRate(double avgRFRate) {
		this.avgRFRate = avgRFRate;
	}

	/**
	 * @return the avgRFRate
	 */
	public double getAvgRFRate() {
		return avgRFRate;
	}

	private boolean calcAvgRFRate() {
		int i;
		double rfrateTotal=0;
		for(i=0;i<this.length;i++)
		{
			rfrateTotal += this.rFRates.get(i);
		}
		this.avgRFRate = rfrateTotal/this.length;
		return true;
	}

	/**
	 * 
	 */
	private boolean createPayments() {
		// TODO Auto-generated method stub
		int i;
		this.payments.clear();
		for(i=1;i<=this.length;i++)
		{
			this.payments.add((this.myFI.getPaymentAtTime(i)));
		}
		return true;
	}

	public boolean setPaymentAt(int index, double value) {
		if(index>=this.length) return false;
		this.payments.set(index, value);
		if(this.payments.get(index)==value)
		{
			this.sumPayments();
			this.npvs.set(index, value/(1+this.rFRates.get(index)));
			this.sumNPV();
			if(!this.createWeights()) return false;
			this.sumWeight();
			if(!this.createDurations()) return false;
			this.sumDuration();
			return true;
		}
		else return false;
	}

	/**
	 * 
	 */
	private boolean createNPVs() {
		// TODO Auto-generated method stub
		int i;
		if(!this.payments.isEmpty()&&!this.rFRates.isEmpty()&&(this.payments.size()==this.rFRates.size()))
		{
			this.npvs.clear();
			for(i=0;i<this.length;i++)
			{
				this.npvs.add(this.payments.get(i)/(1+this.rFRates.get(i)));
			}
			return true;
		}
		else return false;
	}

	/**
	 * 
	 */
	private boolean createWeights() {
		// TODO Auto-generated method stub
		int i;
		if(!this.npvs.isEmpty()&&!(this.npv<=0))
		{
			this.weights.clear();
			for(i=0;i<this.length;i++)
			{
				this.weights.add(this.npvs.get(i)/this.npv);
			}
			return true;
		}
		else return false;
	}

	/**
	 * 
	 */
	private boolean createDurations() {
		// TODO Auto-generated method stub
		int i;
		if(!this.npvs.isEmpty()&&(!this.weights.isEmpty())&&(!(this.totalWeight<=0))&&(this.npvs.size()==this.weights.size()))
		{
			this.durationpieces.clear();
			for(i=0;i<this.length;i++)
			{
				this.durationpieces.add((this.weights.get(i)*(i+1))/this.myFI.getPayoutPeriodsPerYear());
			}
			return true;
		}
		else return false;
	}

	/**
	 * 
	 */
	private double sumPayments() {
		// TODO Auto-generated method stub
		int i;
		if(!this.payments.isEmpty())
		{
			this.totalPayment=0;
			for(i=0;i<this.length;i++)
			{
				this.totalPayment+=this.payments.get(i);
			}
			return this.totalPayment;
		}
		else return -1;
	}

	/**
	 * 
	 */
	private double sumNPV() {
		// TODO Auto-generated method stub
		int i;
		if(!this.npvs.isEmpty())
		{
			this.npv=0;
			for(i=0;i<this.length;i++)
			{
				this.npv+=this.npvs.get(i);
			}
			return this.npv;
		}
		else return -1;		
	}

	/**
	 * 
	 */
	private double sumWeight() {
		// TODO Auto-generated method stub
		int i;
		if(!this.weights.isEmpty())
		{
			this.totalWeight=0;
			for(i=0;i<this.length;i++)
			{
				this.totalWeight+=this.weights.get(i);
			}
			return this.totalWeight;
		}
		else return -1;
	}

	/**
	 * 
	 */
	private double sumDuration() {
		// TODO Auto-generated method stub
		int i;
		if(!this.durationpieces.isEmpty())
		{
			this.duration=0;
			for(i=0;i<this.length;i++)
			{
				this.duration+=this.durationpieces.get(i);
			}
			return this.duration;
		}
		else return -1;
	}

	public boolean generateSchedule() {
		this.sumPayments();
		if(!this.createNPVs()) return false;
		this.sumNPV();
		if(!this.createWeights()) return false;
		this.sumWeight();
		if(!this.createDurations()) return false;
		this.sumDuration();
		return true;
	}
	/**
	 * @param myFI the myFI to set
	 */
	public void setMyFI(Financial_Instrument FI) {
		this.myFI = FI;
	}
	/**
	 * @return the myFI
	 */
	public Financial_Instrument getMyFI() {
		return myFI;
	}

	public boolean addSet(double payment, double rfrate) {
		this.payments.add(payment);
		this.sumPayments();
		this.rFRates.add(rfrate/this.myFI.getPayoutPeriodsPerYear());
		this.length++;
		this.createNPVs();
		this.sumNPV();
		if(!this.createWeights()) return false;
		this.sumWeight();
		if(!this.createDurations()) return false;
		this.sumDuration();
		return true;
	}
	
	public boolean insertSet(int index, double payment, double rfrate) {
		this.payments.add(index,payment);
		this.sumPayments();
		this.rFRates.add(index,rfrate/this.myFI.getPayoutPeriodsPerYear());
		this.length++;
		this.createNPVs();
		this.sumNPV();
		if(!this.createWeights()) return false;
		this.sumWeight();
		if(!this.createDurations()) return false;
		this.sumDuration();
		return true;
	}
	
	public boolean removeSetAt(int index) {
		if(index>this.length) return false;
		this.payments.remove(index);
		this.sumPayments();
		this.rFRates.remove(index);
		this.length--;
		this.createNPVs();
		this.sumNPV();
		if(!this.createWeights()) return false;
		this.sumWeight();
		if(!this.createDurations()) return false;
		this.sumDuration();
		return true;
	}
	
	/**
	 * @return the rFRates
	 */
	public ArrayList<Double> getrFRates() {
		return rFRates;
	}

	/**
	 * @return the payments
	 */
	public ArrayList<Double> getPayments() {
		return payments;
	}

	/**
	 * @return the weights
	 */
	public ArrayList<Double> getWeights() {
		return weights;
	}

	/**
	 * @return the npvs
	 */
	public ArrayList<Double> getNpvs() {
		return npvs;
	}

	
	/**
	 * @return the durationpieces
	 */
	public ArrayList<Double> getDurationpieces() {
		return durationpieces;
	}

		/**
	 * @return the totalPayment
	 */
	public double getTotalPayment() {
		return totalPayment;
	}

	/**
	 * @return the totalWeight
	 */
	public double getTotalWeight() {
		return totalWeight;
	}

	/**
	 * @return the npv
	 */
	public double getNpv() {
		return npv;
	}

	
	/**
	 * @return the duration
	 */
	public double getDuration() {
		return duration;
	}

	
}
