package edu.ksu.cis.bandera.birc;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import ca.mcgill.sable.util.*;

import edu.ksu.cis.bandera.bir.LockLit;
import edu.ksu.cis.bandera.bir.BirThread;
import edu.ksu.cis.bandera.bir.ActiveThread;
import edu.ksu.cis.bandera.bir.ThreadVector;
import edu.ksu.cis.bandera.bir.TransSystem;

import java.util.*;

import org.apache.log4j.Category;


/**
 * An lock literal value in a Jimple trace
 *
 * @author James Corbett &lt;corbett@hawaii.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:32:53 $
 */
public class LockLiteral implements JimpleLiteral {

    private static Category log = Category.getInstance(LockLiteral.class.getName());

    private LockLit birLock;
    private Vector waitingThreads = new Vector();
    private ActiveThread holdingThread;

    public LockLiteral(LockLit birLock) {
	this.birLock = birLock; 
	log.debug("birLock = " + birLock.toString());

	TransSystem system = birLock.getSystem();
	holdingThread = birLock.getOwner();
	ThreadVector waiting = birLock.getWaitingThreads();
	log.debug("waiting.size() = " + waiting.size());
	for (int i = 0; i < waiting.size(); i++) {
	    //waitingThreads.addElement(system.getSource(waiting.elementAt(i)));
	    waitingThreads.addElement(waiting.elementAt(i));
	}
    }

    public void apply(Switch sw) {
	((JimpleLiteralSwitch) sw).caseLockLiteral(this);
    }

    public ActiveThread getHoldingThread() {
	return(holdingThread);
    }

    public int getNumWaiting() {
	return(waitingThreads.size());
    }

    public ActiveThread getWaitingThread(int index) {
	return((ActiveThread)waitingThreads.elementAt(index));
    }
}
