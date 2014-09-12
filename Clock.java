
// Clock Bean Class
// Clock.java
package HTPJB.Clock;

// Imports
import java.awt.*;
import java.io.Serializable;
import java.util.Calendar;

public class Clock extends Canvas implements Serializable, Runnable {
    // state & properties

    // Transient are not serialiezed and are not converted to byte stream.
    private transient Image offImage;
    private transient Graphics offGrfx;
    private transient Thread clockThread;
    private boolean raised;     //This property gives 3-D effect around bean's border.
    private boolean digital;    //This property is set to TRUE if the clock is in Digital mode. FOr analog mode it is false.

    // Constructors
    public Clock() {
        //Call constructor Clock(boolean r, boolean d) to set raised and digital as false.
        this(false, false);
    }   //End of default constructor

    public Clock(boolean r, boolean d) {
        // Allow the superclass constructor to do its thing
        super();

        // Set properties
        raised = r;
        digital = d;
        /*
         * Code added by Sakshi Singh.
         */
        //Setting the background
        setBackground(Color.lightGray);

        //Setting default size
        setSize(200, 200);

        //Creating and starting the thread
        clockThread = new Thread(this);
        clockThread.start();
    }   //End of constructor

    // Accessor methods
    //Check if raised is set to true or false
    public boolean isRaised() {
        return raised;
    }   //End of isRasied() method

    //Set raised to true or false which ever value is passed as argument
    public void setRaised(boolean r) {
        raised = r;
        repaint();
    }   //End of setRasied() method

    // Check if digital is set to true or false
    public boolean isDigital() {
        return digital;
    }   //End of isDigital() method

    //Set digital to true or false which ever value is passed as argument
    public void setDigital(boolean d) {
        digital = d;
        repaint();
    }   //End of setDigital() method

    // Other public methods
    @SuppressWarnings("static-access")
    @Override
    /*
     * Code added by Sakshi Singh.
     */
    //Overriding run method
    public void run() {
        //Clock should be always running.
        while (true) {
            repaint();
            //Wait for 1 second and then repaint.
            try {
                clockThread.currentThread().sleep(1000);
            } //End of try block
            catch (InterruptedException e) {
            }   //End of catch block
        }   //End of while loop.
    }   //End of run() method

    @Override
    public void update(Graphics g) {
        paint(g);
    }   //End of update() method

    @Override
    public synchronized void paint(Graphics g) {
        Dimension d = getSize();

        // Create the offscreen graphics context
        //......
        /*
         * Code added by Sakshi Singh.
         */
        // If no graphics added, Create a new image with dimension d size and assign to graphics object.
        //This graphics object is useed to paint and repaint.
        if (offGrfx == null) {
            offImage = createImage(d.width, d.height);
            offGrfx = offImage.getGraphics();
        }
        // Paint the background with 3D effects
        //...........
        /*
         * Code added by Sakshi Singh.
         */
        //Set graphics color same as background color. i.e. light gray.
        offGrfx.setColor(getBackground());
        //Fill rectangle in the graphic with width and height as set earlier.
        offGrfx.fillRect(0, 0, d.width, d.height);
        //Draw boundary for a 3D rectangle
        offGrfx.draw3DRect(0, 0, d.width, d.height, raised);
        //Set color as current foreground color.
        offGrfx.setColor(getForeground());

        // Paint the clock
        // If digital = true, draw digital clock else draw analog clock
        if (digital) {
            drawDigitalClock(offGrfx);
        } //End of if
        else {
            drawAnalogClock(offGrfx);
        }   //End of else

        // Paint the image onto the screen
        g.drawImage(offImage, 0, 0, null);
    }   //End of paint() method

