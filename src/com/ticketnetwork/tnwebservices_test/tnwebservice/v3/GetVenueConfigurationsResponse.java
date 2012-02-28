
package com.ticketnetwork.tnwebservices_test.tnwebservice.v3;

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
 *         &lt;element name="GetVenueConfigurationsResult" type="{http://tnwebservices-test.ticketnetwork.com/tnwebservice/v3.0}ArrayOfVenueConfiguration" minOccurs="0"/>
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
    "getVenueConfigurationsResult"
})
@XmlRootElement(name = "GetVenueConfigurationsResponse")
public class GetVenueConfigurationsResponse {

    @XmlElement(name = "GetVenueConfigurationsResult")
    protected ArrayOfVenueConfiguration getVenueConfigurationsResult;

    /**
     * Gets the value of the getVenueConfigurationsResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfVenueConfiguration }
     *     
     */
    public ArrayOfVenueConfiguration getGetVenueConfigurationsResult() {
        return getVenueConfigurationsResult;
    }

    /**
     * Sets the value of the getVenueConfigurationsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfVenueConfiguration }
     *     
     */
    public void setGetVenueConfigurationsResult(ArrayOfVenueConfiguration value) {
        this.getVenueConfigurationsResult = value;
    }

}
