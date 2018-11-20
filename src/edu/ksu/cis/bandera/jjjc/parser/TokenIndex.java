package edu.ksu.cis.bandera.jjjc.parser;

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
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;

class TokenIndex extends AnalysisAdapter
{
	int index;

	public void caseEOF(EOF node)
	{
		index = 103;
	}
	public void caseTAbstract(TAbstract node)
	{
		index = 0;
	}
	public void caseTAnd(TAnd node)
	{
		index = 70;
	}
	public void caseTAssign(TAssign node)
	{
		index = 59;
	}
	public void caseTBitAnd(TBitAnd node)
	{
		index = 78;
	}
	public void caseTBitAndAssign(TBitAndAssign node)
	{
		index = 89;
	}
	public void caseTBitComplement(TBitComplement node)
	{
		index = 63;
	}
	public void caseTBitOr(TBitOr node)
	{
		index = 79;
	}
	public void caseTBitOrAssign(TBitOrAssign node)
	{
		index = 90;
	}
	public void caseTBitXor(TBitXor node)
	{
		index = 80;
	}
	public void caseTBitXorAssign(TBitXorAssign node)
	{
		index = 91;
	}
	public void caseTBoolean(TBoolean node)
	{
		index = 5;
	}
	public void caseTBreak(TBreak node)
	{
		index = 10;
	}
	public void caseTByte(TByte node)
	{
		index = 15;
	}
	public void caseTCase(TCase node)
	{
		index = 20;
	}
	public void caseTCatch(TCatch node)
	{
		index = 25;
	}
	public void caseTChar(TChar node)
	{
		index = 30;
	}
	public void caseTCharacterLiteral(TCharacterLiteral node)
	{
		index = 100;
	}
	public void caseTClass(TClass node)
	{
		index = 35;
	}
	public void caseTColon(TColon node)
	{
		index = 65;
	}
	public void caseTComma(TComma node)
	{
		index = 57;
	}
	public void caseTComplement(TComplement node)
	{
		index = 62;
	}
	public void caseTConst(TConst node)
	{
		index = 39;
	}
	public void caseTContinue(TContinue node)
	{
		index = 43;
	}
	public void caseTDecimalIntegerLiteral(TDecimalIntegerLiteral node)
	{
		index = 96;
	}
	public void caseTDefault(TDefault node)
	{
		index = 1;
	}
	public void caseTDiv(TDiv node)
	{
		index = 77;
	}
	public void caseTDivAssign(TDivAssign node)
	{
		index = 88;
	}
	public void caseTDo(TDo node)
	{
		index = 6;
	}
	public void caseTDot(TDot node)
	{
		index = 58;
	}
	public void caseTDouble(TDouble node)
	{
		index = 11;
	}
	public void caseTElse(TElse node)
	{
		index = 16;
	}
	public void caseTEq(TEq node)
	{
		index = 66;
	}
	public void caseTExtends(TExtends node)
	{
		index = 21;
	}
	public void caseTFalse(TFalse node)
	{
		index = 48;
	}
	public void caseTFinal(TFinal node)
	{
		index = 26;
	}
	public void caseTFinally(TFinally node)
	{
		index = 31;
	}
	public void caseTFloat(TFloat node)
	{
		index = 36;
	}
	public void caseTFloatingPointLiteral(TFloatingPointLiteral node)
	{
		index = 99;
	}
	public void caseTFor(TFor node)
	{
		index = 40;
	}
	public void caseTGoto(TGoto node)
	{
		index = 44;
	}
	public void caseTGt(TGt node)
	{
		index = 61;
	}
	public void caseTGteq(TGteq node)
	{
		index = 68;
	}
	public void caseTHexIntegerLiteral(THexIntegerLiteral node)
	{
		index = 97;
	}
	public void caseTId(TId node)
	{
		index = 102;
	}
	public void caseTIf(TIf node)
	{
		index = 2;
	}
	public void caseTImplements(TImplements node)
	{
		index = 7;
	}
	public void caseTImport(TImport node)
	{
		index = 12;
	}
	public void caseTInstanceof(TInstanceof node)
	{
		index = 17;
	}
	public void caseTInt(TInt node)
	{
		index = 22;
	}
	public void caseTInterface(TInterface node)
	{
		index = 27;
	}
	public void caseTLBrace(TLBrace node)
	{
		index = 52;
	}
	public void caseTLBracket(TLBracket node)
	{
		index = 54;
	}
	public void caseTLong(TLong node)
	{
		index = 32;
	}
	public void caseTLPar(TLPar node)
	{
		index = 50;
	}
	public void caseTLt(TLt node)
	{
		index = 60;
	}
	public void caseTLteq(TLteq node)
	{
		index = 67;
	}
	public void caseTMinus(TMinus node)
	{
		index = 75;
	}
	public void caseTMinusAssign(TMinusAssign node)
	{
		index = 86;
	}
	public void caseTMinusMinus(TMinusMinus node)
	{
		index = 73;
	}
	public void caseTMod(TMod node)
	{
		index = 81;
	}
	public void caseTModAssign(TModAssign node)
	{
		index = 92;
	}
	public void caseTNative(TNative node)
	{
		index = 37;
	}
	public void caseTNeq(TNeq node)
	{
		index = 69;
	}
	public void caseTNew(TNew node)
	{
		index = 41;
	}
	public void caseTNull(TNull node)
	{
		index = 49;
	}
	public void caseTOctalIntegerLiteral(TOctalIntegerLiteral node)
	{
		index = 98;
	}
	public void caseTOr(TOr node)
	{
		index = 71;
	}
	public void caseTPackage(TPackage node)
	{
		index = 45;
	}
	public void caseTPlus(TPlus node)
	{
		index = 74;
	}
	public void caseTPlusAssign(TPlusAssign node)
	{
		index = 85;
	}
	public void caseTPlusPlus(TPlusPlus node)
	{
		index = 72;
	}
	public void caseTPrivate(TPrivate node)
	{
		index = 3;
	}
	public void caseTProtected(TProtected node)
	{
		index = 8;
	}
	public void caseTPublic(TPublic node)
	{
		index = 13;
	}
	public void caseTQuestion(TQuestion node)
	{
		index = 64;
	}
	public void caseTRBrace(TRBrace node)
	{
		index = 53;
	}
	public void caseTRBracket(TRBracket node)
	{
		index = 55;
	}
	public void caseTReturn(TReturn node)
	{
		index = 18;
	}
	public void caseTRPar(TRPar node)
	{
		index = 51;
	}
	public void caseTSemicolon(TSemicolon node)
	{
		index = 56;
	}
	public void caseTShiftLeft(TShiftLeft node)
	{
		index = 82;
	}
	public void caseTShiftLeftAssign(TShiftLeftAssign node)
	{
		index = 93;
	}
	public void caseTShort(TShort node)
	{
		index = 23;
	}
	public void caseTSignedShiftRight(TSignedShiftRight node)
	{
		index = 83;
	}
	public void caseTSignedShiftRightAssign(TSignedShiftRightAssign node)
	{
		index = 94;
	}
	public void caseTStar(TStar node)
	{
		index = 76;
	}
	public void caseTStarAssign(TStarAssign node)
	{
		index = 87;
	}
	public void caseTStatic(TStatic node)
	{
		index = 28;
	}
	public void caseTStringLiteral(TStringLiteral node)
	{
		index = 101;
	}
	public void caseTSuper(TSuper node)
	{
		index = 33;
	}
	public void caseTSwitch(TSwitch node)
	{
		index = 38;
	}
	public void caseTSynchronized(TSynchronized node)
	{
		index = 42;
	}
	public void caseTThis(TThis node)
	{
		index = 46;
	}
	public void caseTThrow(TThrow node)
	{
		index = 4;
	}
	public void caseTThrows(TThrows node)
	{
		index = 9;
	}
	public void caseTTransient(TTransient node)
	{
		index = 14;
	}
	public void caseTTrue(TTrue node)
	{
		index = 47;
	}
	public void caseTTry(TTry node)
	{
		index = 19;
	}
	public void caseTUnsignedShiftRight(TUnsignedShiftRight node)
	{
		index = 84;
	}
	public void caseTUnsignedShiftRightAssign(TUnsignedShiftRightAssign node)
	{
		index = 95;
	}
	public void caseTVoid(TVoid node)
	{
		index = 24;
	}
	public void caseTVolatile(TVolatile node)
	{
		index = 29;
	}
	public void caseTWhile(TWhile node)
	{
		index = 34;
	}
}
