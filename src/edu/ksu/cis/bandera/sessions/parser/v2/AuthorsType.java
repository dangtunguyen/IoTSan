/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AuthorsType.java,v 1.1 2003/04/30 19:33:33 tcw Exp $
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
 * Class AuthorsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:33 $
 */
public abstract class AuthorsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _author
     */
    private java.lang.String _author;


      //----------------/
     //- Constructors -/
    //----------------/

    public AuthorsType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.AuthorsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAuthorReturns the value of field 'author'.
     * 
     * @return the value of field 'author'.
     */
    public java.lang.String getAuthor()
    {
        return this._author;
    } //-- java.lang.String getAuthor() 

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
     * Method setAuthorSets the value of field 'author'.
     * 
     * @param author the value of field 'author'.
     */
    public void setAuthor(java.lang.String author)
    {
        this._author = author;
    } //-- void setAuthor(java.lang.String) 

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
