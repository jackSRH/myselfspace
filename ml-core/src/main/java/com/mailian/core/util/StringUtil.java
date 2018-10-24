package com.mailian.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Miscellaneous string utility methods. Mainly for internal use within the framework; consider Jakarta's Commons Lang
 * for a more comprehensive suite of string utilities.
 * 
 * <p>
 * This class delivers some simple functionality that should really be provided by the core Java String and StringBuffer
 * classes, such as the ability to replace all occurrences of a given substring in a target string. It also provides
 * easy-to-use methods to convert between delimited strings, such as CSV strings, and collections and arrays.
 * 
 * <p>
 * 该程序是基于Spring2.0的工具类org.springframework.util.StringUtils的，大部分的功能都是
 * Spring中的实现。但是原来的方法中的替换(replace)，分割(split)等方法都是没有基于正则表达式， 使得他的功能很弱。所以我对此类方法进行了增强，并且保留原来的方法，如果想使用增强的方法，一般的
 * 命名方式是在原用方法名后面加上ByPattern，例如replaceByPattern,splitByPattern。
 * 
 * <p>
 * 并且在该类中增加了更多的方法，其中有中文注释的方法则为Tom Koo添加。
 * 
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 * @author Rob Harrop
 * @author Tom Koo
 * @since 16 April 2001
 */
@SuppressWarnings("unchecked")
public class StringUtil {

	public static final String PASSWOARD = "[[a-z]|[A-Z]|[0-9]]*";

	public static final String USERNAME = "[[a-z]|[A-Z]|[0-9]]*";

	public static final String NUMBER = "[0-9]*";

	public static final String LETTER = "[[A-Z]|[a-z]]*";

	public static final String PHONE_NO = "0[0-9]{2,3}-[0-9]{7,8}";

	public static final String MOBILE_PHONE_NO = "13[0-9][0-9]{8}";

	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	// ---------------------------------------------------------------------
	// General convenience methods for working with Strings
	// ---------------------------------------------------------------------

