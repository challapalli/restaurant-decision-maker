# restaurant-decision-maker
This file describes the Spring Boot backend for the Restaurant Picker application, a collaborative tool for teams to decide on Restaurant locations.  

### Project Overview  
This backend service handles user sessions, restaurant submissions, and random selection logic. It interacts with a frontend (potentially AngularJS) to provide a seamless user experience.
This version leverages Spring Boot and WebSockets to provide real-time updates on restaurant submissions, enhancing the user experience.

### Features  
User are required to register & login to create or join a session.  
Manages session creation, joining, and termination.  
Enables users to submit restaurant preferences.  
Stores submitted restaurants for each session.  
Randomly selects a restaurant upon session closure.  
User created session can only end the session.  

### Key Technologies:  
Spring Boot: Provides a robust backend framework for building the application.  
Java: The primary programming language for backend development.  
WebSockets: Enables bi-directional communication between the server and clients, allowing real-time updates without constant page refreshes.  

### Improved Functionality:  
Real-time Restaurant Updates:  
Users submitting restaurants will trigger a WebSocket message sent to all connected clients.  
Clients will receive the update and dynamically add the new restaurant to their displayed list.  
This eliminates the need for manual page refreshes, ensuring everyone sees the latest submissions.  

### Running the Application  
Ensure you have Java and Maven installed.  Java version 17 & above.  
Clone or download the project repository, Run git clone https://github.com/challapalli/restaurant-decision-maker.git  
Navigate to the project directory in your terminal, cd restaurant-decision-maker  
Run mvn clean install to build the application.  
Run java -jar target/restaurant-decision-maker.jar to start the server.  

### API Endpoints:  

Get Restaurants (GET /api/restaurants/{sessionId})  
Lists all restaurants submitted for a session.  
Add Restaurant (POST /api/restaurants)  
Submits a new restaurant suggestion.  

Get All Sessions (GET /api/sessions)  
Lists all active sessions.  
Create Session (POST /api/sessions)  
Creates a new session with a username. Body: { "createdBy": "username" }  
Join Session (POST /api/sessions/{sessionId}/join)  
Attempts to join a session using its ID.  
End Session (POST /api/sessions/{sessionId}/end)  
Ends a session and potentially returns a randomly chosen restaurant (implementation dependent).  

Register (POST /api/register)  
Creates a new user account.  
Login (POST /api/login)  
Authenticates a user and generates a JWT token.  


