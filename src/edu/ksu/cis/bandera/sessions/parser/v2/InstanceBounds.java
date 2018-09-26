/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: InstanceBounds.java,v 1.1 2003/04/30 19:33:35 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class InstanceBounds.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:35 $
 */
public class InstanceBounds implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _max
     */
    private int _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;

    /**
     * Field _instanceBoundList
     */
    private java.util.ArrayList _instanceBoundList;


      //----------------/
     //- Constructors -/
    //----------------/

    public InstanceBounds() {
        super();
        _instanceBoundList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addInstanceBound
     * 
     * @param vInstanceBound
     */
    public void addInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound vInstanceBound)
        throws java.lang.IndexOutOfBoundsException
    {
        _instanceBoundList.add(vInstanceBound);
    } //-- void addInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound) 

    /**
     * Method addInstanceBound
     * 
     * @param index
     * @param vInstanceBound
     */
    public void addInstanceBound(int index, edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound vInstanceBound)
        throws java.lang.IndexOutOfBoundsException
    {
        _instanceBoundList.add(index, vInstanceBound);
    } //-- void addInstanceBound(int, edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound) 

    /**
     * Method clearInstanceBound
     */
    public void clearInstanceBound()
    {
        _instanceBoundList.clear();
    } //-- void clearInstanceBound() 

    /**
     * Method deleteMax
     */
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
     * Method enumerateInstanceBound
     */
    public java.util.Enumeration enumerateInstanceBound()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_instanceBoundList.iterator());
    } //-- java.util.Enumeration enumerateInstanceBound() 

    /**
     * Method getInstanceBound
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound getInstanceBound(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instanceBoundList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound) _instanceBoundList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound getInstanceBound(int) 

    /**
     * Method getInstanceBound
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound[] getInstanceBound()
    {
        int size = _instanceBoundList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound) _instanceBoundList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound[] getInstanceBound() 

    /**
     * Method getInstanceBoundCount
     */
    public int getInstanceBoundCount()
    {
        return _instanceBoundList.size();
    } //-- int getInstanceBoundCount() 

    /**
     * Method getMaxReturns the value of field 'max'.
     * 
     * @return the value of field 'max'.
     */
    public int getMax()
    {
        return this._max;
    } //-- int getMax() 

    /**
     * Method hasMax
     */
    public boolean hasMax()
    {
        return this._has_max;
    } //-- boolean hasMax() 

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
     * Method removeInstanceBound
     * 
     * @param vInstanceBound
     */
    public boolean removeInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound vInstanceBound)
    {
        boolean removed = _instanceBoundList.remove(vInstanceBound);
        return removed;
    } //-- boolean removeInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound) 

    /**
     * Method setInstanceBound
     * 
     * @param index
     * @param vInstanceBound
     */
    public void setInstanceBound(int index, edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound vInstanceBound)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instanceBoundList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _instanceBoundList.set(index, vInstanceBound);
    } //-- void setInstanceBound(int, edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound) 

    /**
     * Method setInstanceBound
     * 
     * @param instanceBoundArray
     */
    public void setInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound[] instanceBoundArray)
    {
        //-- copy array
        _instanceBoundList.clear();
        for (int i = 0; i < instanceBoundArray.length; i++) {
            _instanceBoundList.add(instanceBoundArray[i]);
        }
    } //-- void setInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBound) 

    /**
     * Method setMaxSets the value of field 'max'.
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(int max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(int) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds unmarshal(java.io.Reader) 

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
