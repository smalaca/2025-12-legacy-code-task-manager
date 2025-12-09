package com.smalaca.taskamanager.processor;

import com.smalaca.taskamanager.registry.EventsRegistry;
import com.smalaca.taskamanager.service.CommunicationService;
import com.smalaca.taskamanager.service.ProjectBacklogService;
import com.smalaca.taskamanager.service.SprintBacklogService;
import com.smalaca.taskamanager.service.StoryService;

import static org.mockito.Mockito.mock;

class ToDoItemProcessorWorkshopTest {
    private final StoryService storyService = mock(StoryService.class);
    private final EventsRegistry eventsRegistry = mock(EventsRegistry.class);
    private final ProjectBacklogService projectBacklogService = mock(ProjectBacklogService.class);
    private final CommunicationService communicationService = mock(CommunicationService.class);
    private final SprintBacklogService sprintBacklogService = mock(SprintBacklogService.class);
    private final ToDoItemProcessor toDoItemProcessor = new ToDoItemProcessor(
            storyService, eventsRegistry, projectBacklogService, communicationService, sprintBacklogService);

//    @BeforeEach
//    void setUnderTest() {
//        toDoItemProcessor.setUnderTest(true);
//    }

//    @Test
//    void baseline() {
//        Task task = mock(Task.class);
//
//        toDoItemProcessor.processFor(task);
//
//        InOrder inOrder = inOrder(
//                task, storyService, eventsRegistry, projectBacklogService,
//                communicationService, sprintBacklogService);
//        inOrder.verifyNoMoreInteractions();
//    }

//    @Test
//    void shouldProcessTaskReleased() {
//        Task task = mock(Task.class);
//        BDDMockito.given(task.getStatus()).willReturn(RELEASED);
//        BDDMockito.given(task.getId()).willReturn(42L);
//        ToDoItemReleasedEvent toDoItemReleasedEvent = mock(ToDoItemReleasedEvent.class);
//        toDoItemProcessor.setToDoItemReleasedEvent(toDoItemReleasedEvent);
//
//        toDoItemProcessor.processFor(task);
//
//        InOrder inOrder = inOrder(
//                task, toDoItemReleasedEvent, storyService, eventsRegistry, projectBacklogService,
//                communicationService, sprintBacklogService);
//        inOrder.verify(task).getStatus();
//        inOrder.verify(task).getId();
//        inOrder.verify(toDoItemReleasedEvent).setToDoItemId(42L);
//        inOrder.verify(eventsRegistry).publish(toDoItemReleasedEvent);
//        verifyNoMoreInteractions(
//                task, toDoItemReleasedEvent, storyService, eventsRegistry, projectBacklogService,
//                communicationService, sprintBacklogService);
//    }
//
//    @Test
//    void shouldProcessStoryReleased() {
//        Story story = mock(Story.class);
//        BDDMockito.given(story.getStatus()).willReturn(RELEASED);
//        BDDMockito.given(story.getId()).willReturn(42L);
//        ToDoItemReleasedEvent toDoItemReleasedEvent = mock(ToDoItemReleasedEvent.class);
//        toDoItemProcessor.setToDoItemReleasedEvent(toDoItemReleasedEvent);
//
//        toDoItemProcessor.processFor(story);
//
//        InOrder inOrder = inOrder(
//                story, toDoItemReleasedEvent, storyService, eventsRegistry, projectBacklogService,
//                communicationService, sprintBacklogService);
//        inOrder.verify(story).getStatus();
//        inOrder.verify(story).getId();
//        inOrder.verify(toDoItemReleasedEvent).setToDoItemId(42L);
//        inOrder.verify(eventsRegistry).publish(toDoItemReleasedEvent);
//        verifyNoMoreInteractions(
//                story, toDoItemReleasedEvent, storyService, eventsRegistry, projectBacklogService,
//                communicationService, sprintBacklogService);
//    }
//
//    @Test
//    void shouldProcessEpicReleased() {
//        Epic epic = mock(Epic.class);
//        BDDMockito.given(epic.getStatus()).willReturn(RELEASED);
//        BDDMockito.given(epic.getId()).willReturn(42L);
//        ToDoItemReleasedEvent toDoItemReleasedEvent = mock(ToDoItemReleasedEvent.class);
//        toDoItemProcessor.setToDoItemReleasedEvent(toDoItemReleasedEvent);
//
//        toDoItemProcessor.processFor(epic);
//
//        InOrder inOrder = inOrder(
//                epic, toDoItemReleasedEvent, storyService, eventsRegistry, projectBacklogService,
//                communicationService, sprintBacklogService);
//        inOrder.verify(epic).getStatus();
//        inOrder.verify(epic).getId();
//        inOrder.verify(toDoItemReleasedEvent).setToDoItemId(42L);
//        inOrder.verify(eventsRegistry).publish(toDoItemReleasedEvent);
//        verifyNoMoreInteractions(
//                epic, toDoItemReleasedEvent, storyService, eventsRegistry, projectBacklogService,
//                communicationService, sprintBacklogService);
//    }
}