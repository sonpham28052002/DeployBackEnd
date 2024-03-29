package vn.edu.iuh.fit.chat_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.chat_backend.models.Account;
import vn.edu.iuh.fit.chat_backend.repositories.AccountRepository;

import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    
    public Account getAccountByPhoneAndPassword(String phone , String password){
        return accountRepository.getAccountByPhoneAndPassword(phone, password);
    }
    
    public boolean updatePasswordAccount(String phone , String passwordOld,String passwordNew ){
        Account account = accountRepository.getAccountByPhoneAndPassword(phone,passwordOld);
        if (account == null)
        {
            return false;
        }
        account.setPassword(passwordNew);
        return accountRepository.save(account).getId() != null;
    }

    public Account getAccountByPhone(String phone){
        return accountRepository.getAccountByPhone(phone);
    }

    public boolean forgotPasswordAccount(String id, String passwordNew) {
        Optional<Account> account = accountRepository.findById(id);
        account.get().setPassword(passwordNew);
        return accountRepository.save(account.get())!=null;
    }

    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }
}
