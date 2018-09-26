/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: IntegerBounds.java,v 1.1 2003/04/30 19:33:35 tcw Exp $
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
 * Class IntegerBounds.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:35 $
 */
public class IntegerBounds implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _min
     */
    private int _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * Field _max
     */
    private int _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;

    /**
     * Field _fieldIntegerBoundList
     */
    private java.util.ArrayList _fieldIntegerBoundList;

    /**
     * Field _localIntegerBoundList
     */
    private java.util.ArrayList _localIntegerBoundList;


      //----------------/
     //- Constructors -/
    //----------------/

    public IntegerBounds() {
        super();
        _fieldIntegerBoundList = new ArrayList();
        _localIntegerBoundList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFieldIntegerBound
     * 
     * @param vFieldIntegerBound
     */
    public void addFieldIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound vFieldIntegerBound)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldIntegerBoundList.add(vFieldIntegerBound);
    } //-- void addFieldIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound) 

    /**
     * Method addFieldIntegerBound
     * 
     * @param index
     * @param vFieldIntegerBound
     */
    public void addFieldIntegerBound(int index, edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound vFieldIntegerBound)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldIntegerBoundList.add(index, vFieldIntegerBound);
    } //-- void addFieldIntegerBound(int, edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound) 

    /**
     * Method addLocalIntegerBound
     * 
     * @param vLocalIntegerBound
     */
    public void addLocalIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound vLocalIntegerBound)
        throws java.lang.IndexOutOfBoundsException
    {
        _localIntegerBoundList.add(vLocalIntegerBound);
    } //-- void addLocalIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound) 

    /**
     * Method addLocalIntegerBound
     * 
     * @param index
     * @param vLocalIntegerBound
     */
    public void addLocalIntegerBound(int index, edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound vLocalIntegerBound)
        throws java.lang.IndexOutOfBoundsException
    {
        _localIntegerBoundList.add(index, vLocalIntegerBound);
    } //-- void addLocalIntegerBound(int, edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound) 

    /**
     * Method clearFieldIntegerBound
     */
    public void clearFieldIntegerBound()
    {
        _fieldIntegerBoundList.clear();
    } //-- void clearFieldIntegerBound() 

    /**
     * Method clearLocalIntegerBound
     */
    public void clearLocalIntegerBound()
    {
        _localIntegerBoundList.clear();
    } //-- void clearLocalIntegerBound() 

    /**
     * Method deleteMax
     */
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
     * Method deleteMin
     */
    public void deleteMin()
    {
        this._has_min= false;
    } //-- void deleteMin() 

    /**
     * Method enumerateFieldIntegerBound
     */
    public java.util.Enumeration enumerateFieldIntegerBound()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_fieldIntegerBoundList.iterator());
    } //-- java.util.Enumeration enumerateFieldIntegerBound() 

    /**
     * Method enumerateLocalIntegerBound
     */
    public java.util.Enumeration enumerateLocalIntegerBound()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_localIntegerBoundList.iterator());
    } //-- java.util.Enumeration enumerateLocalIntegerBound() 

    /**
     * Method getFieldIntegerBound
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound getFieldIntegerBound(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldIntegerBoundList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound) _fieldIntegerBoundList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound getFieldIntegerBound(int) 

    /**
     * Method getFieldIntegerBound
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound[] getFieldIntegerBound()
    {
        int size = _fieldIntegerBoundList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound) _fieldIntegerBoundList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound[] getFieldIntegerBound() 

    /**
     * Method getFieldIntegerBoundCount
     */
    public int getFieldIntegerBoundCount()
    {
        return _fieldIntegerBoundList.size();
    } //-- int getFieldIntegerBoundCount() 

    /**
     * Method getLocalIntegerBound
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound getLocalIntegerBound(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _localIntegerBoundList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound) _localIntegerBoundList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound getLocalIntegerBound(int) 

    /**
     * Method getLocalIntegerBound
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound[] getLocalIntegerBound()
    {
        int size = _localIntegerBoundList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound) _localIntegerBoundList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound[] getLocalIntegerBound() 

    /**
     * Method getLocalIntegerBoundCount
     */
    public int getLocalIntegerBoundCount()
    {
        return _localIntegerBoundList.size();
    } //-- int getLocalIntegerBoundCount() 

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
     * Method getMinReturns the value of field 'min'.
     * 
     * @return the value of field 'min'.
     */
    public int getMin()
    {
        return this._min;
    } //-- int getMin() 

    /**
     * Method hasMax
     */
    public boolean hasMax()
    {
        return this._has_max;
    } //-- boolean hasMax() 

    /**
     * Method hasMin
     */
    public boolean hasMin()
    {
        return this._has_min;
    } //-- boolean hasMin() 

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
     * Method removeFieldIntegerBound
     * 
     * @param vFieldIntegerBound
     */
    public boolean removeFieldIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound vFieldIntegerBound)
    {
        boolean removed = _fieldIntegerBoundList.remove(vFieldIntegerBound);
        return removed;
    } //-- boolean removeFieldIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound) 

    /**
     * Method removeLocalIntegerBound
     * 
     * @param vLocalIntegerBound
     */
    public boolean removeLocalIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound vLocalIntegerBound)
    {
        boolean removed = _localIntegerBoundList.remove(vLocalIntegerBound);
        return removed;
    } //-- boolean removeLocalIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound) 

    /**
     * Method setFieldIntegerBound
     * 
     * @param index
     * @param vFieldIntegerBound
     */
    public void setFieldIntegerBound(int index, edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound vFieldIntegerBound)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldIntegerBoundList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _fieldIntegerBoundList.set(index, vFieldIntegerBound);
    } //-- void setFieldIntegerBound(int, edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound) 

    /**
     * Method setFieldIntegerBound
     * 
     * @param fieldIntegerBoundArray
     */
    public void setFieldIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound[] fieldIntegerBoundArray)
    {
        //-- copy array
        _fieldIntegerBoundList.clear();
        for (int i = 0; i < fieldIntegerBoundArray.length; i++) {
            _fieldIntegerBoundList.add(fieldIntegerBoundArray[i]);
        }
    } //-- void setFieldIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBound) 

    /**
     * Method setLocalIntegerBound
     * 
     * @param index
     * @param vLocalIntegerBound
     */
    public void setLocalIntegerBound(int index, edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound vLocalIntegerBound)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _localIntegerBoundList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _localIntegerBoundList.set(index, vLocalIntegerBound);
    } //-- void setLocalIntegerBound(int, edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound) 

    /**
     * Method setLocalIntegerBound
     * 
     * @param localIntegerBoundArray
     */
    public void setLocalIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound[] localIntegerBoundArray)
    {
        //-- copy array
        _localIntegerBoundList.clear();
        for (int i = 0; i < localIntegerBoundArray.length; i++) {
            _localIntegerBoundList.add(localIntegerBoundArray[i]);
        }
    } //-- void setLocalIntegerBound(edu.ksu.cis.bandera.sessions.parser.v2.LocalIntegerBound) 

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
     * Method setMinSets the value of field 'min'.
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(int min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(int) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds unmarshal(java.io.Reader) 

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
