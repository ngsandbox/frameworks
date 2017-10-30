package org.frameworks.nao;

import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALMemory;

import java.util.Objects;

public class NaoSubscriber implements AutoCloseable {
    private ALMemory memory;

    public NaoSubscriber(Session session) throws Exception {

        memory = new ALMemory(session);
    }

    public long subscribe(NaoEvent event, EventCallback callback) throws Exception {
        Objects.requireNonNull(event);
        Objects.requireNonNull(callback);
        return memory.subscribeToEvent(event.getEvent(), callback);
    }


    @Override
    public void close() throws Exception {
        memory.unsubscribeAllEvents();
    }
}
