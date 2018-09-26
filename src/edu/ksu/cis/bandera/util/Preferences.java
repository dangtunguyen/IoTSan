package edu.ksu.cis.bandera.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000  Roby Joehanes (robbyjo@cis.ksu.edu)           *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import java.util.*;
import java.io.*;
import java.awt.*;
import java.net.*;
import java.util.jar.*;
import java.util.zip.*;

import org.apache.log4j.Category;

/**
 * The Preferences object stores global attributes for the Bandera application suite.  This
 * will include color preferences and locations of files.
 *
 * @author Roby Joehanes &lt;robbyjo@cis.ksu.edu&gt;
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.5 $ - $Date: 2003/06/23 18:59:52 $
 */
public class Preferences {

  private static Properties prop;
  private static final String reportHTMLBackgroundColorKey = "report.html.background.color";
  private static final String reportHTMLBackgroundImageKey = "report.html.background.image";
  private static final String reportHTMLBackImageKey = "report.html.back.image";
  private static String reportHTMLBackgroundColor = "#FFFFFF";
  private static String reportHTMLBackgroundImage = "";
  private static String reportHTMLBackImage = "";
  private static final String clrAssertion = "color.assertion";
  private static final String clrHighlight = "color.highlight";
  private static final String clrPredicate = "color.predicate";
  private static final String clrSliced = "color.sliced";
  private static final String clrModified = "color.modified";
  private static final String clrUnreachable = "color.unreachable";
  private static final String clrDefault = "color.default";

  public static			String defaultCCWarningsFlag ="-w";
  public static			String defaultCCOutputFileFlag = "-o";
  public static			String defaultSpinAlias ="spin";
  public static			String defaultDSpinAlias = "dspin";
  public static			String defaultCCAlias = "gcc";
  private static final String ccWarningsFlagName ="cc.warn_flag";
  private static final String ccOutputFileFlagName ="cc.output_flag"; 
  private static final String dspinName = "dspin.name";
  private static final String spinName = "spin.name";
  private static final String ccName = "cc.name";
  private static final String spinDir = "spin.include";
  private static final String dspinDir = "dspin.include";
  private static final String ccDir = "cc.include";
  private static final String absPath = "abstraction.path";
  private static final String absPackage = "abstraction.package";
  private static final String banderaHome = "bandera.home";
  private static final String jpfHome = "jpf.home";
  private static final String javacupHome = "javacup.home";
  private static       String banderaHomeDir = ".";
  private static       String jpfHomeDir = ".";
  private static       String javacupHomeDir = ".";
  public  static final String userBanderaDir = System.getProperty("user.home") + File.separator + ".bandera";
  public  static final String userConfigFile = userBanderaDir + File.separator + ".banderarc";
  public  static final String banderaHeader = "Bandera Configuration File";
  private static final Color  cdefHighlight = new Color(0, 255, 0);
  private static final Color  cdefAssertion = new Color(128, 64, 64);
  private static final Color  cdefPredicate = new Color(64, 128, 64);
  private static final Color  cdefSliced    = new Color(255,0,0);
  private static final Color  cdefModified  = new Color(0,0,255);
  private static final Color  cdefUnreachable = new Color(0,255,255);
  private static final Color  cdefDefault   = new Color(0,0,0);
  private static       Set    banderaBuiltinClassSet;
  private static       String banderaBuiltinDirectory;
  private static       File   banderaBuiltinFile;
  private static       boolean foundJPF;
  private static final Category log = Category.getInstance(Preferences.class.getName());

