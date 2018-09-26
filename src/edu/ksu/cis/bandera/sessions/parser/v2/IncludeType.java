/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: IncludeType.java,v 1.1 2003/04/30 19:33:35 tcw Exp $
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
 * Class IncludeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:35 $
 */
public abstract class IncludeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resourceList
     */
    private java.util.ArrayList _resourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public IncludeType() {
        super();
        _resourceList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.IncludeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addResource
     * 
     * @param vResource
     */
    public void addResource(java.lang.String vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _resourceList.add(vResource);
    } //-- void addResource(java.lang.String) 

    /**
     * Method addResource
     * 
     * @param index
     * @param vResource
     */
    public void addResource(int index, java.lang.String vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _resourceList.add(index, vResource);
    } //-- void addResource(int, java.lang.String) 

    /**
     * Method clearResource
     */
    public void clearResource()
    {
        _resourceList.clear();
    } //-- void clearResource() 

    /**
     * Method enumerateResource
     */
    public java.util.Enumeration enumerateResource()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_resourceList.iterator());
    } //-- java.util.Enumeration enumerateResource() 

    /**
     * Method getResource
     * 
     * @param index
     */
    public java.lang.String getResource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_resourceList.get(index);
    } //-- java.lang.String getResource(int) 

    /**
     * Method getResource
     */
    public java.lang.String[] getResource()
    {
        int size = _resourceList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_resourceList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getResource() 

    /**
     * Method getResourceCount
     */
    public int getResourceCount()
    {
        return _resourceList.size();
    } //-- int getResourceCount() 

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
     * Method removeResource
     * 
     * @param vResource
     */
    public boolean removeResource(java.lang.String vResource)
    {
        boolean removed = _resourceList.remove(vResource);
        return removed;
    } //-- boolean removeResource(java.lang.String) 

    /**
     * Method setResource
     * 
     * @param index
     * @param vResource
     */
    public void setResource(int index, java.lang.String vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _resourceList.set(index, vResource);
    } //-- void setResource(int, java.lang.String) 

    /**
     * Method setResource
     * 
     * @param resourceArray
     */
    public void setResource(java.lang.String[] resourceArray)
    {
        //-- copy array
        _resourceList.clear();
        for (int i = 0; i < resourceArray.length; i++) {
            _resourceList.add(resourceArray[i]);
        }
    } //-- void setResource(java.lang.String) 

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
