package game.dylandevalia.royal_game_of_ur.utility;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Static class used to organise the console log
 */
public class Log {
	private static final int LEVEL_ALL = 0;
	private static final int LEVEL_TRACE = 1;
	private static final int LEVEL_DEBUG = 2;
	private static final int LEVEL_INFO = 3;
	private static final int LEVEL_WARN = 4;
	private static final int LEVEL_ERROR = 5;
	private static final int LEVEL_NONE = 6;
	
	private static int level = LEVEL_ALL;
	
	public static void SET_ALL() {
		level = LEVEL_ALL;
	}
	
	public static void SET_TRACE() {
		level = LEVEL_TRACE;
	}
	
	public static void SET_DEBUG() {
		level = LEVEL_DEBUG;
	}
	
	public static void SET_INFO() {
		level = LEVEL_INFO;
	}
	
	public static void SET_WARN() {
		level = LEVEL_WARN;
	}
	
	public static void SET_ERROR() {
		level = LEVEL_ERROR;
	}
	
	public static void SET_NONE() {
		level = LEVEL_NONE;
	}
	
	/**
	 * Logs a message to file and console
	 *
	 * @param level    The log level (eg. LEVEL_TRACE or LEVEL_INFO)
	 * @param category Category of the log
	 * @param message  The message to log
	 * @param ex       Throwable exception
	 */
	private static void log(int level, String category, String message, Throwable ex) {
		// Builds final message
		Date date = new Date();
		StringBuilder builder = new StringBuilder(255);
		builder.append(new SimpleDateFormat("HH:mm:ss").format(date));
		builder.append(' ');
		switch (level) {
			case 5:
				builder.append("\u001B[31m" + "ERROR:");
				break;
			case 4:
				builder.append("\u001B[33m" + " WARN:");
				break;
			case 3:
				builder.append("\u001B[37m" + " INFO:");
				break;
			case 2:
				builder.append("\u001B[34m" + "DEBUG:");
				break;
			case 1:
				builder.append("\u001B[35m" + "TRACE:");
				break;
			default:
				builder.append(" NONE:");
				break;
		}
		builder.append(" [");
		builder.append(category.toUpperCase());
		builder.append("] ");
		builder.append(message);
		if (ex != null) {
			builder.append("\u001B[31m");
			StringWriter writer = new StringWriter(255);
			ex.printStackTrace(new PrintWriter(writer));
			builder.append('\n');
			builder.append(writer.toString().trim());
		}
		builder.append("\u001B[0m");
		builder.append('\n');
		
		// Print string if high enough level
		if (level >= Log.level) {
			System.out.print(builder);
		}
	}
	
	public static void log(int level, String category, String message) {
		log(level, category, message, null);
	}
	
	public static void trace(String category, String message, Throwable ex) {
		log(LEVEL_TRACE, category, message, ex);
	}
	
	public static void trace(String category, String message) {
		trace(category, message, null);
	}
	
	public static void debug(String category, String message, Throwable ex) {
		log(LEVEL_DEBUG, category, message, ex);
	}
	
	public static void debug(String category, String message) {
		debug(category, message, null);
	}
	
	public static void info(String category, String message, Throwable ex) {
		log(LEVEL_INFO, category, message, ex);
	}
	
	public static void info(String category, String message) {
		info(category, message, null);
	}
	
	public static void warn(String category, String message, Throwable ex) {
		log(LEVEL_WARN, category, message, ex);
	}
	
	public static void warn(String category, String message) {
		warn(category, message, null);
	}
	
	public static void error(String category, String message, Throwable ex) {
		log(LEVEL_ERROR, category, message, ex);
	}
	
	public static void error(String category, String message) {
		error(category, message, null);
	}
}
