package metier;
	import java.io.IOException;
	
	import javax.ejb.Stateless;
	
	import com.sendgrid.Method;
	import com.sendgrid.Request;
	import com.sendgrid.Response;
	import com.sendgrid.SendGrid;
	import com.sendgrid.helpers.mail.Mail;
	import com.sendgrid.helpers.mail.objects.Content;
	import com.sendgrid.helpers.mail.objects.Email;
	
	@Stateless
	public class EmailService {
		private static final String SENDGRID_API_KEY = "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"; // Remplacez par votre clé API
	
	    // Méthode pour envoyer l'email
	    public boolean sendEmail(String to, String subject, String body) {
	        // Créer l'objet Email SendGrid
	        Email fromEmail = new Email("rguitihibatallah31@gmail.com"); // Votre adresse email
	        Email toEmail = new Email(to); // L'adresse de destination
	        Content content = new Content("text/plain", body); // Contenu de l'email (texte brut)
	        Mail mail = new Mail(fromEmail, subject, toEmail, content);
	
	        // Configuration de SendGrid
	        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
	        Request request = new Request();
	
	        try {
	            // Préparer la requête
	            request.setMethod(Method.POST);
	            request.setEndpoint("mail/send");
	            request.setBody(mail.build());
	            // Envoyer l'email
	            Response response = sg.api(request);
	
	            // Vérifier la réponse pour savoir si l'e-mail a été envoyé avec succès
	            if (response.getStatusCode() == 202) {
	                System.out.println("Email envoyé avec succès!");
	                return true;
	            } else {
	                System.out.println("Erreur lors de l'envoi de l'email: " + response.getBody());
	                return false;
	            }
	        } catch (IOException ex) {
	            ex.printStackTrace();
	            return false;
	        }
	    }
	}
