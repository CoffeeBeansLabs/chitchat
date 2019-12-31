import entity.Customer;
import entity.CustomerStatus;
import entity.Message;
import entity.MessageStatus;
import exception.CustomerAlreadyExistsException;
import service.CustomerService;
import service.MessageService;
import exception.SenderIsNotOnline;
import exception.CustomerNotExists;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CustomerCreation {
    public static void main(String[] args) throws CustomerAlreadyExistsException, CustomerNotExists, SenderIsNotOnline {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");
        EntityManager entityManager = factory.createEntityManager();
        CustomerService customerService = new CustomerService(entityManager);
        Customer preeti = new Customer("Preeti", CustomerStatus.OFFLINE);
        customerService.save(preeti);
        Customer monica = new Customer("Monica", CustomerStatus.OFFLINE);
        customerService.save(monica);

        MessageService messageService = new MessageService(entityManager);
        Message message = new Message(preeti,monica, "hello how are you", MessageStatus.READ);
        messageService.save(message);

        Message reply = new Message(monica,preeti, "Hi i am good", MessageStatus.UNREAD);
        messageService.save(reply);

        entityManager.close();
        factory.close();
    }
}
