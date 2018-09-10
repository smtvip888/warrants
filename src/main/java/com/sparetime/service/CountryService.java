package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Country;

import java.math.BigDecimal;
import java.util.List;

public interface CountryService {

    List<Country> queryListByExample(Country query, Page page);

    Country queryByKey(BigDecimal countryId);

    int insert(Country country);

    int update(Country country);

    int delete(Long id);
}
