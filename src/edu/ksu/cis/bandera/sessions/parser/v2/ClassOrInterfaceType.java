/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ClassOrInterfaceType.java,v 1.1 2003/04/30 19:33:34 tcw Exp $
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
 * Class ClassOrInterfaceType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:34 $
 */
public abstract class ClassOrInterfaceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _fieldList
     */
    private java.util.ArrayList _fieldList;

    /**
     * Field _methodList
     */
    private java.util.ArrayList _methodList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ClassOrInterfaceType() {
        super();
        _fieldList = new ArrayList();
        _methodList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterfaceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addField
     * 
     * @param vField
     */
    public void addField(edu.ksu.cis.bandera.sessions.parser.v2.Field vField)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldList.add(vField);
    } //-- void addField(edu.ksu.cis.bandera.sessions.parser.v2.Field) 

    /**
     * Method addField
     * 
     * @param index
     * @param vField
     */
    public void addField(int index, edu.ksu.cis.bandera.sessions.parser.v2.Field vField)
        throws java.lang.IndexOutOfBoundsException
    {
        _fieldList.add(index, vField);
    } //-- void addField(int, edu.ksu.cis.bandera.sessions.parser.v2.Field) 

    /**
     * Method addMethod
     * 
     * @param vMethod
     */
    public void addMethod(edu.ksu.cis.bandera.sessions.parser.v2.Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        _methodList.add(vMethod);
    } //-- void addMethod(edu.ksu.cis.bandera.sessions.parser.v2.Method) 

    /**
     * Method addMethod
     * 
     * @param index
     * @param vMethod
     */
    public void addMethod(int index, edu.ksu.cis.bandera.sessions.parser.v2.Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        _methodList.add(index, vMethod);
    } //-- void addMethod(int, edu.ksu.cis.bandera.sessions.parser.v2.Method) 

    /**
     * Method clearField
     */
    public void clearField()
    {
        _fieldList.clear();
    } //-- void clearField() 

    /**
     * Method clearMethod
     */
    public void clearMethod()
    {
        _methodList.clear();
    } //-- void clearMethod() 

    /**
     * Method enumerateField
     */
    public java.util.Enumeration enumerateField()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_fieldList.iterator());
    } //-- java.util.Enumeration enumerateField() 

    /**
     * Method enumerateMethod
     */
    public java.util.Enumeration enumerateMethod()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_methodList.iterator());
    } //-- java.util.Enumeration enumerateMethod() 

    /**
     * Method getField
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Field getField(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.Field) _fieldList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Field getField(int) 

    /**
     * Method getField
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Field[] getField()
    {
        int size = _fieldList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.Field[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.Field[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.Field) _fieldList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Field[] getField() 

    /**
     * Method getFieldCount
     */
    public int getFieldCount()
    {
        return _fieldList.size();
    } //-- int getFieldCount() 

    /**
     * Method getMethod
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Method getMethod(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _methodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.Method) _methodList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Method getMethod(int) 

    /**
     * Method getMethod
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Method[] getMethod()
    {
        int size = _methodList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.Method[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.Method[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.Method) _methodList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Method[] getMethod() 

    /**
     * Method getMethodCount
     */
    public int getMethodCount()
    {
        return _methodList.size();
    } //-- int getMethodCount() 

    /**
     * Method getNameReturns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

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
     * Method removeField
     * 
     * @param vField
     */
    public boolean removeField(edu.ksu.cis.bandera.sessions.parser.v2.Field vField)
    {
        boolean removed = _fieldList.remove(vField);
        return removed;
    } //-- boolean removeField(edu.ksu.cis.bandera.sessions.parser.v2.Field) 

    /**
     * Method removeMethod
     * 
     * @param vMethod
     */
    public boolean removeMethod(edu.ksu.cis.bandera.sessions.parser.v2.Method vMethod)
    {
        boolean removed = _methodList.remove(vMethod);
        return removed;
    } //-- boolean removeMethod(edu.ksu.cis.bandera.sessions.parser.v2.Method) 

    /**
     * Method setField
     * 
     * @param index
     * @param vField
     */
    public void setField(int index, edu.ksu.cis.bandera.sessions.parser.v2.Field vField)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _fieldList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _fieldList.set(index, vField);
    } //-- void setField(int, edu.ksu.cis.bandera.sessions.parser.v2.Field) 

    /**
     * Method setField
     * 
     * @param fieldArray
     */
    public void setField(edu.ksu.cis.bandera.sessions.parser.v2.Field[] fieldArray)
    {
        //-- copy array
        _fieldList.clear();
        for (int i = 0; i < fieldArray.length; i++) {
            _fieldList.add(fieldArray[i]);
        }
    } //-- void setField(edu.ksu.cis.bandera.sessions.parser.v2.Field) 

    /**
     * Method setMethod
     * 
     * @param index
     * @param vMethod
     */
    public void setMethod(int index, edu.ksu.cis.bandera.sessions.parser.v2.Method vMethod)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _methodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _methodList.set(index, vMethod);
    } //-- void setMethod(int, edu.ksu.cis.bandera.sessions.parser.v2.Method) 

    /**
     * Method setMethod
     * 
     * @param methodArray
     */
    public void setMethod(edu.ksu.cis.bandera.sessions.parser.v2.Method[] methodArray)
    {
        //-- copy array
        _methodList.clear();
        for (int i = 0; i < methodArray.length; i++) {
            _methodList.add(methodArray[i]);
        }
    } //-- void setMethod(edu.ksu.cis.bandera.sessions.parser.v2.Method) 

    /**
     * Method setNameSets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

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
