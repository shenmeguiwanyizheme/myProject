package com.example.administrator.extenal;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class alipay {
    @ResponseBody
    @RequestMapping("/alipay/callBack")
    public String ali(AlipayTradePayModel aliPayTradeModel) {

        try {
            AliPayTradePayService aliPayTradePayService = new AliPayTradePayService();
            return aliPayTradePayService.callBack(aliPayTradeModel);
        } catch (Exception e) {
            return "failure";
        }
    }

    public String createOrder() {
        AlipayClient alipayClient = new DefaultAlipayClient("", "", "", "", "", "");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayResponse response = new AlipayTradeAppPayResponse();
        return response.getBody();//返回支付宝订单id，字符串类型，不是纯数字
    }
}

class AliPayTradePayService {
    String ALIPAY_PUBLIC_KEY = "";

    public String callBack(AlipayTradePayModel alipayTradePayModel) {

        //   boolean signed = AlipaySignature.rsaCertCheckV1(jsonObject, ALIPAY_PUBLIC_KEY, "UTF-8", "RSA2");
        return null;
    }
}