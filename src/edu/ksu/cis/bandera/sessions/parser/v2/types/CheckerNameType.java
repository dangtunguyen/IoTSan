/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CheckerNameType.java,v 1.1 2003/04/30 19:33:40 tcw Exp $
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
 * Class CheckerNameType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:40 $
 */
public class CheckerNameType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The JPF type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the JPF type
     */
    public static final CheckerNameType VALUE_0 = new CheckerNameType(VALUE_0_TYPE, "JPF");

    /**
     * The SMV type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the SMV type
     */
    public static final CheckerNameType VALUE_1 = new CheckerNameType(VALUE_1_TYPE, "SMV");

    /**
     * The Spin type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the Spin type
     */
    public static final CheckerNameType VALUE_2 = new CheckerNameType(VALUE_2_TYPE, "Spin");

    /**
     * The DSpin type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the DSpin type
     */
    public static final CheckerNameType VALUE_3 = new CheckerNameType(VALUE_3_TYPE, "DSpin");

    /**
     * The HSF-Spin type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the HSF-Spin type
     */
    public static final CheckerNameType VALUE_4 = new CheckerNameType(VALUE_4_TYPE, "HSF-Spin");

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

    private CheckerNameType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CheckerNameType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CheckerNameType
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
        members.put("JPF", VALUE_0);
        members.put("SMV", VALUE_1);
        members.put("Spin", VALUE_2);
        members.put("DSpin", VALUE_3);
        members.put("HSF-Spin", VALUE_4);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * CheckerNameType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CheckerNameType based on the
     * given String value.
     * 
     * @param string
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CheckerNameType";
            throw new IllegalArgumentException(err);
        }
        return (CheckerNameType) obj;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType valueOf(java.lang.String) 

}
