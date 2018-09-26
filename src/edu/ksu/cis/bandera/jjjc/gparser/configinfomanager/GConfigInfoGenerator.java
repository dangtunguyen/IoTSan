package edu.ksu.cis.bandera.jjjc.gparser.configinfomanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* Some code in this file is obtained from 
 * [1] https://stackoverflow.com/questions/29910312/algorithm-to-get-all-the-combinations-of-size-n-from-an-array-java
 * [2] https://stackoverflow.com/questions/17192796/generate-all-combinations-from-multiple-lists
 * */

public class GConfigInfoGenerator {
	/***********************************************************/
	private List<GClassifiedAppConfigInfo> newSmartAppConfigInfo;
	private List<Map<String, List<String>>> generatedConfigInfoList;
	/***********************************************************/
	
	public GConfigInfoGenerator(List<GClassifiedAppConfigInfo> newSmartAppConfigInfo)
	{
		this.newSmartAppConfigInfo = newSmartAppConfigInfo;
		this.generatedConfigInfoList = new ArrayList<Map<String, List<String>>>();
	}

	public static void main(String[] args)
	{
		List<GClassifiedAppConfigInfo> configInfo = new ArrayList<GClassifiedAppConfigInfo>();
		configInfo.add(new GClassifiedAppConfigInfo(
				"thebatterymo",
				Arrays.asList("mainDoorLock", "gateLock"),
				false,
				true
				));
		configInfo.add(new GClassifiedAppConfigInfo(
				"themotionsensor",
				Arrays.asList("livingRoomSensor", "frontDoorSensor", "bathRoomSensor"),
				false,
				true
				));
		configInfo.add(new GClassifiedAppConfigInfo(
				"minutes",
				Arrays.asList("30"),
				false,
				false
				));
		GConfigInfoGenerator generator = new GConfigInfoGenerator(configInfo);
		generator.run();
		System.out.println(generator.getGeneratedConfigInfoList());
		
//		List<String> input = Arrays.asList("A", "B", "C", "D");
//		List<List<String>> result = generator.kCombination(input);
//
//		for(List<String> list : result)
//		{
//			System.out.println(list);
//		}
//		List<List<String>> l1 = generator.kCombination(Arrays.asList("1","2","3"));
//		List<List<String>> l2 = generator.kCombination(Arrays.asList("4","5"));
//		List<List<String>> l3 = generator.kCombination(Arrays.asList("6","7"));
//
//	    List<List<List<String>>> lists = new ArrayList<List<List<String>>>();
//	    lists.add(l1);
//	    lists.add(l2);
//	    lists.add(l3);

//	    Set<List<List<String>>> combs = generator.multipleSetKCombinations(lists);
//	    for(List<List<String>> list : combs) {
//	        System.out.println(list);
//	    }
	}
	
	/* Getters */
	public List<Map<String, List<String>>> getGeneratedConfigInfoList()
	{
		return this.generatedConfigInfoList;
	}
	
	public void run()
	{
		List<List<List<String>>> rangedLists = new ArrayList<List<List<String>>>();
		
		/* Build ranged lists */
		for(GClassifiedAppConfigInfo inputInfo : this.newSmartAppConfigInfo)
		{
			if(inputInfo.isRanged)
			{
				rangedLists.add(this.kCombination(inputInfo.valueList, inputInfo.isMultiple));
			}
		}
		
		if(rangedLists.size() > 0)
		{
			/* Build all possible combinations of ranged lists */
			Set<List<List<String>>> combs = this.multipleSetKCombinations(rangedLists);
			
			/* Generate all possible configuration info */
			System.out.println("All possible configuration info:");
		    for(List<List<String>> list : combs) {
		        System.out.println(list);
		        
		        Map<String, List<String>> inputInfoMap = new HashMap<String, List<String>>();
		        int rangedInputIndex = 0;
		        
		        for(GClassifiedAppConfigInfo inputInfo : this.newSmartAppConfigInfo)
				{
					if(inputInfo.isRanged)
					{
						/* Use generated config info */
						inputInfoMap.put(inputInfo.inputName, list.get(rangedInputIndex++));
					}
					else
					{
						/* Use config info from user */
						inputInfoMap.put(inputInfo.inputName, inputInfo.valueList);
					}
				}
		        this.generatedConfigInfoList.add(inputInfoMap);
		    }
		}
		else
		{
			Map<String, List<String>> inputInfoMap = new HashMap<String, List<String>>();
	        
	        for(GClassifiedAppConfigInfo inputInfo : this.newSmartAppConfigInfo)
			{
	        	/* Use config info from user */
				inputInfoMap.put(inputInfo.inputName, inputInfo.valueList);
			}
	        this.generatedConfigInfoList.add(inputInfoMap);
		}
	}

