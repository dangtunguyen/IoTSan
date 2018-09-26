package edu.ksu.cis.bandera.jjjc.decompiler;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Robby (robby@cis.ksu.edu)              *
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
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.node.*;
public class LineExtractor extends DepthFirstAdapter {
	private static LineExtractor extractor = new LineExtractor();
	private int line;
/**
 * 
 */
private LineExtractor() {}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AAbstractModifier
 */
public void caseAAbstractModifier(AAbstractModifier node) {
	line = node.getAbstract().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AArrayAccessExp
 */
public void caseAArrayAccessExp(AArrayAccessExp node) {
	node.getArrayAccess().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AAssignmentExp
 */
public void caseAAssignmentExp(AAssignmentExp node) {
	node.getLeftHandSide().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABinaryExp
 */
public void caseABinaryExp(ABinaryExp node) {
	node.getFirst().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABitComplementUnaryOperator
 */
public void caseABitComplementUnaryOperator(ABitComplementUnaryOperator node) {
	line = node.getBitComplement().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABlockStmt
 */
public void caseABlockStmt(ABlockStmt node) {
	line = ((ABlock) node.getBlock()).getLBrace().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABooleanPrimitiveType
 */
public void caseABooleanPrimitiveType(ABooleanPrimitiveType node) {
	line = node.getBoolean().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABreakStmt
 */
public void caseABreakStmt(ABreakStmt node) {
	line = node.getBreak().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ABytePrimitiveType
 */
public void caseABytePrimitiveType(ABytePrimitiveType node) {
	line = node.getByte().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ACatchClause
 */
public void caseACatchClause(ACatchClause node) {
	line = node.getCatch().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ACharacterLiteralLiteral
 */
public void caseACharacterLiteralLiteral(ACharacterLiteralLiteral node) {
	line = node.getCharacterLiteral().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ACharPrimitiveType
 */
public void caseACharPrimitiveType(ACharPrimitiveType node) {
	line = node.getChar().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassDeclaration
 */
public void caseAClassDeclaration(AClassDeclaration node) {
	line = node.getId().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AClassOrInterfaceTypeExp
 */
public void caseAClassOrInterfaceTypeExp(AClassOrInterfaceTypeExp node) {
	line = node.getNew().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AComplementUnaryOperator
 */
public void caseAComplementUnaryOperator(AComplementUnaryOperator node) {
	line = node.getComplement().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorDeclaration
 */
public void caseAConstructorDeclaration(AConstructorDeclaration node) {
	Object temp[] = node.getModifier().toArray();
	if (temp.length > 0)
		((PModifier) temp[0]).apply(this);
	else
		node.getConstructorDeclarator().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AConstructorDeclarator
 */
public void caseAConstructorDeclarator(AConstructorDeclarator node) {
	//line = node.getSimpleName().getLine();
	node.getSimpleName().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AContinueStmt
 */
public void caseAContinueStmt(AContinueStmt node) {
	line = node.getContinue().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ADecimalIntegerLiteral
 */
public void caseADecimalIntegerLiteral(ADecimalIntegerLiteral node) {
	line = node.getDecimalIntegerLiteral().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ADecrementUnaryOperator
 */
public void caseADecrementUnaryOperator(ADecrementUnaryOperator node) {
	line = node.getMinusMinus().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ADoStmt
 */
public void caseADoStmt(ADoStmt node) {
	line = node.getDo().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ADoublePrimitiveType
 */
public void caseADoublePrimitiveType(ADoublePrimitiveType node) {
	line = node.getDouble().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AEmptyStmt
 */
public void caseAEmptyStmt(AEmptyStmt node) {
	line = node.getSemicolon().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AExpCastExp
 */
public void caseAExpCastExp(AExpCastExp node) {
	line = node.getLPar().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AExpStmt
 */
public void caseAExpStmt(AExpStmt node) {
	node.getExp().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFalseBooleanLiteral
 */
public void caseAFalseBooleanLiteral(AFalseBooleanLiteral node) {
	line = node.getFalse().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFieldAccessExp
 */
public void caseAFieldAccessExp(AFieldAccessExp node) {
	node.getFieldAccess().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFinalModifier
 */
public void caseAFinalModifier(AFinalModifier node) {
	line = node.getFinal().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFloatingPointLiteralLiteral
 */
public void caseAFloatingPointLiteralLiteral(AFloatingPointLiteralLiteral node) {
	line = node.getFloatingPointLiteral().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AFloatPrimitiveType
 */
public void caseAFloatPrimitiveType(AFloatPrimitiveType node) {
	line = node.getFloat().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AForStmt
 */
public void caseAForStmt(AForStmt node) {
	line = node.getFor().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AHexIntegerLiteral
 */
public void caseAHexIntegerLiteral(AHexIntegerLiteral node) {
	line = node.getHexIntegerLiteral().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AIfStmt
 */
public void caseAIfStmt(AIfStmt node) {
	line = node.getIf().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AIncrementUnaryOperator
 */
public void caseAIncrementUnaryOperator(AIncrementUnaryOperator node) {
	line = node.getPlusPlus().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInitClassInterfaceExp
 */
public void caseAInitClassInterfaceExp(AInitClassInterfaceExp node) {
	line = node.getNew().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInitPrimitiveExp
 */
public void caseAInitPrimitiveExp(AInitPrimitiveExp node) {
	line = node.getNew().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AInstanceofExp
 */
public void caseAInstanceofExp(AInstanceofExp node) {
	node.getExp().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AIntPrimitiveType
 */
public void caseAIntPrimitiveType(AIntPrimitiveType node) {
	line = node.getInt().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ALabelStmt
 */
public void caseALabelStmt(ALabelStmt node) {
	line = node.getId().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ALiteralExp
 */
public void caseALiteralExp(ALiteralExp node) {
	node.getLiteral().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ALocalVariableDeclaration
 */
public void caseALocalVariableDeclaration(ALocalVariableDeclaration node) {
	Object temp[] = node.getModifier().toArray();
	if (temp.length > 0) {
		((PModifier) temp[0]).apply(this);
	} else {
		node.getType().apply(this);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ALocalVariableDeclarationInBlockedStmt
 */
public void caseALocalVariableDeclarationInBlockedStmt(ALocalVariableDeclarationInBlockedStmt node) {
	node.getLocalVariableDeclaration().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ALongPrimitiveType
 */
public void caseALongPrimitiveType(ALongPrimitiveType node) {
	line = node.getLong().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AMethodDeclaration
 */
public void caseAMethodDeclaration(AMethodDeclaration node) {
	node.getMethodHeader().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AMinusUnaryOperator
 */
public void caseAMinusUnaryOperator(AMinusUnaryOperator node) {
	line = node.getMinus().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameArrayAccess
 */
public void caseANameArrayAccess(ANameArrayAccess node) {
	node.getName().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameCastExp
 */
public void caseANameCastExp(ANameCastExp node) {
	line = node.getLPar().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameExp
 */
public void caseANameExp(ANameExp node) {
	node.getName().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANameMethodInvocation
 */
public void caseANameMethodInvocation(ANameMethodInvocation node) {
	node.getName().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANativeModifier
 */
public void caseANativeModifier(ANativeModifier node) {
	line = node.getNative().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ANullLiteral
 */
public void caseANullLiteral(ANullLiteral node) {
	line = node.getNull().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AOctalIntegerLiteral
 */
public void caseAOctalIntegerLiteral(AOctalIntegerLiteral node) {
	line = node.getOctalIntegerLiteral().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AParExp
 */
public void caseAParExp(AParExp node) {
	line = node.getLPar().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APlusUnaryOperator
 */
public void caseAPlusUnaryOperator(APlusUnaryOperator node) {
	line = node.getPlus().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APostDecrementExp
 */
public void caseAPostDecrementExp(APostDecrementExp node) {
	node.getExp().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APostIncrementExp
 */
public void caseAPostIncrementExp(APostIncrementExp node) {
	node.getExp().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryFieldAccess
 */
public void caseAPrimaryFieldAccess(APrimaryFieldAccess node) {
	node.getExp().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryMethodInvocation
 */
public void caseAPrimaryMethodInvocation(APrimaryMethodInvocation node) {
	node.getPrimary().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimaryNoNewArrayArrayAccess
 */
public void caseAPrimaryNoNewArrayArrayAccess(APrimaryNoNewArrayArrayAccess node) {
	node.getFirst().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypeArrayExp
 */
public void caseAPrimitiveTypeArrayExp(APrimitiveTypeArrayExp node) {
	line = node.getNew().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypeCastExp
 */
public void caseAPrimitiveTypeCastExp(APrimitiveTypeCastExp node) {
	line = node.getLPar().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrimitiveTypePrimaryExp
 */
public void caseAPrimitiveTypePrimaryExp(APrimitiveTypePrimaryExp node) {
	node.getPrimitiveType().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APrivateModifier
 */
public void caseAPrivateModifier(APrivateModifier node) {
	line = node.getPrivate().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AProtectedModifier
 */
public void caseAProtectedModifier(AProtectedModifier node) {
	line = node.getProtected().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.APublicModifier
 */
public void caseAPublicModifier(APublicModifier node) {
	line = node.getPublic().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedClassInstanceCreationExp
 */
public void caseAQualifiedClassInstanceCreationExp(AQualifiedClassInstanceCreationExp node) {
	node.getExp().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedConstructorInvocation
 */
public void caseAQualifiedConstructorInvocation(AQualifiedConstructorInvocation node) {
	node.getExp().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedName
 */
public void caseAQualifiedName(AQualifiedName node) {
	node.getName().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQualifiedThisExp
 */
public void caseAQualifiedThisExp(AQualifiedThisExp node) {
	node.getName().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AQuestionExp
 */
public void caseAQuestionExp(AQuestionExp node) {
	node.getFirst().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AReturnStmt
 */
public void caseAReturnStmt(AReturnStmt node) {
	line = node.getReturn().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AShortPrimitiveType
 */
public void caseAShortPrimitiveType(AShortPrimitiveType node) {
	line = node.getShort().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASimpleClassInstanceCreationExp
 */
public void caseASimpleClassInstanceCreationExp(ASimpleClassInstanceCreationExp node) {
	line = node.getNew().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASimpleName
 */
public void caseASimpleName(ASimpleName node) {
	line = node.getId().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AStaticModifier
 */
public void caseAStaticModifier(AStaticModifier node) {
	line = node.getStatic().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AStringLiteralLiteral
 */
public void caseAStringLiteralLiteral(AStringLiteralLiteral node) {
	line = node.getStringLiteral().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperConstructorInvocation
 */
public void caseASuperConstructorInvocation(ASuperConstructorInvocation node) {
	line = node.getSuper().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperFieldAccess
 */
public void caseASuperFieldAccess(ASuperFieldAccess node) {
	line = node.getSuper().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASuperMethodInvocation
 */
public void caseASuperMethodInvocation(ASuperMethodInvocation node) {
	line = node.getSuper().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASwitchStmt
 */
public void caseASwitchStmt(ASwitchStmt node) {
	line = node.getSwitch().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASynchronizedModifier
 */
public void caseASynchronizedModifier(ASynchronizedModifier node) {
	line = node.getSynchronized().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASynchronizedStmt
 */
public void caseASynchronizedStmt(ASynchronizedStmt node) {
	line = node.getSynchronized().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AThisConstructorInvocation
 */
public void caseAThisConstructorInvocation(AThisConstructorInvocation node) {
	line = node.getThis().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AThisExp
 */
public void caseAThisExp(AThisExp node) {
	line = node.getThis().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AThrowStmt
 */
public void caseAThrowStmt(AThrowStmt node) {
	line = node.getThrow().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATransientModifier
 */
public void caseATransientModifier(ATransientModifier node) {
	line = node.getTransient().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATrueBooleanLiteral
 */
public void caseATrueBooleanLiteral(ATrueBooleanLiteral node) {
	line = node.getTrue().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATryFinallyStmt
 */
public void caseATryFinallyStmt(ATryFinallyStmt node) {
	line = node.getTry().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATryStmt
 */
public void caseATryStmt(ATryStmt node) {
	line = node.getTry().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATypedMethodHeader
 */
public void caseATypedMethodHeader(ATypedMethodHeader node) {
	Object temp[] = node.getModifier().toArray();
	if (temp.length > 0)
		 ((PModifier) temp[0]).apply(this);
	else
		node.getType().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AUnaryExp
 */
public void caseAUnaryExp(AUnaryExp node) {
	node.getUnaryOperator().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AVariableDeclaratorId
 */
public void caseAVariableDeclaratorId(AVariableDeclaratorId node) {
	line = node.getId().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AVoidExp
 */
public void caseAVoidExp(AVoidExp node) {
	line = node.getVoid().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AVoidMethodHeader
 */
public void caseAVoidMethodHeader(AVoidMethodHeader node) {
	Object temp[] = node.getModifier().toArray();
	if (temp.length > 0)
		 ((PModifier) temp[0]).apply(this);
	else
		node.getVoid().apply(this);
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AVolatileModifier
 */
public void caseAVolatileModifier(AVolatileModifier node) {
	line = node.getVolatile().getLine();
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.AWhileStmt
 */
public void caseAWhileStmt(AWhileStmt node) {
	line = node.getWhile().getLine();
}
/**
 * 
 * @return int
 * @param node edu.ksu.cis.bandera.jjjc.node.Node
 */
public static int extractLine(Node node) {
	extractor.line = -1;
	node.apply(extractor);
	return extractor.line;
}
}
