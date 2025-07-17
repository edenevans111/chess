package server;

import com.google.gson.Gson;
import dataaccess.*;
import request.*;
import response.*;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;


    public Server() throws DataAccessException{
        this.userDAO = new MemoryUserDAO();
        this.authDAO = new MemoryAuthDAO();
        this.gameDAO = new MemoryGameDAO();
    }

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
        service.Service service = new service.Service(userDAO, authDAO, gameDAO);
        try {
            service.clearEverything();
        } catch (DataAccessException e) {
            response.status(500);
            return String.format("{\"message\": \"Error: %s\"}", e.getMessage());
        }
        response.status(200);
        return "{}";
    }

    private Object registerUser(Request request, Response response) throws DataAccessException{
        response.type("application/json");
        // I need to figure out how to fix this...
        service.UserService service = new UserService(userDAO, authDAO, gameDAO);
        var serializer = new Gson();
        try{
            RegisterRequest registerRequest = serializer.fromJson(request.body(), RegisterRequest.class);
            RegisterResponse registerResponse = service.register(registerRequest);
            response.status(200);
            return serializer.toJson(registerResponse);
        } catch(DataAccessException e){
            String msg = e.getMessage();
            if(msg.contains("Error: already taken")){
                response.status(403);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            }
            else if(msg.contains("Error: bad request")){
                response.status(400);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            }
            else{
                response.status(500);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            }
        }
    }

    private Object login(Request request, Response response) throws DataAccessException{
        response.type("application/json");
        service.UserService service = new UserService(userDAO, authDAO, gameDAO);
        Gson serializer = new Gson();
        try{
            LoginRequest loginRequest = serializer.fromJson(request.body(), LoginRequest.class);
            LoginResponse loginResponse = service.login(loginRequest);
            response.status(200);
            return serializer.toJson(loginResponse);
        } catch (DataAccessException e){
            String msg = e.getMessage() != null ? e.getMessage() : "";
            if(msg.contains("Error: bad request")){
                response.status(400);
            } else if(msg.contains("Error: unauthorized")){
                response.status(401);
            }
            else{
                response.status(500);
            }
            return String.format("{\"message\": \"Error: %s\"}", msg);
        }
    }

    private Object logout(Request request, Response response) throws DataAccessException{
        response.type("application/json");
        service.UserService service = new UserService(userDAO, authDAO, gameDAO);
        Gson serializer = new Gson();
        try{
            LogoutRequest logoutRequest = serializer.fromJson(request.body(), LogoutRequest.class);
            String authToken = request.headers("authorization");
            service.logout(logoutRequest, authToken);
            response.status(200);
            return "{}";
        } catch (DataAccessException e) {
            String msg = e.getMessage();
            if (msg.contains("Error: bad request")) {
                response.status(400);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            } else if (msg.contains("Error: unauthorized")) {
                response.status(401);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            } else {
                response.status(500);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            }
        }
    }

    private Object listGames(Request request, Response response) throws DataAccessException{
        response.type("application/json");
        service.GameService service = new GameService(userDAO, authDAO, gameDAO);
        Gson serializer = new Gson();
        try{
            ListRequest listrequest = serializer.fromJson(request.body(), ListRequest.class);
            String authToken = request.headers("authorization");
            ListResponse listResponse = service.listOfGames(listrequest, authToken);
            response.status(200);
            return serializer.toJson(listResponse);
        } catch (DataAccessException e){
            String msg = e.getMessage();
            if (msg.contains("Error: unauthorized")) {
                response.status(401);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            } else {
                response.status(500);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            }
        }
    }

    private Object createGame(Request request, Response response) throws DataAccessException{
        response.type("application/json");
        service.GameService service = new GameService(userDAO, authDAO, gameDAO);
        Gson serializer = new Gson();
        try{
            CreateRequest createRequest = serializer.fromJson(request.body(), CreateRequest.class);
            String authToken = request.headers("authorization");
            CreateResponse createResponse = service.createGame(createRequest, authToken);
            response.status(200);
            return serializer.toJson(createResponse);
        } catch (DataAccessException e){
            String msg = e.getMessage();
            if (msg.contains("Error: bad request")) {
                response.status(400);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            } else if (msg.contains("Error: unauthorized")) {
                response.status(401);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            } else {
                response.status(500);
                return String.format("{\"message\": \"Error: %s\"}", msg);
            }
        }
    }

    private Object joinGame(Request request, Response response) throws DataAccessException{
        response.type("application/json");
        service.GameService service = new GameService(userDAO, authDAO, gameDAO);
        Gson serializer = new Gson();
        try{
            JoinRequest joinRequest = serializer.fromJson(request.body(), JoinRequest.class);
            String authToken = request.headers("authorization");
            JoinResponse joinResponse = service.joinGame(joinRequest, authToken);
            response.status(200);
            return serializer.toJson(joinResponse);
        } catch (DataAccessException e){
            String msg = e.getMessage();
            if (msg.contains("Error: bad request")) {
                response.status(400);
            } else if (msg.contains("Error: unauthorized")) {
                response.status(401);
            } else if (msg.contains("Error: already taken")) {
                response.status(403);
            }
            else {
                response.status(500);
            }
            return String.format("{\"message\": \"Error: %s\"}", msg);
        }
    }
}
