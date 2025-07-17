package response;

import model.dataaccess.GameData;

import java.util.HashSet;

public record ListResponse(HashSet<GameData> games) {
}
