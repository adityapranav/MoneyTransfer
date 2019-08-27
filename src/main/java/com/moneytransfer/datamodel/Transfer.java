package com.moneytransfer.datamodel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Data Model class for Transfer 
 * 
 * @author admedava
 *
 */
public class Transfer {
	
	private int id;
	private int srcAcctNum;
	private int destAcctNum;
	private Date date;
	private BigDecimal amount;
	private TransferStatus status;
	private String remarks;
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	private static AtomicInteger TRANIDSEQUENCE = new AtomicInteger(0);

	public Transfer() {
		id = TRANIDSEQUENCE.addAndGet(1);
		status = TransferStatus.TRAN_CREATED;
		date = new Date(System.currentTimeMillis());
	}
	
	public Transfer(int srcAccountNum, int destAccountNum, BigDecimal tranAmount, String tranRemarks) {
		id = TRANIDSEQUENCE.addAndGet(1);
		srcAcctNum = srcAccountNum;
		destAcctNum = destAccountNum;
		date = new Date(System.currentTimeMillis());
		amount = tranAmount;
		status = TransferStatus.TRAN_CREATED;
		remarks = tranRemarks;
	}
	
	public TransferStatus getStatus() {
		return status;
	}

	public void setStatus(TransferStatus transferStatus) {
		status = transferStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int transferId) {
		id = transferId;
	}

	public int getSrcAcctNum() {
		return srcAcctNum;
	}

	public void setSrcAcctNum(int sourceAccountNumber) {
		this.srcAcctNum = sourceAccountNumber;
	}

	public int getDestAcctNum() {
		return destAcctNum;
	}

	public void setDestAccountNumber(int destAccountNumber) {
		destAcctNum = destAccountNumber;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date transferDate) {
		date = transferDate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal transferAmount) {
		amount = transferAmount;
	}
}
