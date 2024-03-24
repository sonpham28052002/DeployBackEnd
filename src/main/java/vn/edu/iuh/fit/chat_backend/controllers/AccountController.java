package vn.edu.iuh.fit.chat_backend.controllers;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.chat_backend.models.Account;
import vn.edu.iuh.fit.chat_backend.repositories.AccountRepository;
import vn.edu.iuh.fit.chat_backend.services.AccountService;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @PutMapping("/updatePasswordAccount")
    public boolean updatePasswordAccount(@RequestParam String phone, @RequestParam String passwordOld, @RequestParam String passwordNew) {
        return accountService.updatePasswordAccount(phone, passwordOld, passwordNew);
    }

    @GetMapping("/getAccountPhoneAndPassword")
    public Account getAccountByPhoneAndPassword(@RequestParam String phone, @RequestParam String password) {
        return accountService.getAccountByPhoneAndPassword(phone, password);
    }

    @GetMapping("/getAccountByPhone")
    public Account getAccountByPhone(@RequestParam String phone) {
        System.out.println(phone);
        System.out.println(accountService.getAccountByPhone(phone.trim()));
        return accountService.getAccountByPhone(phone);
    }

    @GetMapping("/all")
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @PostMapping("/registerAccount")
    public Account registerAccount(@RequestBody Account account) {
        return accountRepository.save(account);
    }

    @DeleteMapping("/deleteAccountById")
    public boolean deleteAccountById(@RequestParam String id) {
        try {
            accountRepository.deleteById(id);
            return true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
