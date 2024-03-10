package chiwon.dailyUploadToBlog.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ImgProcessingService {

    public void downloadImage(String imgUrl, String dest) {

        try (InputStream in = new URL(imgUrl).openStream();
             OutputStream out = new FileOutputStream(dest)) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int processingImage(String originImgUrl, String destDir, int imgcnt) {
        try {
            BufferedImage originImg = ImageIO.read(new File(originImgUrl));

            int width = originImg.getWidth();
            int height = originImg.getHeight();
            int flag = 0;

            int x = 0, y = 0;

            while ((double)width / height < (double)11 / 17) {
                int desiredHeight = (int)((double)width / 11 * 17 + 0.5);
                if (desiredHeight > height)
                    desiredHeight = height;
                BufferedImage croppedImage = originImg.getSubimage(x, y, width, desiredHeight);
                ImageIO.write(croppedImage, "jpg", new File(destDir + String.valueOf(imgcnt++) + ".jpg"));
                y += desiredHeight;
                height -= desiredHeight;
                flag = 1;
                if (height == 0) break;
            }
            if (height > 0) {
                if (flag == 0) {
                    try {
                        Files.copy(Paths.get(originImgUrl), Paths.get(destDir + String.valueOf(imgcnt++) + ".jpg"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    BufferedImage croppedImage = originImg.getSubimage(x, y, width, height);
                    ImageIO.write(croppedImage, "jpg", new File(destDir + String.valueOf(imgcnt++) + ".jpg"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgcnt;
    }

}
