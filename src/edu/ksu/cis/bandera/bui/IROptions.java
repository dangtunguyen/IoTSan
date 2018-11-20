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
	/*
	* Note: Changes were made to generated portions of this class and should be fixed
	* before trying to integrate with VisualAge for Java again. 
	*/
import javax.swing.*;
import edu.ksu.cis.bandera.bir.*;
import edu.ksu.cis.bandera.birc.*;

import java.awt.GridBagConstraints;
import java.util.*;
import edu.ksu.cis.bandera.util.Preferences;

public class IROptions extends JDialog {
	private JCheckBox ivjBIRCCheckBox = null;
	private JCheckBox ivjBIRCheckBox = null;
	private JLabel ivjJavaLabel = null;
	private JPanel ivjJDialogContentPane = null;
	private JPanel ivjProgramsPanel = null;
	private JPanel ivjPathNamePanel = null;
	private JPanel ivjCompilerOptionsPanel = null;
	private JPanel ivjCompilerEditPanel = null;
	private JButton ivjSpinPathNameButton = null;
	private JButton ivjDSpinPathNameButton = null;
	private JButton ivjCCPathNameButton = null;
	private JButton ivjDefaultPathsButton = null;
	private JButton ivjDefaultCompilerOptionsButton = null;
	private JButton ivjEditWarningsSwitchButton = null;
	private JButton ivjEditOutputFileSwitchButton = null;
	private JTextField ivjWarningsSwitchTextField = null;
	private JTextField ivjOutputFileSwitchTextField = null;
	private JLabel ivjWarningsSwitchLabel = null;
	private JLabel ivjOutputFileSwitchLabel = null;
	private JLabel ivjJimpleLabel = null;
	private JTextArea ivjCompileOptionsMessageTextArea = null;
	private JCheckBox ivjJJJCCheckBox = null;
	private JButton ivjOkButton = null;
	private JLabel ivjOthersLabel = null;
	private JCheckBox ivjSLABSCheckBox = null;
	private JCheckBox ivjSLABSJavaCheckBox = null;
	private JCheckBox ivjSlicingCheckBox = null;
	private JCheckBox ivjSlicingJavaCheckBox = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private JPanel ivjJPanel1 = null;
	private JCheckBox ivjBSLCheckBox = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == IROptions.this.getOkButton()) 
				connEtoC4();
			if (e.getSource() == IROptions.this.getDSpinPathNameButton())
				connEtoC5();
			if (e.getSource() == IROptions.this.getSpinPathNameButton())
				connEtoC6();			
			if (e.getSource() == IROptions.this.getCCPathNameButton())
				connEtoC7();
			if (e.getSource() == IROptions.this.getDefaultPathsButton())
				connEtoC8();
			if (e.getSource() == IROptions.this.getEditOutputFileSwitchButton())
				connEtoC9();
			if (e.getSource() == IROptions.this.getEditWarningsSwitchButton())
				connEtoC10();
			if (e.getSource() == IROptions.this.getDefaultCompilerOptionsButton())
				connEtoC11();
			
		};
	};
	private JCheckBox ivjBSLJavaCheckBox = null;
/**
 * IROptions constructor comment.
 */
public IROptions() {
	super();
	initialize();
}
/**
 * IROptions constructor comment.
 * @param owner java.awt.Dialog
 */
public IROptions(java.awt.Dialog owner) {
	super(owner);
}
/**
 * IROptions constructor comment.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
public IROptions(java.awt.Dialog owner, String title) {
	super(owner, title);
}
/**
 * IROptions constructor comment.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
public IROptions(java.awt.Dialog owner, String title, boolean modal) {
	super(owner, title, modal);
}
/**
 * IROptions constructor comment.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
public IROptions(java.awt.Dialog owner, boolean modal) {
	super(owner, modal);
}
/**
 * IROptions constructor comment.
 * @param owner java.awt.Frame
 */
