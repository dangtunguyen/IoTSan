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

class Main {


public static void main(String[] args) {

  PrintWriter f_pvs, f_prf, f_el;



  try {

  
  FileInputStream spec = new FileInputStream(args[0]);
  if(spec==null) 
	{ System.err.println("Error: Unable to open specification file");
	   	  return;
	}
  parser parser_obj = new parser(new Yylex(spec));
  parser_obj.parse();
  if(parser_obj.npredicates != parser_obj.tokens.size()) {
	System.err.println("Error: wrong tokens set!"); return;
  }
  spec.close();
  
  f_pvs = new PrintWriter(new OutputStreamWriter(
			  new  FileOutputStream("abstraction.pvs")));
  f_prf = new PrintWriter(new OutputStreamWriter(
			  new  FileOutputStream("abstraction.prf")));
  f_el  = new PrintWriter(new OutputStreamWriter(
			  new  FileOutputStream("abstraction.el")));

  if(f_pvs == null || f_prf == null || f_el == null)
			  { System.err.println("Error: Unable to open tmp file");
				return;
			  }

  if (parser_obj.basic_type==sym.INT) PVS.begin_int(f_pvs);
  else PVS.begin_float(f_pvs);
  parser_obj.tree.print_PVS(f_pvs);

  PVS.check(f_pvs,parser_obj.npredicates);

  PVS.rest(f_pvs,parser_obj.npredicates,parser_obj.basic_type);

  PVS.prf(f_prf,parser_obj.npredicates,parser_obj.basic_type);

  f_el.println("(prove-pvs-file \"abstraction\")");

  f_prf.close();
  f_pvs.close();
  f_el.close();
 

  PVS.execAndWait("pvs -batch -q -l abstraction.el -v 1");

  PrintWriter f_spec = new PrintWriter(new OutputStreamWriter(
			  new  FileOutputStream(parser_obj.name+"Abstraction.basl")));

  if(f_spec == null)
	      { System.err.println("Error: Unable to open tmp file");
				return;
			  }

  /* antet of generated BASL file */
  if(parser_obj.basic_type==sym.INT)
  	f_spec.print("abstraction "+parser_obj.name+" extends integral\n begin\n");
  else
	f_spec.print("abstraction "+parser_obj.name+" extends real\n begin\n");

  f_spec.print("  TOKENS = { ");
  for(int i=0;i<parser_obj.npredicates-1;i++)
	f_spec.print(parser_obj.tokens.elementAt(i)+" , ");
  f_spec.print(parser_obj.tokens.elementAt(parser_obj.npredicates-1)+" }; ");

  PVS.check_BASL(f_spec,parser_obj.npredicates,parser_obj.tokens);

  f_spec.print("\n\n  abstract("+parser_obj.variable+")\n  begin\n");
  parser_obj.tree.print_BASL(f_spec);
  f_spec.print("  end\n\n");


  PVS.rest_BASL(f_spec,parser_obj.npredicates,parser_obj.basic_type,
		parser_obj.tokens);
  f_spec.close();

  //Pvs_Spec.rest_spec(PVS.commandOut);

  } catch (Exception e) {
		e.printStackTrace();
  }
}
}
