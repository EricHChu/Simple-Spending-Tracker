/**
 * Report Generator for User
 * @author Eric Chu
 * @version Jun 2023
 */

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
public class ReportGenerator {
    private User reportingUser;

    /**
     * Constructor for Report Generator
     * @param user User to generate for
     */
    public ReportGenerator(User user){
        this.reportingUser = user;
    }

    /**
     * Generates a report for spending during a time frame
     * @param start Start time to analyze
     * @param end end time to analyze
     * @return JPanel with report
     */
    public JPanel generate(Date start,Date end){
        //Panel for report
        JPanel report = new JPanel();
        JPanel text = new JPanel();
        report.setLayout(new BoxLayout(report,BoxLayout.PAGE_AXIS));
        text.setLayout(new GridLayout(0,1,0,10));
        //Report Text Section:
        ArrayList<Transaction> reportingTransactions = new ArrayList<>();
        Transaction[] transactions = reportingUser.getTransactions().getTransactions().toArray(new Transaction[reportingUser.getTransactions().size()]);
        //Sorts according to date
        Arrays.sort(transactions,new TransactionComparator("Date",false,reportingUser));
        //Gets transactions within dates given
        for(Transaction currentTransaction:transactions){
            if(currentTransaction.getTime().getNumVal() > start.getNumVal() && currentTransaction.getTime().getNumVal() < end.getNumVal()){
                reportingTransactions.add(currentTransaction);
            }
        }
        Transaction[] readingTransactions = reportingTransactions.toArray(new Transaction[reportingTransactions.size()]);
        //Gets total expenditure
        double total = 0;
        for(Transaction currentTransaction:readingTransactions){
            total = total + currentTransaction.getPrice();
        }
        //Adds to text
        text.add(new JLabel("You spent $" + total + " total!"));
        //Gets average spending according to time frame and adds to text
        int timeMin = end.getNumVal() - start.getNumVal();
        if(timeMin > 1576800){
            text.add(new JLabel("That's an average of $" + (total/(timeMin/525600)) + " per year!"));
        }if(timeMin > 129600){
            text.add(new JLabel("That's an average of $" + (total/(timeMin/43200)) + " per month!"));
        }if(timeMin > 4320){
            text.add(new JLabel("That's an average of $" + (total/(timeMin/1440)) + " per day!"));
        }
        //Gets amount spent on each location/company
        Double[] locationAmounts = new Double[reportingUser.getLocations().size()];
        Arrays.fill(locationAmounts,0.0);
        for(Transaction currentTransaction:readingTransactions){
            locationAmounts[reportingUser.getLocations().lastIndexOf(currentTransaction.getLocation())] = locationAmounts[reportingUser.getLocations().lastIndexOf(currentTransaction.getLocation())] + currentTransaction.getPrice();
        }
        //Gets amount spent on each category
        Double[] categoryAmounts = new Double[reportingUser.getCategories().size()];
        Arrays.fill(categoryAmounts,0.0);
        for(Transaction currentTransaction:readingTransactions){
            categoryAmounts[reportingUser.getCategories().lastIndexOf(currentTransaction.getCategory())] = categoryAmounts[reportingUser.getCategories().lastIndexOf(currentTransaction.getCategory())] + currentTransaction.getPrice();
        }
        //Gets highest spending location
        int highestLocation = 0;
        double lastHighestLocAmount = locationAmounts[0];
        for(int i = 1;i < locationAmounts.length;i++){
            if(lastHighestLocAmount < locationAmounts[i]){
                highestLocation = i;
                lastHighestLocAmount = locationAmounts[i];
            }
        }
        //Adds data to text
        text.add(new JLabel("You spent $" + lastHighestLocAmount + ", which is the most at " + reportingUser.getLocations().get(highestLocation).getName() + "!"));
        text.add(new JLabel("It's %" + Math.ceil((lastHighestLocAmount/total)*100) + " of your total spending!"));
        //If it exists, also finds the second-highest spending location and adds to text
        if(locationAmounts.length > 1){
            locationAmounts[highestLocation] = -1.0;
            highestLocation = 0;
            lastHighestLocAmount = locationAmounts[0];
            for(int i = 1;i < locationAmounts.length;i++){
                if(lastHighestLocAmount < locationAmounts[i]){
                    highestLocation = i;
                    lastHighestLocAmount = locationAmounts[i];
                }
            }
            text.add(new JLabel("You spent $" + lastHighestLocAmount + ", which is the second most at " + reportingUser.getLocations().get(highestLocation).getName() + "!"));
            text.add(new JLabel("It's %" + Math.ceil((lastHighestLocAmount/total)*100) + " of your total spending!"));
            //And if possible the third-highest spending location as well and adds to text
        }if(locationAmounts.length > 2){
            locationAmounts[highestLocation] = -1.0;
            highestLocation = 0;
            lastHighestLocAmount = locationAmounts[0];
            for(int i = 1;i < locationAmounts.length;i++){
                if(lastHighestLocAmount < locationAmounts[i]){
                    highestLocation = i;
                    lastHighestLocAmount = locationAmounts[i];
                }
            }
            text.add(new JLabel("You spent $" + lastHighestLocAmount + ", which is the third most at " + reportingUser.getLocations().get(highestLocation).getName() + "!"));
            text.add(new JLabel("It's %" + Math.ceil((lastHighestLocAmount/total)*100) + " of your total spending!"));
        }
        //Gets highest spending category
        int highestCategory = 0;
        double lastHighestCatAmount = categoryAmounts[0];
        for(int i = 1;i < categoryAmounts.length;i++){
            if(lastHighestCatAmount < categoryAmounts[i]){
                highestCategory = i;
                lastHighestCatAmount = categoryAmounts[i];
            }
        }
        //Adds to text
        text.add(new JLabel("You spent $" + lastHighestCatAmount + ", which is the most on " + reportingUser.getCategories().get(highestCategory).getName() + "!"));
        text.add(new JLabel("It's %" + Math.ceil((lastHighestCatAmount/total)*100) + " of your total spending!"));
        //If possible gets second-highest spending category ands adds to text
        if(categoryAmounts.length > 1){
            categoryAmounts[highestCategory] = -1.0;
            highestCategory = 0;
            lastHighestCatAmount = categoryAmounts[0];
            for(int i = 1;i < categoryAmounts.length;i++){
                if(lastHighestCatAmount < categoryAmounts[i]){
                    highestCategory = i;
                    lastHighestCatAmount = categoryAmounts[i];
                }
            }
            text.add(new JLabel("You spent $" + lastHighestCatAmount + ", which is the second most at " + reportingUser.getCategories().get(highestCategory).getName() + "!"));
            text.add(new JLabel("It's %" + Math.ceil((lastHighestCatAmount/total)*100) + " of your total spending!"));
            //If possible gets the third-highest spending category and adds to text
        }if(categoryAmounts.length > 2){
            categoryAmounts[highestCategory] = -1.0;
            highestCategory = 0;
            lastHighestCatAmount = categoryAmounts[0];
            for(int i = 1;i < categoryAmounts.length;i++){
                if(lastHighestCatAmount < categoryAmounts[i]){
                    highestCategory = i;
                    lastHighestCatAmount = categoryAmounts[i];
                }
            }
            text.add(new JLabel("You spent $" + lastHighestCatAmount + ", which is the third most at " + reportingUser.getCategories().get(highestCategory).getName() + "!"));
            text.add(new JLabel("It's %" + Math.ceil((lastHighestCatAmount/total)*100) + " of your total spending!"));
        }
        //Organizes list according to price
        reportingTransactions.sort(new TransactionComparator("Price",false,reportingUser));
        text.add(Box.createRigidArea(new Dimension(1,1)));
        text.add(new JLabel("Your most expensive purchase was:"));
        report.add(text);
        report.add(Box.createRigidArea(new Dimension(1,15)));
        //Gets and adds most expensive purchase
        reportingTransactions.get(0).display(report,reportingUser);
        return report;
    }
}
