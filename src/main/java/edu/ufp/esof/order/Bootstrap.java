package edu.ufp.esof.order;

import edu.ufp.esof.order.models.*;
import edu.ufp.esof.order.models.builders.OrderBuilder;
import edu.ufp.esof.order.repositories.ClientRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {


    private ClientRepo clientRepo;

    @Autowired
    public Bootstrap(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.debug("Startup");

        Client client1=new Client("client1");
        client1.setPassword("12345");


        Supplier supplier1=new Supplier("supplier1");
        Supplier supplier2=new Supplier("supplier2");

        Product product1=new Product("product1",10);
        Product product2=new Product("product2",20);
        Product product3=new Product("product3",30);

        supplier1.addProduct(product1);
        supplier1.addProduct(product2);
        supplier2.addProduct(product3);
        LineOrder l1o1=new LineOrder(product1,1);
        LineOrder l2o1=new LineOrder(product2,2);

        OrderItem order1=new OrderBuilder().setOrderNumber("12345")
                .addLineOrder(l1o1)
                .addLineOrder(l2o1).build();

        LineOrder l1o2=new LineOrder(product1,1);
        LineOrder l2o2=new LineOrder(product3,2);

        OrderItem order2=new OrderBuilder().setOrderNumber("12346")
                .addLineOrder(l1o2)
                .addLineOrder(l2o2).build();


        LineOrder l1o3=new LineOrder(product2,2);
        LineOrder l2o3=new LineOrder(product3,2);

        OrderItem order3=new OrderBuilder().setOrderNumber("12347")
                .addLineOrder(l1o3)
                .addLineOrder(l2o3).build();

        client1.addOrder(order1);
        client1.addOrder(order2);
        client1.addOrder(order3);

        this.clientRepo.save(client1);
    }
}
