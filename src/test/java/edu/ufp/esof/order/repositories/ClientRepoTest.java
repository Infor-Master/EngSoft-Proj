package edu.ufp.esof.order.repositories;

import edu.ufp.esof.order.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClientRepoTest {

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private LineOrderRepo lineOrderRepo;

    @Autowired
    private ProductRepo productRepo;

     @Autowired
     private SupplierRepo supplierRepo;


    @Test
    public void test(){
        Client client1=new Client("client1");
        OrderItem order1=new OrderItem("12345");

        Supplier supplier1=new Supplier("supplier1");
        Supplier supplier2=new Supplier("supplier2");

        Product product1=new Product("product1",10);
        Product product2=new Product("product2",20);
        Product product3=new Product("product3",30);

        supplier1.addProduct(product1);
        supplier1.addProduct(product2);
        supplier2.addProduct(product3);
        LineOrder l1o1=new LineOrder(product1,1);
        order1.addLineOrder(l1o1);
        LineOrder l2o1=new LineOrder(product2,2);
        order1.addLineOrder(l2o1);


        OrderItem order2=new OrderItem("12346");

        LineOrder l1o2=new LineOrder(product1,1);
        order2.addLineOrder(l1o2);
        LineOrder l2o2=new LineOrder(product3,2);
        order2.addLineOrder(l2o2);


        OrderItem order3=new OrderItem("12347");

        LineOrder l1o3=new LineOrder(product2,2);
        order3.addLineOrder(l1o3);
        LineOrder l2o3=new LineOrder(product3,2);
        order3.addLineOrder(l2o3);

        client1.addOrder(order1);
        client1.addOrder(order2);
        client1.addOrder(order3);

        this.clientRepo.save(client1);

        assertEquals(1,this.clientRepo.count());
        assertEquals(3,this.orderRepo.count());
        assertEquals(6,this.lineOrderRepo.count());
        assertEquals(3,this.productRepo.count());
        assertEquals(2,this.supplierRepo.count());
    }

}