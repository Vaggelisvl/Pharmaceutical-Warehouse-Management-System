/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dnton
 */
@Entity
@Table(name = "orderitems", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Orderitems.findAll", query = "SELECT o FROM Orderitems o")
    , @NamedQuery(name = "Orderitems.findByDragorder", query = "SELECT o FROM Orderitems o WHERE o.orderitemsPK.dragorder = :dragorder")
    , @NamedQuery(name = "Orderitems.findByItem", query = "SELECT o FROM Orderitems o WHERE o.orderitemsPK.item = :item")
    , @NamedQuery(name = "Orderitems.findByPartno", query = "SELECT o FROM Orderitems o WHERE o.orderitemsPK.partno = :partno")
    , @NamedQuery(name = "Orderitems.findByQuantity", query = "SELECT o FROM Orderitems o WHERE o.quantity = :quantity")})
public class Orderitems implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OrderitemsPK orderitemsPK;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @JoinColumns({
        @JoinColumn(name = "item", referencedColumnName = "drag", insertable = false, updatable = false)
        , @JoinColumn(name = "partno", referencedColumnName = "partno", insertable = false, updatable = false)})
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Dragpart dragpart;
    @JoinColumn(name = "dragorder", referencedColumnName = "code", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Dragorder dragorder1;

    public Orderitems() {
    }

    public Orderitems(OrderitemsPK orderitemsPK) {
        this.orderitemsPK = orderitemsPK;
    }

    public Orderitems(OrderitemsPK orderitemsPK, int quantity) {
        this.orderitemsPK = orderitemsPK;
        this.quantity = quantity;
    }

    public Orderitems(int dragorder, int item, int partno) {
        this.orderitemsPK = new OrderitemsPK(dragorder, item, partno);
    }

    public OrderitemsPK getOrderitemsPK() {
        return orderitemsPK;
    }

    public void setOrderitemsPK(OrderitemsPK orderitemsPK) {
        this.orderitemsPK = orderitemsPK;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Dragpart getDragpart() {
        return dragpart;
    }

    public void setDragpart(Dragpart dragpart) {
        this.dragpart = dragpart;
    }

    public Dragorder getDragorder1() {
        return dragorder1;
    }

    public void setDragorder1(Dragorder dragorder1) {
        this.dragorder1 = dragorder1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orderitemsPK != null ? orderitemsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Orderitems)) {
            return false;
        }
        Orderitems other = (Orderitems) object;
        if ((this.orderitemsPK == null && other.orderitemsPK != null) || (this.orderitemsPK != null && !this.orderitemsPK.equals(other.orderitemsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.Orderitems[ orderitemsPK=" + orderitemsPK + " ]";
    }
    
}
