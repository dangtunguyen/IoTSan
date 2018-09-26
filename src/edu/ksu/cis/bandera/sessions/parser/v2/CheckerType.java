/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CheckerType.java,v 1.1 2003/04/30 19:33:34 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class CheckerType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:34 $
 */
public abstract class CheckerType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _enabled
     */
    private boolean _enabled = true;

    /**
     * keeps track of state for field: _enabled
     */
    private boolean _has_enabled;

    /**
     * Field _name
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType _name;

    /**
     * Field _checkerOptions
     */
    private java.lang.String _checkerOptions;

    /**
     * Field _birOptions
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.BirOptions _birOptions;


      //----------------/
     //- Constructors -/
    //----------------/

    public CheckerType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.CheckerType()


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
     * Method getBirOptionsReturns the value of field 'birOptions'.
     * 
     * @return the value of field 'birOptions'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.BirOptions getBirOptions()
    {
        return this._birOptions;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.BirOptions getBirOptions() 

    /**
     * Method getCheckerOptionsReturns the value of field
     * 'checkerOptions'.
     * 
     * @return the value of field 'checkerOptions'.
     */
    public java.lang.String getCheckerOptions()
    {
        return this._checkerOptions;
    } //-- java.lang.String getCheckerOptions() 

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
    public edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType getName()
    {
        return this._name;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType getName() 

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
     * Method setBirOptionsSets the value of field 'birOptions'.
     * 
     * @param birOptions the value of field 'birOptions'.
     */
    public void setBirOptions(edu.ksu.cis.bandera.sessions.parser.v2.BirOptions birOptions)
    {
        this._birOptions = birOptions;
    } //-- void setBirOptions(edu.ksu.cis.bandera.sessions.parser.v2.BirOptions) 

    /**
     * Method setCheckerOptionsSets the value of field
     * 'checkerOptions'.
     * 
     * @param checkerOptions the value of field 'checkerOptions'.
     */
    public void setCheckerOptions(java.lang.String checkerOptions)
    {
        this._checkerOptions = checkerOptions;
    } //-- void setCheckerOptions(java.lang.String) 

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
    public void setName(edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType name)
    {
        this._name = name;
    } //-- void setName(edu.ksu.cis.bandera.sessions.parser.v2.types.CheckerNameType) 

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
