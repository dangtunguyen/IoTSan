/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CounterExampleType.java,v 1.1 2003/04/30 19:33:34 tcw Exp $
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
 * Class CounterExampleType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:34 $
 */
public abstract class CounterExampleType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _watches
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Watches _watches;

    /**
     * Field _breakPoints
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.BreakPoints _breakPoints;

    /**
     * Field _lockGraphs
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.LockGraphs _lockGraphs;

    /**
     * Field _objectDiagrams
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagrams _objectDiagrams;


      //----------------/
     //- Constructors -/
    //----------------/

    public CounterExampleType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.CounterExampleType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getBreakPointsReturns the value of field
     * 'breakPoints'.
     * 
     * @return the value of field 'breakPoints'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.BreakPoints getBreakPoints()
    {
        return this._breakPoints;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.BreakPoints getBreakPoints() 

    /**
     * Method getLockGraphsReturns the value of field 'lockGraphs'.
     * 
     * @return the value of field 'lockGraphs'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.LockGraphs getLockGraphs()
    {
        return this._lockGraphs;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.LockGraphs getLockGraphs() 

    /**
     * Method getObjectDiagramsReturns the value of field
     * 'objectDiagrams'.
     * 
     * @return the value of field 'objectDiagrams'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagrams getObjectDiagrams()
    {
        return this._objectDiagrams;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagrams getObjectDiagrams() 

    /**
     * Method getWatchesReturns the value of field 'watches'.
     * 
     * @return the value of field 'watches'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Watches getWatches()
    {
        return this._watches;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Watches getWatches() 

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
     * Method setBreakPointsSets the value of field 'breakPoints'.
     * 
     * @param breakPoints the value of field 'breakPoints'.
     */
    public void setBreakPoints(edu.ksu.cis.bandera.sessions.parser.v2.BreakPoints breakPoints)
    {
        this._breakPoints = breakPoints;
    } //-- void setBreakPoints(edu.ksu.cis.bandera.sessions.parser.v2.BreakPoints) 

    /**
     * Method setLockGraphsSets the value of field 'lockGraphs'.
     * 
     * @param lockGraphs the value of field 'lockGraphs'.
     */
    public void setLockGraphs(edu.ksu.cis.bandera.sessions.parser.v2.LockGraphs lockGraphs)
    {
        this._lockGraphs = lockGraphs;
    } //-- void setLockGraphs(edu.ksu.cis.bandera.sessions.parser.v2.LockGraphs) 

    /**
     * Method setObjectDiagramsSets the value of field
     * 'objectDiagrams'.
     * 
     * @param objectDiagrams the value of field 'objectDiagrams'.
     */
    public void setObjectDiagrams(edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagrams objectDiagrams)
    {
        this._objectDiagrams = objectDiagrams;
    } //-- void setObjectDiagrams(edu.ksu.cis.bandera.sessions.parser.v2.ObjectDiagrams) 

    /**
     * Method setWatchesSets the value of field 'watches'.
     * 
     * @param watches the value of field 'watches'.
     */
    public void setWatches(edu.ksu.cis.bandera.sessions.parser.v2.Watches watches)
    {
        this._watches = watches;
    } //-- void setWatches(edu.ksu.cis.bandera.sessions.parser.v2.Watches) 

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
