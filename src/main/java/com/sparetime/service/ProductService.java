package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by muye on 17/7/25.
 */
public interface ProductService {

    List<Product> queryAll();

    List<Product> queryListByExample(Product product, Page page);

    int insert(Product product);

    int update(Product product);

    Product queryByKey(BigDecimal id);

    int delete(BigDecimal productId);
}
