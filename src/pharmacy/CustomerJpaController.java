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
public class CustomerJpaController implements Serializable {

    public CustomerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Customer customer) {
        if (customer.getDragorderList() == null) {
            customer.setDragorderList(new ArrayList<Dragorder>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            City cuscity = customer.getCuscity();
            if (cuscity != null) {
                cuscity = em.getReference(cuscity.getClass(), cuscity.getCode());
                customer.setCuscity(cuscity);
            }
            List<Dragorder> attachedDragorderList = new ArrayList<Dragorder>();
            for (Dragorder dragorderListDragorderToAttach : customer.getDragorderList()) {
                dragorderListDragorderToAttach = em.getReference(dragorderListDragorderToAttach.getClass(), dragorderListDragorderToAttach.getCode());
                attachedDragorderList.add(dragorderListDragorderToAttach);
            }
            customer.setDragorderList(attachedDragorderList);
            em.persist(customer);
            if (cuscity != null) {
                cuscity.getCustomerList().add(customer);
                cuscity = em.merge(cuscity);
            }
            for (Dragorder dragorderListDragorder : customer.getDragorderList()) {
                Customer oldCustomerOfDragorderListDragorder = dragorderListDragorder.getCustomer();
                dragorderListDragorder.setCustomer(customer);
                dragorderListDragorder = em.merge(dragorderListDragorder);
                if (oldCustomerOfDragorderListDragorder != null) {
                    oldCustomerOfDragorderListDragorder.getDragorderList().remove(dragorderListDragorder);
                    oldCustomerOfDragorderListDragorder = em.merge(oldCustomerOfDragorderListDragorder);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Customer customer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer persistentCustomer = em.find(Customer.class, customer.getCode());
            City cuscityOld = persistentCustomer.getCuscity();
            City cuscityNew = customer.getCuscity();
            List<Dragorder> dragorderListOld = persistentCustomer.getDragorderList();
            List<Dragorder> dragorderListNew = customer.getDragorderList();
            List<String> illegalOrphanMessages = null;
            for (Dragorder dragorderListOldDragorder : dragorderListOld) {
                if (!dragorderListNew.contains(dragorderListOldDragorder)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Dragorder " + dragorderListOldDragorder + " since its customer field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cuscityNew != null) {
                cuscityNew = em.getReference(cuscityNew.getClass(), cuscityNew.getCode());
                customer.setCuscity(cuscityNew);
            }
            List<Dragorder> attachedDragorderListNew = new ArrayList<Dragorder>();
            for (Dragorder dragorderListNewDragorderToAttach : dragorderListNew) {
                dragorderListNewDragorderToAttach = em.getReference(dragorderListNewDragorderToAttach.getClass(), dragorderListNewDragorderToAttach.getCode());
                attachedDragorderListNew.add(dragorderListNewDragorderToAttach);
            }
            dragorderListNew = attachedDragorderListNew;
            customer.setDragorderList(dragorderListNew);
            customer = em.merge(customer);
            if (cuscityOld != null && !cuscityOld.equals(cuscityNew)) {
                cuscityOld.getCustomerList().remove(customer);
                cuscityOld = em.merge(cuscityOld);
            }
            if (cuscityNew != null && !cuscityNew.equals(cuscityOld)) {
                cuscityNew.getCustomerList().add(customer);
                cuscityNew = em.merge(cuscityNew);
            }
            for (Dragorder dragorderListNewDragorder : dragorderListNew) {
                if (!dragorderListOld.contains(dragorderListNewDragorder)) {
                    Customer oldCustomerOfDragorderListNewDragorder = dragorderListNewDragorder.getCustomer();
                    dragorderListNewDragorder.setCustomer(customer);
                    dragorderListNewDragorder = em.merge(dragorderListNewDragorder);
                    if (oldCustomerOfDragorderListNewDragorder != null && !oldCustomerOfDragorderListNewDragorder.equals(customer)) {
                        oldCustomerOfDragorderListNewDragorder.getDragorderList().remove(dragorderListNewDragorder);
                        oldCustomerOfDragorderListNewDragorder = em.merge(oldCustomerOfDragorderListNewDragorder);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = customer.getCode();
                if (findCustomer(id) == null) {
                    throw new NonexistentEntityException("The customer with id " + id + " no longer exists.");
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
            Customer customer;
            try {
                customer = em.getReference(Customer.class, id);
                customer.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The customer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Dragorder> dragorderListOrphanCheck = customer.getDragorderList();
            for (Dragorder dragorderListOrphanCheckDragorder : dragorderListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Customer (" + customer + ") cannot be destroyed since the Dragorder " + dragorderListOrphanCheckDragorder + " in its dragorderList field has a non-nullable customer field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            City cuscity = customer.getCuscity();
            if (cuscity != null) {
                cuscity.getCustomerList().remove(customer);
                cuscity = em.merge(cuscity);
            }
            em.remove(customer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Customer> findCustomerEntities() {
        return findCustomerEntities(true, -1, -1);
    }

    public List<Customer> findCustomerEntities(int maxResults, int firstResult) {
        return findCustomerEntities(false, maxResults, firstResult);
    }

    private List<Customer> findCustomerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Customer.class));
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

    public Customer findCustomer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Customer.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Customer> rt = cq.from(Customer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
