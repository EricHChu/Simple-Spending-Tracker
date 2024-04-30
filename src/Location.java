/**
 * Location Class
 * @author Eric Chu
 * @version Jun 2023
 */

import java.awt.Color;
import java.io.Serializable;
public class Location implements Setting, Serializable {
    private String name;
    private Color color;

    /**
     * Constructor for Location Class
     * @param name Name of the location
     */
    public Location(String name){
        this.name = name;
        this.color = Color.CYAN;
    }

    /**
     * Constructor for Location Class with Color
     * @param name Name of the location
     * @param color Color of the location
     */
    public Location(String name, Color color){
        this.name = name;
        this.color = color;
    }

    /**
     * Gets name of the location
     * @return Name of the Location
     */
    public String getName(){
        return this.name;
    }

    /**
     * Converts Location to String
     * @return Name of the Location
     */
    public String toString(){
        return this.name;
    }

    /**
     * Sets name of the Location to given Name
     * @param newVal Name to set the location to
     */
    public void setName(String newVal){
        this.name = newVal;
    }

    /**
     * Gets color of the Location
     * @return Color of the Location
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Sets Color of the location
     * @param newVal Color to set the Location ot
     */
    public void setColor(Color newVal){
        this.color = newVal;
    }
}
