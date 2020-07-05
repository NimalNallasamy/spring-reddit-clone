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
    
#Use Case of this project :
This project is similar to reddit. Below we mention the use case of this project.
1. User signs up an account in our reddit-clone.
2. Once user signs up, the user must be receiving a mail to verify themselves.
3. Once the user verifies themselves, backend entry should be maintained for the same.
4. Once the user logs in, they must be authenticated usign JWT tokens. 
5. 