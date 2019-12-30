package service;

import entity.Customer;
import exception.UserAlreadyExistsException;

import javax.persistence.EntityManager;

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
        Customer foundCustomer = this.entityManager.find(Customer.class, customer.getId());
        if(foundCustomer != null){
            return true;
        }
        return false;
    }
}
