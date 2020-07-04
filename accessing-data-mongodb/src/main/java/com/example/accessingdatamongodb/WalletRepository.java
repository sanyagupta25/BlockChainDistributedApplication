package com.example.accessingdatamongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WalletRepository extends MongoRepository<Wallet,String>{


}
