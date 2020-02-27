package com.casic.cloud.hyperloop.service.api.k8s;

import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * @Description: k8sclient 待完善 改为sigleton
 * @Author: LDC
 * @Date: 2020/01/19 09:13
 * @version: V1.0
 */
public class KubernetesClientUtils extends KubernetesClientConfig{

    public KubernetesClient getK8sClientWithCert() {
        String caCertFile = Thread.currentThread().getContextClassLoader().getResource("").getPath() + certPath + "/kubernetes.pem";
        String clientCertFile = Thread.currentThread().getContextClassLoader().getResource("").getPath() + certPath + "/client.pem";
        String clientKeyFile = Thread.currentThread().getContextClassLoader().getResource("").getPath() + certPath + "/client.key.pem";
        Config config = new ConfigBuilder().withMasterUrl(super.kubernetesUrl)
                .withTrustCerts(true)
                .withCaCertData(enCodeWithBase64(caCertFile))
                .withClientCertData(enCodeWithBase64(clientCertFile))
                .withClientKeyData(enCodeWithBase64(clientKeyFile))
                /**
                 * Fabric8 在初始化的时候会默认从环境变量读取当前namespace
                 * 需将namesapce初始化为null
                 */
                .withNamespace(null)
                .build();
        return new DefaultKubernetesClient(config);
    }

    public KubernetesClient getK8sClientWithCert(String caCertFileURI, String clientCertFileURI, String clientKeyFileURI) {
        String caCertFile = Thread.currentThread().getContextClassLoader().getResource("").getPath() + certPath + caCertFileURI;
        String clientCertFile = Thread.currentThread().getContextClassLoader().getResource("").getPath() + certPath + clientCertFileURI;
        String clientKeyFile = Thread.currentThread().getContextClassLoader().getResource("").getPath() + certPath + clientKeyFileURI;
        Config config = new ConfigBuilder().withMasterUrl(super.kubernetesUrl)
                .withTrustCerts(true)
                .withCaCertData(enCodeWithBase64(caCertFile))
                .withClientCertData(enCodeWithBase64(clientCertFile))
                .withClientKeyData(enCodeWithBase64(clientKeyFile))
                /**
                 * Fabric8 在初始化的时候会默认从环境变量读取当前namespace
                 * 需将namesapce初始化为null
                 */
                .withNamespace(null)
                .build();
        return new DefaultKubernetesClient(config);
    }

    public KubernetesClient getK8sClientWithoutCert() {
        Config config = new ConfigBuilder().withMasterUrl(super.kubernetesUrl).build();
        return new DefaultKubernetesClient(config);
    }

    /**
     * Fabric8会先将证书通过标准Base64解密，
     * 如果证书未经性加密，会抛异常
     * 故此处先进行加密处理
     *
     * @param path
     * @return
     */
    private String enCodeWithBase64(String path) {
        String ret = "";
        try {
            String s = IOUtils.toString(new FileInputStream(new File(path)));
            ret = Base64.getEncoder().encodeToString(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
