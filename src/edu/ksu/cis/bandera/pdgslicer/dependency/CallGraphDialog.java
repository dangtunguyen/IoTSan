package edu.ksu.cis.bandera.pdgslicer.dependency;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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
import javax.swing.tree.*;
import java.util.*;
import java.awt.*;
import ca.mcgill.sable.util.Map;
import ca.mcgill.sable.util.Iterator;
import ca.mcgill.sable.util.List;
import ca.mcgill.sable.util.ArrayList;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.pdgslicer.*;
import edu.ksu.cis.bandera.jjjc.CompilationManager;

/**
 * Insert the type's description here.
 * Creation date: (00-6-12 21:44:47)
 * @author: 
 */
public class CallGraphDialog extends JDialog {
	private SootClassManager scm = CompilationManager.getSootClassManager();
	private Hashtable newOldSmTable = new Hashtable();
	private Map sootMethodInfoMap = Slicer.sootMethodInfoMap;
	private DefaultMutableTreeNode callGraphTreeRoot = null;
	private DefaultMutableTreeNode succCallGraphTreeRoot = null;
	private DefaultMutableTreeNode predCallGraphTreeRoot = null;
	private boolean succCallGraph = true;
	private boolean predCallGraph = false;
	private JPanel ivjCallGraphDialogContentPane = null;
	private JToolBar ivjCallGraphToolBar = null;
	private JTree ivjCallGraphTree = null;
	private JScrollPane ivjCallGraphTreeScrollPane = null;
	private JButton ivjOkButton = null;
	private JButton ivjPredToolBarButton = null;
	private JButton ivjSuccToolBarButton = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == CallGraphDialog.this.getSuccToolBarButton())
				connEtoC1();
			if (e.getSource() == CallGraphDialog.this.getPredToolBarButton())
				connEtoC2();
			if (e.getSource() == CallGraphDialog.this.getOkButton())
				connEtoM1(e);
		};
	};
/**
 * CallGraphDialog constructor comment.
 */
public CallGraphDialog() {
	super();
	initialize();
}
/**
 * CallGraphDialog constructor comment.
 * @param owner java.awt.Dialog
 */
public CallGraphDialog(java.awt.Dialog owner) {
	super(owner);
}
/**
 * CallGraphDialog constructor comment.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
public CallGraphDialog(java.awt.Dialog owner, String title) {
	super(owner, title);
}
/**
 * CallGraphDialog constructor comment.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
public CallGraphDialog(java.awt.Dialog owner, String title, boolean modal) {
	super(owner, title, modal);
}
/**
 * CallGraphDialog constructor comment.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
public CallGraphDialog(java.awt.Dialog owner, boolean modal) {
	super(owner, modal);
}
/**
 * CallGraphDialog constructor comment.
 * @param owner java.awt.Frame
 */
public CallGraphDialog(java.awt.Frame owner) {
	super(owner);
}
/**
 * CallGraphDialog constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public CallGraphDialog(java.awt.Frame owner, String title) {
	super(owner, title);
}
/**
 * CallGraphDialog constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
public CallGraphDialog(java.awt.Frame owner, String title, boolean modal) {
	super(owner, title, modal);
}
/**
 * CallGraphDialog constructor comment.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
public CallGraphDialog(java.awt.Frame owner, boolean modal) {
	super(owner, modal);
}
/**
 * Insert the method's description here.
 * Creation date: (00-6-12 22:14:27)
 */
