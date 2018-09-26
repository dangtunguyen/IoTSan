/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: FieldIntegerBoundType.java,v 1.1 2003/04/30 19:33:35 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class FieldIntegerBoundType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:35 $
 */
public abstract class FieldIntegerBoundType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _className
     */
    private java.lang.String _className;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _min
     */
    private int _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * Field _max
     */
    private int _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;


      //----------------/
     //- Constructors -/
    //----------------/

    public FieldIntegerBoundType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.FieldIntegerBoundType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteMax
     */
    public void deleteMax()
    {
        this._has_max= false;
    } //-- void deleteMax() 

    /**
     * Method deleteMin
     */
    public void deleteMin()
    {
        this._has_min= false;
    } //-- void deleteMin() 

    /**
     * Method getClassNameReturns the value of field 'className'.
     * 
     * @return the value of field 'className'.
     */
    public java.lang.String getClassName()
    {
        return this._className;
    } //-- java.lang.String getClassName() 

    /**
     * Method getMaxReturns the value of field 'max'.
     * 
     * @return the value of field 'max'.
     */
    public int getMax()
    {
        return this._max;
    } //-- int getMax() 

    /**
     * Method getMinReturns the value of field 'min'.
     * 
     * @return the value of field 'min'.
     */
    public int getMin()
    {
        return this._min;
    } //-- int getMin() 

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
     * Method hasMax
     */
    public boolean hasMax()
    {
        return this._has_max;
    } //-- boolean hasMax() 

    /**
     * Method hasMin
     */
    public boolean hasMin()
    {
        return this._has_min;
    } //-- boolean hasMin() 

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
     * Method setClassNameSets the value of field 'className'.
     * 
     * @param className the value of field 'className'.
     */
    public void setClassName(java.lang.String className)
    {
        this._className = className;
    } //-- void setClassName(java.lang.String) 

    /**
     * Method setMaxSets the value of field 'max'.
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(int max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(int) 

    /**
     * Method setMinSets the value of field 'min'.
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(int min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(int) 

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
