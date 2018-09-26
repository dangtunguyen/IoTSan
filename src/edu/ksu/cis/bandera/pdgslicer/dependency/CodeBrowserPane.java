package edu.ksu.cis.bandera.pdgslicer.dependency;

import javax.swing.*;
import javax.swing.tree.*;
import java.util.*;
import java.awt.Color;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.util.Preferences;
import edu.ksu.cis.bandera.jjjc.CompilationManager;
import edu.ksu.cis.bandera.jjjc.symboltable.Name;
import edu.ksu.cis.bandera.jjjc.symboltable.Package;
import edu.ksu.cis.bandera.jjjc.symboltable.Method;
import edu.ksu.cis.bandera.jjjc.symboltable.Variable;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.bui.BUI;
import edu.ksu.cis.bandera.bui.BUISessionPane;
import edu.ksu.cis.bandera.bui.IconLibrary;
import edu.ksu.cis.bandera.specification.predicate.datastructure.PredicateSet;
import edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate;
import edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion;
import edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet;
import edu.ksu.cis.bandera.pdgslicer.PostProcessOnAnnotation;
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionManager;
/**
 * Insert the type's description here.
 * Creation date: (00-10-23 15:13:56)
 * @author: 
 */
public class CodeBrowserPane extends JPanel implements java.awt.event.MouseListener, javax.swing.event.TreeSelectionListener {
	private PostProcessOnAnnotation postProcessOnAnnotation = null;
	private ca.mcgill.sable.util.Set removablePackages = new ca.mcgill.sable.util.ArraySet();
	private ca.mcgill.sable.util.Set modifiedPackages = new ca.mcgill.sable.util.ArraySet();
	private ca.mcgill.sable.util.Set removableSootClasses = new ca.mcgill.sable.util.ArraySet();
	 ca.mcgill.sable.util.Set unreachableSootClasses = new ca.mcgill.sable.util.ArraySet();
	private ca.mcgill.sable.util.Set modifiedSootClasses = new ca.mcgill.sable.util.ArraySet();
	private ca.mcgill.sable.util.Set removableSootFields = new ca.mcgill.sable.util.ArraySet();
	private ca.mcgill.sable.util.Set removableSootMethods = new ca.mcgill.sable.util.ArraySet();
	 ca.mcgill.sable.util.Set reachableSootMethods = new ca.mcgill.sable.util.ArraySet();
	private ca.mcgill.sable.util.Set modifiedSootMethods = new ca.mcgill.sable.util.ArraySet();
	private ca.mcgill.sable.util.Set parameterModifiedMethods = new ca.mcgill.sable.util.ArraySet();
	private Dependencies dependFrame;
	private boolean setSelectedByProgram = false;
	private JSplitPane ivjCodeSplitPane = null;
	private DraggableHierTree ivjHierTree = null;
	private JScrollPane ivjHierTreeScrollPane = null;
	private DraggableMethodTree ivjMethodTree = null;
	private JScrollPane ivjMethodTreeScrollPane = null;
/**
 * CodeBrowserPane constructor comment.
 */
public CodeBrowserPane() {
	super();
	initialize();
}
/**
 * CodeBrowserPane constructor comment.
 * @param layout java.awt.LayoutManager
 */
public CodeBrowserPane(java.awt.LayoutManager layout) {
	super(layout);
}
/**
 * CodeBrowserPane constructor comment.
 * @param layout java.awt.LayoutManager
 * @param isDoubleBuffered boolean
 */
public CodeBrowserPane(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
	super(layout, isDoubleBuffered);
}
/**
 * CodeBrowserPane constructor comment.
 * @param isDoubleBuffered boolean
 */
public CodeBrowserPane(boolean isDoubleBuffered) {
	super(isDoubleBuffered);
}
/**
 * 
 * @return javax.swing.tree.DefaultMutableTreeNode
 * @param sc ca.mcgill.sable.soot.SootClass
 */
private DefaultMutableTreeNode buildClassNode(SootClass sc) {
	AnnotationManager am = CompilationManager.getAnnotationManager();
	Set ts = new TreeSet();
	for (ca.mcgill.sable.util.Iterator i = sc.getFields().iterator(); i.hasNext();) {
		ts.add(am.getAnnotation(sc, i.next()));
	}
	for (ca.mcgill.sable.util.Iterator i = sc.getMethods().iterator(); i.hasNext();) {
		ts.add(am.getAnnotation(sc, i.next()));
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
	Hashtable compiledClasses = CompilationManager.getAnnotationManager().getAnnotationTable();
	ca.mcgill.sable.util.Set scs = new ca.mcgill.sable.util.ArraySet();
	for (Enumeration e = compiledClasses.keys(); e.hasMoreElements();) {
		SootClass sc = (SootClass) e.nextElement();
		Name cName = new Name(sc.getName());
		if (cName.getSuperName().equals(p.getName()))
			scs.add(sc);
	}
	for (ca.mcgill.sable.util.Iterator j = scs.iterator(); j.hasNext();) {
		SootClass sc = (SootClass) j.next();
		//Hashtable scHashtable = (Hashtable) compiledClasses.get(sc);
		pNode.add(buildClassNode(sc));
	}
	return pNode;
}
/**
 * connEtoC1:  (HierTree.treeSelection. --> CodeBrowserPane.hierTree_TreeSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1() {
	try {
		// user code begin {1}
		if (!setSelectedByProgram)
			
			// user code end
			this.hierTree_TreeSelectionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (MethodTree.mouse.mouseClicked(java.awt.event.MouseEvent) --> CodeBrowserPane.methodTree_MouseClicked(Ljava.awt.event.MouseEvent;)V)
 * @param arg1 java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.MouseEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.methodTree_MouseClicked(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (MethodTree.treeSelection. --> CodeBrowserPane.methodTree_TreeSelectionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4() {
	try {
		// user code begin {1}
		if (!setSelectedByProgram)
		// user code end
		this.methodTree_TreeSelectionEvents();
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
	D0CB838494G88G88GAA0C01A9GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BB8DF4D4551969CAB53B07DA5CCDD7568D676091ADEEF35C28000028D18103A6889294B1EAD0EA719C34584DD6FA162DF706C423D28CC2B516D8A3A4E1C2E29212E883DACD488F89A4A4B2E3928AADF9B3731899B319995F3C18C0515D6F3B77FDEF5ECCE6A6810FB967634D7D6E3D5F7D6E77F73F6F3E875B755E4CFC13F997E326FC267D7D4944D842C70CBDF94550F942D525E61A18616FFD009D6C
	05B3F9707C847A32FE1EE9AAE4BF3597C03B975A5B207DB67C5E4912FBD39B60C70CF592216F135F3AE647DF6759D6DA67955EFEFF6FB6F8EE873081EE8CE0F1AA2EFFF0EF1106CF0676F66DB702498CFB3A99473CD4DFE440975CAC4659606986A83D8547587696EB7D690043B1B9G395A58EDB066D54E5DD7BD0D5A38C5853319DAF7527CBBDC4BC01F40FB0B18BBCDFB9682C7A8082D2C68742738375EA1572732F324FAA8B8D26D70842B6D124FA9ABD2F540698AFABD8ED929F64A8199F0BE4739C307DA7C
	CE7930629F8D4ACA2F641301402D48F7D9C3812789FC43BAD3B9EEC6834A2A95FE1F86D0EFA55E1F6F7EC463ED7039392C7689FBCC5ED75CAAF84E3DB5447B0A97EFB74F1A03743F613A40F98D50768288DE166CA5DEB64CA1DE3AEBF1EC495CCCD3A3C06FDC426FBA00F8B73493G264FA59E1F7A0CF8743A97305C9A17790D98BC264E953C2D199B62F156419B725E6176B2520EBA5FB58F5685688288GCC83C885584D6D3436EDBFBC1B6DEA0BFFF8586FEB8E848EFB1DCAB7885A8B3F8F9D029E8DF7C2F678
	95A7E3429677B7D4C56107A0FDDE389DA2083D75B3321D69661BD862FB8A3707483D594E9EB1E7EBB4B64D8B6D4DF29B4AED4707A837C2780D73109F01FF17633D863C5055F55C9630BD857AB65CC6F2CE3290AF5B7EF996CBF11DB03F920517417BFC662E790287677C90AFC246BD9DA863E900CF81D88C108D308520F8814978FCDB7DC4B26E74293262133C5691FBC0D25443A39EAF7801A66B4B764613F5B490732E3A68F9C2AE336B0DBAAA383DAF2ADC9296594D0D8B04BCFA970464A2563EBF665A018563
	EDE33B2E433B392FCCD9A0F438D547BF4071C9863C502D1563F99B7A8A96E61A4478C46EDB5E0511B6124471D396C65A489F8E100DA4C39F3BBD53A4747BDCA746C4A73493G268324822CG58884096110E173455CC520FFA8D7A95F33FEDFF8B1E72182AC81D3E20AA799CF21F24F8A43BD78EB2ED4C956D481BBDEC5ED46E5F8ED863086C032829FA7CBE20AE8D4BE0CCC1B166A76DBBF00CA273DE1BE7D8068506830C684EE85F09F212026A978127244A2191227F7AF634092F25BE208884D0570BD0DE4DBC
	6616406FCF96112C7EF08CF1C950CE5165D2F264CDF89E8363163D078E35FA9C4806241CA39BFD1B7BEC9A0CAF3E83E4FB079E27874886B77F7CA636E61EB52A8DF21B3BD35813654EC89B5C4BE97782BEGB06DCEBAC7EFEC9808E11B48C7AC5FC07E6279CE2A56AF5638A7669ADB6350481F40BFDF511EC25E09DD383FD458D7864046BBC966270E1488DBBCAC85E5323E9E858CC7F606B4A0468EF46059A2FFAD7B54D66D24D5031A6E4FF43C9A324B33FE5DE6E40E47B8558206935471C39DBB849EE8982D1C
	7A033CDFD2D545E39FD1E52B2C2A9E1FABB47F6F1C17E0D8888CEAFB7D27EE5CEB5EDDE0A7GD5F751DE73BAF79A6C2B4B6F9F9A895850F3B4BBDD474F63A698BFF097697F3EDA4C2BAE686D153C9D6ABFD99BCBF75F85F03D10FDA85F4B9AE4DBE20DAB7518162F49D8602F61786940CF9A4086FD5FA5870CBC659B6CEDA258BD09B1E20D03635608678F51ECFFA2BEF63297BBE35B74EFBB235BF45E5168B67D5A5178B67B6651E8B6AB7A5EB90A7B730F7A8CBE704723988F9DFE1FCF660E28CF51E4713C8D77
	6171F954FE0F53A52BD64FF948F39B53501687F84CAD045F25E994A7C777D21CF4765D481A3EE9091ACF4D5C61B5DFC853F27134D01C94BC3DF78C6DC1764A43A028967F08CF9578B29E4FB5FCF0AB47ADEC6B01E7092543A3ABB6C5162BD478478AE378EE2ACBBE7860531208B43D4A2D0DBDE3B30EEDC37D84AD95FD9575CC507E8F6F651CF6372C3A7DCECEFD58BF92142B793FADD08F8C49CE4B175257D235B42AD6F385D7F7E39F27D591F571E11D5819EEA3192E18F4BE86166A18465366F6DBA4CF135831
	E15198FBBED0AF625A9D47F98C871DAC8648CE2338F66DB16C273826856F963FD7A384FD363B51A67ABE5CE550F3EE1A288B1B20CF744FE80C563FDD1F3FACEAFF725D444715DD05E85F72D7A3D0024998D80F4967A83768FAB3222FCF720E04F21699DD2F4353EB084982BF2DBB3A3D7DEBB74661A11D7EB55DAFB34CBD84CD619F2992528CA81EE1B8427010516A5B4DFB0D75EDAA700F35F096BCB7G0839F3C651D7A30B4804FBF0FE66812AFD99340BG9A6F91B4EC7788F909F109F5B4AE835A6B81726E95
	631A6EB50EBBD6CF63265CAB46913DE4EDDC463DB84E2F572EB5BA6EF20E5B87816F33BC4A493B8B0DB537F725BD2A1FF33FDEAC6817AE0E2CD5FE5503F4DB81EFDBCC7A5DF55CF8FEB68703FE0707270A1A2E6EBDEE0C818217FE7CB5F81E75A8C128DB1C729869FB79F16495F3C98D2D696CE715FFE663F363B627C775AB141E340CF25F3079D1BD9DE0B2ADFA086C734823A882DEB37F68C31217FBB14540EB6A0CB57D5453ED6657E348E626A613B403ECF220DE963459C1224D16104D7CD7855A953F274000
	B1D69D9C0578FF6850D1349D2F18E7AFAD50765A3804EA7BAB77210D87201D88B093C054993F66E7AF0E1D8F38CDCB48CFEB2B0CFEE8D91AE9D281268308B87457BA4407079E126FF5956F21BC62C7D94D0ED8EF1336C6CAEBAAF36F9F08FA37106D795C1C32D448A8EBE9C8D6E3A7EF481B8AA076F2F39D6D2564BE482F6E233DE4159B77B28D70B9GF9693417227A8F60F9D121DD3725607007B877D202BA23DACF2DA73E0B00275EF452F5510E7743E9471024FE17D19766137F1B9053A79353058C1353A3FD
	72614F51F66683BEA31DFC3233E70741A7CF4072124F65456BAEAD3F69C1B9C21C545A7F53036B72B849A84F9AE83C185ABFA71DEA381DD654DDAE348B17C1AD8D5084B0380CF87B075E896FB25A46625CE5649F3C983E9216D1EE6AAA0D1C1734ACD40F4DDEB63EB693753E59023ADE34AC72BEE19B47E7AC0B3CCFC85AC777895938777BE97DAD87A2571FF5FF781AE991EDB16F353A48F955916D75917304ADDE7B897A69C55E0F36AA124B0587ADE5E762EC3CE63F71EC5C826BD99E48B4F5818CBEC077162E
	AA0AE1E38F10ADF8ECF80625C2BB83E0BD0E4D005C9320894019C1B63132B7DAFC8F3709D841BDD4A38AF95DB6CEDE13137312F1737E98511E1291CD4EAFFEFB78BB48B9149552BD7BBF16C613F5B22C1D1AC132FEFD9F49BABD036C6BFC7D256DBBF15C3CC87BE219617304EEEF7C82F9A802BECBA669317AF33C8B2738536B7778D4C6B167314FB19773AB9E1747C7389C0EF37895DE6008FA5B5F08F49B0126CD273B5B9A3B9E5BE30DDE0FD92C516B31EAEB7CBB060F2C316AB541DF71C1624F8DA0F05F7273
	7B8234276938D365081B836D0CCC52D101857353CDFA7BCD5EC67DEAF575BEE3ED3BA9B37EFD514502D8635630B5A6DBBBCF965E65B4173D758617F1B0A887396336CA2A24757DC27DBD0BD23F9E1706E5C6B5D3FD5D2B5A18B6555FF2C077B5D75B3BACEAAEE5F5A8D059E82FF4049F35D80CFE34ED39383FE900E747F272230776D33EB6381C722E3FD7723B8DE8278224AFA75DF82BE21DBBB9B0A6F779F8DDE9F7D6365836FB5E965DF693EC68C7C17F086210A36D738ADB115E970A021D2ABC0CA2546AA3BE
	DF1594287FA367DF4D670B3ED873E75AE27900504F73E17A4173DC4873EE6E87A82F0287C91E6F1523BC1B20BDG90F81064D938AF16BC93E1CC72036365A9563E395E38F6033E2F1B3E40F3F8CCEF5F425B67F45B111B376B7BEE73B935602455CBBC667C8260B7E64255F3BF5F8B13F71AE86D1F14E23C72CA677CA3B037F8850ECB5163F5AA701C8E3089A0EF0530B731764D23D8CF1CD524E19023B5A0BB58E6BED7A92739CEE80F81CC87188F30E685653AD3BBD117F86ED983DE0F2A5938207B6F5545B17C
	C19FAAEC5AC267C553CA524B53952817B1E8CF83183D127472487EF879D55A4AD8F952AF395FE42D0C4C13FE43716BD7C666C9BF2824BCA9977A869E2238F9994FA396C1BBCB472D9D22D83A61A162F3291E4FEDDB15E9AAD9954ED7432AD8FC7E8B37470ED511FC4E607801D511FC1615931FEE684BD8C5BC6D5BC7FC56E6912E300678EC4D22FAAD2B145E733A81B7B30BEC62196A5DC65DE14DF16850E1BF5E53F4E9864675D4DCD6204DDF13C53ABBDA465FDF2E06339720E9B555316737D29D3B25E29E3B70
	72D054F753D5371FB48F2C96723930BAD4CFCC536B5869653466FC685F349AF1CB43EAB21447E7E611AB087E4DE17D98BF30668A756F5FE76CC7FEDF5CEA6CBF9D561FEF18AF76D6F90A76E67BE8A1EB3C2CA766FB774607451ECEEB4F5E9BBA5946E797EB6FF492AC716CBB6961F03B197DB03D8F6C5F4F6B000705FD646BF6E3677895863C507B3395D4A32E073E0EC748C61E5FCFB61272885972FFD7E29E30C6EF3F445BF807A45AD7D4DE4ADDBF521BFCDEB073125618EC7EA0E259B5D5F86FE3179CC3AE45
	BF62F3B2D63A86E5F28B77790D406FE6849E9BDF9822586806B197G12819681E42FA1D9BE4E4F1BAD50AEF914F09FF1FFE90076E99DF7D59949BC70A849745F4AD14649FA7BBA5EDE2F4B3C327C3B3EDFC15A71E55E7A1D5708A7FFA15BDE9E2766GAFE90F12EC775510ECB7820E2D857981B4818C2E257D2F9DD7AFCF4E0EB6DC623C774BAEF61E703FE29E1F93568A7FB369F89137279A7042AFFB4BA89ECF073EADEB29D6F57623CC869F83DB83C882C88148G58709855A1FF6A2BE5935429B6FF208B12BD
	2F2134907B1BD1FEE95FD23CE2233817741F57B33737BD665D641647043D95E70BE776551D9CAF767762G6E2F8B7A5CGD381E6814C87D811CD7B7B3F3EAA31BF3CDAEDD6D5496166373634B93CC88D21B111917EF535057CEBFDB6795C65957CDD536319262A4749267E4AE3DF933487F51C1B63702E926F78846E17956497B8EEA2B9B5CDE24CF8FBAB58CFEBCC744EFCA2BF0B86339EC79964D716E86D04A8BCA6E9B8919BFFB808F220FB170D0F6377FC3FBE43747E8707700E56A70F1668D714ADFE2FDF31
	5455B6D4E9D021594CBAE63CD7ACDCF4AE663DE26D930207D66DB96BE71DDA5E31F78F650C971E209C60541E489C60AAE6BC43FF34A7B287B005755F35A7B2871891567F421E489C00668B996416623711DF4B0A6AF1C8DED1749D1614DE3F5763B827453BB7F82C475CE14E9314C3F717218F387D0A673C5F270E1FC93152436762DDD4AE4C95BA1CDDC6BAECFCF23C8E3B1EB46AF0579EBC5F0CF7BAC20F4DD2C80F79B513512350C1F78D69E070C952418FEB626B60361A78BAF836A63E8EF657465201701BBD
	65CC7FDDE9785DD776FDFC0F906E337E6FF58D9183077BE3576BEAFF74FA7DFCFF743B26047E786F7E937BE33F7B7FF13F715D3F0861BB6C281B69E08BF3G56GEC84E0B9943F5F9D5843F87C0E923B5B1CAE3CAF402F43D4DA1F1D0C3D57D7CECE7CCDD961C91E4F79D555BF0C74897F56C954176A8FF024F607F792E524F8DCEED58C55706F1FC49B774AE7B93AB687ED70A68B79DCB4A84EB17A5C0C8A5C0353733587ABA73FC33CFB947EBC0F1FC157820C65D0AC7855BB784E19622C7162C8740F3C05B28E
	7930E18477498FAA49A793F51AB24F57F18FA27EFE330D4E6C0DC7D2D8FA364BCCFE983917B4EDAF9BF3C2F59879A5E3B50D46B83E785DF633A9861D52F5E2FE6D3A4877C38B9D482F9370896B48CEBE9E2C04E71F746578B729006CD57C8188316784163F11577C4F324718434C84FAF35645BFFB05FC4A4B701E46F0C5D1ED38312B6EEBBBE16BECE96EBAE3EDB9515355F538794419E66B1963EDED2DED2DE2FEC259C7F166F7741C687CCB4FF1DBF48ACC0B25AB4A74BB73F524370272FC32C9C6B166C40DF1
	0F6C29CC532E27626FD17BD6479E5B17CA6C937BD2857FBFGB278006DDCC03B072B3673BB7333E703326E337576C29D57996C7587BDE870E2BF8FD8F0FC0B0C5F82138DD50F99EDB1BBE9B426ADDA34FD5A1E8A37C5C1BB33824AFD56D8CBF22A2BB44AA99166A4CFA0A7E10FF5B27AF6AA0C4DG5884D070F426299820812083E070E932532576F8F62ADFF2713026C8F821276572E7192E17ED0E32D8B4A0AA9E851B6A31FFA9BBD41B6452675C65C0FE7538ADDCC06BDB6A4078005FD7F9A42F67BC8FB7D698
	68F3093C77B31EB7DD00BD6039AAF02D9C1784382BF4BBDB9DD66F24C25F6227634BCF5072540C276F2BB15297B2385C05F2DE8338DC0022751926DA00D60053G81004475A467AE27A50E1C4525D4C848E24D4ECA6263C89E71F13C1278D8E2A6F97DD90E6D972D7244FE51ADC7BFE32C7278B3E6C00EFE467CCDB61EB1826760F49559857A93AF9F845EA3633D1238636D16FC120B1F3F624ECE62B525267BFE0FD3F58BFC71586FC2788ED90BA7C2A69B4BC8CE1F69F2FA21EC3C1C2EF2451653F45744F23A56
	95DDCEBFF50D17D30AAB3A1CEEF10D1753ED9ADDC5FFC9A370F7382249C97C5F259F7C6DF6F826BC4772038A5B9E1E671A2EFF7B32429F570F18594B6D6C653312B76016BAD9BE7F78200B199DC1156D66DFE15440149A87CE2961932D8149A1FFFC0A3DFAD6F15979F01519CF351F82A23C75957B16193B58EB2A9B83028A52D2595BA1CA3DE83ADA4747E3787DBAFBA9A3A3A3GFFC11C4A0C98ADF6AEEAB31E3FD53B7E92330EAAFAC678EE55B311676F86DE573981BF6599723746418F6244A46B306475C215
	B2546CF3E2FCBAFCCE15C3B963F303C8BFC8E334CE5117BB08FCFFE918737FD0CB87880F7FC761FD93GG28B6GGD0CB818294G94G88G88GAA0C01A90F7FC761FD93GG28B6GG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGG3793GGGG
**end of data**/
}
/**
 * Return the JSplitPane1 property value.
 * @return javax.swing.JSplitPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JSplitPane getCodeSplitPane() {
	if (ivjCodeSplitPane == null) {
		try {
			ivjCodeSplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
			ivjCodeSplitPane.setName("CodeSplitPane");
			ivjCodeSplitPane.setDividerLocation(80);
			getCodeSplitPane().add(getHierTreeScrollPane(), "left");
			getCodeSplitPane().add(getMethodTreeScrollPane(), "right");
			// user code begin {1}
			getCodeSplitPane().setDividerLocation(180);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCodeSplitPane;
}
/**
 * Return the HierTree property value.
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.DraggableHierTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private DraggableHierTree getHierTree() {
	if (ivjHierTree == null) {
		try {
			ivjHierTree = new edu.ksu.cis.bandera.pdgslicer.dependency.DraggableHierTree();
			ivjHierTree.setName("HierTree");
			ivjHierTree.setBackground(new java.awt.Color(204, 204, 204));
			ivjHierTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			ivjHierTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(java.awt.Color.black);
					return this;
				}
			}
			.setAngledColor());
			ivjHierTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			ivjHierTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					setIcon(null);
					setForeground(Color.black);
					
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					if (o instanceof Package) {
						String name = ((Package) o).getName().toString().trim();
						name = "".equals(name) ? "<default package>" : name;
						setText(name);
						setIcon(IconLibrary.packageIcon);
						if (CodeBrowserPane.this.removablePackages.contains(o))
							setForeground(Preferences.getSlicedColor());
						else
							if (CodeBrowserPane.this.modifiedPackages.contains(o))
								setForeground(Preferences.getModifiedColor());
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
							if (CodeBrowserPane.this.unreachableSootClasses.contains(o)) {
								setForeground(Preferences.getUnreachableColor());
								
							} else
								if (CodeBrowserPane.this.removableSootClasses.contains(o))
									setForeground(Preferences.getSlicedColor());
								else
									if (CodeBrowserPane.this.modifiedSootClasses.contains(o))
										setForeground(Preferences.getModifiedColor());
						} else
							if (o instanceof FieldDeclarationAnnotation) {
								FieldDeclarationAnnotation fda = (FieldDeclarationAnnotation) o;
								setText(fda.getField().getType() + " " + fda.getField().getName());
								setIcon(IconLibrary.fieldIcon);
								if (CodeBrowserPane.this.removableSootFields.contains(fda.getSootField()))
									setForeground(Preferences.getSlicedColor());
							} else
								if (o instanceof ConstructorDeclarationAnnotation) {
									ConstructorDeclarationAnnotation cda = (ConstructorDeclarationAnnotation) o;
									SootMethod sm = cda.getSootMethod();
									try {
										Method m = cda.getConstructor();
										String result = m.getDeclaringClassOrInterface().getName().getLastIdentifier().trim() + "(";
										JimpleBody body = (JimpleBody) sm.getBody(Jimple.v());
										String parm = "";
										for (Enumeration e = m.getParameters().elements(); e.hasMoreElements();) {
											Variable v = (Variable) e.nextElement();
											if (body.declaresLocal(v.getName().toString().trim())) {
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
									if (!CodeBrowserPane.this.reachableSootMethods.contains(sm))
										setForeground(Preferences.getUnreachableColor());
									else
										if (CodeBrowserPane.this.removableSootMethods.contains(sm))
											setForeground(Preferences.getSlicedColor());
										else
											if (CodeBrowserPane.this.modifiedSootMethods.contains(sm))
												setForeground(Preferences.getModifiedColor());
								} else
									if (o instanceof MethodDeclarationAnnotation) {
										MethodDeclarationAnnotation mda = (MethodDeclarationAnnotation) o;
										SootMethod sm = mda.getSootMethod();
										Method m = mda.getMethod();
										String result = sm.getReturnType().toString().trim() + " " + sm.getName().trim() + "(";
										JimpleBody body = (JimpleBody) sm.getBody(Jimple.v());
										String parm = "";
										for (Enumeration e = m.getParameters().elements(); e.hasMoreElements();) {
											Variable v = (Variable) e.nextElement();
											if (body.declaresLocal(v.getName().toString().trim())) {
												parm += (v.toString() + ", ");
											}
										}
										if (parm.length() > 1)
											parm = parm.substring(0, parm.length() - 2);
										result += (parm + ")");
										setText(result);
										setIcon(IconLibrary.methodIcon);
										if (!CodeBrowserPane.this.reachableSootMethods.contains(sm))
											setForeground(Preferences.getUnreachableColor());
										else
											if (CodeBrowserPane.this.removableSootMethods.contains(sm))
												setForeground(Preferences.getSlicedColor());
											else
												if (CodeBrowserPane.this.modifiedSootMethods.contains(sm))
													setForeground(Preferences.getModifiedColor());
									}
					return this;
				}
			});
			((DefaultTreeCellRenderer) ivjHierTree.getCellRenderer()).setBackgroundNonSelectionColor(new Color(204, 204, 204));
			((DefaultTreeCellRenderer) ivjHierTree.getCellRenderer()).setBackgroundSelectionColor(Preferences.getHighlightColor());
			ivjHierTree.putClientProperty("JTree.lineStyle", "Angled");

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjHierTree;
}
/**
 * Return the JScrollPane1 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JScrollPane getHierTreeScrollPane() {
	if (ivjHierTreeScrollPane == null) {
		try {
			ivjHierTreeScrollPane = new javax.swing.JScrollPane();
			ivjHierTreeScrollPane.setName("HierTreeScrollPane");
			getHierTreeScrollPane().setViewportView(getHierTree());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjHierTreeScrollPane;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-7 14:08:21)
 * @return int
 * @param ldsa edu.ksu.cis.bandera.annotation.LocalDeclarationStmtAnnotation
 */
private int getLocalDecState(LocalDeclarationStmtAnnotation ldsa) {
	Annotation mdAnnotation = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(ldsa);
	SootMethod sootMethod = null;
	if (mdAnnotation instanceof MethodDeclarationAnnotation)
		sootMethod = ((MethodDeclarationAnnotation) mdAnnotation).getSootMethod();
	else
		if (mdAnnotation instanceof ConstructorDeclarationAnnotation)
			sootMethod = ((ConstructorDeclarationAnnotation) mdAnnotation).getSootMethod();
	if (sootMethod == null)
		throw new edu.ksu.cis.bandera.pdgslicer.exceptions.SlicerException("sootMethod is null");
	if (postProcessOnAnnotation == null)
		return Statements.REMAINED;
	ca.mcgill.sable.util.Set removableLocals = postProcessOnAnnotation.getRemovableLocals(sootMethod);
	if (removableLocals == null)
		return ldsa.getAnnotationState();
	int removeNum = 0;
	for (Enumeration elem = ldsa.getDeclaredLocals().elements(); elem.hasMoreElements();) {
		Local decLocal = (Local) elem.nextElement();
		if (removableLocals.contains(decLocal))
			removeNum++;
	}
	if (removeNum == 0)

		
		//return Statements.REMAINED;
		return ldsa.getAnnotationState();
	else
		if (removeNum == ldsa.getDeclaredLocals().size())
			return Statements.SLICED;
		else
			return Statements.MODIFIED;
}
/**
 * Return the MethodTree property value.
 * @return edu.ksu.cis.bandera.pdgslicer.dependency.DraggableMethodTree
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public DraggableMethodTree getMethodTree() {
	if (ivjMethodTree == null) {
		try {
			ivjMethodTree = new edu.ksu.cis.bandera.pdgslicer.dependency.DraggableMethodTree();
			ivjMethodTree.setName("MethodTree");
			ivjMethodTree.setBackground(new java.awt.Color(204, 204, 204));
			ivjMethodTree.setBounds(0, 0, 78, 72);
			// user code begin {1}
			ivjMethodTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
				public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
					setHashColor(Color.black);
					return this;
				}
			}
			.setAngledColor());
			ivjMethodTree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
			ivjMethodTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			ivjMethodTree.setCellRenderer(new DefaultTreeCellRenderer() {
				public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
					super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
					setIcon(null);
					Object o = ((DefaultMutableTreeNode) value).getUserObject();
					setForeground(Color.black);
					if (o instanceof Predicate) {
						setForeground(Preferences.getPredicateColor());
						if (!((Predicate) o).isValid())
							setIcon(IconLibrary.errorIcon);
					} else
						if (o instanceof Assertion) {
							setForeground(Preferences.getAssertionColor());
							if (!((Assertion) o).isValid())
								setIcon(IconLibrary.errorIcon);
						}
					if (o instanceof Annotation) {
						Annotation a = (Annotation) o;
						if (a instanceof MethodDeclarationAnnotation) {
							SootMethod sm = ((MethodDeclarationAnnotation) a).getSootMethod();
							if (parameterModifiedMethods.contains(sm))
								setForeground(Preferences.getModifiedColor());
						} else
							if (a instanceof ConstructorDeclarationAnnotation) {
								SootMethod sm = ((ConstructorDeclarationAnnotation) a).getSootMethod();
								if (parameterModifiedMethods.contains(sm))
									setForeground(Preferences.getModifiedColor());
							} else
								if (!((DefaultMutableTreeNode) value).isRoot() && !(a instanceof LocalDeclarationStmtAnnotation)) {
									switch (a.getAnnotationState()) {
										case Statements.SLICED :
											setForeground(Preferences.getSlicedColor());
											break;
										case Statements.MODIFIED :
											setForeground(Preferences.getModifiedColor());
											break;
										default :
											;
									}
								} else
									if ((a instanceof LocalDeclarationStmtAnnotation)) {
										int localDecState = CodeBrowserPane.this.getLocalDecState((LocalDeclarationStmtAnnotation) a);
										switch (localDecState) {
											case Statements.SLICED :
												setForeground(Preferences.getSlicedColor());
												break;
											case Statements.MODIFIED :
												setForeground(Preferences.getModifiedColor());
												break;
											default :
												;
										}
									}
					}
					return this;
				}
			});
			((DefaultTreeCellRenderer) ivjMethodTree.getCellRenderer()).setBackgroundNonSelectionColor(new Color(204, 204, 204));
			((DefaultTreeCellRenderer) ivjMethodTree.getCellRenderer()).setBackgroundSelectionColor(Preferences.getHighlightColor());
			ivjMethodTree.putClientProperty("JTree.lineStyle", "Angled");

			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMethodTree;
}
/**
 * Return the JScrollPane2 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JScrollPane getMethodTreeScrollPane() {
	if (ivjMethodTreeScrollPane == null) {
		try {
			ivjMethodTreeScrollPane = new javax.swing.JScrollPane();
			ivjMethodTreeScrollPane.setName("MethodTreeScrollPane");
			ivjMethodTreeScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			ivjMethodTreeScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			getMethodTreeScrollPane().setViewportView(getMethodTree());
			// user code begin {1}
			//ivjMethodTreeScrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER,getJimpCritToggleButton());
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMethodTreeScrollPane;
}
private StmtTreeNode getSelectedTreeNode() {
	//get Package and MethodDeclaration
	JTree hierTree = getHierTree();
	JTree methodTree = getMethodTree();
	DefaultMutableTreeNode hierTreeSelected = (DefaultMutableTreeNode) hierTree.getLastSelectedPathComponent();
	if (hierTreeSelected==null) return null;
	Object hierTreeSelectedObj = hierTreeSelected.getUserObject();
	Annotation mda = null;
	SootClass sc = null;
	Annotation stmtAnn = null;
	if ((hierTreeSelectedObj instanceof MethodDeclarationAnnotation) ||
		(hierTreeSelectedObj instanceof ConstructorDeclarationAnnotation)) {
		mda = (Annotation) hierTreeSelectedObj;
		DefaultMutableTreeNode classNode = (DefaultMutableTreeNode) hierTreeSelected.getParent();
		Object classObj = classNode.getUserObject();
		if (classObj instanceof SootClass)
			sc = (SootClass) classObj;
		else
			System.out.println("error: classObj is not SootClass");
	} else
		System.out.println("we do not deal with other selection currently rather than methodDeclarationAnnotation");
	//get StmtAnnotation
	DefaultMutableTreeNode methodTreeSelected = (DefaultMutableTreeNode) methodTree.getLastSelectedPathComponent();
	Object methodTreeSelectedObj = methodTreeSelected.getUserObject();
	if (methodTreeSelectedObj instanceof Annotation) {
		stmtAnn = (Annotation) methodTreeSelectedObj;
	} else
		System.out.println("error: methodTreeSelected is not Annotation");
	dependFrame.currentMethodDecAnn = mda;
	return new StmtTreeNode(sc, mda, stmtAnn);
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
 * Comment
 */
public void hierTree_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	
		 int selRow = getHierTree().getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
		 TreePath selPath = getHierTree().getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
		 if(selRow != -1) {
			 if(mouseEvent.getClickCount() == 1) {
				 //mySingleClick(selRow, selPath);
				 System.out.println("My sigle click on the tree");
			 }
			 else if(mouseEvent.getClickCount() == 2) {
				 //myDoubleClick(selRow, selPath);
				 System.out.println("My DOUBLE click on the tree");
			 }
		 }
	 
