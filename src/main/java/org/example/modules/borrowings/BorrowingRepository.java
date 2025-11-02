package org.example.modules.borrowings;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.models.Borrowing;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BorrowingRepository {
    private final SessionFactory sessionFactory;

    public BorrowingRepository(SessionFactory sessionFactory) {
        this .sessionFactory = sessionFactory;
    }

    public Borrowing save(Borrowing borrowing) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(borrowing);
            transaction.commit();
            return borrowing;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }

    public Borrowing update(Borrowing borrowing) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(borrowing);
            transaction.commit();
            return borrowing;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }

    public Borrowing delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Borrowing borrowing = session.get(Borrowing.class, id);
            if (borrowing != null) {
                session.remove(borrowing);
            }
            transaction.commit();
            return borrowing;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return null;
        }
    }

    public Borrowing findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Borrowing.class, id);
        }
    }

    public List<Borrowing> findAll() {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Borrowing> cq = cb.createQuery(Borrowing.class);
            Root<Borrowing> rootEntry = cq.from(Borrowing.class);
            CriteriaQuery<Borrowing> all = cq.select(rootEntry);

            TypedQuery<Borrowing> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        }
    }
}
