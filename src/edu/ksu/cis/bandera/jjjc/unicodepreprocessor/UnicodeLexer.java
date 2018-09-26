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

public class UnicodeLexer extends Lexer {
	private PushbackReader in;

	private Token buffer;

	public UnicodeLexer(PushbackReader in) {
		super(in);
		this.in = in;
	}
	protected void filter() {
		if (state.equals(State.SUB)) {
			if (buffer == null) {
				buffer = token;
				token = null;
			} else {
				if (token instanceof EOF) {
					buffer = null;
					state = State.NORMAL;
				} else {
					try {
						in.unread(token.getText().toCharArray());
					} catch (IOException e) {
						throw new RuntimeException(
								"Error while unreading: " + e);
					}

					token = buffer;
					buffer = null;
					state = State.NORMAL;
				}
			}
		} else {
			if (token instanceof TUnicodeEscape) {
				String text = token.getText();

				if (text.substring(text.length() - 4).equalsIgnoreCase("001a")) {
					buffer = token;
					token = null;
					state = State.SUB;
				}
			}
		}
	}
}
