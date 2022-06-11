package com.binance.client.examples.trade;

import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;

import com.binance.client.examples.constants.PrivateConfig;
import com.binance.client.model.enums.*;
import com.binance.client.model.trade.Position;
import com.binance.client.model.trade.PositionRisk;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PostOrder {
    public static void main(String[] args) {
        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<PositionRisk> risks = null;
            try {
                risks = syncRequestClient.getPositionRisk();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (risks == null || risks.size() == 0) {
                System.out.println("获取持仓为空");
                continue;
            }
            String symbol = "ETHUSDT";
            String symbol1 = "SOLUSDT";
            Map<String, PositionRisk> positionMap = risks.stream().filter(item -> "BOTH".equals(item.getPositionSide())).collect(Collectors.toMap(PositionRisk::getSymbol, Function.identity()));
            PositionRisk positionRisk = positionMap.get(symbol);
            PositionRisk positionRisk1 = positionMap.get(symbol1);

            if (positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) == 0 || positionRisk1.getPositionAmt().compareTo(BigDecimal.ZERO) == 0) {
                System.out.println("有一边持仓为空,请检查");
                break;
            }
            BigDecimal condition = positionRisk.getUnrealizedProfit().add(positionRisk1.getUnrealizedProfit());
            if (condition.compareTo(BigDecimal.valueOf(13)) >= 0) {
                try {
                    System.out.println(syncRequestClient.postOrder(positionRisk.getSymbol(),
                            positionRisk.getPositionAmt().compareTo(BigDecimal.ZERO) < 0 ? OrderSide.BUY : OrderSide.SELL, PositionSide.BOTH, OrderType.MARKET, null,
                            positionRisk.getPositionAmt().abs().toPlainString(), null, null, null, null, null, NewOrderRespType.RESULT));
                    System.out.println(syncRequestClient.postOrder(symbol1, positionRisk1.getPositionAmt().compareTo(BigDecimal.ZERO) < 0 ? OrderSide.BUY : OrderSide.SELL,
                            PositionSide.BOTH, OrderType.MARKET, null,
                            positionRisk1.getPositionAmt().abs().toPlainString(), null, null, null, null, null, NewOrderRespType.RESULT));
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


//        System.out.println(syncRequestClient.postOrder("BTCUSDT", OrderSide.SELL, PositionSide.BOTH, OrderType.MARKET, null,
//                null, null, null, null, null, null, NewOrderRespType.RESULT));
    }
}