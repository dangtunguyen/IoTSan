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
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.specification.assertion.datastructure.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.jjjc.symboltable.*;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionManager;


public class BUISessionPane extends JPanel {
	private boolean showIconRightTree;
	private JSplitPane ivjBUISplitPane = null;
	private JScrollPane ivjLeftScrollPane = null;
	private JTree ivjLeftTree = null;
	private JScrollPane ivjRightScrollPane = null;
	private JTree ivjRightTree = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();

class IvjEventHandler implements java.awt.event.MouseListener, javax.swing.event.TreeSelectionListener {
		public void mouseClicked(java.awt.event.MouseEvent e) {};
		public void mouseEntered(java.awt.event.MouseEvent e) {};
		public void mouseExited(java.awt.event.MouseEvent e) {};
		public void mousePressed(java.awt.event.MouseEvent e) {};
		public void mouseReleased(java.awt.event.MouseEvent e) {
			if (e.getSource() == BUISessionPane.this.getLeftTree()) 
				connEtoC2(e);
		};
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			if (e.getSource() == BUISessionPane.this.getLeftTree()) 
				connEtoC1();
		};
	};
/**
 * BUISessionPane constructor comment.
 */
public BUISessionPane() {
	super();
	initialize();
}
/**
 * BUISessionPane constructor comment.
 * @param layout java.awt.LayoutManager
 */
public BUISessionPane(LayoutManager layout) {
	super(layout);
}
/**
 * BUISessionPane constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public BUISessionPane(LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}
/**
 * BUISessionPane constructor comment.
 * @param isDoubleBuffered boolean
 */
public BUISessionPane(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
/**
 * 
 * @param node com.sun.java.swing.tree.DefaultMutableTreeNode
 * @param ann edu.ksu.cis.bandera.annotation.BlockStmtAnnotation
 */
private static void addBlock(DefaultMutableTreeNode node, BlockStmtAnnotation ann) {
	if (ann == null) return;
	
	Vector anns = ann.getContainedAnnotations();
	for (Enumeration e = anns.elements(); e.hasMoreElements();) {
		for (Enumeration e2 = buildTree((Annotation) e.nextElement()).elements(); e2.hasMoreElements();) {
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) e2.nextElement();
			Object o = n.getUserObject();
			if (!"".equals(o.toString().trim())) node.add(n);
		}
	}
}
/**
 * 
 * @return javax.swing.tree.DefaultMutableTreeNode
 * @param sc ca.mcgill.sable.soot.SootClass
 */
private DefaultMutableTreeNode buildClassNode(SootClass sc) {
	AnnotationManager am = CompilationManager.getAnnotationManager();

	TreeSet ts = new TreeSet();
	for (ca.mcgill.sable.util.Iterator i = sc.getFields().iterator(); i.hasNext();) {
		Annotation a = am.getAnnotation(sc, i.next());
		if (a != null) 
			ts.add(a);
	}
	for (ca.mcgill.sable.util.Iterator i = sc.getMethods().iterator(); i.hasNext();) {
		Annotation a = am.getAnnotation(sc, i.next());
		if (a != null)
			ts.add(a);
	}
	
	DefaultMutableTreeNode node = new DefaultMutableTreeNode(sc);

	for (Iterator i = ts.iterator(); i.hasNext();) {
		node.add(new DefaultMutableTreeNode(i.next()));
	}
	
	return node;
}
/**
 * 
 * @return javax.swing.tree.DefaultMutableTreeNode
 * @param p edu.ksu.cis.bandera.jjjc.symboltable.Package
 */
