import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");

        Collection<Person> persons = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 10_000_000; i++) {
            persons.add(new Person(
                    names.get(random.nextInt(names.size())),
                    families.get(random.nextInt(families.size())),
                    random.nextInt(100),
                    Sex.values()[random.nextInt(Sex.values().length)],
                    Education.values()[random.nextInt(Education.values().length)])
            );
        }

        System.out.println("=== Анализ данных о населении Лондона (10 млн человек) ===\n");

        // Задание 1: Найти количество несовершеннолетних (младше 18 лет)
        long minorsCount = persons.stream()
                .filter(person -> person.getAge() < 18)
                .count();

        System.out.println("1. Количество несовершеннолетних (младше 18 лет): " + minorsCount);
        System.out.println("   Процент от населения: " + String.format("%.2f", (minorsCount * 100.0 / persons.size())) + "%\n");

        // Задание 2: Получить список фамилий призывников (мужчины от 18 до 27 лет)
        List<String> conscripts = persons.stream()
                .filter(person -> person.getSex() == Sex.MAN)
                .filter(person -> person.getAge() >= 18)
                .filter(person -> person.getAge() < 27)
                .map(Person::getFamily)
                .distinct() // Убираем дубликаты фамилий
                .sorted() // Сортируем по алфавиту
                .collect(Collectors.toList());

        System.out.println("2. Список фамилий призывников (мужчины 18-27 лет):");
        System.out.println("   Всего уникальных фамилий: " + conscripts.size());
        System.out.println("   Первые 20 фамилий: " + conscripts.subList(0, Math.min(20, conscripts.size())) + "\n");

        // Задание 3: Получить отсортированный по фамилии список потенциально работоспособных людей
        // с высшим образованием (женщины 18-60 лет, мужчины 18-65 лет)
        List<Person> workingAgeWithHigherEducation = persons.stream()
                .filter(person -> person.getEducation() == Education.HIGHER)
                .filter(person -> {
                    if (person.getSex() == Sex.WOMAN) {
                        return person.getAge() >= 18 && person.getAge() < 60;
                    } else { // MAN
                        return person.getAge() >= 18 && person.getAge() < 65;
                    }
                })
                .sorted(Comparator.comparing(Person::getFamily))
                .collect(Collectors.toList());

        System.out.println("3. Потенциально работоспособные люди с высшим образованием:");
        System.out.println("   Всего: " + workingAgeWithHigherEducation.size());
        System.out.println("   Процент от населения: " +
                String.format("%.2f", (workingAgeWithHigherEducation.size() * 100.0 / persons.size())) + "%");

        // Дополнительная статистика для третьего задания
        long workingMen = workingAgeWithHigherEducation.stream()
                .filter(person -> person.getSex() == Sex.MAN)
                .count();

        long workingWomen = workingAgeWithHigherEducation.stream()
                .filter(person -> person.getSex() == Sex.WOMAN)
                .count();

        System.out.println("   Из них мужчин: " + workingMen + " (" +
                String.format("%.2f", (workingMen * 100.0 / workingAgeWithHigherEducation.size())) + "%)");
        System.out.println("   Из них женщин: " + workingWomen + " (" +
                String.format("%.2f", (workingWomen * 100.0 / workingAgeWithHigherEducation.size())) + "%)");

        // Вывод первых 10 человек из третьего задания для примера
        System.out.println("\n   Первые 10 человек из списка (по фамилии):");
        workingAgeWithHigherEducation.stream()
                .limit(10)
                .forEach(person -> System.out.println("      " + person.getFamily() + " " +
                        person.getName() + ", " + person.getAge() + " лет, " +
                        person.getSex() + ", " + person.getEducation()));

    }
}