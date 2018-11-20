/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WatchType.java,v 1.1 2003/04/30 19:33:40 tcw Exp $
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
 * Class WatchType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:40 $
 */
public abstract class WatchType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _variableList
     */
    private java.util.ArrayList _variableList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WatchType() {
        super();
        _variableList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.WatchType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addVariable
     * 
     * @param vVariable
     */
    public void addVariable(java.lang.String vVariable)
        throws java.lang.IndexOutOfBoundsException
    {
        _variableList.add(vVariable);
    } //-- void addVariable(java.lang.String) 

    /**
     * Method addVariable
     * 
     * @param index
     * @param vVariable
     */
    public void addVariable(int index, java.lang.String vVariable)
        throws java.lang.IndexOutOfBoundsException
    {
        _variableList.add(index, vVariable);
    } //-- void addVariable(int, java.lang.String) 

    /**
     * Method clearVariable
     */
    public void clearVariable()
    {
        _variableList.clear();
    } //-- void clearVariable() 

    /**
     * Method enumerateVariable
     */
    public java.util.Enumeration enumerateVariable()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_variableList.iterator());
    } //-- java.util.Enumeration enumerateVariable() 

    /**
     * Method getVariable
     * 
     * @param index
     */
    public java.lang.String getVariable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _variableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_variableList.get(index);
    } //-- java.lang.String getVariable(int) 

    /**
     * Method getVariable
     */
    public java.lang.String[] getVariable()
    {
        int size = _variableList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_variableList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getVariable() 

    /**
     * Method getVariableCount
     */
    public int getVariableCount()
    {
        return _variableList.size();
    } //-- int getVariableCount() 

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
     * Method removeVariable
     * 
     * @param vVariable
     */
    public boolean removeVariable(java.lang.String vVariable)
    {
        boolean removed = _variableList.remove(vVariable);
        return removed;
    } //-- boolean removeVariable(java.lang.String) 

    /**
     * Method setVariable
     * 
     * @param index
     * @param vVariable
     */
    public void setVariable(int index, java.lang.String vVariable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _variableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _variableList.set(index, vVariable);
    } //-- void setVariable(int, java.lang.String) 

    /**
     * Method setVariable
     * 
     * @param variableArray
     */
    public void setVariable(java.lang.String[] variableArray)
    {
        //-- copy array
        _variableList.clear();
        for (int i = 0; i < variableArray.length; i++) {
            _variableList.add(variableArray[i]);
        }
    } //-- void setVariable(java.lang.String) 

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
