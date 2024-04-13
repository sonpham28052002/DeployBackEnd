package vn.edu.iuh.fit.chat_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.edu.iuh.fit.chat_backend.models.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String> {
    public Optional<User> getUserByPhone(String phone);
}
