/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ComponentType.java,v 1.1 2003/04/30 19:33:34 tcw Exp $
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
 * Class ComponentType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:34 $
 */
public abstract class ComponentType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _slicer
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Slicer _slicer;

    /**
     * Field _slabs
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Slabs _slabs;

    /**
     * Field _checker
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Checker _checker;

    /**
     * Field _counterExample
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.CounterExample _counterExample;


      //----------------/
     //- Constructors -/
    //----------------/

    public ComponentType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ComponentType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getCheckerReturns the value of field 'checker'.
     * 
     * @return the value of field 'checker'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Checker getChecker()
    {
        return this._checker;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Checker getChecker() 

    /**
     * Method getCounterExampleReturns the value of field
     * 'counterExample'.
     * 
     * @return the value of field 'counterExample'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.CounterExample getCounterExample()
    {
        return this._counterExample;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.CounterExample getCounterExample() 

    /**
     * Method getSlabsReturns the value of field 'slabs'.
     * 
     * @return the value of field 'slabs'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Slabs getSlabs()
    {
        return this._slabs;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Slabs getSlabs() 

    /**
     * Method getSlicerReturns the value of field 'slicer'.
     * 
     * @return the value of field 'slicer'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Slicer getSlicer()
    {
        return this._slicer;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Slicer getSlicer() 

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
     * Method setCheckerSets the value of field 'checker'.
     * 
     * @param checker the value of field 'checker'.
     */
    public void setChecker(edu.ksu.cis.bandera.sessions.parser.v2.Checker checker)
    {
        this._checker = checker;
    } //-- void setChecker(edu.ksu.cis.bandera.sessions.parser.v2.Checker) 

    /**
     * Method setCounterExampleSets the value of field
     * 'counterExample'.
     * 
     * @param counterExample the value of field 'counterExample'.
     */
    public void setCounterExample(edu.ksu.cis.bandera.sessions.parser.v2.CounterExample counterExample)
    {
        this._counterExample = counterExample;
    } //-- void setCounterExample(edu.ksu.cis.bandera.sessions.parser.v2.CounterExample) 

    /**
     * Method setSlabsSets the value of field 'slabs'.
     * 
     * @param slabs the value of field 'slabs'.
     */
    public void setSlabs(edu.ksu.cis.bandera.sessions.parser.v2.Slabs slabs)
    {
        this._slabs = slabs;
    } //-- void setSlabs(edu.ksu.cis.bandera.sessions.parser.v2.Slabs) 

    /**
     * Method setSlicerSets the value of field 'slicer'.
     * 
     * @param slicer the value of field 'slicer'.
     */
    public void setSlicer(edu.ksu.cis.bandera.sessions.parser.v2.Slicer slicer)
    {
        this._slicer = slicer;
    } //-- void setSlicer(edu.ksu.cis.bandera.sessions.parser.v2.Slicer) 

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
