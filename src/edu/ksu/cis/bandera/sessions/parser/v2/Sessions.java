/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Sessions.java,v 1.1 2003/04/30 19:33:38 tcw Exp $
 */

package edu.ksu.cis.bandera.sessions.parser.v2;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Sessions.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/04/30 19:33:38 $
 */
public class Sessions implements java.io.Serializable {


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
     * Field _authors
     */
    private edu.ksu.cis.bandera.sessions.parser.v2.Authors _authors;

    /**
     * Field _sessionList
     */
    private java.util.ArrayList _sessionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sessions() {
        super();
        _sessionList = new ArrayList();
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Sessions()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addSession
     * 
     * @param vSession
     */
    public void addSession(edu.ksu.cis.bandera.sessions.parser.v2.Session vSession)
        throws java.lang.IndexOutOfBoundsException
    {
        _sessionList.add(vSession);
    } //-- void addSession(edu.ksu.cis.bandera.sessions.parser.v2.Session) 

    /**
     * Method addSession
     * 
     * @param index
     * @param vSession
     */
    public void addSession(int index, edu.ksu.cis.bandera.sessions.parser.v2.Session vSession)
        throws java.lang.IndexOutOfBoundsException
    {
        _sessionList.add(index, vSession);
    } //-- void addSession(int, edu.ksu.cis.bandera.sessions.parser.v2.Session) 

    /**
     * Method clearSession
     */
    public void clearSession()
    {
        _sessionList.clear();
    } //-- void clearSession() 

    /**
     * Method enumerateSession
     */
    public java.util.Enumeration enumerateSession()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_sessionList.iterator());
    } //-- java.util.Enumeration enumerateSession() 

    /**
     * Method getAuthorsReturns the value of field 'authors'.
     * 
     * @return the value of field 'authors'.
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Authors getAuthors()
    {
        return this._authors;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Authors getAuthors() 

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
     * Method getSession
     * 
     * @param index
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Session getSession(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _sessionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (edu.ksu.cis.bandera.sessions.parser.v2.Session) _sessionList.get(index);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Session getSession(int) 

    /**
     * Method getSession
     */
    public edu.ksu.cis.bandera.sessions.parser.v2.Session[] getSession()
    {
        int size = _sessionList.size();
        edu.ksu.cis.bandera.sessions.parser.v2.Session[] mArray = new edu.ksu.cis.bandera.sessions.parser.v2.Session[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (edu.ksu.cis.bandera.sessions.parser.v2.Session) _sessionList.get(index);
        }
        return mArray;
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Session[] getSession() 

    /**
     * Method getSessionCount
     */
    public int getSessionCount()
    {
        return _sessionList.size();
    } //-- int getSessionCount() 

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
     * Method removeSession
     * 
     * @param vSession
     */
    public boolean removeSession(edu.ksu.cis.bandera.sessions.parser.v2.Session vSession)
    {
        boolean removed = _sessionList.remove(vSession);
        return removed;
    } //-- boolean removeSession(edu.ksu.cis.bandera.sessions.parser.v2.Session) 

    /**
     * Method setAuthorsSets the value of field 'authors'.
     * 
     * @param authors the value of field 'authors'.
     */
    public void setAuthors(edu.ksu.cis.bandera.sessions.parser.v2.Authors authors)
    {
        this._authors = authors;
    } //-- void setAuthors(edu.ksu.cis.bandera.sessions.parser.v2.Authors) 

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
     * Method setSession
     * 
     * @param index
     * @param vSession
     */
    public void setSession(int index, edu.ksu.cis.bandera.sessions.parser.v2.Session vSession)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _sessionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _sessionList.set(index, vSession);
    } //-- void setSession(int, edu.ksu.cis.bandera.sessions.parser.v2.Session) 

    /**
     * Method setSession
     * 
     * @param sessionArray
     */
    public void setSession(edu.ksu.cis.bandera.sessions.parser.v2.Session[] sessionArray)
    {
        //-- copy array
        _sessionList.clear();
        for (int i = 0; i < sessionArray.length; i++) {
            _sessionList.add(sessionArray[i]);
        }
    } //-- void setSession(edu.ksu.cis.bandera.sessions.parser.v2.Session) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static edu.ksu.cis.bandera.sessions.parser.v2.Sessions unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (edu.ksu.cis.bandera.sessions.parser.v2.Sessions) Unmarshaller.unmarshal(edu.ksu.cis.bandera.sessions.parser.v2.Sessions.class, reader);
    } //-- edu.ksu.cis.bandera.sessions.parser.v2.Sessions unmarshal(java.io.Reader) 

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
