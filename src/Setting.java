/**
 * Setting Interface
 */

import java.awt.Color;
interface Setting {

    /**
     * Gets name of the setting
     * @return Name of setting
     */
    public String getName();

    /**
     * Sets name of the setting
     * @param newVal Name to change setting to
     */
    public void setName(String newVal);

    /**
     * Gets color of the setting
     * @return Color of the setting
     */
    public Color getColor();

    /**
     * Sets color of the setting
     * @param newVal Color to set to
     */
    public void setColor(Color newVal);
}
