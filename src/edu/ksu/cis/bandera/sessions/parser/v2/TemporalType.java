/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TemporalType.java,v 1.1 2003/04/30 19:33:39 tcw Exp $
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
 * Class TemporalType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:39 $
 */
public abstract class TemporalType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _quantification
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Quantification _quantification;

    /**
     * Field _pattern
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Pattern _pattern;

    /**
     * Field _predicateList
     */
    private java.util.ArrayList _predicateList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TemporalType() {
        super();
        _predicateList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.TemporalType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPredicate
     * 
     * @param vPredicate
     */
    public void addPredicate(edu.ksu.cis.bandera.sessions.parser.v2.Predicate vPredicate)
        throws java.lang.IndexOutOfBoundsException
    {
        _predicateList.add(vPredicate);
    } //-- void addPredicate(edu.ksu.cis.bandera.sessions.parser.v2.Predicate) 

    /**
     * Method addPredicate
     * 
     * @param index
     * @param vPredicate
     */
    public void addPredicate(int index, edu.ksu.cis.bandera.sessions.parser.v2.Predicate vPredicate)
        throws java.lang.IndexOutOfBoundsException
    {
        _predicateList.add(index, vPredicate);
    } //-- void addPredicate(int, edu.ksu.cis.bandera.sessions.parser.v2.Predicate) 

    /**
     * Method clearPredicate
     */
    public void clearPredicate()
    {
        _predicateList.clear();
    } //-- void clearPredicate() 

    /**
     * Method enumeratePredicate
     */
    public java.util.Enumeration enumeratePredicate()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_predicateList.iterator());
    } //-- java.util.Enumeration enumeratePredicate() 

    /**
     * Method getPatternReturns the value of field 'pattern'.
     * 
     * @return the value of field 'pattern'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Pattern getPattern()
    {
        return this._pattern;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Pattern getPattern() 

    /**
     * Method getPredicate
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Predicate getPredicate(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _predicateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.Predicate) _predicateList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Predicate getPredicate(int) 

    /**
     * Method getPredicate
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Predicate[] getPredicate()
    {
        int size = _predicateList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.Predicate[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.Predicate[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.Predicate) _predicateList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Predicate[] getPredicate() 

    /**
     * Method getPredicateCount
     */
    public int getPredicateCount()
    {
        return _predicateList.size();
    } //-- int getPredicateCount() 

    /**
     * Method getQuantificationReturns the value of field
     * 'quantification'.
     * 
     * @return the value of field 'quantification'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Quantification getQuantification()
    {
        return this._quantification;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Quantification getQuantification() 

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
     * Method removePredicate
     * 
     * @param vPredicate
     */
    public boolean removePredicate(edu.ksu.cis.bandera.sessions.parser.v2.Predicate vPredicate)
    {
        boolean removed = _predicateList.remove(vPredicate);
        return removed;
    } //-- boolean removePredicate(edu.ksu.cis.bandera.sessions.parser.v2.Predicate) 

    /**
     * Method setPatternSets the value of field 'pattern'.
     * 
     * @param pattern the value of field 'pattern'.
     */
    public void setPattern(edu.ksu.cis.bandera.sessions.parser.v2.Pattern pattern)
    {
        this._pattern = pattern;
    } //-- void setPattern(edu.ksu.cis.bandera.sessions.parser.v2.Pattern) 

    /**
     * Method setPredicate
     * 
     * @param index
     * @param vPredicate
     */
    public void setPredicate(int index, edu.ksu.cis.bandera.sessions.parser.v2.Predicate vPredicate)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _predicateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _predicateList.set(index, vPredicate);
    } //-- void setPredicate(int, edu.ksu.cis.bandera.sessions.parser.v2.Predicate) 

    /**
     * Method setPredicate
     * 
     * @param predicateArray
     */
    public void setPredicate(edu.ksu.cis.bandera.sessions.parser.v2.Predicate[] predicateArray)
    {
        //-- copy array
        _predicateList.clear();
        for (int i = 0; i < predicateArray.length; i++) {
            _predicateList.add(predicateArray[i]);
        }
    } //-- void setPredicate(edu.ksu.cis.bandera.sessions.parser.v2.Predicate) 

    /**
     * Method setQuantificationSets the value of field
     * 'quantification'.
     * 
     * @param quantification the value of field 'quantification'.
     */
    public void setQuantification(edu.ksu.cis.bandera.sessions.parser.v2.Quantification quantification)
    {
        this._quantification = quantification;
    } //-- void setQuantification(edu.ksu.cis.bandera.sessions.parser.v2.Quantification) 

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
