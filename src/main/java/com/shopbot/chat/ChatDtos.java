package com.shopbot.chat;

import com.shopbot.tools.ToolCall;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public final class ChatDtos {

    private ChatDtos() {
    }

    public record ChatRequest(@NotBlank @Size(max = 8000) String message, @Size(max = 64) String sessionId) {
    }

    public record ChatResponse(String answer, List<ToolCall> toolCalls, String sessionId, String version) {
    }
}