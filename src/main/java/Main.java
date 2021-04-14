import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String url = "https://www.cbr-xml-daily.ru/daily_json.js";

        try {
            String json = Jsoup.connect(url).ignoreContentType(true).execute().body();

            System.out.println("URL: " + url);
            System.out.println("JSON response:\n" + json);

            Response response = new Gson().fromJson(json, Response.class);
            List<Valute> valuteList = new ArrayList<>();
            System.out.printf("\n\n%-50s %-15s %-15s %s\n","NAME", "CURRENT", "PREVIOUS", "DIFFERENCE");
            response.valute.forEach(
                            (key, value) -> {
                                System.out.printf("%-50s %-15s %-15s %s\n", (key + "\t" + value.name), value.value, value.previous, value.value - value.previous);
                                valuteList.add(value);
                            });

            Collections.sort(valuteList);

            System.out.println("\n\nMOST CHANGED:");
            System.out.printf("%-50s %-15s %-15s %s\n","NAME", "CURRENT", "PREVIOUS", "DIFFERENCE");
            for (int i = 0; i < 5 && i < valuteList.size(); i++) {
                Valute cur = valuteList.get(i);
                System.out.printf("%-50s %-15s %-15s %s\n", (cur.charCode + "\t" + cur.name), cur.value, cur.previous, cur.value - cur.previous);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Класс для хранения в списке валюты и ее изменения
    public static class Response {
        @SerializedName("Date")
        public Date date;

        @SerializedName("PreviousDate")
        public Date previousDate;

        @SerializedName("PreviousURl")
        public String previousUrl;

        @SerializedName("Timestamp")
        public Date timestamp;

        @SerializedName("Valute")
        public HashMap<String, Valute> valute;
    }

    public static class Valute implements Comparable<Valute>{
        @SerializedName("ID")
        public String id;

        @SerializedName("NumCode")
        public String numCode;

        @SerializedName("CharCode")
        public String charCode;

        @SerializedName("Nominal")
        public int nominal;

        @SerializedName("Name")
        public String name;

        @SerializedName("Value")
        public float value;

        @SerializedName("Previous")
        public float previous;

        public int compareTo(Valute o) {
            float curDiff = Math.abs(value - previous);
            float nextDiff = Math.abs(o.value - o.previous);
            return Float.compare(nextDiff, curDiff);
        }
    }
}


