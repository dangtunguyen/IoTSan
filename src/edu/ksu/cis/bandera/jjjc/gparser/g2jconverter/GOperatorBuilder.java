package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import edu.ksu.cis.bandera.jjjc.node.*;

public class GOperatorBuilder {
	public static PBinaryOperator buildABinaryOperator(org.codehaus.groovy.syntax.Token gToken)
	{
		PBinaryOperator jBinaryOperator = null;
		String gTokenTxt = gToken.getText();
		
		switch(gTokenTxt)
		{
		case "&&": jBinaryOperator = new AAndBinaryOperator(new TAnd()); break;
		case "||": jBinaryOperator = new AOrBinaryOperator(new TOr()); break;
		case "==": jBinaryOperator = new AEqBinaryOperator(new TEq()); break;
		case "!=": jBinaryOperator = new ANeqBinaryOperator(new TNeq()); break;
		case ">=": jBinaryOperator = new AGteqBinaryOperator(new TGteq()); break;
		case ">": jBinaryOperator = new AGtBinaryOperator(new TGt()); break;
		case "<=": jBinaryOperator = new ALteqBinaryOperator(new TLteq()); break;
		case "<": jBinaryOperator = new ALtBinaryOperator(new TLt()); break;
		case "%": jBinaryOperator = new AModBinaryOperator(new TMod()); break;
		case "&": jBinaryOperator = new ABitAndBinaryOperator(new TBitAnd()); break;
		case "|": jBinaryOperator = new ABitOrBinaryOperator(new TBitOr()); break;
		case "^": jBinaryOperator = new ABitXorBinaryOperator(new TBitXor()); break;
		case "/": jBinaryOperator = new ADivBinaryOperator(new TDiv()); break;
		case "-": jBinaryOperator = new AMinusBinaryOperator(new TMinus()); break;
		case "+": jBinaryOperator = new APlusBinaryOperator(new TPlus()); break;
		case "*": jBinaryOperator = new AStarBinaryOperator(new TStar()); break;
//		case "=": jBinaryOperator = new AAssignAssignmentOperator(new TAssign()); break; /* AAssignmentExp */
		}
		
		return jBinaryOperator;
	}
	
	public static PBinaryOperator buildABinaryOperator(String gTokenTxt)
	{
		PBinaryOperator jBinaryOperator = null;
		
		switch(gTokenTxt)
		{
		case "&&": jBinaryOperator = new AAndBinaryOperator(new TAnd()); break;
		case "||": jBinaryOperator = new AOrBinaryOperator(new TOr()); break;
		case "==": jBinaryOperator = new AEqBinaryOperator(new TEq()); break;
		case "!=": jBinaryOperator = new ANeqBinaryOperator(new TNeq()); break;
		case ">=": jBinaryOperator = new AGteqBinaryOperator(new TGteq()); break;
		case ">": jBinaryOperator = new AGtBinaryOperator(new TGt()); break;
		case "<=": jBinaryOperator = new ALteqBinaryOperator(new TLteq()); break;
		case "<": jBinaryOperator = new ALtBinaryOperator(new TLt()); break;
		case "%": jBinaryOperator = new AModBinaryOperator(new TMod()); break;
		case "&": jBinaryOperator = new ABitAndBinaryOperator(new TBitAnd()); break;
		case "|": jBinaryOperator = new ABitOrBinaryOperator(new TBitOr()); break;
		case "^": jBinaryOperator = new ABitXorBinaryOperator(new TBitXor()); break;
		case "/": jBinaryOperator = new ADivBinaryOperator(new TDiv()); break;
		case "-": jBinaryOperator = new AMinusBinaryOperator(new TMinus()); break;
		case "+": jBinaryOperator = new APlusBinaryOperator(new TPlus()); break;
		case "*": jBinaryOperator = new AStarBinaryOperator(new TStar()); break;
		}
		
		return jBinaryOperator;
	}
	
	public static PBinaryOperator buildABinaryOperatorFromMethName(String methName)
	{
		PBinaryOperator jBinaryOperator = null;
		
		switch(methName)
		{
		case "plus": jBinaryOperator = new APlusBinaryOperator(new TPlus()); break;
		case "minus": jBinaryOperator = new AMinusBinaryOperator(new TMinus()); break;
		case "compareNotEqual": jBinaryOperator = new ANeqBinaryOperator(new TNeq()); break;
		case "compareGreaterThanEqual": jBinaryOperator = new AGteqBinaryOperator(new TGteq()); break;
		case "compareGreaterThan": jBinaryOperator = new AGtBinaryOperator(new TGt()); break;
		case "compareLessThanEqual": jBinaryOperator = new ALteqBinaryOperator(new TLteq()); break;
		case "compareLessThan": jBinaryOperator = new ALtBinaryOperator(new TLt()); break;
		default: jBinaryOperator = new AEqBinaryOperator(new TEq()); break; /* compareEquals */
		}
		
		return jBinaryOperator;
	}
}