private DefaultMutableTreeNode buildPackageNode(Package p) {
	DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(p);
	Hashtable compiledClasses = CompilationManager.getCompiledClasses();
	
	TreeSet cNames = new TreeSet();
	for (Enumeration e = compiledClasses.elements(); e.hasMoreElements();) {
		Name cName = new Name(((SootClass) e.nextElement()).getName());
		if (cName.getSuperName().equals(p.getName()))
			cNames.add(cName);
	}
	
	for (Iterator j = cNames.iterator(); j.hasNext();) {
		Name cName = (Name) j.next();
		SootClass sc = (SootClass) compiledClasses.get(cName.toString());
		pNode.add(buildClassNode(sc));
	}
	return pNode;
}
/**
 * 
 * @return java.util.Vector
 * @param annotation edu.ksu.cis.bandera.annotation.Annotation
 */
public static Vector buildTree(Annotation annotation) {
	Vector result = new Vector();

	if (annotation == null) return result;

	DefaultMutableTreeNode node = new DefaultMutableTreeNode(annotation);

	result.addElement(node);
	
	if (annotation instanceof BlockStmtAnnotation) {
		addBlock(node, (BlockStmtAnnotation) annotation);
	} else if (annotation instanceof BreakStmtAnnotation) {
	} else if (annotation instanceof ContinueStmtAnnotation) {
	} else if (annotation instanceof DoWhileStmtAnnotation) {
		Annotation ann = ((DoWhileStmtAnnotation) annotation).getBlockAnnotation();
		if (ann instanceof BlockStmtAnnotation) {
			addBlock(node, (BlockStmtAnnotation) ann);
		} else {
			for (Enumeration e = buildTree(ann).elements(); e.hasMoreElements();) {
				node.add((DefaultMutableTreeNode) e.nextElement());
			}
		}
	} else if (annotation instanceof EmptyStmtAnnotation) {
	} else if (annotation instanceof ExpStmtAnnotation) {
	} else if (annotation instanceof FieldDeclarationAnnotation) {
	} else if (annotation instanceof ForStmtAnnotation) {
		Annotation ann = ((ForStmtAnnotation) annotation).getBlockAnnotation();
		if (ann instanceof BlockStmtAnnotation) {
			addBlock(node, (BlockStmtAnnotation) ann);
		} else {
			for (Enumeration e = buildTree(ann).elements(); e.hasMoreElements();) {
				node.add((DefaultMutableTreeNode) e.nextElement());
			}
		}
	} else if (annotation instanceof IfStmtAnnotation) {
		Annotation tann = ((IfStmtAnnotation) annotation).getThenAnnotation();
		Annotation eann = ((IfStmtAnnotation) annotation).getElseAnnotation();
		for (Enumeration e = buildTree(tann).elements(); e.hasMoreElements();) {
			node.add((DefaultMutableTreeNode) e.nextElement());
		}
		
		if (!(eann instanceof EmptyStmtAnnotation)) {
			DefaultMutableTreeNode elseNode = new DefaultMutableTreeNode("else");
			for (Enumeration e = buildTree(eann).elements(); e.hasMoreElements();) {
				elseNode.add((DefaultMutableTreeNode) e.nextElement());
			}
			result.addElement(elseNode);
		}
	} else if (annotation instanceof InstanceInitializerAnnotation) {
	} else if (annotation instanceof LabeledStmtAnnotation) {
		for (Enumeration e = buildTree(((LabeledStmtAnnotation) annotation).getAnnotation()).elements();
				e.hasMoreElements();) {
			node.add((DefaultMutableTreeNode) e.nextElement());
		}
	} else if (annotation instanceof EmptyStmtAnnotation) {
	} else if (annotation instanceof LocalDeclarationStmtAnnotation) {
	} else if (annotation instanceof ReturnStmtAnnotation) {
	} else if (annotation instanceof StaticInitializerAnnotation) {
	} else if (annotation instanceof SwitchStmtAnnotation) {
		Hashtable cases = ((SwitchStmtAnnotation) annotation).getSwitchCases();
		for (Enumeration e = ((SwitchStmtAnnotation) annotation).getSwitchCases().elements(); e.hasMoreElements();) {
			BlockStmtAnnotation a = (BlockStmtAnnotation) e.nextElement();
			DefaultMutableTreeNode caseNode = new DefaultMutableTreeNode(a.getNode());
			if (a.getNode() instanceof edu.ksu.cis.bandera.jjjc.node.ACaseSwitchLabel) {
				edu.ksu.cis.bandera.jjjc.node.ACaseSwitchLabel caseLabel = (edu.ksu.cis.bandera.jjjc.node.ACaseSwitchLabel) a.getNode();
				caseNode.setUserObject("case " + caseLabel.getExp().toString().trim() + ":");
			} else if (a.getNode() instanceof edu.ksu.cis.bandera.jjjc.node.ASwitchBlockStmtGroup) {
				edu.ksu.cis.bandera.jjjc.node.ASwitchBlockStmtGroup caseLabel = (edu.ksu.cis.bandera.jjjc.node.ASwitchBlockStmtGroup) a.getNode();
				String label = "";
				for (ca.mcgill.sable.util.Iterator i = caseLabel.getSwitchLabel().iterator(); i.hasNext();) {
					label += i.next();
				}

				caseNode.setUserObject(label);
			}
			addBlock(caseNode, a);
			node.add(caseNode);
		}
		Annotation da = ((SwitchStmtAnnotation) annotation).getDefaultAnnotation();
		if (da != null) {
			DefaultMutableTreeNode defaultNode = new DefaultMutableTreeNode("default:");
			addBlock(defaultNode, (BlockStmtAnnotation) da);
			node.add(defaultNode);
		}
	} else if (annotation instanceof SynchronizedStmtAnnotation) {
		addBlock(node, (BlockStmtAnnotation) ((SynchronizedStmtAnnotation) annotation).getBlockAnnotation());
	} else if (annotation instanceof ThrowStmtAnnotation) {
	} else if (annotation instanceof TryFinallyStmtAnnotation) {
		addBlock(node, (BlockStmtAnnotation) ((TryStmtAnnotation) annotation).getBlockAnnotation());
		for (Enumeration e = ((TryStmtAnnotation) annotation).getCatchClauses().elements();
				e.hasMoreElements();) {
			Annotation a = (Annotation) e.nextElement();
			edu.ksu.cis.bandera.jjjc.node.ACatchClause catchClause = (edu.ksu.cis.bandera.jjjc.node.ACatchClause) a.getNode();
	
			DefaultMutableTreeNode catchNode = new DefaultMutableTreeNode(a);
			addBlock(catchNode, (BlockStmtAnnotation) a);
			result.addElement(catchNode);
		}
		Annotation a = ((TryFinallyStmtAnnotation) annotation).getFinallyAnnotation();
		DefaultMutableTreeNode finallyNode = new DefaultMutableTreeNode(a);
		finallyNode.setUserObject("finally");
		addBlock(finallyNode, (BlockStmtAnnotation) a);
		result.addElement(finallyNode);
	} else if (annotation instanceof TryStmtAnnotation) {
		addBlock(node, (BlockStmtAnnotation) ((TryStmtAnnotation) annotation).getBlockAnnotation());
		for (Enumeration e = ((TryStmtAnnotation) annotation).getCatchClauses().elements();
				e.hasMoreElements();) {
			Annotation a = (Annotation) e.nextElement();
			edu.ksu.cis.bandera.jjjc.node.ACatchClause catchClause = (edu.ksu.cis.bandera.jjjc.node.ACatchClause) a.getNode();
	
			DefaultMutableTreeNode catchNode = new DefaultMutableTreeNode(a);
			addBlock(catchNode, (BlockStmtAnnotation) a);
			result.addElement(catchNode);
		}
	} else if (annotation instanceof WhileStmtAnnotation) {
		Annotation ann = ((WhileStmtAnnotation) annotation).getBlockAnnotation();
		if (ann instanceof BlockStmtAnnotation) {
			addBlock(node, (BlockStmtAnnotation) ann);
		} else {
			for (Enumeration e = buildTree(ann).elements(); e.hasMoreElements();) {
				node.add((DefaultMutableTreeNode) e.nextElement());
			}
		}
	} else if (annotation instanceof ConstructorInvocationStmtAnnotation) {
	} else if (annotation instanceof SequentialAnnotation) {
		node.setUserObject("");
	}

	Vector discard = new Vector();

	for (Enumeration e = node.children(); e.hasMoreElements();) {
		DefaultMutableTreeNode child = (DefaultMutableTreeNode) e.nextElement();
		Object o = child.getUserObject();
		if ("".equals(o.toString().trim())) discard.addElement(child);
	}

	for (Enumeration e = discard.elements(); e.hasMoreElements();) {
		node.remove((MutableTreeNode) e.nextElement());
	}
	
	return result;
}
/**
 * connEtoC1:  (LeftTree.treeSelection. --> BUISessionPane.leftTree_TreeSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1() {
	try {
		// user code begin {1}
		// user code end
		this.leftTree_TreeSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (LeftTree.mouse.mouseReleased(java.awt.event.MouseEvent) --> BUISessionPane.leftTree_MouseReleased(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.leftTree_MouseReleased(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Return the BUISplitPane property value.
 * @return javax.swing.JSplitPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JSplitPane getBUISplitPane() {
	if (ivjBUISplitPane == null) {
		try {
			ivjBUISplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
			ivjBUISplitPane.setName("BUISplitPane");
			ivjBUISplitPane.setPreferredSize(new java.awt.Dimension(600, 320));
			getBUISplitPane().add(getLeftScrollPane(), "left");
			getBUISplitPane().add(getRightScrollPane(), "right");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBUISplitPane;
}
/**
 * Return the LeftScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getLeftScrollPane() {
	if (ivjLeftScrollPane == null) {
		try {
			ivjLeftScrollPane = new javax.swing.JScrollPane();
			ivjLeftScrollPane.setName("LeftScrollPane");
			ivjLeftScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			getLeftScrollPane().setViewportView(getLeftTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLeftScrollPane;
}
/**
 * Return the LeftTree property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JTree getLeftTree() {
	if (ivjLeftTree == null) {
		try {
			ivjLeftTree = new javax.swing.JTree();
			ivjLeftTree.setName("LeftTree");
			ivjLeftTree.setBackground(new java.awt.Color(204,204,204));
			ivjLeftTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			ivjLeftTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(Color.black);
					return this;
				}
			}.setAngledColor());
			ivjLeftTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			ivjLeftTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					setIcon(null);
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					if (o instanceof Package) {
						String name = ((Package) o).getName().toString().trim();
						name = "".equals(name) ? "<default package>" : name;
						setText(name);
						setIcon(IconLibrary.packageIcon);
					} else
						if (o instanceof SootClass) {
							SootClass sc = (SootClass) o;
							Name cName = new Name(sc.getName());
							setText(cName.getLastIdentifier());
							setIcon(IconLibrary.classIcon);
							try {
								if (Package.getClassOrInterfaceType(cName).isInterface()) {
									setIcon(IconLibrary.interfaceIcon);
								}
							} catch (Exception e) {
							}
						} else
							if (o instanceof FieldDeclarationAnnotation) {
								FieldDeclarationAnnotation fda = (FieldDeclarationAnnotation) o;
								setText(fda.getField().getType() + " " + fda.getField().getName());
								setIcon(IconLibrary.fieldIcon);
							} else
								if (o instanceof ConstructorDeclarationAnnotation) {
									ConstructorDeclarationAnnotation cda = (ConstructorDeclarationAnnotation) o;
									try {
										SootMethod sm = cda.getSootMethod();
										Method m = cda.getConstructor();
										boolean isAbstractNative = Modifier.isAbstract(sm.getModifiers()) || Modifier.isNative(sm.getModifiers());
										String result = m.getDeclaringClassOrInterface().getName().getLastIdentifier().trim() + "(";
										JimpleBody body = (JimpleBody) sm.getBody(Jimple.v());
										String parm = "";
										for (Enumeration e = m.getParameters().elements(); e.hasMoreElements();) {
											Variable v = (Variable) e.nextElement();
											if (body.declaresLocal(v.getName().toString().trim()) || isAbstractNative) {
												parm += (v.toString() + ", ");
											}
										}
										if (parm.length() > 1)
											parm = parm.substring(0, parm.length() - 2);
										result += (parm + ")");
										setText(result);
									} catch (Exception e) {
									}
									setIcon(IconLibrary.methodIcon);
								} else
									if (o instanceof MethodDeclarationAnnotation) {
										MethodDeclarationAnnotation mda = (MethodDeclarationAnnotation) o;
										SootMethod sm = mda.getSootMethod();
										boolean isAbstractNative = Modifier.isAbstract(sm.getModifiers()) || Modifier.isNative(sm.getModifiers());
										Method m = mda.getMethod();
										String result = sm.getReturnType().toString().trim() + " " + sm.getName().trim() + "(";
										JimpleBody body = (JimpleBody) sm.getBody(Jimple.v());
										String parm = "";
										for (Enumeration e = m.getParameters().elements(); e.hasMoreElements();) {
											Variable v = (Variable) e.nextElement();
											if (body.declaresLocal(v.getName().toString().trim()) || isAbstractNative) {
												parm += (v.toString() + ", ");
											}
										}
										if (parm.length() > 1)
											parm = parm.substring(0, parm.length() - 2);
										result += (parm + ")");
										setText(result);
										setIcon(IconLibrary.methodIcon);
									}
					return this;
				}
			});
			((DefaultTreeCellRenderer) ivjLeftTree.getCellRenderer()).setBackgroundNonSelectionColor(new Color(204, 204, 204));
			((DefaultTreeCellRenderer) ivjLeftTree.getCellRenderer()).setBackgroundSelectionColor(Preferences.getHighlightColor());
			ivjLeftTree.putClientProperty("JTree.lineStyle", "Angled");
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjLeftTree;
}
/**
 * Return the RightScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getRightScrollPane() {
	if (ivjRightScrollPane == null) {
		try {
			ivjRightScrollPane = new javax.swing.JScrollPane();
			ivjRightScrollPane.setName("RightScrollPane");
			ivjRightScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
			getRightScrollPane().setViewportView(getRightTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRightScrollPane;
}
/**
 * Return the RightTree property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JTree getRightTree() {
	if (ivjRightTree == null) {
		try {
			ivjRightTree = new javax.swing.JTree();
			ivjRightTree.setName("RightTree");
			ivjRightTree.setBackground(new java.awt.Color(204,204,204));
			ivjRightTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			ivjRightTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(Color.black);
					return this;
				}
			}
			.setAngledColor());
			ivjRightTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			ivjRightTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					setIcon(null);
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					if (o instanceof Predicate) {
						setForeground(Preferences.getPredicateColor());
						if (!((Predicate) o).isValid())
							setIcon(IconLibrary.errorIcon);
					} else
						if (o instanceof Assertion) {
							setForeground(Preferences.getAssertionColor());
							if (!((Assertion) o).isValid())
								setIcon(IconLibrary.errorIcon);
						} else {
							setForeground(Color.black);
						}
					if (o instanceof Package) {
						String name = ((Package) o).getName().toString().trim();
						name = "".equals(name) ? "<default package>" : name;
						setText(name);
						if (showIconRightTree)
							setIcon(IconLibrary.packageIcon);
					} else
						if (o instanceof SootClass) {
							SootClass sc = (SootClass) o;
							Name cName = new Name(sc.getName());
							String text = null;
							if (Modifier.isInterface(sc.getModifiers())) {
								text = Modifier.toString(sc.getModifiers()) + cName;
							} else {
								text = Modifier.toString(sc.getModifiers()) + " class " + cName;
							}
							if (sc.hasSuperClass()) {
								String superClassName = sc.getSuperClass().getName().trim();
								//if (!"java.lang.Object".equals(superClassName))
								text += " extends " + superClassName;
							}
							if (sc.getInterfaceCount() > 0) {
								text += " implements ";
								for (ca.mcgill.sable.util.Iterator i = sc.getInterfaces().iterator(); i.hasNext();) {
									text += ((SootClass) i.next()).getName().trim() + ", ";
								}
								text.substring(0, text.length() - 2);
							}
							setText(text.trim());
							if (showIconRightTree) {
								setIcon(IconLibrary.classIcon);
								try {
									if (Package.getClassOrInterfaceType(cName).isInterface()) {
										setIcon(IconLibrary.interfaceIcon);
									}
								} catch (Exception e) {
								}
							}
						} else
							if (showIconRightTree && (o instanceof FieldDeclarationAnnotation)) {
								setIcon(IconLibrary.fieldIcon);
							} else
								if (showIconRightTree && (o instanceof ConstructorDeclarationAnnotation)) {
									setIcon(IconLibrary.methodIcon);
								} else
									if (showIconRightTree && (o instanceof MethodDeclarationAnnotation)) {
										setIcon(IconLibrary.methodIcon);
									}
					if (o instanceof Annotation) {
						Annotation a = (Annotation) o;
						if (!(a instanceof MethodDeclarationAnnotation) && !(a instanceof ConstructorDeclarationAnnotation) && !((DefaultMutableTreeNode) value).isRoot() && !(a instanceof LocalDeclarationStmtAnnotation) && (a.getStatements().length == 0)) {
							setForeground(Preferences.getSlicedColor());
						} else
							if ((a instanceof LocalDeclarationStmtAnnotation) && (((LocalDeclarationStmtAnnotation) a).getDeclaredLocals().size() == 0)) {
								setForeground(Preferences.getSlicedColor());
							}
					}
					return this;
				}
			});
			((DefaultTreeCellRenderer) ivjRightTree.getCellRenderer()).setBackgroundNonSelectionColor(new Color(204, 204, 204));
			((DefaultTreeCellRenderer) ivjRightTree.getCellRenderer()).setBackgroundSelectionColor(Preferences.getHighlightColor());
			ivjRightTree.putClientProperty("JTree.lineStyle", "Angled");
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjRightTree;
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getLeftTree().addTreeSelectionListener(ivjEventHandler);
	getLeftTree().addMouseListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("BUISessionPane");
		setPreferredSize(new java.awt.Dimension(600, 320));
		setBorder(new javax.swing.border.EtchedBorder());
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(522, 244);

		java.awt.GridBagConstraints constraintsBUISplitPane = new java.awt.GridBagConstraints();
		constraintsBUISplitPane.gridx = 1; constraintsBUISplitPane.gridy = 1;
		constraintsBUISplitPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsBUISplitPane.weightx = 1.0;
		constraintsBUISplitPane.weighty = 1.0;
		constraintsBUISplitPane.ipadx = 470;
		constraintsBUISplitPane.ipady = 216;
		add(getBUISplitPane(), constraintsBUISplitPane);
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	updateLeftTree();
	// user code end
}

/**
 * Comment
 */
