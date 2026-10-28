package com.shopbot.tools;

import com.shopbot.orders.OrderStore;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** A new instance is created for every chat request, so "calls" only holds that request's tool calls. */
public class ShopTools {

    private final OrderStore orders;
    private final List<ToolCall> calls = new ArrayList<>();

    public ShopTools(OrderStore orders) {
        this.orders = orders;
    }

    public List<ToolCall> calls() {
        return List.copyOf(calls);
    }

    @Tool(description = "Look up the current status of an order by its numeric order ID.")
    public String getOrderStatus(@ToolParam(description = "Numeric order ID, for example 1042") long orderId) {
        calls.add(new ToolCall("getOrderStatus", Map.of("orderId", orderId)));
        return orders.find(orderId)
                .map(o -> "Order " + o.id() + " (" + o.item() + ") status: " + o.status())
                .orElse("No order found with ID " + orderId);
    }

    @Tool(description = "Cancel an order. Only call this AFTER the customer has explicitly confirmed they want "
            + "to cancel that specific order. Set customerConfirmed to true only in that case.")
    public String cancelOrder(
            @ToolParam(description = "Numeric order ID to cancel") long orderId,
            @ToolParam(description = "True only if the customer explicitly confirmed the cancellation") boolean customerConfirmed) {
        calls.add(new ToolCall("cancelOrder", Map.of("orderId", orderId, "customerConfirmed", customerConfirmed)));
        if (!customerConfirmed) {
            return "NOT CANCELLED: ask the customer to confirm the cancellation first.";
        }
        var order = orders.find(orderId);
        if (order.isEmpty()) {
            return "No order found with ID " + orderId;
        }
        String status = order.get().status();
        if (!status.equals("PROCESSING")) {
            return "Order " + orderId + " cannot be cancelled because its status is " + status;
        }
        orders.setStatus(orderId, "CANCELLED");
        return "Order " + orderId + " has been cancelled.";
    }
}