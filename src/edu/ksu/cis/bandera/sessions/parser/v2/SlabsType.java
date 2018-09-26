/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SlabsType.java,v 1.1 2003/04/30 19:33:38 tcw Exp $
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
 * Class SlabsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:38 $
 */
public abstract class SlabsType implements java.io.Serializable {


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
     * Field _abstractionOption
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOption _abstractionOption;


      //----------------/
     //- Constructors -/
    //----------------/

    public SlabsType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.SlabsType()


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
     * Method getAbstractionOptionReturns the value of field
     * 'abstractionOption'.
     * 
     * @return the value of field 'abstractionOption'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOption getAbstractionOption()
    {
        return this._abstractionOption;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOption getAbstractionOption() 

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
     * Method setAbstractionOptionSets the value of field
     * 'abstractionOption'.
     * 
     * @param abstractionOption the value of field
     * 'abstractionOption'.
     */
    public void setAbstractionOption(edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOption abstractionOption)
    {
        this._abstractionOption = abstractionOption;
    } //-- void setAbstractionOption(edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOption) 

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
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
