
package com.ticketnetwork.tnwebservices.tnwebservice.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="websiteConfigID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numberOfRecords" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="eventID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lowPrice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="highPrice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ticketGroupID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestedTixSplit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="whereClause" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderByClause" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "websiteConfigID",
    "numberOfRecords",
    "eventID",
    "lowPrice",
    "highPrice",
    "ticketGroupID",
    "requestedTixSplit",
    "whereClause",
    "orderByClause"
})
@XmlRootElement(name = "GetTickets")
public class GetTickets {

    protected String websiteConfigID;
    protected String numberOfRecords;
    protected String eventID;
    protected String lowPrice;
    protected String highPrice;
    protected String ticketGroupID;
    protected String requestedTixSplit;
    protected String whereClause;
    protected String orderByClause;

    /**
     * Gets the value of the websiteConfigID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWebsiteConfigID() {
        return websiteConfigID;
    }

    /**
     * Sets the value of the websiteConfigID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWebsiteConfigID(String value) {
        this.websiteConfigID = value;
    }

    /**
     * Gets the value of the numberOfRecords property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfRecords() {
        return numberOfRecords;
    }

    /**
     * Sets the value of the numberOfRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfRecords(String value) {
        this.numberOfRecords = value;
    }

    /**
     * Gets the value of the eventID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Sets the value of the eventID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventID(String value) {
        this.eventID = value;
    }

    /**
     * Gets the value of the lowPrice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLowPrice() {
        return lowPrice;
    }

    /**
     * Sets the value of the lowPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLowPrice(String value) {
        this.lowPrice = value;
    }

    /**
     * Gets the value of the highPrice property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHighPrice() {
        return highPrice;
    }

    /**
     * Sets the value of the highPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHighPrice(String value) {
        this.highPrice = value;
    }

    /**
     * Gets the value of the ticketGroupID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTicketGroupID() {
        return ticketGroupID;
    }

    /**
     * Sets the value of the ticketGroupID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTicketGroupID(String value) {
        this.ticketGroupID = value;
    }

    /**
     * Gets the value of the requestedTixSplit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestedTixSplit() {
        return requestedTixSplit;
    }

    /**
     * Sets the value of the requestedTixSplit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestedTixSplit(String value) {
        this.requestedTixSplit = value;
    }

    /**
     * Gets the value of the whereClause property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWhereClause() {
        return whereClause;
    }

    /**
     * Sets the value of the whereClause property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWhereClause(String value) {
        this.whereClause = value;
    }

    /**
     * Gets the value of the orderByClause property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * Sets the value of the orderByClause property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderByClause(String value) {
        this.orderByClause = value;
    }

}
