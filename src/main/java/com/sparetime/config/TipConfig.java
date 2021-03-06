package com.sparetime.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by muye on 17/11/8.
 */
@Component
public class TipConfig extends ConcurrentHashMap<String, String>{

    @PostConstruct
    public void load() throws Exception{
        Resource resource = new ClassPathResource("tip.yml");
        Yaml yaml = new Yaml();
        this.putAll(yaml.loadAs(resource.getInputStream(), HashMap.class));
    }
}
