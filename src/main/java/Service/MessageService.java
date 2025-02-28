package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;


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
        return messageDAO.getMessageById(id);
    }

    public void deleteMessage(int id){
        Message message = getMessageById(id);
        if(message == null){
            throw new IllegalArgumentException("Message with ID " + id + " does not exist and cannot be deleted.");
        }
        messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, String message){
        if(message == null || message.isBlank()){
            throw new IllegalArgumentException("Message cannot be null or blank");
        }
        if(message.length() > 255){
            throw new IllegalArgumentException("Message cannot be more than 255 characters");
        }
        try {
            Message existingMessage = messageDAO.getMessageById(id);
            if (existingMessage == null) {
                throw new IllegalArgumentException("Message not found with the provided ID");
            }
            existingMessage.setMessage_text(message);
            messageDAO.updateMessage(id, existingMessage);
            return messageDAO.getMessageById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating the account: " + e.getMessage());
        }
    }

    public List<Message> getMessagesByAccountId(int id){
        if(id <=0){
            throw new IllegalArgumentException("Invalid Id");
        }
        try {
            return messageDAO.getMessageByAccountId(id);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching messages: " + e.getMessage());
        }
    }

}
