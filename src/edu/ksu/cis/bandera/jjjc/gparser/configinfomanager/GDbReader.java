package edu.ksu.cis.bandera.jjjc.gparser.configinfomanager;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GDbReader {
	/***********************************************************/
	private String configInfoFileName;
	private String specialConfigInfoFileName;
	private Map<String, Map<String, List<String>>> configInfoMap;
	private Map<String, Map<String, String>> deviceTypeMap;
	private List<GSpecialConfiInfo> specialConfiInfoList;
	private List<GClassifiedAppConfigInfo> newSmartAppConfigInfo;
	/***********************************************************/
	
	public GDbReader(String configInfoFileName, String specialConfigInfoFileName)
	{
		this.configInfoFileName = configInfoFileName;
		this.specialConfigInfoFileName = specialConfigInfoFileName;
		this.configInfoMap = new HashMap<String, Map<String, List<String>>>();
		this.deviceTypeMap = new HashMap<String, Map<String, String>>();
		this.specialConfiInfoList = new ArrayList<GSpecialConfiInfo>();
		this.newSmartAppConfigInfo = null;
	}
	
	public static void main(String[] args)
	{
		GDbReader reader = new GDbReader("/Users/tunguyen/Desktop/Workspace/Java/IoTSan/smartapps/ConfigInfo.xlsx",
				"/Users/tunguyen/Desktop/Workspace/Java/IoTSan/smartapps/SpecialConfigInfo.xlsx");
		reader.load();
	}
	
	public void load()
	{
		/* Read general config info */
		try {
			FileInputStream file = new FileInputStream(new File(configInfoFileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			int numSmartApps = workbook.getNumberOfSheets();

			for(int i = 0; i < numSmartApps; i++)
			{
				XSSFSheet sheet = workbook.getSheetAt(i);
				String smartAppName = sheet.getSheetName();
				Iterator<Row> rowIterator = sheet.iterator();
				Map<String, List<String>> inputInfoMap = new HashMap<String, List<String>>();
				Map<String, String> inputName2DeviceTypeMap = new HashMap<String, String>();
				List<GClassifiedAppConfigInfo> newAppInfo = null;
				
				if(smartAppName.equals(GUtil.classifiedSmartAppName))
				{
					/* | input name | isMultiple | isRanged | value list |
					 * | input name | isMultiple | isRanged | value list | device type |
					 * or
					 * | input name | isMultiple | isRanged | value list | min value | max value |
					 * */
					newAppInfo = new ArrayList<GClassifiedAppConfigInfo>();
					
					/* Iterate through each rows one by one */
					while (rowIterator.hasNext())
					{
						Row row = rowIterator.next();
						int numOfCells = row.getPhysicalNumberOfCells();
						
						if(numOfCells >= 4)
						{
							String inputName = row.getCell(0).getStringCellValue();
							Cell cell1 = row.getCell(1);
							Cell cell2 = row.getCell(2);
							Cell cell3 = row.getCell(3);
							String[] inputValueArr;
							String deviceType = "unknown";
							boolean isMultiple = false;
							boolean isRanged = false;
							List<String> inputValues = new ArrayList<String>();
							
							/* Parse isMultiple */
							if(cell1.getCellTypeEnum() == CellType.BOOLEAN)
							{
								isMultiple = cell1.getBooleanCellValue();
							}
							
							/* Parse isRanged */
							if(cell2.getCellTypeEnum() == CellType.BOOLEAN)
							{
								isRanged = cell2.getBooleanCellValue();
							}
							/* Parse values */
							if(cell3.getCellTypeEnum() == CellType.NUMERIC)
							{
								inputValueArr = new String[1];
								inputValueArr[0] = Integer.toString((int)(cell3.getNumericCellValue()));
							}
							else
							{
								inputValueArr = cell3.getStringCellValue().split(",");
							}
							
							/* For some general capability such as battery, we need a specific device type
							 * info from user 
							 * */
							if(numOfCells == 5)
							{
								/* Parse device type */
								deviceType = row.getCell(4).getStringCellValue();
							}
							inputName2DeviceTypeMap.put(inputName, deviceType);
							
							/* This must be a range input info for NewSmartApp
							 * */
							if(numOfCells == 6)
							{
								Cell cell4 = row.getCell(4);
								Cell cell5 = row.getCell(5);
								
								if((cell4.getCellTypeEnum() == CellType.NUMERIC) && (cell5.getCellTypeEnum() == CellType.NUMERIC))
								{
									int minVal = (int) cell4.getNumericCellValue();
									int maxVal = (int) cell5.getNumericCellValue();
									int index = 0;
									
									/* Get all possible values for this input */
									inputValueArr = new String[(maxVal-minVal)+1];
									for(int val = minVal; val <= maxVal; val++)
									{
										inputValueArr[index++] = "" + val;
									}
								}
								else
								{
									System.out.println("[GDbReader][load] wrong range input info for: " + inputName + ", at sheet: " + smartAppName);
								}
							}
							for(int j = 0; j < inputValueArr.length; j++)
							{
								inputValues.add(inputValueArr[j].trim());
							}
							
							newAppInfo.add(new GClassifiedAppConfigInfo(
									inputName, inputValues, isMultiple, isRanged));
						}
						else
						{
							System.out.println("[GDbReader][load] wrong format at sheet " +
									smartAppName + ", row# " + row.getRowNum());
						}
					}
				}
				else
				{
					/* | input name | value list |
					 * or
					 * | input name | value list | device type |
					 * */
					
					/* Iterate through each rows one by one */
					while (rowIterator.hasNext())
					{
						Row row = rowIterator.next();
						int numOfCells = row.getPhysicalNumberOfCells();
						
						if(numOfCells >= 2)
						{
							String inputName = row.getCell(0).getStringCellValue();
							Cell cell1 = row.getCell(1);
							String[] inputValueArr;
							String deviceType = "unknown";
							
							/* For some general capability such as battery, we need a specific device type
							 * info from user 
							 * */
							if(numOfCells == 3)
							{
								deviceType = row.getCell(2).getStringCellValue();
							}
							inputName2DeviceTypeMap.put(inputName, deviceType);
							
							if(cell1.getCellTypeEnum() == CellType.NUMERIC)
							{
								inputValueArr = new String[1];
								inputValueArr[0] = Integer.toString((int)(cell1.getNumericCellValue()));
							}
							else
							{
								inputValueArr = cell1.getStringCellValue().split(",");
							}
							
							if(inputInfoMap.containsKey(inputName))
							{
								System.out.println("[GDbReader][load] duplicate record for " + inputName);
							}
							else
							{
								List<String> inputValues = new ArrayList<String>();
								
								for(int j = 0; j < inputValueArr.length; j++)
								{
									inputValues.add(inputValueArr[j].trim());
								}
								inputInfoMap.put(inputName, inputValues);
							}
						}
						else
						{
							System.out.println("[GDbReader][load] wrong format at sheet " +
									smartAppName + ", row# " + row.getRowNum());
						}
					}
				}
				if(configInfoMap.containsKey(smartAppName))
				{
					System.out.println("[GDbReader][load] Duplicate config info for SmartApp " + smartAppName);
				}
				else
				{
					if(smartAppName.equals(GUtil.classifiedSmartAppName))
					{
						if(this.newSmartAppConfigInfo == null)
						{
							this.newSmartAppConfigInfo = newAppInfo;
						}
						else
						{
							System.out.println("[GDbReader][load] Duplicate config info for NewSmartApp");
						}
					}
					else
					{
						configInfoMap.put(smartAppName, inputInfoMap);
					}
					this.deviceTypeMap.put(smartAppName, inputName2DeviceTypeMap);
				}
			}
			workbook.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* Read special config info */
		try {
			FileInputStream file = new FileInputStream(new File(this.specialConfigInfoFileName));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			int numOfSheets = workbook.getNumberOfSheets();

			for(int i = 0; i < numOfSheets; i++)
			{
				XSSFSheet sheet = workbook.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				Iterator<Row> rowIterator = sheet.iterator();

				if(sheetName.equals("SpecialEvents"))
				{
					/* Iterate through each rows one by one */
					while (rowIterator.hasNext())
					{
						Row row = rowIterator.next();
						
						if(row.getPhysicalNumberOfCells() == 3)
						{
							String deviceName = row.getCell(0).getStringCellValue();
							String deviceType = row.getCell(1).getStringCellValue();
							String command = row.getCell(2).getStringCellValue();
							this.specialConfiInfoList.add(new GSpecialConfiInfo(deviceName + "_" + deviceType,
									deviceType, command));
						}
						else
						{
							System.out.println("[GDbReader][load] wrong format at sheet " +
									sheetName + ", row# " + row.getRowNum());
						}
					}
				}
			}
			workbook.close();
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Map<String, List<String>> getConfigInfo(String smartAppName)
	{
		if(configInfoMap.containsKey(smartAppName))
		{
			return configInfoMap.get(smartAppName);
		}
		else
		{
			System.out.println("[GDbReader][getConfigInfo] config info of SmartApp " + 
								smartAppName + " is not available!!!");
		}
		return null;
	}
	
	public Map<String, String> getDeviceTypeMap(String smartAppName)
	{
		if(this.deviceTypeMap.containsKey(smartAppName))
		{
			return deviceTypeMap.get(smartAppName);
		}
		else
		{
			System.out.println("[GDbReader][getDeviceTypeMap] device type info of SmartApp " + 
								smartAppName + " is not available!!!");
		}
		return null;
	}
	
	public List<GSpecialConfiInfo> getSpecialConfiInfoList()
	{
		return this.specialConfiInfoList;
	}
	
	public List<GClassifiedAppConfigInfo> getNewSmartAppConfigInfo()
	{
		return this.newSmartAppConfigInfo;
	}
	
	public void putNewSmartAppConfigInfo(Map<String, List<String>> inputInfoMap)
	{
		configInfoMap.put(GUtil.classifiedSmartAppName, inputInfoMap);
	}
}
