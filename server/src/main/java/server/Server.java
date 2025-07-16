package server;

import dataaccess.DataAccessException;
import handler.ClearHandler;
import spark.*;

public class Server {

    private final ClearHandler clearHandler;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::deleteEverything);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    // these methods operate as the handlers:
    private Object deleteEverything(Request request, Response response){
        response.type("application/json");
        service.Service service = new service.Service();
        try {
            String result = clearHandler.clearEverything();
            service.clearEverything();
        } catch (DataAccessException e) {
            response.status(500);
            return String.format("{\"message\": \"Error: %s\"}", e.getMessage());
        }
        response.status(200);
        return "Clear Successful";
    }

    private Object registerUser(Request request, Response response){
        System.out.println("This is the registerUser method");

        return "";
    }

    private Object login(Request request, Response response){
        System.out.println("This is the login method");

        return "";
    }

    private Object logout(Request request, Response response){
        System.out.println("This is the logout method");

        return "";
    }

    private Object listGames(Request request, Response response){
        System.out.println("This is the listGames method");

        return "";
    }

    private Object createGame(Request request, Response response){
        System.out.println("This is the createGame method");

        return "";
    }

    private Object joinGame(Request request, Response response){
        System.out.println("This is the joinGame method");

        return "";
    }
}
