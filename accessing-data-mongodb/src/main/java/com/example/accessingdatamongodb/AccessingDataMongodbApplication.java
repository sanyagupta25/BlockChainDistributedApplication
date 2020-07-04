package com.example.accessingdatamongodb;

import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@SpringBootApplication
public class AccessingDataMongodbApplication implements CommandLineRunner {
	
	
	
	
	public static ArrayList<Customer> blockchain = new ArrayList<Customer>();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();
	//public static ArrayList<Customer> b = new ArrayList<Customer>();

	public static int difficulty = 0;
	public static float minimumTransaction = 0.1f;
	
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;
	public static Wallet coinbase;
	public static Customer genesis;
	public static Customer block3; 
	public static  Customer block1; 
	public static Customer block2; 
	public static Person person;


@Autowired
private CustomerRepository customer;
@Autowired
private WalletRepository wallet;
@Autowired
private PersonRepository per;
public static void main(String[] args) {

//	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//	

	
	 walletA = new Wallet();
		walletB = new Wallet();		
		 coinbase = new Wallet();
		 TransactionOutput t2=new TransactionOutput(walletB.publicKey,100f,"mksmk");
		 t2.id="1";
		 genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
			genesisTransaction.generateSignature(coinbase.privateKey);	 //manually sign the genesis transaction	
			genesisTransaction.transactionId = "0"; //manually set the transaction id
			genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //manually add the Transactions Output
			UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //its important to store our first transaction in the UTXOs list.
		    genesis = new Customer("0");
		   
			//genesis.addTransaction(genesisTransaction);
			genesis.transactions.add(genesisTransaction.toString());
			//genesis.addTransaction(genesisTransaction);
			blockchain.add(genesis);
			//Transaction t=walletA.sendFunds(walletB.publicKey, 40f);
			//String s=t.toString();
			//genesis.transactions.add(s);
		walletA.UTXOs.put("1", t2);
		
//			//testing
			 block1 = new Customer(genesis.hash);
			//System.out.println("\nWalletA's balance is: " + walletA.getBalance());
			//System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
			//block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
			//block1.transactions.add(s);
			//System.out.println("\nWalletA's balance is: " + walletA.getBalance());
			//System.out.println("WalletB's balance is: " + walletB.getBalance());
			addBlock(block1);
			
			 block2 = new Customer(block1.hash);
			System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
			//block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
			
			//System.out.println("\nWalletA's balance is: " + walletA.getBalance());
			//System.out.println("WalletB's balance is: " + walletB.getBalance());
			addBlock(block2);
			block3=new Customer(block2.hash);
			System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
			//block3.addTransaction(walletB.sendFunds( walletA.publicKey, 20));
			//System.out.println("\nWalletA's balance is: " + walletA.getBalance());
			//System.out.println("WalletB's balance is: " + walletB.getBalance());
			//person=new Person();
		//	person.name="abcd";
			//person.age="9";
			//person.UTXOs.put("1", "kjhkjh");
			//person.UTXOs.put("2", "hello");
		 SpringApplication.run(AccessingDataMongodbApplication.class, args);
		
	     
}

@Override
public void run(String... args) throws Exception {

	
	//p.deleteAll();
	System.out.println(genesisTransaction.transactionId);
	per.deleteAll();
	//customer.deleteAll();
	System.out.println(walletA.privateKey);
	System.out.println(walletA.publicKey);
	System.out.println(walletB.privateKey);
	System.out.println(walletB.publicKey);
	System.out.println(StringUtil.getStringFromKey(walletB.privateKey));
	System.out.println(StringUtil.getStringFromKey(walletB.publicKey));
	System.out.println(StringUtil.getStringFromKey(walletA.privateKey));
	System.out.println(StringUtil.getStringFromKey(walletA.publicKey));
	Person p=new Person();
	Person p2=new Person();
	p.privatekey=StringUtil.getStringFromKey(walletB.privateKey);
	p.publickey=StringUtil.getStringFromKey(walletB.publicKey);
	p.convert(walletA.UTXOs);
	per.save(p);
	//System.out.println(walletA.toStringprivate());
//	PublicKey key = walletA.publicKey;
//	byte[] byte_pubkey = key.getEncoded();
//	String keyString = new String(Base64.getEncoder().encode(byte_pubkey));
//	System.out.println(keyString);
	
	//p.save(person);
//	Person p=new Person();
//	p.privatekey=walletA.publicKey.;
//	p.publickey=walletA.privateKey.toString();
	//p.convert(walletA.UTXOs);
	//per.save(p);
	//wallet.save(walletA);
	//wallet.insert(walletA);
	//wallet.save(genesisTransaction);
	//walletrepo.saveAll(UTXOs);
	customer.save(genesis);
	//customer.deleteById("3c398fbfb8df08285e2f41fd4c988059fd905d62a017b0114062fc9d3f349895");
//Optional<Customer> c=customer.findById("b805fcbf065c69ffaf39fbb736c8551a1fa60ad707c67a5adc0f5c556c1b5de8")	;
// Customer c1=c.get();
// System.out.println(c1.transactions.get(0));
//	ArrayList<Customer>a=(ArrayList<Customer>) customer.findAll();
//	for(int i=0;i<a.size();i++) {
//		System.out.println(a.get(i).hash);
//		System.out.println(a.get(i).previousHash);
//		System.out.println(a.get(i).transactions.get(0));
//	}
}


	
	
	public static void addBlock(Customer newBlock) {
	
	//newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
		
	}
	
}
