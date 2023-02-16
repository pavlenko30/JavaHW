import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        Set<Notebook> set = new HashSet<>();
        set.add(new Notebook("Notebook 1", 8, "Win10", 80000, "HP"));
        set.add(new Notebook("Notebook 2", 16, "Win10", 85000, "Asus"));
        set.add(new Notebook("Notebook 3", 32, "linux", 85000, "Lenovo"));
        set.add(new Notebook("Notebook 4", 64, "linux", 90000, "Lenovo"));
        set.add(new Notebook("Notebook 5", 16, "Win10", 50000, "Acer"));

        Operation operation = new Operation(set);
        operation.start();

    }
}