public void leftTree_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
}
/**
 * Comment
 */
public void leftTree_TreeSelectionEvents() {
	updateRightTree(((DefaultMutableTreeNode) getLeftTree().getLastSelectedPathComponent()).getUserObject());
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		BUISessionPane aBUISessionPane;
		aBUISessionPane = new BUISessionPane();
		frame.setContentPane(aBUISessionPane);
		frame.setSize(aBUISessionPane.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JPanel");
		exception.printStackTrace(System.out);
	}
}
/**
 * 
 * @param leftObject java.lang.Object
 * @param rightObject java.lang.Object
 */
public void select(Object leftObject, Object rightObject) {
	select(getLeftTree(), leftObject, true);
	updateRightTree(leftObject);
	select(getRightTree(), rightObject, false);
}
/**
 * 
 * @param tree javax.swing.JTree
 * @param object java.lang.Object
 */
public static void select(JTree tree, Object object, boolean collapseAbove) {
	for (int i = 0; i < tree.getRowCount(); i++) {
		tree.setSelectionRow(i);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		Object o = node.getUserObject();
		if (o == object) {
			TreePath path = tree.getSelectionPath();
			if (collapseAbove) {
				Object[] objects = path.getPath();
				for (int j = 0, k = 0; j < tree.getRowCount(); j++) {
						tree.setSelectionRow(j);
						if (objects[k] == tree.getLastSelectedPathComponent()) {
							k++;
							if (k == objects.length) break;
						} else {
							tree.collapseRow(j);
						}
					}
			}
			tree.scrollPathToVisible(path);
			return;
		} else {
			tree.expandRow(i);
		}
	}
}
/**
 * 
 */
