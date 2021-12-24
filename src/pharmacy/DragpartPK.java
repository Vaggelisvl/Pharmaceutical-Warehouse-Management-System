/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author dnton
 */
@Embeddable
public class DragpartPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "drag")
    private int drag;
    @Basic(optional = false)
    @Column(name = "partno")
    private int partno;

    public DragpartPK() {
    }

    public DragpartPK(int drag, int partno) {
        this.drag = drag;
        this.partno = partno;
    }

    public int getDrag() {
        return drag;
    }

    public void setDrag(int drag) {
        this.drag = drag;
    }

    public int getPartno() {
        return partno;
    }

    public void setPartno(int partno) {
        this.partno = partno;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) drag;
        hash += (int) partno;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DragpartPK)) {
            return false;
        }
        DragpartPK other = (DragpartPK) object;
        if (this.drag != other.drag) {
            return false;
        }
        if (this.partno != other.partno) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.DragpartPK[ drag=" + drag + ", partno=" + partno + " ]";
    }
    
}
