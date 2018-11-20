/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VarType.java,v 1.1 2003/04/30 19:33:40 tcw Exp $
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
 * Class VarType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:40 $
 */
public abstract class VarType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _abstraction
     */
    private java.lang.String _abstraction;


      //----------------/
     //- Constructors -/
    //----------------/

    public VarType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.VarType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAbstractionReturns the value of field
     * 'abstraction'.
     * 
     * @return the value of field 'abstraction'.
     */
    public java.lang.String getAbstraction()
    {
        return this._abstraction;
    } //-- java.lang.String getAbstraction() 

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
     * Method setAbstractionSets the value of field 'abstraction'.
     * 
     * @param abstraction the value of field 'abstraction'.
     */
    public void setAbstraction(java.lang.String abstraction)
    {
        this._abstraction = abstraction;
    } //-- void setAbstraction(java.lang.String) 

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
