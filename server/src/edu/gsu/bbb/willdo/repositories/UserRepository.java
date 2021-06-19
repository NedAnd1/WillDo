package edu.gsu.bbb.willdo.repositories;


import edu.gsu.bbb.willdo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByEmail(String email); //Allows for query of users by email
	boolean existsByEmail(String email);
}
