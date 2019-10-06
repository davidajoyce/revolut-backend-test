# revolut-backend-test
Design and implement a RESTful API (including data model and the backing implementation)  for money transfers between accounts.  

## Explicit requirements:  

1. You can use Java, Scala or Kotlin.  
2. Keep it simple and to the point (e.g. no need to implement any authentication).  
3. Assume the API is invoked by multiple systems and services on behalf of end users.  
4. You can use frameworks/libraries if you like (except Spring), but don't forget about  
requirement #2 – keep it simple and avoid heavy frameworks.  
5. The datastore should run in-memory for the sake of this test.  
6. The final result should be executable as a standalone program (should not require  
a pre-installed container/server).  
7. Demonstrate with tests that the API works as expected.  

## Implicit requirements:  
9. The code produced by you is expected to be of high quality.  
10. There are no detailed requirements, use common sense.


# Information

### Libraries

#### Packages used

   - DropWizard
   - Jetty (Server)
   - Jersey (Rest API)
   - Hibernate 
   - H2 database (Run in Memory Database)
    
# Instructions

### Build

    $ mvn package

### Run

    $ java -jar target/revolutTestExample-1.0-SNAPSHOT-shaded.jar server example.yml
    
    
### Server
   
   The Application starts a jetty server on localhost port 8080 with two sample accounts 
   
- http://localhost:8080/api/account/

### Available Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| POST | /api/account | create a new account
| GET | /api/account/{accountId} | get account by accountId |
| DELETE | /api/account/{accountId} | delete account by accountId | 
| POST | /api/transaction | perform transaction between 2 accounts | 
| GET | /api/transaction/{transactionId} | get transaction by id | 
| DELETE | /api/transaction/{transactionId} | get transaction by id | 
 
### Http Status
- 200 OK
- 402 Not Found (reason: fromAccountId and toAccountId must not be equals)
- 403 Not Found (reason: Value transferred must be greater than 0)
- 404 Not Found (reason: The following account does not have enough money)
- 500 Internal Server Error 

#### Transaction:
##### Body:
```sh
{  
   "accountfrom":18181818,
   "accountto":17171717
   "value":10
}
```

##### Response:
```sh
{
    "id": 1,
    "accountfrom":18181818,
    "accountto":17171717
    "value":10
}
```