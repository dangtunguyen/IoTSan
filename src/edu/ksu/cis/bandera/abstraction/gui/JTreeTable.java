package edu.ksu.cis.bandera.abstraction.gui;

/*
 * @(#)JTreeTable.java	1.2 98/10/27
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.util.*;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.util.*;

/**
 * This example shows how to create a simple JTreeTable component, 
 * by using a JTree as a renderer (and editor) for the cells in a 
 * particular column in the JTable.  
 *
 * @version 1.2 10/27/98
 *
 * @author Philip Milne
 * @author Scott Violet
 */
public class JTreeTable extends JTable {
	/** A subclass of JTree. */
	protected TreeTableCellRenderer tree;
	public static java.util.Hashtable absTable;

	/**
	 * A TreeCellRenderer that displays a JTree.
	 */
	public class TreeTableCellRenderer extends JTree implements TableCellRenderer {
		/** Last table/tree row asked to renderer. */
		protected int visibleRow;
		public TreeTableCellRenderer(TreeModel model) {
			super(model);
			putClientProperty("JTree.lineStyle", "Angled");
		}

		/**
		 * updateUI is overridden to set the colors of the Tree's renderer
		 * to match that of the table.
		 */
		public void updateUI() {
			super.updateUI();
			// Make the tree's cell renderer use the table's cell selection
			// colors. 
			TreeCellRenderer tcr = this.getCellRenderer();
			if (tcr instanceof DefaultTreeCellRenderer) {
				DefaultTreeCellRenderer dtcr = ((DefaultTreeCellRenderer) tcr);
				// For 1.1 uncomment this, 1.2 has a bug that will cause an
				// exception to be thrown if the border selection color is
				// null.
				// dtcr.setBorderSelectionColor(null);
				dtcr.setTextSelectionColor(UIManager.getColor("Table.selectionForeground"));
				dtcr.setBackgroundSelectionColor(UIManager.getColor("Table.selectionBackground"));
			}
		}

		/**
		 * Sets the row height of the tree, and forwards the row height to
		 * the table.
		 */
		public void setRowHeight(int rowHeight) {
			if (rowHeight > 0) {
				super.setRowHeight(rowHeight);
				if (JTreeTable.this != null && JTreeTable.this.getRowHeight() != rowHeight) {
					JTreeTable.this.setRowHeight(getRowHeight());
				}
			}
		}

		/**
		 * This is overridden to set the height to match that of the JTable.
		 */
		public void setBounds(int x, int y, int w, int h) {
			super.setBounds(x, 0, w, JTreeTable.this.getHeight());
		}

		/**
		 * Sublcassed to translate the graphics such that the last visible
		 * row will be drawn at 0,0.
		 */
		public void paint(Graphics g) {
			g.translate(0, -visibleRow * getRowHeight());
			super.paint(g);
		}

