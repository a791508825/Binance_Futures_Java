package com.binance.client.examples.trade;

import com.alibaba.fastjson.JSONObject;
import com.binance.client.RequestOptions;
import com.binance.client.SyncRequestClient;
import com.binance.client.examples.constants.PrivateConfig;
import com.binance.client.model.enums.NewOrderRespType;
import com.binance.client.model.enums.OrderSide;
import com.binance.client.model.enums.OrderType;
import com.binance.client.model.enums.PositionSide;
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

public class PostOrder2 {
    public static void sendmsg(String message) {
        try {
            JSONObject postInfo = new JSONObject();
            postInfo.put("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            // 第一屏也就是钉钉消息列表里面的消息
            String title = "", context = "OKOK,小钱到手test";
            markdown.put("title", title + "|运维");
            markdown.put("text", message+"test");
            postInfo.put("markdown", markdown);
            postInfo.put("isAtAll", false);
            Long timestamp = System.currentTimeMillis();
            String uri = "https://oapi.dingtalk.com/robot/send?access_token=97addf9266699e85eb25cd8534dbe38ef3a285f583fbd859256122863f4b8eac";
            String responseText = "";
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), postInfo.toJSONString());
            Request.Builder requestBuilder = new Request.Builder();
            requestBuilder.url(uri + "&timestamp=" + timestamp);
            requestBuilder.post(requestBody);
            Request request = requestBuilder.build();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            OkHttpClient okHttpClient = builder.connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                responseText = response.body().string();
            } else {
            }
            if (response != null) {
                response.close();
            }
            System.out.println(responseText);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String symbol = "GMTUSDT";
        String symbol1 = "AVAXUSDT";
        int symbolScale = 0;
        int symbol1Scale = 0;

        RequestOptions options = new RequestOptions();
        SyncRequestClient syncRequestClient = SyncRequestClient.create(PrivateConfig.API_KEY, PrivateConfig.SECRET_KEY,
                options);
        while (true) {
            //先检查持仓
            List<PositionRisk> risks1 = null;
            try {
                risks1 = syncRequestClient.getPositionRisk();
            } catch (Exception e) {
                e.printStackTrace();
                sendmsg("出现异常,请检查");
                return;
            }
            if (risks1 == null || risks1.size() == 0) {
                System.out.println("获取持仓为空");
                continue;
            }

            Map<String, PositionRisk> positionMap1 = risks1.stream().filter(item -> "BOTH".equals(item.getPositionSide())).collect(Collectors.toMap(PositionRisk::getSymbol, Function.identity()));
            PositionRisk firstCheck = positionMap1.get(symbol);
            PositionRisk firstCheck1 = positionMap1.get(symbol1);



            if (firstCheck.getPositionAmt().compareTo(BigDecimal.ZERO) == 0 && firstCheck1.getPositionAmt().compareTo(BigDecimal.ZERO) == 0) {

            BigDecimal value = BigDecimal.valueOf(5000);


            List<SymbolPrice> symbolPrices = syncRequestClient.getSymbolPriceTicker(symbol);
            List<SymbolPrice> symbol1Prices = syncRequestClient.getSymbolPriceTicker(symbol1);

            if (symbolPrices == null || symbolPrices.size() == 0 || symbol1Prices == null || symbol1Prices.size() == 0
                    || System.currentTimeMillis() - symbolPrices.get(0).getTime() > 2000
                    || System.currentTimeMillis() - symbol1Prices.get(0).getTime() > 2000) {
                System.out.println("无有效价格");
                continue;
            }
            try {

                if (RandomUtils.nextBoolean()) {
                    System.out.println(syncRequestClient.postOrder(symbol,
                            OrderSide.BUY, PositionSide.BOTH, OrderType.MARKET, null,
                            value.divide(symbolPrices.get(0).getPrice(), symbolScale, RoundingMode.HALF_DOWN).toPlainString(), null, null, null, null, null, NewOrderRespType.RESULT));
                    System.out.println(syncRequestClient.postOrder(symbol1,
                            OrderSide.SELL, PositionSide.BOTH, OrderType.MARKET, null,
                            value.divide(symbol1Prices.get(0).getPrice(), symbol1Scale, RoundingMode.HALF_DOWN).toPlainString(), null, null, null, null, null, NewOrderRespType.RESULT));
                } else {
                    System.out.println(syncRequestClient.postOrder(symbol,
                            OrderSide.SELL, PositionSide.BOTH, OrderType.MARKET, null,
                            value.divide(symbolPrices.get(0).getPrice(), symbolScale, RoundingMode.HALF_DOWN).toPlainString(), null, null, null, null, null, NewOrderRespType.RESULT));
                    System.out.println(syncRequestClient.postOrder(symbol1,
                            OrderSide.BUY, PositionSide.BOTH, OrderType.MARKET, null,
                            value.divide(symbol1Prices.get(0).getPrice(), symbol1Scale, RoundingMode.HALF_DOWN).toPlainString(), null, null, null, null, null, NewOrderRespType.RESULT));
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendmsg("出现异常,请检查");
                return;
            }
            } else if (firstCheck.getPositionAmt().compareTo(BigDecimal.ZERO) != 0 && firstCheck1.getPositionAmt().compareTo(BigDecimal.ZERO) != 0) {

            } else {
                sendmsg("出现异常,请检查");
                return;
            }


            while (true) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<PositionRisk> risks = null;
                try {
                    risks = syncRequestClient.getPositionRisk();
                } catch (Exception e) {
                    e.printStackTrace();
                    sendmsg("出现异常,请检查");
                    return;
                }
                if (risks == null || risks.size() == 0) {
                    System.out.println("获取持仓为空");
                    continue;
                }

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
                        AccountInformation information = syncRequestClient.getAccountInformation();
                        sendmsg("walletBalance:" + information.getTotalWalletBalance() + "unrealizedProfit:" + information.getTotalUnrealizedProfit()+symbol+symbol1);
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendmsg("出现异常,请检查");
                        return;
                    }
                }
            }
        }


//        System.out.println(syncRequestClient.postOrder("BTCUSDT", OrderSide.SELL, PositionSide.BOTH, OrderType.MARKET, null,
//                null, null, null, null, null, null, NewOrderRespType.RESULT));
    }


}