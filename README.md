
# KitchenSink

A Test project to showcase the migration of the project from leagcy Jboss to latest spring and react.

[original JBoss project](https://github.com/jboss-developer/jboss-eap-quickstarts/tree/8.0.x/kitchensink)



## Running Tests

To run tests run the following command to make the tests

```bash
./gradlew test
```


## Run Locally

* Set Up MongoDB:

  * Ensure you have a local MongoDB instance running.
  * Alternatively, you can spin up a MongoDB instance using Docker. Simply execute the following command to launch the MongoDB container from mongo_local.yaml:

```bash
  docker-compose -f mongo_local.yaml up -d
```
* Start the Frontend (React App):
  * Navigate to the kitchensinkfrontend/app directory.
  * Ensure you have Node.js runtime installed on your machine.
  * Start the React development server by running
  * by default react app will run at Localhost:3000

```bash
npm run start
```

* Start the Backend (Java Service):

  * Open a new terminal and navigate to the kitchensink folder.
  * Start the backend service using the Gradle wrapper:
  * the background server will start on localhost:8080

```bash
   ./gradlew run
```
you can start using the app
