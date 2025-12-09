package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.events.ToDoItemReleasedEvent;
import com.smalaca.taskamanager.model.interfaces.ToDoItem;
import com.smalaca.taskamanager.registry.EventsRegistry;

class ToDoItemReleasedProcessor {
    private final EventsRegistry eventsRegistry;

    ToDoItemReleasedProcessor(EventsRegistry eventsRegistry) {
        this.eventsRegistry = eventsRegistry;
    }

    public void process(ToDoItem toDoItem) {
        ToDoItemReleasedEvent event = new ToDoItemReleasedEvent();
        event.setToDoItemId(toDoItem.getId());
        eventsRegistry.publish(event);
    }
}
