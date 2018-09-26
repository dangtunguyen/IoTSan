package edu.ksu.cis.bandera.bui.counterexample.objectdiagram;

import org.apache.log4j.Category;

/**
 * Insert the type's description here.
 * Creation date: (1/21/02 1:50:00 PM)
 * @author: 
 */
public class JavaObjectParser implements ObjectParser {
	/**
	  * The log to use.
	  */
	private static Category log = Category.getInstance(JavaObjectParser.class);
	
/**
 * Parse the contents of a Java object into a tree of ObjectData objects.  This can
 * then be used to create the object diagram.
 *
 * Creation date: (1/21/02 1:50:00 PM)
 *
 * @return ObjectData The parsed value.
 * @param Object The object from which to parse values from to create the ObjectData.
 */
public ObjectData parse(Object object) {

	if(object == null) {
		return(null);
	}

    Class thisClass = object.getClass();
    String name = Integer.toString(object.hashCode());
    ObjectData objectData = new ObjectData(name, thisClass.getName());

    try {
        java.lang.reflect.Field[] fieldArray = thisClass.getFields();
        for (int i = 0; i < fieldArray.length; i++) {
            try {

                Object value = null;
                Object temp = fieldArray[i].get(object);
                if (temp == null) {
                    value = "null";
                }
                else {
                    if ((temp instanceof String) || (temp instanceof Number) ||
	                    (temp instanceof Boolean) || (temp instanceof Character) ||
	                    (temp instanceof StringBuffer) || (temp instanceof Thread)) {
                        value = temp.toString();
                    }
                    else {
	                    log.debug("now parsing an unknown type = " + temp.getClass().getName());
	                    value = parse(temp);
                    }
                }

	            objectData.addField(fieldArray[i].getName(), fieldArray[i].getType().getName(), value);
            }
            catch (Exception e) {
                log.error(
                    "Exception caught while getting a field.  Exception = " + e.toString());
            }
        }
    }
    catch (SecurityException se) {
        log.error(
            "SecurityException caught while getting the fields.  Exception = "
                + se.toString());
        objectData = null;
    }

    return(objectData);
}
}
