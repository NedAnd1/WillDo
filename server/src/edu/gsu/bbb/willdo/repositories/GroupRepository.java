package edu.gsu.bbb.willdo.repositories;

import edu.gsu.bbb.willdo.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group,String> {
}
