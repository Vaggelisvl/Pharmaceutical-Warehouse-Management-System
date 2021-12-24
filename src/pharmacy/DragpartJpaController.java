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
import pharmacy.exceptions.PreexistingEntityException;

/**
 *
 * @author dnton
 */
public class DragpartJpaController implements Serializable {

    public DragpartJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dragpart dragpart) throws PreexistingEntityException, Exception {
        if (dragpart.getDragpartPK() == null) {
            dragpart.setDragpartPK(new DragpartPK());
        }
        if (dragpart.getOrderitemsList() == null) {
            dragpart.setOrderitemsList(new ArrayList<Orderitems>());
        }
        dragpart.getDragpartPK().setDrag(dragpart.getDrag1().getCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Drag drag1 = dragpart.getDrag1();
            if (drag1 != null) {
                drag1 = em.getReference(drag1.getClass(), drag1.getCode());
                dragpart.setDrag1(drag1);
            }
            List<Orderitems> attachedOrderitemsList = new ArrayList<Orderitems>();
            for (Orderitems orderitemsListOrderitemsToAttach : dragpart.getOrderitemsList()) {
                orderitemsListOrderitemsToAttach = em.getReference(orderitemsListOrderitemsToAttach.getClass(), orderitemsListOrderitemsToAttach.getOrderitemsPK());
                attachedOrderitemsList.add(orderitemsListOrderitemsToAttach);
            }
            dragpart.setOrderitemsList(attachedOrderitemsList);
            em.persist(dragpart);
            if (drag1 != null) {
                drag1.getDragpartList().add(dragpart);
                drag1 = em.merge(drag1);
            }
            for (Orderitems orderitemsListOrderitems : dragpart.getOrderitemsList()) {
                Dragpart oldDragpartOfOrderitemsListOrderitems = orderitemsListOrderitems.getDragpart();
                orderitemsListOrderitems.setDragpart(dragpart);
                orderitemsListOrderitems = em.merge(orderitemsListOrderitems);
                if (oldDragpartOfOrderitemsListOrderitems != null) {
                    oldDragpartOfOrderitemsListOrderitems.getOrderitemsList().remove(orderitemsListOrderitems);
                    oldDragpartOfOrderitemsListOrderitems = em.merge(oldDragpartOfOrderitemsListOrderitems);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDragpart(dragpart.getDragpartPK()) != null) {
                throw new PreexistingEntityException("Dragpart " + dragpart + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dragpart dragpart) throws IllegalOrphanException, NonexistentEntityException, Exception {
        dragpart.getDragpartPK().setDrag(dragpart.getDrag1().getCode());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dragpart persistentDragpart = em.find(Dragpart.class, dragpart.getDragpartPK());
            Drag drag1Old = persistentDragpart.getDrag1();
            Drag drag1New = dragpart.getDrag1();
            List<Orderitems> orderitemsListOld = persistentDragpart.getOrderitemsList();
            List<Orderitems> orderitemsListNew = dragpart.getOrderitemsList();
            List<String> illegalOrphanMessages = null;
            for (Orderitems orderitemsListOldOrderitems : orderitemsListOld) {
                if (!orderitemsListNew.contains(orderitemsListOldOrderitems)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orderitems " + orderitemsListOldOrderitems + " since its dragpart field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (drag1New != null) {
                drag1New = em.getReference(drag1New.getClass(), drag1New.getCode());
                dragpart.setDrag1(drag1New);
            }
            List<Orderitems> attachedOrderitemsListNew = new ArrayList<Orderitems>();
            for (Orderitems orderitemsListNewOrderitemsToAttach : orderitemsListNew) {
                orderitemsListNewOrderitemsToAttach = em.getReference(orderitemsListNewOrderitemsToAttach.getClass(), orderitemsListNewOrderitemsToAttach.getOrderitemsPK());
                attachedOrderitemsListNew.add(orderitemsListNewOrderitemsToAttach);
            }
            orderitemsListNew = attachedOrderitemsListNew;
            dragpart.setOrderitemsList(orderitemsListNew);
            dragpart = em.merge(dragpart);
            if (drag1Old != null && !drag1Old.equals(drag1New)) {
                drag1Old.getDragpartList().remove(dragpart);
                drag1Old = em.merge(drag1Old);
            }
            if (drag1New != null && !drag1New.equals(drag1Old)) {
                drag1New.getDragpartList().add(dragpart);
                drag1New = em.merge(drag1New);
            }
            for (Orderitems orderitemsListNewOrderitems : orderitemsListNew) {
                if (!orderitemsListOld.contains(orderitemsListNewOrderitems)) {
                    Dragpart oldDragpartOfOrderitemsListNewOrderitems = orderitemsListNewOrderitems.getDragpart();
                    orderitemsListNewOrderitems.setDragpart(dragpart);
                    orderitemsListNewOrderitems = em.merge(orderitemsListNewOrderitems);
                    if (oldDragpartOfOrderitemsListNewOrderitems != null && !oldDragpartOfOrderitemsListNewOrderitems.equals(dragpart)) {
                        oldDragpartOfOrderitemsListNewOrderitems.getOrderitemsList().remove(orderitemsListNewOrderitems);
                        oldDragpartOfOrderitemsListNewOrderitems = em.merge(oldDragpartOfOrderitemsListNewOrderitems);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DragpartPK id = dragpart.getDragpartPK();
                if (findDragpart(id) == null) {
                    throw new NonexistentEntityException("The dragpart with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DragpartPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dragpart dragpart;
            try {
                dragpart = em.getReference(Dragpart.class, id);
                dragpart.getDragpartPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dragpart with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Orderitems> orderitemsListOrphanCheck = dragpart.getOrderitemsList();
            for (Orderitems orderitemsListOrphanCheckOrderitems : orderitemsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dragpart (" + dragpart + ") cannot be destroyed since the Orderitems " + orderitemsListOrphanCheckOrderitems + " in its orderitemsList field has a non-nullable dragpart field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Drag drag1 = dragpart.getDrag1();
            if (drag1 != null) {
                drag1.getDragpartList().remove(dragpart);
                drag1 = em.merge(drag1);
            }
            em.remove(dragpart);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dragpart> findDragpartEntities() {
        return findDragpartEntities(true, -1, -1);
    }

    public List<Dragpart> findDragpartEntities(int maxResults, int firstResult) {
        return findDragpartEntities(false, maxResults, firstResult);
    }

    private List<Dragpart> findDragpartEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dragpart.class));
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

    public Dragpart findDragpart(DragpartPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dragpart.class, id);
        } finally {
            em.close();
        }
    }

    public int getDragpartCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dragpart> rt = cq.from(Dragpart.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
