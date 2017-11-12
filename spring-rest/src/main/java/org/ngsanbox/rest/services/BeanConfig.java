package org.ngsanbox.rest.services;

import org.frameworks.common.nao.ContextDao;
import org.frameworks.common.nlp.NlpService;
import org.frameworks.hazelcast.HazelContextDaoImpl;
import org.frameworks.hazelcast.HazelService;
import org.ngsandbox.nlp.NlpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanConfig {

    @Bean
    public NlpService nlpServiceBean() {
        return new NlpServiceImpl();
    }

    @Bean
    public ContextDao requestsDaoBean() {
        return new HazelContextDaoImpl(new HazelService());
    }
}
