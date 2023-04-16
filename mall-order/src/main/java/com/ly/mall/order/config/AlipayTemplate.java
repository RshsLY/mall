package com.ly.mall.order.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.ly.mall.order.vo.PayVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2021000122615519";

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCjKcInqUDNQ4P6XjtEatLYm5VxfGrRsUl+HO+PX0E14N/XUpHx+kLyQ1TOclbx9ZJBrl2t8ajfvP6W9Jj/BDqRG7XAUBGTXlAOUprr222FymsyPhZF1EahvM0k5T6D1CiwCKKCnBf1tUxaI90S0s9bdSorI1AHv5rlpsGjtSLySDg2odyLSg+TNbf+y+fa022u32iv9LfaHp8x8XLuBWOgL/V8zeS1zeogR1T7e/LYVilzKExG/xLWIwLdR/PZPNp8xJRAYRAPIdfbiL+j+Fi2GA1eWQBuWYIegChpdWbw5O55Fb89SNN03yrrAf129rysrNnYCsq6S6trycEYx2wJAgMBAAECggEAVIP94QNn4Ks3Qsnq0z6uJqs4lsdGoYW+tseQQ2io45qu9976YCgs40aDOEi7xqtYbhM+zSofOn2wNktOsNHk8GM1Gv+eiwJQX4rkWoWXzrSSID1O1i7lw0OD07e3URvlU4G1hAgM1SZq/UoUanquFAa1qBCrlMfoGUruhLj/+BUfSLShFrGqW5vUgTFDFb7uNayXbfne7hC4LPFCNjK2J3KKkckzMx9oqMMnvGMxY+np1yPqu6VXLmpfc1dlLmB4j2f3hW9SZ8P7gfMQCnYo4LRZ1Na3/On9xvjj/dLT2nrPDMyJ6vjCkmigh9nMGZmTQ5ShBjymgiKX/j0MPcmJsQKBgQD1y3WGbLi26FgPL49nO4wU88K9TMiuMiEDKaPJNYiC7qpgP6MNO7PXlzRtw6FyUm7GnD5IBfTkAfk8n1afreXUUtFJuKgxZQr9serdj1g0DD5Ey2KuWw3gKwgtYxYHJrxoPKV964zoDItdiF1dCB6aH4RNYc2M39hVTEA22sDRPQKBgQCp8ALxyg5F328VGbrQnH0EdhqxYskAEmYwR3zPJszyeWzkn1E/vRuqTNYeHSDPXXsco2zELhiHDBOmTLtpvR7w3lifUey1fxv2il2YQZHEZskPiPdLcw+VqrD999WlDZBVAqN4J6Bprt+bwsaAghtbowk/jt0UHVUWfAthVSvavQKBgGx6izf0q+Et0g+Bsgyhn0Vv/7aEON2IikFXE04uLj17D/7Clzez7b9soFsBSDOoZfOJ6YLn/hjTNiinOhfnK9P1aSDcOn7yVVN6PmniZc8yyBicmsTQsuMImudNH9+wHN70RSW528peHvP8xxfUCY2D82QjkhBmipe6u2x6RzcRAoGBAIiypsu0y5JtTz/b+KjCmNYz4vj6C9Hg+w6vErjX71jFUFD/zy3EgzKUqFCFAdWMzvGUjHOq2NQZZN0LMF64YOFFGmjeT9kXrHi/iNZnOEiSnf2/IZ4xqkGskr9Y0yT4KjgX7UNELQeWQg1buq24+yTVmyTjWPdLORzjH4xmJObJAoGAfXJGlXSxQhVtK9x3DS/QE4HQNx7WKgy4XjGsSO74EwmjC8t/QrkAUkANRyhOfc8IWbmIUe4VHAEMszBl3jzdbwPpK6mJDWJYPxpZyGgbiEsr2BgFe+MNmGASjBzjE67O7hv0D/mX2IOjaT7S2ZAgbfTF95SY3j43NeOb+djlOd4=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmiRi4Snfvr+7MXCO8/Ic4NIgkGEmcOCjjO1KBTXPeZU3Kp4+EN6MctC7Vzf65T9VutYqkp+sj510y4K7/xSqcpel/UyXBI8LLJlLHkskcvmKXY3aS//LaMH5MBXfPX6lkZpC8XkpHwczqeec1qXlMG4UWb7PAh7uvQLoOIuDE7EUNH0d6NhACaZn1tL7AmXF/Vsbl0xuDFFKYFxXuivm8JY92tZIFhT7d2wbxUEH2rftgvsQO/JARXOI2f+910Dh5aUEmuK7QXN9npgY8ystK4vxi/S1jjhSJMDYck9wnbyvyD+jSKDdzlVQVwPYI/Pz3fT7VXs1bd8/EmQwMvdvpwIDAQAB";
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url="https://687p20p445.goho.co/payed/notify";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url="http://member.mall.com/orderList.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        String timeout="1m";

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"time_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
