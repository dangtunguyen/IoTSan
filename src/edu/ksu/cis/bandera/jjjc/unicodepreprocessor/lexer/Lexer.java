package edu.ksu.cis.bandera.jjjc.unicodepreprocessor.lexer;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Robby (robby@cis.ksu.edu)              *
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
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.node.*;

public class Lexer
{
	protected Token token;
	protected State state = State.NORMAL;

	private PushbackReader in;
	private int line;
	private int pos;
	private boolean cr;
	private boolean eof;
	private final StringBuffer text = new StringBuffer();

	private static int[][][] gotoTable;
/*  {
		{{0, 25, 1}, {26, 26, 2}, {27, 91, 1}, {92, 92, 3}, {93, 65535, 1}, },
		{},
		{},
		{{92, 92, 4}, {117, 117, 5}, },
		{},
		{{48, 57, 6}, {65, 70, 7}, {97, 102, 8}, {117, 117, 5}, },
		{{48, 57, 9}, {65, 70, 10}, {97, 102, 11}, },
		{{48, 102, -8}, },
		{{48, 102, -8}, },
		{{48, 57, 12}, {65, 70, 13}, {97, 102, 14}, },
		{{48, 102, -11}, },
		{{48, 102, -11}, },
		{{48, 57, 15}, {65, 70, 16}, {97, 102, 17}, },
		{{48, 102, -14}, },
		{{48, 102, -14}, },
		{},
		{},
		{},
	};*/

	private static int[][] accept;
/*  {
		{-1, 4, 3, 4, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, },
		{-1, 4, 3, 4, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, },

	};*/

	public static class State
	{
		public final static State NORMAL = new State(0);
		public final static State SUB = new State(1);

		private int id;

		private State(int id)
		{
			this.id = id;
		}

