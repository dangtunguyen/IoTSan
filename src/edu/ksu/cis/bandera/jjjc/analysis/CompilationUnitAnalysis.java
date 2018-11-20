package edu.ksu.cis.bandera.jjjc.analysis;

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
import java.util.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.exception.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;

public class CompilationUnitAnalysis extends DepthFirstAdapter {
	private SymbolTable symbolTable;
	private Vector exceptions = new Vector();
/**
 * 
 * @param symbolTable SymbolTable
 */
public CompilationUnitAnalysis(SymbolTable symbolTable) {
	this.symbolTable = symbolTable;
}
/**
 * 
 */
public void caseAClassDeclaration(AClassDeclaration node) {
	try {
		ClassOrInterfaceType type = symbolTable.declareType(new Name(node.getId().toString()));
		type.setPath(symbolTable.getPath());
		if (type.isLoaded()) return;
		type.setLoadingState();
		type.setInterface(false);
		type.setSymbolTable(symbolTable);
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ACompilationUnit
 */
public void caseACompilationUnit(ACompilationUnit node) {
	if (node.getPackageDeclaration() != null) {
		try {
			symbolTable.setCurrentPackage(Package.getPackage(new Name(node.getPackageDeclaration().toString())));
		} catch (InvalidNameException ine) {
			exceptions.addElement(ine);
		}
	} else {
		try {
			symbolTable.setCurrentPackage(Package.getPackage(new Name("")));
		} catch (InvalidNameException ine) {
			exceptions.addElement(ine);
		}
	}
	{
		Object temp[] = node.getImportDeclaration().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PImportDeclaration) temp[i]).apply(this);
		}
	}
	{
		Object temp[] = node.getTypeDeclaration().toArray();
		for (int i = 0; i < temp.length; i++) {
			((PTypeDeclaration) temp[i]).apply(this);
		}
	}
}
/**
 * 
 */
public void caseAInterfaceDeclaration(AInterfaceDeclaration node) {
	try {
		ClassOrInterfaceType type = symbolTable.declareType(new Name(node.getId().toString()));
		if (type.isLoaded()) return;
		type.setLoadingState();
		type.setInterface(true);
		type.setSymbolTable(symbolTable);
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
}
/**
 * 
 */
public void caseAPackageDeclaration(APackageDeclaration node) {
	try {
		symbolTable.setCurrentPackage(Package.getPackage(new Name(node.getName().toString())));
	} catch (InvalidNameException ine) {
		exceptions.addElement(ine);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ASingleTypeImportDeclaration
 */
public void caseASingleTypeImportDeclaration(ASingleTypeImportDeclaration node) {
	try {
		symbolTable.importType(new Name(node.getName().toString()));
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
}
/**
 * 
 * @param node edu.ksu.cis.bandera.jjjc.node.ATypeImportOnDemandDeclaration
 */
public void caseATypeOnDemandImportDeclaration(ATypeOnDemandImportDeclaration node) {
	try {
		symbolTable.importTypeOnDemand(new Name(node.getName().toString()));
	} catch (CompilerException e) {
		exceptions.addElement(e);
	}
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getExceptions() {
	return exceptions.elements();
}
}
