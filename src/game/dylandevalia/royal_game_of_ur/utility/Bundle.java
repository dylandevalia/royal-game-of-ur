package game.dylandevalia.royal_game_of_ur.utility;

import java.util.HashMap;
import java.util.Map;

public class Bundle {
	
	private Map<String, Object> map = new HashMap<>();
	
	public Bundle put(String name, Object data) {
		map.put(name, data);
		return this;
	}
	
	public Object get(String name) {
		return map.get(name);
	}
}
