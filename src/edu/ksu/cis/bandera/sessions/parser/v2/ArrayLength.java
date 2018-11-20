/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: ArrayLength.java,v 1.1 2003/04/30 19:33:32 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:32 $
**/
public class ArrayLength implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _max;

    /**
     * keeps track of state for field: _max
    **/
    private boolean _has_max;


      //----------------/
     //- Constructors -/
    //----------------/

    public ArrayLength() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ArrayLength()


      //-----------/
     //- Methods -/
    //-----------/

    /**
    **/
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
     * Returns the value of field 'max'.
     * 
     * @return the value of field 'max'.
    **/
    public int getMax()
    {
        return this._max;
    } //-- int getMax() 

    /**
    **/
    public boolean hasMax()
    {
        return this._has_max;
    } //-- boolean hasMax() 

    /**
    **/
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
     * 
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'max'.
     * 
     * @param max the value of field 'max'.
    **/
    public void setMax(int max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(int) 

    /**
     * 
     * 
     * @param reader
    **/
    public static edu.ksu.cis.bandera.sessions.parser.v2.ArrayLength unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.ArrayLength) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.ArrayLength.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ArrayLength unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
