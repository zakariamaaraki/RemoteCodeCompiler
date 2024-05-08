package com.cp.compiler;

import com.cp.compiler.api.controllers.CompilerController;
import com.cp.compiler.api.controllers.ContainersInfoController;
import com.cp.compiler.api.controllers.ProblemsController;
import com.cp.compiler.repositories.hooks.HooksRepository;
import com.cp.compiler.services.api.CompilerFacade;
import com.cp.compiler.services.businesslogic.CompilerService;
import com.cp.compiler.services.platform.containers.ContainerService;
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
    private ProblemsController indexController;
    
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
