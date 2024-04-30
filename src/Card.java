/**
 * Debit/Credit Card Class
 * @author Eric Chu
 * @version Jun 2023
 */

import java.io.Serializable;
public class Card extends PaymentInfo implements Serializable{
    private String number;
    private int pin;
    private Date expiry;
    private String holder;

    /**
     * Constructor for Card class
     * @param name Name of the card
     * @param number Number of the card
     * @param pin PIN of the card
     * @param expiry Expiry Date of the card
     * @param holder Name of cardholder
     */
    Card(String name, String number,int pin,Date expiry,String holder){
        super(name);
        this.number = number;
        this.pin = pin;
        this.expiry = expiry;
        this.holder = holder;
    }

    /**
     * Converts card to a string
     * @return Name of the card
     */
    public String toString(){
        return super.getName();
    }

    /**
     * Changes number of the card
     * @param newVal Number to change to
     */
    public void editNumber(String newVal){
        this.number = newVal;
    }

    /**
     * Gets number of the card
     * @return Number of the card
     */
    public String getNumber(){
        return this.number;
    }

    /**
     * Changes pin of the card
     * @param newVal PIN to change the card to
     */
    public void editPin(int newVal){
        this.pin = newVal;
    }

    /**
     * Gets the PIN of the card
     * @return PIN of the card
     */
    public int getPin(){
        return this.pin;
    }

    /**
     * Changes expiry date of the card
     * @param newVal Expiry date to change card to
     */
    public void editExpiry(Date newVal){
        this.expiry = newVal;
    }

    /**
     * Gets expiry date of the card
     * @return Expiry date of the card
     */
    public Date getExpiry(){
        return this.expiry;
    }

    /**
     * Changes cardholder name
     * @param newVal Changes name of the cardholder
     */
    public void editHolder(String newVal){
        this.holder = newVal;
    }

    /**
     * Gets the name of the cardholder
     * @return Name of the cardholder
     */
    public String getHolder(){
        return this.holder;
    }
}
