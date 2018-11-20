/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: QuantificationType.java,v 1.1 2003/04/30 19:33:37 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class QuantificationType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:37 $
 */
public abstract class QuantificationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _binding
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType _binding;

    /**
     * Field _quantifiedVariableList
     */
    private java.util.ArrayList _quantifiedVariableList;


      //----------------/
     //- Constructors -/
    //----------------/

    public QuantificationType() {
        super();
        _quantifiedVariableList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.QuantificationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addQuantifiedVariable
     * 
     * @param vQuantifiedVariable
     */
    public void addQuantifiedVariable(edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable vQuantifiedVariable)
        throws java.lang.IndexOutOfBoundsException
    {
        _quantifiedVariableList.add(vQuantifiedVariable);
    } //-- void addQuantifiedVariable(edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable) 

    /**
     * Method addQuantifiedVariable
     * 
     * @param index
     * @param vQuantifiedVariable
     */
    public void addQuantifiedVariable(int index, edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable vQuantifiedVariable)
        throws java.lang.IndexOutOfBoundsException
    {
        _quantifiedVariableList.add(index, vQuantifiedVariable);
    } //-- void addQuantifiedVariable(int, edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable) 

    /**
     * Method clearQuantifiedVariable
     */
    public void clearQuantifiedVariable()
    {
        _quantifiedVariableList.clear();
    } //-- void clearQuantifiedVariable() 

    /**
     * Method enumerateQuantifiedVariable
     */
    public java.util.Enumeration enumerateQuantifiedVariable()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_quantifiedVariableList.iterator());
    } //-- java.util.Enumeration enumerateQuantifiedVariable() 

    /**
     * Method getBindingReturns the value of field 'binding'.
     * 
     * @return the value of field 'binding'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType getBinding()
    {
        return this._binding;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType getBinding() 

    /**
     * Method getQuantifiedVariable
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable getQuantifiedVariable(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _quantifiedVariableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable) _quantifiedVariableList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable getQuantifiedVariable(int) 

    /**
     * Method getQuantifiedVariable
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable[] getQuantifiedVariable()
    {
        int size = _quantifiedVariableList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable) _quantifiedVariableList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable[] getQuantifiedVariable() 

    /**
     * Method getQuantifiedVariableCount
     */
    public int getQuantifiedVariableCount()
    {
        return _quantifiedVariableList.size();
    } //-- int getQuantifiedVariableCount() 

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
     * Method removeQuantifiedVariable
     * 
     * @param vQuantifiedVariable
     */
    public boolean removeQuantifiedVariable(edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable vQuantifiedVariable)
    {
        boolean removed = _quantifiedVariableList.remove(vQuantifiedVariable);
        return removed;
    } //-- boolean removeQuantifiedVariable(edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable) 

    /**
     * Method setBindingSets the value of field 'binding'.
     * 
     * @param binding the value of field 'binding'.
     */
    public void setBinding(edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType binding)
    {
        this._binding = binding;
    } //-- void setBinding(edu.ksu.cis.bandera.sessions.parser.v2.types.BindingType) 

    /**
     * Method setQuantifiedVariable
     * 
     * @param index
     * @param vQuantifiedVariable
     */
    public void setQuantifiedVariable(int index, edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable vQuantifiedVariable)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _quantifiedVariableList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _quantifiedVariableList.set(index, vQuantifiedVariable);
    } //-- void setQuantifiedVariable(int, edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable) 

    /**
     * Method setQuantifiedVariable
     * 
     * @param quantifiedVariableArray
     */
    public void setQuantifiedVariable(edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable[] quantifiedVariableArray)
    {
        //-- copy array
        _quantifiedVariableList.clear();
        for (int i = 0; i < quantifiedVariableArray.length; i++) {
            _quantifiedVariableList.add(quantifiedVariableArray[i]);
        }
    } //-- void setQuantifiedVariable(edu.ksu.cis.bandera.sessions.parser.v2.QuantifiedVariable) 

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