	return;
}
/**
 * Comment
 */
public void hierTree_TreeSelectionAction(Object selectedObj, boolean showJimple) {
	if (selectedObj instanceof MethodDeclarationAnnotation) {
		updateMethodTree((MethodDeclarationAnnotation) selectedObj, null);
		SootMethod sm = ((MethodDeclarationAnnotation) selectedObj).getSootMethod();
		if (showJimple)
		dependFrame.jimpleCodeForSootMethod(sm);
		//repaintDependTree(null);
	} else
		if (selectedObj instanceof ConstructorDeclarationAnnotation) {
			ConstructorDeclarationAnnotation cda = (ConstructorDeclarationAnnotation) selectedObj;
			if (showJimple)
			dependFrame.jimpleCodeForSootMethod(cda.getSootMethod());
			if (cda.getNode() == null) {
				getMethodTree().setRootVisible(true);
				getMethodTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Source code unavailable")));
				getMethodTreeScrollPane().validate();
				getMethodTreeScrollPane().repaint();
				return;
			}
			DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
			root.add((DefaultMutableTreeNode) BUISessionPane.buildTree(cda).elementAt(0));
			JTree rightTree = getMethodTree();
			rightTree.setRootVisible(false);
			rightTree.setModel(new DefaultTreeModel(root));
			for (int i = 0; i < rightTree.getRowCount(); i++) {
				rightTree.expandPath(rightTree.getPathForRow(i));
			}
			getMethodTreeScrollPane().validate();
			getMethodTreeScrollPane().repaint();
		} else
			if (selectedObj instanceof FieldDeclarationAnnotation) {
				FieldDeclarationAnnotation fda = (FieldDeclarationAnnotation) selectedObj;
				getMethodTree().setRootVisible(true);
				getMethodTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode(fda)));
				getMethodTreeScrollPane().validate();
				getMethodTreeScrollPane().repaint();
			} else {
				if (selectedObj instanceof SootClass) {
					//Annotation classAnnotation= annotationManager.getAnnotation((SootClass)selectedObj);
					if (showJimple)
					dependFrame.jimpleCodeForSootClass((SootClass) selectedObj);
				}
				getMethodTree().setRootVisible(false);
				getMethodTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
				getMethodTreeScrollPane().validate();
				getMethodTreeScrollPane().repaint();
			}
	return;
}
/**
 * Comment
 */
