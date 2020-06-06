package com.example.accessingdatamongodb;





import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String>{


	
	
	
}