public IROptions(java.awt.Frame owner) {
	super(owner);
}
/**
 * IROptions constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
public IROptions(java.awt.Frame owner, String title) {
	super(owner, title);
}
/**
 * IROptions constructor comment.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
public IROptions(java.awt.Frame owner, String title, boolean modal) {
	super(owner, title, modal);
}
/**
 * IROptions constructor comment.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
public IROptions(java.awt.Frame owner, boolean modal) {
	super(owner, modal);
}
/**
 * connEtoC4:  (OkButton.action. --> IROptions.okButton_ActionEvents()V)
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4() {
	try {
		// user code begin {1}
		// user code end
		this.okButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
private void connEtoC5() {
	try {
		// user code begin {1}
		// user code end
		this.dspinPathNameButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
private void connEtoC6() {
	try {
		// user code begin {1}
		// user code end
		this.spinPathNameButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
private void connEtoC7() {
	try {
		// user code begin {1}
		// user code end
		this.ccPathNameButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
private void connEtoC8() {
	try {
		// user code begin {1}
		// user code end
		this.defaultPathsButton_ActionEvents();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
private void connEtoC9(){
	try{
		this.editOutputFileSwitchButton_ActionEvents();
	}catch(java.lang.Throwable ivjExc){
		handleException(ivjExc);
	}
}
private void connEtoC10(){
	try{
		this.editWarningsSwitchButton_ActionEvents();
	}catch(java.lang.Throwable ivjExc){
		handleException(ivjExc);
	}
}
private void connEtoC11(){
	try{
		this.defaultCompilerOptionsButton_ActionEvents();
	}catch(java.lang.Throwable ivjExc){
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (OkButton.action.actionPerformed(java.awt.event.ActionEvent) --> IROptions.dispose()V)
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
 * Return the BIRCCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getBIRCCheckBox() {
	if (ivjBIRCCheckBox == null) {
		try {
			ivjBIRCCheckBox = new javax.swing.JCheckBox();
			ivjBIRCCheckBox.setName("BIRCCheckBox");
			ivjBIRCCheckBox.setText("after checker");
			ivjBIRCCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjBIRCCheckBox.setActionCommand("BIRCCheckBox");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBIRCCheckBox;
}
/**
 * Return the BIRCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getBIRCheckBox() {
	if (ivjBIRCheckBox == null) {
		try {
			ivjBIRCheckBox = new javax.swing.JCheckBox();
			ivjBIRCheckBox.setName("BIRCheckBox");
			ivjBIRCheckBox.setOpaque(false);
			ivjBIRCheckBox.setText("BIR");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBIRCheckBox;
}
/**
 * Return the BSLCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getBSLCheckBox() {
	if (ivjBSLCheckBox == null) {
		try {
			ivjBSLCheckBox = new javax.swing.JCheckBox();
			ivjBSLCheckBox.setName("BSLCheckBox");
			ivjBSLCheckBox.setText("after BSL");
			ivjBSLCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBSLCheckBox;
}
/**
 * Return the BSLJavaCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getBSLJavaCheckBox() {
	if (ivjBSLJavaCheckBox == null) {
		try {
			ivjBSLJavaCheckBox = new javax.swing.JCheckBox();
			ivjBSLJavaCheckBox.setName("BSLJavaCheckBox");
			ivjBSLJavaCheckBox.setText("after BSL");
			ivjBSLJavaCheckBox.setBackground(new java.awt.Color(204,204,255));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBSLJavaCheckBox;
}
/**
 * 
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private static void getBuilderData() {
/*V1.1
**start of data**
	D0CB838494G88G88G35E0B5ABGGGGGGGGGGGG8CGGGE2F5E9ECE4E5F2A0E4E1F4E135BC8DD8D4571536F1D35B50EA9B5BD017EDE9C23BF65734A411463424EBD233C9365838E95A1A56A636212D59DA6BEE6DAEEDE972F078D37997A3A2BF9345868D6228E890090A0C869405GA28622BC18870C4C9FB3EFGAD15BD77675C7718F903E8D23F6FFCEF6E39777C5CF34FBD771C7B9ECA792511A9C96B73A5A9A9C5627F5ECA122402E6C9FAFB76213FA0AE5D92E312F47FDE874815DEB8168C
	4F8268EB3891E34A111EACCF07F685344B7E9AE35A8A3FB7CBF1652B4F400F90F216C0DF046731ADD34B79C9964AC9236D612C8CF8E681D8G578E90E9A172BF1ED54071E3504E623F89A42D17A46BFEB2A6D1AD506163793882682BGD8C547C4E595717EB0E813B1D170DC341F0DDD864F8DD43B6166EABEAEB67DB3126D401254CCA2CB479F41F20051367327A4B9D209A1CC52507BEF403336E5E86018E273D78E797C15FDF6DFE52F6C32A9DE393257EF2F29E9ECED7628F6374BA7ADB9C0745B61CF273420
	9F34DB8E925CBCBF31C9BA7CB683D49FC49D571C2B65BA747C660B12790525A1F51CFB106936682026E3C5652BE903D5047FD2EAF3C217G7D9640B1DD36E423AE8B2AD017A73DE4ECBC349381728FA17ED3BE022F003695207B906A7873632823E370BEA9FE5F332959A1F4D48FB15DA28EE9BA46A63C12360D7A453EA432364B21AF91A067F00C298A209DC08558CE7DF1C7769EF856752A75EE27536D2A73F8CEBAEC5EF3EE1B62005FB5B55043F12DCA1F5BEB13A466F3EF78AB8C74C188BBBC595F883039DD
	1450C7425707CBE325BF256B9EB8B7EB7F0329111C4742435A5C628F933BE50D903BBD873F13382D993E00624B5F5270EC2DA229AF10F6B574C53C05F61EDB0E3AE4FC66D3D244402329E9863A34BC79FC2AD44D77C535268B33F13E0958588A789E00B100B9G51G4B2A51460E6C03B733F123CBD53CAE5951666F7548DE75245F6EGFF67367EFE56D436B682C67733DB26E3F619FFD03FC6895FFC4C50AE1E5825292B39BD120FE8F6E1328B33C349AEBB926C9BD9E28D0F52BDD1FDA4F0EDCFD1FC43114035
	DD6A45356D043ED8GB6FE3DCA70661AC0BE9994EF298964B35707FCCEC0DF94GDB5F8713C96CCBBF8AB19C2096209DC085188390F5945778616CFD535CC7AD3A75E53455C92F41D399D33DF2234B274A2EBE25C3765A65DE07621378183AA4E2575EC9F427697E6E83996FA8AE080EA4A682F7592900B37958186624DCB2462B505EF63BD38181CE0F04FCAFA6EDA62BA67B548B9E1B2CAA5A8856FFA5097844085C819C01817CDEF714586B980D39CB60774A23E82B97D3882E3A9662DBAD5A6597C9F970BC8B
	4E2DB8EAEA2C76BE22066C3D06BE1ABFC24EB2DB2D084F1D68FB893F88171E7A46378DFD0F4002DA66C7CBEA83FD2F0472DCDD0B6763D77C1D37388F3238CE535FEB7FF6DBB2CC539C476C5B19C6FCB4636D98D3B9C0435BE8E373FAB3733D1332CFC1EFEB760223A8B64D62ECEC31091CA54A0862D22D0AC701D355257A78DA1751B560FE586F96BE320762C9DC2A87C771893C45144B7040C36F5558DFCD7BE5D5755AFB7D2A5226282A5DB520511F203A78A605BC9F1F6BFB69E42EBDB047B100D9E22ECF1BB6
	6B7C29496D9E72FB5A49CE61FE391F1E3F91B0FE615B387EF5BE628F4BC57BACEDEB7D5F77862E9D99FB2BEBB935FF2C8E12B15D351F3E0CF4154CEB03E8675176DAB14FF9BEA39F5D70216A7010210C8FEB2E4C87E213C37B70F7120DFD7869E4E39FFEA6F9EA9F7DD132110F323E9712C96E629EF569FC7E25E4926F7A5CAE17C2B71EA011D8FE3B261D4C436E322B976D3681C5ED33DFD7604CB9C6FC77820D2985703B62984643511D988FED9DF7CB163FFD4DB0DF1A137B23541947184F069F5362A153A9BE
	056CA54521B841D075EE3FCBE5787F22711A63FDA60A7B4B68D1F81A4B1B07CE7AD5556D2A1069B4AA4DFB779EB30B443B424D3B2F54516ED3E4ADFC653BBBF6D7717D7B498DC466A250A58EA07E986E5FFE1AB7617E65C12A5E6DF0B0F3C1DF77F1E20B6611FC5D7C920F313A2661B872391C16C356C59976ABF0DC12GF0D639464FBCA5ADAF202FC3F678353354193609D8C797BB983EA75D58CE7D69E49F8D897ED753D3A5F2A6B21EECDD23E542536335BBA15412E0486B2B06ECFDFD95857A13DAAC8E1EAB
	81986D15B63256DAD583B94989C2DB2FE25D15C0EC82B84B89C6EFBD41F3E3BA4EA16A1FFD82F787457D96B665534918571D4B5157E75D7F3954501728BC4EBF23AEB05FFDFCA32DE981DFDB07EB911F2E0F49F5BE1F3B4FCE538DEE573BB7687D0C6142B7EC04E73F5D6B035C57260C615AFCE183511564A39C4D6D7B2FEF3EAD8567D727ECF6556D45A32FFE147AE13B3B9ED22286D85EFA318DBB6C4AA850339A6D4E9134D7CB9D6E33F98774F561F8F7F46A2690361945EDB2DFE71B1CBF3C124674EC9DA6F6
	22756BD5F9C49E2B740DC2BC2929B9E3354B8E77005061C1FACE91592B6A309ED472E84EFA926AD50096G161BBAE84ECA788F82AE92006F11C27D9ED98EF8299E72D4G36173F2F12184CB7B037633CA3449FC3EDDFE68B296584673940BACF6A5C739D437AB34E9E1BDADD4FEC50D32F59E2EEEDCA7AFCGDE6F9D1864F3FF7EF5C81FDB40F9AC2E8F743987B2082DD6GFEEDBD7A5C360D39BA1F035A94526681584968C7CFEEB431BD4B5B5F5D1802FBD642336A2E11DB290F122D18672E49A4F6AF00F695C0
	93008DE02695F5EB5DF453FA6F545894755E4334F60A329A67A00BB8BE0EBFD7DA3166F91E7A4FBAEBE04D739245A71F8A2CF90672306649073E31D318A7F855C03B2CB8B5D9170400B6231B91C4D79950AEBABD190E79E4F6DB08BD431CD4423B2A424D7AD8DAF51A65F81D70F41C467B17BB8B092F103DBB73B42E17B51B4451D8E8AF87D88B10718E44D200A6G4FBB38EEDF5AE494E3A62FDB28G23653ECC664C9C22479C609DA9783F32A9D36733ADEE3BCB957E7A0D8CF226383D76813B8BF15F2238BE37
	17A698AC3F3D17462EE86099A7787EEFE668FC684F19467910A94BB89F5A10B5F5CE1F1B95AADFE27AFD0F46351520DBF2C30C0961F6D1DB94C05BA2F0D13984578E6DC1017B8D450DC3FBCE837A620BF9646C0D906D17E91B44B9FE7719B7D55DC568BEF8CE7B09A9E5FC505C361872F87A35ECEAF71FCF71D1A7334A2A4C7BFE25EE118CF24A73D068F3791B252F2A9B7818D32A3B3ECA6C2D5F6DAEB05EDBADB2F8A15BB3697969FCFCF4831EC54B371358308A5AD2E30C2920916DFDD7E1609E37C01F952093
	4081B023F1324D195F1C4F8E6D2397320DFD34AB1B6C971F5B6F6DD3827D301766D52CCF5B050D2A62A4E6E1E306A83DAAFBA14F8E2477D2FA56970A7E2F59D357869F4FA171F4CC34BFC95B57046D153AACA163144B46B7BC4B4F9E217B788DD83CC712F18DFF11CD565926744BFE87ECCB37CFE97670EB9D4047B592BAA98757EAEEA3660F8F0C12F8576F151D20DE1BC76913224E103135B998AFF320DD86E08530814CBC03B94237F2898F275BA6BB040CE7F3B1BFDAAD46A56711F12ADD25E7EC629972DE62
	D1F2ED45F53FE3187887732A7DFC5FE466877A4A09331AEF341F8D5E1B6C3CEAAAA23EB7F8161DD7A982FF116247F5F8F60E3D309D7323B950376EAC4613FB69D9505984FE590438E1B3414D00769C01FB1662A2203DD0608EF9B1E6BDA5F0AE0ADB876D02F3E8172DB9E40E3A2DD5F9516E3239C7412748CD1E6A2B34B63635B4371D3A52FC7ECAFD937CE0F9514FA4FDDEA44546074C0B4A4FB1BB5572E74266DD29EC5F76D2DFA0B4AA74ADB8C7F0EBF274B9A15171A76B59B94C7A4D397AFE625F6EA4FDFF6B
	24FE226BBF1B747DB3A775931D07B8BD1B571BDD18C7379F0E124A6F78E64877AB6B78FC8A4E33E7D4562ED454F93374F2AE09732A572FB03E7B2A7576DAD87CC0EAD2883EAD1CDF77F9AD0FE46BD7C6635C9C40C71D47B57D4AF6314E67B1463596915CEAE8E7B4A36EFD0AB3B733BA0C6162375F4E1D5FD400B23533674F9F3A8C93DFB74D560F50FD62F4A91074587BD88CD8C6F1AF82D81B51571FE2EB82011A6D7FC192DEB80F95A5241E6B157B06863CD0B95BB0167C096E2DF66051839059CC773757BC05
	FEE1FC7E0C6F5E9BA44838C066E9F71F5FC72AF5484EBCEE17C2E2E9FA8B61F70F1768352A99733DAD5B084CA6689BEC419CE97D56B76159A1DF8D2E9C815962F5C324D52FB1AC3DAD6076FFA267765E21243563BE187B7559D22763015075E48B2FA77953A231FD40667209C27A8E8F7A5681943C8BB9940095207BDD1CDB63D622A349CEC17DBAD5157B86E9250DC9ABA9FEB5B4B9A0986F7B89B9BF3BE6EF5D954ABE40618C4437665E2BF0C8364B83E2ED0E95127C54A1DFF37BE1ADD63CCB6F308A714C71C0
	FB96C0B44012F7F9ED5ACA46BC3A3D40408EFA7F5CACFD6CB0394361D5B384D3156623AE857B1FEDA7EB0C37D42CBE18DD94D89F90198E10B92395FD58E9A64FB75CEF70B36C2EE24D0777F8CB292F0D1AC9BC2F14C7554AFA37436DADBFE0292A20F06850EEBE566EA5FB288DE6BEA8563CF28C57FC75BB1F1362667EBCB5B7441A073772F7972DDA0C5266FE3F1750C152F0664C197A7AC125EF6824FB2CFC3710B36B20BE9646B7BE1AEA8AA1A70173CFEF534E8E365E055BC8DB64B64C6539AF3C3E0D44F8F2
	C7895637DF2789C89B8CF48DB03FF9290474D7834FF600B100B00048B67447AFEF5B417C514097CF598694BC9BD15E5D0521F343F905B7FFB7F6DFA10DBDEE7084A7610F782F9612BD2C3ABD9449F0DFABA4B604B2EBD0E5C3B9FEA10DF78E251F22D9DE7137C2FC479F8BF3DB57067956AFAB890D4829092354541CF4133B4CA6369958797CC4A11E4F956D18D77F0E7234C1FB9CE0EEBB5EBFDDB7216FA46E0E84EF78216139D9742DA5298B5A79F7976D1A6F4CCFF8A50D71795EE13D8F123ED067EF46859EEF
	AECC76C1267B13C5F8BF6539003A3F13C3F41F8F6D45G4BAFE03CDDEAB643F3AD34B3BAA0BE81B4GF48328G33GA2BB50B71EA9AE8F95E7G490CC87D43ABC3364C783F7ABA096F31024766129DD370A035B973D6DC5BDF1704762F7FA93939FF7D29045EDF02DF34DF73705AC0AA2575FAFF3FCF91FE1CD21AA3F00D3E96374FCEF68EBBC76E882A1BEE860C6E5337C9779F37CC47F26C811ACBAF6BE0B9F61240FBA8FE058E4F76489F449ED985FDCD9751A7F66DA431B89A5AF1G71G09GC51DB1266ACE
	DC47F9259669A677AD6DEECF93AC1CC3F7954178CF14917ECD40EB90A08CA08AE09140B241BF3E746094FE526AF65393059CAE60EBFA3763B60CAB525BB09E78A6F472FB71CBB1A67325D075CBDAAE31C955A5E62BED82DFC4719E9D1E59709C5DD7243DG7A465EC37D171BC37B6D0B661B7B6DAA33F1DCDCE38E0E0B3FB39B4745BF1875F1115FE3F13EDEF1894170492F13D8437201F332CB9E50555B269DE43F78783EE476F5527C037D9E517DFE3570434EF90377C741071ED76B014D39F2E76875BB345378
	4E6368CE63FB393A1DD35FC5B46E8C7D1E32E5E770FB4AD9E521F51BDDF6F35F8AAFB3762DAF14857B56FDE5463E7555B23DEF31FA6CA5FA6ED83BE2CC1DDDD8231DAEA638C1E80F8B5C93A5D833C78ADCE2B656F28B3B30C64B4CA5B1E6A53493GF23AE3CC95G8DGBDGB3GA2819681AC83D885A03D0F34BBA8ED9134EB813A814681A2GE281D6GA48294DD06FE00EE00714BC85B163378B6FD0768C3F4A53F09BED30DDD592575677079907DFB8C689A78BC75380A907C17F56BEB50BBEFFBAE44E73156
	5CFA13317AFBD4A21778A709091986E3B7D17F190C8B759D43969D7F4D529F0BD2A50CDDD8BF669653BB664BD8A33CDF9CDC572845D8DB94DC019C8720934081C06E84D82D3B0E7A64CC404D3902356E669C7394F30E3D222FF5633712F91D31BBBD8E25C96ED59C9CFF8FFDCF23AF86987EAEBB31D11B435E8721G3BB84D8F2D44EEEDCDF5A75BC40F28F3DA082DC8CD4724887957B6EB7CC8F7G4F47ED3948F3D2AF525FFD08685F2C8EC268516BDFE6A578130D2D817C8E96E4F07C64F96D5CC54737B5850C
	BFD91E4D70132433F72117A75DCBAD636B326E8A3EAB7D69FB7A7E749E56EF69417E319BC67DD6517F6D3DC67D1D227F2F93C6729D223F58E2C4BFC374BF65B76A1FAB7A5FABB162BFDF740FBB0D68E3C57F0345C67D4BC4FFDB09D1FF3C687F21A1FDC28F6E19B70ADDE4ED683EBAAD77C11EF52D12FEF5283413BBE39B6BA9B79B0DB825768D0A913BABF6500FE11D0A4D8EFD66D64563D5FCF0BA51B2542CFD6938E3B766CF91451803E64830E6B256E97BFB48FB3F00BA6D9D2F5DF6D29EE0059A2BF5620B31
	CE9B17315649ABA1BC97C2BB8EA09E005D972CA157A45C9F6F4A1A7EFD01A5074414FA05CCCD3C1B698F71EE06EE9E265BE769DDBC090709E2DE47BA369B5309BD41E8733B70DDE8D5AF3E9F7D7A8D2C6B9C82777B3D080B923887A7D06E8A01EB3760387CBE443D6AC7DC1340FD3A84E9E78A5CC3CE9C97ABF0AF97A3EE1D40FDC250D6589017A9F0B69B1E2F77965FDEDD94FD1BF4AF5FB29D4B796F24FE94E18B2C27E6D37CFCDBE0BDD5D0023E3C907A8A94716DDE2E117CF1051D9FF3609925983D135F5605
	F40BF47DF170FBA540BA0074FEA33A05B7102EC857EF613F2D706CB4247B63DE24F3687AE7705FF361B95F106E618923794572314B6099EFC8F74902F4893A7E0C81765B8C4F2A81A33AE4716DC7032E3F1B7F7640F326A15D67CA0C748C67E39740F331A15DA2A752BD276BDF45FFA742B3E75008EE0D215F154158EA00A6CE532323E5F4338D75D4E15CAC000841603C0FFFB3EAC8978D6317GAC8FC997D6A2095F1F557DAEA9797B57F2EF7DDDE5305A6201DCFCEF38FA906B0D5773305EC890B8C79641B558
	A18737A36EA80D43AA34E7895CBFF4A13F88017BE59756B473856ED0A64A08953857EE60FB480C2B087B6E8D14D1A4F0B993A843A2F04393482FD660143D48AFCC60F2AB11DF0440F5D4A23FE8011B35DF68A7F0BBA604FEC3087B1E5025C86076C9C8DBAEF08F6CC37B0D891C47023A0489DC0C8579C58A5CEBC2178582677223AE098237FD8479E5B89057B3027C4C82775888722B90387FABC6FE6382F7FF89520689DCD6A952068BDC0A936736CA601EF122AE0982176FC2FEB9CE4479DD482FC860AE8BDD9C
	8217C2F59907F61840DD2538F0E8C78B5C7B9E6437C4601E277C72DD60AFAE71FE145256C23BC960B65139F5C25BA3F0D641EF26406D96344F899CBB5F896DCA01ABF6A26DBA9766D9BF21FFEB9966A67D55F9D63E972D6E607803672709C3A70CF1GEC616D99EE6D0C655FAAE451EFCF5DDEE5723B4AFF24FB77B9983F520DF9EDEB973D67754843647D889BE76F42FCD172203E8F11BFDB647D595D540E50E786E0F3E84A44B9B4F90C67E052C18F0CA1C066C03F3B70E8F3E0B5EB438DD2334E70085A73866A
	94A9F47AD239E643D993A8FFE18879E9D348FFAAC87E277692794F8979B515A8FF1D10FFED37A67F0B7BD1FE7A30317C8DD348AF9A8E14FFF60248AF9FC6D96F6E51E41D16D0D60301ACFCD706323A07A74B7268E431397DF89F7A4578B04E7773961CEF0450E1771B1A8EFB45FC23C368B055FAAF891A6FBDFEB25FE5C23EF5C458DB48CF2A5064AF9F9176769A4B1FEA3D0B3C01725FAC217676222C67F532F6150A7DE9A08B3FEB917B53BBD916439BE86FCAA75AFB4C0B73CDF062FC43058E3F5F2B699066C2
	9D960450E1AAFBAF8E1A6F47681DD01C9763C0D14964EF9F16D6CA9246130F0478766173B456AD879E71DE0C832A87754C71996F03CDBABD4BE08C8154335A97984B569613FB9E760DB7F90DA293717C5DF637C5537151G7D2B76EBFDBBCA0D752F21F15E8AB2DBFC28FF1750FFBC047EFADB4F01B1F3F47AC76974E7FAE49C10443ABFC46D958DE3E2053CAAA74ADBE5A04FF493F909BAF92C1634676177F369EA0C29D4E2770884477E2DBD9E0CC3DDFFCBF5E56FD90BFC7A772C361F46256E140C5F331ED0F9
	0D266A7E0E791932F446F3DF15368E5F21F7D4136F92AAFB9D30AE2CFFC697513BDFF67834EF704A8FE974FBB227F39F416C7037CC3443D306F6C07E9507B57E17699D9979232549D70E653BD97F26893DFECC4EA393A8C772633E4DFECB639935B70F7354DFD5120F9DD87F3B1541BC176DC71E5502672FF4BC65897E7D047EEA11FEBF4168C7A52477780DFD00717CEE354653E419FAED22AD41FADE96FA4E97FAFE6D084673FE7FEB3A396BAFB53579F70D84735DB00AFCD78A3EF756E8FC6BCAF234798B0E0C
	362394E98BC6261A7B65239A3F0B4E29671E6D8A5671D1974AE99991BA56EABC7F29D873310937B5FC2BE77ABE7CE58F4A18E5B89764FF711876FB24D313F56496E40589D90B2714F57E12763B75BDCDD61EF37A3294A74ACA90363B5AC5F831FA5715272F51AD23B1A66B68546F09785F8FF713D85A89E3D500F000E800A5GABG5682A40F45188AGACG8DG5DG9E0019GF3816683440E617B692A03E4CF05FA3F4EAFDAF5EF55D17F0FE66A75DF867C624726561F51FDFB529DD18250E4DC1B8E5D8F766B
	694CC0D3B5AD3AF1C9CF57GB45D53226B1BA44F83B4B327C517B52A278B871A8553226BAA5553AD861A6726C57738CBCF378AE89227C577AF9EBDDD4EF548D12ECF076E5313682A0126E9DAF4D71DFA3A9E20D1EFC2476FD868D9BF6BBA4B81F87DC9F1119C477FFF0E6E506F42FD5DB7FF97BE5EED7CAE7C235D416F42BF56ED7CAE7C135D415FD9FC261B44008158C71DE25CBD82F7C960A2A8CF023BE837516F91997EDE2A9741B7A8C4A06267D33DC6A92E13636E5761AEF15C035D247605A98F3A3DC87BF0
	B749A37B6D8E877F9BA5BA466E72A92A3047E22A235DA36B74FEC260AE093578FCE6707AFCA1D33FBE8C77030960F1BF1E889E37FAFF70387F5E9FBC6EEEA9F8DC3894BC6E3A813F8983FE3BC603476D998D9E67AD8D9EB7DA9ABC6EF9D7703897DC41639E73840F7BF7CF70387986636EB7987791E77038BB1D7AF1AC57167F12A1096FC3294BB1FC51D82606C71713587FFB75235A82B845967CC6CAD125EC76924FE166FF702CCA6B1B07FCDEA9EB72511874652DF7644CBE68DFAF251E16D2A13D708C4A0DD2
	8A7D9B4EA6E9FD9F50EDEFA2A47B00E4DF9FA159C7895BBCF21FF234CB5A506F9D682543C1C25769AEE0C2DB43528DA939C95A288E520F3FE0F32B52D60DD38BB92CF84751B172DF25C82F46453D6A01FFBEC23ADE0FD125B476423EC61552591F6F498ED54A147BD545EB7631C4D915F2D9DB6EA5E7A1AD56D4A905E4FCB5539B1B465E2A43E893A4F502260FE4F70AD715B274292EAAEDE05DA75B1A743F41445942440DF44B2E9CB9D8E3FA69BE53FAA939D1CAE5A12CD1CAA6F188CCCD43D423E40290466CG
	7FB662D3E78BGE36BD04C16G4A148F0C5D7801B8ECB46430D10C1D6E8CBE44F12C260CBD257F2E3BA87F0910FF9394F6031DFF11B7827FD67A874749FEDD8178049B18276DBACEAECAC2FD5B5F66149D8ED5F68C5539EC44A3CFDED3956D3B329F9FA77CFDB806F7323E1F9DA7FADF55517CBFD0CB8788CCE494145A9AGGC0CFGGD0CB818294G94G88G88G35E0B5ABCCE494145A9AGGC0CFGG8CGGGGGGGGGGGGGGGGGE2F5E9ECE4E5F2A0E4E1F4E1D0CB8586GGGG81G
	81GBAGGG949BGGGG
**end of data**/
}
/**
 * Return the JavaLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJavaLabel() {
	if (ivjJavaLabel == null) {
		try {
			ivjJavaLabel = new javax.swing.JLabel();
			ivjJavaLabel.setName("JavaLabel");
			ivjJavaLabel.setText("Java:");
			ivjJavaLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJavaLabel;
}
/**
 * Return the JDialogContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJDialogContentPane() {
	if (ivjJDialogContentPane == null) {
		try {
			ivjJDialogContentPane = new javax.swing.JPanel();
			ivjJDialogContentPane.setName("JDialogContentPane");
			ivjJDialogContentPane.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),"Intermediate Representation Generation"));
			ivjJDialogContentPane.setLayout(new java.awt.GridBagLayout());
			ivjJDialogContentPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsJimpleLabel = new java.awt.GridBagConstraints();
			constraintsJimpleLabel.gridx = 0; constraintsJimpleLabel.gridy = 0;
			constraintsJimpleLabel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJimpleLabel.weightx = 1.0;
			constraintsJimpleLabel.insets = new java.awt.Insets(10, 10, 0, 0);
			getJDialogContentPane().add(getJimpleLabel(), constraintsJimpleLabel);

			java.awt.GridBagConstraints constraintsJJJCCheckBox = new java.awt.GridBagConstraints();
			constraintsJJJCCheckBox.gridx = 0; constraintsJJJCCheckBox.gridy = 1;
			constraintsJJJCCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJJJCCheckBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getJDialogContentPane().add(getJJJCCheckBox(), constraintsJJJCCheckBox);

			java.awt.GridBagConstraints constraintsSlicingCheckBox = new java.awt.GridBagConstraints();
			constraintsSlicingCheckBox.gridx = 0; constraintsSlicingCheckBox.gridy = 3;
			constraintsSlicingCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSlicingCheckBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getJDialogContentPane().add(getSlicingCheckBox(), constraintsSlicingCheckBox);

			java.awt.GridBagConstraints constraintsSLABSCheckBox = new java.awt.GridBagConstraints();
			constraintsSLABSCheckBox.gridx = 0; constraintsSLABSCheckBox.gridy = 4;
			constraintsSLABSCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSLABSCheckBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getJDialogContentPane().add(getSLABSCheckBox(), constraintsSLABSCheckBox);

			java.awt.GridBagConstraints constraintsJavaLabel = new java.awt.GridBagConstraints();
			constraintsJavaLabel.gridx = 1; constraintsJavaLabel.gridy = 0;
			constraintsJavaLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJavaLabel.weightx = 1.0;
			constraintsJavaLabel.insets = new java.awt.Insets(10, 10, 0, 0);
			getJDialogContentPane().add(getJavaLabel(), constraintsJavaLabel);

			java.awt.GridBagConstraints constraintsSlicingJavaCheckBox = new java.awt.GridBagConstraints();
			constraintsSlicingJavaCheckBox.gridx = 1; constraintsSlicingJavaCheckBox.gridy = 1;
			constraintsSlicingJavaCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSlicingJavaCheckBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getJDialogContentPane().add(getSlicingJavaCheckBox(), constraintsSlicingJavaCheckBox);

			java.awt.GridBagConstraints constraintsSLABSJavaCheckBox = new java.awt.GridBagConstraints();
			constraintsSLABSJavaCheckBox.gridx = 1; constraintsSLABSJavaCheckBox.gridy = 3;
			constraintsSLABSJavaCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsSLABSJavaCheckBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getJDialogContentPane().add(getSLABSJavaCheckBox(), constraintsSLABSJavaCheckBox);

			java.awt.GridBagConstraints constraintsOthersLabel = new java.awt.GridBagConstraints();
			constraintsOthersLabel.gridx = 2; constraintsOthersLabel.gridy = 0;
			constraintsOthersLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsOthersLabel.weightx = 1.0;
			constraintsOthersLabel.insets = new java.awt.Insets(10, 10, 0, 10);
			getJDialogContentPane().add(getOthersLabel(), constraintsOthersLabel);

			java.awt.GridBagConstraints constraintsBIRCheckBox = new java.awt.GridBagConstraints();
			constraintsBIRCheckBox.gridx = 2; constraintsBIRCheckBox.gridy = 1;
			constraintsBIRCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsBIRCheckBox.insets = new java.awt.Insets(5, 10, 0, 10);
			getJDialogContentPane().add(getBIRCheckBox(), constraintsBIRCheckBox);

			java.awt.GridBagConstraints constraintsBIRCCheckBox = new java.awt.GridBagConstraints();
			constraintsBIRCCheckBox.gridx = 0; constraintsBIRCCheckBox.gridy = 5;
			constraintsBIRCCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsBIRCCheckBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getJDialogContentPane().add(getBIRCCheckBox(), constraintsBIRCCheckBox);

			java.awt.GridBagConstraints constraintsBSLCheckBox = new java.awt.GridBagConstraints();
			constraintsBSLCheckBox.gridx = 0; constraintsBSLCheckBox.gridy = 2;
			constraintsBSLCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsBSLCheckBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getJDialogContentPane().add(getBSLCheckBox(), constraintsBSLCheckBox);

			java.awt.GridBagConstraints constraintsBSLJavaCheckBox = new java.awt.GridBagConstraints();
			constraintsBSLJavaCheckBox.gridx = 1; constraintsBSLJavaCheckBox.gridy = 2;
			constraintsBSLJavaCheckBox.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsBSLJavaCheckBox.insets = new java.awt.Insets(5, 10, 0, 0);
			getJDialogContentPane().add(getBSLJavaCheckBox(), constraintsBSLJavaCheckBox);
			// user code begin {1}
			((javax.swing.border.TitledBorder) ivjJDialogContentPane.getBorder()).setTitleColor(java.awt.Color.black);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJDialogContentPane;
}
/**
 * Return the JimpleLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJimpleLabel() {
	if (ivjJimpleLabel == null) {
		try {
			ivjJimpleLabel = new javax.swing.JLabel();
			ivjJimpleLabel.setName("JimpleLabel");
			ivjJimpleLabel.setText("Jimple:");
			ivjJimpleLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJimpleLabel;
}
/**
 * Return the JJJCCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getJJJCCheckBox() {
	if (ivjJJJCCheckBox == null) {
		try {
			ivjJJJCCheckBox = new javax.swing.JCheckBox();
			ivjJJJCCheckBox.setName("JJJCCheckBox");
			ivjJJJCCheckBox.setOpaque(false);
			ivjJJJCCheckBox.setText("original");
			ivjJJJCCheckBox.setBackground(new java.awt.Color(204,204,255));
			ivjJJJCCheckBox.setNextFocusableComponent(getSlicingCheckBox());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJJJCCheckBox;
}
/**
 * Return the JPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel1() {
	if (ivjJPanel1 == null) {
		try {
			ivjJPanel1 = new javax.swing.JPanel();
			ivjJPanel1.setName("JPanel1");
			ivjJPanel1.setLayout(new java.awt.BorderLayout());
			getJPanel1().add(getJDialogContentPane(), "North");
			getJPanel1().add(getProgramsPanel(), "Center");
			getJPanel1().add(getOkButton(), "South");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanel1;
}
/**
 * Return the OkButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JButton getOkButton() {
	if (ivjOkButton == null) {
		try {
			ivjOkButton = new javax.swing.JButton();
			ivjOkButton.setName("OkButton");
			ivjOkButton.setMnemonic('o');
			ivjOkButton.setText("Ok");
			ivjOkButton.setBackground(new java.awt.Color(204,204,255));
			ivjOkButton.setNextFocusableComponent(getJJJCCheckBox());
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
 * Return the OthersLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getOthersLabel() {
	if (ivjOthersLabel == null) {
		try {
			ivjOthersLabel = new javax.swing.JLabel();
			ivjOthersLabel.setName("OthersLabel");
			ivjOthersLabel.setText("Others:");
			ivjOthersLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOthersLabel;
}
/**
 * Return the SLABSCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getSLABSCheckBox() {
	if (ivjSLABSCheckBox == null) {
		try {
			ivjSLABSCheckBox = new javax.swing.JCheckBox();
			ivjSLABSCheckBox.setName("SLABSCheckBox");
			ivjSLABSCheckBox.setOpaque(false);
			ivjSLABSCheckBox.setText("after abstraction");
			ivjSLABSCheckBox.setNextFocusableComponent(getBIRCCheckBox());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSLABSCheckBox;
}
/**
 * Return the SLABSJavaCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getSLABSJavaCheckBox() {
	if (ivjSLABSJavaCheckBox == null) {
		try {
			ivjSLABSJavaCheckBox = new javax.swing.JCheckBox();
			ivjSLABSJavaCheckBox.setName("SLABSJavaCheckBox");
			ivjSLABSJavaCheckBox.setOpaque(false);
			ivjSLABSJavaCheckBox.setText("after abstraction");
			ivjSLABSJavaCheckBox.setNextFocusableComponent(getBIRCheckBox());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSLABSJavaCheckBox;
}
/**
 * Return the SlicingCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getSlicingCheckBox() {
	if (ivjSlicingCheckBox == null) {
		try {
			ivjSlicingCheckBox = new javax.swing.JCheckBox();
			ivjSlicingCheckBox.setName("SlicingCheckBox");
			ivjSlicingCheckBox.setOpaque(false);
			ivjSlicingCheckBox.setText("after slicing");
			ivjSlicingCheckBox.setNextFocusableComponent(getSLABSCheckBox());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSlicingCheckBox;
}
/**
 * Return the SlicingJavaCheckBox property value.
 * @return javax.swing.JCheckBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public javax.swing.JCheckBox getSlicingJavaCheckBox() {
	if (ivjSlicingJavaCheckBox == null) {
		try {
			ivjSlicingJavaCheckBox = new javax.swing.JCheckBox();
			ivjSlicingJavaCheckBox.setName("SlicingJavaCheckBox");
			ivjSlicingJavaCheckBox.setOpaque(false);
			ivjSlicingJavaCheckBox.setText("after slicing");
			ivjSlicingJavaCheckBox.setNextFocusableComponent(getSLABSJavaCheckBox());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjSlicingJavaCheckBox;
}
/**
 * 
 * @return
 */
