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
public class OrderitemsJpaController implements Serializable {

    public OrderitemsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Orderitems orderitems) throws PreexistingEntityException, Exception {
        if (orderitems.getOrderitemsPK() == null) {
            orderitems.setOrderitemsPK(new OrderitemsPK());
        }
        orderitems.getOrderitemsPK().setDragorder(orderitems.getDragorder1().getCode());
        orderitems.getOrderitemsPK().setPartno(orderitems.getDragpart().getDragpartPK().getPartno());
        orderitems.getOrderitemsPK().setItem(orderitems.getDragpart().getDragpartPK().getDrag());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dragpart dragpart = orderitems.getDragpart();
            if (dragpart != null) {
                dragpart = em.getReference(dragpart.getClass(), dragpart.getDragpartPK());
                orderitems.setDragpart(dragpart);
            }
            Dragorder dragorder1 = orderitems.getDragorder1();
            if (dragorder1 != null) {
                dragorder1 = em.getReference(dragorder1.getClass(), dragorder1.getCode());
                orderitems.setDragorder1(dragorder1);
            }
            em.persist(orderitems);
            if (dragpart != null) {
                dragpart.getOrderitemsList().add(orderitems);
                dragpart = em.merge(dragpart);
            }
            if (dragorder1 != null) {
                dragorder1.getOrderitemsList().add(orderitems);
                dragorder1 = em.merge(dragorder1);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findOrderitems(orderitems.getOrderitemsPK()) != null) {
                throw new PreexistingEntityException("Orderitems " + orderitems + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Orderitems orderitems) throws NonexistentEntityException, Exception {
        orderitems.getOrderitemsPK().setDragorder(orderitems.getDragorder1().getCode());
        orderitems.getOrderitemsPK().setPartno(orderitems.getDragpart().getDragpartPK().getPartno());
        orderitems.getOrderitemsPK().setItem(orderitems.getDragpart().getDragpartPK().getDrag());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orderitems persistentOrderitems = em.find(Orderitems.class, orderitems.getOrderitemsPK());
            Dragpart dragpartOld = persistentOrderitems.getDragpart();
            Dragpart dragpartNew = orderitems.getDragpart();
            Dragorder dragorder1Old = persistentOrderitems.getDragorder1();
            Dragorder dragorder1New = orderitems.getDragorder1();
            if (dragpartNew != null) {
                dragpartNew = em.getReference(dragpartNew.getClass(), dragpartNew.getDragpartPK());
                orderitems.setDragpart(dragpartNew);
            }
            if (dragorder1New != null) {
                dragorder1New = em.getReference(dragorder1New.getClass(), dragorder1New.getCode());
                orderitems.setDragorder1(dragorder1New);
            }
            orderitems = em.merge(orderitems);
            if (dragpartOld != null && !dragpartOld.equals(dragpartNew)) {
                dragpartOld.getOrderitemsList().remove(orderitems);
                dragpartOld = em.merge(dragpartOld);
            }
            if (dragpartNew != null && !dragpartNew.equals(dragpartOld)) {
                dragpartNew.getOrderitemsList().add(orderitems);
                dragpartNew = em.merge(dragpartNew);
            }
            if (dragorder1Old != null && !dragorder1Old.equals(dragorder1New)) {
                dragorder1Old.getOrderitemsList().remove(orderitems);
                dragorder1Old = em.merge(dragorder1Old);
            }
            if (dragorder1New != null && !dragorder1New.equals(dragorder1Old)) {
                dragorder1New.getOrderitemsList().add(orderitems);
                dragorder1New = em.merge(dragorder1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                OrderitemsPK id = orderitems.getOrderitemsPK();
                if (findOrderitems(id) == null) {
                    throw new NonexistentEntityException("The orderitems with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(OrderitemsPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Orderitems orderitems;
            try {
                orderitems = em.getReference(Orderitems.class, id);
                orderitems.getOrderitemsPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The orderitems with id " + id + " no longer exists.", enfe);
            }
            Dragpart dragpart = orderitems.getDragpart();
            if (dragpart != null) {
                dragpart.getOrderitemsList().remove(orderitems);
                dragpart = em.merge(dragpart);
            }
            Dragorder dragorder1 = orderitems.getDragorder1();
            if (dragorder1 != null) {
                dragorder1.getOrderitemsList().remove(orderitems);
                dragorder1 = em.merge(dragorder1);
            }
            em.remove(orderitems);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Orderitems> findOrderitemsEntities() {
        return findOrderitemsEntities(true, -1, -1);
    }

    public List<Orderitems> findOrderitemsEntities(int maxResults, int firstResult) {
        return findOrderitemsEntities(false, maxResults, firstResult);
    }

    private List<Orderitems> findOrderitemsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Orderitems.class));
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

    public Orderitems findOrderitems(OrderitemsPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Orderitems.class, id);
        } finally {
            em.close();
        }
    }

    public int getOrderitemsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Orderitems> rt = cq.from(Orderitems.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
