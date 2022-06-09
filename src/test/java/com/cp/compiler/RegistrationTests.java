package com.cp.compiler;

import com.cp.compiler.controllers.CompilerController;
import com.cp.compiler.controllers.ContainersInfoController;
import com.cp.compiler.controllers.IndexController;
import com.cp.compiler.repositories.HooksRepository;
import com.cp.compiler.services.CompilerFacade;
import com.cp.compiler.services.CompilerService;
import com.cp.compiler.services.ContainerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RegistrationTests {
    
    @Autowired
    private CompilerFacade compilerFacade;
    
    @Autowired
    @Qualifier("client")
    private CompilerService compilerServiceClient;
    
    @Autowired
    @Qualifier("longRunning")
    private CompilerService compilerServiceLongRunning;
    
    @Autowired
    @Qualifier("proxy")
    private CompilerService compilerServiceProxy;
    
    @Autowired
    private HooksRepository hooksRepository;
    
    @Autowired
    private ContainerService containerService;
    
    @Autowired
    private IndexController indexController;
    
    @Autowired
    private ContainersInfoController containersInfoController;
    
    @Autowired
    private CompilerController compilerController;
    
    @Test
    void allComponentsShouldBeRegistered() {
        Assertions.assertNotNull(compilerFacade);
        Assertions.assertNotNull(compilerServiceClient);
        Assertions.assertNotNull(compilerServiceLongRunning);
        Assertions.assertNotNull(compilerServiceProxy);
        Assertions.assertNotNull(hooksRepository);
        Assertions.assertNotNull(containerService);
        Assertions.assertNotNull(indexController);
        Assertions.assertNotNull(containersInfoController);
        Assertions.assertNotNull(compilerController);
    }
}
