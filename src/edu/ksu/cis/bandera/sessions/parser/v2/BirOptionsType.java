/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: BirOptionsType.java,v 1.1 2003/04/30 19:33:33 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * Class BirOptionsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:33 $
 */
public abstract class BirOptionsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _searchMode
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType _searchMode;

    /**
     * Field _resourceBounds
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.ResourceBounds _resourceBounds;


      //----------------/
     //- Constructors -/
    //----------------/

    public BirOptionsType() {
        super();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.BirOptionsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getResourceBoundsReturns the value of field
     * 'resourceBounds'.
     * 
     * @return the value of field 'resourceBounds'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.ResourceBounds getResourceBounds()
    {
        return this._resourceBounds;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.ResourceBounds getResourceBounds() 

    /**
     * Method getSearchModeReturns the value of field 'searchMode'.
     * 
     * @return the value of field 'searchMode'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType getSearchMode()
    {
        return this._searchMode;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType getSearchMode() 

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
     * Method setResourceBoundsSets the value of field
     * 'resourceBounds'.
     * 
     * @param resourceBounds the value of field 'resourceBounds'.
     */
    public void setResourceBounds(edu.ksu.cis.bandera.sessions.parser.v2.ResourceBounds resourceBounds)
    {
        this._resourceBounds = resourceBounds;
    } //-- void setResourceBounds(edu.ksu.cis.bandera.sessions.parser.v2.ResourceBounds) 

    /**
     * Method setSearchModeSets the value of field 'searchMode'.
     * 
     * @param searchMode the value of field 'searchMode'.
     */
    public void setSearchMode(edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType searchMode)
    {
        this._searchMode = searchMode;
    } //-- void setSearchMode(edu.ksu.cis.bandera.sessions.parser.v2.types.SearchModeType) 

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
