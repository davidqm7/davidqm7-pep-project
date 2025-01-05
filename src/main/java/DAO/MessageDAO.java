package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    public Message insertMessage(Message message){
        try (Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO message (posted_by,message_text, time_posted_enoch) VALUES (?,?,?);", 
         Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();

        try(ResultSet pkResultSet = preparedStatement.getGeneratedKeys()){
                if(pkResultSet.next())
                {
                    int generated_message_id = pkResultSet.getInt(1);
                    return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }       
        } catch (SQLException e) {
            System.err.println("Error inserting account: " + e.getMessage());
        }
        return null; 
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                 rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                 messages.add(message); 
            }
            
        } catch (SQLException e) {
            System.out.print(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return message; 
            }
            
        } catch (SQLException e) {
            System.out.print("Error while getting message" + e.getMessage());
        }
        return null; 
    }

    



}
