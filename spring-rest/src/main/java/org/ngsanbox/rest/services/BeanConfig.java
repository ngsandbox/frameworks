package org.ngsanbox.rest.services;

import org.frameworks.common.nao.ContextDao;
import org.frameworks.hazelcast.HazelContextDaoImpl;
import org.frameworks.hazelcast.HazelService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanConfig {

    @Bean
    public ContextDao requestsDaoBean() {
        return new HazelContextDaoImpl(new HazelService());
    }
}
