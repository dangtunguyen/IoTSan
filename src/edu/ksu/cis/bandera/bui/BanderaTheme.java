/*
 * Created on Jun 12, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package edu.ksu.cis.bandera.bui;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalTheme;
import java.awt.Font;

/**
 * @author jlt6666
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BanderaTheme extends MetalTheme {
 ColorUIResource lightGrey			= new ColorUIResource(204,204,204);
 ColorUIResource midGrey 		= new ColorUIResource(153,153,153);
 ColorUIResource darkGrey  		= new ColorUIResource(102,102,102);
 ColorUIResource lightLavender 	= new ColorUIResource(204,204,255);
 ColorUIResource midLavender  	= new ColorUIResource(153,153,204);
 ColorUIResource darkLavender 	= new ColorUIResource(102,102,153);

 FontUIResource controlFont   	= new FontUIResource( new Font("control font", Font.BOLD, 12));
 FontUIResource smallFont  	 	= new FontUIResource( new Font("small font",   Font.PLAIN, 10));
 FontUIResource systemFont   	= new FontUIResource( new Font("system font",Font.PLAIN, 12));
 FontUIResource userFont   	 	= new FontUIResource( new Font("user font",    Font.PLAIN, 12));
 
 
	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getPrimary1()
	 */
	protected ColorUIResource getPrimary1() {
		return darkLavender;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getPrimary2()
	 */
	protected ColorUIResource getPrimary2() {
		return midLavender;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getPrimary3()
	 */
	protected ColorUIResource getPrimary3() {
		return lightLavender;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getSecondary1()
	 */
	protected ColorUIResource getSecondary1() {
		return darkGrey;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getSecondary2()
	 */
	protected ColorUIResource getSecondary2() {
		return midGrey;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getSecondary3()
	 */
	protected ColorUIResource getSecondary3() {
		return lightLavender;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getControlTextFont()
	 */
	public FontUIResource getControlTextFont() {
		return controlFont;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getSystemTextFont()
	 */
	public FontUIResource getSystemTextFont() {
		return systemFont;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getUserTextFont()
	 */
	public FontUIResource getUserTextFont() {
		return userFont;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getMenuTextFont()
	 */
	public FontUIResource getMenuTextFont() {
		return systemFont;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getWindowTitleFont()
	 */
	public FontUIResource getWindowTitleFont() {
		return controlFont;
	}

	/* (non-Javadoc)
	 * @see javax.swing.plaf.metal.MetalTheme#getSubTextFont()
	 */
	public FontUIResource getSubTextFont() {
		return smallFont;
	}

}
