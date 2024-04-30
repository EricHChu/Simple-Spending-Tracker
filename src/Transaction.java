/**
 * Class for a single Transaction made
 * @author Eric Chu
 * @version Jun 2023
 */

import javax.swing.*;
import java.awt.*;
import javax.swing.border.MatteBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

public class Transaction implements Serializable{
    private double price;
    private String item;
    private Location location;
    private PaymentInfo payment;
    private Date time;
    private Category category;

    /**
     * Empty Constructor for Transaction with random content
     */
    public Transaction(){
        this.price = 0;
        this.item = "Venti Mung Bean Chai Tea Espresso Extreme Two Pumps";
        this.location = new Location("Amurica");
        this.time = new Date(2005,12,3,4,50);
    }

    /**
     * Constructor for Transaction with all information
     * @param price Price of transaction
     * @param item Name of transaction
     * @param location Location or Company of transaction
     * @param payment Payment type used
     * @param time Date and Time of Transaction
     * @param category Category of Transaction
     */
    public Transaction(double price, String item, Location location, PaymentInfo payment, Date time, Category category){
        this.price = price;
        this.item = item;
        this.location = location;
        this.payment = payment;
        this.time = time;
        this.category = category;
    }

    /**
     * Changes price of transaction
     * @param newVal New price to set
     */
    public void editPrice(double newVal){
        this.price = newVal;
    }

    /**
     * Gets the price of the transaction
     * @return Price of the transaction
     */
    public double getPrice(){
        return this.price;
    }

    /**
     * Edits name of the transaction
     * @param newVal New name to set
     */
    public void editItem(String newVal){
        this.item = newVal;
    }

    /**
     * Gets the name of the transaction
     * @return Item/Name of the transaction
     */
    public String getItem(){
        return this.item;
    }

    /**
     * Changes the location/company of the transaction
     * @param newVal New Location to change to
     */
    public void editLocation(Location newVal){
        this.location = newVal;
    }

    /**
     * Gets the location/company of the transaction
     * @return Location of the transaction
     */
    public Location getLocation(){
        return this.location;
    }

    /**
     * Changes date and time of the transaction
     * @param newVal New date and time to change it to
     */
    public void editTime(Date newVal){
        this.time = newVal;
    }

    /**
     * Gets the date and time of the transaction
     * @return Date of the transaction
     */
    public Date getTime(){
        return this.time;
    }

    /**
     * Changes the payment type of the transaction
     * @param newVal Payment Info to change the transaction to
     */
    public void editPayment(PaymentInfo newVal){
        this.payment = newVal;
    }

    /**
     * Gets the payment info of the transaction
     * @return Payment Info of the transaction
     */
    public PaymentInfo getPayment(){
        return this.payment;
    }

    /**
     * Changes the category of the transaction
     * @param newVal Category to change the transaction to
     */
    public void editCategories(Category newVal){
        this.category = newVal;
    }

    /**
     * Gets the category of the transaction
     * @return Category of the transaction
     */
    public Category getCategory(){
        return this.category;
    }

    /**
     * Displays the transaction to the given JPanel
     * @param root JPanel to display transaction to
     * @param user User transaction is associated with
     */
    public void display(JPanel root,User user){
        //Font of transaction
        Font dx = new Font("DengXian",1,15);
        //Panel containing transaction details with specific measurements
        JPanel transaction = new JPanel(){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(100,75);
            }
        };
        Component glue = Box.createRigidArea(new Dimension(0,15));
        //Border of transaction
        MatteBorder border = new MatteBorder(2,10,2,2,Color.WHITE);
        //Add layout and border
        transaction.setLayout(new BorderLayout(10,0));
        transaction.setBorder(border);
        //Add Transaction details and specify design choices
        JLabel timeLabel = new JLabel(time.getText());
        timeLabel.setPreferredSize(new Dimension(60,60));
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setFont(dx);
        JLabel titleLabel = new JLabel(location.getName() + ", " + item);
        titleLabel.setFont(dx);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        JLabel priceLabel = new JLabel("$" + this.price);
        priceLabel.setFont(dx);
        priceLabel.setPreferredSize(new Dimension(60,60));
        transaction.add(timeLabel,BorderLayout.LINE_START);
        transaction.add(titleLabel,BorderLayout.CENTER);
        transaction.add(priceLabel,BorderLayout.LINE_END);
        //Transaction Editing Listener
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //Create pane for editing
                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info,BoxLayout.PAGE_AXIS));
                //Add editable details to pane
                JTextField priceField = new JTextField("" + price);
                JComboBox<Location> locationField = new JComboBox(user.getLocations().toArray());
                locationField.setSelectedItem(location);
                JTextField itemField = new JTextField(item);
                JComboBox<Location> categoryField = new JComboBox(user.getCategories().toArray());
                info.add(priceField);
                info.add(locationField);
                info.add(itemField);
                info.add(categoryField);
                //Give user editing dialog
                int result = JOptionPane.showOptionDialog(null,info,"Transaction Info",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, new String[]{"Save","Cancel","Delete"},0);
                if(result == 0) {
                    //Edit details according to user given parameters and update JComponents
                    editPrice(Double.parseDouble(priceField.getText()));
                    //update!
                    priceLabel.setText("$" + price);
                    editItem(itemField.getText());
                    //update!
                    for(Location currentLocation:user.getLocations()){
                        if(currentLocation.equals(locationField.getSelectedItem())){
                            location = currentLocation;
                        }
                    }
                    //update!
                    titleLabel.setText(location.getName() + ", " + item);
                    for(Category currentCategory :user.getCategories()){
                        if(currentCategory.equals(categoryField.getSelectedItem())){
                            category = currentCategory;
                        }
                    }
                }else if(result == 2){
                    //Removes transaction and redraws pane
                    user.getTransactions().getTransactions().remove(Transaction.this);
                    root.remove(transaction);
                    root.remove(glue);
                    root.revalidate();
                    root.repaint();
                }
            }
        };
        //Add Listener
        transaction.addMouseListener(ml);
        //Add transaction to pane
        root.add(transaction);
        root.add(glue);
    }
}
