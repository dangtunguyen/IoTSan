package edu.ksu.cis.bandera.sessions;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import java.util.Observer;
import java.util.Observable;

import org.apache.log4j.Category;

/**
 * The CounterExampleDescription provides information that will
 * configure the counter example GUI when it starts up.  This might
 * contain lock graphs, object diagrams, watch variables, or
 * breakpoints.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:27 $
 */
public final class CounterExampleDescription extends Observable implements Cloneable, Observer {

    /**
     * The log to write messages to.
     */
    private static final Category log = Category.getInstance(CounterExampleDescription.class.getName());

    /**
     * A set of breakpoints defined for the counter example to set
     * when it starts.
     */
    private Set breakpoints;

    /**
     * A set of watches defined for the counter example to set
     * when it starts.
     */
    private Set watchVariables;

    /**
     * A set of lock graphs defined for the counter example to
     * create when it starts.
     */
    private Set lockGraphs;

    /**
     * A set of object diagrams defined for the counter example to
     * create when it starts.
     */
    private Set objectDiagrams;

    /**
     * Add a lock graph to the local store.
     *
     * @param LockGraph lockGraph
     */
    public void addLockGraph(LockGraph lockGraph) {
	if(lockGraph == null) {
	    return;
	}
	if(lockGraphs == null) {
	    lockGraphs = new HashSet();
	}
	boolean changed = lockGraphs.add(lockGraph);
	if(changed) {
	    lockGraph.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove a lock graph from the local store.
     *
     * @param LockGraph lockGraph
     */
    public void removeLockGraph(LockGraph lockGraph) {
	if(lockGraph == null) {
	    return;
	}
	if(lockGraphs == null) {
	    return;
	}
	boolean changed = lockGraphs.remove(lockGraph);
	if(changed) {
	    lockGraph.deleteObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Clear all the lock graphs in the store.
     */
    public void clearLockGraphs() {
	if((lockGraphs == null) || (lockGraphs.size() <= 0)) {
	    return;
	}
	Iterator lgi = lockGraphs.iterator();
	while(lgi.hasNext()) {
	    LockGraph lg = (LockGraph)lgi.next();
	    lg.deleteObserver(this);
	}
	lockGraphs.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Get the set of lock graphs defined for this counter example.
     *
     * @return Set
     */
    public Set getLockGraphs() {
	return(lockGraphs);
    }

    /**
     * Add an object diagram to the local store.
     *
     * @param ObjectDiagram objectDiagram
     */
    public void addObjectDiagram(ObjectDiagram objectDiagram) {
	if(objectDiagram == null) {
	    return;
	}
	if(objectDiagrams == null) {
	    objectDiagrams = new HashSet();
	}
	boolean changed = objectDiagrams.add(objectDiagram);
	if(changed) {
	    objectDiagram.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove an object diagram from the local store.
     *
     * @param ObjectDiagram objectDiagram
     */
    public void removeObjectDiagram(ObjectDiagram objectDiagram) {
	if(objectDiagram == null) {
	    return;
	}
	if(objectDiagrams == null) {
	    return;
	}
	boolean changed = objectDiagrams.remove(objectDiagram);
	if(changed) {
	    objectDiagram.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Clear all the object diagrams from the store.
     */
    public void clearObjectDiagrams() {
	if((objectDiagrams == null) || (objectDiagrams.size() <= 0)) {
	    return;
	}
	Iterator odi = objectDiagrams.iterator();
	while(odi.hasNext()) {
	    ObjectDiagram od = (ObjectDiagram)odi.next();
	    od.deleteObserver(this);
	}
	objectDiagrams.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Get the set of object diagrams defined for this counter example.
     *
     * @return Set
     */
    public Set getObjectDiagrams() {
	return(objectDiagrams);
    }

    /**
     * Add a watch variable to the local store.
     *
     * @param WatchVariable watchVariable
     */
    public void addWatchVariable(WatchVariable watchVariable) {
	if(watchVariable == null) {
	    return;
	}
	if(watchVariables == null) {
	    watchVariables = new HashSet();
	}
	boolean changed = watchVariables.add(watchVariable);
	if(changed) {
	    watchVariable.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove a watch variable from the local store.
     *
     * @param WatchVariable watchVariable
     */
    public void removeWatchVariable(WatchVariable watchVariable) {
	if(watchVariable == null) {
	    return;
	}
	if(watchVariables == null) {
	    return;
	}
	boolean changed = watchVariables.remove(watchVariable);
	if(changed) {
	    watchVariable.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Clear all the watch variables from the local store.
     */
    public void clearWatchVariables() {
	if((watchVariables == null) && (watchVariables.size() <= 0)) {
	    return;
	}
	Iterator wvi = watchVariables.iterator();
	while(wvi.hasNext()) {
	    WatchVariable wv = (WatchVariable)wvi.next();
	    wv.deleteObserver(this);
	}
	watchVariables.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Get the set of watch variables defined for this counter example.
     *
     * @return Set
     */
    public Set getWatchVariables() {
	return(watchVariables);
    }

    /**
     * Add a breakpoint to the local store.
     * 
     * @param Breakpoint breakpoint
     */
    public void addBreakpoint(Breakpoint breakpoint) {
	if(breakpoint == null) {
	    return;
	}
	if(breakpoints == null) {
	    breakpoints = new HashSet();
	}
	boolean changed = breakpoints.add(breakpoint);
	if(changed) {
	    breakpoint.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Remove a breakpoint from the local store.
     * 
     * @param Breakpoint breakpoint
     */
    public void removeBreakpoint(Breakpoint breakpoint) {
	if(breakpoint == null) {
	    return;
	}
	if(breakpoints == null) {
	    return;
	}
	boolean changed = breakpoints.remove(breakpoint);
	if(changed) {
	    breakpoint.addObserver(this);
	    setChanged();
	    notifyObservers();
	}
    }

    /**
     * Clear all the breakpoints from the local store.
     */
    public void clearBreakpoints() {
	if((breakpoints == null) || (breakpoints.size() <= 0)) {
	    return;
	}
	Iterator bi = breakpoints.iterator();
	while(bi.hasNext()) {
	    Breakpoint b = (Breakpoint)bi.next();
	}
	breakpoints.clear();
	setChanged();
	notifyObservers();
    }

    /**
     * Get the set of breakpoints defined for this counter example.
     *
     * @return Set
     */
    public Set getBreakpoints() {
	return(breakpoints);
    }

    /**
     * Clone this counter example description.
     *
     * @return CounterExampleDescription A new counter example description just like
     *         this one.
     */
    public Object clone() {
	CounterExampleDescription ced = new CounterExampleDescription();
	Set wvs = this.getWatchVariables();
	if((wvs != null) && (wvs.size() > 0)) {
	    Iterator i = wvs.iterator();
	    while(i.hasNext()) {
		WatchVariable wv = (WatchVariable)i.next();
		ced.addWatchVariable((WatchVariable)wv.clone());
	    }
	}

	Set lgs = this.getLockGraphs();
	if((lgs != null) && (lgs.size() > 0)) {
	    Iterator i = lgs.iterator();
	    while(i.hasNext()) {
		LockGraph lg = (LockGraph)i.next();
		ced.addLockGraph((LockGraph)lg.clone());
	    }
	}

	Set ods = this.getObjectDiagrams();
	if((ods != null) && (ods.size() > 0)) {
	    Iterator i = ods.iterator();
	    while(i.hasNext()) {
		ObjectDiagram od = (ObjectDiagram)i.next();
		ced.addObjectDiagram((ObjectDiagram)od.clone());
	    }
	}

	Set bps = this.getBreakpoints();
	if((bps != null) && (bps.size() > 0)) {
	    Iterator i = bps.iterator();
	    while(i.hasNext()) {
		Breakpoint bp = (Breakpoint)i.next();
		ced.addBreakpoint((Breakpoint)bp.clone());
	    }
	}

	return(ced);
    }

    public boolean equals(Object object) {

	boolean equal = false;

	if(object == null) {
	    equal = false;
	}
	else if(object == this) {
	    equal = true;
	}
	else if(object instanceof CounterExampleDescription) {
	    CounterExampleDescription ced = (CounterExampleDescription)object;
	    if((((watchVariables == null) && (ced.watchVariables == null)) ||
		(watchVariables.equals(ced.watchVariables))) &&
	       (((breakpoints == null) && (ced.breakpoints == null)) ||
		(breakpoints.equals(ced.breakpoints))) &&
	       (((lockGraphs == null) && (ced.lockGraphs == null)) ||
		(lockGraphs.equals(ced.lockGraphs))) &&
	       (((objectDiagrams == null) && (ced.objectDiagrams == null)) ||
		(objectDiagrams.equals(ced.objectDiagrams)))) {
		equal = true;
	    }
	    
	}

	return(equal);
    }

    public int hashCode() {
	int hash = 0;

	if(watchVariables != null) {
	    hash += watchVariables.hashCode();
	}

	if(breakpoints != null) {
	    hash += breakpoints.hashCode();
	}

	if(objectDiagrams != null) {
	    hash += objectDiagrams.hashCode();
	}

	if(lockGraphs != null) {
	    hash += lockGraphs.hashCode();
	}

	return(hash);
    }

    public String toString() {

	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);

	pw.println("Counter Example Description: ");
	
	Set wvs = this.getWatchVariables();
	if((wvs != null) && (wvs.size() > 0)) {
	    Iterator i = wvs.iterator();
	    while(i.hasNext()) {
		WatchVariable wv = (WatchVariable)i.next();
		pw.println(wv.toString());
	    }
	}

	Set lgs = this.getLockGraphs();
	if((lgs != null) && (lgs.size() > 0)) {
	    Iterator i = lgs.iterator();
	    while(i.hasNext()) {
		LockGraph lg = (LockGraph)i.next();
		pw.println(lg.toString());
	    }
	}

	Set ods = this.getObjectDiagrams();
	if((ods != null) && (ods.size() > 0)) {
	    Iterator i = ods.iterator();
	    while(i.hasNext()) {
		ObjectDiagram od = (ObjectDiagram)i.next();
		pw.println(od.toString());
	    }
	}

	Set bps = this.getBreakpoints();
	if((bps != null) && (bps.size() > 0)) {
	    Iterator i = bps.iterator();
	    while(i.hasNext()) {
		Breakpoint bp = (Breakpoint)i.next();
		pw.println(bp.toString());
	    }
	}

	return(sw.toString());

    }

    public void update(Observable o, Object arg) {
	setChanged();
	notifyObservers();
    }

}
