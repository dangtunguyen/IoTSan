package edu.ksu.cis.bandera.specification.parser;

/* This file was generated by SableCC (http://www.sablecc.org/). */

import edu.ksu.cis.bandera.specification.lexer.*;
import edu.ksu.cis.bandera.specification.node.*;
import edu.ksu.cis.bandera.specification.analysis.*;
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
	private Token last_token;
	private final TokenIndex converter = new TokenIndex();
	private final int[] action = new int[2];

	private final static int SHIFT = 0;
	private final static int REDUCE = 1;
	private final static int ACCEPT = 2;
	private final static int ERROR = 3;

	private static int[][][] actionTable;
/*      {
			{{-1, REDUCE, 0}, {0, SHIFT, 1}, {19, SHIFT, 2}, {23, SHIFT, 3}, {30, SHIFT, 4}, },
			{{-1, ERROR, 1}, {30, SHIFT, 13}, },
			{{-1, ERROR, 2}, {9, SHIFT, 14}, {14, SHIFT, 15}, {18, SHIFT, 16}, {19, SHIFT, 17}, {29, SHIFT, 18}, {30, SHIFT, 19}, },
			{{-1, ERROR, 3}, {27, SHIFT, 22}, {28, SHIFT, 23}, {30, SHIFT, 24}, },
			{{-1, ERROR, 4}, {10, SHIFT, 28}, },
			{{-1, ERROR, 5}, {31, ACCEPT, -1}, },
			{{-1, REDUCE, 8}, },
			{{-1, REDUCE, 3}, },
			{{-1, REDUCE, 6}, },
			{{-1, REDUCE, 27}, },
			{{-1, REDUCE, 28}, },
			{{-1, REDUCE, 1}, {0, SHIFT, 1}, {23, SHIFT, 3}, {30, SHIFT, 4}, },
			{{-1, REDUCE, 4}, {0, SHIFT, 1}, {30, SHIFT, 4}, },
			{{-1, ERROR, 13}, {10, SHIFT, 32}, },
			{{-1, REDUCE, 17}, },
			{{-1, ERROR, 15}, {30, SHIFT, 33}, },
			{{-1, REDUCE, 15}, },
			{{-1, REDUCE, 9}, },
			{{-1, REDUCE, 16}, },
			{{-1, REDUCE, 14}, },
			{{-1, REDUCE, 12}, },
			{{-1, ERROR, 21}, {9, SHIFT, 14}, {14, SHIFT, 15}, {18, SHIFT, 16}, {19, SHIFT, 34}, {29, SHIFT, 18}, {30, SHIFT, 19}, },
			{{-1, ERROR, 22}, {30, SHIFT, 24}, },
			{{-1, ERROR, 23}, {30, SHIFT, 24}, },
			{{-1, REDUCE, 25}, },
			{{-1, ERROR, 25}, {11, SHIFT, 38}, {18, SHIFT, 39}, },
			{{-1, REDUCE, 21}, },
			{{-1, REDUCE, 24}, {9, SHIFT, 40}, },
			{{-1, ERROR, 28}, {9, SHIFT, 41}, {14, SHIFT, 42}, {18, SHIFT, 43}, {24, SHIFT, 44}, {26, SHIFT, 45}, {29, SHIFT, 46}, {30, SHIFT, 47}, },
			{{-1, REDUCE, 2}, },
			{{-1, REDUCE, 7}, {0, SHIFT, 1}, {30, SHIFT, 4}, },
			{{-1, REDUCE, 5}, },
			{{-1, ERROR, 32}, {9, SHIFT, 41}, {14, SHIFT, 42}, {18, SHIFT, 43}, {24, SHIFT, 52}, {26, SHIFT, 45}, {29, SHIFT, 46}, {30, SHIFT, 47}, },
			{{-1, ERROR, 33}, {15, SHIFT, 55}, },
			{{-1, REDUCE, 10}, },
			{{-1, REDUCE, 11}, },
			{{-1, ERROR, 36}, {11, SHIFT, 56}, {18, SHIFT, 39}, },
			{{-1, ERROR, 37}, {11, SHIFT, 57}, {18, SHIFT, 39}, },
			{{-1, REDUCE, 18}, },
			{{-1, ERROR, 39}, {30, SHIFT, 24}, },
			{{-1, ERROR, 40}, {20, SHIFT, 59}, {30, SHIFT, 60}, },
			{{-1, REDUCE, 56}, },
			{{-1, ERROR, 42}, {8, SHIFT, 61}, {16, SHIFT, 62}, {30, SHIFT, 24}, },
			{{-1, REDUCE, 54}, },
			{{-1, ERROR, 44}, {25, SHIFT, 70}, },
			{{-1, ERROR, 45}, {12, SHIFT, 71}, },
			{{-1, REDUCE, 55}, },
			{{-1, REDUCE, 53}, },
			{{-1, REDUCE, 39}, },
			{{-1, REDUCE, 35}, },
			{{-1, ERROR, 50}, {9, SHIFT, 41}, {11, SHIFT, 72}, {14, SHIFT, 42}, {18, SHIFT, 43}, {29, SHIFT, 46}, {30, SHIFT, 47}, },
			{{-1, ERROR, 51}, {9, SHIFT, 41}, {14, SHIFT, 42}, {18, SHIFT, 43}, {26, SHIFT, 45}, {29, SHIFT, 46}, {30, SHIFT, 47}, },
			{{-1, ERROR, 52}, {25, SHIFT, 76}, },
			{{-1, ERROR, 53}, {9, SHIFT, 41}, {11, SHIFT, 77}, {14, SHIFT, 42}, {18, SHIFT, 43}, {29, SHIFT, 46}, {30, SHIFT, 47}, },
			{{-1, ERROR, 54}, {9, SHIFT, 41}, {14, SHIFT, 42}, {18, SHIFT, 43}, {26, SHIFT, 45}, {29, SHIFT, 46}, {30, SHIFT, 47}, },
			{{-1, REDUCE, 13}, },
			{{-1, REDUCE, 20}, },
			{{-1, REDUCE, 19}, },
			{{-1, REDUCE, 22}, },
			{{-1, REDUCE, 23}, },
			{{-1, REDUCE, 26}, },
			{{-1, ERROR, 61}, {16, SHIFT, 62}, {30, SHIFT, 24}, },
			{{-1, ERROR, 62}, {8, SHIFT, 61}, {16, SHIFT, 62}, {30, SHIFT, 24}, },
			{{-1, ERROR, 63}, {9, SHIFT, 81}, {16, SHIFT, 82}, },
			{{-1, ERROR, 64}, {15, SHIFT, 83}, },
			{{-1, REDUCE, 57}, {5, SHIFT, 84}, },
			{{-1, REDUCE, 60}, {7, SHIFT, 85}, },
			{{-1, REDUCE, 62}, {6, SHIFT, 86}, },
			{{-1, REDUCE, 64}, },
			{{-1, REDUCE, 66}, },
			{{-1, ERROR, 70}, {14, SHIFT, 87}, },
			{{-1, ERROR, 71}, {30, SHIFT, 88}, },
			{{-1, REDUCE, 33}, },
			{{-1, REDUCE, 34}, },
			{{-1, REDUCE, 38}, },
			{{-1, ERROR, 75}, {9, SHIFT, 41}, {11, SHIFT, 90}, {14, SHIFT, 42}, {18, SHIFT, 43}, {29, SHIFT, 46}, {30, SHIFT, 47}, },
			{{-1, ERROR, 76}, {14, SHIFT, 91}, },
			{{-1, REDUCE, 36}, },
			{{-1, ERROR, 78}, {9, SHIFT, 41}, {11, SHIFT, 92}, {14, SHIFT, 42}, {18, SHIFT, 43}, {29, SHIFT, 46}, {30, SHIFT, 47}, },
			{{-1, REDUCE, 67}, },
			{{-1, ERROR, 80}, {17, SHIFT, 93}, },
			{{-1, ERROR, 81}, {30, SHIFT, 60}, },
			{{-1, ERROR, 82}, {17, SHIFT, 94}, {30, SHIFT, 95}, },
			{{-1, REDUCE, 52}, },
			{{-1, ERROR, 84}, {8, SHIFT, 61}, {16, SHIFT, 62}, {30, SHIFT, 24}, },
			{{-1, ERROR, 85}, {8, SHIFT, 61}, {16, SHIFT, 62}, {30, SHIFT, 24}, },
			{{-1, ERROR, 86}, {8, SHIFT, 61}, {16, SHIFT, 62}, {30, SHIFT, 24}, },
			{{-1, ERROR, 87}, {30, SHIFT, 24}, },
			{{-1, REDUCE, 50}, },
			{{-1, ERROR, 89}, {3, SHIFT, 102}, {10, SHIFT, 103}, {18, SHIFT, 104}, },
			{{-1, REDUCE, 37}, },
			{{-1, ERROR, 91}, {30, SHIFT, 24}, },
			{{-1, REDUCE, 40}, },
			{{-1, REDUCE, 70}, },
			{{-1, REDUCE, 68}, },
			{{-1, REDUCE, 58}, },
			{{-1, ERROR, 96}, {17, SHIFT, 107}, {18, SHIFT, 108}, },
			{{-1, REDUCE, 61}, {7, SHIFT, 85}, },
			{{-1, REDUCE, 63}, {6, SHIFT, 86}, },
			{{-1, REDUCE, 65}, },
			{{-1, REDUCE, 31}, {9, SHIFT, 81}, },
			{{-1, ERROR, 101}, {15, SHIFT, 109}, {18, SHIFT, 110}, },
			{{-1, REDUCE, 43}, },
			{{-1, REDUCE, 42}, },
			{{-1, ERROR, 104}, {30, SHIFT, 111}, },
			{{-1, ERROR, 105}, {16, SHIFT, 112}, {30, SHIFT, 24}, },
			{{-1, ERROR, 106}, {15, SHIFT, 116}, {18, SHIFT, 110}, },
			{{-1, REDUCE, 69}, },
			{{-1, ERROR, 108}, {30, SHIFT, 117}, },
			{{-1, ERROR, 109}, {11, SHIFT, 118}, },
			{{-1, ERROR, 110}, {30, SHIFT, 24}, },
			{{-1, REDUCE, 51}, },
			{{-1, ERROR, 112}, {16, SHIFT, 112}, {30, SHIFT, 24}, },
			{{-1, REDUCE, 46}, {9, SHIFT, 81}, },
			{{-1, ERROR, 114}, {13, SHIFT, 121}, },
			{{-1, REDUCE, 44}, {21, SHIFT, 122}, {22, SHIFT, 123}, },
			{{-1, ERROR, 116}, {11, SHIFT, 125}, },
			{{-1, REDUCE, 59}, },
			{{-1, REDUCE, 29}, },
			{{-1, REDUCE, 32}, {9, SHIFT, 81}, },
			{{-1, ERROR, 120}, {17, SHIFT, 126}, },
			{{-1, ERROR, 121}, {9, SHIFT, 127}, },
			{{-1, REDUCE, 49}, },
			{{-1, REDUCE, 48}, },
			{{-1, ERROR, 124}, {16, SHIFT, 112}, {30, SHIFT, 24}, },
			{{-1, REDUCE, 30}, },
			{{-1, REDUCE, 47}, },
			{{-1, REDUCE, 41}, },
			{{-1, REDUCE, 45}, },
		};*/
	private static int[][][] gotoTable;
