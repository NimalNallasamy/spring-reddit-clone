# Read Me

This project is all about building the reddit cloned application. 

#Technologies used :
1. Spring Boot for the backend.
    1. For encoding password : BCryptPasswordEncoder.
    2. For Random String : UUID
    3. For ready-made templates for email : Thymeleaf
2. Angular for the frontend. 
3. PostgresQL as the DB.
4. MailTrap - Fake SMTP server to send mail.
5. MapStruct - Java library which generates the mapping code.
6. Time ago - a Kotlin Library to calculate the time difference.
7. Swagger and SpringFox - To document the Rest APIs we have built.

#Steps followed in building this project :
1. On the first step, we will need to check if all the mentioned technologies are available in our development setup.
2. Then we create the entity files which acts as the base for relation creation in Postgres
3. Then we create the repository files for each of the entity, and then start the server to know if there are any issues in creating the db relations.
4. Then we move on to create the security file.
    1. As a security measure, we create the SecurityConfig.java, which acts as the central security file.
    2. Since we use rest apis we disable the cors.
    3. Also, we make sure the non rest apis are authenticated.
5. Now we create the controller files.
    1. The first one being AuthController.java
6. To accommodate the controller changes we build the service packages, which contains the business logic.
    1. For the AuthController we need to have AuthService. In this authService class, we create a new user. Also, we encode the password using BCryptPassword Encoder.  
7. For sending the mail on successful sign up, we make use of mail trap mock server.
    1. For that we get the user name and password from mail trap page and configure it on our application.yaml file.
    2. Then we make use of MimeMessagePreparator and MimemessageHelper to construct the mail.
    3. We use JavaMailSender to send the mail, which contains the Auth URL, which is used to verify the account.
8. Construct the Controller, Services, DTO, Mapper(if needed), for SubReddit, Post, Comment and Vote.
9. To calculate the duration, we make use of the Time ago, a Kotlin library. 
   
#User authentication using JWT a brief explanation :
   ##HighLevel Overview
   1. First the client issues a login request with their credentials.
   2. Server validates the credentials and issues a *JSON WEB TOKEN*.
   3. This JSON Web Token is provided back to the client.
   4. Client makes use of this token for each and every request that it makes to the server.
   5. Server validates the token, and provides the response for client's request. 

   ##Technical Overview
   1. Authentication request reaches the AuthService class.
   2. Inside the AuthService class we retrieve the username and password and construct UserNamePasswordAuthenticationToken class, pass this to Authentication Manager class.
   3. AuthenticationManager class makes use of the UserDetails service which tries to retrieve the UserDetails from the DB, and throws an Exception if not found.
   4. On successful fetch of UserDetails, AuthenticationManager would provide Authentication details to AuthService, which in turn creates a JWT and provides the client.  
   5. When the user is provided with the JWT token, they use it authorises the request like : Authorization : Bearer Jwt Token
   6. We do have a intermediate filter JWT filter. Which takes the JWT token from the header. And tries to authorize the request. If success, forwards to respective controller.
   7. We can invalidate the JWT Token in 6 methods.
      1. Delete the token from the browser.
      2. Introduce Expiry Time for the tokens.
      3. Using Refresh Tokens. (We use this in our project)
      4. Token Blacklisting.

#MapStruct

   1. MapStruct is a Java Library that is used for generating the mapping code and sharing the data between 2 objects. 
   2. Url for mapstruct - https://mapstruct.org/
   
#Time Ago
   1. It's a kotlin library to calculate the time duration between 2 times.
   2. Documentation Url : https://github.com/marlonlom/timeago

#SpringFox & Swagger
   1. The Springfox suite of java libraries are all about automating the generation of machine and human readable specifications for JSON APIs written using the spring family of projects.
   2. Documentation Url : https://springfox.github.io/springfox/docs/current/#introduction
   3. Docket stands for  - **A summary or other brief statement of the contents of a document; an abstract.**
   4. Documentation Url of APIs when server started up - http://localhost:8080/swagger-ui.html#/

#Use Case of this project :
This project is similar to reddit. Below we mention the use case of this project.
1. User signs up an account in our reddit-clone.
2. Once user signs up, the user must be receiving a mail to verify themselves.
3. Once the user verifies themselves, backend entry should be maintained for the same.
4. Once the user logs in, they must be authenticated usign JWT tokens.
5. Once the user logs out, the tokens must be unvalidated.

