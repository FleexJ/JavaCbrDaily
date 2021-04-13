import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String url = "http://www.cbr.ru/scripts/XML_daily.asp";

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String dateFormat = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());
            System.out.println(url);
            System.out.println( url + "?date_req=" + dateFormat);

            //previous course
            Document documentPrev = Jsoup.connect(url + "?date_req=" + dateFormat).get();
            List<String> namePrev = documentPrev.select("Name").eachText();
            List<String> valuePrev = documentPrev.select("Value").eachText();

            //current course
            Document documentNow = Jsoup.connect(url).get();
            List<String> nameNow = documentNow.select("Name").eachText();
            List<String> valueNow = documentNow.select("Value").eachText();
            
            List<ValDiff> valDiffs = new ArrayList<>();
            System.out.printf("%-50s %-15s %-15s %s\n", "NAME", "CURRENT", "PREVIOUS", "DIFFERENCE");
            for (int i = 0; i < valuePrev.size() && i < valueNow.size(); i++) {
                //Из запроса значение валюты приходит с ',' , поэтому происходит замена на '.' для конвертации в float
                float valPrev = Float.parseFloat(valuePrev.get(i).replace(",", "."));
                float valNow = Float.parseFloat(valueNow.get(i).replace(",", "."));
                if (nameNow.get(i).equals(namePrev.get(i))) {
                    ValDiff diff = new ValDiff(nameNow.get(i), valNow - valPrev);
                    valDiffs.add(diff);
                    System.out.printf("%-50s %-15s %-15s %s\n", nameNow.get(i), valNow, valPrev, diff.value);
                }
            }

            Collections.sort(valDiffs);

            System.out.println("\n\nFIVE MOST CHANGED:");
            System.out.printf("%-50s%-15s\n", "NAME", "DIFFERENCE");
            for (int i = 0; i < 5; i++) {
                valDiffs.get(i).print();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Класс для хранения в списке валюты и ее изменения
    public static class ValDiff implements Comparable<ValDiff> {
        public String name;
        public float value;

        public ValDiff(String name, float value) {
            this.name = name;
            this.value = value;
        }

        public void print() {
            System.out.printf("%-50s%s\n", name, value);
        }

        @Override
        public int compareTo(ValDiff o) {
            Float cur = Math.abs(value);
            Float next = Math.abs(o.value);
            return next.compareTo(cur);
        }
    }
}


