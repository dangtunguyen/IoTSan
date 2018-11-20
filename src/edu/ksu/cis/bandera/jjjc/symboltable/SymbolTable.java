package edu.ksu.cis.bandera.jjjc.symboltable;

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
import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.node.*;
import edu.ksu.cis.bandera.jjjc.lexer.*;
import ca.mcgill.sable.laleh.java.astfix.*;
import edu.ksu.cis.bandera.jjjc.unicodepreprocessor.*;
import edu.ksu.cis.bandera.jjjc.analysis.*;
import edu.ksu.cis.bandera.jjjc.exception.*;
import edu.ksu.cis.bandera.jjjc.util.*;

public class SymbolTable {
	private Hashtable declaredTypes = new Hashtable();
  private Hashtable importedTypesOnDemand = new Hashtable();
  private Hashtable importedTypes = new Hashtable();
  private static Hashtable primitiveTypes = new Hashtable();

  private Package currentPackage = null;
  private ClassOrInterfaceType currentClassOrInterfaceType = null;
  private String currentJavaFilePath = null;
  private Stack scopes = new Stack();

  static {
	  initPrimitiveTypes();
  }
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public SymbolTable(Name name) throws CompilerException {

	ClassOrInterfaceType classOrInterfaceType = Package.getClassOrInterfaceType(name);

	loadFromClassFile(classOrInterfaceType);
	
	for (Enumeration e = declaredTypes.elements(); e.hasMoreElements();) {
		ClassOrInterfaceType type = (ClassOrInterfaceType) e.nextElement();
		type.loadReferences();
	}
}
/**
 * 
 * @param path java.lang.String
 */
public SymbolTable(String path) throws CompilerException {
	loadFromSourceFile(path);

	for (Enumeration e = declaredTypes.elements(); e.hasMoreElements();) {
		ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) e.nextElement();
		classOrInterfaceType.loadReferences();
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param type java.lang.Class
 */
protected Type convertType(Class type) throws InvalidNameException, ClassOrInterfaceTypeNotFoundException {
	if (type == Boolean.TYPE) return BooleanType.TYPE;
	else if (type == Byte.TYPE) return ByteType.TYPE;
	else if (type == Character.TYPE) return CharType.TYPE;
	else if (type == Short.TYPE) return ShortType.TYPE;
	else if (type == Integer.TYPE) return IntType.TYPE;
	else if (type == Long.TYPE) return LongType.TYPE;
	else if (type == Float.TYPE) return FloatType.TYPE;
	else if (type == Double.TYPE) return DoubleType.TYPE;
	else if (type == Void.TYPE) return VoidType.TYPE;
	else if (type.isArray()) {
		Type componentType = convertType(type.getComponentType());
		if (componentType instanceof ArrayType) {
			ArrayType arrayType = (ArrayType) componentType;
			arrayType.nDimensions++;
			return arrayType;
		} else
			return new ArrayType(componentType, 1);
	} else
		return Package.getClassOrInterfaceType(new Name(type.getName()));
}
/**
 * 
 * @param variable edu.ksu.cis.bandera.jjjc.symboltable.Variable
 */
public void declareLocalVariable(Variable variable) throws AlreadyDeclaredException {
	Name varName = variable.getName();
	
	for (int i = 0; i < scopes.size(); i++)
		if (((Hashtable) scopes.elementAt(i)).get(varName) != null)
			throw new AlreadyDeclaredException("Local variable named '" + varName.toString() + "' has already declared");
	
	Hashtable scope = (Hashtable) scopes.peek();
	scope.put(varName, variable);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public ClassOrInterfaceType declareType(Name name) throws InvalidNameException {
	String msg1 = name.toString() + " in " + currentPackage.getName().toString();
	String msg2 = "Could not declare type with name " + msg1;
	
	if (!(name.isSimpleName()))
		throw new InvalidNameException(msg2);

	if (primitiveTypes.get(name) != null)
		throw new InvalidNameException(msg2);

	if (importedTypes.get(name) != null)
		throw new InvalidNameException(msg2);

  Name className = new Name(currentPackage.getName(), name);

  if (declaredTypes.get(className) != null)
		throw new InvalidNameException("Redefinition of type with name " + msg1);

	ClassOrInterfaceType declaredType;
	try {
		declaredType = Package.getClassOrInterfaceType(className);
	} catch (ClassOrInterfaceTypeNotFoundException e) {
		declaredType = new ClassOrInterfaceType(currentPackage, name, currentJavaFilePath);
		currentPackage.addType(declaredType);
	}

	declaredTypes.put(name, declaredType);
	
	return declaredType;
}
/**
 * 
 */
public void enterScope() {
	scopes.push(new Hashtable());
}
/**
 * 
 */
public void exitScope() {
	scopes.pop();
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public ClassOrInterfaceType getCurrentClassOrInterfaceType() {
	return currentClassOrInterfaceType;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Package
 */
public Package getCurrentPackage() {
	return currentPackage;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public ClassOrInterfaceType getDeclaredType(Name name) throws NotDeclaredException {
	if (declaredTypes.get(name) == null)
		throw new NotDeclaredException("Type named '" + name + "' is not declared");
	else
		return (ClassOrInterfaceType) declaredTypes.get(name);
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getDeclaredTypes() {
	return declaredTypes.elements();
}
/**
 * 
 * @return java.lang.String
 * @param exceptions java.util.Enumeration
 */
private String getExceptionMessages(Enumeration exceptions) {
	String msg = "{";
	for (Enumeration e = exceptions; e.hasMoreElements();) {
		msg += (e.nextElement().toString() + ", ");
	}
	return msg.substring(0, msg.length() - 2) + "}";
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Variable
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Variable getLocalVariable(Name name) throws NotDeclaredException {
	for (int i = 0; i < scopes.size(); i++)
		if (((Hashtable) scopes.elementAt(i)).get(name) != null)
			return (Variable) ((Hashtable) scopes.elementAt(i)).get(name);
	
	throw new NotDeclaredException("Local variable named '" + name.toString() + "' is not declared");
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 * @param methods java.util.Vector
 */
private Method getMostSpecificMethod(Vector methods) throws AmbiguousMethodException {
	Vector result = (Vector) methods.clone();

	for (Enumeration e1 = methods.elements(); e1.hasMoreElements();) {
	  Method m1 = (Method) e1.nextElement();

	  for (Enumeration e2 = methods.elements(); e2.hasMoreElements();) {
			Method m2 = (Method) e2.nextElement();
			
			if ((m1 != m2) && m2.isMoreSpecific(m1)) {
			  result.removeElement(m1);
			  break;
			}
	  }
	}

	if (result.size() > 1)
		try {
			Method m = (Method) result.elementAt(0);
			throw new AmbiguousMethodException("Cannot determine the most specific method for method named '"
				+ m.getName().toString() + "' in class or interface type named '"
				+ m.getDeclaringClassOrInterface().getName().toString() + "'");
		} catch (NotDeclaredException nde) {
			nde.printStackTrace();
			System.exit(1);
		}
		
	return (Method) result.elementAt(0);	
}
/**
 * 
 * @return int
 */
public int getNumScopeLevels() {
	return scopes.size();
}
/**
 * 
 * @return java.lang.String
 */
public String getPath() {
	return currentJavaFilePath;
}
/**
 * 
 * @return boolean
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public boolean hasLocalVariableDeclared(Name name) {
	for (int i = 0; i < scopes.size(); i++)
		if (((Hashtable) scopes.elementAt(i)).get(name) != null)
			return true;
			
	return false;
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void importType(Name name) throws InvalidNameException, ClassOrInterfaceTypeNotFoundException {
	String msg1 = "Invalid import type name '" + name + "'";
	String msg2 = "Redefinition of type '" + name + "' in import type";
	
	Name simpleName = new Name(name.getLastIdentifier());

	if (simpleName.equals(""))
		throw new InvalidNameException(msg1);

	if (primitiveTypes.get(simpleName) != null)
		throw new InvalidNameException(msg1);

	if (declaredTypes.get(simpleName) != null)
		throw new InvalidNameException(msg2);

	if (importedTypes.get(simpleName) != null)
		throw new InvalidNameException(msg2);

	importedTypes.put(simpleName, Package.getClassOrInterfaceType(name));
}
/**
 * 
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public void importTypeOnDemand(Name name) throws InvalidNameException {
	Package p = Package.getPackage(name);
	importedTypesOnDemand.put(p.getName(), p);
}
/**
 * 
 */
private static void initPrimitiveTypes() {
	primitiveTypes.put(BooleanType.TYPE.getName(), BooleanType.TYPE);
	primitiveTypes.put(ByteType.TYPE.getName(), ByteType.TYPE);
	primitiveTypes.put(CharType.TYPE.getName(), CharType.TYPE);
	primitiveTypes.put(ShortType.TYPE.getName(), ShortType.TYPE);
	primitiveTypes.put(IntType.TYPE.getName(), IntType.TYPE);
	primitiveTypes.put(LongType.TYPE.getName(), LongType.TYPE);
	primitiveTypes.put(FloatType.TYPE.getName(), FloatType.TYPE);
	primitiveTypes.put(DoubleType.TYPE.getName(), DoubleType.TYPE);
}
/**
 * 
 * @param classOrInterfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
private void loadFromClassFile(ClassOrInterfaceType classOrInterfaceType) throws InvalidNameException,
		AlreadyDeclaredException, ClassOrInterfaceTypeNotFoundException {

	/*
	if (analyzedType.get(classOrInterfaceType.toString()) == null)
		analyzedType.put(classOrInterfaceType.toString(), classOrInterfaceType);
	else
		System.out.println("Type '" + classOrInterfaceType.toString() + "' is analyzed again");
	*/
	classOrInterfaceType.setLoadingState();
		
	Class classOrInterface = null;
	
	try {
		classOrInterface = Class.forName(classOrInterfaceType.getName().toString());
	} catch (ClassNotFoundException cnfe) {}

	classOrInterfaceType.setInterface(classOrInterface.isInterface());

	if (classOrInterface.getSuperclass() != null) {
		classOrInterfaceType.setSuperClass((ClassOrInterfaceType) convertType(classOrInterface.getSuperclass()));
	} else {
		if (!"java.lang.Object".equals(classOrInterface.getName().trim())) {
			classOrInterfaceType.setSuperClass(Package.getClassOrInterfaceType(new Name("java.lang.Object")));
		}
	}

	Class[] superInterfaces = classOrInterface.getInterfaces();
	for (int i = 0; i < superInterfaces.length; i++) {
		classOrInterfaceType.addSuperInterface((ClassOrInterfaceType) convertType(superInterfaces[i]));
	}

 	if (classOrInterface.getDeclaringClass() != null) {
		classOrInterfaceType.setDeclaringClass((ClassOrInterfaceType) convertType(classOrInterface.getDeclaringClass()));
 	}
 	
	try {
		classOrInterfaceType.setModifiers(classOrInterface.getModifiers());
	} catch (InvalidModifiersException ime) {}
	
	java.lang.reflect.Field[] fields = classOrInterface.getDeclaredFields();
	java.lang.reflect.Constructor[] constructors = classOrInterface.getDeclaredConstructors();
	java.lang.reflect.Method[] methods = classOrInterface.getDeclaredMethods();

	for (int i = 0; i < fields.length; i++) {
		java.lang.reflect.Field field = fields[i];
		Type fieldType = convertType(field.getType());
		Name fieldName = new Name(field.getName());
		Field newField = new Field(fieldName, fieldType);
		classOrInterfaceType.addField(newField);
		
		try {
			newField.setModifiers(field.getModifiers());
		} catch (InvalidModifiersException ime) {}
	}

	Name constructorName = new Name((new Name(classOrInterface.getName())).getLastIdentifier());
	for (int i = 0; i < constructors.length; i++) {
		java.lang.reflect.Constructor constructor = constructors[i];
		Method method = new Method(constructorName);

		try {
			Class[] parmTypes = constructor.getParameterTypes();
			NameGenerator paramName = new NameGenerator("JJJCPARAM$");
			for (int j = 0; j < parmTypes.length; j++) {
				try {
					method.addParameterVariable(new Variable(0, convertType(parmTypes[j]),
							new Name(paramName.newName())));
				} catch (InvalidModifiersException ime) {}
			}
		} catch (AlreadyDeclaredException ade) {}

		Class[] exceptionTypes = constructor.getExceptionTypes();
		
		for (int j = 0; j < exceptionTypes.length; j++) {
			method.addException((ClassOrInterfaceType) convertType(exceptionTypes[j]));
		}
		
		classOrInterfaceType.addConstructor(method);

		try {
			method.setModifiers(constructor.getModifiers());
		} catch (InvalidModifiersException ime) {}
	}
	
	for (int i = 0; i < methods.length; i++) {
		java.lang.reflect.Method method = methods[i];
		Method newMethod = new Method(new Name(method.getName()));

		newMethod.setReturnType(convertType(method.getReturnType()));

		try {
			Class[] parmTypes = method.getParameterTypes();
			NameGenerator paramName = new NameGenerator("JJJCPARAM$");
			for (int j = 0; j < parmTypes.length; j++) {
				try {
					newMethod.addParameterVariable(new Variable(0, convertType(parmTypes[j]),
							new Name(paramName.newName())));
				} catch (InvalidModifiersException ime) {}
			}
		} catch (AlreadyDeclaredException ade) {}

		Class[] exceptionTypes = method.getExceptionTypes();
		for (int j = 0; j < exceptionTypes.length; j++) {
			newMethod.addException((ClassOrInterfaceType) convertType(exceptionTypes[j]));
		}	

		classOrInterfaceType.addMethod(newMethod);

		try {
			newMethod.setModifiers(method.getModifiers());
		} catch (InvalidModifiersException ime) {}
	}

	/*

	Class[] declaredClasses = classOrInterface.getDeclaredClasses();
	for (int i = 0; i < declaredClasses.length; i++) {
		classOrInterfaceType.addDeclaredType((ClassOrInterfaceType) convertType(declaredClasses[i]));
	}

	*/
	
	classOrInterfaceType.setLoadedState();
	declaredTypes.put(new Name(classOrInterfaceType.getName().getLastIdentifier()), classOrInterfaceType);
}
/**
 * 
 * @param classOrInterfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
private void loadFromSourceFile(String path) throws InvalidNameException, AnalysisException {
	currentJavaFilePath = path;

	Start ast = null;
		
	try {
		ast = CompilationManager.parseFile(currentJavaFilePath);
	} catch (Exception e) {
		throw new AnalysisException(e.toString());
	}

	setCurrentPackage(Package.getPackage(new Name("")));

	Package javaLang = Package.getPackage(new Name("java.lang"));
	importedTypesOnDemand.put(javaLang.getName(), javaLang);

	CompilationUnitAnalysis compilationUnitAnalysis = new CompilationUnitAnalysis(this);
	ast.apply(compilationUnitAnalysis);
	if (compilationUnitAnalysis.getExceptions().hasMoreElements()) {
		String msg = getExceptionMessages(compilationUnitAnalysis.getExceptions());
		throw new AnalysisException("Compilation unit analysis for " + currentJavaFilePath
				+ " failed due to the following exceptions: " + msg);
	}

	ClassOrInterfaceAnalysis classOrInterfaceAnalysis = new ClassOrInterfaceAnalysis(this);
	ast.apply(classOrInterfaceAnalysis);
	if (classOrInterfaceAnalysis.getExceptions().hasMoreElements()) {
		String msg = getExceptionMessages(classOrInterfaceAnalysis.getExceptions());
		throw new AnalysisException("Class or interface analysis for " + currentJavaFilePath
				+ " failed due to the following exceptions: " + msg);
	}

	ClassOrInterfaceMembersAnalysis classOrInterfaceMembersAnalysis = new ClassOrInterfaceMembersAnalysis(this);
	ast.apply(classOrInterfaceMembersAnalysis);
	if (classOrInterfaceMembersAnalysis.getExceptions().hasMoreElements()) {
		String msg = getExceptionMessages(classOrInterfaceMembersAnalysis.getExceptions());
		throw new AnalysisException("Class or interface members analysis for " + currentJavaFilePath
				+ " failed due to the following exceptions: " + msg);
	}

	setLoadedState();
}
/**
 * 
 */
public void resetScope() {
	scopes = new Stack();
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Named
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Named resolveAmbiguousName(Name name) throws MeaninglessNameException {
	try {
		return resolveExpression(name);
	} catch (CompilerException e) {
	}
	
	try {
		return resolveType(name);
	} catch (CompilerException e) {
	}

	try {
		return resolvePackage(name);
	} catch (CompilerException e) {
	}

	throw new MeaninglessNameException("Cannot resolve ambiguous name '" + name + "'");
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public ClassOrInterfaceType resolveClassOrInterfaceType(Name name) throws InvalidNameException,
		ClassOrInterfaceTypeNotFoundException, AmbiguousTypeException, ClassCircularityException, AlreadyDeclaredException,
		AmbiguousMethodException, AmbiguousFieldException, NotDeclaredException, AnalysisException {
	ClassOrInterfaceType type = null;

	if (name.isSimpleName()) {
		if (declaredTypes.get(name) != null)
			type = (ClassOrInterfaceType) declaredTypes.get(name);
		else if (importedTypes.get(name) != null)
			type = (ClassOrInterfaceType) importedTypes.get(name);
		else {
			int typesFound = 0;

			try {
				type = Package.getClassOrInterfaceType(name);
			} catch (Exception e) {
			}
			if (type != null) return type;
				
			for (Enumeration e = importedTypesOnDemand.elements(); e.hasMoreElements();) {
				Package p = (Package) e.nextElement();
				if (p.hasType(name)) {
					if (typesFound == 0)
						type = Package.getClassOrInterfaceType(new Name(p.getName(), name));
					typesFound++;
				}
			}

			if (typesFound > 1) {
				throw new AmbiguousTypeException("Ambiguous type named '" + name + "' in import on demand");
			} else if (typesFound == 0) {
				type = Package.getClassOrInterfaceType(new Name(currentPackage.getName(), name));
			}
		}
	} else {
		type = Package.getClassOrInterfaceType(name);
	}

	return type;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 * @param className edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param argumentTypes java.util.Vector
 */
public Method resolveConstructor(Name className, Vector argumentTypes) throws CompilerException {
	String msg = "Could not find a constructor for type named '" + className.toString()
				+ "' with argument types " + argumentTypes.toString();
				
	ClassOrInterfaceType classType = resolveClassOrInterfaceType(className);
	classType.loadReferences();
	if (classType.isInterface())
		throw new NoSuchConstructorException("Cannot have a constructor for an interface named '" +
				className.toString() + "'");

	Vector applicableConstructors = classType.getApplicableConstructors(argumentTypes);
	Vector possibleConstructors = new Vector();
	for (int i = 0; i < applicableConstructors.size(); i++) {
		Method constructor = (Method) applicableConstructors.elementAt(i);
		if (constructor.isAccessible(currentClassOrInterfaceType))
			possibleConstructors.addElement(constructor);
	}

	if (possibleConstructors.size() == 0)
		throw new NoSuchConstructorException(msg);

	try {
		return getMostSpecificMethod(possibleConstructors);
	} catch (CompilerException e) {
	}
	throw new NoSuchConstructorException(msg);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Named
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Named resolveExpression(Name name) throws CompilerException {
	if (name.getLastIdentifier().equals("length") && !name.isSimpleName()) {
		Typed array = (Typed) resolveFieldOrLocal(name.getSuperName());
		
		if (array instanceof Variable) {
			return ((ArrayType) ((Variable) array).getType()).length;
		} else {
			return ((ArrayType) ((Field) array).getType()).length;
		}
	} else {
		return resolveFieldOrLocal(name);
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Named
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Named resolveFieldOrLocal(Name name) throws CompilerException {
	if (hasLocalVariableDeclared(name)) {
		return getLocalVariable(name);
	} else {
		if (name.isSimpleName()) {
			return currentClassOrInterfaceType.getField(name);
		} else {
			Name fieldName = new Name(name.getLastIdentifier());
			Name superName = name.getSuperName();
			boolean isTypeName;
			try {
				superName = resolveClassOrInterfaceType(superName).getName();
				isTypeName = true;
			} catch (Exception e) {
				isTypeName = false;
			}
			Name className;
			if (isTypeName) {
				className = superName;
			} else {
				edu.ksu.cis.bandera.jjjc.symboltable.Type superType = ((Typed) resolveFieldOrLocal(superName)).getType();
				if (!(superType instanceof ClassOrInterfaceType))
					throw new ExpressionException("Type '" + superType.getFullyQualifiedName() + "' does not have field named '"
							+ fieldName + "'");
				className = superType.getName();
			}
			ClassOrInterfaceType type = resolveClassOrInterfaceType(className);
			type.loadReferences();
			Field f = type.getField(fieldName);
			if (f.isAccessible(currentClassOrInterfaceType)) {
				if (className.equals(superName)) {
					if (Modifier.isStatic(f.getModifiers()))
						return f;
					else
						throw new ExpressionException("Cannot acccess instance field named '" + fieldName + "' with type named '" +
								className + "'");
				} else {
					return f;
				}
			} else {
				throw new ExpressionException("Field named '" + fieldName + "' of type named '" + className +
						"' is not accessible from '" + currentClassOrInterfaceType.getName() + "'");
			}
		}
	}
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Method resolveMethod(Name name) throws CompilerException {
	if (name.isSimpleName())
		resolveMethod(currentClassOrInterfaceType.getName(), name);
	else
		resolveMethod(name.getSuperName(), new Name(name.getLastIdentifier()));
	return null;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 * @param typeName edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param methodName edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Method resolveMethod(Name typeName, Name methodName) throws CompilerException {
	ClassOrInterfaceType type = resolveClassOrInterfaceType(typeName);
	type.loadReferences();
	
	Method result = null;
	
	for (Enumeration e = type.getMethods(methodName); e.hasMoreElements();) {
		if (result == null)
			result = (Method) e.nextElement();
		else
			throw new AmbiguousMethodException("Cannot resolve method named '" + methodName + "' in type '" + typeName +
				"' without the argument types");
	}
	return result;	
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 * @param typeName edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param simpleMethodName edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param argumentTypes java.util.Vector
 */
public Method resolveMethod(Name typeName, Name methodName, Vector argumentTypes) throws CompilerException {
	String msg = "Could not find a method named '" + methodName.toString() +"' in type named '" + typeName.toString()
				+ "' with argument types " + argumentTypes.toString();
				
	ClassOrInterfaceType classType = resolveClassOrInterfaceType(typeName);
	classType.loadReferences();
	Vector applicableMethods = classType.getApplicableMethods(methodName, argumentTypes);
	Vector possibleMethods = new Vector();
	
	for (int i = 0; i < applicableMethods.size(); i++) {
		Method method = (Method) applicableMethods.elementAt(i);
		if (method.isAccessible(currentClassOrInterfaceType))
			possibleMethods.addElement(method);
	}

	if (possibleMethods.size() == 0)
		throw new edu.ksu.cis.bandera.jjjc.exception.NoSuchMethodException(msg);

	try {
		return getMostSpecificMethod(possibleMethods);
	} catch (CompilerException e) {
	}
	throw new edu.ksu.cis.bandera.jjjc.exception.NoSuchMethodException(msg);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param argumentTypes java.util.Vector
 */
public Method resolveMethod(Name name, Vector argumentTypes) throws CompilerException {
	if (name.isSimpleName())
		return resolveMethod(currentClassOrInterfaceType.getName(), name, argumentTypes);
	else
		return resolveMethod(name.getSuperName(), new Name(name.getLastIdentifier()),	argumentTypes);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Package
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Package resolvePackage(Name name) throws InvalidNameException {
	return Package.getPackage(name);
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Type
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Type resolveType(Name name) throws CompilerException {
	if (primitiveTypes.get(name) != null)
		return (Type) primitiveTypes.get(name);
	else
		return resolveClassOrInterfaceType(name);
}
/**
 * 
 * @param classOrInterfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void setCurrentClassOrInterfaceType(ClassOrInterfaceType classOrInterfaceType) {
	currentClassOrInterfaceType = classOrInterfaceType;
}
/**
 * 
 * @param p edu.ksu.cis.bandera.jjjc.symboltable.Package
 */
public void setCurrentPackage(Package p) {
	currentPackage = p;
}
/**
 * 
 */
private void setLoadedState() {
	for (Enumeration e = declaredTypes.elements(); e.hasMoreElements();) {
		((ClassOrInterfaceType) e.nextElement()).setLoadedState();
	}
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return declaredTypes.toString();
}
}
