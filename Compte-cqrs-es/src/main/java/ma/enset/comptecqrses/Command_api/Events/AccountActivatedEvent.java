package ma.enset.comptecqrses.Command_api.Events;

import lombok.Getter;
import ma.enset.comptecqrses.Command_api.enums.AccountStatus;

public class AccountActivatedEvent extends BaseEvent<String>{
    @Getter private AccountStatus status;

    public AccountActivatedEvent(String id,AccountStatus status) {
        super(id);
        this.status = status;

    }
}
