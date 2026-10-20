import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExpenseTracker {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Expense> expenses = new ArrayList<>();
        loadExpenses(expenses);

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
                    String name = null;
                    boolean trying = true;
                    while(trying) {
                        System.out.print("Enter expense name: ");
                        name = input.nextLine();
                        if(name.isBlank() || name.contains(",")) {
                            System.out.println("Invalid name! Cannot be blank or contain commas.");
                            continue;
                        }
                        trying = false;
                    }

                    BigDecimal cost = null;
                    trying = true;
                    while(trying) {
                        System.out.print("Enter cost: ");
                        try {
                            cost = new BigDecimal(input.nextLine());
                            if(cost.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
                            cost = cost.setScale(2, RoundingMode.UNNECESSARY);
                            trying = false;
                        } catch (NumberFormatException | ArithmeticException e) {
                            System.out.println("Invalid input");
                        }
                    }

                    String category = null;
                    trying = true;
                    while(trying) {
                        System.out.print("Enter category: ");
                        category = input.nextLine();
                        if(category.isBlank() || category.contains(",")) {
                            System.out.println("Invalid category! Cannot be blank or contain commas.");
                            continue;
                        }
                        trying = false;
                    }

                    Expense expense = new Expense(name, cost, category);
                    expenses.add(expense);
                    System.out.println("Expense added successfully!");
                    break;
                case "2":
                    if(expenses.isEmpty()){
                        System.out.println("No expenses recorded yet!");
                    }
                    else{
                        System.out.println("====== ALL EXPENSES ======");
                        int i = 1;
                        for(Expense e : expenses){
                            System.out.println(i + ". " + e.toString());
                            i++;
                        }
                    }
                    break;
                case "3":
                    BigDecimal total = BigDecimal.ZERO;
                    for(Expense e : expenses){
                        total = total.add(e.getAmount());
                    }
                    System.out.println("====== TOTAL SPENDING ======");
                    System.out.println("Total expenses: $" + total.setScale(2, RoundingMode.HALF_UP));
                    break;
                case "4":
                    if(expenses.isEmpty()){
                        System.out.println("No expenses to delete!");
                    }
                    else{
                        System.out.println("====== ALL EXPENSES ======");
                        int i = 1;
                        for(Expense e : expenses){
                            System.out.println(i + ". " + e.toString());
                            i++;
                        }

                        int index = -1;
                        boolean tryingIndex = true;
                        while(tryingIndex) {
                            System.out.print("Enter the number of the expense to delete: ");
                            try {
                                index = Integer.parseInt(input.nextLine()) - 1;
                                if(index < 0 || index >= expenses.size()) throw new NumberFormatException();
                                tryingIndex = false;
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid index");
                            }
                        }

                        String removed = expenses.get(index).getName();
                        expenses.remove(index);
                        System.out.println(removed + " successfully deleted!");

                    }
                    break;
                case "5":
                    System.out.println("Exit");
                    saveExpenses(expenses);
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }

        input.close();
    }

    public static void saveExpenses(ArrayList<Expense> expenses){
        Path original = Path.of("expenses.csv");
        Path temp = Path.of("expenses-temp.csv");

        try{
            try (PrintWriter writer = new PrintWriter(new FileWriter(temp.toFile()))) {
                for (Expense e : expenses) {
                    writer.println(e.getName() + "," + e.getAmount() + "," + e.getCategory());
                }
                if (writer.checkError()) throw new IOException("Error writing temporary file!");
            }
            Files.move(temp, original, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }

    public static void loadExpenses(ArrayList<Expense> expenses) {
        try (BufferedReader reader = new BufferedReader(new FileReader("expenses.csv"))) {
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",", -1);
                if (parts.length != 3 || parts[0].isBlank() || parts[2].isBlank()){
                    System.out.println("Skipping invalid expense!");
                    continue;
                }

                BigDecimal cost = null;
                try {
                    cost = new BigDecimal(parts[1]);
                    if(cost.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
                    cost = cost.setScale(2, RoundingMode.UNNECESSARY);
                }
                catch (NumberFormatException | ArithmeticException e) {
                    System.out.println("Skipping invalid expense!");
                    continue;
                }
                Expense newExpense = new Expense(parts[0], cost, parts[2]);
                expenses.add(newExpense);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("No saved expenses found. Starting fresh!");
        }
        catch (IOException e) {
            System.out.println("Error reading file!");
        }
    }
}