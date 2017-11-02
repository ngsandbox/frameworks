package org.ngsanbox.rest.services;

import org.frameworks.common.nao.RequestsDao;
import org.frameworks.hazelcast.HazelRequestsDaoImpl;
import org.frameworks.hazelcast.HazelService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanConfig {

    @Bean
    public RequestsDao requestsDaoBean() {
        return new HazelRequestsDaoImpl(new HazelService());
    }
}
