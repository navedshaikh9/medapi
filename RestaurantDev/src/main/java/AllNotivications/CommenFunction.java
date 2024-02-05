package AllNotivications;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CommenFunction {


	public static String getDateTime()
	{
		DateFormat date1 = new SimpleDateFormat("dd-MM-yyyy hh.mm aa");
		String newDate = date1.format(new Date()).toString();
			
		return newDate;
		
	}
	
	
	public static String getDate()
	{
		
		DateFormat date1 = new SimpleDateFormat("dd-MM-yyyy");
		String newDate = date1.format(new Date()).toString();
			
		return newDate;
		
	}
	
	
	
	
	public static LocalDate getLocalDate(String date)
	{
		LocalDate ld = LocalDate.parse(date);
		
		return ld;
		
	}
	
	
	public static String getLocalDateString(LocalDate date)
	{
	if(date!=null)
	{
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String d =date.format(format);
		
		return d;
		
	}
	return null;
		
	}
	
	
	public static LocalDate getAfterOneYearDate()
	{
		
		LocalDate currentDate = LocalDate.now();
		LocalDate afterOneYear = currentDate.plusYears(1);
				
		return afterOneYear;
		
	}
	
	
	public static LocalDate getYear()
	{
		
		LocalDate currentDate = LocalDate.now();
		LocalDate afterOneYear = currentDate.plusYears(1);
		
		return afterOneYear;
		
	}
	
	public static LocalDate getDate(int days)
	{
		
		LocalDate currentDate = LocalDate.now();
		LocalDate afterOneYear = currentDate.plusDays(days);
		
		return afterOneYear;
		
	}
	
	public static String getTime()
	{
		Date currentTime = new Date();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String formattedTime = dateFormat.format(currentTime);
		return formattedTime;
	}
	
	
	public static String timeDifference(String time) {
        LocalTime currentTime = LocalTime.now();

        // Parse the target time string into LocalTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        LocalTime targetLocalTime = LocalTime.parse(time, formatter);

        // Calculate the difference in minutes
        long differenceInMinutes = targetLocalTime.until(currentTime, java.time.temporal.ChronoUnit.MINUTES);

        // Check if the difference is less than or equal to 1 minute
        if (differenceInMinutes <= 1) {
            return "Just Placed";
        } else {
            if (differenceInMinutes >= 60) {
                long hours = differenceInMinutes / 60;
                long remainingMinutes = differenceInMinutes % 60;

                if (remainingMinutes > 0) {
                    return hours + ":" + remainingMinutes + " h ago";
                } else {
                    return hours + " h ago";
                }
            } else {
                return differenceInMinutes + " min ago";
            }
        }
    }
	
	
	 public static int timeDifferenceInMin(String time) {
	        LocalTime currentTime = LocalTime.now();

	        // Parse the target time string into LocalTime
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
	        LocalTime targetLocalTime = LocalTime.parse(time, formatter);

	        // Calculate the difference in minutes
	        long differenceInMinutes = targetLocalTime.until(currentTime, java.time.temporal.ChronoUnit.MINUTES);

	        // Always return the difference in minutes
	        return (int) differenceInMinutes;
	    }

}
