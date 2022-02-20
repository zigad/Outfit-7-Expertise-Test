# Instructions

You have 2 options to run the program:

- Use IDE, open src/java/si/deisinger/Services/Fun7Application.class and run it,
- Open terminal, cd into /target/ folder and run: java -jar Outfit-7-Expertise-Test-1.0-SNAPSHOT.jar (Please note that you should have environmental variables to be able to access Google Cloud - DataStore)

There are some comments about API endpoints in the code but for easier understanding you can access Swagger UI here: `http://localhost:1598/swagger-ui/index.html`

You will also find POSTMAN json, so you can import API requests with sample data into the application. You can find it under `src/main/resources/Outfit 7 Backend.postman_collection.json`


# Software Engineer Expertise Test: Services

Mobile game “Fun7” includes many different features e.g. multiplayer, in-app purchases, cross 
promotion, ads or internal surveys. However not all of the features are available to all users all 
the time. For simplicity purposes you can assume that “Fun7” supports only 3 features: 
multiplayer, customer-support and ads.


In order for “Fun7” to know if it should enable or disable a particular feature, it will contact 
backend servers via a REST endpoint.

Application should expose two REST APIs, one for checking the state of the services by the 
users and one for administrators.

### Check services API
1. It should expose the endpoint that will return the status of the three services.

2. It should accept three query parameters:
   - `timezone`: timezone of the user 
   - `userId`: string id of the user 
   - `cc`: country code of the user
3. Response should contain information about enabled services in json format. Sample json: `{“multiplayer”: “enabled”, ”user-support”: “disabled”, “ads”: “enabled”}`,
   
### Admin API
1. API is used by administrators to enable managing of users
2. It should expose one or more endpoints with the following features:
   - List all users
   - Get user details
   - Delete a user
   

Each service should be enabled based on the following checks:

### Multiplayer
Multiplayer is a feature that is available only for more skilled players so it should be enabled if a 
user has used the “Fun7” game more than 5 times (based on the number of API calls). Also our 
multiplayer server infrastructure is located in the US so it should be enabled only if the user 
comes from the US.


### Customer Support
Customer support should be enabled only on work days between 9:00 - 15:00 Ljubljana time, 
because only then support personnel is available.


### Ads
Ads in the game are served by the external partner so this service should be enabled only if our 
external partner supports user device. And to know it, we must call partner’s public API which is 
already provided and is secured by basic access authentication (via HTTP header).

The (external) API specs:

HTTP Method: GET

URL : https://us-central1-o7tools.cloudfunctions.net/fun7-ad-partner

Query params expected:

countryCode: The country code of the user User name (for basic auth): fun7user

Password (for basic auth): fun7pass

**HTTP Response body:**
- 200OK 
  - If ads are enabled:
  Response body: {“ads”: “sure, why not!”}
  - If ads are disabled:
  Response body: {“ads”: “you shall not pass!”}
  - 400 BAD REQUEST - missing mandatory parameters
  - 401 UNAUTHORIZED - invalid credentials
  - 500 SERVER ERROR - server is temporarily not available


  Your assignment is to design and implement a backend application for “Fun7” mobile game 
  and prepare unit and integration tests that will validate your solution.


### Technical requirements:
1. The solution should be written in Java or Kotlin.
2. Your solution should leverage Google Cloud (App Engine, Cloud Run or similar) and you
   can (but don’t have to) use other Google Cloud components (e.g. Firestore, CloudSQL,
   Memorystore etc).
3. Feel free to use any external libraries of your choice (e.g. REST framework or
   persistence library).
4. Please send us either a zip archive or a link to project on the github/bitbucket (preferred).
5. Make sure that your solution is production ready so apply all the techniques that you
   would normally do when writing code in a real life situation.
6. Please provide a readme file with the description on how to build your application and how to run it.
7. If something is not clear feel free to make some assumptions. In that case make sure to put it in the readme file. It should contain all the assumptions you made and all the extra information or explanations that you think might be useful for anybody reading your solution.
 