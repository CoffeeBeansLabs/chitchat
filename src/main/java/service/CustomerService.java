package service;

import entity.Customer;
import entity.CustomerStatus;
import exception.CustomerAlreadyExistsException;

import javax.persistence.EntityManager;
import java.util.List;

public class CustomerService {

    private final EntityManager entityManager;

    public CustomerService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Customer customer) throws CustomerAlreadyExistsException {
        if(isExists(customer)){
            throw new CustomerAlreadyExistsException();
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

    public void updateStatus(int customerId, CustomerStatus status) {
        Customer customer = this.find(customerId);
        this.entityManager.getTransaction().begin();
        customer.setStatus(status);
        this.entityManager.getTransaction().commit();
    }

    public Customer find(int customerId) {
        return this.entityManager.find( Customer.class, customerId);
    }

    public void deleteAll() {
        this.entityManager.getTransaction().begin();
        this.entityManager.createQuery("delete from Customer").executeUpdate();
        this.entityManager.getTransaction().commit();
    }
}
