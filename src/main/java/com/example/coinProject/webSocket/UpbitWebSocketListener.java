package com.example.coinProject.webSocket;

import com.example.coinProject.common.*;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

@Slf4j
@Repository
public class UpbitWebSocketListener extends WebSocketListener {

    public static final int CODE = 1000;    //기본값으로 정상 종료를 의미함

    private static final int UNIT = 1;

    private static final double EMA = (1 / 14);

    public static List<BigDecimal> currentPrice = new ArrayList<>();
    private TradeType type;
    private String json;

    private TradeResult tradeResult;

    private TickerResult tickerResult;

    private OrderBookResult orderBookResult;

    private final Queue<BigDecimal> up = new LimitedQueue<>(200);
    private final Queue<BigDecimal> down = new LimitedQueue<>(200);


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


    public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        log.info(JsonUtil.fromJson(bytes.string(StandardCharsets.UTF_8), JsonNode.class).toPrettyString());
        switch (type) {
            case TRADE:


                break;
            case TICKER:
                tickerResult = JsonUtil.fromJson(bytes.string(StandardCharsets.UTF_8), TickerResult.class);


                Double upAverage = Double.valueOf(0);
                Double downAverage = Double.valueOf(0);

                BigDecimal AD = new BigDecimal(0);
                BigDecimal AU = new BigDecimal(0);
                if (tickerResult.getChange().equals("FALL")) {
                    down.add(tickerResult.getChangePrice());

                } else if (tickerResult.getChange().equals("RISE")) {
                    up.add(tickerResult.getChangePrice());
                }

                BigDecimal upEma = BigDecimal.valueOf(0);

                BigDecimal downEma = BigDecimal.valueOf(0);

                if (!CollectionUtils.isEmpty(down)) {
                    downEma = down.poll();
                    if (down.size() > 1) {
                        for (BigDecimal downElement : down) {
                            downEma = (downElement.multiply(BigDecimal.valueOf(EMA))).add(downEma.multiply(
                                    BigDecimal.valueOf(1 - EMA)));
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(up)) {
                    upEma = up.poll();
                    if (up.size() > 1) {
                        for (BigDecimal upElement : up) {
                            upEma = (upElement.multiply(BigDecimal.valueOf(EMA))).add(upEma.multiply(
                                    BigDecimal.valueOf(1 - EMA)));
                        }
                    }
                }


                double rs = upEma.doubleValue() / downEma.doubleValue();
                double rsi = 100 - (100 / rs + 1);


                log.info("asd" + " " + rsi);
                log.info(tickerResult.toString());
                break;
            case ORDERBOOK:
                orderBookResult = JsonUtil.fromJson(bytes.string(StandardCharsets.UTF_8), OrderBookResult.class);
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

    public OrderBookResult getOrderBookResult() {
        return orderBookResult;
    }

    public TickerResult getTickerResult() {
        return tickerResult;
    }

    public TradeResult getTradeResult() {
        System.out.println();
        return tradeResult;
    }
}