public void hierTree_TreeSelectionEvents() {
	Object selectedObj = ((DefaultMutableTreeNode) getHierTree().getLastSelectedPathComponent()).getUserObject();
	boolean showJimple = true;
	if (selectedObj != null)
		hierTree_TreeSelectionAction(selectedObj, showJimple);
	return;
}
/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getHierTree().addTreeSelectionListener(this);
	getMethodTree().addMouseListener(this);
	getMethodTree().addTreeSelectionListener(this);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("CodeBrowserPane");
		setLayout(new java.awt.BorderLayout());
		setSize(507, 287);
		add(getCodeSplitPane(), "Center");
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
		CodeBrowserPane aCodeBrowserPane;
		aCodeBrowserPane = new CodeBrowserPane();
		frame.setContentPane(aCodeBrowserPane);
		frame.setSize(aCodeBrowserPane.getSize());
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
 * Comment
 */
public void methodTree_MouseClicked(java.awt.event.MouseEvent mouseEvent) {
	/*
	int selRow = getMethodTree().getRowForLocation(mouseEvent.getX(), mouseEvent.getY());
	TreePath selPath = getMethodTree().getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
	if (selRow != -1) {
		if (mouseEvent.getClickCount() == 1) {
			//mySingleClick(selRow, selPath);
			System.out.println("Do nothing --- My sigle click on the tree");
		} else
			if (mouseEvent.getClickCount() == 2) {
				//myDoubleClick(selRow, selPath);
				System.out.println("My DOUBLE click on the tree");
				StmtTreeNode selectedNode = getSelectedTreeNode();
				dependFrame.addSelectedNodeToDependFrame(selectedNode);
				System.out.println("End adding by double clicking");
			}
	}
	*/
	return;
}
/**
 * Comment
 */
