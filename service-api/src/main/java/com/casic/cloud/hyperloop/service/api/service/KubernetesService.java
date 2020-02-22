package com.casic.cloud.hyperloop.service.api.service;

import com.casic.cloud.hyperloop.service.api.k8s.KubernetesClientUtils;
import io.fabric8.kubernetes.api.model.PodList;

public interface KubernetesService {
    /**
     * 查询pod
     */
    PodList getPodList();

    public static void main(String[] args) {
        PodList list = KubernetesClientUtils.getK8sClientWithoutCert().pods().inNamespace("admin").list();

    }
}