public void updateLeftTree() {
    String rootName = "root";

    if (CompilationManager.getExceptions().size() > 0) {
	SessionManager sm = SessionManager.getInstance();
	Session s = sm.getActiveSession();
	if(s != null) {
	    //DefaultMutableTreeNode root = new DefaultMutableTreeNode(BUI.sessions.getActiveSession().getName() + " -- Exception");
	    DefaultMutableTreeNode root = new DefaultMutableTreeNode(s.getName() + " -- Exception");
	    Hashtable table = CompilationManager.getExceptions();
	    for (Enumeration e = table.keys(); e.hasMoreElements();) {
		Object key = e.nextElement();
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(key);
		root.add(node);
		Vector v = (Vector) table.get(key);
		for (Enumeration e2 = v.elements(); e2.hasMoreElements();) {
		    Exception ex = (Exception) e2.nextElement();
		    node.add(new DefaultMutableTreeNode(ex.getMessage()));
		}
	    }
	    getLeftTree().setRootVisible(true);
	    getLeftTree().setModel(new DefaultTreeModel(root));
	    getLeftScrollPane().validate();
	    getLeftScrollPane().repaint();
	    return;
	}
	rootName = s.getName();
	getLeftTree().setRootVisible(true);
    }
    else {
	getLeftTree().setRootVisible(false);
    }
    
    DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootName);
    
    Hashtable compiledClasses = CompilationManager.getCompiledClasses();
    
    TreeSet pNames = new TreeSet();
    for (Enumeration e = compiledClasses.elements(); e.hasMoreElements();) {
	pNames.add(new Name(((SootClass) e.nextElement()).getName()).getSuperName());
    }
    
    for (Iterator i = pNames.iterator(); i.hasNext();) {
	try {
	    Package p = Package.getPackage((Name) i.next());
	    DefaultMutableTreeNode pNode = buildPackageNode(p);
	    root.add(pNode);
	} catch (Exception e) {
	}
    }
    getLeftTree().setModel(new DefaultTreeModel(root));
    getLeftScrollPane().validate();
    getLeftScrollPane().repaint();
    updateRightTree((Object) null);
    getBUISplitPane().resetToPreferredSizes();
}

