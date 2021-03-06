package com.qualle.trip.repository.impl;

import com.qualle.trip.model.entity.Trip;
import com.qualle.trip.repository.TripDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class TripDaoImpl implements TripDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Trip> getAll() {
        Query query = entityManager.createQuery("SELECT t FROM Trip t", Trip.class);
        return query.getResultList();
    }

    @Override
    public List<Trip> getByEmployee(long employeeId) {
        Query query = entityManager.createQuery("SELECT t FROM Trip t INNER JOIN t.members m JOIN m.employee e WHERE e.id = :employeeId", Trip.class);
        query.setParameter("employeeId", employeeId);
        return query.getResultList();
    }

    @Override
    public List<Trip> getByTitle(String title) {
        Query query = entityManager.createQuery("SELECT t FROM Trip t WHERE lower(t.title) like lower(concat('%', :title,'%'))", Trip.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public Trip getById(long id) {
        return entityManager.find(Trip.class, id);
    }

    @Override
    public Trip getFullById(long id) {
        Query query = entityManager.createQuery(
                "SELECT t FROM Trip t " +
                        "LEFT JOIN FETCH t.members m " +
                        "LEFT JOIN FETCH m.employee " +
                        "LEFT JOIN FETCH m.memberAllowances a " +
                        "LEFT JOIN FETCH a.allowance " +
                        "LEFT JOIN FETCH m.tickets " +
                        "WHERE t.id = :id", Trip.class);
        query.setParameter("id", id);
        return (Trip) query.getSingleResult();
    }

    @Override
    public void add(Trip trip) {
        entityManager.persist(trip);
    }

    @Override
    public void update(Trip trip) {
        entityManager.merge(trip);
    }

    @Override
    public void delete(long id) {
        Query query = entityManager.createQuery("DELETE FROM Trip t WHERE t.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
