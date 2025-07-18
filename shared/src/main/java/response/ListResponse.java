package response;

import model.dataaccess.GameData;

import java.util.Collection;
import java.util.HashMap;

public record ListResponse(Collection<GameData> games) {
}
