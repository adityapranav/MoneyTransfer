package com.moneytransfer.controller;

import com.moneytransfer.dao.AccountDAO;
import com.moneytransfer.dao.TransferDAO;
import com.moneytransfer.database.MoneyTransferDB;

import io.vertx.core.*;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * MoneyTransferController is the entry point class for MoneyTransfer service.
 * Requests are routed to AccountController and TransferController based on the
 * incoming request.
 * 
 * @author Aditya.
 *
 */
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

	/**
	 * This method registers handlers for various API end points.
	 * @param router - vert.x router
	 */
	private void registerHandlers(Router router) {
		router.route().handler(BodyHandler.create());
		
		// Accounts API Handlers
		AccountController.setDAO(acDAO);
		router.post("/api/accounts").handler(AccountController::createAccount);
		router.get("/api/accounts").handler(AccountController::getAllAccounts);
		router.get("/api/accounts/:id").handler(AccountController::getAccount);
		router.delete("/api/accounts/:id").handler(AccountController::deleteAccount);
		router.patch("/api/accounts/:id").handler(AccountController::updateAccount);
		
		// Transactions API Handlers
		TransferController.setDAO(tranDAO);
		router.post("/api/transfers").handler(TransferController::createTransfer);
		router.get("/api/transfers").handler(TransferController::getAllTransfers);
		router.get("/api/transfers/:id").handler(TransferController::getTransfer);
		router.put("/api/transfers/:id").handler(TransferController::executeTransfer);
	}

}
