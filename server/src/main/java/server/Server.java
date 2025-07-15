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
    // these methods operate as the handlers:

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

    private Object deleteEverything(Request request, Response response){
        response.type("application/json");
        service.Service service = new service.Service();
        try {
            service.clearEverything();
        } catch (DataAccessException e) {
            response.status(500);
            return String.format("\"message\": \"Error: %s", e.getMessage());
        }
        response.status(200);
        return "";
    }


}
