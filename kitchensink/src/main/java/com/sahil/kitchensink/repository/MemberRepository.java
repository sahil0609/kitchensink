package com.sahil.kitchensink.repository;

import com.sahil.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {

   @Query("{ 'registeredBy' : ?0 }")
   List<Member> finaAllMMembersByRegisteredBy(String registeredBy);

    Optional<Member> findByEmail(String email);

}
