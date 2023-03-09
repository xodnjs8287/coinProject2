package com.example.coinProject.webSocket;

import com.example.coinProject.common.JsonUtil;
import com.example.coinProject.common.OrderBookResult;
import com.example.coinProject.common.TickerResult;
import com.example.coinProject.common.Ticket;
import com.example.coinProject.common.TradeResult;
import com.example.coinProject.common.TradeType;
import com.example.coinProject.common.Type;
import com.fasterxml.jackson.databind.JsonNode;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class UpbitWebSocketListener extends WebSocketListener {

    public static final int CODE = 1000;    //기본값으로 정상 종료를 의미함
    private TradeType type;
    private String json;

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("Socket closed : {} / {}\r\n", code, reason);
    }

    @Override
    public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("Socket closing : {} / {}\r\n", code, reason);
        webSocket.close(CODE, null);
        webSocket.cancel();
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t,
        @Nullable Response response) {
        log.error("Socket error : {}\r\n", t.getMessage());
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        JsonNode jsonNode = JsonUtil.fromJson(text, JsonNode.class);
        log.info(jsonNode.toPrettyString());
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        log.info(JsonUtil.fromJson(bytes.string(StandardCharsets.UTF_8), JsonNode.class).toPrettyString());
        switch (type) {
            case TRADE:
                TradeResult tradeResult = JsonUtil.fromJson(bytes.string(StandardCharsets.UTF_8), TradeResult.class);
                log.info(tradeResult.toString());
                break;
            case TICKER:
                TickerResult tickerResult = JsonUtil.fromJson(bytes.string(StandardCharsets.UTF_8), TickerResult.class);
                log.info(tickerResult.toString());
                break;
            case ORDERBOOK:
                OrderBookResult orderBookResult = JsonUtil.fromJson(bytes.string(StandardCharsets.UTF_8), OrderBookResult.class);
                log.info(orderBookResult.toString());
                break;
            default:
                log.error("지원하지 않는 웹소켓 조회 유형입니다. : {}", type.getType());
        }
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        webSocket.send(getParameter());
    }

    public void setParameters(TradeType type, List<String> codes) {
        this.type = type;
        this.json = JsonUtil.toJson(List.of(Ticket.of(UUID.randomUUID().toString()), Type.of(type, codes)));
        System.out.println("test : " + this.json);
    }

    private String getParameter() {
        return this.json;
    }
}
