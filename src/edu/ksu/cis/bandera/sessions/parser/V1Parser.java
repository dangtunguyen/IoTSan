package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.Specification;
import edu.ksu.cis.bandera.sessions.Assertion;
import edu.ksu.cis.bandera.sessions.TemporalProperty;
import edu.ksu.cis.bandera.sessions.Quantification;
import edu.ksu.cis.bandera.sessions.QuantifiedVariable;
import edu.ksu.cis.bandera.sessions.Binding;
import edu.ksu.cis.bandera.sessions.Predicate;
import edu.ksu.cis.bandera.sessions.Abstraction;
import edu.ksu.cis.bandera.sessions.ClassDescription;
import edu.ksu.cis.bandera.sessions.FieldDescription;
import edu.ksu.cis.bandera.sessions.MethodDescription;
import edu.ksu.cis.bandera.sessions.ResourceBounds;
import edu.ksu.cis.bandera.sessions.BIROptions;

import edu.ksu.cis.bandera.specification.predicate.datastructure.PredicateSet;

import edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern;
import edu.ksu.cis.bandera.specification.pattern.PatternSaverLoader;

import edu.ksu.cis.bandera.specification.datastructure.Property;

import edu.ksu.cis.bandera.specification.SpecificationSaverLoader;
import edu.ksu.cis.bandera.specification.exception.SpecificationException;

import edu.ksu.cis.bandera.specification.assertion.datastructure.AssertionSet;

import edu.ksu.cis.bandera.sessions.parser.v1.SessionsSaverLoader;

import edu.ksu.cis.bandera.sessions.parser.AbstractParser;
import edu.ksu.cis.bandera.sessions.parser.NotAFileException;
import edu.ksu.cis.bandera.sessions.parser.FileNotReadableException;
import edu.ksu.cis.bandera.sessions.parser.FileNotWritableException;
import edu.ksu.cis.bandera.sessions.parser.FileExistsException;
import edu.ksu.cis.bandera.sessions.parser.OldSessionFileFormatException;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import edu.ksu.cis.bandera.jjjc.CompilationManager;

import edu.ksu.cis.bandera.jjjc.symboltable.Name;

import ca.mcgill.sable.soot.jimple.Local;

import edu.ksu.cis.bandera.abstraction.util.LocalMethod;

import ca.mcgill.sable.soot.SootClass;
import ca.mcgill.sable.soot.SootField;
import ca.mcgill.sable.soot.SootMethod;

import edu.ksu.cis.bandera.abstraction.options.OptionsSaverLoader;

import edu.ksu.cis.bandera.util.Preferences;

import org.apache.log4j.Category;

/**
 * The V1Parser provides a way to parse the v1 session file
 * format.  It will use the SessionSaverLoader (that uses the
 * SableCC generated framework) to load the file into the
 * old Session object and then convert it into the new format.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:29 $
 */
public final class V1Parser extends AbstractParser {

	/**
	 * The log to write messages to.
	 */
	private static final Category log = Category.getInstance(V1Parser.class.getName());

	/**
	 * A Map from assertion name to the imported assertion.
	 */
	private Map importedAssertionMap;

	/**
	 * A map from an assertion set name to the imported assertion set.
	 */
	private Map importedAssertionSetMap;

	/**
	 * A mapping from the short predicate names to the full predicate name.
	 */
	private Map predicateNameMap;

	/**
	 * A set of all predicate names (full names) that are imported.
	 */
	private Set predicateNameSet;

