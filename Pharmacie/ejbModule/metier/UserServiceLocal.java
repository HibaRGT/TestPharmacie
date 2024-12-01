package metier;

import java.util.List;

import entities.User;
public interface UserServiceLocal {
	User login(String mail, String password);  // Connexion d'un utilisateur
    void register(User user);                 // Inscription d'un utilisateur
    boolean resetPassword(String mail, String newPassword);  // Réinitialisation du mot de passe
    boolean forgotPassword(String mail);         // Envoyer un email pour réinitialiser le mot de passe
    void updateUser(User user);               // Mise à jour des informations utilisateur
    User getUserById(int id);                 // Récupération d'un utilisateur par ID
    List<User> getAllUsers();                 // Liste des utilisateurs (pour l'admin)
}

