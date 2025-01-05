package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

import javax.management.RuntimeErrorException;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO(); 
    }

    public Message addMessage (Message message){
        if(message.getMessage_text() == null || message.getMessage_text().isBlank()){
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
        if(message.getMessage_text().length() > 255){
            throw new IllegalArgumentException("Message cannot be more than 255 characters");
        }
        if (message.getPosted_by() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        if (message.getTime_posted_epoch() <= 0) {
            throw new IllegalArgumentException("Invalid timestamp");
        }
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        Message message = messageDAO.getMessageById(id);
        if(message == null){
            throw new IllegalArgumentException("Message not found with the id");
        }
        return message;
    }

    public void deleteMessage(int id){
        Message message = getMessageById(id);
        if(message == null){
            throw new IllegalArgumentException("Message with ID " + id + " does not exist and cannot be deleted.");
        }
        messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, Message message){
        if(message.getMessage_text() == null || message.getMessage_text().isBlank()){
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
        if(message.getMessage_text().length() > 255){
            throw new IllegalArgumentException("Message cannot be more than 255 characters");
        }
        if (message.getPosted_by() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        try {
            messageDAO.updateMessage(id, message);
            Message updatedMessage = messageDAO.getMessageById(id);
            return updatedMessage; 
        } catch (Exception e) {
            throw new RuntimeException("Error while updating the account: " + e.getMessage());
        }
    }


}
