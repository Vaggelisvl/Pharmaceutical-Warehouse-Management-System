/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pharmacy;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import pharmacy.exceptions.IllegalOrphanException;
import pharmacy.exceptions.NonexistentEntityException;

/**
 *
 * @author dnton
 */
public class DepartmentJpaController implements Serializable {

    public DepartmentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Department department) {
        if (department.getSystemuserList() == null) {
            department.setSystemuserList(new ArrayList<Systemuser>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Systemuser> attachedSystemuserList = new ArrayList<Systemuser>();
            for (Systemuser systemuserListSystemuserToAttach : department.getSystemuserList()) {
                systemuserListSystemuserToAttach = em.getReference(systemuserListSystemuserToAttach.getClass(), systemuserListSystemuserToAttach.getCode());
                attachedSystemuserList.add(systemuserListSystemuserToAttach);
            }
            department.setSystemuserList(attachedSystemuserList);
            em.persist(department);
            for (Systemuser systemuserListSystemuser : department.getSystemuserList()) {
                Department oldDepartmentOfSystemuserListSystemuser = systemuserListSystemuser.getDepartment();
                systemuserListSystemuser.setDepartment(department);
                systemuserListSystemuser = em.merge(systemuserListSystemuser);
                if (oldDepartmentOfSystemuserListSystemuser != null) {
                    oldDepartmentOfSystemuserListSystemuser.getSystemuserList().remove(systemuserListSystemuser);
                    oldDepartmentOfSystemuserListSystemuser = em.merge(oldDepartmentOfSystemuserListSystemuser);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Department department) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department persistentDepartment = em.find(Department.class, department.getCode());
            List<Systemuser> systemuserListOld = persistentDepartment.getSystemuserList();
            List<Systemuser> systemuserListNew = department.getSystemuserList();
            List<String> illegalOrphanMessages = null;
            for (Systemuser systemuserListOldSystemuser : systemuserListOld) {
                if (!systemuserListNew.contains(systemuserListOldSystemuser)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Systemuser " + systemuserListOldSystemuser + " since its department field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Systemuser> attachedSystemuserListNew = new ArrayList<Systemuser>();
            for (Systemuser systemuserListNewSystemuserToAttach : systemuserListNew) {
                systemuserListNewSystemuserToAttach = em.getReference(systemuserListNewSystemuserToAttach.getClass(), systemuserListNewSystemuserToAttach.getCode());
                attachedSystemuserListNew.add(systemuserListNewSystemuserToAttach);
            }
            systemuserListNew = attachedSystemuserListNew;
            department.setSystemuserList(systemuserListNew);
            department = em.merge(department);
            for (Systemuser systemuserListNewSystemuser : systemuserListNew) {
                if (!systemuserListOld.contains(systemuserListNewSystemuser)) {
                    Department oldDepartmentOfSystemuserListNewSystemuser = systemuserListNewSystemuser.getDepartment();
                    systemuserListNewSystemuser.setDepartment(department);
                    systemuserListNewSystemuser = em.merge(systemuserListNewSystemuser);
                    if (oldDepartmentOfSystemuserListNewSystemuser != null && !oldDepartmentOfSystemuserListNewSystemuser.equals(department)) {
                        oldDepartmentOfSystemuserListNewSystemuser.getSystemuserList().remove(systemuserListNewSystemuser);
                        oldDepartmentOfSystemuserListNewSystemuser = em.merge(oldDepartmentOfSystemuserListNewSystemuser);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = department.getCode();
                if (findDepartment(id) == null) {
                    throw new NonexistentEntityException("The department with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Department department;
            try {
                department = em.getReference(Department.class, id);
                department.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The department with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Systemuser> systemuserListOrphanCheck = department.getSystemuserList();
            for (Systemuser systemuserListOrphanCheckSystemuser : systemuserListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Department (" + department + ") cannot be destroyed since the Systemuser " + systemuserListOrphanCheckSystemuser + " in its systemuserList field has a non-nullable department field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(department);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Department> findDepartmentEntities() {
        return findDepartmentEntities(true, -1, -1);
    }

    public List<Department> findDepartmentEntities(int maxResults, int firstResult) {
        return findDepartmentEntities(false, maxResults, firstResult);
    }

    private List<Department> findDepartmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Department.class));
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

    public Department findDepartment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Department> rt = cq.from(Department.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
