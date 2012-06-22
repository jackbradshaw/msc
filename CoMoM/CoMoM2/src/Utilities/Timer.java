package Utilities;

/**
 *
 * The Timer object can receive "start" and "pause" commands. It can also
 * return the time interval in various formats.
 *
 *  @author Michail Makaronidis, 2009
 *
 */
public class Timer {

    /**
     *  Stores the respective timestamp.
     */
    private long startTime, stopTime, interval;

    /**
     * Creates and initialises a Timer object.
     */
    public Timer() {
        super();
        this.interval = 0;
    }

    /**
     * Creates and initialises a Timer object. The initial time interval is set
     * equal to the argument interval.
     * @param interval The desired interval in msecs
     */
    public Timer(long interval) {
        super();
        this.interval = interval;
    }

    /**
     * Starts the timer, i.e. sets the current time as the beginning of the
     * interval.
     *
     */
    public void start() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Stops the timer, i.e. sets the current time as the end of the interval.
     *
     */
    public void pause() {
        stopTime = System.currentTimeMillis();
        interval += stopTime - startTime;
    }

    /**
     * This method returns the sum of time intervals in msecs between every 
     * start and pause of the timer. The result is valid only if each start call
     * is followed by a subsequent pause call.
     *
     * @return  Time interval in msecs
     */
    public long getInterval() {
        return interval;
    }

    /**
     * This method returns the sum of time intervals between every start and
     * pause of the timer in a pretty form. The result is valid only if each
     * start call is followed by a subsequent pause call.
     *
     * @return  String representing the time interval in pretty form.
     */
    public String getPrettyInterval() {

        if (interval < 1000) {
            return (interval + " msec");
        } else if (interval < 60000) {
            return (interval / 1000.0 + " sec");
        } else {
            return ((interval / 60000) + " min " + ((interval - (interval / 60000) * 60000) / 1000.0) + " sec");
        }
    }
}