public javax.swing.JButton getSpinPathNameButton(){
	if (ivjSpinPathNameButton == null){
		ivjSpinPathNameButton = new JButton("spin");
		ivjSpinPathNameButton.setName("spinPathNameButton");
	}
	return ivjSpinPathNameButton;
}
/**
 * 
 * @return
 */
public javax.swing.JButton getDSpinPathNameButton(){
	if(ivjDSpinPathNameButton == null){
		ivjDSpinPathNameButton = new JButton("dspin");
		ivjDSpinPathNameButton.setName("dspinPathNameButton");
	}
	return ivjDSpinPathNameButton;
}
public javax.swing.JLabel getWarningsSwitchLabel(){
	if (ivjWarningsSwitchLabel ==  null){
		ivjWarningsSwitchLabel = new JLabel("disable warnings (default -w)");
		ivjWarningsSwitchLabel.setName("WarningsSwitch");
		ivjWarningsSwitchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	}
	
	return ivjWarningsSwitchLabel;
}
public javax.swing.JTextField getWarningsSwitchTextField(){
	if (ivjWarningsSwitchTextField == null){
		ivjWarningsSwitchTextField = new JTextField(15);
		ivjWarningsSwitchTextField.setText(Preferences.getCCWarningsFlag());
		ivjWarningsSwitchTextField.setEditable(false);
	}
	return ivjWarningsSwitchTextField ;
}
public javax.swing.JButton getEditWarningsSwitchButton(){
	if (ivjEditWarningsSwitchButton == null){
		ivjEditWarningsSwitchButton = new JButton("Edit");
		ivjEditWarningsSwitchButton.setName("editWarningsSwitchButton");
	}
	return ivjEditWarningsSwitchButton;
}
public javax.swing.JButton getEditOutputFileSwitchButton(){
	if (ivjEditOutputFileSwitchButton == null){
		ivjEditOutputFileSwitchButton = new JButton("Edit");
		ivjEditOutputFileSwitchButton.setName("editOutputFileSwitchButton");
	}
	return ivjEditOutputFileSwitchButton;
}
public javax.swing.JLabel getOutputFileSwitchLabel(){
	if (ivjOutputFileSwitchLabel == null){
		ivjOutputFileSwitchLabel = new JLabel("file output (default -o)");
		ivjOutputFileSwitchLabel.setName("fileOutputSwitch");
		ivjOutputFileSwitchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
	}
	return ivjOutputFileSwitchLabel;
}
public javax.swing.JTextField getOutputFileSwitchTextField(){
	if (ivjOutputFileSwitchTextField == null){
		ivjOutputFileSwitchTextField = new JTextField(15);
		ivjOutputFileSwitchTextField.setText(Preferences.getCCOutputFileFlag());
		ivjOutputFileSwitchTextField.setEditable(false);
	}
	return ivjOutputFileSwitchTextField;
}
/**
 * 
 * @return
 */
