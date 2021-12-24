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
public class ProducerJpaController implements Serializable {

    public ProducerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producer producer) {
        if (producer.getDragList() == null) {
            producer.setDragList(new ArrayList<Drag>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Drag> attachedDragList = new ArrayList<Drag>();
            for (Drag dragListDragToAttach : producer.getDragList()) {
                dragListDragToAttach = em.getReference(dragListDragToAttach.getClass(), dragListDragToAttach.getCode());
                attachedDragList.add(dragListDragToAttach);
            }
            producer.setDragList(attachedDragList);
            em.persist(producer);
            for (Drag dragListDrag : producer.getDragList()) {
                Producer oldProducerOfDragListDrag = dragListDrag.getProducer();
                dragListDrag.setProducer(producer);
                dragListDrag = em.merge(dragListDrag);
                if (oldProducerOfDragListDrag != null) {
                    oldProducerOfDragListDrag.getDragList().remove(dragListDrag);
                    oldProducerOfDragListDrag = em.merge(oldProducerOfDragListDrag);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producer producer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producer persistentProducer = em.find(Producer.class, producer.getCode());
            List<Drag> dragListOld = persistentProducer.getDragList();
            List<Drag> dragListNew = producer.getDragList();
            List<String> illegalOrphanMessages = null;
            for (Drag dragListOldDrag : dragListOld) {
                if (!dragListNew.contains(dragListOldDrag)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Drag " + dragListOldDrag + " since its producer field is not nullable.");
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
            producer.setDragList(dragListNew);
            producer = em.merge(producer);
            for (Drag dragListNewDrag : dragListNew) {
                if (!dragListOld.contains(dragListNewDrag)) {
                    Producer oldProducerOfDragListNewDrag = dragListNewDrag.getProducer();
                    dragListNewDrag.setProducer(producer);
                    dragListNewDrag = em.merge(dragListNewDrag);
                    if (oldProducerOfDragListNewDrag != null && !oldProducerOfDragListNewDrag.equals(producer)) {
                        oldProducerOfDragListNewDrag.getDragList().remove(dragListNewDrag);
                        oldProducerOfDragListNewDrag = em.merge(oldProducerOfDragListNewDrag);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producer.getCode();
                if (findProducer(id) == null) {
                    throw new NonexistentEntityException("The producer with id " + id + " no longer exists.");
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
            Producer producer;
            try {
                producer = em.getReference(Producer.class, id);
                producer.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Drag> dragListOrphanCheck = producer.getDragList();
            for (Drag dragListOrphanCheckDrag : dragListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producer (" + producer + ") cannot be destroyed since the Drag " + dragListOrphanCheckDrag + " in its dragList field has a non-nullable producer field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(producer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producer> findProducerEntities() {
        return findProducerEntities(true, -1, -1);
    }

    public List<Producer> findProducerEntities(int maxResults, int firstResult) {
        return findProducerEntities(false, maxResults, firstResult);
    }

    private List<Producer> findProducerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producer.class));
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

    public Producer findProducer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producer.class, id);
        } finally {
            em.close();
        }
    }

    public int getProducerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producer> rt = cq.from(Producer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
