package com.moneytransfer.controller;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.constants.HttpStatusCode;
import com.moneytransfer.dao.AccountDAO;
import com.moneytransfer.datamodel.Account;
import com.moneytransfer.exceptions.MoneyTransferException;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class AccountController {

	private static AccountDAO acDAO = null;

	public static void setDAO(AccountDAO accountDAO) {
		acDAO = accountDAO;
	}

	public static void createAccount(RoutingContext context) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			Account a = mapper.readValue(context.getBodyAsString(), Account.class);
			if (!isCurrencyValid(a.getCurrency())) {
				context.response().setStatusCode(HttpStatusCode.BAD_REQUEST)
						.putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encodePrettily("Invalid Currency"));
			}
			acDAO.save(a);
			context.response().setStatusCode(HttpStatusCode.CREATED_OK)
					.putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(a));
		} catch (Exception e) {
			context.response().setStatusCode(HttpStatusCode.BAD_REQUEST)
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(new MoneyTransferException("AccountRetrieval", e.getMessage())));
		}
	}

	private static boolean isCurrencyValid(Currency accountCurrency) {
		// validating currency
		return Currency.getAvailableCurrencies().contains(accountCurrency);
	}

	public static void getAllAccounts(RoutingContext context) {

		try {
			List<Account> accountList = acDAO.getAll();
			context.response().setStatusCode(HttpStatusCode.OK)
					.putHeader("content-type", "application/json; charset=utf-8").end(Json.encodePrettily(accountList));
		} catch (Exception e) {
			context.response().setStatusCode(HttpStatusCode.SERVER_ERROR)
					.putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(new MoneyTransferException("AccountRetrieval", e.getMessage())));
		}
	}

	public static void getAccount(RoutingContext context) {
		final String id = context.request().getParam("id");
		if (id == null) {
			context.response().setStatusCode(HttpStatusCode.BAD_REQUEST).end();
		} else {
			final Integer accountId = Integer.valueOf(id);
			Account account = acDAO.getAccountById(accountId);
			if (account == null) {
				context.response().setStatusCode(HttpStatusCode.PAGE_NOT_FOUND).end();
			} else {
				context.response().putHeader("content-type", "application/json; charset=utf-8")
						.end(Json.encodePrettily(account));
			}
		}
	}

	public static void deleteAccount(RoutingContext context) {
		final String id = context.request().getParam("id");
		if (id == null) {
			context.response().setStatusCode(HttpStatusCode.BAD_REQUEST).end();
		} else if (!acDAO.doesAccountExist(Integer.valueOf(id))) {
			context.response().setStatusCode(HttpStatusCode.PAGE_NOT_FOUND).end();
		} else {
			acDAO.deleteAccountById(Integer.valueOf(id));
			context.response().setStatusCode(HttpStatusCode.DELETE_OK).end();
		}
	}

	public static void updateAccount(RoutingContext context) {
		  
		 final String id = context.request().getParam("id");
		 JsonObject json = context.getBodyAsJson();
		 
		 if (id == null || json == null) {
             context.response().setStatusCode(HttpStatusCode.BAD_REQUEST).end();
		 } else {
             final Integer idAsInteger = Integer.valueOf(id);
             Account account = acDAO.getAccountById(idAsInteger);
             if (account == null) {
                 context.response().setStatusCode(HttpStatusCode.PAGE_NOT_FOUND).end();
             } else {
                 boolean updated = false;
                 if (json.getString("name") != null && !json.getString("name").isEmpty()) {
                     acDAO.setAccountName(idAsInteger, json.getString("name"));
                     updated = true;
                 }
                 if (json.getString("balance") != null && !json.getString("balance").isEmpty() && (new BigDecimal(json.getString("balance"))).compareTo(BigDecimal.ZERO) >= 0) {
                     acDAO.setAccountBalance(idAsInteger, new BigDecimal(json.getString("balance")));
                     updated = true;
                 }
                 if (json.getString("currency") != null && !json.getString("currency").isEmpty()) {
                     try {
                         acDAO.setAccountCurrency(idAsInteger, Currency.getInstance(json.getString("currency")));
                         updated = true;
                     } catch (Exception e) {
                         updated = false;
                     }
                 }
                 if (!updated) {
                     context.response().setStatusCode(HttpStatusCode.BAD_REQUEST).end();
                 } else {
                     context.response()
                             .putHeader("content-type", "application/json; charset=utf-8")
                             .end(Json.encodePrettily(account));
                 }
             }
		 }
	}
}
