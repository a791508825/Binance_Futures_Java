package com.binance.client.examples.trade;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.examples.constants.PrivateConfig;

import java.math.BigDecimal;

public class PostOrder3 {

    public static void main(String[] args) {
        String symbol = "BTCUSDT";
        String symbol1 = "BNBUSDT";
        int symbolScale = 3;
        int symbol1Scale = 2;
        BigDecimal value = BigDecimal.valueOf(5000);
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        TaskService taskService = new TaskService();
        taskService.task(symbol, symbol1, symbolScale, symbol1Scale, value, syncRequestClient);

//        System.out.println(syncRequestClient.postOrder("BTCUSDT", OrderSide.SELL, PositionSide.BOTH, OrderType.MARKET, null,
//                null, null, null, null, null, null, NewOrderRespType.RESULT));
    }


}