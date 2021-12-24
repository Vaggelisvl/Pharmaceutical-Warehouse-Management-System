/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.io.Serializable;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dnton
 */
@Entity
@Table(name = "systemuser", catalog = "pharmacyware", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Systemuser.findAll", query = "SELECT s FROM Systemuser s")
    , @NamedQuery(name = "Systemuser.findByCode", query = "SELECT s FROM Systemuser s WHERE s.code = :code")
    , @NamedQuery(name = "Systemuser.findByUsertype", query = "SELECT s FROM Systemuser s WHERE s.usertype = :usertype")
    , @NamedQuery(name = "Systemuser.findByUsername", query = "SELECT s FROM Systemuser s WHERE s.username = :username")
    , @NamedQuery(name = "Systemuser.findByPassword", query = "SELECT s FROM Systemuser s WHERE s.password = :password")
    , @NamedQuery(name = "Systemuser.findByFullname", query = "SELECT s FROM Systemuser s WHERE s.fullname = :fullname")
    , @NamedQuery(name = "Systemuser.findByEmail", query = "SELECT s FROM Systemuser s WHERE s.email = :email")
    , @NamedQuery(name = "Systemuser.findByPhone", query = "SELECT s FROM Systemuser s WHERE s.phone = :phone")})
public class Systemuser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "code")
    private Integer code;
    @Basic(optional = false)
    @Column(name = "usertype")
    private String usertype;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "fullname")
    private String fullname;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @JoinColumn(name = "department", referencedColumnName = "code")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Department department;

    public Systemuser() {
    }

    public Systemuser(Integer code) {
        this.code = code;
    }

    public Systemuser(Integer code, String usertype, String username, String password, String fullname, String email, String phone) {
        this.code = code;
        this.usertype = usertype;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
        if (!(object instanceof Systemuser)) {
            return false;
        }
        Systemuser other = (Systemuser) object;
        if ((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pharmacy.Systemuser[ code=" + code + " ]";
    }
    
}
