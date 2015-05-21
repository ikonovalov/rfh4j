package ru.codeunited.wmq.bus;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

/**
 * codeunited.ru
 * konovalov84@gmail.com
 * Created by ikonovalov on 19.05.15.
 */
public class DeadMessageListener {

    @Subscribe @AllowConcurrentEvents
    public void handle(DeadEvent deadEvent) {

    }

}
