# CLI Конвертор файлів між форматами JSON ↔ XML ↔ CSV

## Опис проекту
Консольна утиліта для конвертації файлів між трьома форматами даних: JSON, XML та CSV.

## Технології
- Java 21+
- Gradle
- Jackson (fasterxml) для JSON та XML
- OpenCSV для CSV
- JUnit 5 для тестування
- Mockito для створення моків

## Встановлення

### Вимоги
- Java 21 або вище
- Gradle (автоматично встановлюється через Gradle Wrapper)

### Кроки
1. Клонуйте репозиторій
2. Перейдіть в директорію проєкту
3. Виконайте `./gradlew build`

## Використання

### Синтаксис командного рядка
```
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input <вхідний_файл> --output <вихідний_файл>
```

### Приклади конвертації

#### З JSON
```bash
# JSON → XML
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input examples/sample.json --output output.xml

# JSON → CSV
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input examples/sample.json --output output.csv
```

#### З XML
```bash
# XML → JSON
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input examples/sample.xml --output output.json

# XML → CSV
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input examples/sample.xml --output output.csv
```

#### З CSV
```bash
# CSV → JSON
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input examples/sample.csv --output output.json

# CSV → XML
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input examples/sample.csv --output output.xml
```

## Обробка помилок

### Приклади помилок та їх рішення

#### Відсутній файл
```
Помилка: Файл не існує: input.json
Рішення: Переконайтеся, що файл існує та шлях вірний
```

#### Непідтримуваний формат
```
Помилка: Непідтримуваний формат файлу: .txt
Рішення: Використовуйте файли з розширенням .json, .xml або .csv
```

#### Відсутній обов'язковий аргумент
```
Помилка: Відсутній прапорець --input
Рішення: Додайте --input <файл> та --output <файл> до команди
```

## Тестування

### Запуск тестів
```bash
./gradlew test
```

### Генерація звіту про покриття
```bash
./gradlew test jacocoTestReport
```

Звіт про покриття знаходиться в: `build/reports/jacoco/test/html/index.html`

## Структура проекту

```
src/
├── main/
│   └── java/
│       └── global/goit/java_final_n_kovalchuk/
│           ├── Main.java
│           ├── cli/ (парсинг командного рядка)
│           ├── converter/ (логіка конвертації)
│           ├── model/ (моделі даних)
│           ├── parser/ (парсери форматів)
│           ├── writer/ (записувачі форматів)
│           ├── validator/ (валідація файлів)
│           └── exception/ (власні винятки)
└── test/
    └── java/
        └── global/goit/java_final_n_kovalchuk/
            ├── parser/ (тести парсерів)
            ├── writer/ (тести записувачів)
            ├── converter/ (тести конвертерів)
            ├── validator/ (тести валідації)
            └── cli/ (тести CLI)
```

## Архітектура

### Використані патерни GoF
- **Strategy Pattern** - Для різних стратегій конвертації (JSON→XML, CSV→JSON, тощо)
- **Factory Pattern** - Для створення відповідних парсерів/записувачів на основі типу формату

### Основні компоненти
- `SimpleFormatConverter` - Основний клас конвертації
- `FileParser<T>` - Інтерфейс для парсингу файлів
- `FileWriter<T>` - Інтерфейс для запису файлів
- `DataRecord` - Універсальне представлення запису даних

## Приклад використання з вкладеними структурами

### Файл persons.json

Файл [`persons.json`](examples/persons.json) містить складні дані з вкладеними об'єктами (`address`) та масивами (`data`):

```json
{
  "status": "OK",
  "code": 200,
  "locale": "fr_FR",
  "seed": null,
  "total": 10,
  "data": [
    {
      "id": 1,
      "firstname": "Lucie",
      "lastname": "Caron",
      "email": "paul.bernier@club-internet.fr",
      "phone": "+33780559746",
      "birthday": "1972-09-28",
      "gender": "female",
      "address": {
        "id": 1,
        "street": "673, boulevard Laetitia Evrard",
        "streetName": "chemin Nath Masson",
        "buildingNumber": "13",
        "city": "Meyer",
        "zipcode": "63379",
        "country": "Grenade",
        "country_code": "GD",
        "latitude": -50.305502,
        "longitude": -164.550901
      },
      "website": "http://poulain.fr",
      "image": "http://placeimg.com/640/480/people"
    }
  ]
}
```

