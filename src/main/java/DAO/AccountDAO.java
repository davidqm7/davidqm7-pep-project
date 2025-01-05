package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>(); 

        try {
            String sql = "SELECT * FROM account;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                 rs.getString("password"));
                 accounts.add(account);
            }
            
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return accounts; 
    }

    public Account getAccountById(int Id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "Select * FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                 rs.getString("password"));
                 return account;
            }
            
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return null; 
    }

    public Account insertAccount(Account account){
        try (Connection connection = ConnectionUtil.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(
             "INSERT INTO account (username, password) VALUES (?, ?);",
             Statement.RETURN_GENERATED_KEYS)) {

        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());
        preparedStatement.executeUpdate();

        try (ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys()) {
            if (pkeyResultSet.next()) {
                int generated_account_id = pkeyResultSet.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }
    } catch (SQLException e) {
        System.err.println("Error inserting account: " + e.getMessage());
    }
    return null;
    }

    public void updateAccount(int id, Account account){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE account SET username = ?, password = ? WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
    }

    
    
}
