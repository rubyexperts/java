
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
 *         &lt;element name="GetCategoriesResult" type="{http://tnwebservices-test.ticketnetwork.com/tnwebservice/v3.0}ArrayOfCategory" minOccurs="0"/>
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
    "getCategoriesResult"
})
@XmlRootElement(name = "GetCategoriesResponse")
public class GetCategoriesResponse {

    @XmlElement(name = "GetCategoriesResult")
    protected ArrayOfCategory getCategoriesResult;

    /**
     * Gets the value of the getCategoriesResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCategory }
     *     
     */
    public ArrayOfCategory getGetCategoriesResult() {
        return getCategoriesResult;
    }

    /**
     * Sets the value of the getCategoriesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCategory }
     *     
     */
    public void setGetCategoriesResult(ArrayOfCategory value) {
        this.getCategoriesResult = value;
    }

}
