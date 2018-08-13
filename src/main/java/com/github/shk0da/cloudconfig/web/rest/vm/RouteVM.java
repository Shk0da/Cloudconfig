package com.github.shk0da.cloudconfig.web.rest.vm;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * View Model that stores a route managed by the Registry.
 */
public class RouteVM {

    private String path;

    private String prefix;

    private String serviceId;

    private String appName;

    private String status;

    private String host;

    private Integer port;

    private List<ServiceInstance> serviceInstances;

    public RouteVM() {
    }

    public RouteVM(String path, String prefix, String serviceId, String appName, String status, List<ServiceInstance> serviceInstances) {
        this.path = path;
        this.prefix = prefix;
        this.serviceId = serviceId;
        this.appName = appName;
        this.status = status;
        this.serviceInstances = serviceInstances;
    }

    public RouteVM(String path, String prefix, String serviceId, String appName, String status, String host, Integer port, List<ServiceInstance> serviceInstances) {
        this.path = path;
        this.prefix = prefix;
        this.serviceId = serviceId;
        this.appName = appName;
        this.status = status;
        this.host = host;
        this.port = port;
        this.serviceInstances = serviceInstances;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<ServiceInstance> getServiceInstances() {
        return serviceInstances;
    }

    public void setServiceInstances(List<ServiceInstance> serviceInstances) {
        this.serviceInstances = serviceInstances;
    }

    @Override
    public String toString() {
        return "RouteVM{" +
                "path='" + path + '\'' +
                ", prefix='" + prefix + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", appName='" + appName + '\'' +
                ", status='" + status + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", serviceInstances=" + serviceInstances +
                '}';
    }
}