public void methodTree_TreeSelectionAction(Annotation ann) {
	if (ann instanceof MethodDeclarationAnnotation) {
		SootMethod sm = ((MethodDeclarationAnnotation) ann).getSootMethod();
		dependFrame.jimpleCodeForSootMethod(sm);
	} else
		if (ann instanceof ConstructorDeclarationAnnotation) {
			SootMethod sm = ((ConstructorDeclarationAnnotation) ann).getSootMethod();
			dependFrame.jimpleCodeForSootMethod(sm);
		} else {
			dependFrame.jimpleCodeForAnnotation(ann);
			StmtTreeNode selectedNode = getSelectedTreeNode();
			dependFrame.addSelectedNodeToDependFrame(selectedNode);
		}
	return;
}
/**
 * Comment
 */
public void methodTree_TreeSelectionActionByProgram(Annotation ann) {
	if (ann instanceof MethodDeclarationAnnotation) {
		SootMethod sm = ((MethodDeclarationAnnotation) ann).getSootMethod();
		dependFrame.jimpleCodeForSootMethod(sm);
	} else
		if (ann instanceof ConstructorDeclarationAnnotation) {
			SootMethod sm = ((ConstructorDeclarationAnnotation) ann).getSootMethod();
			dependFrame.jimpleCodeForSootMethod(sm);
		} else {
			dependFrame.jimpleCodeForAnnotation(ann);
			//Object selectedNode = getSelectedTreeNode();
			//dependFrame.addSelectedNodeToDependFrame(selectedNode);
		}
	return;
}
/**
 * Comment
 */