  static {
    try {
      banderaHomeDir = new File(Class.forName("Bandera").getResource("Bandera.class").getFile()).getParent();
      log.debug("banderaHomeDir = " + banderaHomeDir);
    } catch (Exception e) {
      System.out.println("Could not find Bandera");
      System.exit(1);
    }
    try {
      jpfHomeDir = new File(Class.forName("gov.nasa.arc.ase.jpf.jvm.Main").getResource("Main.class").getFile()).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParent();
      log.debug("jpfHomeDir = " + jpfHomeDir);
      foundJPF = true;
    } catch (Exception e) {
      System.out.println("Could not find JPF");
      foundJPF = false;
    }
    try {
      javacupHomeDir = new File(Class.forName("java_cup.runtime.Symbol").getResource("Symbol.class").getFile()).getParentFile().getParentFile().getParent();
      log.debug("javacupHomeDir = " + javacupHomeDir);
    } catch (Exception e) {
      System.out.println("Could not find Java Cup runtime library");
    }

    banderaBuiltinDirectory = banderaHomeDir + File.separator + ".." + File.separator + "lib" + File.separator + "bandera";
    log.debug("banderaBuiltinDiretory = " + banderaBuiltinDirectory);
    log.debug("userBanderaDir = " + userBanderaDir);
    log.debug("userConfigFile = " + userConfigFile);
  }

  public static boolean isJPFAvailable() {
    return(foundJPF);
  }
  public static String getJPFHomeDirectory() {
    return(jpfHomeDir);
  }

  public static String getBanderaBuiltinDirectory() {
    return(banderaBuiltinDirectory);
  }

  /**
   * The Bandera Builtin File is the file that will be placed into the classpath
   * so that the builtin classes will be found before the Java runtime libraries
   * are loaded.  This means that Bandera versions of those libraries will be
   * used instead.  This file might be a jar file or a directory depending on what
   * is available at runtime.
   *
   * @return File A jar file or a directory where the builtin classes can be found.
   */
  public static File getBanderaBuiltinFile() {
    if(banderaBuiltinFile == null) {
      initBanderaBuiltinClassSet();
    }
    return(banderaBuiltinFile);
  }

  /**
   * Get the set of all classes that should be overridden in Bandera.  This list will
   * be generated based upon what is available at runtime.
   */
  public static Set getBanderaBuiltinClassSet() {
    if(banderaBuiltinClassSet == null) {
      initBanderaBuiltinClassSet();
    }
    return(banderaBuiltinClassSet);
  }

  private static void initBanderaBuiltinClassSet() {
    log.debug("Initializing the bandera builtin class set ...");

    banderaBuiltinClassSet = new HashSet();

    // look for a jar file that contains the classes to include: banderaBuiltin.jar
    File banderaBuiltinJarFile = new File(banderaBuiltinDirectory + File.separator + "banderaBuiltin.jar");
    try {
      banderaBuiltinJarFile = banderaBuiltinJarFile.getCanonicalFile();
    }
    catch(Exception e) {
    }
    log.info("Attempting to load the bandera builtin list from the jar file: " +
        banderaBuiltinJarFile.getPath() + " ...");
    loadFromJarFile(banderaBuiltinJarFile);
    if(banderaBuiltinClassSet.size() > 0) {
      log.info("Loaded the bandera builtin list from the jar file: " + banderaBuiltinJarFile.getPath());
      banderaBuiltinFile = banderaBuiltinJarFile;
      return;
    }

    // look for a text file that contains the classes to include: banderaBuiltin.txt
    File banderaBuiltinTextFile = new File(banderaBuiltinDirectory + File.separator + "banderaBuiltin.txt");
    try {
      banderaBuiltinTextFile = banderaBuiltinTextFile.getCanonicalFile();
    }
    catch(Exception e) {
    }
    log.info("Attempting to load the bandera builtin list from the text file: " +
        banderaBuiltinTextFile.getPath() + " ...");
    loadFromTextFile(banderaBuiltinTextFile);
    if(banderaBuiltinClassSet.size() > 0) {
      log.info("Loaded the bandera builtin list from the text file: " + banderaBuiltinTextFile.getPath());
      banderaBuiltinFile = banderaBuiltinTextFile.getParentFile();
      return;
    }

    // look into the bandera builtin directory and create a list of classes based upon
    //  the class files that exist in it's directory tree
    File banderaBuiltinDirectoryFile = new File(banderaBuiltinDirectory);
    try {
      banderaBuiltinDirectoryFile = banderaBuiltinDirectoryFile.getCanonicalFile();
    }
    catch(Exception e) {
    }
    log.info("Attempting to load the bandera builtin list from a scan of the directory: " +
        banderaBuiltinDirectoryFile.getPath() + " ...");
    loadFromDirectory(banderaBuiltinDirectoryFile, "");
    if(banderaBuiltinClassSet.size() > 0) {
      log.info("Loaded the bandera builtin list from a scan of the directory: " + banderaBuiltinDirectoryFile.getPath());
      banderaBuiltinFile = banderaBuiltinDirectoryFile;
      return;
    }

    // Other ideas
    // look for an xml file that contains the classes to include: banderaBuiltin.xml

    log.warn("Failed to load the bandera builtin class set.");
  }

