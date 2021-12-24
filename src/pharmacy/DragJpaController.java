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
public class DragJpaController implements Serializable {

    public DragJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Drag drag) {
        if (drag.getDragpartList() == null) {
            drag.setDragpartList(new ArrayList<Dragpart>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Supplier supplier = drag.getSupplier();
            if (supplier != null) {
                supplier = em.getReference(supplier.getClass(), supplier.getCode());
                drag.setSupplier(supplier);
            }
            Producer producer = drag.getProducer();
            if (producer != null) {
                producer = em.getReference(producer.getClass(), producer.getCode());
                drag.setProducer(producer);
            }
            Dragtype type = drag.getType();
            if (type != null) {
                type = em.getReference(type.getClass(), type.getCode());
                drag.setType(type);
            }
            List<Dragpart> attachedDragpartList = new ArrayList<Dragpart>();
            for (Dragpart dragpartListDragpartToAttach : drag.getDragpartList()) {
                dragpartListDragpartToAttach = em.getReference(dragpartListDragpartToAttach.getClass(), dragpartListDragpartToAttach.getDragpartPK());
                attachedDragpartList.add(dragpartListDragpartToAttach);
            }
            drag.setDragpartList(attachedDragpartList);
            em.persist(drag);
            if (supplier != null) {
                supplier.getDragList().add(drag);
                supplier = em.merge(supplier);
            }
            if (producer != null) {
                producer.getDragList().add(drag);
                producer = em.merge(producer);
            }
            if (type != null) {
                type.getDragList().add(drag);
                type = em.merge(type);
            }
            for (Dragpart dragpartListDragpart : drag.getDragpartList()) {
                Drag oldDrag1OfDragpartListDragpart = dragpartListDragpart.getDrag1();
                dragpartListDragpart.setDrag1(drag);
                dragpartListDragpart = em.merge(dragpartListDragpart);
                if (oldDrag1OfDragpartListDragpart != null) {
                    oldDrag1OfDragpartListDragpart.getDragpartList().remove(dragpartListDragpart);
                    oldDrag1OfDragpartListDragpart = em.merge(oldDrag1OfDragpartListDragpart);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Drag drag) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Drag persistentDrag = em.find(Drag.class, drag.getCode());
            Supplier supplierOld = persistentDrag.getSupplier();
            Supplier supplierNew = drag.getSupplier();
            Producer producerOld = persistentDrag.getProducer();
            Producer producerNew = drag.getProducer();
            Dragtype typeOld = persistentDrag.getType();
            Dragtype typeNew = drag.getType();
            List<Dragpart> dragpartListOld = persistentDrag.getDragpartList();
            List<Dragpart> dragpartListNew = drag.getDragpartList();
            List<String> illegalOrphanMessages = null;
            for (Dragpart dragpartListOldDragpart : dragpartListOld) {
                if (!dragpartListNew.contains(dragpartListOldDragpart)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Dragpart " + dragpartListOldDragpart + " since its drag1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (supplierNew != null) {
                supplierNew = em.getReference(supplierNew.getClass(), supplierNew.getCode());
                drag.setSupplier(supplierNew);
            }
            if (producerNew != null) {
                producerNew = em.getReference(producerNew.getClass(), producerNew.getCode());
                drag.setProducer(producerNew);
            }
            if (typeNew != null) {
                typeNew = em.getReference(typeNew.getClass(), typeNew.getCode());
                drag.setType(typeNew);
            }
            List<Dragpart> attachedDragpartListNew = new ArrayList<Dragpart>();
            for (Dragpart dragpartListNewDragpartToAttach : dragpartListNew) {
                dragpartListNewDragpartToAttach = em.getReference(dragpartListNewDragpartToAttach.getClass(), dragpartListNewDragpartToAttach.getDragpartPK());
                attachedDragpartListNew.add(dragpartListNewDragpartToAttach);
            }
            dragpartListNew = attachedDragpartListNew;
            drag.setDragpartList(dragpartListNew);
            drag = em.merge(drag);
            if (supplierOld != null && !supplierOld.equals(supplierNew)) {
                supplierOld.getDragList().remove(drag);
                supplierOld = em.merge(supplierOld);
            }
            if (supplierNew != null && !supplierNew.equals(supplierOld)) {
                supplierNew.getDragList().add(drag);
                supplierNew = em.merge(supplierNew);
            }
            if (producerOld != null && !producerOld.equals(producerNew)) {
                producerOld.getDragList().remove(drag);
                producerOld = em.merge(producerOld);
            }
            if (producerNew != null && !producerNew.equals(producerOld)) {
                producerNew.getDragList().add(drag);
                producerNew = em.merge(producerNew);
            }
            if (typeOld != null && !typeOld.equals(typeNew)) {
                typeOld.getDragList().remove(drag);
                typeOld = em.merge(typeOld);
            }
            if (typeNew != null && !typeNew.equals(typeOld)) {
                typeNew.getDragList().add(drag);
                typeNew = em.merge(typeNew);
            }
            for (Dragpart dragpartListNewDragpart : dragpartListNew) {
                if (!dragpartListOld.contains(dragpartListNewDragpart)) {
                    Drag oldDrag1OfDragpartListNewDragpart = dragpartListNewDragpart.getDrag1();
                    dragpartListNewDragpart.setDrag1(drag);
                    dragpartListNewDragpart = em.merge(dragpartListNewDragpart);
                    if (oldDrag1OfDragpartListNewDragpart != null && !oldDrag1OfDragpartListNewDragpart.equals(drag)) {
                        oldDrag1OfDragpartListNewDragpart.getDragpartList().remove(dragpartListNewDragpart);
                        oldDrag1OfDragpartListNewDragpart = em.merge(oldDrag1OfDragpartListNewDragpart);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = drag.getCode();
                if (findDrag(id) == null) {
                    throw new NonexistentEntityException("The drag with id " + id + " no longer exists.");
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
            Drag drag;
            try {
                drag = em.getReference(Drag.class, id);
                drag.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The drag with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Dragpart> dragpartListOrphanCheck = drag.getDragpartList();
            for (Dragpart dragpartListOrphanCheckDragpart : dragpartListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Drag (" + drag + ") cannot be destroyed since the Dragpart " + dragpartListOrphanCheckDragpart + " in its dragpartList field has a non-nullable drag1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Supplier supplier = drag.getSupplier();
            if (supplier != null) {
                supplier.getDragList().remove(drag);
                supplier = em.merge(supplier);
            }
            Producer producer = drag.getProducer();
            if (producer != null) {
                producer.getDragList().remove(drag);
                producer = em.merge(producer);
            }
            Dragtype type = drag.getType();
            if (type != null) {
                type.getDragList().remove(drag);
                type = em.merge(type);
            }
            em.remove(drag);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Drag> findDragEntities() {
        return findDragEntities(true, -1, -1);
    }

    public List<Drag> findDragEntities(int maxResults, int firstResult) {
        return findDragEntities(false, maxResults, firstResult);
    }

    private List<Drag> findDragEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Drag.class));
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

    public Drag findDrag(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Drag.class, id);
        } finally {
            em.close();
        }
    }

    public int getDragCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Drag> rt = cq.from(Drag.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
