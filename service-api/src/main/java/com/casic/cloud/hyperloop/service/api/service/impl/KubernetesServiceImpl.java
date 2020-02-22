package com.casic.cloud.hyperloop.service.api.service.impl;

import com.casic.cloud.hyperloop.service.api.k8s.KubernetesClientUtils;
import com.casic.cloud.hyperloop.service.api.service.KubernetesService;
import io.fabric8.kubernetes.api.model.PodList;

/**
 * @Description:
 * @Author: LDC
 * @Date: 2020/01/19 16:35
 * @version: V1.0
 */
public class KubernetesServiceImpl implements KubernetesService {

    @Override
    public PodList getPodList() {
        KubernetesClientUtils client = new KubernetesClientUtils();
        return  client.getK8sClientWithoutCert().pods().inNamespace("admin").list();
    }
}
