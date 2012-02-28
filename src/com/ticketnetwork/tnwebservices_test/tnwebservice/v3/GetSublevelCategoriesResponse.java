
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
 *         &lt;element name="GetSublevelCategoriesResult" type="{http://tnwebservices-test.ticketnetwork.com/tnwebservice/v3.0}ArrayOfCategory" minOccurs="0"/>
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
    "getSublevelCategoriesResult"
})
@XmlRootElement(name = "GetSublevelCategoriesResponse")
public class GetSublevelCategoriesResponse {

    @XmlElement(name = "GetSublevelCategoriesResult")
    protected ArrayOfCategory getSublevelCategoriesResult;

    /**
     * Gets the value of the getSublevelCategoriesResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfCategory }
     *     
     */
    public ArrayOfCategory getGetSublevelCategoriesResult() {
        return getSublevelCategoriesResult;
    }

    /**
     * Sets the value of the getSublevelCategoriesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfCategory }
     *     
     */
    public void setGetSublevelCategoriesResult(ArrayOfCategory value) {
        this.getSublevelCategoriesResult = value;
    }

}
