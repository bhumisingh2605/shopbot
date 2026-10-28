package com.shopbot.chat;

import com.shopbot.chat.ChatDtos.ChatRequest;
import com.shopbot.chat.ChatDtos.ChatResponse;
import com.shopbot.orders.OrderStore;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class ChatController {

    private final ShopAgent agent;
    private final OrderStore orders;

    public ChatController(ShopAgent agent, OrderStore orders) {
        this.agent = agent;
        this.orders = orders;
    }

    @PostMapping("/chat")
    public ChatResponse chat(@Valid @RequestBody ChatRequest req) {
        String session = req.sessionId() == null || req.sessionId().isBlank()
                ? UUID.randomUUID().toString() : req.sessionId();
        return agent.chat(session, req.message());
    }

    /** Test helper: puts the fake orders back to their starting state. */
    @PostMapping("/admin/reset")
    public Map<String, String> reset() {
        orders.reset();
        return Map.of("status", "reset");
    }
}