import java.util.List;

public class Main {
    public static void main(String[] args) {
        Toylist toys1 = new Toylist();
        toys1.addToy(new Toy(25, "Плюшевый медведь", 3));
        toys1.addToyList(List.of(
                new Toy(5, "Велосипед", 1),
                new Toy(10, "Телефон детский", 2),
                new Toy(10, "Игрушка антистресс детская", 2)
        ));

        ParticipantPriority pq = new ParticipantPriority(List.of(
                new Participant("Коля"),
                new Participant("Игорь"),
                new Participant("Павел"),
                new Participant("Инна"),
                new Participant("Марина"),
                new Participant("Олег"),
                new Participant("Андрей"),
                new Participant("Вика"),
                new Participant("Олеся"),
                new Participant("Мариана")
        ));
        Lotto raf = new Lotto(pq, toys1);
        System.out.println(raf.currentToys.toString());
        raf.runRaffle();

        toys1.saveToFile();
    }

}
