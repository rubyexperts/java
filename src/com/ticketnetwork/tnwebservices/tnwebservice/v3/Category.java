
package com.ticketnetwork.tnwebservices.tnwebservice.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Category complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Category">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ChildCategoryDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ChildCategoryID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="GrandchildCategoryDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GrandchildCategoryID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ParentCategoryDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ParentCategoryID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Category", propOrder = {
    "childCategoryDescription",
    "childCategoryID",
    "grandchildCategoryDescription",
    "grandchildCategoryID",
    "parentCategoryDescription",
    "parentCategoryID"
})
public class Category {

    @XmlElement(name = "ChildCategoryDescription")
    protected String childCategoryDescription;
    @XmlElement(name = "ChildCategoryID")
    protected int childCategoryID;
    @XmlElement(name = "GrandchildCategoryDescription")
    protected String grandchildCategoryDescription;
    @XmlElement(name = "GrandchildCategoryID")
    protected int grandchildCategoryID;
    @XmlElement(name = "ParentCategoryDescription")
    protected String parentCategoryDescription;
    @XmlElement(name = "ParentCategoryID")
    protected int parentCategoryID;

    /**
     * Gets the value of the childCategoryDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChildCategoryDescription() {
        return childCategoryDescription;
    }

    /**
     * Sets the value of the childCategoryDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChildCategoryDescription(String value) {
        this.childCategoryDescription = value;
    }

    /**
     * Gets the value of the childCategoryID property.
     * 
     */
    public int getChildCategoryID() {
        return childCategoryID;
    }

    /**
     * Sets the value of the childCategoryID property.
     * 
     */
    public void setChildCategoryID(int value) {
        this.childCategoryID = value;
    }

    /**
     * Gets the value of the grandchildCategoryDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrandchildCategoryDescription() {
        return grandchildCategoryDescription;
    }

    /**
     * Sets the value of the grandchildCategoryDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrandchildCategoryDescription(String value) {
        this.grandchildCategoryDescription = value;
    }

    /**
     * Gets the value of the grandchildCategoryID property.
     * 
     */
    public int getGrandchildCategoryID() {
        return grandchildCategoryID;
    }

    /**
     * Sets the value of the grandchildCategoryID property.
     * 
     */
    public void setGrandchildCategoryID(int value) {
        this.grandchildCategoryID = value;
    }

    /**
     * Gets the value of the parentCategoryDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentCategoryDescription() {
        return parentCategoryDescription;
    }

    /**
     * Sets the value of the parentCategoryDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentCategoryDescription(String value) {
        this.parentCategoryDescription = value;
    }

    /**
     * Gets the value of the parentCategoryID property.
     * 
     */
    public int getParentCategoryID() {
        return parentCategoryID;
    }

    /**
     * Sets the value of the parentCategoryID property.
     * 
     */
    public void setParentCategoryID(int value) {
        this.parentCategoryID = value;
    }

}
