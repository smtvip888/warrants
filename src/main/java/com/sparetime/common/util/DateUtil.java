package com.sparetime.common.util;

import com.sparetime.domian.BaseDomain;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by muye on 17/8/17.
 */
public class DateUtil {

    public static LocalDateTime dateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    public static void timeHandle(BaseDomain baseDomain, String daterange){
        try {
            if (StringUtils.isEmpty(daterange))
                return;
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date startTime = sdf.parse(daterange.split("-")[0].trim() + " 00:00:00");
            Date endTime = sdf.parse(daterange.split("-")[1].trim() + " 23:59:59");
            baseDomain.setStartTime(startTime);
            baseDomain.setEndTime(endTime);
        }catch (Exception e){
        }
    }

    public static String[] dateToStr(int days, String format){
        LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0, 0));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String[] dates = new String[days];
        for (int i = 0; i < days; days --){
            dates[dates.length - days] = dateTime.plusDays(-days + 1).format(formatter);
        }
        return dates;
    }
}
