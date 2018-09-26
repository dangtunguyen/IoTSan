/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SpecificationOptionType.java,v 1.1 2003/04/30 19:33:39 tcw Exp $
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
 * Class SpecificationOptionType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:39 $
 */
public abstract class SpecificationOptionType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _temporal
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Temporal _temporal;

    /**
     * Field _assertions
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Assertions _assertions;


      //----------------/
     //- Constructors -/
    //----------------/

    public SpecificationOptionType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOptionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAssertionsReturns the value of field 'assertions'.
     * 
     * @return the value of field 'assertions'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Assertions getAssertions()
    {
        return this._assertions;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Assertions getAssertions() 

    /**
     * Method getTemporalReturns the value of field 'temporal'.
     * 
     * @return the value of field 'temporal'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Temporal getTemporal()
    {
        return this._temporal;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Temporal getTemporal() 

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
     * Method setAssertionsSets the value of field 'assertions'.
     * 
     * @param assertions the value of field 'assertions'.
     */
    public void setAssertions(edu.ksu.cis.bandera.sessions.parser.v2.Assertions assertions)
    {
        this._assertions = assertions;
    } //-- void setAssertions(edu.ksu.cis.bandera.sessions.parser.v2.Assertions) 

    /**
     * Method setTemporalSets the value of field 'temporal'.
     * 
     * @param temporal the value of field 'temporal'.
     */
    public void setTemporal(edu.ksu.cis.bandera.sessions.parser.v2.Temporal temporal)
    {
        this._temporal = temporal;
    } //-- void setTemporal(edu.ksu.cis.bandera.sessions.parser.v2.Temporal) 

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
