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
@Table(name = "dragtype", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dragtype.findAll", query = "SELECT d FROM Dragtype d")
    , @NamedQuery(name = "Dragtype.findByCode", query = "SELECT d FROM Dragtype d WHERE d.code = :code")
    , @NamedQuery(name = "Dragtype.findByDragtype", query = "SELECT d FROM Dragtype d WHERE d.dragtype = :dragtype")})
public class Dragtype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @Column(name = "dragtype")
    private String dragtype;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "type", fetch = FetchType.EAGER)
    private List<Drag> dragList;

    public Dragtype() {
    }

    public Dragtype(Integer code) {
        this.code = code;
    }

    public Dragtype(Integer code, String dragtype) {
        this.code = code;
        this.dragtype = dragtype;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDragtype() {
        return dragtype;
    }

    public void setDragtype(String dragtype) {
        this.dragtype = dragtype;
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
        if (!(object instanceof Dragtype)) {
            return false;
        }
        Dragtype other = (Dragtype) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getDragtype();
        //return "pharmacy.Dragtype[ code=" + code + " ]";
    }
    
}
