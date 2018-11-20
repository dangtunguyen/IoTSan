package edu.ksu.cis.bandera.bui.session.parser;

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
/* This file was generated by SableCC (http://www.sable.mcgill.ca/sablecc/). */

import edu.ksu.cis.bandera.bui.session.lexer.*;
import edu.ksu.cis.bandera.bui.session.node.*;
import edu.ksu.cis.bandera.bui.session.analysis.*;
import java.util.*;

import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

public class Parser
{
	public final Analysis ignoredTokens = new AnalysisAdapter();

	protected Node node;

	private final Lexer lexer;
	private final ListIterator stack = new LinkedList().listIterator();
	private int last_shift;
	private int last_pos;
	private int last_line;
	private final TokenIndex converter = new TokenIndex();
	private final int[] action = new int[2];

	private final static int SHIFT = 0;
	private final static int REDUCE = 1;
	private final static int ACCEPT = 2;
	private final static int ERROR = 3;

	private static int[][][] actionTable;
/*      {
			{{-1, REDUCE, 0}, {4, SHIFT, 1}, },
			{{-1, ERROR, 1}, {6, SHIFT, 5}, },
			{{-1, ERROR, 2}, {7, ACCEPT, -1}, },
			{{-1, REDUCE, 3}, },
			{{-1, REDUCE, 1}, {4, SHIFT, 1}, },
			{{-1, ERROR, 5}, {0, SHIFT, 7}, },
			{{-1, REDUCE, 2}, },
			{{-1, ERROR, 7}, {1, SHIFT, 8}, {6, SHIFT, 9}, },
			{{-1, REDUCE, 4}, },
			{{-1, ERROR, 9}, {3, SHIFT, 12}, },
			{{-1, REDUCE, 7}, },
			{{-1, ERROR, 11}, {1, SHIFT, 13}, {6, SHIFT, 9}, },
			{{-1, ERROR, 12}, {5, SHIFT, 15}, },
			{{-1, REDUCE, 5}, },
			{{-1, REDUCE, 6}, },
			{{-1, REDUCE, 9}, },
			{{-1, REDUCE, 8}, {2, SHIFT, 17}, },
			{{-1, ERROR, 17}, {5, SHIFT, 18}, },
			{{-1, REDUCE, 10}, },
		};*/
	private static int[][][] gotoTable;
/*      {
			{{-1, 2}, },
			{{-1, 3}, {4, 6}, },
			{{-1, 10}, {11, 14}, },
			{{-1, 16}, },
			{{-1, 4}, },
			{{-1, 11}, },
		};*/
	private static String[] errorMessages;
/*      {
			"TSession EOF expected.",
			"TId expected.",
			"EOF expected.",
			"TLBrace expected.",
			"TRBrace TId expected.",
			"TEqual expected.",
			"TStringLiteral expected.",
			"TRBrace TPlus TId expected.",
		};*/
	private static int[] errors;
/*      {
			0, 1, 2, 0, 0, 3, 0, 4, 0, 5, 4, 4, 6, 0, 4, 7, 7, 6, 7, 
		};*/
	public Parser(Lexer lexer)
	{
		this.lexer = lexer;

		if(actionTable == null)
		{
			try
			{
				DataInputStream s = new DataInputStream(
					new BufferedInputStream(
					Parser.class.getResourceAsStream("parser.dat")));

				// read actionTable
				int length = s.readInt();
				actionTable = new int[length][][];
				for(int i = 0; i < actionTable.length; i++)
				{
					length = s.readInt();
					actionTable[i] = new int[length][3];
					for(int j = 0; j < actionTable[i].length; j++)
					{
						for(int k = 0; k < 3; k++)
						{
							actionTable[i][j][k] = s.readInt();
						}
					}
				}

				// read gotoTable
				length = s.readInt();
				gotoTable = new int[length][][];
				for(int i = 0; i < gotoTable.length; i++)
				{
					length = s.readInt();
					gotoTable[i] = new int[length][2];
					for(int j = 0; j < gotoTable[i].length; j++)
					{
						for(int k = 0; k < 2; k++)
						{
							gotoTable[i][j][k] = s.readInt();
						}
					}
				}

				// read errorMessages
				length = s.readInt();
				errorMessages = new String[length];
				for(int i = 0; i < errorMessages.length; i++)
				{
					length = s.readInt();
					StringBuffer buffer = new StringBuffer();

					for(int j = 0; j < length; j++)
					{
						buffer.append(s.readChar());
					}
					errorMessages[i] = buffer.toString();
				}

				// read errors
				length = s.readInt();
				errors = new int[length];
				for(int i = 0; i < errors.length; i++)
				{
					errors[i] = s.readInt();
				}

				s.close();
			}
			catch(Exception e)
			{
				throw new RuntimeException("Unable to read parser.dat.");
			}
		}
	}
	protected void filter() throws ParserException, LexerException, IOException
	{
	}
	private int goTo(int index)
	{
		int state = state();
		int low = 1;
		int high = gotoTable[index].length - 1;
		int value = gotoTable[index][0][1];

		while(low <= high)
		{
			int middle = (low + high) / 2;

			if(state < gotoTable[index][middle][0])
			{
				high = middle - 1;
			}
			else if(state > gotoTable[index][middle][0])
			{
				low = middle + 1;
			}
			else
			{
				value = gotoTable[index][middle][1];
				break;
			}
		}

		return value;
	}
	private int index(Switchable token)
	{
		converter.index = -1;
		token.apply(converter);
		return converter.index;
	}
	Node new0()
	{
		XPSession node1 = null;
		AUnit node = new AUnit(node1);
		return node;
	}
	Node new1()
	{
		XPSession node1 = (XPSession) pop();
		AUnit node = new AUnit(node1);
		return node;
	}
	Node new10()
	{
		TStringLiteral node3 = (TStringLiteral) pop();
		TPlus node2 = (TPlus) pop();
		PStrings node1 = (PStrings) pop();
		AStringsStrings node = new AStringsStrings(node1, node2, node3);
		return node;
	}
	Node new2()
	{
		PSession node2 = (PSession) pop();
		XPSession node1 = (XPSession) pop();
		X1PSession node = new X1PSession(node1, node2);
		return node;
	}
	Node new3()
	{
		PSession node1 = (PSession) pop();
		X2PSession node = new X2PSession(node1);
		return node;
	}
	Node new4()
	{
		TRBrace node5 = (TRBrace) pop();
		XPResource node4 = null;
		TLBrace node3 = (TLBrace) pop();
		TId node2 = (TId) pop();
		TSession node1 = (TSession) pop();
		ASession node = new ASession(node1, node2, node3, node4, node5);
		return node;
	}
	Node new5()
	{
		TRBrace node5 = (TRBrace) pop();
		XPResource node4 = (XPResource) pop();
		TLBrace node3 = (TLBrace) pop();
		TId node2 = (TId) pop();
		TSession node1 = (TSession) pop();
		ASession node = new ASession(node1, node2, node3, node4, node5);
		return node;
	}
	Node new6()
	{
		PResource node2 = (PResource) pop();
		XPResource node1 = (XPResource) pop();
		X1PResource node = new X1PResource(node1, node2);
		return node;
	}
	Node new7()
	{
		PResource node1 = (PResource) pop();
		X2PResource node = new X2PResource(node1);
		return node;
	}
	Node new8()
	{
		PStrings node3 = (PStrings) pop();
		TEqual node2 = (TEqual) pop();
		TId node1 = (TId) pop();
		AStringResource node = new AStringResource(node1, node2, node3);
		return node;
	}
	Node new9()
	{
		TStringLiteral node1 = (TStringLiteral) pop();
		AStringStrings node = new AStringStrings(node1);
		return node;
	}
	public Start parse() throws ParserException, LexerException, IOException
	{
		push(0, null, false);

		List ign = null;
		while(true)
		{
			while(index(lexer.peek()) == -1)
			{
				if(ign == null)
				{
					ign = new TypedLinkedList(NodeCast.instance);
				}

				ign.add(lexer.next());
			}

			if(ign != null)
			{
				ignoredTokens.setIn(lexer.peek(), ign);
				ign = null;
			}

			last_pos = lexer.peek().getPos();
			last_line = lexer.peek().getLine();

			int index = index(lexer.peek());
			action[0] = actionTable[state()][0][1];
			action[1] = actionTable[state()][0][2];

			int low = 1;
			int high = actionTable[state()].length - 1;

			while(low <= high)
			{
				int middle = (low + high) / 2;

				if(index < actionTable[state()][middle][0])
				{
					high = middle - 1;
				}
				else if(index > actionTable[state()][middle][0])
				{
					low = middle + 1;
				}
				else
				{
					action[0] = actionTable[state()][middle][1];
					action[1] = actionTable[state()][middle][2];
					break;
				}
			}

			switch(action[0])
			{
				case SHIFT:
					push(action[1], lexer.next(), true);
					last_shift = action[1];
					break;
				case REDUCE:
					switch(action[1])
					{
					case 0: { Node node = new0(); push(goTo(0), node, true); } break;
					case 1: { Node node = new1(); push(goTo(0), node, true); } break;
					case 2: { Node node = new2(); push(goTo(4), node, false); } break;
					case 3: { Node node = new3(); push(goTo(4), node, false); } break;
					case 4: { Node node = new4(); push(goTo(1), node, true); } break;
					case 5: { Node node = new5(); push(goTo(1), node, true); } break;
					case 6: { Node node = new6(); push(goTo(5), node, false); } break;
					case 7: { Node node = new7(); push(goTo(5), node, false); } break;
					case 8: { Node node = new8(); push(goTo(2), node, true); } break;
					case 9: { Node node = new9(); push(goTo(3), node, true); } break;
					case 10: { Node node = new10(); push(goTo(3), node, true); } break;
					}
					break;
				case ACCEPT:
					{
						EOF node2 = (EOF) lexer.next();
						PUnit node1 = (PUnit) pop();
						Start node = new Start(node1, node2);
						return node;
					}
				case ERROR:
					throw new ParserException(
						"[" + last_line + "," + last_pos + "] " +
						errorMessages[errors[action[1]]]);
			}
		}
	}
	private Node pop()
	{
		return (Node) ((State) stack.previous()).node;
	}
	private void push(int state, Node node, boolean filter) throws ParserException, LexerException, IOException
	{
		this.node = node;

		if(filter)
		{
			filter();
		}

		if(!stack.hasNext())
		{
			stack.add(new State(state, this.node));
			return;
		}

		State s = (State) stack.next();
		s.state = state;
		s.node = this.node;
	}
	private int state()
	{
		State s = (State) stack.previous();
		stack.next();
		return s.state;
	}
}