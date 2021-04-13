# JavaCbrDaily

### Задание
  
На сайте https://www.cbr-xml-daily.ru ежедневно публикуют курсы валют. Напишите программу, которая выведет пять валют, курс которых изменился сильнее всего за прошедшие сутки.

Использована зависимость для парсинга результата запроса:
```
<dependency>
<!-- jsoup HTML parser library @ https://jsoup.org/ -->
    <groupId>org.jsoup</groupId>
    <artifactId>jsoup</artifactId>
    <version>1.13.1</version>
</dependency>
```

upd. Вместо https://www.cbr-xml-daily.ru использовался другой url: http://www.cbr.ru/scripts/XML_daily.asp , не получалось взять курс за определенную дату.
