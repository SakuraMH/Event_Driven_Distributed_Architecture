package ma.enset.comptecqrses.Command_api.Commands;

import lombok.Getter;

public class CreatAccountCommand extends BaseCommand<String>{
    @Getter
    private double initialBalance;
     @Getter private String currency;

    public CreatAccountCommand(String id, double initialBalance, String currency) {
        super(id);
        this.initialBalance = initialBalance;
        this.currency = currency;
    }
}
