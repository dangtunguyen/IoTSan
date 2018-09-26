package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GDependentGraphBuilder {
	/********************************************/
	private GEvtHandlerInfo[] evtHandlerInfoArr;
	private Graph initialGraph; /* Initial dependent graph of event handlers */
	private Graph sccGraph; /* Strongly connected component graph */
	private Map<String, List<Integer>> smartAppStateChangeEvtHandlerMap;
	/********************************************/
	
	public GDependentGraphBuilder(GEvtHandlerInfo[] evtHandlerInfoArr)
	{
		this.evtHandlerInfoArr = evtHandlerInfoArr;
		this.smartAppStateChangeEvtHandlerMap = new HashMap<String, List<Integer>>();
		this.buildSmartAppStateChangeEvtHandlerMap();
	}
	
	private void buildSmartAppStateChangeEvtHandlerMap()
	{
		for(int i = 0; i < this.evtHandlerInfoArr.length; i++)
		{
			if(this.evtHandlerInfoArr[i].isAppStateChanged)
			{
				if(this.smartAppStateChangeEvtHandlerMap.containsKey(evtHandlerInfoArr[i].smartAppName))
				{
					this.smartAppStateChangeEvtHandlerMap.get(evtHandlerInfoArr[i].smartAppName).add(i);
				}
				else
				{
					List<Integer> stateChangeEvtHandlerList = new ArrayList<Integer>();
					stateChangeEvtHandlerList.add(i);
					this.smartAppStateChangeEvtHandlerMap.put(evtHandlerInfoArr[i].smartAppName,
							stateChangeEvtHandlerList);
				}
			}
		}
	}
	
	/* An event handler A is said to be connected to B (A --> B) if any of A's output events
	 * is a subset of any of B's input events
	 * */
	private boolean isConnected(List<GEventInfo> AOutputEvtList, List<GEventInfo> BInputEvtList)
	{
		for(GEventInfo AOutputEvt : AOutputEvtList)
		{
			for(GEventInfo BInputEvt : BInputEvtList)
			{
				if(AOutputEvt.attribute.equals(BInputEvt.attribute))
				{
					if(AOutputEvt.evtType.equals(BInputEvt.evtType) || BInputEvt.equals(""))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	/* An event handler A is said to be conflict with B (A <> B) if any of A's output event types
	 * is different from any of B's output event types
	 * */
	private boolean isConflict(List<GEventInfo> AOutputEvtList, List<GEventInfo> BOutputEvtList)
	{
		for(GEventInfo AOutputEvt : AOutputEvtList)
		{
			for(GEventInfo BOutputEvt : BOutputEvtList)
			{
				if(AOutputEvt.attribute.equals(BOutputEvt.attribute))
				{
					if(!AOutputEvt.evtType.equals(BOutputEvt.evtType))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private void buildInitialGraph()
	{
		this.initialGraph = new Graph(this.evtHandlerInfoArr.length);
		
		/* Create edges for the graph */
		for(int i = 0; i < this.evtHandlerInfoArr.length-1; i++)
		{
			for(int j = i+1; j < this.evtHandlerInfoArr.length; j++)
			{
				/* Check if there is an edge from i to j */
				if(this.isConnected(this.evtHandlerInfoArr[i].outputEvtList, this.evtHandlerInfoArr[j].inputEvtList))
				{
					this.initialGraph.addEdge(i, j);
				}
				/* Check if there is an edge from j to i */
				if(this.isConnected(this.evtHandlerInfoArr[j].outputEvtList, this.evtHandlerInfoArr[i].inputEvtList))
				{
					this.initialGraph.addEdge(j, i);
				}
			}
		}
	}
	
	private void buildSccGraph()
	{
		this.sccGraph = this.initialGraph.buildSCCGraph();
	}
	
	private Set<Integer> getParentVertexes(int v)
	{
		Set<Integer> sccParentVerList = new HashSet<Integer>();
		
		sccGraph.getAllSccParentVertexes(initialGraph.sccIndexArr[v], sccParentVerList);
//		sccGraph.getSccParentVertex(initialGraph.sccIndexArr[v], sccParentVerList);
		return initialGraph.getAllParentVertexes(v, sccParentVerList);
	}
	
	private Set<Integer> getDependentVertexes(int u, int v)
	{
		Set<Integer> uParents, vParents;
		
		uParents = this.getParentVertexes(u);
		vParents = this.getParentVertexes(v);
		
		for(int w : vParents)
		{
			if(!uParents.contains(w))
			{
				uParents.add(w);
			}
		}
		
		return uParents;
	}
	
	/* For each smart app included in a dependent graph, we have to add all
	 * app's state change event handlers to this dependent graph
	 * */
	private void addAppStateChangeEvtHandlers(List<Set<Integer>> dependentForest)
	{
		for(Set<Integer> g : dependentForest)
		{
			Map<String, Boolean> smartAppVisitedMap = new HashMap<String, Boolean>();
			List<Integer> additionalVerList = new ArrayList<Integer>();
			
			/* Get app's state change event handlers of all included smart apps */
			for(int v : g)
			{
				if(!smartAppVisitedMap.containsKey(this.evtHandlerInfoArr[v].smartAppName)
						&& this.smartAppStateChangeEvtHandlerMap.containsKey(this.evtHandlerInfoArr[v].smartAppName))
				{
					smartAppVisitedMap.put(this.evtHandlerInfoArr[v].smartAppName, true);
					additionalVerList.addAll(
							this.smartAppStateChangeEvtHandlerMap.get(this.evtHandlerInfoArr[v].smartAppName));
				}
			}
			/* Add the additional event handlers to the current dependent graph g */
			for(int u : additionalVerList)
			{
				if(!g.contains(u))
				{
					g.add(u);
				}
			}
		}
	}
	
	/* Remove duplicate trees in dependentForest.
	 * resultForest contains all distinct trees, whose 
	 * elements are different regardless of their order in the tree
	 * */
	private List<Set<Integer>> removeDuplicateTrees(List<Set<Integer>> dependentForest)
	{
		for(int i = 0; i < dependentForest.size(); i++)
		{
			for(int j = 0; j < dependentForest.size();)
			{
				if((i != j) && dependentForest.get(i).containsAll(dependentForest.get(j)))
				{
					dependentForest.remove(j);
					i--;
					if(i < 0)
					{
						i = 0;
					}
				}
				else
				{
					j++;
				}
			}
		}
		
		for(int i = 0; i < dependentForest.size();)
		{
			for(int j = 0; j < dependentForest.size(); j++)
			{
				if((i != j) && dependentForest.get(j).containsAll(dependentForest.get(i)))
				{
					dependentForest.remove(i);
					break;
				}
				else
				{
					i++;
				}
			}
		}
		return dependentForest;
	}
	
	private boolean doesEvtHandlerOutputSecSensitiveAction(List<GEventInfo> outputEvtList)
	{
		for(GEventInfo evt : outputEvtList)
		{
			if(GUtil.isASecuritySensitiveAction(evt.attribute, evt.evtType))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<Set<Integer>> buildDependentForest()
	{
		List<Set<Integer>> dependentForest = new ArrayList<Set<Integer>>();
		this.buildInitialGraph();
		this.buildSccGraph();
		
		/* Print strongly connected component graph */
		System.out.println("******************************************************");
		System.out.println("Strongly connected component graph:");
		System.out.println("Number of vertices:" + this.sccGraph.V);
		for(int i = 0; i < this.sccGraph.V; i++)
		{
			System.out.println("neighbors of " + i + ": " + this.sccGraph.nextVerList[i]);
		}
		
		/* Check potential conflicts */
		for(int u = 0; u < this.evtHandlerInfoArr.length-1; u++)
		{
			for(int v = u+1; v < this.evtHandlerInfoArr.length; v++)
			{
				/* Check if output events of u and v are potentially conflict */
				if(this.isConflict(this.evtHandlerInfoArr[u].outputEvtList, 
						this.evtHandlerInfoArr[v].outputEvtList))
				{
					Set<Integer> dependentGraph = this.getDependentVertexes(u, v);
					dependentForest.add(dependentGraph);
				}
			}
		}
		/* Add potential loops to dependentForest */
		for(int i = 0; i < this.initialGraph.numSCCs; i++)
		{
			/* A loop has at least 2 vertexes */
			if(this.initialGraph.sccMemberArr[i].size() > 1)
			{
				dependentForest.add(this.initialGraph.sccMemberArr[i]);
			}
		}
		/* Add evtHandler with security sensitive action 
		 * to the dependentForest
		 * */
		for(int u = 0; u < this.evtHandlerInfoArr.length; u++)
		{
			/* Check if the current event handler outputs any security sensitive action */
			if(doesEvtHandlerOutputSecSensitiveAction(evtHandlerInfoArr[u].outputEvtList))
			{
				Set<Integer> uParents;
				
				uParents = this.getParentVertexes(u);
				dependentForest.add(uParents);
			}
		}
		/* Add all app's state change event handlers of related smart apps
		 * to the dependentForest
		 * */
		addAppStateChangeEvtHandlers(dependentForest);
		
		/* Remove duplicate trees before returning the result */
		return removeDuplicateTrees(dependentForest);
//		return dependentForest;
	}
	
	public static void main(String[] args)
	{
		List<List<Integer>> forest = new ArrayList<List<Integer>>();
		List<Integer> graph = new ArrayList<Integer>();
		graph.addAll(Arrays.asList(1,2,3));
		forest.add(graph);
		
		for(List<Integer> g : forest)
		{
			if(!g.contains(4))
			{
				g.add(4);
			}
		}
		for(List<Integer> g : forest)
		{
			System.out.println(g);
		}
	}
}