	/**
	 * Check that the given String is neither <code>null</code> nor of length 0. Note: Will return <code>true</code> for
	 * a String that purely consists of whitespace.
	 * <p>
	 * 
	 * <pre>
	 *                                                                                                StringUtils.hasLength(null) = false
	 *                                                                                                StringUtils.hasLength(&quot;&quot;) = false
	 *                                                                                                StringUtils.hasLength(&quot; &quot;) = true
	 *                                                                                                StringUtils.hasLength(&quot;Hello&quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(String str) {
		return str != null && str.length() > 0;
	}

	/**
	 * Check whether the given String has actual text. More specifically, returns <code>true</code> if the string not
	 * <code>null<code>,
	 * its length is greater than 0, and it contains at least one non-whitespace character.
	 * <p><pre>
	 *                                                                                                StringUtils.hasText(null) = false
	 *                                                                                                StringUtils.hasText(&quot;&quot;) = false
	 *                                                                                                StringUtils.hasText(&quot; &quot;) = false
	 *                                                                                                StringUtils.hasText(&quot;12345&quot;) = true
	 *                                                                                                StringUtils.hasText(&quot; 12345 &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not <code>null</code>, its length is greater than 0, and is does not
	 *         contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(String str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check whether the given String contains any whitespace characters.
	 *
	 * @param str
	 *            the String to check (may be <code>null</code>)
	 * @return <code>true</code> if the String is not empty and contains at least 1 whitespace character
	 * @see Character#isWhitespace
	 */
	public static boolean containsWhitespace(String str) {
		if (!hasLength(str)) {
			return false;
		}
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Trim leading and trailing whitespace from the given String.
	 *
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim leading whitespace from the given String.
	 *
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimLeadingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(0))) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Trim trailing whitespace from the given String.
	 *
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimTrailingWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1))) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * Trim <i>all</i> whitespace from the given String: leading, trailing, and inbetween characters.
	 *
	 * @param str
	 *            the String to check
	 * @return the trimmed String
	 * @see Character#isWhitespace
	 */
	public static String trimAllWhitespace(String str) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		int index = 0;
		while (buf.length() > index) {
			if (Character.isWhitespace(buf.charAt(index))) {
				buf.deleteCharAt(index);
			} else {
				index++;
			}
		}
		return buf.toString();
	}

	/**
	 * Test if the given String starts with the specified prefix, ignoring upper/lower case.
	 *
	 * @param str
	 *            the String to check
	 * @param prefix
	 *            the prefix to look for
	 * @see String#startsWith
	 */
	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null) {
			return false;
		}
		if (str.startsWith(prefix)) {
			return true;
		}
		if (str.length() < prefix.length()) {
			return false;
		}
		String lcStr = str.substring(0, prefix.length()).toLowerCase();
		String lcPrefix = prefix.toLowerCase();
		return lcStr.equals(lcPrefix);
	}

	/**
	 * Test if the given String ends with the specified suffix, ignoring upper/lower case.
	 *
	 * @param str
	 *            the String to check
	 * @param suffix
	 *            the suffix to look for
	 * @see String#endsWith
	 */
	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null) {
			return false;
		}
		if (str.endsWith(suffix)) {
			return true;
		}
		if (str.length() < suffix.length()) {
			return false;
		}

		String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
		String lcSuffix = suffix.toLowerCase();
		return lcStr.equals(lcSuffix);
	}

	/**
	 * Count the occurrences of the substring in string s.
	 *
	 * @param str
	 *            string to search in. Return 0 if this is null.
	 * @param sub
	 *            string to search for. Return 0 if this is null.
	 */
	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0) {
			return 0;
		}
		int count = 0, pos = 0, idx = 0;
		while ((idx = str.indexOf(sub, pos)) != -1) {
			++count;
			pos = idx + sub.length();
		}
		return count;
	}

	/**
	 * Replace all occurences of a substring within a string with another string.
	 *
	 * @param inString
	 *            String to examine
	 * @param oldPattern
	 *            String to replace
	 * @param newPattern
	 *            String to insert
	 * @return a String with the replacements
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (inString == null) {
			return null;
		}
		if (oldPattern == null || newPattern == null) {
			return inString;
		}

		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		// remember to append any characters to the right of a match
		return sbuf.toString();
	}

	/**
	 * Delete all occurrences of the given substring.
	 *
	 * @param pattern
	 *            the pattern to delete all occurrences of
	 */
	public static String delete(String inString, String pattern) {
		return replace(inString, pattern, "");
	}

	/**
	 * Delete any character in a given string.
	 *
	 * @param charsToDelete
	 *            a set of characters to delete. E.g. "az\n" will delete 'a's, 'z's and new lines.
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (inString == null || charsToDelete == null) {
			return inString;
		}
		StringBuffer out = new StringBuffer();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				out.append(c);
			}
		}
		return out.toString();
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with formatted Strings
	// ---------------------------------------------------------------------

	/**
	 * Quote the given String with single quotes.
	 *
	 * @param str
	 *            the input String (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or <code>null<code> if the input was <code>null</code>
	 */
	public static String quote(String str) {
		return str != null ? "'" + str + "'" : null;
	}

	/**
	 * Turn the given Object into a String with single quotes if it is a String; keeping the Object as-is else.
	 *
	 * @param obj
	 *            the input Object (e.g. "myString")
	 * @return the quoted String (e.g. "'myString'"), or the input object as-is if not a String
	 */
	public static Object quoteIfString(Object obj) {
		return obj instanceof String ? quote((String) obj) : obj;
	}

	/**
	 * Unqualify a string qualified by a '.' dot character. For example, "this.name.is.qualified", returns "qualified".
	 *
	 * @param qualifiedName
	 *            the qualified name
	 */
	public static String unqualify(String qualifiedName) {
		return unqualify(qualifiedName, '.');
	}

	/**
	 * Unqualify a string qualified by a separator character. For example, "this:name:is:qualified" returns "qualified"
	 * if using a ':' separator.
	 *
	 * @param qualifiedName
	 *            the qualified name
	 * @param separator
	 *            the separator
	 */
	public static String unqualify(String qualifiedName, char separator) {
		return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
	}

	/**
	 * Capitalize a <code>String</code>, changing the first letter to upper case as per
	 * {@link Character#toUpperCase(char)}. No other letters are changed.
	 *
	 * @param str
	 *            the String to capitalize, may be <code>null</code>
	 * @return the capitalized String, <code>null</code> if null
	 */
	public static String capitalize(String str) {
		return changeFirstCharacterCase(str, true);
	}

	/**
	 * Uncapitalize a <code>String</code>, changing the first letter to lower case as per
	 * {@link Character#toLowerCase(char)}. No other letters are changed.
	 *
	 * @param str
	 *            the String to uncapitalize, may be <code>null</code>
	 * @return the uncapitalized String, <code>null</code> if null
	 */
	public static String uncapitalize(String str) {
		return changeFirstCharacterCase(str, false);
	}

	private static String changeFirstCharacterCase(String str, boolean capitalize) {
		if (str == null || str.length() == 0) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str.length());
		if (capitalize) {
			buf.append(Character.toUpperCase(str.charAt(0)));
		} else {
			buf.append(Character.toLowerCase(str.charAt(0)));
		}
		buf.append(str.substring(1));
		return buf.toString();
	}

	/**
	 * Extract the filename from the given path, e.g. "mypath/myfile.txt" -> "myfile.txt".
	 *
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the extracted filename, or <code>null</code> if none
	 */
	public static String getFilename(String path) {
		if (path == null) {
			return null;
		}
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
	}

	/**
	 * Extract the filename extension from the given path, e.g. "mypath/myfile.txt" -> "txt".
	 *
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the extracted filename extension, or <code>null</code> if none
	 */
	public static String getFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return sepIndex != -1 ? path.substring(sepIndex + 1) : null;
	}

	/**
	 * Strip the filename extension from the given path, e.g. "mypath/myfile.txt" -> "mypath/myfile".
	 *
	 * @param path
	 *            the file path (may be <code>null</code>)
	 * @return the path with stripped filename extension, or <code>null</code> if none
	 */
	public static String stripFilenameExtension(String path) {
		if (path == null) {
			return null;
		}
		int sepIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		return sepIndex != -1 ? path.substring(0, sepIndex) : path;
	}

	/**
	 * Apply the given relative path to the given path, assuming standard Java folder separation (i.e. "/" separators);
	 *
	 * @param path
	 *            the path to start from (usually a full file path)
	 * @param relativePath
	 *            the relative path to apply (relative to the full file path above)
	 * @return the full file path that results from applying the relative path
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner simple dots.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that Windows separators ("\") are replaced
	 * by simple slashes.
	 *
	 * @param path
	 *            the original path
	 * @return the normalized path
	 */

	public static String cleanPath(String path) {
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			pathToUse = pathToUse.substring(prefixIndex + 1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List pathElements = new LinkedList();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			if (CURRENT_PATH.equals(pathArray[i])) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(pathArray[i])) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with corresponding to top path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, pathArray[i]);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * Compare two paths after normalization of them.
	 *
	 * @param path1
	 *            First path for comparizon
	 * @param path2
	 *            Second path for comparizon
	 * @return whether the two paths are equivalent after normalization
	 */
	public static boolean pathEquals(String path1, String path2) {
		return cleanPath(path1).equals(cleanPath(path2));
	}

	/**
	 * Parse the given locale string into a <code>java.util.Locale</code>. This is the inverse operation of Locale's
	 * <code>toString</code>.
	 *
	 * @param localeString
	 *            the locale string, following <code>java.util.Locale</code>'s toString format ("en", "en_UK", etc).
	 *            Also accepts spaces as separators, as alternative to underscores.
	 * @return a corresponding Locale instance
	 */
	public static Locale parseLocaleString(String localeString) {
		String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
		String language = parts.length > 0 ? parts[0] : "";
		String country = parts.length > 1 ? parts[1] : "";
		String variant = parts.length > 2 ? parts[2] : "";
		return language.length() > 0 ? new Locale(language, country, variant) : null;
	}

	// ---------------------------------------------------------------------
	// Convenience methods for working with String arrays
	// ---------------------------------------------------------------------

	/**
	 * Append the given String to the given String array, returning a new array consisting of the input array contents
	 * plus the given String.
	 *
	 * @param array
	 *            the array to append to (can be <code>null</code>)
	 * @param str
	 *            the String to append
	 * @return the new array (never <code>null</code>)
	 */
	public static String[] addStringToArray(String[] array, String str) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[] { str };
		}
		String[] newArr = new String[array.length + 1];
		System.arraycopy(array, 0, newArr, 0, array.length);
		newArr[array.length] = str;
		return newArr;
	}

	/**
	 * Concatenate the given String arrays into one, with overlapping array elements included twice.
	 * <p>
	 * The order of elements in the original arrays is preserved.
	 *
	 * @param array1
	 *            the first array (can be <code>null</code>)
	 * @param array2
	 *            the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
	 */
	public static String[] concatenateStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		String[] newArr = new String[array1.length + array2.length];
		System.arraycopy(array1, 0, newArr, 0, array1.length);
		System.arraycopy(array2, 0, newArr, array1.length, array2.length);
		return newArr;
	}

	/**
	 * Merge the given String arrays into one, with overlapping array elements only included once.
	 * <p>
	 * The order of elements in the original arrays is preserved (with the exception of overlapping elements, which are
	 * only included on their first occurence).
	 *
	 * @param array1
	 *            the first array (can be <code>null</code>)
	 * @param array2
	 *            the second array (can be <code>null</code>)
	 * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
	 */
	public static String[] mergeStringArrays(String[] array1, String[] array2) {
		if (ObjectUtils.isEmpty(array1)) {
			return array2;
		}
		if (ObjectUtils.isEmpty(array2)) {
			return array1;
		}
		List result = new ArrayList();
		result.addAll(Arrays.asList(array1));
		for (String element : array2) {
			String str = element;
			if (!result.contains(str)) {
				result.add(str);
			}
		}
		return toStringArray(result);
	}

	/**
	 * Turn given source String array into sorted array.
	 *
	 * @param array
	 *            the source array
	 * @return the sorted array (never <code>null</code>)
	 */
	public static String[] sortStringArray(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return new String[0];
		}
		Arrays.sort(array);
		return array;
	}

	/**
	 * Copy the given Collection into a String array. The Collection must contain String elements only.
	 *
	 * @param collection
	 *            the Collection to copy
	 * @return the String array (<code>null</code> if the Collection was <code>null</code> as well)
	 */
	public static String[] toStringArray(Collection collection) {
		if (collection == null) {
			return null;
		}
		return (String[]) collection.toArray(new String[collection.size()]);
	}

	/**
	 * Remove duplicate Strings from the given array. Also sorts the array, as it uses a TreeSet.
	 *
	 * @param array
	 *            the String array
	 * @return an array without duplicates, in natural sort order
	 */
	public static String[] removeDuplicateStrings(String[] array) {
		if (ObjectUtils.isEmpty(array)) {
			return array;
		}
		Set set = new TreeSet();
		for (String element : array) {
			set.add(element);
		}
		return toStringArray(set);
	}

	/**
	 * Split a String at the first occurrence of the delimiter. Does not include the delimiter in the result.
	 *
	 * @param toSplit
	 *            the string to split
	 * @param delimiter
	 *            to split the string up with
	 * @return a two element array with index 0 being before the delimiter, and index 1 being after the delimiter
	 *         (neither element includes the delimiter); or <code>null</code> if the delimiter wasn't found in the given
	 *         input String
	 */
	public static String[] splitFirst(String toSplit, String delimiter) {
		if (!hasLength(toSplit) || !hasLength(delimiter)) {
			return null;
		}
		int offset = toSplit.indexOf(delimiter);
		if (offset < 0) {
			return null;
		}
		String beforeDelimiter = toSplit.substring(0, offset);
		String afterDelimiter = toSplit.substring(offset + delimiter.length());
		return new String[] { beforeDelimiter, afterDelimiter };
	}

	/**
	 * Take an array Strings and split each element based on the given delimiter. A <code>Properties</code> instance is
	 * then generated, with the left of the delimiter providing the key, and the right of the delimiter providing the
	 * value.
	 * <p>
	 * Will trim both the key and value before adding them to the <code>Properties</code> instance.
	 *
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @return a <code>Properties</code> instance representing the array contents, or <code>null</code> if the array to
	 *         process was null or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
		return splitArrayElementsIntoProperties(array, delimiter, null);
	}

	/**
	 * Take an array Strings and split each element based on the given delimiter. A <code>Properties</code> instance is
	 * then generated, with the left of the delimiter providing the key, and the right of the delimiter providing the
	 * value.
	 * <p>
	 * Will trim both the key and value before adding them to the <code>Properties</code> instance.
	 *
	 * @param array
	 *            the array to process
	 * @param delimiter
	 *            to split each element using (typically the equals symbol)
	 * @param charsToDelete
	 *            one or more characters to remove from each element prior to attempting the split operation (typically
	 *            the quotation mark symbol), or <code>null</code> if no removal should occur
	 * @return a <code>Properties</code> instance representing the array contents, or <code>null</code> if the array to
	 *         process was <code>null</code> or empty
	 */
	public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {

		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		Properties result = new Properties();
		for (String element2 : array) {
			String element = element2;
			if (charsToDelete != null) {
				element = deleteAny(element2, charsToDelete);
			}
			String[] splittedElement = splitFirst(element, delimiter);
			if (splittedElement == null) {
				continue;
			}
			result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
		}
		return result;
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer. Trims tokens and omits empty tokens.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those
	 * characters can be used to separate tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 *
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those characters is individually considered as
	 *            delimiter).
	 * @return an array of the tokens
	 * @see StringTokenizer
	 * @see String#trim
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given String into a String array via a StringTokenizer.
	 * <p>
	 * The given delimiters string is supposed to consist of any number of delimiter characters. Each of those
	 * characters can be used to separate tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using <code>delimitedListToStringArray</code>
	 *
	 * @param str
	 *            the String to tokenize
	 * @param delimiters
	 *            the delimiter characters, assembled as String (each of those characters is individually considered as
	 *            delimiter)
	 * @param trimTokens
	 *            trim the tokens via String's <code>trim</code>
	 * @param ignoreEmptyTokens
	 *            omit empty tokens from the result array (only applies to tokens that are empty after trimming;
	 *            StringTokenizer will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens
	 * @see StringTokenizer
	 * @see String#trim
	 * @see #delimitedListToStringArray
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens,
			boolean ignoreEmptyTokens) {

		StringTokenizer st = new StringTokenizer(str, delimiters);
		List tokens = new ArrayList();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}
		return toStringArray(tokens);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * <p>
	 * A single delimiter can consists of more than one character: It will still be considered as single delimiter
	 * string, rather than as bunch of potential delimiter characters - in contrast to
	 * <code>tokenizeToStringArray</code>.
	 *
	 * @param str
	 *            the input String
	 * @param delimiter
	 *            the delimiter between elements (this is a single delimiter, rather than a bunch individual delimiter
	 *            characters)
	 * @return an array of the tokens in the list
	 * @see #tokenizeToStringArray
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List result = new ArrayList();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(str.substring(i, i + 1));
			}
		} else {
			int pos = 0;
			int delPos = 0;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(str.substring(pos, delPos));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(str.substring(pos));
			}
		}
		return toStringArray(result);
	}

	/**
	 * Convert a CSV list into an array of Strings.
	 *
	 * @param str
	 *            CSV list
	 * @return an array of Strings, or the empty array if s is null
	 */
	public static String[] commaDelimitedListToStringArray(String str) {
		return delimitedListToStringArray(str, ",");
	}

	/**
	 * Convenience method to convert a CSV string list to a set. Note that this will suppress duplicates.
	 *
	 * @param str
	 *            CSV String
	 * @return a Set of String entries in the list
	 */
	public static Set commaDelimitedListToSet(String str) {
		Set set = new TreeSet();
		String[] tokens = commaDelimitedListToStringArray(str);
		for (String token : tokens) {
			set.add(token);
		}
		return set;
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 *
	 * @param coll
	 *            Collection to display
	 * @param delim
	 *            delimiter to use (probably a ",")
	 * @param prefix
	 *            string to start each element with
	 * @param suffix
	 *            string to end each element with
	 */
	public static String collectionToDelimitedString(Collection coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * Convenience method to return a Collection as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 *
	 * @param coll
	 *            Collection to display
	 * @param delim
	 *            delimiter to use (probably a ",")
	 */
	public static String collectionToDelimitedString(Collection coll, String delim) {
		return collectionToDelimitedString(coll, delim, "", "");
	}

	/**
	 * Convenience method to return a Collection as a CSV String. E.g. useful for <code>toString()</code>
	 * implementations.
	 *
	 * @param coll
	 *            Collection to display
	 */
	public static String collectionToCommaDelimitedString(Collection coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * Convenience method to return a String array as a delimited (e.g. CSV) String. E.g. useful for
	 * <code>toString()</code> implementations.
	 *
	 * @param arr
	 *            array to display. Elements may be of any type (toString will be called on each element).
	 * @param delim
	 *            delimiter to use (probably a ",")
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if (ObjectUtils.isEmpty(arr)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	/**
	 * Convenience method to return a String array as a CSV String. E.g. useful for <code>toString()</code>
	 * implementations.
	 *
	 * @param arr
	 *            array to display. Elements may be of any type (toString will be called on each element).
	 */
	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	// -------------------------------------------------------------------------------------------

	/**
	 * 判断str字符串是否符合我们定义的标准格式，pattern表示格式(正则表达式)。
	 *
	 * @param str
	 * @param pattern
	 */
	public static boolean isDefinedPattern(String str, String pattern) {
		Assert.notNull(str);
		Assert.notNull(pattern);

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 使用str2代替str1中的pattern字符串（pattern可以是正则表达式）
	 *
	 * @param str1
	 *            将被替换的源字符串
	 * @param pattern
	 *            指定的将被替换的字符串符合的模式（pattern）
	 * @param str2
	 *            用来替换掉符合模式字符串的字符串
	 */
	public static String replaceByPattern(String str1, String pattern, String str2) {
		Assert.notNull(str1);
		Assert.notNull(pattern);
		Assert.notNull(str2);

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str1);
		return m.replaceAll(str2);
	}

	/**
	 * 通过指定字符串模式来截取字符串，并且返回切断后的字符串数组
	 *
	 * <p>
	 * 该方法同StringUtils.split方法有一些区别，StringUtils.split是在分割字符第一次出现的地方把字符串分割成两段， 形成一个length为2的数组。而本方法是在字符串中任何出现符合切割模式
	 * 的地方进行切割，形成一个长度不定的字符串数组
	 *
	 * @param str
	 * @param pattern
	 */
	public static String[] splitByPattern(String str, String pattern) {
		Assert.notNull(pattern);

		if (StringUtil.hasLength(str)) {
			Pattern p = Pattern.compile(pattern);
			return p.split(str);
		} else {
			return null;
		}
	}

	/**
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNullOrBlank(String str) {
		if (str == null) {
			return true;
		} else if (str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 去掉字符串中 回车符 换行符 制表符
	 *
	 * @param str
	 * @return
	 */
	public static String parseToString(String str) {
		if (str == null) {
			return null;
		}
		return str.replaceAll("\r|\n|\t", " ");
	}

	public static String getText(String info, Object... arg) {

		if (arg != null && arg.length > 0) {
			for (int i = 0; i < arg.length; i++) {
				String val = arg[i] == null ? "" : arg[i].toString();
				// String replaceAll(regex, replacement)函数 ,
				// 由于第一个参数支持正则表达式，replacement中出现"$",会按照$1$2的分组
				// 模式进行匹配，当编译器发现"$"后跟的不是整数的时候，就会抛出"非法的组引用"的异常。
				// 所以我们在使用replaceAll(regex,replacement)函数的时候,
				// 要把String中的字符替换成"$AAA"的话，可以对replacement进行"$"的转义处理,filterDollarStr就是用来做转义处理的函数
				info = info.replaceAll("(\\{" + i + "\\})", filterDollarStr(val));
			}
		}
		return info;
	}

	/**
	 * String replaceAll(regex, replacement)函数 , 由于第一个参数支持正则表达式，replacement中出现"$",会按照$1$2的分组
	 * 模式进行匹配，当编译器发现"$"后跟的不是整数的时候，就会抛出"非法的组引用"的异常。 所以我们在使用replaceAll(regex,replacement)函数的时候,
	 * 要把String中的字符替换成"$AAA"的话，可以对replacement进行 "$"的转义处理,filterDollarStr就是用来做转义处理的函数
	 *
	 * @param str
	 * @return
	 */
	public static String filterDollarStr(String str) {
		StringBuffer sReturn = new StringBuffer("");
		if (str.indexOf('$', 0) > -1) {
			while (str.length() > 0) {
				if (str.indexOf('$', 0) > -1) {
					sReturn.append(str.subSequence(0, str.indexOf('$', 0)));
					sReturn.append("\\$");
					str = str.substring(str.indexOf('$', 0) + 1, str.length());
				} else {
					sReturn.append(str);
					str = "";
				}
			}
		} else {
			sReturn = new StringBuffer(str);
		}
		return sReturn.toString();
	}


	/**
	 * 分割字符串,查询的时候用ID的时候超过1000个会出问题的解决方法
	 *
	 * @param strs
	 * @return
	 */
	public static List<String> getSplitStr(String strs) {
		List<String> list = new ArrayList<String>();
		StringBuffer strTemp = new StringBuffer();
		if (strs == null) {
			return list;
		}
		String[] strGroup = strs.split(",");
		for (int i = 0; i < strGroup.length; i++) {
			if (i != 0 && i % 1000 == 0) {
				list.add(strTemp.toString().substring(0, strTemp.toString().length() - 1));
				strTemp = new StringBuffer();
				strTemp.append(strGroup[i]).append(",");
			} else {
				strTemp.append(strGroup[i]).append(",");
			}
		}
		list.add(strTemp.toString().substring(0, strTemp.toString().length() - 1));
		return list;
	}

	/**
	 * 用户String提供的split方法会过滤掉后面的空字符串，所以提供本方法，严格按照分隔符个数形成数组
	 *
	 * 例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}， 而用本函数则返回数组{"1","2","3","4","",""}
	 *
	 * @param input
	 *            本分割的字符串或者StringBuffer对象
	 * @param delimiterPattern
	 *            分割字符
	 * @param limit
	 *            参数控制模式应用的次数，因此影响结果数组的长度。如果该限制 n 大于 0，则模式将被最多应用 n - 1 次，数组的长度将不会大于 n，而且数组的最后项将包含超出最后匹配的定界符的所有输入。如果 n
	 *            为非正，则模式将被应用尽可能多的次数，而且数组可以是任意长度。如果 n 为零，则模式将被应用尽可能多的次数，数组可有任何长度，并且结尾空字符串将被丢弃。
	 * @return
	 */
	public static String[] split(CharSequence input, String delimiterPattern, int limit) {
		int index = 0;
		boolean matchLimited = limit > 0;
		ArrayList matchList = new ArrayList();
		Pattern p = Pattern.compile(delimiterPattern);
		Matcher m = p.matcher(input);

		// Add segments before each match found
		while (m.find()) {
			if (!matchLimited || matchList.size() < limit - 1) {
				String match = input.subSequence(index, m.start()).toString();
				matchList.add(match);
				index = m.end();
			} else if (matchList.size() == limit - 1) { // last one
				String match = input.subSequence(index, input.length()).toString();
				matchList.add(match);
				index = m.end();
			}
		}

		// If no match was found, return this
		if (index == 0) {
			return new String[] { input.toString() };
		}

		// Add remaining segment
		if (!matchLimited || matchList.size() < limit) {
			matchList.add(input.subSequence(index, input.length()).toString());
		}

		// Construct result
		int resultSize = matchList.size();
		// 去掉的代码是同jdk原生split方法的不同之处，例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}，而用本函数则返回数组{"1","2","3","4","",""}
		// if (limit == 0)
		// while (resultSize > 0 && matchList.get(resultSize - 1).equals(""))
		// resultSize--;
		String[] result = new String[resultSize];
		return (String[]) matchList.subList(0, resultSize).toArray(result);
	}

	/**
	 * 判断一个字符串中是否包含一个子串,不区分大小写
	 *
	 * @param str
	 *            String
	 * @param subString
	 *            String
	 * @return boolean
	 */
	public static boolean isIncludeSubString(String str, String subString) {
		boolean result = false;
		if (str == null || subString == null) {
			return false;
		}
		int strLength = str.length();
		int subStrLength = subString.length();
		String tmpStr = null;
		for (int i = 0; i < strLength; i++) {
			if (strLength - i < subStrLength) {
				return false;
			}
			tmpStr = str.substring(i, subStrLength + i);
			if (tmpStr.endsWith(subString)) {
				return true;
			}
		}
		return result;
	}

	/**
	 * /** 用户String提供的split方法会过滤掉后面的空字符串，所以提供本方法，严格按照分隔符个数形成数组
	 *
	 * 例如有一个字符串"1,2,3,4,,,"，使用原生string的split形成的字符串数组为{"1","2","3","4"}， 而用本函数则返回数组{"1","2","3","4","",""}
	 *
	 * @param input
	 *            本分割的字符串或者StringBuffer对象
	 * @param delimiterPattern
	 *            分割字符
	 * @return
	 */
	public static String[] split(CharSequence input, String delimiterPattern) {
		return split(input, delimiterPattern, 0);
	}

	/**
	 * 将字符串str 按照length长度为一个字符串平均分割。 如果length<=0，则返回null。 wuwm
	 *
	 * @param str
	 * @param length
	 * @return String[]
	 */
	public static String[] splitSameLegthArray(String str, int length) {
		if (length <= 0 || str == null) {
			return null;
		}
		String[] strArr = new String[str.length() / length + 1];

		char[] cStr = str.toCharArray();
		StringBuffer buf = new StringBuffer();
		int count = 0;
		int leg = 0;
		for (char s : cStr) {
			if (count != length) {
				buf.append(String.valueOf(s));
				count++;
			} else {
				strArr[leg++] = buf.toString();
				buf.delete(0, buf.length());
				buf.append(String.valueOf(s));
				count = 1;
			}
		}
		if (buf.length() != 0) {
			strArr[leg++] = buf.toString();
		}
		return strArr;
	}

	public static Properties toProperties(String str) {
		return toProperties(str, ",");
	}

	public static Properties toProperties(String str, String split) {
		Properties props = new Properties();
		if (!StringUtil.hasLength(str)) {
			return props;
		}
		String[] lines = str.split(split);
		for (String line : lines) {
			if (line == null) {
				return null;
			}
			line = StringUtil.trimLeadingWhitespace(line);
			if (line.length() > 0) {
				char firstChar = line.charAt(0);
				if (firstChar != '#' && firstChar != '!') {
					int separatorIndex = line.indexOf("=");
					if (separatorIndex == -1) {
						separatorIndex = line.indexOf(":");
					}
					String key = separatorIndex != -1 ? line.substring(0, separatorIndex) : line;
					String value = separatorIndex != -1 ? line.substring(separatorIndex + 1) : "";
					key = StringUtil.trimTrailingWhitespace(key);
					value = StringUtil.trimLeadingWhitespace(value);
					props.put(key, value);
				}
			}
		}
		return props;
	}

	// 金额名称样式
	static String[] partern = { "元", "拾", "百", "千", "万", "拾", "百", "千", "亿", "拾", "百", "千", "兆" };

	/**
	 * Constructor for StringUtils.
	 */
	public StringUtil() {
		super();
	}

	/**
	 * 替换字符串函数 String strSource - 源字符串 String strFrom - 要替换的子串 String strTo - 替换为的字符串
	 */
	public static String replaceStrs(String strSource, String strFrom, String strTo) {
		// 如果要替换的子串为空，则直接返回源串
		if (strFrom == null || strFrom.equals("")) {
			return strSource;
		}
		String strDest = "";
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest + strSource.substring(0, intPos);
			// 加上替换后的子串
			strDest = strDest + strTo;
			// 修改源串为匹配子串后的子串
			strSource = strSource.substring(intPos + intFromLen);
		}
		// 加上没有匹配的子串
		strDest = strDest + strSource;
		// 返回
		return strDest;
	}

	public static String filterNull(Object obj) {
		if (obj == null) {
			return "";
		}
		// if(String.valueOf(obj)=="null") return "";
		return String.valueOf(obj);
	}

	public static String replaceRN(String strSource) {
		char c1 = 10;
		char c2 = 13;
		String result = StringUtil.replace(strSource, String.valueOf(c2) + String.valueOf(c1), "&#13;&#10;");
		return result;
	}

	/**
	 * <p>
	 * The method search for the specified pattern and replaces the all occurances of the given pattern with the
	 * specifed arg String.
	 * </p>
	 *
	 * @param source
	 *            is the source string that we would like to apply the replacement on
	 * @param pattern
	 *            is the pattern in that string that we are looking to replace
	 * @param arg
	 *            is the string that we will replace the pattern segment with
	 * @return The substituted <code>String</code>.
	 */
	/*
	 * public static String replace( String source , String pattern , String arg ) { if ( source == null ) return null;
	 * String result = source; int startIndex = source.indexOf( pattern ); while ( startIndex != -1 ) { result =
	 * result.substring( 0 , startIndex ) + arg + result.substring( Math.min( startIndex + pattern.length() ,
	 * result.length() ) ); int offset = startIndex + arg.length(); if ( offset > result.length() ) return result;
	 * startIndex = result.indexOf( pattern , offset ); } return result; }
	 */
	/**
	 * Returns a string consisting of "s", plus enough copies of "pad_ch" on the left hand side to make the length of
	 * "s" equal to or greater than len (if "s" is already longer than "len", then "s" is returned).
	 */

	/**
	 * 如果字符串不符合规定长度，在字符串左边补充指定字符
	 *
	 * @param s 原字符串
	 * @param len 字符串应有长度
	 * @param pad_ch 指定补充字符
	 * @return
	 */
	public static String padLeft(String s, int len, char pad_ch) {
		if (s.length() >= len) {
			return s;
		} else {
			StringBuilder sb = new StringBuilder();
			int n = len - s.length();
			for (int i = 0; i < n; i++) {
				sb.append(pad_ch);
			}
			sb.append(s);
			return sb.toString();
		}
	}

	/**
	 * 如果字符串不符合规定长度，在字符串右边补充指定字符
	 *
	 * @param s 原字符串
	 * @param len 字符串应有长度
	 * @param pad_ch 指定补充字符
	 * @return
	 */
	public static String padRight(String s, int len, char pad_ch) {
		if (s.length() >= len) {
			return s;
		} else {
			StringBuilder sb = new StringBuilder();
			int n = len - s.length();
			sb.append(s);
			for (int i = 0; i < n; i++) {
				sb.append(pad_ch);
			}
			return sb.toString();
		}
	}

	/**
	 * Returns a string consisting of "s", with each of the first "len" characters replaced by "mask_ch" character.
	 */
	public static String maskLeft(String s, int len, char mask_ch) {
		if (len <= 0) {
			return s;
		}
		len = Math.min(len, s.length());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append(mask_ch);
		}
		sb.append(s.substring(len));
		return sb.toString();
	}

	/**
	 * Returns a string consisting of "s", with each of the last "len" characters replaces by "mask_ch" character.
	 */
	public static String maskRight(String s, int len, char mask_ch) {
		if (len <= 0) {
			return s;
		}
		len = Math.min(len, s.length());
		StringBuilder sb = new StringBuilder();
		sb.append(s.substring(0, s.length() - len));
		for (int i = 0; i < len; i++) {
			sb.append(mask_ch);
		}
		return sb.toString();
	}

	public static String switchMoneyChinese(BigDecimal bg) throws Exception {
		// 校验入口参数
		if (bg == null) {
			throw new Exception("输入金额不正确！");
		}

		// 转换为字符形态
		String amt = bg.toString();

		if ("0".equalsIgnoreCase(amt) || "0.0".equalsIgnoreCase(amt) || "0.00".equalsIgnoreCase(amt)) {
			return "零元整";
		}

		// 校验小数位数是否小于两位
		int local = amt.indexOf(".");
		if (local != -1 && amt.substring(local + 1).length() > 2) {
			throw new Exception("输入金额不能操过两位小数！");
		}

		// 将金额字串转为整数和小数部分
		String temp[] = switchIntAndDig(amt);

		StringBuffer sb = new StringBuffer();
		sb.append(generateIntegerChinese(temp[0]));
		sb.append(generateDigitalChinese(temp[1]));

		return sb.toString();
	}

	// 产生整数位的金额大写
	private static String generateIntegerChinese(String digital) throws Exception {
		// 参数校验
		if (digital == null || digital.length() > 13) {
			throw new Exception("输入的整数位数不正确或者金额过于庞大！");
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digital.length(); i++) {
			char ch = digital.charAt(i);
			if (digital.length() - i == 5 && ch == '0') {
				sb.append("万");
			}

			if (digital.length() - i == 9 && ch == '0') {
				sb.append("亿");
			}

			if (i == digital.length() - 1 && ch == '0') {
				sb.append("元");
			}

			if (ch == '0') {
				continue;
			}
			sb.append(swtichtoChinese(ch));
			sb.append(partern[digital.length() - 1 - i]);
		}
		return sb.toString();
	}

	// 产生小数位的金额大写
	private static String generateDigitalChinese(String digital) throws Exception {
		// 参数校验
		if (digital == null || digital.length() > 2) {
			throw new Exception("输入的小数位数不正确，请在两位之内！");
		}

		// 小数为0的判断处理
		if ("00".equals(digital) || "0".equals(digital)) {
			return "整";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digital.length(); i++) {
			char ch = digital.charAt(i);
			if (ch == '0') {
				continue;
			}
			sb.append(swtichtoChinese(ch));
			if (i == 0) {
				sb.append("角");
			} else {
				sb.append("分");
			}

		}
		return sb.toString();
	}

	// 将金额字串转为整数和小数部分
	private static String[] switchIntAndDig(String money) throws Exception {
		// 定义会传数组
		String[] rslt = { "", "" };

		// 检查入口参数
		try {
			new BigDecimal(money);
		} catch (Exception e) {
			throw new Exception("输入参数不正确！");
		}

		// 分解数组，如果没有小数位，小数位用0表示。
		int local = money.indexOf(".");
		if (local == -1) {
			rslt[0] = money;
			rslt[1] = "0";
		} else {
			rslt[0] = money.substring(0, local);
			rslt[1] = money.substring(local + 1);
		}

		return rslt;
	}

	// 将阿拉伯数字转为中文数字
	private static String swtichtoChinese(char ch) throws Exception {
		switch (ch) {
		case '0':
			return "零";
		case '1':
			return "壹";
		case '2':
			return "贰";
		case '3':
			return "叁";
		case '4':
			return "肆";
		case '5':
			return "伍";
		case '6':
			return "陆";
		case '7':
			return "柒";
		case '8':
			return "捌";
		case '9':
			return "玖";
		default:
			throw new Exception("输入字符不正确");
		}
	}

	/**
	 * add by thw 返回单个金额大写值
	 *
	 * @param request
	 * @param bg
	 * @param flag
	 *            如果同一request中有多个金额值显示，可以加flag来区分
	 * @throws Exception
	 */
	public static void genChineseMoneyTable(HttpServletRequest request, BigDecimal bg, String flag) throws Exception {
		StringBuilder sb = new StringBuilder("");
		// 将金额字串转为整数和小数部分
		String amt = bg.toString();
		String temp[] = switchIntAndDig(amt);
		String arr = padLeft(temp[0], 8, '0');
		String str = "";
		for (int i = 0; i < arr.length(); i++) {
			str = arr.substring(i, i + 1);
			if (i == 0) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "QW", "&nbsp;零");
				} else {
					request.setAttribute(flag + "QW", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 1) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "BW", "&nbsp;零");
				} else {
					request.setAttribute(flag + "BW", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 2) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "SW", "&nbsp;零");
				} else {
					request.setAttribute(flag + "SW", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 3) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "W", "&nbsp;零");
				} else {
					request.setAttribute(flag + "W", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 4) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "Q", "零&nbsp;");
				} else {
					request.setAttribute(flag + "Q", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 5) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "B", "零&nbsp;");
				} else {
					request.setAttribute(flag + "B", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 6) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "S", "&nbsp;零");
				} else {
					request.setAttribute(flag + "S", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			} else if (i == 7) {
				if ("0".equals(str)) {
					request.setAttribute(flag + "Y", "&nbsp;零");
				} else {
					request.setAttribute(flag + "Y", "&nbsp;" + swtichtoChinese(str.charAt(0)));
				}
			}
		}
	}

	/**
	 * add by lwy 20081222
	 *
	 * @param strIn
	 * @param regexString
	 * @param target
	 * @return
	 */
	public static String regexReplaceString(String strIn, String regexString, Object target) {
		String targetString = filterNull(target);
		return regexReplaceString(strIn, regexString, targetString);
	}

	public static String regexReplaceString(String strIn, String regexString, String target) {

		Pattern p = Pattern.compile(regexString, Pattern.CASE_INSENSITIVE);
		String s = strIn;
		Matcher m = p.matcher(s);

		StringBuffer sb = new StringBuffer();
		int i = 0;
		boolean result = m.find();
		while (result) {
			i++;
			try {
				m.appendReplacement(sb, target.replace("$", "\\$"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			result = m.find();
		}
		m.appendTail(sb);

		return sb.toString();
	}

	public static String StripHTML(String input) {
		StringBuffer output = new StringBuffer();
		StringBuffer temp = new StringBuffer();
		boolean inHTML = false;
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '<') {

				if (inHTML) {
					output.append(temp.toString());
					temp = new StringBuffer();
				}
				inHTML = true;
				temp.append(c);
			} else {
				if (c == '>') {
					inHTML = false;
					if (temp.toString().length() > 1) {
						temp = new StringBuffer();
					} else {
						output.append(temp.toString());
						output.append(c);
						temp = new StringBuffer();
					}
				} else {
					if (!inHTML) {
						output.append(c);

					} else {
						temp.append(c);
					}
				}
			}
		}
		output.append(temp.toString());
		return output.toString();
	}

	public static String StripHTML1(String source) {

		String result;

		// Remove HTML Development formatting
		result = source.replace("\r", " "); // Replace line breaks with space
											// because browsers inserts space
		result = result.replace("\n", " "); // Replace line breaks with space
											// because browsers inserts space
		result = result.replace("\t", ""); // Remove step-formatting
		result = regexReplaceString(result, "( )+", " "); // Remove repeating
															// speces becuase
															// browsers ignore
															// them

		// Remove the header (prepare first by clearing attributes)
		result = regexReplaceString(result, "<( )*head([^>])*>", "<head>");
		result = regexReplaceString(result, "(<( )*(/)( )*head( )*>)", "</head>");
		result = regexReplaceString(result, "(<head>).*(</head>)", "");

		// remove all scripts (prepare first by clearing attributes)
		result = regexReplaceString(result, "<( )*script([^>])*>", "<script>");
		result = regexReplaceString(result, "(<( )*(/)( )*script( )*>)", "</script>");
		// result = regexReplaceString(result,
		// "(<script>)([^(<script>\.</script>)])*(</script>)","");
		result = regexReplaceString(result, "(<script>).*(</script>)", "");

		// remove all styles (prepare first by clearing attributes)
		result = regexReplaceString(result, "<( )*style([^>])*>", "<style>");
		result = regexReplaceString(result, "(<( )*(/)( )*style( )*>)", "</style>");
		result = regexReplaceString(result, "(<style>).*(</style>)", "");

		// insert tabs in spaces of <td> tags
		result = regexReplaceString(result, "<( )*td([^>])*>", "\t");

		// insert line breaks in places of <BR> and <LI> tags
		result = regexReplaceString(result, "<( )*br( )*>", "\r");
		result = regexReplaceString(result, "<( )*li( )*>", "\r");

		// insert line paragraphs (double line breaks) in place if <P>, <DIV>
		// and <TR> tags
		result = regexReplaceString(result, "<( )*div([^>])*>", "\r\r");
		result = regexReplaceString(result, "<( )*tr([^>])*>", "\r\r");
		result = regexReplaceString(result, "<( )*p([^>])*>", "\r\r");

		// Remove remaining tags like <a>, links, images, comments etc -
		// anything thats enclosed inside < >
		result = regexReplaceString(result, "<[^>]*>", "");

		// replace special characters:
		result = regexReplaceString(result, "&nbsp;", " ");

		result = regexReplaceString(result, "&bull;", " * ");
		result = regexReplaceString(result, "&lsaquo;", "<");
		result = regexReplaceString(result, "&rsaquo;", ">");
		result = regexReplaceString(result, "&trade;", "(tm)");
		result = regexReplaceString(result, "&frasl;", "/");
		// result = regexReplaceString(result, "<","<");
		// result = regexReplaceString(result, ">",">");
		result = regexReplaceString(result, "&copy;", "(c)");
		result = regexReplaceString(result, "&reg;", "(r)");
		// Remove all others. More can be added, see
		// http://hotwired.lycos.com/webmonkey/reference/special_characters/
		result = regexReplaceString(result, "&(.{2,6});", "");

		// for testng
		// regexReplaceString(result, this.txtRegex.Text,"");

		// make line breaking consistent
		result = result.replace("\n", "\r");

		// Remove extra line breaks and tabs: replace over 2 breaks with 2 and
		// over 4 tabs with 4.
		// Prepare first to remove any whitespaces inbetween the escaped
		// characters and remove redundant tabs inbetween linebreaks
		result = regexReplaceString(result, "(\r)( )+(\r)", "\r\r");
		result = regexReplaceString(result, "(\t)( )+(\t)", "\t\t");
		result = regexReplaceString(result, "(\t)( )+(\r)", "\t\r");
		result = regexReplaceString(result, "(\r)( )+(\t)", "\r\t");
		result = regexReplaceString(result, "(\r)(\t)+(\r)", "\r\r"); // Remove
																		// redundant
																		// tabs
		result = regexReplaceString(result, "(\r)(\t)+", "\r\t"); // Remove
																	// multible
																	// tabs
																	// followind
																	// a
																	// linebreak
																	// with just
																	// one tab
		String breaks = "\r\r\r"; // Initial replacement target string for
									// linebreaks
		String tabs = "\t\t\t\t\t"; // Initial replacement target string for
									// tabs
		for (int index = 0; index < result.length(); index++) {
			result = result.replace(breaks, "\r\r");
			result = result.replace(tabs, "\t\t\t\t");
			breaks = breaks + "\r";
			tabs = tabs + "\t";
		}

		// Thats it.
		return result;

	}

	/**
	 * add by lgq，rtf转换.
	 *
	 * @param rtf
	 * @return
	 * @throws Exception
	 */
	public static String RtfToString(String rtf) throws Exception {
		// rtf=rtf.replaceAll("\\\\ldblquote","\\\\'A1\\\\'B0");
		// rtf=rtf.replaceAll("\\\\rdblquote","\\\\'A1\\\\'B1");

		RTFEditorKit rtf_edit = new RTFEditorKit();

		DefaultStyledDocument doc = new DefaultStyledDocument();

		StringReader reader = new StringReader(rtf);

		rtf_edit.read(reader, doc, 0);

		String text = doc.getText(0, doc.getLength());
		text = new String(text.getBytes("ISO8859-1"), "GBK");
		text = text.replaceAll(" ", "&nbsp;");
		text = text.replaceAll("\\n", "<br>");
		text = text.replaceAll("\\r", "<br>");
		return text;
	}

	/**
	 * 数据库的内容转化为html格式.
	 *
	 * @param source
	 * @return
	 */
	public static String replaceToHtml(String source) {
		if (isNullOrBlank(source)) {
			return "";
		}
		return source.replace("\r\n", "<br>").replace("\n", "<br>");
	}

	/**
	 * add by zxg，判断字符串中有多少汉字（包括繁体）.
	 *
	 * @param str
	 * @return
	 */
	public static int countZH(String str) {
		int count = 0;
		String regEx = "[\u4E00-\u9FA5\uFE30-\uFFA0]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		while (m.find()) {
			for (int i = 0; i <= m.groupCount(); i++) {
				count = count + 1;
			}
		}
		return count;
		// System.out.println("共有 " + count + "个 ");
	}

	/**
	 * 判断字符串中是否存在"[@"和"@]".
	 *
	 * @param str
	 * @return
	 */
	public static boolean isExistParam(String str) {
		if (str == null) {
			return false;
		}
		if (str.contains("[@") && str.contains("@]")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断map所有value中是否存在"[@"和"@]".
	 *
	 * @param map
	 * @return
	 */
	public static boolean isExistParam(Map<String, Object> map) {
		if (map == null) {
			return false;
		}
		Set<String> keySet = map.keySet();
		for (Object element : keySet) {
			String key = (String) element;
			String value = (String) map.get(key);
			if (isExistParam(value)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过业务类传过来的参数对SQL中对应的参数进行替换,SQL中参数格式为[@参数名@].
	 *
	 * @param content
	 * @param map
	 * @return
	 */
	public static String transSql(String content, Map<?, ?> map, List<Object> params) {
		Pattern p = Pattern.compile("\\[@\\S[^@]*@\\]", 2);// 正则表达式，\\S表示去掉空白字符，如空格、回车等，*表示任意符号，值2是表示大小写不限制
		Matcher m = p.matcher(content);
		String key;
		String paramKey;

		while (m.find()) {
			key = m.group().toLowerCase();
			paramKey = key.substring(2, key.length() - 2);// 去掉[@ @]
			content = StringUtil.regexReplaceString(content, "\\[@" + paramKey + "@\\]", "?");
			params.add(map.get(paramKey));
		}
		return content;

	}

	/**
	 * 去除字符串中的空格、回车、换行符、制表符，本方法采用的是java的正则表达式.
	 *
	 * @param str
	 */
	public static String replaceBlank(String str) {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		Matcher m = p.matcher(str);
		String after = m.replaceAll("");
		return after;

	}

	/**
	 * 字符串转化为list.
	 *
	 * @param str
	 * @param regex
	 *            分隔符
	 * @return
	 */
	public static List<String> splitStrToList(String str, String regex) {
		List<String> list = new ArrayList<String>();
		if (str == null) {
			return list;
		}
		if (regex == null) {
			regex = ",";
		}
		String[] strAry = str.split(regex);
		for (String element : strAry) {
			list.add(element);
		}
		return list;
	}

	/**
	 * 从指定字符串中萃取 某种规则的一批字符串.
	 *
	 * @author lineshow created on 2011-11-30
	 * @param srcStr
	 * @param regex
	 *            eg.(default)"\\[@([A-Za-z|_]+)@\\]"
	 * @param case_state
	 *            [1:upper;0:ignore;-1:lower]
	 * @return
	 */
	public static List<String> extractFromSpecStr(String srcStr, String regex, int case_state) {
		if (StringUtils.isEmpty(srcStr)) {
			return new ArrayList<String>();
		}
		if (StringUtils.isEmpty(regex)) {
			regex = "\\[@([A-Za-z|_]+)@\\]";
		}

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(srcStr);

		List<String> extractList = new ArrayList<String>();
		while (matcher.find()) {
			switch (case_state) {
			case 1:
				extractList.add(matcher.group(1).toUpperCase());
				break;
			case -1:
				extractList.add(matcher.group(1).toLowerCase());
				break;
			default:
				extractList.add(matcher.group(1));
			}
		}

		return extractList;
	}

	/**
	 * 全角转半角
	 *
	 * @param full
	 * @return String
	 */
	public static String full2Half(String full) {
		char c[] = full.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	// 去除增补字符 unicode说明定义的算法 计算出增补字符范围0x10000 至 0x10FFFF
	public static String filterSupplementaryChars(String text) {

		StringBuilder sb = new StringBuilder();

		if (text == null) {
			return "";
		}
		char[] data = text.toCharArray();
		for (int i = 0, len = data.length; i < len; i++) {

			char c = data[i];
			char high = c;
			char low;
			if (!Character.isHighSurrogate(high)) {
				sb.append(c);
				continue;

			}
			if (i + 1 == len) {
				break;
			}
			low = data[i + 1];
			// 先判断是否在代理范围（surrogate blocks）
			// 增补字符编码为两个代码单元，
			// 第一个单元来自于高代理（high surrogate）范围（0xD800 至 0xDBFF），
			// 第二个单元来自于低代理（low surrogate）范围（0xDC00 至 0xDFFF）。

			if (Character.isSurrogatePair(high, low)) { // 如果在代理范围

				int codePoint = Character.toCodePoint(high, low);
				if (Character.isSupplementaryCodePoint(codePoint)) {

					i++;

				} else {

					sb.append(c);

				}

			} else {

				sb.append(c);
			}

		}

		return sb.toString();
	}

	public static String replaceAll(String remark, String regex,String replaceValue) {
		if(StringUtil.isNullOrBlank(remark)||remark.equals("")){
			return remark;
		}

		return remark.replaceAll( regex,replaceValue);
	}


	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 *
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:zhangji@aspire-tech.com">ZhangJi</a>
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 *
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:zhangji@aspire-tech.com">ZhangJi</a>
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**
	 * @param str
	 *            the string need to be parsed
	 * @param delim
	 *            the delimiter to seperate created by YanFeng at 14/5/2003
	 */
	public static String[] parseToArray(String str, String delim) {
		ArrayList arr = new ArrayList();
		StringTokenizer st = new StringTokenizer(str, delim);
		while (st.hasMoreTokens()) {
			arr.add(st.nextToken());
		}
		String[] ret = new String[arr.size()];
		for (int i = 0; i < arr.size(); i++) {
			ret[i] = (String) arr.get(i);
		}
		return ret;
	}

	/**
	 * 带逗号分隔的数字转换为NUMBER类型
	 *
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Number stringToNumber(String str) throws ParseException {
		if (str == null || str.equals("")) {
			return null;
		}
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setDecimalSeparator('.');
		dfs.setGroupingSeparator(',');
		dfs.setMonetaryDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("###,###,###,###.##", dfs);
		return df.parse(str);
	}

	public static String getExtensionName(String filename) {
		if (filename != null && filename.length() > 0) {
			int dot = filename.lastIndexOf('.');
			if (dot > -1 && dot < filename.length() - 1) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	public static String valueOf(Object obj) {
		if (obj == null) {
			return null;
		}
		return String.valueOf(obj);
	}

	/**
	 * 用于字符串替换
	 *
	 * @param target
	 *            目标对象 需要替换的字符串
	 * @param replacement
	 *            要替换的字符串
	 * @param value
	 *            替换的值
	 * @return
	 */
	public static String replacement(String target, String replacement,
			String value) {
		if (target != null) {
			return target.replace(replacement, value);
		}
		return null;
	}

	/**
	 * 判断字符串是否为数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 计算指定时间与当前时间的差
	 *
	 * @param date
	 * @return
	 */
	public static String convDateToString(Date date) {
		Long time = new Date().getTime() - date.getTime();
		Long min = time / 1000 / 60;
		if (min < 5) {
			return "刚刚";
		} else if (min >= 5 && min < 60) {
			return min + "分钟之前";
		} else if (min >= 60 && min < 1440) {
			return min / 60 + "小时之前";
		} else if (min >= 1440 && min < 10080) {
			return min / 60 / 24 + "天之前";
		} else if (min >= 10080 && min < 40320) {
			return min / 60 / 24 / 7 + "周之前";
		} else if (min >= 40320 && min < 525600) {
			return min / 60 / 24 / 7 / 4 + "月之前";
		} else if (min >= 525600) {
			return min / 60 / 24 / 365 + "年之前";
		}
		return null;
	}

	public static String convNullToValue(String str, String value) {
		if (str == null) {
			return value;
		}
		return str;
	}

	/**
	 * 把手机号码中的中间四位用*号代替
	 *
	 * @param phone
	 * @returnStringUtil
	 */
	public static String securityPhone(String phone) {
		if (phone == null || phone.equals("")) {
			return "";
		}
		if (phone.length() < 11) {
			return phone;
		}
		String tempStr = "";
		tempStr = phone.substring(0, phone.length() - 4);
		return tempStr + "****";
	}

	/**
	 * 字符串是不是为空
	 *
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (null == str || str.trim().length() < 1) {
			return true;
		}
		return false;
	}

	/**
	 * 将 空格 替换成加号 , fid 传到页面的时候会将加号变成空格，这个方法是将其变回原来的样子 added by taking.wang
	 *
	 * @param str
	 * @return
	 */
	public static String replaceSpaceToAddChar(String str) {
		if (isEmpty(str)) {
			return null;
		}
		return str.replaceAll(" ", "+");
	}

	/**
	 * 转换html标记
	 *
	 * @return
	 */
	public static String html(String html) {
		if (html == null || html.equals("")) {
			return null;
		}
		StringBuffer result = null;
		String filtered = null;
		for (int i = 0; i < html.length(); ++i) {
			filtered = null;
			switch (html.charAt(i)) {
			case '<':
				filtered = "&lt;";
				break;
			case '>':
				filtered = "&gt;";
				break;
			case '&':
				filtered = "&amp;";
				break;
			case '"':
				filtered = "&quot;";
				break;
			case '\'':
				filtered = "&#39;";
			}
			if (result == null) {
				if (filtered != null) {
					result = new StringBuffer(html.length() + 50);
					if (i > 0) {
						result.append(html.substring(0, i));
					}
					result.append(filtered);
				}

			} else if (filtered == null) {
				result.append(html.charAt(i));
			} else {
				result.append(filtered);
			}
		}

		return ((result == null) ? html : result.toString());
	}

	/**
	 * 得到一个字符窜标识的内容
	 *
	 * @param str
	 * @param markeBegin
	 * @param markeEnd
	 * @return
	 */
	public static String getMarkeContent(String str, String markeBegin,
			String markeEnd) {
		return StringUtil.getMarkeContent(str, markeBegin, markeEnd, null);
	}

	/**
	 * 得到一个字符窜标识的内容
	 *
	 * @param str
	 * @param markeBegin
	 * @param markeEnd
	 * @param defualtValue
	 * @return
	 */
	public static String getMarkeContent(String str, String markeBegin,
			String markeEnd, String defualtValue) {
		if (StringUtil.isEmpty(str)) {
			return defualtValue;
		}
		if (StringUtil.isEmpty(markeBegin)) {
			return defualtValue;
		}
		int beginIndex = str.indexOf(markeBegin);
		int endIndex = str.lastIndexOf(markeEnd);
		if (beginIndex < 0 || endIndex < 0 || endIndex <= beginIndex) {
			return defualtValue;
		}
		return str.substring(beginIndex + 1, endIndex);
	}

	/**
	 * 得到一个字符排除标识中的内容
	 *
	 * @param str
	 * @param markeBegin
	 * @return
	 */
	public static String getNotMarkeContent(String str, String markeBegin,
			String markeEnd) {
		if (StringUtil.isEmpty(str)) {
			return null;
		}
		if (StringUtil.isEmpty(markeEnd)) {
			return str;
		}
		int endIndex = str.lastIndexOf(markeEnd) + 1;
		return str.substring(endIndex);
	}

	/**
	 * 所以的参数多不为空
	 *
	 * @param strs
	 * @return
	 */
	public static boolean isNotEmpty(String... strs) {
		if (strs == null || strs.length <= 0) {
			return false;
		}
		for (String str : strs) {
			if (StringUtil.isEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isEmpty(Object obj) {
		if (null == obj) {
			return true;
		}
		return String.valueOf(obj).trim().length() < 1;
	}

	/*
	 * 判断是否为整数
	 *
	 * @param str 传入的字符串
	 *
	 * @return 是整数返回true,否则返回false
	 */

	public static boolean isInteger(String str) {
		if (!isEmpty(str)) {
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
			return pattern.matcher(str).matches();
		}
		return Boolean.FALSE;
	}

	/*
	 * 判断是否为浮点数，包括double和float
	 *
	 * @param str 传入的字符串
	 *
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		if (!isEmpty(str)) {
			Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]+$");
			return pattern.matcher(str).matches();
		}
		return Boolean.FALSE;
	}

	/*
	 * 判断是否为数字类型
	 *
	 * @param str 传入的字符串
	 *
	 * @return 是数字类型返回true,否则返回false
	 */
	public static boolean isNumber(String str) {
		if (!isEmpty(str)) {
			return isDouble(str) || isInteger(str);
		}
		return Boolean.FALSE;
	}

	/**
	 * 过滤html相关标签
	 *
	 * @param htmlStr
	 * @return
	 */
	public static String getHtmlString(String htmlStr) {
		String textStr = "";
		Pattern p_script;
		Matcher m_script;
		Pattern p_style;
		Matcher m_style;
		Pattern p_html;
		Matcher m_html;

		Pattern p_html1;
		Matcher m_html1;

		try {
			String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
			String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}

	public static String toString(Object obj) {
		if (obj == null) {
			return "";
		}
		return StringUtil.valueOf(obj);
	}

	public static boolean matches(String regex, String value) {
		if (StringUtil.isEmpty(regex) || StringUtil.isEmpty(value)) {
			return false;
		}
		return value.matches(regex);
	}

	public static String formatCustomerPhone(String phone){
		String result = "";
		if(StringUtil.isNotEmpty(phone)){
			if(phone.length() != 11){
				result = phone;
			}else{
				result = phone.substring(0 , 3)+" " + phone.substring(3,7)+" "+phone.substring(7);
			}
		}
		return result;
	}

	public static String formatPhone(String phone){
		String result = "";
		if(StringUtil.isNotEmpty(phone)){
			if(phone.length() >= 11){
				result = phone.substring(0 , 3)+"****"+phone.substring(7);
			}else{
				if(phone.length() >= 4){
					result = phone.substring(0,phone.length() - 3)+"***";
				}else{
					result = phone;
				}
			}
		}
		return result;
	}
	
	public static String formatCornet(String cornet){
		String result = "";
		if(StringUtil.isNotEmpty(cornet)){
			result = cornet.substring(0,4) + " "+cornet.substring(4);
		}
		return result;
	}
	//半角转换为全角
	public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
        	if (c[i] == ' ') {
        		c[i] = '\u3000';
        	} else if (c[i] < '\177') {
        		c[i] = (char) (c[i] + 65248);
        	}
        }
        return new String(c);
	}
	//全角转换为半角
	public static String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
	          if (c[i] == '\u3000') {
	        	  c[i] = ' ';
	          } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
	        	  c[i] = (char) (c[i] - 65248);
	          }
        }
        String returnString = new String(c);
        return returnString;
	}
	
	public static String filterCRLFInjection(String target) {
		if (target == null) {
			return target;
		}
		target = target.trim();
		target = target.replace("\r", "");
		target = target.replace("\n", "");
		return target;
	}
	
	public static String getResData(String res) {
		if(null == res) {
			return null;
		}
		res.indexOf("\"data\": ");
		String[] spiltBydata = res.split("\"data\": ");
		return spiltBydata[1].substring(0, spiltBydata[1].length() - 1);
	}
	
	public static String listTurnString(List<String> strs) {
		String res = "";
		if(null == strs || strs.isEmpty()) {
			return res;
		}
		
		for(String str : strs) {
			res+=str + ",";
		}
		
		return res.substring(0, res.length() - 1);
	}
	
	public static List<String> stringTurnList(String str){
		String[] strs = str.split(",");
		List<String> strList = new ArrayList<String>();
		for(int i = 0;i<strs.length;i++) {
			strList.add(strs[i]);
		}
		return strList;
	}

	public static String getMd5InputStream(InputStream in) {
		MessageDigest digest = null;
		byte[] buffer = new byte[1024];

		try {
			digest = MessageDigest.getInstance("MD5");
			while (true) {
				int len;
				if ((len = in.read(buffer, 0, 1024)) == -1) {
					in.close();
					break;
				}
				digest.update(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BigInteger var5 = new BigInteger(1, digest.digest());
		return String.format("%1$032x", new Object[]{var5});
	}

	public static String streamTurnString(InputStream in)throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		StringBuffer sb = new StringBuffer();
		String line ="";
		while((line=bf.readLine())!=null){
			sb.append(line);
	    }
		return sb.toString();
	}

	public static String stringTurnMD5(String s)throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = md.digest(s.getBytes("utf-8"));
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	   
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
	
	public static List<String> dateSort(List<String> dates) throws ParseException{
		
		String[] dateStr = (String[])dates.toArray(new String[dates.size()]);
		for(int i=0; i< dates.size() -1; i++) {
			for(int j=0; j< dates.size()-1-i; j++) {
				if(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parse(dateStr[j]).getTime() > new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
								.parse(dateStr[j+1]).getTime() ) {
					String temp = dateStr[j];
					dateStr[j] = dateStr[j+1];
					dateStr[j+1] = temp;
				}
			}
		}
		return Arrays.asList(dateStr);
	}
	
	// 截取小数后面的000
	public static String subData(String source) {

		if (isNumeric(source)) {
			
			BigDecimal bigDecimal = new BigDecimal(source);
			
			source=bigDecimal+"";
			int indexOf = source.indexOf(".");
			if (indexOf != -1) {
				String substring = source.substring(indexOf + 1,
						source.length());

				if (Integer.valueOf(substring) != 0) {
					return source;
				} else {

					String sub = source.substring(0, indexOf);
					String data = Integer.valueOf(sub) + "";
					return data;
				}
			}

		}

		return source;
	}
	
	public static Map<String, Object> json2Map(String str){
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析  
        JSONObject json = JSONObject.parseObject(str);
        for(Object k : json.keySet()){  
            Object v = json.get(k);   
            //如果内层还是数组的话，继续解析  
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();  
                Iterator<Object> it = ((JSONArray)v).iterator();
                while(it.hasNext()){  
                    JSONObject json2 = (JSONObject) it.next();
                    list.add(json2Map(json2.toString()));  
                }  
                map.put(k.toString(), list);  
            } else {  
                map.put(k.toString(), v);  
            }  
        }  
        return map; 
	}
	
	public static String getEmailContentDemo(String message) {
		StringBuffer sb = new StringBuffer();
		sb.append(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")
				.append("<html>")
				.append("<head>")
				.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
				.append("<title>告警通知</title>")
				.append("</head>")
				.append("<body>")
				.append("<span >尊敬的用户,您好！</span><br>")
				.append("<span >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + message + "</span><br>")
				.append("<span > 请及时查看。</span><br>")
				.append("<span > 感谢您使用脉联云服务，我们将竭诚为您服务。如有任何问题请随时与我们联系，以下是我们的联系方式：</span><br>")
				.append("<span ></span><br>")
				.append("<span >深圳市脉联电子有限公司</span><br>")
				.append("<span >地址：深圳市龙华新区龙华街道清龙路6号港之龙商务中心A座A300（4号线清湖地铁站A出口）</span><br>")
				.append("<span >电话：0755-23771112-801</span><br>")
				.append("<span >传真：0755-23771115-808</span><br>")
				.append("<a href=")
				.append("http://www.szmlink.com")
				.append("><span >网址：www.szmlink.com</span><br></a>")
				.append("<a href=")
				.append("http://www.mlcloud.com.cn")
				.append("><span >云平台：www.mlcloud.com.cn</span><br></a>")
				.append("<span ></span>").append("</body>")
				.append("</html>");
		return sb.toString();
	}
	
	
}