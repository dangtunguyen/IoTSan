package configextractor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DatabaseManager {
	private static String removeWhiteSpaces(String str)
	{
		StringBuilder sb = new StringBuilder();

		for(int i = 0; i < str.length(); i++)
		{
			if(str.charAt(i) != ' ' && str.charAt(i) != '/')
			{
				sb.append(str.charAt(i));
			}
		}
		return sb.toString();
	}
	private static String toString(ArrayList<String> strList)
	{
		StringBuilder sb = new StringBuilder();

		if(strList.size() > 0)
		{
			for(int i = 0; i < strList.size()-1; i++)
			{
				sb.append(removeWhiteSpaces(strList.get(i)));
				sb.append(", ");
			}
			sb.append(removeWhiteSpaces(strList.get(strList.size()-1)));
		}

		return sb.toString();
	}

	public static void writeToExcell(ArrayList<LocationInfo> locInfoList, String path)
	{
		for(LocationInfo loc : locInfoList)
		{
			String fileName = path + removeWhiteSpaces(loc.name) + ".xlsx";
			XSSFWorkbook workBook = new XSSFWorkbook();
			FileOutputStream outPutStream = null;

			for(AppInfo app : loc.appList)
			{
				XSSFSheet sheet = workBook.createSheet(removeWhiteSpaces(app.appName));
				int currentRow = 0;
				for(SettingInfo setting : app.settingInfoList)
				{
					XSSFRow row =  sheet.createRow(currentRow++);
					row.createCell(0).setCellValue(setting.varName);
					row.createCell(1).setCellValue(toString(setting.configValues));
				}
			}

			try {
				outPutStream = new FileOutputStream(fileName);
				workBook.write(outPutStream);
				workBook.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (outPutStream != null) {
					try {
						outPutStream.flush();
						outPutStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args)
	{
		ArrayList<String> strList = new ArrayList<String>();
		strList.add("Hello");
		strList.add("World");
		strList.add("Abc");
		System.out.println(toString(strList));
	}
}
