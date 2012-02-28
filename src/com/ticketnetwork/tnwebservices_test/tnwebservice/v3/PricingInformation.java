
package com.ticketnetwork.tnwebservices_test.tnwebservice.v3;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PricingInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PricingInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ticketsAvailable" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="highPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="lowPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="weightedAvgPrice" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *         &lt;element name="eventName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PricingInformation", propOrder = {
    "ticketsAvailable",
    "highPrice",
    "lowPrice",
    "weightedAvgPrice",
    "eventName"
})
public class PricingInformation {

    protected int ticketsAvailable;
    @XmlElement(required = true)
    protected BigDecimal highPrice;
    @XmlElement(required = true)
    protected BigDecimal lowPrice;
    @XmlElement(required = true)
    protected BigDecimal weightedAvgPrice;
    protected String eventName;

    /**
     * Gets the value of the ticketsAvailable property.
     * 
     */
    public int getTicketsAvailable() {
        return ticketsAvailable;
    }

    /**
     * Sets the value of the ticketsAvailable property.
     * 
     */
    public void setTicketsAvailable(int value) {
        this.ticketsAvailable = value;
    }

    /**
     * Gets the value of the highPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHighPrice() {
        return highPrice;
    }

    /**
     * Sets the value of the highPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHighPrice(BigDecimal value) {
        this.highPrice = value;
    }

    /**
     * Gets the value of the lowPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    /**
     * Sets the value of the lowPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLowPrice(BigDecimal value) {
        this.lowPrice = value;
    }

    /**
     * Gets the value of the weightedAvgPrice property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getWeightedAvgPrice() {
        return weightedAvgPrice;
    }

    /**
     * Sets the value of the weightedAvgPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setWeightedAvgPrice(BigDecimal value) {
        this.weightedAvgPrice = value;
    }

    /**
     * Gets the value of the eventName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the value of the eventName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEventName(String value) {
        this.eventName = value;
    }

}
