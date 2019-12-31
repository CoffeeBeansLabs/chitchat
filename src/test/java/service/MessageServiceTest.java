package service;

import entity.Customer;
import entity.CustomerStatus;
import entity.Message;
import entity.MessageStatus;
import exception.CustomerNotExists;
import exception.SenderIsNotOnline;
import exception.CustomerAlreadyExistsException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MessageServiceTest {
    private static MessageService messageService;
    private static CustomerService customerService;
    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    @BeforeAll
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("PersistenceUnitTest");
        entityManager = entityManagerFactory.createEntityManager();
        messageService = new MessageService(entityManager);
        customerService = new CustomerService(entityManager);
    }

    @AfterEach
    public void deleteAllRow() {
        messageService.deleteAll();
        customerService.deleteAll();
    }

    @AfterAll
    public static void tearDown() {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Test
    public void addMessage_shouldAddNewMessage_whenSenderAndReceiverExistsInDatabase() throws CustomerAlreadyExistsException, CustomerNotExists, SenderIsNotOnline {
        Customer sender = new Customer("testUser1", CustomerStatus.ONLINE);
        Customer receiver = new Customer("testUser2", CustomerStatus.ONLINE);
        customerService.save(sender);
        customerService.save(receiver);

        Message message = new Message(sender, receiver, "Hello", MessageStatus.UNREAD);

        messageService.save(message);
    }

    @Test
    public void addMessage_shouldThrowCustomerNotExistsException_whenSenderDoesNotExistInDatabase() throws CustomerAlreadyExistsException {
        Customer sender = new Customer("testUser1", CustomerStatus.ONLINE);
        Customer receiver = new Customer("testUser2", CustomerStatus.ONLINE);

        customerService.save(receiver);

        Message message = new Message(sender, receiver, "Hello", MessageStatus.UNREAD);
        assertThrows(CustomerNotExists.class, () -> messageService.save(message));
    }

    @Test
    public void addMessage_shouldThrowCustomerNotExistsException_whenReceiverDoesNotExistInDatabase() throws CustomerAlreadyExistsException {
        Customer sender = new Customer("testUser1", CustomerStatus.ONLINE);
        Customer receiver = new Customer("testUser2", CustomerStatus.ONLINE);

        customerService.save(sender);

        Message message = new Message(sender, receiver, "Hello", MessageStatus.UNREAD);
        assertThrows(CustomerNotExists.class, () -> messageService.save(message));
    }

    @Test
    public void addMessage_shouldThrowSenderIsNotOnlineException_whenSenderIsOffline() throws CustomerAlreadyExistsException {
        Customer sender = new Customer("testUser1", CustomerStatus.OFFLINE);
        Customer receiver = new Customer("testUser2", CustomerStatus.ONLINE);

        customerService.save(sender);
        customerService.save(receiver);

        Message message = new Message(sender, receiver, "Hello", MessageStatus.UNREAD);
        assertThrows(SenderIsNotOnline.class, () -> messageService.save(message));
    }

    @Test
    public void fetchAllReceivedMessage_shouldReturnEmptyList_whenThereIsNoMessageForGivenCustomer() throws CustomerAlreadyExistsException, CustomerNotExists {
        Customer testUser1 = new Customer("testUser1", CustomerStatus.ONLINE);
        customerService.save(testUser1);

        List<Message> receivedMessages = messageService.fetchAllReceivedMessage(testUser1);

        assertTrue(receivedMessages.isEmpty());
    }

    @Test
    public void fetchAllReceivedMessage_shouldReturnListOfReceivedMessages_whenThereIsAMessageForGivenCustomer() throws CustomerAlreadyExistsException, CustomerNotExists, SenderIsNotOnline {
        Customer sender = new Customer("testUser1", CustomerStatus.ONLINE);
        Customer receiver = new Customer("testUser2", CustomerStatus.ONLINE);
        Message message = new Message(sender, receiver, "Hello", MessageStatus.UNREAD);

        customerService.save(sender);
        customerService.save(receiver);
        messageService.save(message);

        List<Message> receivedMessages = messageService.fetchAllReceivedMessage(receiver);

        assertEquals(message, receivedMessages.get(0));
    }

    @Test
    public void fetchAllReceivedMessage_shouldThrowCustomerNotExistException_whenGivenReceiverDoesNotExistsInDatabase() throws CustomerAlreadyExistsException, CustomerNotExists, SenderIsNotOnline {
        Customer receiver = new Customer("testUser2", CustomerStatus.ONLINE);

        assertThrows(CustomerNotExists.class, () -> messageService.fetchAllReceivedMessage(receiver));
    }

    @Test
    public void fetchAllSentMessage_shouldReturnListOfSentMessage_whenThereIsAMessageSentByCustomer() throws CustomerAlreadyExistsException, CustomerNotExists, SenderIsNotOnline {
        Customer sender = new Customer("testUser1", CustomerStatus.ONLINE);
        Customer receiver = new Customer("testUser2", CustomerStatus.ONLINE);
        Message message = new Message(sender, receiver, "Hello", MessageStatus.UNREAD);

        customerService.save(sender);
        customerService.save(receiver);
        messageService.save(message);

        List<Message> receivedMessages = messageService.fetchAllSentMessage(sender);

        assertEquals(message, receivedMessages.get(0));
    }

    @Test
    public void fetchAllSentMessage_shouldThrowCustomerNotExistException_whenGivenSenderDoesNotExistsInDatabase() throws CustomerAlreadyExistsException, CustomerNotExists, SenderIsNotOnline {
        Customer sender = new Customer("testUser1", CustomerStatus.ONLINE);

        assertThrows(CustomerNotExists.class, () -> messageService.fetchAllSentMessage(sender));
    }

}
