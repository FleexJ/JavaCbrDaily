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

            sortList(valDiffs);
//            System.out.printf("%-50s %-15s %s\n", "Name", "Current", "Previous");
//            System.out.println("\nSORTED DIFFS:");
//            for (ValDiff diff : valDiffs) {
//                diff.print();
//            }

            System.out.println("\n\nFIVE MOST CHANGED:");
            System.out.printf("%-50s%-15s\n", "NAME", "DIFFERENCE");
            for (int i = 0; i < 5; i++) {
                valDiffs.get(i).print();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Сортировка по модулю разницы
    public static void sortList(List<ValDiff> list) {
        boolean fl = true;
        while (fl){
            fl = false;
            for (int i = 0; i < list.size() - 1; i++) {
                ValDiff cur = list.get(i);
                ValDiff next = list.get(i + 1);
                if (next.greatAbsThan(cur)) {
                    cur.swap(next);
                    fl = true;
                }
            }
        }
    }

    //Класс для хранения в списке валюты и ее изменения
    public static class ValDiff {
        public String name;
        public float value;

        public ValDiff(String name, float value) {
            this.name = name;
            this.value = value;
        }

        public boolean greatAbsThan(ValDiff valDiff) {
            return Math.abs(this.value) > Math.abs(valDiff.value);
        }

        public void swap(ValDiff valDiff) {
            String tmpName = this.name;
            float tmpValue = this.value;

            this.name = valDiff.name;
            this.value = valDiff.value;

            valDiff.name = tmpName;
            valDiff.value = tmpValue;
        }

        public void print() {
            System.out.printf("%-50s%s\n", name, value);
        }
    }
}


