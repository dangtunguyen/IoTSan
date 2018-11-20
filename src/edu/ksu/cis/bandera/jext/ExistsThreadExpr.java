package edu.ksu.cis.bandera.jext;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000   Robby (robby@cis.ksu.edu)                    *
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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;
import org.apache.log4j.Category;

/**
 *
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:23 $
 */
public class ExistsThreadExpr implements UnopExpr {
    private static final Category log = Category.getInstance(ExistsThreadExpr.class);
    protected ValueBox opBox;

    /**
     * Create a new ExistsThreadExpr with the given Value.
     *
     * @param Value v
     */
    public ExistsThreadExpr(Value v) {
	this(Grimp.v().newArgBox(v));
    }

    /**
     * Create a new ExistsThreadExpr with the given ValueBox.
     *
     * @param ValueBox valueBox
     */
    public ExistsThreadExpr(ValueBox valueBox) {
	opBox = valueBox;
    }

    /**
     * apply method comment.
     */
    public void apply(ca.mcgill.sable.util.Switch sw) {

	if(sw == null) {
	    log.error("The Switch given was null.");
	    throw new RuntimeException("The Switch given was null.  Not sure what to do.");
	}
	else if(sw instanceof BanderaSpecExprSwitch) {
	    ((BanderaSpecExprSwitch) sw).caseExistsThreadExpr(this);
	}
	else {
	    log.error("The Switch given was not a BanderaSpecExprSwitch.  It was a " + sw.getClass().getName());
	    throw new RuntimeException("The Switch given was not a BanderaSpecExprSwitch.  Not sure what to do.");
	}

    }

    /**
     * Get the op in this ExistsThreadExpr.
     */
    public Value getOp() {
	return opBox.getValue();
    }

    /**
     * Get the op box in this ExistsThreadExpr.
     */
    public ValueBox getOpBox() {
	return opBox;
    }

    /**
     * Get the type of the op in this ExistsThreadExpr.
     */
    public Type getType() {
	return getOp().getType();
    }

    /**
     * getUseBoxes method comment.
     */
    public ca.mcgill.sable.util.List getUseBoxes() {
	ca.mcgill.sable.util.List list = new ca.mcgill.sable.util.ArrayList();

	list.addAll(opBox.getValue().getUseBoxes());
	list.add(opBox);

	return list;
    }

    /**
     * Set the op in this ExistsThreadExpr.
     */
    public void setOp(Value op) {
	opBox.setValue(op);
    }

    /**
     * toBriefString method comment.
     */
    public String toBriefString() {
	return "E t. " + ((ToBriefString) opBox.getValue()).toBriefString();
    }

    /**
     * 
     * @return java.lang.String
     */
    public String toString() {
	return "E t. " + opBox.getValue();
    }
}
