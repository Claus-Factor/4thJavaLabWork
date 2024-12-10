package com.labs.csvparser.scvparser;

import com.labs.csvparser.model.Department;
import com.labs.csvparser.model.Person;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvParser {

    public static void main(String[] args) throws IOException, CsvValidationException {
        List<Person> people = readPeopleFromCsv("people.csv");
        people.forEach(System.out::println);
    }

    public static List<Person> readPeopleFromCsv(String csvFilePath) throws IOException, CsvValidationException {
        List<Person> people = new ArrayList<>();
        AtomicInteger departmentCounter = new AtomicInteger(1); // Используем обычный int для счетчика

        Map<String, Department> departments = new HashMap<>();

        try (InputStream in = CsvParser.class.getClassLoader().getResourceAsStream(csvFilePath);
             Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                     .build()) {

            if (csvReader == null) {
                throw new FileNotFoundException(csvFilePath);
            }

            String[] nextLine;
            csvReader.readNext(); // Пропускаем заголовок
            while ((nextLine = csvReader.readNext()) != null) {
                try {
                    int id = Integer.parseInt(nextLine[0]);
                    String name = nextLine[1];
                    String gender = nextLine[2];
                    LocalDate birthDate = LocalDate.parse(nextLine[3], DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                    String departmentName = nextLine[4];
                    int salary = Integer.parseInt(nextLine[5]);

                    Department department = departments.computeIfAbsent(departmentName, dname -> {
                        Department newDepartment = new Department(departmentCounter.getAndIncrement(), dname); // Используем инкрементацию после использования
                        return newDepartment;
                    });

                    Person person = new Person(id, name, gender, department, salary, birthDate);
                    people.add(person);
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка преобразования строки: " + String.join(",", nextLine) + ". Ошибка: " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Неправильный формат строки: " + String.join(",", nextLine) + ". Ошибка: " + e.getMessage());
                }
            }
        }
        return people;
    }

}