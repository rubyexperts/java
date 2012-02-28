
package com.ticketnetwork.tnwebservices_test.tnwebservice.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Event complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Event">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ChildCategoryID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Clicks" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="DisplayDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GrandchildCategoryID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsWomensEvent" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="MapURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ParentCategoryID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="StateProvince" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StateProvinceID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Venue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VenueConfigurationID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="VenueID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Event", propOrder = {
    "childCategoryID",
    "city",
    "clicks",
    "date",
    "displayDate",
    "grandchildCategoryID",
    "id",
    "isWomensEvent",
    "mapURL",
    "name",
    "parentCategoryID",
    "stateProvince",
    "stateProvinceID",
    "venue",
    "venueConfigurationID",
    "venueID"
})
public class Event {

    @XmlElement(name = "ChildCategoryID")
    protected int childCategoryID;
    @XmlElement(name = "City")
    protected String city;
    @XmlElement(name = "Clicks")
    protected int clicks;
    @XmlElement(name = "Date", required = true)
    protected XMLGregorianCalendar date;
    @XmlElement(name = "DisplayDate")
    protected String displayDate;
    @XmlElement(name = "GrandchildCategoryID")
    protected int grandchildCategoryID;
    @XmlElement(name = "ID")
    protected int id;
    @XmlElement(name = "IsWomensEvent")
    protected boolean isWomensEvent;
    @XmlElement(name = "MapURL")
    protected String mapURL;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "ParentCategoryID")
    protected int parentCategoryID;
    @XmlElement(name = "StateProvince")
    protected String stateProvince;
    @XmlElement(name = "StateProvinceID")
    protected int stateProvinceID;
    @XmlElement(name = "Venue")
    protected String venue;
    @XmlElement(name = "VenueConfigurationID", required = true, type = Integer.class, nillable = true)
    protected Integer venueConfigurationID;
    @XmlElement(name = "VenueID")
    protected int venueID;

    /**
     * Gets the value of the childCategoryID property.
     * 
     */
    public int getChildCategoryID() {
        return childCategoryID;
    }

    /**
     * Sets the value of the childCategoryID property.
     * 
     */
    public void setChildCategoryID(int value) {
        this.childCategoryID = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the clicks property.
     * 
     */
    public int getClicks() {
        return clicks;
    }

    /**
     * Sets the value of the clicks property.
     * 
     */
    public void setClicks(int value) {
        this.clicks = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the displayDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayDate() {
        return displayDate;
    }

    /**
     * Sets the value of the displayDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayDate(String value) {
        this.displayDate = value;
    }

    /**
     * Gets the value of the grandchildCategoryID property.
     * 
     */
    public int getGrandchildCategoryID() {
        return grandchildCategoryID;
    }

    /**
     * Sets the value of the grandchildCategoryID property.
     * 
     */
    public void setGrandchildCategoryID(int value) {
        this.grandchildCategoryID = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setID(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the isWomensEvent property.
     * 
     */
    public boolean isIsWomensEvent() {
        return isWomensEvent;
    }

    /**
     * Sets the value of the isWomensEvent property.
     * 
     */
    public void setIsWomensEvent(boolean value) {
        this.isWomensEvent = value;
    }

    /**
     * Gets the value of the mapURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapURL() {
        return mapURL;
    }

    /**
     * Sets the value of the mapURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapURL(String value) {
        this.mapURL = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the parentCategoryID property.
     * 
     */
    public int getParentCategoryID() {
        return parentCategoryID;
    }

    /**
     * Sets the value of the parentCategoryID property.
     * 
     */
    public void setParentCategoryID(int value) {
        this.parentCategoryID = value;
    }

    /**
     * Gets the value of the stateProvince property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateProvince() {
        return stateProvince;
    }

    /**
     * Sets the value of the stateProvince property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateProvince(String value) {
        this.stateProvince = value;
    }

    /**
     * Gets the value of the stateProvinceID property.
     * 
     */
    public int getStateProvinceID() {
        return stateProvinceID;
    }

    /**
     * Sets the value of the stateProvinceID property.
     * 
     */
    public void setStateProvinceID(int value) {
        this.stateProvinceID = value;
    }

    /**
     * Gets the value of the venue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVenue() {
        return venue;
    }

    /**
     * Sets the value of the venue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVenue(String value) {
        this.venue = value;
    }

    /**
     * Gets the value of the venueConfigurationID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVenueConfigurationID() {
        return venueConfigurationID;
    }

    /**
     * Sets the value of the venueConfigurationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVenueConfigurationID(Integer value) {
        this.venueConfigurationID = value;
    }

    /**
     * Gets the value of the venueID property.
     * 
     */
    public int getVenueID() {
        return venueID;
    }

    /**
     * Sets the value of the venueID property.
     * 
     */
    public void setVenueID(int value) {
        this.venueID = value;
    }

}
