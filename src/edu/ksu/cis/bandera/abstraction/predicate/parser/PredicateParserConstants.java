package edu.ksu.cis.bandera.abstraction.predicate.parser;

/* Generated By:JJTree&JavaCC: Do not edit this line. PredicateParserConstants.java */
public interface PredicateParserConstants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 9;
  int FORMAL_COMMENT = 10;
  int MULTI_LINE_COMMENT = 11;
  int BOOLEAN = 13;
  int BYTE = 14;
  int CHAR = 15;
  int CLASS = 16;
  int DOUBLE = 17;
  int FALSE = 18;
  int FLOAT = 19;
  int INSTANCEOF = 20;
  int INT = 21;
  int LONG = 22;
  int NULL = 23;
  int SUPER = 24;
  int SHORT = 25;
  int THIS = 26;
  int TRUE = 27;
  int PREDICATE = 28;
  int METHOD = 29;
  int EXPR = 30;
  int GLOBAL = 31;
  int INTEGER_LITERAL = 32;
  int DECIMAL_LITERAL = 33;
  int HEX_LITERAL = 34;
  int OCTAL_LITERAL = 35;
  int FLOATING_POINT_LITERAL = 36;
  int EXPONENT = 37;
  int CHARACTER_LITERAL = 38;
  int STRING_LITERAL = 39;
  int NON_ESCAPED_LITERAL = 41;
  int ESCAPE_SEQ = 42;
  int ESCAPE_OCTAL = 43;
  int ESCAPE_UNICODE = 44;
  int IDENTIFIER = 46;
  int LETTER = 47;
  int DIGIT = 48;
  int LPAREN = 49;
  int RPAREN = 50;
  int LBRACE = 51;
  int RBRACE = 52;
  int LBRACKET = 53;
  int RBRACKET = 54;
  int SEMICOLON = 55;
  int COMMA = 56;
  int DOT = 57;
  int ASSIGN = 58;
  int GT = 59;
  int LT = 60;
  int BANG = 61;
  int TILDE = 62;
  int EQ = 63;
  int LE = 64;
  int GE = 65;
  int NE = 66;
  int SC_OR = 67;
  int SC_AND = 68;
  int INCR = 69;
  int DECR = 70;
  int PLUS = 71;
  int MINUS = 72;
  int STAR = 73;
  int SLASH = 74;
  int BIT_AND = 75;
  int BIT_OR = 76;
  int XOR = 77;
  int REM = 78;
  int LSHIFT = 79;
  int RSIGNEDSHIFT = 80;
  int RUNSIGNEDSHIFT = 81;

  int DEFAULT = 0;
  int IN_SINGLE_LINE_COMMENT = 1;
  int IN_FORMAL_COMMENT = 2;
  int IN_MULTI_LINE_COMMENT = 3;
  int CHAR_LIT = 4;
  int ESCAPED_CHAR_LIT = 5;
  int CHAR_LIT_CLOSE = 6;

  String[] tokenImage = {
	"<EOF>",
	"\" \"",
	"\"\\t\"",
	"\"\\n\"",
	"\"\\r\"",
	"\"\\f\"",
	"\"//\"",
	"<token of kind 7>",
	"\"/*\"",
	"<SINGLE_LINE_COMMENT>",
	"\"*/\"",
	"\"*/\"",
	"<token of kind 12>",
	"\"boolean\"",
	"\"byte\"",
	"\"char\"",
	"\"class\"",
	"\"double\"",
	"\"false\"",
	"\"float\"",
	"\"instanceof\"",
	"\"int\"",
	"\"long\"",
	"\"null\"",
	"\"super\"",
	"\"short\"",
	"\"this\"",
	"\"true\"",
	"\"predicate\"",
	"\"method\"",
	"\"expr\"",
	"\"global\"",
	"<INTEGER_LITERAL>",
	"<DECIMAL_LITERAL>",
	"<HEX_LITERAL>",
	"<OCTAL_LITERAL>",
	"<FLOATING_POINT_LITERAL>",
	"<EXPONENT>",
	"\"\\\'\"",
	"<STRING_LITERAL>",
	"\"\\\\\"",
	"<NON_ESCAPED_LITERAL>",
	"<ESCAPE_SEQ>",
	"<ESCAPE_OCTAL>",
	"<ESCAPE_UNICODE>",
	"\"\\\'\"",
	"<IDENTIFIER>",
	"<LETTER>",
	"<DIGIT>",
	"\"(\"",
	"\")\"",
	"\"{\"",
	"\"}\"",
	"\"[\"",
	"\"]\"",
	"\";\"",
	"\",\"",
	"\".\"",
	"\"=\"",
	"\">\"",
	"\"<\"",
	"\"!\"",
	"\"~\"",
	"\"==\"",
	"\"<=\"",
	"\">=\"",
	"\"!=\"",
	"\"||\"",
	"\"&&\"",
	"\"++\"",
	"\"--\"",
	"\"+\"",
	"\"-\"",
	"\"*\"",
	"\"/\"",
	"\"&\"",
	"\"|\"",
	"\"^\"",
	"\"%\"",
	"\"<<\"",
	"\">>\"",
	"\">>>\"",
	"\"?\"",
	"\":\"",
  };

}
