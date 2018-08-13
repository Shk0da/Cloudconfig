package com.github.shk0da.cloudconfig.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.shk0da.cloudconfig.config.ConfigServerConfig;
import com.github.shk0da.cloudconfig.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/config")
public class ConfigResource {

    private final Logger log = LoggerFactory.getLogger(ConfigResource.class);

    private final ConfigServerConfig configServerConfig;

    public ConfigResource(ConfigServerConfig configServerConfig) {
        this.configServerConfig = configServerConfig;
    }

    @Timed
    @GetMapping("available")
    public Map<String, File> getAvailableConfigList() {
        Map<String, File> applicationAndProfileToFile = Maps.newHashMap();
        configServerConfig.getComposite()
                .parallelStream()
                .filter(prop -> prop.containsKey("type") && "native".equals(prop.get("type")))
                .map(prop -> prop.get("search-locations").toString().replaceFirst("file:", ""))
                .map(File::new)
                .filter(File::isDirectory)
                .map(dir -> FileUtil.extractFilesFromFolder(dir.getPath()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .forEach(file -> applicationAndProfileToFile.put(FilenameUtils.removeExtension(file.getName()), file));

        return applicationAndProfileToFile;
    }

    @Timed
    @GetMapping(value = "content", params = {"application", "profile"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getConfigContent(String application, String profile) {
        final String configKey = String.format("%s-%s", application, profile);
        final Map<String, File> applicationAndProfileToFile = getAvailableConfigList();
        if (!applicationAndProfileToFile.containsKey(configKey)) {
            return new ResponseEntity<>("Not found this application name or profile!", HttpStatus.NOT_FOUND);
        }

        byte[] content;
        try {
            content = Files.readAllBytes(applicationAndProfileToFile.get(configKey).toPath());
        } catch (IOException e) {
            log.error("Failed get content from {}", applicationAndProfileToFile.get(configKey));
            return new ResponseEntity<>("Failed get content", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new String(content), HttpStatus.OK);
    }

    @Timed
    @PostMapping(value = "content", params = {"application", "profile"})
    public ResponseEntity saveConfigContent(String application, String profile, @RequestBody String content) {
        final String configKey = String.format("%s-%s", application, profile);
        final Map<String, File> applicationAndProfileToFile = getAvailableConfigList();

        File configFile;
        if (!applicationAndProfileToFile.containsKey(configKey)) {
            File configFolder = configServerConfig.getComposite()
                    .stream()
                    .filter(prop -> prop.containsKey("type") && "native".equals(prop.get("type")))
                    .map(prop -> prop.get("search-locations").toString().replaceFirst("file:", ""))
                    .findFirst()
                    .map(File::new)
                    // default spring cloud path
                    .orElse(new File("./config/"));
            FileUtil.checkDirAndMake(configFolder.getPath());
            // default ext for config .properties
            configFile = new File(configFolder.getPath() + "/" + configKey + ".properties");
        } else {
            configFile = applicationAndProfileToFile.get(configKey);
        }

        try {
            if (!configFile.exists() || (configFile.exists() && configFile.delete())) {
                Files.write(
                        configFile.toPath(),
                        content.getBytes(),
                        StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE
                );
            }
        } catch (IOException e) {
            log.error("Failed write file {}, because {}", configFile, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
