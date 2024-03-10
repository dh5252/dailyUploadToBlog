package chiwon.dailyUploadToBlog.service;

import chiwon.dailyUploadToBlog.dto.CrolledItemInfo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

@Service
public class OcrService {

    public void imageToText(CrolledItemInfo crolledItemInfo, String secretKey, String apiURL) {

        try {

            for (String localLink : crolledItemInfo.getDescriptionsLocalLink()) {
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setUseCaches(false);
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                con.setRequestProperty("X-OCR-SECRET", secretKey);
                JSONObject json = new JSONObject();
                json.put("version", "V2");
                json.put("requestId", UUID.randomUUID().toString());
                json.put("timestamp", System.currentTimeMillis());
                JSONObject image = new JSONObject();
                image.put("format", "jpg");
                FileInputStream inputStream = new FileInputStream(localLink);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                image.put("data", buffer);
                image.put("name", "ocrImage");

                JSONArray images = new JSONArray();
                images.put(image);
                json.put("images", images);

                String postParams = json.toString();
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();

                JSONObject obj = new JSONObject(response.toString());
                JSONArray responseImages = obj.getJSONArray("images");

                StringBuilder inferTextCombined = new StringBuilder();
                for (int i = 0 ; i < responseImages.length() ; i++) {
                    JSONObject responseImage = responseImages.getJSONObject(i);
                    JSONArray fields = responseImage.getJSONArray("fields");

                    for (int j = 0 ; j < fields.length() ; j++) {
                        JSONObject field = fields.getJSONObject(j);
                        String inferText = field.getString("inferText");
                        inferTextCombined.append(inferText).append(" ");
                    }
                }
                crolledItemInfo.setDescription(crolledItemInfo.getDescription() + "\n" + inferTextCombined.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
