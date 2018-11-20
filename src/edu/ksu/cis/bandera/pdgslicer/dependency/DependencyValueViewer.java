package edu.ksu.cis.bandera.pdgslicer.dependency;

import javax.swing.*;
import javax.swing.tree.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.util.Preferences;
import edu.ksu.cis.bandera.pdgslicer.SliceTraceNode;
/**
 * Insert the type's description here.
 * Creation date: (00-10-27 18:52:54)
 * @author: 
 */
public class DependencyValueViewer extends JFrame implements java.awt.event.MouseListener, java.awt.event.WindowListener, javax.swing.event.TreeSelectionListener {
	private boolean isSetSelectedByProgram = false;
	DefaultMutableTreeNode currentValueTreeRoot = null;
	DefaultMutableTreeNode currentDependTreeRoot = null;
	private Dependencies dependFrame;
	private final int EMPTY = 0;
	private final int DEPEND = 1;
	private final int VALUE = 2;
	private final int DEPENDVALUE = 3;
	private final int VALUEDEPEND = 4;
	private boolean doDepend = false;
	private boolean doValue = false;
	private boolean doDependValue = false;
	private boolean doValueDepend = false;
	private JPanel ivjJFrameContentPane = null;
	private JPanel ivjDependencyPanel = null;
	private JSplitPane ivjDependValueSplitPane = null;
	private JPanel ivjValuesPanel = null;
	private JLabel ivjDependencyLabel = null;
	private JTree ivjDependencyTree = null;
	private JScrollPane ivjDependScrollPane = null;
	private JLabel ivjValueLabel = null;
	private JScrollPane ivjValueScrollPane = null;
	private JTree ivjValueTree = null;

class IvjEventHandler implements java.awt.event.MouseListener, java.awt.event.WindowListener, javax.swing.event.TreeSelectionListener {
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == DependencyValueViewer.this.getDependencyTree()) 
				connEtoC2(e);
			if (e.getSource() == DependencyValueViewer.this.getValueTree()) 
				connEtoC3(e);
		};
		public void mouseEntered(java.awt.event.MouseEvent e) {};
		public void mouseExited(java.awt.event.MouseEvent e) {};
		public void mousePressed(java.awt.event.MouseEvent e) {};
		public void mouseReleased(java.awt.event.MouseEvent e) {};
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			if (e.getSource() == DependencyValueViewer.this.getDependencyTree()) 
				connEtoC1();
			if (e.getSource() == DependencyValueViewer.this.getValueTree()) 
				connEtoC4();
		};
		public void windowActivated(java.awt.event.WindowEvent e) {};
		public void windowClosed(java.awt.event.WindowEvent e) {
			if (e.getSource() == DependencyValueViewer.this) 
				connEtoM1(e);
		};
		public void windowClosing(java.awt.event.WindowEvent e) {};
		public void windowDeactivated(java.awt.event.WindowEvent e) {};
		public void windowDeiconified(java.awt.event.WindowEvent e) {};
		public void windowIconified(java.awt.event.WindowEvent e) {};
		public void windowOpened(java.awt.event.WindowEvent e) {};
	};
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
/**
 * DependencyValueViewer constructor comment.
 */
public DependencyValueViewer() {
	super();
	initialize();
}
/**
 * DependencyValueViewer constructor comment.
 * @param title java.lang.String
 */
public DependencyValueViewer(String title) {
	super(title);
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:35:22)
 */
