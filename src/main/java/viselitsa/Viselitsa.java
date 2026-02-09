package viselitsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author tunyaa
 */
public class Viselitsa {

    private static BufferedReader reader;
    private static List<String> words;            // Словарь
    private static String word = "";                // Выбранное слово
    private static String consoleArg = "";      // Ввод с консоли
    private static int guessedWords;             // Количество отгаданных слов
    private static int life;                                // Жизни сохраняются между играми

    static {
        reader = new BufferedReader(new InputStreamReader(System.in));
        words = new ArrayList<>();
        loadWordsFromResources();
    }

    public static void main(String[] args) {
        viselitsaMenu();
    }

    // Меню(Начать игру\ Выход\ Выбор сложности)
    private static void viselitsaMenu() {
        // Условие для выхода
        while (!consoleArg.equals("0")) {
            System.out.println("'1' - Чтобы начать игру.                    '0' - Чтобы выйти.");
            consoleArg = readLine();

            // Выбор сложности 
            if (consoleArg.equals("1")) {
                // Подбор слова(Сложность)
                getRandomWordByDifficult(сhooseDifficult());

                // Начинается игра
                viselitsaGameStart();

            }
        }
        // OFF
    }

    // Выбор сложности
    private static int сhooseDifficult() {

        while (true) {
            System.out.println("Выберете сложность:\n'1' - До 5 букв.\n'2' - 5 - 8 букв.\n'3' - 8 и более букв");
            consoleArg = readLine();

            // Условия для сложности [Количество жизней]
            int[] difficultOptoins = new int[]{0, 6, 5, 3};

            if (consoleArg.matches("[123]")) {
                Integer dif = Integer.valueOf(consoleArg);
                System.out.println("Добавляется " + difficultOptoins[dif] + " жизни");
                life += difficultOptoins[dif];// На новую игру добавляются жизни
                return dif;
            }
        }

    }

    // Подбор случайного слова по сложности 1(до 5 букв), 2(5 - 8 букв), 3(8 и более букв).
    private static void getRandomWordByDifficult(int dif) {

        // Условия для сложности [Сложность][от, до]
        int[][] difficultСondition = new int[][]{{}, {1, 5}, {5, 8}, {8, 30}};

        do {
            // Получаем рандомное число на основе количества слов
            int random = new Random().nextInt(words.size());

            // Получаем рандомное слово
            word = words.get(random);

        } while (!(word.length() >= difficultСondition[dif][0] == word.length() <= difficultСondition[dif][1]));

    }

    // Начало игры(отгадывание слова)
    private static void viselitsaGameStart() {

        // Форматирование
        word = word.toUpperCase();

        // Слово "прячется" за символы #
        StringBuilder hiddenWord = new StringBuilder("#".repeat(word.length()));
        // Введенные буквы, которых нет в слове
        StringBuilder lettersNotContains = new StringBuilder();

        System.out.println("Загадано слово - " + hiddenWord + "  Количество букв -" + word.length()
                + ". Количество жизней - " + life + " Слов отгадано - " + guessedWords);

        while (life > 0) {
            System.out.println("Введите букву                   '0' - Чтобы выйти");
            System.out.println("[   " + hiddenWord + "   ]                   Количество жизней - " + life);
            consoleArg = readLine();
            // Выход из текущей игры
            if (consoleArg.startsWith("0")) {
                life = 0;
                consoleArg = "";
                return;
            }
            // Если строка null, с длиной 0, в ней сесть цифры, символы, пробелы
            if (consoleArg == null || consoleArg.matches("^(?!^[а-яА-ЯёЁ]+$).*$")) {
                System.out.println("Не дожно быть цифр, пробелов и символов. Только русские буквы.");
                continue;
            } else {
                // Если введено несколько символов, берется символ по индексу 0
                consoleArg = consoleArg.substring(0, 1).toUpperCase();
                System.out.println("Ведена буква - " + consoleArg.toUpperCase());
            }
            // Буква отгадана. Буквы открывается в слове.
            if (word.contains(consoleArg)) {
                System.out.println("Есть такая буква!");
                // Если повторно вводится отгаданная буква
                if (hiddenWord.indexOf(consoleArg) != -1) {
                    System.out.println("Эта буква уже отгадана.");
                    continue;
                }
                // # заменяются на угаданную букву
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == consoleArg.charAt(0)) {
                        hiddenWord.setCharAt(i, consoleArg.charAt(0));
                    }
                }
                // Если в отгадываемом слове не осталось #, значит слово отгадано
                if (hiddenWord.indexOf("#") == -1) {
                    System.out.println("Победа! Это слово - " + word.toUpperCase());
                    System.out.println("За отгаданное слово добавляется 3 жизни.");
                    life += 3;
                    guessedWords += 1;
                    return;
                }
            } else {// Буква не отгадана. Минус одна жизнь.
                // Если повторно  вводится буква которой нет в слове
                if (lettersNotContains.indexOf(consoleArg.toUpperCase()) != -1) {
                    System.out.println("Такую букву уже пробовали");
                    continue;
                }
                System.out.println("Такой буквы нет.          Одна попытка минус...            ");
                lettersNotContains.append(consoleArg.toUpperCase()).append(" ");
                life -= 1;
            }

            System.out.println("Буквы, которых нет в слове - " + lettersNotContains);

        }
        // Если life == 0, цикл завершается
        System.out.println("Жизни закончились");
        System.out.println("Загаданное слово - " + word.toUpperCase());
        System.out.println("Отгадано слов - " + guessedWords);
        guessedWords = 0;
    }

    // Загружает слова из файла
    private static void loadWordsFromResources() {

        // Загржает файл
        try (InputStream inputStream = Viselitsa.class
                .getResourceAsStream("/words.txt")) {

            if (inputStream == null) {
                System.out.println("Файл words.txt не найден в ресурсах");
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream))) {
                // Сохраняет слова в массив
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        words.add(line.toUpperCase());
                    }
                }

                System.out.println("Загружено " + words.size() + " слов");

            }
        } catch (IOException e) {
            System.err.println("Ошибка загрузки словаря: " + e.getMessage());
        }

    }

    // Чтение с консоли
    private static String readLine() {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            System.getLogger(Viselitsa.class.getName()).log(System.Logger.Level.ERROR, "Ошибка ввода!", ex);
        }
        return "0";
    }
}
