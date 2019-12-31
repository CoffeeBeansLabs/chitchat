package service;

import entity.Customer;
import entity.CustomerStatus;
import exception.CustomerAlreadyExistsException;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {
    private static CustomerService customerService;
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnitTest");
        entityManager = entityManagerFactory.createEntityManager();
        customerService = new CustomerService(entityManager);
    }

    @AfterEach
    public void deleteAllRow(){
        customerService.deleteAll();
    }

    @AfterAll
    public static void tearDown(){
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void isExists_shouldReturnTrue_whenGivenCustomerSavedInDatabase() throws CustomerAlreadyExistsException {
        Customer testUser = new Customer("testUser", CustomerStatus.OFFLINE);
        customerService.save(testUser);

        assertTrue(customerService.isExists(testUser));
    }

    @Test
    public void isExists_shouldReturnFalse_whenGivenCustomerDoesNotSavedInDatabase() {
        Customer testUser = new Customer("testUser", CustomerStatus.OFFLINE);

        assertFalse(customerService.isExists(testUser));
    }

    @Test
    public void save_shouldReturnCustomerAlreadyExistsException_whenTryToSaveAlreadyExistsCustomer() throws CustomerAlreadyExistsException {
        final Customer testUser2 = new Customer("testUser2", CustomerStatus.OFFLINE);

        customerService.save(testUser2);
        assertThrows(CustomerAlreadyExistsException.class, () -> customerService.save(testUser2));
    }

    @Test
    public void findAll_shouldReturnOneCustomer_whenDatabaseHasOneCustomerSaved() throws CustomerAlreadyExistsException {
        Customer testUser = new Customer("testUser", CustomerStatus.OFFLINE);
        customerService.save(testUser);
        assertEquals(1, customerService.findAll().size());
    }

    @Test
    public void findAll_shouldReturnTwoCustomers_whenDatabaseHasTwoCustomersSaved() throws CustomerAlreadyExistsException {
        Customer testUser = new Customer("testUser", CustomerStatus.OFFLINE);
        Customer testUser2 = new Customer("testUser2", CustomerStatus.ONLINE);
        customerService.save(testUser);
        customerService.save(testUser2);

        assertEquals(2, customerService.findAll().size());
    }

    @Test
    public void update_shouldChangeCustomerStatusToOnline_whenGivenCustomerIsOffline() throws CustomerAlreadyExistsException {
        Customer testUser = new Customer("testUser", CustomerStatus.OFFLINE);
        customerService.save(testUser);

        customerService.updateStatus(testUser.getId(), CustomerStatus.ONLINE);
        Customer updatedCustomer = customerService.find(testUser.getId());

        assertEquals(CustomerStatus.ONLINE, updatedCustomer.getStatus());
    }

    @Test
    public void isOffline_shouldReturnTrue_whenGivenCustomerIsOffline(){
        Customer testUser = new Customer("testUser", CustomerStatus.OFFLINE);
        assertTrue(testUser.isOffline());
    }

    @Test
    public void isOffline_shouldReturnFalse_whenGivenCustomerIsOnline(){
        Customer testUser = new Customer("testUser", CustomerStatus.ONLINE);
        assertFalse(testUser.isOffline());
    }

}
