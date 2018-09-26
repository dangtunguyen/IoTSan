/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: PatternType.java,v 1.1 2003/04/30 19:33:37 tcw Exp $
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
 * Class PatternType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:37 $
 */
public abstract class PatternType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _scope
     */
    private java.lang.String _scope;

    /**
     * Field _name
     */
    private java.lang.String _name;


      //----------------/
     //- Constructors -/
    //----------------/

    public PatternType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.PatternType()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Method getScopeReturns the value of field 'scope'.
     * 
     * @return the value of field 'scope'.
     */
    public java.lang.String getScope()
    {
        return this._scope;
    } //-- java.lang.String getScope() 

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
     * Method setNameSets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method setScopeSets the value of field 'scope'.
     * 
     * @param scope the value of field 'scope'.
     */
    public void setScope(java.lang.String scope)
    {
        this._scope = scope;
    } //-- void setScope(java.lang.String) 

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
