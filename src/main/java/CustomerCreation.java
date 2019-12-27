import entities.Customer;
import entities.CustomerStatus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CustomerCreation {
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("NewPersistenceUnit");
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();

        Customer preeti = new Customer("Preeti", CustomerStatus.OFFLINE);
        entityManager.persist(preeti);
        entityManager.getTransaction().commit();
        entityManager.close();
        factory.close();
    }
}
