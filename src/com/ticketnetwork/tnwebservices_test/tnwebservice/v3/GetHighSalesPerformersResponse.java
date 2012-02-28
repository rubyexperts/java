
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
 *         &lt;element name="GetHighSalesPerformersResult" type="{http://tnwebservices-test.ticketnetwork.com/tnwebservice/v3.0}ArrayOfPerformerPercent" minOccurs="0"/>
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
    "getHighSalesPerformersResult"
})
@XmlRootElement(name = "GetHighSalesPerformersResponse")
public class GetHighSalesPerformersResponse {

    @XmlElement(name = "GetHighSalesPerformersResult")
    protected ArrayOfPerformerPercent getHighSalesPerformersResult;

    /**
     * Gets the value of the getHighSalesPerformersResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPerformerPercent }
     *     
     */
    public ArrayOfPerformerPercent getGetHighSalesPerformersResult() {
        return getHighSalesPerformersResult;
    }

    /**
     * Sets the value of the getHighSalesPerformersResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPerformerPercent }
     *     
     */
    public void setGetHighSalesPerformersResult(ArrayOfPerformerPercent value) {
        this.getHighSalesPerformersResult = value;
    }

}
