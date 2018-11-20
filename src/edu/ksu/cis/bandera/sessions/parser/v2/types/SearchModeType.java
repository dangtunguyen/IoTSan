/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SearchModeType.java,v 1.1 2003/04/30 19:33:40 tcw Exp $
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
 * Class SearchModeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:40 $
 */
public class SearchModeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The ChooseFree type
     */
    public static final int CHOOSEFREE_TYPE = 0;

    /**
     * The instance of the ChooseFree type
     */
    public static final SearchModeType CHOOSEFREE = new SearchModeType(CHOOSEFREE_TYPE, "ChooseFree");

    /**
     * The Exhaustive type
     */
    public static final int EXHAUSTIVE_TYPE = 1;

    /**
     * The instance of the Exhaustive type
     */
    public static final SearchModeType EXHAUSTIVE = new SearchModeType(EXHAUSTIVE_TYPE, "Exhaustive");

    /**
     * The ResourceBounded type
     */
    public static final int RESOURCEBOUNDED_TYPE = 2;

    /**
     * The instance of the ResourceBounded type
     */
    public static final SearchModeType RESOURCEBOUNDED = new SearchModeType(RESOURCEBOUNDED_TYPE, "ResourceBounded");

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

    private SearchModeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of SearchModeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this SearchModeType
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
        members.put("ChooseFree", CHOOSEFREE);
        members.put("Exhaustive", EXHAUSTIVE);
        members.put("ResourceBounded", RESOURCEBOUNDED);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * SearchModeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new SearchModeType based on the
     * given String value.
     * 
     * @param string
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid SearchModeType";
            throw new IllegalArgumentException(err);
        }
        return (SearchModeType) obj;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType valueOf(java.lang.String) 

}
