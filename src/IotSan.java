import edu.ksu.cis.bandera.bui.GDriver;

public class IotSan {
	/**
	 * 
	 * @param args java.lang.String[]
	 */
	public static void main(String[] args) {
		String project_root = System.getProperty("user.dir");
		GDriver driver = new GDriver(false);
		
		/* Do system initializations */
		driver.init(project_root);
		
		/* Translate SmartApps' Groovy source code into Promela code */
		driver.run();
	}
}
