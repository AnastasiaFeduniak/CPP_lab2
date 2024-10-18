import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class MeetingScheduler {
    private List<User> users;
    private int requiredDuration;

    public MeetingScheduler(int requiredDuration) {
        this.users = new ArrayList<>();
        this.requiredDuration = requiredDuration;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers(){
        return users;
    }
    public Map<LocalDate, List<TimeRange>> findAvailableMeetingTime() {
        //   List<LocalDate> allDays = users.stream().filter(x -> x.getAvailability().entrySet().stream().filter(y -> y.getKey()!=null).distinct().toList() != null).distinct().collect(Collector.toList());
        List<LocalDate> allDates = users.stream()
                .flatMap(user -> user.getAvailability().keySet().stream())
                .distinct()
                .collect(Collectors.toList());
        Map<LocalDate, List<TimeRange>> availableTime = new HashMap<>();
        int totalHours = allDates.size() * 24;
        int[] hourAvailability = new int[totalHours];

        for (User user : users) {
            for (var day : user.getAvailability().entrySet()) {
                for (var range : day.getValue()) {
                    for (int i = range.getStart(); i <= range.getEnd(); i++) {
                        hourAvailability[allDates.indexOf(day.getKey()) * 24 + i]++;
                    }
                }
            }
        }

        int longestTime = 0;
        for (int i = 0; i < hourAvailability.length; i++) {
            if (hourAvailability[i] == users.size()) {
                int count = 1;

                for (int j = i+1; j < hourAvailability.length; j++) {
                    if (hourAvailability[j] == users.size()) {
                        count++;
                        if (longestTime < count) {
                            longestTime = count;
                        }
                        if (count == requiredDuration) {
                            availableTime.putIfAbsent(allDates.get(i / 24), new ArrayList<>());
                            availableTime.get(allDates.get(i / 24)).add(new TimeRange(i % 24, j % 24));
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        System.out.println("The longest available meeting: " + longestTime + "h");
        return availableTime;
    }

}

