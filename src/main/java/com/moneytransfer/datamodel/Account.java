/**
 * 
 */
package com.moneytransfer.datamodel;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Aditya
 * @Summary Model class for Account.
 */
public class Account {

	private int        id;
	private String     name;
	private BigDecimal balance;
	private Currency   currency;
	
	private static AtomicInteger ACCOUNTIDSEQUENCE = new AtomicInteger(0);
	
	public Account() {
		this.id = ACCOUNTIDSEQUENCE.addAndGet(1);
	}
	
	public Account(String accountName, BigDecimal initialBalance, Currency accountCurrency) {
		name = accountName;
		balance = initialBalance;
		currency = accountCurrency;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String accountName) {
		name = accountName;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal accountBalance) {
		balance = accountBalance;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency accountCurrency) {
		currency = accountCurrency;
	}
	
	public int getId() {
		return id;
	}
	
	public synchronized void deposit(BigDecimal amount) {
		balance = balance.add(amount);
	}
	
	public synchronized void withdraw(BigDecimal amount) {
		balance = balance.subtract(amount);
	}
}
