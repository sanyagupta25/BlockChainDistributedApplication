package com.example.accessingdatamongodb;



import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="Books")
public class Customer {
	//@hide
	//ArrayList<Transaction> transactions2 = new ArrayList<Transaction>();
	@Id
	public String hash;
	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

//	public String getData() {
//		return data;
//	}
//
//	public void setData(String data) {
//		this.data = data;
//	}

	public String previousHash; 
	
	private long timeStamp; //as number of milliseconds since 1/1/1970.
	private int nonce;
	public String merkleRoot;

	
	public ArrayList<String> transactions = new ArrayList<String>();

	public Customer(String previousHash ) {
		
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		
		this.hash = calculateHash(); //Making sure we do this after we set the other values.
	}

	//Calculate new hash based on blocks contents
	public String calculateHash() {
		String calculatedhash = StringUtil.applySha256( 
				previousHash +
				Long.toString(timeStamp) +
				Integer.toString(nonce) 
				
				);
		return calculatedhash;
	}
	
	//Increases nonce value until hash target is reached.
//	public void mineBlock(int difficulty) {
//		merkleRoot = StringUtil.getMerkleRoot(transactions2);
//		String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0" 
//		while(!hash.substring( 0, difficulty).equals(target)) {
//			nonce ++;
//			hash = calculateHash();
//		}
//		System.out.println("Block Mined!!! : " + hash);
//	}
//	public boolean addTransaction(Transaction transaction) {
//		//process transaction and check if valid, unless block is genesis block then ignore.
//		if(transaction == null) return false;		
//		if((!"0".equals(previousHash))) {
//			if((transaction.processTransaction() != true)) {
//				System.out.println("Transaction failed to process. Discarded.");
//				return false;
//			}
//		}
//
//		transactions2.add(transaction);
//		System.out.println("Transaction Successfully added to Block");
//		return true;
//	}
}