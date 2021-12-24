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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "dragpart", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dragpart.findAll", query = "SELECT d FROM Dragpart d")
    , @NamedQuery(name = "Dragpart.findByDrag", query = "SELECT d FROM Dragpart d WHERE d.dragpartPK.drag = :drag")
    , @NamedQuery(name = "Dragpart.findByPartno", query = "SELECT d FROM Dragpart d WHERE d.dragpartPK.partno = :partno")
    , @NamedQuery(name = "Dragpart.findByPartdate", query = "SELECT d FROM Dragpart d WHERE d.partdate = :partdate")
    , @NamedQuery(name = "Dragpart.findByExpdate", query = "SELECT d FROM Dragpart d WHERE d.expdate = :expdate")
    , @NamedQuery(name = "Dragpart.findByQuantity", query = "SELECT d FROM Dragpart d WHERE d.quantity = :quantity")
    , @NamedQuery(name = "Dragpart.findBySeqquantity", query = "SELECT d FROM Dragpart d WHERE d.seqquantity = :seqquantity")
    , @NamedQuery(name = "Dragpart.findByQuantmeasure", query = "SELECT d FROM Dragpart d WHERE d.quantmeasure = :quantmeasure")
    , @NamedQuery(name = "Dragpart.findByCode", query = "SELECT d FROM Dragpart d WHERE d.code = :code")})
public class Dragpart implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DragpartPK dragpartPK;
    @Basic(optional = false)
    @Column(name = "partdate")
    @Temporal(TemporalType.DATE)
    private Date partdate;
    @Basic(optional = false)
    @Column(name = "expdate")
    @Temporal(TemporalType.DATE)
    private Date expdate;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @Column(name = "seqquantity")
    private int seqquantity;
    @Basic(optional = false)
    @Column(name = "quantmeasure")
    private String quantmeasure;
    @Basic(optional = false)
    @Column(name = "code")
    private int code;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dragpart", fetch = FetchType.EAGER)
    private List<Orderitems> orderitemsList;
    @JoinColumn(name = "drag", referencedColumnName = "code", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Drag drag1;

    public Dragpart() {
    }

    public Dragpart(DragpartPK dragpartPK) {
        this.dragpartPK = dragpartPK;
    }

    public Dragpart(DragpartPK dragpartPK, Date partdate, Date expdate, int quantity, int seqquantity, String quantmeasure, int code) {
        this.dragpartPK = dragpartPK;
        this.partdate = partdate;
        this.expdate = expdate;
        this.quantity = quantity;
        this.seqquantity = seqquantity;
        this.quantmeasure = quantmeasure;
        this.code = code;
    }

    public Dragpart(int drag, int partno) {
        this.dragpartPK = new DragpartPK(drag, partno);
    }

    public DragpartPK getDragpartPK() {
        return dragpartPK;
    }

    public void setDragpartPK(DragpartPK dragpartPK) {
        this.dragpartPK = dragpartPK;
    }

    public Date getPartdate() {
        return partdate;
    }

    public void setPartdate(Date partdate) {
        this.partdate = partdate;
    }

    public Date getExpdate() {
        return expdate;
    }

    public void setExpdate(Date expdate) {
        this.expdate = expdate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSeqquantity() {
        return seqquantity;
    }

    public void setSeqquantity(int seqquantity) {
        this.seqquantity = seqquantity;
    }

    public String getQuantmeasure() {
        return quantmeasure;
    }

    public void setQuantmeasure(String quantmeasure) {
        this.quantmeasure = quantmeasure;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @XmlTransient
    public List<Orderitems> getOrderitemsList() {
        return orderitemsList;
    }

    public void setOrderitemsList(List<Orderitems> orderitemsList) {
        this.orderitemsList = orderitemsList;
    }

    public Drag getDrag1() {
        return drag1;
    }

    public void setDrag1(Drag drag1) {
        this.drag1 = drag1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dragpartPK != null ? dragpartPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dragpart)) {
            return false;
        }
        Dragpart other = (Dragpart) object;
        if ((this.dragpartPK == null && other.dragpartPK != null) || (this.dragpartPK != null && !this.dragpartPK.equals(other.dragpartPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.Dragpart[ dragpartPK=" + dragpartPK + " ]";
    }
    
}
