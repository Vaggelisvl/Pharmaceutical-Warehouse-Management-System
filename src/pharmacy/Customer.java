/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author dnton
 */
@Entity
@Table(name = "customer", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
    , @NamedQuery(name = "Customer.findByCode", query = "SELECT c FROM Customer c WHERE c.code = :code")
    , @NamedQuery(name = "Customer.findByCusname", query = "SELECT c FROM Customer c WHERE c.cusname = :cusname")
    , @NamedQuery(name = "Customer.findByCusperson", query = "SELECT c FROM Customer c WHERE c.cusperson = :cusperson")
    , @NamedQuery(name = "Customer.findByCusaddress", query = "SELECT c FROM Customer c WHERE c.cusaddress = :cusaddress")
    , @NamedQuery(name = "Customer.findByCusemail", query = "SELECT c FROM Customer c WHERE c.cusemail = :cusemail")
    , @NamedQuery(name = "Customer.findByCusphone", query = "SELECT c FROM Customer c WHERE c.cusphone = :cusphone")
    , @NamedQuery(name = "Customer.findByCuslocation", query = "SELECT c FROM Customer c WHERE c.cuslocation = :cuslocation")})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @Column(name = "cusname")
    private String cusname;
    @Basic(optional = false)
    @Column(name = "cusperson")
    private String cusperson;
    @Basic(optional = false)
    @Column(name = "cusaddress")
    private String cusaddress;
    @Basic(optional = false)
    @Column(name = "cusemail")
    private String cusemail;
    @Basic(optional = false)
    @Column(name = "cusphone")
    private String cusphone;
    @Basic(optional = false)
    @Column(name = "cuslocation")
    private String cuslocation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Dragorder> dragorderList;
    @JoinColumn(name = "cuscity", referencedColumnName = "code")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private City cuscity;

    public Customer() {
    }

    public Customer(Integer code) {
        this.code = code;
    }

    public Customer(Integer code, String cusname, String cusperson, String cusaddress, String cusemail, String cusphone, String cuslocation) {
        this.code = code;
        this.cusname = cusname;
        this.cusperson = cusperson;
        this.cusaddress = cusaddress;
        this.cusemail = cusemail;
        this.cusphone = cusphone;
        this.cuslocation = cuslocation;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getCusperson() {
        return cusperson;
    }

    public void setCusperson(String cusperson) {
        this.cusperson = cusperson;
    }

    public String getCusaddress() {
        return cusaddress;
    }

    public void setCusaddress(String cusaddress) {
        this.cusaddress = cusaddress;
    }

    public String getCusemail() {
        return cusemail;
    }

    public void setCusemail(String cusemail) {
        this.cusemail = cusemail;
    }

    public String getCusphone() {
        return cusphone;
    }

    public void setCusphone(String cusphone) {
        this.cusphone = cusphone;
    }

    public String getCuslocation() {
        return cuslocation;
    }

    public void setCuslocation(String cuslocation) {
        this.cuslocation = cuslocation;
    }

    @XmlTransient
    public List<Dragorder> getDragorderList() {
        return dragorderList;
    }

    public void setDragorderList(List<Dragorder> dragorderList) {
        this.dragorderList = dragorderList;
    }

    public City getCuscity() {
        return cuscity;
    }

    public void setCuscity(City cuscity) {
        this.cuscity = cuscity;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.Customer[ code=" + code + " ]";
    }
    
}