/*      {
			{{-1, 5}, },
			{{-1, 6}, },
			{{-1, 20}, {21, 35}, },
			{{-1, 7}, {11, 29}, },
			{{-1, 25}, {22, 36}, {23, 37}, },
			{{-1, 26}, {39, 58}, },
			{{-1, 63}, {3, 27}, {22, 27}, {23, 27}, {39, 27}, {87, 100}, {91, 100}, {105, 113}, {110, 119}, {112, 113}, {124, 113}, },
			{{-1, 8}, {12, 31}, {30, 31}, },
			{{-1, 9}, },
			{{-1, 101}, {91, 106}, },
			{{-1, 10}, },
			{{-1, 48}, {51, 74}, {54, 74}, },
			{{-1, 105}, },
			{{-1, 114}, {112, 120}, {124, 128}, },
			{{-1, 115}, },
			{{-1, 124}, },
			{{-1, 89}, },
			{{-1, 49}, {50, 73}, {53, 73}, {75, 73}, {78, 73}, },
			{{-1, 64}, {62, 80}, },
			{{-1, 96}, },
			{{-1, 65}, },
			{{-1, 66}, {84, 97}, },
			{{-1, 67}, {85, 98}, },
			{{-1, 68}, {86, 99}, },
			{{-1, 69}, {61, 79}, },
			{{-1, -1}, },
			{{-1, 11}, },
			{{-1, 12}, {11, 30}, },
			{{-1, 21}, },
			{{-1, 50}, {32, 53}, {51, 75}, {54, 78}, },
			{{-1, 51}, {32, 54}, },
		};*/
	private static String[] errorMessages;