	/**
	 * Load the session information from the file given and return it
	 * in a List of Session objects.
	 *
	 * @param File file A file to read the session information from.
	 * @return List A List of Session objects based upon the information in the file.
	 * @pre file != null
	 * @pre file exists
	 * @pre file is readable
	 */
	public List load(File file)
			throws FileNotFoundException, FileNotReadableException, NotAFileException, SessionFileException{

		// ensure pre-conditions!
		if(file == null) {
			throw new FileNotFoundException("Cannot load a null file.");
		}
		if(!file.exists()) {
			throw new FileNotFoundException("The file given (" + file.getAbsolutePath() + ") could not be found.");
		}
		if(!file.canRead()) {
			throw new FileNotReadableException("The file given (" + file.getAbsolutePath() + ") is not readable.");
		}
		if(!file.isFile()) {
			throw new NotAFileException("The file given (" + file.getAbsolutePath() + ") is not a file.");
		}

		// load the old format into a List of Session objects
		List oldSessionList = null;
		try{
			oldSessionList = SessionsSaverLoader.load(file.getAbsolutePath());
		}
		catch(Exception e) {
			throw new SessionFileException("Exception (" + e.toString() + ") while loading the session file (" + file +
					") with the SessionsSaverLoader.load().", e);
		}

		// validate the sessions before converting and throw SessionFileFormatException? -tcw

		// convert to each Session object in the list to the new Session data structure
		List newSessionList = new ArrayList();
		if(oldSessionList != null) {
			edu.ksu.cis.bandera.sessions.parser.v1.Session oldSession;
			Session newSession;
			for(int i = 0; i < oldSessionList.size(); i++) {
				oldSession = (edu.ksu.cis.bandera.sessions.parser.v1.Session)oldSessionList.get(i);
				newSession = new Session();

				//log.debug("processing old session: " + oldSession.getName());

				// convert the flat properties
				newSession.setName(oldSession.getName());
				newSession.setDescription(oldSession.getDescription());
				newSession.setOutputName(oldSession.getOutputName());
				File mainClassFile = getFile(oldSession.getFilename(), file.getParentFile());
				//log.debug("mainClassFile = " + mainClassFile);
				newSession.setMainClassFile(mainClassFile);
				File workingDirectory = getFile(oldSession.getWorkingDirectory(), file.getParentFile());
				//log.debug("workingDirectory = " + workingDirectory);
				newSession.setWorkingDirectory(workingDirectory);

				String classpath = oldSession.getClasspath();
				StringTokenizer st = new StringTokenizer(classpath, File.pathSeparator);
				while(st.hasMoreTokens()) {
					newSession.addClasspathResource(st.nextToken());
				}

				// convert the checker name and options
				String checker = ""; 
				String checkerOptions = "";
				if(oldSession.isUseSPIN()) {
					checker = Session.SPIN_CHECKER_NAME_PROPERTY;
					checkerOptions = oldSession.getSpinOptions();
				}
				if(oldSession.isUseDSPIN()) {
					checker = Session.DSPIN_CHECKER_NAME_PROPERTY;
					checkerOptions = oldSession.getDspinOptions();
				}
				if(oldSession.isUseJPF()) {
					checker = Session.JPF_CHECKER_NAME_PROPERTY;
					checkerOptions = oldSession.getJpfOptions();
				}
				if(oldSession.isUseSMV()) {
					checker = Session.SMV_CHECKER_NAME_PROPERTY;
					checkerOptions = oldSession.getSmvOptions();
				}
				newSession.setProperty(Session.CHECKER_NAME_PROPERTY, checker);
				newSession.setProperty(Session.CHECKER_OPTIONS_PROPERTY, checkerOptions);

				/*
				 * Since the old session file format did not support the specification
				 * of individual bounds for threads, classes, fields, and locals we can skip
				 * that portion of the new session.  We also don't need to worry about
				 * the search mode since it was not supported in the old format. -tcw
				 */
				BIROptions birOptions = new BIROptions();
				ResourceBounds rb = new ResourceBounds();
				rb.setIntMax(oldSession.getBirMaxIntRange());
				rb.setIntMin(oldSession.getBirMinIntRange());
				rb.setDefaultAllocMax(oldSession.getBirMaxInstances());
				rb.setDefaultThreadMax(oldSession.getBirMaxThreadInstances());
				rb.setArrayMax(oldSession.getBirMaxArrayLen());
				birOptions.setResourceBounds(rb);
				newSession.setBIROptions(birOptions);

				newSession.setAbstractionEnabled(oldSession.isDoSLABS());
				newSession.setModelCheckingEnabled(oldSession.isDoChecker());
				newSession.setSlicingEnabled(oldSession.isDoSlicer());

				// convert includes
				String[] includesArray = oldSession.getIncludedPackagesOrTypes();
				if(includesArray != null) {
					for(int j = 0; j < includesArray.length; j++) {
						newSession.addInclude(includesArray[j]);
					}
				}

				// in order to correctly parse the specification file, we have to compile
				// the application in this session!  what a pain! -tcw
				try {
					log.debug("Compiling the application ...");
					Preferences.load();
					CompilationManager.setClasspath(oldSession.getClasspath());
					CompilationManager.setFilename(mainClassFile.getPath());
					CompilationManager.setIncludedPackagesOrTypes(oldSession.getIncludedPackagesOrTypes());
					CompilationManager.compile();
					log.debug("Finished compiling the application.");
				}
				catch(Exception e) {
					throw new SessionFileException("Exception (" + e.toString() + ") while compiling.", e);
				}

				// load the specification file and create a Specification
				// for now, just pass the filename around! -tcw
				String specFilename = oldSession.getSpecFilename();
				if((specFilename != null) && (specFilename.length() > 0)) {
					File specFile = getFile(specFilename, file.getParentFile());
					newSession.setSpecificationFilename(specFile.toString());
					//log.debug("specFilename = " + specFilename);
					//log.debug("specFile = " + specFile);

					edu.ksu.cis.bandera.specification.datastructure.Property property;
					try {
						property = SpecificationSaverLoader.load(specFile.toString());
					}
					catch(SpecificationException se) {
						log.error("Exception (" + se.toString() + ") while loading the specification from " +
								specFilename + ".");
						se.printStackTrace(System.err);
						Vector exceptions = SpecificationSaverLoader.getExceptions();
						if(exceptions != null) {
							Iterator iterator = exceptions.iterator();
							while(iterator.hasNext()) {
								log.error("Exception: " + iterator.next());
							}
						}
						throw new SessionFileException("An exception occured (" + se.toString() +
								") while parsing/loading the specification from " +
								specFilename + ".", se);
					}

					// init the assertion import/resolve information using the property
					initAssertions(property);
					initPredicates(property);

					Specification spec = new Specification();
					Hashtable table = AssertionSet.getAssertionSetTable();
					if((table != null) && (table.size() > 0)) {

						// create the set of active assertions
						Set activeAssertionSet = new HashSet();
						HashSet activeAssertions = oldSession.getActiveAssertions();
						if(activeAssertions != null) {
							Iterator iterator = activeAssertions.iterator();
							while(iterator.hasNext()) {
								String assertionName = (String)iterator.next();
								edu.ksu.cis.bandera.specification.datastructure.AssertionProperty assertionProperty =
										property.getAssertionProperty(assertionName);
								Set currentAssertions = assertionProperty.getAssertions();
								Iterator iterator2 = currentAssertions.iterator();
								while(iterator2.hasNext()) {
									try {
										Name currentAssertionName = (Name)iterator2.next();
										edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion currentAssertion =
												resolveAssertionName(currentAssertionName);
										String assertionFullName = currentAssertion.getName().toString();
										activeAssertionSet.add(assertionFullName);
										log.debug("Assertion " + assertionFullName + " is active.");
									}
									catch(Exception e) {
										log.warn("An exception occured while resolving an active assertion.", e);
									}
								}
							}
						}
						else {
							log.debug("No assertions are active at this time.");
						}

						// get all the assertions from the application, add them to
						//  the spec, but only add those that are active! -tcw
						Set keySet = table.keySet();
						Iterator iterator = keySet.iterator();
						while (iterator.hasNext()) {
							Object key = iterator.next();
							log.debug("key = " + key);
							Object value = table.get(key);
							if(value instanceof AssertionSet) {
								AssertionSet set = (AssertionSet) value;
								Hashtable assertionTable = set.getAssertionTable();
								Set currentKeySet = assertionTable.keySet();
								Iterator currentIterator = currentKeySet.iterator();
								while(currentIterator.hasNext()) {
									Object currentAssertionKey = currentIterator.next();
									edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion currentAssertion =
											(edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion)assertionTable.get(currentAssertionKey);
									String assertionName = currentAssertion.getName().toString();
									if(activeAssertionSet.contains(assertionName)) {
										Assertion newAssertion = new Assertion();
										newAssertion.setName(assertionName);
										newAssertion.setEnabled(true);
										spec.addAssertion(newAssertion);
										log.debug("Adding assertion " + assertionName + " to the session.");
									}
								}
							}
							else {
								// don't know what to do with this
								log.warn("Unhandled type while parsing the assertions.");
								log.warn("value.getClass().getName() = " + value.getClass().getName());
							}
						}
					}
					else {
						log.debug("No assertions in this application.");
					}


					TemporalProperty temporalProperty = new TemporalProperty();
					String activeTemporalPropertyName = oldSession.getActiveTemporal();
					edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty temporalLogicProperty =
							property.getTemporalLogicProperty(activeTemporalPropertyName);
					if(temporalLogicProperty != null) {
						temporalLogicProperty.validate(property.getImportedType(), property.getImportedPackage(), property.getImportedAssertion(), property.getImportedAssertionSet(), property.getImportedPredicate(), property.getImportedPredicateSet());
						Vector exceptions = temporalLogicProperty.getExceptions();
						if((exceptions != null) && (exceptions.size() > 0)) {
							log.debug("Exceptions while validating the temporal logic property:");
							for(int j = 0; j < exceptions.size(); j++) {
								Object o = exceptions.get(j);
								log.debug("Exception: " + exceptions.get(j));
								if(o instanceof Exception) {
									((Exception)o).printStackTrace(System.err);
								}
							}
						}

						// create the pattern for this temporal property
						String patternName = temporalLogicProperty.getPatternName();
						String patternScope = temporalLogicProperty.getPatternScope();
						Pattern pattern = PatternSaverLoader.getPattern(patternName, patternScope);
						temporalProperty.setPattern(pattern);

						// create the predicates for this temporal property
						Vector parameters = pattern.getParameters();
						if(parameters != null) {
							for(int j = 0; j < parameters.size(); j++) {
								String parameterName = (String)parameters.get(j);
								String proposition = temporalLogicProperty.getProposition(parameterName);
								if(proposition != null) {
									Predicate predicate = new Predicate();
									predicate.setName(parameterName);
									String expandedProposition = expandProposition(proposition);
									predicate.setExpression(expandedProposition);
									log.debug("predicate " + parameterName + " = " + proposition);
									temporalProperty.addPredicate(predicate);
								}
								else {
									log.error("No proposition matches this parameter (" + parameterName + ").");
								}
							}
						}
						else {
							log.error("The pattern chosen does not have any parameters.");
						}

						// create the quantification for this temporal property
						Quantification quantification = new Quantification();
						quantification.setBinding(Binding.INSTANCE);  // don't know what else to do? -tcw
						Hashtable quantifiersTable = temporalLogicProperty.getQuantifiersTable();
						if(quantifiersTable != null) {
							Iterator iterator = quantifiersTable.keySet().iterator();
							while(iterator.hasNext()) {
								Object key = iterator.next();
								edu.ksu.cis.bandera.specification.datastructure.QuantifiedVariable currentQV =
										(edu.ksu.cis.bandera.specification.datastructure.QuantifiedVariable)quantifiersTable.get(key);
								QuantifiedVariable qv = new QuantifiedVariable();
								qv.setName(key.toString());
								qv.setType(currentQV.getType().toString());
								quantification.addQuantifiedVariable(qv);
								//log.debug("added qv " + qv.getName() + " : " + qv.getType());
							}
							temporalProperty.setQuantification(quantification);
						}
						else {
							log.debug("No quantification for this property.");
						}

						spec.setTemporalProperty(temporalProperty);
					}
					else {
						log.debug("No temporal property defined for this session.");
					}
					newSession.setSpecification(spec);
				}

				// load the abstraction file and create an Abstraction
				// for now, just pass the filename around! -tcw
				String absFilename = oldSession.getAbsFilename();
				if((absFilename != null) && (absFilename.length() > 0)) {
					File absFile = getFile(absFilename, file.getParentFile());
					//log.debug("absFilename = " + absFilename);
					//log.debug("absFile = " + absFile);
					newSession.setAbstractionFilename(absFile.toString());

					Abstraction abstraction = new Abstraction();
					try {
						Hashtable options = OptionsSaverLoader.load(CompilationManager.getSootClassManager(), new FileReader(absFile.toString()));
						if(options != null) {
							Iterator optionsIterator = options.keySet().iterator();
							while(optionsIterator.hasNext()) {
								Object key = optionsIterator.next();
								if((key != null) && (key instanceof SootClass)) {
									SootClass sootClass = (SootClass)key;
									ClassDescription classDescription = new ClassDescription();
									classDescription.setName(sootClass.getName());

									ca.mcgill.sable.util.List fieldList = sootClass.getFields();
									if((fieldList != null) && (fieldList.size() > 0)) {
										for(int j = 0; j < fieldList.size(); j++) {
											SootField sootField = (SootField)fieldList.get(j);
											Object abstractionObject = options.get(sootField);
											if(abstractionObject == null) {
												// if there is no abstraction for this field, skip it!
												log.debug("sootField = " + sootField + " has no abstraction associated with it.");
												continue;
											}

											String fieldAbstraction = abstractionObject.getClass().getName();
											FieldDescription fieldDescription = new FieldDescription();
											fieldDescription.setName(sootField.getName());
											fieldDescription.setAbstraction(fieldAbstraction);
											classDescription.addFieldDescription(fieldDescription);
										}
									}

									ca.mcgill.sable.util.List methodList = sootClass.getMethods();
									if((methodList != null) && (methodList.size() > 0)) {
										for(int j = 0; j < methodList.size(); j++) {
											SootMethod sootMethod = (SootMethod)methodList.get(j) ;
											String methodName = sootMethod.getName() + "()";
											ca.mcgill.sable.util.List methodParameterTypes = sootMethod.getParameterTypes();
											if((methodParameterTypes != null) && (methodParameterTypes.size() > 0)) {
												StringBuffer sb = new StringBuffer();
												for(int k = 0; k < methodParameterTypes.size(); k++) {
													String currentParameter = methodParameterTypes.get(k).toString();
													sb.append(currentParameter + ",");
												}
												methodName = sootMethod.getName() + "(" + 
														sb.toString().substring(0, sb.length() - 1).trim() + ")";
											}
											//log.debug("adding method: " + methodName);
											MethodDescription methodDescription = new MethodDescription();
											methodDescription.setName(methodName);
											methodDescription.setReturnType(sootMethod.getReturnType().toString());
											HashSet localSet = (HashSet)options.get(sootMethod);
											if((localSet != null) && (localSet.size() > 0)) {
												Iterator localIterator = localSet.iterator();
												while(localIterator.hasNext()) {
													Local local = (Local)localIterator.next();
													LocalMethod localMethod = new LocalMethod(sootMethod, local);
													Object abstractionObject = options.get(localMethod);
													if(abstractionObject == null) {
														// if there is no abstraction for this field, skip it!
														log.debug("local = " + local + " has no abstraction associated with it.");
														continue;
													}
													String localAbstraction = abstractionObject.getClass().getName();
													FieldDescription fieldDescription = new FieldDescription();
													fieldDescription.setName(local.getName());
													fieldDescription.setAbstraction(localAbstraction);
													methodDescription.addLocal(fieldDescription);
												}
											}
											classDescription.addMethodDescription(methodDescription);
										}
									}

									abstraction.addClassDescription(classDescription);
								}
							}
						}
					}
					catch(Exception e) {
						log.error("Exception while parsing the abstraction.", e);
					}

					newSession.setAbstraction(abstraction);
				}

				// counter example options not available in the v1 file format! -tcw
				// misc properties are ignored! -tcw

				newSessionList.add(newSession);

			}
		}

		return(newSessionList);
	}