  /**
   * This method will attempt to load the bandera builtin class from a jar file.  This
   * will walk through the files contained in the jar file trying to find all the class
   * files that exist and add them to the set of classes that will be over-ridden in Bandera.
   */
  private static void loadFromJarFile(File banderaBuiltinJarFile) {

    if((banderaBuiltinJarFile == null) ||
        (!banderaBuiltinJarFile.exists()) ||
        (!banderaBuiltinJarFile.canRead()) ||
        (!banderaBuiltinJarFile.isFile())) {
      log.info("The jar file given could not be read.  Either it was null, didn't exist, or was unreadable.");
      return;
    }

    try {
      JarFile jarFile = new JarFile(banderaBuiltinJarFile);
      Enumeration entries = jarFile.entries();
      while(entries.hasMoreElements()) {
        JarEntry jarEntry = (JarEntry)entries.nextElement();
        if(!jarEntry.isDirectory()) {
          String jarEntryName = jarEntry.getName();
          if(jarEntryName.endsWith(".class")) {
            log.debug("found a class file: " + jarEntryName);
            String temp = jarEntryName.substring(0, jarEntryName.length() - 6);
            String className = temp.replace('/', '.');
            log.debug("adding class to builtin set: " + className);
            banderaBuiltinClassSet.add(className);
          }
        }
      }
    }
    catch(Exception e) {
      // ignore
    }
  }

  /**
   * This method will attempt to load the bandera builtin class list from a text file.  This
   * will walk through the list of classes in the file and add them to the set of classes
   * that will be over-ridden in Bandera.  If this method succeeds, it is assumed that in the
   * directory where this text file exists the corresponding class files will also be found.
   */
  private static void loadFromTextFile(File banderaBuiltinTextFile) {

    if((banderaBuiltinTextFile != null) &&
        (banderaBuiltinTextFile.exists()) &&
        (banderaBuiltinTextFile.canRead()) &&
        (banderaBuiltinTextFile.isFile())) {

      // each line of this file is a class to be added to the set
      FileReader fr = null;
      BufferedReader br = null;
      try {
        fr = new FileReader(banderaBuiltinTextFile);
        br = new BufferedReader(fr);
        String className = "";
        while(br.ready()) {
          className = br.readLine();
          if((className != null) && (className.length() > 0)) {
            log.debug("adding class to builtin set: " + className);
            banderaBuiltinClassSet.add(className);
          }
        }

      }
      catch(FileNotFoundException fnfe) {
        // thrown by FileReader constructor when the file isn't found
      }
      catch(IOException ioe) {
        // thrown by BufferedReader.readLine when a problem exists when reading data from
        // the file
      }
      catch(Exception e) {
        // ignore these
      }
      finally {
        // make sure we close the readers
        try {
          if(br != null) {
            br.close();
          }
          if(fr != null) {
            fr.close();
          }
        }
        catch(Exception e) {
          // ignore this
        }
      }
    }
  }

