/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: Threads.java,v 1.1 2003/04/30 19:33:39 tcw Exp $
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
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:39 $
**/
public class Threads implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _max;

    /**
     * keeps track of state for field: _max
    **/
    private boolean _has_max;

    private java.util.ArrayList _threadList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Threads() {
        super();
        _threadList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Threads()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vThread
    **/
    public void addThread(Thread vThread)
        throws java.lang.IndexOutOfBoundsException
    {
        _threadList.add(vThread);
    } //-- void addThread(Thread) 

    /**
     * 
     * 
     * @param index
     * @param vThread
    **/
    public void addThread(int index, Thread vThread)
        throws java.lang.IndexOutOfBoundsException
    {
        _threadList.add(index, vThread);
    } //-- void addThread(int, Thread) 

    /**
    **/
    public void clearThread()
    {
        _threadList.clear();
    } //-- void clearThread() 

    /**
    **/
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
    **/
    public java.util.Enumeration enumerateThread()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_threadList.iterator());
    } //-- java.util.Enumeration enumerateThread() 

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
     * 
     * 
     * @param index
    **/
    public Thread getThread(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _threadList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Thread) _threadList.get(index);
    } //-- Thread getThread(int) 

    /**
    **/
    public Thread[] getThread()
    {
        int size = _threadList.size();
        Thread[] mArray = new Thread[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Thread) _threadList.get(index);
        }
        return mArray;
    } //-- Thread[] getThread() 

    /**
    **/
    public int getThreadCount()
    {
        return _threadList.size();
    } //-- int getThreadCount() 

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
     * 
     * 
     * @param vThread
    **/
    public boolean removeThread(Thread vThread)
    {
        boolean removed = _threadList.remove(vThread);
        return removed;
    } //-- boolean removeThread(Thread) 

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
     * @param index
     * @param vThread
    **/
    public void setThread(int index, Thread vThread)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _threadList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _threadList.set(index, vThread);
    } //-- void setThread(int, Thread) 

    /**
     * 
     * 
     * @param threadArray
    **/
    public void setThread(Thread[] threadArray)
    {
        //-- copy array
        _threadList.clear();
        for (int i = 0; i < threadArray.length; i++) {
            _threadList.add(threadArray[i]);
        }
    } //-- void setThread(Thread) 

    /**
     * 
     * 
     * @param reader
    **/
    public static edu.ksu.cis.bandera.sessions.parser.v2.Threads unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.Threads) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.Threads.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Threads unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
