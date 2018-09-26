package edu.ksu.cis.bandera.bir;

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
import ca.mcgill.sable.util.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Category;

/**
 * A lock literal value.
 * <p>
 * Can only appear in a BirState (not in transition system source).
 */

public class LockLit implements Literal, BirConstants, Cloneable {

    private static Category log = Category.getInstance(LockLit.class.getName());

    int heldCount = 0;          // number of times acquired (0 if free)
    ActiveThread owner = null;  // acquiring thread
    //boolean [] waiting;
    //int [] waitHeldCount;
    TransSystem system;
    int numThreads;
    BirState state;

    Map threadMap;
    Map waitingThreadMap;
    Map waitingCountMap;

    /*
      public LockLit(TransSystem system) {
      this.system = system;
      numThreads = system.getThreads().size();
      waiting = new boolean[numThreads];
      waitHeldCount = new int[numThreads];
      }
    */

    public LockLit(TransSystem system, BirState state) {
	this.system = system;
	this.state = state;
	ThreadVector threads = state.getThreads();
	if(threads != null) {
	    numThreads = threads.size();
	}
	else {
	    numThreads = 0;
	}
	log.debug("When creating a LockLit " + numThreads + " threads (numThreads) were found in the state.");

	waitingThreadMap = new HashMap(numThreads);
	waitingCountMap = new HashMap(numThreads);
	threadMap = new HashMap(numThreads);
	for(int i = 0; i < numThreads; i++) {
	    BirThread thread = threads.elementAt(i);
	    if((thread != null) && (thread instanceof ActiveThread)) {
		ActiveThread activeThread = (ActiveThread)thread;
		Integer tid = new Integer(activeThread.getTid());
		threadMap.put(tid, activeThread);
		waitingThreadMap.put(tid, new Boolean(false));
		waitingCountMap.put(tid, new Integer(0));
		log.debug("added thread " + tid + " to the collection.");
	    }
	}

	//waiting = new boolean[numThreads];
	//waitHeldCount = new int[numThreads];
    }

  public void apply(Switch sw)
  {
    ((ExprSwitch) sw).caseLockLit(this);
  }
  public ActiveThread getOwner() { return owner; }
  public TransSystem getSystem() { return system; }
  public Type getType() { return Type.WR_lockType; }

    /*
      public ThreadVector getWaitingThreads() {
      ThreadVector waitThreads = new ThreadVector();
      ThreadVector threads = system.getThreads();
      for (int i = 0; i < threads.size(); i++) {
      BirThread thread = threads.elementAt(i);
      if (waiting[thread.getId()])
      waitThreads.addElement(thread);
      }
      return waitThreads;
      }
    */
    public ThreadVector getWaitingThreads() {
	ThreadVector waitingThreads = new ThreadVector();
	if((waitingThreadMap != null) && (waitingThreadMap.size() > 0)) {
	    Iterator i = waitingThreadMap.keySet().iterator();
	    while(i.hasNext()) {
		Integer threadID = (Integer)i.next();
		log.debug("checking thread " + threadID + " to see if it is waiting.");

		Boolean waiting = (Boolean)waitingThreadMap.get(threadID);
		if((waiting != null) && (waiting.booleanValue())) {
		    BirThread waitingThread = (BirThread)threadMap.get(threadID);
		    // the waitingThread is really an ActiveThread!
		    if(waitingThread == null) {
			log.error("Major problem when adding a thread to the waiting list." +
				  "  No BirThread in threadMap when one exists in waitingThreadMap."+
				  "  threadID = " + threadID +
				  "  Skipping this thread.");
		    }
		    else {
			waitingThreads.addElement(waitingThread);
		    }
		}
	    }
	}
	return(waitingThreads);
    }

    /**
     * Create a copy of this LockLit.  Make sure to do a deep clone so that new maps
     * for thread collections are cloned as well.
     *
     * @return Object A new LockLit.
     */
    public Object clone() {

	LockLit lockLit = null;
	try {
	    lockLit = (LockLit)super.clone();

	    // clone the Maps
	    //log.debug("cloning the waitingThreadMap ...");
	    lockLit.waitingThreadMap = new HashMap(waitingThreadMap.size());
	    Iterator wtmi = waitingThreadMap.keySet().iterator();
	    while(wtmi.hasNext()) {
		Object key = wtmi.next();  // Integer
		Object value = waitingThreadMap.get(key); // Boolean
		//log.debug("key = " + key + ", value = " + value);
		lockLit.waitingThreadMap.put(key, value);
	    }

	    //log.debug("cloning the threadMap ...");
	    lockLit.threadMap = new HashMap(threadMap.size());
	    Iterator tmi = threadMap.keySet().iterator();
	    while(tmi.hasNext()) {
		Object key = tmi.next();  // Integer
		Object value = threadMap.get(key); // ActiveThread
		//log.debug("key = " + key + ", value = " + value);
		lockLit.threadMap.put(key, value);
	    }

	    //log.debug("cloning the waitingCountMap ...");
	    lockLit.waitingCountMap = new HashMap(waitingCountMap.size());
	    Iterator wcmi = waitingCountMap.keySet().iterator();
	    while(wcmi.hasNext()) {
		Object key = wcmi.next();  // Integer
		Object value = waitingCountMap.get(key); // Integer
		//log.debug("key = " + key + ", value = " + value);
		lockLit.waitingCountMap.put(key, value);
	    }
	}
	catch(Exception e) {
	    log.error("An exception occured while cloning the LockLit.", e);
	}

	return(lockLit);
    }

