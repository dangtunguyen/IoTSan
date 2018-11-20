package edu.ksu.cis.bandera.bui.counterexample;

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
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.util.*;
import edu.ksu.cis.bandera.annotation.*;
import ca.mcgill.sable.soot.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import edu.ksu.cis.bandera.bui.*;

import java.util.Iterator;

import org.apache.log4j.Category;


/**
 * The ThreadCounterExample class provides a view of an individual thread's
 * status as the user walks through the counter example.  This shows the
 * thread's local variables (fields and method locals), the associated
 * values, and the current statement the thread is executing.
 *
 * @author Robby &lt;robby@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.4 $ - $Date: 2003/06/23 18:59:52 $
 */
public class ThreadCounterExample extends JFrame {

    /*
     * Note: In order to get the scrolling to work, I modified the generated code to take
     * out the min, max, and preferred size of the scroll panes and trees.  This seemed to
     * do the trick.  However, this was no changed using VisualAge (which is the application
     * that was used to generate the code originally) so it will be inconsistent if it is
     * ever moved back into VisualAge (the builder data will not match the code).  Make sure
     * to make this fix again in VisualAge if this ever happens. -tcw
     */

    private static final Category log = Category.getInstance(ThreadCounterExample.class);
    private CounterExample ce;
    private int threadID;
    private JButton ivjCloseButton = null;
    private JSplitPane ivjHSplitPane = null;
    private JScrollPane ivjValueScrollPane = null;
    private JTextArea ivjValueTextArea = null;
    private JScrollPane ivjVariableScrollPane = null;
    private JTree ivjVariableTree = null;
    private JSplitPane ivjVSplitPane = null;
    IvjEventHandler ivjEventHandler = new IvjEventHandler();
    private JScrollPane ivjThreadScrollPane = null;
    private JTree ivjThreadTree = null;
    private JPanel ivjThreadCounterExampleContentPane = null;

