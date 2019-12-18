package edu.ufp.esof.order.services.authentication;

import edu.ufp.esof.order.models.Client;
import edu.ufp.esof.order.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginService {

    private Map<String,String> users=new HashMap<>();

    private ClientService clientService;

    @Autowired
    public LoginService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void addUser(String username, String password){

        this.users.put(username,password);
    }

    private Optional<Client> isAuthenticated(String username, String password){
        Optional<Client> optionalClient=this.clientService.findByName(username);
        if(optionalClient.isPresent()){
            Client client=optionalClient.get();
            if(client.getPassword().equals(password)){
                return optionalClient;
            }
        }
        return Optional.empty();
    }

    public boolean authenticateUser(Client client ,String token){
        if(client!=null && client.getName()!=null) {
            String cachedToken=this.users.get(client.getName());
            if(cachedToken!=null) {
                return cachedToken.equals(token);
            }
        }
        return false;
    }

    public Optional<String> generateToken(String username, String password) {
        Optional<Client> optionalClient=this.isAuthenticated(username,password);
        if(optionalClient.isPresent()){
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                //token generated from concatenate client username and password
                byte[] encodedHash = digest.digest((username+password).getBytes());

                String encodedHashString=bytesToHex(encodedHash);
                this.users.put(username,encodedHashString);
                return Optional.of(encodedHashString);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    public Optional<String> generateToken(Credentials credentials) {
        return this.generateToken(credentials.getUsername(),credentials.getPassword());
    }
}
