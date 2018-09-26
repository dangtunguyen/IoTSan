package edu.ksu.cis.bandera.jjjc.codegenerator;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2002   Robby (robby@cis.ksu.edu)                    *
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
import java.util.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
class TokenLineColumnNumberExtractor {
	static class TokenEscapeException extends RuntimeException {
		public TokenEscapeException() {
			super();
		}
		public TokenEscapeException(String msg) {
			super(msg);
		}
	}
	private static  DepthFirstAdapter fte = new DepthFirstAdapter() {
		public void defaultCase(Node node) {
			if (node instanceof Token) {
				Token t = (Token) node;
				if (t.getLine() == 0 && t.getPos() == 0) return;
				throw new TokenEscapeException(t.getLine() + " " + t.getPos());
			}
		}
	};
	private static ReversedDepthFirstAdapter lte = new ReversedDepthFirstAdapter() {
		public void defaultCase(Node node) {
			if (node instanceof Token) {
				Token t = (Token) node;
				if (t.getLine() == 0 && t.getPos() == 0) return;
				throw new TokenEscapeException(t.getLine() + " " + t.getPos());
			}
		}
	};
	private static TokenLineColumnNumberExtractor tlcne = new TokenLineColumnNumberExtractor();
/**
 * 
 */
private TokenLineColumnNumberExtractor() {}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.codegenerator.LineColumn
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public static LineColumn getLineColumn(Node node) {
	int firstLine = 0, firstColumn = 0, lastLine = 0, lastColumn = 0;
	try {
		node.apply(fte);
	} catch (RuntimeException re) {
		StringTokenizer t = new StringTokenizer(re.getMessage());
		firstLine = Integer.parseInt(t.nextToken());
		firstColumn = Integer.parseInt(t.nextToken());
	}
	try {
		node.apply(lte);
	} catch (RuntimeException re) {
		StringTokenizer t = new StringTokenizer(re.getMessage());
		lastLine = Integer.parseInt(t.nextToken());
		lastColumn = Integer.parseInt(t.nextToken());
	}
	return new LineColumn(firstLine, firstColumn, lastLine, lastColumn);
}
}
