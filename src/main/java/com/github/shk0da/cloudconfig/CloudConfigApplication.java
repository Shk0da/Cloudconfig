package com.github.shk0da.cloudconfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.core.env.Environment;
import org.springframework.web.WebApplicationInitializer;
import com.github.shk0da.cloudconfig.config.ApplicationProperties;
import com.github.shk0da.cloudconfig.config.CloudConfigConstants;
import com.github.shk0da.cloudconfig.config.ConfigServerConfig;
import com.github.shk0da.cloudconfig.config.DefaultProfileUtil;

import javax.annotation.PostConstruct;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

@EnableZuulProxy
@EnableEurekaServer
@EnableConfigServer
@SpringCloudApplication
@EnableConfigurationProperties({ApplicationProperties.class, ConfigServerConfig.class})
public class CloudConfigApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    private static final Logger log = LoggerFactory.getLogger(CloudConfigApplication.class);

    private final Environment env;

    public CloudConfigApplication(Environment env) {
        this.env = env;
    }

    /**
     * Initializes CloudConfigApplication.
     * <p>
     * Spring profiles can be configured with a program arguments --spring.profiles.active=your-active-profile
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (activeProfiles.contains(CloudConfigConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(CloudConfigConstants.SPRING_PROFILE_PRODUCTION)) {
            log.error("You have misconfigured your application! It should not run " +
                    "with both the 'dev' and 'prod' profiles at the same time.");
        }
        if (activeProfiles.contains(CloudConfigConstants.SPRING_PROFILE_DEVELOPMENT) && activeProfiles.contains(CloudConfigConstants.SPRING_PROFILE_CLOUD)) {
            log.error("You have misconfigured your application! It should not" +
                    "run with both the 'dev' and 'cloud' profiles at the same time.");
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException, ConnectException if the local host name could not be resolved into an address
     * @throws ConnectException if we has problem with connection
     */
    public static void main(String[] args) throws UnknownHostException, ConnectException {
        SpringApplication app = new SpringApplication(CloudConfigApplication.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();

        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}\n\t" +
                        "External: \t{}://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                hostAddress,
                env.getProperty("server.port"),
                env.getActiveProfiles());

        String secretKey = env.getProperty("cloudconfig.security.authentication.jwt.secret");
        if (secretKey == null) {
            log.error("\n----------------------------------------------------------\n" +
                    "Your JWT secret key is not set up, you will not be able to log into the Cloud Config.\n" +
                    "----------------------------------------------------------");
        } else if ("this-secret-should-not-be-used-read-the-comment".equals(secretKey)) {
            log.error("\n----------------------------------------------------------\n" +
                    "Your JWT secret key is not configured using Spring Cloud Config, you will not be able to \n" +
                    "use the Cloud Config dashboards to monitor external applications. \n" +
                    "----------------------------------------------------------");
        }
        String configServerStatus = env.getProperty("configserver.status");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Config Server: \t{}\n----------------------------------------------------------",
                configServerStatus == null ? "Not found or not setup for this application" : configServerStatus);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CloudConfigApplication.class);
    }
}
