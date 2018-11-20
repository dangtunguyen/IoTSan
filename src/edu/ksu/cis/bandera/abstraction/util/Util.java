package edu.ksu.cis.bandera.abstraction.util;

import java.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.Type;
public class Util {
/**
 * Util constructor comment.
 */
private Util() {
}
public static String deleteChars(String s, String chars) {
	StringBuffer buffer = new StringBuffer();
	for (StringTokenizer t = new StringTokenizer(s, chars); t.hasMoreTokens();) {
		buffer.append(t.nextToken());
	}
	return buffer.toString();
}
public static Type getType(String typeName) {
	if ("int".equals(typeName)) {
		return IntType.v();
	} else
		if ("byte".equals(typeName)) {
			return ByteType.v();
		} else
			if ("short".equals(typeName)) {
				return ShortType.v();
			} else
				if ("long".equals(typeName)) {
					return LongType.v();
				} else
					if ("char".equals(typeName)) {
						return CharType.v();
					} else
						if ("boolean".equals(typeName)) {
							return BooleanType.v();
						} else
							if ("float".equals(typeName)) {
								return FloatType.v();
							} else
								if ("double".equals(typeName)) {
									return DoubleType.v();
								} else {
									return RefType.v(typeName);
								}
}
/**
 * 
 * @return boolean
 * @param s java.lang.String
 */
public static boolean hasJavaPrefix(String s) {
	return s.startsWith("java.") || s.startsWith("javax.");
}
}
