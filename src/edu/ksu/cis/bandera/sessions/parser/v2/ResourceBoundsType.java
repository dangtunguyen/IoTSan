/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ResourceBoundsType.java,v 1.1 2003/04/30 19:33:37 tcw Exp $
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
 * Class ResourceBoundsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:37 $
 */
public abstract class ResourceBoundsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _integerBounds
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds _integerBounds;

    /**
     * Field _instanceBounds
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds _instanceBounds;

    /**
     * Field _arrayBounds
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.ArrayBounds _arrayBounds;

    /**
     * Field _threadInstanceBounds
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds _threadInstanceBounds;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResourceBoundsType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ResourceBoundsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getArrayBoundsReturns the value of field
     * 'arrayBounds'.
     * 
     * @return the value of field 'arrayBounds'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.ArrayBounds getArrayBounds()
    {
        return this._arrayBounds;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ArrayBounds getArrayBounds() 

    /**
     * Method getInstanceBoundsReturns the value of field
     * 'instanceBounds'.
     * 
     * @return the value of field 'instanceBounds'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds getInstanceBounds()
    {
        return this._instanceBounds;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds getInstanceBounds() 

    /**
     * Method getIntegerBoundsReturns the value of field
     * 'integerBounds'.
     * 
     * @return the value of field 'integerBounds'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds getIntegerBounds()
    {
        return this._integerBounds;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds getIntegerBounds() 

    /**
     * Method getThreadInstanceBoundsReturns the value of field
     * 'threadInstanceBounds'.
     * 
     * @return the value of field 'threadInstanceBounds'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds getThreadInstanceBounds()
    {
        return this._threadInstanceBounds;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds getThreadInstanceBounds() 

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
     * Method setArrayBoundsSets the value of field 'arrayBounds'.
     * 
     * @param arrayBounds the value of field 'arrayBounds'.
     */
    public void setArrayBounds(edu.ksu.cis.bandera.sessions.parser.v2.ArrayBounds arrayBounds)
    {
        this._arrayBounds = arrayBounds;
    } //-- void setArrayBounds(edu.ksu.cis.bandera.sessions.parser.v2.ArrayBounds) 

    /**
     * Method setInstanceBoundsSets the value of field
     * 'instanceBounds'.
     * 
     * @param instanceBounds the value of field 'instanceBounds'.
     */
    public void setInstanceBounds(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds instanceBounds)
    {
        this._instanceBounds = instanceBounds;
    } //-- void setInstanceBounds(edu.ksu.cis.bandera.sessions.parser.v2.InstanceBounds) 

    /**
     * Method setIntegerBoundsSets the value of field
     * 'integerBounds'.
     * 
     * @param integerBounds the value of field 'integerBounds'.
     */
    public void setIntegerBounds(edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds integerBounds)
    {
        this._integerBounds = integerBounds;
    } //-- void setIntegerBounds(edu.ksu.cis.bandera.sessions.parser.v2.IntegerBounds) 

    /**
     * Method setThreadInstanceBoundsSets the value of field
     * 'threadInstanceBounds'.
     * 
     * @param threadInstanceBounds the value of field
     * 'threadInstanceBounds'.
     */
    public void setThreadInstanceBounds(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds threadInstanceBounds)
    {
        this._threadInstanceBounds = threadInstanceBounds;
    } //-- void setThreadInstanceBounds(edu.ksu.cis.bandera.sessions.parser.v2.ThreadInstanceBounds) 

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
