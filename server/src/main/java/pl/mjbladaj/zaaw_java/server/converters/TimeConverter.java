package pl.mjbladaj.zaaw_java.server.converters;

import org.joda.time.DateTime;

public abstract class TimeConverter {
    public static DateTime convertStringToDateTime(String stringDate) {
        String[] splitedDate = stringDate.split("-");
        DateTime date = new DateTime(Integer.parseInt(splitedDate[0]),
                Integer.parseInt(splitedDate[1]),
                Integer.parseInt(splitedDate[2]), 0, 0);
        return date;
    }

    public static String convertDateToString(DateTime date) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date.year().get());
        stringBuilder.append("-");
        stringBuilder.append(date.monthOfYear().get());
        stringBuilder.append("-");
        stringBuilder.append(date.dayOfMonth().get());

        return stringBuilder.toString();
    }
}
