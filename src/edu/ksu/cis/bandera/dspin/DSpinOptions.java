package edu.ksu.cis.bandera.dspin;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999   Matthew Dwyer (dwyer@cis.ksu.edu)            *
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
/**
 * DSpinOptions is used to configure the options to be used in invoking
 * the SPIN model checker on generated PROMELA systems.
 * <p>
 * Most user setable options available through the XSPIN interface
 * can be configured through this class.  Default values for those
 * options are as follows: 
 * <ul> 
 * <li> <tt>Safety</tt> : false
 * <li> <tt>Assertions</tt> : true
 * <li> <tt>AcceptanceCycles</tt> : true
 * <li> <tt>ApplyNeverClaim</tt> : true
 * <li> <tt>SearchMode</tt> : <tt>Exhaustive</tt><br>
 * 	[out of <tt>{Exhaustive, SuperTrace, HashCompact}</tt>]
 * <li> <tt>StopAtError</tt> : 1
 * <li> <tt>SaveAllTrails</tt> : true
 * <li> <tt>FindShortestTrail</tt> : false
 * <li> <tt>MemoryLimit</tt> : 128 (MBytes)
 * <li> <tt>SpaceEstimate</tt> : 500 (x10^3)
 * <li> <tt>SearchDepth</tt> : 10000
 * <li> <tt>POReduction</tt> : false
 * <li> <tt>Compression</tt> : false
 * </ul>
 * <p>
 * Some options do not make sense when used with Bandera generated
 * models.
 * <ul> 
 * <li> Bandera models will not contain "progress" state labels
 * <li> unreachable model code should not be reported to the user
 * <li> weak-fairness is not supported either 
 * </ul>
 * <p>
 * Some basic guidelines for setting SPIN options are:
 * <p>
 * If the model contains Bandera.assert(...) calls then <tt>Assertions</tt>
 * should be enabled.
 * <p> 
 * If a temporal formula is checked then never claims should be applied, 
 * otherwise it is a deadlock check.  If the temporal formula is known
 * to be a safety property then <tt>Safety</tt> should be set, otherwise
 * <tt>AcceptanceCycles</tt> should be set.  
 * <p>
 * Some option settings are linked:
 * <ul>
 * <li> Setting <tt>Safety</tt> to true automatically 
 * sets <tt>AcceptanceCycles</tt> to be false, and vice-versa.
 * </ul>
 */
import edu.ksu.cis.bandera.checker.*;
import edu.ksu.cis.bandera.util.Preferences;

public class DSpinOptions extends CheckerOption
{
	boolean Safety = false;
	boolean Assertions = true;
	boolean AcceptanceCycles = true;
	boolean ApplyNeverClaim = true;
	
	public static final int Exhaustive = 0;
	public static final int SuperTrace = 1;
	public static final int HashCompact = 2;
	
	int SearchMode = Exhaustive;
	
	int StopAtError = 1;
	boolean SaveAllTrails = true;
	boolean FindShortestTrail = false;
	
	int MemoryLimit = 128;
	int SpaceEstimate = 500;
	int SearchDepth = 10000;
	
	boolean POReduction = false;
	boolean Compression = false;

	public DSpinOptions() { super(); }
	public DSpinOptions(String s) { super(s); }
	public String compileOptions() {
	String co;
	switch (SearchMode) {
	case SuperTrace : 
	    co = "-DBITSTATE -DMEMLIM=" + MemoryLimit;
	    break;
	case HashCompact : 
	    co = "-DHC -DMEMLIM=" + MemoryLimit;
	    break;
	default : 
	    co = "-DMEMLIM=" + MemoryLimit;
	    break;
	}
	if (Safety) co = co + " -DSAFETY";
	if (!ApplyNeverClaim) co = co + " -DNOCLAIM";
	if (!POReduction) co = co + " -DNOREDUCE";
	if (Compression) co = co + " -DCOLLAPSE";
	return co;
	}
/**
 * 
 * @return int
 */
public int getMemoryLimit() {
	return MemoryLimit;
}
/**
 * 
 * @return int
 */
public int getSearchDepth() {
	return SearchDepth;
}
/**
 * 
 * @return int
 */
public int getSpaceEstimate() {
	return SpaceEstimate;
}
/**
 * 
 * @return int
 */
public int getStopAtError() {
	return StopAtError;
}
	int log2(int x) {
	int log = 0;
	for (int total = 1; total < x; log++) {
	    total = total * 2;
	}
	return log-1;
	}
	public static void main (String argv[]) {
	DSpinOptions so = new DSpinOptions();
	System.out.println("Default compile options :" + so.compileOptions());
	System.out.println("Default pan options :" + so.panOptions());
	(so = new DSpinOptions()).setSearchMode(SuperTrace);
	System.out.println("Supertrace compile options :" + so.compileOptions());
	System.out.println("Supertrace pan options :" + so.panOptions());
	(so = new DSpinOptions()).setSearchMode(HashCompact);
	System.out.println("Hash-compact compile options :" + so.compileOptions());
	System.out.println("Hash-compact pan options :" + so.panOptions());
	(so = new DSpinOptions()).setSafety(true);
	System.out.println("Safety compile options :" + so.compileOptions());
	System.out.println("Safety pan options :" + so.panOptions());
	(so = new DSpinOptions()).setMemoryLimit(1000);
	so.setSearchDepth(50000);
	so.setSpaceEstimate(2050);
	System.out.println("Limits compile options :" + so.compileOptions());
	System.out.println("Limits pan options :" + so.panOptions());
	(so = new DSpinOptions()).setApplyNeverClaim(false);
	so.setPOReduction(true);
	so.setCompression(true);
	System.out.println("No never, po, compress compile options :" + so.compileOptions());
	System.out.println("No never, po, compress pan options :" + so.panOptions());
	}
	public String panOptions() {
	String po;
	po = "-n -m" + SearchDepth + "-w" + (10 + log2(SpaceEstimate));
	if (AcceptanceCycles) po = po + " -a";
	if (!Assertions) po = po + " -A";
	if (StopAtError>1) po = po + " -c" + StopAtError;
	if (SaveAllTrails) po = po + " -e";
	if (FindShortestTrail) po = po + " -I";
	return po;
	}
	public void parseOptions(String s) {}
	public void setAcceptanceCycles(boolean ac) { 
	AcceptanceCycles = ac;
	Safety = !ac; 
	}
	public void setApplyNeverClaim(boolean nc) { ApplyNeverClaim = nc; }
	public void setAssertions(boolean as) { 
	Assertions = as; 
	}
	public void setCompression(boolean c) { Compression = c;}
	public void setFindShortestTrail(boolean st) { FindShortestTrail = st; }
	public void setMemoryLimit(int l) { MemoryLimit = l;}
	public void setPOReduction(boolean po) { POReduction = po;}
	public void setSafety(boolean s) { 
	Safety = s; 
	AcceptanceCycles = !s;
	ApplyNeverClaim = !s;
	}
	public void setSaveAllTrails(boolean at) { SaveAllTrails = at; }
	public void setSearchDepth(int d) { SearchDepth = d;}
	public void setSearchMode(int m) { SearchMode = m; }
	public void setSpaceEstimate(int e) { SpaceEstimate = e;}
	public void setStopAtError(int e) { StopAtError = 1; }
}