		/**
		 * TreeCellRenderer method. Overridden to update the visible row.
		 */
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			if (isSelected)
				setBackground(table.getSelectionBackground());
			else
				setBackground(table.getBackground());
			visibleRow = row;
			return this;
		}
	}


	/**
	 * TreeTableCellEditor implementation. Component returned is the
	 * JTree.
	 */
	public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
			return tree;
		}

		/**
		 * Overridden to return false, and if the event is a mouse event
		 * it is forwarded to the tree.<p>
		 * The behavior for this is debatable, and should really be offered
		 * as a property. By returning false, all keyboard actions are
		 * implemented in terms of the table. By returning true, the
		 * tree would get a chance to do something with the keyboard
		 * events. For the most part this is ok. But for certain keys,
		 * such as left/right, the tree will expand/collapse where as
		 * the table focus should really move to a different column. Page
		 * up/down should also be implemented in terms of the table.
		 * By returning false this also has the added benefit that clicking
		 * outside of the bounds of the tree node, but still in the tree
		 * column will select the row, whereas if this returned true
		 * that wouldn't be the case.
		 * <p>By returning false we are also enforcing the policy that
		 * the tree will never be editable (at least by a key sequence).
		 */
		public boolean isCellEditable(EventObject e) {
			if (e instanceof MouseEvent) {
				for (int counter = getColumnCount() - 1; counter >= 0; counter--) {
					if (getColumnClass(counter) == TreeTableModel.class) {
						MouseEvent me = (MouseEvent) e;
						MouseEvent newME = new MouseEvent(tree, me.getID(), me.getWhen(), me.getModifiers(), me.getX() - getCellRect(0, counter, true).x, me.getY(), me.getClickCount(), me.isPopupTrigger());
						tree.dispatchEvent(newME);
						break;
					}
				}
			}
			return false;
		}
	}


	/**
	 * ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel
	 * to listen for changes in the ListSelectionModel it maintains. Once
	 * a change in the ListSelectionModel happens, the paths are updated
	 * in the DefaultTreeSelectionModel.
	 */
	class ListToTreeSelectionModelWrapper extends DefaultTreeSelectionModel {
		/** Set to true when we are updating the ListSelectionModel. */
		protected boolean updatingListSelectionModel;
		public ListToTreeSelectionModelWrapper() {
			super();
			getListSelectionModel().addListSelectionListener(createListSelectionListener());
		}

		/**
		 * Returns the list selection model. ListToTreeSelectionModelWrapper
		 * listens for changes to this model and updates the selected paths
		 * accordingly.
		 */
		ListSelectionModel getListSelectionModel() {
			return listSelectionModel;
		}

		/**
		 * This is overridden to set <code>updatingListSelectionModel</code>
		 * and message super. This is the only place DefaultTreeSelectionModel
		 * alters the ListSelectionModel.
		 */
		public void resetRowSelection() {
			if (!updatingListSelectionModel) {
				updatingListSelectionModel = true;
				try {
					super.resetRowSelection();
				} finally {
					updatingListSelectionModel = false;
				}
			}
			// Notice how we don't message super if
			// updatingListSelectionModel is true. If
			// updatingListSelectionModel is true, it implies the
			// ListSelectionModel has already been updated and the
			// paths are the only thing that needs to be updated.
		}

		/**
		 * Creates and returns an instance of ListSelectionHandler.
		 */
		protected ListSelectionListener createListSelectionListener() {
			return new ListSelectionHandler();
		}

		/**
		 * If <code>updatingListSelectionModel</code> is false, this will
		 * reset the selected paths from the selected rows in the list
		 * selection model.
		 */
		protected void updateSelectedPathsFromSelectedRows() {
			if (!updatingListSelectionModel) {
				updatingListSelectionModel = true;
				try {
					// This is way expensive, ListSelectionModel needs an
					// enumerator for iterating.
					int min = listSelectionModel.getMinSelectionIndex();
					int max = listSelectionModel.getMaxSelectionIndex();
					clearSelection();
					if (min != -1 && max != -1) {
						for (int counter = min; counter <= max; counter++) {
							if (listSelectionModel.isSelectedIndex(counter)) {
								TreePath selPath = tree.getPathForRow(counter);
								if (selPath != null) {
									addSelectionPath(selPath);
								}
							}
						}
					}
				} finally {
					updatingListSelectionModel = false;
				}
			}
		}

		/**
		 * Class responsible for calling updateSelectedPathsFromSelectedRows
		 * when the selection of the list changse.
		 */
		class ListSelectionHandler implements ListSelectionListener {
			public void valueChanged(ListSelectionEvent e) {
				updateSelectedPathsFromSelectedRows();
			}
		}
	}