	/**
	 * Save the list of sessions to the given file.  If overwrite is true, force
	 * overwriting if the file exists. Otherwise, throw an exception if the file
	 * exists.
	 *
	 * @param List sessionList A List of Session objects to be saved.
	 * @param File file The file in which to write the session information given.
	 * @param boolean overwrite If the file exists and overwrite is true, delete the
	 *        current file information and write the new file over the old.   If false,
	 *        and the file exists, throw an exception.
	 * @pre sessionList != null
	 * @pre sessionList has at least 1 Session
	 * @pre file != null
	 * @throws FileNotFoundException Thrown when the file given is null
	 * @pre file can be created or exists already
	 * @throws FileExistsException Thrown when the file exists already and we are not set to overwrite it.
	 * @pre file is writable
	 * @throws FileNotWritableException Thrown when the file is not writable.
	 * @pre file is a file (not a directory)
	 * @throws NotAFileException Thrown when the file is not a file (but rather a directory).
	 */
	public void save(List sessionList, File file, boolean overwrite)
			throws FileNotFoundException, FileExistsException, FileNotWritableException, NotAFileException, SessionFileException {

		throw new OldSessionFileFormatException("This version of the session file format is out of date.  Please use a newer version.");

		// 	if(file == null) {
		// 	    throw new FileNotFoundException("Cannot save to a null file.");
		// 	}

		// 	if(file.exists() && !overwrite) {
		// 	    throw new FileExistsException("The file given (" + file.getAbsolutePath() + ") already exists.");
		// 	}
		// 	else {
		// 	    try {
		// 		file.createNewFile();
		// 	    }
		// 	    catch(Exception e) {
		// 		throw new SessionFileException("An exception occured while creating the session file (" + file + ").", e);
		// 	    }
		// 	}

		// 	if(!file.canWrite()) {
		// 	    throw new FileNotWritableException("The file given (" + file.getAbsolutePath() + ") is not writable.");
		// 	}

		// 	if(!file.isFile()) {
		// 	    throw new NotAFileException("The file given (" + file.getAbsolutePath() + ") is not a file.");
		// 	}

		// 	if((sessionList == null) || (sessionList.size() <= 0)) {
		// 	    try {
		// 		file.delete();
		// 		file.createNewFile();
		// 		return;
		// 	    }
		// 	    catch(Exception e) {
		// 		throw new SessionFileException("An exception occured while clearing the session file " + file + ".", e);
		// 	    }
		// 	}

		// 	// walk thru each Session object and convert it to the old Session object format
		// 	List newSessionList = new ArrayList();
		// 	for(int i = 0; i < sessionList.size(); i++) {
		// 	    edu.ksu.cis.bandera.sessions.parser.v1.Session newSession =
		// 		new edu.ksu.cis.bandera.sessions.parser.v1.Session("");
		// 	    Session oldSession = (Session)sessionList.get(i);

		// 	    // convert the flat properties
		// 	    newSession.setName(oldSession.getName());
		// 	    newSession.setDescription(oldSession.getDescription());
		// 	    newSession.setOutputName(oldSession.getOutputName());
		// 	    String mainClassFilename = getRelativePath(file.getParentFile(), oldSession.getMainClassFile());
		// 	    newSession.setFilename(mainClassFilename);
		// 	    String workingDirectory = getRelativePath(file.getParentFile(), oldSession.getWorkingDirectory());
		// 	    newSession.setWorkingDirectory(workingDirectory);

		// 	    String classpathString = "";
		// 	    List classpath = oldSession.getClasspath();
		// 	    if(classpath != null) {
		// 		StringBuffer classpathBuffer = new StringBuffer();
		// 		for(int j = 0; j < classpath.size(); j++) {
		// 		    classpathBuffer.append(classpath.get(j).toString() + File.pathSeparator);
		// 		}
		// 		classpathString = classpathBuffer.toString().substring(0, classpathBuffer.length() - 1).trim();
		// 	    }
		// 	    newSession.setClasspath(classpathString);

		// 	    String checkerName = oldSession.getProperty(Session.CHECKER_NAME_PROPERTY);
		// 	    if((checkerName != null) && (checkerName.equals(Session.JPF_CHECKER_NAME_PROPERTY))) {
		// 		newSession.setUseJPF(true);
		// 		newSession.setJpfOptions(oldSession.getProperty(Session.JPF_OPTIONS_PROPERTY));
		// 	    }
		// 	    if((checkerName != null) && (checkerName.equals(Session.SPIN_CHECKER_NAME_PROPERTY))) {
		// 		newSession.setUseSPIN(true);
		// 		newSession.setSpinOptions(oldSession.getProperty(Session.SPIN_OPTIONS_PROPERTY));
		// 	    }
		// 	    if((checkerName != null) && (checkerName.equals(Session.DSPIN_CHECKER_NAME_PROPERTY))) {
		// 		newSession.setUseDSPIN(true);
		// 		newSession.setDspinOptions(oldSession.getProperty(Session.DSPIN_OPTIONS_PROPERTY));
		// 	    }
		// 	    if((checkerName != null) && (checkerName.equals(Session.SMV_CHECKER_NAME_PROPERTY))) {
		// 		newSession.setUseSMV(true);
		// 		newSession.setSmvOptions(oldSession.getProperty(Session.SMV_OPTIONS_PROPERTY));
		// 	    }

		// 	    newSession.setDoChecker(oldSession.isModelCheckingEnabled());
		// 	    newSession.setDoSLABS(oldSession.isAbstractionEnabled());
		// 	    newSession.setDoSlicer(oldSession.isSlicingEnabled());

		// 	    // we will lose the search mode as well as the per instance bounds -tcw
		// 	    BIROptions oldBIROptions = oldSession.getBIROptions();
		// 	    ResourceBounds rb = oldBIROptions.getResourceBounds();
		// 	    newSession.setBirMaxIntRange(rb.getIntMax());
		// 	    newSession.setBirMinIntRange(rb.getIntMin());
		// 	    newSession.setBirMaxArrayLen(rb.getArrayMax());
		// 	    newSession.setBirMaxInstances(rb.getDefaultAllocMax());
		// 	    newSession.setBirMaxThreadInstances(rb.getDefaultThreadMax());

		// 	    List includes = oldSession.getIncludes();
		// 	    if((includes != null) && (includes.size() > 0)) {
		// 		String[] includesArray = new String[includes.size()];
		// 		for(int j = 0; j < includes.size(); j++) {
		// 		    includesArray[j] = (String)includes.get(j);
		// 		}
		// 		newSession.setIncludedPackagesOrTypes(includesArray);
		// 	    }

		// 	    String specFilename = oldSession.getSpecificationFilename();
		// 	    if((specFilename == null) || (specFilename.length() <= 0)) {
		// 		specFilename = oldSession.getName() + ".specification";
		// 	    }
		// 	    newSession.setSpecFilename(specFilename);
		// 	    // write out the spec information to the file
		// 	    // set the temporal and assertion properties in the newSession

		// 	    String absFilename = oldSession.getAbstractionFilename();
		// 	    if((absFilename == null) || (absFilename.length() <= 0)) {
		// 		absFilename = oldSession.getName() + ".abstraction";
		// 	    }
		// 	    newSession.setAbsFilename(absFilename);
		// 	    // write out the abs information to the file

		// 	    newSessionList.add(newSession);
		// 	    //log.debug("Finished converting session " + newSession.getName());
		// 	}

		// 	// write it out using the SessionSaverLoader
		// 	try {
		// 	    SessionsSaverLoader.save(newSessionList, file, overwrite);
		// 	}
		// 	catch(Exception e) {
		// 	    throw new SessionFileException("Exception while saving the file using SessionsSaverLoader.save().", e);
		// 	}

	}

