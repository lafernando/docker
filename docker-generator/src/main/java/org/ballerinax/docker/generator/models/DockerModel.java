/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinax.docker.generator.models;

import com.spotify.docker.client.DockerHost;
import org.ballerinax.docker.generator.DockerGenConstants;
import org.ballerinax.docker.generator.exceptions.DockerGenException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


/**
 * Docker annotations model class.
 */
public class DockerModel {
    private String name;
    private String registry;
    private String tag;
    private boolean push;
    private String username;
    private String password;
    private boolean buildImage;
    private String baseImage;
    private Set<Integer> ports;
    private boolean enableDebug;
    private int debugPort;
    private String dockerHost;
    private boolean isService;
    private String balxFileName;
    private String dockerCertPath;
    private Set<CopyFileModel> externalFiles;
    private String commandArg;

    public DockerModel() {
        // Initialize with default values except for image name
        this.tag = "latest";
        this.push = false;
        this.buildImage = true;
        String baseImageVersion = getClass().getPackage().getImplementationVersion();
        this.baseImage = DockerGenConstants.BALLERINA_BASE_IMAGE + ":" + baseImageVersion;
        this.enableDebug = false;
        this.debugPort = 5005;

        String operatingSystem = System.getProperty("os.name").toLowerCase(Locale.getDefault());
        if (operatingSystem.contains("win")) {
            this.setDockerHost(DockerHost.defaultWindowsEndpoint());
        } else {
            this.setDockerHost(DockerHost.defaultUnixEndpoint());
        }
        externalFiles = new HashSet<>();
        commandArg = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public Set<Integer> getPorts() {
        return ports;
    }

    public void setPorts(Set<Integer> ports) {
        this.ports = ports;
    }

    public String getBalxFileName() {
        return balxFileName;
    }

    public void setBalxFileName(String balxFileName) {
        this.balxFileName = balxFileName;
    }

    public boolean isService() {
        return isService;
    }

    public void setService(boolean service) {
        isService = service;
    }

    public boolean isBuildImage() {
        return buildImage;
    }

    public void setBuildImage(boolean buildImage) {
        this.buildImage = buildImage;
    }

    public String getBaseImage() {
        return baseImage;
    }

    public void setBaseImage(String baseImage) {
        this.baseImage = baseImage;
    }

    public boolean isEnableDebug() {
        return enableDebug;
    }

    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }

    public int getDebugPort() {
        return debugPort;
    }

    public void setDebugPort(int debugPort) {
        this.debugPort = debugPort;
    }

    public String getDockerHost() {
        return dockerHost;
    }

    public void setDockerHost(String dockerHost) {
        this.dockerHost = dockerHost.replace("tcp", "https");
    }

    public Set<CopyFileModel> getCopyFiles() {
        return externalFiles;
    }

    public void setCopyFiles(Set<CopyFileModel> externalFiles) throws DockerGenException {
        this.externalFiles = externalFiles;
        for (CopyFileModel externalFile : externalFiles) {
            if (!externalFile.isBallerinaConf()) {
                continue;
            }
    
            if (Files.isDirectory(Paths.get(externalFile.getSource()))) {
                throw new DockerGenException("invalid config file given: " + externalFile.getSource());
            }
            addCommandArg(" --config " + externalFile.getTarget());
        }
    }

    public String getDockerCertPath() {
        return dockerCertPath;
    }

    public void setDockerCertPath(String dockerCertPath) {
        this.dockerCertPath = dockerCertPath;
    }
    
    public String getCommandArg() {
        return commandArg;
    }
    
    public void addCommandArg(String commandArg) {
        this.commandArg += commandArg;
    }
    
    @Override
    public String toString() {
        return "DockerModel{" +
               "name='" + name + '\'' +
               ", registry='" + registry + '\'' +
               ", tag='" + tag + '\'' +
               ", push=" + push +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", buildImage" + "=" + buildImage +
               ", baseImage='" + baseImage + '\'' +
               ", ports=" + ports +
               ", enableDebug=" + enableDebug +
               ", debugPort=" + debugPort +
               ", dockerHost='" + dockerHost + '\'' +
               ", isService=" + isService +
               ", balxFileName='" + balxFileName + '\'' +
               ", dockerCertPath='" + dockerCertPath + '\'' +
               ", externalFiles=" + externalFiles +
               ", commandArg='" + commandArg + '\'' +
               '}';
    }
}
