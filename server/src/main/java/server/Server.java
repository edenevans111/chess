package server;

import dataaccess.DataAccessException;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.delete("/db", this::deleteEverything);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    // I will need to change all of these to not be void-it's making everything in the run method red...

    private void registerUser(Request request, Response response){
        System.out.println("This is the registerUser method");
    }

    private void login(Request request, Response response){
        System.out.println("This is the login method");
    }

    private void logout(Request request, Response response){
        System.out.println("This is the logout method");
    }

    private void listGames(Request request, Response response){
        System.out.println("This is the listGames method");
    }

    private void createGame(Request request, Response response){
        System.out.println("This is the createGame method");
    }

    private Object deleteEverything(Request request, Response response){
        System.out.println("This is the deleteEverything method");
        response.type("application/json");
        service.Service service = new service.Service();
        try {
            service.clearEverything();
        } catch (DataAccessException e) {
            response.status(500);
            return "Error"; // this is the error response body
        }
        response.status(200);
        return "";
    }


}
