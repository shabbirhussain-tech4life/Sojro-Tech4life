package com.mdcbeta.util;


import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.mdcbeta.Events;

import io.reactivex.Observable;

/**
 * Created by Shakil Karim on 12/4/16.
 */

public class RxBus {

    private final Relay<Object> _bus = PublishRelay.create().toSerialized();
    private final BehaviorRelay<Object> sticky_bus = BehaviorRelay.createDefault(new Events.Default());


    public RxBus() {
    }

    public void send(Object o) {
        _bus.accept(o);
    }

    public void sendSticky(Object o) {
        sticky_bus.accept(o);
    }


    public Observable<Object> asStickyObservable() {
        return sticky_bus.filter(o -> !(o instanceof Events.Default));
    }

    public Observable<Object> asObservable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}
