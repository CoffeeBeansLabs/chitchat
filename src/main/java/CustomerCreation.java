import entity.Customer;
import entity.CustomerStatus;
import exception.UserAlreadyExistsException;
import service.CustomerService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CustomerCreation {
    public static void main(String[] args) throws UserAlreadyExistsException {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");
        EntityManager entityManager = factory.createEntityManager();
        CustomerService customerService = new CustomerService(entityManager);
        Customer preeti = new Customer("Preeti", CustomerStatus.OFFLINE);
        customerService.save(preeti);

        entityManager.close();
        factory.close();
    }
}
