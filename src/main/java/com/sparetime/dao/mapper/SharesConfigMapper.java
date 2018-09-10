package com.sparetime.dao.mapper;

import com.sparetime.domian.SharesConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by muye on 17/8/8.
 */
public interface SharesConfigMapper {

    @Insert("insert into Shares_Config (DTFTBL,DTTXBL,DTLYJFBL,JTSXFBL,JTTXBL,JTFTBL,JTSCJFBL,JDJBL,initialPrice,splitPrice," +
            "unitPrice,unitShares,maxBuyPrice,ZXTXJE,MRJG,MCJG,enabledReg,enabledLogin,enabledGetCash,enabledTrade, DPJZFDBS,HKQRSJ,SKQRSJ,TXSXF) values (" +
            "#{sharesConfig.DTFTBL},#{sharesConfig.DTTXBL},#{sharesConfig.DTLYJFBL},#{sharesConfig.JTSXFBL},#{sharesConfig.JTTXBL}," +
            "#{sharesConfig.JTFTBL},#{sharesConfig.JTSCJFBL},#{sharesConfig.JDJBL},#{sharesConfig.initialPrice},#{sharesConfig.splitPrice}," +
            "#{sharesConfig.unitPrice},#{sharesConfig.unitShares},#{sharesConfig.maxBuyPrice},#{sharesConfig.ZXTXJE},#{sharesConfig.MRJG}," +
            "#{sharesConfig.MCJG},#{sharesConfig.enabledReg},#{sharesConfig.enabledLogin},#{sharesConfig.enabledGetCash}," +
            "#{sharesConfig.enabledTrade},#{sharesConfig.DPJZFDBS},#{sharesConfig.HKQRSJ},#{sharesConfig.SKQRSJ},#{sharesConfig.TXSXF})")
    int insert(@Param("sharesConfig") SharesConfig sharesConfig);

    @Delete("delete from Shares_Config")
    int delete();

    @Select("select * from Shares_Config")
    SharesConfig select();
}