  /**
   * This method will attempt to load the bandera builtin class from a directory.  This
   * will walk through the files contained in the directory trying to find all the class
   * files that exist and add them to the set of classes that will be over-ridden in Bandera.
   */
  private static void loadFromDirectory(File directory, String packageName) {
    if((directory == null) ||
        (directory.isFile()) ||
        (!directory.exists()) ||
        (!directory.canRead())) {
      return;
    }

    File[] files = directory.listFiles();
    if(files != null) {
      log.debug("files.length = " + files.length);
      for(int i = 0; i < files.length; i++) {
        log.debug("file[i] + " + files[i].getName());
        if((files[i] != null) &&
            (files[i].exists()) &&
            (files[i].canRead())) {
          if(files[i].isDirectory()) {
            if((packageName != null) && (packageName.length() > 0)) {
              loadFromDirectory(files[i], packageName + "." + files[i].getName());
            }
            else {
              loadFromDirectory(files[i], files[i].getName());
            }
          }
          else {
            if(files[i].getName().endsWith(".class")) {
              String fileName = files[i].getName();
              String className = fileName.substring(0, fileName.length() - 6);
              String fullClassName = "";
              if((packageName != null) && (packageName.length() > 0)) {
                fullClassName = packageName + "." + className;
              }
              else {
                fullClassName = className;
              }
              banderaBuiltinClassSet.add(fullClassName);
              log.debug("adding class to builin set: " + fullClassName);
            }
          }
        }
      }
    }
  }

