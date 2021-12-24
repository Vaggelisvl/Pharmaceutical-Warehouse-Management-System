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
public class SupplierJpaController implements Serializable {

    public SupplierJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Supplier supplier) {
        if (supplier.getDragList() == null) {
            supplier.setDragList(new ArrayList<Drag>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Drag> attachedDragList = new ArrayList<Drag>();
            for (Drag dragListDragToAttach : supplier.getDragList()) {
                dragListDragToAttach = em.getReference(dragListDragToAttach.getClass(), dragListDragToAttach.getCode());
                attachedDragList.add(dragListDragToAttach);
            }
            supplier.setDragList(attachedDragList);
            em.persist(supplier);
            for (Drag dragListDrag : supplier.getDragList()) {
                Supplier oldSupplierOfDragListDrag = dragListDrag.getSupplier();
                dragListDrag.setSupplier(supplier);
                dragListDrag = em.merge(dragListDrag);
                if (oldSupplierOfDragListDrag != null) {
                    oldSupplierOfDragListDrag.getDragList().remove(dragListDrag);
                    oldSupplierOfDragListDrag = em.merge(oldSupplierOfDragListDrag);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Supplier supplier) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier persistentSupplier = em.find(Supplier.class, supplier.getCode());
            List<Drag> dragListOld = persistentSupplier.getDragList();
            List<Drag> dragListNew = supplier.getDragList();
            List<String> illegalOrphanMessages = null;
            for (Drag dragListOldDrag : dragListOld) {
                if (!dragListNew.contains(dragListOldDrag)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Drag " + dragListOldDrag + " since its supplier field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Drag> attachedDragListNew = new ArrayList<Drag>();
            for (Drag dragListNewDragToAttach : dragListNew) {
                dragListNewDragToAttach = em.getReference(dragListNewDragToAttach.getClass(), dragListNewDragToAttach.getCode());
                attachedDragListNew.add(dragListNewDragToAttach);
            }
            dragListNew = attachedDragListNew;
            supplier.setDragList(dragListNew);
            supplier = em.merge(supplier);
            for (Drag dragListNewDrag : dragListNew) {
                if (!dragListOld.contains(dragListNewDrag)) {
                    Supplier oldSupplierOfDragListNewDrag = dragListNewDrag.getSupplier();
                    dragListNewDrag.setSupplier(supplier);
                    dragListNewDrag = em.merge(dragListNewDrag);
                    if (oldSupplierOfDragListNewDrag != null && !oldSupplierOfDragListNewDrag.equals(supplier)) {
                        oldSupplierOfDragListNewDrag.getDragList().remove(dragListNewDrag);
                        oldSupplierOfDragListNewDrag = em.merge(oldSupplierOfDragListNewDrag);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = supplier.getCode();
                if (findSupplier(id) == null) {
                    throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.");
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
            Supplier supplier;
            try {
                supplier = em.getReference(Supplier.class, id);
                supplier.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The supplier with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Drag> dragListOrphanCheck = supplier.getDragList();
            for (Drag dragListOrphanCheckDrag : dragListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Supplier (" + supplier + ") cannot be destroyed since the Drag " + dragListOrphanCheckDrag + " in its dragList field has a non-nullable supplier field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(supplier);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Supplier> findSupplierEntities() {
        return findSupplierEntities(true, -1, -1);
    }

    public List<Supplier> findSupplierEntities(int maxResults, int firstResult) {
        return findSupplierEntities(false, maxResults, firstResult);
    }

    private List<Supplier> findSupplierEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Supplier.class));
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

    public Supplier findSupplier(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Supplier.class, id);
        } finally {
            em.close();
        }
    }

    public int getSupplierCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Supplier> rt = cq.from(Supplier.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
