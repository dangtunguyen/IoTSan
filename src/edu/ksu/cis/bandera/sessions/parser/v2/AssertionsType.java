/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AssertionsType.java,v 1.1 2003/04/30 19:33:33 tcw Exp $
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
 * Class AssertionsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:33 $
 */
public abstract class AssertionsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _assertionList
     */
    private java.util.ArrayList _assertionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AssertionsType() {
        super();
        _assertionList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.AssertionsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAssertion
     * 
     * @param vAssertion
     */
    public void addAssertion(edu.ksu.cis.bandera.sessions.parser.v2.Assertion vAssertion)
        throws java.lang.IndexOutOfBoundsException
    {
        _assertionList.add(vAssertion);
    } //-- void addAssertion(edu.ksu.cis.bandera.sessions.parser.v2.Assertion) 

    /**
     * Method addAssertion
     * 
     * @param index
     * @param vAssertion
     */
    public void addAssertion(int index, edu.ksu.cis.bandera.sessions.parser.v2.Assertion vAssertion)
        throws java.lang.IndexOutOfBoundsException
    {
        _assertionList.add(index, vAssertion);
    } //-- void addAssertion(int, edu.ksu.cis.bandera.sessions.parser.v2.Assertion) 

    /**
     * Method clearAssertion
     */
    public void clearAssertion()
    {
        _assertionList.clear();
    } //-- void clearAssertion() 

    /**
     * Method enumerateAssertion
     */
    public java.util.Enumeration enumerateAssertion()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_assertionList.iterator());
    } //-- java.util.Enumeration enumerateAssertion() 

    /**
     * Method getAssertion
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Assertion getAssertion(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _assertionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.Assertion) _assertionList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Assertion getAssertion(int) 

    /**
     * Method getAssertion
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Assertion[] getAssertion()
    {
        int size = _assertionList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.Assertion[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.Assertion[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.Assertion) _assertionList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Assertion[] getAssertion() 

    /**
     * Method getAssertionCount
     */
    public int getAssertionCount()
    {
        return _assertionList.size();
    } //-- int getAssertionCount() 

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
     * Method removeAssertion
     * 
     * @param vAssertion
     */
    public boolean removeAssertion(edu.ksu.cis.bandera.sessions.parser.v2.Assertion vAssertion)
    {
        boolean removed = _assertionList.remove(vAssertion);
        return removed;
    } //-- boolean removeAssertion(edu.ksu.cis.bandera.sessions.parser.v2.Assertion) 

    /**
     * Method setAssertion
     * 
     * @param index
     * @param vAssertion
     */
    public void setAssertion(int index, edu.ksu.cis.bandera.sessions.parser.v2.Assertion vAssertion)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _assertionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _assertionList.set(index, vAssertion);
    } //-- void setAssertion(int, edu.ksu.cis.bandera.sessions.parser.v2.Assertion) 

    /**
     * Method setAssertion
     * 
     * @param assertionArray
     */
    public void setAssertion(edu.ksu.cis.bandera.sessions.parser.v2.Assertion[] assertionArray)
    {
        //-- copy array
        _assertionList.clear();
        for (int i = 0; i < assertionArray.length; i++) {
            _assertionList.add(assertionArray[i]);
        }
    } //-- void setAssertion(edu.ksu.cis.bandera.sessions.parser.v2.Assertion) 

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
