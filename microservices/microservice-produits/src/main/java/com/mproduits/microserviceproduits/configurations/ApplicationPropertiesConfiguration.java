package com.mproduits.microserviceproduits.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mes-configs")
@RefreshScope
public class ApplicationPropertiesConfiguration {
    // correspond à la propriété « mes-configs.limitDeProduits » dans le fichier de configuration du MS
    private int limitDeProduits;
    public int getLimitDeProduits() {
        return limitDeProduits;
    }
    public void setLimitDeProduits(int limitDeProduits) {
        this.limitDeProduits = limitDeProduits;
    }
}
