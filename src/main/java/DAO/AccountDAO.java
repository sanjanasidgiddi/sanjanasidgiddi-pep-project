package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    /**
     * TODO: insert an account into the account table.
     */
    public Account insertAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            boolean unameCheck = false, passwordCheck = account.getPassword().length() >= 4;
            String quer = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedS = connection.prepareStatement(quer);
            preparedS.setString(1, account.getUsername());
            ResultSet r = null;
            if (!("").equals(account.getUsername())) {
                r = preparedS.executeQuery();
                /* Account with that username doesn't exist, and username not blank */
                unameCheck = !r.next();
            } else {
                unameCheck = false;
            }
            if (unameCheck && passwordCheck) {
                String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, account.getUsername());
                preparedStatement.setString(2, account.getPassword());
                preparedStatement.executeUpdate();
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_account_id = pkeyResultSet.getInt(1);
                    return new Account(generated_account_id, account.getUsername(), account.getPassword());
                }
            }
            
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account loginSuccessful(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String quer = "SELECT account_id FROM account WHERE username = ? and password = ?";
            PreparedStatement preparedS = connection.prepareStatement(quer);
            preparedS.setString(1, account.getUsername());
            preparedS.setString(2, account.getPassword());
            ResultSet rs = preparedS.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt(1), account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
