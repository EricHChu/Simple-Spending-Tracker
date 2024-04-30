/**
 * Main Class
 * @author Eric Chu
 * @version Jun 2023
 * I'm so sorry, all comments will be added by tuesday morning T_T
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.*;
import com.formdev.flatlaf.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
public class Main {
    public static String currentUser = "User";
    public static User accessedUser = new User("","");
    public static File user;
    public static JFrame frame;
    public static void main(String[] args) throws FontFormatException, IOException{
        //Graphics set up
        FlatDarculaLaf.setup();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Main.class.getClassLoader().getResourceAsStream("Deng.otf")));

        //Frame creation
        frame = new JFrame("Personal Finance");
        frame.setSize(800,800);
        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);

        //Labels for the different tabs
        JLabel tab1 = new JLabel(currentUser);
        JLabel tab2 = new JLabel("Transactions");
        JLabel tab3 = new JLabel("Timed Transactions");
        JLabel tab4 = new JLabel("Reports");
        JLabel tab5 = new JLabel("Payment Info");
        JLabel tab6 = new JLabel("Account");


        //TabbedPane
        JTabbedPane panels = new JTabbedPane(JTabbedPane.LEFT);


        //Login Tab
        JPanel userPanel = new JPanel();
        //Login screen
        JPanel login = new JPanel();
        //Welcome screen
        JPanel userDetails = new JPanel();
        JLabel welcome = new JLabel("Welcome, " + currentUser);
        userDetails.add(Box.createRigidArea(new Dimension(1,500)));
        userDetails.add(welcome);
        userDetails.add(Box.createRigidArea(new Dimension(1,1)));
        //Logout button
        JButton logout = new JButton("Logout");
        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Double Checks
                int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to log out?","Logout",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null);
                if(result == 0){
                    try{
                        //Saves Account to file
                        JFileChooser fc = new JFileChooser();
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        fc.showOpenDialog(frame);
                        FileOutputStream fos = new FileOutputStream(fc.getSelectedFile() + "/" + currentUser + ".account");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(accessedUser);
                        oos.flush();
                        oos.close();
                        //Encrypts
                        Encrypter encrypter = new Encrypter(accessedUser.getPassword());
                        encrypter.encrypt(new File(fc.getSelectedFile() + "/" + currentUser + ".account"));
                    }catch(FileNotFoundException a){
                        System.out.println("File Not Found - Logout");
                    }catch(IOException a){
                        System.out.println("Error - IO - Logout");
                    }
                    //Wipes data on user
                    accessedUser = null;
                    currentUser = "User";
                    //Disables other tabs
                    panels.setEnabledAt(1,false);
                    panels.setEnabledAt(2,false);
                    panels.setEnabledAt(3,false);
                    panels.setEnabledAt(4,false);
                    panels.setEnabledAt(5,false);
                    userPanel.remove(userDetails);
                    userPanel.add(login);
                    userPanel.revalidate();
                    userPanel.repaint();
                }
            }
        });
        userDetails.add(logout);
        login.add(Box.createRigidArea(new Dimension(1,500)));
        //Account finder (File)
        JButton findAcc = new JButton("Find Account");
        findAcc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Gets file from directory
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(frame);
                if(result == 0){
                    user = fc.getSelectedFile();
                }
            }
        });
        login.add(findAcc);
        //Password entry
        JLabel passwordText = new JLabel("Password:");
        login.add(passwordText);
        JPasswordField password = new JPasswordField();
        password.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Makes encrypter with password as key
                Encrypter encrypter = new Encrypter(password.getText());
                try {
                    //Decrypts selected file
                    encrypter.decrypt(user);
                }catch(IOException a){
                    System.out.println("Error - IO - Login Decrypting");
                }
                try{
                    //Attempts to access User account
                    FileInputStream fis = new FileInputStream(user);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    User loggingIn = (User)ois.readObject();
                    if(loggingIn.login(password.getText())){
                        //Successful login will open data
                        accessedUser = loggingIn;
                        currentUser = user.getName();
                        currentUser = currentUser.substring(0,currentUser.length()-8);
                        //Account features are enabled
                        welcome.setText("Welcome, " + currentUser);
                        userPanel.remove(login);
                        userPanel.add(userDetails);
                        tab1.setText(currentUser);
                        panels.setEnabledAt(1,true);
                        panels.setEnabledAt(2,true);
                        panels.setEnabledAt(3,true);
                        panels.setEnabledAt(4,true);
                        panels.setEnabledAt(5,true);
                        userPanel.revalidate();
                        userPanel.repaint();
                    }
                    ois.close();
                }catch(FileNotFoundException a){
                    System.out.println("File Not Found - Login");
                }catch(IOException a){
                    System.out.println("Error - IO - Login");
                }catch(ClassNotFoundException a){
                    System.out.println("Class Not Found - Login");
                }
                try{
                    //Re-encrypts file
                    encrypter.encrypt(user);
                }catch(IOException a){
                    System.out.println("Error - IO - Login Encrypting");
                }
            }
        });
        login.add(password);
        //New Account Creation
        JButton newAcc = new JButton("New Account");
        newAcc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Creates new panel with username and password entry
                JPanel info = new JPanel();
                info.add(new JLabel("Username:"));
                JTextField username = new JTextField();
                info.add(username);
                info.add(new JLabel("Password:"));
                JPasswordField password = new JPasswordField();
                info.add(password);
                //Gives dialog to user
                int result = JOptionPane.showOptionDialog(frame,info,"Account Creation",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                if(result == 0){
                    //Creates new user
                    currentUser = username.getText();
                    accessedUser = new User(username.getText(),password.getText());
                    //Enables account features
                    welcome.setText("Welcome, " + currentUser);
                    userPanel.remove(login);
                    userPanel.add(userDetails);
                    tab1.setText(currentUser);
                    panels.setEnabledAt(1,true);
                    panels.setEnabledAt(2,true);
                    panels.setEnabledAt(3,true);
                    panels.setEnabledAt(4,true);
                    panels.setEnabledAt(5,true);
                    userPanel.revalidate();
                    userPanel.repaint();
                }
            }
        });
        login.add(newAcc);
        login.add(Box.createVerticalGlue());
        userPanel.add(login);


        //Transactions Tab
        JPanel transactionPane = new JPanel();
        transactionPane.setLayout(new BoxLayout(transactionPane,BoxLayout.Y_AXIS));
        //Adds scrolling
        JScrollPane transactions = new JScrollPane(transactionPane);
        transactionPane.add(Box.createRigidArea(new Dimension(0,5)));
        //New transaction button
        JPanel addNew = new JPanel();
        addNew.setLayout(new BoxLayout(addNew,BoxLayout.X_AXIS));
        Font dxButton = new Font("DengXian",0,13);
        JButton addNewButton = new JButton("Add New Transaction");
        addNewButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //Creates panel with needed info
                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info,BoxLayout.PAGE_AXIS));
                JTextField priceField = new JTextField();
                JComboBox<Location> locationField = new JComboBox(accessedUser.getLocations().toArray());
                locationField.setEditable(true);
                JTextField itemField = new JTextField();
                JComboBox<PaymentInfo> paymentField = new JComboBox(accessedUser.getPaymentInfo().toArray());
                JComboBox<Location> categoryField = new JComboBox(accessedUser.getCategories().toArray());
                categoryField.setEditable(true);
                JSpinner time = new JSpinner(new SpinnerDateModel());
                new JSpinner.DateEditor(time);
                time.setEditor(new JSpinner.DateEditor(time,"yyyy/MM/dd hh:mm:ss"));
                info.add(new JLabel("Price:"));
                info.add(priceField);
                info.add(new JLabel("Location:"));
                info.add(locationField);
                info.add(new JLabel("Item Name:"));
                info.add(itemField);
                info.add(new JLabel("Payment Type:"));
                info.add(paymentField);
                info.add(new JLabel("Category:"));
                info.add(categoryField);
                info.add(new JLabel("Time:"));
                info.add(time);
                //Gives dialog to enter transaction info
                int result = JOptionPane.showOptionDialog(null,info,"Transaction Info",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, new String[]{"Save","Cancel"},0);
                //Gets information given
                Location location = new Location("");
                boolean changed = false;
                for(Location currentLocation:accessedUser.getLocations()){
                    if(currentLocation.equals(locationField.getSelectedItem())){
                        location = currentLocation;
                        changed = true;
                    }
                }
                if(!changed){
                    Location newLocation = new Location(locationField.getSelectedItem().toString());
                    location = newLocation;
                    accessedUser.getLocations().add(newLocation);
                }
                changed = false;
                Category category = new Category("");
                for(Category currentCategory:accessedUser.getCategories()){
                    if(currentCategory.equals(categoryField.getSelectedItem())){
                        category = currentCategory;
                        changed = true;
                    }
                }
                if(!changed){
                    Category newCategory = new Category(categoryField.getSelectedItem().toString());
                    category = newCategory;
                    accessedUser.getCategories().add(newCategory);
                }
                PaymentInfo payment = (PaymentInfo)paymentField.getSelectedItem();
                for(PaymentInfo currentInfo :accessedUser.getPaymentInfo()){
                    if(currentInfo.equals(paymentField.getSelectedItem())){
                        payment = currentInfo;
                    }
                }
                java.util.Date newTime = (java.util.Date)time.getValue();
                Date date = new Date(newTime.getYear()+1900,newTime.getMonth() + 1,newTime.getDate(),newTime.getHours(),newTime.getMinutes());
                //Checks information, creates necessary new info and creates transaction with it all
                Transaction t = new Transaction(Double.parseDouble(priceField.getText()),itemField.getText(),location,payment,date,category);
                //Adds and displays new transaction
                accessedUser.getTransactions().addTransaction(t);
                t.display(transactionPane,accessedUser);
            }
        });
        addNewButton.setFont(dxButton);
        //Sorting button
        JButton sort = new JButton("Sort");
        sort.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Creates info panel with option to sort
                JPanel info = new JPanel();
                //Sorting fields
                JComboBox<String> field = new JComboBox<>(new String[]{"Date","Price","Category","Location","Name"});
                info.add(new JLabel("Sort By: "));
                info.add(field);
                int result = JOptionPane.showOptionDialog(null,info,"Sort",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,new String[]{"Descending","Ascending","Cancel"},0);
                if(result == 0){
                    //Sorts according to field
                    accessedUser.getTransactions().sort(new TransactionComparator((String)field.getSelectedItem(),false,accessedUser));
                    //Refreshes transaction pane
                    transactionPane.removeAll();
                    transactionPane.add(Box.createRigidArea(new Dimension(0,5)));
                    transactionPane.add(addNew);
                    transactionPane.add(Box.createRigidArea(new Dimension(0,10)));
                    accessedUser.getTransactions().outputTransactions(transactionPane,accessedUser);
                }else if(result == 1){
                    //Sorts according to field
                    accessedUser.getTransactions().sort(new TransactionComparator((String)field.getSelectedItem(),true,accessedUser));
                    //Refreshed transaction pane
                    transactionPane.removeAll();
                    transactionPane.add(Box.createRigidArea(new Dimension(0,5)));
                    transactionPane.add(addNew);
                    transactionPane.add(Box.createRigidArea(new Dimension(0,10)));
                    accessedUser.getTransactions().outputTransactions(transactionPane,accessedUser);
                }
            }
        });
        //Transaction refresher
        JButton refresh = new JButton("Refresh Transactions");
        refresh.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //Refreshes transaction pane
                transactionPane.removeAll();
                transactionPane.add(Box.createRigidArea(new Dimension(0,5)));
                transactionPane.add(addNew);
                transactionPane.add(Box.createRigidArea(new Dimension(0,10)));
                accessedUser.getTransactions().outputTransactions(transactionPane,accessedUser);
            }
        });
        //Adds components into panel
        refresh.setFont(dxButton);
        addNew.add(addNewButton);
        addNew.add(Box.createHorizontalGlue());
        addNew.add(sort);
        addNew.add(Box.createHorizontalGlue());
        addNew.add(refresh);
        //Adds panel to master panel
        transactionPane.add(addNew);
        transactionPane.add(Box.createRigidArea(new Dimension(0,10)));


        //Timed Transactions Tab
        JPanel recurring = new JPanel();
        recurring.add(Box.createRigidArea(new Dimension(1,400)));
        //Recurring transactions available
        ArrayList<RecurrentTransaction> recurTransactions = accessedUser.getRecurringTransactions();
        JComboBox<RecurrentTransaction> timedTransactions = new JComboBox<>(recurTransactions.toArray(new RecurrentTransaction[recurTransactions.size()]));
        //Add new button
        JButton addNewRT = new JButton("Add New");
        addNewRT.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Creates JPanel with all needed info
                JPanel info = new JPanel();
                info.setLayout(new BoxLayout(info,BoxLayout.PAGE_AXIS));
                JTextField priceField = new JTextField();
                JComboBox<Location> locationField = new JComboBox(accessedUser.getLocations().toArray());
                locationField.setEditable(true);
                JTextField itemField = new JTextField();
                JComboBox<PaymentInfo> paymentField = new JComboBox(accessedUser.getPaymentInfo().toArray());
                JComboBox<Location> categoryField = new JComboBox(accessedUser.getCategories().toArray());
                categoryField.setEditable(true);
                JSpinner time = new JSpinner(new SpinnerDateModel());
                new JSpinner.DateEditor(time);
                time.setEditor(new JSpinner.DateEditor(time,"yyyy/MM/dd"));
                JTextField timeInterval = new JTextField();
                //Adds everything to Jpanel
                info.add(new JLabel("Price:"));
                info.add(priceField);
                info.add(new JLabel("Location:"));
                info.add(locationField);
                info.add(new JLabel("Item Name:"));
                info.add(itemField);
                info.add(new JLabel("Payment Type:"));
                info.add(paymentField);
                info.add(new JLabel("Category:"));
                info.add(categoryField);
                info.add(new JLabel("Start Date:"));
                info.add(time);
                info.add(new JLabel("Time Interval (Days):"));
                info.add(timeInterval);
                //Prompts user for input
                int result = JOptionPane.showOptionDialog(null,info,"Transaction Info",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null, new String[]{"Save","Cancel"},0);
                //Checks information
                Location location = new Location("");
                boolean changed = false;
                for(Location currentLocation:accessedUser.getLocations()){
                    if(currentLocation.equals(locationField.getSelectedItem())){
                        location = currentLocation;
                        changed = true;
                    }
                }
                if(!changed){
                    Location newLocation = new Location(locationField.getSelectedItem().toString());
                    location = newLocation;
                    accessedUser.getLocations().add(newLocation);
                }
                changed = false;
                Category category = new Category("");
                for(Category currentCategory:accessedUser.getCategories()){
                    if(currentCategory.equals(categoryField.getSelectedItem())){
                        category = currentCategory;
                        changed = true;
                    }
                }
                if(!changed){
                    Category newCategory = new Category(categoryField.getSelectedItem().toString());
                    category = newCategory;
                    accessedUser.getCategories().add(newCategory);
                }
                PaymentInfo payment = new Cash();
                for(PaymentInfo currentInfo :accessedUser.getPaymentInfo()){
                    if(currentInfo.equals(paymentField.getSelectedItem())){
                        payment = currentInfo;
                    }
                }
                java.util.Date newTime = (java.util.Date)time.getValue();
                Date date = new Date(newTime.getYear()+1900,newTime.getMonth() + 1,newTime.getDate(),newTime.getHours(),newTime.getMinutes());
                //Once info is checked and created, all is placed into a Recurrent Transaction
                RecurrentTransaction rT = new RecurrentTransaction(Double.parseDouble(priceField.getText()),itemField.getText(),location,payment,category,date,Integer.parseInt(timeInterval.getText()));
                //Creates starting transaction and adds it and displays it
                Transaction t = new Transaction(Double.parseDouble(priceField.getText()),itemField.getText(),location,payment,date,category);
                accessedUser.getTransactions().addTransaction(t);
                t.display(transactionPane,accessedUser);
                //Adds recurringTransaction to user and updates pane
                accessedUser.getRecurringTransactions().add(rT);
                rT.update(accessedUser.getTransactions());
                timedTransactions.removeAllItems();
                for(Object currentTransaction:accessedUser.getRecurringTransactions().toArray()){
                    timedTransactions.addItem((RecurrentTransaction) currentTransaction);
                }
            }
        });
        //Delete Recurring Transaction Button
        JButton delete = new JButton("Delete Timed Transaction");
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Deletes selected RecurringTransaction
                RecurrentTransaction rT = (RecurrentTransaction) timedTransactions.getSelectedItem();
                accessedUser.getRecurringTransactions().remove(rT);
                timedTransactions.removeAllItems();
                for(Object currentTransaction:accessedUser.getRecurringTransactions().toArray()){
                    timedTransactions.addItem((RecurrentTransaction) currentTransaction);
                }
            }
        });
        //Adds buttons and fields in
        recurring.add(timedTransactions);
        recurring.add(addNewRT);
        recurring.add(delete);


        //Report Generation Tab
        JPanel report = new JPanel();
        report.add(Box.createRigidArea(new Dimension(1,400)));
        //Start date for generating report
        report.add(new JLabel("Start Date:"));
        JSpinner startTime = new JSpinner(new SpinnerDateModel());
        new JSpinner.DateEditor(startTime);
        startTime.setEditor(new JSpinner.DateEditor(startTime,"yyyy/MM/dd"));
        report.add(startTime);
        //End date for generating report
        report.add(new JLabel("End Time:"));
        JSpinner endTime = new JSpinner(new SpinnerDateModel());
        new JSpinner.DateEditor(endTime);
        endTime.setEditor(new JSpinner.DateEditor(endTime,"yyyy/MM/dd"));
        report.add(endTime);
        //Button to generate report
        JButton generate = new JButton("Generate Report");
        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Creates report generator
                ReportGenerator rg = new ReportGenerator(accessedUser);
                //Creates start date
                java.util.Date startDate = (java.util.Date)startTime.getValue();
                Date start = new Date(startDate.getYear()+1900,startDate.getMonth()+1,startDate.getDate(),0,0);
                //Created end date
                java.util.Date endDate = (java.util.Date)endTime.getValue();
                Date end = new Date(endDate.getYear()+1900,endDate.getMonth()+1,endDate.getDate(),23,59);
                //Creates JPanel with finished report
                JPanel finishedReport = rg.generate(start,end);
                //Displays report
                int result = JOptionPane.showConfirmDialog(null,finishedReport,"Report",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
            }
        });
        report.add(generate);


        //Payment Info Tab
        JPanel paymentInfo = new JPanel();
        paymentInfo.setLayout(new BoxLayout(paymentInfo,BoxLayout.Y_AXIS));
        paymentInfo.add(Box.createRigidArea(new Dimension(1,200)));
        JPanel paymentInformation = new JPanel();
        //Payment information selector
        JComboBox<PaymentInfo> paymentSelector = new JComboBox(accessedUser.getPaymentInfo().toArray());
        paymentInformation.add(paymentSelector);
        JPanel paymentOptions = new JPanel();
        //Create new card button
        JButton newPayment = new JButton("Add New Card");
        newPayment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Creates info panel with all needed fields
                JPanel info = new JPanel();
                info.add(new JLabel("Cardholder Name:"));
                JTextField cardholderName = new JTextField();
                info.add(cardholderName);
                info.add(new JLabel("Card Name:"));
                JTextField cardName = new JTextField();
                info.add(cardName);
                info.add(new JLabel("Card Number:"));
                JTextField cardNumber = new JTextField();
                info.add(cardNumber);
                info.add(new JLabel("Card Pin:"));
                JPasswordField cardPin = new JPasswordField();
                info.add(cardPin);
                info.add(new JLabel("Expiry Date"));
                JSpinner time = new JSpinner(new SpinnerDateModel());
                new JSpinner.DateEditor(time);
                time.setEditor(new JSpinner.DateEditor(time,"MM/yyyy"));
                info.add(time);
                //Displays panel to get input
                int result = JOptionPane.showOptionDialog(null,info,"New Card",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                if(result == 0){
                    //Creates expiry Date
                    java.util.Date expiry = (java.util.Date)time.getValue();
                    Date expiryDate = new Date(expiry.getYear()+1900,expiry.getMonth()+1,0,0,0);
                    //Creates new card
                    Card newCard = new Card(cardName.getText(),cardNumber.getText(),Integer.parseInt(cardPin.getText()),expiryDate,cardholderName.getText());
                    //Adds card and refreshes selector
                    accessedUser.getPaymentInfo().add(newCard);
                    paymentSelector.removeAllItems();
                    for(Object currentInfo: accessedUser.getPaymentInfo().toArray()){
                        paymentSelector.addItem((PaymentInfo)currentInfo);
                    }
                }
            }
        });
        //View payment info button
        JButton viewPayment = new JButton("View Payment Details");
        viewPayment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!paymentSelector.getSelectedItem().toString().equals("Cash")) {
                    //Displays info on a JPanel
                    Card card = (Card)paymentSelector.getSelectedItem();
                    JPanel info = new JPanel();
                    info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));
                    info.add(new JLabel("Cardholder Name: " + card.getHolder()));
                    info.add(new JLabel("Card Name: " + card.getName()));
                    info.add(new JLabel("Card Number: " + card.getNumber()));
                    info.add(new JLabel("Card Pin: " + card.getPin()));
                    info.add(new JLabel("Expiry Date: " + card.getExpiry().getTextExpiry()));
                    JSpinner time = new JSpinner(new SpinnerDateModel());
                    //Displays JPanel for person to see
                    int result = JOptionPane.showOptionDialog(null, info, card.getName() + " Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                }
            }
        });
        //Delete Payment Info Button
        JButton deletePayment = new JButton("Delete");
        deletePayment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Gets selected payment info
                PaymentInfo selected = (PaymentInfo)paymentSelector.getSelectedItem();
                //Checks if its in use by a transaction
                boolean inUse = false;
                for(Object currentTransaction:accessedUser.getTransactions().getTransactions().toArray()){
                    Transaction transaction = (Transaction)currentTransaction;
                    if(transaction.getPayment().equals(paymentSelector.getSelectedItem())){
                        inUse = true;
                    }
                }
                //Cash is not allowed to be deleted :D
                if(paymentSelector.getSelectedItem().toString().equals("Cash")){
                    JOptionPane.showConfirmDialog(null,"Cash cannot be deleted.","Error",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null);
                }else if(!inUse){
                    //If its not being used, payment info is deleted after confirmation
                    int result = JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this?","Confirm",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE,null);
                    if(result == 0) {
                        accessedUser.getPaymentInfo().remove(paymentSelector.getSelectedItem());
                    }
                }else{
                    //If its in use, it will not be deleted
                    JOptionPane.showConfirmDialog(null,"Payment Type in Use!","Error",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null);
                }
            }
        });
        //Adds everything to payment info pane
        paymentOptions.add(newPayment);
        paymentOptions.add(viewPayment);
        paymentOptions.add(deletePayment);
        paymentInfo.add(paymentInformation);
        paymentInfo.add(Box.createRigidArea(new Dimension(1,10)));
        paymentInfo.add(paymentOptions);
        paymentInfo.add(Box.createVerticalGlue());


        //Account Details Tab
        JPanel account = new JPanel();
        account.setLayout(new BoxLayout(account,BoxLayout.Y_AXIS));
        //Data panel
        JPanel data = new JPanel();
        //Locations and Categories Display
        JComboBox<Location> locations = new JComboBox(accessedUser.getLocations().toArray());
        JComboBox<Category> categories = new JComboBox(accessedUser.getCategories().toArray());
        //Delete Location button
        JButton delLocation = new JButton("Delete");
        delLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Checks if the location is in use
                boolean inUse = false;
                for(Transaction currentTrans : accessedUser.getTransactions().getTransactions()){
                    if(currentTrans.getLocation().equals(locations.getSelectedItem())){
                        inUse = true;
                    }
                }
                //If its not in use, delete it
                if(!inUse){
                    accessedUser.getLocations().remove(locations.getSelectedItem());
                    locations.removeAllItems();
                    for(Object currentLocation:accessedUser.getLocations().toArray()){
                        locations.addItem((Location)currentLocation);
                    }
                }else{
                    //Otherwise, dont delete it
                    int result = JOptionPane.showConfirmDialog(null,new JLabel("Location is in Use!"), "Deletion Error!",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null);
                }
            }
        });
        //Delete Category button
        JButton delCategory = new JButton("Delete");
        delCategory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Checks if category is in use
                boolean inUse = false;
                for(Transaction currentTrans : accessedUser.getTransactions().getTransactions()){
                    if(currentTrans.getCategory().equals(categories.getSelectedItem())){
                        inUse = true;
                    }
                }
                //If its not, delete it
                if(!inUse){
                    accessedUser.getCategories().remove(categories.getSelectedItem());
                    categories.removeAllItems();
                    for(Object currentCategory:accessedUser.getCategories().toArray()){
                        categories.addItem((Category)currentCategory);
                    }
                }else{
                    //Otherwise, dont delete it
                    int result = JOptionPane.showConfirmDialog(null,new JLabel("Category is in Use!"), "Deletion Error!",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null);
                }
            }
        });
        //Add in locations and categories selectors and deleting
        account.add(Box.createRigidArea(new Dimension(0,150)));
        data.add(new JLabel("Locations:"));
        data.add(locations);
        data.add(delLocation);
        data.add(Box.createRigidArea(new Dimension(10,0)));
        data.add(new JLabel("Categories:"));
        data.add(categories);
        data.add(delCategory);
        account.add(data);
        //Account data changing
        JPanel changeAccData = new JPanel();
        //Username changing button
        JButton changeUsername = new JButton("Change Username");
        changeUsername.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Creates panel with new username and password confirmation
                JPanel changeUsername = new JPanel();
                changeUsername.setLayout(new BoxLayout(changeUsername,BoxLayout.Y_AXIS));
                changeUsername.add(new JLabel("Password:"));
                JPasswordField passwordCheck = new JPasswordField();
                changeUsername.add(passwordCheck);
                changeUsername.add(new JLabel("New Username:"));
                JTextField newUsername = new JTextField(accessedUser.getUsername());
                changeUsername.add(newUsername);
                //Displays pane to get input
                int result = JOptionPane.showOptionDialog(null,changeUsername,"Change Username", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                if(result == 0){
                    //Checks password
                    if(accessedUser.login(passwordCheck.getText())){
                        //Changes username and updates text
                        accessedUser.editUsername(newUsername.getText());
                        currentUser = newUsername.getText();
                        welcome.setText(currentUser);
                        tab1.setText(currentUser);
                    }else{
                        //Password wrong?, don't change
                        JOptionPane.showConfirmDialog(null,"Password Incorrect!","Password Error",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null);
                    }
                }
            }
        });
        //Change password button
        JButton changePass = new JButton("Change Password");
        changePass.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Creates panel to display new password and check old one
                JPanel changePassword = new JPanel();
                changePassword.setLayout(new BoxLayout(changePassword,BoxLayout.Y_AXIS));
                changePassword.add(new JLabel("Password:"));
                JPasswordField passwordCheck = new JPasswordField();
                changePassword.add(passwordCheck);
                changePassword.add(new JLabel("New Password:"));
                JPasswordField newPassword = new JPasswordField();
                changePassword.add(newPassword);
                //Displays panel to get user input
                int result = JOptionPane.showOptionDialog(null,changePassword,"Change Password", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);
                if(result == 0){
                    //Checks password
                    if(accessedUser.login(passwordCheck.getText())){
                        //Change password
                        accessedUser.changePassword(newPassword.getText());
                    }else{
                        //Password wrong?, don't change
                        JOptionPane.showConfirmDialog(null,"Password Incorrect!","Password Error",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null);
                    }
                }
            }
        });
        //Adds account editing to pane
        changeAccData.add(changeUsername);
        changeAccData.add(changePass);
        account.add(changeAccData);
        account.add(Box.createVerticalGlue());

        //Tabbed Pane Set Up
        recurring.setSize(100,100);
        report.setSize(100,100);
        paymentInfo.setSize(100,100);
        account.setSize(100,100);
        panels.addTab(currentUser,null,userPanel,"Hello, " + currentUser);
        panels.addTab("Transactions",null,transactions,"Your Transaction History");
        panels.addTab("Timed Transactions",null,recurring,"Repeated Transactions");
        panels.addTab("Reports",null,report,"Report Generator");
        panels.addTab("Payment Info",null,paymentInfo,"Saved Payment Types & Info");
        panels.addTab("Account",null,account,"Your Account");
        Font dx = new Font("DengXian",0,20);
        tab1.setPreferredSize(new Dimension(175,25));
        tab2.setPreferredSize(new Dimension(175,25));
        tab3.setPreferredSize(new Dimension(175,25));
        tab4.setPreferredSize(new Dimension(175,25));
        tab5.setPreferredSize(new Dimension(175,25));
        tab6.setPreferredSize(new Dimension(175,25));
        panels.setEnabledAt(1,false);
        panels.setEnabledAt(2,false);
        panels.setEnabledAt(3,false);
        panels.setEnabledAt(4,false);
        panels.setEnabledAt(5,false);
        tab1.setFont(dx);
        tab2.setFont(dx);
        tab3.setFont(dx);
        tab4.setFont(dx);
        tab5.setFont(dx);
        tab6.setFont(dx);
        panels.setTabComponentAt(0,tab1);
        panels.setTabComponentAt(1,tab2);
        panels.setTabComponentAt(2,tab3);
        panels.setTabComponentAt(3,tab4);
        panels.setTabComponentAt(4,tab5);
        panels.setTabComponentAt(5,tab6);
        //Tab Refresher
        panels.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JTabbedPane pane = (JTabbedPane)e.getSource();
                int index = pane.getSelectedIndex();
                //Refreshes tab we switched to
                switch(index){
                    case 1:
                        transactionPane.removeAll();
                        transactionPane.add(Box.createRigidArea(new Dimension(0,5)));
                        transactionPane.add(addNew);
                        transactionPane.add(Box.createRigidArea(new Dimension(0,10)));
                        timedTransactions.removeAllItems();
                        for(Object currentTransaction:accessedUser.getRecurringTransactions().toArray()){
                            RecurrentTransaction timed = (RecurrentTransaction) currentTransaction;
                            timed.update(accessedUser.getTransactions());
                        }
                        accessedUser.getTransactions().outputTransactions(transactionPane,accessedUser);
                        transactionPane.revalidate();
                        transactionPane.repaint();
                        break;
                    case 2:
                        timedTransactions.removeAllItems();
                        for(Object currentTransaction:accessedUser.getRecurringTransactions().toArray()){
                            timedTransactions.addItem((RecurrentTransaction) currentTransaction);
                        }
                        timedTransactions.revalidate();
                        timedTransactions.repaint();
                        break;
                    case 3:
                        break;
                    case 4:
                        paymentSelector.removeAllItems();
                        for(Object currentInfo:accessedUser.getPaymentInfo().toArray()){
                            paymentSelector.addItem((PaymentInfo)currentInfo);
                        }
                        break;
                    case 5:
                        locations.removeAllItems();
                        for(Object currentLocation:accessedUser.getLocations().toArray()){
                            locations.addItem((Location)currentLocation);
                        }
                        categories.removeAllItems();
                        for(Object currentCategory:accessedUser.getCategories().toArray()){
                            categories.addItem((Category)currentCategory);
                        }
                        break;
                }
            }
        });

        //Adds everything to frame and sets close operation
        frame.add(panels);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //Adds Listener for closing window to save
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                //If User is present
                if(accessedUser != null) {
                    try {
                        //Tries to save it before closing
                        JFileChooser fc = new JFileChooser();
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        fc.showOpenDialog(frame);
                        FileOutputStream fos = new FileOutputStream(fc.getSelectedFile() + "/" + currentUser + ".account");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(accessedUser);
                        oos.flush();
                        oos.close();
                        //Encrypts it
                        Encrypter encrypter = new Encrypter(accessedUser.getPassword());
                        encrypter.encrypt(new File(fc.getSelectedFile() + "/" + currentUser + ".account"));
                    } catch (FileNotFoundException a) {
                        System.out.println("Error File Not Found - Window Closing");
                    } catch (IOException a) {
                        System.out.println("Error - IO - Window Closing");
                    }
                }
            }
        });
    }
}