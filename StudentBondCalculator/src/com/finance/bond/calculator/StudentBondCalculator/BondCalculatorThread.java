/**
 * 
 */
package com.finance.bond.calculator.StudentBondCalculator;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

/**
 * @author DarcWynd
 *
 */
public class BondCalculatorThread extends Thread {

	
	/**
	 * 
	 */
	public BondCalculatorThread() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param runnable
	 */
	public BondCalculatorThread(Runnable runnable) {
		super(runnable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param threadName
	 */
	public BondCalculatorThread(String threadName) {
		super(threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param runnable
	 * @param threadName
	 */
	public BondCalculatorThread(Runnable runnable, String threadName) {
		super(runnable, threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param runnable
	 */
	public BondCalculatorThread(ThreadGroup group, Runnable runnable) {
		super(group, runnable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param threadName
	 */
	public BondCalculatorThread(ThreadGroup group, String threadName) {
		super(group, threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param runnable
	 * @param threadName
	 */
	public BondCalculatorThread(ThreadGroup group, Runnable runnable,
			String threadName) {
		super(group, runnable, threadName);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param group
	 * @param runnable
	 * @param threadName
	 * @param stackSize
	 */
	public BondCalculatorThread(ThreadGroup group, Runnable runnable,
			String threadName, long stackSize) {
		super(group, runnable, threadName, stackSize);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param holder
	 * @param context
	 * @param handler
	 */
	public BondCalculatorThread(SurfaceHolder holder, Context context,
			Handler handler) {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param width
	 * @param height
	 */
	public void setSurfaceSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param keyCode
	 * @param msg
	 * @return
	 */
	public boolean doKeyUp(int keyCode, KeyEvent msg) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @param b
	 */
	public void setRunning(boolean b) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param keyCode
	 * @param msg
	 * @return
	 */
	public boolean doKeyDown(int keyCode, KeyEvent msg) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
	 */
	public void doStart() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void unpause() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param stateLose
	 * @param text
	 */
	public void setState(int stateLose, CharSequence text) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param outState
	 */
	public void saveState(Bundle outState) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return
	 */
	public BondCalculatorThread getThread() {
		// TODO Auto-generated method stub
		return this;
	}

}
