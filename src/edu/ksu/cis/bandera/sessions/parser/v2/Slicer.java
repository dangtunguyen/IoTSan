/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Slicer.java,v 1.1 2003/04/30 19:33:38 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Slicer.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:38 $
 */
public class Slicer implements java.io.Serializable {


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


      //----------------/
     //- Constructors -/
    //----------------/

    public Slicer() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Slicer()


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
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

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
     * Method unmarshal
     * 
     * @param reader
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.Slicer unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.Slicer) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.Slicer.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Slicer unmarshal(java.io.Reader) 

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
