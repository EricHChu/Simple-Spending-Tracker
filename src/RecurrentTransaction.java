/**
 * Recurring Transactions Class
 * @author Eric Chu
 * @version Jun 2023
 */

import java.time.*;

public class RecurrentTransaction extends Transaction{
    private Date lastBill;
    private int timeInterval;

    /**
     * Constructor for recurring transaction
     * @param price Price of transaction
     * @param item Name of transaction
     * @param location Location/Company of transaction
     * @param payment Payment Type of transaction
     * @param category Category of Transaction
     * @param lastBill Last time transaction was added
     * @param interval Time interval between transactions in days
     */
    public RecurrentTransaction(double price, String item, Location location, PaymentInfo payment, Category category, Date lastBill, int interval){
        super(price, item, location, payment, null, category);
        this.lastBill = lastBill;
        this.timeInterval = interval;
    }

    /**
     * Converts object to string of Name
     * @return
     */
    public String toString(){
        return getItem();
    }

    /**
     * Checks if transactions are up-to-date
     * @param tHistory Transaction History to add to
     */
    public void update(TransactionHistory tHistory){
        //Current time
        LocalDate now = LocalDate.now();
        //Creates date
        Date current = new Date(now.getYear(),now.getMonthValue(),now.getDayOfMonth(),0,0);
        //Checks if its time
        if(current.getNumVal() - lastBill.getNumVal() > timeInterval*24*60){
            //Adds time and updates last transaction
            Date newBill = lastBill.addTime(timeInterval);
            //Creates transaction and adds
            Transaction transaction = new Transaction(getPrice(),getItem(),getLocation(),getPayment(),newBill,getCategory());
            tHistory.addTransaction(transaction);
            lastBill = newBill;
            //Checks again
            update(tHistory);
        }
    }
}
