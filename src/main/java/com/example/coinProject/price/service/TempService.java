package com.example.coinProject.price.service;

import com.example.coinProject.common.TradeType;
import com.example.coinProject.webSocket.UpbitWebSocketListener;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TempService {

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
            .url("wss://api.upbit.com/websocket/v1")
            .build();

        UpbitWebSocketListener webSocketListener = new UpbitWebSocketListener();
        webSocketListener.setParameters(TradeType.TRADE, List.of("KRW-BTC"));

        client.newWebSocket(request, webSocketListener);
        client.dispatcher().executorService().shutdown();
    }
}
