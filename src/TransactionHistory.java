/**
 * History of Transactions
 * @autor Eric Chu
 * @version Jun 2023
 */

import java.util.ArrayList;
import javax.swing.JPanel;
import java.io.Serializable;
public class TransactionHistory implements Serializable{

    //Transactions
    private ArrayList<Transaction> transactions;

    /**
     * Constructor for Transaction History, creates an empty ArrayList of transactions
     */
    public TransactionHistory(){
        this.transactions = new ArrayList<Transaction>();
    }

    /**
     * Displays the transactions stored in the history
     * @param root JPanel to display transactions in
     * @param user Current user we are accessing
     */

    public void outputTransactions(JPanel root,User user){
        for(Transaction current : this.transactions){
            current.display(root,user);
        }
    }

    /**
     * Amount of Transactions held
     * @return Number of transactions held
     */
    public int size(){
        return this.transactions.size();
    }

    /**
     * Gives the ArrayList of transactions
     * @return ArrayList of transaction history
     */
    public ArrayList<Transaction> getTransactions(){
        return this.transactions;
    }

    /**
     * Adds a transaction to the history
     * @param newT Transaction to add
     */
    public void addTransaction(Transaction newT){
        this.transactions.add(newT);
    }

    /**
     * Deletes a transaction based on id
     * @param id Id to delete
     */
    public void deleteTransaction(int id){
        this.transactions.remove(id);
    }

    /**
     * Edits transaction within the history
     * @param index Index of transaction to edit
     * @param field Field to edit
     * @param newT Transaction object carrying new values
     */
    public void editTransaction(int index, String field, Transaction newT){
        Transaction editedT = transactions.get(index);
        switch(field.toLowerCase()){
            case "price":
                editedT.editPrice(newT.getPrice());
                break;
            case "item":
                editedT.editItem(newT.getItem());
                break;
            case "location":
                editedT.editLocation(newT.getLocation());
                break;
            case "time":
                editedT.editTime(newT.getTime());
                break;
            case "category":
                editedT.editCategories(newT.getCategory());
                break;
        }
        transactions.set(index,editedT);
    }

    /**
     * Sorts the arraylist according to the field and order(Ascending/Descending)
     * @param field Field to sort by
     * @param order Whether to sort ascending or descending
     */
    public void sort(TransactionComparator comparator){
        this.transactions.sort(comparator);
    }
}
