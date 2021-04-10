import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        // Dane wejściowe
        JSONObject parsedCalendar1 = (JSONObject) parser.parse(new FileReader("JSON\\calendar1.json"));
        JSONObject parsedCalendar2 = (JSONObject) parser.parse(new FileReader("JSON\\calendar2.json"));
        String[] meetingDuration = {"00:30"};

        String[] meetingDurationSplit = meetingDuration[0].trim().split(":");
        int meetingDurationTime = (Integer.parseInt(meetingDurationSplit[0]) * 60) + Integer.parseInt(meetingDurationSplit[1]);

        Calendar calendar1 = new Calendar(parsedCalendar1);
        Calendar calendar2 = new Calendar(parsedCalendar2);

        List<Hours> possibleTimes = Calendar.findAllPossibleMeetings(calendar1, calendar2, meetingDurationTime);

        System.out.println("Zakresy, w których można zorganizować spotkania:");
        System.out.println(Arrays.toString(possibleTimes.toArray()));
    }
}
