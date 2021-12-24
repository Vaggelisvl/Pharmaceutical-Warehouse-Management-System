/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pharmacy.exceptions.NonexistentEntityException;

/**
 *
 * @author dnton
 */
public class SystemuserJpaController implements Serializable {

    public SystemuserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Systemuser systemuser) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department department = systemuser.getDepartment();
            if (department != null) {
                department = em.getReference(department.getClass(), department.getCode());
                systemuser.setDepartment(department);
            }
            em.persist(systemuser);
            if (department != null) {
                department.getSystemuserList().add(systemuser);
                department = em.merge(department);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Systemuser systemuser) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Systemuser persistentSystemuser = em.find(Systemuser.class, systemuser.getCode());
            Department departmentOld = persistentSystemuser.getDepartment();
            Department departmentNew = systemuser.getDepartment();
            if (departmentNew != null) {
                departmentNew = em.getReference(departmentNew.getClass(), departmentNew.getCode());
                systemuser.setDepartment(departmentNew);
            }
            systemuser = em.merge(systemuser);
            if (departmentOld != null && !departmentOld.equals(departmentNew)) {
                departmentOld.getSystemuserList().remove(systemuser);
                departmentOld = em.merge(departmentOld);
            }
            if (departmentNew != null && !departmentNew.equals(departmentOld)) {
                departmentNew.getSystemuserList().add(systemuser);
                departmentNew = em.merge(departmentNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = systemuser.getCode();
                if (findSystemuser(id) == null) {
                    throw new NonexistentEntityException("The systemuser with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Systemuser systemuser;
            try {
                systemuser = em.getReference(Systemuser.class, id);
                systemuser.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The systemuser with id " + id + " no longer exists.", enfe);
            }
            Department department = systemuser.getDepartment();
            if (department != null) {
                department.getSystemuserList().remove(systemuser);
                department = em.merge(department);
            }
            em.remove(systemuser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Systemuser> findSystemuserEntities() {
        return findSystemuserEntities(true, -1, -1);
    }

    public List<Systemuser> findSystemuserEntities(int maxResults, int firstResult) {
        return findSystemuserEntities(false, maxResults, firstResult);
    }

    private List<Systemuser> findSystemuserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Systemuser.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Systemuser findSystemuser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Systemuser.class, id);
        } finally {
            em.close();
        }
    }

    public int getSystemuserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Systemuser> rt = cq.from(Systemuser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
