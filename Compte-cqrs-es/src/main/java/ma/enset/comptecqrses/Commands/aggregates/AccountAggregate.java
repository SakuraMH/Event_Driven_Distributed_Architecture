package ma.enset.comptecqrses.Commands.aggregates;

import ma.enset.comptecqrses.Command_api.Commands.CreatAccountCommand;
import ma.enset.comptecqrses.Command_api.Commands.CreditAccountCommand;
import ma.enset.comptecqrses.Command_api.Commands.DebitAccountCommand;
import ma.enset.comptecqrses.Command_api.Events.AccountActivatedEvent;
import ma.enset.comptecqrses.Command_api.Events.AccountCreatedEvent;
import ma.enset.comptecqrses.Command_api.Events.AccountCreditedEvent;
import ma.enset.comptecqrses.Command_api.Events.AccountDebitedEvent;
import ma.enset.comptecqrses.Command_api.enums.AccountStatus;
import ma.enset.comptecqrses.Command_api.exceptions.AmountNegativeExceprion;
import ma.enset.comptecqrses.Command_api.exceptions.BalanceNotSufficientException;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private double balance;
    private String currency;
    private AccountStatus status;

    public AccountAggregate() {
        //Required by AXON
    }

    @CommandHandler
    public AccountAggregate(CreatAccountCommand creatAccountCommand) {
        if(creatAccountCommand.getInitialBalance()<0) throw new RuntimeException("Impossible....");
           //OK

        AggregateLifecycle.apply(new AccountCreatedEvent(
                creatAccountCommand.getId(),
                creatAccountCommand.getInitialBalance(),
                creatAccountCommand.getCurrency(),
                AccountStatus.CREATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event){
        this.accountId= event.getId();
        this.balance= event.getInitialBalance();
        this.currency= event.getCurrency();
        this.status=AccountStatus.CREATED;
        AggregateLifecycle.apply(new AccountActivatedEvent(
                event.getId(),
                AccountStatus.ACTIVATED
        ));

    }
    @EventSourcingHandler
    public void on(AccountActivatedEvent event){
        this.status=event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand creditAccountCommand){
        if(creditAccountCommand.getAmount()<0) throw new AmountNegativeExceprion("Amount should not be negative");
        AggregateLifecycle.apply(new AccountCreditedEvent(
                creditAccountCommand.getId(),
                creditAccountCommand.getAmount(),
                creditAccountCommand.getCurrency()
        ));

    }
    @EventSourcingHandler
    public void on(AccountCreditedEvent event){
        this.balance+=event.getAmount();

    }

    @CommandHandler
    public void handle(DebitAccountCommand debitAccountCommand){
        if(debitAccountCommand.getAmount()<0) throw new AmountNegativeExceprion("Amount should not be negative");
        if(this.balance < debitAccountCommand.getAmount()) throw new BalanceNotSufficientException("Balance not sufficient Exception =>"+balance);

        AggregateLifecycle.apply(new AccountDebitedEvent(
                debitAccountCommand.getId(),
                debitAccountCommand.getAmount(),
                debitAccountCommand.getCurrency()
        ));

    }
    @EventSourcingHandler
    public void on(AccountDebitedEvent event){
        this.balance-=event.getAmount();

    }


}
