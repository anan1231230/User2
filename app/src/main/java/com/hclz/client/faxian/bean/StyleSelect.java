package com.hclz.client.faxian.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hjm on 16/9/1.
 */

public class StyleSelect {

    private static StyleSelect styleSelect = new StyleSelect();
    private Map<String, Map<String, Boolean>> mapSelect;

    private StyleSelect() {
    }

    public static StyleSelect getInstence() {
        return styleSelect;
    }

    public void initMap(Map<String, List<String>> map) {
        mapSelect = new HashMap<>();
        for (String key : map.keySet()) {
            Map<String, Boolean> mapBoolean = new HashMap<>();
            mapBoolean.put("全部", true);
            for (String type : map.get(key)) {
                mapBoolean.put(type, false);
            }
            mapSelect.put(key, mapBoolean);
        }
    }

    public void setTrueMap(String key, String type) {
        for (String key2 : mapSelect.get(key).keySet()) {
            mapSelect.get(key).put(key2, false);
        }
        mapSelect.get(key).put(type, true);
    }

    public boolean isSelect(String key, String type) {
        return mapSelect.get(key).get(type);
    }

    public Map<String, String> getSelectScreen() {
        Map<String, String> map = new HashMap<>();
        for (String key : mapSelect.keySet()) {
            for (String key2 : mapSelect.get(key).keySet()) {
                if (mapSelect.get(key).get(key2) == true && !"全部".equals(key2)) {
                    map.put(key, key2);
                    break;
                }
            }
        }
        return map;
    }
}
