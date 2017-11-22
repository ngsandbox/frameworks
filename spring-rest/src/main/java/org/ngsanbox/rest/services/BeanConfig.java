package org.ngsanbox.rest.services;

import org.ngsandbox.common.face.FaceService;
import org.ngsandbox.common.nao.ContextDao;
import org.ngsandbox.common.nlp.NlpService;
import org.ngsandbox.face.RemoteFaceServiceImpl;
import org.ngsandbox.hazelcast.HazelContextDaoImpl;
import org.ngsandbox.hazelcast.HazelService;
import org.ngsandbox.nlp.NlpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanConfig {

    private static final String HOST_NAME = "http://93.88.76.57:8090";

    @Bean
    public NlpService nlpServiceBean() {
        return new NlpServiceImpl();
    }

    @Bean
    public FaceService findPhotoBean() {
        return new RemoteFaceServiceImpl(HOST_NAME);
    }

    @Bean
    public ContextDao requestsDaoBean() {
        return new HazelContextDaoImpl(new HazelService());
    }
}
