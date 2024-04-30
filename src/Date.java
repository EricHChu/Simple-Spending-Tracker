/**
 * Date Class
 * @author Eric Chu
 * @version Jun 2023
 */

import java.io.Serializable;
import java.util.Arrays;
public class Date implements Serializable{
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    /**
     * Constructor for date
     * @param year Year of date
     * @param month Month of date
     * @param day Day of date
     * @param hour Hour of date
     * @param minute Minute of Date
     */
    public Date(int year,int month,int day,int hour,int minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Creates text to be displayed by transactions
     * @return String to display on JLabel
     */
    public String getText(){
        String text = "<html>" + year + "<br>";
        String month = "JanFebMarAprMayJunJulAugSepOctNovDec";
        text = text + month.substring((this.month - 1)*3, this.month*3) + " " + day + "<br>";
        if(this.hour > 12){
            text = text + (this.hour - 12) + ":" + minute + "pm</html>";
            return text;
        }
        text = text + this.hour + ":" + minute + "am</html>";
        return text;
    }

    /**
     * Creates text to be displayed by Card (Expiry)
     * @return Text date to display on JComponent
     */
    public String getTextExpiry(){
        String month = "JanFebMarAprMayJunJulAugSepOctNovDec";
        String text = month.substring((this.month - 1)*3, this.month*3);
        text = text + " " + year;
        return text;
    }

    /**
     * Adds time to current date and creates new Date from that
     * @param days Days to add
     * @return New Date with added time
     */
    public Date addTime(int days){
        int[] time = new int[]{year,month,day};
        addDays(time,days);
        return new Date(time[0],time[1],time[2],0,0);
    }

    /**
     * Helper class to add days
     * @param currentTime Time to work with
     * @param days Days left to add
     * @return Array with year, month and day after adding
     */
    public int[] addDays(int[] currentTime, int days){
        //Checks Month/Days in current month
        if(currentTime[1] == 2){
            //Checks if theres more than a month to add
            if(days > 28){
                //adds month
                currentTime[1]++;
                //removes days
                days = days - 28;
            }else{
                //checks if adding days will go past the month
                if(days + currentTime[2] > 28){
                    //adds month
                    currentTime[1]++;
                    //adds days into next month
                    currentTime[2] = currentTime[2] + days - 28;
                }else{
                    //adds days
                    currentTime[2] = currentTime[2] + days;
                    //returns time
                    return currentTime;
                }
            }
        }else if(Arrays.asList(4,6,9,11).contains(currentTime[1])){
            //Does it but for 30 day months
            if(days > 30){
                currentTime[1]++;
                days = days - 30;
            }else{
                if(days + currentTime[2] > 30){
                    currentTime[1]++;
                    currentTime[2] = currentTime[2] + days - 30;
                }else{
                    currentTime[2] = currentTime[2] + days;
                    return currentTime;
                }
            }
        }else if(currentTime[1] == 12){
            //Checks specifically for the new year
            if(days > 31){
                //If it passes december, its january!
                currentTime[0]++;
                currentTime[1] = 1;
                days = days - 31;
            }else{
                if(days + currentTime[2] > 31){
                    //Passing december goes into the new year
                    currentTime[0]++;
                    currentTime[1] = 1;
                    currentTime[2] = currentTime[2] + days - 30;
                }else{
                    currentTime[2] = currentTime[2] + days;
                    return currentTime;
                }
            }
        }else{
            //Same procedure for 31 day months
            if(days > 31){
                currentTime[1]++;
                days = days - 31;
                //If the current day might be out of bounds for the next month, moves it into days
                if(currentTime[2] > 28){
                    currentTime[2] = currentTime[2] - (28 - currentTime[2]);
                    days = days + 28 - currentTime[2];
                }
            }else{
                if(days + currentTime[2] > 31){
                    currentTime[1]++;
                    currentTime[2] = currentTime[2] + days - 31;
                    //If the current day might be out of bounds for the next month, moves it into days
                    if(currentTime[2] > 28){
                        currentTime[2] = currentTime[2] - (28 - currentTime[2]);
                        days = days + 28 - currentTime[2];
                    }
                }else{
                    currentTime[2] = currentTime[2] + days;
                    return currentTime;
                }
            }
        }
        //returns new array with added days
        return addDays(currentTime,days);
    }

    /**
     * Calculates minutes since year 0000
     * @return Minutes since year 0000
     */
    public int getNumVal(){
        int val = 0;
        val = val + (year*365*24*60);
        val = val + (month*30*24*60);
        val = val + (day*24*60);
        val = val + (hour*60);
        val = val + minute;
        return val;
    }

    /**
     * Compares two dates
     * @param other Date to compare to
     * @return 1 if self is older, 0 if its the same, -1 if self is younger
     */
    public int compare(Date other){
        if(this.getNumVal() > other.getNumVal()){
            return 1;
        }else if(this.getNumVal() == other.getNumVal()){
            return 0;
        }
        return -1;
    }
}
