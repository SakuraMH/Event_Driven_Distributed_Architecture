package ma.enset.comptecqrses.Commands.Controllers;

import lombok.AllArgsConstructor;
import ma.enset.comptecqrses.Command_api.Commands.CreatAccountCommand;
import ma.enset.comptecqrses.Command_api.Commands.CreditAccountCommand;
import ma.enset.comptecqrses.Command_api.Commands.DebitAccountCommand;
import ma.enset.comptecqrses.Command_api.dtos.CreateAccountRequestDTO;
import ma.enset.comptecqrses.Command_api.dtos.CreditAccountRequestDTO;
import ma.enset.comptecqrses.Command_api.dtos.DebitAccountRequestDTO;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@RestController
@RequestMapping("/commands/account")
@AllArgsConstructor
public class AccountCommandController {
    private CommandGateway commandGateway;
    private EventStore eventStore;
    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request){
        CompletableFuture<String> commandResponse=commandGateway.send(new CreatAccountCommand(
                UUID.randomUUID().toString(),
                request.getInitialBalance(),
                request.getCurrency()
        ));
        return commandResponse;
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception exception){
          ResponseEntity<String> entity= new ResponseEntity<>(
                  exception.getMessage(),
                  HttpStatus.INTERNAL_SERVER_ERROR
                  );
          return entity;
    }

    @GetMapping("/eventStore/{accountId}")
    public Stream eventStore(@PathVariable String accountId){
        return eventStore.readEvents(accountId).asStream();
    }

    @PutMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request){
        CompletableFuture<String> commandResponse=commandGateway.send(new CreditAccountCommand(
              request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        ));
        return commandResponse;
    }

    @PutMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request){
        CompletableFuture<String> commandResponse=commandGateway.send(new DebitAccountCommand(
                request.getAccountId(),
                request.getAmount(),
                request.getCurrency()
        ));
        return commandResponse;
    }
}
