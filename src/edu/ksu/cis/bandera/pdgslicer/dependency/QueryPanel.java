package edu.ksu.cis.bandera.pdgslicer.dependency;

import javax.swing.*;
import javax.swing.tree.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.pdgslicer.*;
import ca.mcgill.sable.util.Set;
import ca.mcgill.sable.util.ArraySet;
import ca.mcgill.sable.util.Iterator;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Hashtable;
import edu.ksu.cis.bandera.util.Preferences;
/**
 * Insert the type's description here.
 * Creation date: (00-12-1 15:47:32)
 * @author: 
 */
public class QueryPanel extends JPanel implements java.awt.event.ActionListener, java.awt.event.MouseListener, javax.swing.event.TreeSelectionListener {
	private boolean setQueryIndexByProgram = false;
	private Dependencies dependFrame = null;
	DefaultMutableTreeNode currentQueryResultTreeRoot = null;
	private String queries[] = {"none", "Slice trace from current stmt", "All conditionals in the slice", "All conditionals influence current node"};
	static final int NO_QUERY = 0;
	static final int SLICE_TRACE = 1;
	static final int ALL_CONDITIONALS = 2;
	static final int CONDITIONALS_TO_CURRENT_STMT = 3;
	private JComboBox ivjQueryKindComboBox = null;
	private JLabel ivjQueryKindLabel = null;
	private JScrollPane ivjQueryResultScrollPane = null;
	private JTree ivjQueryResultTree = null;
/**
 * QueryPanel constructor comment.
 */
public QueryPanel() {
	super();
	initialize();
}
/**
 * QueryPanel constructor comment.
 * @param layout java.awt.LayoutManager
 */
public QueryPanel(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * QueryPanel constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public QueryPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}
/**
 * QueryPanel constructor comment.
 * @param isDoubleBuffered boolean
 */
public QueryPanel(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
/**
 * Method to handle events for the ActionListener interface.
 * @param e java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void actionPerformed(java.awt.event.ActionEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getQueryKindComboBox()) 
		connEtoC1();
	// user code begin {2}
	// user code end
}
/**
 * connEtoC1:  (QueryKindComboBox.action. --> QueryPanel.queryKindComboBox_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1() {
	try {
		// user code begin {1}
		if (!setQueryIndexByProgram)
		// user code end
		this.queryKindComboBox_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (QueryResultTree.treeSelection. --> QueryPanel.queryResultTree_TreeSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.queryResultTree_TreeSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (QueryResultTree.mouse.mouseClicked(java.awt.event.MouseEvent) --> QueryPanel.queryResultTree_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.queryResultTree_MouseClicked(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-12 22:06:25)
 * @return javax.swing.tree.DefaultMutableTreeNode
 * @param root javax.swing.tree.DefaultMutableTreeNode
 * @param sliceTraceNode edu.ksu.cis.bandera.pdgslicer.SliceTraceNode
 */
private DefaultMutableTreeNode cuttingTreeWRT(DefaultMutableTreeNode root, SliceTraceNode sliceTraceNode) {

	if (!sliceTraceContains(root, sliceTraceNode))
		return null;
	if (traceRootIs(root, sliceTraceNode))
		return root;
	Set workSetOfNextLevel;
	Set workSetOfCurrentLevel = new ArraySet();
	workSetOfCurrentLevel.add(root);
	do {
		workSetOfNextLevel = new ArraySet();
		for (Iterator nodeIt = workSetOfCurrentLevel.iterator(); nodeIt.hasNext();) {
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) nodeIt.next();
			Vector removableChildren = new Vector();
			for (int i = 0; i < currentNode.getChildCount(); i++) {
				DefaultMutableTreeNode oneChild = (DefaultMutableTreeNode) currentNode.getChildAt(i);
				if (sliceTraceContains(oneChild, sliceTraceNode)) {
					if (!traceRootIs(oneChild, sliceTraceNode))
						workSetOfNextLevel.add(oneChild);
				} else
					removableChildren.addElement(oneChild);
			}
			for (Enumeration enumVar = removableChildren.elements(); enumVar.hasMoreElements();) {
				DefaultMutableTreeNode removableNode = (DefaultMutableTreeNode) enumVar.nextElement();
				currentNode.remove(removableNode);
			}
		}
		workSetOfCurrentLevel = workSetOfNextLevel;
	} while (!workSetOfNextLevel.isEmpty());
	return root;
	
}
private DefaultMutableTreeNode getBackwardSliceTraceFrom(SliceTraceNode stn) {
	DefaultMutableTreeNode pathTreeRoot = new DefaultMutableTreeNode(stn);
	Set visitedTraceNode = new ArraySet();
	visitedTraceNode.add(stn);
	Hashtable traceNodeForCurrentLevel = new Hashtable();
	traceNodeForCurrentLevel.put(stn, pathTreeRoot);
	Hashtable traceNodeForNextLevel;
	do {
		traceNodeForNextLevel = new Hashtable();
		for (java.util.Iterator nodeIt = traceNodeForCurrentLevel.keySet().iterator(); nodeIt.hasNext();) {
			SliceTraceNode sNode = (SliceTraceNode) nodeIt.next();
			DefaultMutableTreeNode currentRoot = (DefaultMutableTreeNode) traceNodeForCurrentLevel.get(sNode);
			for (java.util.Iterator childIt = sNode.children.keySet().iterator(); childIt.hasNext();) {
				SliceTraceNode child = (SliceTraceNode) childIt.next();
				if (!visitedTraceNode.contains(child)) {
					if (child != Slicer.sliceTraceRoot && !child.stmtAnnotation.toString().equals("")) {
						DefaultMutableTreeNode treeChild = new DefaultMutableTreeNode(child);
						currentRoot.add(treeChild);
						visitedTraceNode.add(child);
						traceNodeForNextLevel.put(child, treeChild);
					}
				}
			}
		}
		traceNodeForCurrentLevel = traceNodeForNextLevel;
	} while (!traceNodeForNextLevel.isEmpty());
	return pathTreeRoot;
}
/**
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88G3BF908A9GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DA8FD4D45719A70DFB16BD753836EBCFE9EAF34C091E35A91B12C40DFF2812C892CD4092C392CC0C51D64F3A3BA6651CE4CF6DD952DC86B0E8082212C8933B1936AAEA90D0D04781790F0AC0A4B2EB30268D0F19870C3C19B73EF98838F63B5FF77F3CF9B33C81358967FC3CF73F7B5DFB3F7B5D5F7D6E775DB724686399F95936A2C23273887FBB19CDC862D1C20A53EABF943C520454ECE27A7B9D50
	CE7216128B4F9A28ABDC87C49E7AF3BE14BB211C78EAEA769EF85FCD667D39279CDE220C63003A06C0523F0FBFCE30CC0C738EAD6F779540F3BB108BF8A3C0492883B96DAB667C69D05E4E5F1132ED045CFC88E5DE2BA9B671739FE2F21D7074G599FC619EE5FDEDE1F8CBC14D98D1449E573C1E6AB556E5A858717CB4C1FCD74E317ED6F61D8267E858DBC4C5ACE664FC250888D11C336B9CF615CBA877BEBE54FD059E0F0284C6D8D1675CAFE0F2CC9E581CFFFD0713AE52D4CA387E460795D23CE67E7C332B6
	5AA979E5855AE6D11DF71F4E277D014ED00EFD84F95FBC0DF62A0777EE2040A3C26F57BBEA38DEBD2F4DA595EBFA236A1D728853B77311105E76DFAF491DB1877B3F0B2E83362B0072G9053E539CF6832FE0E506544A9145DBBB7B53B8A28FD2E603FC5799EA85F841AB6D7687833BA2123B230003CD95EEF5B91C5477839CC377439A19DB75FF31F6DB794ABEF1763FA974E03F1014E81A9C013016201BE2298DDD8F6981E8D3DFA136A73297E06C020D171E89D2AC7D6605D69049A4E3BA83BD54DC38843E171
	69D28BFD84A54F8B47A0921B5B67C460A64E76A0097DD823D8081C1B2B6F2CAD037731E9DEE8EE7B9EC53B3DCD5736G5E319D6A43786F56A2FF4044E7EB75ADAA0F659828DB7F2830733403C21702EF4FA4717D97ED6FD8685273145FF6EEBE5321E7FEC897E663D5C751461301BF9BA889A883E8B3D07182E1635C724A09EC5C6A57E54DAFA9DDC33D81C9539B073C8A6C81EE6B3F770DE7EBABE26D3EF35B6D18DDE6D71A57A8FF51A5CB3B44A4765A2A96B0FB34AF8859050D3DA26A584A02315858EE2C6183
	F4CF44ACE0EB18E37067D07ED4931F2DED8D6553B25465AECC4DE672571C948B8BA3B172974A0FD99809119C2740C89C54E58131759D2EC4FF58338876A850B420F82094200DC0D90B449ADFA8AF3F45FD54E9DADF56B63F6283F84AA33AA6357A033A64F74B17A44DAB75AAF210F0191D9528DBEFD83B8FAA90FBEE98632C6C870F29FBD5BF74AE79E4GD31049FCDC3193E5B419563A3CBE9986708508683724E2B72E0994547FA76011F4B9A4416A4BAB90935725CB50A3F400EB1D0876AA27BEF7AF3CBB9205
	2DE6D6A1EFAA14E7A78A3BBCD0318B1EEDGEED9F1BA1B3DEED4C352C685C673681ECD8479621F000DFFE27869EE01414D9B9EA499733AACB108D434186129F4F1A486F7523EDD40570126AD96E768F7CFF75F663EC05DD0C7546F5676DE42ED0F1147F17C4EAD48B2FB6FBA01731B8373CA834ADCACEC3E21424E3058A885E5013E8B9AGC77604D600497E6CA81EAD72F5592FB773D3D68F72357F3723DBC3386CD38D4C7C027251CFB581F002867FBF0F6EE4FC68430CF2D17F962D17F4DD73768E69F2172C6B
	DEFFFF28FD5EB154A598668203FC2EC5A7F12E79CBA0CE812AD8A266BA6558EE933E5AD5F5F0A860421D43F13A011E474DA07F4592317E8927B0260AB14A8BE9B9D45FF86AF6574E0A307F68783872150C11B3AE86071DA2C6496178E27C3BA9BF8E74CB825AE84CFB2B33BBC27EEF55EF2CCE3F27E3187579AA47E018382BB2BA26E3AB2DB1BD25529A5353AA47476C74CAAB4C323A7BABF10E6A305F3487E6D722BFF42BFE3FCCB72251046F7F57DDB88F2F5F2BFF6E75744BFA1777068C39C292E2798C753905
	703EAFC9784961BF88BF693974C3D27F17A64BF8EA46CE45F6B30961F1EAD248CFB21DFED805F81095598706EAD2077CBA637F187AF34E8F66D0DE57602770349F24E1F81B576F01732ED7EDD4C7CAA5BA1FB27B11A33576D010DEFAADD2704B86AA58024B93BCF86852218A06075D64754F3BC3FDDF1403C30A6E52E439D407FFDD2046B88304243FF41925C3239CE5E37CCA7E266598BEF5A8A817517FCD10F48C4A1E03D7256BD219B42C17D1A415F5E09D6D6F50C4AAF4183BE90F5ACFB528F50C7B37338EDC
	0384D81BE42055C9423FFDF29C6B05FF63CE3CC9D5787C204E7598E223393648345EEFA6315C309E6AD87D931675530D7AB3F5D67521BDD5FC2290F1AE43D4A1BCC1875BA60F0A9863442E083ACB12B2940ADDCACE3C8BCF45641B997F43135638331FC4FFBCE8743F6F649602B1886B136D1384897B8CE8DE1F042B2069BC474D7219F35C84189B664369705C88445AB66F5986CFF3A2F973F1ECBB4FA9F2DF8265E2A047632CFD7B634C96CCCE2D96F24B203C9608ACE5F2FB171A658E5788394E25CCEE004B4D
	64F21B16225C1F0C7C355C606914D7828E32AED744659D05661CDBF93657F22FA3D5A4337E1B13A373151B2730DF8F7075E40131FBCF1A4F5006E0D0F5FBE93848572943E176830CF75101B66C73EAC148DDBC7208D86BEE876A0A71A4E773757A51A72749587838456355D5CD04A8CD43F4DB3854A688E94F81DC1A8CB7F949AB8F93116F7FEF2D30D7EC327003779CB36775133FE8313D9B45B6C95CA6EBCD36297A7EFDB656F71651377D890117B78EA12668BD856E65113260B01C81CE67F90A9E562E4F1F4F
	675A7D04486F3FFD0D66ACD01E8D148444F20DD769790B329940ABFCD26C4175D566BDF86EC99633846049FC5719EABCAF391F65F65DF3846F09A6F432C4E008F456737E631B93484061EE4B3B05555E4FEC131EE47609FFB2E42714057759568231F944D50BF9EC86190A2744BCCE171867B1G7C49C089CF09F93CDD0D7CF02FAEF0F23E7000184F04BEDD64EEDDB5C20FE498A34B98E7E14D91F85E16E7B7703AE158E921472DBBF6633E63D8581EFDABA66A1E2DDA466CFCEED9641E6D2926FB9678A34B441E
	FD4E315334E7AF02E5A4FFBFF8D6A386BA05B6G9F4A4BFFF78A4725BE1408D8AC2676F66E87A6AF93F95E8FEAF0FDE7C0B991A89DE893D041F221DB01F3427B0E1611F16EBB326BEEC72F5265A2FE9D74C7362BDF9E0A41DD4B4746636CCEE00BC66F8A16C75E956C227C186511F785F1570C3B82284BB2465F600C9C3F676970B1EFC614D93BB74634AB0BA84F08E847B05A2F203DAD7C1270825CDFFEF4DC783479CF0BB5FC63B41ECD19D0AEF88662C920FAA08FD08C504CE744DADEF1DA796D70350C6634C3
	799F1B673D55F7E61F78B16DA26D137C0C15FD92DCFB2C6D03FBD95C09B71B6305554FB01F1B1F82E7F90A385355CB04EF2FCA91EBFD39664E6652BF265DFFC714151470F6EC3DAA6BD007C9D0B7ADC52C4DFCA75EDD8B9F5029FA7DBA917BDF2E4618C9553C7DDE3F60DD25BC372A51040465475F25675DCC68B34968F7D6CD747CA93E46BAFFFA3846BAFF1ADFB37E1DC0D2CD347C0A69D7DCA774CB87E23CEF5033F6A32E0F417BB200BCFBAA44EC29E20D8A687E7D824AA3C0F1863F187293D3D90C07E79347
	6735BB494931FFFC0E70E7DC6AF8F97F23F7B406487373CCFDD9497D164AAE7FE08735F9B0A8876966EC16F409577D2B7EBE3148BFBF15FCB291F9585E6A2DDC26C5D71B0E9A3A5B0F0F6717D7DB4C1B5F7B1C0E0E275CDAEBBCED2BC5EC8755A14DADC7E2E6E7ED31D19772B62D3A6C43E9705C023657A58DF26848763F236DD9DD347607EB236112596879B09B9DA306CFEEC1BBDB781CAE3786D99BC60CC444317BDB04DF4ECC95B1E36C8144664195600F013AD788EC0E16C433FDGE4A62D98EBFB2667549A
	331E15068D9CF5F8260D98659ADA9EB566A1B7ECB7EC546277F06742F21387294FB88B0255C4704A69BEBD843CDF9AE3FF4F0F7EC611C655A1E81B7BD314CB708BFF9B87BAA7GE5GEDDA417CAE9B7B21E10C5D7BB44987266B8A48EE3216363D9E90ED5B215C839483B483A86927A266DB5A0775466DC43B540B799D2B5B31EF27394EB8A5C57DDD556F0BFA9673052D990BED5BB4F3ECFBB00D695D1EA67C504963428F75240935BB5CB25E3EC9C80B9617E4D1ACA427C5FE8B59D2027C743448EFA1D38E08EF
	A16B216E5C33C22729747C0A07F232414BF188BDB31E95F9C906DF5C97E6812FEA25D80F23BDFB4D1846105769BC2BF9BD0DD2FFBBDDDD06E73B1A4F5BABAB851EBFD5314F19D0CE84CADFA972359BB9A2DF4BBAB40F5C5CB2E8799D36F451E55B2615BC9EFEAE94972F3FC764EB5383E24CF6281F749C721E8E1857886D51E4E3B1832B4F8A2BC7CCE05C9F2AAF3BE62EC7FD7FA347DC5F93D61FE7EA4F66764995B1B757090544F17705685F18791CB279B3E6DEAB2949DC164B734E3EA8F9A7C5B51B7FB7B41C
	BF626CE0BA6419695466471BF97FBD6956FEC16086C98719D8207779DC2627B3DCD11BD22CB434882CE4268BCC5BCA700E267EF9632C25E53C63E065E9873E0A7BEDAB8A0D79AFDF5B9842B63B49BF1760BDC72F649E6C5754A13F0710E94F23CD1650BD75853C8FGB17F7421C378278C60ED82AAF8A1B53B94287D8521731AB1B94AC4445AED3A43F67B555BED47FC0CBD007BD6F9A1048546BFC479A3A6BE739DAED5780EC9D037718511E73CDE0FB6E9FF916C8494839487B49FA87DC591EFBED6D7C1A648B1
	DCEA209D82884594C232795D973833EF5C6F38449E1D7663D9E4C0B993753EE8630B7C37AF996CD9B03B1572597CA6B7627C9CD067829A811A8AB493A8B1C34CEFEBFDA91B9FDEF7B5683A649E20B7E9E2F2F83995E26301473F7BEBD6F3CB63FA2C0767660C707D4DFC42DF0F0BFC25E0D5EA7647D487501F083F4C3A31BCC21AD7616F0F0ADB09E1233DC1E123CDE763097E0AE47BC3949B452EE2FAE42C8A79687941B9B6660F3ADB04BFAAFCE92CBFB2FFEF22E79C48B8DEE27E884B6DAF0530467664FF1D45
	F8B262861F6B7F40F9E248EDBE0973744B43F6632626C9D5D46D60310A2AD2CE0778DC579173BDCDE162E854FB1AEEBE8705BF539735F23B57BB44D9B473A5F196799D11E751370879AC196108BC0B32436AD7BAA24F229F0455671E0ABC0BC4FB06218381E23C171B5E7F28FEDD3EB364FB02DF6B98ECEF7D32BEFAEE716BFA6B5CA23741BAD75D56B07E373EDD8D513F757D26417C2D0F7906475AF0EDD68396B6836DFDB9B53B822879E561979EEE78BD21FE4142A734F87AB13741DF0368E27C6FB4C61F6B74
	4609FFC3B22B111EE52A2E2BBE6CDF70FF54086B252B814AE43C04C63411666D9F5019A8674FEBC40CABF29FE5337860DE9A0374403C82AF0B78B2F1D4448CB78D5EC7C548CB01727A1745F9B8A9AC275C8C7C0255637F46026D57F207F93FA67F760CCD7808C8F9BB6F2FF4F5643D2A27837DD28F70EF824DG1A8F1482349EE8B3D041AB2959F6A087D0A71087E884E8B250F420842064D744FA7E29956340CB525531DFB7004983E63A281A446EEA185F780796612FB3DE39D5FF1DD9477C5D0D8B4440DBC9A7
	7DBE3C86E20EB5C227DF1C7FE49C1DBAB5B5A0EB7AA863522FF6FC6F36B8EE77DCE563ADEF0B0E51343609B13A2A0DFEEB82EC39C683BC97DF53C66F327A7A02323197FE5ED6E870DA031DEA500BB9BC737B73C7B09655E1A0E6235E4F50C66CACFAEED41CC56DEB2C4FA2F3EC9CEB0A0D115F33A672ACBAD6C43F03291A9C9E9F1E27D8D7C0FEE40D407F7769399C477BE03C99141740F9EC9DF7DCA406BD075A77C7DBBF70926761DC3B50FBD5F66BAE295FE8B35291FD8D7E5AB1719A44F6D87B09A99DE37D44
	BFF5D87B096FF5187D847F4E5801986F07FD50ED485DEF70AE9B3C5934CF64FD6E756883867FC12A97724F49B820604F21FA8DD3DEB767ADB071AEF35E628E4491CCF9C055C45B259D0815BE2F223072D3D4466B87D89976C821BAFA8312C96FE78D9E6A4D764C1430BCBF8556F42D8546466E1952CEFCB74E9D368DF9DDE5E791E13F8771CA0A77867DAC5B85027EFE66BBF2EF885F71A60C45703E2F8B71435EABDD44406A53747BE2F8ACD4562BC0EE43E2AF095E9F7031286999FF6F48FBA13EB0BD476F2C16
	3118FE20450CE97E5B229BE65B143E1A1ADD7F6A7836E1FD59EF0C6DFF7F8DF37F6C3776D787DB20907F9A498BEAE407B147D63A10EB2FD7BAF3FEFEFF0E0D643612AD8C582DA4975159CA72A8F8DBC98EGFB84A324DCB3C1EF5B43AD16BDEB4F5D057FD8B9E4A3DB4E10ADFD1292981020977AE92D1D585CC11DFCC4BF8D16C313F2B7B6A9278D3B8212DB2E3EC23676E97D3DD4DCA736ABE72EC0A734F40D7C9F49EDA75B7481BC7951FD69E4CF2827CEBCB3F8C5B5D5143C1D121292003FA0B6A5112A6B64BD
	9AA35B71324B6E34DBCBD12E0D7686043D91F146A636184F585222BFC60D09DD6B58DA8E2C0BBCE35F243F3F1B897C24F562CCEA769D994727F579A4C551A5E530416FC17F56B82A4B21986CD7BE6CBFA8E4F8A52BA37E2D9C5F224D7F83D0CB8788C728D8C1AC92GG64B1GGD0CB818294G94G88G88G3BF908A9C728D8C1AC92GG64B1GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGE692GGGG
**end of data**/
}
private DefaultMutableTreeNode getDependencyPathTreeStartFrom(Object startingNode) {
	Annotation nodeAnnotation = null;
	Annotation mda = null;
	if (startingNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) startingNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		nodeAnnotation = startTreeNode.currentStmtAnnotation;
	} else
		if (startingNode instanceof Annotation) {
			if ((startingNode instanceof MethodDeclarationAnnotation) || (startingNode instanceof ConstructorDeclarationAnnotation))
				return null;
			else {
				mda = (Annotation) CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation((Annotation) startingNode);
				nodeAnnotation = (Annotation) startingNode;
			}
		} else
			return null;
	SootMethod sm = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sm = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sm = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	SliceTraceNode stn = SlicingMethod.sliceTraceContains(new SliceTraceNode(sm, nodeAnnotation));
	if (stn == null)
		return null;
	DefaultMutableTreeNode pathTreeRoot = getBackwardSliceTraceFrom(stn);
	return pathTreeRoot;
}
private DefaultMutableTreeNode getForwardSliceTraceFrom(SliceTraceNode stn) {
	DefaultMutableTreeNode pathTreeRoot = new DefaultMutableTreeNode(stn);
	Set visitedTraceNode = new ArraySet();
	visitedTraceNode.add(stn);
	Hashtable traceNodeForCurrentLevel = new Hashtable();
	traceNodeForCurrentLevel.put(stn, pathTreeRoot);
	Hashtable traceNodeForNextLevel;
	do {
		traceNodeForNextLevel = new Hashtable();
		for (java.util.Iterator nodeIt = traceNodeForCurrentLevel.keySet().iterator(); nodeIt.hasNext();) {
			SliceTraceNode sNode = (SliceTraceNode) nodeIt.next();
			DefaultMutableTreeNode currentRoot = (DefaultMutableTreeNode) traceNodeForCurrentLevel.get(sNode);
			for (java.util.Iterator parentIt = sNode.parents.keySet().iterator(); parentIt.hasNext();) {
				SliceTraceNode parent = (SliceTraceNode) parentIt.next();
				if (!visitedTraceNode.contains(parent)) {
					if (parent != Slicer.sliceTraceRoot && !parent.stmtAnnotation.toString().equals("")) {
						DefaultMutableTreeNode treeChild = new DefaultMutableTreeNode(parent);
						currentRoot.add(treeChild);
						visitedTraceNode.add(parent);
						traceNodeForNextLevel.put(parent, treeChild);
					}
				}
			}
		}
		traceNodeForCurrentLevel = traceNodeForNextLevel;
	} while (!traceNodeForNextLevel.isEmpty());
	return pathTreeRoot;
}
private DefaultMutableTreeNode getIfDependencyPathTree() {
	DefaultMutableTreeNode pathTreeRoot = new DefaultMutableTreeNode("Conditionals");
	Set visitedConditionals = new ArraySet();
	for (Iterator traceNodeIt = Slicer.allSliceTraceNodes.iterator(); traceNodeIt.hasNext();) {
		SliceTraceNode stn = (SliceTraceNode) traceNodeIt.next();
		if ((stn.stmtAnnotation instanceof ControlFlowAnnotation) || (stn.stmtAnnotation instanceof ConditionalAnnotation)) {
			if (!visitedConditionals.contains(stn)) {
				DefaultMutableTreeNode trace = getForwardSliceTraceFrom(stn);
				pathTreeRoot.add(trace);
				visitedConditionals.add(stn);
			}
		}
	}
	//dumpTreeIntoFile(pathTreeRoot);
	return pathTreeRoot;
}
private DefaultMutableTreeNode getIfDependencyPathTreeForNode(Object startingNode) {
	Annotation nodeAnnotation = null;
	Annotation mda = null;
	if (startingNode instanceof StmtTreeNode) {
		StmtTreeNode startTreeNode = (StmtTreeNode) startingNode;
		mda = startTreeNode.currentMethodDeclarationAnnotation;
		nodeAnnotation = startTreeNode.currentStmtAnnotation;
	} else
		if (startingNode instanceof Annotation) {
			if ((startingNode instanceof MethodDeclarationAnnotation) || (startingNode instanceof ConstructorDeclarationAnnotation))
				return null;
			else {
				mda = (Annotation) CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation((Annotation) startingNode);
				nodeAnnotation = (Annotation) startingNode;
			}
		} else
			return null;
	SootMethod sm = null;
	if (mda instanceof MethodDeclarationAnnotation)
		sm = ((MethodDeclarationAnnotation) mda).getSootMethod();
	else
		if (mda instanceof ConstructorDeclarationAnnotation)
			sm = ((ConstructorDeclarationAnnotation) mda).getSootMethod();
	SliceTraceNode stnForCond = SlicingMethod.sliceTraceContains(new SliceTraceNode(sm, nodeAnnotation));
	if (stnForCond == null)
		return null;
	DefaultMutableTreeNode pathTreeRoot = new DefaultMutableTreeNode("Conditionals");
	Set visitedConditionals = new ArraySet();
	for (Iterator traceNodeIt = Slicer.allSliceTraceNodes.iterator(); traceNodeIt.hasNext();) {
		SliceTraceNode stn = (SliceTraceNode) traceNodeIt.next();
		if ((stn.stmtAnnotation instanceof ControlFlowAnnotation) || (stn.stmtAnnotation instanceof ConditionalAnnotation)) {
			if (!visitedConditionals.contains(stn)) {
				DefaultMutableTreeNode trace = getForwardSliceTraceFrom(stn);
				DefaultMutableTreeNode cuttedTrace = cuttingTreeWRT(trace, stnForCond);
				if (cuttedTrace != null) {
					pathTreeRoot.add(cuttedTrace);
					visitedConditionals.add(stn);
				}
			}
		}
	}
	return pathTreeRoot;
}
/**
 * Return the QueryKindComboBox property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JComboBox getQueryKindComboBox() {
	if (ivjQueryKindComboBox == null) {
		try {
			ivjQueryKindComboBox = new javax.swing.JComboBox();
			ivjQueryKindComboBox.setName("QueryKindComboBox");
			// user code begin {1}
			for (int i = 0; i < queries.length; i++)
				ivjQueryKindComboBox.addItem(queries[i]);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQueryKindComboBox;
}
/**
 * Return the QueryKindLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getQueryKindLabel() {
	if (ivjQueryKindLabel == null) {
		try {
			ivjQueryKindLabel = new javax.swing.JLabel();
			ivjQueryKindLabel.setName("QueryKindLabel");
			ivjQueryKindLabel.setText("Query Kind : ");
			ivjQueryKindLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQueryKindLabel;
}
/**
 * Return the QueryResultScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getQueryResultScrollPane() {
	if (ivjQueryResultScrollPane == null) {
		try {
			ivjQueryResultScrollPane = new javax.swing.JScrollPane();
			ivjQueryResultScrollPane.setName("QueryResultScrollPane");
			getQueryResultScrollPane().setViewportView(getQueryResultTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQueryResultScrollPane;
}
/**
 * Return the QueryResultTree property value.
 * @return javax.swing.JTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTree getQueryResultTree() {
	if (ivjQueryResultTree == null) {
		try {
			ivjQueryResultTree = new javax.swing.JTree();
			ivjQueryResultTree.setName("QueryResultTree");
			ivjQueryResultTree.setBackground(new java.awt.Color(204,204,204));
			ivjQueryResultTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			currentQueryResultTreeRoot = new DefaultMutableTreeNode("");
			getQueryResultTree().setModel(new DefaultTreeModel(currentQueryResultTreeRoot));
			getQueryResultScrollPane().validate();
			getQueryResultScrollPane().repaint();
			ivjQueryResultTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(java.awt.Color.black);
					return this;
				}
			}
			.setAngledColor());
			ivjQueryResultTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			ivjQueryResultTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					setForeground(java.awt.Color.black);
					setIcon(null);
					return this;
				}
			});
			((DefaultTreeCellRenderer) ivjQueryResultTree.getCellRenderer()).setBackgroundNonSelectionColor(new java.awt.Color(204, 204, 204));
			((DefaultTreeCellRenderer) ivjQueryResultTree.getCellRenderer()).setBackgroundSelectionColor(Preferences.getHighlightColor());
			ivjQueryResultTree.putClientProperty("JTree.lineStyle", "Angled");

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjQueryResultTree;
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
	getQueryKindComboBox().addActionListener(this);
	getQueryResultTree().addTreeSelectionListener(this);
	getQueryResultTree().addMouseListener(this);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("QueryPanel");
		setLayout(new java.awt.GridBagLayout());
		setSize(501, 289);

		java.awt.GridBagConstraints constraintsQueryKindLabel = new java.awt.GridBagConstraints();
		constraintsQueryKindLabel.gridx = 0; constraintsQueryKindLabel.gridy = 0;
		constraintsQueryKindLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getQueryKindLabel(), constraintsQueryKindLabel);

		java.awt.GridBagConstraints constraintsQueryKindComboBox = new java.awt.GridBagConstraints();
		constraintsQueryKindComboBox.gridx = 1; constraintsQueryKindComboBox.gridy = 0;
		constraintsQueryKindComboBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
		constraintsQueryKindComboBox.weightx = 1.0;
		constraintsQueryKindComboBox.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getQueryKindComboBox(), constraintsQueryKindComboBox);

		java.awt.GridBagConstraints constraintsQueryResultScrollPane = new java.awt.GridBagConstraints();
		constraintsQueryResultScrollPane.gridx = 0; constraintsQueryResultScrollPane.gridy = 1;
		constraintsQueryResultScrollPane.gridwidth = 2;
		constraintsQueryResultScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsQueryResultScrollPane.weightx = 1.0;
		constraintsQueryResultScrollPane.weighty = 1.0;
		constraintsQueryResultScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getQueryResultScrollPane(), constraintsQueryResultScrollPane);
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
		JFrame frame = new javax.swing.JFrame();
		QueryPanel aQueryPanel;
		aQueryPanel = new QueryPanel();
		frame.setContentPane(aQueryPanel);
		frame.setSize(aQueryPanel.getSize());
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
 * Method to handle events for the MouseListener interface.
 * @param e java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void mouseClicked(java.awt.event.MouseEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getQueryResultTree()) 
		connEtoC3(e);
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
 * Comment
 */
