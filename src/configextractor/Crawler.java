package configextractor;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {
	private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36";
	private final static String loginFormUrl = "https://account.smartthings.com/login/smartthings";
	private final static String loginActionUrl = "https://account.smartthings.com/login";
	
	public static void main(String[] args) throws Exception {
		String path = System.getProperty("user.dir") + File.separator + "output" + File.separator;
		
		/* Create the log file */
		File logFile = new File(path + "crawler_log.txt");
		logFile.createNewFile();
		FileWriter logWriter = new FileWriter(logFile);
		String fieldSeparator = "************************************\n";
		HashMap<String, String> reqCookies = new HashMap<>();  
		HashMap<String, String> formData = new HashMap<>();
		
		System.out.println("Running...");

		/* Connect to the login page to get the login form */
		Response loginForm = Jsoup.connect(loginFormUrl)
				.method(Method.GET)
				.userAgent(USER_AGENT)
				.execute();
		reqCookies = (HashMap<String, String>) loginForm.cookies();
		
		logWriter.write("Request cookies of https://account.smartthings.com/login/smartthings:\n");
		for(Entry<String, String> entry : reqCookies.entrySet())
		{
			logWriter.write(entry.getKey() + ":" + entry.getValue() + "\n");
		}
		logWriter.write(fieldSeparator);

		formData.put("username", "your user name");
		formData.put("password", "your password");
		formData.put("XSRF-TOKEN", reqCookies.get("XSRF-TOKEN"));

		/* Login to SmartThings' page */
		Response homePage = Jsoup.connect(loginActionUrl)  
				.cookies(reqCookies)  
				.data(formData)  
				.method(Method.POST)  
				.userAgent(USER_AGENT)
				.execute();
		Document homePageDoc = homePage.parse();
		logWriter.write("Home page:\n");
		logWriter.write(homePageDoc.html() + "\n");
		String redirectUrl = HtmlParser.extractRedirectUrl(homePageDoc.html());
		logWriter.write("Redirect URL:" + redirectUrl + "\n");

		logWriter.write("Home page's cookies:\n");
		for(Entry<String, String> entry : homePage.cookies().entrySet())
		{
			logWriter.write(entry.getKey() + ":" + entry.getValue() + "\n");
		}
		logWriter.write(fieldSeparator);
		System.out.println("Logged in");

		/* Start crawling setting info */
		System.out.println("Crawling setting info...");
		Response redirectPage = Jsoup.connect(redirectUrl)
				.cookies(homePage.cookies())
				.method(Method.GET)  
				.userAgent(USER_AGENT)  
				.execute();
		logWriter.write("Redirected page:\n");
		logWriter.write(redirectPage.parse().html() + "\n");
		logWriter.write(fieldSeparator);

		Response locListPage = Jsoup.connect(redirectUrl + "/location/list")
				.cookies(homePage.cookies())
				.method(Method.GET)  
				.userAgent(USER_AGENT)  
				.execute();
		logWriter.write("Location's list page:\n");
		Document locPageDoc = locListPage.parse();
		logWriter.write(locPageDoc.html() + "\n");
		logWriter.write(fieldSeparator);
		
		/* Parse location's page */
		ArrayList<LocationHrefInfo> locList = HtmlParser.extractLocHrefInfo(locPageDoc);
		ArrayList<LocationInfo> locInfoList = new ArrayList<LocationInfo>();
		for(LocationHrefInfo loc : locList)
		{
			Response smartappsPage = Jsoup.connect(loc.smartappsHref)
					.cookies(homePage.cookies())
					.method(Method.GET)  
					.userAgent(USER_AGENT)  
					.execute();
			Document appsDoc = smartappsPage.parse();
			logWriter.write("smart apps' page of " + loc.name + ":\n");
			logWriter.write(appsDoc.html() + "\n");
			logWriter.write(fieldSeparator);
			
			/* Extract settings info of installed smart apps */
			ArrayList<AppHrefInfo> appHrefInfoList = HtmlParser.extractAppHrefInfo(appsDoc);
			ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
			HashSet<String> appNames = new HashSet<String>();
			for(AppHrefInfo appHref : appHrefInfoList)
			{
				Response appPage = Jsoup.connect(redirectUrl + appHref.href)
						.cookies(homePage.cookies())
						.method(Method.GET)  
						.userAgent(USER_AGENT)  
						.execute();
				Document appDoc = appPage.parse();
				logWriter.write("app' page of " + appHref.name + ":\n");
				logWriter.write(appDoc.html() + "\n");
				logWriter.write(fieldSeparator);
				
				if(!appNames.contains(appHref.name))
				{
					appNames.add(appHref.name);
					AppInfo app = HtmlParser.extractAppInfo(appDoc, appHref.name);
					if(app != null)
					{
						appList.add(app);
					}
				}
			}
			locInfoList.add(new LocationInfo(loc.name, appList));
		}
		
		logWriter.close();
		System.out.println("Writing to output file...");
		DatabaseManager.writeToExcell(locInfoList, path);
		
		System.out.println("Done!!!");
	}
}