  public LockLit nextState(int operation, ActiveThread thread, int choice) {
      log.debug("getting the nextState: operation = " + operation +
		", threadID = " + thread.getTid() + ", choice = " + choice);
      LockLit nextState;

      nextState = (LockLit) this.clone();
      if(nextState == null) {
	  log.warn("After cloning the current state, the next state was null.  This will be returned.");
	  return(nextState);
      }
      
    switch (operation) {
    case LOCK: 
      if (owner == null)
	nextState.owner = thread;
      else 
	nextState.heldCount++;
      break;
    case UNLOCK: 
      if (heldCount == 0)
	nextState.owner = null;
      else 
	nextState.heldCount--;
      break;
    case WAIT: 
	// the wait will cause the thread to placed into a waiting state

	//nextState.waiting = (boolean []) waiting.clone();
	//nextState.waiting[thread.getId()] = true;
	//nextState.waitingThreadMap = new HashMap(waitingThreadMap);
	nextState.waitingThreadMap.put(new Integer(thread.getTid()), new Boolean(true));
	nextState.threadMap.put(new Integer(thread.getTid()), thread);

	//nextState.waitHeldCount = (int []) waitHeldCount.clone();
	//nextState.waitHeldCount[thread.getId()] = heldCount;
	//nextState.waitingCountMap = new HashMap(waitingCountMap);
	nextState.waitingCountMap.put(new Integer(thread.getTid()), new Integer(heldCount));

	nextState.owner = null;
	break;
    case UNWAIT:
	//nextState.heldCount = waitHeldCount[thread.getId()];
	//nextState.heldCount = waitHeldCount[thread.getTid()];
	Integer waitingCount = (Integer)waitingCountMap.get(new Integer(thread.getTid()));
	nextState.heldCount = waitingCount.intValue();
	nextState.owner = thread;
	break;
    case NOTIFY:
	// the notify will cause the particular thread to be awaken (thus, not waiting!)

	//nextState.waiting = (boolean []) waiting.clone();
	//nextState.waitingThreadMap = new HashMap(waitingThreadMap);
	//if (choice < numThreads) {
	if(nextState.waitingThreadMap.containsKey(new Integer(choice))) {
	    //nextState.waiting[choice] = false;
	    nextState.waitingThreadMap.put(new Integer(choice), new Boolean(false));
	    nextState.threadMap.put(new Integer(choice), thread);
	}
	break;
    case NOTIFYALL:
	// the notify all will cause all threads to be taken from the waiting state

	//nextState.waiting = (boolean []) waiting.clone();
	nextState.waitingThreadMap = new HashMap(waitingThreadMap);
	for (int i = 0; i < numThreads; i++) {
	    //nextState.waiting[i] = false;
	    nextState.waitingThreadMap.put(new Integer(i), new Boolean(false));
	    nextState.threadMap.put(new Integer(i), thread);
	}
	break;
    default:
      throw new RuntimeException("Bad operation code");
    }
    return nextState;
  }

  public boolean queryState(int operation, ActiveThread thread) {
    switch (operation) {
    case LOCK_AVAILABLE: 
      return owner == null;
    case HAS_LOCK: 
      return owner == thread;
    case WAS_NOTIFIED: 
	//return ! waiting[thread.getId()];
	Boolean waitingBoolean = (Boolean)waitingThreadMap.get(new Integer(thread.getTid()));
	if(waitingBoolean != null) {
	    return(waitingBoolean.booleanValue());
	}
	else {
	    return(false);
	}
    default:
      throw new RuntimeException("Bad operation code");
    }
  }

  public String toString() {
      String ownerName;
      if (owner != null) {
	  ownerName = owner.getName() + "[" + owner.getTid() + "]";
      }
      else {
	  ownerName = "*";
      }
      String result = "Lock:" + ownerName;

      /*
      for (int i = 0; i < numThreads; i++)
	  if(waiting[i])
	      result += "," + i;
      */

      if((threadMap != null) && (threadMap.size() > 0)) {
	  Iterator threadIterator = threadMap.keySet().iterator();
	  while(threadIterator.hasNext()) {
	      Integer threadID = (Integer)threadIterator.next();
	      Boolean threadWaiting = (Boolean)waitingThreadMap.get(threadID);
	      if((threadWaiting != null) && (threadWaiting.booleanValue())) {
		  result += "," + threadID.toString();
	      }
	  }
      }
      return "<" + result + ">";
  }
}