public void queryKindComboBox_ActionEvents() {
	int selectedIndex = getQueryKindComboBox().getSelectedIndex();
	if (selectedIndex == NO_QUERY) {
		currentQueryResultTreeRoot = new DefaultMutableTreeNode("");
		getQueryResultTree().setModel(new DefaultTreeModel(currentQueryResultTreeRoot));
	} else {
		if (dependFrame.currentNode == null) {
			JOptionPane.showMessageDialog(this, "Please select one statement for query.", "Select Statement", JOptionPane.WARNING_MESSAGE);
			getQueryKindComboBox().setSelectedIndex(NO_QUERY);
			return;
		}
		dependFrame.criterionViewer.runSlicerWithCurrentCriterion();
		if (dependFrame.criterionViewer.runningCriterion == null) {
			JOptionPane.showMessageDialog(this, "You can not query on the current node, since the node is not a statement.", "Can not run slicer", JOptionPane.WARNING_MESSAGE);
			getQueryKindComboBox().setSelectedIndex(NO_QUERY);
			return;
		}
		if (selectedIndex == SLICE_TRACE) {
			currentQueryResultTreeRoot = getDependencyPathTreeStartFrom(dependFrame.currentNode);
			getQueryResultTree().setModel(new DefaultTreeModel(currentQueryResultTreeRoot));
		} else
			if (selectedIndex == ALL_CONDITIONALS) {
				currentQueryResultTreeRoot = getIfDependencyPathTree();
				getQueryResultTree().setModel(new DefaultTreeModel(currentQueryResultTreeRoot));
			} else
				if (selectedIndex == CONDITIONALS_TO_CURRENT_STMT) {
					currentQueryResultTreeRoot = getIfDependencyPathTreeForNode(dependFrame.currentNode);
					getQueryResultTree().setModel(new DefaultTreeModel(currentQueryResultTreeRoot));
				}
	}
	return;
}
/**
 * Comment
 */
