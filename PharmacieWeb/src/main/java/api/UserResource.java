package api;

import java.util.List;


import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import entities.User;
import metier.UserServiceLocal;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @EJB
    private UserServiceLocal userService;
    

    // Endpoint pour la connexion (login)
    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest) {
        User user = userService.login(loginRequest.getMail(), loginRequest.getPassword());
        if (user != null) {
            return Response.ok(user).build();  // Réponse avec l'utilisateur en cas de succès
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }

    // Endpoint pour l'inscription
    @POST
    @Path("/register")	
    public Response register(User user) {
        userService.register(user);
        return Response.status(Response.Status.CREATED).entity("User created successfully").build();
    }

    // Endpoint pour la réinitialisation du mot de passe
    @POST
    @Path("/forgot-password")
    public Response forgotPassword(ForgotPasswordRequest request) {
        boolean success = userService.forgotPassword(request.getMail());
        if (success) {
            return Response.ok("Password reset email sent").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }

    // Endpoint pour réinitialiser le mot de passe
    @POST
    @Path("/reset-password")
    public Response resetPassword(ResetPasswordRequest request) {
        boolean success = userService.resetPassword(request.getToken(), request.getNewPassword());
        if (success) {
            return Response.ok("Password reset successfully").build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid or expired token").build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return Response.ok(user).build(); // Réponse avec l'utilisateur en cas de succès
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
    }
    
    @GET
    @Path("/all")
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build(); // Aucun utilisateur trouvé
        }
        System.out.println("Utilisateurs trouvés : " + users);
        return Response.ok(users).build(); // Liste des utilisateurs
    }
    
    @PUT
    @Path("/update")
    public Response updateUser(User user) {
        if (user.getIdUser() == 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("User ID is required").build();
        }
        userService.updateUser(user);
        return Response.ok("User updated successfully").build();
    }
    @Path("/test")
    @GET
    public Response testEndpoint() {
    	System.out.println("Test endpoint reached!");
        return Response.ok("Test OK").build();
    }

    
}
