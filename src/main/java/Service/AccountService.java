package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    /*
     * No params constructor
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /*
     * Param constructor
     * @params: accountDAO
     */
    public AccountService(AccountDAO accountDao){
        this.accountDAO = accountDao;
    }

    /*
     * addAccount insterts a new account into the account table
     * @params: the account to be added
     * @returns: the account if added, null if not
     */
    public Account addAccount(Account acc){
        return accountDAO.insertAccount(acc);
    }

    /*
     * getUserAccount returns the account with the givin username
     * @params: the username
     * @returns: the account under that username if username exists, null if not
     */
    public Account getUserAccount(String username, String password){
        return accountDAO.getAccountByUsernameAndPassword(username, password);
    }
}