	/**
	 * Resolve the name to an assertion that might have been imported.   We will
	 * first check the imported assertion set.  If that doesn't contain the name
	 * requested, test the AssertionSet to see if the assertion is stored in it.
	 * If that fails, walk through the imported AssertionSets.  This might
	 * result in more than one assertion being found.  If so, an exception will
	 * be thrown.  If no assertion is found, an exception will be thrown.
	 *
	 * @param Name name The name of the assertion to resolve.
	 * @return Assertion The assertion the name resolved to or null.
	 * @exception Exception Thrown when the assertion cannot be resolved.
	 */
	private edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion resolveAssertionName(Name name) throws Exception {

		edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion resolvedAssertion = null;

		if(name == null) {
			throw new Exception("Can't resolve a null assertion name.");
		}

		edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion a =
				(edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion)importedAssertionMap.get(name);
		if (a != null) {
			resolvedAssertion = a;
		}
		else {
			if (!name.isSimpleName()) {
				try {
					resolvedAssertion = AssertionSet.getAssertion(name);
				}
				catch (Exception e) {
					throw new Exception("Can't resolve assertion with name '" + name + "'");
				}
			}
			else {

				Vector v = new Vector();

				Iterator i = importedAssertionSetMap.keySet().iterator();
				while(i.hasNext()) {
					Name assertionSetName = (Name)i.next();
					AssertionSet as = (AssertionSet)importedAssertionSetMap.get(assertionSetName);
					if (as.getAssertionTable().get(new Name(as.getName(), name)) != null) {
						v.add(as);
					}
				}

				if (v.size() > 1)
					throw new Exception("Ambiguous assertion with name '" + name + "'");

				if (v.size() < 1)
					throw new Exception("Can't resolve assertion with name '" + name + "'");

				AssertionSet as = (AssertionSet) v.firstElement();
				resolvedAssertion = (edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion)as.getAssertionTable().get(new Name(as.getName(), name));
			}
		}

		return(resolvedAssertion);
	}