private void changeStateFromDepend() {
	if (!dependFrame.doDepends() && !dependFrame.doValues())
		this.setToEmptyState();
	if (dependFrame.doDepends() && dependFrame.doValues()) {
		//change state to depend/value
		doDependValue = true;
		doDepend = true;
		doValue = true;
		doValueDepend = false;
		//change viewer
		resizeViewer(true);
		getJFrameContentPane().removeAll();
		getDependValueSplitPane().add(getDependencyPanel(), "top");
		getDependValueSplitPane().add(getValuesPanel(), "bottom");
		getJFrameContentPane().add(getDependValueSplitPane(), "Center");
		validate();
		repaint();
		getDependValueSplitPane().setDividerLocation((double) 0.5);
	}
	else if (dependFrame.doDepends() && !dependFrame.doValues()){
		//keep depend state
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:35:22)
 */
private void changeStateFromDependValue() {
	if (dependFrame.doDepends() && dependFrame.doValues()) {
		//keep depend/value state
	} else
		if (dependFrame.doDepends() && !dependFrame.doValues()) {
			//change to state Depend
			doDepend = true;
			doValue = false;
			doDependValue = false;
			doValueDepend = false;
			// change viewer
			int componentCount = getJFrameContentPane().getComponentCount();
			if (componentCount == 1) {
				java.awt.Component component = getJFrameContentPane().getComponent(0);
				if (component.getName().equals("DependValueSplitPane"))
					resizeViewer(false);
			}
			getJFrameContentPane().removeAll();
			getJFrameContentPane().add(getDependencyPanel(), "Center");
		} else
			if (!dependFrame.doDepends() && dependFrame.doValues()) {
				//change to state Value
				doValue = true;
				doDepend = false;
				doDependValue = false;
				doValueDepend = false;
				int componentCount = getJFrameContentPane().getComponentCount();
				if (componentCount == 1) {
					java.awt.Component component = getJFrameContentPane().getComponent(0);
					if (component.getName().equals("DependValueSplitPane"))
						resizeViewer(false);
				}
				getJFrameContentPane().removeAll();
				getJFrameContentPane().add(getValuesPanel(), "Center");
			}
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:35:22)
 */
private void changeStateFromEmpty() {
	getJFrameContentPane().removeAll();
	if (dependFrame.doDepends()) {
		//change state from Empty to Depend
		doDepend = true;
		doValue = false;
		doDependValue = false;
		doValueDepend = false;
		//change the viewer

		getJFrameContentPane().add(getDependencyPanel(), "Center");
	} else
		if (dependFrame.doValues()) {
			//change state from Empty to Value
			doValue = true;
			doDepend = false;
			doDependValue = false;
			doValueDepend = false;
			//change the viewer

			getJFrameContentPane().add(getValuesPanel(), "Center");
		}
		else {
			// !doValues() && !doDepends()
			// do nothing --- keep empty state
		}
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:35:22)
 */
void changeStateFromEmptyTo(int newState) {
	int currentState = getViewersCurrentState();
	setToEmptyState();
	getJFrameContentPane().removeAll();
	switch (newState) {
		case EMPTY :
			//do nothing
			break;
		case DEPEND :
			doDepend = true;
			doValue = false;
			doDependValue = false;
			doValueDepend = false;
			//change the viewer
			switch (currentState) {
				case EMPTY :
				case DEPEND :
				case VALUE :
					break;
				case DEPENDVALUE :
				case VALUEDEPEND :
					resizeViewer(false);
					break;
			}
			getJFrameContentPane().add(getDependencyPanel(), "Center");
			break;
		case VALUE :
			//change state from Empty to Value
			doValue = true;
			doDepend = false;
			doDependValue = false;
			doValueDepend = false;
			//change the viewer
			switch (currentState) {
				case EMPTY :
				case DEPEND :
				case VALUE :
					break;
				case DEPENDVALUE :
				case VALUEDEPEND :
					resizeViewer(false);
					break;
			}
			getJFrameContentPane().add(getValuesPanel(), "Center");
			break;
		case DEPENDVALUE :
			{
				//change state to depend/value
				doDependValue = true;
				doDepend = true;
				doValue = true;
				doValueDepend = false;
				//change viewer
				switch (currentState) {
					case EMPTY :
					case DEPEND :
					case VALUE :
						resizeViewer(true);
						break;
					case DEPENDVALUE :
					case VALUEDEPEND :
						break;
				}
				getDependValueSplitPane().add(getDependencyPanel(), "top");
				getDependValueSplitPane().add(getValuesPanel(), "bottom");
				getJFrameContentPane().add(getDependValueSplitPane(), "Center");
				validate();
				repaint();
				getDependValueSplitPane().setDividerLocation((double) 0.5);
			}
			break;
		case VALUEDEPEND :
			//change state to value/depend
			doDependValue = false;
			doDepend = true;
			doValue = true;
			doValueDepend = true;
			//change viewer
			switch (currentState) {
				case EMPTY :
				case DEPEND :
				case VALUE :
					resizeViewer(true);
					break;
				case DEPENDVALUE :
				case VALUEDEPEND :
					break;
			}
			getDependValueSplitPane().add(getValuesPanel(), "top");
			getDependValueSplitPane().add(getDependencyPanel(), "bottom");
			getJFrameContentPane().add(getDependValueSplitPane(), "Center");
			validate();
			repaint();
			getDependValueSplitPane().setDividerLocation((double) 0.5);
			break;
	}
	currentState = getViewersCurrentState();
	if (currentState != EMPTY) {
		this.validate();
		this.repaint();
		if (!this.isVisible())
			this.setVisible(true);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:35:22)
 */
private void changeStateFromValue() {
	if (!dependFrame.doDepends() && !dependFrame.doValues())
		this.setToEmptyState();
	if (dependFrame.doDepends() && dependFrame.doValues()) {
		//change state to value/depend
		doDependValue = false;
		doDepend = true;
		doValue = true;
		doValueDepend = true;
		//change viewer

		resizeViewer(true);
		getJFrameContentPane().removeAll();
		getDependValueSplitPane().add(getValuesPanel(), "top");
		getDependValueSplitPane().add(getDependencyPanel(), "bottom");
		getJFrameContentPane().add(getDependValueSplitPane(), "Center");
		validate();
		repaint();
		getDependValueSplitPane().setDividerLocation((double) 0.5);
	} else
		if (dependFrame.doValues() && !dependFrame.doDepends()) {
			//keep value state
		}
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:35:22)
 */
private void changeStateFromValueDepend() {
	changeStateFromDependValue();
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:30:19)
 */
void changeViewerState() {
	int currentState = getViewersCurrentState();
	switch (currentState) {
		case EMPTY :
			changeStateFromEmpty();
			break;
		case DEPEND :
			changeStateFromDepend();
			break;
		case VALUE :
			changeStateFromValue();
			break;
		case DEPENDVALUE :
			changeStateFromDependValue();
			break;
		case VALUEDEPEND :
			changeStateFromValueDepend();
			break;
	}
	currentState = getViewersCurrentState();
	if (currentState != EMPTY) {
		this.validate();
		this.repaint();
		if (!this.isVisible())
			this.setVisible(true);
	}
}
/**
 * connEtoC1:  (DependencyTree.treeSelection. --> DependencyValueViewer.dependencyTree_TreeSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1() {
	try {
		// user code begin {1}
		if (!isSetSelectedByProgram)
		// user code end
		this.dependencyTree_TreeSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (DependencyTree.mouse.mouseClicked(java.awt.event.MouseEvent) --> DependencyValueViewer.dependencyTree_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dependencyTree_MouseClicked(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (ValueTree.mouse.mouseClicked(java.awt.event.MouseEvent) --> DependencyValueViewer.valueTree_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.valueTree_MouseClicked(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (ValueTree.treeSelection. --> DependencyValueViewer.valueTree_TreeSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4() {
	try {
		// user code begin {1}
		// user code end
		this.valueTree_TreeSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (DependencyValueViewer.window.windowClosed(java.awt.event.WindowEvent) --> DependencyValueViewer.setVisible(Z)V)
 * @param arg1 java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.WindowEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Comment
 */
public boolean connEtoM1_ABoolean() {
	return false;
}
/**
 * Comment
 */
public void dependencyTree_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	int selRow = getDependencyTree().getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
	//TreePath selPath = getDependencyTree().getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
	if (selRow != -1) {
		if (mouseEvent.getClickCount() == 1) {
			//mySingleClick(selRow, selPath);
			//System.out.println("Do nothing --- My sigle click on the tree");
		} else
			if (mouseEvent.getClickCount() == 2) {
				//myDoubleClick(selRow, selPath);
				//System.out.println("My DOUBLE click on the tree");
				DefaultMutableTreeNode dependTreeSelected = (DefaultMutableTreeNode) getDependencyTree().getLastSelectedPathComponent();
				if (dependTreeSelected == null)
					return;
				Object userObject = dependTreeSelected.getUserObject();
				if (userObject instanceof MethodDeclarationAnnotation)
					return;
				StmtTreeNode selectedNode = getSelectedStmtTreeNode(userObject);
				dependFrame.addSelectedNodeToDependFrame(selectedNode);
				//System.out.println("End adding by double clicking");
			}
	}
	return;
}
/**
 * Comment
 */
public void dependencyTree_TreeSelectionEvents() {
	DefaultMutableTreeNode lastSelectedNode = (DefaultMutableTreeNode) getDependencyTree().getLastSelectedPathComponent();
	if (lastSelectedNode == null)
		return;
	if (lastSelectedNode.isRoot()) {
		dependFrame.getCodeBrowserPane().setCurrentNodeSelected(dependFrame.currentNode);
		return;
	}
	Object userObject = lastSelectedNode.getUserObject();
	if (userObject.equals(dependFrame.currentNode) || (userObject instanceof String))
		return;
	StmtTreeNode selectedNode = getSelectedStmtTreeNode(userObject);
	dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedNode);
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (00-5-28 20:09:47)
 * @param node javax.swing.tree.DefaultMutableTreeNode
 */
void expandAndCollapseFrom(DefaultMutableTreeNode rootNode, boolean isDependency) {
	isSetSelectedByProgram = true;
	int rootRow = -1;
	//Object object = rootNode.getUserObject();
	if (isDependency) {
		for (int i = 0; i < getDependencyTree().getRowCount(); i++) {
			getDependencyTree().setSelectionRow(i);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) getDependencyTree().getLastSelectedPathComponent();
			//Object o = node.getUserObject();
			//if (o.equals(object)) {
			if (node.equals(rootNode)) {
				getDependencyTree().expandRow(i);
				rootRow = i;
				break;
			}
		}
		getDependencyTree().setSelectionRow(0);
		isSetSelectedByProgram = false;
		if (rootRow >= 0) {
			for (int i = 1; i <= rootNode.getChildCount(); i++)
				getDependencyTree().collapseRow(rootRow + i);
		}
	} else {
		for (int i = 0; i < getValueTree().getRowCount(); i++) {
			getValueTree().setSelectionRow(i);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) getValueTree().getLastSelectedPathComponent();
			//Object o = node.getUserObject();
			//if (o.equals(object)) {
			//if (node.equals(rootNode)) 
			{
				getValueTree().expandRow(i);
				//rootRow = i;
				//break;
			}
		}
		getValueTree().setSelectionRow(0);
		isSetSelectedByProgram = false;
		/*
		if (rootRow >= 0) {
			for (int i = 1; i <= rootNode.getChildCount(); i++)
				getValueTree().collapseRow(rootRow + i);
		}
		*/
	}
}
void expandDependTreeToOneLevel(boolean isDependency) {
	if (isDependency) {
		for (int i = 0; i < currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) currentDependTreeRoot.getChildAt(i);
			expandAndCollapseFrom(child, isDependency);
			for (int j = 0; j < child.getChildCount(); j++) {
				DefaultMutableTreeNode dependChild = (DefaultMutableTreeNode) child.getChildAt(j);
				expandAndCollapseFrom(dependChild, isDependency);
			}
		}
	} else {
		for (int i = 0; i < currentValueTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) currentValueTreeRoot.getChildAt(i);
			expandAndCollapseFrom(child, isDependency);
			for (int j = 0; j < child.getChildCount(); j++) {
				DefaultMutableTreeNode valueChild = (DefaultMutableTreeNode) child.getChildAt(j);
				expandAndCollapseFrom(valueChild, isDependency);
			}
		}
	}
}
/**
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88GE9EF53AAGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E13DBC8DD8D45715A1C2EB2D595814ECEDEB3AB4EBDB7DCA931B5AAEEDCDEBB7E6EB3A34357D6C56CDED6366B32DDFE33AF6E37A19547DF2079F95A5080A8A71EF84C6C0E078B708000C83B86070D319981A580601F94068B0B319F96800A45DF36FFD674E1B474CA0B62E5FF7F86F1EF36EBD671EFB6E3D671E7BC612F3A2A1DD1712C308AE1DA87F5E5091B250CB4831CB01DFA0AE174468086A5FC900FD
	643E33E970AC86DAC2CF0CAE1B6CED5E8565C6A86F3A9423BB8C6F87C8CE734ACEF809A0E7A1509ADD9D8FC41773C1814A59494A3FB811894FAC009640B9G1691AA7F4593398AFEAE143314F78A3A94C23CBA4A735D2EDC95FE2B42578A34FE001C944A1325BF2A5013GC7F95640F3230E73EE07E786536E3D1EFA05EF452EA54461F3453DC6E5295AC7381A426B5E23BCC9CBCE9CB5C4AAA1652D54E63D57C71AA54738713AFF5CB864749B876DEE0764339B3D0E913F4BB9A4790C8E49AB814EBDB4515050AA
	5E2F58DD63529527F4D372EDE13ABF57350B37CB1220BC15CAF1FF6926762A05F7AB00A395757FDDD723225F408BDFA77A63B7A26A3FB2156B3DB1B528BFD932B5A5BE0D367F06BED329D788652BGDC173F69D117F5E928CBE2B9654DCE0F5195833424A35E5CC1717DD076824CCDC79D1FBD0FBA3AC63FC9B6FAA66376C650B1A11D6B36BABD28637C0A4369F94CE72E996838276DG39G0DG83G77GA4G9CE13EEAA8BC8DCF5320EC760C0DF95CA62F77024B616B72B8A4973CB7B4GC541DD1606BCBE87
	A15C9FFF5BD596C69F04249D213EC801776DAD027E33B0659192F8B70E790436EF2DDFFCBBAED9E9E3430EE05F74BB295DFE42461640BB2D378700631FE1F8078A4F47EA0861E9B996E81BF6221D630BD1174C879613C5DF1262F6065125F7673DF156DDDC87472E20AE5C462DC554468BG1F88308AE0BD40F6002399E8E3CFE14DCCB66EF04B124FEDF7750D8FFA6DBE7942385385F3C131F513BE1A2D43812FF7E9567538DD16542847E85BAB4E30F671EE9F0F2B4F6076684D885A054B0E0DA85B1BB15DB732
	44986A0DF42CE2B37898E688FCB943A72870FCEC9FE3F30B16D7GCD3F9B00717F8B1BDB313B35BE72A443AF5E2D75110E8E7411D5C0AB5E932363637BECB1DD9767C3F98940CA00F5G5BGF2B201AF934778DB059537B90FFAD5634B6B7A8D0760A985E41F3D436D176D6EA1690A5D67348F3AA4BFD1F8268C542E03A175E2587C9E829997A5B72C1C32536306566DE392B8131F734CB36C23BCBE09D1ED4EB189840CF989367B0061GBCDDF63F7C2657E11725A08727FF46C0EDFB43FE85DA048660BD10C96D
	256B266B1C855E9D19E82B3D27A9EE8514578A3B9CBC359F1E174039A597AC624EA12A065DB701BE7A73EE58A049A6609FFD8DEC8B90480276770A753A9FFDF15BF30F1055FF988E6B0B94643D5C2F666F557A62F3CC46F2402F85588A10139D232B4D463D55595D9F41D733153628EEEABC55D14BCB7B006F6BD47CDC463D91E56CD11E69E1662216B7D5753ED3754E6D7FFEA975E19B744B8B30A09B472077141E7B66853BDFC2EF6C7101A3C90E6008F05E2EA23A57C8B7A4370CBB2E6CD7FC41D614917453E1
	0F702137991E2EDBE6F0A43F408F946D63F8E8C36D75C89FE5F43BAC7B1C03633254A7493253BD922C6FE53A78C316C43F52573F15513EAE01BE2E84D8A37A7AF25181153FD9BD1E6B63DE9B1DC90A5FFE166D4F9B01FF3B987FD346EC3ABE6C0B513582B8G2676A12D0651A86FC2402DG58A0E8C9651146143691E90D1D0AC253AB34D6A163A791E56C0C000F64BBDA9FCA973E79F8871D9FBA0506788CC17FF7C6CF5B9F23AB833081CC81AC5E0FBA76F474C728BF1B3DA77ADEB64AE4E85B5F73A149603E33
	39B8326F3FD89C5E77DFAE8E6F7B2F96C7776D1DA561FC1B5332CAE89FBDB75D2A39F220042E23C39E37DBE293D6D4A1BC267E1D0D764369F64AEFB99DA3125C671C14A0565FCFFDBE172D5549703EE1BF2E2BB78BF0DDF5DCD9CAAA9F3F92B68EDB70273F45559F60BEE9BD90DCD739CEB927699C14DC5298984A6C99F74B9C7FBA5B87943CBF15611A5F26FD5197078D634BEEBA5D8E4FCDA3FF18DD9E3F64A83EE63FE1B75AEF4AC6B6A04637982D0D3E17684B4B1B7561DB72CB7295275F89933E786D12ABA5
	155CE78E106F7A6C9A79B61FA4154970278FB4E5B6B5C6E95791D26FDD1B3A9ED34ADF8C622A884A2BEF0A8FA7EF4CB36E170C6C2F994EB8572777330B52E66C26C61DAEF5035608AD1E83952B39FE1F4C1A4774E30DFE282A5D4096E723D58D5AED2C6AAD0DDE37BDC4C14191C627D659BBF84C6882FF3E87E061815CBB7E71B425635E21EC10E60FCBE988E8DB8E5079E46849D14D11E58778F9BCCB507F94C22F4F6174D8F8F27ACF4352978A7AC2CFB8FA222067F94351D766E0BF661452BD4BAF3DB78EA612
	6866F7C91A40F830F43F0646CCA8624C87CA691A6AD26D1B9C9FD796FE2EFF3C0C66A92E0B766FAF5BC1E83C485B64EB53B2BBED536BF30EC118C18381A5AF7118DE1D17D88B7A539C46B6F8E69E0C51713A7B5EBC884F3083AE665A8A1AE4D1F8983D4F6BF2C2446116B8FD72F5AA1F7713E2DDCA3D054E3DA1EDF39A57ED4228562D70A0574D8A4F81G4EF335C04D93D0F89641F3196079F1A09C4FEAF82E87F09C22BC2493F3AFDB8F7238A4739067339C62FED5FF1872BDA1781681FEB540A605AF6D301A6F
	03CB4897FB1873ADBA4C79560570192C48274F0D5155837467F23E78DCB5DFDA97722DD578B6ABFC3B72B8DFED9E652B68465CC7054035B35CFF43067D65B4BCDFF6E52BF3C8D7775D883B77B0FBA96D4F4F531E3BFFE3246DA682FEC59E7AFDC639BA76B3797D1EA1A7BB76A8BE1CD42E5E17B86E71725D709CF67A7CF086F7C8811C87CF14D3DD6939C8C1AB3E5CFBB15CD93E4D6114BDBE8C2D4DFC1BB20B4D1A7A2B52774FD42B73CFD30B7A6376C4687B9A254FDBD4FD5F7C792DA9DC0FAF17B65D2E9EB60F
	990E089618529A0D20ED16331A5B0363D679BA6631B2FA91B7AAF0BF76A0AEC160FA3DA25F75BA4E5917DF27BE4F72FBF4518F987D30310FB4B4F4365333A92FF774A42DC763386CA3188F7BC881DDE3ACD09EG889C4176BEEBBC9A3EBDB14FF91BDFE5BEC16D3CC03439B41F36399A4A9BG3609B6DFAC0920A3DB84F8FB71853B14F14FBEAA720A27180ED09EG889CC5BB6C1CC05E8502F74DADA69B4A9BG368170DCC3C9E7A636FB8C66B8GDF0F96F452F9B34BD8C8714BBD4CE7C2239F1CEF4F9EEAD1FC
	AA31F599D973523DE1730959476F0D3B67987735A54702BE377149BF26EC83603AEF963AEF811E626338A7BD5D2C5E1386G9F8B3064B8762D6F9245076E65B8273E58EE0067EDC5D8847BC4FA55FD6A3FF6BDEC1F1217BA62D69CD7723847D5736811C3296451C3293CCFDF3A04FD2ABC91239BBD01BAE7D94B61B92B38C82C93BF301C1D5E275B8F088866252ED9D1378450EB5D89347752EE2D3D3375B13ADABD6A7E133FC3F7F92416A8FB78AC5566BA17F7214E9650679E21D3FCD7E49F7865E5FD084C7043
	2F64102BC3760F2678087BC7121E0FF932DE3BFF9C292273F59360376BF17FE8B652B39B6E9F17C1365DBD829110B89F9AE9BFA056D14ADB0C6928B3419CCCFFB59D176C13F0D686E885388AB0F592E55C2C18B1975C9608128B3E2FE7B6B95DC5A7710C1E55BB1BFA4BC43D351E5954DBA56A3D691DCD3DF5225E4913345E661361F3A4A4BF941F130F795ED7585ED11C4F733AF41CB8BE2D10626BD5F86E237399BFADDB0016180F723FE12472338B264BAF84DC3D86EFADC07947D8FB8385DC0ECE48BF4570AE
	951E4BFFB49F6F0F82C0DBDF0072BFDD2035DBCBE1D0262DF03ADE4A1D885B17C68B391CF401DF4F70D3AA3CF2A7F28A67688220EDADC4791993DA797D0620CC17A1127C56A95A5EBD862D7C9E060FB7E8657F47AD143F84E81927D0FEC527D63E77D4284C791A32F2C7B22D1ED16B531AFAFC3EBED2C3E30B55C05B88309DA037A8C65788508FB0D5047376490AF3E426F94B03AD93DB324C5ACC0D22677F5CE17F4825197A3728A8341EF2763251BB69B0F18EDDE015334E471B54E71465C5BC4739811EDB0BB0
	3E59F79E631B4CE24C779B6A286DDA21BCG90G0887C882D88B302D98EDF728B2DC2C9A4EF6538355E04E185B61AB536CF0FB767B7634FA47B5655C12F0753C56BB13774134FADAF9159AF9FCAC5E6C520E854DB9DB0077EA890E4506EE9C8B39C46429275D475E1E1E26E97534FE15D892EE5EC8E7288E2B003636844759DFC96F52F17F6C75B85DB2413DD3DFC573829E1FF34469C61C016106BCBE1668D46E8C2AA367E0C72A4367E0472A4367E07D55516F9FEED5C74A51723B02456CDC34917AC7CE03FFB3
	1C2197F1B982773C87F145826772A22ED1606A591927974A32405D4FF0F3211CA0F0D96C5C318A4A6BCE637812C21AE759A84A3BD9B92D944B8F94C43AA7C020F52351238117716599E5245EE17BD94266BFE4E738E268D7EBA95AA33702627A216C953847CF205DE2856ED76CEEE78914D78A3B94949021FF196A7D6B79B375656F81AE63C7F7D5866F73B266ABEB213F1B049DCE9B50B619E5E8071F1F2238CAA8DB846ED906F314713303F297FA8AF5263CDA1916B038595B65DEA1A31C3FE86FC16F4CAE8593
	E81758B2344BC313E817A542861DD36CEE844A1B846ECD06CBAB67E7D80EDBFFEB36E339E986DBA52B68DC46CB3316B1132D5476BD464ACF9D226B2B9716C9494FB65DD63BECD7E83F11E92EF55A9DD337FDCCA218EB9D6E4CD0F85AE4CF973D6FD0F2EBB511576D6F5604DF371F2C217B055FB36E9B1234EBF3F2CD2E208523018ED99A236AF01E1F317A325DB7A2CDDB5B57337A1C96297EF3B51156FF5E5712CB2A3E1A2B8846D4A3ED54DE6172D1C3BE0F4BC5B3C884F3EBCFB4E1FE2C309C73C206E69A7BDE
	05328CB03F9CFD6C5079C8E726454013D4BE5D1FF8BFAA6AA25B7ECCDDF85BB756C5363D39EEE65BDB6BA25B3E2FEEE65BDF2D0BEE7B0DA1362F9678AFD955789AE1677B44B70049424EDBD99EA24D9823BB8AD0EBC4BBBF579D494ED66099B0CE37B317FD2DCBAD3BD674775A99F60E93E5B7ABCF88FF11CCD9429EEDEE079298717C7837D90CD18E1B7963BA444530F862EB0023E78E0ECBE17144D32008BD95F1F3982E8678FE1C02BA761C24F358A18D5B47DDB24B9E76F815CF03G1FE8E4B92513A2FEB4E2
	BE742BB7E99EFA58E79F03E16873CAC3A4360272FE71A44623DB00B78DF005G2DG0E8A4C6B988AD8DBCE1965CA12AB68772C8F51B43722531EFEBA56BC7B8B7EA033F00C4D919E731EAB545E1150BB106CCA10D509B2B69A290FBB1CB71C60E9D64F906F916F4F7F3218013657A878ABAAE8DEFF50BEF4FD446799F7BB306F97D9ACE1853EFEG5E4735629B5AD840AD84C882D88730CD3477121E5A8762D7D0DEAD5B1FCF5B23631DDB053CCBCF533856E51F700C43B8A7D5D13E7F94F7C5D6A8BBG6683A4D4
	71781E6BE1B2A08F2906789220964086A0D7E35B7FDAC27B456E322C76C14905B67E581BFB912FDA05F82EB027C81DAB0C2F66B2136049F533CE205C64EA2E5BC6F8EE2B6667C02E5B72C964492E01F181E885F0G445620EEDB2BC36F5338021C76573CECC18BAA48757B6814D6BFAAF7F18D4E5755CDD13FB52D09141378B4DBE713EB34B921FFE67875B55A1CD0E9B36604B6832D37966306727398FFAE9738EC8B46922BEBC58C9E751B584D3511749C655F3356E97574B1FCF61DD64FD73AD0CFBD50E26BD0
	276D5D2867A601DBF899755CDE07639443F2EC2A2DCB39D00039C93FBE147D46560E3E5E1E3E36F7FB3A5FB5DB610567DD7F0B28732EE47B7C08F9D77D9965DED0F9EE7B5A43F378BA1DFC92670B8BE80B4FD0DCFF3EFA7C290E4E94FE8E6574F9A1F43A16FC142869ABC268D45702D4B57DF7A1F42A739C25BE6FD769BBF8A7E7BB73ADD27C44E044EF42B7AB7D49FE03BF97BF72701C321F9ECA656DD4542B6D337CAFFF0E53C5E8472254373D914CCBF37B788BF1ED1E7B866EE3AF333B7715D0DE8730658D9C
	471C0A946AB6153866E61FA784570347CE1C0027DB3A299779FA3347657195D7D75616A9D0226C1D2F1C40B159D50FE3D22C570E496FC3EC7E36DEBBA63F51296973755AB1B105503F23570EC9FABA27F37D67659BB4BE9A3C6DB3DEE93BEC6BB01B2C6F7675DABBECE2DE688AB1275A827D8854635E76EBA35D17C53CC60D5150F041C33F6F3072C51A5B2123906D302C9EEDFF133539894A64EC0CAE77ACFEBFB5190ABE333D649B44557EC1D81FB97AC7D7DC65D9E52C4F8647BA2162F0BA17B9E2C0198B4E22
	6D9FB4C4377D778DDA5B1704787B4B066836AFBD25357D0BE91C2E5C0D8EC7389BE58BBA577BD2915E352EBE0B771A72845A1FB444686A9BB0DF35E2827B68EDC03B7E1671AE0172CA00F5027769C9645D52007658F5AB3ABD9AEEE96D7169903A6BD6F4FBFC6E16569E3FD76C41F57A4E947635789C2E2B4FB4613ABAF08E577FFFEA26B91979223C14156977973CDC55FCB77281D49EFDAA67593BA2834F11BC6639427643A5A0F779B90C8F0AAC989FECG5CD6001CC608DFGEC0DE83325DDE8B33991ED626E
	26B6CA9065GAB53EFCC143C1778EE1A72FC587D62B2723A2365A37EDE60B6B330B8BF897A302A91ED7668E591DB83AE2D8962AA00D6GC7936658F73650FA31D0CEE842BC6B330D250446FA5726FF758148DE1F87A2FEF902E3D9C4287433A27F4EFE8F325306EBDD7C23DFA07D09FF0E784D4A4AA665B7BB4A33B26161B974497B721E0976B73BB9C6D786508BB08AB085905F0CFDABE1319E3D91G75CD32EC9F9AE5DF6B60C5817DF0A5082687955EF67C852A2972FB1BA6C3A47BC08B1D3024758CDE03C329
	4DBEA2FCA0D028354D1266203F2DE80E943B0D31EF8957B4EB6F7DEEB07C7AE66D3D5F2BE538C6EE821A75BC7A792BA76E6CB7BEBB2769FDFB758E5EE13D6ED9576376F81E6D234B4EEB6D341561D71C575A69FD31172F86DAD98B4E0765E676FBABA8A7822C82D88F309DA05704BE745679CAB243FD134D6335CAB7A4176ADA00774FE538B33B7496296B1519383F34A8CF9BBCC7CD616EDFA6275D375E1E3C39F7D86FFB332E4747692F136C9E59249D3F91061FEB520E5F96918B4483ED1B897D7C1949BB537B
	09A9F53D2B9738AD3D4AB3961E8BAF045AF765852A479F1A74D15ACED46A701853F2113E07FE310D31F5E19B91FC69165B0F2D3BACA236B6E3BCF34B220DE7BEC1547148D7AC5AF8C697C2FF46220DE79E8A216F6B5046B3D81F5BE4E5576D5B64AFA65A3E789A1B20BD0E35876DF15F655B37474FAE23BDDA04BDD27B225B232EAF3ABD2E87225B6321C0A4FB7075A2330562C5AE12EF19783B0B96DA177E2E42E9F7B9A7D96A278F985DA3FCBDFA211D52835017F800A400E4008DE6DC0BBE5512CF58DA94E6AF
	EBF30CC8F866C6F9132648395C98534C3F931CE7E2F9AF0FACFB46E87B08DFE02273C876F89912639EE4E36BF30E0C4A1CD541FF56C44748A58DB334F2CFD90877A95B4D984FFD3F8C63B9FDAB4EEFBF7D6CCC1933D32A7774083FE37AB0004BB87E7FA063770541BE3D22FA1FD3F877E5AF3A2BB2146F214D117DEF15B97CDD428F4C616F5F5718237F86EA1DB972EF201EB12BFF83457DED041DB9DB4157EC2D68037F56C7F12334ACF025B6F1B7ACF08E06DB86655542D79D1341719BD33D7FE1626E0F655E49
	3BFFB77C74945AEB3D30433CC634579601FB3ED16488856EC4935A2B388D73CD3F6C22F9ED33C4FF2B0EE783E3G4F861BAFA61264357362F0FD566A5458267C1636AD1887895A6332654E620227AF4F369E772F175AA37B797676195759740B6C4E8156C75B04D739535AFD11FD63B2BC6C1744FA3E77E23640F578FBBDFEA75DC1F85EFD65E9BC7FADEA63F9776570DC8DC03F6B5850463EE9A84B0D5297BEAE9F9451B3733047AD7333E6BC1BA7456DF07EEE97E357C7F9E468945F3F6F3750A1607140851BBE
	0A0C51F6F5BCF04B46FEAF6171C92177AA1FE473EB8AF86782F0DC6A7BACCFG6565GDC6EC8C7D0EECA54588C657236ACCD587E26F6BC8BD8BBE97DDD97615C8CE08198G10816683A4GA4DD44FD7873970BC964B3B3CF86323D58E7F73AE59E43FE2E94E3583550CE2685F3D43FAE4598265502792C9CF62E1B02F2BCC022857304BFBA231E9F5467A36516D7D9F8FFB7D9C26787371B251708B178E69F6DCBC83C558E03DF1C6F009845B3E2743A208F79E57A4AB2FDD5F5892F732E0D2E275D9E1FBCAA5AB4
	7DA9586639AE9A470446EF46C117FD68BA673D37AF487BFE636C653F579429FD654C5699F9BE7E35F36679B85299BE6E996B1C9E7778BA4347BD01CEF55C43F16FAB6D7A4407904AEF6CBA69B872BB3EAE3B5BBE227AC6F1F1691D2DE50F5DE13D67E7DD0F1F5D66B21FAD6E501E69BE4170359D5AB35D6BA2F7518234459DB88FAB72689A352B9366A10085E0GC086185F0973AF77D2CD1479F75963E119FDFABB8C3E231E0ACA9F7F01F6D1F97F146ADDDFF677776A33F7D58637CFFDDEE47FBF1FB7337FDB72
	427BFFF75EF47F6F4B8B6F7FFF4ED37BBF7F3F2C5E3BBC8FB60E4497C83ACC72455158A97975F637C3FF45BE6E12746DAE4FCDBD7FCD265FC732C2A7396E8B0767E45FDFB31EC2F634139D43F617F7545EC152592FA62CA4E548AF13A36CB7A595D025E208D629E0957B3C76A1695CBBA4E358B7B24858E51272CE7BBB5088AB3DC7BEA029D632DB9E659F9584E4199C8E3654CB1DC7A11C8B507F70043C1A1C1C6C05FFFEDAD5979DA3139D7C8723B249D4F79BBA78DA482A7764676BFE1D7C2BAB7ED432230364
	51CD38CF7231332264205FD6A97FF1CABB4970526FBE686F72BBC81A1FBD527D72048B2C10B3FC197EA057AFCB8E93C64BA078A06F4D386CF4D5D86C7ED1190E78B9ABC9F3537F62056C67E4175DBDD2410F243C1FBAD38B6D89E1174A206F8126AFED45A8DAB9706D3D06131D9B13D2C9CABB596D72589D6D76A118109DA4C39E75C97ED10F4B91701E678D6D3AC97D0266FCD32139A3227B3433C69B58D7A8FDBD70507C57B63D561E82A1280FD4B0CEBAGC64161B00F5AFDA0CA72F548743B940F2FD050E20E
	4B27FEF87170559422D3BAC907A5B3G0311813E6F76C358B1869A68B59AA0C9B4E47C64D3158FAEFD65ED56D0302ACCCE847DE3C4720CD5180607E135918D007D2079E322F94AE354706C8FFC62E0674FDE9D0471EEA529E315156CEF957BDBCD7FD6B1CC9543D4D503913710287F3474GF346EC19BB8968331769C3075988B34DAEBF7D42D38F7F22F14BE668DD3BB211405859C12E6E7131C149771144870F6D095F70F1BA96DE6A9258AECC42B127EB82DEDA59FE9D20F8FDC0B587G442C58BD4D23A8B6EB
	3AEBD074FEC64CE1FF8F4F3A6334D69E2BFB048A24AF47C335AC8A6039E3C51B7A2E7D28EDAC6237889BFB14EF90FA343F810BED204A57764668FAFBF11FDAFB16A6ADA25D9D740D59DDAE596E3AEEF2BB68BD420589D98A1EE7BF56C05B77A30FC2D472AF8DD46FEB2ABA7F87D0CB8788BF7DAEA4539AGG08D0GGD0CB818294G94G88G88GE9EF53AABF7DAEA4539AGG08D0GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG8D
	9BGGGG
**end of data**/
}
/**
 * Return the DependencyLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getDependencyLabel() {
	if (ivjDependencyLabel == null) {
		try {
			ivjDependencyLabel = new javax.swing.JLabel();
			ivjDependencyLabel.setName("DependencyLabel");
			ivjDependencyLabel.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjDependencyLabel.setText("Dependencies");
			ivjDependencyLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependencyLabel;
}
/**
 * Return the DependencyPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JPanel getDependencyPanel() {
	if (ivjDependencyPanel == null) {
		try {
			ivjDependencyPanel = new javax.swing.JPanel();
			ivjDependencyPanel.setName("DependencyPanel");
			ivjDependencyPanel.setLayout(new java.awt.BorderLayout());
			ivjDependencyPanel.setBounds(632, 76, 160, 120);
			getDependencyPanel().add(getDependencyLabel(), "North");
			getDependencyPanel().add(getDependScrollPane(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependencyPanel;
}
/**
 * Return the DependencyTree property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JTree getDependencyTree() {
	if (ivjDependencyTree == null) {
		try {
			ivjDependencyTree = new javax.swing.JTree();
			ivjDependencyTree.setName("DependencyTree");
			ivjDependencyTree.setBackground(new java.awt.Color(204,204,204));
			ivjDependencyTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			getDependencyTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
			getDependScrollPane().validate();
			getDependScrollPane().repaint();
			ivjDependencyTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(java.awt.Color.black);
					return this;
				}
			}
			.setAngledColor());
			ivjDependencyTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			ivjDependencyTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					setForeground(java.awt.Color.black);
					setIcon(null);
					return this;
				}
			});
			((DefaultTreeCellRenderer) ivjDependencyTree.getCellRenderer()).setBackgroundNonSelectionColor(new java.awt.Color(204, 204, 204));
			ivjDependencyTree.putClientProperty("JTree.lineStyle", "Angled");

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependencyTree;
}
/**
 * Return the DependScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JScrollPane getDependScrollPane() {
	if (ivjDependScrollPane == null) {
		try {
			ivjDependScrollPane = new javax.swing.JScrollPane();
			ivjDependScrollPane.setName("DependScrollPane");
			getDependScrollPane().setViewportView(getDependencyTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependScrollPane;
}
/**
 * Return the DependValueSplitPane property value.
 * @return javax.swing.JSplitPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JSplitPane getDependValueSplitPane() {
	if (ivjDependValueSplitPane == null) {
		try {
			ivjDependValueSplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
			ivjDependValueSplitPane.setName("DependValueSplitPane");
			ivjDependValueSplitPane.setDividerLocation(50);
			ivjDependValueSplitPane.setBackground(new java.awt.Color(204,204,204));
			ivjDependValueSplitPane.setBounds(642, 243, 160, 120);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjDependValueSplitPane;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-10 10:59:03)
 * @return int
 * @param dependKind java.lang.String
 */
