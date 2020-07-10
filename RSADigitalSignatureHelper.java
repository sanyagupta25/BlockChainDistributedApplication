package com.za.crypto.ds.rsa.client;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.stream.IntStream;
import javax.json.Json;
import javax.json.JsonObject;

public class RSADigitalSignatureHelper {
static BigInteger calculateD(BigInteger phi,BigInteger n, BigInteger e) { return e.modInverse(phi); } 
static BigInteger calculatePhi(BigInteger p,BigInteger q) { 
	return ((p.subtract(BigInteger.valueOf(1))).multiply(q.subtract(BigInteger.valueOf(1))));
}

static void handleGenerateKeys(BufferedReader bR,StringWriter sW,Client client) throws IOException { 
	handleInput(bR,sW,client); 
	client.setD(RSADigitalSignatureHelper.calculateD(client.getPhi(), client.getN(), client.getE()));
	System.out.print("[" + client.getName()+"]: d*"+client.getE() +" <congruent> 1 mod "
			                                               +client.getPhi()+" ==> d = "+ client.getD()+"."); 
	Json.createWriter(sW).writeObject(Json.createObjectBuilder().add("name",client.getName())
			                                                    .add("e",client.getE().toString())
			                                                    .add("n",client.getN().toString()).build());
	client.getPrintWriter().println(sW); 
	System.out.println("My public key(n,e)=("+client.getN()+","
			            +client.getE()+") | my private key d="+client.getD()); 
	if(client.getN()!=null && client.getE()!=null && client.getEncryptN()!=null && client.getEncryptE()!=null)
		System.out.println("[System]: ready to send and recieve signed transactions"); 
	
}




private static void handleInput(BufferedReader bR,StringWriter sW,Client client) throws IOException{ 
	boolean flag=true;
	BigInteger p=new BigInteger("0"),q=new BigInteger("0");
	while(flag)
	{
		try { 
			System.out.println("[System]: enter username, and 2 primes p!=q (separate w/ space)"); 
			String values[]=bR.readLine().split(" "); 
			client.setName(values[0]);
			p=new BigInteger(values[1]); 
			q=new BigInteger(values[2]);
			if(!p.equals(q) && isPrime(p) && isPrime(q)) flag=false; 
			else System.out.println("[System]: p & q must be distinct prime numbers"); 
		} 
		catch(Exception e) { System.out.println("Invalid Input"); } 
		
		}
	client.setN(p.multiply(q));
	System.out.print("["+client.getName()+"]: n=p*q = "+client.getN()); 
	client.setPhi(RSADigitalSignatureHelper.calculatePhi(p, q)); 
	System.out.println("  |  phi(n) = (p-1)*(q-1) ="+client.getPhi()); 
	while(!flag)
	{ 
		try
		{ 
			System.out.print("[System]: pick public exponent e from set{1,2,....,phi(n)-1} ");
			BigInteger input = new BigInteger(bR.readLine()); 
			if(isRelativelyPrime(input,client.getPhi()) && input.compareTo(new BigInteger ("1")) >=0
					  && input.compareTo(client.getPhi().subtract(new BigInteger("1"))) <=0) {
				client.setE(input);
				flag=true; 
			}
		}
			catch(Exception e) { System.out.println("Invalid input"); } 
		}
	
}
static void handleReceivePublicKey(JsonObject jsonObject,Client client)
{ 
	client.setEncryptE(new BigInteger(jsonObject.getString("e"))) ;
	client.setEncryptN(new BigInteger(jsonObject.getString("n"))) ;
	client.setOtherPartyname(jsonObject.getString("name"));
	System.out.println("[System]: " + client.getOtherPartyUsername()
	                             +" 's public key (n,e)=("+jsonObject.getString("n")+"," +jsonObject.getString("e")+")"); 
    if(client.getN()!=null && client.getE()!=null &&jsonObject.getString("n")!=null)
	System.out.println("[System]: ready to send and recieve signed transactions"); 
    else if(client.getN()!=null  && client.getE()==null ) System.out.println("[System]: pick public # e"); 
    else if(client.getN()==null ) System.out.println("[System]: enter username, & 2 primes p & q(separate w/ space)");
	
}

static BigInteger[] signMessage(String name,String x, BigInteger d,BigInteger n) { 
		String[] xSplit = x.split(" "); 
		BigInteger[] xPrime = new BigInteger[xSplit.length]; 
		// IntStream.range(0,s.length).forEach(i-> xPrime[i]=s[i].modPow(e, n));
		IntStream.range(0,xSplit.length).forEach(i->xPrime[i]=new BigInteger(xSplit[i]).modPow(d,n));
		System.out.println("["+name+"]: sign message w/ key # ' "+d+" ' ==> s= "+Arrays.toString(xPrime)); 
		return xPrime;
}
static void sendMessage(BufferedReader bR,Client client) throws IOException{ 
	String m=bR.readLine(); 	
	StringBuffer x=new StringBuffer(); 
	IntStream.range(0,m.length()).forEach(i-> x.append(RSADigitalSignatureHelper.characterToAscii(m.charAt(i))+ "  ")); 
		System.out.println("["+client.getName()+"]: map char to ascii message ==> x="+x); 
		boolean flag=true; 
		while(flag)
		{                             
			try { 
				System.out.println("[system]: please enter key to sign message with"); 
				BigInteger signingKey=new BigInteger(bR.readLine()); 
				//System.out.println("1");
				BigInteger push = client.getN();
				System.out.println(push.getClass().getName() + push + x.toString() + client.getName());
				BigInteger[] xPrime=signMessage(client.getName(),x.toString(),signingKey,push);
				//System.out.println("2");
				StringBuffer xPrimeSB=new StringBuffer(); 
				//System.out.println("3");
				for(int i=0;i< xPrime.length;i++) xPrimeSB.append(xPrime[i].toString()+"  "); 
				if(client.getOtherPartyUsername()!=null) {
					System.out.print("["+client.getName()+"]: send 'signed transaction' = (transaction,signature) ==> (x,s) ="
							+ " (' "+x.toString()+" ',' ");
							System.out.println(xPrimeSB.toString().trim()+" ' )"); 
					StringWriter sW=new StringWriter(); 
					Json.createWriter(sW).writeObject(Json.createObjectBuilder().add("name", client.getName())
						                                                    	.add("x",x.toString())
						                                                     	.add("s",xPrimeSB.toString()).build());
					client.getPrintWriter().println(sW); 
						}
				flag=false;
			}
			catch(Exception e) { System.out.println("invalid input"); }
		}
		
}
static boolean validateSignature(String otherPartyName,String x,BigInteger[]s,BigInteger e, BigInteger n,String name)
{ 
	System.out.println("["+name+"]:use "+otherPartyName+
			" 's public key to determine if calculated transaction x' is same as transaction x:");
	BigInteger[] xPrime=new BigInteger[s.length]; 
	IntStream.range(0,s.length).forEach(i-> xPrime[i]=s[i].modPow(e, n)); 
	System.out.println("["+name+"]: (1) calculate message ==> x' <congruent> s^e mod n ="+Arrays.toString(xPrime)+ ""); 
	StringBuffer xPrimeSB=new StringBuffer(); 
	for(int i=0;i<xPrime.length;i++) xPrimeSB.append(xPrime[i].intValue()+ "  "); 
	return (xPrimeSB.toString().trim().equals(x.trim())); 
}

static void handleRecieveMessage(JsonObject jsonObject,Client client)
{ 
	String otherPartyName=jsonObject.getString("name"); 
	System.out.println("["+client.getName()+"]: receive 'signed transaction' =(transaction,signature)==>(x,s)=(' " +
	                    jsonObject.getString("x")+ " ',' " + jsonObject.getString("s")+ " ')"); 
	String[] sValues=jsonObject.getString("s").split("  ");
	BigInteger[] s=new BigInteger[sValues.length]; 
	IntStream.range(0,sValues.length).forEach(i->s[i]=new BigInteger(sValues[i])); 
	if(RSADigitalSignatureHelper.validateSignature(otherPartyName, jsonObject.getString("x"), s, client.getEncryptE(), client.getEncryptN(), client.getName())) {
		System.out.println("["+client.getName()+ 
				            "]: (2) compare message to calculated message: x=x' ==> message was signed w/" +otherPartyName+" 's private key"); 
		String[] xValues=jsonObject.getString("x").split("  "); 
		BigInteger[] x=new BigInteger[xValues.length]; 
		IntStream.range(0,xValues.length).forEach(i->x[i]=new BigInteger(xValues[i]));
		StringBuffer m= new StringBuffer();
		for(int i=0;i<x.length;i++) m.append(RSADigitalSignatureHelper.asciiToCharacter(x[i].intValue())); 
		System.out.println("["+client.getName()+"]:"+" map ascii to char ==> "+m);
		System.out.println("["+otherPartyName+"]:"+m); 
		
	}else System.out.println("["+client.getName()+
			"]: (2)compare message to calculated message: x!=x' ==> message was not signed w/"+otherPartyName+ " 's private key");
}


static boolean isPrime(BigInteger number) { return number.isProbablePrime(1000); } 
static boolean isRelativelyPrime(BigInteger e,BigInteger phi) { return e.gcd(phi).intValue()==1; } 
static int characterToAscii(char character) {return (int)character; } 
static char asciiToCharacter(int ascii)  { return (char)ascii; } 
}
