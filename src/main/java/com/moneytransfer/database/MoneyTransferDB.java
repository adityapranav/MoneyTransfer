package com.moneytransfer.database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.moneytransfer.datamodel.Account;
import com.moneytransfer.datamodel.Transfer;

public class MoneyTransferDB {

	private Map<Integer, Account> accounts = null;
	private Map<Integer, Transfer> transfers = null;

	private static final MoneyTransferDB databaseInstance = new MoneyTransferDB();

	private MoneyTransferDB() {
		accounts = new ConcurrentHashMap<Integer, Account>();
		transfers = new ConcurrentHashMap<Integer, Transfer>();
	}

	public static MoneyTransferDB getDatabaseInstance() {
		return databaseInstance;
	}

	public Account getAccount(int accountId) {
		return accounts.get(accountId);
	}

	public List<Account> getAllAccounts() {
		return new ArrayList<Account>(accounts.values());
	}

	public void createAccount(Account t) {
		accounts.put(t.getId(), t);
	}

	public void removeAccount(int accountId) {
		accounts.remove(accountId);
	}

	public Account getAccountById(Integer accountId) {
		return accounts.get(accountId);
	}

	public Transfer getTransfer(int transactionId) {
		return transfers.get(transactionId);
	}

	public List<Transfer> getAllTransfers() {
		return new ArrayList<Transfer>(transfers.values());
	}

	public synchronized void createTransfer(Transfer t) {
		transfers.put(t.getId(), t);
	}

	public void removeTransfer(int transactionId) {
		transfers.remove(transactionId);
	}

	public Transfer getTransferById(Integer transactionId) {
		return transfers.get(transactionId);
	}

	public void setAccountName(Integer accountId, String name) {
		accounts.get(accountId).setName(name);
	}

	public void setAccountBalance(Integer accountId, BigDecimal balance) {
		accounts.get(accountId).setBalance(balance);
	}
	
	public void setAccountCurrency(Integer accountId, Currency currency) {
		accounts.get(accountId).setCurrency(currency);
	}
}