public void buildCallGraphTree() {
	if (succCallGraph) {
		buildSuccCallGraphTree();
	} else
		if (predCallGraph)
			buildPredCallGraphTree();
	getCallGraphTree().setModel(new DefaultTreeModel(callGraphTreeRoot));
	getCallGraphTreeScrollPane().validate();
	getCallGraphTreeScrollPane().repaint();
}
private void buildPredCallGraphTree() {
	if (predCallGraphTreeRoot != null) {
		callGraphTreeRoot = predCallGraphTreeRoot;
		return;
	}
	predCallGraphTreeRoot = new DefaultMutableTreeNode("Pred Call Graph");
	callGraphTreeRoot = predCallGraphTreeRoot;
	Hashtable callersForCurrentLevel = new Hashtable();
	Hashtable callersForNextLevel;
	/*
	Hashtable compiledClasses = CompilationManager.getCompiledClasses();
	List compiledMethods = new ArrayList();
	for (Enumeration e = compiledClasses.elements(); e.hasMoreElements();) {
	SootClass sc = (SootClass) e.nextElement();
	compiledMethods.addAll(sc.getMethods());
	}
	*/
	//collect all method without call site
	Set currentCallers = new HashSet();
	for (Iterator mdIt = sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		//for (Iterator mdIt = compiledMethods.iterator(); mdIt.hasNext();) {
		SootMethod sm = (SootMethod) mdIt.next();
		MethodInfo mdInfo = (MethodInfo) sootMethodInfoMap.get(sm);
		Map callSiteMap = mdInfo.indexMaps.getCallSiteMap();
		if (callSiteMap == null || callSiteMap.isEmpty()) {
			SootMethod newSm = getNewSootMethod(mdInfo.sootClass, sm);
			currentCallers.add(newSm);
			newOldSmTable.put(newSm, sm);
		}
	}
	callersForCurrentLevel.put(callGraphTreeRoot, currentCallers);
	do {
		callersForNextLevel = new Hashtable();
		for (java.util.Iterator calleeIt = callersForCurrentLevel.keySet().iterator(); calleeIt.hasNext();) {
			DefaultMutableTreeNode currentCallee = (DefaultMutableTreeNode) calleeIt.next();
			currentCallers = (HashSet) callersForCurrentLevel.get(currentCallee);
			for (java.util.Iterator callerIt = currentCallers.iterator(); callerIt.hasNext();) {
				SootMethod sm = (SootMethod) callerIt.next();
				DefaultMutableTreeNode smTreeNode = new DefaultMutableTreeNode(sm);
				currentCallee.add(smTreeNode);
				Set callersForSm = getCallersFor(sm);
				if (!callersForSm.isEmpty())
					callersForNextLevel.put(smTreeNode, callersForSm);
			}
		}
		callersForCurrentLevel = callersForNextLevel;
	} while (!callersForNextLevel.isEmpty());
}
private void buildSuccCallGraphTree() {
	if (succCallGraphTreeRoot != null) {
		callGraphTreeRoot = succCallGraphTreeRoot;
		return;
	}
	succCallGraphTreeRoot = new DefaultMutableTreeNode("Succ Call Graph");
	callGraphTreeRoot = succCallGraphTreeRoot;
	Hashtable callSitesForCurrentLevel = new Hashtable();
	Hashtable callSitesForNextLevel;
	/*
	Hashtable compiledClasses = CompilationManager.getCompiledClasses();
	List compiledMethods = new ArrayList();
	for (Enumeration e = compiledClasses.elements(); e.hasMoreElements();) {
	SootClass sc = (SootClass) e.nextElement();
	compiledMethods.addAll(sc.getMethods());
	}
	*/
	
	//collect all method without who call me
	Set currentCallSites = new HashSet();
	for (Iterator mdIt = sootMethodInfoMap.keySet().iterator(); mdIt.hasNext();) {
		//for (Iterator mdIt = compiledMethods.iterator(); mdIt.hasNext();) {
		SootMethod sm = (SootMethod) mdIt.next();
		MethodInfo mdInfo = (MethodInfo) sootMethodInfoMap.get(sm);
		if (mdInfo.whoCallMe == null || mdInfo.whoCallMe.isEmpty()) {
			SootMethod newSm = getNewSootMethod(mdInfo.sootClass, sm);
			currentCallSites.add(newSm);
			newOldSmTable.put(newSm, sm);
		}
	}
	callSitesForCurrentLevel.put(callGraphTreeRoot, currentCallSites);
	do {
		callSitesForNextLevel = new Hashtable();
		for (java.util.Iterator callerIt = callSitesForCurrentLevel.keySet().iterator(); callerIt.hasNext();) {
			DefaultMutableTreeNode currentCaller = (DefaultMutableTreeNode) callerIt.next();
			currentCallSites = (HashSet) callSitesForCurrentLevel.get(currentCaller);
			for (java.util.Iterator siteIt = currentCallSites.iterator(); siteIt.hasNext();) {
				SootMethod sm = (SootMethod) siteIt.next();
				//if (compiledMethods.contains(sm)) {
				DefaultMutableTreeNode smTreeNode = new DefaultMutableTreeNode(sm);
				currentCaller.add(smTreeNode);
				Set callSitesForSm = getCallSitesFor(sm);
				if (!callSitesForSm.isEmpty())
					callSitesForNextLevel.put(smTreeNode, callSitesForSm);
				//}
			}
		}
		callSitesForCurrentLevel = callSitesForNextLevel;
	} while (!callSitesForNextLevel.isEmpty());
}
/**
 * connEtoC1:  (SuccToolBarButton.action. --> CallGraphDialog.succToolBarButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1() {
	try {
		// user code begin {1}
		// user code end
		this.succToolBarButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (PredToolBarButton.action. --> CallGraphDialog.predToolBarButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.predToolBarButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> CallGraphDialog.dispose()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dispose();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88GA23B4CA8GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E145BB8DD414571536C2EB4FFA92133567F8D2B7A7BDB13B3465B4744434A64BF65D5436E6F5D35B320DA7CB3BA6F5D33BA50D5D58DD37354D9BFE9491F1948494D0C0A220B80320027C7FAAA888AB2312044479187900510F19F1669B8163463D773D6FFD5F474CB700A66DFA4E751BF75FFB775DF76FFD773DFB5F03E49DFEA2451418C508A905A87FEACC048CF692F2356CCBDF6338DCE2B7915D3FA200
	3D64FBA71361DB86F5CBBA6CA6B3A93E108A6586A84B4DF6D38E7C4EA44B8AAA1A61C718F112216EE7C7BF7A764C63BC16414759C14BFB64F478E6G34G4E8990C3F07CD2B9D741AF00F206729B4114C808E442B6AFB4646A70DB14F6ADD0E7437AC4EC53A667AB754B8107ED5642F70309355D8A5F1D143BDBFD35CA3B5854A8627C6CF864EE9CCBC71F43E0A26BFBD779127E7D11A808A4326AD4934A2C7F66E813688CD8EE7A8396074BEF99965CCE51A7D83C4ED13F64F208BE0BD3740A00F3BB266A6A5A85
	C9BA6F933CE39DAEC1720C9212C0795E5410CAE9825FD01ECCC21C3D91E5D5893F3BG6CC91C77DF77B6A83C595FF81694DAA642729E1B44F85E102471FECAF8B4F1D1B2523FC9F5017D8A21BC8840F8F9C6663C44A5F3DE761E453669A9F6D389C0C38A472F27F89B14A50008944E63EB4D1CC7E96CB932715EC711FB427038B4057136AAC563F1D53CAD6DG351723E654B9598E638254828C815C85D88A10C76D74550CE370ED9D165BBD63639EF72B575BA6B9FD3D9E27A8416F3ABA28D1F017C5074767A404
	59629B8D95867CF0D83EFD3A9DA23039DDA35CF616A4FE1DCCFABEC76DA1F8EE9D5FFCBFF235C2A3FE3BB6372C9DA837B82A5BED0047FE488F43EF2078419D9A1E69EA0462316C053A389DDC4E0B4BB8AF690FAEA3CB3FB592394300177E1C791135290C077ED40D97A6E3328FE5FC9770CBGE2GD683EC82C85D49E5FCFCCF75ECB26EF64B224FADC88301E12F60135B82AE895601A26BDB72CC32B682566F5EFD77E3F2092A566BE8EB5ACDC339F853A7A2CBF6B2F9B46C54644246DE91F66C211D213611216A
	B0122E896FCE2643A4957FB0454F536119EEF3A81E1621EE8BGEB5FD3C7EDA1AD58C686A95E1B96ECA3BFBB4BEDE48154A5GB07DBE1E09BE31FF972CD100880025G31G6B8192F6F19D5F5EE31D63BA6A57691775351A3361ABCE4ABE215B6D178537C33CAA78DC4230A47A095226560C3C8DCF6B57C857378346B8AF3A41EB4AAE0F9B288B63A298131F35B9E75E0BEDFCA22D353946C598E05CCBB85DCB66CC78CA02DF3E62F58A322835E075D74DE89337052BC09188202E53D1DE490D68675261F7D9BA17
	551AAC444D037212F4AE179F1A774177A29837A85555F5389C4806601B62B67A8ADD1B5150BEFDB748F8376A2787398DEE7D372F13555B478CED90E17DEEE6CF1BF7875B606B14F6EE8678BE00418C3E0FCA0D0386F452F79B5B259EA403B648FB685A0BB49CA3E58EEBF00781AEA9284C64FDB69B67B78973DA8C30BC034BBCFDEFA13345B641AFF26B6B73016108CECD832C6D4EFD38370837C5375C216C3432DF513DF95FCE4DAEC7BC2A4DE4D3BC7A29F6B09C3F0A4F5F37176101065E4AF9FD892D97E45967
	9A8E486200A84BAE7728563F0272621F66827D4ADC6BF3F02EEBE10E9B813628F3FDF6DF264E3EFABC1E1B812F8DD70EE227BF207BF16A9E3B29F08F577FDF1C45F3D507DA7EA2ADEB7585E70DF4B7D7C03A337B66B31FE80CE0FBB09E63F29DBF23A4A93647709FD23C9D78DCG90234EFBA365C95F7EC1798B4F539F6898FAFEBE4DB118CDC4E506376927B30DED7A59CCE31B7E5B4C19ED76BB19C6B64B6A7EB193676819F06B56408FB2519FBABCEE37C897225A0530F3712FEDB88F175BA5DFF3B9C7C5F940
	F5C7840499EDB90F7A5C75202F04BD5CCFCE9C61FE52F975AB24721F2E991E27968973A2BB4C4CAE874D1A1FE4BC2D4BC2FB90A5F19C84556E8938E5067F8975678A5E1FC4F1CFDD9C00EFE159C0406130F9BCD21B60EB8B483247DDA15079D88ACF1CE8AA8CBA28D77803DBDFEF252DBBD1C77E32722B651504510F3D456877032F1ABBFDEFF06B907AA7957A3B4FFFC869775D1CCE16FD7AC55F0847B7AEBA4BEE8837850BB0A1DB2899D9F4644A8DB9F03A7CDE0FDF44212A38FFF667D27F8C72DE88304C4CFD
	56657DD84FFD1662185BBD1244CC826A7277223E8FB4E569F498EBE6B1DFAA54317A478C6B1DEAFDDE333EFEF2AF2B5756C9F33699EDD73C950070CAC427F9D11C6267066CFDC1F5D785A9201DC7AEE56F022F2473378C5F1AE3ECCB67F350475ED469DF4C59CE70DC41E8B25B0F91102657679A076D89B790A5F6FDDD56472EABE09E9867EE006F96G56B7229857E1F00038E09F76FF240E473569D0AE81685847E88C6EE372E06DAE5473F6F1D0CE5035CB4FE46D56E6E23BBF2A6D9A20EC8310B3D93B65CA3B
	5CACECD75548E3C12B0A6B2638B7C1983B1378F93B572C0F27070AA68C57B042D096236F4D8A0EC31EBC00F497817E09ACEEE75F4D516F0D2DFE3F47612247C0C5D7F22EFEFDB35CD4EE9AFCC7DCBEBF44A4CEF1126B7BDEAE720A67C4852D686CEB479BC9683937536912BDBEFE74E81F208B4766E90723EA9718CC3B6A7E2E3A44894263787EA6B50E4F627E6D31AAFD3CFEB77A5D48DDE1E4132048A4FD3FA69B72D3DB9A237DC315F64FFEEEB37D670D62B2C35E03B8E67DAF5FC259539C863A0BC90BFF8276
	063A3A8BBCBD4167954031F12F0B584FE37F67A7F01D44C2B98EA0810045A14F533D9969A7E75BCD8D59FCADC75E512FE5997051G6BG18DFAB2AC77A0AB3D3787BFCB66611E6753D045BA169EFD168C7F7441041BB12E15EE16DF3E26446ECC556B91A2C17EC3325C58330F9786B79BC86214D229CBE8FF72F51BCD667F01DAC4DAF22BEE096FFCEF85CD65A40633FF82011154B477977FA7DB8BD0017811664F2F9BD54D025496BBE1CBF377B3F4E41F522F9FDAE43573275B2DC1792E1A8C333E5FEE4F4AE13
	DDDC2EA6435C634B92ABG14DCC955B43F507B4530FEA17FG23D1F9A058AF3C1C07365783F87B816E97FE173BD76797AE03DD8B6ED1890F994A7969G4A88FC35D20EBE0063D2DFCD78B96ECA537D64966483BCC64CAAC03DAC0072B200D800B800ADAAEF5587E74D15F4CE4E10AB7949FD7115F5101FFD2F8430DF59C16333FBC3903E7FA04FB3D8A6517E1C870373953594AF9F8C4ED72C1C60769A81F51B5471D75685735D13B7FD4C31BC23B8732510FE1620724220FE4CFE93F24B09623B9C1EF10BEBF85C
	9238BDEE91064168418102931B367E884FA3471974FB7132BC362E5642F7C39E4F01465EC9253CC16C1E4775FE63906E19C379607FG9681C4832C83C8GB097F07DFF762051FEB2DD7F61B693AD5EE432192AFFB019FEBE24DF30CCAB8B2677E373FE2A174F3B2B006B75440CF95270BC544F5AAF3A40C82FFB86426CC9C1FA74CC6A75388AE8E17C3B893E5B8A389E0B9A783E15F5086B71554328479EA80F814C83D88A3092A09E009C66FA7C71417AB96A11F957D0AD32710A0A1007C220DB29523666E14E1E
	7B2FFE0F4BAD936E3B627370DC697139C6DDEE0EFB15629C9E9F8D44D8DEC02267019620B924524DAF889FB79EA9B00E9B4B8B0C63C6EB414C39101A02F0F1A5636F8D15BFAF0042B3BDC7C4C0B9CA45FD0762E2219C27625AA69037954A69055C8ECFCD6279BBD7ADB751B22EDDD67E5E440364A3105EDC5A31B17E75014618AD5741210036FDB1FB8F153B5FAF7A2901F5883220547DC25ECF8CE26FCB4238280C13C97E2BEE275226D376341FD457157D5EC163F52538C96E835293D315BEAD057C2C7747BE9A
	87C2F991C0142A03FF38E3A40F15D09F27136BA66577B6781A0BA0FEA98A15F9AD60FA02704A997FD0F81B7E681031CDFF66902EAF3FA760F30841F63B60D02EDA272D59EED99CC7B132B60F507E32609B95C36CFE896D4F6A42757F7221F0EB0369A826DE2F23AA553E3E5AEBA4CFBB48C55649E60172FBA9FCE38CE4C97DA1605783A4686A59584E865D583D1AFD3CD51EEBEC9F7D82F8A9E69BFFB349ED0394F35B48B321ED54C2399FE02C185B46D1C37FBF8F6A9783C49507724DE4772B4361757D9F070D75
	3D75F0F8FD3FFDF8F6FD27960657771E4259751DDBB8F30E6DE8911E21A65572B1DA1ED2E5AF36E628E3F43A1D4A0640626D79540F0ACADB06CBEEC6DCCEA25B2B19DE678750B7B84591A1A001AB07C3FD1FD739D2817C16E26C77F8006BAF361847AB5F1840F8F444A70C834B83DE51C1569E41362F84787EDA8B659EG89E0A1C074919E033CBB01B4E417CC4F5ED9A5788620231428BCFDA189FDCED0CE8304A853BD45ADF23B5F1B986E2CB088B42512D0DBE1E7D4DFBF3DD3ABE1E75494957F3F943FD807E7
	E75764FE9E53C5C1DD7ABBFC3F79B9BD6FF921BCCF45F5FD043805D0DE266206287D2F00F23C0A7B8645ED063279A84725CCA12E844A27D45C23A5086B0272100A0B27B8AF14A3D45C51CE1A8701F2140AFB391773972B62EAAFF17ED62B38B56D217CEDE98F652F200373972F62BEF3017332D9453537723DF91B0AF334F27E8ACB79DA0F1E8C565F29D2CDE7DD25617477B0759DC32541F71CDF22F839B4780E3348446FB897C25D66D24ED3E383170F2D0C639EEF6072312B38794DFCBEF2993761E7E95C24F3
	F916EBAE3753B381E60AB719325F5251BD505FB750F93D6F5275769E7841E25BD709BE36A5698F050DEDD71531792FD73EDB2B63A218BFA537D315BE66F23B292B9CF18BEF6B63FE6471D6A2BB3F337AEFCF2B47750F3ECB2B5FBC2D9EF97DD6123E3EF8C2DF0FBCDFD17A33F99DFF1F672BECE71EA7E5BFF604FDDF32281C4DE73972DDE6090BF8626E93CA6CEF2D554BE77978FB1126B0F492147EDB4B35581F692BAC116B30629857617ECA2E4316E35C7EA67B29CDC2B9C6454D273892A58F4AF0E97D8FFAE7
	0534796F18E346FBAE9B6348830F21BFA7063B3BE2B67B3BBB746C8B9BA17339EF1691352EB99173BA43026366284F93F0BB396F3FC57D59BA60BD7E9837FD7FA45E5B0FC130F1871C32A05900E23F476FE21B866B7750A4375154633C5F6AC934215B22CFF6B92677E2FDDE1B64F1D72D5AA7FF8AF9F6B9F81BAA6AA34720DED65BFC057AC33F087788221335DBD94247A7953C5DDBA5988F0D43FE09B19213C15EB1CD860D1DAC8FAF42A15A598DC3AAF339C2FDEB9650A984E07D48F16C476A856AD317C1DD8C
	G431D233EB29E4A1B54BE6BE99F6984563F572A0FC3030151D8C8FD66F2E83F72049A77D09975D840BFD9B86E17045BF604C53D5B25BEAF8A4A2BGB62978AFD3BC669E37D9D88E1F61CBCD4176077D6E478E238C5AEFD2F0EC0C1610B16E772E55A8A662F729CC46B1951ADE3ECFFDF921957CG40C2AB3DBBEA6ED05A8FDAC379657BFE12829234C1582F6067D9595E024062748FCE6098F1005FEC65717497CAD151D7059B2119F5C0767BBCF042122798165EAEA972F15CFE30F7CBBBEC5C77AEFE7AA9B298
	7BDE583B82F32512AF655F4238088CF5AEBF3220CD5A216EAE4093GABG5682EC2C64F3DBD70ABCE2968358EF15E541B1C6EFD1F8F2832F89B4B49E8C996D5F62D590FFC7D176CEB879G058B3027768D5F0035EC93C6D5DDDE23BEFDAB7011FC126B3222126B32722431AEF98E92216364F4DD8E1E54F449567F75BE5C97C3EEF8557AE5B5D89FF2C32B5A5E539896A9EDA3EC07616B96A78A5524FA3BC772784A2AAACFD5D0B8FD3ADCE97B54A4667E75E709817CDFBBCA34F7DE32F5DED6E97F77EC6D5B7AEC
	363EDE152F55559ADF1E12F9BA3EF87F6EF1E1D4C40FD706618AA6F44A4AAD70733248A22032E22B688CD8EF7A83D6074BEFD5DE2BDA55572AD66D352A5505347CD63CFE39AE78FC1E096BAE176B3A62D5AD232E116272F2464B4BA5280791C172EBF1C067990D577A5E497F8FDE959E99BFCDB59ABFAB5B6722D356EF24D66BE73F70E71A87DE9F054C0379085F7B746733EDDB7E5270FCE63BBD9069D4565202135A392A23F7E99A1B575BE735F975357C19ECE97A3C8CED69279F635EAE89D31EG4440E5D5B4
	0679186F3131B017B800EDG66AA65AE1936A1261976E1C6FB4DB1FCAF2046234A62E7F54F6465CC2B73096200436711A49A2832B652FE74DD7C65854BC3C734CC3B5B2FE277899B613B350A1F97126BB5D9CFB64F4576D8FC302619476B66EA3B69B06DCF6899017FE37463A9FDE69F79FEEE9F8967230973446D48A386760150D54D643724DA330FCA308F665B07CFA0CE4DE9306DCD717B2E93F87E43B7BE90513B6E50A443GB4F40F32BD622D862CDF87F493G4C276C2692005AD3FC6F59FA2218503D47E0
	5F69F40E0A5C8172717E4592BEA7741AE57637273F3250B3318734B60E74B97EB7965C2FE50F17A2996EBFADA8C71FEBF4CCE6CD957CB68B4ADD92C7A81A49FD28C953EBDA655C757AC1A5572B6D1411DEB7B7B37A2770AFB014BDBD035A59C2E89F853096E0A340D6G73E9AE572F56E2A0906E3C52C35794152DCFF039E596D37F689EFFE3D782F406CE733C50C9BB5E132879C01CD0DD5DF91F4B59A60CB2CAECAE8789BF1FAFBE4DF302DD043EF50472C600ED2779DB08BBC95CB63715FF13C8D76F997A2E7C
	DD6E482CB34CA6EB4FE8369993EFCBE3F4DEBA2337F12C8B97234ACA7FA5BABA7C3C1AC4C6BB8B08E14ED33772F9DEBC36053F3FD8FF064F7317E7F01E669A3B298C2025061F4157B56B4FFF030017EAE6BE03B39D6FE843F350BCE83B94E0A540FA00840064DA3BA99720822085E090408B90813084A09AA096E09D4046DAEE8FC58D65B3580322CC4DA0D4DB7FFDC378751658B87BFA5BC55F880930CEECD3DEA5C7EAEE2477DAA3A3FED1DD577B9B4DAA2E5B4FE3C8A64361CA3D8C37421C524F4EACC3C6B327
	A5BC6F07DAE6673D344558D71CE8897595D5AD463E2236C56FAB58FC565FFB303F01F87D817B9DA6775B0F655EE2695AADBC2B1D29997E3B94DF2143B37FF109F07FD18BF5CB4F72FB56A75B695F6E5459CD79G27GFAGA40085F55CBE5734D412D96ED8ED9EEF8FA8D352DDC1B27AFF68C07ACB01568A00F800ADG2975F6D3E1BD273F3F35FA867B3F6C7150ACB4A624417CC51FEEA5A877DBADB34970D4FD383C6687B5A8132E7A60B7952394BFD89F7C26624DB37CCD4598542D2D677900DF507D3F320135
	E738840A6BD2F0EC1EF9ED61ED3E38EDF61BBF5EE6EC73A75BC2ED7ECC1B314DB73469EDDEF9CB5906FEF6947C4C205A6E1C0AFBD745DD24B491F74D651447D47C7FD03E905FA562009CFF0D72B5C1F1038AEEC807FBD74189ED68F3E04AE39E9F6FBB5206F9289117A431728D5A46658697244A43C3F9F4F9859D5F320AFBD75545D4E528FEBE2E546B0759518B3892145FEBF43FED64133C211F1B9F987F130E213C2FEA8FEFFF4F349B5F59BD57EE7C6E606F5BE73ECB7BFEFB78776AAF35073ED75F5E991E37
	5D1D332F0D6CCE6335115F993AB60ABA0D57C6E927FEEDB0BBD95F42F3FD4E86FEEEF05420EDAED74B63343CCEAD7F768C57A536795358CBC68D9FE32E6F966E875839CBB80A7ECE099F6812E7787C495D9A1EAFF942F2C4EF36709C51E603396B73C388490DF0DEEAE4B9A2ACD7B4866708BABE44312645FB043FDBFF3CC6FFC7D25B48F22CC30D7CDDD1E1ABBF3FFB9B0D4F6F614F740B4E91D58EB1B60CB90D5EE4A0B7EC2CFF2E61674E28C6EE876F346A77258D005F52B8173F59F925E6267DAC35A9148633
	550656D0BF575A2A77F38C771B9AEE5379C00B5DA3275672783885F0B6G99E0B14072A6AECFCBAD0FA1621A78BD58D0ED70BD58A3C4FF0FE52B8F3E87B3CD2B7FDCFD70BD58DFCD2BDFD19FFC8F467BB3D9D5575E6F79097D5DFC33B58708C53FC1D2E4120666D5C86DCBA649181C727B489EB5BE6C261B4E065B55F5C9BFFFB2A911A4F7136D4C51F413E474965DA405BA13EE12840EE652CB027E81D14C2878EF110FC9E2E8E5E25FC598AFE3FABCEAFAAAE73E79616AC0A259FE0EEC9F91A46F18G63504731
	BDA4516117C9DE8FF631C2972B833BD8E947812F60906B5FA7BBC7FC234334B90C707E39770188ADA193BBFBC81ABCC6CFD460D5E51223D16A47F315D2D14F2672765A35EFFB619FB0F944688D2785DFD8B855B454FB5F312F98FBF1FFF1A2C969A63BC6DC22649C2009289EE226258E516F70393C98C348E49F9BDA925C23D6161B286FA1FBE9BB1E40C27248756E9E1229A3F74EA509EE2DAAC3D7F565F20FCCB298E537A8DB214C66E21249950BE5280D654006C4E7E06826BFB064F07907143C58101A979B52
	72E2C3ACAFB694BE47AA130B1FE2469627E476D2FBC9E7DC77FEEADCB3EE5BBF051CAD321749385CB14DCEBEA53EC3332B8F42F7A895E4AFE79A171F44A64267CA9F44A642D3C3F68F0613B6FA15E4BD5CD73B902C12230C6D543F8D48F73A433E1D086FD07EB63AA3786F827E2E8A770C0ACE58AFBAF93C373B6A8489FF9FB5B08E5ED896241B2DEEA75ECD35CD4922F62EFC218A697BF99B2512553DD805FC5F507579BFD0CB8788DB3120D16B97GG00C5GGD0CB818294G94G88G88GA23B4CA8DB3120
	D16B97GG00C5GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGA598GGGG
**end of data**/
}
private Set getCallersFor(SootMethod newSm) {
	SootMethod sm = (SootMethod) newOldSmTable.get(newSm);
	Set callers = new HashSet();
	MethodInfo methodInfo = (MethodInfo) sootMethodInfoMap.get(sm);
	ca.mcgill.sable.util.Set whoCallme = methodInfo.whoCallMe;
	if (whoCallme == null)
		return callers;
	for (Iterator callerIt = whoCallme.iterator(); callerIt.hasNext();) {
		CallSite callSite = (CallSite) callerIt.next();
		SootMethod callSm = callSite.callerSootMethod;
		MethodInfo callSmInfo = (MethodInfo) sootMethodInfoMap.get(callSm);
		SootMethod newCallSm = getNewSootMethod(callSmInfo.sootClass, callSm);
		callers.add(newCallSm);
		newOldSmTable.put(newCallSm, callSm);
	}
	return callers;
}
/**
 * Return the CallGraphDialogContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getCallGraphDialogContentPane() {
	if (ivjCallGraphDialogContentPane == null) {
		try {
			ivjCallGraphDialogContentPane = new javax.swing.JPanel();
			ivjCallGraphDialogContentPane.setName("CallGraphDialogContentPane");
			ivjCallGraphDialogContentPane.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsCallGraphToolBar = new java.awt.GridBagConstraints();
			constraintsCallGraphToolBar.gridx = 0; constraintsCallGraphToolBar.gridy = 0;
			constraintsCallGraphToolBar.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsCallGraphToolBar.weightx = 1.0;
			constraintsCallGraphToolBar.insets = new java.awt.Insets(4, 4, 4, 4);
			getCallGraphDialogContentPane().add(getCallGraphToolBar(), constraintsCallGraphToolBar);

			java.awt.GridBagConstraints constraintsCallGraphTreeScrollPane = new java.awt.GridBagConstraints();
			constraintsCallGraphTreeScrollPane.gridx = 0; constraintsCallGraphTreeScrollPane.gridy = 1;
			constraintsCallGraphTreeScrollPane.fill = java.awt.GridBagConstraints.BOTH;
			constraintsCallGraphTreeScrollPane.weightx = 1.0;
			constraintsCallGraphTreeScrollPane.weighty = 1.0;
			constraintsCallGraphTreeScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
			getCallGraphDialogContentPane().add(getCallGraphTreeScrollPane(), constraintsCallGraphTreeScrollPane);

			java.awt.GridBagConstraints constraintsOkButton = new java.awt.GridBagConstraints();
			constraintsOkButton.gridx = 0; constraintsOkButton.gridy = 2;
			constraintsOkButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsOkButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getCallGraphDialogContentPane().add(getOkButton(), constraintsOkButton);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCallGraphDialogContentPane;
}
/**
 * Return the CallGraphToolBar property value.
 * @return javax.swing.JToolBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JToolBar getCallGraphToolBar() {
	if (ivjCallGraphToolBar == null) {
		try {
			ivjCallGraphToolBar = new javax.swing.JToolBar();
			ivjCallGraphToolBar.setName("CallGraphToolBar");
			ivjCallGraphToolBar.setBackground(new java.awt.Color(204,204,255));
			ivjCallGraphToolBar.add(getSuccToolBarButton());
			getCallGraphToolBar().add(getPredToolBarButton(), getPredToolBarButton().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCallGraphToolBar;
}
/**
 * Return the CallGraphTree property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTree getCallGraphTree() {
	if (ivjCallGraphTree == null) {
		try {
			ivjCallGraphTree = new javax.swing.JTree();
			ivjCallGraphTree.setName("CallGraphTree");
			ivjCallGraphTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			getCallGraphTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
			getCallGraphTreeScrollPane().validate();
			getCallGraphTreeScrollPane().repaint();
			ivjCallGraphTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(Color.black);
					return this;
				}
			}
			.setAngledColor());
			ivjCallGraphTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			ivjCallGraphTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					setForeground(Color.black);
					setIcon(null);
					return this;
				}
			});
			//((DefaultTreeCellRenderer) ivjCallGraphTree.getCellRenderer()).setBackgroundNonSelectionColor(new Color(204, 204, 204));
			ivjCallGraphTree.putClientProperty("JTree.lineStyle", "Angled");

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCallGraphTree;
}
/**
 * Return the CallGraphTreeScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getCallGraphTreeScrollPane() {
	if (ivjCallGraphTreeScrollPane == null) {
		try {
			ivjCallGraphTreeScrollPane = new javax.swing.JScrollPane();
			ivjCallGraphTreeScrollPane.setName("CallGraphTreeScrollPane");
			getCallGraphTreeScrollPane().setViewportView(getCallGraphTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCallGraphTreeScrollPane;
}
private Set getCallSitesFor(SootMethod newSm) {
	SootMethod sm = (SootMethod) newOldSmTable.get(newSm);
	Set callSites = new HashSet();
	MethodInfo mdInfo = (MethodInfo) sootMethodInfoMap.get(sm);
	Map callSiteMap = mdInfo.indexMaps.getCallSiteMap();
	if (callSiteMap.isEmpty())
		return callSites;
	for (Iterator siteIt = callSiteMap.keySet().iterator(); siteIt.hasNext();) {
		CallSite callSite = (CallSite) siteIt.next();
		SootMethod sootMethod = (SootMethod) callSiteMap.get(callSite);
		MethodInfo smdInfo = (MethodInfo) sootMethodInfoMap.get(sootMethod);
		SootMethod newSootMethod = getNewSootMethod(smdInfo.sootClass, sootMethod);
		newOldSmTable.put(newSootMethod, sootMethod);
		callSites.add(newSootMethod);
	}
	return callSites;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-4 15:02:17)
 * @return ca.mcgill.sable.soot.SootMethod
 * @param sc ca.mcgill.sable.soot.SootClass
 * @param sm ca.mcgill.sable.soot.SootMethod
 */