public void queryResultTree_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
		int selRow = getQueryResultTree().getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
	//TreePath selPath = getDependencyTree().getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
	if (selRow != -1) {
		if (mouseEvent.getClickCount() == 1) {
			//mySingleClick(selRow, selPath);
			//System.out.println("Do nothing --- My sigle click on the tree");
		} else
			if (mouseEvent.getClickCount() == 2) {
				//myDoubleClick(selRow, selPath);
				//System.out.println("My DOUBLE click on the tree");
				DefaultMutableTreeNode queryTreeSelected = (DefaultMutableTreeNode) getQueryResultTree().getLastSelectedPathComponent();
				if (queryTreeSelected == null)
					return;
				Object userObject = queryTreeSelected.getUserObject();
				StmtTreeNode selectedNode = dependFrame.getDependencyValueViewer().getSelectedStmtTreeNode(userObject);
				dependFrame.addSelectedNodeToDependFrame(selectedNode);
				//System.out.println("End adding by double clicking");
			}
	}
	return;
}
/**
 * Comment
 */
public void queryResultTree_TreeSelectionEvents() {	
	DefaultMutableTreeNode lastSelectedNode = (DefaultMutableTreeNode) getQueryResultTree().getLastSelectedPathComponent();
	if (lastSelectedNode == null)
		return;
	if (lastSelectedNode.isRoot()) {
		dependFrame.getCodeBrowserPane().setCurrentNodeSelected(dependFrame.currentNode);
		return;
	}
	Object userObject = lastSelectedNode.getUserObject();
	if (userObject.equals(dependFrame.currentNode) || (userObject instanceof String))
		return;
	StmtTreeNode selectedNode = dependFrame.getDependencyValueViewer().getSelectedStmtTreeNode(userObject);
	dependFrame.getCodeBrowserPane().setCurrentNodeSelected(selectedNode);
	return;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-1 16:21:27)
 * @param df edu.ksu.cis.bandera.pdgslicer.dependency.Dependencies
 */
