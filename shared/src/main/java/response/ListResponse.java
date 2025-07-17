package response;

import model.dataaccess.GameData;

import java.util.HashMap;

public record ListResponse(HashMap<Integer, GameData> games) {
}
