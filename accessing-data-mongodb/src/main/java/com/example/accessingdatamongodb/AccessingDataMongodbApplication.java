package com.example.accessingdatamongodb;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@SpringBootApplication
public class AccessingDataMongodbApplication implements CommandLineRunner {
	
	
	
	
	public static ArrayList<Customer> blockchain = new ArrayList<Customer>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

	public static int difficulty = 0;
	public static float minimumTransaction = 0.1f;
	
//	public static Wallet walletA;
//	public static Wallet walletB;
//	public static Transaction genesisTransaction;
//	public static Wallet coinbase;
//	public static Customer genesis;
//	//public static Transaction tr1;
//	//public static Customer block2;
//	public static Customer block3; 
//	public static  Customer block1; 
//	public static Customer block2; 


@Autowired
private CustomerRepository customer;
public static void main(String[] args) {

	SpringApplication.run(AccessingDataMongodbApplication.class, args);

	//SpringApplication.run(AccessingDataMongodbApplication.class, args);
	System.out.println(blockchain.size());
	     
}

@Override
public void run(String... args) throws Exception {
	Wallet walletA;
	Wallet walletB;
	Transaction genesisTransaction;
	Wallet coinbase;
	Customer genesis;
	//public static Transaction tr1;
	//public static Customer block2;
	Customer block3; 
	Customer block1; 
	Customer block2; 
	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	walletA = new Wallet();
	walletB = new Wallet();		
	 coinbase = new Wallet();
	
	 genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
		genesisTransaction.transactionId = "0"; //manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		
		//System.out.println("Creating and Mining Genesis block... ");
	genesis = new Customer("0");
		genesis.addTransaction(genesisTransaction);
		 customer.insert(genesis);
		addBlock(genesis);
		
		//testing
		 block1 = new Customer(genesis.hash);
		//System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		//System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		
		//System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		//System.out.println("WalletB's balance is: " + walletB.getBalance());
		addBlock(block1);
		
		 block2 = new Customer(block1.hash);
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		
		//System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		//System.out.println("WalletB's balance is: " + walletB.getBalance());
		addBlock(block2);
		block3=new Customer(block2.hash);
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
		System.out.println("\nWalletA's balance is: " + walletA.getBalance());
		System.out.println("WalletB's balance is: " + walletB.getBalance());
	//genesis=new Customer("0");
		//customer.deleteAll();
	
	 
}


	
	
	public static void addBlock(Customer newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
		
	}
	
}
