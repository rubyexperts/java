
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
 *         &lt;element name="searchTerms" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "searchTerms",
    "whereClause",
    "orderByClause"
})
@XmlRootElement(name = "SearchEvents")
public class SearchEvents {

    protected String websiteConfigID;
    protected String searchTerms;
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
     * Gets the value of the searchTerms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSearchTerms() {
        return searchTerms;
    }

    /**
     * Sets the value of the searchTerms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSearchTerms(String value) {
        this.searchTerms = value;
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
