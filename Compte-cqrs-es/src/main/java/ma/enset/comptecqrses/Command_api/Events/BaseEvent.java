package ma.enset.comptecqrses.Command_api.Events;

import lombok.Getter;

public abstract class BaseEvent<T> {
    @Getter private T id;

    public BaseEvent(T id) {
        this.id = id;
    }
}
