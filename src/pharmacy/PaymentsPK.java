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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dnton
 */
@Embeddable
public class PaymentsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "dragorder")
    private int dragorder;
    @Basic(optional = false)
    @Column(name = "paydate")
    @Temporal(TemporalType.DATE)
    private Date paydate;

    public PaymentsPK() {
    }

    public PaymentsPK(int dragorder, Date paydate) {
        this.dragorder = dragorder;
        this.paydate = paydate;
    }

    public int getDragorder() {
        return dragorder;
    }

    public void setDragorder(int dragorder) {
        this.dragorder = dragorder;
    }

    public Date getPaydate() {
        return paydate;
    }

    public void setPaydate(Date paydate) {
        this.paydate = paydate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) dragorder;
        hash += (paydate != null ? paydate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PaymentsPK)) {
            return false;
        }
        PaymentsPK other = (PaymentsPK) object;
        if (this.dragorder != other.dragorder) {
            return false;
        }
        if ((this.paydate == null && other.paydate != null) || (this.paydate != null && !this.paydate.equals(other.paydate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.PaymentsPK[ dragorder=" + dragorder + ", paydate=" + paydate + " ]";
    }
    
}
