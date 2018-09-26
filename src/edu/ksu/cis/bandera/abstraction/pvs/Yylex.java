package edu.ksu.cis.bandera.abstraction.pvs;

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
import java_cup.runtime.Symbol;


public class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
 
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private boolean yy_last_was_cr=false;
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"41:9,40:2,41,40:2,41:18,40,9,41:3,6,7,41,13,14,4,2,37,3,41,5,38:10,41,1,11," +
"10,12,41:2,39:4,32,39:5,31,39:2,33,30,39:3,34,29,39:6,41:6,21,15,25,20,16,3" +
"9,17,39,18,39:2,28,39,19,26,39:2,24,22,23,39:3,27,39:2,35,8,36,41:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,73,
"0,1:3,2,1:3,3,4,5,6,7,1:2,8,1:3,9,1:8,10:6,11,10,12,13,1,14,15,16,17,18,19," +
"20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,10,42,43," +
"44,45,46")[0];

	private int yy_nxt[][] = unpackFromString(47,42,
"1,2,3,4,5,6,7,8,36,9,10,11,12,13,14,15,46,67,69,67:2,70,67:2,54,67:4,71,67:" +
"5,16,17,18,19,67,20,38,-1:54,21,-1:36,22,-1:44,24,-1:41,25,-1:41,26,-1:41,2" +
"7,-1:46,67,55,67:18,-1:3,67:2,-1:40,19,-1:18,67:20,-1:3,67:2,-1:17,67:3,53," +
"67:16,-1:3,67:2,-1:10,23,-1:48,67:5,28,67:14,-1:3,67:2,-1:17,67:13,29,67:6," +
"-1:3,67:2,-1:17,67:4,30,67:15,-1:3,67:2,-1:17,67:19,31,-1:3,67:2,-1:17,67:7" +
",32,67:12,-1:3,67:2,-1:17,67:13,33,67:6,-1:3,67:2,-1:17,67:8,34,67:11,-1:3," +
"67:2,-1:17,67:4,35,67:15,-1:3,67:2,-1:17,67:4,37,67:7,56,67:7,-1:3,67:2,-1:" +
"17,67:6,39,67:13,-1:3,67:2,-1:17,67:3,40,67:16,-1:3,67:2,-1:17,67:18,41,67," +
"-1:3,67:2,-1:17,67:5,42,67:14,-1:3,67:2,-1:17,67:6,43,67:13,-1:3,67:2,-1:17" +
",67:10,44,67:9,-1:3,67:2,-1:17,67:11,45,67:8,-1:3,67:2,-1:17,67,47,67:18,-1" +
":3,67:2,-1:17,67:2,48,67:17,-1:3,67:2,-1:17,67:8,59,67:11,-1:3,67:2,-1:17,6" +
"7:7,60,67:12,-1:3,67:2,-1:17,67:16,61,67:3,-1:3,67:2,-1:17,67,62,67:18,-1:3" +
",67:2,-1:17,67:8,64,67:11,-1:3,67:2,-1:17,67:17,49,67:2,-1:3,67:2,-1:17,67:" +
"4,50,67:15,-1:3,67:2,-1:17,67:2,65,67:17,-1:3,67:2,-1:17,67:9,66,67:10,-1:3" +
",67:2,-1:17,67:9,51,67:10,-1:3,67:2,-1:17,67:6,52,67:13,-1:3,67:2,-1:17,67," +
"63,67:18,-1:3,67:2,-1:17,67:4,72,67:15,-1:3,67:2,-1:17,57,67:19,-1:3,67:2,-" +
"1:17,67:15,58,67:4,-1:3,67:2,-1:17,67:8,68,67:11,-1:3,67:2,-1:2");

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}
	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}
	public Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}