	/**
	 * Use the property given to initialize the mappings from assertion names
	 * to the actual assertions when dealing with imported assertions.
	 *
	 * @param Property property The property to use to configure the imported assertions.
	 */
	private void initAssertions(Property property) {

		if(importedAssertionMap == null) {
			importedAssertionMap = new HashMap();
		}
		else {
			importedAssertionMap.clear();
		}
		for (Iterator i = property.getImportedAssertion().iterator(); i.hasNext();) {
			Name name = (Name) i.next();
			try {
				edu.ksu.cis.bandera.specification.assertion.datastructure.Assertion as = AssertionSet.getAssertion(name);
				importedAssertionMap.put(name, as);
			}
			catch(Exception e) {
				// ignore
				log.debug("Exception while getting an assertion with a name of " + name.toString() + " out of the AssertionSet.", e);
			}
		}

		if(importedAssertionSetMap == null) {
			importedAssertionSetMap = new HashMap();
		}
		else {
			importedAssertionSetMap.clear();
		}
		for (Iterator i = property.getImportedAssertionSet().iterator(); i.hasNext();) {
			Name name = (Name) i.next();
			try {
				AssertionSet as = AssertionSet.getAssertionSet(name);
				importedAssertionSetMap.put(name, as);
			}
			catch(Exception e) {
				// ignore
				log.debug("Exception while getting an assertion set with a name of " + name.toString() + " out of the AssertionSet.", e);
			}
		}
	}

