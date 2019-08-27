# REST Api for Account to Account Money Transfer 

## Feature List 

1. CRUD Operations on Account 

2. Create a Transfer between two Accounts of same currency. Execute the Transfer to commit it.

## NOTE: 
    Transfer creation is extremely dumb. 
    Transfer execution checks for the below aspects
   a. Whether the transaction is in a sane ( TRAN_CREATED ) state ( There are three status for a Transacion TRAN_CREATED, TRAN_EXECUTED, TRAN_FAILED )
   b. Whether the source and Destination accounts have same currency.
   
   c. Balance check on the source account.


## Things for Future 

1. Authentication 

2. Multi currency transactions with a converter.

3. Transaction Date in UTC. Clients can convert to their local timezone.

3. Transaction fee processing.

4. Persistent Logging 

# Tools and Technologies used to develop this 

Eclipse, Maven, Java8, vertx.

# Program Arguments in Eclipse 
## run com.moneytransfer.controller.MoneyTransferController
## The above is the starting point which creates a server listening on port 8080 

# Create an Account

## Request

POST  http://localhost:8080/api/accounts

{
    "name": "testAccount",
    "balance": 1000,
    "currency": "EUR"
}

## Response

{
    "id": 1,
    "name": "testAccount",
    "balance": 1000,
    "currency": "EUR"
}

# Get all Accounts 

## Request
GET localhost:8080/api/accounts

## Response

[
    {
        "id": 1,
        "name": "testAccount",
        "balance": 1000,
        "currency": "EUR"
    },
    {
        "id": 2,
        "name": "testAccount2",
        "balance": 2000,
        "currency": "EUR"
    }
]

# Get Account Details

## Request 

GET localhost:8080/api/accounts/1

## Response

{
    "id": 1,
    "name": "testAccount",
    "balance": 1000,
    "currency": "EUR"
}

# Delete an Account 

## Request
DELETE localhost:8080/api/accounts/1


# Update an Account

## Request
PATCH localhost:8080/api/accounts/2

{
    "name": "UpdatedAccountPatch",
    "balance": "8000",   ##Note this Double quotes :-) 
    "currency": "EUR"
}

## Response

{
    "id": 2,
    "name": "UpdatedAccountPatch2",
    "balance": 8000,
    "currency": "EUR"
}


# create a Transfer 

## Request 

POST localhost:8080/api/transfers

{
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "amount": 100
}

## Response

{
    "id": 1,
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "date": 1566230798114,
    "amount": 100,
    "status": "TRAN_CREATED",
    "remarks": "Transaction Created" 
}

# Execute a Transfer 

## Request 

PUT localhost:8080/transfers/1 

{
    "id": 1,
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "date": 1566230798114,
    "amount": 100,
    "status": "TRAN_CREATED",
    "remarks": "Transaction Created"
}

## Response

{
    "id": 1,
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "date": 1566230798114,
    "amount": 100,
    "status": "TRAN_EXECUTED",
    "remarks": "Transaction Executed Successfully!"
}

# Get a Transaction 

# Request 

GET localhost:8080/api/transfers/1

# Response 

{
    "id": 1,
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "date": 1566230798114,
    "amount": 100,
    "status": "TRAN_EXECUTED",
    "remarks": "Transaction Executed Successfully!"
}

# An Illustation of a Failed Transaction 

## Let's look at two accounts 

[
    {
        "id": 1,
        "name": "UpdatedAccountPatch",
        "balance": 1900,
        "currency": "EUR"
    },
    {
        "id": 2,
        "name": "UpdatedAccountPatch2",
        "balance": 8100,
        "currency": "EUR"
    }
]

## Let's Post a Debit Transaction from Account 2 with amount greater than 1900 

## POST localhost:8080/api/transfers

{
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "amount": 10000
}

## Response 

{
    "id": 3,
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "date": 1566231098086,
    "amount": 10000,
    "status": "TRAN_CREATED",
    "remarks": "Transaction Created"
}

## Now, Let's Try to Execute the Transfer 

## PUT localhost:8080/api/transfers/3


{
    "id": 3,
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "date": 1566231098086,
    "amount": 10000,
    "status": "TRAN_CREATED",
    "remarks": "Transaction Created"
}

## Response 

{
    "id": 3,
    "srcAcctNum": 1,
    "destAcctNum": 2,
    "date": 1566231098086,
    "amount": 10000,
    "status": "TRAN_FAILED",
    "remarks": "Transaction Failed! Insufficient Balance in Account1"
}
