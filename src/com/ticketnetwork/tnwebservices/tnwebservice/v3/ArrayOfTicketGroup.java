
package com.ticketnetwork.tnwebservices.tnwebservice.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTicketGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTicketGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TicketGroup" type="{http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0}TicketGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTicketGroup", propOrder = {
    "ticketGroup"
})
public class ArrayOfTicketGroup {

    @XmlElement(name = "TicketGroup", nillable = true)
    protected List<TicketGroup> ticketGroup;

    /**
     * Gets the value of the ticketGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ticketGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTicketGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TicketGroup }
     * 
     * 
     */
    public List<TicketGroup> getTicketGroup() {
        if (ticketGroup == null) {
            ticketGroup = new ArrayList<TicketGroup>();
        }
        return this.ticketGroup;
    }

}
