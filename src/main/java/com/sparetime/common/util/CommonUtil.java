package com.sparetime.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zdj on 16-12-8.
 */
public class CommonUtil {

    private static final String[] SUFFIX = new String[]{".jpg",".jpeg",".gif",".png",".bmp"};

    public static String getIpAddresses() throws Exception{
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()){
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()){
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address){
                    return ip.getHostAddress();
                }
            }
        }

        throw new RuntimeException("Not found qualified IP");
    }

    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] concatBytes(byte[] first, byte[]... rest){

        int totalLength = first.length;
        for (byte[] array : rest) {
            if(array != null)
                totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            if(array != null ){
                System.arraycopy(array, 0, result, offset, array.length);
                offset += array.length;
            }
        }
        return result;
    }

    public static String fileNameBuilder(MultipartFile file){

        String[] nameConstitute = file.getOriginalFilename().split("\\.");
        String suffix = "." + nameConstitute[nameConstitute.length - 1];
        boolean isImage = false;
        for (String s : SUFFIX){
            if (s.equals(suffix) || s.toUpperCase().equals(suffix)){
                isImage = true;
                break;
            }
        }

        if (!isImage)
            throw new RuntimeException("格式不正确");
        return  NumberBuilder.build() + suffix;
    }

    public static List<String> extractImgSrc(String content){
        List<String> list = new ArrayList<>();
        Matcher imgMatcher = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)").matcher(content);
        while (imgMatcher.find()){
            Matcher srcMatcher = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')").matcher(imgMatcher.group(2));
            if (srcMatcher.find()){
                list.add(srcMatcher.group(3));
            }
        }
        return list;
    }
}
