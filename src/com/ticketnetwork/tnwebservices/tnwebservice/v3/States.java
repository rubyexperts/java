
package com.ticketnetwork.tnwebservices.tnwebservice.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for States complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="States">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="StateProvinceShortDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StateProvinceLongDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StateProvinceID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CountryID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "States", propOrder = {
    "stateProvinceShortDesc",
    "stateProvinceLongDesc",
    "stateProvinceID",
    "countryID"
})
public class States {

    @XmlElement(name = "StateProvinceShortDesc")
    protected String stateProvinceShortDesc;
    @XmlElement(name = "StateProvinceLongDesc")
    protected String stateProvinceLongDesc;
    @XmlElement(name = "StateProvinceID")
    protected int stateProvinceID;
    @XmlElement(name = "CountryID")
    protected int countryID;

    /**
     * Gets the value of the stateProvinceShortDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateProvinceShortDesc() {
        return stateProvinceShortDesc;
    }

    /**
     * Sets the value of the stateProvinceShortDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateProvinceShortDesc(String value) {
        this.stateProvinceShortDesc = value;
    }

    /**
     * Gets the value of the stateProvinceLongDesc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateProvinceLongDesc() {
        return stateProvinceLongDesc;
    }

    /**
     * Sets the value of the stateProvinceLongDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateProvinceLongDesc(String value) {
        this.stateProvinceLongDesc = value;
    }

    /**
     * Gets the value of the stateProvinceID property.
     * 
     */
    public int getStateProvinceID() {
        return stateProvinceID;
    }

    /**
     * Sets the value of the stateProvinceID property.
     * 
     */
    public void setStateProvinceID(int value) {
        this.stateProvinceID = value;
    }

    /**
     * Gets the value of the countryID property.
     * 
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Sets the value of the countryID property.
     * 
     */
    public void setCountryID(int value) {
        this.countryID = value;
    }

}
