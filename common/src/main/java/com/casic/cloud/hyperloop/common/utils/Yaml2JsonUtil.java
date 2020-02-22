package com.casic.cloud.hyperloop.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.casic.cloud.hyperloop.common.exception.CloudApiServerException;
import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class Yaml2JsonUtil {

    public static String json2Yaml(String jsonString) {
        JsonNode jsonNodeTree;
        String jsonAsYaml = "";
        try {
            // parse JSON
            jsonNodeTree = new ObjectMapper().readTree(jsonString);
            // save it as YAML
            jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CloudApiServerException(ApiErrorCode.parsing_yaml_failure);
        }

        return jsonAsYaml;
    }

    public static String yaml2Json(String yamlString) {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> map = (Map<String, Object>) yaml.load(yamlString);
            JSONObject jsonObject = new JSONObject(map);
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CloudApiServerException(ApiErrorCode.parsing_yaml_to_json_failure);
        }
    }

    public static String readYamlFile(String path) {
        try {
            Yaml yaml = new Yaml();
            //注意此处返回的是linkedhashmap
            Map<String, Object> map = yaml.loadAs(new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8")), LinkedHashMap.class);
            return JSONObject.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CloudApiServerException(ApiErrorCode.parsing_yaml_failure);
        }
    }
}
