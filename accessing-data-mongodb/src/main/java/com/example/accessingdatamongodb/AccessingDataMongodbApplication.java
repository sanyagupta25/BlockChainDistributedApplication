package com.example.accessingdatamongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccessingDataMongodbApplication implements CommandLineRunner {
@Autowired
private CustomerRepository customer;
	
	public static void main(String[] args) {
		SpringApplication.run(AccessingDataMongodbApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		
		customer.deleteAll();
		// TODO Auto-generated method stub
		Customer c1=new Customer("message1","0");
		Customer c2=new Customer("message2",c1.hash);
		Customer c3=new Customer("message3",c2.hash);
		customer.save(c1);
		customer.save(c2);
		customer.save(c3);
		System.out.println("**************************");
		List<Customer>AllCustomer=customer.findAll();
		for(Customer c:AllCustomer) {
		System.out.println(c.toString());	
		}
		
	}

}
