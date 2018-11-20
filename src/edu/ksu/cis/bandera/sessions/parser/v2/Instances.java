/*
 * This class was automatically generated with 
 * <a href="http://castor.exolab.org">Castor 0.9.3.9+</a>, using an
 * XML Schema.
 * $Id: Instances.java,v 1.1 2003/04/30 19:33:35 tcw Exp $
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
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:35 $
**/
public class Instances implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    private int _max;

    /**
     * keeps track of state for field: _max
    **/
    private boolean _has_max;

    private java.util.ArrayList _instanceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Instances() {
        super();
        _instanceList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Instances()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * 
     * 
     * @param vInstance
    **/
    public void addInstance(Instance vInstance)
        throws java.lang.IndexOutOfBoundsException
    {
        _instanceList.add(vInstance);
    } //-- void addInstance(Instance) 

    /**
     * 
     * 
     * @param index
     * @param vInstance
    **/
    public void addInstance(int index, Instance vInstance)
        throws java.lang.IndexOutOfBoundsException
    {
        _instanceList.add(index, vInstance);
    } //-- void addInstance(int, Instance) 

    /**
    **/
    public void clearInstance()
    {
        _instanceList.clear();
    } //-- void clearInstance() 

    /**
    **/
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
    **/
    public java.util.Enumeration enumerateInstance()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_instanceList.iterator());
    } //-- java.util.Enumeration enumerateInstance() 

    /**
     * 
     * 
     * @param index
    **/
    public Instance getInstance(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instanceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Instance) _instanceList.get(index);
    } //-- Instance getInstance(int) 

    /**
    **/
    public Instance[] getInstance()
    {
        int size = _instanceList.size();
        Instance[] mArray = new Instance[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Instance) _instanceList.get(index);
        }
        return mArray;
    } //-- Instance[] getInstance() 

    /**
    **/
    public int getInstanceCount()
    {
        return _instanceList.size();
    } //-- int getInstanceCount() 

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
     * 
     * 
     * @param vInstance
    **/
    public boolean removeInstance(Instance vInstance)
    {
        boolean removed = _instanceList.remove(vInstance);
        return removed;
    } //-- boolean removeInstance(Instance) 

    /**
     * 
     * 
     * @param index
     * @param vInstance
    **/
    public void setInstance(int index, Instance vInstance)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instanceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _instanceList.set(index, vInstance);
    } //-- void setInstance(int, Instance) 

    /**
     * 
     * 
     * @param instanceArray
    **/
    public void setInstance(Instance[] instanceArray)
    {
        //-- copy array
        _instanceList.clear();
        for (int i = 0; i < instanceArray.length; i++) {
            _instanceList.add(instanceArray[i]);
        }
    } //-- void setInstance(Instance) 

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
    public static edu.ksu.cis.bandera.sessions.parser.v2.Instances unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.Instances) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.Instances.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Instances unmarshal(java.io.Reader) 

    /**
    **/
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
