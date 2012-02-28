
package com.ticketnetwork.tnwebservices_test.tnwebservice.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EventPerformer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventPerformer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PerformerID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PerformerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventPerformer", propOrder = {
    "eventID",
    "performerID",
    "performerName"
})
public class EventPerformer {

    @XmlElement(name = "EventID")
    protected int eventID;
    @XmlElement(name = "PerformerID")
    protected int performerID;
    @XmlElement(name = "PerformerName")
    protected String performerName;

    /**
     * Gets the value of the eventID property.
     * 
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Sets the value of the eventID property.
     * 
     */
    public void setEventID(int value) {
        this.eventID = value;
    }

    /**
     * Gets the value of the performerID property.
     * 
     */
    public int getPerformerID() {
        return performerID;
    }

    /**
     * Sets the value of the performerID property.
     * 
     */
    public void setPerformerID(int value) {
        this.performerID = value;
    }

    /**
     * Gets the value of the performerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPerformerName() {
        return performerName;
    }

    /**
     * Sets the value of the performerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPerformerName(String value) {
        this.performerName = value;
    }

}