int getInsertIndexOf(String dependValueKind, boolean isDependency) {
	if (isDependency) {
		int kind = dependFrame.dependencyKindsList.indexOf(dependValueKind);
		for (int i = 0; i < currentDependTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) currentDependTreeRoot.getChildAt(i);
			int childIndex = dependFrame.dependencyKindsList.indexOf(oneChild.toString());
			if (kind < childIndex)
				return i;
		}
		return currentDependTreeRoot.getChildCount();
	} else {
		int kind = dependFrame.valueKindsList.indexOf(dependValueKind);
		for (int i = 0; i < currentValueTreeRoot.getChildCount(); i++) {
			DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) currentValueTreeRoot.getChildAt(i);
			int childIndex = dependFrame.valueKindsList.indexOf(oneChild.toString());
			if (kind < childIndex)
				return i;
		}
		return currentValueTreeRoot.getChildCount();
	}
}
/**
 * Return the JFrameContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JPanel getJFrameContentPane() {
	if (ivjJFrameContentPane == null) {
		try {
			ivjJFrameContentPane = new javax.swing.JPanel();
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
			// user code begin {1}

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJFrameContentPane;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-15 11:03:32)
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.StmtTreeNode
 * @param userObject java.lang.Object
 */
 StmtTreeNode getSelectedStmtTreeNode(Object userObject) {
	StmtTreeNode selectedNode = null;
	if (userObject instanceof StmtTreeNode) {
		selectedNode = (StmtTreeNode) userObject;
	} else
		if (userObject instanceof ParameterNode) {
			ParameterNode pn = (ParameterNode) userObject;
			selectedNode = new StmtTreeNode(pn.sootClass, pn.mda, pn.mda);
		} else
			if (userObject instanceof Annotation) {
				Annotation ma = null;
				if ((userObject instanceof MethodDeclarationAnnotation) || (userObject instanceof ConstructorDeclarationAnnotation))
					ma = (Annotation) userObject;
				else
					ma = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation((Annotation) userObject);
				if (ma instanceof MethodDeclarationAnnotation) {
					SootClass sc = ((MethodDeclarationAnnotation) ma).getSootMethod().getDeclaringClass();
					selectedNode = new StmtTreeNode(sc, ma, (Annotation) userObject);
				} else
					if (ma instanceof ConstructorDeclarationAnnotation) {
						SootClass sc = ((ConstructorDeclarationAnnotation) ma).getSootMethod().getDeclaringClass();
						selectedNode = new StmtTreeNode(sc, ma, (Annotation) userObject);
					} else
						throw new edu.ksu.cis.bandera.pdgslicer.exceptions.SlicerException("Annotation containing userObject should be method declaration or constructor declaration.");
			} else
				if (userObject instanceof SliceTraceNode) {
					SliceTraceNode stn = (SliceTraceNode) userObject;
					Annotation mdAnnotation = edu.ksu.cis.bandera.jjjc.CompilationManager.getAnnotationManager().getAnnotation(stn.methodInfo.sootClass, stn.methodInfo.sootMethod);
					selectedNode = new StmtTreeNode(stn.methodInfo.sootClass, mdAnnotation, stn.stmtAnnotation);
				} else
					throw new edu.ksu.cis.bandera.pdgslicer.exceptions.SlicerException("The object of nodes in dependency tree should be StmtTreeNode, ParameterNode or Annotation.");
	return selectedNode;
}
/**
 * Return the ValueLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getValueLabel() {
	if (ivjValueLabel == null) {
		try {
			ivjValueLabel = new javax.swing.JLabel();
			ivjValueLabel.setName("ValueLabel");
			ivjValueLabel.setFont(new java.awt.Font("dialog.plain", 0, 12));
			ivjValueLabel.setText("Values");
			ivjValueLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValueLabel;
}
/**
 * Return the ValueScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getValueScrollPane() {
	if (ivjValueScrollPane == null) {
		try {
			ivjValueScrollPane = new javax.swing.JScrollPane();
			ivjValueScrollPane.setName("ValueScrollPane");
			getValueScrollPane().setViewportView(getValueTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValueScrollPane;
}
/**
 * Return the ValuesPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JPanel getValuesPanel() {
	if (ivjValuesPanel == null) {
		try {
			ivjValuesPanel = new javax.swing.JPanel();
			ivjValuesPanel.setName("ValuesPanel");
			ivjValuesPanel.setLayout(new java.awt.BorderLayout());
			ivjValuesPanel.setBounds(633, 404, 160, 120);
			getValuesPanel().add(getValueLabel(), "North");
			getValuesPanel().add(getValueScrollPane(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValuesPanel;
}
/**
 * Return the ValueTree property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JTree getValueTree() {
	if (ivjValueTree == null) {
		try {
			ivjValueTree = new javax.swing.JTree();
			ivjValueTree.setName("ValueTree");
			ivjValueTree.setBackground(new java.awt.Color(204,204,204));
			ivjValueTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			getValueTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
			getValueScrollPane().validate();
			getValueScrollPane().repaint();
			ivjValueTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(java.awt.Color.black);
					return this;
				}
			}
			.setAngledColor());
			ivjValueTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			ivjValueTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					setForeground(java.awt.Color.black);
					setIcon(null);
					return this;
				}
			});
			((DefaultTreeCellRenderer) ivjValueTree.getCellRenderer()).setBackgroundNonSelectionColor(new java.awt.Color(204, 204, 204));
			ivjValueTree.putClientProperty("JTree.lineStyle", "Angled");

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjValueTree;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 14:40:25)
 * @return int
 */
 int getViewersCurrentState() {
	if (!doDepend && !doValue)
		return EMPTY;
	if (doDepend && !(doValue || doDependValue || doValueDepend))
		return DEPEND;
	if (doValue && !(doDepend || doDependValue || doValueDepend))
		return VALUE;
	if (doDependValue && doDepend && doValue && !doValueDepend)
		return DEPENDVALUE;
	if (doValueDepend && doDepend && doValue && !doDependValue)
		return VALUEDEPEND;
	else
		throw new edu.ksu.cis.bandera.pdgslicer.exceptions.SlicerException("The dependency/value viewer should be one of the following states: empty, depend, value, dependvalue and valuedepend");
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
	this.addWindowListener(ivjEventHandler);
	getDependencyTree().addTreeSelectionListener(ivjEventHandler);
	getDependencyTree().addMouseListener(ivjEventHandler);
	getValueTree().addMouseListener(ivjEventHandler);
	getValueTree().addTreeSelectionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("DependencyValueViewer");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(401, 308);
		setTitle("Dependencies and Vaule Flow Viewer");
		setContentPane(getJFrameContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		DependencyValueViewer aDependencyValueViewer;
		aDependencyValueViewer = new DependencyValueViewer();
		aDependencyValueViewer.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aDependencyValueViewer.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
/**
 * Method to handle events for the MouseListener interface.
 * @param e java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void mouseClicked(java.awt.event.MouseEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getDependencyTree()) 
		connEtoC2(e);
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the MouseListener interface.
 * @param e java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void mouseEntered(java.awt.event.MouseEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the MouseListener interface.
 * @param e java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void mouseExited(java.awt.event.MouseEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the MouseListener interface.
 * @param e java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void mousePressed(java.awt.event.MouseEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the MouseListener interface.
 * @param e java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void mouseReleased(java.awt.event.MouseEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-10 14:17:13)
 * @param enlarge boolean
 */
private void resizeViewer(boolean enlarge) {
	if (enlarge) {
		java.awt.Dimension contentPaneSize = getJFrameContentPane().getSize();
		int newWidth = (int) contentPaneSize.getWidth() + 5;
		double oldHeight = contentPaneSize.getHeight();
		int newHeight = (int) oldHeight * 2 + 30;
		contentPaneSize = new java.awt.Dimension(newWidth, newHeight);
		setSize(contentPaneSize);
	} else {
		java.awt.Dimension contentPaneSize = getJFrameContentPane().getSize();
		int newWidth = (int) contentPaneSize.getWidth() + 10;
		double oldHeight = contentPaneSize.getHeight();
		int newHeight = (int) oldHeight / 2 + 40;
		contentPaneSize = new java.awt.Dimension(newWidth, newHeight);
		this.setSize(contentPaneSize);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-7 13:51:33)
 * @param dpnd edu.ksu.cis.bandera.pdgslicer.dependency.Dependencies
 */
void setDependFrame(Dependencies dpnd) {
	dependFrame = dpnd;
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-5 15:04:23)
 */
void setToEmptyState() {
	doDepend = false;
	doValue = false;
	doDependValue = false;
	doValueDepend = false;
	this.setVisible(false);
	//this.setVisible(true);
}
/**
 * Method to handle events for the TreeSelectionListener interface.
 * @param e javax.swing.event.TreeSelectionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getDependencyTree()) 
		connEtoC1();
	// user code begin {2}
	// user code end
}
/**
 * Comment
 */
public void valueTree_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
		int selRow = getValueTree().getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
	//TreePath selPath = getDependencyTree().getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
	if (selRow != -1) {
		if (mouseEvent.getClickCount() == 1) {
			//mySingleClick(selRow, selPath);
			//System.out.println("Do nothing --- My sigle click on the tree");
		} else
			if (mouseEvent.getClickCount() == 2) {
				//myDoubleClick(selRow, selPath);
				//System.out.println("My DOUBLE click on the tree");
				DefaultMutableTreeNode  valueTreeSelected = (DefaultMutableTreeNode) getValueTree().getLastSelectedPathComponent();
				if (valueTreeSelected == null)
					return;
				Object userObject = valueTreeSelected.getUserObject();
				if (userObject instanceof MethodDeclarationAnnotation)
					return;
				StmtTreeNode selectedNode = getSelectedStmtTreeNode(userObject);
				dependFrame.addSelectedNodeToDependFrame(selectedNode);
				//System.out.println("End adding by double clicking");
			}
	}

	return;
}
/**
 * Comment
 */
public void valueTree_TreeSelectionEvents() {
		DefaultMutableTreeNode lastSelectedNode = (DefaultMutableTreeNode) getValueTree().getLastSelectedPathComponent();
	if (lastSelectedNode == null)
		return;
	if (lastSelectedNode.isRoot()) {
		dependFrame.getCodeBrowserPane().setCurrentNodeSelected(dependFrame.currentNode);
		return;
	}
	Object userObject = lastSelectedNode.getUserObject();
	if (userObject.equals(dependFrame.currentNode) || (userObject instanceof String))
		return;
	StmtTreeNode selectedNode = getSelectedStmtTreeNode(userObject);
	dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedNode);
	return;
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowActivated(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowClosed(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == this) 
		connEtoM1(e);
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowClosing(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowDeactivated(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowDeiconified(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowIconified(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowOpened(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
}