public void methodTree_TreeSelectionEvents() {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) getMethodTree().getLastSelectedPathComponent();
	Annotation ann = (Annotation) node.getUserObject();
	if (ann != null)
		methodTree_TreeSelectionAction(ann);
	return;
}
/**
 * Method to handle events for the MouseListener interface.
 * @param e java.awt.event.MouseEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void mouseClicked(java.awt.event.MouseEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getMethodTree()) 
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
 * Insert the method's description here.
 * Creation date: (00-12-7 17:21:16)
 */
void repaintTrees() {
	//update Hiertree and Methodtree
	getHierTreeScrollPane().validate();
	getHierTreeScrollPane().repaint();
	getHierTreeScrollPane().setVisible(true);
	getMethodTreeScrollPane().validate();
	getMethodTreeScrollPane().repaint();
	getMethodTreeScrollPane().setVisible(true);
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-10 15:52:15)
 * @param currentNode java.lang.Object
 */
void setCurrentNodeSelected(Object currentNode) {
	if (currentNode instanceof StmtTreeNode) {
		StmtTreeNode currentTreeNode = (StmtTreeNode) currentNode;
		setSelected(getHierTree(), currentTreeNode.currentMethodDeclarationAnnotation);
		boolean showJimple = false;
		hierTree_TreeSelectionAction(currentTreeNode.currentMethodDeclarationAnnotation, showJimple);
		setSelected(getMethodTree(), currentTreeNode.currentStmtAnnotation);
		methodTree_TreeSelectionActionByProgram(currentTreeNode.currentStmtAnnotation);
	} else
		if (currentNode instanceof LocationNodeInCriterionViewer) {
			LocationNodeInCriterionViewer currentTreeNode = (LocationNodeInCriterionViewer) currentNode;
			Annotation currentMethodDeclarationAnnotation = CompilationManager.getAnnotationManager().getAnnotation(currentTreeNode.sootMethod.getDeclaringClass(), currentTreeNode.sootMethod);
			setSelected(getHierTree(), currentMethodDeclarationAnnotation);
			boolean showJimple = false;
			hierTree_TreeSelectionAction(currentMethodDeclarationAnnotation, showJimple);
			setSelected(getMethodTree(), currentTreeNode.stmtAnnotation);
			methodTree_TreeSelectionActionByProgram(currentTreeNode.stmtAnnotation);
		} else
			if (currentNode instanceof SliceField) {
				SliceField sf = (SliceField) currentNode;
				Annotation fieldAnnotation = CompilationManager.getAnnotationManager().getAnnotation(sf.getSootClass(), sf.getSootField());
				setSelected(getHierTree(), fieldAnnotation);
				boolean showJimple = false;
				hierTree_TreeSelectionAction(fieldAnnotation, showJimple);
			} else
				if (currentNode instanceof SliceLocal) {
					SliceLocal sliceLocal = (SliceLocal) currentNode;
					Annotation annotationForMd = CompilationManager.getAnnotationManager().getAnnotation(sliceLocal.getSootClass(), sliceLocal.getSootMethod());
					Vector allAnnotationsInMd = annotationForMd.getAllAnnotations(true);
					for (Enumeration annEnum = allAnnotationsInMd.elements(); annEnum.hasMoreElements();) {
						Object nextElement = annEnum.nextElement();
						if (nextElement instanceof LocalDeclarationStmtAnnotation) {
							Hashtable declaredLocals = ((LocalDeclarationStmtAnnotation) nextElement).getDeclaredLocals();
							if (declaredLocals.containsValue(sliceLocal.getLocal())) {
								setSelected(getHierTree(), annotationForMd);
								boolean showJimple = false;
								hierTree_TreeSelectionAction(annotationForMd, showJimple);
								setSelected(getMethodTree(), nextElement);
								methodTree_TreeSelectionActionByProgram((Annotation)nextElement);
								break;
							}
						}
					}
				} else
					System.out.println("We can not deal with other type of node currently");
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-7 13:45:45)
 * @param dpnd edu.ksu.cis.bandera.pdgslicer.dependency.Dependencies
 */