	/**
	 * Initialize the set of predicate names that will be used to resolve predicate names
	 * from their short version.  For example, in the BoundedBuffer example, the specification
	 * has a proposition that looks like this: <pre>!Full(b)</pre>.  We need to resolve this
	 * to the full names of the predicates so it will look like this: <pre>!BoundedBuffer.Full(b)</pre>.
	 * This method will initialize the list of all imported predicates so that we can make
	 * this mapping.
	 *
	 * @param Property property The property that holds the set of imported predicates and imported
	 *        predicate sets.
	 */
	private void initPredicates(Property property) {

		if(predicateNameSet == null) {
			predicateNameSet = new HashSet();
		}
		else {
			predicateNameSet.clear();
		}

		if(predicateNameMap == null) {
			predicateNameMap = new HashMap();
		}
		else {
			predicateNameMap.clear();
		}

		TreeSet importedPredicates = property.getImportedPredicate();
		if((importedPredicates != null) && (importedPredicates.size() > 0)) {
			Iterator ipi = importedPredicates.iterator();
			while(ipi.hasNext()) {
				Name name = (Name)ipi.next();
				predicateNameSet.add(name.toString());
				log.debug("importing predicate: " + name.toString().trim());
			}
		}

		TreeSet importedPredicatesSet = property.getImportedPredicateSet();
		if((importedPredicatesSet != null) && (importedPredicatesSet.size() > 0)) {
			Iterator ipsi = importedPredicatesSet.iterator();
			while(ipsi.hasNext()) {
				Name name = (Name)ipsi.next();
				//log.debug("importing predicate set: " + name.toString());
				PredicateSet predicateSet = null;
				try {
					predicateSet = PredicateSet.getPredicateSet(name);
				}
				catch(Exception e) {
					continue;
				}
				if(predicateSet != null) {
					Hashtable predicateTable = predicateSet.getPredicateTable();
					if((predicateTable != null) && (predicateTable.size() > 0)) {
						Iterator pti = predicateTable.keySet().iterator();
						while(pti.hasNext()) {
							Name predicateName = (Name)pti.next();
							log.debug("importing predicate from predicate set: " + predicateName.toString());
							predicateNameSet.add(predicateName.toString().trim());
						}
					}
				}
			}
		}
	}

