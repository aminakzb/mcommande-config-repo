package com.devoir.mcommandes.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mes-config-ms")
@RefreshScope
public class ApplicationPropertiesConfiguration {
    private int commandesLast;
    public int getCommandesLast() {
        return commandesLast;
    }
    public void setCommandesLast(int commandesLast) {
        this.commandesLast = commandesLast;
    }

}
