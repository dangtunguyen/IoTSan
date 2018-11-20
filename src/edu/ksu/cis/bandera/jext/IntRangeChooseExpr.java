package edu.ksu.cis.bandera.jext;

import edu.ksu.cis.bandera.jext.ChooseExpr;
import edu.ksu.cis.bandera.jext.BanderaExprSwitch;

import java.util.Vector;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.Type;

import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.jimple.Constant;
import ca.mcgill.sable.soot.jimple.IntConstant;
import ca.mcgill.sable.soot.jimple.Expr;

import ca.mcgill.sable.util.List;
import ca.mcgill.sable.util.ArrayList;
import ca.mcgill.sable.util.Switch;


/**
 * The IntRangeChooseExpr represents a specialized choose expression
 * that takes a range of integers to choose from.  Since it extends
 * ChooseExpr, it implements the getChoices() method but will actually
 * generate that list from the start and end during that method call
 * instead of storing the list.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:24 $
 */
public class IntRangeChooseExpr extends ChooseExpr {

    protected int start;
    protected int end;

    public IntRangeChooseExpr(int start, int end) {
	super(new ArrayList());
	this.start = start;
	this.end = end;
    }

    public int getStart() {
	return(start);
    }

    public int getEnd() {
	return(end);
    }

    public IntConstant getStartIntConstant() {
	return(IntConstant.v(start));
    }

    public IntConstant getEndIntConstant() {
	return(IntConstant.v(end));
    }

    public void apply(Switch sw) {
	((BanderaExprSwitch)sw).caseChooseExpr(this);
    }
  
    /**
     */
    public boolean equals(Constant c) {
	return false;
    }

    /**
     * Gets the list of choices.
     */
    public List getChoices() {
	List choices = new ArrayList();

	if(start < end) {
	    for(int i = start; i <= end; i++) {
		choices.add(IntConstant.v(i));
	    }
	}
	else {
	    // this would cause problems so report an error!
	}

	return(choices);
    }
  
    /**
     * Gets the type.
     */
    public Type getType() {
	return type;
    }
  
    /**
     *  Gets the use boxes.
     */
    public List getUseBoxes() {
	return new ArrayList();
    }
  
    /**
     * Sets the type.
     */
    public void setType(Type t) {
	type = t;
    }  

    public String toBriefString() {
	return toString();
    }

    public String toString() {
	return "Choose(" + getChoices() + ")";
    }  
}
