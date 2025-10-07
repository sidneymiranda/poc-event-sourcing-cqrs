package com.github.sidneymiranda.order.command.application.port.out;

import com.eventstore.dbclient.EventData;
import java.util.List;

public interface EventStoreRepository {
    void appendStream(String streamName, List<EventData> events);
}
