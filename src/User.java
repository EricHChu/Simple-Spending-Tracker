/**
 * User Class to store all information about an account
 * @author Eric Chu
 * @Jun 2023
 */

import java.util.ArrayList;
import java.io.Serializable;
public class User implements Serializable{
    private String username;
    private String password;
    private TransactionHistory transactions = new TransactionHistory();
    private ArrayList<Category> categories = new ArrayList<Category>();
    private ArrayList<Location> locations = new ArrayList<Location>();
    private ArrayList<PaymentInfo> paymentInfo = new ArrayList<PaymentInfo>();
    private ArrayList<RecurrentTransaction> recurringTransactions = new ArrayList<>();

    /**
     * Constructor for User class
     * @param username Username of account
     * @param password password of account
     */
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.paymentInfo.add(new Cash());
    }

    /**
     * Get transaction history of account
     * @return TransactionHistory stored in user
     */
    public TransactionHistory getTransactions(){
        return this.transactions;
    }

    public ArrayList<RecurrentTransaction> getRecurringTransactions(){
        return this.recurringTransactions;
    }

    /**
     * Get categories associated with account
     * @return ArrayList of categories stored in user
     */
    public ArrayList<Category> getCategories(){
        return this.categories;
    }

    /**
     * Get locations associated with account
     * @return ArrayList of locations stored in user
     */
    public ArrayList<Location> getLocations() {
        return this.locations;
    }

    /**
     * Get payment info stored by account
     * @return ArrayList of payment info stored in user
     */
    public ArrayList<PaymentInfo> getPaymentInfo(){
        return this.paymentInfo;
    }

    /**
     * Changes username to new name
     * @param newUsername Username to change to
     */
    public void editUsername(String newUsername){
        this.username = newUsername;
    }

    /**
     * Changes password of the User
     * @param newPass Password to change to
     */
    public void changePassword(String newPass){
        this.password = newPass;
    }

    /**
     * Gets password of User
     * @return Password of User
     */
    public String getPassword(){
        return this.password;
    }

    /**
     * Get username of account
     * @return Returns username of user
     */
    public String getUsername(){
        return this.username;
    }

    /**
     * Checks if password given is correct
     * @param password Password given
     * @return Whether the password matches or not
     */
    public boolean login(String password){
        if(password.equals(this.password)){
            return true;
        }
        return false;
    }


}