public javax.swing.JButton getCCPathNameButton(){
	if(ivjCCPathNameButton == null){
		ivjCCPathNameButton = new JButton("C compiler");
		ivjCCPathNameButton.setName("ccPathNameButton");
	}
	return ivjCCPathNameButton;
}
public javax.swing.JButton getDefaultPathsButton(){
	if (ivjDefaultPathsButton == null){
		ivjDefaultPathsButton = new JButton("reset spin, dspin and C compiler defaults");
		ivjDefaultPathsButton.setName("resetDefaultPathsButton");
	}
	return ivjDefaultPathsButton;
}
public javax.swing.JButton getDefaultCompilerOptionsButton(){
	if (ivjDefaultCompilerOptionsButton == null){
		ivjDefaultCompilerOptionsButton = new JButton("reset compiler switch defaults");
		ivjDefaultCompilerOptionsButton.setName("resetDefaultCompilerOptions");
	}
	
	return ivjDefaultCompilerOptionsButton;
}
public javax.swing.JPanel getCompilerOptionsPanel(){
	if(ivjCompilerOptionsPanel == null){
		ivjCompilerOptionsPanel = new JPanel();
		ivjCompilerOptionsPanel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),"Compiler options"));
		ivjCompilerOptionsPanel.setLayout(new java.awt.GridBagLayout());
		
		java.awt.GridBagConstraints constraintsCompilerOptionsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsCompilerOptionsMessageTextArea.gridx = 0; constraintsCompilerOptionsMessageTextArea.gridy = 0;
		constraintsCompilerOptionsMessageTextArea.weighty = 0.1;
		constraintsCompilerOptionsMessageTextArea.insets = new java.awt.Insets(4,4,4,4);
		ivjCompilerOptionsPanel.add(getCompilerOptionsMessageTextArea(),constraintsCompilerOptionsMessageTextArea );
		
		java.awt.GridBagConstraints constraintsCompilerEditPanel = new java.awt.GridBagConstraints();
		constraintsCompilerEditPanel.gridx = 0; constraintsCompilerEditPanel.gridy = 1;
		constraintsCompilerEditPanel.weighty = 0.8;
		constraintsCompilerEditPanel.weightx = 1;
		constraintsCompilerEditPanel.insets = new java.awt.Insets(15,4,15,4);
		ivjCompilerOptionsPanel.add(getCompilerEditPanel(), constraintsCompilerEditPanel);
		
		java.awt.GridBagConstraints constraintsDefaultCompilerOptionsButton = new java.awt.GridBagConstraints();
		constraintsDefaultCompilerOptionsButton.gridx = 0; constraintsDefaultCompilerOptionsButton.gridy = 2;
		constraintsDefaultCompilerOptionsButton.weighty = 0.1;
		constraintsDefaultCompilerOptionsButton.insets = new java.awt.Insets(4,4,4,4);
		ivjCompilerOptionsPanel.add(getDefaultCompilerOptionsButton(), constraintsDefaultCompilerOptionsButton);
		
	}
	
	return ivjCompilerOptionsPanel;
}
public javax.swing.JPanel getCompilerEditPanel(){
	if (ivjCompilerEditPanel == null){
		ivjCompilerEditPanel = new JPanel();
		ivjCompilerEditPanel.setLayout(new java.awt.GridLayout(1,2,25,25));
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new java.awt.GridLayout(3,1,2,2));
		leftPanel.add(getWarningsSwitchLabel());
		leftPanel.add(getWarningsSwitchTextField());
		leftPanel.add(getEditWarningsSwitchButton());
		getCompilerEditPanel().add(leftPanel);
		
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new java.awt.GridLayout(3,1,2,2));
		rightPanel.add(getOutputFileSwitchLabel());
		rightPanel.add(getOutputFileSwitchTextField());
		rightPanel.add(getEditOutputFileSwitchButton());
		getCompilerEditPanel().add(rightPanel);
	}
	return ivjCompilerEditPanel;
	
}
public javax.swing.JTextArea getCompilerOptionsMessageTextArea(){
	if(ivjCompileOptionsMessageTextArea == null){
		ivjCompileOptionsMessageTextArea = new JTextArea();
		ivjCompileOptionsMessageTextArea.setEditable(false);
		ivjCompileOptionsMessageTextArea.setBackground(new java.awt.Color(204,204,255));
		ivjCompileOptionsMessageTextArea.setText("Use these if your compiler has different flags for turning off warnings or \n" +																		  "setting the output file.  Note that if you do change these settings they \n" +																		  "need to have the \"-\" in front of the command ie \"-w\" not just \"w\". ");
	}
	return ivjCompileOptionsMessageTextArea;
}
public javax.swing.JPanel getProgramsPanel(){
	if(ivjProgramsPanel == null){
		ivjProgramsPanel = new JPanel();
		ivjProgramsPanel.setName("programsPanel");
		ivjProgramsPanel.setLayout(new java.awt.GridLayout(1,2,2,2));
		
		getProgramsPanel().add(getPathNamePanel());
		getProgramsPanel().add(getCompilerOptionsPanel());

	}
	return ivjProgramsPanel;
	
}
/**
 * 
 * @return
 */
