/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AbstractionOptionType.java,v 1.1 2003/04/30 19:33:32 tcw Exp $
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
 * Class AbstractionOptionType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:32 $
 */
public abstract class AbstractionOptionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _defaultIntegralAbstraction
     */
    private java.lang.String _defaultIntegralAbstraction;

    /**
     * Field _defaultRealAbstraction
     */
    private java.lang.String _defaultRealAbstraction;

    /**
     * Field _classOrInterfaceList
     */
    private java.util.ArrayList _classOrInterfaceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AbstractionOptionType() {
        super();
        _classOrInterfaceList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.AbstractionOptionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addClassOrInterface
     * 
     * @param vClassOrInterface
     */
    public void addClassOrInterface(edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface vClassOrInterface)
        throws java.lang.IndexOutOfBoundsException
    {
        _classOrInterfaceList.add(vClassOrInterface);
    } //-- void addClassOrInterface(edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface) 

    /**
     * Method addClassOrInterface
     * 
     * @param index
     * @param vClassOrInterface
     */
    public void addClassOrInterface(int index, edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface vClassOrInterface)
        throws java.lang.IndexOutOfBoundsException
    {
        _classOrInterfaceList.add(index, vClassOrInterface);
    } //-- void addClassOrInterface(int, edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface) 

    /**
     * Method clearClassOrInterface
     */
    public void clearClassOrInterface()
    {
        _classOrInterfaceList.clear();
    } //-- void clearClassOrInterface() 

    /**
     * Method enumerateClassOrInterface
     */
    public java.util.Enumeration enumerateClassOrInterface()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_classOrInterfaceList.iterator());
    } //-- java.util.Enumeration enumerateClassOrInterface() 

    /**
     * Method getClassOrInterface
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface getClassOrInterface(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _classOrInterfaceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface) _classOrInterfaceList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface getClassOrInterface(int) 

    /**
     * Method getClassOrInterface
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface[] getClassOrInterface()
    {
        int size = _classOrInterfaceList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface) _classOrInterfaceList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface[] getClassOrInterface() 

    /**
     * Method getClassOrInterfaceCount
     */
    public int getClassOrInterfaceCount()
    {
        return _classOrInterfaceList.size();
    } //-- int getClassOrInterfaceCount() 

    /**
     * Method getDefaultIntegralAbstractionReturns the value of
     * field 'defaultIntegralAbstraction'.
     * 
     * @return the value of field 'defaultIntegralAbstraction'.
     */
    public java.lang.String getDefaultIntegralAbstraction()
    {
        return this._defaultIntegralAbstraction;
    } //-- java.lang.String getDefaultIntegralAbstraction() 

    /**
     * Method getDefaultRealAbstractionReturns the value of field
     * 'defaultRealAbstraction'.
     * 
     * @return the value of field 'defaultRealAbstraction'.
     */
    public java.lang.String getDefaultRealAbstraction()
    {
        return this._defaultRealAbstraction;
    } //-- java.lang.String getDefaultRealAbstraction() 

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
     * Method removeClassOrInterface
     * 
     * @param vClassOrInterface
     */
    public boolean removeClassOrInterface(edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface vClassOrInterface)
    {
        boolean removed = _classOrInterfaceList.remove(vClassOrInterface);
        return removed;
    } //-- boolean removeClassOrInterface(edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface) 

    /**
     * Method setClassOrInterface
     * 
     * @param index
     * @param vClassOrInterface
     */
    public void setClassOrInterface(int index, edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface vClassOrInterface)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _classOrInterfaceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _classOrInterfaceList.set(index, vClassOrInterface);
    } //-- void setClassOrInterface(int, edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface) 

    /**
     * Method setClassOrInterface
     * 
     * @param classOrInterfaceArray
     */
    public void setClassOrInterface(edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface[] classOrInterfaceArray)
    {
        //-- copy array
        _classOrInterfaceList.clear();
        for (int i = 0; i < classOrInterfaceArray.length; i++) {
            _classOrInterfaceList.add(classOrInterfaceArray[i]);
        }
    } //-- void setClassOrInterface(edu.ksu.cis.bandera.sessions.parser.v2.ClassOrInterface) 

    /**
     * Method setDefaultIntegralAbstractionSets the value of field
     * 'defaultIntegralAbstraction'.
     * 
     * @param defaultIntegralAbstraction the value of field
     * 'defaultIntegralAbstraction'.
     */
    public void setDefaultIntegralAbstraction(java.lang.String defaultIntegralAbstraction)
    {
        this._defaultIntegralAbstraction = defaultIntegralAbstraction;
    } //-- void setDefaultIntegralAbstraction(java.lang.String) 

    /**
     * Method setDefaultRealAbstractionSets the value of field
     * 'defaultRealAbstraction'.
     * 
     * @param defaultRealAbstraction the value of field
     * 'defaultRealAbstraction'.
     */
    public void setDefaultRealAbstraction(java.lang.String defaultRealAbstraction)
    {
        this._defaultRealAbstraction = defaultRealAbstraction;
    } //-- void setDefaultRealAbstraction(java.lang.String) 

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