public int line() {return yyline;}
	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {
				return null;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return new Symbol(sym.SEMI); }
					case -3:
						break;
					case 3:
						{ return new Symbol(sym.PLUS); }
					case -4:
						break;
					case 4:
						{ return new Symbol(sym.MINUS); }
					case -5:
						break;
					case 5:
						{ return new Symbol(sym.TIMES); }
					case -6:
						break;
					case 6:
						{ return new Symbol(sym.DIV); }
					case -7:
						break;
					case 7:
						{ return new Symbol(sym.MOD); }
					case -8:
						break;
					case 8:
						{ System.err.println("Illegal character: "+yytext()); }
					case -9:
						break;
					case 9:
						{ return new Symbol(sym.NOT); }
					case -10:
						break;
					case 10:
						{ return new Symbol(sym.EQUAL); }
					case -11:
						break;
					case 11:
						{ return new Symbol(sym.LT); }
					case -12:
						break;
					case 12:
						{ return new Symbol(sym.GT); }
					case -13:
						break;
					case 13:
						{ return new Symbol(sym.LPAREN); }
					case -14:
						break;
					case 14:
						{ return new Symbol(sym.RPAREN); }
					case -15:
						break;
					case 15:
						{ return new Symbol(sym.ID, yytext()); }
					case -16:
						break;
					case 16:
						{ return new Symbol(sym.LBRACE); }
					case -17:
						break;
					case 17:
						{ return new Symbol(sym.RBRACE); }
					case -18:
						break;
					case 18:
						{ return new Symbol(sym.COMMA); }
					case -19:
						break;
					case 19:
						{ return new Symbol(sym.NUMBER, new Integer(yytext())); }
					case -20:
						break;
					case 20:
						{ /* ignore white space. */ }
					case -21:
						break;
					case 21:
						{ return new Symbol(sym.ARROW); }
					case -22:
						break;
					case 22:
						{ return new Symbol(sym.AND); }
					case -23:
						break;
					case 23:
						{ return new Symbol(sym.OR); }
					case -24:
						break;
					case 24:
						{ return new Symbol(sym.NE); }
					case -25:
						break;
					case 25:
						{ return new Symbol(sym.EQ); }
					case -26:
						break;
					case 26:
						{ return new Symbol(sym.LE); }
					case -27:
						break;
					case 27:
						{ return new Symbol(sym.GE); }
					case -28:
						break;
					case 28:
						{ return new Symbol(sym.END); }
					case -29:
						break;
					case 29:
						{ return new Symbol(sym.FLOAT); }
					case -30:
						break;
					case 30:
						{ return new Symbol(sym.BEGIN); }
					case -31:
						break;
					case 31:
						{ return new Symbol(sym.TOKENS); }
					case -32:
						break;
					case 32:
						{ return new Symbol(sym.EXTENDS); }
					case -33:
						break;
					case 33:
						{ return new Symbol(sym.INT); }
					case -34:
						break;
					case 34:
						{ return new Symbol(sym.ABSTRACT); }
					case -35:
						break;
					case 35:
						{ return new Symbol(sym.ABSTRACTION); }
					case -36:
						break;
					case 36:
						{ System.err.println("Illegal character: "+yytext()); }
					case -37:
						break;
					case 37:
						{ return new Symbol(sym.ID, yytext()); }
					case -38:
						break;
					case 38:
						{ System.err.println("Illegal character: "+yytext()); }
					case -39:
						break;
					case 39:
						{ return new Symbol(sym.ID, yytext()); }
					case -40:
						break;
					case 40:
						{ return new Symbol(sym.ID, yytext()); }
					case -41:
						break;
					case 41:
						{ return new Symbol(sym.ID, yytext()); }
					case -42:
						break;
					case 42:
						{ return new Symbol(sym.ID, yytext()); }
					case -43:
						break;
					case 43:
						{ return new Symbol(sym.ID, yytext()); }
					case -44:
						break;
					case 44:
						{ return new Symbol(sym.ID, yytext()); }
					case -45:
						break;
					case 45:
						{ return new Symbol(sym.ID, yytext()); }
					case -46:
						break;
					case 46:
						{ return new Symbol(sym.ID, yytext()); }
					case -47:
						break;
					case 47:
						{ return new Symbol(sym.ID, yytext()); }
					case -48:
						break;
					case 48:
						{ return new Symbol(sym.ID, yytext()); }
					case -49:
						break;
					case 49:
						{ return new Symbol(sym.ID, yytext()); }
					case -50:
						break;
					case 50:
						{ return new Symbol(sym.ID, yytext()); }
					case -51:
						break;
					case 51:
						{ return new Symbol(sym.ID, yytext()); }
					case -52:
						break;
					case 52:
						{ return new Symbol(sym.ID, yytext()); }
					case -53:
						break;
					case 53:
						{ return new Symbol(sym.ID, yytext()); }
					case -54:
						break;
					case 54:
						{ return new Symbol(sym.ID, yytext()); }
					case -55:
						break;
					case 55:
						{ return new Symbol(sym.ID, yytext()); }
					case -56:
						break;
					case 56:
						{ return new Symbol(sym.ID, yytext()); }
					case -57:
						break;
					case 57:
						{ return new Symbol(sym.ID, yytext()); }
					case -58:
						break;
					case 58:
						{ return new Symbol(sym.ID, yytext()); }
					case -59:
						break;
					case 59:
						{ return new Symbol(sym.ID, yytext()); }
					case -60:
						break;
					case 60:
						{ return new Symbol(sym.ID, yytext()); }
					case -61:
						break;
					case 61:
						{ return new Symbol(sym.ID, yytext()); }
					case -62:
						break;
					case 62:
						{ return new Symbol(sym.ID, yytext()); }
					case -63:
						break;
					case 63:
						{ return new Symbol(sym.ID, yytext()); }
					case -64:
						break;
					case 64:
						{ return new Symbol(sym.ID, yytext()); }
					case -65:
						break;
					case 65:
						{ return new Symbol(sym.ID, yytext()); }
					case -66:
						break;
					case 66:
						{ return new Symbol(sym.ID, yytext()); }
					case -67:
						break;
					case 67:
						{ return new Symbol(sym.ID, yytext()); }
					case -68:
						break;
					case 68:
						{ return new Symbol(sym.ID, yytext()); }
					case -69:
						break;
					case 69:
						{ return new Symbol(sym.ID, yytext()); }
					case -70:
						break;
					case 70:
						{ return new Symbol(sym.ID, yytext()); }
					case -71:
						break;
					case 71:
						{ return new Symbol(sym.ID, yytext()); }
					case -72:
						break;
					case 72:
						{ return new Symbol(sym.ID, yytext()); }
					case -73:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
}