public javax.swing.JPanel getPathNamePanel(){
	if(ivjPathNamePanel == null){
		ivjPathNamePanel = new JPanel();
		ivjPathNamePanel.setName("pathNamePanel");
		ivjPathNamePanel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),"Set alternate pathnames for external programs"));
		ivjPathNamePanel.setLayout(new java.awt.GridLayout(2,1,2,2));

		JPanel panel1 = new JPanel();
		panel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 20,0));		
		panel1.add(getSpinPathNameButton());
		panel1.add(getDSpinPathNameButton());
		panel1.add(getCCPathNameButton());

		JPanel panel2 = new JPanel();
		
		panel2.add(getDefaultPathsButton());
		getPathNamePanel().add(panel1);
		getPathNamePanel().add(panel2);
	}
	return ivjPathNamePanel;
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
	getOkButton().addActionListener(ivjEventHandler);
	getSpinPathNameButton().addActionListener(ivjEventHandler);
	getDSpinPathNameButton().addActionListener(ivjEventHandler);
	getCCPathNameButton().addActionListener(ivjEventHandler);
	getDefaultPathsButton().addActionListener(ivjEventHandler);
	getEditWarningsSwitchButton().addActionListener(ivjEventHandler);
	getEditOutputFileSwitchButton().addActionListener(ivjEventHandler);
	getDefaultCompilerOptionsButton().addActionListener(ivjEventHandler);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("IROptions");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setSize(467, 365);
		setModal(true);
		setTitle("General Options");
		setContentPane(getJPanel1());
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
		IROptions aIROptions;
		aIROptions = new IROptions();
		aIROptions.setModal(true);
		aIROptions.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aIROptions.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JDialog");
		exception.printStackTrace(System.out);
	}
}
/**
 * Comment
 */
