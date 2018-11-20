package configextractor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlParser {
	/* input string: window.location.replace('https://graph-na02-useast1.api.smartthings.com:443');
	 * return value: https://graph-na02-useast1.api.smartthings.com/
	 * */
	public static String extractRedirectUrl(String str)
	{
		String url = "";
		String jsCommand = "window.location.replace('";
		int startIndex = str.indexOf(jsCommand);

		if(startIndex >= 0)
		{
			String subStr = str.substring(startIndex+jsCommand.length());
			int endIndex = subStr.indexOf("');");

			if(endIndex >= 0)
			{
				String fullUrl = subStr.substring(0, endIndex);
				int end = fullUrl.length() - 1;

				while(end >= 0)
				{
					if(fullUrl.charAt(end) == ':') break;
					end--;
				}
				url = fullUrl.substring(0, end);
			}
		}

		return url;
	}
	/* input: https://graph-na02-useast1.api.smartthings.com:443/location/installedSmartApps/6be87d7c-688c-4b7e-93ce-617fa05ef8ea
	 * return: https://graph-na02-useast1.api.smartthings.com/location/installedSmartApps/6be87d7c-688c-4b7e-93ce-617fa05ef8ea
	 * */
	public static String extractLocationUrl(String str)
	{
		String url = "";
		int locIndex = str.indexOf("/location/");

		if(locIndex >= 0)
		{
			int firstEndIndex = locIndex;
			while(firstEndIndex >= 0)
			{
				if(str.charAt(firstEndIndex) == ':') break;
				firstEndIndex--;
			}
			url = str.substring(0, firstEndIndex) + str.substring(locIndex);
		}
		
		return url;
	}
	public static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		try {
			while((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}
			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}
	/***************************************************************/
	
	public static ArrayList<LocationHrefInfo> extractLocHrefInfo(Document locDoc)
	{
		ArrayList<LocationHrefInfo> locList = new ArrayList<LocationHrefInfo>();
		
		/* Navigate the location list section */
		Element locListSec = locDoc.select("div#list-location").first();
		
		if(locListSec != null)
		{
			/* Select the first table, which contains necessary info of location */
			Element table = locListSec.select("table").get(0);
			
			if(table != null)
			{
				Element body = table.select("tbody").get(0);
				
				if(body != null)
				{
					Elements rows = body.select("tr");
					
					for(int r = 0; r < rows.size(); r++)
					{
						Element row = rows.get(r);
		        	    		Elements cols = row.select("td");
		        	    		
		        	    		if(cols.size() == 5)
		        	    		{
		        	    			String name = cols.get(0).text();
		        	    			String deviceHref = extractLocationUrl(cols.get(3).select("a").first().attr("href"));
		        	    			String smartappsHref = extractLocationUrl(cols.get(4).select("a").first().attr("href"));
		        	    			
		        	    			LocationHrefInfo loc = new LocationHrefInfo(name, deviceHref, smartappsHref);
		        	    			locList.add(loc);
		        	    		}
		        	    		else
		        	    		{
		        	    			System.out.println("[HtmlParser.extractLocationInfo] unexpected number of columns " + cols.size());
		        	    		}
					}
				}
				else
				{
					System.out.println("[HtmlParser.extractLocationInfo] body = null");
				}
			}
			else
			{
				System.out.println("[HtmlParser.extractLocationInfo] table = null");
			}
		}
		else
		{
			System.out.println("[HtmlParser.extractLocationInfo] locListSec = null");
		}
		return locList;
	}
	
	public static ArrayList<AppHrefInfo> extractAppHrefInfo(Document appDoc)
	{
		ArrayList<AppHrefInfo> appList = new ArrayList<AppHrefInfo>();
		
		Elements apps = appDoc.select("a.app-edit");
		
		for(Element app : apps)
		{
			String name = app.text();
			String href = app.attr("href");
			AppHrefInfo appHref = new AppHrefInfo(name, href);
			appList.add(appHref);
		}
		
		return appList;
	}
	
	public static AppInfo extractAppInfo(Document appDoc, String appName)
	{
		Element contentSec = appDoc.selectFirst("div.modal-body");
		if(contentSec != null)
		{
			Element table = contentSec.selectFirst("table");
			if(table != null)
			{
				Element body = table.selectFirst("tbody");
				if(body != null)
				{
					Elements rows = body.select("tr");
					if(rows.size() > 0)
					{
						ArrayList<SettingInfo> settingList = new ArrayList<SettingInfo>();
						for(Element row : rows)
						{
							Elements cols = row.select("td");
							if(cols.size() == 3)
							{
								String varName = cols.get(0).text();
								String type = cols.get(1).text();
								Elements values = cols.get(2).select("a");
								ArrayList<String> vList = new ArrayList<String>();
								
								if(values.size() > 0)
								{
									for(Element val : values)
									{
										vList.add(val.text());
									}
								}
								else
								{
									String tempStr = cols.get(2).text();
									String[] strArr = tempStr.split(",");
									for(String str : strArr)
									{
										str = str.trim();
										if(!str.equals("\"\"") && !str.equals("[]"))
										{
											vList.add(str);
										}
									}
								}
								SettingInfo setting = new SettingInfo(varName, type, vList);
								settingList.add(setting);
							}
							else
							{
								System.out.println("[HtmlParser.extractAppInfo] unexpected number of columns " + cols.size());
							}
						}
						return new AppInfo(appName, settingList);
					}
				}
				else
				{
					System.out.println("[HtmlParser.extractAppInfo] body = null");
				}
			}
			else
			{
				System.out.println("[HtmlParser.extractAppInfo] table = null");
			}
		}
		else
		{
			System.out.println("[HtmlParser.extractAppInfo] contentSec = null");
		}
		
		return null;
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		//		String str = "window.location.replace('https://graph-na02-useast1.api.smartthings.com:443');";
		//		System.out.println(extractRedirectUrl(str));
		File file = new File("/Users/tunguyen/Desktop/Workspace/Java/IoTSan/input/app.txt");
		try {
			Document doc = Jsoup.parse(file, "UTF-8");
			AppInfo app = extractAppInfo(doc,"Good Morning");
			System.out.println(app.appName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