/*      {
			"expecting: documentation comment, '"', 'import', id, EOF",
			"expecting: id",
			"expecting: '.', '{', ',', '"', number, id",
			"expecting: 'predicate', 'assertion', id",
			"expecting: ':'",
			"expecting: EOF",
			"expecting: documentation comment, 'import', id, EOF",
			"expecting: documentation comment, id, EOF",
			"expecting: '.', ';', ']', '}', '(', ')', ',', '+', '-'",
			"expecting: ';', ','",
			"expecting: '.', ';', ','",
			"expecting: '.', '{', ',', 'enable', 'forall', number, id",
			"expecting: '}'",
			"expecting: '*', id",
			"expecting: '.', ';', '{', ',', number, id",
			"expecting: '!', '(', id",
			"expecting: 'assertions'",
			"expecting: '['",
			"expecting: '.', '{', ',', 'forall', number, id",
			"expecting: '(', id",
			"expecting: '.', '('",
			"expecting: '->', '}', ')'",
			"expecting: '->', '||', '}', ')'",
			"expecting: '->', '&&', '||', '}', ')'",
			"expecting: '{'",
			"expecting: ')'",
			"expecting: ')', id",
			"expecting: '<:', ':', ','",
			"expecting: ')', ','",
			"expecting: '.', '}', ','",
			"expecting: '}', ','",
			"expecting: ';'",
			"expecting: '.', ']', ')', '+', '-'",
			"expecting: ']'",
			"expecting: ']', ')', '+', '-'",
			"expecting: '.'",
			"expecting: ']', ')'",
		};*/
	private static int[] errors;
