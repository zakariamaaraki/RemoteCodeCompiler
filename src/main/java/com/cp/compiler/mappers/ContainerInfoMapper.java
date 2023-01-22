package com.cp.compiler.mappers;

import com.cp.compiler.models.containers.ContainerInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ContainerInfo mapper class
 *
 * @author Zakaria Maaraki
 */
public abstract class ContainerInfoMapper {
    
    private ContainerInfoMapper() {}
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    static {
        objectMapper.findAndRegisterModules();
    }
    
    /**
     * Map json String to ContainerInfo object.
     *
     * @param containerInfoJson the container info json
     * @return the container info
     * @throws JsonProcessingException the json processing exception
     */
    public static ContainerInfo toContainerInfo(String containerInfoJson) throws JsonProcessingException {
        return objectMapper.readValue(containerInfoJson, ContainerInfo.class);
    }
}
