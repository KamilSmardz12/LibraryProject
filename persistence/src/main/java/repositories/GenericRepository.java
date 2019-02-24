package repositories;

import hibernate.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class GenericRepository<T, K> {

    protected final Session session;
    protected final Class<T> entityClass;
    //protected final String className;

    @SuppressWarnings("unchecked")
    protected GenericRepository() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass.getActualTypeArguments()[0];
        session = HibernateConnection.getSession();
        //String name = entityClass.getName();
        //String[] onlyClassName = name.split(".");
      //  className = onlyClassName[1];
    }

    public void create(T entity) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public T read(K id) {
        return session.find(entityClass, id);
    }

    public void update(T entity) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }


    public void delete(T entity) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void deleteById(K id) {
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            T entityToRemove = read(id);
            session.remove(entityToRemove);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public List<T> findAll() {
        Transaction transaction = null;
        List<T> items = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            items = (List<T>) session.createQuery("from "+ entityClass.getName()).list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
                e.getMessage();
            }
        }
        return items;
    }

}
