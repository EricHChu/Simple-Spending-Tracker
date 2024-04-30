/**
 * Category Class
 * @author Eric Chu
 * @version Jun 2023
 */

import java.awt.Color;
import java.io.Serializable;
public class Category implements Setting, Serializable{
    private String name;
    private Color color;

    /**
     * Constructor for Category
     * @param name Name of the category
     */
    public Category(String name){
        this.name = name;
        this.color = Color.MAGENTA;
    }

    /**
     * Constructor for Category with Color
     * @param name Name of the category
     * @param color Color of the category
     */
    public Category(String name, Color color){
        this.name = name;
        this.color = color;
    }

    /**
     * Gets name of the category
     * @return Name of the category
     */
    public String getName(){
        return this.name;
    }

    /**
     * Sets name of the category
     * @param newVal Name to set the category to
     */
    public void setName(String newVal){
        this.name = newVal;
    }

    /**
     * Gets color of the category
     * @return Color of the category
     */
    public Color getColor(){
        return this.color;
    }

    /**
     * Sets color of the category
     * @param newVal Color to set category to
     */
    public void setColor(Color newVal){
        this.color = newVal;
    }

    /**
     * Converts Category to String
     * @return Name of the category
     */
    public String toString(){
        return this.name;
    }
}