    // Private support methods
    private void drawAnalogClock(Graphics g) {
        //...................
        /*
         * Code added by Sakshi Singh.
         */
        Dimension d = getSize();
        g.setFont(getFont());

        //Set diameter of the circular clock same as width or height (which ever is smaller) of the rectangle
        int diameter = (d.width <= d.height ? d.width : d.height);
        //Set top left point of circle as 0,0 --> Corner of the rectangle.
        //Point circleTopLeft = new Point(0,0);
        Point circleTopLeft = new Point((d.width > d.height ? Math.abs((d.width - d.height) / 2) : 0), (d.height > d.width ? Math.abs((d.width - d.height) / 2) : 0));
        //Set center point as width/2, height/2
        Point center = new Point(d.width / 2, d.height / 2);

        // Graphics2D object helps set stroke size.
        Graphics2D g2D = (Graphics2D) g;
        // Draw the clock shape
        //...............
        /*
         * Code added by Sakshi Singh.
         */
        //Draw circular clock with white background
        g.setColor(Color.white);
        g.fillArc(circleTopLeft.x, circleTopLeft.y, diameter, diameter, 0, 360);

        //Draw circumference/boundary of the clock with black color.
        g.setColor(Color.BLACK);
        g.drawArc(circleTopLeft.x, circleTopLeft.y, diameter, diameter, 0, 360);

        // Draw the hour marks and numbers
        //..........
        //Loop through numbers 1 to 60
        //For every hour, print number i.e when i = 5 print 1, i=10 print 2, ..., i=60 print 12.
        //For all other numbers, 1,2,3,4,6,7,8,9 etc print a "." DOT.
        for (int markLoopCounter = 1; markLoopCounter <= 60; markLoopCounter++) {
            //Calculate i/5 value. if i/5 = 0, in case of 60, number to be printed is 12.
            int num = (markLoopCounter / 5 == 0 ? 12 : markLoopCounter / 5);

            // Set the length within clock where numbers/ dots will be printed.
            //Print numbers in a circle with smaller radius than dots.
            double markLength = 0.45 * diameter;
            if (markLoopCounter % 5 == 0) {
                markLength = 0.375 * diameter;
            }   //End of if
            // Calculate angle in radians for each second/ value of i.
            double angle = (Math.PI / 180) * 6 * markLoopCounter;
            // Calculate x an y cordiante for each number/ dot to be printed.
            int startX = (center.x + ((int) (markLength * Math.sin(angle))) - 1);
            int startY = (center.y - ((int) (markLength * Math.cos(angle))) + 1);
            //Point where number/dot will be printed
            Point markPosition = new Point(startX, startY);
            // Set the color to blue.
            g2D.setColor(Color.blue);
            // Set stroke size as 1 for printed numbers.
            g2D.setStroke(new BasicStroke(1));

            // If i%5 is not 0, print dot else print num.
            if (markLoopCounter % 5 != 0) {
                g2D.drawString(".", markPosition.x, markPosition.y);
            } //End of if
            else {
                g2D.drawString(Integer.toString(num), markPosition.x, markPosition.y);
            }   //End of else
        }   //End of for loop.

        // Draw the hour hand
        //...............
        /*
         * Code added by Sakshi Singh.
         * Length of hourHand = 0.25 * radius
         * Length of minute hand = 0.4 * radius
         * Length of second hand = 0.35 * radius
         */
        
        //Get second from current date.
        int second = Calendar.getInstance().get(Calendar.SECOND);
        //Get minutes from current date.        
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        //Get hours from current date. we use 12 hour clock system since in analog clock we can show 12 hours only.
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        
        //Setting length of hour hand as 1/4th of clock diameter size.
        double hourHandLength = 0.25 * diameter;
        // Calculating angle swept by hour hand in radians as sin and cos functions need angle in radians.
        // In 1 hour, hour hand sweeps 30 degrees, In 1 minute it sweeps (30/60)= 0.5 degrees. 
        // In 1 second it sweeps 0.5/60 = 0.00833 degrees
        // Also, total number of seconds for an hour hand in current time is hour*60*60 + minutes*60 + seconds
        double hourAngle = (Math.PI / 180) * 0.0083333 * ((hour*60*60) + (minute*60) + second);
        // Calculating x and y end points of hour hand.
        int hourEndX = center.x + ((int) (hourHandLength * Math.sin(hourAngle)));
        int hourEndY = center.y - ((int) (hourHandLength * Math.cos(hourAngle)));
        //Setting hour hand end point.
        Point hourEndPoint = new Point(hourEndX, hourEndY);

        // Set color of the hour hand as black
        g2D.setColor(Color.black);
        // Set stroke size as 4 for thicker hand.
        g2D.setStroke(new BasicStroke(4));
        //Draw the hour hand.
        g2D.drawLine(center.x, center.y, hourEndPoint.x, hourEndPoint.y);

        // Draw the minute hand
        //////...................
        
        
        //Setting length of minute hand as 0.4 times of clock diameter size.
        double minuteHandLength = 0.4 * diameter;
        //Calculating angle swept by minute hand in radians.
        // In 1 minute, minute hand sweeps 6 degrees. In 1 second minute hand sweeps 0.1 degrees
        //Also, total number of seconds for a minute hand in current time is minutes*60 + seconds
        double minuteAngle = (Math.PI / 180) * 0.1 * (minute*60 + second);
        // Calculating x and y end points of minute hand.
        int minuteEndX = center.x + ((int) (minuteHandLength * Math.sin(minuteAngle)));
        int minuteEndY = center.y - ((int) (minuteHandLength * Math.cos(minuteAngle)));
        //Setting minute hand end point.
        Point minuteEndPoint = new Point(minuteEndX, minuteEndY);

        // Set color of the minute hand as black
        g2D.setColor(Color.black);
        // Set stroke size as 3 for medium thickness hand.
        g2D.setStroke(new BasicStroke(3));
        //Draw the minute hand.
        g2D.drawLine(center.x, center.y, minuteEndPoint.x, minuteEndPoint.y);

        // Draw the second hand and center
        //...............

        //Setting length of second hand as 0.35 times of clock diameter size.
        double secondHandLength = 0.35 * diameter;
        //Calculating angle swept by second hand in radians.
        // In 1 minute, second hand sweeps 6 degrees.
        double secondAngle = (Math.PI / 180) * 6 * second;
        // Calculating x and y end points of second hand.
        int secondEndX = center.x + ((int) (secondHandLength * Math.sin(secondAngle)));
        int secondEndY = center.y - ((int) (secondHandLength * Math.cos(secondAngle)));
        //Setting second hand end point.
        Point secondEndPoint = new Point(secondEndX, secondEndY);

        // Set color of the second hand as red
        g2D.setColor(Color.red);
        // Set stroke size as 1 for thin hand.
        g2D.setStroke(new BasicStroke(1));
        //Draw the second hand.
        g2D.drawLine(center.x, center.y, secondEndPoint.x, secondEndPoint.y);
    }   //End of drawAnalogClock() method

    // Draw digital clock
    private void drawDigitalClock(Graphics g) {
        Dimension d = getSize();

        // Get the current time as a string
        Calendar now = Calendar.getInstance();
        ////.................
        /*
         * Code added by Sakshi Singh.
         */
        //Extract hour (24 hour clock), minutes, seconds from current system date.
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        // Store current time in string in format HH24:mi:ss
        // In case hour/minute/second is single digit, pad it with 0.
        String time = (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);
        // Draw the time
        //////..................
        /*
         * Code added by Sakshi Singh.
         */
        //Set font color as black
        g.setColor(Color.black);
        //Set font, style and size
        g.setFont(new Font("Dialog", Font.BOLD, 32));
        //Draw the current time string.
        g.drawString(time, d.width / 5, d.height / 2);
    }   //End of drawDigitalClock() method
}   //End of class Clock
