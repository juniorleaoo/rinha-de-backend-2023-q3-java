package com.rinhabackend;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CacheConfig {

    public static String KEY_APELIDOS = "apelidos";
    public static String KEY_PESSOAS = "pessoas";

    @Bean
    public Map<String, Boolean> apelidosMap(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(KEY_APELIDOS);
    }

    @Bean
    public Map<String, Pessoa> pessoasMap(HazelcastInstance hazelcastInstance) {
        return hazelcastInstance.getMap(KEY_PESSOAS);
    }

}
