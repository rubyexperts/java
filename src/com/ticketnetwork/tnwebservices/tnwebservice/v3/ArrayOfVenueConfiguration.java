
package com.ticketnetwork.tnwebservices.tnwebservice.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfVenueConfiguration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfVenueConfiguration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VenueConfiguration" type="{http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0}VenueConfiguration" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfVenueConfiguration", propOrder = {
    "venueConfiguration"
})
public class ArrayOfVenueConfiguration {

    @XmlElement(name = "VenueConfiguration", nillable = true)
    protected List<VenueConfiguration> venueConfiguration;

    /**
     * Gets the value of the venueConfiguration property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the venueConfiguration property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVenueConfiguration().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VenueConfiguration }
     * 
     * 
     */
    public List<VenueConfiguration> getVenueConfiguration() {
        if (venueConfiguration == null) {
            venueConfiguration = new ArrayList<VenueConfiguration>();
        }
        return this.venueConfiguration;
    }

}
