package metier;


import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;


import entities.User;

@Stateless(name="USER")
public class UserServiceImp implements UserServiceLocal, UserServiceRemote {

    @PersistenceContext(unitName = "Pharmacie")
    private EntityManager em;

    @Inject
    private EmailService emailService;

    @Override
    public User login(String mail, String password) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u FROM User u WHERE u.mail = :mail AND u.password = :password", User.class);
        query.setParameter("mail", mail);
        query.setParameter("password", password);
        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public void register(User user) {
        em.merge(user);
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.resetToken = :token", User.class)
                      .setParameter("token", token)
                      .getResultList().stream()
                      .findFirst()
                      .orElse(null);

        if (user == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return false;
        }
        user.setPassword(newPassword);
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        em.merge(user);
        return true;
    }

    @Override
    public boolean forgotPassword(String mail) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.mail = :mail", User.class)
                      .setParameter("mail", mail)
                      .getResultList().stream()
                      .findFirst()
                      .orElse(null);

        if (user == null) {
            return false;
        }

        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(1);

        user.setResetToken(resetToken);
        user.setResetTokenExpiry(expiryTime);
        em.merge(user);

        String resetLink = "https://votreapp.com/reset-password?token=" + resetToken;
        String subject = "Réinitialisation de votre mot de passe";
        String body = "Bonjour,\n\n" +
                      "Vous avez demandé à réinitialiser votre mot de passe. Cliquez sur le lien suivant pour réinitialiser votre mot de passe :\n\n" +
                      resetLink + "\n\nCe lien expirera dans 1 heure.";

        return emailService.sendEmail(user.getMail(), subject, body);
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public User getUserByEmail(String email) {
        TypedQuery<User> query = em.createQuery(
            "SELECT u FROM User u WHERE u.mail = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst().orElse(null);
    }
    
}

