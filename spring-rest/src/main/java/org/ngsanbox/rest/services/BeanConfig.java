package org.ngsanbox.rest.services;

import org.ngsandbox.common.nao.ContextDao;
import org.ngsandbox.common.nlp.NlpService;
import org.ngsandbox.hazelcast.HazelContextDaoImpl;
import org.ngsandbox.hazelcast.HazelService;
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
