package com.gyhr.config.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gyhr.utils.ResultUtils;
import com.gyhr.utils.ResultVo;
import com.gyhr.web.fee.parkfee.entity.Parkfee;
import com.gyhr.web.fee.parkfee.service.ParkfeeService;
import com.gyhr.web.fee.power.entity.Power;
import com.gyhr.web.fee.power.service.PowerService;
import com.gyhr.web.fee.rental.entity.Rental;
import com.gyhr.web.fee.rental.service.RentalService;
import com.gyhr.web.fee.water.entity.Water;
import com.gyhr.web.fee.water.service.WaterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/alipay")
public class AlipayController {
    // 支付宝沙箱网关地址
    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AlipayConfig alipayConfig;
    @Resource
    private ParkfeeService parkfeeService;
    @Resource
    private PowerService powerService;
    @Resource
    private RentalService rentalService;
    @Resource
    private WaterService waterService;

    @GetMapping("/pay") //alipay/pay?orderNo=xxx
    public void pay(String feeNo, HttpServletResponse httpResponse) throws Exception {
        //查询订单信息
        String feeType = feeNo.split("No")[0];
        BigDecimal Price = BigDecimal.valueOf(0);
        String payName = "";
        String returnUrl = "";
        switch (feeType) {
            case "PKF":
                Parkfee parkfee = parkfeeService.getOne(new QueryWrapper<Parkfee>()
                        .lambda().eq(Parkfee::getFeeNo, feeNo));
                Price = parkfee.getPayMoney();
                payName = "宫苑小区 " + parkfee.getPayMonth() + " 停车费";
                returnUrl = "MyParkFeeList";

                break;
            case "PWF":
                Power power = powerService.getOne(new QueryWrapper<Power>()
                        .lambda().eq(Power::getFeeNo, feeNo));
                Price = power.getPayMoney();
                payName = "宫苑小区 " + power.getPayMonth() + " 电费";
                returnUrl = "MyPowerFeeList";

                break;
            case "RTF":
                Rental rental = rentalService.getOne(new QueryWrapper<Rental>()
                        .lambda().eq(Rental::getFeeNo, feeNo));
                Price = rental.getPayMoney();
                payName = "宫苑小区 " + rental.getPayMonth() + " 租凭费";
                returnUrl = "MyRentalFeeList";

                break;
            case "WTF":
                Water water = waterService.getOne(new QueryWrapper<Water>()
                        .lambda().eq(Water::getFeeNo, feeNo));
                Price = water.getPayMoney();
                payName = "宫苑小区 " + water.getPayMonth() + " 水费";
                returnUrl = "MyWaterFeeList";

                break;
            default:
                return;
        }
        //1. 创建CLinet，通过SDK提供的Client，负责调用支付宝的API
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(), FORMAT, CHARSET, alipayConfig.getAlipayPublicKey(), SIGN_TYPE);

        //2. 创建Request并设置Request参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest(); //发送请求的类
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", feeNo);  // 我们自己生成的订单编号
        bizContent.put("total_amount", Price);   // 订单总金额
        bizContent.put("subject", payName);  //支付的名称
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        request.setReturnUrl("http://localhost:9528/#/myfee/" + returnUrl); //支付成功返回至订单界面
        //执行请求，拿到响应的结果，返回浏览器
        String form = "";
        try {
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public ResultVo<Object> payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getAlipayPublicKey(), "UTF-8"); // 验证签名
            // 支付宝验签
            if (checkSignature) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 更新订单未已支付
                String payNo =params.get("trade_no");
                String feeNo = params.get("out_trade_no");
                Date payTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse(params.get("gmt_payment"));
                String feeType = feeNo.split("No")[0];
                boolean update = false;
                switch (feeType) {
                    case "PKF":
                        Parkfee parkfee = parkfeeService.getOne(new QueryWrapper<Parkfee>()
                                .lambda().eq(Parkfee::getFeeNo, feeNo));
                        parkfee.setPayNo(payNo);
                        parkfee.setPayStatus("1");
                        parkfee.setPayTime(payTime);
                        update = parkfeeService.updateById(parkfee);

                        break;
                    case "PWF":
                        Power power = powerService.getOne(new QueryWrapper<Power>()
                                .lambda().eq(Power::getFeeNo, feeNo));
                        power.setPayNo(payNo);
                        power.setPayStatus("1");
                        power.setPayTime(payTime);
                        update = powerService.updateById(power);

                        break;
                    case "RTF":
                        Rental rental = rentalService.getOne(new QueryWrapper<Rental>()
                                .lambda().eq(Rental::getFeeNo, feeNo));
                        rental.setPayNo(payNo);
                        rental.setPayStatus("1");
                        rental.setPayTime(payTime);
                        update = rentalService.updateById(rental);

                        break;
                    case "WTF":
                        Water water = waterService.getOne(new QueryWrapper<Water>()
                                .lambda().eq(Water::getFeeNo, feeNo));
                        water.setPayNo(payNo);
                        water.setPayStatus("1");
                        water.setPayTime(payTime);
                        update = waterService.updateById(water);

                        break;
                    default:
                        return ResultUtils.error("更新订单失败");
                }
                if (update) {
                    return ResultUtils.success("支付成功");
                }
            }
        }
        return ResultUtils.error("支付失败");
        }
}
