package com.moneytransfer.exceptions;

public class MoneyTransferException {
    private String exceptionName;
    private String exceptionMessage;
    
    public MoneyTransferException() {
    	
    }
    
    public MoneyTransferException(String exName, String exMsg) {
    	exceptionName = exName;
    	exceptionMessage = exMsg;
    }

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
