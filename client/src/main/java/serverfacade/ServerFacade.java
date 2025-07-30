package serverfacade;

import com.google.gson.Gson;
import request.*;
import response.*;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;
    private String authToken;

    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }

    public RegisterResponse register(RegisterRequest request) throws ResponseException {
        String path = "/user";
        RegisterResponse response =  this.makeRequest("POST", path, request, RegisterResponse.class);
        this.authToken = response.authToken();
        return response;
    }

    public LoginResponse login(LoginRequest request) throws ResponseException {
        String path = "/session";
        LoginResponse response =  this.makeRequest("POST", path, request, LoginResponse.class);
        this.authToken = response.authToken();
        return response;
    }

    public void logout(LogoutRequest request) throws ResponseException {
        String path = "/session";
        // I think there might be something wrong with this, I'm ot exactly sure what though
        this.makeRequest("DELETE", path, null, null);
    }

    public JoinResponse join(JoinRequest request) throws ResponseException {
        String path = "/game";
        return this.makeRequest("PUT", path, request, JoinResponse.class);
    }

    public CreateResponse createGame(CreateRequest request) throws ResponseException {
        String path = "/game";
        String method = "POST";
        return this.makeRequest(method, path, request, CreateResponse.class);
    }

    public ListResponse listGames(ListRequest request) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListResponse.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException{
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            if(authToken != null && !this.authToken.isBlank()){
                http.addRequestProperty("authorization", authToken);
            }
            if (!method.equalsIgnoreCase("GET") && request != null) {
                http.setDoOutput(true);
                writeBody(request, http);
            } else {
                http.setDoOutput(false);
            }
            
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws ResponseException, IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw new IOException("IOException happened for some reason " + status);
                }
            }
            throw new ResponseException("Error: something went wrong..." + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status >= 200 && status < 300;
    }
}
