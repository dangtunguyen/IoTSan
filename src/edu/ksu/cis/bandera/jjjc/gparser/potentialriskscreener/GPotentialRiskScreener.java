package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import edu.ksu.cis.bandera.bui.GDriver;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSTCommonAPIsProcesser;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSmartAppPreProcessor;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.util.BanderaUtil;
import groovy.lang.GroovyShell;
import groovy.lang.MissingMethodException;

public class GPotentialRiskScreener {
	/********************************************/
	public static GEvtHandlerInfo[] evtHandlerInfoArr;
	public static List<Set<Integer>> dependentForest;

	private static String project_root = "/Users/tunguyen/Desktop/Workspace/Java/IoTSan";
	private static List<MethodNode> gSTCommonAPIs = null;
	private static int currentTreeIndex = 0;
	private static Set<String> currentSmartAppNames = new HashSet<String>();
	/********************************************/

	public static void main(String args[]) {
		/*GPotentialRiskScreener.init("/Users/tunguyen/Desktop/Workspace/Java/IoTSan");
		GPotentialRiskScreener.run();*/
		
		/* Randomly divide smartapps into equal subsets of size "maxSize" */
		String smartAppPath = project_root + "/input/smartapps";
		File smartAppFoler = new File(smartAppPath);
		List<String> fullList = new ArrayList<String>();
		for (File file : smartAppFoler.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".groovy")) {
				String[] arrStr = file.getName().split("/");
				fullList.add(arrStr[arrStr.length-1]);
			}
		}
		Random rand = new Random();
		int maxSize = 25;
		while(fullList.size() > 0)
		{
			int count = 0;
			List<String> subList = new ArrayList<String>();
			while((count < maxSize) && (fullList.size() > 0))
			{
				int index = rand.nextInt(fullList.size());
				subList.add(fullList.remove(index));
				count++;
			}
			Collections.sort(subList);
			System.out.println(subList);
		}

		//		evtHandlerInfoArr = new GEvtHandlerInfo[9];
		//		/* app1 */
		//		//evt0
		//		GEvtHandlerInfo evt0 = new GEvtHandlerInfo("app1", "0", 
		//				Arrays.asList(new GEventInfo("presence", "present")),
		//				Arrays.asList(new GEventInfo("switch", "on")),
		//				false);
		//		evtHandlerInfoArr[0] = evt0;
		//		//evt1
		//		GEvtHandlerInfo evt1 = new GEvtHandlerInfo("app1", "1", 
		//				Arrays.asList(new GEventInfo("motion", "active")),
		//				Arrays.asList(new GEventInfo("switch", "on")),
		//					false);
		//		evtHandlerInfoArr[1] = evt1;
		//		//evt2
		//		GEvtHandlerInfo evt2 = new GEvtHandlerInfo("app1", "2", 
		//				Arrays.asList(new GEventInfo("switch", "on")),
		//				Arrays.asList(new GEventInfo("door", "open")),
		//					false);
		//		evtHandlerInfoArr[2] = evt2;
		//		
		//		/* app2 */
		//		//evt3
		//		GEvtHandlerInfo evt3 = new GEvtHandlerInfo("app2", "3", 
		//				Arrays.asList(new GEventInfo("lock", "unlock")),
		//				Arrays.asList(new GEventInfo("switch", "on")),
		//				false);
		//		evtHandlerInfoArr[3] = evt3;
		//		//evt4
		//		GEvtHandlerInfo evt4 = new GEvtHandlerInfo("app2", "4", 
		//				Arrays.asList(new GEventInfo("door", "open")),
		//				Arrays.asList(new GEventInfo("lock", "unlock")),
		//					false);
		//		evtHandlerInfoArr[4] = evt4;
		//		//evt5
		//		GEvtHandlerInfo evt5 = new GEvtHandlerInfo("app2", "5", 
		//				Arrays.asList(new GEventInfo("lock", "unlock")),
		//				new ArrayList<GEventInfo>(),
		//					false);
		//		evtHandlerInfoArr[5] = evt5;
		//		
		//		/* app3 */
		//		//evt6
		//		GEvtHandlerInfo evt6 = new GEvtHandlerInfo("app3", "6", 
		//				Arrays.asList(new GEventInfo("presence", "absent")),
		//				Arrays.asList(new GEventInfo("location", "Away")),
		//				false);
		//		evtHandlerInfoArr[6] = evt6;
		//		//evt7
		//		GEvtHandlerInfo evt7 = new GEvtHandlerInfo("app3", "7", 
		//				Arrays.asList(new GEventInfo("location", "Away")),
		//				Arrays.asList(new GEventInfo("lock", "lock")),
		//					false);
		//		evtHandlerInfoArr[7] = evt7;
		//		//evt8
		//		GEvtHandlerInfo evt8 = new GEvtHandlerInfo("app3", "8", 
		//				Arrays.asList(new GEventInfo("motion", "active")),
		//				new ArrayList<GEventInfo>(),
		//					true);
		//		evtHandlerInfoArr[8] = evt8;
		//		
		//		GDependentGraphBuilder graphBuilder = new GDependentGraphBuilder(evtHandlerInfoArr);
		//		dependentForest = graphBuilder.buildDependentForest();
		//		System.out.println("******************************************************");
		//		System.out.println("dependentForest:");
		//		for(Set<Integer> g : dependentForest)
		//		{
		//			for(int v : g)
		//			{
		//				System.out.print(evtHandlerInfoArr[v].smartAppName + "." + 
		//						evtHandlerInfoArr[v].evtHandler + ", ");
		//			}
		//			System.out.println();
		//		}
	}

	public static void init(String project_root)
	{
		GPotentialRiskScreener.project_root = project_root;

		/* Load SmartThings' common APIs from library */
		try {
			GPotentialRiskScreener.loadGSTCommonAPIs();
		}
		catch(IOException ioe)
		{
			System.out.println("Cannot find STCommonAPIs.groovy" + ioe.toString());
		}
	}

	private static void loadGSTCommonAPIs() throws IOException
	{
		CompilerConfiguration CC = new CompilerConfiguration(CompilerConfiguration.DEFAULT);
		GroovyShell gShell;
		File f;
		GSTCommonAPIsProcesser comAPIsProc = new GSTCommonAPIsProcesser();

		CC.addCompilationCustomizers(comAPIsProc);
		gShell = new GroovyShell(CC);
		try {
			f = new File(project_root + "/lib/groovy/STCommonAPIs.groovy");
			gShell.evaluate(f);
		} catch(MissingMethodException mme)
		{
			String missingMethod = mme.toString();

			if(!missingMethod.contains("definition()") && !missingMethod.contains("main()"))
			{
				System.out.println(missingMethod);
			}
		}
		gSTCommonAPIs = comAPIsProc.getMethodsList();
	}

	private static void extractInputInfo()
	{
		CompilerConfiguration CC = new CompilerConfiguration(CompilerConfiguration.DEFAULT);
		GSmartAppPreProcessor gSmartAppPreProcessor = new GSmartAppPreProcessor(gSTCommonAPIs, false);
		GInputInfoExtractor infoExtractor = new GInputInfoExtractor();
		GroovyShell gShell;
		String smartAppPath = project_root + "/input/smartapps";
		File smartAppFoler = new File(smartAppPath);
		List<String> gClassPath;
		List<GEvtHandlerInfo> evtHandlerInfoList = new ArrayList<GEvtHandlerInfo>();

		gClassPath = CC.getClasspath();
		gClassPath.add(project_root + "/lib/groovy/SmartThings.jar");
		CC.setClasspathList(gClassPath);
		CC.addCompilationCustomizers(gSmartAppPreProcessor, infoExtractor);
		gShell = new GroovyShell(CC);
		for (File file : smartAppFoler.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".groovy")) {
				try {
					System.out.println("Parsing file " + file.getName() + "...");
					gShell.evaluate(file);

					GInOutputEvtProcessor evtProcessor = new GInOutputEvtProcessor(gSmartAppPreProcessor.getClassName(),
							gSmartAppPreProcessor.getEvtHandlerList(), 
							gSmartAppPreProcessor.getSubscriptionInfoList(), 
							gSmartAppPreProcessor.getCalleeInfoMap(),
							infoExtractor.getOutputEvtMap(),
							infoExtractor.getInputEvtMap(),
							gSmartAppPreProcessor.getLocalMethAppStateChangedMap());
					evtProcessor.printEvtHandlerInfoList();
					evtHandlerInfoList.addAll(evtProcessor.getEvtHandlerInfoList());
				} catch(MissingMethodException mme){} catch (CompilationFailedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println(e.getMessage());
				} finally {	
				}
			}
		}

		/* Create evtHandlerInfoArr from evtHandlerInfoList */
		evtHandlerInfoArr = new GEvtHandlerInfo[evtHandlerInfoList.size()];
		for(int i = 0; i < evtHandlerInfoList.size(); i++)
		{
			evtHandlerInfoArr[i] = evtHandlerInfoList.get(i);
			/* Get standard name for input event's type */
			for(GEventInfo evt : evtHandlerInfoArr[i].inputEvtList)
			{
				evt.evtType = GUtil.getCommandFromEvtType(evt.evtType);
			}
		}
	}

	public static void run(boolean classificationMode)
	{
		if(gSTCommonAPIs == null)
		{
			System.out.println("[GPotentialRiskScreener][run] init function must be run first!!!");
			return;
		}

		/* Extract input events and output events of smart apps from source code */
		extractInputInfo();

		/* Build dependent forest of event handlers */
		{
			GDependentGraphBuilder graphBuilder = new GDependentGraphBuilder(evtHandlerInfoArr);
			dependentForest = graphBuilder.buildDependentForest();
			
			if(classificationMode)
			{
				/* We need to prune the dependentForest
				 * */
				pruneDependentForest(GUtil.classifiedSmartAppName);
			}

			//			Set<Integer> fullTree = new HashSet<Integer>();
			//			for(int v = 0; v < evtHandlerInfoArr.length; v++)
			//			{
			//				fullTree.add(v);
			//			}
			//			dependentForest.add(fullTree);
		}

		/* Print to a file */
		{
			String path = GDriver.getTempDir("dependencyGraph");
			if (BanderaUtil.mkdirs(path)) {
				try{
					PrintWriter writer = new PrintWriter(path + File.separator + "graph.txt", "UTF-8");
					writer.println("Total number of event handlers: " + evtHandlerInfoArr.length);
					writer.println("Number of trees: " + dependentForest.size());

					writer.println("Dependency Forest:");
					int treeNumber = 0;
					for(Set<Integer> g : dependentForest)
					{
						writer.print("Tree number " + treeNumber++ + ", size = " + g.size() + ": ");
						for(int v : g)
						{
							writer.print(evtHandlerInfoArr[v].smartAppName + "." + 
									evtHandlerInfoArr[v].evtHandler + ", ");
						}
						writer.println();
					}

					writer.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	private static boolean containSmartApp(Set<Integer> tree, String smartAppName)
	{
		for(int v : tree)
		{
			if(evtHandlerInfoArr[v].smartAppName.equals(smartAppName))
			{
				return true;
			}
		}
		return false;
	}
	/* Remove all trees, which do not contain smartAppName, in the dependentForest
	 * */
	public static void pruneDependentForest(String smartAppName)
	{
		for(int treeIndex = 0;  treeIndex < dependentForest.size(); treeIndex++)
		{
			if(!containSmartApp(dependentForest.get(treeIndex), smartAppName))
			{
				dependentForest.remove(treeIndex);
				treeIndex--;
				if(treeIndex < 0)
				{
					treeIndex = 0;
				}
			}
		}
	}

	public static Set<String> getSmartAppNames(int treeIndex)
	{
		Set<String> result = new HashSet<String>();

		currentTreeIndex = treeIndex;
		currentSmartAppNames = new HashSet<String>();

		for(int v : dependentForest.get(treeIndex))
		{
			result.add(evtHandlerInfoArr[v].smartAppName);
			currentSmartAppNames.add(evtHandlerInfoArr[v].smartAppName);
		}
		result.add("STInitializer");

		return result;
	}
	public static Set<String> getCurrentEvtHandlers()
	{
		Set<String> result = new HashSet<String>();

		for(int v : dependentForest.get(currentTreeIndex))
		{
			result.add(evtHandlerInfoArr[v].smartAppName + "_" + evtHandlerInfoArr[v].evtHandler);
		}
		return result;
	}
	public static Set<String> getCurrentSmartAppNames()
	{
		return currentSmartAppNames;
	}

	/* Check if any event handler's output events contain "location"
	 * */
	public static boolean isLocationOutputEvtPresent()
	{
		/* For each event handler */
		for(int v : dependentForest.get(currentTreeIndex))
		{
			/* For each output event */
			for(GEventInfo evt : evtHandlerInfoArr[v].outputEvtList)
			{
				if(evt.attribute.equals("location"))
				{
					return true;
				}
			}
		}
		return false;
	}
}
