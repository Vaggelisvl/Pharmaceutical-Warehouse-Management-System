/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dnton
 */
@Entity
@Table(name = "dragorder", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dragorder.findAll", query = "SELECT d FROM Dragorder d")
    , @NamedQuery(name = "Dragorder.findByCode", query = "SELECT d FROM Dragorder d WHERE d.code = :code")
    , @NamedQuery(name = "Dragorder.findByOrderdate", query = "SELECT d FROM Dragorder d WHERE d.orderdate = :orderdate")
    , @NamedQuery(name = "Dragorder.findByOrderstatus", query = "SELECT d FROM Dragorder d WHERE d.orderstatus = :orderstatus")})
public class Dragorder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @Column(name = "orderdate")
    @Temporal(TemporalType.DATE)
    private Date orderdate;
    @Basic(optional = false)
    @Column(name = "orderstatus")
    private String orderstatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dragorder1", fetch = FetchType.EAGER)
    private List<Orderitems> orderitemsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dragorder1", fetch = FetchType.EAGER)
    private List<Payments> paymentsList;
    @JoinColumn(name = "customer", referencedColumnName = "code")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Customer customer;

    public Dragorder() {
    }

    public Dragorder(Integer code) {
        this.code = code;
    }

    public Dragorder(Integer code, Date orderdate, String orderstatus) {
        this.code = code;
        this.orderdate = orderdate;
        this.orderstatus = orderstatus;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Date getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Date orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    @XmlTransient
    public List<Orderitems> getOrderitemsList() {
        return orderitemsList;
    }

    public void setOrderitemsList(List<Orderitems> orderitemsList) {
        this.orderitemsList = orderitemsList;
    }

    @XmlTransient
    public List<Payments> getPaymentsList() {
        return paymentsList;
    }

    public void setPaymentsList(List<Payments> paymentsList) {
        this.paymentsList = paymentsList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dragorder)) {
            return false;
        }
        Dragorder other = (Dragorder) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.Dragorder[ code=" + code + " ]";
    }
    
}
