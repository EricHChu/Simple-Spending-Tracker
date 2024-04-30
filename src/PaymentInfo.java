/**
 * Payment Information Abstract Class
 * @author Eric Chu
 * @version Jun 2023
 */

import java.io.Serializable;
abstract class PaymentInfo implements Serializable{
    private String name;

    /**
     * Empty Constructor for PaymentInfo
     */
    public PaymentInfo(){

    }

    /**
     * Constructor for PaymentInfo with Name
     * @param name Name of the Payment Info
     */
    public PaymentInfo(String name){
        this.name = name;
    }

    /**
     * Sets name of the Payment Info
     * @param newVal Name of the payment info
     */
    public void setName(String newVal){
        this.name = newVal;
    }

    /**
     * Gets name of the payment info
     * @return Name of the payment info
     */
    public String getName(){
        return this.name;
    }

    /**
     * Converts payment info to a string
     * @return Name of the Payment Info
     */
    public String toString(){
        return this.name;
    }
}
