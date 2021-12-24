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
public class OrderitemsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "dragorder")
    private int dragorder;
    @Basic(optional = false)
    @Column(name = "item")
    private int item;
    @Basic(optional = false)
    @Column(name = "partno")
    private int partno;

    public OrderitemsPK() {
    }

    public OrderitemsPK(int dragorder, int item, int partno) {
        this.dragorder = dragorder;
        this.item = item;
        this.partno = partno;
    }

    public int getDragorder() {
        return dragorder;
    }

    public void setDragorder(int dragorder) {
        this.dragorder = dragorder;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
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
        hash += (int) dragorder;
        hash += (int) item;
        hash += (int) partno;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OrderitemsPK)) {
            return false;
        }
        OrderitemsPK other = (OrderitemsPK) object;
        if (this.dragorder != other.dragorder) {
            return false;
        }
        if (this.item != other.item) {
            return false;
        }
        if (this.partno != other.partno) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.OrderitemsPK[ dragorder=" + dragorder + ", item=" + item + ", partno=" + partno + " ]";
    }
    
}
