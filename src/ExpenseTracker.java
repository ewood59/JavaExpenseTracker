import java.util.Scanner;

public class ExpenseTracker {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean running = true;

        while(running){
            System.out.print("""
                    ====== EXPENSE TRACKER ======
                    1. Add an expense
                    2. View all expenses
                    3. View total spending
                    4. Delete an expense
                    5. Exit
                   \s
                    Enter your choice:\s""");
            String choice = input.nextLine();
            switch(choice){
                case "1":
                    System.out.println("Add an expense selected");
                    break;
                case "2":
                    System.out.println("View all expenses selected");
                    break;
                case "3":
                    System.out.println("View total spending selected");
                    break;
                case "4":
                    System.out.println("Delete an expense selected");
                    break;
                case "5":
                    System.out.println("Exit");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        input.close();
    }
}
