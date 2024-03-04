User Authentication : Signup and login with JWT-Based User Authentication and Authorisation. 
Url API's : Manage Url via RESTful endpoints (/generate, /{shortUrl}, /dashboard). 
Authentication Required : Only logged-in users can perform url shortening and can manage the Url's.
MurmurHash3 Algorithm : Murmur3 is used for generating unique hash key for original URL. This key will be treated as the shorter URL.
Spring Data JPA Integration : Data storage and retrieval using JPA and a Mysql database. 
SpringBoot:Spring Boot eliminates boilerplate code, letting you jumpstart development and focus on core functionality. 

Project Implementation

 type this in local folder where you want to store code git clone https://github.com/gtejakrishna2000/UrlShort.git
- Make sure you have access to local or any MySQL server.
- Open project in your favorite editor and change application.properties file to point to your MySQL database (change username and password)
- Build SpringBoot project.


Project Working

Step 1: User Registration Endpoint: The userRegistration method in the controller handles POST requests to the "/AddCustomer" endpoint for user registration. It accepts an AddCustomerDto object containing user registration details, attempts to add the user to the database using the userService,every one can access to this end point.it permit all no authorization is require. ***Imp mail id is unique so provide unique maild for adding user.
 
Step 2: Jwt Token generation: User needs to provide Login credentials Like username and password. here we generate a token which is used for performing further crud operation on url's.

Step 3: 

1.customer/urls/generate: Generates the short url when the user provides longurl, if it is successful  it returns an appropriate short url string response based on the success or error message for failure of the operation. Note: Before performing step 3 -> select authorisation in postman and select bearer token field and paste token generated(step 2) in it. 

2.customer/urls/{shorturlstring}: Redirects to the original url address or an appropriate error response if the url is not found or if an unexpected error occurs.

3.dashboard: Displays all short urls generated for the long urls for perticular user and url analytics like count of short url clicks

project structure

/Controller : Handles incoming requests, maps them to appropriate methods, and orchestrates the flow of data in Spring MVC applications.

/Service: Implements business logic and performs operations on data retrieved from repositories, promoting separation of concerns and maintainability.

/Models: Represents domain objects or entities, encapsulating data and behavior, ensuring consistency and integrity in the application.

/Repository: Interacts with the Mysql database or external data sources, providing CRUD (Create, Read, Update, Delete) operations for domain objects, promoting data persistence and retrieval.

/DTOs (Data Transfer Objects): Transfers data between different layers of the application or between the application and external systems, facilitating loose coupling and preventing overexposure of domain objects.

/Transformers: Converts data between different formats or structures, such as transforming domain entities to DTOs or vice versa, ensuring compatibility between layers and systems.

/Security: Implements authentication, authorization, and other security mechanisms to protect resources and enforce access control, ensuring the confidentiality, integrity, and availability of the application.

Notes:
1. Token gets expired after 60 Minutes for defining custom time go to jwt service file and in createToken method body select setExpiration change the number 60 to ur custom minutes
2. Short url expires after 10 minutes for custom time go to url service and select getExpirationDate and change 600 seconds to the custom number
