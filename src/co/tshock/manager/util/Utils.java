package co.tshock.manager.util;

public class Utils {
	/**
	 * Parses the string argument as a signed decimal integer. The characters in
	 * the string must all be decimal digits, except that the first character
	 * may be an ASCII minus sign {@code '-'} (<code>'&#92;u002D'</code>) to
	 * indicate a negative value. The resulting integer value is returned,
	 * exactly as if the argument and the radix 10 were given as arguments to
	 * the {@link java.lang.Integer#parseInt(java.lang.String, int)} method. If
	 * the integer cannot be parsed, the defaultValue is returned.
	 * 
	 * @param s
	 *            a {@code String} containing the {@code int} representation to
	 *            be parsed
	 * @param defaultValue
	 *            the return value when the string does not contain a parsable
	 *            integer
	 * @return the integer value represented by the argument in decimal.
	 */
	public static int parseInt(String s, int defaultValue) {
		int value = defaultValue;
		try {
			value = Integer.parseInt(s);
		} catch (NumberFormatException e) {

		}
		return value;
	}
}
