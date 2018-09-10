package com.sparetime.common.util;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * Created by muye on 17/10/12.
 */
public class FileUtil {

    public static List<String> upload(MultipartFile[] files, String dirPath, int serverPort) throws Exception{

//        File dir = new File(dirPath);
//        if (!dir.exists())
//            dir.mkdir();
//
//        List<String> paths = new ArrayList<>();
//
//        String serverPath = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + serverPort;
//
//        Arrays.asList(files).forEach(file -> {
//            String fileName = CommonUtil.fileNameBuilder(file);
//            String path = Paths.get(dirPath, fileName).toString();
//            BufferedOutputStream stream = null;
//            try {
//                stream = new BufferedOutputStream(new FileOutputStream(new File(path)));
//                stream.write(file.getBytes());
//                paths.add(serverPath + "/file/" + fileName);
//            }catch (Exception e){
//                throw new RuntimeException(e);
//            }finally {
//                if (stream != null)
//                    try {
//                        stream.close();
//                    }catch (Exception e){
//                    }
//            }
//        });
//
//        return paths;


        List<String> paths = new ArrayList<>();
        Arrays.asList(files).forEach(f -> {
            try {
                String suff = f.getOriginalFilename().split("\\.")[f.getOriginalFilename().split("\\.").length - 1];
                BufferedImage image = Thumbnails.of(f.getInputStream()).scale(1f).outputQuality(0.25f).asBufferedImage();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(image, suff, out);
                String stream = Base64.getEncoder().encodeToString(out.toByteArray());
                paths.add(stream);
            }catch (Exception e){
                throw new RuntimeException("上传图片失败");
            }
        });
        return paths;

    }
}
