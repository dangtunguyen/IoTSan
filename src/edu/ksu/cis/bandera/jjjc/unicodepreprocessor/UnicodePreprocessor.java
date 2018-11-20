package edu.ksu.cis.bandera.jjjc.unicodepreprocessor;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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

import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.lexer.*;
import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.node.*;
import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.analysis.*;

public class UnicodePreprocessor extends Reader {
	private UnicodeLexer lexer;
	private ProcessToken processor = new ProcessToken();

	int available;
	char[] buffer = new char[2];

	private class ProcessToken extends AnalysisAdapter {
		public void caseTEvenBackslash(TEvenBackslash node) {
			buffer[0] = '\\';
			buffer[1] = '\\';
			available = 2;
		}

		public void caseTUnicodeEscape(TUnicodeEscape node) {
			String text = node.getText();
			buffer[0] = (char) Integer.parseInt(
					text.substring(text.length() - 4), 16);
			available = 1;
		}

		public void caseTErroneousEscape(TErroneousEscape node) {
			throw new RuntimeException("Erroneous escape: " + node);
		}

		public void caseTSub(TSub node) {
			buffer[0] = node.getText().charAt(0);
			available = 1;
		}

		public void caseTRawInputCharacter(TRawInputCharacter node) {
			buffer[0] = node.getText().charAt(0);
			available = 1;
		}

		public void caseEOF(EOF node) {
			available = 0;
		}
	}

	public UnicodePreprocessor(PushbackReader in) {
		lexer = new UnicodeLexer(in);
	}
	public void close() {
	}
	public int read() throws IOException {
		if (available == 0) {
			try {
				lexer.next().apply(processor);
			} catch (LexerException e) {
				throw new RuntimeException(e.toString());
			}
		}

		if (available == 0) {
			return -1;
		}

		char c = buffer[0];
		buffer[0] = buffer[1];
		available--;

		return c;
	}
	public int read(char cbuf[], int off, int len) throws IOException {
		for (int i = 0; i < len; i++) {
			int c = read();

			if (c == -1) {
				if (i == 0) {
					return -1;
				} else {
					return i;
				}
			}

			cbuf[off + i] = (char) c;
		}

		return len;
	}
}
