
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
 *         &lt;element name="GetEventPerformersResult" type="{http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0}ArrayOfEventPerformer" minOccurs="0"/>
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
    "getEventPerformersResult"
})
@XmlRootElement(name = "GetEventPerformersResponse")
public class GetEventPerformersResponse {

    @XmlElement(name = "GetEventPerformersResult")
    protected ArrayOfEventPerformer getEventPerformersResult;

    /**
     * Gets the value of the getEventPerformersResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEventPerformer }
     *     
     */
    public ArrayOfEventPerformer getGetEventPerformersResult() {
        return getEventPerformersResult;
    }

    /**
     * Sets the value of the getEventPerformersResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEventPerformer }
     *     
     */
    public void setGetEventPerformersResult(ArrayOfEventPerformer value) {
        this.getEventPerformersResult = value;
    }

}
