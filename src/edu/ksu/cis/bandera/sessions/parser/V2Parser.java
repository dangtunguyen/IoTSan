package edu.ksu.cis.bandera.sessions.parser;

import edu.ksu.cis.bandera.sessions.parser.AbstractParser;
import edu.ksu.cis.bandera.sessions.parser.FileNotReadableException;
import edu.ksu.cis.bandera.sessions.parser.FileNotWritableException;
import edu.ksu.cis.bandera.sessions.parser.FileExistsException;
import edu.ksu.cis.bandera.sessions.parser.NotAFileException;

import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.Specification;
import edu.ksu.cis.bandera.sessions.Assertion;
import edu.ksu.cis.bandera.sessions.Predicate;
import edu.ksu.cis.bandera.sessions.Binding;
import edu.ksu.cis.bandera.sessions.TemporalProperty;
import edu.ksu.cis.bandera.sessions.Quantification;
import edu.ksu.cis.bandera.sessions.QuantifiedVariable;
import edu.ksu.cis.bandera.sessions.BIROptions;
import edu.ksu.cis.bandera.sessions.ResourceBounds;
import edu.ksu.cis.bandera.sessions.CounterExampleDescription;
import edu.ksu.cis.bandera.sessions.WatchVariable;
import edu.ksu.cis.bandera.sessions.Breakpoint;
import edu.ksu.cis.bandera.sessions.LockGraph;
import edu.ksu.cis.bandera.sessions.ObjectDiagram;
import edu.ksu.cis.bandera.sessions.Abstraction;
import edu.ksu.cis.bandera.sessions.ClassDescription;
import edu.ksu.cis.bandera.sessions.FieldDescription;
import edu.ksu.cis.bandera.sessions.MethodDescription;

import edu.ksu.cis.bandera.specification.pattern.PatternSaverLoader;

import edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Hashtable;
import java.util.Map;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.StringWriter;

import org.apache.log4j.Category;

public class V2Parser extends AbstractParser {

    private static final Category log = Category.getInstance(V2Parser.class);

