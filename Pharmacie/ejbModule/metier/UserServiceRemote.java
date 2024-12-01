package metier;

import java.util.List;

import entities.User;

public interface UserServiceRemote {
	User login(String mail, String password);  
    void register(User user);                 
    boolean resetPassword(String mail, String newPassword); 
    boolean forgotPassword(String mail);         
    void updateUser(User user);               
    User getUserById(int id);                 
    List<User> getAllUsers();                 
}