private SootMethod getNewSootMethod(SootClass sc, SootMethod sm) {
	SootClass newSc = scm.getClass(sc.getName());
	SootMethod newSm = newSc.getMethod(sm.getName());
	return newSm;
}
/**
 * Return the OkButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOkButton() {
	if (ivjOkButton == null) {
		try {
			ivjOkButton = new javax.swing.JButton();
			ivjOkButton.setName("OkButton");
			ivjOkButton.setText("OK");
			ivjOkButton.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOkButton;
}
/**
 * Return the PredToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getPredToolBarButton() {
	if (ivjPredToolBarButton == null) {
		try {
			ivjPredToolBarButton = new javax.swing.JButton();
			ivjPredToolBarButton.setName("PredToolBarButton");
			ivjPredToolBarButton.setText("pred");
			ivjPredToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjPredToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjPredToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjPredToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/pred_arrow_iii.gif")));
			ivjPredToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjPredToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/pred_arrow_iii_pressed.gif")));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPredToolBarButton;
}
/**
 * Return the SuccToolBarButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSuccToolBarButton() {
	if (ivjSuccToolBarButton == null) {
		try {
			ivjSuccToolBarButton = new javax.swing.JButton();
			ivjSuccToolBarButton.setName("SuccToolBarButton");
			ivjSuccToolBarButton.setText("succ");
			ivjSuccToolBarButton.setBackground(new java.awt.Color(204,204,255));
			ivjSuccToolBarButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjSuccToolBarButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjSuccToolBarButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/succ_arrow_iii_pressed.gif")));
			ivjSuccToolBarButton.setSelected(false);
			ivjSuccToolBarButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			ivjSuccToolBarButton.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/pdgslicer/dependency/images/succ_arrow_iii.gif")));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSuccToolBarButton;
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
	getSuccToolBarButton().addActionListener(ivjEventHandler);
	getPredToolBarButton().addActionListener(ivjEventHandler);
	getOkButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("CallGraphDialog");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(475, 312);
		setTitle("Call Graph");
		setContentPane(getCallGraphDialogContentPane());
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
		CallGraphDialog aCallGraphDialog;
		aCallGraphDialog = new CallGraphDialog();
		aCallGraphDialog.setModal(true);
		aCallGraphDialog.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aCallGraphDialog.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JDialog");
		exception.printStackTrace(System.out);
	}
}
/**
 * Comment
 */
public void predToolBarButton_ActionEvents() {
			Icon temp = getPredToolBarButton().getPressedIcon();
	getPredToolBarButton().setPressedIcon(getPredToolBarButton().getIcon());
	getPredToolBarButton().setIcon(temp);
	predCallGraph = !predCallGraph;
	if (predCallGraph) {
		//set successor up
		if (succCallGraph)
			succToolBarButton_ActionEvents();
		buildCallGraphTree();
	} else
		
		//set successor down
		if (!succCallGraph)
			succToolBarButton_ActionEvents();
	return;
}
/**
 * Comment
 */
public void succToolBarButton_ActionEvents() {
		Icon temp = getSuccToolBarButton().getPressedIcon();
	getSuccToolBarButton().setPressedIcon(getSuccToolBarButton().getIcon());
	getSuccToolBarButton().setIcon(temp);
	succCallGraph = !succCallGraph;
	if (succCallGraph) {
		//set successor up
		if (predCallGraph)
			predToolBarButton_ActionEvents();
		buildCallGraphTree();
	} else
		
		//set successor down
		if (!predCallGraph)
			predToolBarButton_ActionEvents();
	return;
}
}
