package service;

import entity.Customer;
import entity.CustomerStatus;
import exception.UserAlreadyExistsException;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;
    @BeforeAll
    public static void init(){
       entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnitTest");
       entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void tearDown(){
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void isExists_shouldReturnTrue_whenGivenCustomerSavedInDatabase() throws UserAlreadyExistsException {
        CustomerService customerService = new CustomerService(entityManager);
        Customer testUser = new Customer("testUser", CustomerStatus.OFFLINE);
        customerService.save(testUser);

        assertTrue(customerService.isExists(testUser));
    }

    @Test
    public void isExists_shouldReturnFalse_whenGivenCustomerDoesNotSavedInDatabase(){
        CustomerService customerService = new CustomerService(entityManager);
        Customer testUser = new Customer("testUser", CustomerStatus.OFFLINE);

        assertFalse(customerService.isExists(testUser));
    }

    @Test
    public void save_shouldReturnUserAlreadyExistsException_whenTryToSaveAlreadyExistsCustomer() throws UserAlreadyExistsException {
        final CustomerService customerService = new CustomerService(entityManager);
        final Customer testUser2 = new Customer("testUser2", CustomerStatus.OFFLINE);

        customerService.save(testUser2);
        assertThrows(UserAlreadyExistsException.class, () -> customerService.save(testUser2));

    }
}
