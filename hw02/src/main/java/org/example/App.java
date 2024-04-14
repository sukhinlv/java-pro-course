package org.example;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {

        List<Integer> integers = IntStream.generate(() -> rnd.nextInt(10))
                .limit(20)
                .boxed()
                .toList();
        System.out.println("Original list: " + integers);

        // Реализуйте удаление из листа всех дубликатов
        List<Integer> distinctIntegers = integers.stream().distinct().toList();
        System.out.println("Distinct list: " + distinctIntegers);
        System.out.println();

        // Найдите в списке целых чисел 3-е наибольшее число (пример: 5 2 10 9 4 3 10 1 13 => 10)
        Integer thirdMax = integers.stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .skip(2)
                .findAny()
                .orElseThrow();
        System.out.println("Third max: " + thirdMax);
        System.out.println();

        // Найдите в списке целых чисел 3-е наибольшее «уникальное» число (пример: 5 2 10 9 4 3 10 1 13 => 9,
        // в отличие от прошлой задачи здесь разные 10 считает за одно число)
        Integer thirdMaxDistinct = integers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .skip(2)
                .findAny()
                .orElseThrow();
        System.out.println("Third maxDistinct: " + thirdMaxDistinct);
        System.out.println();

        List<Employee> employees = Stream.generate(App::getRandomEmployee)
                .limit(30)
                .toList();
        List<Employee> engineers = employees.stream()
                .filter(employee -> JobTitle.ENGINEER.equals(employee.jobTitle()))
                .sorted(Comparator.comparing(Employee::dateOfBirth))
                .toList();
        System.out.println("Engineers: ");
        engineers.forEach(System.out::println);
        System.out.println();

        // Имеется список объектов типа Сотрудник (имя, возраст, должность), необходимо получить список имен 3 самых
        // старших сотрудников с должностью «Инженер», в порядке убывания возраста
        List<Employee> agedEngineers = employees.stream()
                .filter(employee -> JobTitle.ENGINEER.equals(employee.jobTitle()))
                .sorted(Comparator.comparing(Employee::dateOfBirth))
                .limit(3)
                .toList();
        System.out.println("AgedEngineers: " + agedEngineers);
        System.out.println();

        // Имеется список объектов типа Сотрудник (имя, возраст, должность), посчитайте средний возраст
        // сотрудников с должностью «Инженер»
        double engineersAverageAge = employees.stream()
                .filter(employee -> JobTitle.ENGINEER.equals(employee.jobTitle()))
                .map(employee -> Period.between(employee.dateOfBirth(), LocalDate.now()).get(ChronoUnit.YEARS))
                .mapToLong(Long::longValue)
                .average()
                .orElseThrow();
        System.out.println("EngineersAverageAge: " + (int) engineersAverageAge);
        System.out.println();

        // Найдите в списке слов самое длинное
        String longestName = firstNames.stream()
                .max(Comparator.comparing(String::length))
                .orElseThrow();
        System.out.println("Longest name: " + longestName);
        System.out.println();

        // Имеется строка с набором слов в нижнем регистре, разделенных пробелом. Постройте хеш-мапы,
        // в которой будут хранится пары: слово - сколько раз оно встречается во входной строке
        String stringOfWords = String.join(" ", employees.stream()
                .map(employee -> employee.name().toLowerCase())
                .toList());
        String[] words = stringOfWords.split(" ");
        Map<String, Integer> wordsCount = new HashMap<>();
        Arrays.stream(words).forEach(word -> wordsCount.merge(word, 1, Integer::sum));
        System.out.println("String of lowercase words: " + stringOfWords);
        System.out.println("Words count: " + wordsCount);
        System.out.println();

        // Отпечатайте в консоль строки из списка в порядке увеличения длины слова, если слова имеют одинаковую длины,
        // то должен быть сохранен алфавитный порядок
        System.out.println("Words sorted: " + wordsCount.keySet()
                .stream()
                .sorted((s1, s2) -> {
                    if (s1.length() == s2.length()) {
                        return s1.compareTo(s2);
                    }
                    return s1.length() - s2.length();
                })
                .toList()
        );
        System.out.println();

        // Имеется массив строк, в каждой из которых лежит набор из 5 строк, разделенных пробелом,
        // найдите среди всех слов самое длинное, если таких слов несколько, получите любое из них
        String[] wordsArray = new String[10];
        for (int i = 0; i < wordsArray.length; i++) {
            wordsArray[i] = String.join(" ", Stream.generate(() -> words[rnd.nextInt(words.length)])
                    .limit(5)
                    .toList());
        }
        System.out.println("Words array: " + Arrays.toString(wordsArray));
        String someLongestWord = Arrays.stream(wordsArray)
                .map(s -> Arrays.stream(s.split(" ")).toList())
                .flatMap(List::stream)
                .sorted(Comparator.comparing(String::length).reversed())
                .findAny()
                .orElseThrow();
        System.out.println("Some longest word: " + someLongestWord);
    }

    private static final List<String> firstNames = List.of(
            "Алексей",
            "Артём",
            "Вадим",
            "Владимир",
            "Валентин",
            "Данил",
            "Денис",
            "Дмитрий",
            "Егор",
            "Кирилл",
            "Леонид",
            "Максим",
            "Матвей",
            "Никита",
            "Олег",
            "Павел",
            "Пётр",
            "Роман",
            "Сергей",
            "Станислав"
    );

    private static final List<String> middleNames = List.of(
            "Александрович",
            "Алексеевич",
            "Анатольевич",
            "Андреевич",
            "Антонович",
            "Аркадьевич",
            "Борисович",
            "Валентинович",
            "Валериевич",
            "Васильевич",
            "Викторович",
            "Владимирович",
            "Вячеславович",
            "Геннадиевич",
            "Георгиевич",
            "Григорьевич",
            "Данилович",
            "Дмитриевич",
            "Евгеньевич",
            "Егорович",
            "Иванович",
            "Игоревич",
            "Ильич",
            "Кириллович",
            "Константинович",
            "Леонидович",
            "Львович",
            "Максимович",
            "Михайлович",
            "Николаевич",
            "Олегович",
            "Павлович",
            "Петрович",
            "Романович",
            "Семенович",
            "Сергеевич",
            "Станиславович",
            "Степанович"
    );

    private static final List<String> lastNames = List.of(
            "Иванов",
            "Смирнов",
            "Кузнецов",
            "Попов",
            "Васильев",
            "Петров",
            "Соколов",
            "Михайлов",
            "Новиков",
            "Фёдоров",
            "Морозов",
            "Волков",
            "Алексеев",
            "Лебедев",
            "Семёнов",
            "Егоров",
            "Павлов",
            "Козлов",
            "Степанов",
            "Николаев"
    );

    private static final Random rnd = new Random();

    private static String getRandomName() {
        return "%s %s %s".formatted(
                lastNames.get(rnd.nextInt(lastNames.size())),
                firstNames.get(rnd.nextInt(firstNames.size())),
                middleNames.get(rnd.nextInt(middleNames.size()))
        );
    }

    private static JobTitle getRandomJobTitle() {
        JobTitle[] jobTitles = JobTitle.values();
        return jobTitles[rnd.nextInt(jobTitles.length)];
    }

    private static Employee getRandomEmployee() {
        return new Employee(
                getRandomName(),
                LocalDate.of(rnd.nextInt(40) + 1960, rnd.nextInt(12) + 1, rnd.nextInt(27) + 1),
                getRandomJobTitle()
        );
    }
}
