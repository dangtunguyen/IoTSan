package edu.ksu.cis.bandera.bui;

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
import javax.swing.*;
public class IconLibrary {
	public static Icon errorIcon;
	public static Icon packageIcon;
	public static Icon classIcon;
	public static Icon interfaceIcon;
	public static Icon methodIcon;
	public static Icon fieldIcon;
	public static Icon spinIcon;
	public static Icon smvIcon;
	public static Icon jpfIcon;
	public static Icon spinSelectedIcon;
	public static Icon smvSelectedIcon;
	public static Icon jpfSelectedIcon;
	public static Icon arrow = new ImageIcon(BUI.class.getResource("images/right_arrow.gif"));
	public static Icon earrow = new ImageIcon(BUI.class.getResource("images/empty_arrow.gif"));
/**
 * 
 */
public static void initialize() {
	errorIcon = new ImageIcon(BUI.class.getResource("images/error.gif"));
	packageIcon = new ImageIcon(BUI.class.getResource("images/package.gif"));
	classIcon = new ImageIcon(BUI.class.getResource("images/class.gif"));
	interfaceIcon = new ImageIcon(BUI.class.getResource("images/interface.gif"));
	methodIcon = new ImageIcon(BUI.class.getResource("images/method.gif"));
	fieldIcon = new ImageIcon(BUI.class.getResource("images/field.gif"));
	spinIcon = new ImageIcon(BUI.class.getResource("images/Spin.gif"));
	smvIcon = new ImageIcon(BUI.class.getResource("images/NuSMV.gif"));
	jpfIcon = new ImageIcon(BUI.class.getResource("images/JPF2.gif"));
	spinSelectedIcon = new ImageIcon(BUI.class.getResource("images/SpinSelected.gif"));
	smvSelectedIcon = new ImageIcon(BUI.class.getResource("images/NuSMVSelected.gif"));
	jpfSelectedIcon = new ImageIcon(BUI.class.getResource("images/JPF2Selected.gif"));
	arrow = new ImageIcon(BUI.class.getResource("images/right_arrow.gif"));
	earrow = new ImageIcon(BUI.class.getResource("images/empty_arrow.gif"));
}
}
