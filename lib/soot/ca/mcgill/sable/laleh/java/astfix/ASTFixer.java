package ca.mcgill.sable.laleh.java.asfix;

import edu.ksu.cis.bandera.jjjc.parser.*;
import edu.ksu.cis.bandera.jjjc.lexer.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import ca.mcgill.sable.util.*;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.BitSet;
import java.io.IOException;
class ASTFixer extends AnalysisAdapter {
	Hashtable table = new Hashtable();
	Node result;
	public void caseAAbstractMethodDeclaration(AAbstractMethodDeclaration node) {
		table.put(node, new AAbstractMethodDeclarationInterfaceMemberDeclaration(node.getMethodHeader(), node.getSemicolon()));
		node.setMethodHeader(null);
		node.setSemicolon(null);
	}
	public void caseAAdditiveExpShiftExp(AAdditiveExpShiftExp node) {
		table.put(node, (PExp) table.remove(node.getAdditiveExp()));
		node.setAdditiveExp(null);
	}
	public void caseAAndExpAndExp(AAndExpAndExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getAndExp()), new ABitAndBinaryOperator(node.getBitAnd()), (PExp) table.remove(node.getEqualityExp())));
		node.setAndExp(null);
		node.setBitAnd(null);
		node.setEqualityExp(null);
	}
	public void caseAAndExpExclusiveOrExp(AAndExpExclusiveOrExp node) {
		table.put(node, (PExp) table.remove(node.getAndExp()));
		node.setAndExp(null);
	}
	public void caseAArgumentListArgumentList(AArgumentListArgumentList node) {
		LinkedList list = (LinkedList) table.remove(node.getArgumentList());
		list.add(node.getExp());
		table.put(node, list);
		node.setArgumentList(null);
		node.setComma(null);
		node.setExp(null);
	}
	public void caseAArrayAccessPrimaryNoNewArray(AArrayAccessPrimaryNoNewArray node) {
		table.put(node, new AArrayAccessExp(node.getArrayAccess()));
		node.setArrayAccess(null);
	}
	public void caseAArrayCreationExpPrimary(AArrayCreationExpPrimary node) {
		table.put(node, (PExp) table.remove(node.getArrayCreationExp()));
		node.setArrayCreationExp(null);
	}
	public void caseAAssertionCompilationUnit(AAssertionCompilationUnit node) {
		result = node;
	}
	public void caseAAssignment(AAssignment node) {
		table.put(node, new AAssignmentExp(node.getLeftHandSide(), node.getAssignmentOperator(), (PExp) table.remove(node.getAssignmentExp())));
		node.setLeftHandSide(null);
		node.setAssignmentOperator(null);
		node.setAssignmentExp(null);
	}
	public void caseAAssignmentAssignmentExp(AAssignmentAssignmentExp node) {
		table.put(node, (PExp) table.remove(node.getAssignment()));
		node.setAssignment(null);
	}
	public void caseAAssignmentStmtExp(AAssignmentStmtExp node) {
		table.put(node, (PExp) table.remove(node.getAssignment()));
		node.setAssignment(null);
	}
	public void caseABitComplementUnaryExpNotPlusMinus(ABitComplementUnaryExpNotPlusMinus node) {
		table.put(node, new AUnaryExp(new ABitComplementUnaryOperator(node.getBitComplement()), (PExp) table.remove(node.getUnaryExp())));
		node.setBitComplement(null);
		node.setUnaryExp(null);
	}
	public void caseABlockStmtWithoutTrailingSubstmt(ABlockStmtWithoutTrailingSubstmt node) {
		table.put(node, new ABlockStmt(node.getBlock()));
		node.setBlock(null);
	}
	public void caseABooleanPrimitiveType(ABooleanPrimitiveType node) {
		result = (PPrimitiveType) table.remove(node.getBoolean());
	}
	public void caseABreakStmtStmtWithoutTrailingSubstmt(ABreakStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getOneBreakStmt()));
		node.setOneBreakStmt(null);
	}
	public void caseAByteIntegralType(AByteIntegralType node) {
		table.put(node, new ABytePrimitiveType(node.getByte()));
		node.setByte(null);
	}
	public void caseACastExpUnaryExpNotPlusMinus(ACastExpUnaryExpNotPlusMinus node) {
		table.put(node, (PExp) table.remove(node.getCastExp()));
		node.setCastExp(null);
	}
	public void caseACharIntegralType(ACharIntegralType node) {
		table.put(node, new ACharPrimitiveType(node.getChar()));
		node.setChar(null);
	}
	public void caseAClassDeclarationClassMemberDeclaration(AClassDeclarationClassMemberDeclaration node) {
		table.put(node, new AClassClassBodyDeclaration(node.getClassDeclaration()));
		node.setClassDeclaration(null);
	}
	public void caseAClassInstanceCreationExpPrimaryNoNewArray(AClassInstanceCreationExpPrimaryNoNewArray node) {
		table.put(node, (PExp) table.remove(node.getClassInstanceCreationExp()));
		node.setClassInstanceCreationExp(null);
	}
	public void caseAClassInstanceCreationExpStmtExp(AClassInstanceCreationExpStmtExp node) {
		table.put(node, (PExp) table.remove(node.getClassInstanceCreationExp()));
		node.setClassInstanceCreationExp(null);
	}
	public void caseAClassMemberDeclarationClassBodyDeclaration(AClassMemberDeclarationClassBodyDeclaration node) {
		result = (PClassBodyDeclaration) table.remove(node.getClassMemberDeclaration());
	}
	public void caseAClassOrInterfaceType(AClassOrInterfaceType node) {
		table.put(node, node.getName());
		node.setName(null);
	}
	public void caseAClassOrInterfaceTypeArrayCreationExp(AClassOrInterfaceTypeArrayCreationExp node) {
		table.put(node, new AClassOrInterfaceTypeExp(node.getNew(), (PName) table.remove(node.getClassOrInterfaceType()), node.getDimExp(), node.getDim()));
		node.setNew(null);
		node.setClassOrInterfaceType(null);
		node.setDimExp(new LinkedList());
		node.setDim(new LinkedList());
	}
	public void caseAClassOrInterfaceTypeReferenceType(AClassOrInterfaceTypeReferenceType node) {
		result = new ANameReferenceType((PName) table.remove(node.getClassOrInterfaceType()));
	}
	public void caseAClassType(AClassType node) {
		table.put(node, (PName) table.remove(node.getClassOrInterfaceType()));
		node.setClassOrInterfaceType(null);
	}
	public void caseAClassTypeClassTypeList(AClassTypeClassTypeList node) {
		LinkedList list = new LinkedList();
		list.add((PName) table.remove(node.getClassType()));
		table.put(node, list);
		node.setClassType(null);
	}
	public void caseAClassTypeListClassTypeList(AClassTypeListClassTypeList node) {
		LinkedList list = (LinkedList) table.remove(node.getClassTypeList());
		list.add((PName) table.remove(node.getClassType()));
		table.put(node, list);
		node.setClassTypeList(null);
		node.setComma(null);
		node.setClassType(null);
	}
	public void caseAComplementUnaryExpNotPlusMinus(AComplementUnaryExpNotPlusMinus node) {
		table.put(node, new AUnaryExp(new AComplementUnaryOperator(node.getComplement()), (PExp) table.remove(node.getUnaryExp())));
		node.setComplement(null);
		node.setUnaryExp(null);
	}
	public void caseAConditionalAndExpConditionalAndExp(AConditionalAndExpConditionalAndExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getConditionalAndExp()), new AAndBinaryOperator(node.getAnd()), (PExp) table.remove(node.getInclusiveOrExp())));
		node.setConditionalAndExp(null);
		node.setAnd(null);
		node.setInclusiveOrExp(null);
	}
	public void caseAConditionalAndExpConditionalOrExp(AConditionalAndExpConditionalOrExp node) {
		table.put(node, (PExp) table.remove(node.getConditionalAndExp()));
		node.setConditionalAndExp(null);
	}
	public void caseAConditionalExpAssignmentExp(AConditionalExpAssignmentExp node) {
		table.put(node, (PExp) table.remove(node.getConditionalExp()));
		node.setConditionalExp(null);
	}
	public void caseAConditionalOrExpConditionalExp(AConditionalOrExpConditionalExp node) {
		table.put(node, (PExp) table.remove(node.getConditionalOrExp()));
		node.setConditionalOrExp(null);
	}
	public void caseAConditionalOrExpConditionalOrExp(AConditionalOrExpConditionalOrExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getConditionalOrExp()), new AOrBinaryOperator(node.getOr()), (PExp) table.remove(node.getConditionalAndExp())));
		node.setConditionalOrExp(null);
		node.setOr(null);
		node.setConditionalAndExp(null);
	}
	public void caseAConstantDeclaration(AConstantDeclaration node) {
		table.put(node, new AConstantDeclarationInterfaceMemberDeclaration(node.getFieldDeclaration()));
		node.setFieldDeclaration(null);
	}
	public void caseAConstantExp(AConstantExp node) {
		table.put(node, node.getExp());
		node.setExp(null);
	}
	public void caseAContinueStmtStmtWithoutTrailingSubstmt(AContinueStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getOneContinueStmt()));
		node.setOneContinueStmt(null);
	}
	public void caseADivMultiplicativeExp(ADivMultiplicativeExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getMultiplicativeExp()), new ADivBinaryOperator(node.getDiv()), (PExp) table.remove(node.getUnaryExp())));
		node.setMultiplicativeExp(null);
		node.setDiv(null);
		node.setUnaryExp(null);
	}
	public void caseADoStmtStmtWithoutTrailingSubstmt(ADoStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getOneDoStmt()));
		node.setOneDoStmt(null);
	}
	public void caseADoubleFloatingPointType(ADoubleFloatingPointType node) {
		table.put(node, new ADoublePrimitiveType(node.getDouble()));
		node.setDouble(null);
	}
	public void caseAEmptyStmtStmtWithoutTrailingSubstmt(AEmptyStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getSemicolonStmt()));
		node.setSemicolonStmt(null);
	}
	public void caseAEmptyTypeDeclaration(AEmptyTypeDeclaration node) {
		result = null;
	}
	public void caseAEqEqualityExp(AEqEqualityExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getEqualityExp()), new AEqBinaryOperator(node.getEq()), (PExp) table.remove(node.getRelationalExp())));
		node.setEqualityExp(null);
		node.setEq(null);
		node.setRelationalExp(null);
	}
	public void caseAEqualityExpAndExp(AEqualityExpAndExp node) {
		table.put(node, (PExp) table.remove(node.getEqualityExp()));
		node.setEqualityExp(null);
	}
	public void caseAExclusiveOrExpExclusiveOrExp(AExclusiveOrExpExclusiveOrExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getExclusiveOrExp()), new ABitXorBinaryOperator(node.getBitXor()), (PExp) table.remove(node.getAndExp())));
		node.setExclusiveOrExp(null);
		node.setBitXor(null);
		node.setAndExp(null);
	}
	public void caseAExclusiveOrExpInclusiveOrExp(AExclusiveOrExpInclusiveOrExp node) {
		table.put(node, (PExp) table.remove(node.getExclusiveOrExp()));
		node.setExclusiveOrExp(null);
	}
	public void caseAExpArgumentList(AExpArgumentList node) {
		LinkedList list = new LinkedList();
		list.add(node.getExp());
		table.put(node, list);
		node.setExp(null);
	}
	public void caseAExpStmtStmtWithoutTrailingSubstmt(AExpStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, new AExpStmt((PExp) table.remove(node.getExpStmt()), new TSemicolon()));
		node.setExpStmt(null);
	}
	public void caseAExtendsExtendsInterfaces(AExtendsExtendsInterfaces node) {
		LinkedList list = new LinkedList();
		list.add((PName) table.remove(node.getInterfaceType()));
		table.put(node, list);
		node.setExtends(null);
		node.setInterfaceType(null);
	}
	public void caseAExtendsInterfacesExtendsInterfaces(AExtendsInterfacesExtendsInterfaces node) {
		LinkedList list = (LinkedList) table.remove(node.getExtendsInterfaces());
		list.add((PName) table.remove(node.getInterfaceType()));
		table.put(node, list);
		node.setExtendsInterfaces(null);
		node.setComma(null);
		node.setInterfaceType(null);
	}
	public void caseAFieldAccessPrimaryNoNewArray(AFieldAccessPrimaryNoNewArray node) {
		table.put(node, new AFieldAccessExp(node.getFieldAccess()));
		node.setFieldAccess(null);
	}
	public void caseAFieldDeclarationClassMemberDeclaration(AFieldDeclarationClassMemberDeclaration node) {
		table.put(node, new AFieldClassBodyDeclaration(node.getFieldDeclaration()));
		node.setFieldDeclaration(null);
	}
	public void caseAFinallyOneTryStmt(AFinallyOneTryStmt node) {
		table.put(node, new ATryFinallyStmt(node.getTry(), node.getBlock(), node.getCatchClause(), node.getFinally()));
		node.setTry(null);
		node.setBlock(null);
		node.setCatchClause((new LinkedList()));
		node.setFinally(null);
	}
	public void caseAFloatFloatingPointType(AFloatFloatingPointType node) {
		table.put(node, new AFloatPrimitiveType(node.getFloat()));
		node.setFloat(null);
	}
	public void caseAFloatingPointTypeNumericType(AFloatingPointTypeNumericType node) {
		table.put(node, (PPrimitiveType) table.remove(node.getFloatingPointType()));
		node.setFloatingPointType(null);
	}
	public void caseAFormalParameterFormalParameterList(AFormalParameterFormalParameterList node) {
		LinkedList list = new LinkedList();
		list.add(node.getFormalParameter());
		table.put(node, list);
		node.setFormalParameter(null);
	}
	public void caseAFormalParameterListFormalParameterList(AFormalParameterListFormalParameterList node) {
		LinkedList list = (LinkedList) table.remove(node.getFormalParameterList());
		list.add(node.getFormalParameter());
		table.put(node, list);
		node.setFormalParameterList(null);
		node.setComma(null);
		node.setFormalParameter(null);
	}
	public void caseAForStmtNoShortIf(AForStmtNoShortIf node) {
		// list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list = new LinkedList();
		list.add(new AStmtBlockedStmt((PStmt) table.remove(node.getStmtNoShortIf())));
		if (node.getForUpdate() == null)
			table.put(node, new AForStmt(node.getFor(), node.getLPar(), node.getForInit(), node.getSemicolon1(), node.getExp(), node.getSemicolon2(), new LinkedList(), node.getRPar(), new ABlock(new TLBrace(), list, new TRBrace())));
		else
			table.put(node, new AForStmt(node.getFor(), node.getLPar(), node.getForInit(), node.getSemicolon1(), node.getExp(), node.getSemicolon2(), (LinkedList) table.remove(node.getForUpdate()), node.getRPar(), new ABlock(new TLBrace(), list, new TRBrace())));
		node.setFor(null);
		node.setLPar(null);
		node.setForInit(null);
		node.setSemicolon1(null);
		node.setExp(null);
		node.setSemicolon2(null);
		node.setForUpdate(null);
		node.setRPar(null);
		node.setStmtNoShortIf(null);
	}
	public void caseAForStmtNoShortIfStmtNoShortIf(AForStmtNoShortIfStmtNoShortIf node) {
		table.put(node, (PStmt) table.remove(node.getForStmtNoShortIf()));
		node.setForStmtNoShortIf(null);
	}
	public void caseAForStmtStmt(AForStmtStmt node) {
		result = (PStmt) table.remove(node.getOneForStmt());
	}
	public void caseAForUpdate(AForUpdate node) {
		table.put(node, (LinkedList) table.remove(node.getStmtExpList()));
	}
	public void caseAGteqRelationalExp(AGteqRelationalExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getRelationalExp()), new AGteqBinaryOperator(node.getGteq()), (PExp) table.remove(node.getShiftExp())));
		node.setRelationalExp(null);
		node.setGteq(null);
		node.setShiftExp(null);
	}
	public void caseAGtRelationalExp(AGtRelationalExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getRelationalExp()), new AGtBinaryOperator(node.getGt()), (PExp) table.remove(node.getShiftExp())));
		node.setRelationalExp(null);
		node.setGt(null);
		node.setShiftExp(null);
	}
	public void caseAIfThenElseStmt(AIfThenElseStmt node) {
		// list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list1 = new LinkedList();
		LinkedList list2 = new LinkedList();
		list1.add(new AStmtBlockedStmt((PStmt) table.remove(node.getStmtNoShortIf())));
		list2.add(new AStmtBlockedStmt(node.getStmt()));
		table.put(node, new AIfStmt(node.getIf(), node.getLPar(), node.getExp(), node.getRPar(), new ABlock(new TLBrace(), list1, new TRBrace()), node.getElse(), new ABlock(new TLBrace(), list2, new TRBrace())));
		node.setIf(null);
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
		node.setStmtNoShortIf(null);
		node.setElse(null);
		node.setStmt(null);
	}
	public void caseAIfThenElseStmtNoShortIf(AIfThenElseStmtNoShortIf node) {
		// list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list1 = new LinkedList();
		LinkedList list2 = new LinkedList();
		list1.add(new AStmtBlockedStmt((PStmt) table.remove(node.getStmtNoShortIf1())));
		list2.add(new AStmtBlockedStmt((PStmt) table.remove(node.getStmtNoShortIf2())));
		table.put(node, new AIfStmt(node.getIf(), node.getLPar(), node.getExp(), node.getRPar(), new ABlock(new TLBrace(), list1, new TRBrace()), node.getElse(), new ABlock(new TLBrace(), list2, new TRBrace())));
		node.setIf(null);
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
		node.setStmtNoShortIf1(null);
		node.setElse(null);
		node.setStmtNoShortIf2(null);
	}
	public void caseAIfThenElseStmtNoShortIfStmtNoShortIf(AIfThenElseStmtNoShortIfStmtNoShortIf node) {
		table.put(node, (PStmt) table.remove(node.getIfThenElseStmtNoShortIf()));
		node.setIfThenElseStmtNoShortIf(null);
	}
	public void caseAIfThenElseStmtStmt(AIfThenElseStmtStmt node) {
		result = (PStmt) table.remove(node.getIfThenElseStmt());
	}
	public void caseAIfThenStmt(AIfThenStmt node) {
		LinkedList list = new LinkedList();
		list.add(new AStmtBlockedStmt(node.getStmt()));
		table.put(node, new AIfStmt(node.getIf(), node.getLPar(), node.getExp(), node.getRPar(), new ABlock(new TLBrace(), list, new TRBrace()), new TElse(), new ABlock(new TLBrace(), new LinkedList(), new TRBrace())));
		node.setIf(null);
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
		node.setStmt(null);
	}
	public void caseAIfThenStmtStmt(AIfThenStmtStmt node) {
		result = (PStmt) table.remove(node.getIfThenStmt());
	}
	public void caseAInclusiveOrExpConditionalAndExp(AInclusiveOrExpConditionalAndExp node) {
		table.put(node, (PExp) table.remove(node.getInclusiveOrExp()));
		node.setInclusiveOrExp(null);
	}
	public void caseAInclusiveOrExpInclusiveOrExp(AInclusiveOrExpInclusiveOrExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getInclusiveOrExp()), new ABitOrBinaryOperator(node.getBitOr()), (PExp) table.remove(node.getExclusiveOrExp())));
		node.setInclusiveOrExp(null);
		node.setBitOr(null);
		node.setExclusiveOrExp(null);
	}
	public void caseAInitClassInterfaceArrayCreationExp(AInitClassInterfaceArrayCreationExp node) {
		table.put(node, new AInitClassInterfaceExp(node.getNew(), (PName) table.remove(node.getClassOrInterfaceType()), node.getDim(), node.getArrayInitializer()));
		node.setNew(null);
		node.setClassOrInterfaceType(null);
		node.setDim(new LinkedList());
		node.setArrayInitializer(null);
	}
	public void caseAInitPrimitiveArrayCreationExp(AInitPrimitiveArrayCreationExp node) {
		table.put(node, new AInitPrimitiveExp(node.getNew(), node.getPrimitiveType(), node.getDim(), node.getArrayInitializer()));
		node.setNew(null);
		node.setPrimitiveType(null);
		node.setDim(new LinkedList());
		node.setArrayInitializer(null);
	}
	public void caseAInstanceofRelationalExp(AInstanceofRelationalExp node) {
		table.put(node, new AInstanceofExp((PExp) table.remove(node.getRelationalExp()), node.getInstanceof(), node.getReferenceType()));
		node.setRelationalExp(null);
		node.setInstanceof(null);
		node.setReferenceType(null);
	}
	public void caseAIntegralTypeNumericType(AIntegralTypeNumericType node) {
		table.put(node, (PPrimitiveType) table.remove(node.getIntegralType()));
		node.setIntegralType(null);
	}
	public void caseAInterfaceDeclarationClassMemberDeclaration(AInterfaceDeclarationClassMemberDeclaration node) {
		table.put(node, new AInterfaceClassBodyDeclaration(node.getInterfaceDeclaration()));
		node.setInterfaceDeclaration(null);
	}
	public void caseAInterfaceType(AInterfaceType node) {
		table.put(node, (PName) table.remove(node.getClassOrInterfaceType()));
		node.setClassOrInterfaceType(null);
	}
	public void caseAInterfaceTypeInterfaceTypeList(AInterfaceTypeInterfaceTypeList node) {
		LinkedList list = new LinkedList();
		list.add((PName) table.remove(node.getInterfaceType()));
		table.put(node, list);
		node.setInterfaceType(null);
	}
	public void caseAInterfaceTypeListInterfaceTypeList(AInterfaceTypeListInterfaceTypeList node) {
		LinkedList list = (LinkedList) table.remove(node.getInterfaceTypeList());
		list.add((PName) table.remove(node.getInterfaceType()));
		table.put(node, list);
		node.setInterfaceType(null);
		node.setComma(null);
		node.setInterfaceTypeList(null);
	}
	public void caseAIntIntegralType(AIntIntegralType node) {
		table.put(node, new AIntPrimitiveType(node.getInt()));
		node.setInt(null);
	}
	public void caseALabeledStmt(ALabeledStmt node) {
		// list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list = new LinkedList();
		list.add((new AStmtBlockedStmt(node.getStmt())));
		table.put(node, new ALabelStmt(node.getId(), node.getColon(), new ABlock(new TLBrace(), list, new TRBrace())));
		node.setId(null);
		node.setColon(null);
		node.setStmt(null);
	}
	public void caseALabeledStmtNoShortIf(ALabeledStmtNoShortIf node) {
		//  list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list = new LinkedList();
		list.add(new AStmtBlockedStmt((PStmt) table.remove(node.getStmtNoShortIf())));
		table.put(node, new ALabelStmt(node.getId(), node.getColon(), new ABlock(new TLBrace(), list, new TRBrace())));
		node.setId(null);
		node.setColon(null);
		node.setStmtNoShortIf(null);
	}
	public void caseALabeledStmtNoShortIfStmtNoShortIf(ALabeledStmtNoShortIfStmtNoShortIf node) {
		table.put(node, (PStmt) table.remove(node.getLabeledStmtNoShortIf()));
		node.setLabeledStmtNoShortIf(null);
	}
	public void caseALabeledStmtStmt(ALabeledStmtStmt node) {
		result = (PStmt) table.remove(node.getLabeledStmt());
	}
	public void caseALiteralPrimaryNoNewArray(ALiteralPrimaryNoNewArray node) {
		table.put(node, new ALiteralExp(node.getLiteral()));
		node.setLiteral(null);
	}
	public void caseALocalVariableDeclarationStmt(ALocalVariableDeclarationStmt node) {
		table.put(node, (new ALocalVariableDeclarationInBlockedStmt(node.getLocalVariableDeclaration(), node.getSemicolon())));
		node.setLocalVariableDeclaration(null);
		node.setSemicolon(null);
	}
	public void caseALocalVariableDeclarationStmtBlockedStmt(ALocalVariableDeclarationStmtBlockedStmt node) {
		result = (PBlockedStmt) table.remove(node.getLocalVariableDeclarationStmt());
	}
	public void caseALongIntegralType(ALongIntegralType node) {
		table.put(node, new ALongPrimitiveType(node.getLong()));
		node.setLong(null);
	}
	/*public void caseALParPrimaryNoNewArray(ALParPrimaryNoNewArray node)
	{
	table.put(node, new AUnaryExp(
	new APlusUnaryOperator(),
	node.getExp()));
	node.setLPar(null);
	node.setExp(null);
	node.setRPar(null);
	}*/

	public void caseALParPrimaryNoNewArray(ALParPrimaryNoNewArray node) {
		table.put(node, new AParExp(node.getLPar(), node.getExp(), node.getRPar()));
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
	}
	public void caseALteqRelationalExp(ALteqRelationalExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getRelationalExp()), new ALteqBinaryOperator(node.getLteq()), (PExp) table.remove(node.getShiftExp())));
		node.setRelationalExp(null);
		node.setLteq(null);
		node.setShiftExp(null);
	}
	public void caseALtRelationalExp(ALtRelationalExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getRelationalExp()), new ALtBinaryOperator(node.getLt()), (PExp) table.remove(node.getShiftExp())));
		node.setRelationalExp(null);
		node.setLt(null);
		node.setShiftExp(null);
	}
	public void caseAMethodDeclarationClassMemberDeclaration(AMethodDeclarationClassMemberDeclaration node) {
		table.put(node, new AMethodClassBodyDeclaration(node.getMethodDeclaration()));
		node.setMethodDeclaration(null);
	}
	public void caseAMethodInvocationPrimaryNoNewArray(AMethodInvocationPrimaryNoNewArray node) {
		table.put(node, (PExp) table.remove(node.getMethodInvocation()));
		node.setMethodInvocation(null);
	}
	public void caseAMethodInvocationStmtExp(AMethodInvocationStmtExp node) {
		table.put(node, (PExp) table.remove(node.getMethodInvocation()));
		node.setMethodInvocation(null);
	}
	public void caseAMinusAdditiveExp(AMinusAdditiveExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getAdditiveExp()), new AMinusBinaryOperator(node.getMinus()), (PExp) table.remove(node.getMultiplicativeExp())));
		node.setAdditiveExp(null);
		node.setMinus(null);
		node.setMultiplicativeExp(null);
	}
	public void caseAMinusUnaryExp(AMinusUnaryExp node) {
		table.put(node, new AUnaryExp(new AMinusUnaryOperator(node.getMinus()), (PExp) table.remove(node.getUnaryExp())));
		node.setUnaryExp(null);
		node.setMinus(null);
	}
	public void caseAModMultiplicativeExp(AModMultiplicativeExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getMultiplicativeExp()), new AModBinaryOperator(node.getMod()), (PExp) table.remove(node.getUnaryExp())));
		node.setMultiplicativeExp(null);
		node.setMod(null);
		node.setUnaryExp(null);
	}
	public void caseAMultiplicativeExpAdditiveExp(AMultiplicativeExpAdditiveExp node) {
		table.put(node, (PExp) table.remove(node.getMultiplicativeExp()));
		node.setMultiplicativeExp(null);
	}
	public void caseANameMethodInvocation(ANameMethodInvocation node) {
		if (node.getArgumentList() != null)
			table.put(node, new ANameMethodInvocationExp(node.getName(), node.getLPar(), (LinkedList) table.remove(node.getArgumentList()), node.getRPar()));
		else
			table.put(node, new ANameMethodInvocationExp(node.getName(), node.getLPar(), new LinkedList(), node.getRPar()));
	}
	public void caseANamePostfixExp(ANamePostfixExp node) {
		table.put(node, new ANameExp(node.getName()));
		node.setName(null);
	}
	public void caseANeqEqualityExp(ANeqEqualityExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getEqualityExp()), new ANeqBinaryOperator(node.getNeq()), (PExp) table.remove(node.getRelationalExp())));
		node.setEqualityExp(null);
		node.setNeq(null);
		node.setRelationalExp(null);
	}
	public void caseANotPlusMinusUnaryExp(ANotPlusMinusUnaryExp node) {
		table.put(node, (PExp) table.remove(node.getUnaryExpNotPlusMinus()));
		node.setUnaryExpNotPlusMinus(null);
	}
	public void caseANumericTypePrimitiveType(ANumericTypePrimitiveType node) {
		result = (PPrimitiveType) table.remove(node.getNumericType());
	}
	public void caseAOldAbstractMethodDeclarationInterfaceMemberDeclaration(AOldAbstractMethodDeclarationInterfaceMemberDeclaration node) {
		result = (PInterfaceMemberDeclaration) table.remove(node.getAbstractMethodDeclaration());
		node.setAbstractMethodDeclaration(null);
	}
	public void caseAOldArrayInitializer(AOldArrayInitializer node) {
		if (node.getVariableInitializers() != null)
			result = new AArrayInitializer(node.getLBrace(), (LinkedList) table.remove(node.getVariableInitializers()), new TComma(), node.getRBrace());
		else
			result = new AArrayInitializer(node.getLBrace(), new LinkedList(), new TComma(), node.getRBrace());
	}
	public void caseAOldCaseSwitchLabel(AOldCaseSwitchLabel node) {
		result = new ACaseSwitchLabel(node.getCase(), (PExp) table.remove(node.getConstantExp()), node.getColon());
	}
	public void caseAOldCompilationUnit(AOldCompilationUnit node) {
		if (node.getPackageDeclaration() != null)
			result = new ACompilationUnit((new TPackage()), ((PName) table.remove(node.getPackageDeclaration())), (new TSemicolon()), node.getImportDeclaration(), node.getTypeDeclaration());
		else
			result = new ACompilationUnit(null, null, null, node.getImportDeclaration(), node.getTypeDeclaration());
	}
	public void caseAOldConstantDeclarationInterfaceMemberDeclaration(AOldConstantDeclarationInterfaceMemberDeclaration node) {
		result = (PInterfaceMemberDeclaration) table.remove(node.getConstantDeclaration());
		node.setConstantDeclaration(null);
	}
	public void caseAOldConstructorDeclarator(AOldConstructorDeclarator node) {
		if (node.getFormalParameterList() != null)
			result = new AConstructorDeclarator(node.getId(), new TLPar(), (LinkedList) table.remove(node.getFormalParameterList()), new TRPar());
		else
			result = new AConstructorDeclarator(node.getId(), (new TLPar()), new LinkedList(), new TRPar());
	}
	public void caseAOldExp(AOldExp node) {
		result = (PExp) table.remove(node.getAssignmentExp());
	}
	public void caseAOldExpCastExp(AOldExpCastExp node) {
		table.put(node, new AExpCastExp(node.getLPar(), node.getExp(), node.getRPar(), (PExp) table.remove(node.getUnaryExpNotPlusMinus())));
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
		node.setUnaryExpNotPlusMinus(null);
	}
	public void caseAOldFieldDeclaration(AOldFieldDeclaration node) {
		result = new AFieldDeclaration(node.getModifier(), node.getType(), (LinkedList) table.remove(node.getVariableDeclarators()), (new TSemicolon()));
	}
	public void caseAOldInterfaceDeclaration(AOldInterfaceDeclaration node) {
		if (node.getExtendsInterfaces() != null)
			result = new AInterfaceDeclaration(node.getModifier(), node.getInterface(), node.getId(), new TExtends(), (LinkedList) table.remove(node.getExtendsInterfaces()), node.getInterfaceBody());
		else
			result = new AInterfaceDeclaration(node.getModifier(), node.getInterface(), node.getId(), null, new LinkedList(), node.getInterfaceBody());
	}
	public void caseAOldInterfaces(AOldInterfaces node) {
		result = new AInterfaces((new TImplements()), (LinkedList) table.remove(node.getInterfaceTypeList()));
	}
	public void caseAOldLocalVariableDeclaration(AOldLocalVariableDeclaration node) {
		result = new ALocalVariableDeclaration(node.getModifier(), node.getType(), (LinkedList) table.remove(node.getVariableDeclarators()));
		node.setModifier((new LinkedList()));
		node.setType(null);
		node.setVariableDeclarators(null);
	}
	public void caseAOldMethodDeclarator(AOldMethodDeclarator node) {
		if (node.getFormalParameterList() != null)
			result = new AMethodDeclarator(node.getId(), new TLPar(), (LinkedList) table.remove(node.getFormalParameterList()), new TRPar(), node.getDim());
		else
			result = new AMethodDeclarator(node.getId(), new TLPar(), new LinkedList(), new TRPar(), node.getDim());
	}
	public void caseAOldNameCastExp(AOldNameCastExp node) {
		table.put(node, new ANameCastExp(node.getLPar(), node.getName(), node.getDim(), node.getRPar(), (PExp) table.remove(node.getUnaryExpNotPlusMinus())));
		node.setLPar(null);
		node.setName(null);
		node.setDim(new LinkedList());
		node.setRPar(null);
		node.setUnaryExpNotPlusMinus(null);
	}
	public void caseAOldNamedTypePrimaryNoNewArray(AOldNamedTypePrimaryNoNewArray node) {
		table.put(node, new ANamedTypeExp(node.getName(), node.getDim(), node.getDot(), node.getTClass()));
		node.setName(null);
		node.setDim(new LinkedList());
		node.setDot(null);
		node.setTClass(null);
	}
	public void caseAOldPrimaryFieldAccess(AOldPrimaryFieldAccess node) {
		result = new APrimaryFieldAccess((PExp) table.remove(node.getPrimary()), node.getDot(), node.getId());
	}
	public void caseAOldPrimaryNoNewArrayArrayAccess(AOldPrimaryNoNewArrayArrayAccess node) {
		result = new APrimaryNoNewArrayArrayAccess((PExp) table.remove(node.getPrimaryNoNewArray()), node.getLBracket(), node.getExp(), node.getRBracket());
	}
	public void caseAOldPrimitiveTypeCastExp(AOldPrimitiveTypeCastExp node) {
		table.put(node, new APrimitiveTypeCastExp(node.getLPar(), node.getPrimitiveType(), node.getDim(), node.getRPar(), (PExp) table.remove(node.getUnaryExp())));
		node.setLPar(null);
		node.setPrimitiveType(null);
		node.setDim(new LinkedList());
		node.setRPar(null);
		node.setUnaryExp(null);
	}
	public void caseAOldPrimitiveTypePrimaryNoNewArray(AOldPrimitiveTypePrimaryNoNewArray node) {
		table.put(node, new APrimitiveTypePrimaryExp(node.getPrimitiveType(), node.getDim(), node.getDot(), node.getTClass()));
		node.setPrimitiveType(null);
		node.setDim(new LinkedList());
		node.setDot(null);
		node.setTClass(null);
	}
	public void caseAOldQualifiedClassInstanceCreationExp(AOldQualifiedClassInstanceCreationExp node) {
		if (node.getArgumentList() != null)
			table.put(node, new AQualifiedClassInstanceCreationExp((PExp) table.remove(node.getPrimary()), node.getDot(), node.getNew(), node.getId(), node.getLPar(), (LinkedList) table.remove(node.getArgumentList()), node.getRPar(), node.getClassBody()));
		else
			table.put(node, new AQualifiedClassInstanceCreationExp((PExp) table.remove(node.getPrimary()), node.getDot(), node.getNew(), node.getId(), node.getLPar(), new LinkedList(), node.getRPar(), node.getClassBody()));
		node.setPrimary(null);
		node.setDot(null);
		node.setNew(null);
		node.setId(null);
		node.setLPar(null);
		node.setArgumentList(null);
		node.setRPar(null);
		node.setClassBody(null);
	}
	public void caseAOldQualifiedConstructorInvocation(AOldQualifiedConstructorInvocation node) {
		if (node.getArgumentList() != null)
			result = new AQualifiedConstructorInvocation((PExp) table.remove(node.getPrimary()), node.getDot(), node.getSuper(), new TLPar(), (LinkedList) table.remove(node.getArgumentList()), new TRPar(), node.getSemicolon());
		else
			result = new AQualifiedConstructorInvocation((PExp) table.remove(node.getPrimary()), node.getDot(), node.getSuper(), new TLPar(), new LinkedList(), new TRPar(), node.getSemicolon());
	}
	public void caseAOldSimpleClassInstanceCreationExp(AOldSimpleClassInstanceCreationExp node) {
		if (node.getArgumentList() != null)
			table.put(node, new ASimpleClassInstanceCreationExp(node.getNew(), node.getName(), node.getLPar(), (LinkedList) table.remove(node.getArgumentList()), node.getRPar(), node.getClassBody()));
		else
			table.put(node, new ASimpleClassInstanceCreationExp(node.getNew(), node.getName(), node.getLPar(), new LinkedList(), node.getRPar(), node.getClassBody()));
		node.setNew(null);
		node.setName(null);
		node.setLPar(null);
		node.setArgumentList(null);
		node.setRPar(null);
		node.setClassBody(null);
	}
	public void caseAOldStaticInitializerClassBodyDeclaration(AOldStaticInitializerClassBodyDeclaration node) {
		result = (PClassBodyDeclaration) table.remove(node.getStaticInitializer());
	}
	public void caseAOldSuper(AOldSuper node) {
		result = new ASuper((new TExtends()), (PName) table.remove(node.getClassType()));
	}
	public void caseAOldSuperConstructorInvocation(AOldSuperConstructorInvocation node) {
		if (node.getArgumentList() != null) {
			result = new ASuperConstructorInvocation(node.getSuper(), node.getLPar(), (LinkedList) table.remove(node.getArgumentList()), node.getRPar(), node.getSemicolon());
		} else {
			result = new ASuperConstructorInvocation(node.getSuper(), node.getLPar(), new LinkedList(), node.getRPar(), node.getSemicolon());
		}
	}
	public void caseAOldThisConstructorInvocation(AOldThisConstructorInvocation node) {
		if (node.getArgumentList() != null)
			result = new AThisConstructorInvocation((TThis) node.getThis(), node.getLPar(), (LinkedList) table.remove(node.getArgumentList()), node.getRPar(), node.getSemicolon());
		else
			result = new AThisConstructorInvocation((TThis) node.getThis(), node.getLPar(), new LinkedList(), node.getRPar(), node.getSemicolon());
	}
	public void caseAOldThrows(AOldThrows node) {
		result = new AThrows((new TThrows()), (LinkedList) table.remove(node.getClassTypeList()));
	}
	public void caseAOneBreakStmt(AOneBreakStmt node) {
		table.put(node, new ABreakStmt(node.getBreak(), node.getId(), node.getSemicolon()));
		node.setBreak(null);
		node.setId(null);
		node.setSemicolon(null);
	}
	public void caseAOneContinueStmt(AOneContinueStmt node) {
		table.put(node, new AContinueStmt(node.getContinue(), node.getId(), node.getSemicolon()));
		node.setContinue(null);
		node.setId(null);
		node.setSemicolon(null);
	}
	public void caseAOneDoStmt(AOneDoStmt node) {
		// list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list = new LinkedList();
		list.add(new AStmtBlockedStmt(node.getStmt()));
		table.put(node, new ADoStmt(node.getDo(), new ABlock(new TLBrace(), list, new TRBrace()), node.getWhile(), node.getLPar(), node.getExp(), node.getRPar(), node.getSemicolon()));
	}
	public void caseAOneForStmt(AOneForStmt node) {
		// list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list = new LinkedList();
		list.add(new AStmtBlockedStmt(node.getStmt()));
		if (node.getForUpdate() == null)
			table.put(node, new AForStmt(node.getFor(), node.getLPar(), node.getForInit(), node.getSemicolon1(), node.getExp(), node.getSemicolon2(), new LinkedList(), node.getRPar(), new ABlock(new TLBrace(), list, new TRBrace())));
		else
			table.put(node, new AForStmt(node.getFor(), node.getLPar(), node.getForInit(), node.getSemicolon1(), node.getExp(), node.getSemicolon2(), (LinkedList) table.remove(node.getForUpdate()), node.getRPar(), new ABlock(new TLBrace(), list, new TRBrace())));
		node.setFor(null);
		node.setLPar(null);
		node.setForInit(null);
		node.setSemicolon1(null);
		node.setExp(null);
		node.setSemicolon2(null);
		node.setForUpdate(null);
		node.setRPar(null);
		node.setStmt(null);
	}
	public void caseAOneQualifiedName(AOneQualifiedName node) {
		table.put(node, new AQualifiedName(node.getName(), node.getDot(), node.getId()));
		node.setName(null);
		node.setDot(null);
		node.setId(null);
	}
	public void caseAOneReturnStmt(AOneReturnStmt node) {
		table.put(node, new AReturnStmt(node.getReturn(), node.getExp(), node.getSemicolon()));
		node.setReturn(null);
		node.setExp(null);
		node.setSemicolon(null);
	}
	public void caseAOneSimpleName(AOneSimpleName node) {
		table.put(node, new ASimpleName(node.getId()));
		node.setId(null);
	}
	public void caseAOneSingleTypeImportDeclaration(AOneSingleTypeImportDeclaration node) {
		table.put(node, new ASingleTypeImportDeclaration(node.getImport(), node.getName(), node.getSemicolon()));
		node.setImport(null);
		node.setName(null);
		node.setSemicolon(null);
	}
	public void caseAOneSwitchStmt(AOneSwitchStmt node) {
		table.put(node, new ASwitchStmt(node.getSwitch(), node.getLPar(), node.getExp(), node.getRPar(), node.getLBrace(), node.getSwitchBlockStmtGroup(), node.getSwitchLabel(), node.getRBrace()));
		node.setSwitch(null);
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
		node.setLBrace(null);
		node.setSwitchBlockStmtGroup(new LinkedList());
		node.setSwitchLabel(new LinkedList());
		node.setRBrace(null);
	}
	public void caseAOneSynchronizedStmt(AOneSynchronizedStmt node) {
		table.put(node, new ASynchronizedStmt(node.getSynchronized(), node.getLPar(), node.getExp(), node.getRPar(), node.getBlock()));
		node.setSynchronized(null);
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
		node.setBlock(null);
	}
	public void caseAOneThrowStmt(AOneThrowStmt node) {
		table.put(node, new AThrowStmt(node.getThrow(), node.getExp(), node.getSemicolon()));
		node.setThrow(null);
		node.setExp(null);
		node.setSemicolon(null);
	}
	public void caseAOneTypeImportOnDemandDeclaration(AOneTypeImportOnDemandDeclaration node) {
		table.put(node, new ATypeOnDemandImportDeclaration(node.getImport(), node.getName(), node.getDot(), node.getStar(), node.getSemicolon()));
		node.setImport(null);
		node.setName(null);
		node.setDot(null);
		node.setStar(null);
		node.setSemicolon(null);
	}
	public void caseAOneWhileStmt(AOneWhileStmt node) {
		// list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list = new LinkedList();
		list.add(new AStmtBlockedStmt(node.getStmt()));
		table.put(node, new AWhileStmt(node.getWhile(), node.getLPar(), node.getExp(), node.getRPar(), new ABlock(new TLBrace(), list, new TRBrace())));
		node.setWhile(null);
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
		node.setStmt(null);
	}
	public void caseAOriginalExpStmt(AOriginalExpStmt node) {
		table.put(node, (PExp) table.remove(node.getStmtExp()));
		node.setStmtExp(null);
	}
	public void caseAPackageDeclaration(APackageDeclaration node) {
		table.put(node, node.getName());
		node.setPackage(null);
		node.setName(null);
		node.setSemicolon(null);
	}
	public void caseAPlusAdditiveExp(APlusAdditiveExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getAdditiveExp()), new APlusBinaryOperator(node.getPlus()), (PExp) table.remove(node.getMultiplicativeExp())));
		node.setAdditiveExp(null);
		node.setPlus(null);
		node.setMultiplicativeExp(null);
	}
	public void caseAPlusUnaryExp(APlusUnaryExp node) {
		table.put(node, new AUnaryExp(new APlusUnaryOperator(node.getPlus()), (PExp) table.remove(node.getUnaryExp())));
		node.setUnaryExp(null);
		node.setPlus(null);
	}
	public void caseAPostDecrementExpPostfixExp(APostDecrementExpPostfixExp node) {
		table.put(node, (PExp) table.remove(node.getPostDecrementExpr()));
		node.setPostDecrementExpr(null);
	}
	public void caseAPostDecrementExpr(APostDecrementExpr node) {
		table.put(node, new APostDecrementExp((PExp) table.remove(node.getPostfixExp()), node.getMinusMinus()));
		node.setPostfixExp(null);
		node.setMinusMinus(null);
	}
	public void caseAPostDecrementExpStmtExp(APostDecrementExpStmtExp node) {
		table.put(node, (PExp) table.remove(node.getPostDecrementExpr()));
		node.setPostDecrementExpr(null);
	}
	public void caseAPostfixExpUnaryExpNotPlusMinus(APostfixExpUnaryExpNotPlusMinus node) {
		table.put(node, (PExp) table.remove(node.getPostfixExp()));
		node.setPostfixExp(null);
	}
	public void caseAPostIncrementExpPostfixExp(APostIncrementExpPostfixExp node) {
		table.put(node, (PExp) table.remove(node.getPostIncrementExpr()));
		node.setPostIncrementExpr(null);
	}
	public void caseAPostIncrementExpr(APostIncrementExpr node) {
		table.put(node, new APostIncrementExp((PExp) table.remove(node.getPostfixExp()), node.getPlusPlus()));
		node.setPostfixExp(null);
		node.setPlusPlus(null);
	}
	public void caseAPostIncrementExpStmtExp(APostIncrementExpStmtExp node) {
		table.put(node, (PExp) table.remove(node.getPostIncrementExpr()));
		node.setPostIncrementExpr(null);
	}
	public void caseAPreDecrementExp(APreDecrementExp node) {
		table.put(node, new AUnaryExp(new ADecrementUnaryOperator(node.getMinusMinus()), (PExp) table.remove(node.getUnaryExp())));
		node.setMinusMinus(null);
		node.setUnaryExp(null);
	}
	public void caseAPreDecrementExpStmtExp(APreDecrementExpStmtExp node) {
		table.put(node, (PExp) table.remove(node.getPreDecrementExp()));
		node.setPreDecrementExp(null);
	}
	public void caseAPreDecrementExpUnaryExp(APreDecrementExpUnaryExp node) {
		table.put(node, (PExp) table.remove(node.getPreDecrementExp()));
		node.setPreDecrementExp(null);
	}
	public void caseAPreIncrementExp(APreIncrementExp node) {
		table.put(node, new AUnaryExp(new AIncrementUnaryOperator(node.getPlusPlus()), (PExp) table.remove(node.getUnaryExp())));
		node.setPlusPlus(null);
		node.setUnaryExp(null);
	}
	public void caseAPreIncrementExpStmtExp(APreIncrementExpStmtExp node) {
		table.put(node, (PExp) table.remove(node.getPreIncrementExp()));
		node.setPreIncrementExp(null);
	}
	public void caseAPreIncrementExpUnaryExp(APreIncrementExpUnaryExp node) {
		table.put(node, (PExp) table.remove(node.getPreIncrementExp()));
		node.setPreIncrementExp(null);
	}
	public void caseAPrimaryMethodInvocation(APrimaryMethodInvocation node) {
		if (node.getArgumentList() != null)
			table.put(node, new APrimaryMethodInvocationExp((PExp) table.remove(node.getPrimary()), node.getDot(), node.getId(), node.getLPar(), (LinkedList) table.remove(node.getArgumentList()), node.getRPar()));
		else
			table.put(node, new APrimaryMethodInvocationExp((PExp) table.remove(node.getPrimary()), node.getDot(), node.getId(), node.getLPar(), new LinkedList(), node.getRPar()));
	}
	public void caseAPrimaryNoNewArrayPrimary(APrimaryNoNewArrayPrimary node) {
		table.put(node, (PExp) table.remove(node.getPrimaryNoNewArray()));
		node.setPrimaryNoNewArray(null);
	}
	public void caseAPrimaryPostfixExp(APrimaryPostfixExp node) {
		table.put(node, (PExp) table.remove(node.getPrimary()));
		node.setPrimary(null);
	}
	public void caseAPrimitiveTypeArrayCreationExp(APrimitiveTypeArrayCreationExp node) {
		table.put(node, new APrimitiveTypeArrayExp(node.getNew(), node.getPrimitiveType(), node.getDimExp(), node.getDim()));
		node.setNew(null);
		node.setPrimitiveType(null);
		node.setDimExp(new LinkedList());
		node.setDim(new LinkedList());
	}
	public void caseAQualifiedNameName(AQualifiedNameName node) {
		result = (PName) table.remove(node.getOneQualifiedName());
	}
	public void caseAQualifiedThisPrimaryNoNewArray(AQualifiedThisPrimaryNoNewArray node) {
		table.put(node, new AQualifiedThisExp(node.getName(), node.getDot(), node.getThis()));
		node.setName(null);
		node.setDot(null);
		node.setThis(null);
	}
	public void caseAQuestionConditionalExp(AQuestionConditionalExp node) {
		table.put(node, new AQuestionExp((PExp) table.remove(node.getConditionalOrExp()), node.getQuestion(), node.getExp(), node.getColon(), (PExp) table.remove(node.getConditionalExp())));
		node.setConditionalOrExp(null);
		node.setQuestion(null);
		node.setExp(null);
		node.setColon(null);
		node.setConditionalExp(null);
	}
	public void caseARelationalExpEqualityExp(ARelationalExpEqualityExp node) {
		table.put(node, (PExp) table.remove(node.getRelationalExp()));
		node.setRelationalExp(null);
	}
	public void caseAReturnStmtStmtWithoutTrailingSubstmt(AReturnStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getOneReturnStmt()));
		node.setOneReturnStmt(null);
	}
	public void caseASemicolonStmt(ASemicolonStmt node) {
		table.put(node, new AEmptyStmt(node.getSemicolon()));
		node.setSemicolon(null);
	}
	public void caseAShiftExpRelationalExp(AShiftExpRelationalExp node) {
		table.put(node, (PExp) table.remove(node.getShiftExp()));
		node.setShiftExp(null);
	}
	public void caseAShiftLeftShiftExp(AShiftLeftShiftExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getShiftExp()), new AShiftLeftBinaryOperator(node.getShiftLeft()), (PExp) table.remove(node.getAdditiveExp())));
		node.setShiftExp(null);
		node.setShiftLeft(null);
		node.setAdditiveExp(null);
	}
	public void caseAShortIntegralType(AShortIntegralType node) {
		table.put(node, new AShortPrimitiveType(node.getShort()));
		node.setShort(null);
	}
	public void caseASignedShiftRightShiftExp(ASignedShiftRightShiftExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getShiftExp()), new ASignedShiftRightBinaryOperator(node.getSignedShiftRight()), (PExp) table.remove(node.getAdditiveExp())));
		node.setShiftExp(null);
		node.setSignedShiftRight(null);
		node.setAdditiveExp(null);
	}
	public void caseASimpleNameName(ASimpleNameName node) {
		result = (PName) table.remove(node.getOneSimpleName());
	}
	public void caseASingleTypeImportDeclarationImportDeclaration(ASingleTypeImportDeclarationImportDeclaration node) {
		result = (PImportDeclaration) table.remove(node.getOneSingleTypeImportDeclaration());
	}
	public void caseAStarMultiplicativeExp(AStarMultiplicativeExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getMultiplicativeExp()), new AStarBinaryOperator(node.getStar()), (PExp) table.remove(node.getUnaryExp())));
		node.setMultiplicativeExp(null);
		node.setStar(null);
		node.setUnaryExp(null);
	}
	public void caseAStaticInitializer(AStaticInitializer node) {
		table.put(node, new AStaticInitializerClassBodyDeclaration(node.getStatic(), node.getBlock()));
		node.setStatic(null);
		node.setBlock(null);
	}
	public void caseAStmtExpListForInit(AStmtExpListForInit node) {
		result = new AExpListForInit((LinkedList) table.remove(node.getStmtExpList()));
	}
	public void caseAStmtExpListStmtExpList(AStmtExpListStmtExpList node) {
		LinkedList list = (LinkedList) table.remove(node.getStmtExpList());
		list.add((PExp) table.remove(node.getStmtExp()));
		table.put(node, list);
		node.setStmtExpList(null);
		node.setComma(null);
		node.setStmtExp(null);
	}
	public void caseAStmtExpStmtExpList(AStmtExpStmtExpList node) {
		LinkedList list = new LinkedList();
		list.add((PExp) table.remove(node.getStmtExp()));
		table.put(node, list);
		node.setStmtExp(null);
	}
	public void caseAStmtWithoutTrailingSubstmtStmt(AStmtWithoutTrailingSubstmtStmt node) {
		result = (PStmt) table.remove(node.getStmtWithoutTrailingSubstmt());
	}
	public void caseAStmtWithoutTrailingSubstmtStmtNoShortIf(AStmtWithoutTrailingSubstmtStmtNoShortIf node) {
		table.put(node, (PStmt) table.remove(node.getStmtWithoutTrailingSubstmt()));
		node.setStmtWithoutTrailingSubstmt(null);
	}
	public void caseASuperMethodInvocation(ASuperMethodInvocation node) {
		if (node.getArgumentList() != null)
			table.put(node, new ASuperMethodInvocationExp(node.getSuper(), node.getDot(), node.getId(), node.getLPar(), (LinkedList) table.remove(node.getArgumentList()), node.getRPar()));
		else
			table.put(node, new ASuperMethodInvocationExp(node.getSuper(), node.getDot(), node.getId(), node.getLPar(), new LinkedList(), node.getRPar()));
	}
	public void caseASwitchStmtStmtWithoutTrailingSubstmt(ASwitchStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getOneSwitchStmt()));
		node.setOneSwitchStmt(null);
	}
	public void caseASynchronizedStmtStmtWithoutTrailingSubstmt(ASynchronizedStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getOneSynchronizedStmt()));
		node.setOneSynchronizedStmt(null);
	}
	public void caseAThisPrimaryNoNewArray(AThisPrimaryNoNewArray node) {
		table.put(node, new AThisExp(node.getThis()));
		node.setThis(null);
	}
	public void caseAThrowStmtStmtWithoutTrailingSubstmt(AThrowStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getOneThrowStmt()));
		node.setOneThrowStmt(null);
	}
	public void caseATryOneTryStmt(ATryOneTryStmt node) {
		table.put(node, new ATryStmt(node.getTry(), node.getBlock(), node.getCatchClause()));
		node.setTry(null);
		node.setBlock(null);
		node.setCatchClause((new LinkedList()));
	}
	public void caseATryStmtStmtWithoutTrailingSubstmt(ATryStmtStmtWithoutTrailingSubstmt node) {
		table.put(node, (PStmt) table.remove(node.getOneTryStmt()));
		node.setOneTryStmt(null);
	}
	public void caseATypeImportOnDemandDeclarationImportDeclaration(ATypeImportOnDemandDeclarationImportDeclaration node) {
		result = (PImportDeclaration) table.remove(node.getOneTypeImportOnDemandDeclaration());
	}
	public void caseAUnaryExpMultiplicativeExp(AUnaryExpMultiplicativeExp node) {
		table.put(node, (PExp) table.remove(node.getUnaryExp()));
		node.setUnaryExp(null);
	}
	public void caseAUnsignedShiftRightShiftExp(AUnsignedShiftRightShiftExp node) {
		table.put(node, new ABinaryExp((PExp) table.remove(node.getShiftExp()), new AUnsignedShiftRightBinaryOperator(node.getUnsignedShiftRight()), (PExp) table.remove(node.getAdditiveExp())));
		node.setShiftExp(null);
		node.setUnsignedShiftRight(null);
		node.setAdditiveExp(null);
	}
	public void caseAVariableDeclaratorsVariableDeclarators(AVariableDeclaratorsVariableDeclarators node) {
		LinkedList list = (LinkedList) table.remove(node.getVariableDeclarators());
		list.add(node.getVariableDeclarator());
		table.put(node, list);
		node.setVariableDeclarators(null);
		node.setComma(null);
		node.setVariableDeclarator(null);
	}
	public void caseAVariableDeclaratorVariableDeclarators(AVariableDeclaratorVariableDeclarators node) {
		LinkedList list = new LinkedList();
		list.add(node.getVariableDeclarator());
		table.put(node, list);
		node.setVariableDeclarator(null);
	}
	public void caseAVariableInitializersVariableInitializers(AVariableInitializersVariableInitializers node) {
		LinkedList list = (LinkedList) table.remove(node.getVariableInitializers());
		list.add(node.getVariableInitializer());
		table.put(node, list);
		node.setVariableInitializers(null);
		node.setComma(null);
		node.setVariableInitializer(null);
	}
	public void caseAVariableInitializerVariableInitializers(AVariableInitializerVariableInitializers node) {
		LinkedList list = new LinkedList();
		list.add(node.getVariableInitializer());
		table.put(node, list);
		node.setVariableInitializer(null);
	}
	public void caseAVoidPrimaryNoNewArray(AVoidPrimaryNoNewArray node) {
		table.put(node, new AVoidExp(node.getVoid(), node.getDot(), node.getTClass()));
		node.setVoid(null);
		node.setDot(null);
		node.setTClass(null);
	}
	public void caseAWhileStmtNoShortIf(AWhileStmtNoShortIf node) {
		// list of PBlockedStmt (i.e. AStmtBlockedStmt)
		LinkedList list = new LinkedList();
		list.add(new AStmtBlockedStmt((PStmt) table.remove(node.getStmtNoShortIf())));
		table.put(node, new AWhileStmt(node.getWhile(), node.getLPar(), node.getExp(), node.getRPar(), new ABlock(new TLBrace(), list, new TRBrace())));
		node.setWhile(null);
		node.setLPar(null);
		node.setExp(null);
		node.setRPar(null);
		node.setStmtNoShortIf(null);
	}
	public void caseAWhileStmtNoShortIfStmtNoShortIf(AWhileStmtNoShortIfStmtNoShortIf node) {
		table.put(node, (PStmt) table.remove(node.getWhileStmtNoShortIf()));
		node.setWhileStmtNoShortIf(null);
	}
	public void caseAWhileStmtStmt(AWhileStmtStmt node) {
		result = (PStmt) table.remove(node.getOneWhileStmt());
	}
	Node fix(Node node) {
		result = null;
		node.apply(this);
		return (result == null) ? node : result;
	}
}