		public int id()
		{
			return id;
		}
	}
	public Lexer(PushbackReader in)
	{
		this.in = in;

		if(gotoTable == null)
		{
			try
			{
				DataInputStream s = new DataInputStream(
					new BufferedInputStream(
					Lexer.class.getResourceAsStream("lexer.dat")));

				// read gotoTable
				int length = s.readInt();
				gotoTable = new int[length][][];
				for(int i = 0; i < gotoTable.length; i++)
				{
					length = s.readInt();
					gotoTable[i] = new int[length][3];
					for(int j = 0; j < gotoTable[i].length; j++)
					{
						for(int k = 0; k < 3; k++)
						{
							gotoTable[i][j][k] = s.readInt();
						}
					}
				}

				// read accept
				length = s.readInt();
				accept = new int[length][];
				for(int i = 0; i < accept.length; i++)
				{
					length = s.readInt();
					accept[i] = new int[length];
					for(int j = 0; j < accept[i].length; j++)
					{
						accept[i][j] = s.readInt();
					}
				}

				s.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("Unable to read lexer.dat.");
			}
		}
	}
	protected void filter() throws LexerException, IOException
	{
	}
	private int getChar() throws IOException
	{
		if(eof)
		{
			return -1;
		}

		int result = in.read();

		if(result == -1)
		{
			eof = true;
		}

		return result;
	}
	private String getText(int acceptLength)
	{
		StringBuffer s = new StringBuffer(acceptLength);
		for(int i = 0; i < acceptLength; i++)
		{
			s.append(text.charAt(i));
		}

		return s.toString();
	}
	protected Token getToken() throws IOException, LexerException
	{
		int dfa_state = 0;

		int start_pos = pos;
		int start_line = line;

		int accept_state = -1;
		int accept_token = -1;
		int accept_length = -1;
		int accept_pos = -1;
		int accept_line = -1;

		text.setLength(0);

		while(true)
		{
			int c = getChar();

			if(c != -1)
			{
				switch(c)
				{
				case 10:
					if(cr)
					{
						cr = false;
					}
					else
					{
						line++;
						pos = 0;
					}
					break;
				case 13:
					line++;
					pos = 0;
					cr = true;
					break;
				default:
					pos++;
					cr = false;
					break;
				};

				text.append((char) c);

				do
				{
					int oldState = (dfa_state < -1) ? (-2 -dfa_state) : dfa_state;

					dfa_state = -1;

					int low = 0;
					int high = gotoTable[oldState].length - 1;

					while(low <= high)
					{
						int middle = (low + high) / 2;

						if(c < gotoTable[oldState][middle][0])
						{
							high = middle - 1;
						}
						else if(c > gotoTable[oldState][middle][1])
						{
							low = middle + 1;
						}
						else
						{
							dfa_state = gotoTable[oldState][middle][2];
							break;
						}
					}
				}while(dfa_state < -1);
			}
			else
			{
				dfa_state = -1;
			}

			if(dfa_state >= 0)
			{
				if(accept[state.id()][dfa_state] != -1)
				{
					accept_state = dfa_state;
					accept_token = accept[state.id()][dfa_state];
					accept_length = text.length();
					accept_pos = pos;
					accept_line = line;
				}
			}
			else
			{
				if(accept_state != -1)
				{
					switch(accept_token)
					{
					case 0:
						{
							Token token = new0(
								start_line + 1,
								start_pos + 1);
							pushBack(accept_length);
							pos = accept_pos;
							line = accept_line;
							return token;
						}
					case 1:
						{
							Token token = new1(
								getText(accept_length),
								start_line + 1,
								start_pos + 1);
							pushBack(accept_length);
							pos = accept_pos;
							line = accept_line;
							return token;
						}
					case 2:
						{
							Token token = new2(
								getText(accept_length),
								start_line + 1,
								start_pos + 1);
							pushBack(accept_length);
							pos = accept_pos;
							line = accept_line;
							return token;
						}
					case 3:
						{
							Token token = new3(
								getText(accept_length),
								start_line + 1,
								start_pos + 1);
							pushBack(accept_length);
							pos = accept_pos;
							line = accept_line;
							switch(state.id())
							{
								case 0: state = State.SUB; break;
								case 1: state = State.SUB; break;
							}
							return token;
						}
					case 4:
						{
							Token token = new4(
								getText(accept_length),
								start_line + 1,
								start_pos + 1);
							pushBack(accept_length);
							pos = accept_pos;
							line = accept_line;
							return token;
						}
					}
				}
				else
				{
					if(text.length() > 0)
					{
						throw new LexerException(
							"[" + (start_line + 1) + "," + (start_pos + 1) + "]" +
							" Unknown token: " + text);
					}
					else
					{
						EOF token = new EOF(
							start_line + 1,
							start_pos + 1);
						return token;
					}
				}
			}
		}
	}
	Token new0(int line, int pos) { return new TEvenBackslash(line, pos); }
	Token new1(String text, int line, int pos) { return new TUnicodeEscape(text, line, pos); }
	Token new2(String text, int line, int pos) { return new TErroneousEscape(text, line, pos); }
	Token new3(String text, int line, int pos) { return new TSub(text, line, pos); }
	Token new4(String text, int line, int pos) { return new TRawInputCharacter(text, line, pos); }
	public Token next() throws LexerException, IOException
	{
		while(token == null)
		{
			token = getToken();
			filter();
		}

		Token result = token;
		token = null;
		return result;
	}
	public Token peek() throws LexerException, IOException
	{
		while(token == null)
		{
			token = getToken();
			filter();
		}

		return token;
	}
	private void pushBack(int acceptLength) throws IOException
	{
		int length = text.length();
		for(int i = length - 1; i >= acceptLength; i--)
		{
			eof = false;

			in.unread(text.charAt(i));
		}
	}
	protected void unread(Token token) throws IOException
	{
		String text = token.getText();
		int length = text.length();

		for(int i = length - 1; i >= 0; i--)
		{
			eof = false;

			in.unread(text.charAt(i));
		}

		pos = token.getPos() - 1;
		line = token.getLine() - 1;
	}
}
