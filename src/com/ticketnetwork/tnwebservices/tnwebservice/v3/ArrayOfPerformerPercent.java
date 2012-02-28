
package com.ticketnetwork.tnwebservices.tnwebservice.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfPerformerPercent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfPerformerPercent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PerformerPercent" type="{http://tnwebservices.ticketnetwork.com/tnwebservice/v3.0}PerformerPercent" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfPerformerPercent", propOrder = {
    "performerPercent"
})
public class ArrayOfPerformerPercent {

    @XmlElement(name = "PerformerPercent", nillable = true)
    protected List<PerformerPercent> performerPercent;

    /**
     * Gets the value of the performerPercent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the performerPercent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerformerPercent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PerformerPercent }
     * 
     * 
     */
    public List<PerformerPercent> getPerformerPercent() {
        if (performerPercent == null) {
            performerPercent = new ArrayList<PerformerPercent>();
        }
        return this.performerPercent;
    }

}
