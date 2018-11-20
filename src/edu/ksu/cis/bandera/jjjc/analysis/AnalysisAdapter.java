package edu.ksu.cis.bandera.jjjc.analysis;

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
import ca.mcgill.sable.util.*;
import java.util.*;
import edu.ksu.cis.bandera.jjjc.node.*;

public class AnalysisAdapter implements Analysis
{
	private Hashtable in;
	private Hashtable out;

	public void caseAAbstractMethodDeclaration(AAbstractMethodDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAAbstractMethodDeclarationInterfaceMemberDeclaration(AAbstractMethodDeclarationInterfaceMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAAbstractModifier(AAbstractModifier node)
	{
		defaultCase(node);
	}
	public void caseAAdditiveExpShiftExp(AAdditiveExpShiftExp node)
	{
		defaultCase(node);
	}
	public void caseAAndBinaryOperator(AAndBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAAndExpAndExp(AAndExpAndExp node)
	{
		defaultCase(node);
	}
	public void caseAAndExpExclusiveOrExp(AAndExpExclusiveOrExp node)
	{
		defaultCase(node);
	}
	public void caseAArgumentListArgumentList(AArgumentListArgumentList node)
	{
		defaultCase(node);
	}
	public void caseAArrayAccessExp(AArrayAccessExp node)
	{
		defaultCase(node);
	}
	public void caseAArrayAccessLeftHandSide(AArrayAccessLeftHandSide node)
	{
		defaultCase(node);
	}
	public void caseAArrayAccessPrimaryNoNewArray(AArrayAccessPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAArrayCreationExpPrimary(AArrayCreationExpPrimary node)
	{
		defaultCase(node);
	}
	public void caseAArrayInitializer(AArrayInitializer node)
	{
		defaultCase(node);
	}
	public void caseAArrayReferenceType(AArrayReferenceType node)
	{
		defaultCase(node);
	}
	public void caseAArrayVariableInitializer(AArrayVariableInitializer node)
	{
		defaultCase(node);
	}
	public void caseAAssertionCompilationUnit(AAssertionCompilationUnit node)
	{
		defaultCase(node);
	}
	public void caseAAssignAssignmentOperator(AAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseAAssignedVariableDeclarator(AAssignedVariableDeclarator node)
	{
		defaultCase(node);
	}
	public void caseAAssignment(AAssignment node)
	{
		defaultCase(node);
	}
	public void caseAAssignmentAssignmentExp(AAssignmentAssignmentExp node)
	{
		defaultCase(node);
	}
	public void caseAAssignmentExp(AAssignmentExp node)
	{
		defaultCase(node);
	}
	public void caseAAssignmentStmtExp(AAssignmentStmtExp node)
	{
		defaultCase(node);
	}
	public void caseABinaryExp(ABinaryExp node)
	{
		defaultCase(node);
	}
	public void caseABitAndAssignAssignmentOperator(ABitAndAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseABitAndBinaryOperator(ABitAndBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseABitComplementUnaryExpNotPlusMinus(ABitComplementUnaryExpNotPlusMinus node)
	{
		defaultCase(node);
	}
	public void caseABitComplementUnaryOperator(ABitComplementUnaryOperator node)
	{
		defaultCase(node);
	}
	public void caseABitOrAssignAssignmentOperator(ABitOrAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseABitOrBinaryOperator(ABitOrBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseABitXorAssignAssignmentOperator(ABitXorAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseABitXorBinaryOperator(ABitXorBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseABlock(ABlock node)
	{
		defaultCase(node);
	}
	public void caseABlockClassBodyDeclaration(ABlockClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseABlockMethodBody(ABlockMethodBody node)
	{
		defaultCase(node);
	}
	public void caseABlockStmt(ABlockStmt node)
	{
		defaultCase(node);
	}
	public void caseABlockStmtWithoutTrailingSubstmt(ABlockStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseABooleanLiteralLiteral(ABooleanLiteralLiteral node)
	{
		defaultCase(node);
	}
	public void caseABooleanPrimitiveType(ABooleanPrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseABreakStmt(ABreakStmt node)
	{
		defaultCase(node);
	}
	public void caseABreakStmtStmtWithoutTrailingSubstmt(ABreakStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseAByteIntegralType(AByteIntegralType node)
	{
		defaultCase(node);
	}
	public void caseABytePrimitiveType(ABytePrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseACaseSwitchLabel(ACaseSwitchLabel node)
	{
		defaultCase(node);
	}
	public void caseACastExpUnaryExpNotPlusMinus(ACastExpUnaryExpNotPlusMinus node)
	{
		defaultCase(node);
	}
	public void caseACatchClause(ACatchClause node)
	{
		defaultCase(node);
	}
	public void caseACharacterLiteralLiteral(ACharacterLiteralLiteral node)
	{
		defaultCase(node);
	}
	public void caseACharIntegralType(ACharIntegralType node)
	{
		defaultCase(node);
	}
	public void caseACharPrimitiveType(ACharPrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseAClassBody(AClassBody node)
	{
		defaultCase(node);
	}
	public void caseAClassClassBodyDeclaration(AClassClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAClassDeclaration(AClassDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAClassDeclarationBlockedStmt(AClassDeclarationBlockedStmt node)
	{
		defaultCase(node);
	}
	public void caseAClassDeclarationClassMemberDeclaration(AClassDeclarationClassMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAClassDeclarationInterfaceMemberDeclaration(AClassDeclarationInterfaceMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAClassInstanceCreationExpPrimaryNoNewArray(AClassInstanceCreationExpPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAClassInstanceCreationExpStmtExp(AClassInstanceCreationExpStmtExp node)
	{
		defaultCase(node);
	}
	public void caseAClassMemberDeclarationClassBodyDeclaration(AClassMemberDeclarationClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAClassOrInterfaceType(AClassOrInterfaceType node)
	{
		defaultCase(node);
	}
	public void caseAClassOrInterfaceTypeArrayCreationExp(AClassOrInterfaceTypeArrayCreationExp node)
	{
		defaultCase(node);
	}
	public void caseAClassOrInterfaceTypeExp(AClassOrInterfaceTypeExp node)
	{
		defaultCase(node);
	}
	public void caseAClassOrInterfaceTypeReferenceType(AClassOrInterfaceTypeReferenceType node)
	{
		defaultCase(node);
	}
	public void caseAClassType(AClassType node)
	{
		defaultCase(node);
	}
	public void caseAClassTypeClassTypeList(AClassTypeClassTypeList node)
	{
		defaultCase(node);
	}
	public void caseAClassTypeDeclaration(AClassTypeDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAClassTypeListClassTypeList(AClassTypeListClassTypeList node)
	{
		defaultCase(node);
	}
	public void caseACompilationUnit(ACompilationUnit node)
	{
		defaultCase(node);
	}
	public void caseAComplementUnaryExpNotPlusMinus(AComplementUnaryExpNotPlusMinus node)
	{
		defaultCase(node);
	}
	public void caseAComplementUnaryOperator(AComplementUnaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAConditionalAndExpConditionalAndExp(AConditionalAndExpConditionalAndExp node)
	{
		defaultCase(node);
	}
	public void caseAConditionalAndExpConditionalOrExp(AConditionalAndExpConditionalOrExp node)
	{
		defaultCase(node);
	}
	public void caseAConditionalExpAssignmentExp(AConditionalExpAssignmentExp node)
	{
		defaultCase(node);
	}
	public void caseAConditionalOrExpConditionalExp(AConditionalOrExpConditionalExp node)
	{
		defaultCase(node);
	}
	public void caseAConditionalOrExpConditionalOrExp(AConditionalOrExpConditionalOrExp node)
	{
		defaultCase(node);
	}
	public void caseAConstantDeclaration(AConstantDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAConstantDeclarationInterfaceMemberDeclaration(AConstantDeclarationInterfaceMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAConstantExp(AConstantExp node)
	{
		defaultCase(node);
	}
	public void caseAConstructorBody(AConstructorBody node)
	{
		defaultCase(node);
	}
	public void caseAConstructorClassBodyDeclaration(AConstructorClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAConstructorDeclaration(AConstructorDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAConstructorDeclarator(AConstructorDeclarator node)
	{
		defaultCase(node);
	}
	public void caseAContinueStmt(AContinueStmt node)
	{
		defaultCase(node);
	}
	public void caseAContinueStmtStmtWithoutTrailingSubstmt(AContinueStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseADecimalIntegerLiteral(ADecimalIntegerLiteral node)
	{
		defaultCase(node);
	}
	public void caseADecrementUnaryOperator(ADecrementUnaryOperator node)
	{
		defaultCase(node);
	}
	public void caseADefaultSwitchLabel(ADefaultSwitchLabel node)
	{
		defaultCase(node);
	}
	public void caseADim(ADim node)
	{
		defaultCase(node);
	}
	public void caseADimExp(ADimExp node)
	{
		defaultCase(node);
	}
	public void caseADivAssignAssignmentOperator(ADivAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseADivBinaryOperator(ADivBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseADivMultiplicativeExp(ADivMultiplicativeExp node)
	{
		defaultCase(node);
	}
	public void caseADoStmt(ADoStmt node)
	{
		defaultCase(node);
	}
	public void caseADoStmtStmtWithoutTrailingSubstmt(ADoStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseADoubleFloatingPointType(ADoubleFloatingPointType node)
	{
		defaultCase(node);
	}
	public void caseADoublePrimitiveType(ADoublePrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseAEmptyMethodBody(AEmptyMethodBody node)
	{
		defaultCase(node);
	}
	public void caseAEmptyStmt(AEmptyStmt node)
	{
		defaultCase(node);
	}
	public void caseAEmptyStmtStmtWithoutTrailingSubstmt(AEmptyStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseAEmptyTypeDeclaration(AEmptyTypeDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAEqBinaryOperator(AEqBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAEqEqualityExp(AEqEqualityExp node)
	{
		defaultCase(node);
	}
	public void caseAEqualityExpAndExp(AEqualityExpAndExp node)
	{
		defaultCase(node);
	}
	public void caseAExclusiveOrExpExclusiveOrExp(AExclusiveOrExpExclusiveOrExp node)
	{
		defaultCase(node);
	}
	public void caseAExclusiveOrExpInclusiveOrExp(AExclusiveOrExpInclusiveOrExp node)
	{
		defaultCase(node);
	}
	public void caseAExpArgumentList(AExpArgumentList node)
	{
		defaultCase(node);
	}
	public void caseAExpCastExp(AExpCastExp node)
	{
		defaultCase(node);
	}
	public void caseAExpListForInit(AExpListForInit node)
	{
		defaultCase(node);
	}
	public void caseAExpStmt(AExpStmt node)
	{
		defaultCase(node);
	}
	public void caseAExpStmtStmtWithoutTrailingSubstmt(AExpStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseAExpVariableInitializer(AExpVariableInitializer node)
	{
		defaultCase(node);
	}
	public void caseAExtendsExtendsInterfaces(AExtendsExtendsInterfaces node)
	{
		defaultCase(node);
	}
	public void caseAExtendsInterfacesExtendsInterfaces(AExtendsInterfacesExtendsInterfaces node)
	{
		defaultCase(node);
	}
	public void caseAFalseBooleanLiteral(AFalseBooleanLiteral node)
	{
		defaultCase(node);
	}
	public void caseAFieldAccessExp(AFieldAccessExp node)
	{
		defaultCase(node);
	}
	public void caseAFieldAccessLeftHandSide(AFieldAccessLeftHandSide node)
	{
		defaultCase(node);
	}
	public void caseAFieldAccessPrimaryNoNewArray(AFieldAccessPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAFieldClassBodyDeclaration(AFieldClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAFieldDeclaration(AFieldDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAFieldDeclarationClassMemberDeclaration(AFieldDeclarationClassMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAFinally(AFinally node)
	{
		defaultCase(node);
	}
	public void caseAFinallyOneTryStmt(AFinallyOneTryStmt node)
	{
		defaultCase(node);
	}
	public void caseAFinalModifier(AFinalModifier node)
	{
		defaultCase(node);
	}
	public void caseAFloatFloatingPointType(AFloatFloatingPointType node)
	{
		defaultCase(node);
	}
	public void caseAFloatingPointLiteralLiteral(AFloatingPointLiteralLiteral node)
	{
		defaultCase(node);
	}
	public void caseAFloatingPointTypeNumericType(AFloatingPointTypeNumericType node)
	{
		defaultCase(node);
	}
	public void caseAFloatPrimitiveType(AFloatPrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseAFormalParameter(AFormalParameter node)
	{
		defaultCase(node);
	}
	public void caseAFormalParameterFormalParameterList(AFormalParameterFormalParameterList node)
	{
		defaultCase(node);
	}
	public void caseAFormalParameterListFormalParameterList(AFormalParameterListFormalParameterList node)
	{
		defaultCase(node);
	}
	public void caseAForStmt(AForStmt node)
	{
		defaultCase(node);
	}
	public void caseAForStmtNoShortIf(AForStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseAForStmtNoShortIfStmtNoShortIf(AForStmtNoShortIfStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseAForStmtStmt(AForStmtStmt node)
	{
		defaultCase(node);
	}
	public void caseAForUpdate(AForUpdate node)
	{
		defaultCase(node);
	}
	public void caseAGtBinaryOperator(AGtBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAGteqBinaryOperator(AGteqBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAGteqRelationalExp(AGteqRelationalExp node)
	{
		defaultCase(node);
	}
	public void caseAGtRelationalExp(AGtRelationalExp node)
	{
		defaultCase(node);
	}
	public void caseAHexIntegerLiteral(AHexIntegerLiteral node)
	{
		defaultCase(node);
	}
	public void caseAIdVariableDeclarator(AIdVariableDeclarator node)
	{
		defaultCase(node);
	}
	public void caseAIfStmt(AIfStmt node)
	{
		defaultCase(node);
	}
	public void caseAIfThenElseStmt(AIfThenElseStmt node)
	{
		defaultCase(node);
	}
	public void caseAIfThenElseStmtNoShortIf(AIfThenElseStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseAIfThenElseStmtNoShortIfStmtNoShortIf(AIfThenElseStmtNoShortIfStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseAIfThenElseStmtStmt(AIfThenElseStmtStmt node)
	{
		defaultCase(node);
	}
	public void caseAIfThenStmt(AIfThenStmt node)
	{
		defaultCase(node);
	}
	public void caseAIfThenStmtStmt(AIfThenStmtStmt node)
	{
		defaultCase(node);
	}
	public void caseAInclusiveOrExpConditionalAndExp(AInclusiveOrExpConditionalAndExp node)
	{
		defaultCase(node);
	}
	public void caseAInclusiveOrExpInclusiveOrExp(AInclusiveOrExpInclusiveOrExp node)
	{
		defaultCase(node);
	}
	public void caseAIncrementUnaryOperator(AIncrementUnaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAInitClassInterfaceArrayCreationExp(AInitClassInterfaceArrayCreationExp node)
	{
		defaultCase(node);
	}
	public void caseAInitClassInterfaceExp(AInitClassInterfaceExp node)
	{
		defaultCase(node);
	}
	public void caseAInitPrimitiveArrayCreationExp(AInitPrimitiveArrayCreationExp node)
	{
		defaultCase(node);
	}
	public void caseAInitPrimitiveExp(AInitPrimitiveExp node)
	{
		defaultCase(node);
	}
	public void caseAInstanceofExp(AInstanceofExp node)
	{
		defaultCase(node);
	}
	public void caseAInstanceofRelationalExp(AInstanceofRelationalExp node)
	{
		defaultCase(node);
	}
	public void caseAIntegerLiteralLiteral(AIntegerLiteralLiteral node)
	{
		defaultCase(node);
	}
	public void caseAIntegralTypeNumericType(AIntegralTypeNumericType node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceBody(AInterfaceBody node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceClassBodyDeclaration(AInterfaceClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceDeclaration(AInterfaceDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceDeclarationClassMemberDeclaration(AInterfaceDeclarationClassMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceDeclarationInterfaceMemberDeclaration(AInterfaceDeclarationInterfaceMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAInterfaces(AInterfaces node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceType(AInterfaceType node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceTypeDeclaration(AInterfaceTypeDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceTypeInterfaceTypeList(AInterfaceTypeInterfaceTypeList node)
	{
		defaultCase(node);
	}
	public void caseAInterfaceTypeListInterfaceTypeList(AInterfaceTypeListInterfaceTypeList node)
	{
		defaultCase(node);
	}
	public void caseAIntIntegralType(AIntIntegralType node)
	{
		defaultCase(node);
	}
	public void caseAIntPrimitiveType(AIntPrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseALabeledStmt(ALabeledStmt node)
	{
		defaultCase(node);
	}
	public void caseALabeledStmtNoShortIf(ALabeledStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseALabeledStmtNoShortIfStmtNoShortIf(ALabeledStmtNoShortIfStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseALabeledStmtStmt(ALabeledStmtStmt node)
	{
		defaultCase(node);
	}
	public void caseALabelStmt(ALabelStmt node)
	{
		defaultCase(node);
	}
	public void caseALiteralExp(ALiteralExp node)
	{
		defaultCase(node);
	}
	public void caseALiteralPrimaryNoNewArray(ALiteralPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseALocalVariableDeclaration(ALocalVariableDeclaration node)
	{
		defaultCase(node);
	}
	public void caseALocalVariableDeclarationForInit(ALocalVariableDeclarationForInit node)
	{
		defaultCase(node);
	}
	public void caseALocalVariableDeclarationInBlockedStmt(ALocalVariableDeclarationInBlockedStmt node)
	{
		defaultCase(node);
	}
	public void caseALocalVariableDeclarationStmt(ALocalVariableDeclarationStmt node)
	{
		defaultCase(node);
	}
	public void caseALocalVariableDeclarationStmtBlockedStmt(ALocalVariableDeclarationStmtBlockedStmt node)
	{
		defaultCase(node);
	}
	public void caseALongIntegralType(ALongIntegralType node)
	{
		defaultCase(node);
	}
	public void caseALongPrimitiveType(ALongPrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseALParPrimaryNoNewArray(ALParPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseALtBinaryOperator(ALtBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseALteqBinaryOperator(ALteqBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseALteqRelationalExp(ALteqRelationalExp node)
	{
		defaultCase(node);
	}
	public void caseALtRelationalExp(ALtRelationalExp node)
	{
		defaultCase(node);
	}
	public void caseAMethodClassBodyDeclaration(AMethodClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAMethodDeclaration(AMethodDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAMethodDeclarationClassMemberDeclaration(AMethodDeclarationClassMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAMethodDeclarator(AMethodDeclarator node)
	{
		defaultCase(node);
	}
	public void caseAMethodInvocationPrimaryNoNewArray(AMethodInvocationPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAMethodInvocationStmtExp(AMethodInvocationStmtExp node)
	{
		defaultCase(node);
	}
	public void caseAMinusAdditiveExp(AMinusAdditiveExp node)
	{
		defaultCase(node);
	}
	public void caseAMinusAssignAssignmentOperator(AMinusAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseAMinusBinaryOperator(AMinusBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAMinusUnaryExp(AMinusUnaryExp node)
	{
		defaultCase(node);
	}
	public void caseAMinusUnaryOperator(AMinusUnaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAModAssignAssignmentOperator(AModAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseAModBinaryOperator(AModBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAModMultiplicativeExp(AModMultiplicativeExp node)
	{
		defaultCase(node);
	}
	public void caseAMultiplicativeExpAdditiveExp(AMultiplicativeExpAdditiveExp node)
	{
		defaultCase(node);
	}
	public void caseANameArrayAccess(ANameArrayAccess node)
	{
		defaultCase(node);
	}
	public void caseANameArrayType(ANameArrayType node)
	{
		defaultCase(node);
	}
	public void caseANameCastExp(ANameCastExp node)
	{
		defaultCase(node);
	}
	public void caseANamedTypeExp(ANamedTypeExp node)
	{
		defaultCase(node);
	}
	public void caseANameExp(ANameExp node)
	{
		defaultCase(node);
	}
	public void caseANameLeftHandSide(ANameLeftHandSide node)
	{
		defaultCase(node);
	}
	public void caseANameMethodInvocation(ANameMethodInvocation node)
	{
		defaultCase(node);
	}
	public void caseANameMethodInvocationExp(ANameMethodInvocationExp node)
	{
		defaultCase(node);
	}
	public void caseANamePostfixExp(ANamePostfixExp node)
	{
		defaultCase(node);
	}
	public void caseANameReferenceType(ANameReferenceType node)
	{
		defaultCase(node);
	}
	public void caseANativeModifier(ANativeModifier node)
	{
		defaultCase(node);
	}
	public void caseANeqBinaryOperator(ANeqBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseANeqEqualityExp(ANeqEqualityExp node)
	{
		defaultCase(node);
	}
	public void caseANotPlusMinusUnaryExp(ANotPlusMinusUnaryExp node)
	{
		defaultCase(node);
	}
	public void caseANullLiteral(ANullLiteral node)
	{
		defaultCase(node);
	}
	public void caseANullLiteralLiteral(ANullLiteralLiteral node)
	{
		defaultCase(node);
	}
	public void caseANumericTypePrimitiveType(ANumericTypePrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseAOctalIntegerLiteral(AOctalIntegerLiteral node)
	{
		defaultCase(node);
	}
	public void caseAOldAbstractMethodDeclarationInterfaceMemberDeclaration(AOldAbstractMethodDeclarationInterfaceMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAOldArrayInitializer(AOldArrayInitializer node)
	{
		defaultCase(node);
	}
	public void caseAOldCaseSwitchLabel(AOldCaseSwitchLabel node)
	{
		defaultCase(node);
	}
	public void caseAOldCompilationUnit(AOldCompilationUnit node)
	{
		defaultCase(node);
	}
	public void caseAOldConstantDeclarationInterfaceMemberDeclaration(AOldConstantDeclarationInterfaceMemberDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAOldConstructorDeclarator(AOldConstructorDeclarator node)
	{
		defaultCase(node);
	}
	public void caseAOldExp(AOldExp node)
	{
		defaultCase(node);
	}
	public void caseAOldExpCastExp(AOldExpCastExp node)
	{
		defaultCase(node);
	}
	public void caseAOldFieldDeclaration(AOldFieldDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAOldInterfaceDeclaration(AOldInterfaceDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAOldInterfaces(AOldInterfaces node)
	{
		defaultCase(node);
	}
	public void caseAOldLocalVariableDeclaration(AOldLocalVariableDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAOldMethodDeclarator(AOldMethodDeclarator node)
	{
		defaultCase(node);
	}
	public void caseAOldNameCastExp(AOldNameCastExp node)
	{
		defaultCase(node);
	}
	public void caseAOldNamedTypePrimaryNoNewArray(AOldNamedTypePrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAOldPrimaryFieldAccess(AOldPrimaryFieldAccess node)
	{
		defaultCase(node);
	}
	public void caseAOldPrimaryNoNewArrayArrayAccess(AOldPrimaryNoNewArrayArrayAccess node)
	{
		defaultCase(node);
	}
	public void caseAOldPrimitiveTypeCastExp(AOldPrimitiveTypeCastExp node)
	{
		defaultCase(node);
	}
	public void caseAOldPrimitiveTypePrimaryNoNewArray(AOldPrimitiveTypePrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAOldQualifiedClassInstanceCreationExp(AOldQualifiedClassInstanceCreationExp node)
	{
		defaultCase(node);
	}
	public void caseAOldQualifiedConstructorInvocation(AOldQualifiedConstructorInvocation node)
	{
		defaultCase(node);
	}
	public void caseAOldSimpleClassInstanceCreationExp(AOldSimpleClassInstanceCreationExp node)
	{
		defaultCase(node);
	}
	public void caseAOldStaticInitializerClassBodyDeclaration(AOldStaticInitializerClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAOldSuper(AOldSuper node)
	{
		defaultCase(node);
	}
	public void caseAOldSuperConstructorInvocation(AOldSuperConstructorInvocation node)
	{
		defaultCase(node);
	}
	public void caseAOldThisConstructorInvocation(AOldThisConstructorInvocation node)
	{
		defaultCase(node);
	}
	public void caseAOldThrows(AOldThrows node)
	{
		defaultCase(node);
	}
	public void caseAOneBreakStmt(AOneBreakStmt node)
	{
		defaultCase(node);
	}
	public void caseAOneContinueStmt(AOneContinueStmt node)
	{
		defaultCase(node);
	}
	public void caseAOneDoStmt(AOneDoStmt node)
	{
		defaultCase(node);
	}
	public void caseAOneForStmt(AOneForStmt node)
	{
		defaultCase(node);
	}
	public void caseAOneQualifiedName(AOneQualifiedName node)
	{
		defaultCase(node);
	}
	public void caseAOneReturnStmt(AOneReturnStmt node)
	{
		defaultCase(node);
	}
	public void caseAOneSimpleName(AOneSimpleName node)
	{
		defaultCase(node);
	}
	public void caseAOneSingleTypeImportDeclaration(AOneSingleTypeImportDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAOneSwitchStmt(AOneSwitchStmt node)
	{
		defaultCase(node);
	}
	public void caseAOneSynchronizedStmt(AOneSynchronizedStmt node)
	{
		defaultCase(node);
	}
	public void caseAOneThrowStmt(AOneThrowStmt node)
	{
		defaultCase(node);
	}
	public void caseAOneTypeImportOnDemandDeclaration(AOneTypeImportOnDemandDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAOneWhileStmt(AOneWhileStmt node)
	{
		defaultCase(node);
	}
	public void caseAOrBinaryOperator(AOrBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAOriginalExpStmt(AOriginalExpStmt node)
	{
		defaultCase(node);
	}
	public void caseAPackageDeclaration(APackageDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAParExp(AParExp node)
	{
		defaultCase(node);
	}
	public void caseAPlusAdditiveExp(APlusAdditiveExp node)
	{
		defaultCase(node);
	}
	public void caseAPlusAssignAssignmentOperator(APlusAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseAPlusBinaryOperator(APlusBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAPlusUnaryExp(APlusUnaryExp node)
	{
		defaultCase(node);
	}
	public void caseAPlusUnaryOperator(APlusUnaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAPostDecrementExp(APostDecrementExp node)
	{
		defaultCase(node);
	}
	public void caseAPostDecrementExpPostfixExp(APostDecrementExpPostfixExp node)
	{
		defaultCase(node);
	}
	public void caseAPostDecrementExpr(APostDecrementExpr node)
	{
		defaultCase(node);
	}
	public void caseAPostDecrementExpStmtExp(APostDecrementExpStmtExp node)
	{
		defaultCase(node);
	}
	public void caseAPostfixExpUnaryExpNotPlusMinus(APostfixExpUnaryExpNotPlusMinus node)
	{
		defaultCase(node);
	}
	public void caseAPostIncrementExp(APostIncrementExp node)
	{
		defaultCase(node);
	}
	public void caseAPostIncrementExpPostfixExp(APostIncrementExpPostfixExp node)
	{
		defaultCase(node);
	}
	public void caseAPostIncrementExpr(APostIncrementExpr node)
	{
		defaultCase(node);
	}
	public void caseAPostIncrementExpStmtExp(APostIncrementExpStmtExp node)
	{
		defaultCase(node);
	}
	public void caseAPreDecrementExp(APreDecrementExp node)
	{
		defaultCase(node);
	}
	public void caseAPreDecrementExpStmtExp(APreDecrementExpStmtExp node)
	{
		defaultCase(node);
	}
	public void caseAPreDecrementExpUnaryExp(APreDecrementExpUnaryExp node)
	{
		defaultCase(node);
	}
	public void caseAPreIncrementExp(APreIncrementExp node)
	{
		defaultCase(node);
	}
	public void caseAPreIncrementExpStmtExp(APreIncrementExpStmtExp node)
	{
		defaultCase(node);
	}
	public void caseAPreIncrementExpUnaryExp(APreIncrementExpUnaryExp node)
	{
		defaultCase(node);
	}
	public void caseAPrimaryFieldAccess(APrimaryFieldAccess node)
	{
		defaultCase(node);
	}
	public void caseAPrimaryMethodInvocation(APrimaryMethodInvocation node)
	{
		defaultCase(node);
	}
	public void caseAPrimaryMethodInvocationExp(APrimaryMethodInvocationExp node)
	{
		defaultCase(node);
	}
	public void caseAPrimaryNoNewArrayArrayAccess(APrimaryNoNewArrayArrayAccess node)
	{
		defaultCase(node);
	}
	public void caseAPrimaryNoNewArrayPrimary(APrimaryNoNewArrayPrimary node)
	{
		defaultCase(node);
	}
	public void caseAPrimaryPostfixExp(APrimaryPostfixExp node)
	{
		defaultCase(node);
	}
	public void caseAPrimitiveArrayType(APrimitiveArrayType node)
	{
		defaultCase(node);
	}
	public void caseAPrimitiveType(APrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseAPrimitiveTypeArrayCreationExp(APrimitiveTypeArrayCreationExp node)
	{
		defaultCase(node);
	}
	public void caseAPrimitiveTypeArrayExp(APrimitiveTypeArrayExp node)
	{
		defaultCase(node);
	}
	public void caseAPrimitiveTypeCastExp(APrimitiveTypeCastExp node)
	{
		defaultCase(node);
	}
	public void caseAPrimitiveTypePrimaryExp(APrimitiveTypePrimaryExp node)
	{
		defaultCase(node);
	}
	public void caseAPrivateModifier(APrivateModifier node)
	{
		defaultCase(node);
	}
	public void caseAProtectedModifier(AProtectedModifier node)
	{
		defaultCase(node);
	}
	public void caseAPublicModifier(APublicModifier node)
	{
		defaultCase(node);
	}
	public void caseAQualifiedClassInstanceCreationExp(AQualifiedClassInstanceCreationExp node)
	{
		defaultCase(node);
	}
	public void caseAQualifiedConstructorInvocation(AQualifiedConstructorInvocation node)
	{
		defaultCase(node);
	}
	public void caseAQualifiedName(AQualifiedName node)
	{
		defaultCase(node);
	}
	public void caseAQualifiedNameName(AQualifiedNameName node)
	{
		defaultCase(node);
	}
	public void caseAQualifiedThisExp(AQualifiedThisExp node)
	{
		defaultCase(node);
	}
	public void caseAQualifiedThisPrimaryNoNewArray(AQualifiedThisPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAQuestionConditionalExp(AQuestionConditionalExp node)
	{
		defaultCase(node);
	}
	public void caseAQuestionExp(AQuestionExp node)
	{
		defaultCase(node);
	}
	public void caseAReferenceType(AReferenceType node)
	{
		defaultCase(node);
	}
	public void caseARelationalExpEqualityExp(ARelationalExpEqualityExp node)
	{
		defaultCase(node);
	}
	public void caseAReturnStmt(AReturnStmt node)
	{
		defaultCase(node);
	}
	public void caseAReturnStmtStmtWithoutTrailingSubstmt(AReturnStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseASemicolonStmt(ASemicolonStmt node)
	{
		defaultCase(node);
	}
	public void caseAShiftExpRelationalExp(AShiftExpRelationalExp node)
	{
		defaultCase(node);
	}
	public void caseAShiftLeftAssignAssignmentOperator(AShiftLeftAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseAShiftLeftBinaryOperator(AShiftLeftBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAShiftLeftShiftExp(AShiftLeftShiftExp node)
	{
		defaultCase(node);
	}
	public void caseAShortIntegralType(AShortIntegralType node)
	{
		defaultCase(node);
	}
	public void caseAShortPrimitiveType(AShortPrimitiveType node)
	{
		defaultCase(node);
	}
	public void caseASignedShiftRightAssignAssignmentOperator(ASignedShiftRightAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseASignedShiftRightBinaryOperator(ASignedShiftRightBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseASignedShiftRightShiftExp(ASignedShiftRightShiftExp node)
	{
		defaultCase(node);
	}
	public void caseASimpleClassInstanceCreationExp(ASimpleClassInstanceCreationExp node)
	{
		defaultCase(node);
	}
	public void caseASimpleName(ASimpleName node)
	{
		defaultCase(node);
	}
	public void caseASimpleNameName(ASimpleNameName node)
	{
		defaultCase(node);
	}
	public void caseASingleTypeImportDeclaration(ASingleTypeImportDeclaration node)
	{
		defaultCase(node);
	}
	public void caseASingleTypeImportDeclarationImportDeclaration(ASingleTypeImportDeclarationImportDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAStarAssignAssignmentOperator(AStarAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseAStarBinaryOperator(AStarBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAStarMultiplicativeExp(AStarMultiplicativeExp node)
	{
		defaultCase(node);
	}
	public void caseAStaticInitializer(AStaticInitializer node)
	{
		defaultCase(node);
	}
	public void caseAStaticInitializerClassBodyDeclaration(AStaticInitializerClassBodyDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAStaticModifier(AStaticModifier node)
	{
		defaultCase(node);
	}
	public void caseAStmtBlockedStmt(AStmtBlockedStmt node)
	{
		defaultCase(node);
	}
	public void caseAStmtExpListForInit(AStmtExpListForInit node)
	{
		defaultCase(node);
	}
	public void caseAStmtExpListStmtExpList(AStmtExpListStmtExpList node)
	{
		defaultCase(node);
	}
	public void caseAStmtExpStmtExpList(AStmtExpStmtExpList node)
	{
		defaultCase(node);
	}
	public void caseAStmtWithoutTrailingSubstmtStmt(AStmtWithoutTrailingSubstmtStmt node)
	{
		defaultCase(node);
	}
	public void caseAStmtWithoutTrailingSubstmtStmtNoShortIf(AStmtWithoutTrailingSubstmtStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseAStringLiteralLiteral(AStringLiteralLiteral node)
	{
		defaultCase(node);
	}
	public void caseASuper(ASuper node)
	{
		defaultCase(node);
	}
	public void caseASuperConstructorInvocation(ASuperConstructorInvocation node)
	{
		defaultCase(node);
	}
	public void caseASuperFieldAccess(ASuperFieldAccess node)
	{
		defaultCase(node);
	}
	public void caseASuperMethodInvocation(ASuperMethodInvocation node)
	{
		defaultCase(node);
	}
	public void caseASuperMethodInvocationExp(ASuperMethodInvocationExp node)
	{
		defaultCase(node);
	}
	public void caseASwitchBlockStmtGroup(ASwitchBlockStmtGroup node)
	{
		defaultCase(node);
	}
	public void caseASwitchStmt(ASwitchStmt node)
	{
		defaultCase(node);
	}
	public void caseASwitchStmtStmtWithoutTrailingSubstmt(ASwitchStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseASynchronizedModifier(ASynchronizedModifier node)
	{
		defaultCase(node);
	}
	public void caseASynchronizedStmt(ASynchronizedStmt node)
	{
		defaultCase(node);
	}
	public void caseASynchronizedStmtStmtWithoutTrailingSubstmt(ASynchronizedStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseAThisConstructorInvocation(AThisConstructorInvocation node)
	{
		defaultCase(node);
	}
	public void caseAThisExp(AThisExp node)
	{
		defaultCase(node);
	}
	public void caseAThisPrimaryNoNewArray(AThisPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAThrows(AThrows node)
	{
		defaultCase(node);
	}
	public void caseAThrowStmt(AThrowStmt node)
	{
		defaultCase(node);
	}
	public void caseAThrowStmtStmtWithoutTrailingSubstmt(AThrowStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseATransientModifier(ATransientModifier node)
	{
		defaultCase(node);
	}
	public void caseATrueBooleanLiteral(ATrueBooleanLiteral node)
	{
		defaultCase(node);
	}
	public void caseATryFinallyStmt(ATryFinallyStmt node)
	{
		defaultCase(node);
	}
	public void caseATryOneTryStmt(ATryOneTryStmt node)
	{
		defaultCase(node);
	}
	public void caseATryStmt(ATryStmt node)
	{
		defaultCase(node);
	}
	public void caseATryStmtStmtWithoutTrailingSubstmt(ATryStmtStmtWithoutTrailingSubstmt node)
	{
		defaultCase(node);
	}
	public void caseATypedMethodHeader(ATypedMethodHeader node)
	{
		defaultCase(node);
	}
	public void caseATypeImportOnDemandDeclarationImportDeclaration(ATypeImportOnDemandDeclarationImportDeclaration node)
	{
		defaultCase(node);
	}
	public void caseATypeOnDemandImportDeclaration(ATypeOnDemandImportDeclaration node)
	{
		defaultCase(node);
	}
	public void caseAUnaryExp(AUnaryExp node)
	{
		defaultCase(node);
	}
	public void caseAUnaryExpMultiplicativeExp(AUnaryExpMultiplicativeExp node)
	{
		defaultCase(node);
	}
	public void caseAUnsignedShiftRightAssignAssignmentOperator(AUnsignedShiftRightAssignAssignmentOperator node)
	{
		defaultCase(node);
	}
	public void caseAUnsignedShiftRightBinaryOperator(AUnsignedShiftRightBinaryOperator node)
	{
		defaultCase(node);
	}
	public void caseAUnsignedShiftRightShiftExp(AUnsignedShiftRightShiftExp node)
	{
		defaultCase(node);
	}
	public void caseAVariableDeclaratorId(AVariableDeclaratorId node)
	{
		defaultCase(node);
	}
	public void caseAVariableDeclaratorsVariableDeclarators(AVariableDeclaratorsVariableDeclarators node)
	{
		defaultCase(node);
	}
	public void caseAVariableDeclaratorVariableDeclarators(AVariableDeclaratorVariableDeclarators node)
	{
		defaultCase(node);
	}
	public void caseAVariableInitializersVariableInitializers(AVariableInitializersVariableInitializers node)
	{
		defaultCase(node);
	}
	public void caseAVariableInitializerVariableInitializers(AVariableInitializerVariableInitializers node)
	{
		defaultCase(node);
	}
	public void caseAVoidExp(AVoidExp node)
	{
		defaultCase(node);
	}
	public void caseAVoidMethodHeader(AVoidMethodHeader node)
	{
		defaultCase(node);
	}
	public void caseAVoidPrimaryNoNewArray(AVoidPrimaryNoNewArray node)
	{
		defaultCase(node);
	}
	public void caseAVolatileModifier(AVolatileModifier node)
	{
		defaultCase(node);
	}
	public void caseAWhileStmt(AWhileStmt node)
	{
		defaultCase(node);
	}
	public void caseAWhileStmtNoShortIf(AWhileStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseAWhileStmtNoShortIfStmtNoShortIf(AWhileStmtNoShortIfStmtNoShortIf node)
	{
		defaultCase(node);
	}
	public void caseAWhileStmtStmt(AWhileStmtStmt node)
	{
		defaultCase(node);
	}
	public void caseEOF(EOF node)
	{
		defaultCase(node);
	}
	public void caseStart(Start node)
	{
		defaultCase(node);
	}
	public void caseTAbstract(TAbstract node)
	{
		defaultCase(node);
	}
	public void caseTAnd(TAnd node)
	{
		defaultCase(node);
	}
	public void caseTAssign(TAssign node)
	{
		defaultCase(node);
	}
	public void caseTBitAnd(TBitAnd node)
	{
		defaultCase(node);
	}
	public void caseTBitAndAssign(TBitAndAssign node)
	{
		defaultCase(node);
	}
	public void caseTBitComplement(TBitComplement node)
	{
		defaultCase(node);
	}
	public void caseTBitOr(TBitOr node)
	{
		defaultCase(node);
	}
	public void caseTBitOrAssign(TBitOrAssign node)
	{
		defaultCase(node);
	}
	public void caseTBitXor(TBitXor node)
	{
		defaultCase(node);
	}
	public void caseTBitXorAssign(TBitXorAssign node)
	{
		defaultCase(node);
	}
	public void caseTBoolean(TBoolean node)
	{
		defaultCase(node);
	}
	public void caseTBreak(TBreak node)
	{
		defaultCase(node);
	}
	public void caseTByte(TByte node)
	{
		defaultCase(node);
	}
	public void caseTCase(TCase node)
	{
		defaultCase(node);
	}
	public void caseTCatch(TCatch node)
	{
		defaultCase(node);
	}
	public void caseTChar(TChar node)
	{
		defaultCase(node);
	}
	public void caseTCharacterLiteral(TCharacterLiteral node)
	{
		defaultCase(node);
	}
	public void caseTClass(TClass node)
	{
		defaultCase(node);
	}
	public void caseTColon(TColon node)
	{
		defaultCase(node);
	}
	public void caseTComma(TComma node)
	{
		defaultCase(node);
	}
	public void caseTComplement(TComplement node)
	{
		defaultCase(node);
	}
	public void caseTConst(TConst node)
	{
		defaultCase(node);
	}
	public void caseTContinue(TContinue node)
	{
		defaultCase(node);
	}
	public void caseTDecimalIntegerLiteral(TDecimalIntegerLiteral node)
	{
		defaultCase(node);
	}
	public void caseTDefault(TDefault node)
	{
		defaultCase(node);
	}
	public void caseTDiv(TDiv node)
	{
		defaultCase(node);
	}
	public void caseTDivAssign(TDivAssign node)
	{
		defaultCase(node);
	}
	public void caseTDo(TDo node)
	{
		defaultCase(node);
	}
	public void caseTDocumentationComment(TDocumentationComment node)
	{
		defaultCase(node);
	}
	public void caseTDot(TDot node)
	{
		defaultCase(node);
	}
	public void caseTDouble(TDouble node)
	{
		defaultCase(node);
	}
	public void caseTElse(TElse node)
	{
		defaultCase(node);
	}
	public void caseTEndOfLineComment(TEndOfLineComment node)
	{
		defaultCase(node);
	}
	public void caseTEq(TEq node)
	{
		defaultCase(node);
	}
	public void caseTExtends(TExtends node)
	{
		defaultCase(node);
	}
	public void caseTFalse(TFalse node)
	{
		defaultCase(node);
	}
	public void caseTFinal(TFinal node)
	{
		defaultCase(node);
	}
	public void caseTFinally(TFinally node)
	{
		defaultCase(node);
	}
	public void caseTFloat(TFloat node)
	{
		defaultCase(node);
	}
	public void caseTFloatingPointLiteral(TFloatingPointLiteral node)
	{
		defaultCase(node);
	}
	public void caseTFor(TFor node)
	{
		defaultCase(node);
	}
	public void caseTGoto(TGoto node)
	{
		defaultCase(node);
	}
	public void caseTGt(TGt node)
	{
		defaultCase(node);
	}
	public void caseTGteq(TGteq node)
	{
		defaultCase(node);
	}
	public void caseTHexIntegerLiteral(THexIntegerLiteral node)
	{
		defaultCase(node);
	}
	public void caseTId(TId node)
	{
		defaultCase(node);
	}
	public void caseTIf(TIf node)
	{
		defaultCase(node);
	}
	public void caseTImplements(TImplements node)
	{
		defaultCase(node);
	}
	public void caseTImport(TImport node)
	{
		defaultCase(node);
	}
	public void caseTInstanceof(TInstanceof node)
	{
		defaultCase(node);
	}
	public void caseTInt(TInt node)
	{
		defaultCase(node);
	}
	public void caseTInterface(TInterface node)
	{
		defaultCase(node);
	}
	public void caseTLBrace(TLBrace node)
	{
		defaultCase(node);
	}
	public void caseTLBracket(TLBracket node)
	{
		defaultCase(node);
	}
	public void caseTLong(TLong node)
	{
		defaultCase(node);
	}
	public void caseTLPar(TLPar node)
	{
		defaultCase(node);
	}
	public void caseTLt(TLt node)
	{
		defaultCase(node);
	}
	public void caseTLteq(TLteq node)
	{
		defaultCase(node);
	}
	public void caseTMinus(TMinus node)
	{
		defaultCase(node);
	}
	public void caseTMinusAssign(TMinusAssign node)
	{
		defaultCase(node);
	}
	public void caseTMinusMinus(TMinusMinus node)
	{
		defaultCase(node);
	}
	public void caseTMod(TMod node)
	{
		defaultCase(node);
	}
	public void caseTModAssign(TModAssign node)
	{
		defaultCase(node);
	}
	public void caseTNative(TNative node)
	{
		defaultCase(node);
	}
	public void caseTNeq(TNeq node)
	{
		defaultCase(node);
	}
	public void caseTNew(TNew node)
	{
		defaultCase(node);
	}
	public void caseTNull(TNull node)
	{
		defaultCase(node);
	}
	public void caseTOctalIntegerLiteral(TOctalIntegerLiteral node)
	{
		defaultCase(node);
	}
	public void caseTOr(TOr node)
	{
		defaultCase(node);
	}
	public void caseTPackage(TPackage node)
	{
		defaultCase(node);
	}
	public void caseTPlus(TPlus node)
	{
		defaultCase(node);
	}
	public void caseTPlusAssign(TPlusAssign node)
	{
		defaultCase(node);
	}
	public void caseTPlusPlus(TPlusPlus node)
	{
		defaultCase(node);
	}
	public void caseTPrivate(TPrivate node)
	{
		defaultCase(node);
	}
	public void caseTProtected(TProtected node)
	{
		defaultCase(node);
	}
	public void caseTPublic(TPublic node)
	{
		defaultCase(node);
	}
	public void caseTQuestion(TQuestion node)
	{
		defaultCase(node);
	}
	public void caseTRBrace(TRBrace node)
	{
		defaultCase(node);
	}
	public void caseTRBracket(TRBracket node)
	{
		defaultCase(node);
	}
	public void caseTReturn(TReturn node)
	{
		defaultCase(node);
	}
	public void caseTRPar(TRPar node)
	{
		defaultCase(node);
	}
	public void caseTSemicolon(TSemicolon node)
	{
		defaultCase(node);
	}
	public void caseTShiftLeft(TShiftLeft node)
	{
		defaultCase(node);
	}
	public void caseTShiftLeftAssign(TShiftLeftAssign node)
	{
		defaultCase(node);
	}
	public void caseTShort(TShort node)
	{
		defaultCase(node);
	}
	public void caseTSignedShiftRight(TSignedShiftRight node)
	{
		defaultCase(node);
	}
	public void caseTSignedShiftRightAssign(TSignedShiftRightAssign node)
	{
		defaultCase(node);
	}
	public void caseTStar(TStar node)
	{
		defaultCase(node);
	}
	public void caseTStarAssign(TStarAssign node)
	{
		defaultCase(node);
	}
	public void caseTStatic(TStatic node)
	{
		defaultCase(node);
	}
	public void caseTStringLiteral(TStringLiteral node)
	{
		defaultCase(node);
	}
	public void caseTSuper(TSuper node)
	{
		defaultCase(node);
	}
	public void caseTSwitch(TSwitch node)
	{
		defaultCase(node);
	}
	public void caseTSynchronized(TSynchronized node)
	{
		defaultCase(node);
	}
	public void caseTThis(TThis node)
	{
		defaultCase(node);
	}
	public void caseTThrow(TThrow node)
	{
		defaultCase(node);
	}
	public void caseTThrows(TThrows node)
	{
		defaultCase(node);
	}
	public void caseTTraditionalComment(TTraditionalComment node)
	{
		defaultCase(node);
	}
	public void caseTTransient(TTransient node)
	{
		defaultCase(node);
	}
	public void caseTTrue(TTrue node)
	{
		defaultCase(node);
	}
	public void caseTTry(TTry node)
	{
		defaultCase(node);
	}
	public void caseTUnsignedShiftRight(TUnsignedShiftRight node)
	{
		defaultCase(node);
	}
	public void caseTUnsignedShiftRightAssign(TUnsignedShiftRightAssign node)
	{
		defaultCase(node);
	}
	public void caseTVoid(TVoid node)
	{
		defaultCase(node);
	}
	public void caseTVolatile(TVolatile node)
	{
		defaultCase(node);
	}
	public void caseTWhile(TWhile node)
	{
		defaultCase(node);
	}
	public void caseTWhiteSpace(TWhiteSpace node)
	{
		defaultCase(node);
	}
	public void defaultCase(Node node)
	{
	}
/**
 * 
 * @return java.util.Hashtable
 */
public java.util.Hashtable getIn() {
	return in;
}
	public Object getIn(Node node)
	{
		if(in == null)
		{
			return null;
		}

		return in.get(node);
	}
	public Object getOut(Node node)
	{
		if(out == null)
		{
			return null;
		}

		return out.get(node);
	}
	public void setIn(Node node, Object in)
	{
		if(this.in == null)
		{
			this.in = new Hashtable(1);
		}

		if(in != null)
		{
			this.in.put(node, in);
		}
		else
		{
			this.in.remove(node);
		}
	}
	public void setOut(Node node, Object out)
	{
		if(this.out == null)
		{
			this.out = new Hashtable(1);
		}

		if(out != null)
		{
			this.out.put(node, out);
		}
		else
		{
			this.out.remove(node);
		}
	}
}
