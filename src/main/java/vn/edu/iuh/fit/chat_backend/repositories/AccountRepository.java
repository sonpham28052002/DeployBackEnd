package vn.edu.iuh.fit.chat_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import vn.edu.iuh.fit.chat_backend.models.Account;

public interface AccountRepository extends MongoRepository<Account,String> {
    public Account getAccountByPhoneAndPassword(String phone , String password);
    public Account getAccountByPhone(String phone);
}