  private static String clr2str(Color c)
  {
    int r, g, b;
    if (c == null) return null;
    r = c.getRed(); g = c.getGreen(); b = c.getBlue();
    return r + "," + g + "," + b;
  }
  public static String getAbstractionPackage() { return prop.getProperty(absPackage); }
  public static String getAbstractionPath() { return prop.getProperty(absPath); }
  public static Color getAssertionColor()
  {
    Color c = str2clr(prop.getProperty(clrAssertion));
    // Wrong settings? revert to default values
    if (c == null)
    {
      c = cdefAssertion;
      setAssertionColor(c);
    }
    return c;
  }
  public static String getBanderaHomeDir()
  {
    return banderaHomeDir;
  }
  public static String getCCWarningsFlag(){
    String ccWarn = prop.getProperty(ccWarningsFlagName);
    if(ccWarn == null){
      ccWarn = defaultCCWarningsFlag;
    }

    return ccWarn;
  }
  public static String getCCOutputFileFlag(){
    String ccOut = prop.getProperty(ccOutputFileFlagName);
    if(ccOut == null){
      ccOut = defaultCCOutputFileFlag;
    }
    return ccOut;
  }
  public static String getCCAlias()
  { String ccAlias = prop.getProperty(ccName);
  if (ccAlias == null){
    ccAlias = defaultCCAlias;
  }

  return ccAlias;
  }
  public static String getCCDir()
  {return prop.getProperty(ccDir);
  }
  public static Color getDefaultColor()
  {
    Color c = str2clr(prop.getProperty(clrDefault));
    // Wrong settings? revert to default values
    if (c == null)
    {
      c = cdefDefault;
      setDefaultColor(c);
    }
    return c;
  }
  public static String getDSpinAlias()
  { String dspinAlias = prop.getProperty(dspinName);
  if (dspinAlias == null)
  {dspinAlias = defaultDSpinAlias;}
  return dspinAlias;
  }
  public static String getDSpinDir() { return prop.getProperty(dspinDir); }
  public static Color getHighlightColor()
  {
    Color c = str2clr(prop.getProperty(clrHighlight));
    // Wrong settings? revert to default values
    if (c == null)
    {
      c = cdefHighlight;
      setHighlightColor(c);
    }
    return c;
  }
  public static String getJavaCupHomeDir() { return javacupHomeDir; }
  public static String getJPFHomeDir() { return jpfHomeDir; }
  public static Color getModifiedColor()
  {
    Color c = str2clr(prop.getProperty(clrModified));
    // Wrong settings? revert to default values
    if (c == null)
    {
      c = cdefModified;
      setModifiedColor(c);
    }
    return c;
  }
  public static Color getPredicateColor()
  {
    Color c = str2clr(prop.getProperty(clrPredicate));
    // Wrong settings? revert to default values
    if (c == null)
    {
      c = cdefPredicate;
      setPredicateColor(c);
    }
    return c;
  }
  public static Properties getProperties() { return prop; }
  /**
   * 
   * @return java.lang.String
   */
  public static java.lang.String getReportHTMLBackgroundColor() {
    return prop.getProperty(reportHTMLBackgroundColorKey);
  }
  /**
   * 
   * @return java.lang.String
   */
  public static java.lang.String getReportHTMLBackgroundImage() {
    return prop.getProperty(reportHTMLBackgroundImageKey);
  }
  /**
   * 
   * @return java.lang.String
   */
  public static java.lang.String getReportHTMLBackImage() {
    return prop.getProperty(reportHTMLBackImageKey);
  }
  public static Color getSlicedColor()
  {
    Color c = str2clr(prop.getProperty(clrSliced));
    // Wrong settings? revert to default values
    if (c == null)
    {
      c = cdefSliced;
      setSlicedColor(c);
    }
    return c;
  }
  public static String getSpinAlias()
  {String spinAlias = prop.getProperty(spinName);

  if (spinAlias == null) {
    spinAlias = defaultSpinAlias;	  	
  }
  return spinAlias;
  }
  public static String getSpinDir()
  { return prop.getProperty(spinDir);
  }
  public static Color getUnreachableColor()
  {
    Color c = str2clr(prop.getProperty(clrUnreachable));
    // Wrong settings? revert to default values
    if (c == null)
    {
      c = cdefUnreachable;
      setUnreachableColor(c);
    }
    return c;
  }
  public static String getUserPrefDir() { return userBanderaDir; }
  private static void init()
  {
    System.out.println("Setting up Bandera for the first time...");
    reset();

    File f = new File(userBanderaDir);
    System.out.println("Creating '"+f.getAbsolutePath() + "'");
    if (!f.exists() && !f.mkdir()) {
      System.out.println("Fatal error: Cannot create " + f.getAbsolutePath()+ " to setup Bandera!");
      System.exit(0);
    }

    String[] dirs = { "integral", "real" };

    for (int i = 0; i < dirs.length; i++)
    {
      f = new File(userBanderaDir + File.separator + dirs[i]);
      System.out.println("Creating '"+f.getAbsolutePath() + "'");
      if (!f.exists() && !f.mkdir()) {
        System.out.println("Fatal error: Cannot create " + f.getAbsolutePath()+ " to setup Bandera!");
        System.exit(0);
      }
    }
    try {
      f = new File(userBanderaDir+ File.separator + "Abstractions");
      f.createNewFile();
      System.out.println("Creating '"+f.getAbsolutePath() + "'");
    } catch (Exception e)
    {
      System.out.println("Fatal error: Cannot setup Bandera for the first time!");
      System.exit(0);
    }
    try {
      f = new File(userBanderaDir + File.separator + "log");
      System.out.println("Creating '"+f.getAbsolutePath() + "'");
      if (!f.exists() && !f.mkdir()) throw new Exception();
    } catch (Exception e)
    {
      System.out.println("Fatal error: Cannot setup Bandera logging system!");
      System.exit(0);
    }
    save();
    System.out.println("First time setup done!");
  }
  public static void load()
  {
    String userHomeDir = System.getProperty("user.home");
    prop = new Properties();

    try {
      prop.load(new FileInputStream(userConfigFile));
      System.out.println("User preferences loaded.");
    } catch (Exception e) {
      // Then in user's home directory there is no .bandera.
      // Thus, make one.
      init();
    }
  }
  public static void reset()
  {
    System.out.println("Resetting preferences to default values");
    prop.setProperty(clrAssertion,clr2str(cdefAssertion));
    prop.setProperty(clrHighlight,clr2str(cdefHighlight));
    prop.setProperty(clrPredicate,clr2str(cdefPredicate));
    prop.setProperty(clrSliced,clr2str(cdefSliced));
    prop.setProperty(clrModified,clr2str(cdefModified));
    prop.setProperty(clrUnreachable,clr2str(cdefUnreachable));
    prop.setProperty(clrDefault,clr2str(cdefDefault));
    prop.setProperty(dspinDir,userBanderaDir);
    prop.setProperty(absPath,userBanderaDir);
    prop.setProperty(banderaHome,banderaHomeDir);
    prop.setProperty(jpfHome,jpfHomeDir);
    prop.setProperty(javacupHome,javacupHomeDir);
    prop.setProperty(absPackage,"");
    prop.setProperty(reportHTMLBackgroundColorKey, reportHTMLBackgroundColor);
    prop.setProperty(reportHTMLBackgroundImageKey, reportHTMLBackgroundImage);
    prop.setProperty(reportHTMLBackImageKey, reportHTMLBackImage);
  }
  public static void save()
  {
    try {
      prop.store(new FileOutputStream(userConfigFile),banderaHeader);
      System.out.println("Preferences saved.");
    } catch (Exception e)
    {
      System.out.println("Cannot save Bandera config file!");
    }
  }
  public static void setAbstractionPackage(String p) { prop.setProperty(absPackage, p); }
  public static void setAbstractionPath(String p) { prop.setProperty(absPath, p); }
  public static void setAssertionColor(Color c)
  {
    prop.setProperty(clrAssertion,clr2str(c));
  }
  public static void setCCWarningsFlag(String s){
    prop.setProperty(ccWarningsFlagName,s);
  }
  public static void setCCOutputFileFlag(String s){
    prop.setProperty(ccOutputFileFlagName,s);
  }
  public static void setCCAlias(String s)
  {prop.setProperty(ccName, s);
  }
  public static void setccDir(String s)
  {prop.setProperty(ccDir,s);    	
  }
  public static void setDefaultColor(Color c)
  {
    prop.setProperty(clrDefault,clr2str(c));
  }
  public static void setDSpinDir(String p) { prop.setProperty(dspinDir, p); }
  public static void setDSpinAlias(String s)
  {
    prop.setProperty(dspinName, s);
  }
  public static void setHighlightColor(Color c)
  {
    prop.setProperty(clrHighlight,clr2str(c));
  }
  public static void setJPFHomeDir(String dir) { prop.setProperty(jpfHome,dir); }
  public static void setModifiedColor(Color c)
  {
    prop.setProperty(clrModified,clr2str(c));
  }
  public static void setPredicateColor(Color c)
  {
    prop.setProperty(clrPredicate,clr2str(c));
  }


