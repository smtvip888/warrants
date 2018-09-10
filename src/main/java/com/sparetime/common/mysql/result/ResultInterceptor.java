package com.sparetime.common.mysql.result;

import com.sparetime.common.util.DateUtil;
import com.sparetime.domian.BaseDomain;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class ResultInterceptor implements Interceptor{

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        if (result != null && result instanceof ArrayList){
            ((ArrayList) result).forEach(v -> {
                if (v != null && v instanceof BaseDomain){
                    Class clazz = v.getClass();
                    Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
                        field.setAccessible(true);
                        try {
                            Object fv = field.get(v);
                            if (fv != null && fv instanceof Date){
                                ZonedDateTime dateTime = ZonedDateTime.of(LocalDateTime.ofInstant(((Date) fv).toInstant(), ZoneId.of("GMT")), ZoneId.of("GMT"));
                                field.set(v, sdf.parse(dateTime.format(dtf)));
                            }
                        }catch (Exception e){
                            e.getMessage();
                        }
                    });
                }

            });
        }
        return result;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
