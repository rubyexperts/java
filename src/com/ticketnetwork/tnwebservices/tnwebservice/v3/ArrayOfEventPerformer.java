
package com.ticketnetwork.tnwebservices.tnwebservice.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfEventPerformer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfEventPerformer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="EventPerformer" type="{http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0}EventPerformer" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfEventPerformer", propOrder = {
    "eventPerformer"
})
public class ArrayOfEventPerformer {

    @XmlElement(name = "EventPerformer", nillable = true)
    protected List<EventPerformer> eventPerformer;

    /**
     * Gets the value of the eventPerformer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eventPerformer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEventPerformer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EventPerformer }
     * 
     * 
     */
    public List<EventPerformer> getEventPerformer() {
        if (eventPerformer == null) {
            eventPerformer = new ArrayList<EventPerformer>();
        }
        return this.eventPerformer;
    }

}
