package com.github.shk0da.cloudconfig.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.github.shk0da.cloudconfig.web.rest.vm.RouteVM;
import com.netflix.appinfo.InstanceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.shk0da.cloudconfig.service.dto.ZuulRouteDTO;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RoutesResource {

    private final Logger log = LoggerFactory.getLogger(RoutesResource.class);

    @Value("${spring.application.name}")
    private String registryName;

    private final RouteLocator routeLocator;

    private final DiscoveryClient discoveryClient;

    private ZuulProperties zuulProperties;

    public RoutesResource(RouteLocator routeLocator, DiscoveryClient discoveryClient, ZuulProperties zuulProperties) {
        this.routeLocator = routeLocator;
        this.discoveryClient = discoveryClient;
        this.zuulProperties = zuulProperties;
    }

    @Timed
    @GetMapping("/routes")
    public ResponseEntity<List<RouteVM>> getRoutes() {
        List<Route> routes = routeLocator.getRoutes();
        Map<String, RouteVM> routeVMs = new HashMap<>();
        routeVMs.put(null, registryRoute());

        routes.forEach(route -> {
            RouteVM routeVM = new RouteVM();
            routeVM.setPath(route.getFullPath());
            routeVM.setPrefix(route.getPrefix());
            routeVM.setServiceId(route.getId());
            routeVM.setAppName(extractName(route.getId()));

            try {
                URL appUrl = new URL(route.getLocation());
                routeVM.setHost(appUrl.getHost());
                routeVM.setPort(appUrl.getPort());
            } catch (MalformedURLException e) {
                log.error("Failed route parse: {}", e.getMessage());
            }

            routeVM.setServiceInstances(discoveryClient.getInstances(route.getId()));
            routeVMs.put(route.getId(), routeVM);
        });

        fillStatus(routeVMs);

        return new ResponseEntity<>(new ArrayList<>(routeVMs.values()), HttpStatus.OK);
    }

    /**
     * Fill all Routes with each instance status.
     */
    private void fillStatus(Map<String, RouteVM> routeVMs) {
        if (routeVMs != null && !routeVMs.isEmpty()) {
            zuulProperties.getRoutes().values().forEach(oneRoute -> {
                if (oneRoute instanceof ZuulRouteDTO) {
                    routeVMs.get(oneRoute.getId()).setStatus(((ZuulRouteDTO) oneRoute).getStatus());
                }
            });
        }
    }

    /**
     * Return the registry routeVM
     */
    private RouteVM registryRoute() {
        return new RouteVM("/**", null, null, registryName, InstanceInfo.InstanceStatus.UP.toString(), null);
    }

    private String extractName(String id) {
        if (id != null && id.contains(":")) {
            return id.split(":")[1];
        }
        return id;
    }
}
