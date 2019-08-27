/**
 * 
 */
package com.moneytransfer.dao;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import com.moneytransfer.database.MoneyTransferDB;
import com.moneytransfer.datamodel.Account;

/**
 * @author admedava
 *
 */
public class AccountDAO implements DAO<Account, Integer> {

	private MoneyTransferDB databaseInstance;

	public AccountDAO(MoneyTransferDB database) {
		databaseInstance = database;
	}

	public Account get(Account account) {
		return databaseInstance.getAccount(account.getId());
	}

	public List<Account> getAll() {
		return databaseInstance.getAllAccounts();
	}

	public void save(Account account) {
		databaseInstance.createAccount(account);
	}

	public void delete(Account account) {
		databaseInstance.removeAccount(account.getId());
	}

	public Account getById(Integer accountId) {
		return databaseInstance.getAccountById(accountId);
	}

	public boolean doesAccountExist(Integer accountId) {
		return databaseInstance.getAccountById(accountId) != null;
	}

	public void deleteById(Integer accountId) {
		databaseInstance.removeAccount(accountId);
	}

	public synchronized void setAccountName(Integer accountId, String name) {
		databaseInstance.setAccountName(accountId, name);
	}

	public synchronized void setAccountBalance(Integer accountId, BigDecimal balance) {
		databaseInstance.setAccountBalance(accountId, balance);
	}

	public synchronized void setAccountCurrency(Integer accountId, Currency currency) {
		databaseInstance.setAccountCurrency(accountId, currency);
	}
}
