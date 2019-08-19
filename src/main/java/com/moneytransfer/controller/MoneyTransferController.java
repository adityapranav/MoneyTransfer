package com.moneytransfer.controller;

import com.moneytransfer.dao.AccountDAO;
import com.moneytransfer.dao.TransferDAO;
import com.moneytransfer.database.MoneyTransferDB;

import io.vertx.core.*;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MoneyTransferController extends AbstractVerticle {

	private static final MoneyTransferDB databaseInstance = MoneyTransferDB.getDatabaseInstance();
	private static final TransferDAO tranDAO = new TransferDAO(databaseInstance);
	private static final AccountDAO acDAO = new AccountDAO(databaseInstance);

	@Override
	public void start(Future<Void> f) {

		Router router = Router.router(vertx);

		registerHandlers(router);

		router.route("/").handler(routingContext -> {
			HttpServerResponse response = routingContext.response();
			response.putHeader("content-type", "text/html").end("<h1>MoneyTransfer</h1>");
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(8080, result -> {
			if (result.succeeded()) {
				f.complete();
			} else {
				f.fail(result.cause());
			}
		});
	}

	private void registerHandlers(Router router) {
		router.route().handler(BodyHandler.create());
		
		// Accounts API Handlers
		AccountController.setDAO(acDAO);
		router.post("/api/accounts").blockingHandler(AccountController::createAccount);
		router.get("/api/accounts").blockingHandler(AccountController::getAllAccounts);
		router.get("/api/accounts/:id").blockingHandler(AccountController::getAccount);
		router.delete("/api/accounts/:id").blockingHandler(AccountController::deleteAccount);
		router.patch("/api/accounts/:id").blockingHandler(AccountController::updateAccount);
		// we can do a patch as well.
		
		// Transactions API Handlers
		TransferController.setDAO(tranDAO);
		router.post("/api/transfers").blockingHandler(TransferController::createTransfer);
		router.get("/api/transfers").blockingHandler(TransferController::getAllTransfers);
		router.put("/api/transfers/:id").blockingHandler(TransferController::executeTransfer);
		router.get("/api/transfers/:id").blockingHandler(TransferController::getTransfer);
	}

}