/**
 * 
 * @param sc ca.mcgill.sable.soot.SootClass
 */
private void updateRightTree(SootClass sc) {
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

	for (Iterator i = PredicateSet.getDefinedPredicates(sc).iterator(); i.hasNext();) {
		root.add(new DefaultMutableTreeNode(i.next()));
	}
	
	root.add(buildClassNode(sc));
	showIconRightTree = true;
	getRightTree().setRootVisible(false);
	getRightTree().setModel(new DefaultTreeModel(root));
	getRightScrollPane().validate();
	getRightScrollPane().repaint();
}
/**
 * 
 * @param cda edu.ksu.cis.bandera.annotation.ConstructorDeclarationAnnotation
 */
private void updateRightTree(ConstructorDeclarationAnnotation cda) {
	if (cda.getNode() == null) {
		updateRightTree("nocode");
		return;
	}
	
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

	for (Iterator i = PredicateSet.getDefinedPredicates(cda).iterator(); i.hasNext();) {
		root.add(new DefaultMutableTreeNode(i.next()));
	}
	for (Iterator i = AssertionSet.getDefinedAssertions(cda).iterator(); i.hasNext();) {
		root.add(new DefaultMutableTreeNode(i.next()));
	}
	
	root.add((DefaultMutableTreeNode) buildTree(cda).elementAt(0));
	
	JTree rightTree = getRightTree();
	rightTree.setRootVisible(false);
	rightTree.setModel(new DefaultTreeModel(root));
	for (int i = 0; i < rightTree.getRowCount(); i++) {
		rightTree.expandPath(rightTree.getPathForRow(i));
	}
	getRightScrollPane().validate();
	getRightScrollPane().repaint();
}
/**
 * 
 * @param fda edu.ksu.cis.bandera.annotation.FieldDeclarationAnnotation
 */
