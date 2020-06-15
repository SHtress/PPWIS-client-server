package kek;

import java.util.Random;

public class RandomizationData {
    private static String[] surnameList = {
            "Бутрин", "Окабэ", "Василевский", "Поживилко", "Штрасс", "Козловский", "Распутин", "Жуков", "Козлов", "Лукашэнко", "Шункевич", "Голенков", "Петров", "Садовский", "Синельников", "Раманов", "Калиновский", "Зализняк", "Шагал", "Малевич", "Гандзи", "Ленин", "Гарбачев", "Гагарин", "Леонтьев", "Лавкрафт", "Манчан", "Колас", "Купала", "Лермантов", "Блок", "Ломаносов", "Летов", "Энштэйн", "Эйлер", "Бернули", "Буль", "Лем", "Цой", "Ким", "Маркс", "Дэн", "Мао", "Ван Бюрэн", "Махно", "Гаус", "Рыбников", "Ямаока", "Кодзима", "Хэмил", "Форд", "Гётэ", "Станкевич", "Кутузов", "Камю", "Оруэл", "Макисе", "Чёрч", "Пит", "Граймс", "Анно", "Тьюринг", "Фримэн", "Калхун", "Вэнс", "Клаус", "Азимов", "Стругацкий", "Гонтарев", "Ли", "Кобэйн", "Кинг"
    };
    private static String[] nameList =
    {
        "Станислав", "Линус", "Андрей", "Алексей", "Владимир", "Павел", "Джон", "Александр", "Генадий", "Максим", "Юрий", "Иван", "Марк", "Казимир", "Ульфрык", "Герберт", "Стэфан", "Якуб", "Янка", "Михаил", "Даниил", "Дмитрий", "Сергей", "Виктор", "Чен Ир", "Ир Сэн", "Чен Ин", "Нурсултан", "Мустафа", "Карл", "Цзэдун", "Отто", "Адольф", "Николай", "Армэн", "Ринтаро", "Ганс", "Нестер", "Стив", "Глеб", "Акира", "Хидэо", "Сердж", "Томми", "Генрих", "Илья", "Напалеон", "Герман", "Сильвестер", "Жан-Жак", "Алан", "Гордан", "Барни", "Айзэк", "Куан Ю", "Макар", "Курт", "Мартин", "Лютэр", "Камина", "Симон"
    };
    private static String[] patronymList =
    {
        "Адамович", "Платонович", "Семёнович", "Рыгоравич", "Иванович", "Александрович", "Валентинович", "Валерьянович", "Игаревич", "Анатольевич", "Мустафавич", "Стэфанавич", "Юрьевич", "Армени", "Эстрыдсан", "Торстэнсан", "Антыпойка", "Васи", "Пека", "Салим-аглы", "ибн Мухаммад", "бэн Давид", "бар Йохай", "фи дэ Геральд", "Карлавич", "Ильич", "Петрович", "Гендасан", "Герасимович", "Сидорович"
    };

    public static String reqSurname() {
        return surnameList[new Random().nextInt(surnameList.length)];
    }

    public static String reqName() {
        return nameList[new Random().nextInt(nameList.length)];
    }

    public static String reqPatronym() {
        return patronymList[new Random().nextInt(patronymList.length)];
    }

}