### Конвертація persons.json → XML

```bash
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input examples/persons.json --output examples/persons_output.xml
```

**Результат (persons_output.xml):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<records>
  <record>
    <status>OK</status>
    <code>200</code>
    <locale>fr_FR</locale>
    <seed/>
    <total>10</total>
    <data>
      <item>
        <id>1</id>
        <firstname>Lucie</firstname>
        <lastname>Caron</lastname>
        <email>paul.bernier@club-internet.fr</email>
        <phone>+33780559746</phone>
        <birthday>1972-09-28</birthday>
        <gender>female</gender>
        <address>
          <id>1</id>
          <street>673, boulevard Laetitia Evrard</street>
          <streetName>chemin Nath Masson</streetName>
          <buildingNumber>13</buildingNumber>
          <city>Meyer</city>
          <zipcode>63379</zipcode>
          <country>Grenade</country>
          <country_code>GD</country_code>
          <latitude>-50.305502</latitude>
          <longitude>-164.550901</longitude>
        </address>
        <website>http://poulain.fr</website>
        <image>http://placeimg.com/640/480/people</image>
      </item>
    </data>
  </record>
</records>
```

**Особливості:**
- Вкладений об'єкт `address` зберігається як ієрархічна структура XML
- Масив `data` перетворюється на елементи `<item>`
- Порожні значення (`null`) записуються як порожні елементи

### Конвертація persons.json → CSV

```bash
java -jar build/libs/java_final_project_n_kovalchuk-1.0-SNAPSHOT.jar --input examples/persons.json --output examples/persons_output.csv
```

**Результат (persons_output.csv):**
```csv
address,code,data,locale,seed,status,total
"{"id":1,"street":"673, boulevard Laetitia Evrard","streetName":"chemin Nath Masson","buildingNumber":"13","city":"Meyer","zipcode":"63379","country":"Grenade","country_code":"GD","latitude":-50.305502,"longitude":-164.550901}",200,"[{"id":1,"firstname":"Lucie","lastname":"Caron","email":"paul.bernier@club-internet.fr","phone":"+33780559746","birthday":"1972-09-28","gender":"female","address":{"id":1,"street":"673, boulevard Laetitia Evrard","streetName":"chemin Nath Masson","buildingNumber":"13","city":"Meyer","zipcode":"63379","country":"Grenade","country_code":"GD","latitude":-50.305502,"longitude":-164.550901},"website":"http://poulain.fr","image":"http://placeimg.com/640/480/people"}]",fr_FR,,OK,10
```

**Особливості:**
- Вкладені структури (`address`, `data`) серіалізуються в JSON
- Порожні значення записуються як порожні рядки
- JSON-рядки беруться в лапки для коректного відображення в CSV

## Нові можливості

### Підтримка вкладених структур у XML

**[`XmlWriter`](src/main/java/global/goit/java_final_n_kovalchuk/writer/xml/XmlWriter.java)** підтримує:
- **Вкладені об'єкти (Map):** Записуються як дочірні елементи з ієрархічною структурою
- **Вкладені масиви (List):** Записуються як елементи `<item>` з рекурсивною обробкою вкладених елементів
- **Рекурсивна обробка:** Будь-який рівень вкладеності коректно обробляється

**Приклад:**
```xml
<address>
  <street>673, boulevard Laetitia Evrard</street>
  <city>Meyer</city>
  <country>Grenade</country>
</address>
```

### Підтримка вкладених структур у CSV

**[`CsvWriter`](src/main/java/global/goit/java_final_n_kovalchuk/writer/csv/CsvWriter.java)** підтримує:
- **JSON-серіалізація вкладених структур:** Вкладені об'єкти (Map) та масиви (List) перетворюються в JSON-рядки
- **Збереження даних:** Вся інформація зберігається без втрати даних
- **Сумісність:** JSON-рядки беруться в лапки для коректного відображення в CSV

**Приклад:**
```csv
address
"{"street":"673, boulevard Laetitia Evrard","city":"Meyer","country":"Grenade"}"
```


## Автор
Nazar Kovalchuk

## Ліцензія
MIT License