private void updateRightTree(FieldDeclarationAnnotation fda) {
	showIconRightTree = false;
	getRightTree().setRootVisible(true);
	getRightTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode(fda)));
	getRightScrollPane().validate();
	getRightScrollPane().repaint();
}
/**
 * 
 * @param mda edu.ksu.cis.bandera.annotation.MethodDeclarationAnnotation
 */
private void updateRightTree(MethodDeclarationAnnotation mda) {
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

	for (Iterator i = PredicateSet.getDefinedPredicates(mda).iterator(); i.hasNext();) {
		root.add(new DefaultMutableTreeNode(i.next()));
	}
	for (Iterator i = AssertionSet.getDefinedAssertions(mda).iterator(); i.hasNext();) {
		root.add(new DefaultMutableTreeNode(i.next()));
	}
	
	root.add((DefaultMutableTreeNode) buildTree(mda).elementAt(0));
	
	JTree rightTree = getRightTree();
	rightTree.setRootVisible(false);
	rightTree.setModel(new DefaultTreeModel(root));
	for (int i = 0; i < rightTree.getRowCount(); i++) {
		rightTree.expandPath(rightTree.getPathForRow(i));
	}
	getRightScrollPane().validate();
	getRightScrollPane().repaint();
}
/**
 * 
 * @param p edu.ksu.cis.bandera.jjjc.symboltable.Package
 */