void setDependFrame(Dependencies dpnd) {
	dependFrame = dpnd;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-7 17:20:43)
 */
void setPostProcessValues() {
	postProcessOnAnnotation = dependFrame.slicer.getPostProcessOnAnnotation();
	removablePackages = postProcessOnAnnotation.getRemovablePackages();
	modifiedPackages = postProcessOnAnnotation.getModifiedPackages();
	removableSootClasses = postProcessOnAnnotation.getRemovableSootClasses();
	modifiedSootClasses = postProcessOnAnnotation.getModifiedSootClasses();
	removableSootFields = postProcessOnAnnotation.getRemovableSootFields();
	removableSootMethods = postProcessOnAnnotation.getRemovableSootMethods();
	modifiedSootMethods = postProcessOnAnnotation.getModifiedSootMethods();
	parameterModifiedMethods = postProcessOnAnnotation.getParameterModifiedMethods();
}
/**
 * 
 * @param tree javax.swing.JTree
 * @param object java.lang.Object
 */
private void setSelected(JTree tree, Object object) {
	setSelectedByProgram = true;
	for (int i = 0; i < tree.getRowCount(); i++) {
		tree.setSelectionRow(i);
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		Object o = node.getUserObject();
		if (o == object) {
			TreePath path = tree.getSelectionPath();
			tree.scrollPathToVisible(path);
			setSelectedByProgram = false;
			return;
		} else {
			tree.expandRow(i);
		}
	}
	setSelectedByProgram = false;
}
void updateHierTree(Object hierTreeSelectedUserObject) {
    SessionManager sm = SessionManager.getInstance();
    Session sess = sm.getActiveSession();
	String rootName = sess.getName();
	getHierTree().setRootVisible(true);
	DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootName);
	Hashtable compiledClasses = CompilationManager.getAnnotationManager().getAnnotationTable();
	TreeSet packageNames = new TreeSet();
	for (Enumeration e = compiledClasses.keys(); e.hasMoreElements();) {
		packageNames.add(new Name(((SootClass) e.nextElement()).getName()).getSuperName());
	}
	for (Iterator i = packageNames.iterator(); i.hasNext();) {
		try {
			Package p = Package.getPackage((Name) i.next());
			DefaultMutableTreeNode pNode = buildPackageNode(p);
			root.add(pNode);
		} catch (Exception e) {
			System.out.println("There is an exception");
			System.out.println("Exception e: " + e);
			e.printStackTrace();
		}
	}
	getHierTree().setModel(new DefaultTreeModel(root));
	getHierTreeScrollPane().validate();
	getHierTreeScrollPane().repaint();
	getCodeSplitPane().resetToPreferredSizes();
	if (hierTreeSelectedUserObject != null)
		setSelected(getHierTree(), hierTreeSelectedUserObject);
	else
		getMethodTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("")));
}
/**
 * Insert the method's description here.
 * Creation date: (00-4-17 17:06:15)
 * @param mdAnnotaton edu.ksu.cis.bandera.annotation.MethodDeclarationAnnotation
 * @param lastSelected java.lang.Object
 */
