package com.example.accessingdatamongodb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="walletoutput")
public class Person {
	@Id
public String publickey;
public String privatekey;
public HashMap<String,String> UTXOs = new HashMap<String,String>();

public void convert(HashMap<String,TransactionOutput> utxo){
	
	Iterator hmIterator = utxo.entrySet().iterator(); 
	 while (hmIterator.hasNext()) { 
         Map.Entry mapElement = (Map.Entry)hmIterator.next(); 
        String k=(String) mapElement.getKey();
        TransactionOutput t=(TransactionOutput) mapElement.getValue();
        UTXOs.put(k, t.toString());
     } 
	
}
}
