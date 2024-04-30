/**
 * Transaction Comparator class for sorting Transactions
 * @author Eric Chu
 * @version Jun 2023
 */

import java.util.Comparator;
public class TransactionComparator implements Comparator<Transaction>{
    private String field = "";
    private boolean ascending = false;
    private User user;

    /**
     * Constructor for Comparator
     * @param field Field to compare
     * @param order Ascending/Descending
     * @param user User to check in
     */
    public TransactionComparator(String field,boolean order,User user){
        this.field = field;
        this.ascending = order;
        this.user = user;
    }

    /**
     * Compares two transactions
     * @param one The transaction to be compared.
     * @param two the transaction to be compared to.
     * @return 1 if one is greater, 0 if they are equal, -1 if two is greater
     */
    public int compare(Transaction one,Transaction two){
        int result = 0;
        switch(this.field){
            case "Date":
                result = one.getTime().compare(two.getTime());
                break;
            case "Location":
                result = user.getLocations().lastIndexOf(two.getLocation()) - user.getLocations().lastIndexOf(one.getLocation());
                break;
            case "Category":
                result =  user.getCategories().lastIndexOf(two.getCategory()) - user.getCategories().lastIndexOf(one.getCategory());
                break;
            case "Payment":
                result = user.getPaymentInfo().lastIndexOf(two.getPayment()) - user.getPaymentInfo().lastIndexOf(one.getPayment());
                break;
            case "Price":
                result = (int)Math.ceil(one.getPrice() - two.getPrice());
                break;
            case "Name":
                result = one.getItem().compareTo(two.getItem());
                break;
        }
        //If we are ascending, flip the results
        if(ascending){
            result = -1*result;
        }
        return result;
    }
}
