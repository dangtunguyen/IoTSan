package edu.ksu.cis.bandera.abstraction.pvs;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Corina Pasareanu (pcorina@cis.ksu.edu) *
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

public class PVS {
  public static Vector proofs; /* contains pvs proofs */

/* builds the antet of a pvs theory for float */
 public static void begin_float(PrintWriter f_pvs) {
	f_pvs.print("abstraction: THEORY\n\nBEGIN\n\n");
		f_pvs.print(" b: VAR boolean\n");
		f_pvs.print(" TRUE?(b): boolean = (b=TRUE)\n");
		f_pvs.print(" FALSE?(b): boolean = (b=FALSE)\n\n");
		f_pvs.print(" \n");
		f_pvs.print(" e,e1,e2: VAR real\n");
		f_pvs.print(" nz_e: VAR {e | e/=0}\n");
 } 
/* builds the antet of a pvs theory for int */
 public static void begin_int(PrintWriter f_pvs) {
		f_pvs.print("basic1: THEORY\n\nBEGIN\n\n");
		f_pvs.print(" e1: VAR int\n e2: VAR {e:int|e/=0}\n");
		f_pvs.print(" /(e1,e2) : int = floor(e1/e2)\n END basic1\n\n");
		f_pvs.print("basic2: THEORY\n\nBEGIN\n\n");
		f_pvs.print(" e1: VAR int\n e2: VAR {e:int|e/=0}\n");
		f_pvs.print(" //(e1,e2) : int = e1 - e2*floor(e1/e2)\n");
		f_pvs.print(" END basic2\n\n");
		
		f_pvs.print(" abstraction: THEORY\n\nBEGIN\n\n");
		f_pvs.print("IMPORTING basic1\n");
		f_pvs.print("IMPORTING basic2\n");
		f_pvs.print(" b: VAR boolean\n");
		f_pvs.print(" TRUE?(b): boolean = (b=TRUE)\n");
		f_pvs.print(" FALSE?(b): boolean = (b=FALSE)\n\n");
		f_pvs.print(" \n");
		f_pvs.print(" e,e1,e2: VAR int\n");
		f_pvs.print(" nz_e: VAR {e | e/=0}\n");

 } 
/* builds the checking for function and default value of pvs theory */
 public static void check(PrintWriter f_pvs, int npredicates) {
	int i,j;
	for(i=0;i<npredicates;i++)
	  f_pvs.print(" default"+i+": THEOREM P"+i+"(0)\n");

	f_pvs.print(" domain: THEOREM ");
	for(i=0;i<npredicates-1;i++)
		  f_pvs.print(" P"+i+"(e) OR ");
	f_pvs.print(" P"+i+"(e)\n");

	f_pvs.print(" overlap: THEOREM ");
	for(i=0;i<npredicates;i++)
	  for(j=i+1;j<npredicates;j++)
	    f_pvs.print(" NOT(P"+i+"(e) AND P"+j+"(e)) AND ");
		f_pvs.print(" TRUE\n\n");  	
 } 
 /* writes the results of check part in abstraction f_spec file */
 public static void check_BASL(PrintWriter f_spec, int npredicates,
				Vector tokens) {
	int lookahead;

	for(int i=0;i<npredicates;i++) {
		lookahead=((Integer)proofs.elementAt(i)).intValue();
		if(lookahead==1) { /* PROVED */
			f_spec.print("\n  DEFAULT = "+tokens.elementAt(i)+";\n");
			i=npredicates;
		}
	}
	lookahead=((Integer)proofs.elementAt(npredicates)).intValue();
	if(lookahead==0)  /* UNPROVED */
		f_spec.print("/* Warning: abstraction does not cover the concrete domain!!! */\n");
	lookahead=((Integer)proofs.elementAt(npredicates+1)).intValue();
		if(lookahead==0)  /* UNPROVED */
				f_spec.print("/* Warning: overlapping !!! */\n");
	
	
 } 
/* invokes PVS */
 public static void execAndWait(String command) {

	try {
	    System.out.println(command);
	    Runtime runtime = Runtime.getRuntime();
	    Process p = runtime.exec(command);
	    InputStream commandErr = p.getErrorStream();
			InputStream commandOut = p.getInputStream();

			byte [] buffer = new byte[4000];
	    int count = 0;       // total chars read
			int charsAvail = commandErr.available();
			if (charsAvail > 0) 
				count = commandErr.read(buffer,count,charsAvail);
	    
   	    proofs = read(commandOut);

	    String output = new String(buffer,0,count);
			System.err.println(output);
			p.waitFor();
			return;
		}
		catch (Exception e) {
			throw new RuntimeException("exec of command '" + command
									   + "' was aborted: \n" + e);
		}

 } 
/* builds the prf file */
 public static void prf(PrintWriter f_prf, int npredicates, int basic_type)
 {
  int n; /* theorem number */
  int i,j,k;

  /* for each operation generate all the possible combinations 
	 given by npredicates */

  f_prf.print("(|abstraction|\n");

  for(i=0;i<npredicates;i++)
  	f_prf.print(" (|default"+i+"| \"\" (GRIND) (SKIP) (SKIP))\n");
  f_prf.print(" (|domain| \"\" (GRIND) (SKIP) (SKIP))\n");
  f_prf.print(" (|overlap| \"\" (GRIND) (SKIP) (SKIP))\n");

  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_prf.print(" (|add"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
	  } 

  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_prf.print(" (|sub"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
	  }

  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_prf.print(" (|mul"+n+"| \"\" (GRIND :THEORIES (\"real_props\")) (SKIP) (SKIP))\n");
		n++;
	  }

  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_prf.print(" (|div"+n+"| \"\" (GRIND :THEORIES (\"real_props\")) (SKIP) (SKIP))\n");
		n++;
	  }

  if(basic_type==sym.INT) {
  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_prf.print(" (|rem"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
	  }
  }

  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++) {
		f_prf.print(" (|eq"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
		f_prf.print(" (|eq"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
	  }



  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++) {
		f_prf.print(" (|lt"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
		f_prf.print(" (|lt"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
	  }


  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++) {
		f_prf.print(" (|gt"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
		f_prf.print(" (|gt"+n+"| \"\" (GRIND) (SKIP) (SKIP))\n");
		n++;
	  }



  f_prf.print(")");
  
 } 
/* reads the output of PVS in a vector */
 public static Vector read(InputStream is) {
	try {
		BufferedReader r = new BufferedReader( new InputStreamReader(is));
	String line = r.readLine();
		Vector v = new Vector();
		
		while(line != null) {
	  StringTokenizer st = new StringTokenizer(line);
	  while (st.hasMoreTokens()) {
			   String s = st.nextToken();
	       if(s.compareTo("proved")==0) 
		 v.add( new Integer(1)); 
	       if(s.compareTo("unproved")==0)
				 v.add( new Integer(0)); 
		  }
	  line = r.readLine();
		}
		r.close();
		return v;       
	} catch (Exception e) {
		e.printStackTrace();
	throw new RuntimeException("could not read output from pvs\n");
	}
 } 
/* builds the rest of the pvs theory */
 public static void rest(PrintWriter f_pvs, int npredicates, int basic_type) {

  int n; /* theorem number */
  int i,j,k;

  /* for each operation generate all the possible combinations 
	 given by npredicates */


  f_pvs.println("\n");

  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_pvs.print(" add"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT P"+k+"(e1+e2))\n");
		n++;
	  } 

  f_pvs.print("\n");
  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_pvs.print(" sub"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT P"+k+"(e1-e2))\n");
		n++;
	  }

  f_pvs.print("\n");
  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_pvs.print(" mul"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT P"+k+"(e1*e2))\n");
		n++;
	  }

  f_pvs.print("\n");
  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_pvs.print(" div"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(nz_e)) IMPLIES NOT P"+k+"(e1/nz_e))\n");
		n++;
	  }

  if(basic_type==sym.INT) {
  f_pvs.print("\n");
  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++)
	  for(k=0;k<npredicates;k++) {
		f_pvs.print(" rem"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(nz_e)) IMPLIES NOT P"+k+"(e1//nz_e))\n");
		n++;
	  }
  }
  f_pvs.print("\n");
  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++) {
		f_pvs.print(" eq"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT TRUE?(e1=e2))\n"); n++;
		f_pvs.print(" eq"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT FALSE?(e1=e2))\n");
		n++;
	  }



  f_pvs.print("\n");
  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++) {
		f_pvs.print(" lt"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT TRUE?(e1<e2))\n"); n++;
		f_pvs.print(" lt"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT FALSE?(e1<e2))\n");
		n++;
	  }



  f_pvs.print("\n");
  n=1;
  for(i=0;i<npredicates;i++)
	for(j=0;j<npredicates;j++) {
		f_pvs.print(" gt"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT TRUE?(e1>e2))\n"); n++;
		f_pvs.print(" gt"+n+": THEOREM ((P"+i+"(e1) AND P"+j+"(e2)) IMPLIES NOT FALSE?(e1>e2))\n");
		n++;
	  }


  f_pvs.println("END abstraction");

 } 
