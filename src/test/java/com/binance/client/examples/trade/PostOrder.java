package com.binance.client.examples.trade;

import com.alibaba.fastjson.JSONObject;
import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;

import com.binance.client.examples.constants.PrivateConfig;
import com.binance.client.model.enums.*;
import com.binance.client.model.market.SymbolPrice;
import com.binance.client.model.trade.AccountInformation;
import com.binance.client.model.trade.PositionRisk;
import okhttp3.*;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PostOrder {

    public static void main(String[] args) {
        String symbol = "ETHUSDT";
        String symbol1 = "SOLUSDT";
        int symbolScale = 3;
        int symbol1Scale = 0;
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