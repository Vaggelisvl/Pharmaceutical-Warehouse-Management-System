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
@Table(name = "drag", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Drag.findAll", query = "SELECT d FROM Drag d")
    , @NamedQuery(name = "Drag.findByCode", query = "SELECT d FROM Drag d WHERE d.code = :code")
    , @NamedQuery(name = "Drag.findByDescription", query = "SELECT d FROM Drag d WHERE d.description = :description")
    , @NamedQuery(name = "Drag.findByBprice", query = "SELECT d FROM Drag d WHERE d.bprice = :bprice")
    , @NamedQuery(name = "Drag.findBySprice", query = "SELECT d FROM Drag d WHERE d.sprice = :sprice")})
public class Drag implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "bprice")
    private float bprice;
    @Basic(optional = false)
    @Column(name = "sprice")
    private float sprice;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "drag1", fetch = FetchType.EAGER)
    private List<Dragpart> dragpartList;
    @JoinColumn(name = "supplier", referencedColumnName = "code")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Supplier supplier;
    @JoinColumn(name = "producer", referencedColumnName = "code")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Producer producer;
    @JoinColumn(name = "type", referencedColumnName = "code")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Dragtype type;

    public Drag() {
    }

    public Drag(Integer code) {
        this.code = code;
    }

    public Drag(Integer code, String description, float bprice, float sprice) {
        this.code = code;
        this.description = description;
        this.bprice = bprice;
        this.sprice = sprice;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getBprice() {
        return bprice;
    }

    public void setBprice(float bprice) {
        this.bprice = bprice;
    }

    public float getSprice() {
        return sprice;
    }

    public void setSprice(float sprice) {
        this.sprice = sprice;
    }

    @XmlTransient
    public List<Dragpart> getDragpartList() {
        return dragpartList;
    }

    public void setDragpartList(List<Dragpart> dragpartList) {
        this.dragpartList = dragpartList;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }

    public Dragtype getType() {
        return type;
    }

    public void setType(Dragtype type) {
        this.type = type;
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
        if (!(object instanceof Drag)) {
            return false;
        }
        Drag other = (Drag) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.Drag[ code=" + code + " ]";
    }
    
}
