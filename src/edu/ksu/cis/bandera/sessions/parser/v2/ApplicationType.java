/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationType.java,v 1.1 2003/04/30 19:33:32 tcw Exp $
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
 * Class ApplicationType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:32 $
 */
public abstract class ApplicationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _main
     */
    private java.lang.String _main;

    /**
     * Field _classpath
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Classpath _classpath;

    /**
     * Field _includes
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Includes _includes;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ApplicationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getClasspathReturns the value of field 'classpath'.
     * 
     * @return the value of field 'classpath'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Classpath getClasspath()
    {
        return this._classpath;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Classpath getClasspath() 

    /**
     * Method getIncludesReturns the value of field 'includes'.
     * 
     * @return the value of field 'includes'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Includes getIncludes()
    {
        return this._includes;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Includes getIncludes() 

    /**
     * Method getMainReturns the value of field 'main'.
     * 
     * @return the value of field 'main'.
     */
    public java.lang.String getMain()
    {
        return this._main;
    } //-- java.lang.String getMain() 

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
     * Method setClasspathSets the value of field 'classpath'.
     * 
     * @param classpath the value of field 'classpath'.
     */
    public void setClasspath(edu.ksu.cis.bandera.sessions.parser.v2.Classpath classpath)
    {
        this._classpath = classpath;
    } //-- void setClasspath(edu.ksu.cis.bandera.sessions.parser.v2.Classpath) 

    /**
     * Method setIncludesSets the value of field 'includes'.
     * 
     * @param includes the value of field 'includes'.
     */
    public void setIncludes(edu.ksu.cis.bandera.sessions.parser.v2.Includes includes)
    {
        this._includes = includes;
    } //-- void setIncludes(edu.ksu.cis.bandera.sessions.parser.v2.Includes) 

    /**
     * Method setMainSets the value of field 'main'.
     * 
     * @param main the value of field 'main'.
     */
    public void setMain(java.lang.String main)
    {
        this._main = main;
    } //-- void setMain(java.lang.String) 

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
