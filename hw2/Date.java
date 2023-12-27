public class Date {

    public static String MONTHS[] = {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"
    };

    public static int getMonth(String month) {
        for (int i = 0; i < MONTHS.length; i++) {
            if (month.equals(MONTHS[i]))
                return i + 1;
        }
        return -1;
    }

    public static boolean isMonth(String month) {
        return getMonth(month) != -1;
    }

    public static void main(String[] args) {
        // Your code here
    }
}
