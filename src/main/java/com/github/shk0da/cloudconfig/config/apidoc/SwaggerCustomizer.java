package com.github.shk0da.cloudconfig.config.apidoc;

import org.springframework.core.Ordered;
import org.springframework.http.ResponseEntity;
import com.github.shk0da.cloudconfig.config.CloudConfigProperties;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class SwaggerCustomizer implements Ordered {

    private int order = 0;
    private final CloudConfigProperties.Swagger properties;

    public SwaggerCustomizer(CloudConfigProperties.Swagger properties) {
        this.properties = properties;
    }

    public void customize(Docket docket) {
        Contact contact = new Contact(
                this.properties.getContactName(),
                this.properties.getContactUrl(),
                this.properties.getContactEmail()
        );
        ApiInfo apiInfo = new ApiInfo(
                this.properties.getTitle(),
                this.properties.getDescription(),
                this.properties.getVersion(),
                this.properties.getTermsOfServiceUrl(),
                contact,
                this.properties.getLicense(),
                this.properties.getLicenseUrl(),
                new ArrayList()
        );
        docket.host(this.properties.getHost())
                .protocols(new HashSet(Arrays.asList(this.properties.getProtocols())))
                .apiInfo(apiInfo)
                .useDefaultResponseMessages(this.properties.isUseDefaultResponseMessages())
                .forCodeGeneration(true)
                .directModelSubstitute(ByteBuffer.class, String.class)
                .genericModelSubstitutes(new Class[]{ResponseEntity.class})
                .select()
                .paths(PathSelectors.regex(this.properties.getDefaultIncludePattern()))
                .build();
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }
}
