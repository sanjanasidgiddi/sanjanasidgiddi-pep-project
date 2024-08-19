package Service;

import Model.Account;
import DAO.AccountDAO;

/**
 * The purpose of a Service class is to contain "business logic" that sits between the web layer (controller) and
 * persistence layer (DAO). That means that the Service class performs tasks that aren't done through the web or
 * SQL: programming tasks like checking that the input is valid, conducting additional security checks, or saving the
 * actions undertaken by the API to a logging file.
 */
public class AccountService {
    private AccountDAO accountDAO;
    /**
     * no-args constructor for creating a new AccountService with a new AccountDAO.
     */
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    /**
     * Constructor for a AccountService when a AccountDAO is provided.
     * @param accountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    
    /**
     * TODO: Use the AccountDAO to persist an account. The given Account will not have an id provided.
     *
     * @param account an Account object.
     * @return The persisted account if the persistence is successful.
     */
    public Account addAccount(Account account) {
        return accountDAO.insertAccount(account);
    }

    /**
     * TODO: Use the AccountDAO to verify successful login. The given Account will not have an id provided.
     *
     * @param account an Account object.
     * @return The loggedin account if the successful.
     */
    public Account verifyLogin(Account account) {
        return accountDAO.loginSuccessful(account);
    }
}
