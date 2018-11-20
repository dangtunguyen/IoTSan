/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MethodType.java,v 1.1 2003/04/30 19:33:36 tcw Exp $
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
 * Class MethodType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:36 $
 */
public abstract class MethodType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _signature
     */
    private java.lang.String _signature;

    /**
     * Field _return
     */
    private java.lang.String _return;

    /**
     * Field _localList
     */
    private java.util.ArrayList _localList;


      //----------------/
     //- Constructors -/
    //----------------/

    public MethodType() {
        super();
        _localList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.MethodType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addLocal
     * 
     * @param vLocal
     */
    public void addLocal(edu.ksu.cis.bandera.sessions.parser.v2.Local vLocal)
        throws java.lang.IndexOutOfBoundsException
    {
        _localList.add(vLocal);
    } //-- void addLocal(edu.ksu.cis.bandera.sessions.parser.v2.Local) 

    /**
     * Method addLocal
     * 
     * @param index
     * @param vLocal
     */
    public void addLocal(int index, edu.ksu.cis.bandera.sessions.parser.v2.Local vLocal)
        throws java.lang.IndexOutOfBoundsException
    {
        _localList.add(index, vLocal);
    } //-- void addLocal(int, edu.ksu.cis.bandera.sessions.parser.v2.Local) 

    /**
     * Method clearLocal
     */
    public void clearLocal()
    {
        _localList.clear();
    } //-- void clearLocal() 

    /**
     * Method enumerateLocal
     */
    public java.util.Enumeration enumerateLocal()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_localList.iterator());
    } //-- java.util.Enumeration enumerateLocal() 

    /**
     * Method getLocal
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Local getLocal(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _localList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.Local) _localList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Local getLocal(int) 

    /**
     * Method getLocal
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Local[] getLocal()
    {
        int size = _localList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.Local[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.Local[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.Local) _localList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Local[] getLocal() 

    /**
     * Method getLocalCount
     */
    public int getLocalCount()
    {
        return _localList.size();
    } //-- int getLocalCount() 

    /**
     * Method getReturnReturns the value of field 'return'.
     * 
     * @return the value of field 'return'.
     */
    public java.lang.String getReturn()
    {
        return this._return;
    } //-- java.lang.String getReturn() 

    /**
     * Method getSignatureReturns the value of field 'signature'.
     * 
     * @return the value of field 'signature'.
     */
    public java.lang.String getSignature()
    {
        return this._signature;
    } //-- java.lang.String getSignature() 

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
     * Method removeLocal
     * 
     * @param vLocal
     */
    public boolean removeLocal(edu.ksu.cis.bandera.sessions.parser.v2.Local vLocal)
    {
        boolean removed = _localList.remove(vLocal);
        return removed;
    } //-- boolean removeLocal(edu.ksu.cis.bandera.sessions.parser.v2.Local) 

    /**
     * Method setLocal
     * 
     * @param index
     * @param vLocal
     */
    public void setLocal(int index, edu.ksu.cis.bandera.sessions.parser.v2.Local vLocal)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _localList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _localList.set(index, vLocal);
    } //-- void setLocal(int, edu.ksu.cis.bandera.sessions.parser.v2.Local) 

    /**
     * Method setLocal
     * 
     * @param localArray
     */
    public void setLocal(edu.ksu.cis.bandera.sessions.parser.v2.Local[] localArray)
    {
        //-- copy array
        _localList.clear();
        for (int i = 0; i < localArray.length; i++) {
            _localList.add(localArray[i]);
        }
    } //-- void setLocal(edu.ksu.cis.bandera.sessions.parser.v2.Local) 

    /**
     * Method setReturnSets the value of field 'return'.
     * 
     * @param _return
     * @param return the value of field 'return'.
     */
    public void setReturn(java.lang.String _return)
    {
        this._return = _return;
    } //-- void setReturn(java.lang.String) 

    /**
     * Method setSignatureSets the value of field 'signature'.
     * 
     * @param signature the value of field 'signature'.
     */
    public void setSignature(java.lang.String signature)
    {
        this._signature = signature;
    } //-- void setSignature(java.lang.String) 

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
