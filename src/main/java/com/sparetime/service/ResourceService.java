package com.sparetime.service;

import com.sparetime.common.mysql.page.Page;
import com.sparetime.domian.Authority;
import com.sparetime.domian.Resource;

import java.util.List;

/**
 * Created by muye on 17/7/19.
 */
public interface ResourceService {

    Resource getResourceTree(List<Authority> authList);

    List<Resource> queryListByExample(Resource query, Page page);

    Resource queryByKey(Long id);

    Long addResource(Resource resource, Long[] authIds, Long[] writes);

    void update(Resource resource, Long[] authIds, Long[] writes);

    void delete(Long id);

    Resource queryByLikeUrl(String url);
}
