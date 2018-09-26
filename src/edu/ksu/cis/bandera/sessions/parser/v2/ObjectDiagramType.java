/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ObjectDiagramType.java,v 1.1 2003/04/30 19:33:36 tcw Exp $
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
 * Class ObjectDiagramType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:36 $
 */
public abstract class ObjectDiagramType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _rootList
     */
    private java.util.ArrayList _rootList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ObjectDiagramType() {
        super();
        _rootList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagramType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRoot
     * 
     * @param vRoot
     */
    public void addRoot(java.lang.String vRoot)
        throws java.lang.IndexOutOfBoundsException
    {
        _rootList.add(vRoot);
    } //-- void addRoot(java.lang.String) 

    /**
     * Method addRoot
     * 
     * @param index
     * @param vRoot
     */
    public void addRoot(int index, java.lang.String vRoot)
        throws java.lang.IndexOutOfBoundsException
    {
        _rootList.add(index, vRoot);
    } //-- void addRoot(int, java.lang.String) 

    /**
     * Method clearRoot
     */
    public void clearRoot()
    {
        _rootList.clear();
    } //-- void clearRoot() 

    /**
     * Method enumerateRoot
     */
    public java.util.Enumeration enumerateRoot()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_rootList.iterator());
    } //-- java.util.Enumeration enumerateRoot() 

    /**
     * Method getRoot
     * 
     * @param index
     */
    public java.lang.String getRoot(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rootList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_rootList.get(index);
    } //-- java.lang.String getRoot(int) 

    /**
     * Method getRoot
     */
    public java.lang.String[] getRoot()
    {
        int size = _rootList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_rootList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getRoot() 

    /**
     * Method getRootCount
     */
    public int getRootCount()
    {
        return _rootList.size();
    } //-- int getRootCount() 

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
     * Method removeRoot
     * 
     * @param vRoot
     */
    public boolean removeRoot(java.lang.String vRoot)
    {
        boolean removed = _rootList.remove(vRoot);
        return removed;
    } //-- boolean removeRoot(java.lang.String) 

    /**
     * Method setRoot
     * 
     * @param index
     * @param vRoot
     */
    public void setRoot(int index, java.lang.String vRoot)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _rootList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _rootList.set(index, vRoot);
    } //-- void setRoot(int, java.lang.String) 

    /**
     * Method setRoot
     * 
     * @param rootArray
     */
    public void setRoot(java.lang.String[] rootArray)
    {
        //-- copy array
        _rootList.clear();
        for (int i = 0; i < rootArray.length; i++) {
            _rootList.add(rootArray[i]);
        }
    } //-- void setRoot(java.lang.String) 

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
