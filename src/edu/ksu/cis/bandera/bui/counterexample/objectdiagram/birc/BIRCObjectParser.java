package edu.ksu.cis.bandera.bui.counterexample.objectdiagram.birc;

import edu.ksu.cis.bandera.bui.counterexample.objectdiagram.*;
import edu.ksu.cis.bandera.bui.counterexample.TraceManager;
import edu.ksu.cis.bandera.bui.counterexample.BIRCTraceManager;
import edu.ksu.cis.bandera.bui.counterexample.ValueNode;

import java.util.Vector;

import edu.ksu.cis.bandera.birc.*;

import org.apache.log4j.Category;

import ca.mcgill.sable.soot.SootField;
import java.util.*;

/**
 * Insert the type's description here.
 * 
 * @author   &lt;<userEmail&gt;
 */
public class BIRCObjectParser implements ObjectParser {
	private TraceManager traceManager;
	private static Category log = Category.getInstance(BIRCObjectParser.class);
	private Map valueMap;
/**
 * Insert the method's description here.
 * Creation date: (1/18/02 1:13:00 PM)
 * @param traceManager edu.ksu.cis.bandera.bui.counterexample.TraceManager
 */
public BIRCObjectParser(TraceManager traceManager) {
	this.traceManager = traceManager;
	valueMap = new HashMap();
}
/**
 * Decode the value and place the relevant contents into an object of type ObjectData.
 * The most important things for the object are the type and name.  If it is an array, we
 * will also decode each of the values in the array.  If it is a class, we will decode
 * each field and the associated values.
 *
 * Creation date: (1/22/02 5:30:06 PM)
 *
 * @return ObjectData An ObjectData object that represents the given value.  This might be null.
 * @param Object value The value to decode.
 */
private ObjectData decode(Object value) {

    if (value == null) {
        log.info("value is null.  We will return a null value.");
        return (null);
    }

    /* is this still necessary? */
    if (value instanceof SootField) {
        SootField sootField = (SootField) value;
        String sootFieldName = sootField.getName();
        String sootFieldTypeString = sootField.getType().toString();
        
        ObjectData objectData = new ObjectData(sootFieldName, sootFieldTypeString);

        // now get the value of this field thru the BIRCTraceManager
        BIRCTraceManager btm = (BIRCTraceManager) traceManager;
        Object sootFieldValue = btm.getValueAsObject(sootField);
        
        ObjectData sootFieldValueObjectData = decode(sootFieldValue);

        // inline the value if it is a fundamental type
        Object tempValue = sootFieldValueObjectData;
        if (sootFieldValueObjectData != null) {
            String sootFieldValueObjectDataType = sootFieldValueObjectData.getType();
            if ((sootFieldValueObjectDataType.equals("int"))
                || (sootFieldValueObjectDataType.equals("boolean"))) {
                tempValue = sootFieldValueObjectData.getValue();
            }
        }

        objectData.setValue(tempValue);

        valueMap.put(tempValue, objectData);

        return (objectData);
    }

    if (value instanceof BooleanLiteral) {
        BooleanLiteral booleanLiteral = (BooleanLiteral) value;
        ObjectData objectData = new ObjectData("", "boolean");
        objectData.setValue(new Boolean(booleanLiteral.value));

        valueMap.put(value, objectData);

        return (objectData);
    }

    if (value instanceof IntegerLiteral) {
        IntegerLiteral integerLiteral = (IntegerLiteral) value;
        ObjectData objectData = new ObjectData("", "int");
        objectData.setValue(new Integer(integerLiteral.value));

        valueMap.put(value, objectData);

        return (objectData);
    }

    if (value instanceof ArrayLiteral) {
        ArrayLiteral arrayLiteral = (ArrayLiteral) value;
        ObjectData objectData = new ObjectData("some type");

        valueMap.put(value, objectData);

        for (int i = 0; i < arrayLiteral.contents.length; i++) {
	        Object fieldValue = arrayLiteral.contents[i];
	        
            ObjectData currentObjectData = null;
            if(valueMap.containsKey(fieldValue)) {
	            currentObjectData = (ObjectData)valueMap.get(fieldValue);
            }
            else {
	            currentObjectData = decode(fieldValue);
	            valueMap.put(fieldValue, currentObjectData);
            }

            if (currentObjectData == null) {
                log.info("skipping this field because it decodes to a null value.");
                continue;
            }

            String currentObjectDataType = currentObjectData.getType();
            objectData.addField(
                String.valueOf(i),
                currentObjectDataType,
                currentObjectData);

            // on the first pass, set the type of the array to the type of the first object in the array.
            if (i == 0) {
                // set the type of the array
                objectData.setType(currentObjectDataType);
            }
        }

        return (objectData);
    }

    if (value instanceof ReferenceLiteral) {
        ReferenceLiteral referenceLiteral = (ReferenceLiteral) value;
        ObjectData objectData = decode(referenceLiteral.value);

        valueMap.put(value, objectData);
        
        return (objectData);
    }

    if (value instanceof ClassLiteral) {
        ClassLiteral classLiteral = (ClassLiteral) value;
        ObjectData objectData =
            new ObjectData(
                String.valueOf(classLiteral.getId()),
                classLiteral.getSource().getName());

            valueMap.put(value, objectData);

        // now add all the fields into this object
        Vector fields = classLiteral.getFields();
        if (fields == null) {
            return (objectData);
        }

        for (int i = 0; i < fields.size(); i++) {
            Object currentField = fields.get(i);

            if((currentField != null) && (currentField instanceof SootField)) {
	            SootField currentSootField = (SootField)currentField;
	            String currentFieldName = currentSootField.getName();
	            String currentFieldTypeString = currentSootField.getType().toString();
	            Object currentFieldValue = null;

	            Object undecodedValue = classLiteral.getFieldValue(currentSootField);
	            currentFieldValue = decode(undecodedValue);

	            if(currentFieldValue == null) {
		            currentFieldValue = "<null/>";
	            }
	            else {
	            	if((currentFieldValue instanceof ObjectData) &&
		            	((currentFieldTypeString.equals("int")) ||
		            	 (currentFieldTypeString.equals("boolean")))) {
			            	currentFieldValue = ((ObjectData)currentFieldValue).getValue();
		            	}
	            }

	            objectData.addField(currentFieldName, currentFieldTypeString, currentFieldValue);

            }
            else {
	            log.info("Skipped this field (" + i + ") because it is null or not a SootField.");
            }

        }

        return (objectData);
    }

    // if all else fails, just return a null value. -tcw
    log.info(
        "Not sure how to handle this value's ("
            + value
            + ") type ("
            + value.getClass().getName()
            + ") this is so we will return a null value.");
    return (null);
}
/**
 * Insert the method's description here.
 * Creation date: (1/18/02 1:11:42 PM)
 * @return edu.ksu.cis.bandera.bui.counterexample.objectdiagram.ObjectData
 * @param Object The object from which to retrieve the info to create an ObjectData object.
 * @pre object is of type ValueNode
 */
public ObjectData parse(Object object) {

    if (object == null) {
        return (null);
    }

    if ((traceManager == null) || (!(traceManager instanceof BIRCTraceManager))) {
        log.error(
            "A valid TraceManager is required in order to parse this object."
                + "  It must be non-null and an instance of BIRCTraceManager.");
        return (null);
    }

    if (!(object instanceof ValueNode)) {
        log.error(
            "object must be a ValueNode.  Cannot parse an object that is not a ValueNode.");
        return (null);
    }

    if(valueMap == null) {
	    log.error("valueMap is null.  Cannot parse an object when the BIRCObjectParser has not been initialized properly.");
	    return(null);
    }

    valueMap.clear();

    try {
        ValueNode valueNode = (ValueNode) object;
        if(valueNode.object == null) {
	        log.info("valueNode.object is null.  There is not object stored in this ValueNode." +
		        "  Cannot parse an object that is null.");
	        return(null);
        }

        BIRCTraceManager btm = (BIRCTraceManager) traceManager;
        Object temp = btm.getValueAsObject(valueNode);
        
        ObjectData objectData = decode(temp);

        /* HACK: This is a really sloppy hack.  Since we cannot figure out the name of this field name
         * inside the decode method, we will attempt to do it here.  This should really be fixed
         * when it is figured out how. -tcw
         */
        if((objectData != null) &&
	        ((objectData.getType().equals("int")) ||
	        (objectData.getType().equals("boolean")))) {
	        if((valueNode.object != null) && (valueNode.object instanceof SootField)) {
		        SootField sf = (SootField)valueNode.object;
	        	objectData.setName(sf.getName());
	        }
        }
        
        return(objectData);
        
    } catch (Exception e) {
        log.error("Exception caught while parsing an object.");
        log.error("Exception = " + e.toString());
    }

    return (null);
}
}
