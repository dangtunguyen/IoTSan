/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: BreakType.java,v 1.1 2003/04/30 19:33:34 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class BreakType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:34 $
 */
public abstract class BreakType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pointList
     */
    private java.util.ArrayList _pointList;


      //----------------/
     //- Constructors -/
    //----------------/

    public BreakType() {
        super();
        _pointList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.BreakType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPoint
     * 
     * @param vPoint
     */
    public void addPoint(java.lang.String vPoint)
        throws java.lang.IndexOutOfBoundsException
    {
        _pointList.add(vPoint);
    } //-- void addPoint(java.lang.String) 

    /**
     * Method addPoint
     * 
     * @param index
     * @param vPoint
     */
    public void addPoint(int index, java.lang.String vPoint)
        throws java.lang.IndexOutOfBoundsException
    {
        _pointList.add(index, vPoint);
    } //-- void addPoint(int, java.lang.String) 

    /**
     * Method clearPoint
     */
    public void clearPoint()
    {
        _pointList.clear();
    } //-- void clearPoint() 

    /**
     * Method enumeratePoint
     */
    public java.util.Enumeration enumeratePoint()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_pointList.iterator());
    } //-- java.util.Enumeration enumeratePoint() 

    /**
     * Method getPoint
     * 
     * @param index
     */
    public java.lang.String getPoint(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pointList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_pointList.get(index);
    } //-- java.lang.String getPoint(int) 

    /**
     * Method getPoint
     */
    public java.lang.String[] getPoint()
    {
        int size = _pointList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_pointList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getPoint() 

    /**
     * Method getPointCount
     */
    public int getPointCount()
    {
        return _pointList.size();
    } //-- int getPointCount() 

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
     * Method removePoint
     * 
     * @param vPoint
     */
    public boolean removePoint(java.lang.String vPoint)
    {
        boolean removed = _pointList.remove(vPoint);
        return removed;
    } //-- boolean removePoint(java.lang.String) 

    /**
     * Method setPoint
     * 
     * @param index
     * @param vPoint
     */
    public void setPoint(int index, java.lang.String vPoint)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _pointList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _pointList.set(index, vPoint);
    } //-- void setPoint(int, java.lang.String) 

    /**
     * Method setPoint
     * 
     * @param pointArray
     */
    public void setPoint(java.lang.String[] pointArray)
    {
        //-- copy array
        _pointList.clear();
        for (int i = 0; i < pointArray.length; i++) {
            _pointList.add(pointArray[i]);
        }
    } //-- void setPoint(java.lang.String) 

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
