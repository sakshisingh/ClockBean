/*
**************************************************************
Name: Sakshi Singh
SCU ID: W1041542
Course: COEN235
Assignment: #1 JavaBeans --> Clock Bean
Date of Submission: 10/08/2013
Current File: ClockBeanInfo.java
**************************************************************
*/

// ClockBeanInfo Class
// ClockBeanInfo.java
package HTPJB.Clock;

// Imports
import java.beans.*;

public class ClockBeanInfo extends SimpleBeanInfo {
    // Get the appropriate icon

    @Override
    public java.awt.Image getIcon(int iconKind) {
        // Check if 16 bit image or 32 bit image is supported.
        if (iconKind == BeanInfo.ICON_COLOR_16x16) {
            java.awt.Image img = loadImage("ClockIcon16.gif");
            return img;
        }   //End of if
        if (iconKind == BeanInfo.ICON_COLOR_32x32) {
            java.awt.Image img = loadImage("ClockIcon32.gif");
            return img;
        }   //End of if
        return null;
    }   //End of getIcon() method
}   //End of class ClockBeanInfo
