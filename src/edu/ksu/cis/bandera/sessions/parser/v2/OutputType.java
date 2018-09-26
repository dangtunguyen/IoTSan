/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: OutputType.java,v 1.1 2003/04/30 19:33:37 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class OutputType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:37 $
 */
public abstract class OutputType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _location
     */
    private java.lang.String _location;


      //----------------/
     //- Constructors -/
    //----------------/

    public OutputType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.OutputType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getLocationReturns the value of field 'location'.
     * 
     * @return the value of field 'location'.
     */
    public java.lang.String getLocation()
    {
        return this._location;
    } //-- java.lang.String getLocation() 

    /**
     * Method getNameReturns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method setLocationSets the value of field 'location'.
     * 
     * @param location the value of field 'location'.
     */
    public void setLocation(java.lang.String location)
    {
        this._location = location;
    } //-- void setLocation(java.lang.String) 

    /**
     * Method setNameSets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