	/**
	 * Resolve the predicate name given to it's full name.
	 * For example, in the BoundedBuffer example, the specification
	 * has a predicate name that look like this: <pre>Full</pre>.  We need to resolve this
	 * to the full name of the predicate so it will look like this: <pre>BoundedBuffer.Full</pre>.
	 *
	 * @param String predicateName The name of a predicate to resolve.
	 * @return String The full name of the predicate or an empty String if we cannot resolve it.
	 */
	private String resolvePredicateName(String predicateName) {

		if(predicateNameSet == null) {
			log.warn("The predicate resolving system was not initialized properly.  predicateNameSet == null");
			return("");
		}

		if(predicateNameMap == null) {
			log.warn("The predicate resolving system was not initialized properly.  predicateNameMap == null");
			return("");
		}

		String fullPredicateName = (String)predicateNameMap.get(predicateName);
		if((fullPredicateName == null) || (fullPredicateName.length() == 0)) {
			fullPredicateName = "";

			// try to find this name in the Set of all predicate names
			Iterator pnsi = predicateNameSet.iterator();
			while(pnsi.hasNext()) {
				String currentPredicateName = (String)pnsi.next();
				log.debug("checking currentPredicateName " + currentPredicateName +
						" for similarity to " + predicateName + ".");
				if(currentPredicateName.endsWith(predicateName)) {
					// we have found a match, add it to our mapping and return it
					fullPredicateName = currentPredicateName;
					predicateNameMap.put(predicateName, fullPredicateName);
					log.debug("Added mapping of predicate from " + predicateName + " to " + fullPredicateName + ".");
					break;
				}
			}
		}
		else {
			log.debug("Found mapping of predicate from " + predicateName + " to " + fullPredicateName + ".");
		}

		log.debug("resolved predicate name from " + predicateName + " to " + fullPredicateName + ".");
		return(fullPredicateName);
	}