    public List load(File file)
	throws FileNotFoundException, FileNotReadableException, NotAFileException, SessionFileException {

	if(file == null) {
	    throw new FileNotFoundException("A null file cannot be loaded.");
	}

	if(!file.exists()) {
	    throw new FileNotFoundException("The file given, " + file.getAbsolutePath() + ", cannot be found.");
	}

	if(!file.canRead()) {
	    throw new FileNotReadableException("The file given, " + file.getAbsolutePath() + ", cannot be read.");
	}

	if(!file.isFile()) {
	    throw new NotAFileException("The file given, " + file.getAbsolutePath() + ", is not a file.");
	}

	log.debug("Loading the session file using the Sessions.unmarshal method ...");
	edu.ksu.cis.bandera.sessions.parser.v2.Sessions sessions = null;
	try {
	    FileReader fr = new FileReader(file);
	    sessions = edu.ksu.cis.bandera.sessions.parser.v2.Sessions.unmarshal(fr);

	    /*
	    StringWriter sw = new StringWriter();
	    sessions.marshal(sw);
	    System.out.println("Session data:");
	    System.out.println("**********************************************");
	    System.out.println(sw.toString());
	    System.out.println("**********************************************");
	    */
	}
	catch(Exception e) {
	    throw new SessionFileException("Exception while loading the file (" + file.getAbsolutePath() +
					   ") using Sessions.marshal().", e);
	}
	log.debug("Loaded successfully.");

	// get the base directory from which to reference files -> the session file's directory
	File base = file.getParentFile();
	try {
	    base = base.getCanonicalFile();
	}
	catch(Exception e) {
	}

	log.debug("Converting the list of Session objects ...");

	List sessionList = new ArrayList();
	if((sessions != null) && (sessions.getSessionCount() > 0)) {
	    edu.ksu.cis.bandera.sessions.parser.v2.Session oldSession = null;
	    Session newSession = null;
	    for(int i = 0; i < sessions.getSessionCount(); i++) {
		oldSession = sessions.getSession(i);
		newSession = new Session();

		newSession.setName(oldSession.getId());
		newSession.setDescription(oldSession.getDescription());

		edu.ksu.cis.bandera.sessions.parser.v2.Output output = oldSession.getOutput();
		String outputName = output.getName();
		newSession.setOutputName(outputName);
		String location = output.getLocation();
		File workingDirectory = getFile(location, base);
		newSession.setWorkingDirectory(workingDirectory);

		edu.ksu.cis.bandera.sessions.parser.v2.Application application = oldSession.getApplication();
		String mainClassFilename = application.getMain();
		File mainClassFile = getFile(mainClassFilename, base);
		newSession.setMainClassFile(mainClassFile);
		edu.ksu.cis.bandera.sessions.parser.v2.Classpath classpath = application.getClasspath();
		if((classpath != null) && (classpath.getResourceCount() > 0)) {
		    for(int j = 0; j < classpath.getResourceCount(); j++) {
			String resource = classpath.getResource(j);
			// change this possibly relative path into an absolute path
			File resourceFile = getFile(resource, base);
			resource = resourceFile.getAbsolutePath();
			newSession.addClasspathResource(resource);
		    }
		}
		edu.ksu.cis.bandera.sessions.parser.v2.Includes includes = application.getIncludes();
		if((includes != null) && (includes.getResourceCount() > 0)) {
		    for(int j = 0; j < includes.getResourceCount(); j++) {
			String resource = includes.getResource(j);
			newSession.addInclude(resource);
		    }
		}

		edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOption specificationOption =
		    oldSession.getSpecificationOption();
		if(specificationOption != null) {
		    Specification specification = new Specification();
		    edu.ksu.cis.bandera.sessions.parser.v2.Assertions assertions = specificationOption.getAssertions();
		    if((assertions != null) && (assertions.getAssertionCount() > 0)) {
			for(int j = 0; j < assertions.getAssertionCount(); j++) {
			    edu.ksu.cis.bandera.sessions.parser.v2.Assertion oldAssertion = assertions.getAssertion(j);
			    Assertion newAssertion = new Assertion();
			    newAssertion.setName(oldAssertion.getName());
			    newAssertion.setEnabled(oldAssertion.getEnabled());
			    specification.addAssertion(newAssertion);
			}
		    }
		    edu.ksu.cis.bandera.sessions.parser.v2.Temporal temporal = specificationOption.getTemporal();
		    if(temporal != null) {
			TemporalProperty temporalProperty = new TemporalProperty();

			edu.ksu.cis.bandera.sessions.parser.v2.Pattern oldPattern = temporal.getPattern();
			if(oldPattern != null) {
			    Pattern pattern = PatternSaverLoader.getPattern(oldPattern.getName(), oldPattern.getScope());
			    temporalProperty.setPattern(pattern);
			}
			else {
			    System.err.println("No pattern specified for this property!");
			}

			edu.ksu.cis.bandera.sessions.parser.v2.Quantification oldQuantification = temporal.getQuantification();
			if(oldQuantification != null) {
			    Quantification newQuantification = new Quantification();
			    edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType binding = oldQuantification.getBinding();
			    if(binding != null) {
				if(binding.toString().equals("instance")) {
				    newQuantification.setBinding(Binding.INSTANCE);
				}
				if(binding.toString().equals("exact")) {
				    newQuantification.setBinding(Binding.EXACT);
				}
			    }
			    for(int j = 0; j < oldQuantification.getQuantifiedVariableCount(); j++) {
				edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable oldQV =
				    oldQuantification.getQuantifiedVariable(j);
				QuantifiedVariable newQV = new QuantifiedVariable();
				newQV.setName(oldQV.getName());
				newQV.setType(oldQV.getType());
				newQuantification.addQuantifiedVariable(newQV);
			    }
			    temporalProperty.setQuantification(newQuantification);
			}

			for(int j = 0; j < temporal.getPredicateCount(); j++) {
			    edu.ksu.cis.bandera.sessions.parser.v2.Predicate oldPredicate = temporal.getPredicate(j);
			    Predicate newPredicate = new Predicate();
			    newPredicate.setName(oldPredicate.getName());
			    newPredicate.setExpression(oldPredicate.getContent());
			    temporalProperty.addPredicate(newPredicate);
			}

			specification.setTemporalProperty(temporalProperty);
		    }
		    newSession.setSpecification(specification);
		}

		// components
		edu.ksu.cis.bandera.sessions.parser.v2.Components components = oldSession.getComponents();
		if(components != null) {
		    edu.ksu.cis.bandera.sessions.parser.v2.Slabs slabs = components.getSlabs();
		    if(slabs != null) {
			newSession.setAbstractionEnabled(slabs.getEnabled());
			edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOption abstractionOption = slabs.getAbstractionOption();
			if(abstractionOption != null) {
			    Abstraction abstraction = new Abstraction();
			    abstraction.setDefaultIntegralAbstraction(abstractionOption.getDefaultIntegralAbstraction());
			    abstraction.setDefaultRealAbstraction(abstractionOption.getDefaultRealAbstraction());
			    for(int j = 0; j < abstractionOption.getClassOrInterfaceCount(); j++) {
				edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface clazz =
				    abstractionOption.getClassOrInterface(j);
				ClassDescription classDescription = new ClassDescription();
				abstraction.addClassDescription(classDescription);
				classDescription.setName(clazz.getName());
				log.debug("Adding class to the abstraction: " + classDescription.getName());
				for(int k = 0; k < clazz.getFieldCount(); k++) {
				    edu.ksu.cis.bandera.sessions.parser.v2.Field field = clazz.getField(k);
				    FieldDescription fieldDescription = new FieldDescription();
				    fieldDescription.setName(field.getName());
				    fieldDescription.setAbstraction(field.getAbstraction());
				    classDescription.addFieldDescription(fieldDescription);
				    log.debug("Adding field to class: " + fieldDescription.getName());
				}
				for(int k = 0; k < clazz.getMethodCount(); k++) {
				    edu.ksu.cis.bandera.sessions.parser.v2.Method method = clazz.getMethod(k);
				    MethodDescription methodDescription = new MethodDescription();
				    methodDescription.setName(method.getSignature());
				    methodDescription.setReturnType(method.getReturn());
				    classDescription.addMethodDescription(methodDescription);
				    log.debug("Adding method to class: " + methodDescription.getName());
				    for(int l = 0; l < method.getLocalCount(); l++) {
					edu.ksu.cis.bandera.sessions.parser.v2.Local local = method.getLocal(l);
					FieldDescription fieldDescription = new FieldDescription();
					fieldDescription.setName(local.getName());
					fieldDescription.setAbstraction(local.getAbstraction());
					methodDescription.addLocal(fieldDescription);
					log.debug("Adding local to method: " + fieldDescription.getName());
				    }
				}
			    }

			    log.debug("Setting an abstraction for this session.");
			    newSession.setAbstraction(abstraction);
			}
			else {
			    log.debug("No abstraction defined in the session.  Disabling abstraction.");
			    newSession.setAbstractionEnabled(false);
			}
		    }
		    else {
			log.debug("No abstraction defined in the session.  Disabling abstraction.");
			newSession.setAbstractionEnabled(false);
		    }

		    edu.ksu.cis.bandera.sessions.parser.v2.Slicer slicer = components.getSlicer();
		    if(slicer != null) {
			newSession.setSlicingEnabled(slicer.getEnabled());
		    }
		    else {
			newSession.setSlicingEnabled(false);
		    }

		    edu.ksu.cis.bandera.sessions.parser.v2.Checker checker = components.getChecker();
		    if(checker != null) {
			newSession.setModelCheckingEnabled(checker.getEnabled());

			String checkerName = "";
			String checkerOptions = "";
			edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType checkerNameType = checker.getName();
			if(checkerNameType == null) {
			    System.err.println("checkerNameType is null.  The checker name is invalid!");
			    checkerName = "";
			}
			else {
			    checkerName = checkerNameType.toString();
			    checkerOptions = checker.getCheckerOptions();
			    newSession.setProperty(Session.CHECKER_OPTIONS_PROPERTY, checkerOptions);
			}
			if((checkerName != null) && (checkerName.equals("Spin"))) {
			    newSession.setProperty(Session.CHECKER_NAME_PROPERTY, Session.SPIN_CHECKER_NAME_PROPERTY);
			    newSession.setProperty(Session.SPIN_OPTIONS_PROPERTY, checkerOptions);
			}
			if((checkerName != null) && (checkerName.equals("DSpin"))) {
			    newSession.setProperty(Session.CHECKER_NAME_PROPERTY, Session.DSPIN_CHECKER_NAME_PROPERTY);
			    newSession.setProperty(Session.DSPIN_OPTIONS_PROPERTY, checkerOptions);
			}
			if((checkerName != null) && (checkerName.equals("SMV"))) {
			    newSession.setProperty(Session.CHECKER_NAME_PROPERTY, Session.SMV_CHECKER_NAME_PROPERTY);
			    newSession.setProperty(Session.SMV_OPTIONS_PROPERTY, checkerOptions);
			}
			if((checkerName != null) && (checkerName.equals("JPF"))) {
			    newSession.setProperty(Session.CHECKER_NAME_PROPERTY, Session.JPF_CHECKER_NAME_PROPERTY);
			    newSession.setProperty(Session.JPF_OPTIONS_PROPERTY, checkerOptions);
			}
			if((checkerName != null) && (checkerName.equals("HSF-Spin"))) {
			    newSession.setProperty(Session.CHECKER_NAME_PROPERTY, Session.HSF_SPIN_CHECKER_NAME_PROPERTY);
			    newSession.setProperty(Session.HSF_SPIN_OPTIONS_PROPERTY, checkerOptions);
			}

			edu.ksu.cis.bandera.sessions.parser.v2.BirOptions birOptions = checker.getBirOptions();
			if(birOptions != null) {
			    BIROptions newBirOptions = new BIROptions();
			    newSession.setBIROptions(newBirOptions);
			    edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType searchModeType = birOptions.getSearchMode();
			    String searchMode = "";
			    if(searchModeType == null) {
				System.err.println("searchModeType is null.  The search mode is invalid!");
				searchMode = "";
			    }
			    else {
				searchMode = searchModeType.toString();
			    }
			    if((searchMode != null) && searchMode.equals("Choose Free")) {
				newBirOptions.setSearchMode(BIROptions.CHOOSE_FREE_MODE);
			    }
			    if((searchMode != null) && searchMode.equals("Resource Bounded")) {
				newBirOptions.setSearchMode(BIROptions.RESOURCE_BOUNDED_MODE);
			    }
			    if((searchMode != null) && searchMode.equals("Exhaustive")) {
				newBirOptions.setSearchMode(BIROptions.EXHAUSTIVE_MODE);
			    }
			    
			    ResourceBounds newResourceBounds = new ResourceBounds();
			    newBirOptions.setResourceBounds(newResourceBounds);
			    edu.ksu.cis.bandera.sessions.parser.v2.ResourceBounds resourceBounds = birOptions.getResourceBounds();
			    if(resourceBounds != null) {
				edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds integerBounds = resourceBounds.getIntegerBounds();
				if(integerBounds != null) {
				    newResourceBounds.setDefaultIntMax(integerBounds.getMax());
				    newResourceBounds.setDefaultIntMin(integerBounds.getMin());
				    
				    int fieldCount = integerBounds.getFieldIntegerBoundCount();
				    for(int j = 0; j < fieldCount; j++) {
					edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound fib =
					    integerBounds.getFieldIntegerBound(j);
					String fieldName = fib.getName();
					String className = fib.getClassName();
					int min = fib.getMin();
					int max = fib.getMax();
					newResourceBounds.setFieldBounds(className, fieldName, min, max);
				    }
				    
				    int localCount = integerBounds.getLocalIntegerBoundCount();
				    for(int j = 0; j < localCount; j++) {
					edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound fib =
					    integerBounds.getLocalIntegerBound(j);
					String localName = fib.getName();
					String className = fib.getClassName();
					String methodName = fib.getMethodName();
					int min = fib.getMin();
					int max = fib.getMax();
					newResourceBounds.setMethodLocalBounds(className, methodName, localName, min, max);
				    }
				}
				
				edu.ksu.cis.bandera.sessions.parser.v2.ArrayBounds arrayBounds = resourceBounds.getArrayBounds();
				if(arrayBounds != null) {
				    newResourceBounds.setArrayMax(arrayBounds.getMax());
				}
				
				edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds instanceBounds = resourceBounds.getInstanceBounds();
				if(instanceBounds != null) {
				    newResourceBounds.setDefaultAllocMax(instanceBounds.getMax());
				    for(int k = 0; k < instanceBounds.getInstanceBoundCount(); k++) {
					edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound instanceBound = instanceBounds.getInstanceBound(k);
					newResourceBounds.setAllocMax(instanceBound.getName(), instanceBound.getMax());
				    }
				}
				
				edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds threadInstanceBounds =
				    resourceBounds.getThreadInstanceBounds();
				if(threadInstanceBounds != null) {
				    newResourceBounds.setDefaultThreadMax(threadInstanceBounds.getMax());
				    for(int k = 0; k < threadInstanceBounds.getThreadInstanceBoundCount(); k++) {
					edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound threadInstanceBound =
					    threadInstanceBounds.getThreadInstanceBound(k);
					newResourceBounds.setThreadMax(threadInstanceBound.getName(), threadInstanceBound.getMax());
				    }
				}
			    }
			}
		    }
		    else {
			newSession.setModelCheckingEnabled(false);
		    }
		    edu.ksu.cis.bandera.sessions.parser.v2.CounterExample counterExample = components.getCounterExample();
		    if(counterExample != null) {
			CounterExampleDescription counterExampleDescription = new CounterExampleDescription();
			edu.ksu.cis.bandera.sessions.parser.v2.Watches watches = counterExample.getWatches();
			if((watches != null) && (watches.getVariableCount() > 0)) {
			    for(int j = 0; j < watches.getVariableCount(); j++) {
				String variable = watches.getVariable(j);
				WatchVariable watchVariable = new WatchVariable();
				watchVariable.setVariable(variable);
				counterExampleDescription.addWatchVariable(watchVariable);
			    }
			}

			edu.ksu.cis.bandera.sessions.parser.v2.BreakPoints breakPoints = counterExample.getBreakPoints();
			if((breakPoints != null) && (breakPoints.getPointCount() > 0)) {
			    for(int j = 0; j < breakPoints.getPointCount(); j++) {
				String point = breakPoints.getPoint(j);
				Breakpoint breakPoint = new Breakpoint();
				breakPoint.setPoint(point);
				counterExampleDescription.addBreakpoint(breakPoint);
			    }
			}

			edu.ksu.cis.bandera.sessions.parser.v2.LockGraphs lockGraphs = counterExample.getLockGraphs();
			if((lockGraphs != null) && (lockGraphs.getRootCount() > 0)) {
			    for(int j = 0; j < lockGraphs.getRootCount(); j++) {
				String root = lockGraphs.getRoot(j);
				LockGraph lockGraph = new LockGraph();
				lockGraph.setRoot(root);
				counterExampleDescription.addLockGraph(lockGraph);
			    }
			}

			edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagrams objectDiagrams = counterExample.getObjectDiagrams();
			if((objectDiagrams != null) && (objectDiagrams.getRootCount() > 0)) {
			    for(int j = 0; j < objectDiagrams.getRootCount(); j++) {
				String root = objectDiagrams.getRoot(j);
				ObjectDiagram objectDiagram = new ObjectDiagram();
				objectDiagram.setRoot(root);
				counterExampleDescription.addObjectDiagram(objectDiagram);
			    }
			}

			newSession.setCounterExampleDescription(counterExampleDescription);
		    }
		}

		log.debug("Adding a session(" + newSession.getName() + ") to the converted list of sessions.");
		sessionList.add(newSession);
	    }
	}

	log.debug("Finished loading the session file.");
	return(sessionList);
    }

