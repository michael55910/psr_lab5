import com.couchbase.client.java.Bucket;
import config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        new Config();

        AnimalRepository animalRepository = new AnimalRepository();
        CageRepository cageRepository = new CageRepository();

        //MENU
        Menu menu = new Menu();
        while (true) {
            int operation = menu.selectOperation();
            if (operation == 0) {
                return;
            }
            int target = menu.selectTarget();
            switch (operation) {
                case 1:
                    switch (target) {
                        case 1 -> animalRepository.add();
                        /*case 2 -> repairBookEntryRepository.addEntry();*/
                    }
                    break;
                case 2:
                    switch (target) {
                        case 1 -> animalRepository.updateById(getId());
                        /*case 2 -> repairBookEntryRepository.updateById(getId());*/
                    }
                    break;
                case 3:
                    switch (target) {
                        case 1 -> animalRepository.deleteById(getId());
                        /*case 2 -> repairBookEntryRepository.deleteById(getId());*/
                    }
                    break;
                case 4:
                    switch (target) {
                        case 1 -> animalRepository.getById(getId());
                        /*case 2 -> repairBookEntryRepository.getById(getId());*/
                    }
                    break;
                case 5:
                    Scanner scanner = new Scanner(System.in);
                    switch (target) {
                        case 1 -> {
                            System.out.print("Enter name: ");
                            animalRepository.getByName(scanner.next());
                        }
                        /*case 2 -> repairBookEntryRepository.getByDate();*/
                    }
                    break;
                case 6:
                    switch (target) {
                        case 1 -> animalRepository.getAvgAge();
                    }
                    break;
                case 7:
                    switch (target) {
                        case 1 -> animalRepository.getAll();
                    }
                    break;
                default:
                    System.out.println("Błędny wybór!");
                    return;
            }
        }

    }

    @NotNull
    private static Long getId() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Podaj id: ");
        return scanner.nextLong();
    }
}
