
package com.ticketnetwork.tnwebservices.tnwebservice.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="SearchEventsResult" type="{http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0}ArrayOfEvent" minOccurs="0"/>
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
    "searchEventsResult"
})
@XmlRootElement(name = "SearchEventsResponse")
public class SearchEventsResponse {

    @XmlElement(name = "SearchEventsResult")
    protected ArrayOfEvent searchEventsResult;

    /**
     * Gets the value of the searchEventsResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEvent }
     *     
     */
    public ArrayOfEvent getSearchEventsResult() {
        return searchEventsResult;
    }

    /**
     * Sets the value of the searchEventsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEvent }
     *     
     */
    public void setSearchEventsResult(ArrayOfEvent value) {
        this.searchEventsResult = value;
    }

}
