package edu.ksu.cis.bandera.specification.predicate.datastructure;

import edu.ksu.cis.bandera.specification.predicate.exception.*;
import edu.ksu.cis.bandera.specification.predicate.node.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import java.util.*;
public final class PredicateFactory {
/**
 * 
 * @return edu.ksu.cis.bandera.specification.predicate.datastructure.ExpressionPredicate
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param node edu.ksu.cis.bandera.specification.predicate.node.Node
 * @param params java.util.Vector
 * @param paramTypes java.util.Vector
 * @param isStatic boolean
 * @param variablesUsed java.util.Hashtable
 * @param constraint edu.ksu.cis.bandera.specification.predicate.node.PExp
 * @param description java.lang.String
 * @param exceptions java.util.Vector
 */
public static ExpressionPredicate createExpressionPredicate(Name name, ClassOrInterfaceType type, Node node, Vector params, Vector paramTypes, boolean isStatic, Hashtable variablesUsed, PExp constraint, String description, Vector exceptions) throws DuplicatePredicateException {
	ExpressionPredicate result = new ExpressionPredicate(name, type, node, exceptions);
	result.setParams(params);
	result.setParamTypes(paramTypes);
	result.setConstraint(constraint);
	result.setDescription(description);
	result.setStatic(isStatic);
	result.setVariablesUsed(variablesUsed);
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.predicate.datastructure.InvokePredicate
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param node edu.ksu.cis.bandera.specification.predicate.node.Node
 * @param params java.util.Vector
 * @param paramTypes java.util.Vector
 * @param isStatic boolean
 * @param variablesUsed java.util.Hashtable
 * @param constraint edu.ksu.cis.bandera.specification.predicate.node.PExp
 * @param description java.lang.String
 * @param exceptions java.util.Vector
 * @exception edu.ksu.cis.bandera.specification.predicate.exception.DuplicatePredicateException The exception description.
 */
public static InvokePredicate createInvokePredicate(Name name, ClassOrInterfaceType type, Annotation annotation, Node node, Vector params, Vector paramTypes, boolean isStatic, Hashtable variablesUsed, PExp constraint, String description, Vector exceptions) throws edu.ksu.cis.bandera.specification.predicate.exception.DuplicatePredicateException {
	InvokePredicate result = new InvokePredicate(name, type, annotation, node, exceptions);
	result.setParams(params);
	result.setParamTypes(paramTypes);
	result.setConstraint(constraint);
	result.setDescription(description);
	result.setStatic(isStatic);
	result.setVariablesUsed(variablesUsed);
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.predicate.datastructure.LocationPredicate
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 * @param node edu.ksu.cis.bandera.specification.predicate.node.Node
 * @param label java.lang.String
 * @param params java.util.Vector
 * @param paramTypes java.util.Vector
 * @param isStatic boolean
 * @param variablesUsed java.util.Hashtable
 * @param constraint edu.ksu.cis.bandera.specification.predicate.node.PExp
 * @param description java.lang.String
 * @param exceptions java.util.Vector
 * @exception edu.ksu.cis.bandera.specification.predicate.exception.DuplicatePredicateException The exception description.
 */
public static LocationPredicate createLocationPredicate(Name name, ClassOrInterfaceType type, Annotation annotation, Node node, String label, Vector params, Vector paramTypes, boolean isStatic, Hashtable variablesUsed, PExp constraint, String description, Vector exceptions) throws edu.ksu.cis.bandera.specification.predicate.exception.DuplicatePredicateException {
	LocationPredicate result = new LocationPredicate(name, type, annotation, node, exceptions, label);
	result.setParams(params);
	result.setParamTypes(paramTypes);
	result.setConstraint(constraint);
	result.setDescription(description);
	result.setStatic(isStatic);
	result.setVariablesUsed(variablesUsed);
	return result;
}
/**
 * 
 * @return edu.ksu.cis.bandera.specification.predicate.datastructure.ReturnPredicate
 * @param name edu.ksu.cis.bandera.jjjc.symboltable.Name
 * @param type edu.ksu.cis.bandera.jjjc.symboltable.ClassOrInterfaceType
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 * @param node edu.ksu.cis.bandera.specification.predicate.node.Node
 * @param params java.util.Vector
 * @param paramTypes java.util.Vector
 * @param isStatic boolean
 * @param ret boolean
 * @param variablesUsed java.util.Hashtable
 * @param constraint edu.ksu.cis.bandera.specification.predicate.node.PExp
 * @param description java.lang.String
 * @param exceptions java.util.Vector
 * @exception edu.ksu.cis.bandera.specification.predicate.exception.DuplicatePredicateException The exception description.
 */
public static ReturnPredicate createReturnPredicate(Name name, ClassOrInterfaceType type, Annotation annotation, Node node, Vector params, Vector paramTypes, boolean isStatic, boolean ret, Hashtable variablesUsed, PExp constraint, String description, Vector exceptions) throws edu.ksu.cis.bandera.specification.predicate.exception.DuplicatePredicateException {
	ReturnPredicate result = new ReturnPredicate(name, type, annotation, node, exceptions, ret);
	result.setParams(params);
	result.setParamTypes(paramTypes);
	result.setConstraint(constraint);
	result.setDescription(description);
	result.setStatic(isStatic);
	result.setVariablesUsed(variablesUsed);
	return result;
}
}
