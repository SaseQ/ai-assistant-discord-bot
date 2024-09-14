package it.marczuk.aiassistantdiscordbot.web.google.service;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleCalendarService {

    private final Calendar calendarService;

    public String addTask(String summary, String description, Date startDate){
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(startDate))
                .setTimeZone(TimeZone.getDefault().getID());
        event.setStart(start);

        Date endDate = new Date(startDate.getTime() + 3600000);
        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endDate))
                .setTimeZone(TimeZone.getDefault().getID());
        event.setEnd(end);

        try {
            event = calendarService.events().insert("primary", event).execute();
        } catch (IOException e) {
            log.error("calendarService error");
        }

        log.info("Event created: " + event.getHtmlLink());
        return "Event created: " + event.getHtmlLink();
    }
}
