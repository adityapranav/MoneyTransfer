

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
