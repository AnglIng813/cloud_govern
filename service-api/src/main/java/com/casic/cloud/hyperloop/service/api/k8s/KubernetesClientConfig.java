package com.casic.cloud.hyperloop.service.api.k8s;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public abstract class KubernetesClientConfig {

    @Value("${kubernetes.kubernetesUrl}")
    public String kubernetesUrl;
    @Value("${kubernetes.certPath}")
    public String certPath;

    public void setKubernetesUrl(String kubernetesUrl) {
        this.kubernetesUrl = kubernetesUrl;
    }

    public void setCertPath(String certPath) {
        this.certPath = certPath;
    }
}
