package com.chatBot.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.chatBot.model.LawData;

@Repository
public interface LawRepository extends MongoRepository<LawData, String> {
}
