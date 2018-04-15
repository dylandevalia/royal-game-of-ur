package game.dylandevalia.royal_game_of_ur.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Small class to allow data to be sent between {@link game.dylandevalia.royal_game_of_ur.states.IState}s
 */
public class Bundle {
	
	/** Map used to store the objects being passed */
	private Map<String, Object> map = new HashMap<>();
	
	/**
	 * Puts data into the bundle with a name. This name will need to be used to retrieve the data
	 * back in {@link #get(String)}
	 *
	 * @param name The name of the data (case-sensitive)
	 * @param data The data to be stored
	 * @return Returns this object to allow builder styled construction
	 */
	public Bundle put(String name, Object data) {
		map.put(name, data);
		return this;
	}
	
	/**
	 * Retrieves the data from the bundle using the key string used in {@link #put(String, Object)}
	 *
	 * @param name The key string used in {@link #put(String, Object)}
	 * @return The data corresponding with the given key string. {@code null} if not found
	 */
	public Object get(String name) {
		return map.get(name);
	}
}
