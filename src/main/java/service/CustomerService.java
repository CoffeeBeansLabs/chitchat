package service;

import entity.Customer;
import exception.UserAlreadyExistsException;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomerService {

    private final EntityManager entityManager;

    public CustomerService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Customer customer) throws UserAlreadyExistsException {
        if(isExists(customer)){
            throw new UserAlreadyExistsException();
        }
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(customer);
        this.entityManager.getTransaction().commit();
    }

    public boolean isExists(Customer customer) {
        return this.entityManager.contains(customer);
    }

    public List<Customer> findAll() {
        return this.entityManager.createQuery("Select c from Customer c", Customer.class).getResultList();
    }
}
