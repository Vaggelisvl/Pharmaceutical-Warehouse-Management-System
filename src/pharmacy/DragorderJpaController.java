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
public class DragorderJpaController implements Serializable {

    public DragorderJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Dragorder dragorder) {
        if (dragorder.getOrderitemsList() == null) {
            dragorder.setOrderitemsList(new ArrayList<Orderitems>());
        }
        if (dragorder.getPaymentsList() == null) {
            dragorder.setPaymentsList(new ArrayList<Payments>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Customer customer = dragorder.getCustomer();
            if (customer != null) {
                customer = em.getReference(customer.getClass(), customer.getCode());
                dragorder.setCustomer(customer);
            }
            List<Orderitems> attachedOrderitemsList = new ArrayList<Orderitems>();
            for (Orderitems orderitemsListOrderitemsToAttach : dragorder.getOrderitemsList()) {
                orderitemsListOrderitemsToAttach = em.getReference(orderitemsListOrderitemsToAttach.getClass(), orderitemsListOrderitemsToAttach.getOrderitemsPK());
                attachedOrderitemsList.add(orderitemsListOrderitemsToAttach);
            }
            dragorder.setOrderitemsList(attachedOrderitemsList);
            List<Payments> attachedPaymentsList = new ArrayList<Payments>();
            for (Payments paymentsListPaymentsToAttach : dragorder.getPaymentsList()) {
                paymentsListPaymentsToAttach = em.getReference(paymentsListPaymentsToAttach.getClass(), paymentsListPaymentsToAttach.getPaymentsPK());
                attachedPaymentsList.add(paymentsListPaymentsToAttach);
            }
            dragorder.setPaymentsList(attachedPaymentsList);
            em.persist(dragorder);
            if (customer != null) {
                customer.getDragorderList().add(dragorder);
                customer = em.merge(customer);
            }
            for (Orderitems orderitemsListOrderitems : dragorder.getOrderitemsList()) {
                Dragorder oldDragorder1OfOrderitemsListOrderitems = orderitemsListOrderitems.getDragorder1();
                orderitemsListOrderitems.setDragorder1(dragorder);
                orderitemsListOrderitems = em.merge(orderitemsListOrderitems);
                if (oldDragorder1OfOrderitemsListOrderitems != null) {
                    oldDragorder1OfOrderitemsListOrderitems.getOrderitemsList().remove(orderitemsListOrderitems);
                    oldDragorder1OfOrderitemsListOrderitems = em.merge(oldDragorder1OfOrderitemsListOrderitems);
                }
            }
            for (Payments paymentsListPayments : dragorder.getPaymentsList()) {
                Dragorder oldDragorder1OfPaymentsListPayments = paymentsListPayments.getDragorder1();
                paymentsListPayments.setDragorder1(dragorder);
                paymentsListPayments = em.merge(paymentsListPayments);
                if (oldDragorder1OfPaymentsListPayments != null) {
                    oldDragorder1OfPaymentsListPayments.getPaymentsList().remove(paymentsListPayments);
                    oldDragorder1OfPaymentsListPayments = em.merge(oldDragorder1OfPaymentsListPayments);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dragorder dragorder) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dragorder persistentDragorder = em.find(Dragorder.class, dragorder.getCode());
            Customer customerOld = persistentDragorder.getCustomer();
            Customer customerNew = dragorder.getCustomer();
            List<Orderitems> orderitemsListOld = persistentDragorder.getOrderitemsList();
            List<Orderitems> orderitemsListNew = dragorder.getOrderitemsList();
            List<Payments> paymentsListOld = persistentDragorder.getPaymentsList();
            List<Payments> paymentsListNew = dragorder.getPaymentsList();
            List<String> illegalOrphanMessages = null;
            for (Orderitems orderitemsListOldOrderitems : orderitemsListOld) {
                if (!orderitemsListNew.contains(orderitemsListOldOrderitems)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Orderitems " + orderitemsListOldOrderitems + " since its dragorder1 field is not nullable.");
                }
            }
            for (Payments paymentsListOldPayments : paymentsListOld) {
                if (!paymentsListNew.contains(paymentsListOldPayments)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Payments " + paymentsListOldPayments + " since its dragorder1 field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (customerNew != null) {
                customerNew = em.getReference(customerNew.getClass(), customerNew.getCode());
                dragorder.setCustomer(customerNew);
            }
            List<Orderitems> attachedOrderitemsListNew = new ArrayList<Orderitems>();
            for (Orderitems orderitemsListNewOrderitemsToAttach : orderitemsListNew) {
                orderitemsListNewOrderitemsToAttach = em.getReference(orderitemsListNewOrderitemsToAttach.getClass(), orderitemsListNewOrderitemsToAttach.getOrderitemsPK());
                attachedOrderitemsListNew.add(orderitemsListNewOrderitemsToAttach);
            }
            orderitemsListNew = attachedOrderitemsListNew;
            dragorder.setOrderitemsList(orderitemsListNew);
            List<Payments> attachedPaymentsListNew = new ArrayList<Payments>();
            for (Payments paymentsListNewPaymentsToAttach : paymentsListNew) {
                paymentsListNewPaymentsToAttach = em.getReference(paymentsListNewPaymentsToAttach.getClass(), paymentsListNewPaymentsToAttach.getPaymentsPK());
                attachedPaymentsListNew.add(paymentsListNewPaymentsToAttach);
            }
            paymentsListNew = attachedPaymentsListNew;
            dragorder.setPaymentsList(paymentsListNew);
            dragorder = em.merge(dragorder);
            if (customerOld != null && !customerOld.equals(customerNew)) {
                customerOld.getDragorderList().remove(dragorder);
                customerOld = em.merge(customerOld);
            }
            if (customerNew != null && !customerNew.equals(customerOld)) {
                customerNew.getDragorderList().add(dragorder);
                customerNew = em.merge(customerNew);
            }
            for (Orderitems orderitemsListNewOrderitems : orderitemsListNew) {
                if (!orderitemsListOld.contains(orderitemsListNewOrderitems)) {
                    Dragorder oldDragorder1OfOrderitemsListNewOrderitems = orderitemsListNewOrderitems.getDragorder1();
                    orderitemsListNewOrderitems.setDragorder1(dragorder);
                    orderitemsListNewOrderitems = em.merge(orderitemsListNewOrderitems);
                    if (oldDragorder1OfOrderitemsListNewOrderitems != null && !oldDragorder1OfOrderitemsListNewOrderitems.equals(dragorder)) {
                        oldDragorder1OfOrderitemsListNewOrderitems.getOrderitemsList().remove(orderitemsListNewOrderitems);
                        oldDragorder1OfOrderitemsListNewOrderitems = em.merge(oldDragorder1OfOrderitemsListNewOrderitems);
                    }
                }
            }
            for (Payments paymentsListNewPayments : paymentsListNew) {
                if (!paymentsListOld.contains(paymentsListNewPayments)) {
                    Dragorder oldDragorder1OfPaymentsListNewPayments = paymentsListNewPayments.getDragorder1();
                    paymentsListNewPayments.setDragorder1(dragorder);
                    paymentsListNewPayments = em.merge(paymentsListNewPayments);
                    if (oldDragorder1OfPaymentsListNewPayments != null && !oldDragorder1OfPaymentsListNewPayments.equals(dragorder)) {
                        oldDragorder1OfPaymentsListNewPayments.getPaymentsList().remove(paymentsListNewPayments);
                        oldDragorder1OfPaymentsListNewPayments = em.merge(oldDragorder1OfPaymentsListNewPayments);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dragorder.getCode();
                if (findDragorder(id) == null) {
                    throw new NonexistentEntityException("The dragorder with id " + id + " no longer exists.");
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
            Dragorder dragorder;
            try {
                dragorder = em.getReference(Dragorder.class, id);
                dragorder.getCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dragorder with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Orderitems> orderitemsListOrphanCheck = dragorder.getOrderitemsList();
            for (Orderitems orderitemsListOrphanCheckOrderitems : orderitemsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dragorder (" + dragorder + ") cannot be destroyed since the Orderitems " + orderitemsListOrphanCheckOrderitems + " in its orderitemsList field has a non-nullable dragorder1 field.");
            }
            List<Payments> paymentsListOrphanCheck = dragorder.getPaymentsList();
            for (Payments paymentsListOrphanCheckPayments : paymentsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Dragorder (" + dragorder + ") cannot be destroyed since the Payments " + paymentsListOrphanCheckPayments + " in its paymentsList field has a non-nullable dragorder1 field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Customer customer = dragorder.getCustomer();
            if (customer != null) {
                customer.getDragorderList().remove(dragorder);
                customer = em.merge(customer);
            }
            em.remove(dragorder);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dragorder> findDragorderEntities() {
        return findDragorderEntities(true, -1, -1);
    }

    public List<Dragorder> findDragorderEntities(int maxResults, int firstResult) {
        return findDragorderEntities(false, maxResults, firstResult);
    }

    private List<Dragorder> findDragorderEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dragorder.class));
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

    public Dragorder findDragorder(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dragorder.class, id);
        } finally {
            em.close();
        }
    }

    public int getDragorderCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dragorder> rt = cq.from(Dragorder.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
