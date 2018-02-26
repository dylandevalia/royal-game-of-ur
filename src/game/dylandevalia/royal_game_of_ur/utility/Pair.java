package game.dylandevalia.royal_game_of_ur.utility;

/**
 * Utility class which stores a key-value pair
 *
 * @param <K> The object used for the key
 * @param <V> The object used for the value
 */
public class Pair<K, V> {
	
	/** The key of the pair */
	private K key;
	/** The value of the pair */
	private V value;
	
	/**
	 * Creates a key-value pair
	 *
	 * @param key   The key of the pair
	 * @param value The value of the pair
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * Creates an pair with an empty {@link #value}
	 *
	 * @param key The key of the pair
	 */
	public Pair(K key) {
		this(key, null);
	}
	
	public K getKey() {
		return key;
	}
	
	public void setKey(K key) {
		this.key = key;
	}
	
	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
}