    class IvjEventHandler implements java.awt.event.ActionListener, javax.swing.event.TreeSelectionListener {
	public void actionPerformed(java.awt.event.ActionEvent e) {
	    if (e.getSource() == ThreadCounterExample.this.getCloseButton()) 
		connEtoM1(e);
	};
	public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
	    if (e.getSource() == ThreadCounterExample.this.getVariableTree()) 
		connEtoC1();
	};
    };
    /**
     * Constructor
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    public ThreadCounterExample() {
	super();
	initialize();
    }
    /**
     * Create a new ThreadCounterExample window using the given title, thread ID, and
     * CounterExample as the basis for this thread's model.
     *
     * @param CounterExample ce The CounterExample this thread counter example is based upon.
     * @param String title The title for this threads window.
     * @param int threadID The id for the thread this counter example window represents.
     */
    public ThreadCounterExample(CounterExample ce, String title, int threadID) {
	super();
	this.ce = ce;
	this.threadID = threadID;
	setTitle(title);
	initialize();
    }
    /**
     * ThreadCounterExample constructor comment.
     * @param title java.lang.String
     */
    public ThreadCounterExample(String title) {
	super(title);
    }
    /**
     * connEtoC1:  (VariableTree.treeSelection. --> ThreadCounterExample.variableTree_TreeSelectionEvents()V)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1() {
	try {
	    // user code begin {1}
	    // user code end
	    this.variableTree_TreeSelectionEvents();
	    // user code begin {2}
	    // user code end
	} catch (java.lang.Throwable ivjExc) {
	    // user code begin {3}
	    // user code end
	    handleException(ivjExc);
	}
    }
    /**
     * connEtoM1:  (CloseButton.action.actionPerformed(java.awt.event.ActionEvent) --> ThreadCounterExample.dispose()V)
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
	D0CB838494G88G88GA0EDAFA9GGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135DC8FD8DCD715C7CD5BE823250A1A5AD433B536F1B7EE63A6EA5C0D5DF4B7DA2A540F567819FE4EF6E3B7DDD1D30DABD56AC67BE08825B5CDC9C3DA5AD0CBED2B1070E7121016921270BFC08698E082249061B1731899F84C8CEF9E0170276EB9773E7366B13C99C8374D771D3E39673EFB6F39671C7B3B675C7B28107B726A3D69993902103ED7507E3D19AE88DB4F8B42572E3B56C53C35E5962BE078
	778A508121D84C0467A628CB1836D8F3043B05ECA8A7C1F97714457ABC7CBEA8345B731A61C714F1F6B78942615F05BE9AFB1C72D3B44E932C1CDC388F1E7B011201178AD4ED4371D38A73B4FE9E1477EB3F1152B360BF47701DC7C7728C7CE55AFB5B60198234063DE3AD4A576A1B200C6F8C42D3BD465FDD814FA719F4636D955AFB1B33BFA9380F87331E46318C7D936D525A669C67CF10B08B95E1953E52D5834FAA4728670C641EA89D8DCD143AFC2152A1516F169431F4E842D76A8ACC78D5C91126443120
	ACD5D6BA3D0AA43A6BB83701F3210F3863A87B6151EC56AF488E65FDA7107709D15457D6A8DB00D20F137CBFEF2D52649BFC64D6216041ABD165F71C60F22BA74272477D3CA37D35F26C3F38E81F56EEA3546F82623228C5A40B371CE4C973603B2B213C89A8D9677F2B9779A9D096CEDA2CF9A7C946074E120C3277B6E1E759BBD94FC41151F6124B56FBB2ACE375CF8EE53C40FCE6CB955ABD816AB681A5816D864AF943E235819DE63EBAF36A88BCEB06543A4058D840DF938C564AEE25B56016E478DDD989B5
	9A2FD3F2859437A0F0FFAC98ADB6110728690D793E08446756A3107F2C4C38D918FA79434CA7A267D6BF7C572CFE2D0F609B61392DF9837576040CFA0B075F588E5BF37E330C1FE260F3DB5D44FC814BFB20EE70CD52F3C2A149326F63B78A2BBC3E2CA7CCE4F15CFDCD5656B7398C16B74332F09DFF668C6AB83B42E2FD8D288A48892482A5D4100E3FF96A44E2BAEEC63F760BF25744D0D0D4545A891F8CEBC153757122D83AB6A35E2E7A2A5BF13D2CBDE134D15ABF0F196AA53879CA567A8A2E0F440A30DE78
	58D7220E1DD23150B7766BB614591A58D341EDE855791799FF5FDBE1BE376D6F595A42F2BE5445817177CF78103F732DC89FB94378E9EFC57A48B73D64A37BAAAD56A09037EFC5B56262B6A86F864ABEE53196GD583758385CF110DF32A4A16380E9C867B723677D4BD87CFE9CAD544E6FFC8957DAE29DBD4FC6210AC05846D1D7B2BD02FC3735A7D002DEF970CF1CE7283F22A3E009FFA9747A4F02690FFE7C75581FC875093EB1D3EB18986988B8A54EFCA55C1F84AE2C83D90F40B2A94FE03576F2EC21F38AC
	F6C30F50817C5EF28A7575A6435CF8783D6E9469CA2AC1DEE615451ADFC5FA99BD7DACBC4F03F3CBF2E5E53D4F05E2084A95725127E5ECE3037757D469985DCB7E1776704DC262D7C7CC7D0F2D5B535C17DEBB9D69FFF92CDFBB7065533407FEEE3457248F9CFB296BE3CD0CF779981BCC4730DE459A5C9B230E6B79FB35682BABE0DEEB0192CF132EEFBBDD40FD30D68CC964F56D8AB80C648EEB1E3F7B79B33827C817A53FDAAF85A558F97DEAC833795F1FF9B26C0F43815DD756B3BE62D39DB8CCC867FF754C
	814E07BE0C5ECD75F732FAD1D5955F5004AAF5C92A6A73FB426D3F45E4894D033E10B6576F5761DCF7409C5300324F50DC5B4F9EB47815BD90981D88BAF145E87E994F7661FCF83F7C8C59DF71E2CC552B17E7D9B9DC1F69B5339D91363B9AACB5770F17E20EF1B5FE92FD0C37FCB8F742C94E2BE73CA91843EAFD3E5FE132243F8763C617E5A79B433A08DF3FDB623ED0DB9D5D171B2B4DFD393D5A5C17AFD4477655CB55E63E4A6B5C55B84740245F607B3EEA44BFD7406F175882541B88BC967E39936761737B
	549E1F5BA329DD3EE988ECF71FC19FEEE3981B883FADE7889BA7DFA5ECF4F7FFCA304D3DED9ABF459F78D0D679D96E2FCDE7435848E59A2941B5A5495298A80A45541C9FE4782D71C3D646EBFCF6981E8505F5F2A0A455CE28EA40DFAC3219147207C3D206834A1864AE9C91AF0B256224DA4AACD1DA432A9B70F7D1C1C949198233983E586D8B85216742226EA29B7713034276437DECCC5A841D0AA4952B701FAE100D0F9D3D434B06C683CEE3A3A6CA88C7BA4631A53B817560857D4C82A51CA5EC59D5037504
	AD9A005685E42D9F285BF5966D53A2679AF43E6EAC4F4B766B7517675517D7737A38EA9A67015A9C74A5E9FCC202AD9F05BEAFDD21FD3B7659083AEED11E884783A935FF0027EC40BD4E7FE11D39EDFFD407D8B72A777FF0DD16007BBA6F137B62FA917B8CAA3EB158A69048357C7176A2E37E18G72E32E39891EC9C03C6DF0894E554CA2EA8D53050F724B1D50E68FD0FE8D47667E9A2E337ADAFC2FF2045E5B827C6DC0E9B5FC3C5CDA7EDEBCFBCF52F33EB21DB74BF83FG6039A713E259569CE33E5AFF5F95
	53750234CD6BBF25B6B246BF5D087DE657019CF5E43B01FA637ED3938A85DCBE96E2E9F6585FE0DCCB1CF73061A9F88E7B1490447BEEE90AEC79C2834A0AB11846566C61B8E716B7B438FDEAC0216D3DEE1239F51D8EB05DBEE9D21B7BE70E9BF35D597E7E2CBFC419FBF19D1F333DAEBCF799F2DDAE47BF9CBD3DD4B91C01BA88C71B40ADA3A422DC7DCBB259EDC59D794EE3AF22FF3173G4410295250A4E0EEE5E5CBA346323C5D01A3591ADE3656D17EFC2818658AD04E2D37D80B01F8AC7FB99F658475401B
	85626BF155F963FA5C88FC8B1050E031F25C499AB98E4F2BC7B73257ED4F23FD4308C6BE7850F355049175378849DF3956B41F4FF9701A2C5C862E7F2A06309D16B7BD10318A084F4DB5C2F94AB2F8E7F3834D4D7A0EF1EE29404FEB04FC341166766D91644F87D1127B246FD506850B8227B69F41E11CCF6F110069FC12FC5EACC7A31F47D4E3F8BE09B0978B10F69EF0FC5E7A7C5D27232E4F2DDA9F5B9BA357E7FBB37AC6AA70334F517ACCE8B8E0D81F1DB091516FE1E7C6DA4C5008FA81BC544A0D0DFB858D
	8F850A33D73420CE73214F8AA0871097E839BE463D0D0B66758DD3B1727AFF122FA6BFDFFB0E6236670EE03B4D674C6351E493BE4FFDBF4C564B4EF3113975A798BF75DCE4EEFD381856D1E6934C3D09467F02EFB139131A664B405BEDD8345D6E08F65CBF1F69C2FB1860830FC7509558BE756DE263BE155D4C7717E2F8D6B753795C774E232F206F75B6D35E7A50F95CF362213C8EE8AB508E204C964875016ADB485E0F1DBB35283DB988F2F4280B0C7AB4BD78C6A27560EE19BFEFF37DCDAEE8D79AD10EEBB1
	535FFA6773667A43D52E69AEFF56283B15504FD6209420BDAD243B4AF78803F34F136EAE36226E6CD0768245812D825A82E481926C243B16A633BD44CCF78BB710F0BE49473C298D65A8003EEDFA7F37B7BDEDD86F0E004F2F6AEB5D590C31CEC071F9FCFE62DDE4BCD7C0E1413A0699AD5173081BDB4C73083FEBB14FA3EEEF091D935F5192AD4F60796BBDEC1F2C06797583F1DE895B8303D0CE50F94B98EFAD166D34BEEFA846D8EF27DE3E0915F12F6365D70AE36520CBA56C5F0C4F4728FCCF460816173E44
	6AFE2C9E92CCF22BB6889A844AA5AC3EA735F79A54C06BB1FDCD7C2AA8CA4C61904103383FE71490D66434D24C71F08BE2E29314870126DAC9275B4FC72E4DF828DB833489A829F5212E34DC63FCF41F7B55F9F31FFB6CBC7AFAA8B0213824C83F926CF9FADDF885B52B52982A053F73249D5B2B228279E6E47B9C561E57C5EB7F02BD1A6FF23B5C735CB34C77C3A1A94496F53D280A1ABDD60F986CD19736072761A5F3FBF43994483C50AAD04F312AD6B5E2558E50EDF69B60BCD0F59BE115F316302A370DEC57
	B6476E8D203C96E8CB9B596E6CBB5176256D704E2E36E836F335C6375DC82B396D022D51EDB7593A386D62DA235B6E3A5645ED975F9A7BFC6333ED9883CC6965DBD9790AEEA729E63FBEC6035F2D81AD4F3FF6B04C3AA51D63B437F77D91DC23EEE9D81C10D5F608509E540E2A01DF510E6D867458DEE8271C604B13182B8CC382G62F585A517106340F7EFBEC2361F05F763015683A5836DEE27D877BBD9AF8B6639A8E8C5E5008C5E44ED7FC40951763DB0066C60760E03E702A3DAACF571BC7A52DAC7647D02
	1B71B7BBA26F97FE56C2F1FBB25415F790FE7F0245A3AB213CCE67D5581137854A49BA6F964633C0B9D567D995427E6CCE525B4D2579D156D1D076710933B55426C42EA15C9F4A211F26CEDAC353AA615FE0A72DA1E9866731924A6B0192BBE98DDD1CB56AF1C767FC3D25F6C653633F31B5195D959913A6B1FEDED7E4CC6A1C231834906AD6F411AE3A5FA17D6C56F9F75AC9BF19CE720D4BACF6B62826344767F787A641B37066C58D1556B7F7B95A3B9A865A5B866A6C700367A9BF920CF90A30B9BEEA1ED268
	54F2B86D19F6703E3DDC1FF79D21F3D9956A56B91157FA541837210C1399BC2E6375FF1BD70FEB77F541D87F65F975A8EB1755D87FDF736AD166862DBD1F5751BEBAA3F03EF13BD0782161287761295AFC72AE70671A7D776D4DF4FFA1036B7A0FEC2FF4C01D7B82693F2B8CF94B213CCA675DEFA3BBED51F99BDA48CE5BAE10BF8D30F51186651CEE620DB09E1E9DE56B587D4B16E8F1C65A05586775586FE271879F6329735146D86C5CFCA9E75E5A59837355B13F8441314F45F171A5463B9BFD2A1B7C375D0E
	311C8A9BCBC5B7FEB752F2C1507B78098D4F9F06C45728C78158F3933EFE0B75B38B6FAF87CA6CC65E0BCAC18C19B6EA3A61FEFBAD439954C5CE8F614075A19687C1F990E82A076C71B715F0A03E47E44D83EF13011F54C377358FB0DC5951930927BB99BF35A792CFC7E788CF33FB8133FB491702EC5FCF0672CE1DF757AC79D7EAAF4919B807FAAC38C8657BD9994F9BF9796D39DE2DFEB17B3D9B62E3046668EE647D98035B7EEF8C97ABE09E75C0FC8F7D3E1D30BF883CE5C0EB0036826D3CC8F95DEFAE303B
	5C37A187FE1BF22C575B0F4233DB9CD9F88288CC0792001DDD3D42396C34DA1B67EBC75F5DF788CFB8891B926E783850ABDF0AFAFED97DB6375320763489776D45A71F0B5803730D073AF5C049C0A9C0FB00F27BE8EEF2BB4A08D9A808DF232A224B4BCEEEA9B945C34CB09B83923E66CE16E09EB994D0D8A0363A8F75EBAFA17D96C2399AC885DA5E4777D85EEE248C5B4962154004CA6DBED0C66DD220AC74DB2C05C0D57D3C9D37E77AB1340B93F803C0AB7A5939C7CB1E09CE66634B0686541D6196C763270F
	A0E6F40723830DDFEE4767DFDCFF51ECD1D6E15C7FB63EF4A9ABBD0AAD3675F39B6C688F1F53F12C3A7D22204B7371A046E8FEE932C0BFD82D8B48812570382D3C18514913C55A3BEFAA384E3BC0D3DE5DB7CA2748B7F61D3BC9C8CA78D85601A87264DE62F2D4DF8A4B93DE0357ABE8F7751279BADF9E41DFF509706AF106FF6BAFC56255D3AA7B6E6CD2A4DE5D98A23C5A8EF5D5FFA5EC7A940B31D6C3F9334E3BB30370AAD9673DE5E5FB9F14F38708774C8C728A21DCBFC038751F0C578B65A91D77D3464373
	60841DB7BE93ABBF4736CBDD2B3DA66F72B1BE92F30C2B398FB643C86E9B574D9A575F2E81544BE7E7593713705BB908656D3385DA9B5B60C2D96BB51E969F32BBC1634DA3775D075F8E7B6E867BE23EEBABAF52764E3B5AA937D9B5C8B15FE36D11B15F0D02B1E6BB6A080C79526755DFF2C446FC374C2B3F41919973D1FB0EF5FBAEA0DF4F81B9D4EBF556EEEC0B7751BED176CD33C42D8BDE74FBB816FE258F6B3741DCF683650A00D1C0D5A26168F55DFF92980E1AE0E8035BA3D11CCD630D74C44F2147FB96
	7FAEEA3A0745BC810034B16C1F78F1BD6803EAA048181CF7CD8F6ACA71F93CAAFFD563DF5F03B61225E146666BD5AD21BC23976696AFD2FE3537G636F487C2A14BBE0E5E5036A72CA6EDA4D9DB59F58DCC2F1FF22C871536AD26CDF9832D87300ECC3F43FBFEDA5DC5BD374F9C14E7A20E9BC1E7FAF412C26A16E3772D09857B2FFF2C83BA77AE299FD1F3A966AF78C117C7FDDFA58A8BFBAEFE5A517574EB7A3AE73AF4BC866BC97497C328D7BF4C0598B948744570677D2F8ED2CEDD98A2E7339FE778DA30623
	7C51F20F15AEBE474D2E79984EFDF2702F02EE4B433D2F8BD1E2947040961029FDE8C4F229CE5123377F60G62439E68BB4F8D3E8C648472824581A5G2DF313FF7F6CE2E1347E01499549FCDC91FDFE1563742D8A61F4221B307B91BDF737683CBF0758BD3384B117C4B83D176D8F65D0366B3C9C464373F92F4E7BF468EA4EDB31BF7AED17161A872D3F2AB10C183D74BC68CBAA7AC0D01106A5C5115C7CF3984AE3583CD7003CABA542766FE794E86D371A4CE33BE444F60795E320B1G373109B156B36F574B
	765694F8B7D522786C6B8A62125BF75907ABA36062A7D67C7D8A361F5E880EDAB0CC6F0B194F8A61FD43F8E8496B5FE25F8DB128B5D63233183B9CE6F7AFB6683BDE6FFF63AC7A9D772FC65185BEF72514FD55A959831378FDCAADE0284C6B8B0B787ADAFE4298336DEC76E5D9232C2F7EE18DCB06A34FAC04B9FD7F52E529E0399262A59FE7EE1FF19DEF323C93B5965B2675AFF84CE231EF359B73048AF8476E096D1BFCCDFE23AF7A9E3235EF71BD645EBE762D86E03F73CAD0BBA73D3F0F5D998D8F07A4FD2F
	7AC1DF0E4EEB8EB982A19FBA8517FD3AE4319CE7B0E2AE3C5D72C55B7DB1222C5D8516DEEDCE456353DC36AF4CFAF8FC1A2E73DFE27C6586BE5FC7EEAB25FDA4816A52BC149F2AA27AEC3F9762CC20F82035C009C0BB3DFACE55EF93963973F38602F6F4DC4395991F5F9DE56F6E1B77EF3C4BF63F3E6AF65A5DB95B93533C117ADC4578193EC8FD3EEEA3FD66C01D6AA3FDFEF78875399D4A29C0F9A3307E016A017AC7C81FF7DC426F8D226DBD1D01G4B60700C9B769E63B624796FC787222F130401455749AD
	8366315667879646DAEB874CE32DFF9CB046DA1C37C16BD751AF6FB87F6B833897709C39D5740B9E96D3F18C3E1F6D1F77G85C768CCABB3D49043DEF1A3463D60F48877020062839D98317DC208621E0491423B4013A8DF53029C7950E14483FE4FE0GF506692FAE38CF47B34BC46853B2C2F153B137204B33016DE3736226FA9FC44F7851FDE1394DD6DCF04CE6AB62187C3B8C4264D4DD46136C89B690F85FAFCB613EB3E7165237F63EB8034F63B61B9687BE6620310AC7A98E7CC5877A309B4A33C0892364
	43C9926E8B6BC675BB7F41BF4570D73C676719852D413DF346B538857A59B69A9B7F79382943B83F94F8B7D306FC84289A4889A48345812D14C9269B86E31DE169A6B62C9D9E1B7D874B63B6C2BF18EBF15EC30C1784BC4C5F785D6716036833BE3F6C43CE58DFE6689873A2FE2A287DFE5570FBDD69FBF1D79E9B5F3E793E0E41F5BBA7C64717E52279FD68C7C4736FA76245587714AB45685FE1FFCEB4FE072DFD83E18B6B7BD6436F233677DF77756F6B98DAECB294DD778F8EAD0E6DBF9EB24776C70696E27B
	7F8C19E3FB5A10915B35BB89851F00AF9A963D9242382ACE42CBD942A7B904B91BB11D8E5FD616F6B7F4BA1B6BEA6C83DD8EFB33D3201C3A0F1D69A306274804CB4D8A62D2419860AB10E30CF2238C2B91D73C401F9D0B0DAB3C5D6F9D46F67191ED5618744177FED7A74A31E9AC72CEF30C719347A26FB49F6D2033066DD0D765A72CCA16D0D7A18DA3B966644F918E210C1C774A1C0EC369E40B4707237BC316E7F1FFF846E36E8F07BC8B7D61C50F39BF94F8966E752F33FE954983FB0FF12FBF6AB15B6B7919
	0578E7540FF6A65563F32BDE4E4F1FFABA4CEF12F0FC5DF6037AFD74EA5067C67FFCDBA5C25982345B4067B67A8673214CC0246D6EE67C5CC0246D5A95325DEBD037BCC0397C8BD632D307156C74C7AB5969136944BB54C177D53B7436E2871D8D6C89108F7BBAF1EFAD8CD2B9444A75FA79FFBBE2E18C367B7F63D8EE677B7937CBFC6E1333F49E6E8E12BEFEBDCEBA1A52F9357A7949F21DF727C2E7649BF5DE5BB47B9B9AA86F50F5756F8A6AAED5AF5F4B4A7B4629FCC1F9AF71BAB5E81677795F53B1A2F54F
	4F414FF4603C0A47692CE42A23C02B9F9CDF688F6A38B1BE4EBB02EB0B1DD7B881A9EAA04695686FDA56F49A4F17170F73FB057563F4C7351A59F0AB14130138ACAFB39D6F51E539285FBF5B9413B3F90D477B7BB133EBAF705C8A453B52B4BB4B9127A24EF266183D63613DD57A3B0FCFA36670B81DFF724A6771FBA5721BF9BCCFCB545BB9BB2E664C056FCB2BBBA8DE36687DEC6F4032226193472EFB67661D439A3E49A5ED737EFE58C9F76F39A14A9BBA709ACC33534F46D10F31E27B63FAECFFD2397A79FC
	C0217958C36133B1FE6702FB0320659BC53E30CCF553CB1109377B62C838DD313214F6DC2E13A15A7F1705746F52BB0C7B75E660A70716324F6F680C167B6F0476E9C07BD433B5FC2DE2ECD7G6F142BCB992FB2C46D1AA25E6F07F290E89950CAC39D47324D9D3427FCB403F8F7F5501E9252F93F6924EFEC562904032B26756F04F55E5D6C9CF3A71453F45EAD8A0DF1CE5F377879B50E71CBFD5F6A8891CE6F1B2036D746E95C821DF7C026F14BF5DE559861F42F4E7BED00F0DA1EA09CA968C2DE9C14D7CC50
	378B9B26A92F5E843CA420DDC07BAEDB2C451749BF9F1C6167F9BE95C39A5E5723B314575BAFD37F53EC8E03D016F55E2DEC8E89D0DEFD1946AC14E94CA4606DG92A6E10EC0551354CEE2F39A04322C73DE8D323E203CFA12566E2D23E1FF3FF1FA6977088FCC53BACC1C24FB4483B311770857279B6F816BE6E25FA3CE4CC45EA3164C7BF66C7619E87708DC7EFD33489B966590BB23E7731B1643738B0C67C7DF4F301A89833EB696EE13A5AFDDA7ED327E5D68946964DAB9765D6AA6B936CEFEAA473EDB4D9F
	0B2693BE179B7C61396C8EACED7E1EC0380D4533D89B0EA18FCE4FBBAB07795B271602BDDD737E66E8905A280B34636B7351E94205929D97FEBBCD38F03D0E8BA37242F31200ECBCA7617F0F24EB86BE8DED56BDA26CD5053D6C331410A26C1FEF7374AFBC7F211C9BCECCE488D90DC29678D950ABB68BFB599FE959058CD7C8958E5B31C999B4A9F3E113B25630ABA83A24D3FD4213430AE7083D2E8A99FD0DFD5089AB0D8B6F88D63B70146A65DF84CF292A70FC38A7871E7BE8952726707F15A1BC1E1474F890
	7E0530E93A46C9DA40616FA842338B0DD7733187FE30EE20BEB7C348E896323CACDB0149CCE2BA4346F003AB45335E3297149217D4DAA8975633FFCB2A57CB5A7D6C6686637DD1FE6EF854EFDE37CFEBFF77BC9D7937F92F32B30142990B35FE06725A10D8A2C43F236D9A93E5D99565519A3F9B6FEBEB2F28D2785C27F0887B8F51BBDAA52FABE36792A306B67F87D0CB8788D072770EAD9AGG6CCBGGD0CB818294G94G88G88GA0EDAFA9D072770EAD9AGG6CCBGG8CGGGGGGGGGGGG
	GGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G81GBAGGGE79AGGGG
	**end of data**/
    }
    /**
     * Return the CloseButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getCloseButton() {
	if (ivjCloseButton == null) {
	    try {
		ivjCloseButton = new javax.swing.JButton();
		ivjCloseButton.setName("CloseButton");
		ivjCloseButton.setMnemonic('c');
		ivjCloseButton.setText("Close");
		ivjCloseButton.setBackground(new java.awt.Color(204,204,255));
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjCloseButton;
    }
    /**
     * Return the HSplitPane property value.
     * @return javax.swing.JSplitPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSplitPane getHSplitPane() {
	if (ivjHSplitPane == null) {
	    try {
		ivjHSplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
		ivjHSplitPane.setName("HSplitPane");
		ivjHSplitPane.setPreferredSize(new java.awt.Dimension(400, 100));
		ivjHSplitPane.setMinimumSize(new java.awt.Dimension(400, 100));
		ivjHSplitPane.setDividerLocation(200);
		getHSplitPane().add(getVariableScrollPane(), "left");
		getHSplitPane().add(getValueScrollPane(), "right");
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjHSplitPane;
    }
    /**
     * Return the JFrameContentPane property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getThreadCounterExampleContentPane() {
	if (ivjThreadCounterExampleContentPane == null) {
	    try {
		ivjThreadCounterExampleContentPane = new javax.swing.JPanel();
		ivjThreadCounterExampleContentPane.setName("ThreadCounterExampleContentPane");
		ivjThreadCounterExampleContentPane.setBorder(new javax.swing.border.EtchedBorder());
		ivjThreadCounterExampleContentPane.setLayout(new java.awt.BorderLayout());
		ivjThreadCounterExampleContentPane.setBackground(new java.awt.Color(204,204,255));
		getThreadCounterExampleContentPane().add(getCloseButton(), "South");
		getThreadCounterExampleContentPane().add(getVSplitPane(), "Center");
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjThreadCounterExampleContentPane;
    }
    /**
     * Return the ThreadScrollPane property value.
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getThreadScrollPane() {
	if (ivjThreadScrollPane == null) {
	    try {
		ivjThreadScrollPane = new javax.swing.JScrollPane();
		ivjThreadScrollPane.setName("ThreadScrollPane");
		//ivjThreadScrollPane.setPreferredSize(new java.awt.Dimension(400, 40));
		ivjThreadScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		//ivjThreadScrollPane.setMinimumSize(new java.awt.Dimension(200, 200));
		//ivjThreadScrollPane.setMaximumSize(new java.awt.Dimension(1000, 1000));
		getThreadScrollPane().setViewportView(getThreadTree());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjThreadScrollPane;
    }
    /**
     * Return the ThreadTree property value.
     * @return javax.swing.JTree
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTree getThreadTree() {
	if (ivjThreadTree == null) {
	    try {
		ivjThreadTree = new javax.swing.JTree();
		ivjThreadTree.setName("ThreadTree");
		//ivjThreadTree.setPreferredSize(new java.awt.Dimension(300, 100));
		ivjThreadTree.setBackground(new java.awt.Color(204,204,204));
		ivjThreadTree.setBounds(0, 0, 78, 72);
		//ivjThreadTree.setMinimumSize(new java.awt.Dimension(200, 50));
		//ivjThreadTree.setMaximumSize(new java.awt.Dimension(10000, 10000));
		// user code begin {1}
		ivjThreadTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
			public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
			    setHashColor(Color.black);
			    return this;
			}
		    }.setAngledColor());
		final Color slicedColor = new Color(150, 150, 150);
		ivjThreadTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		ivjThreadTree.setCellRenderer(new DefaultTreeCellRenderer() {
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
								      boolean expanded, boolean leaf, int row, boolean hasFocus) {
			    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			    setIcon(null);
			    Object o = ((DefaultMutableTreeNode) value).getUserObject();
			    setForeground(Color.black);
			    if (o instanceof Annotation) {
				Annotation a = (Annotation) o;
				if (!((DefaultMutableTreeNode) value).isRoot() &&
				    !(a instanceof LocalDeclarationStmtAnnotation) &&
				    (a.getStatements().length == 0)) {
				    setForeground(slicedColor);
				}
				else
				    if ((a instanceof LocalDeclarationStmtAnnotation) && (((LocalDeclarationStmtAnnotation) a).getDeclaredLocals().size() == 0)) {
					setForeground(slicedColor);
				    }
			    }
			    return this;
			}
		    });
		((DefaultTreeCellRenderer) ivjThreadTree.getCellRenderer()).setBackgroundNonSelectionColor(new Color(204, 204, 204));
		((DefaultTreeCellRenderer) ivjThreadTree.getCellRenderer()).setBackgroundSelectionColor(Preferences.getHighlightColor());
		ivjThreadTree.putClientProperty("JTree.lineStyle", "Angled");
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjThreadTree;
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
		ivjValueScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		getValueScrollPane().setViewportView(getValueTextArea());
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
     * Return the ValueTextArea property value.
     * @return javax.swing.JTextArea
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextArea getValueTextArea() {
	if (ivjValueTextArea == null) {
	    try {
		ivjValueTextArea = new javax.swing.JTextArea();
		ivjValueTextArea.setName("ValueTextArea");
		ivjValueTextArea.setBackground(new java.awt.Color(204,204,204));
		ivjValueTextArea.setBounds(0, 0, 406, 197);
		ivjValueTextArea.setEditable(false);
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjValueTextArea;
    }
    /**
     * Return the VariableScrollPane property value.
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getVariableScrollPane() {
	if (ivjVariableScrollPane == null) {
	    try {
		ivjVariableScrollPane = new javax.swing.JScrollPane();
		ivjVariableScrollPane.setName("VariableScrollPane");
		ivjVariableScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
		getVariableScrollPane().setViewportView(getVariableTree());
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjVariableScrollPane;
    }
    /**
     * Return the VariableTree property value.
     * @return javax.swing.JTree
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTree getVariableTree() {
	if (ivjVariableTree == null) {
	    try {
		ivjVariableTree = new javax.swing.JTree();
		ivjVariableTree.setName("VariableTree");
		ivjVariableTree.setBackground(new java.awt.Color(204,204,204));
		ivjVariableTree.setBounds(0, 0, 78, 72);
		// user code begin {1}
		ivjVariableTree.setUI(new javax.swing.plaf.metal.MetalTreeUI() {
			public javax.swing.plaf.metal.MetalTreeUI setAngledColor() {
			    setHashColor(Color.black);
			    return this;
			}
		    }.setAngledColor());
		ivjVariableTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		/*
		  ivjVariableTree.setCellRenderer(new DefaultTreeCellRenderer() {
		  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		  super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		  setIcon(null);
					
		  Object o = ((DefaultMutableTreeNode) value).getUserObject();
		  if (o instanceof ValueNode) {
		  ValueNode vn = (ValueNode) o;
		  o = vn.object;
		  if (o instanceof Local) {
		  Local lcl = (Local) o;
		  if ("JJJCTEMP$0".equals(lcl.getName()))
		  setText("this: " + lcl.getType());
		  else
		  setText(lcl.getName() + ": " + lcl.getType());
		  } else if (o instanceof SootField) {
		  SootField sf = (SootField) o;
		  setText(sf.getName() + ": " + sf.getType());
		  } else {
		  setText("" + vn.i);
		  }
		  }
					
		  return this;
		  }
		  });
		*/
		((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setLeafIcon(null);
		((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setOpenIcon(null);
		((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setClosedIcon(null);
		((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setBackgroundNonSelectionColor(new Color(204, 204, 204));
		((DefaultTreeCellRenderer) ivjVariableTree.getCellRenderer()).setBackgroundSelectionColor(Preferences.getHighlightColor());
		ivjVariableTree.putClientProperty("JTree.lineStyle", "Angled");
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjVariableTree;
    }
    /**
     * Return the VSplitPane property value.
     * @return javax.swing.JSplitPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JSplitPane getVSplitPane() {
	if (ivjVSplitPane == null) {
	    try {
		ivjVSplitPane = new javax.swing.JSplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
		ivjVSplitPane.setName("VSplitPane");
		ivjVSplitPane.setDividerLocation(100);
		getVSplitPane().add(getHSplitPane(), "top");
		getVSplitPane().add(getThreadScrollPane(), "bottom");
		// user code begin {1}
		// user code end
	    } catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	    }
	}
	return ivjVSplitPane;
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
	getCloseButton().addActionListener(ivjEventHandler);
	getVariableTree().addTreeSelectionListener(ivjEventHandler);
    }
    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
	try {
	    // user code begin {1}
	    // user code end
	    setName("ThreadCounterExample");
	    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	    setSize(425, 315);
	    setContentPane(getThreadCounterExampleContentPane());
	    initConnections();
	} catch (java.lang.Throwable ivjExc) {
	    handleException(ivjExc);
	}
	// user code begin {2}
	pack();
	// user code end
    }
    /**
     * main entrypoint - starts the part when it is run as an application
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args) {
	try {
	    ThreadCounterExample aThreadCounterExample;
	    aThreadCounterExample = new ThreadCounterExample(null, "", 0);
	    aThreadCounterExample.addWindowListener(new java.awt.event.WindowAdapter() {
		    public void windowClosing(java.awt.event.WindowEvent e) {
			System.exit(0);
		    };
		});
	    aThreadCounterExample.setVisible(true);
	} catch (Throwable exception) {
	    System.err.println("Exception occurred in main() of javax.swing.JFrame");
	    exception.printStackTrace(System.out);
	}
    }
    /**
     * 
     * @param model javax.swing.tree.TreeModel
     */
    public void setVariableTreeModel(TreeModel model) {
	getVariableTree().setModel(model);
	getValueTextArea().setText("");
    }
    /**
     * 
     * @param a edu.ksu.cis.bandera.annotation.Annotation
     */
    public void update(Annotation a) {
	JTree threadTree = getThreadTree();
	if (a == null) {
	    threadTree.setModel(null);
	    return;
	}
	Annotation ta = CompilationManager.getAnnotationManager().getMethodAnnotationContainingAnnotation(a);
	if ((a instanceof ConstructorDeclarationAnnotation) || (a instanceof MethodDeclarationAnnotation)
	    || (a instanceof FieldDeclarationAnnotation) || (a instanceof ClassDeclarationAnnotation)) {
	    ta = a;
	}
	threadTree.setModel(new DefaultTreeModel((DefaultMutableTreeNode) BUISessionPane.buildTree(ta).elementAt(0)));
	BUISessionPane.select(threadTree, a, false);
	for (int i = 0; i < threadTree.getRowCount(); i++) {
	    threadTree.expandRow(i);
	}
    }

    /**
     * This method will handle events from the user upon the variable tree.  It will grab the
     * node that is selected and display it's associated value in the value text area (if one exists).
     * It will also attempt to expand the node if it has children.
     */
    public void variableTree_TreeSelectionEvents() {
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) getVariableTree().getLastSelectedPathComponent();
	DefaultTreeModel model = (DefaultTreeModel) getVariableTree().getModel();
	
	// set the value of the text area to the value associated with the selected node.
	//String text = ce.traceManager.getValueText(model, node);
	String text = ce.traceManager.getValueText(node);
	getValueTextArea().setText(text);

	try {	    
	    // expand the children of the current selected node
	    java.util.List children = ce.traceManager.getNodeChildren(node);
	    if((children != null) && (children.size() > 0)) {
		log.debug("Found " + children.size() + " children for the current node.");
		Iterator ci = children.iterator();
		while(ci.hasNext()) {
		    Object child = ci.next();
		    if((child != null) && (child instanceof MutableTreeNode)) {
			MutableTreeNode mtn = (MutableTreeNode)child;
			node.add(mtn);
		    }
		}
	    }
	    else {
		log.debug("There are no children for the node.");
	    }
	}
	catch(Exception e) {
	    log.warn("An exception occured while expanding the children of a node.", e);
	}
	
	log.debug("Finished handling tree selection events.");
    }
}
