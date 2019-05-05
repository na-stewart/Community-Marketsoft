package main.java.com.traderbobsemporium.model;

public class Account extends Profile {

    private String password;

    public Account(long id, String username, String password) {
        super(id, username);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
