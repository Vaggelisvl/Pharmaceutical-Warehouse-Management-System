/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
@Table(name = "payments", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payments.findAll", query = "SELECT p FROM Payments p")
    , @NamedQuery(name = "Payments.findByDragorder", query = "SELECT p FROM Payments p WHERE p.paymentsPK.dragorder = :dragorder")
    , @NamedQuery(name = "Payments.findByAmount", query = "SELECT p FROM Payments p WHERE p.amount = :amount")
    , @NamedQuery(name = "Payments.findByPaydate", query = "SELECT p FROM Payments p WHERE p.paymentsPK.paydate = :paydate")})
public class Payments implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PaymentsPK paymentsPK;
    @Basic(optional = false)
    @Column(name = "amount")
    private double amount;
    @JoinColumn(name = "dragorder", referencedColumnName = "code", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Dragorder dragorder1;

    public Payments() {
    }

    public Payments(PaymentsPK paymentsPK) {
        this.paymentsPK = paymentsPK;
    }

    public Payments(PaymentsPK paymentsPK, double amount) {
        this.paymentsPK = paymentsPK;
        this.amount = amount;
    }

    public Payments(int dragorder, Date paydate) {
        this.paymentsPK = new PaymentsPK(dragorder, paydate);
    }

    public PaymentsPK getPaymentsPK() {
        return paymentsPK;
    }

    public void setPaymentsPK(PaymentsPK paymentsPK) {
        this.paymentsPK = paymentsPK;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
        hash += (paymentsPK != null ? paymentsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payments)) {
            return false;
        }
        Payments other = (Payments) object;
        if ((this.paymentsPK == null && other.paymentsPK != null) || (this.paymentsPK != null && !this.paymentsPK.equals(other.paymentsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.Payments[ paymentsPK=" + paymentsPK + " ]";
    }
    
}
