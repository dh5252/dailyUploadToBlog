package chiwon.dailyUploadToBlog.service;

import chiwon.dailyUploadToBlog.dto.ChatGptRequest;
import chiwon.dailyUploadToBlog.dto.CrolledItemInfo;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatGPTService {

    public void analyzeByChatGpt(List<CrolledItemInfo> crolledItemInfoList, String apiKey) {

        String jsonResponse = "{ \"type\": \"json_object\" }";
        String system = ""

        for (CrolledItemInfo crolledItemInfo : crolledItemInfoList) {
            ChatGptRequest chatGptRequest = new ChatGptRequest(apiKey, jsonResponse, )
        }


    }
}
