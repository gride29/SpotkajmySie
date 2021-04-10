import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calendar {
    private final Hours workHours;

    private final List<Hours> meetings = new ArrayList<Hours>();

    @SuppressWarnings("unchecked")
    public Calendar(JSONObject calendar) {
        JSONObject workHours = (JSONObject) calendar.get("working_hours");
        this.workHours = new Hours((String) workHours.get("start"), (String) workHours.get("end"));

        JSONArray meetings = (JSONArray) calendar.get("planned_meeting");
        for (JSONObject meeting : (Iterable<JSONObject>) meetings) {
            this.meetings.add(new Hours((String) meeting.get("start"), (String) meeting.get("end")));
        }
    }

    public static List<Hours> findAllPossibleMeetings(Calendar calendar1, Calendar calendar2, int meetingDuration) {
        List<Hours> separatedTimes = new ArrayList<Hours>();

        // Dodajemy wszystkie spotkania do jednej listy.
        separatedTimes.addAll(calendar1.meetings);
        separatedTimes.addAll(calendar2.meetings);

        // Sortujemy spotkania po czasie rozpoczęcia i długości trwania.
        Collections.sort(separatedTimes);

        for (int i = 0; i <= separatedTimes.size() - 1; i++) {

            // Jeżeli spotkania na siebie nachodzą łączymy je, aby lista zawierała tylko odseparowane czasowo spotkania.
            while (Hours.checkIfOverlap(separatedTimes.get(i), separatedTimes.get(i + 1))) {
                separatedTimes.set(i, Hours.merge(separatedTimes.get(i), separatedTimes.get(i + 1)));
                separatedTimes.remove(i + 1);

                if (i == separatedTimes.size() - 1) {
                    break;
                }
            }
        }

        List<Hours> freeTimes = new ArrayList<Hours>();

        int earliestTime = Math.max(calendar1.workHours.getStart(), calendar2.workHours.getStart());
        int latestTime = Math.min(calendar1.workHours.getEnd(), calendar2.workHours.getEnd());

        int newStart = earliestTime;

        // Na podstawie czasu pracy kalendarzy dwóch osób, obliczamy godziny spotkań, które będą pasować im obu.
        for (Hours time : separatedTimes) {
            int newEnd = time.getStart();
            freeTimes.add(new Hours(newStart, newEnd));
            newStart = time.getEnd();
        }

        // Dodajemy do listy edge case.
        freeTimes.add(new Hours(newStart, latestTime));

        // Usuwamy spotkania, których czas trwania jest mniejszy niż wcześniej zdefiniowany meetingDuration.
        freeTimes.removeIf(meeting -> meeting.getDuration() < meetingDuration);

        return freeTimes;
    }
}
