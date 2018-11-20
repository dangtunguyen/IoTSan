package edu.ksu.cis.bandera.bui.dialog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * State Chart Analysis (SCA), a State Chart Analyzer                *
 * Copyright (C) 2000-2002 Roby Joehanes (robbyjo@cis.ksu.edu)       *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as the author's master thesis project in       *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/~robbyjo/thesis).         *
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
 * this project, please visit the web-site:                          *
 *            http://www.cis.ksu.edu/~robbyjo/thesis                 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
/**
 * Insert the type's description here.
 * Creation date: (7/8/01 11:46:01 PM)
 * @author: Roby Joehanes
 */
public class FileDialogFactory {
public static JFileChooser create(List l, String desc)
{
	JFileChooser fc = new JFileChooser();
	CustomFileFilter ff = new CustomFileFilter();

	if (l != null)
	{
		ff.setFileList(l);
		if (desc != null) ff.setDescription(desc);
		fc.addChoosableFileFilter(ff);
		fc.setFileFilter(ff);
	}
	fc.setMultiSelectionEnabled(false);
	fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	return fc;
}
public static File display(JFileChooser fc, JFrame parent, String text)
{
	if (fc == null) return null;
	File chosen = null;
	
	do {
		int response = fc.showDialog(parent, text);
		if (response == JFileChooser.APPROVE_OPTION)
		{
			chosen = fc.getSelectedFile();
			if (chosen.isDirectory())
			{
				fc.setCurrentDirectory(chosen);
				fc.rescanCurrentDirectory();
			} else //if (chosen.exists())
			{
				return chosen;
			}
		} else return null;
	} while (true);

}
}
