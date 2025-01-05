package Service;

import DAO.AccountDAO;
import Model.Account;

import java.util.List;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO= new AccountDAO(); 
    }

    public Account addAccount (Account account){
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (account.getPassword() == null || account.getPassword().length() <= 4) {
            throw new IllegalArgumentException("Password must be longer than 4 characters");
        }
        return accountDAO.insertAccount(account);
    }

    public Account updateAccount(int account_id, Account account){
        try {
            accountDAO.updateAccount(account_id, account);
            Account updatedAccount = accountDAO.getAccountById(account_id);
            return updatedAccount; 
        } catch (Exception e) {
            System.out.print("Error while updating the account: " + e.getMessage());
            return null;
        }
    }   

    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }




}
