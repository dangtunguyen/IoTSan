/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ThreadInstanceBounds.java,v 1.1 2003/04/30 19:33:39 tcw Exp $
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
 * Class ThreadInstanceBounds.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:39 $
 */
public class ThreadInstanceBounds implements java.io.Serializable {


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
     * Field _threadInstanceBoundList
     */
    private java.util.ArrayList _threadInstanceBoundList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ThreadInstanceBounds() {
        super();
        _threadInstanceBoundList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addThreadInstanceBound
     * 
     * @param vThreadInstanceBound
     */
    public void addThreadInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound vThreadInstanceBound)
        throws java.lang.IndexOutOfBoundsException
    {
        _threadInstanceBoundList.add(vThreadInstanceBound);
    } //-- void addThreadInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound) 

    /**
     * Method addThreadInstanceBound
     * 
     * @param index
     * @param vThreadInstanceBound
     */
    public void addThreadInstanceBound(int index, edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound vThreadInstanceBound)
        throws java.lang.IndexOutOfBoundsException
    {
        _threadInstanceBoundList.add(index, vThreadInstanceBound);
    } //-- void addThreadInstanceBound(int, edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound) 

    /**
     * Method clearThreadInstanceBound
     */
    public void clearThreadInstanceBound()
    {
        _threadInstanceBoundList.clear();
    } //-- void clearThreadInstanceBound() 

    /**
     * Method deleteMax
     */
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
     * Method enumerateThreadInstanceBound
     */
    public java.util.Enumeration enumerateThreadInstanceBound()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_threadInstanceBoundList.iterator());
    } //-- java.util.Enumeration enumerateThreadInstanceBound() 

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
     * Method getThreadInstanceBound
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound getThreadInstanceBound(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _threadInstanceBoundList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound) _threadInstanceBoundList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound getThreadInstanceBound(int) 

    /**
     * Method getThreadInstanceBound
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound[] getThreadInstanceBound()
    {
        int size = _threadInstanceBoundList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound) _threadInstanceBoundList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound[] getThreadInstanceBound() 

    /**
     * Method getThreadInstanceBoundCount
     */
    public int getThreadInstanceBoundCount()
    {
        return _threadInstanceBoundList.size();
    } //-- int getThreadInstanceBoundCount() 

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
     * Method removeThreadInstanceBound
     * 
     * @param vThreadInstanceBound
     */
    public boolean removeThreadInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound vThreadInstanceBound)
    {
        boolean removed = _threadInstanceBoundList.remove(vThreadInstanceBound);
        return removed;
    } //-- boolean removeThreadInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound) 

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
     * Method setThreadInstanceBound
     * 
     * @param index
     * @param vThreadInstanceBound
     */
    public void setThreadInstanceBound(int index, edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound vThreadInstanceBound)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _threadInstanceBoundList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _threadInstanceBoundList.set(index, vThreadInstanceBound);
    } //-- void setThreadInstanceBound(int, edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound) 

    /**
     * Method setThreadInstanceBound
     * 
     * @param threadInstanceBoundArray
     */
    public void setThreadInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound[] threadInstanceBoundArray)
    {
        //-- copy array
        _threadInstanceBoundList.clear();
        for (int i = 0; i < threadInstanceBoundArray.length; i++) {
            _threadInstanceBoundList.add(threadInstanceBoundArray[i]);
        }
    } //-- void setThreadInstanceBound(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBound) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds unmarshal(java.io.Reader) 

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