    public void save(List sessionList, File file, boolean overwrite)
	throws FileNotFoundException, FileNotWritableException, NotAFileException, FileExistsException, SessionFileException {

	if(file == null) {
	    throw new FileNotFoundException("Cannot save to a null file.");
	}

	if(file.exists() && !overwrite) {
	    throw new FileExistsException("Cannot write to a file that exists without the overwrite flag being set.");
	}
	else {
	    try {
		file.createNewFile();
	    }
	    catch(Exception e) {
		throw new SessionFileException("An exception occured while creating the session file (" + file + ").", e);
	    }
	}

	/*
	if(!file.canWrite()) {
	    throw new FileNotWritableException("The file given, " + file.getAbsolutePath() + ", is not writable.");
	}
	*/

	if(!file.isFile()) {
	    throw new NotAFileException("The file given, " + file.getAbsolutePath() + ", is not a file.");
	}

	if((sessionList == null) || (sessionList.size() <= 0)) {
	    try {
		file.delete();
		file.createNewFile();
		return;
	    }
	    catch(Exception e) {
		throw new SessionFileException("An exception occured while clearing the session file " + file + ".", e);
	    }
	}


	// get the base directory from which to reference files -> the session file's directory
	File base = file.getParentFile();
	try {
	    base = base.getCanonicalFile();
	}
	catch(Exception e) {
	}

	// convert the sessionList to a Sessions object
	edu.ksu.cis.bandera.sessions.parser.v2.Sessions sessions =
	    new edu.ksu.cis.bandera.sessions.parser.v2.Sessions();
	for(int i = 0; i < sessionList.size(); i++) {
	    edu.ksu.cis.bandera.sessions.parser.v2.Session newSession =
		new edu.ksu.cis.bandera.sessions.parser.v2.Session();
	    Session oldSession = (Session)sessionList.get(i);

	    newSession.setId(oldSession.getName());
	    newSession.setDescription(oldSession.getDescription());

	    edu.ksu.cis.bandera.sessions.parser.v2.Output output =
		new edu.ksu.cis.bandera.sessions.parser.v2.Output();
	    output.setName(oldSession.getOutputName());
	    String location = getRelativePath(base, oldSession.getWorkingDirectory());
	    output.setLocation(location);
	    newSession.setOutput(output);

	    edu.ksu.cis.bandera.sessions.parser.v2.Application application =
		new edu.ksu.cis.bandera.sessions.parser.v2.Application();

	    String mainClassFilename = getRelativePath(base, oldSession.getMainClassFile());
	    application.setMain(mainClassFilename);

	    List oldClasspath = oldSession.getClasspath();
	    if((oldClasspath != null) && (oldClasspath.size() > 0)) {
		edu.ksu.cis.bandera.sessions.parser.v2.Classpath newClasspath =
		    new edu.ksu.cis.bandera.sessions.parser.v2.Classpath();
		for(int j = 0; j < oldClasspath.size(); j++) {
		    String resource = (String)oldClasspath.get(j);
		    // change this into an relative path before adding it to the new session. -tcw
		    File resourceFile = new File(resource);
		    resource = getRelativePath(base, resourceFile);
		    newClasspath.addResource(resource);
		}
		application.setClasspath(newClasspath);
	    }

	    List oldIncludes = oldSession.getIncludes();
	    if((oldIncludes != null) && (oldIncludes.size() > 0)) {
		edu.ksu.cis.bandera.sessions.parser.v2.Includes newIncludes =
		    new edu.ksu.cis.bandera.sessions.parser.v2.Includes();
		for(int j = 0; j < oldIncludes.size(); j++) {
		    String resource = (String)oldIncludes.get(j);
		    newIncludes.addResource(resource);
		}
		application.setIncludes(newIncludes);
	    }
	    newSession.setApplication(application);

	    Specification specification = oldSession.getSpecification();
	    if(specification != null) {
		edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOption specificationOption =
		    new edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOption();

		TemporalProperty temporalProperty = specification.getTemporalProperty();
		if(temporalProperty != null) {
		    edu.ksu.cis.bandera.sessions.parser.v2.Temporal temporal =
			new edu.ksu.cis.bandera.sessions.parser.v2.Temporal();
		    Pattern oldPattern = temporalProperty.getPattern();
		    if(oldPattern != null) {
			edu.ksu.cis.bandera.sessions.parser.v2.Pattern newPattern =
			    new edu.ksu.cis.bandera.sessions.parser.v2.Pattern();
			newPattern.setName(oldPattern.getName());
			newPattern.setScope(oldPattern.getScope());
			temporal.setPattern(newPattern);
		    }
		    Set predicates = temporalProperty.getPredicates();
		    if(predicates != null) {
			Iterator predicatesIterator = predicates.iterator();
			while(predicatesIterator.hasNext()) {
			    Predicate oldPredicate = (Predicate)predicatesIterator.next();
			    edu.ksu.cis.bandera.sessions.parser.v2.Predicate newPredicate =
				new edu.ksu.cis.bandera.sessions.parser.v2.Predicate();
			    newPredicate.setName(oldPredicate.getName());
			    newPredicate.setContent(oldPredicate.getExpression());
			    temporal.addPredicate(newPredicate);
			}
		    }
		    Quantification oldQuantification = temporalProperty.getQuantification();
		    if(oldQuantification != null) {
			Set quantifiedVariables = oldQuantification.getQuantifiedVariables();
			if(quantifiedVariables.size() > 0) {
			    edu.ksu.cis.bandera.sessions.parser.v2.Quantification newQuantification =
				new edu.ksu.cis.bandera.sessions.parser.v2.Quantification();
			    Iterator qvIterator = quantifiedVariables.iterator();
			    while(qvIterator.hasNext()) {
				QuantifiedVariable oldQV = (QuantifiedVariable)qvIterator.next();
				edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable newQV =
				    new edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable();
				newQV.setName(oldQV.getName());
				newQV.setType(oldQV.getType());
				newQuantification.addQuantifiedVariable(newQV);
			    }
			    temporal.setQuantification(newQuantification);
			}
		    }
		    specificationOption.setTemporal(temporal);
		}

		Set oldAssertions = specification.getAssertions();
		if((oldAssertions != null) && (oldAssertions.size() > 0)) {
		    edu.ksu.cis.bandera.sessions.parser.v2.Assertions newAssertions =
			new edu.ksu.cis.bandera.sessions.parser.v2.Assertions();
		    Iterator assertionIterator = oldAssertions.iterator();
		    while(assertionIterator.hasNext()) {
			Assertion oldAssertion = (Assertion)assertionIterator.next();
			edu.ksu.cis.bandera.sessions.parser.v2.Assertion newAssertion =
			    new edu.ksu.cis.bandera.sessions.parser.v2.Assertion();
			newAssertion.setName(oldAssertion.getName());
			newAssertion.setEnabled(oldAssertion.enabled());
			newAssertions.addAssertion(newAssertion);
		    }
		    specificationOption.setAssertions(newAssertions);
		}

		newSession.setSpecificationOption(specificationOption);
	    }

	    edu.ksu.cis.bandera.sessions.parser.v2.Components components =
		new edu.ksu.cis.bandera.sessions.parser.v2.Components();

	    edu.ksu.cis.bandera.sessions.parser.v2.Slabs slabs =
		new edu.ksu.cis.bandera.sessions.parser.v2.Slabs();
	    slabs.setEnabled(oldSession.isAbstractionEnabled());
	    Abstraction abstraction = oldSession.getAbstraction();
	    if(abstraction != null) {
		edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOption abstractionOption =
		    new edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOption();
		abstractionOption.setDefaultIntegralAbstraction(abstraction.getDefaultIntegralAbstraction());
		abstractionOption.setDefaultRealAbstraction(abstraction.getDefaultRealAbstraction());
		Set classDescriptions = abstraction.getClassDescriptions();
		if((classDescriptions != null) && (classDescriptions.size() > 0)) {
		    //System.out.println("classDescriptions.size() = " + classDescriptions.size());
		    Iterator classIterator = classDescriptions.iterator();
		    while(classIterator.hasNext()) {
			ClassDescription classDescription = (ClassDescription)classIterator.next();
			edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface clazz =
			    new edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface();
			clazz.setName(classDescription.getName());
			//System.out.println("class name = " + clazz.getName());

			Set fieldDescriptions = classDescription.getFieldDescriptions();
			if((fieldDescriptions != null) && (fieldDescriptions.size() > 0)) {
			    Iterator fieldIterator = fieldDescriptions.iterator();
			    while(fieldIterator.hasNext()) {
				FieldDescription fd = (FieldDescription)fieldIterator.next();
				edu.ksu.cis.bandera.sessions.parser.v2.Field field =
				    new edu.ksu.cis.bandera.sessions.parser.v2.Field();
				field.setName(fd.getName());
				field.setAbstraction(fd.getAbstraction());
				clazz.addField(field);
				//System.out.println("fieldName = " + field.getName());
			    }
			}

			Set methodDescriptions = classDescription.getMethodDescriptions();
			if((methodDescriptions != null) && (methodDescriptions.size() > 0)) {
			    Iterator methodIterator = methodDescriptions.iterator();
			    while(methodIterator.hasNext()) {
				MethodDescription md = (MethodDescription)methodIterator.next();
				edu.ksu.cis.bandera.sessions.parser.v2.Method method =
				    new edu.ksu.cis.bandera.sessions.parser.v2.Method();
				method.setSignature(md.getName());
				method.setReturn(md.getReturnType());
				Set localSet = md.getLocals();
				if((localSet != null) && (localSet.size() > 0)) {
				    Iterator localIterator = localSet.iterator();
				    while(localIterator.hasNext()) {
					FieldDescription fd = (FieldDescription)localIterator.next();
					edu.ksu.cis.bandera.sessions.parser.v2.Local local =
					    new edu.ksu.cis.bandera.sessions.parser.v2.Local();
					local.setName(fd.getName());
					local.setAbstraction(fd.getAbstraction());
					method.addLocal(local);
				    }
				}
				clazz.addMethod(method);
			    }
			}
			abstractionOption.addClassOrInterface(clazz);
		    }
		}
		slabs.setAbstractionOption(abstractionOption);
	    }
	    components.setSlabs(slabs);

	    edu.ksu.cis.bandera.sessions.parser.v2.Slicer slicer =
		new edu.ksu.cis.bandera.sessions.parser.v2.Slicer();
	    slicer.setEnabled(oldSession.isSlicingEnabled());
	    components.setSlicer(slicer);
	    
	    edu.ksu.cis.bandera.sessions.parser.v2.Checker checker =
		new edu.ksu.cis.bandera.sessions.parser.v2.Checker();
	    checker.setEnabled(oldSession.isModelCheckingEnabled());
	    String checkerName = oldSession.getProperty(Session.CHECKER_NAME_PROPERTY);
	    //System.out.println("checkerName = " + checkerName);
	    if((checkerName != null) && (checkerName.equals(Session.SPIN_CHECKER_NAME_PROPERTY))) {
		checkerName = "Spin";
	    }
	    if((checkerName != null) && (checkerName.equals(Session.DSPIN_CHECKER_NAME_PROPERTY))) {
		checkerName = "DSpin";
	    }
	    if((checkerName != null) && (checkerName.equals(Session.HSF_SPIN_CHECKER_NAME_PROPERTY))) {
		checkerName = "HSF-Spin";
	    }
	    if((checkerName != null) && (checkerName.equals(Session.SMV_CHECKER_NAME_PROPERTY))) {
		checkerName = "SMV";
	    }
	    if((checkerName != null) && (checkerName.equals(Session.JPF_CHECKER_NAME_PROPERTY))) {
		checkerName = "JPF";
	    }
	    //System.out.println("checkerName = " + checkerName);
	    if((checkerName != null) && (checkerName.length() > 0)) {
		edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType checkerNameType =
		    edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType.valueOf(checkerName);
		checker.setName(checkerNameType);
		//System.out.println("checkerNameType = " + checkerNameType.toString());

		String checkerOptions = oldSession.getProperty(Session.CHECKER_OPTIONS_PROPERTY);
		//System.out.println("checkerOptions = " + checkerOptions);

		checker.setCheckerOptions(checkerOptions);
	    }
	    components.setChecker(checker);

	    BIROptions oldBirOptions = oldSession.getBIROptions();
	    if(oldBirOptions != null) {
		edu.ksu.cis.bandera.sessions.parser.v2.BirOptions newBirOptions =
		    new edu.ksu.cis.bandera.sessions.parser.v2.BirOptions();
		int oldSearchMode = oldBirOptions.getSearchMode();
		edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType newSearchMode = null;
		switch(oldSearchMode) {
		case BIROptions.CHOOSE_FREE_MODE:
		    newSearchMode = edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType.valueOf("Choose Free");
		    break;
		case BIROptions.EXHAUSTIVE_MODE:
		    newSearchMode = edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType.valueOf("Exhaustive");
		    break;
		case BIROptions.RESOURCE_BOUNDED_MODE:
		    newSearchMode = edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType.valueOf("Resource Bounded");
		    break;
		default:
		    newSearchMode = null;
		    break;
		}
		newBirOptions.setSearchMode(newSearchMode);

		ResourceBounds oldResourceBounds = oldBirOptions.getResourceBounds();
		if(oldResourceBounds != null) {
		    edu.ksu.cis.bandera.sessions.parser.v2.ResourceBounds newResourceBounds =
			new edu.ksu.cis.bandera.sessions.parser.v2.ResourceBounds();
		    edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds integerBounds =
			new edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds();
		    integerBounds.setMax(oldResourceBounds.getDefaultIntMax());
		    integerBounds.setMin(oldResourceBounds.getDefaultIntMin());
		    Map variableBoundMap = oldResourceBounds.getVariableBoundMap();
		    if((variableBoundMap != null) && (variableBoundMap.size() > 0)) {
			Iterator vbmi = variableBoundMap.keySet().iterator();
			while(vbmi.hasNext()) {
			    String className = (String)vbmi.next();
			    Map classMap = (Map)variableBoundMap.get(className);
			    if((classMap != null) && (classMap.size() > 0)) {
				Iterator cmi = classMap.keySet().iterator();
				while(cmi.hasNext()) {
				    String memberName = (String)cmi.next();
				    Object memberValue = classMap.get(memberName);
				    if(memberValue == null) {
					continue;
				    }
				    if(memberValue instanceof List) {
					// found a field
					List l = (List)memberValue;
					if((l != null) && (l.size() == 2)) {
					    int min = ((Integer)l.get(0)).intValue();
					    int max = ((Integer)l.get(1)).intValue();
					    edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound fib =
						new edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound();
					    fib.setName(memberName);
					    fib.setClassName(className);
					    fib.setMin(min);
					    fib.setMax(max);
					    integerBounds.addFieldIntegerBound(fib);
					}
				    }
				    if(memberValue instanceof Map) {
					// found a method
					Map methodMap = (Map)memberValue;
					Iterator mmi = methodMap.keySet().iterator();
					while(mmi.hasNext()) {
					    String localName = (String)mmi.next();
					    List l = (List)methodMap.get(localName);
					    if((l != null) && (l.size() == 2)) {
						int min = ((Integer)l.get(0)).intValue();
						int max = ((Integer)l.get(1)).intValue();
						edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound lib =
						    new edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound();
						lib.setName(localName);
						lib.setMethodName(memberName);
						lib.setClassName(className);
						lib.setMin(min);
						lib.setMax(max);
						integerBounds.addLocalIntegerBound(lib);
					    }
					}
				    }
				}
			    }
			}
		    }
		    newResourceBounds.setIntegerBounds(integerBounds);
		    
		    edu.ksu.cis.bandera.sessions.parser.v2.ArrayBounds arrayBounds =
			new edu.ksu.cis.bandera.sessions.parser.v2.ArrayBounds();
		    arrayBounds.setMax(oldResourceBounds.getArrayMax());
		    newResourceBounds.setArrayBounds(arrayBounds);
		    
		    edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds instanceBounds =
			new edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds();
		    instanceBounds.setMax(oldResourceBounds.getDefaultAllocMax());
		    Hashtable instanceTable = oldResourceBounds.getAllocMaxTable();
		    if(instanceTable != null) {
			Iterator instanceIterator = instanceTable.keySet().iterator();
			while(instanceIterator.hasNext()) {
			    String className = (String)instanceIterator.next();
			    int max = oldResourceBounds.getAllocMax(className);
			    edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound instanceBound =
				new edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound();
			    instanceBound.setName(className);
			    instanceBound.setMax(max);
			    instanceBounds.addInstanceBound(instanceBound);
			}
		    }
		    newResourceBounds.setInstanceBounds(instanceBounds);

		    edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds threadInstanceBounds =
			new edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds();
		    threadInstanceBounds.setMax(oldResourceBounds.getDefaultThreadMax());
		    Hashtable threadTable = oldResourceBounds.getThreadMaxTable();
		    if(threadTable != null) {
			Iterator threadIterator = threadTable.keySet().iterator();
			while(threadIterator.hasNext()) {
			    String threadName = (String)threadIterator.next();
			    int max = oldResourceBounds.getThreadMax(threadName);
			    edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound threadInstanceBound =
				new edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound();
			    threadInstanceBound.setName(threadName);
			    threadInstanceBound.setMax(max);
			    threadInstanceBounds.addThreadInstanceBound(threadInstanceBound);
			}
		    }
		    newResourceBounds.setThreadInstanceBounds(threadInstanceBounds);
		    
		    newBirOptions.setResourceBounds(newResourceBounds);
		}
		checker.setBirOptions(newBirOptions);
	    }

	    components.setChecker(checker);

	    CounterExampleDescription ced = oldSession.getCounterExampleDescription();
	    if(ced != null) {
		edu.ksu.cis.bandera.sessions.parser.v2.CounterExample counterExample =
		    new edu.ksu.cis.bandera.sessions.parser.v2.CounterExample();

		Set lockGraphSet = ced.getLockGraphs();
		if((lockGraphSet != null) && (lockGraphSet.size() > 0)) {
		    edu.ksu.cis.bandera.sessions.parser.v2.LockGraphs lockGraphs =
			new edu.ksu.cis.bandera.sessions.parser.v2.LockGraphs();
		    Iterator lockGraphIterator = lockGraphSet.iterator();
		    while(lockGraphIterator.hasNext()) {
			LockGraph lockGraph = (LockGraph)lockGraphIterator.next();
			lockGraphs.addRoot(lockGraph.getRoot());
		    }
		    counterExample.setLockGraphs(lockGraphs);
		}

		Set objectDiagramSet = ced.getObjectDiagrams();
		if((objectDiagramSet != null) && (objectDiagramSet.size() > 0)) {
		    edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagrams objectDiagrams =
			new edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagrams();
		    Iterator objectDiagramIterator = objectDiagramSet.iterator();
		    while(objectDiagramIterator.hasNext()) {
			ObjectDiagram objectDiagram = (ObjectDiagram)objectDiagramIterator.next();
			objectDiagrams.addRoot(objectDiagram.getRoot());
		    }
		    counterExample.setObjectDiagrams(objectDiagrams);
		}

		Set breakPointSet = ced.getBreakpoints();
		if((breakPointSet != null) && (breakPointSet.size() > 0)) {
		    edu.ksu.cis.bandera.sessions.parser.v2.BreakPoints breakPoints =
			new edu.ksu.cis.bandera.sessions.parser.v2.BreakPoints();
		    Iterator breakPointIterator = breakPointSet.iterator();
		    while(breakPointIterator.hasNext()) {
			Breakpoint breakPoint = (Breakpoint)breakPointIterator.next();
			breakPoints.addPoint(breakPoint.getPoint());
		    }
		    counterExample.setBreakPoints(breakPoints);
		}

		Set watchVariableSet = ced.getWatchVariables();
		if((watchVariableSet != null) && (watchVariableSet.size() > 0)) {
		    edu.ksu.cis.bandera.sessions.parser.v2.Watches watches =
			new edu.ksu.cis.bandera.sessions.parser.v2.Watches();
		    Iterator watchVariableIterator = watchVariableSet.iterator();
		    while(watchVariableIterator.hasNext()) {
			WatchVariable watchVariable = (WatchVariable)watchVariableIterator.next();
			watches.addVariable(watchVariable.getVariable());
		    }
		    counterExample.setWatches(watches);
		}

		components.setCounterExample(counterExample);
	    }

	    newSession.setComponents(components);

	    sessions.addSession(newSession);
	}

	// save the Sessions object
	try {
	    FileWriter fw = new FileWriter(file);
	    sessions.marshal(fw);
	}
	catch(Exception e) {
	    throw new SessionFileException("An exception (" + e.toString() +
					   ") occured while using Sessions.marshal to write the session data to " +
					   file + ".", e);
	}

    }
}