void updateMethodTree(MethodDeclarationAnnotation mdAnnotation, Object lastSelectedUserObject) {
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
	for (java.util.Iterator i = PredicateSet.getDefinedPredicates(mdAnnotation).iterator(); i.hasNext();) {
		root.add(new DefaultMutableTreeNode(i.next()));
	}
	for (java.util.Iterator i = AssertionSet.getDefinedAssertions(mdAnnotation).iterator(); i.hasNext();) {
		root.add(new DefaultMutableTreeNode(i.next()));
	}
	root.add((DefaultMutableTreeNode) BUISessionPane.buildTree(mdAnnotation).elementAt(0));
	JTree methodTree = getMethodTree();
	methodTree.setRootVisible(false);
	methodTree.setModel(new DefaultTreeModel(root));
	for (int i = 0; i < methodTree.getRowCount(); i++) {
		methodTree.expandPath(methodTree.getPathForRow(i));
	}
	getMethodTreeScrollPane().validate();
	getMethodTreeScrollPane().repaint();
	//getCodeSplitPane().resetToPreferredSizes();
	if (lastSelectedUserObject != null)
		setSelected(methodTree, lastSelectedUserObject);
}
/**
 * Method to handle events for the TreeSelectionListener interface.
 * @param e javax.swing.event.TreeSelectionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
	// user code begin {1}
	// user code end
	if (e.getSource() == getHierTree()) 
		connEtoC1();
	if (e.getSource() == getMethodTree()) 
		connEtoC4();
	// user code begin {2}
	// user code end
}
}
