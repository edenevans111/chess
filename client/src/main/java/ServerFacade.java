import com.google.gson.Gson;
import dataaccess.DataAccessException;
import request.*;
import response.*;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url){
        serverUrl = url;
    }

    public RegisterResponse register(RegisterRequest request) throws DataAccessException {
        String path = "/user";
        return this.makeRequest("POST", path, request, RegisterResponse.class);
    }

    public LoginResponse login(LoginRequest request) throws DataAccessException {
        String path = "/session";
        return this.makeRequest("POST", path, request, LoginResponse.class);
    }

    public void logout(LogoutRequest request) throws DataAccessException {
        String path = "/session";
        // I think there might be something wrong with this, I'm ot exactly sure what though
        this.makeRequest("DELETE", path, request, null);
    }

    public JoinResponse join(JoinRequest request) throws DataAccessException {
        String path = "/game";
    }

    public CreateResponse createGame(CreateRequest request) throws DataAccessException {
        String path = "/game";

    }

    public ListResponse listGames(ListRequest request) throws DataAccessException {
        var path = "/game";
        // this is going to have to be fixed somehow later...
        // I have no idea what I am doing
        return this.makeRequest("GET", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (DataAccessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws DataAccessException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws DataAccessException, IOException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw DataAccessException.fromJson(respErr);
                }
            }

            throw new DataAccessException(status, "other failure: " + status);
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
        return status == 200;
    }
}