public void okButton_ActionEvents() {
	dispose();
}
public void getFileSelector(){
	
}
public String selectFile(String currentPathName){
	java.io.File currentFile = null;
	try {currentFile = new java.io.File(currentPathName);}
	catch(java.lang.NullPointerException e)	{		}
	JFileChooser fileChooser = new JFileChooser(currentFile);
	fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	fileChooser.setMultiSelectionEnabled(false);
	int response = fileChooser.showOpenDialog(this);
	if(response == JFileChooser.APPROVE_OPTION){
		java.io.File file = fileChooser.getSelectedFile();
		String pathName = file.getAbsoluteFile().getPath();
		return pathName;
	}
	return null;
	
}
public void dspinPathNameButton_ActionEvents(){
	String pathName = selectFile(Preferences.getDSpinAlias());
	if (pathName != null){
		Preferences.setDSpinAlias(pathName);
	}
}
public void spinPathNameButton_ActionEvents(){
	String pathName = selectFile(Preferences.getSpinAlias());
	if (pathName != null){
		Preferences.setSpinAlias(pathName);
	}
}
public void ccPathNameButton_ActionEvents(){
	String pathName = selectFile(Preferences.getCCAlias());
	if (pathName != null){
		Preferences.setCCAlias(pathName);
		
	}
}
public void defaultPathsButton_ActionEvents(){
	Preferences.setCCAlias(Preferences.defaultCCAlias);
	Preferences.setDSpinAlias(Preferences.defaultDSpinAlias);
	Preferences.setSpinAlias(Preferences.defaultSpinAlias);
	
}
public void editWarningsSwitchButton_ActionEvents(){
	String newFlag = JOptionPane.showInputDialog("Enter the disable warnings flag");
	newFlag = newFlag.trim();
	if (newFlag.charAt(0) != '-'){
		newFlag = "-" + newFlag;
	}
	Preferences.setCCWarningsFlag(newFlag);
	getWarningsSwitchTextField().setText(Preferences.getCCWarningsFlag());
}
public void editOutputFileSwitchButton_ActionEvents(){
	String newFlag = JOptionPane.showInputDialog("Enter the output file flag");
	newFlag = newFlag.trim();
	if (newFlag.charAt(0) != '-'){
		newFlag = "-" + newFlag;
	}
	Preferences.setCCOutputFileFlag(newFlag);
	getOutputFileSwitchTextField().setText(Preferences.getCCOutputFileFlag());
	
}
public void defaultCompilerOptionsButton_ActionEvents(){
	//reset output file flag in the model and update view
	Preferences.setCCOutputFileFlag(Preferences.defaultCCOutputFileFlag);
	getOutputFileSwitchTextField().setText(Preferences.getCCOutputFileFlag());

	//reset warnings flag in the model and update view
	Preferences.setCCWarningsFlag(Preferences.defaultCCWarningsFlag);
	getWarningsSwitchTextField().setText(Preferences.getCCWarningsFlag());
}
}
