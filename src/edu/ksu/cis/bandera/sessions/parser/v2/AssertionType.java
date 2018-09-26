/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AssertionType.java,v 1.1 2003/04/30 19:33:33 tcw Exp $
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
 * Class AssertionType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:33 $
 */
public abstract class AssertionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _enabled
     */
    private boolean _enabled = true;

    /**
     * keeps track of state for field: _enabled
     */
    private boolean _has_enabled;


      //----------------/
     //- Constructors -/
    //----------------/

    public AssertionType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.AssertionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteEnabled
     */
    public void deleteEnabled()
    {
        this._has_enabled= false;
    } //-- void deleteEnabled() 

    /**
     * Method getEnabledReturns the value of field 'enabled'.
     * 
     * @return the value of field 'enabled'.
     */
    public boolean getEnabled()
    {
        return this._enabled;
    } //-- boolean getEnabled() 

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
     * Method hasEnabled
     */
    public boolean hasEnabled()
    {
        return this._has_enabled;
    } //-- boolean hasEnabled() 

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
     * Method setEnabledSets the value of field 'enabled'.
     * 
     * @param enabled the value of field 'enabled'.
     */
    public void setEnabled(boolean enabled)
    {
        this._enabled = enabled;
        this._has_enabled = true;
    } //-- void setEnabled(boolean) 

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
