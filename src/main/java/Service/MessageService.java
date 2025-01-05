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
        return messageDAO.insertMessage(message);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public void deleteMessage(int id){
        messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, Message message){
        try {
            messageDAO.updateMessage(id, message);
            Message updatedMessage = messageDAO.getMessageById(id);
            return updatedMessage; 
        } catch (Exception e) {
            System.out.print("Error while updating the account: " + e.getMessage());
            return null;
        }
    }


}