public JTreeTable(TreeTableModel treeTableModel) {
	super();

	// Create the tree. It will be used as a renderer and editor. 
	tree = new TreeTableCellRenderer(treeTableModel);

	// Install a tableModel representing the visible rows in the tree. 
	super.setModel(new TreeTableModelAdapter(treeTableModel, tree));

	// Force the JTable and JTree to share their row selection models. 
	ListToTreeSelectionModelWrapper selectionWrapper = new ListToTreeSelectionModelWrapper();
	tree.setSelectionModel(selectionWrapper);
	setSelectionModel(selectionWrapper.getListSelectionModel());

	// Install the tree editor renderer and editor. 
	setDefaultRenderer(TreeTableModel.class, tree);
	setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

	// No grid.
	setShowGrid(true);

	// No intercell spacing
	setIntercellSpacing(new Dimension(1, 0));

	// And update the height of the trees row to match that of
	// the table.
	if (tree.getRowHeight() < 1) {
		// Metal looks better like this.
		setRowHeight(18);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (3/24/00 12:20:36 AM)
 * @return javax.swing.table.TableCellRenderer
 * @param row int
 * @param column int
 */
public TableCellEditor getCellEditor(int row, int column) {
	TableCellEditor editor;
	if (((TreeTableModelAdapter) getModel()).nodeForRow(row) instanceof TypeTreeNode)
		if ((editor = ((AbstractTreeNode) ((TreeTableModelAdapter) getModel()).nodeForRow(row)).getCellEditor(column)) == null)
			return super.getCellEditor(row, column);
		else
			return editor;
	else
		return super.getCellEditor(row, column);
	/*
	if (column == 2) {
	if (getValueAt(row, 0) != null)
	System.out.println(getValueAt(row, 0).getClass());
	System.out.print(((TreeTableModelAdapter) getModel()).nodeForRow(row).getClass() + "\t");
	System.out.print(absTable.get(getValueAt(row, column - 1)) + "\t");
	System.out.println(getValueAt(row, column - 1).getClass());
	TableCellEditor editor;
	if (((TreeTableModelAdapter) getModel()).nodeForRow(row) instanceof TypeTreeNode)
	if ((editor = ((TypeTreeNode) ((TreeTableModelAdapter) getModel()).nodeForRow(row)).getCellEditor(column)) == null)
	return super.getCellEditor(row, column);
	else
	return editor;
	else
	if (getValueAt(row, column) instanceof Boolean) {
	JCheckBox checkBox = new JCheckBox("Include", !((Boolean) getValueAt(row, column)).booleanValue());
	return new DefaultCellEditor(checkBox);
	}
	if (absTable.get(getValueAt(row, column - 1)) != null) {
	JComboBox comboBox = new JComboBox((Vector) absTable.get(getValueAt(row, column - 1)));
	comboBox.setSelectedItem(edu.ksu.cis.bandera.abps.lib.ConcreteIntAbstraction.v().getType());
	System.out.println("1");
	////comboBox.setPopupVisible(true);
	return new DefaultCellEditor(comboBox);
	} else
	if (getValueAt(row, column - 1).getClass() == RefType.class) {
	JComboBox comboBox = new JComboBox();
	comboBox.addItem(ObjectAbstraction.v(AbstractRefType.v((RefType) getValueAt(row, column - 1))).getType());
	System.out.println("2");
	//comboBox.setPopupVisible(true);
	return new DefaultCellEditor(comboBox);
	} else
	if (getValueAt(row, column - 1).getClass() == AbstractRefType.class) {
	JComboBox comboBox = new JComboBox();
	comboBox.addItem(ObjectAbstraction.v((AbstractRefType) getValueAt(row, column - 1)).getType());
	System.out.println("3");
	//comboBox.setPopupVisible(true);
	return new DefaultCellEditor(comboBox);
	} else
	if (getValueAt(row, column - 1).getClass() == IntType.class) {
	JComboBox comboBox = new JComboBox();
	comboBox.addItem("4");
	comboBox.addItem(new Integer(3));
	comboBox.addItem("");
	comboBox.addItem("None");
	System.out.println("4");
	//comboBox.setPopupVisible(true);
	return new DefaultCellEditor(comboBox);
	}
	}
	System.out.println("Going to super editor:  " + getValueAt(row, column - 1).getClass());
	return super.getCellEditor(row, column);
	*/
}
/**
 * Insert the method's description here.
 * Creation date: (3/24/00 12:20:36 AM)
 * @return javax.swing.table.TableCellRenderer
 * @param row int
 * @param column int
 */
public TableCellRenderer getCellRenderer(int row, int column) {
	//	System.out.println("Renderer:  " + ((TreeTableModelAdapter) getModel()).nodeForRow(row).getClass() + "\t");
	//	System.out.println(getValueAt(row, column - 1).getClass());
	TableCellRenderer renderer;
	if (((TreeTableModelAdapter) getModel()).nodeForRow(row) instanceof TypeTreeNode) {
		renderer = ((AbstractTreeNode) ((TreeTableModelAdapter) getModel()).nodeForRow(row)).getCellRenderer(column);
		//		System.out.println("\t" + renderer);
		if (renderer == null)
			return super.getCellRenderer(row, column);
		else
			return renderer;
	} else
		return super.getCellRenderer(row, column);
	/*
	if (column == 2) {
	if (getValueAt(row, 0) != null)
	System.out.println(getValueAt(row, 0).getClass());
	System.out.print(((TreeTableModelAdapter) getModel()).nodeForRow(row).getClass() + "\t");
	System.out.print(absTable.get(getValueAt(row, column - 1)) + "\t");
	System.out.println(getValueAt(row, column - 1).getClass());
	TableCellEditor editor;
	if (((TreeTableModelAdapter) getModel()).nodeForRow(row) instanceof TypeTreeNode)
	if ((editor = ((TypeTreeNode) ((TreeTableModelAdapter) getModel()).nodeForRow(row)).getCellEditor(column)) == null)
	return super.getCellEditor(row, column);
	else
	return editor;
	else
	if (getValueAt(row, column) instanceof Boolean) {
	JCheckBox checkBox = new JCheckBox("Include", !((Boolean) getValueAt(row, column)).booleanValue());
	return new DefaultCellEditor(checkBox);
	}
	if (absTable.get(getValueAt(row, column - 1)) != null) {
	JComboBox comboBox = new JComboBox((Vector) absTable.get(getValueAt(row, column - 1)));
	comboBox.setSelectedItem(edu.ksu.cis.bandera.abps.lib.ConcreteIntAbstraction.v().getType());
	System.out.println("1");
	////comboBox.setPopupVisible(true);
	return new DefaultCellEditor(comboBox);
	} else
	if (getValueAt(row, column - 1).getClass() == RefType.class) {
	JComboBox comboBox = new JComboBox();
	comboBox.addItem(ObjectAbstraction.v(AbstractRefType.v((RefType) getValueAt(row, column - 1))).getType());
	System.out.println("2");
	//comboBox.setPopupVisible(true);
	return new DefaultCellEditor(comboBox);
	} else
	if (getValueAt(row, column - 1).getClass() == AbstractRefType.class) {
	JComboBox comboBox = new JComboBox();
	comboBox.addItem(ObjectAbstraction.v((AbstractRefType) getValueAt(row, column - 1)).getType());
	System.out.println("3");
	//comboBox.setPopupVisible(true);
	return new DefaultCellEditor(comboBox);
	} else
	if (getValueAt(row, column - 1).getClass() == IntType.class) {
	JComboBox comboBox = new JComboBox();
	comboBox.addItem("4");
	comboBox.addItem(new Integer(3));
	comboBox.addItem("");
	comboBox.addItem("None");
	System.out.println("4");
	//comboBox.setPopupVisible(true);
	return new DefaultCellEditor(comboBox);
	}
	}
	System.out.println("Going to super editor:  " + getValueAt(row, column - 1).getClass());
	return super.getCellEditor(row, column);
	*/
}
/* Workaround for BasicTableUI anomaly. Make sure the UI never tries to 
 * paint the editor. The UI currently uses different techniques to 
 * paint the renderers and editors and overriding setBounds() below 
 * is not the right thing to do for an editor. Returning -1 for the 
 * editing row in this case, ensures the editor is never painted. 
 */
public int getEditingRow() {
	return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;
}
/**
 * Returns the tree that is being shared between the model.
 */
public JTree getTree() {
	return tree;
}
/**
 * This method was created in VisualAge.
 * @param abs java.util.Collection
 */
public static void processAbstractions(Collection abstractions) {
	absTable = new java.util.Hashtable();
	Type[] integralTypes = new Type[] {ByteType.v(), CharType.v(), ShortType.v(), IntType.v()};
	Type[] realTypes = new Type[] {FloatType.v(), DoubleType.v()};
	for (Iterator i = abstractions.iterator(); i.hasNext();) {
		try {
			String s = (String) i.next();
			Abstraction a = (Abstraction) AbstractionClassLoader.invokeMethod(s, "v", new Class[0], null, new Object[0]);
			for (int j = 0; (j < integralTypes.length) && (a instanceof IntegralAbstraction); j++) {
				java.util.Vector abstractions2 = (java.util.Vector) absTable.get(integralTypes[j]);
				if (abstractions2 == null) {
					abstractions2 = new java.util.Vector();
					abstractions2.addElement("No Selection");
					absTable.put(integralTypes[j], abstractions2);
				}
				abstractions2.addElement(a);
			}
			for (int j = 0; (j < realTypes.length) && (a instanceof RealAbstraction); j++) {
				java.util.Vector abstractions2 = (java.util.Vector) absTable.get(realTypes[j]);
				if (abstractions2 == null) {
					abstractions2 = new java.util.Vector();
					abstractions2.addElement("No Selection");
					absTable.put(realTypes[j], abstractions2);
				}
				abstractions2.addElement(a);
			}
		} catch (Exception e) {
		}
	}
}
/**
 * Overridden to pass the new rowHeight to the tree.
 */
public void setRowHeight(int rowHeight) {
	super.setRowHeight(rowHeight);
	if (tree != null && tree.getRowHeight() != rowHeight) {
		tree.setRowHeight(getRowHeight());
	}
}
/**
 * Overridden to message super and forward the method to the tree.
 * Since the tree is not actually in the component hieachy it will
 * never receive this unless we forward it in this manner.
 */
public void updateUI() {
	super.updateUI();
	if (tree != null) {
		tree.updateUI();
	}
	// Use the tree's default foreground and background colors in the
	// table. 
	LookAndFeel.installColorsAndFont(this, "Tree.background", "Tree.foreground", "Tree.font");
}
}
