# restaurant-decision-maker
This file describes the Spring Boot backend for the Restaurant Picker application, a collaborative tool for teams to decide on Restaurant locations.

###Project Overview
This backend service handles user sessions, restaurant submissions, and random selection logic. It interacts with a frontend (potentially AngularJS) to provide a seamless user experience.

###Features
User are required to register & login to create or join a session.  
Manages session creation, joining, and termination.  
Enables users to submit restaurant preferences.  
Stores submitted restaurants for each session.  
Randomly selects a restaurant upon session closure.

###Technologies
Spring Boot  
Java

###Running the Application
Ensure you have Java and Maven installed.  
Clone or download the project repository.  
Navigate to the project directory in your terminal.  
Run mvn clean install to build the application.  
Run java -jar target/lunch-picker-backend.jar to start the server.  

