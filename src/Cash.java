/**
 * Cash Class
 * @author Eric Chu
 * @version Jun 2023
 */

import java.io.Serializable;
public class Cash extends PaymentInfo implements Serializable{

    /**
     * Empty Constructor for Cash
     */
    public Cash(){
        super("Cash");
    }
}
