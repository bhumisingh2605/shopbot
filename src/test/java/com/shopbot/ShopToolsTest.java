package com.shopbot;

import com.shopbot.orders.OrderStore;
import com.shopbot.tools.ShopTools;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShopToolsTest {

    private final OrderStore store = new OrderStore();

    @Test
    void cancelWithoutConfirmationDoesNothing() {
        ShopTools tools = new ShopTools(store);
        assertThat(tools.cancelOrder(1042, false)).contains("NOT CANCELLED");
        assertThat(store.find(1042).orElseThrow().status()).isEqualTo("PROCESSING");
    }

    @Test
    void confirmedCancelWorksOnProcessingOrder() {
        ShopTools tools = new ShopTools(store);
        assertThat(tools.cancelOrder(1042, true)).contains("cancelled");
        assertThat(store.find(1042).orElseThrow().status()).isEqualTo("CANCELLED");
        assertThat(tools.calls()).hasSize(1);
        assertThat(tools.calls().get(0).name()).isEqualTo("cancelOrder");
    }

    @Test
    void shippedOrderCannotBeCancelled() {
        assertThat(new ShopTools(store).cancelOrder(1043, true)).contains("cannot be cancelled");
    }

    @Test
    void unknownOrderIsReported() {
        assertThat(new ShopTools(store).getOrderStatus(9999)).contains("No order found");
    }
}