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
    public void fetchAllSentMessage_shouldReturnListOfSentMessageInSortingFormat_whenThereAreMessagesSentByCustomer() throws CustomerAlreadyExistsException, CustomerNotExists, SenderIsNotOnline {
        Customer sender = new Customer("testUser1", CustomerStatus.ONLINE);
        Customer receiver = new Customer("testUser2", CustomerStatus.ONLINE);
        Message message1 = new Message(sender, receiver, "Hello", MessageStatus.UNREAD);
        Message message2 = new Message(sender, receiver, "How are you?", MessageStatus.UNREAD);

        customerService.save(sender);
        customerService.save(receiver);
        messageService.save(message1);
        messageService.save(message2);

        List<Message> receivedMessages = messageService.fetchAllSentMessage(sender);

        assertEquals(message1, receivedMessages.get(0));
        assertEquals(message2, receivedMessages.get(1));
    }

    @Test
    public void fetchAllSentMessage_shouldThrowCustomerNotExistException_whenGivenSenderDoesNotExistsInDatabase() throws CustomerAlreadyExistsException, CustomerNotExists, SenderIsNotOnline {
        Customer sender = new Customer("testUser1", CustomerStatus.ONLINE);

        assertThrows(CustomerNotExists.class, () -> messageService.fetchAllSentMessage(sender));
    }

    @Test
    public void fetchAllMessages_shouldReturnEmptyList_whenThereIsNoMessagesOfGivenCustomer() throws CustomerNotExists, CustomerAlreadyExistsException {
        Customer customer = new Customer("testUser1", CustomerStatus.ONLINE);
        customerService.save(customer);

        assertTrue(messageService.fetchAllMessages(customer).isEmpty());
    }

    @Test
    public void fetchAllMessages_shouldReturListOfMessages_whenThereAreMessagesOfGivenCustomer() throws CustomerNotExists, CustomerAlreadyExistsException, SenderIsNotOnline {
        Customer testUser1 = new Customer("testUser1", CustomerStatus.ONLINE);
        Customer testUser2 = new Customer("testUser2", CustomerStatus.ONLINE);

        Message sentMessage = new Message(testUser1, testUser2, "Hello", MessageStatus.UNREAD);
        Message receivedMessage = new Message(testUser2, testUser1, "HI", MessageStatus.UNREAD);

        customerService.save(testUser1);
        customerService.save(testUser2);
        messageService.save(sentMessage);
        messageService.save(receivedMessage);

        List<Message> messages = messageService.fetchAllMessages(testUser1);

        assertEquals(2, messages.size());
        assertEquals(sentMessage, messages.get(0));
        assertEquals(receivedMessage, messages.get(1));
    }

}