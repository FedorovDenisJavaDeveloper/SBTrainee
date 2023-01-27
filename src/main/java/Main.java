import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static String pathCsvCities = "src"
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "Задача ВС Java Сбер.csv";

    private static char DELIMITER = ';';

    public static void main(String[] args) {
        List<City> cities = parseCsvToListCities();
        printList(cities);
        List<City> sortedByNameCities = sortByCityName(cities);
        List<City> sortedByDistrictAndNameCities = sortByDistrictAndCityName(cities);
        findIndexWithMaxPopulation(cities);
        countCitiesInRegion(cities);
    }

    private static void countCitiesInRegion(List<City> cities) {
        Map<String, Long> countCitiesInRegion = cities.stream()
                .collect(Collectors.groupingBy(City::getRegion, Collectors.counting()));
        System.out.println("Количество городов в регионах!");
        System.out.println("------------------------------");
        countCitiesInRegion.entrySet().forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));
    }

    private static void findIndexWithMaxPopulation(List<City> list) {
        City[] cities = new City[list.size()];
        list.toArray(cities);
        String maxIndex = "";
        int maxPopulation = 0;
        int cityPopulation = 0;
        for (City city :
                cities) {
            if ((cityPopulation = Integer.parseInt(city.getPopulation())) >= maxPopulation) {
                maxIndex = city.getId();
                maxPopulation = cityPopulation;
            }
        }
        System.out.printf("Индекс города с максимальным количеством людей \n" +
                "______________________________________________\n[%s]=%d\n\n", maxIndex, maxPopulation);
    }

    private static void printList(List<City> list) {
        list.forEach(System.out::println);
        System.out.println();
    }

    private static List<City> sortByCityName(List<City> list) {
        System.out.println("Список, отсортированный по наименованию в алфавитном порядке по убыванию без учета регистра");
        System.out.println("___________________________________________________________________________________________");
        List<City> sortedList = list.stream()
                .sorted((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()))
                .collect(Collectors.toList());
        printList(sortedList);
        return sortedList;
    }

    private static List<City> sortByDistrictAndCityName(List<City> list) {
        System.out.println("Список, отсортированный по федеральному округу и наименованию города внутри каждого \n" +
                "федерального округа в алфавитном порядке по убыванию с учетом регистра");
        System.out.println("____________________________________________________________________________________");
        List<City> sortedList = list.stream()
                .sorted((c1, c2) -> {
                    if (c1.getDistrict().compareTo(c2.getDistrict()) == 0) {
                        return c1.getName().compareTo(c2.getName());
                    } else return c1.getDistrict().compareTo(c2.getDistrict());
                })
                .collect(Collectors.toList());
        printList(sortedList);
        return sortedList;
    }

    public static List<City> parseCsvToListCities() {
        List<City> cities = new ArrayList<>();
        CSVParser csvParser = new CSVParserBuilder().withSeparator(DELIMITER).build();
        try (Scanner sc = new Scanner(new File(pathCsvCities))) {
            while (sc.hasNextLine()) {
                String[] res = csvParser.parseLine(sc.nextLine());
                cities.add(new City(res[0], res[1], res[2], res[3], res[4], res[5]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }
}
