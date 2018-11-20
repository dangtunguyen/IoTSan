/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: BindingType.java,v 1.1 2003/04/30 19:33:40 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class BindingType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:40 $
 */
public class BindingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The instance type
     */
    public static final int INSTANCE_TYPE = 0;

    /**
     * The instance of the instance type
     */
    public static final BindingType INSTANCE = new BindingType(INSTANCE_TYPE, "instance");

    /**
     * The exact type
     */
    public static final int EXACT_TYPE = 1;

    /**
     * The instance of the exact type
     */
    public static final BindingType EXACT = new BindingType(EXACT_TYPE, "exact");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private BindingType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of BindingType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this BindingType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("instance", INSTANCE);
        members.put("exact", EXACT);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * BindingType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new BindingType based on the given
     * String value.
     * 
     * @param string
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid BindingType";
            throw new IllegalArgumentException(err);
        }
        return (BindingType) obj;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType valueOf(java.lang.String) 

}