/*      {
			0, 1, 2, 3, 4, 5, 5, 6, 7, 7, 7, 6, 7, 4, 2, 1, 2, 5, 2, 2, 2, 2, 1, 1, 8, 9, 9, 10, 11, 6, 7, 7, 11, 12, 5, 2, 9, 9, 6, 1, 13, 14, 15, 14, 16, 17, 14, 14, 18, 14, 14, 18, 16, 14, 18, 2, 6, 6, 9, 9, 8, 19, 15, 20, 12, 21, 22, 23, 23, 23, 24, 1, 7, 14, 18, 14, 24, 7, 14, 23, 25, 1, 26, 14, 15, 15, 15, 1, 27, 27, 7, 1, 7, 23, 23, 28, 28, 22, 23, 23, 29, 30, 19, 19, 1, 19, 30, 23, 1, 31, 1, 27, 19, 32, 33, 34, 31, 28, 7, 29, 25, 35, 19, 19, 19, 7, 34, 18, 36, 
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
				throw new RuntimeException("The file \"parser.dat\" is either missing or corrupted.");
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
		XPDef node2 = null;
		XPImport node1 = null;
		APropertiesUnit node = new APropertiesUnit(node1, node2);
		return node;
	}
	Node new1()
	{
		XPDef node2 = null;
		XPImport node1 = (XPImport) pop();
		APropertiesUnit node = new APropertiesUnit(node1, node2);
		return node;
	}
	Node new10()
	{
		TDoubleQuote node3 = (TDoubleQuote) pop();
		XPFormatWord node2 = (XPFormatWord) pop();
		TDoubleQuote node1 = (TDoubleQuote) pop();
		AFormat node = new AFormat(node1, node2, node3);
		return node;
	}
	Node new11()
	{
		PFormatWord node2 = (PFormatWord) pop();
		XPFormatWord node1 = (XPFormatWord) pop();
		X1PFormatWord node = new X1PFormatWord(node1, node2);
		return node;
	}
	Node new12()
	{
		PFormatWord node1 = (PFormatWord) pop();
		X2PFormatWord node = new X2PFormatWord(node1);
		return node;
	}
	Node new13()
	{
		TRBrace node3 = (TRBrace) pop();
		TId node2 = (TId) pop();
		TLBrace node1 = (TLBrace) pop();
		AHoleFormatWord node = new AHoleFormatWord(node1, node2, node3);
		return node;
	}
	Node new14()
	{
		TId node1 = (TId) pop();
		AIdFormatWord node = new AIdFormatWord(node1);
		return node;
	}
	Node new15()
	{
		TComma node1 = (TComma) pop();
		ACommaFormatWord node = new ACommaFormatWord(node1);
		return node;
	}
	Node new16()
	{
		TNumber node1 = (TNumber) pop();
		ANumberFormatWord node = new ANumberFormatWord(node1);
		return node;
	}
	Node new17()
	{
		TDot node1 = (TDot) pop();
		ADotFormatWord node = new ADotFormatWord(node1);
		return node;
	}
	Node new18()
	{
		TSemicolon node3 = (TSemicolon) pop();
		PImportNames node2 = (PImportNames) pop();
		TImport node1 = (TImport) pop();
		ATypeImport node = new ATypeImport(node1, node2, node3);
		return node;
	}
	Node new19()
	{
		TSemicolon node4 = (TSemicolon) pop();
		PImportNames node3 = (PImportNames) pop();
		TAssertion node2 = (TAssertion) pop();
		TImport node1 = (TImport) pop();
		AAssertionImport node = new AAssertionImport(node1, node2, node3, node4);
		return node;
	}
	Node new2()
	{
		PImport node2 = (PImport) pop();
		XPImport node1 = (XPImport) pop();
		X1PImport node = new X1PImport(node1, node2);
		return node;
	}
	Node new20()
	{
		TSemicolon node4 = (TSemicolon) pop();
		PImportNames node3 = (PImportNames) pop();
		TPredicate node2 = (TPredicate) pop();
		TImport node1 = (TImport) pop();
		APredicateImport node = new APredicateImport(node1, node2, node3, node4);
		return node;
	}
	Node new21()
	{
		PImportName node1 = (PImportName) pop();
		ANameImportNames node = new ANameImportNames(node1);
		return node;
	}
	Node new22()
	{
		PImportName node3 = (PImportName) pop();
		TComma node2 = (TComma) pop();
		PImportNames node1 = (PImportNames) pop();
		ANamesImportNames node = new ANamesImportNames(node1, node2, node3);
		return node;
	}
	Node new23()
	{
		TStar node3 = (TStar) pop();
		TDot node2 = (TDot) pop();
		PName node1 = (PName) pop();
		AOnDemandImportName node = new AOnDemandImportName(node1, node2, node3);
		return node;
	}
	Node new24()
	{
		PName node1 = (PName) pop();
		ASpecificImportName node = new ASpecificImportName(node1);
		return node;
	}
	Node new25()
	{
		TId node1 = (TId) pop();
		ASimpleName node = new ASimpleName(node1);
		return node;
	}
	Node new26()
	{
		TId node3 = (TId) pop();
		TDot node2 = (TDot) pop();
		PName node1 = (PName) pop();
		AQualifiedName node = new AQualifiedName(node1, node2, node3);
		return node;
	}
	Node new27()
	{
		PAssert node1 = (PAssert) pop();
		AAssertDef node = new AAssertDef(node1);
		return node;
	}
	Node new28()
	{
		PTl node1 = (PTl) pop();
		ATlDef node = new ATlDef(node1);
		return node;
	}
	Node new29()
	{
		TSemicolon node9 = (TSemicolon) pop();
		TRBrace node8 = (TRBrace) pop();
		PNames node7 = (PNames) pop();
		TLBrace node6 = (TLBrace) pop();
		TAssertions node5 = (TAssertions) pop();
		TEnable node4 = (TEnable) pop();
		TColon node3 = (TColon) pop();
		TId node2 = (TId) pop();
		TDocumentationComment node1 = null;
		AAssert node = new AAssert(node1, node2, node3, node4, node5, node6, node7, node8, node9);
		return node;
	}
	Node new3()
	{
		PImport node1 = (PImport) pop();
		X2PImport node = new X2PImport(node1);
		return node;
	}
	Node new30()
	{
		TSemicolon node9 = (TSemicolon) pop();
		TRBrace node8 = (TRBrace) pop();
		PNames node7 = (PNames) pop();
		TLBrace node6 = (TLBrace) pop();
		TAssertions node5 = (TAssertions) pop();
		TEnable node4 = (TEnable) pop();
		TColon node3 = (TColon) pop();
		TId node2 = (TId) pop();
		TDocumentationComment node1 = (TDocumentationComment) pop();
		AAssert node = new AAssert(node1, node2, node3, node4, node5, node6, node7, node8, node9);
		return node;
	}
	Node new31()
	{
		PName node1 = (PName) pop();
		ANameNames node = new ANameNames(node1);
		return node;
	}
	Node new32()
	{
		PName node3 = (PName) pop();
		TComma node2 = (TComma) pop();
		PNames node1 = (PNames) pop();
		ANamesNames node = new ANamesNames(node1, node2, node3);
		return node;
	}
	Node new33()
	{
		TSemicolon node6 = (TSemicolon) pop();
		XPWord node5 = (XPWord) pop();
		XPQtl node4 = null;
		TColon node3 = (TColon) pop();
		TId node2 = (TId) pop();
		TDocumentationComment node1 = null;
		ATl node = new ATl(node1, node2, node3, node4, node5, node6);
		return node;
	}
	Node new34()
	{
		PWord node2 = (PWord) pop();
		XPWord node1 = (XPWord) pop();
		X1PWord node = new X1PWord(node1, node2);
		return node;
	}
	Node new35()
	{
		PWord node1 = (PWord) pop();
		X2PWord node = new X2PWord(node1);
		return node;
	}
	Node new36()
	{
		TSemicolon node6 = (TSemicolon) pop();
		XPWord node5 = (XPWord) pop();
		XPQtl node4 = null;
		TColon node3 = (TColon) pop();
		TId node2 = (TId) pop();
		TDocumentationComment node1 = (TDocumentationComment) pop();
		ATl node = new ATl(node1, node2, node3, node4, node5, node6);
		return node;
	}
	Node new37()
	{
		TSemicolon node6 = (TSemicolon) pop();
		XPWord node5 = (XPWord) pop();
		XPQtl node4 = (XPQtl) pop();
		TColon node3 = (TColon) pop();
		TId node2 = (TId) pop();
		TDocumentationComment node1 = null;
		ATl node = new ATl(node1, node2, node3, node4, node5, node6);
		return node;
	}
	Node new38()
	{
		PQtl node2 = (PQtl) pop();
		XPQtl node1 = (XPQtl) pop();
		X1PQtl node = new X1PQtl(node1, node2);
		return node;
	}
	Node new39()
	{
		PQtl node1 = (PQtl) pop();
		X2PQtl node = new X2PQtl(node1);
		return node;
	}
	Node new4()
	{
		XPDef node2 = (XPDef) pop();
		XPImport node1 = null;
		APropertiesUnit node = new APropertiesUnit(node1, node2);
		return node;
	}
	Node new40()
	{
		TSemicolon node6 = (TSemicolon) pop();
		XPWord node5 = (XPWord) pop();
		XPQtl node4 = (XPQtl) pop();
		TColon node3 = (TColon) pop();
		TId node2 = (TId) pop();
		TDocumentationComment node1 = (TDocumentationComment) pop();
		ATl node = new ATl(node1, node2, node3, node4, node5, node6);
		return node;
	}
	Node new41()
	{
		TDot node7 = (TDot) pop();
		TRBracket node6 = (TRBracket) pop();
		PTypeExp node5 = (PTypeExp) pop();
		PQtlBinding node4 = (PQtlBinding) pop();
		PIds node3 = (PIds) pop();
		TLBracket node2 = (TLBracket) pop();
		TForall node1 = (TForall) pop();
		AQtl node = new AQtl(node1, node2, node3, node4, node5, node6, node7);
		return node;
	}
	Node new42()
	{
		TColon node1 = (TColon) pop();
		AExactQtlBinding node = new AExactQtlBinding(node1);
		return node;
	}
	Node new43()
	{
		TInstance node1 = (TInstance) pop();
		AInstanceQtlBinding node = new AInstanceQtlBinding(node1);
		return node;
	}
	Node new44()
	{
		PPrimaryTypeExp node1 = (PPrimaryTypeExp) pop();
		APrimaryTypeExp node = new APrimaryTypeExp(node1);
		return node;
	}
	Node new45()
	{
		PTypeExp node3 = (PTypeExp) pop();
		PTypeOp node2 = (PTypeOp) pop();
		PPrimaryTypeExp node1 = (PPrimaryTypeExp) pop();
		AOpTypeExp node = new AOpTypeExp(node1, node2, node3);
		return node;
	}
	Node new46()
	{
		PName node1 = (PName) pop();
		ANamePrimaryTypeExp node = new ANamePrimaryTypeExp(node1);
		return node;
	}
	Node new47()
	{
		TRParen node3 = (TRParen) pop();
		PTypeExp node2 = (PTypeExp) pop();
		TLParen node1 = (TLParen) pop();
		AParenPrimaryTypeExp node = new AParenPrimaryTypeExp(node1, node2, node3);
		return node;
	}
	Node new48()
	{
		TMinus node1 = (TMinus) pop();
		AFilterTypeOp node = new AFilterTypeOp(node1);
		return node;
	}
	Node new49()
	{
		TPlus node1 = (TPlus) pop();
		AUnionTypeOp node = new AUnionTypeOp(node1);
		return node;
	}
	Node new5()
	{
		PDef node2 = (PDef) pop();
		XPDef node1 = (XPDef) pop();
		X1PDef node = new X1PDef(node1, node2);
		return node;
	}
	Node new50()
	{
		TId node1 = (TId) pop();
		AIdIds node = new AIdIds(node1);
		return node;
	}
	Node new51()
	{
		TId node3 = (TId) pop();
		TComma node2 = (TComma) pop();
		PIds node1 = (PIds) pop();
		AIdsIds node = new AIdsIds(node1, node2, node3);
		return node;
	}
	Node new52()
	{
		TRBrace node3 = (TRBrace) pop();
		PExp node2 = (PExp) pop();
		TLBrace node1 = (TLBrace) pop();
		AExpWord node = new AExpWord(node1, node2, node3);
		return node;
	}
	Node new53()
	{
		TId node1 = (TId) pop();
		AIdWord node = new AIdWord(node1);
		return node;
	}
	Node new54()
	{
		TComma node1 = (TComma) pop();
		ACommaWord node = new ACommaWord(node1);
		return node;
	}
	Node new55()
	{
		TNumber node1 = (TNumber) pop();
		ANumberWord node = new ANumberWord(node1);
		return node;
	}
	Node new56()
	{
		TDot node1 = (TDot) pop();
		ADotWord node = new ADotWord(node1);
		return node;
	}
	Node new57()
	{
		PImplicationExp node1 = (PImplicationExp) pop();
		AImplicationExp node = new AImplicationExp(node1);
		return node;
	}
	Node new58()
	{
		TId node1 = (TId) pop();
		AIdArgs node = new AIdArgs(node1);
		return node;
	}
	Node new59()
	{
		TId node3 = (TId) pop();
		TComma node2 = (TComma) pop();
		PArgs node1 = (PArgs) pop();
		AArgsArgs node = new AArgsArgs(node1, node2, node3);
		return node;
	}
	Node new6()
	{
		PDef node1 = (PDef) pop();
		X2PDef node = new X2PDef(node1);
		return node;
	}
	Node new60()
	{
		POrExp node1 = (POrExp) pop();
		AOrImplicationExp node = new AOrImplicationExp(node1);
		return node;
	}
	Node new61()
	{
		POrExp node3 = (POrExp) pop();
		TImply node2 = (TImply) pop();
		PImplicationExp node1 = (PImplicationExp) pop();
		AImplyImplicationExp node = new AImplyImplicationExp(node1, node2, node3);
		return node;
	}
	Node new62()
	{
		PAndExp node1 = (PAndExp) pop();
		AAndOrExp node = new AAndOrExp(node1);
		return node;
	}
	Node new63()
	{
		PAndExp node3 = (PAndExp) pop();
		TOr node2 = (TOr) pop();
		POrExp node1 = (POrExp) pop();
		AOrOrExp node = new AOrOrExp(node1, node2, node3);
		return node;
	}
	Node new64()
	{
		PUnaryExp node1 = (PUnaryExp) pop();
		AUnaryAndExp node = new AUnaryAndExp(node1);
		return node;
	}
	Node new65()
	{
		PUnaryExp node3 = (PUnaryExp) pop();
		TAnd node2 = (TAnd) pop();
		PAndExp node1 = (PAndExp) pop();
		AAndAndExp node = new AAndAndExp(node1, node2, node3);
		return node;
	}
	Node new66()
	{
		PPrimaryExp node1 = (PPrimaryExp) pop();
		APrimaryUnaryExp node = new APrimaryUnaryExp(node1);
		return node;
	}
	Node new67()
	{
		PPrimaryExp node2 = (PPrimaryExp) pop();
		TNot node1 = (TNot) pop();
		AComplementUnaryExp node = new AComplementUnaryExp(node1, node2);
		return node;
	}
	Node new68()
	{
		TRParen node4 = (TRParen) pop();
		PArgs node3 = null;
		TLParen node2 = (TLParen) pop();
		PName node1 = (PName) pop();
		APredicatePrimaryExp node = new APredicatePrimaryExp(node1, node2, node3, node4);
		return node;
	}
	Node new69()
	{
		TRParen node4 = (TRParen) pop();
		PArgs node3 = (PArgs) pop();
		TLParen node2 = (TLParen) pop();
		PName node1 = (PName) pop();
		APredicatePrimaryExp node = new APredicatePrimaryExp(node1, node2, node3, node4);
		return node;
	}
	Node new7()
	{
		XPDef node2 = (XPDef) pop();
		XPImport node1 = (XPImport) pop();
		APropertiesUnit node = new APropertiesUnit(node1, node2);
		return node;
	}
	Node new70()
	{
		TRParen node3 = (TRParen) pop();
		PExp node2 = (PExp) pop();
		TLParen node1 = (TLParen) pop();
		AParenPrimaryExp node = new AParenPrimaryExp(node1, node2, node3);
		return node;
	}
	Node new71()
	{
		TAnd node1 = (TAnd) pop();
		AAndBinOp node = new AAndBinOp(node1);
		return node;
	}
	Node new72()
	{
		TOr node1 = (TOr) pop();
		AOrBinOp node = new AOrBinOp(node1);
		return node;
	}
	Node new73()
	{
		TImply node1 = (TImply) pop();
		AImplyBinOp node = new AImplyBinOp(node1);
		return node;
	}
	Node new8()
	{
		PFormat node1 = (PFormat) pop();
		AFormatUnit node = new AFormatUnit(node1);
		return node;
	}
	Node new9()
	{
		TDoubleQuote node3 = (TDoubleQuote) pop();
		XPFormatWord node2 = null;
		TDoubleQuote node1 = (TDoubleQuote) pop();
		AFormat node = new AFormat(node1, node2, node3);
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
			last_token = lexer.peek();

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
					case 2: { Node node = new2(); push(goTo(26), node, false); } break;
					case 3: { Node node = new3(); push(goTo(26), node, false); } break;
					case 4: { Node node = new4(); push(goTo(0), node, true); } break;
					case 5: { Node node = new5(); push(goTo(27), node, false); } break;
					case 6: { Node node = new6(); push(goTo(27), node, false); } break;
					case 7: { Node node = new7(); push(goTo(0), node, true); } break;
					case 8: { Node node = new8(); push(goTo(0), node, true); } break;
					case 9: { Node node = new9(); push(goTo(1), node, true); } break;
					case 10: { Node node = new10(); push(goTo(1), node, true); } break;
					case 11: { Node node = new11(); push(goTo(28), node, false); } break;
					case 12: { Node node = new12(); push(goTo(28), node, false); } break;
					case 13: { Node node = new13(); push(goTo(2), node, true); } break;
					case 14: { Node node = new14(); push(goTo(2), node, true); } break;
					case 15: { Node node = new15(); push(goTo(2), node, true); } break;
					case 16: { Node node = new16(); push(goTo(2), node, true); } break;
					case 17: { Node node = new17(); push(goTo(2), node, true); } break;
					case 18: { Node node = new18(); push(goTo(3), node, true); } break;
					case 19: { Node node = new19(); push(goTo(3), node, true); } break;
					case 20: { Node node = new20(); push(goTo(3), node, true); } break;
					case 21: { Node node = new21(); push(goTo(4), node, true); } break;
					case 22: { Node node = new22(); push(goTo(4), node, true); } break;
					case 23: { Node node = new23(); push(goTo(5), node, true); } break;
					case 24: { Node node = new24(); push(goTo(5), node, true); } break;
					case 25: { Node node = new25(); push(goTo(6), node, true); } break;
					case 26: { Node node = new26(); push(goTo(6), node, true); } break;
					case 27: { Node node = new27(); push(goTo(7), node, true); } break;
					case 28: { Node node = new28(); push(goTo(7), node, true); } break;
					case 29: { Node node = new29(); push(goTo(8), node, true); } break;
					case 30: { Node node = new30(); push(goTo(8), node, true); } break;
					case 31: { Node node = new31(); push(goTo(9), node, true); } break;
					case 32: { Node node = new32(); push(goTo(9), node, true); } break;
					case 33: { Node node = new33(); push(goTo(10), node, true); } break;
					case 34: { Node node = new34(); push(goTo(29), node, false); } break;
					case 35: { Node node = new35(); push(goTo(29), node, false); } break;
					case 36: { Node node = new36(); push(goTo(10), node, true); } break;
					case 37: { Node node = new37(); push(goTo(10), node, true); } break;
					case 38: { Node node = new38(); push(goTo(30), node, false); } break;
					case 39: { Node node = new39(); push(goTo(30), node, false); } break;
					case 40: { Node node = new40(); push(goTo(10), node, true); } break;
					case 41: { Node node = new41(); push(goTo(11), node, true); } break;
					case 42: { Node node = new42(); push(goTo(12), node, true); } break;
					case 43: { Node node = new43(); push(goTo(12), node, true); } break;
					case 44: { Node node = new44(); push(goTo(13), node, true); } break;
					case 45: { Node node = new45(); push(goTo(13), node, true); } break;
					case 46: { Node node = new46(); push(goTo(14), node, true); } break;
					case 47: { Node node = new47(); push(goTo(14), node, true); } break;
					case 48: { Node node = new48(); push(goTo(15), node, true); } break;
					case 49: { Node node = new49(); push(goTo(15), node, true); } break;
					case 50: { Node node = new50(); push(goTo(16), node, true); } break;
					case 51: { Node node = new51(); push(goTo(16), node, true); } break;
					case 52: { Node node = new52(); push(goTo(17), node, true); } break;
					case 53: { Node node = new53(); push(goTo(17), node, true); } break;
					case 54: { Node node = new54(); push(goTo(17), node, true); } break;
					case 55: { Node node = new55(); push(goTo(17), node, true); } break;
					case 56: { Node node = new56(); push(goTo(17), node, true); } break;
					case 57: { Node node = new57(); push(goTo(18), node, true); } break;
					case 58: { Node node = new58(); push(goTo(19), node, true); } break;
					case 59: { Node node = new59(); push(goTo(19), node, true); } break;
					case 60: { Node node = new60(); push(goTo(20), node, true); } break;
					case 61: { Node node = new61(); push(goTo(20), node, true); } break;
					case 62: { Node node = new62(); push(goTo(21), node, true); } break;
					case 63: { Node node = new63(); push(goTo(21), node, true); } break;
					case 64: { Node node = new64(); push(goTo(22), node, true); } break;
					case 65: { Node node = new65(); push(goTo(22), node, true); } break;
					case 66: { Node node = new66(); push(goTo(23), node, true); } break;
					case 67: { Node node = new67(); push(goTo(23), node, true); } break;
					case 68: { Node node = new68(); push(goTo(24), node, true); } break;
					case 69: { Node node = new69(); push(goTo(24), node, true); } break;
					case 70: { Node node = new70(); push(goTo(24), node, true); } break;
					case 71: { Node node = new71(); push(goTo(25), node, true); } break;
					case 72: { Node node = new72(); push(goTo(25), node, true); } break;
					case 73: { Node node = new73(); push(goTo(25), node, true); } break;
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
					throw new ParserException(last_token,
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