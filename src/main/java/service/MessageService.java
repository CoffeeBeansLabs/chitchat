package service;

import entity.Customer;
import entity.Message;
import exception.CustomerNotExists;
import exception.SenderIsNotOnline;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class MessageService {
    private final EntityManager entityManager;
    private final CustomerService customerService;

    public MessageService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.customerService = new CustomerService(entityManager);
    }

    public void save(Message message) throws CustomerNotExists, SenderIsNotOnline {
        if(!areSenderAndReceiverExists(message)){
            throw new CustomerNotExists();
        }
        if(isSenderOffline(message.getSender())){
            throw new SenderIsNotOnline();
        }
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(message);
        this.entityManager.getTransaction().commit();
    }

    private boolean isSenderOffline(Customer sender) {
        return sender.isOffline();
    }

    private boolean areSenderAndReceiverExists(Message message) {
        return customerService.isExists(message.getSender()) && customerService.isExists(message.getReceiver());
    }

    public void deleteAll() {
        this.entityManager.getTransaction().begin();
        this.entityManager.createQuery("delete from Message").executeUpdate();
        this.entityManager.getTransaction().commit();
    }

    public List<Message> fetchAllReceivedMessage(Customer receiver) throws CustomerNotExists {
        if(!customerService.isExists(receiver)){
            throw new CustomerNotExists();
        }
        Query query = this.entityManager.createQuery("SELECT message from Message message where message.receiver=:receiver");
        query.setParameter("receiver", receiver);
        return query.getResultList();
    }

    public List<Message> fetchAllSentMessage(Customer sender) throws CustomerNotExists {
        if(!customerService.isExists(sender)){
            throw new CustomerNotExists();
        }
        Query query = this.entityManager.createQuery("SELECT message from Message message where message.sender=:sender");
        query.setParameter("sender", sender);
        return query.getResultList();
    }
}
