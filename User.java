import java.time.*;
import java.util.*;

public class User {
    private String name;
    private Map<LocalDate, List<TimeRange>> availability;
    private ZoneId timeZone;

    public User(String name, ZoneId timeZone) {
        this.name = name;
        this.timeZone = timeZone;
        this.availability = new HashMap<>();
    }

    public void addAvailability(LocalDate day, LocalTime startTime, LocalTime endTime) {
        Map<LocalDate, TimeRange> fg = convertToGMT(day, startTime, endTime);
        for (var a : fg.entrySet()){
           availability.putIfAbsent(a.getKey(), new ArrayList<>());
            availability.get(a.getKey()).add(a.getValue());
        }
    }

    public void addAvailability(LocalDate day, LocalTime time) {
        Map<LocalDate, TimeRange> fg = convertToGMT(day, time, time);
        for (var a : fg.entrySet()){
            availability.putIfAbsent(a.getKey(), new ArrayList<>());
            availability.get(a.getKey()).add(a.getValue());
        }
    }



    private Map<LocalDate, TimeRange> convertToGMT(LocalDate day, LocalTime startTime, LocalTime endTime) {
        ZonedDateTime startZoned = ZonedDateTime.of(day, startTime, timeZone);
        ZonedDateTime endZoned = ZonedDateTime.of(day, endTime, timeZone);

        ZonedDateTime startGMT = startZoned.withZoneSameInstant(ZoneId.of("GMT"));
        ZonedDateTime endGMT = endZoned.withZoneSameInstant(ZoneId.of("GMT"));

        DayOfWeek startDay = startGMT.getDayOfWeek();
        DayOfWeek endDay = endGMT.getDayOfWeek();
        Map<LocalDate, TimeRange> a = new HashMap<LocalDate, TimeRange>();
        if (!startDay.equals(endDay)) {
            a.putIfAbsent(startGMT.toLocalDate(), new TimeRange(startGMT.toLocalTime().getHour(), 23));
            a.putIfAbsent(endGMT.toLocalDate(), new TimeRange(0, endGMT.toLocalTime().getHour()));
        }
        a.putIfAbsent(startGMT.toLocalDate(), new TimeRange(startGMT.toLocalTime().getHour(), endGMT.toLocalTime().getHour()));
        return a;

    }

    public String getName() {
        return name;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public Map<LocalDate, List<TimeRange>> getAvailability() {
        return availability;
    }
}
