package com.example.mppbackend.service;

import com.example.mppbackend.api.models.Movie;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebSocketService {
    private List<WebSocketSession> sessions = new ArrayList<>();

    public void notifyFrontend(Movie movie) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> message = new HashMap<>();
        message.put("type", "NEW_MOVIE");
        message.put("movie", movie);
        String messageJson = mapper.writeValueAsString(message);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(messageJson));
            }
        }
    }

}
