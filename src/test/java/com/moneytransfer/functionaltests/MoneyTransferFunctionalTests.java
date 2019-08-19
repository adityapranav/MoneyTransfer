package com.moneytransfer.functionaltests;

import com.jayway.restassured.RestAssured;
import com.moneytransfer.constants.HttpStatusCode;
import com.moneytransfer.controller.MoneyTransferController;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import static com.jayway.restassured.RestAssured.*;

import java.net.ServerSocket;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MoneyTransferFunctionalTests {

	private static Vertx vertx = null;
	
	@BeforeClass
	public static void initialize() {
		 RestAssured.baseURI = "http://localhost";
		 RestAssured.port = 8080;
	}

	@Test
	public void testCreateAccount() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\"name\": \"testAccount\",").append("\"balance\": 100,").append("\"currency\": \"INR\"")
				.append("}");

		given().body(sb.toString()).when().post("api/accounts").then().assertThat()
				.statusCode(HttpStatusCode.CREATED_OK);
	}

	@Test
	public void testCreateAccountFailure() {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\"name\": \"testAccount\",").append("\"balance\": 100,").append("\"currency\": \"JUNK\"")
				.append("}");

		given().body(sb.toString()).when().post("api/accounts").then().assertThat()
				.statusCode(HttpStatusCode.BAD_REQUEST);
	}

	@Test
	public void testCreateAndExecuteTransaction() {
		StringBuilder srcAccount = new StringBuilder();
		srcAccount.append("{").append("\"name\": \"testTransrcAccount\",").append("\"balance\": 10000,")
				.append("\"currency\": \"USD\"").append("}");

		StringBuilder destAccount = new StringBuilder();
		destAccount.append("{").append("\"name\": \"testTrandestAccount\",").append("\"balance\": 1000,")
				.append("\"currency\": \"USD\"").append("}");

		int srcAccountId = given().body(srcAccount.toString()).when().post("api/accounts").then().assertThat()
				.statusCode(HttpStatusCode.CREATED_OK).extract().jsonPath().getInt("id");

		int destAccountId = given().body(destAccount.toString()).when().post("api/accounts").then().assertThat()
				.statusCode(HttpStatusCode.CREATED_OK).extract().jsonPath().getInt("id");

		StringBuilder tran = new StringBuilder();
		tran.append("{").append("\"srcAcctNum\":" + srcAccountId + ",").append("\"destAcctNum\":" + destAccountId + ",")
				.append("\"amount\": 1000").append("}");

		Integer transferId = given().body(tran.toString()).when().post("api/transfers").then().assertThat()
				.statusCode(HttpStatusCode.CREATED_OK).extract().jsonPath().getInt("id");
		
		// Try to execute the transfer;
		String status = given().body(tran.toString()).when().put("/api/transfers/"+transferId).then().assertThat().statusCode(HttpStatusCode.OK).extract().jsonPath().getString("status");
		Assert.assertEquals(status, "TRAN_EXECUTED");
	}
    
	@Test
	public void testExecuteTransactionFailureInsufficientBalance() {
		StringBuilder srcAccount = new StringBuilder();
		srcAccount.append("{").append("\"name\": \"testTransrcAccount\",").append("\"balance\": 10000,")
				.append("\"currency\": \"USD\"").append("}");

		StringBuilder destAccount = new StringBuilder();
		destAccount.append("{").append("\"name\": \"testTrandestAccount\",").append("\"balance\": 1000,")
				.append("\"currency\": \"USD\"").append("}");

		int srcAccountId = given().body(srcAccount.toString()).when().post("api/accounts").then().assertThat()
				.statusCode(HttpStatusCode.CREATED_OK).extract().jsonPath().getInt("id");

		int destAccountId = given().body(destAccount.toString()).when().post("api/accounts").then().assertThat()
				.statusCode(HttpStatusCode.CREATED_OK).extract().jsonPath().getInt("id");

		StringBuilder tran = new StringBuilder();
		tran.append("{").append("\"srcAcctNum\":" + srcAccountId + ",").append("\"destAcctNum\":" + destAccountId + ",")
				.append("\"amount\": 20000").append("}");

		Integer transferId = given().body(tran.toString()).when().post("api/transfers").then().assertThat()
				.statusCode(HttpStatusCode.CREATED_OK).extract().jsonPath().getInt("id");
		// Try to execute the transfer
		given().body(tran.toString()).when().put("/api/transfers/"+transferId).then().assertThat().statusCode(HttpStatusCode.BAD_REQUEST);
	}
	
	@AfterClass
	public static void cleanUp() {
		RestAssured.reset();
	}
}
