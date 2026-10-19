import java.math.BigDecimal;
import java.math.RoundingMode;

public class Expense {

    private String name;
    private BigDecimal amount;
    private String category;

    public Expense(String name, BigDecimal amount, String category){
        this.name = name;
        this.amount = amount;
        this.category = category;
    }

    @Override
    public String toString(){
        return name + " - $" + amount.setScale(2, RoundingMode.HALF_UP)+ " (" + category + ")";
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getName(){
        return name;
    }

    public String getCategory(){
        return category;
    }
}
