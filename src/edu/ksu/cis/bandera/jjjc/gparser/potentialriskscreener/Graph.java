package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/* Some code in this file is obtained from 
 * http://www.geeksforgeeks.org/strongly-connected-components/
 * */
public class Graph {
	/********************************************/
	int V;   /* Number of vertices */
	List<Integer>[] nextVerList; /* List of next/adjacent/child vertexes */
	List<Integer>[] prevVerList; /* List of previous/parent vertexes */
	int[] sccIndexArr; /* Component index of each vertex */
	Set<Integer>[] sccMemberArr; /* Members of each SCC */
	int numSCCs; /* Number of SCCs */
	/********************************************/

	/* Constructor */
	Graph(int v)
	{
		V = v;
		nextVerList = new List[v];
		for (int i=0; i<v; ++i)
		{
			nextVerList[i] = new ArrayList<Integer>();
		}
		prevVerList = new List[v];
		for (int i=0; i<v; ++i)
		{
			prevVerList[i] = new ArrayList<Integer>();
		}
		
		sccIndexArr = new int[v];
		sccMemberArr = new Set[v];
		for (int i=0; i<v; ++i)
		{
			sccMemberArr[i] = new HashSet<Integer>();
		}
	}

	/* Function to add an edge (v --> w) into the graph */
	void addEdge(int v, int w) 
	{
		if(!nextVerList[v].contains(w))
		{
			nextVerList[v].add(w);
		}
		if(!prevVerList[w].contains(v))
		{
			prevVerList[w].add(v);
		}
	}

	/* A recursive function to print DFS starting from v */
	void DFSUtil(int v, boolean visited[], Set<Integer> sccMemberSet)
	{
		/* Mark the current node as visited and add it to sccMemberList */
		visited[v] = true;
		sccMemberSet.add(v);

		for(int n : nextVerList[v])
		{
			if(!visited[n])
			{
				DFSUtil(n,visited, sccMemberSet);
			}
		}
	}

	/* Function that returns reverse (or transpose) of this graph */
	Graph getTranspose()
	{
		Graph g = new Graph(V);
		for (int v = 0; v < V; v++)
		{
			/* Recursive for all the vertices adjacent to this vertex */
			for(int i : nextVerList[v])
			{
				g.nextVerList[i].add(v);
			}
		}
		return g;
	}

	void fillOrder(int v, boolean visited[], Stack<Integer> stack)
	{
		/* Mark the current node as visited and print it */
		visited[v] = true;

		/* Recursive for all the vertices adjacent to this vertex */
		for(int i : nextVerList[v])
		{
			if(!visited[i])
			{
				fillOrder(i, visited, stack);
			}
		}

		/* All vertices reachable from v are processed by now, push v to Stack */
		stack.push(new Integer(v));
	}

	/* The main function that finds and prints all strongly 
	 * connected components 
	 * */
	void buildSCCs()
	{
		Stack<Integer> stack = new Stack<Integer>();

		/* Mark all the vertices as not visited (For first DFS) */
		boolean visited[] = new boolean[V];
		for(int i = 0; i < V; i++)
		{
			visited[i] = false;
		}

		/* Fill vertices in stack according to their finishing times */
		for (int i = 0; i < V; i++)
			if (visited[i] == false)
				fillOrder(i, visited, stack);

		/* Create a reversed graph */
		Graph gr = getTranspose();

		/* Mark all the vertices as not visited (For second DFS) */
		for (int i = 0; i < V; i++)
		{
			visited[i] = false;
		}

		/* Now process all vertices in order defined by Stack */
		numSCCs = 0;
		while (stack.empty() == false)
		{
			/* Pop a vertex from stack */
			int v = (int)stack.pop();

			/* Create strongly connected component of the popped vertex */
			if (visited[v] == false)
			{
				Set<Integer> sccMemberSet = new HashSet<Integer>();
				
				gr.DFSUtil(v, visited, sccMemberSet);
				sccMemberArr[numSCCs] = sccMemberSet;
				/* Set SCC index for the vertices in sccMemberList */
				for(int u : sccMemberSet)
				{
					this.sccIndexArr[u] = numSCCs;
				}
				numSCCs++;
			}
		}
		
		/* Print out the components */
		System.out.println("******************************************************");
		System.out.println("Strongly connected components:");
		for(int i = 0; i < numSCCs; i++)
		{
			System.out.println("Component #" + i + ": " + sccMemberArr[i]);
		}
	}
	
