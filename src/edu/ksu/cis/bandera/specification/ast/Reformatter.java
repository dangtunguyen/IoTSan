package edu.ksu.cis.bandera.specification.ast;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import java.io.*;
import java.util.*;
import edu.ksu.cis.bandera.specification.node.*;
import edu.ksu.cis.bandera.specification.lexer.*;
import edu.ksu.cis.bandera.specification.parser.*;
public class Reformatter {
/**
 * 
 * @return java.lang.String
 * @param format java.lang.String
 */
public static String format(String format) throws Exception{
	String formatted =
			new Parser(new Lexer(new PushbackReader(new StringReader("\"" 
			+ format + "\"")))).parse().toString().trim();
	StringBuffer buffer = new StringBuffer();
	for (StringTokenizer tokenizer = new StringTokenizer(formatted, "{}", true); tokenizer.hasMoreTokens();) {
		String token = tokenizer.nextToken();
		if ("{".equals(token)) {
			while (!"}".equals(tokenizer.nextToken()));
			buffer.append("{}");
		} else {
			buffer.append(token);
		}
	}
		
	return buffer.toString();
}
}
