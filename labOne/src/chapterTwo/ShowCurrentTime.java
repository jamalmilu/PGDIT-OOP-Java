package chapterTwo;

public class ShowCurrentTime {
    public static void main(String[] args){

        // Obtain the total milliseconds since midnight, January 1970
        long totalMilliSeconds = System.currentTimeMillis();

        // Total seconds
        long totalSeconds = totalMilliSeconds / 1000;

        long currentSeconds = (int) (totalSeconds % 60);

        // Obtain total minute
        long totalMinutes =(int) (totalSeconds / 60);

        // Obtain current minute
        long currentMinute = (int) (totalMinutes % 60);

        long totalHours = totalMinutes / 60;

        long currentHour = (int) (totalHours % 24);

        System.out.println("Current time is " + currentHour + ":"
                + currentMinute + ":" + currentSeconds + " GMT");


    }
}
