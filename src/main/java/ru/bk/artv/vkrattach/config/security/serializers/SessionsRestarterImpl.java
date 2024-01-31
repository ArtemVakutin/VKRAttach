package ru.bk.artv.vkrattach.config.security.serializers;

import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.Map;

@Component
@Slf4j
public class SessionsRestarterImpl implements SessionsRestarter {

    private String filePath = getClass().getClassLoader()
            .getResource("application-keys.yml")
            .getPath();
    private String groupName = "jwt";

    private String accessKeyValueName = "access-token-key";
    private String refreshKeyValueName = "refresh-token-key";

    public SessionsRestarterImpl() {
    }

    public SessionsRestarterImpl(String filePath) {
        this.filePath = filePath;
    }

    public SessionsRestarterImpl(String filePath, String groupName, String accessKeyValueName, String refreshKeyValueName) {
        this.filePath = filePath;
        this.groupName = groupName;
        this.accessKeyValueName = accessKeyValueName;
        this.refreshKeyValueName = refreshKeyValueName;
    }

    public void closeAllSessions() throws Exception {
        writeNewTokenKeys();
    }

    private void writeNewTokenKeys() throws Exception {
        OctetSequenceKey accessTokenKey = new OctetSequenceKeyGenerator(256).generate();
        OctetSequenceKey refreshTokenKey = new OctetSequenceKeyGenerator(128).generate();
//        String accessTokenKeyString = "'" + accessTokenKey.toJSONString() +"'";
//        String refreshTokenKeyString = "'" + refreshTokenKey.toJSONString() +"'";
        log.info(filePath);
        try (InputStream input = new FileInputStream(filePath)) {


            Yaml yaml = new Yaml();
            Map<String, Object> yamlData = yaml.load(input);
//            yamlData.entrySet().forEach(stringStringEntry -> log.info(stringStringEntry.getKey() + ":" + stringStringEntry.getValue()));
            Map<String, Object> jwt = (Map<String, Object>) yamlData.get(groupName);
            jwt.put(accessKeyValueName, accessTokenKey.toJSONString());
            jwt.put(refreshKeyValueName, refreshTokenKey.toJSONString());

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml updatedYaml = new Yaml(options);

            try(Writer writer = new FileWriter(filePath)) {
                updatedYaml.dump(yamlData, writer);
            }
        }
    }


}