	/* Generate actual subset by index sequence 
	 * */
	private  String[] getSubset(String[] input, int[] subset) 
	{
		String[] result = new String[subset.length];

		for (int i = 0; i < subset.length; i++) 
			result[i] = input[subset[i]];
		return result;
	}

	private List<List<String>> kCombination(String[] input, int k)
	{
		List<List<String>> result = new ArrayList<List<String>>();
		List<String[]> subsets = new ArrayList<>();
		/* indiceArr points to elements in input array
		 * */
		int[] indiceArr = new int[k];

		if (k <= input.length) 
		{
			/* first index sequence: 0, 1, 2, ...
			 * */
			for (int i = 0; (indiceArr[i] = i) < k - 1; i++);  

			subsets.add(getSubset(input, indiceArr));
			while(true) 
			{
				int i;
				/* find position of item that can be incremented 
				 * */
				for (i = k - 1; i >= 0 && indiceArr[i] == input.length - k + i; i--); 
				if (i < 0) {
					break;
				}
				/* increment this item 
				 * */
				indiceArr[i]++;
				for (++i; i < k; i++) {
					/* Fill up remaining items 
					 * */
					indiceArr[i] = indiceArr[i - 1] + 1; 
				}
				subsets.add(getSubset(input, indiceArr));
			}
		}

		/* Convert List<String[]> to List<List<String>>
		 * */
		for(String[] arr : subsets)
		{
			List<String> list = new ArrayList<String>();

			for(String str : arr)
			{
				list.add(str);
			}
			result.add(list);
		}

		return result;
	}

	private List<List<String>> kCombination(List<String> inputList, boolean isMultiple)
	{
		List<List<String>> result = new ArrayList<List<String>>();
		String[] input = new String[inputList.size()];
		
		for(int i = 0; i < input.length; i++)
		{
			input[i] = inputList.get(i);
		}
		
		if(!isMultiple)
		{
			result.addAll(this.kCombination(input, 1));
		}
		else
		{
			int maxSize = inputList.size();
	
			for(int k = 1; k <= maxSize; k++)
			{
				result.addAll(this.kCombination(input, k));
			}
		}

		return result;
	}

	private <T> Set<List<T>> multipleSetKCombinations(List<List<T>> lists) 
	{
		Set<List<T>> combinations = new HashSet<List<T>>();
		Set<List<T>> newCombinations;
		int index = 0;

		/* Extract each of the integers in the first list 
		 * and add each to ints as a new list 
		 * */
		for(T i: lists.get(0)) {
			List<T> newList = new ArrayList<T>();
			newList.add(i);
			combinations.add(newList);
		}
		index++;
		while(index < lists.size()) {
			List<T> nextList = lists.get(index);
			newCombinations = new HashSet<List<T>>();
			for(List<T> first: combinations) {
				for(T second: nextList) {
					List<T> newList = new ArrayList<T>();
					newList.addAll(first);
					newList.add(second);
					newCombinations.add(newList);
				}
			}
			combinations = newCombinations;

			index++;
		}

		return combinations;
	}
}
