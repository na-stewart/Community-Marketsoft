package main.java.com.traderbobsemporium.model;

public class Camper extends Profile {

    private int balance;

    public Camper(long id, String name, int balance) {
        super(id, name);
        this.balance = balance;
    }


    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
