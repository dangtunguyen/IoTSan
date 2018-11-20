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
import java.util.*;
import java.lang.reflect.Modifier;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.exception.*;

public class ClassOrInterfaceType extends ReferenceType {
	public static int NOT_LOADED = 0;
	public static int LOADING = 1;
	public static int LOADED = 2;
	public static int INHERITING = 3;
	public static int DONE = 4;
	
	private Package containingPackage;
	private int modifiers = 0;
	private boolean isInterface = false;
	private String path;
	
	private ClassOrInterfaceType declaringClass = null;
	private Hashtable superClasses = new Hashtable();
	private Hashtable superInterfaces = new Hashtable();
	private ClassOrInterfaceType directSuperClass;
	private Hashtable directSuperInterfaces = new Hashtable();
	private Hashtable fields = new Hashtable();
	private Vector constructors = new Vector();
	private Hashtable methods = new Hashtable();
	private Hashtable types = new Hashtable();
	private String msg;

	private int state = NOT_LOADED;
	private SymbolTable symTable;
/**
 * 
 * @param containingPackage edu.ksu.cis.bandera.jjjc.symboltable.Package
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param path java.lang.String
 */
public ClassOrInterfaceType(Package containingPackage, Name name, String path) throws InvalidNameException {
	msg = "class or interface named '" + name.toString() + "'";
	
	if (!(name.isSimpleName()) || Package.hasPackage(new Name(containingPackage.getName(), name)))
		throw new InvalidNameException("Cannot have a " + msg + " in package '"
				+ containingPackage.getName().toString() + "'");
	else {
		this.containingPackage = containingPackage;
		this.name = new Name(containingPackage.getName(), name);
		this.path = path;
	}
}
/**
 * 
 * @param constructor edu.ksu.cis.bandera.jjjc.symboltable.Method
 */
public void addConstructor(Method constructor) throws AlreadyDeclaredException {
	if (declaresConstructor(constructor.getParameterTypes()))
		throw new AlreadyDeclaredException("Constructor '" + constructor.getName()
				+ "' with the given signature has already been defined in " + msg);

	constructors.addElement(constructor);
	constructor.setDeclaringClassOrInterface(this);
}
/**
 * 
 * @param classOrInterfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void addDeclaredType(ClassOrInterfaceType classOrInterfaceType) throws AlreadyDeclaredException{
	Name simpleName = new Name(classOrInterfaceType.getName().getLastIdentifier());
	if (types.get(simpleName) == null)
		types.put(simpleName, classOrInterfaceType);
	else
		throw new AlreadyDeclaredException("Type named '" + simpleName
				+ "' has already declared in " + msg);
}
/**
 * 
 * @param field edu.ksu.cis.bandera.jjjc.symboltable.Field
 */
public void addField(Field field) throws AlreadyDeclaredException {
	Name name = field.getName();

	try {
		if (getField(name).getDeclaringClassOrInterface() == this)
			throw new AlreadyDeclaredException("Field named '" + field.getName().toString()
					+ "' has already declared in " + msg);
	} catch (NotDeclaredException nde) {
	}

	fields.put(field.getName(), field);
	field.setDeclaringClassOrInterface(this);
}
/**
 * 
 * @param method edu.ksu.cis.bandera.jjjc.symboltable.Method
 */
public void addMethod(Method method) throws AlreadyDeclaredException {
	if (containsMethod(method.getName(), method.getParameterTypes()))
		throw new AlreadyDeclaredException("Method '" + method.getName() + "' with the given signature has already been "
				+ "defined in " + msg);

	Vector methods = (Vector) this.methods.get(method.getName());
	if (methods == null) {
		methods = new Vector();
		this.methods.put(method.getName(), methods);
	}
	methods.addElement(method);
	method.setDeclaringClassOrInterface(this);
}
/**
 * 
 * @param interfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void addSuperInterface(ClassOrInterfaceType interfaceType) {
	directSuperInterfaces.put(interfaceType.getName(), interfaceType);
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public static boolean areValidClassModifiers(int modifiers) {
	if (Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers)
			|| Modifier.isNative(modifiers) || Modifier.isSynchronized(modifiers) || Modifier.isTransient(modifiers)
			|| Modifier.isVolatile(modifiers))
		return false;
	else return !(Modifier.isAbstract(modifiers) && Modifier.isFinal(modifiers));
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public static boolean areValidInterfaceModifiers(int modifiers) {
	if (Modifier.isProtected(modifiers) || Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers)
			|| Modifier.isNative(modifiers) || Modifier.isSynchronized(modifiers) || Modifier.isTransient(modifiers)
			|| Modifier.isVolatile(modifiers) || Modifier.isFinal(modifiers))
		return false;
	else return true;
}
/**
 * 
 * @return boolean
 * @param modifiers int
 */
public boolean areValidModifiers(int modifiers) {
	return (path == null) || areValidModifiers(modifiers, isInterface);
}
/**
 * 
 * @return boolean
 * @param modifiers int
 * @param isInterface boolean
 */
public static boolean areValidModifiers(int modifiers, boolean isInterface) {
	return (isInterface) ? areValidInterfaceModifiers(modifiers) : areValidClassModifiers(modifiers);
}
/**
 * 
 * @return boolean
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public boolean containsField(Name name) {
	return (fields.get(name) != null);
}
/**
 * 
 * @return boolean
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param parameterTypes java.util.Vector
 */
public boolean containsMethod(Name name, Vector parameterTypes) {
	Vector methods = (Vector) this.methods.get(name);

	if (methods == null)
		return false;

	for (int i = 0; i < methods.size(); i++) {
		Method m = (Method) methods.elementAt(i);
		if (m.hasSignature(name, parameterTypes))
			return true;
	}
	
	return false;
}
/**
 * 
 * @return boolean
 * @param parameterTypes java.util.Vector
 */
public boolean declaresConstructor(Vector parameterTypes) {
	for (int i = 0; i < constructors.size(); i++) {
		Method m = (Method) constructors.elementAt(i);
		if (m.hasSignature(name, parameterTypes))
			return true;
	}
	
	return false;
}
/**
 * 
 * @return java.util.Vector
 * @param argumentTypes java.util.Vector
 */
public Vector getApplicableConstructors(Vector argumentTypes) {
	Vector result = new Vector();

	for (int i = 0; i < constructors.size(); i++) {
		Method constructor = (Method) constructors.elementAt(i);
		if (constructor.isApplicable(argumentTypes))
			result.addElement(constructor);
	}
	
	return result;
}
/**
 * 
 * @return java.util.Vector
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param argumentTypes java.util.Vector
 */
public Vector getApplicableMethods(Name name, Vector argumentTypes) {
	Vector result = new Vector();
	Vector methods = (Vector) this.methods.get(name);

	if (methods == null)
		return result;

	for (int i = 0; i < methods.size(); i++) {
		Method method = (Method) methods.elementAt(i);
		if (method.isApplicable(argumentTypes))
			result.addElement(method);
	}
	
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 * @param parameterTypes java.util.Vector
 */
public Method getConstructor(Vector parameterTypes) throws NotDeclaredException{
	for (int i = 0; i < constructors.size(); i++) {
		Method m = (Method) constructors.elementAt(i);
		if (m.hasSignature(name, parameterTypes))
			return m;
	}

	throw new NotDeclaredException("Contructor with the given signature is not declared in " + msg);
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getConstructors() {
	return constructors.elements();
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Package
 */
public Package getContainingPackage() {
	return containingPackage;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public ClassOrInterfaceType getDeclaredType(Name name) throws NotDeclaredException {
	if (name.isSimpleName()) {
		if (types.get(name) != null)
			return (ClassOrInterfaceType) types.get(name);
	} else {
		if (name.getSuperName().equals(this.name))
			return getDeclaredType(new Name(name.getLastIdentifier()));
	}
	throw new NotDeclaredException("Type named '" + name + "' is not declared in " + msg);
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getDeclaredTypes() {
	return types.elements();
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public ClassOrInterfaceType getDeclaringClass() {
	return declaringClass;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public ClassOrInterfaceType getDirectSuperClass() {
	return directSuperClass;
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getDirectSuperInterfaces() {
	return directSuperInterfaces.elements();
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Field
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Field getField(Name name) throws NotDeclaredException{
	Field f = (Field) fields.get(name);
	if (f != null)
		return f;
	else throw new NotDeclaredException("Field named '" + name.toString()
			+ "' is not contained in " + msg);
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getFields() {
	return fields.elements();
}
/**
 * 
 * @return java.lang.String
 */
public String getFullyQualifiedName() {
	return name.toString();
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Method
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param parameterTypes java.util.Vector
 */
public Method getMethod(Name name, Vector parameterTypes) throws NotDeclaredException {
	Vector methods = (Vector) this.methods.get(name);
	String msg1 = "Method named '" + name.toString() + "' with the given parameters is not contained in " + msg;

	if (methods == null)
		throw new NotDeclaredException(msg1);

	for (int i = 0; i < methods.size(); i++) {
		Method m = (Method) methods.elementAt(i);
		if (m.hasSignature(name, parameterTypes))
			return m;
	}

	throw new NotDeclaredException(msg1);
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getMethods() {
	return methods.elements();
}
/**
 * 
 * @return java.util.Enumeration
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Enumeration getMethods(Name name) throws NotDeclaredException {
	if (methods.get(name) != null)
		return ((Vector) methods.get(name)).elements();
	else
		throw new NotDeclaredException("Method named '" + name + "' is not declared in type '" + this.name + "'");
}
/**
 * 
 * @return int
 */
public int getModifiers() {
	return modifiers;
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public Name getName() {
	return name;
}
/**
 * 
 * @return java.lang.String
 */
public String getPath() {
	return path;
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getSuperClasses() {
	try {
		loadReferences();
	} catch (CompilerException e) {
	}
	return superClasses.elements();
}
/**
 * 
 * @return java.util.Enumeration
 */
public Enumeration getSuperInterfaces() {
	try {
		loadReferences();
	} catch (CompilerException e) {
	}
	return superInterfaces.elements();
}
/**
 * 
 * @return edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
 */
public SymbolTable getSymbolTable() {
	return symTable;
}
/**
 * 
 * @return boolean
 * @param classType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public boolean hasSuperClass(ClassOrInterfaceType classType) {
	try {
		loadReferences();
	} catch (CompilerException e) {
	}
	return hasSuperClass(classType.getName());
}
/**
 * 
 * @return boolean
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public boolean hasSuperClass(Name name) {
	try {
		loadReferences();
	} catch (CompilerException e) {
	}
	return (superClasses.get(name) != null);
}
/**
 * 
 * @return boolean
 * @param interfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public boolean hasSuperInterface(ClassOrInterfaceType interfaceType) {
	try {
		loadReferences();
	} catch (CompilerException e) {
	}
	return hasSuperInterface(interfaceType.getName());
}
/**
 * 
 * @return boolean
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 */
public boolean hasSuperInterface(Name name) {
	try {
		loadReferences();
	} catch (CompilerException e) {
	}
	return (superInterfaces.get(name) != null);
}
/**
 * 
 * @param classOrInterfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
protected void inheritFields(ClassOrInterfaceType classOrInterfaceType) throws NotDeclaredException,
		AmbiguousFieldException {
	for (Enumeration e = classOrInterfaceType.fields.elements(); e.hasMoreElements();) {
		Field f = (Field) e.nextElement();
		if (Modifier.isPrivate(f.getModifiers()))
			continue; 
		else if (containsField(f.getName())) {
			ClassOrInterfaceType declaringClassOrInterface = getField(f.getName()).getDeclaringClassOrInterface();
			if (declaringClassOrInterface == this)
				continue;
			else if (declaringClassOrInterface != f.getDeclaringClassOrInterface())
				throw new AmbiguousFieldException("Ambiguous inheritance of field named '" + f.getName()
					+ "' in " + msg);
		}
		fields.put(f.getName(), f);
	}
}
/**
 * 
 */
protected void inheritFromDirectSuperClass() throws CompilerException {
	if (directSuperClass == null)
		return;

	directSuperClass.loadReferences();

	superClasses.put(directSuperClass.getName(), directSuperClass);

	for (Enumeration e = directSuperClass.superClasses.keys(); e.hasMoreElements();) {
		Name name = (Name) e.nextElement();
		superClasses.put(name, directSuperClass.superClasses.get(name));
	}

	for (Enumeration e = directSuperClass.superInterfaces.keys(); e.hasMoreElements();) {
		Name name = (Name) e.nextElement();
		superInterfaces.put(name, directSuperClass.superInterfaces.get(name));
	}

	inheritFields(directSuperClass);
	inheritMethods(directSuperClass);
}
/**
 * 
 */
protected void inheritFromDirectSuperInterfaces() throws CompilerException {
	for (Enumeration enumVar = directSuperInterfaces.elements(); enumVar.hasMoreElements();) {
		ClassOrInterfaceType directSuperInterface = (ClassOrInterfaceType) enumVar.nextElement();
		
		directSuperInterface.loadReferences();

		superInterfaces.put(directSuperInterface.getName(), directSuperInterface);

		for (Enumeration e = directSuperInterface.superInterfaces.keys(); e.hasMoreElements();) {
			Name name = (Name) e.nextElement();
			superInterfaces.put(name, directSuperInterface.superInterfaces.get(name));
		}
		
		inheritFields(directSuperInterface);
		inheritMethods(directSuperInterface);
	}
}
/**
 * 
 * @param classOrInterfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
protected void inheritMethods(ClassOrInterfaceType classOrInterfaceType) throws NotDeclaredException,
		AmbiguousMethodException {
	for (Enumeration enumVar = classOrInterfaceType.methods.keys(); enumVar.hasMoreElements();) {
		Name methodName = (Name) enumVar.nextElement();
		Vector thisMethods = (Vector) methods.get(methodName);
		Vector otherMethods = (Vector) classOrInterfaceType.methods.get(methodName);

		if (thisMethods == null) {
			thisMethods = new Vector();
			methods.put(methodName, thisMethods);
		}
		
		for (Enumeration e = otherMethods.elements(); e.hasMoreElements();) {
			Method m = (Method) e.nextElement();
			if (Modifier.isPrivate(m.getModifiers()))
				continue; 
			else if (containsMethod(m.getName(), m.getParameterTypes())) {
				ClassOrInterfaceType declaringClassOrInterface = getMethod(m.getName(),
						m.getParameterTypes()).getDeclaringClassOrInterface();
				if (declaringClassOrInterface == this)
					continue;
				//else if (declaringClassOrInterface != m.getDeclaringClassOrInterface()
				//		&& !declaringClassOrInterface.isInterface() && !m.getDeclaringClassOrInterface().isInterface())
				//	throw new AmbiguousMethodException("Ambiguous inheritance of method named '" + m.getName() + "' in " + msg);
				else continue;
			}
			thisMethods.addElement(m);
		}
	}
}
/**
 * 
 * @return boolean
 * @param otherClassOrInterfaceType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public boolean isAccesible(ClassOrInterfaceType otherClassOrInterfaceType) {
	if (Modifier.isPublic(modifiers))
		return true;
	else return this.containingPackage == otherClassOrInterfaceType.containingPackage;
}
/**
 * 
 * @return boolean
 */
public boolean isInterface() {
	return isInterface;
}
/**
 * 
 * @return boolean
 */
public boolean isLoaded() {
	return (state >= LOADED);
}
/**
 * 
 * @return boolean
 * @param otherType edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public boolean isValidNarrowingConversion(Type otherType) {
	if (!(otherType instanceof ClassOrInterfaceType))
		return false;

	ClassOrInterfaceType otherClassOrInterfaceType = (ClassOrInterfaceType) otherType;
		
	if (this.isInterface) {
		if (!(otherClassOrInterfaceType.isInterface))
			if (Modifier.isFinal(otherClassOrInterfaceType.getModifiers()))
				return otherClassOrInterfaceType.hasSuperInterface(this);
			else return true;
		else
			if (!this.hasSuperInterface(otherClassOrInterfaceType)) {
				boolean result = true;

				for (Enumeration e = otherClassOrInterfaceType.getMethods(); e.hasMoreElements();) {
					Vector methods1 = (Vector) e.nextElement();
					Vector methods2 = (Vector) otherClassOrInterfaceType.methods.get(((Method) methods1.elementAt(0)).getName());
					if (methods2 == null)
						continue;

					for (int i = 0; i < methods1.size(); i++) {
						Method m1 = (Method) methods1.elementAt(i);
						for (int j = 0; j < methods2.size(); j++) {
							Method m2 = (Method) methods2.elementAt(j);
							if (m1.hasSameSignature(m2))
								return false;
						}
					}
					return true;
				}
				
				return true;
			} else return false;
	} else {
		String objectName = "java.lang.Object";
		try {
			objectName = Package.getClassOrInterfaceType(new Name("Object")).getFullyQualifiedName();	
			// should be
			// objectName = Package.getClassOrInterfaceType(new Name("java.lang.Object")).getFullyQualifiedName();		
			// a hack so that we can replace java.lang.Object with abstracted Object
		} catch (Exception e) {}

		if (otherClassOrInterfaceType.isInterface)
			return !Modifier.isFinal(this.modifiers) && !this.hasSuperInterface(otherClassOrInterfaceType);
		else if (!(otherClassOrInterfaceType.isInterface))
			return otherClassOrInterfaceType.hasSuperClass(this);
		else if (objectName.equals(this.getFullyQualifiedName()))
			return (otherType instanceof ReferenceType);
		else return false;
	}
}
/**
 * 
 * @return boolean
 * @param otherType edu.ksu.cis.bandera.jjjc.symboltable.Type
 */
public boolean isValidWideningConversion(Type otherType) {
	if (!(otherType instanceof ClassOrInterfaceType))
		return false;

	ClassOrInterfaceType otherClassOrInterfaceType = (ClassOrInterfaceType) otherType;

	String objectName = "java.lang.Object";
	try {
		objectName = Package.getClassOrInterfaceType(new Name("Object")).getFullyQualifiedName();	
		// should be
		// objectName = Package.getClassOrInterfaceType(new Name("java.lang.Object")).getFullyQualifiedName();	
		// a hack so that we can replace java.lang.Object with abstracted Object
	} catch (Exception e) {}
	
	if (this.isInterface) {
		if (otherClassOrInterfaceType.isInterface)
			return this.hasSuperInterface(otherClassOrInterfaceType);
		else
			return objectName.equals(otherClassOrInterfaceType.getFullyQualifiedName());
	} else {
		if (!(otherClassOrInterfaceType.isInterface))
			return this.hasSuperClass(otherClassOrInterfaceType);
		else if (otherClassOrInterfaceType.isInterface)
			return this.hasSuperInterface(otherClassOrInterfaceType);
		else return objectName.equals(otherClassOrInterfaceType.getFullyQualifiedName());
	}
}
/**
 * 
 */
public void loadReferences() throws CompilerException {
	if (state == NOT_LOADED) {
		SymbolTable symbolTable = CompilationManager.buildSymbolTable(name);
	}
	
	if (state == LOADING) {
		//System.out.println("Loading " + name.toString() + " twice eh?");
	} else if (state == LOADED) {
		if (!"java.lang.Object".equals(getFullyQualifiedName()))
		{
			state = INHERITING;
			inheritFromDirectSuperClass();
			inheritFromDirectSuperInterfaces();
		}
		state = DONE;
	}	else if (state == INHERITING) {
		throw new ClassCircularityException("Class circularity in " + msg);
	}
}
/**
 * 
 * @param declaringClass edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void setDeclaringClass(ClassOrInterfaceType declaringClass) {
	this.declaringClass = declaringClass;
}
/**
 *
 * @param isInterface boolean
 */
public void setInterface(boolean isInterface) {
	this.isInterface = isInterface;
}
/**
 * 
 */
public void setLoadedState() {
	state = LOADED;

	for (Enumeration e = types.elements(); e.hasMoreElements();) {
		((ClassOrInterfaceType) e.nextElement()).setLoadedState();
	}
}
/**
 * 
 */
public void setLoadingState() {
	state = LOADING;

	for (Enumeration e = types.elements(); e.hasMoreElements();) {
		((ClassOrInterfaceType) e.nextElement()).setLoadingState();
	}
}
/**
 * 
 * @param modifiers int
 */
public void setModifiers(int modifiers) throws InvalidModifiersException {
	if (areValidModifiers(modifiers))
		this.modifiers = modifiers;
	else
		throw new InvalidModifiersException("Invalid modifiers for " + msg);
}
/**
 * 
 * @param path java.lang.String
 */
public void setPath(String path) {
	this.path = path;
}
/**
 * 
 * @param classType edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 */
public void setSuperClass(ClassOrInterfaceType classType) {
	directSuperClass = classType;
}
/**
 * 
 * @param symbolTable edu.ksu.cis.bandera.jjjc.symboltable.SymbolTable
 */
public void setSymbolTable(SymbolTable symbolTable) {
	symTable = symbolTable;
}
}
