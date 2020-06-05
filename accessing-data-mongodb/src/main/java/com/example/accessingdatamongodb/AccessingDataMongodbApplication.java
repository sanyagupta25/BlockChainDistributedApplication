package com.example.accessingdatamongodb;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccessingDataMongodbApplication implements CommandLineRunner {
	
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	
	public static int difficulty = 3;
	public static float minimumTransaction = 0.1f;
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;

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
		Customer c4=new Customer("message4",c3.hash);
		customer.save(c1);
		customer.save(c2);
		customer.save(c3);
		customer.save(c4);
		System.out.println("**************************");
		List<Customer>AllCustomer=customer.findAll();
		for(Customer c:AllCustomer) {
		System.out.println(c.toString());	
		}
		
	}

}
