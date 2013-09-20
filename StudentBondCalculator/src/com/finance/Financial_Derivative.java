/**
 * 
 */
package com.finance;

/**
 * @author DarcWynd
 *
 */
public class Financial_Derivative extends Financial_Instrument {

	/**
	 * 
	 */
	private Financial_Instrument underlyingAsset;
	
	public Financial_Derivative() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param FI
	 */
	public Financial_Derivative(Financial_Instrument FI) {
		super(FI);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param underlyingAsset the underlyingAsset to set
	 */
	public void setUnderlyingAsset(Financial_Instrument underlyingAsset) {
		this.underlyingAsset = underlyingAsset;
	}

	/**
	 * @return the underlyingAsset
	 */
	public Financial_Instrument getUnderlyingAsset() {
		return underlyingAsset;
	}

}
