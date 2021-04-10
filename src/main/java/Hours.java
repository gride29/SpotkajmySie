public class Hours implements Comparable<Hours> {
    private final int start;
    private final int end;
    private final int duration;
    private final String startString;
    private final String endString;

    public Hours (int start, int end) {
        if (end < start) end = start;

        this.start = start;
        this.end = end;
        this.duration = this.end - this.start;

        int startMinutes = start % 60;
        int endMinutes = end % 60;
        int startHours = (start - startMinutes) / 60;
        int endHours = (end - endMinutes) / 60;

        this.startString = String.format("%02d:%02d", startHours, startMinutes);
        this.endString = String.format("%02d:%02d", endHours, endMinutes);
    }

    public Hours (String start, String end) {
        String[] startSplit = start.trim().split(":");
        String[] endSplit = end.trim().split(":");

        this.start = (Integer.parseInt(startSplit[0]) * 60) + Integer.parseInt(startSplit[1]);
        this.end = (Integer.parseInt(endSplit[0]) * 60) + Integer.parseInt(endSplit[1]);
        this.duration = this.end - this.start;
        this.startString = start;
        this.endString = end;
    }

    @Override
    public String toString() {
        return String.format("[\"%s\", \"%s\"]", startString, endString);
    }

    public static Hours merge(Hours h1, Hours h2) {
        return new Hours(Math.min(h1.start, h2.start), Math.max(h1.end, h2.end));
    }

    public static boolean checkIfOverlap(Hours h1, Hours h2) {
        return h1.end >= h2.start;
    }

    @Override
    public int compareTo(Hours h) {
        if (this.start < h.start) {
            return -1;
        }
        if (this.start == h.start) {
            if (this.duration < h.duration) {
                return -1;
            }
            else if (this.duration == h.duration) {
                return 0;
            }
        }
        return 1;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getDuration() {
        return duration;
    }
}
