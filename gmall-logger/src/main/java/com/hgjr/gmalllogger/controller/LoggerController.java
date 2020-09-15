package com.hgjr.gmalllogger.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hgjr.gmall.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoggerController {
    @PostMapping("/log")
    public String logger(@RequestParam("log") String log){
        System.out.println(log);

        log = addTs(log);
        saveToFile(log);
//        sendToKafka(log);
        return "ok";

    }
    @Autowired
    KafkaTemplate template;
    private void sendToKafka(String log) {
        String topic = Constant.TOPIC_STARTUP;
        if(log.contains("event")){
            topic = Constant.TOPIC_EVENT;
        }
        template.send(topic,log);
    }

    private Logger logger = LoggerFactory.getLogger(LoggerController.class);
    private void saveToFile(String log) {
        logger.info(log);

    }

    private String addTs(String log) {
        JSONObject obj = JSON.parseObject(log);
        obj.put("ts", System.currentTimeMillis());
        return obj.toJSONString();
    }
}