void setDependFrame(Dependencies df) {
	dependFrame = df;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-12 21:23:54)
 * @return boolean
 * @param sliceTraceNode edu.ksu.cis.bandera.pdgslicer.SliceTraceNode
 */
private boolean sliceTraceContains(DefaultMutableTreeNode root, SliceTraceNode sliceTraceNode) {
	for (Enumeration enumVar = root.depthFirstEnumeration(); enumVar.hasMoreElements();) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumVar.nextElement();
		SliceTraceNode stn = (SliceTraceNode) node.getUserObject();
		if (stn.equals(sliceTraceNode))
			return true;
	}
	return false;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-12 23:13:14)
 * @return boolean
 * @param root javax.swing.tree.DefaultMutableTreeNode
 * @param stn edu.ksu.cis.bandera.pdgslicer.SliceTraceNode
 */
private boolean traceRootIs(DefaultMutableTreeNode root, SliceTraceNode stn) {
	SliceTraceNode rootNode = (SliceTraceNode) root.getUserObject();
	if (rootNode.equals(stn))
		return true;
	else
		return false;
}
/**
 * Method to handle events for the TreeSelectionListener interface.
 * @param e javax.swing.event.TreeSelectionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getQueryResultTree()) 
		connEtoC2();
	// user code begin {2}
	// user code end
}
}
