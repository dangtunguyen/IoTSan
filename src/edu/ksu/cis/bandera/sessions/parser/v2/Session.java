/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Session.java,v 1.1 2003/04/30 19:33:38 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Session.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:38 $
 */
public class Session implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _application
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Application _application;

    /**
     * Field _output
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Output _output;

    /**
     * Field _specificationOption
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOption _specificationOption;

    /**
     * Field _components
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Components _components;


      //----------------/
     //- Constructors -/
    //----------------/

    public Session() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Session()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getApplicationReturns the value of field
     * 'application'.
     * 
     * @return the value of field 'application'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Application getApplication()
    {
        return this._application;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Application getApplication() 

    /**
     * Method getComponentsReturns the value of field 'components'.
     * 
     * @return the value of field 'components'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Components getComponents()
    {
        return this._components;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Components getComponents() 

    /**
     * Method getDescriptionReturns the value of field
     * 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Method getIdReturns the value of field 'id'.
     * 
     * @return the value of field 'id'.
     */
    public java.lang.String getId()
    {
        return this._id;
    } //-- java.lang.String getId() 

    /**
     * Method getOutputReturns the value of field 'output'.
     * 
     * @return the value of field 'output'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Output getOutput()
    {
        return this._output;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Output getOutput() 

    /**
     * Method getSpecificationOptionReturns the value of field
     * 'specificationOption'.
     * 
     * @return the value of field 'specificationOption'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOption getSpecificationOption()
    {
        return this._specificationOption;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOption getSpecificationOption() 

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
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method setApplicationSets the value of field 'application'.
     * 
     * @param application the value of field 'application'.
     */
    public void setApplication(edu.ksu.cis.bandera.sessions.parser.v2.Application application)
    {
        this._application = application;
    } //-- void setApplication(edu.ksu.cis.bandera.sessions.parser.v2.Application) 

    /**
     * Method setComponentsSets the value of field 'components'.
     * 
     * @param components the value of field 'components'.
     */
    public void setComponents(edu.ksu.cis.bandera.sessions.parser.v2.Components components)
    {
        this._components = components;
    } //-- void setComponents(edu.ksu.cis.bandera.sessions.parser.v2.Components) 

    /**
     * Method setDescriptionSets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Method setIdSets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id)
    {
        this._id = id;
    } //-- void setId(java.lang.String) 

    /**
     * Method setOutputSets the value of field 'output'.
     * 
     * @param output the value of field 'output'.
     */
    public void setOutput(edu.ksu.cis.bandera.sessions.parser.v2.Output output)
    {
        this._output = output;
    } //-- void setOutput(edu.ksu.cis.bandera.sessions.parser.v2.Output) 

    /**
     * Method setSpecificationOptionSets the value of field
     * 'specificationOption'.
     * 
     * @param specificationOption the value of field
     * 'specificationOption'.
     */
    public void setSpecificationOption(edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOption specificationOption)
    {
        this._specificationOption = specificationOption;
    } //-- void setSpecificationOption(edu.ksu.cis.bandera.sessions.parser.v2.SpecificationOption) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.Session unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.Session) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.Session.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Session unmarshal(java.io.Reader) 

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
