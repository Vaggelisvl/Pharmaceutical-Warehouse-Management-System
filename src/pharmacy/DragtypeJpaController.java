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
public class DragtypeJpaController implements Serializable {

    public DragtypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dragtype dragtype) {
        if (dragtype.getDragList() == null) {
            dragtype.setDragList(new ArrayList<Drag>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Drag> attachedDragList = new ArrayList<Drag>();
            for (Drag dragListDragToAttach : dragtype.getDragList()) {
                dragListDragToAttach = em.getReference(dragListDragToAttach.getClass(), dragListDragToAttach.getCode());
                attachedDragList.add(dragListDragToAttach);
            }
            dragtype.setDragList(attachedDragList);
            em.persist(dragtype);
            for (Drag dragListDrag : dragtype.getDragList()) {
                Dragtype oldTypeOfDragListDrag = dragListDrag.getType();
                dragListDrag.setType(dragtype);
                dragListDrag = em.merge(dragListDrag);
                if (oldTypeOfDragListDrag != null) {
                    oldTypeOfDragListDrag.getDragList().remove(dragListDrag);
                    oldTypeOfDragListDrag = em.merge(oldTypeOfDragListDrag);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dragtype dragtype) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dragtype persistentDragtype = em.find(Dragtype.class, dragtype.getCode());
            List<Drag> dragListOld = persistentDragtype.getDragList();
            List<Drag> dragListNew = dragtype.getDragList();
            List<String> illegalOrphanMessages = null;
            for (Drag dragListOldDrag : dragListOld) {
                if (!dragListNew.contains(dragListOldDrag)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Drag " + dragListOldDrag + " since its type field is not nullable.");
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
            dragtype.setDragList(dragListNew);
            dragtype = em.merge(dragtype);
            for (Drag dragListNewDrag : dragListNew) {
                if (!dragListOld.contains(dragListNewDrag)) {
                    Dragtype oldTypeOfDragListNewDrag = dragListNewDrag.getType();
                    dragListNewDrag.setType(dragtype);
                    dragListNewDrag = em.merge(dragListNewDrag);
                    if (oldTypeOfDragListNewDrag != null && !oldTypeOfDragListNewDrag.equals(dragtype)) {
                        oldTypeOfDragListNewDrag.getDragList().remove(dragListNewDrag);
                        oldTypeOfDragListNewDrag = em.merge(oldTypeOfDragListNewDrag);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dragtype.getCode();
                if (findDragtype(id) == null) {
                    throw new NonexistentEntityException("The dragtype with id " + id + " no longer exists.");
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
            Dragtype dragtype;
            try {
                dragtype = em.getReference(Dragtype.class, id);
                dragtype.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dragtype with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Drag> dragListOrphanCheck = dragtype.getDragList();
            for (Drag dragListOrphanCheckDrag : dragListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dragtype (" + dragtype + ") cannot be destroyed since the Drag " + dragListOrphanCheckDrag + " in its dragList field has a non-nullable type field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dragtype);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dragtype> findDragtypeEntities() {
        return findDragtypeEntities(true, -1, -1);
    }

    public List<Dragtype> findDragtypeEntities(int maxResults, int firstResult) {
        return findDragtypeEntities(false, maxResults, firstResult);
    }

    private List<Dragtype> findDragtypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dragtype.class));
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

    public Dragtype findDragtype(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dragtype.class, id);
        } finally {
            em.close();
        }
    }

    public int getDragtypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dragtype> rt = cq.from(Dragtype.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