/* writes ops in abstraction f_spec file */
 public static void rest_BASL(PrintWriter f_spec, int npredicates, 
			      int basic_type, Vector tokens) {
	int n,i,j,k,flag,m;

	Vector v;
		int value; /* 1 = TRUE, 2 = FALSE, 0 = T */

		try {
	
	n=npredicates+2; /* ignore first npredicates+2 proofs from check */

		int lookahead=((Integer)proofs.elementAt(n)).intValue();

	/* generate code for add */
	f_spec.print(" operator + add\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {
			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");flag=0;
			for(k=0;k<npredicates;k++) {
			  if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" "+tokens.elementAt(k)+" ");
			  }
			  n++; lookahead=((Integer)proofs.elementAt(n)).intValue();
			}
			if (flag==0) f_spec.print(" T; /* ERROR */\n");
			else f_spec.print("};\n");
		  } 
		f_spec.print("  end\n\n");

		/* generate code for sub */
	f_spec.print(" operator - sub\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {
			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");flag=0;
			for(k=0;k<npredicates;k++) {
			  if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" "+tokens.elementAt(k)+" ");
			  }
			  n++; lookahead=((Integer)proofs.elementAt(n)).intValue();
			}
			if (flag==0) f_spec.print(" T; /* ERROR */\n");
			else f_spec.print("};\n");
		  } 
		f_spec.print("  end\n\n");

	/* generate code for mul */
	f_spec.print(" operator * mul\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {
			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");flag=0;
			for(k=0;k<npredicates;k++) {
			  if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" "+tokens.elementAt(k)+" ");
			  }
			  n++; lookahead=((Integer)proofs.elementAt(n)).intValue();
			}
			if (flag==0) f_spec.print(" T; /* ERROR */\n");
			else f_spec.print("};\n");
		  } 
		f_spec.print("  end\n\n");

	
		/* generate code for div */
	f_spec.print(" operator / div\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {
			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");flag=0;
			for(k=0;k<npredicates;k++) {
			  if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" "+tokens.elementAt(k)+" ");
			  }
			  n++; lookahead=((Integer)proofs.elementAt(n)).intValue();
			}
			if (flag==0) f_spec.print(" T; /* ERROR */\n");
			else f_spec.print("};\n");
		  } 
		f_spec.print("  end\n\n");

	if(basic_type == sym.INT) {
	/* generate code for rem */
	f_spec.print(" operator % rem\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {
			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");flag=0;
			for(k=0;k<npredicates;k++) {
			  if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" "+tokens.elementAt(k)+" ");
			  }
			  n++; lookahead=((Integer)proofs.elementAt(n)).intValue();
			}
			if (flag==0) f_spec.print(" T; /* ERROR */\n");
			else f_spec.print("};\n");
		  } 
		f_spec.print("  end\n\n");
	}


	
	v = new Vector();
	/* generate code for eq */
	f_spec.print(" test == eq\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {

	    value = 0;

			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");flag=0;
			if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" TRUE ");

		value = 1;

			  }
			n++; lookahead=((Integer)proofs.elementAt(n)).intValue();
			if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" FALSE ");

		if(value==1) value=0; else value=2;

			  }
			n++; lookahead=((Integer)proofs.elementAt(n)).intValue();            

			if (flag==0) f_spec.print(" T; /* ERROR */\n");
			else f_spec.print("};\n");

	    v.add( new Integer(value));

		  } 
		f_spec.print("  end\n\n");

	/* generate code for neq */
	m =0;
	f_spec.print(" test != neq\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {
			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");

	    value = ((Integer)v.elementAt(m)).intValue();m++;
			if(value==0) /* T */ 
	      f_spec.print("{ TRUE , FALSE };\n");
	    if(value==1) /* TRUE */
	      f_spec.print("{ FALSE };\n");
	    if(value==2) /* FALSE */
	      f_spec.print("{ TRUE };\n");
		  }    
		f_spec.print("  end\n\n");

		v = new Vector();
	/* generate code for lt */
	f_spec.print(" test < lt\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {

	    value = 0;

			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");flag=0;
			if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" TRUE ");

		value = 1;

			  }
			n++; lookahead=((Integer)proofs.elementAt(n)).intValue();
			if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" FALSE ");

		if(value==1) value=0; else value=2;

			  }
			n++; lookahead=((Integer)proofs.elementAt(n)).intValue();            

			if (flag==0) f_spec.print(" T; /* ERROR */\n");
			else f_spec.print("};\n");

	    v.add( new Integer(value));
		  } 
		f_spec.print("  end\n\n");

		/* generate code for ge */
	m =0;
	f_spec.print(" test >= ge\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {
			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");

	    value = ((Integer)v.elementAt(m)).intValue();m++;
			if(value==0) /* T */ 
	      f_spec.print("{ TRUE , FALSE };\n");
	    if(value==1) /* TRUE */
	      f_spec.print("{ FALSE };\n");
	    if(value==2) /* FALSE */
	      f_spec.print("{ TRUE };\n");
		  }     
		f_spec.print("  end\n\n");

		v = new Vector();
	/* generate code for gt */
	f_spec.print(" test > gt\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {

	    value = 0;

			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");flag=0;
			if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" TRUE ");

		value = 1;

			  }
			n++; lookahead=((Integer)proofs.elementAt(n)).intValue();
			if(lookahead==0) /* UNPROVED */ {
				if(flag==0) {f_spec.print("{");flag=1;}
				else f_spec.print(",");
				f_spec.print(" FALSE ");

		if(value==1) value=0; else value=2;

			  }
			n++; lookahead=((Integer)proofs.elementAt(n)).intValue();            

			if (flag==0) f_spec.print(" T; /* ERROR */\n");
			else f_spec.print("};\n");

	    v.add( new Integer(value));
		  } 
		f_spec.print("  end\n\n");

		/* generate code for le */
	m =0;
	f_spec.print(" test <= le\n  begin\n");
	for(i=0;i<npredicates;i++)
		  for(j=0;j<npredicates;j++) {
			f_spec.print("   ("+tokens.elementAt(i)+" , "+tokens.elementAt(j)+
			 ") -> ");

	    value = ((Integer)v.elementAt(m)).intValue();m++;
			if(value==0) /* T */ 
	      f_spec.print("{ TRUE , FALSE };\n");
	    if(value==1) /* TRUE */
	      f_spec.print("{ FALSE };\n");
	    if(value==2) /* FALSE */
	      f_spec.print("{ TRUE };\n");
		  }     
		f_spec.print("  end\n\n");

		f_spec.print(" end\n");

		 } catch (Exception e) {
			e.printStackTrace();
			 throw new 
	     RuntimeException("could not generate spec file from pvs output");
		 }

 } 
}