	public Graph buildSCCGraph()
	{
		Graph sccGraph;
		
		this.buildSCCs();
		sccGraph = new Graph(this.numSCCs);
		
		/* For each vertex v */
		for(int v = 0; v < this.V; v++)
		{
			/* For each adjacent vertex of v */
			for(int n : this.nextVerList[v])
			{
				/* If v and n belong to different SCCs */
				if(this.sccIndexArr[v] != this.sccIndexArr[n])
				{
					/* Add an edge from v's SCC to n's SCC */
					sccGraph.addEdge(this.sccIndexArr[v], this.sccIndexArr[n]);
				}
			}
		}
		
		return sccGraph;
	}
	
	/* v: index in scc graph
	 * sccParentVerList: list of all parent vertexes in scc graph
	 * Note: this function is called by only scc graph
	 * */
	void getAllSccParentVertexes(int v, Set<Integer> sccParentVerList)
	{
		/* For each previous vertex */
		for(int prev : this.prevVerList[v])
		{
			if(!sccParentVerList.contains(prev))
			{
				sccParentVerList.add(prev);
				this.getAllSccParentVertexes(prev, sccParentVerList);
			}
		}
	}
	void getSccParentVertex(int v, Set<Integer> sccParentVerList)
	{
		List<Integer> rootNodeParents = new ArrayList<Integer>();
		
		/* For each previous vertex */
		for(int prev : this.prevVerList[v])
		{
			/* Check if parent is a root node */
			if((this.prevVerList[prev].size() == 0) && !rootNodeParents.contains(prev))
			{
				rootNodeParents.add(prev);
			}
		}
		
		if(rootNodeParents.size() > 0)
		{
			for(int prev : this.prevVerList[v])
			{	
				if(!rootNodeParents.contains(prev) && !sccParentVerList.contains(prev))
				{
					sccParentVerList.add(prev);
					this.getSccParentVertex(prev, sccParentVerList);
				}
			}
			sccParentVerList.add(rootNodeParents.get(0));
			this.getSccParentVertex(rootNodeParents.get(0), sccParentVerList);
		}
		else
		{
			for(int prev : this.prevVerList[v])
			{	
				if(!sccParentVerList.contains(prev))
				{
					sccParentVerList.add(prev);
					this.getSccParentVertex(prev, sccParentVerList);
				}
			}
		}
	}
	
	/* v: vertex index in initial graph
	 * sccParentVerList: list of all parent vertexes in scc graph
	 * return value: list of all parent vertexes in initial graph
	 * Note: this function is called by only initial graph after
	 * building scc graph
	 * */
	Set<Integer> getAllParentVertexes(int v, Set<Integer> sccParentVerList)
	{
		Set<Integer> parentVerList = new HashSet<Integer>();
		
		/* Build parentVerList */
		parentVerList.addAll(this.sccMemberArr[this.sccIndexArr[v]]);
		for(int sccVertex : sccParentVerList)
		{
			parentVerList.addAll(this.sccMemberArr[sccVertex]);
		}
		
		return parentVerList;
	}

	public static void main(String args[])
	{
		/* Create a graph given in the above diagram */
//		Graph g = new Graph(5);
//		g.addEdge(1, 0);
//		g.addEdge(0, 2);
//		g.addEdge(2, 1);
//		g.addEdge(0, 3);
//		g.addEdge(3, 4);
		
		Graph g = new Graph(8);
		g.addEdge(0, 2);
		g.addEdge(1, 2);
		g.addEdge(2, 4);
		g.addEdge(4, 3);
		g.addEdge(3, 2);
		g.addEdge(4, 5);
		g.addEdge(6, 7);

		System.out.println("Following are strongly connected components "+
				"in given graph ");
		Graph sccGraph = g.buildSCCGraph();
		System.out.println("******************************************************");
		System.out.println("Strongly connected component graph:");
		System.out.println("Number of vertices:" + sccGraph.V);
		for(int i = 0; i < sccGraph.V; i++)
		{
			System.out.println("Neighbors of vertex " + i + ":" + sccGraph.nextVerList[i]);
		}
		Set<Integer> parentVerList, sccParentVerList = new HashSet<Integer>();
		int v = 4;
		sccGraph.getAllSccParentVertexes(g.sccIndexArr[v], sccParentVerList);
		parentVerList = g.getAllParentVertexes(v, sccParentVerList);
		System.out.println("******************************************************");
		System.out.println("All parents of vertex " + v + ": " + parentVerList);
	}
}
