package com.mycompany.myapp.config;

import java.io.IOException;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DroolConfig {

    private KieServices kieServices = KieServices.Factory.get();

    private KieFileSystem getKieFileSystem() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("Rule.drl"));
        return kieFileSystem;
    }

    @Bean
    public KieContainer getKieContainer() {
        getKieRepository();
        KieBuilder kb = kieServices.newKieBuilder(getKieFileSystem());
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();

        return kieServices.newKieContainer(kieModule.getReleaseId());
    }

    private void getKieRepository() {
        final KieRepository kieRepository = kieServices.getRepository();
        kieRepository.addKieModule(
            new KieModule() {
                public ReleaseId getReleaseId() {
                    return kieRepository.getDefaultReleaseId();
                }
            }
        );
    }

    @Bean
    public KieSession getKieSession() {
        return getKieContainer().newKieSession();
    }
}
