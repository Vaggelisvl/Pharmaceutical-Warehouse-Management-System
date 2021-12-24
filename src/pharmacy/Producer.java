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
@Table(name = "producer", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producer.findAll", query = "SELECT p FROM Producer p")
    , @NamedQuery(name = "Producer.findByCode", query = "SELECT p FROM Producer p WHERE p.code = :code")
    , @NamedQuery(name = "Producer.findByProdname", query = "SELECT p FROM Producer p WHERE p.prodname = :prodname")
    , @NamedQuery(name = "Producer.findByEmail", query = "SELECT p FROM Producer p WHERE p.email = :email")
    , @NamedQuery(name = "Producer.findByWeb", query = "SELECT p FROM Producer p WHERE p.web = :web")})
public class Producer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @Column(name = "prodname")
    private String prodname;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "web")
    private String web;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producer", fetch = FetchType.EAGER)
    private List<Drag> dragList;

    public Producer() {
    }

    public Producer(Integer code) {
        this.code = code;
    }

    public Producer(Integer code, String prodname, String email, String web) {
        this.code = code;
        this.prodname = prodname;
        this.email = email;
        this.web = web;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @XmlTransient
    public List<Drag> getDragList() {
        return dragList;
    }

    public void setDragList(List<Drag> dragList) {
        this.dragList = dragList;
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
        if (!(object instanceof Producer)) {
            return false;
        }
        Producer other = (Producer) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.prodname+" ("+code+")";
        //return "pharmacy.Producer[ code=" + code + " ]";
    }
    
}
