package vn.edu.iuh.fit.chat_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.edu.iuh.fit.chat_backend.models.User;

public interface UserRepository extends MongoRepository<User,String> {
}
