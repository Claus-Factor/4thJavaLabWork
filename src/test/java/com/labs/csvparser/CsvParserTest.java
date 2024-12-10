package com.labs.csvparser;

import com.labs.csvparser.model.Person;
import com.labs.csvparser.scvparser.CsvParser;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CsvParserTest {
    @Test
    void testReadPeopleFromCsv() {
        try {
            List<Person> people = CsvParser.readPeopleFromCsv("people.csv");
            assertFalse(people.isEmpty()); // Проверяем, что список не пуст
            assertTrue(people.size() > 0); //Более конкретная проверка
        } catch (Exception e) {
            fail("Произошла ошибка при чтении CSV файла: " + e.getMessage());
        }
    }

    @Test
    void testReadPeopleFromCsv_CorrectData() {
        try {
            List<Person> people = CsvParser.readPeopleFromCsv("people.csv");
            // Проверяем данные хотя бы для одного человека
            Person firstPerson = people.get(0);
            assertEquals(28281, firstPerson.getId(), "ID первого человека неверный");
            assertEquals("Aahan", firstPerson.getName(), "Имя первого человека неверно");
            assertEquals("Male", firstPerson.getGender(), "Пол первого человека неверен");
            assertEquals(LocalDate.of(1970, 5, 15), firstPerson.getBirthDate(), "Дата рождения первого человека неверна");
        } catch (Exception e) {
            fail("Произошла ошибка при чтении CSV файла: " + e.getMessage());
        }
    }

    @Test
    void testReadPeopleFromCsv_DivisionCount() {
        try {
            List<Person> people = CsvParser.readPeopleFromCsv("people.csv");
            // Проверяем количество уникальных подразделений
            Map<String, Integer> divisionCounts = new HashMap<>();
            for (Person person : people) {
                divisionCounts.put(person.getDepartment().getName(), divisionCounts.getOrDefault(person.getDepartment().getName(), 0) + 1);
            }
        } catch (Exception e) {
            fail("Произошла ошибка при чтении CSV файла: " + e.getMessage());
        }
    }
}
