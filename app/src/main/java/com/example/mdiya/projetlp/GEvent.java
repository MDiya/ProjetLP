package com.example.mdiya.projetlp;

import com.google.api.services.calendar.model.Events;

public class GEvent {

    public void createEvent(Piscine piscine){
        Event event = buildEvent(piscine);
        event = service.events().insert(calendarId, event).execute();
    }

    public Event buildEvent(Piscine piscine) {
        Event piscineEvent = new Event().setSummary("Aller a la piscine " + p.getNom()).setLocation(p.getAdr()).setDescription("J'y vais");
        DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);
        DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=WEEKLY;COUNT=1"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("popup").setMinutes(10),
        };

        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";

        return Event;
    }
}