  /**
   * 
   * @param newReportHTMLBackgroundColor java.lang.String
   */
  public static void setReportHTMLBackgroundColor(java.lang.String newReportHTMLBackgroundColor) {
    prop.setProperty(reportHTMLBackgroundColorKey, newReportHTMLBackgroundColor);
  }
  /**
   * 
   * @param newReportHTMLBackgroundImage java.lang.String
   */
  public static void setReportHTMLBackgroundImage(java.lang.String newReportHTMLBackgroundImage) {
    prop.setProperty(reportHTMLBackgroundImageKey, newReportHTMLBackgroundImage);
  }
  /**
   * 
   * @param newReportHTMLBackImage java.lang.String
   */
  public static void setReportHTMLBackImage(java.lang.String newReportHTMLBackImage) {
    prop.setProperty(reportHTMLBackImageKey, newReportHTMLBackImage);
  }
  public static void setSlicedColor(Color c)
  {
    prop.setProperty(clrSliced,clr2str(c));
  }
  public static void setSpinDir(String s)
  {
    prop.setProperty(spinDir, s);
  }
  public static void setSpinAlias(String s)
  {
    prop.setProperty(spinName, s);
  }
  public static void setUnreachableColor(Color c)
  {
    prop.setProperty(clrUnreachable,clr2str(c));
  }
  private static Color str2clr(String s)
  {
    Color c = null;
    if (s == null) return null;

    // Old remnant of code
    if (s.indexOf(",") == -1)
    {
      try {
        c = new Color(Integer.parseInt(s));
      } catch (Exception e) {}
      return c;
    }

    StringTokenizer tok = new StringTokenizer(s, ",");
    int r, g, b;
    try
    {
      r = Integer.parseInt(tok.nextToken());
      g = Integer.parseInt(tok.nextToken());
      b = Integer.parseInt(tok.nextToken());
      c = new Color(r,g,b);
    } catch (Exception e)
    {
    }
    return c;
  }
}
