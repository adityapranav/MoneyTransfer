/**
 * 
 */
package com.moneytransfer.dao;

import java.util.List;

import com.moneytransfer.database.MoneyTransferDB;
import com.moneytransfer.datamodel.Account;
import com.moneytransfer.datamodel.Transfer;
import com.moneytransfer.datamodel.TransferStatus;

/**
 * @author admedava
 *
 */
public class TransferDAO implements DAO<Transfer> {

	private MoneyTransferDB databaseInstance;
	
	public TransferDAO(MoneyTransferDB database) {
		databaseInstance = database;
	}
	
	public Transfer get(Transfer t) {
		return databaseInstance.getTransfer(t.getId());
	}

	public List<Transfer> getAll() {
		return databaseInstance.getAllTransfers();
	}

	public void save(Transfer t) {
		databaseInstance.createTransfer(t);
	}

	public void delete(Transfer t) {
		databaseInstance.removeTransfer(t.getId());
	}

	public Transfer getTransferById(Integer transferId) {
		return databaseInstance.getTransferById(transferId);
	}
	
	public boolean doesTransferExist(Integer transferId) {
		return databaseInstance.getTransferById(transferId) != null;
	}

	public void deleteTransferById(Integer transferId) {
		databaseInstance.removeTransfer(transferId);
	}
	
	public Account getSourceAccount(Transfer t) {
		return databaseInstance.getAccount(t.getSrcAcctNum());
	}
	
	public Account getDestinationAccount(Transfer t) {
		return databaseInstance.getAccount(t.getDestAcctNum());
	}

	public synchronized void setTransferStatusConfirmed(Transfer t, String remarks) {
		t.setStatus(TransferStatus.TRAN_EXECUTED);
		t.setRemarks(remarks);
	}
	
	public synchronized void setTransferStatusFailed(Transfer t, String remarks) {
		t.setStatus(TransferStatus.TRAN_FAILED);
		t.setRemarks(remarks);
	}
}
