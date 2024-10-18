import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class TimeRange {
    private int start; // початок у годинах (0-23)
    private int end;   // кінець у годинах (0-23)

    public TimeRange(int start, int end) {
        this.start = start;
        this.end = end;
    }
    public TimeRange(int time) {
        this.start = time;
        this.end = time;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public static List<TimeRange> returnIntersection(int hoursNeeded, TimeRange a, TimeRange b) {
        int intersectionStart = Math.max(a.getStart(), b.getStart());
        int intersectionEnd = Math.min(a.getEnd(), b.getEnd());

        if (intersectionStart < intersectionEnd) {
            int totalIntersectionHours = intersectionEnd - intersectionStart;

            if (totalIntersectionHours >= hoursNeeded) {
                List<TimeRange> result = new ArrayList<>();

                for (int i = 0; i <= totalIntersectionHours - hoursNeeded; i++) {
                    int slotStart = intersectionStart + i;
                    int slotEnd = slotStart + hoursNeeded;
                    if (slotEnd > intersectionEnd) {
                        break;
                    }
                    result.add(new TimeRange(slotStart, slotEnd));
                }

                return result;
            }
        }
        return new ArrayList<>();
    }

    public static boolean isLonger(TimeRange a, TimeRange b){
        return (a.getEnd() - a.getStart()) > (b.getEnd() - b.getStart());
    }
}

