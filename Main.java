import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.*;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws IOException {

        MeetingScheduler scheduler = new MeetingScheduler(2);

        /*User user1 = new User("Alice", ZoneId.of("America/New_York"));
        user1.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(9, 0), LocalTime.of(12, 0)); // Доступність з 9:00 до 12:00
        user1.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(15, 0), LocalTime.of(18, 0)); // Доступність з 15:00 до 18:00

        User user2 = new User("Bob", ZoneId.of("Europe/London"));
        user2.addAvailability(LocalDate.of(2024, 9, 30), LocalTime.of(10, 0), LocalTime.of(15, 0));
        user2.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(14, 0), LocalTime.of(18, 0));

        User user3 = new User("Charlie", ZoneId.of("Asia/Singapore"));
        user3.addAvailability(LocalDate.of(2024, 9, 30), LocalTime.of(7, 0), LocalTime.of(12, 0));
        user3.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(16, 0), LocalTime.of(20, 0));
        user3.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(22, 0), LocalTime.of(23, 59)); // Доступність з 22:00 до 23:59


        scheduler.addUser(user1);
        scheduler.addUser(user2);
        scheduler.addUser(user3);*/

        User user1 = new User("Alice", ZoneId.of("Europe/London"));
        user1.addAvailability(LocalDate.of(2024, 10, 1), LocalTime.of(10, 0), LocalTime.of(18, 0));
        user1.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(15, 0), LocalTime.of(18, 0));

        User user2 = new User("Bob", ZoneId.of("Europe/London"));
        user2.addAvailability(LocalDate.of(2024, 10, 1), LocalTime.of(10, 0), LocalTime.of(18, 0));
        user2.addAvailability(LocalDate.of(2024, 10, 3), LocalTime.of(10, 0), LocalTime.of(12, 0));
        user2.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(16, 0), LocalTime.of(19, 0));

        User user3 = new User("Charlie", ZoneId.of("Europe/Berlin"));
        user3.addAvailability(LocalDate.of(2024, 10, 1), LocalTime.of(10, 0), LocalTime.of(20, 0));
        user3.addAvailability(LocalDate.of(2024, 10, 3), LocalTime.of(11, 0), LocalTime.of(14, 0));
        user3.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(12, 0), LocalTime.of(18, 0));

        User user4 = new User("Diana", ZoneId.of("Europe/Kyiv"));
        user4.addAvailability(LocalDate.of(2024, 10, 1), LocalTime.of(13, 0), LocalTime.of(21, 0));
        user4.addAvailability(LocalDate.of(2024, 10, 2), LocalTime.of(10, 0), LocalTime.of(23, 0));
        user4.addAvailability(LocalDate.of(2024, 10, 3), LocalTime.of(10, 0), LocalTime.of(12, 0));

        scheduler.addUser(user1);
        scheduler.addUser(user2);
        scheduler.addUser(user3);
        scheduler.addUser(user4);


        //List<ZonedDateTime> meetingTimes = scheduler.findMeetingTimes();
        // Map<LocalDate, List<TimeRange>> find = scheduler.findAvailableMeetingTime();
        //System.out.println("Можливі часи для зустрічей: " + find);
        //printAvailableMeetingTimesForUsers(find, scheduler.getUsers());
        System.out.println(returnWords());
    }

    public static void printAvailableMeetingTimesForUsers(Map<LocalDate, List<TimeRange>> availableTimes, List<User> users) {
        for (User user : users) {
            System.out.println("Meeting times for user " + user.getName() + " in their timezone (" + user.getTimeZone() + "):");

            for (Map.Entry<LocalDate, List<TimeRange>> entry : availableTimes.entrySet()) {
                LocalDate date = entry.getKey();
                List<TimeRange> timeRanges = entry.getValue();
                for (TimeRange range : timeRanges) {

                    ZonedDateTime startUtc = ZonedDateTime.of(date, LocalTime.of(range.getStart(), 0), ZoneOffset.UTC);
                    ZonedDateTime endUtc = ZonedDateTime.of(date, LocalTime.of(range.getEnd(), 0), ZoneOffset.UTC).plusHours(1);

                    ZonedDateTime startInUserTimeZone = startUtc.withZoneSameInstant(user.getTimeZone());
                    ZonedDateTime endInUserTimeZone = endUtc.withZoneSameInstant(user.getTimeZone());

                    // Print the converted times
                    System.out.println(" - " + startInUserTimeZone.toLocalTime() + " to " + endInUserTimeZone.toLocalTime()
                            + " on " + startInUserTimeZone.toLocalDate());
                }
            }
            System.out.println();
        }
    }

    public static void printAvailableMeetingTimes(Map<LocalDate, List<TimeRange>> availableTimes) {
        for (Map.Entry<LocalDate, List<TimeRange>> entry : availableTimes.entrySet()) {
            LocalDate date = entry.getKey();
            List<TimeRange> timeRanges = entry.getValue();

            // Отримуємо день тижня
            DayOfWeek dayOfWeek = date.getDayOfWeek();

            System.out.println("Available meeting times for " + date + " (" + dayOfWeek + "):");

            for (TimeRange range : timeRanges) {
                System.out.println(" - " + range.getStart() + " to " + range.getEnd());
            }
        }
    }



    public static List<String> returnWords() throws IOException {
        String fp = "E:\\універ\\3 КУРС\\КПП\\Lab2\\Lab1_1\\Sentences.txt";

        StringBuilder text = new StringBuilder();

        BufferedReader reader = new BufferedReader(new FileReader(fp));
            String line;
            while ((line = reader.readLine()) != null) {
                text.append(line.trim()).append(" ");
            }
          //  System.out.println(text);
        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[a-zA-Z]{2,6}";
        String sentenceRegex = "[^.?!]+[.!?]+\\s*";
        Pattern sentencePattern = Pattern.compile(sentenceRegex);
        Pattern emailPattern = Pattern.compile(emailRegex);

        Matcher sentenceMatcher = sentencePattern.matcher(text.toString());
        ArrayList<String> firstWords = new ArrayList<>();

        while (sentenceMatcher.find()) {
            String sentence = sentenceMatcher.group();
            Matcher emailMatcher = emailPattern.matcher(sentence);

            if (emailMatcher.find()) {
                String first = sentence.split("\\s+")[0];
                firstWords.add(first);
            }
        }

        return firstWords;
    }
}