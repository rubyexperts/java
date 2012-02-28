
package com.ticketnetwork.tnwebservices_test.tnwebservice.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VenueConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VenueConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Capacity" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MapSite" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MapURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TypeDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TypeID" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
@XmlType(name = "VenueConfiguration", propOrder = {
    "capacity",
    "id",
    "mapSite",
    "mapURL",
    "typeDescription",
    "typeID",
    "venueID"
})
public class VenueConfiguration {

    @XmlElement(name = "Capacity")
    protected int capacity;
    @XmlElement(name = "ID")
    protected int id;
    @XmlElement(name = "MapSite")
    protected String mapSite;
    @XmlElement(name = "MapURL")
    protected String mapURL;
    @XmlElement(name = "TypeDescription")
    protected String typeDescription;
    @XmlElement(name = "TypeID")
    protected int typeID;
    @XmlElement(name = "VenueID")
    protected int venueID;

    /**
     * Gets the value of the capacity property.
     * 
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the value of the capacity property.
     * 
     */
    public void setCapacity(int value) {
        this.capacity = value;
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
     * Gets the value of the mapSite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMapSite() {
        return mapSite;
    }

    /**
     * Sets the value of the mapSite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMapSite(String value) {
        this.mapSite = value;
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
     * Gets the value of the typeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeDescription() {
        return typeDescription;
    }

    /**
     * Sets the value of the typeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeDescription(String value) {
        this.typeDescription = value;
    }

    /**
     * Gets the value of the typeID property.
     * 
     */
    public int getTypeID() {
        return typeID;
    }

    /**
     * Sets the value of the typeID property.
     * 
     */
    public void setTypeID(int value) {
        this.typeID = value;
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
