package com.moneytransfer.controller;

import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.constants.HttpStatusCode;
import com.moneytransfer.dao.TransferDAO;
import com.moneytransfer.datamodel.Account;
import com.moneytransfer.datamodel.Transfer;
import com.moneytransfer.datamodel.TransferStatus;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class TransferController {

	private static TransferDAO tranDAO = null;

	public static void setDAO(TransferDAO transactionDAO) {
		tranDAO = transactionDAO;
	}

	public static void createTransfer(RoutingContext context) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			Transfer t = mapper.readValue(context.getBodyAsString(), Transfer.class);
			t.setRemarks("Transaction Created");
			tranDAO.save(t);
			context.response().setStatusCode(HttpStatusCode.CREATED_OK)
					.putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(t));
		} catch (Exception e) {
			context.response().setStatusCode(HttpStatusCode.BAD_REQUEST)
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(e.getMessage()));
		}
	}

	public static void getAllTransfers(RoutingContext context) {
       try {
			List<Transfer> transferList = tranDAO.getAll();
			context.response().setStatusCode(HttpStatusCode.OK).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(transferList));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void executeTransfer(RoutingContext context) {
		final String id = context.request().getParam("id");
		if (id == null) {
			context.response().setStatusCode(HttpStatusCode.BAD_REQUEST).end();
		} else {
			final Integer tranId = Integer.valueOf(id);
			Transfer transfer = tranDAO.getTransferById(tranId);
			if (transfer == null) {
				context.response().setStatusCode(HttpStatusCode.PAGE_NOT_FOUND).end();
			} else {
				// If there is sufficient amount in source account, transaction is not already
				// executed, not failed.
				// both source and destination account currencies are same
				StringBuffer transactionRemarks = new StringBuffer();
				Account srcAccount = tranDAO.getSourceAccount(transfer);
				Account destAccount = tranDAO.getDestinationAccount(transfer);
				
				if (validateTransfer(transfer,srcAccount, destAccount, transactionRemarks)) {
					srcAccount.withdraw(transfer.getAmount());
					destAccount.deposit(transfer.getAmount());
					tranDAO.setTransferStatusConfirmed(transfer,  transactionRemarks.toString());
					context.response().setStatusCode(HttpStatusCode.OK)
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(transfer));
				} else {
					tranDAO.setTransferStatusFailed(transfer, transactionRemarks.toString());
					context.response().setStatusCode(HttpStatusCode.BAD_REQUEST)
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(transfer));
				}
			}
		}
	}

	private static boolean validateTransfer(Transfer transfer,Account srcAccount, Account destAccount, StringBuffer tranRemarks) {
        
		String failureRemarks="";
		
		if (srcAccount == null) {
			failureRemarks = "Source Account "+transfer.getSrcAcctNum()+" does not exist";
		} else if (destAccount == null) {
			failureRemarks = "Destination Account "+transfer.getDestAcctNum()+" does not exist";
		} else {
			boolean isValidTransactionState = transfer.getStatus() != TransferStatus.TRAN_EXECUTED
            		&& transfer.getStatus() != TransferStatus.TRAN_FAILED;
            if(!isValidTransactionState){
            	failureRemarks = "Transfer you attempted to execute is in the state "+transfer.getStatus();
            } else if(!srcAccount.getCurrency().equals(destAccount.getCurrency())) {
            	failureRemarks = "Currency of Account "+srcAccount.getId()+ " and that of Account "+destAccount.getId()+" do not match!";
            } else if (srcAccount.getBalance().compareTo(transfer.getAmount()) < 0) {
            	failureRemarks = "Insufficient Balance in Account"+srcAccount.getId();
            } 
		}
		
		if(!failureRemarks.isEmpty()) {
			tranRemarks.append("Transaction Failed! "+failureRemarks);
			return false;
		} else {
			tranRemarks.append("Transaction Executed Successfully!");
		}
        return true;
	}

	public static void getTransfer(RoutingContext context) {
		final String id = context.request().getParam("id");
		if (id == null) {
			context.response().setStatusCode(HttpStatusCode.BAD_REQUEST).end();
		} else {
			final Integer tranId = Integer.valueOf(id);
			Transfer transfer = tranDAO.getTransferById(tranId);
			if (transfer == null) {
				context.response().setStatusCode(HttpStatusCode.PAGE_NOT_FOUND).end();
			} else {
				context.response().putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encodePrettily(transfer));
			}
		}
	}
}
