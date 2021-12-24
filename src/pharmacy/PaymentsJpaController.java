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
import pharmacy.exceptions.PreexistingEntityException;

/**
 *
 * @author dnton
 */
public class PaymentsJpaController implements Serializable {

    public PaymentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Payments payments) throws PreexistingEntityException, Exception {
        if (payments.getPaymentsPK() == null) {
            payments.setPaymentsPK(new PaymentsPK());
        }
        payments.getPaymentsPK().setDragorder(payments.getDragorder1().getCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dragorder dragorder1 = payments.getDragorder1();
            if (dragorder1 != null) {
                dragorder1 = em.getReference(dragorder1.getClass(), dragorder1.getCode());
                payments.setDragorder1(dragorder1);
            }
            em.persist(payments);
            if (dragorder1 != null) {
                dragorder1.getPaymentsList().add(payments);
                dragorder1 = em.merge(dragorder1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPayments(payments.getPaymentsPK()) != null) {
                throw new PreexistingEntityException("Payments " + payments + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Payments payments) throws NonexistentEntityException, Exception {
        payments.getPaymentsPK().setDragorder(payments.getDragorder1().getCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Payments persistentPayments = em.find(Payments.class, payments.getPaymentsPK());
            Dragorder dragorder1Old = persistentPayments.getDragorder1();
            Dragorder dragorder1New = payments.getDragorder1();
            if (dragorder1New != null) {
                dragorder1New = em.getReference(dragorder1New.getClass(), dragorder1New.getCode());
                payments.setDragorder1(dragorder1New);
            }
            payments = em.merge(payments);
            if (dragorder1Old != null && !dragorder1Old.equals(dragorder1New)) {
                dragorder1Old.getPaymentsList().remove(payments);
                dragorder1Old = em.merge(dragorder1Old);
            }
            if (dragorder1New != null && !dragorder1New.equals(dragorder1Old)) {
                dragorder1New.getPaymentsList().add(payments);
                dragorder1New = em.merge(dragorder1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PaymentsPK id = payments.getPaymentsPK();
                if (findPayments(id) == null) {
                    throw new NonexistentEntityException("The payments with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PaymentsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Payments payments;
            try {
                payments = em.getReference(Payments.class, id);
                payments.getPaymentsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The payments with id " + id + " no longer exists.", enfe);
            }
            Dragorder dragorder1 = payments.getDragorder1();
            if (dragorder1 != null) {
                dragorder1.getPaymentsList().remove(payments);
                dragorder1 = em.merge(dragorder1);
            }
            em.remove(payments);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Payments> findPaymentsEntities() {
        return findPaymentsEntities(true, -1, -1);
    }

    public List<Payments> findPaymentsEntities(int maxResults, int firstResult) {
        return findPaymentsEntities(false, maxResults, firstResult);
    }

    private List<Payments> findPaymentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Payments.class));
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

    public Payments findPayments(PaymentsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Payments.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaymentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Payments> rt = cq.from(Payments.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