	/**
	 * Expand the proposition given so that all the predicates have their full name.
	 * For example, in the BoundedBuffer example, the specification
	 * has a proposition that looks like this: <pre>!Full(b)</pre>.  We need to resolve this
	 * to the full names of the predicates so it will look like this: <pre>!BoundedBuffer.Full(b)</pre>.
	 *
	 * @param String proposition The expression that is used in the property.
	 * @param String The exact same expression but it will have the predicate names expanded.
	 */
	private String expandProposition(String proposition) {

		StringBuffer sb = new StringBuffer(proposition);
		for(int i = 0; i < sb.length(); i++) {
			char currentChar = sb.charAt(i);
			if((currentChar == ')') || (currentChar == '(') ||
					(currentChar == '!') || (currentChar == '|') ||
					(currentChar == '&') || (currentChar == ',') ||
					(currentChar == '.') || (currentChar == '-') ||
					(currentChar == '\n') || (currentChar == '\r') ||
					(currentChar == '>') || (currentChar == ' ')) {
				// we can safely skip these
			}
			else {
				// we found the start of a name
				int nameStart = i;
				int nameEnd = sb.indexOf("(", nameStart);
				if(nameEnd == -1) {
					// error, there is no end to this name!
					throw new RuntimeException("An invalid name was in in the proposition: " +
							proposition + ".  There was no parameter list.");
				}

				// replace the name with a resolved name if one exists
				String name = sb.substring(nameStart, nameEnd);
				name = name.trim();
				log.debug("found a name in the proposition: " + name);

				String newName = resolvePredicateName(name);
				log.debug("resolved name: " + newName);

				if((newName != null) && (newName.length() > 0) && (newName.length() > name.length())) {
					sb.replace(nameStart, nameEnd, newName);

					// shift the end of the name since the newName will probably be longer
					nameEnd = nameStart + newName.length();
				}

				int parametersEnd = sb.indexOf(")", nameEnd);
				if(parametersEnd == -1) {
					throw new RuntimeException("An invalid name was in in the proposition: " +
							proposition + ".  The parameter list never ends.");
				}
				i = parametersEnd;
			}
		}

		return(sb.toString().trim());
	}

}