private void updateRightTree(Package p) {
	showIconRightTree = true;
	getRightTree().setRootVisible(true);
	getRightTree().setModel(new DefaultTreeModel(buildPackageNode(p)));
	getRightScrollPane().validate();
	getRightScrollPane().repaint();
}
/**
 * 
 * @param object java.lang.Object
 */
public void updateRightTree(Object object) {
	showIconRightTree = false;
	if (object instanceof Package) {
		updateRightTree((Package) object);
	} else if (object instanceof SootClass) {
		updateRightTree((SootClass) object);
	} else if (object instanceof FieldDeclarationAnnotation) {
		updateRightTree((FieldDeclarationAnnotation) object);
	} else if (object instanceof ConstructorDeclarationAnnotation) {
		updateRightTree((ConstructorDeclarationAnnotation) object);
	} else if (object instanceof MethodDeclarationAnnotation) {
		updateRightTree((MethodDeclarationAnnotation) object);
	} else if ("nocode".equals(object)) {
		getRightTree().setRootVisible(true);
		getRightTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Source code unavailable")));
		getRightScrollPane().validate();
		getRightScrollPane().repaint();
	} else {
		getRightTree().setRootVisible(false);
		getRightTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
		getRightScrollPane().validate();
		getRightScrollPane().repaint();
	}
}
}
