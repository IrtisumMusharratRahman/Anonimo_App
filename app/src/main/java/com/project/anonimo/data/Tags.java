package com.project.anonimo.data;

import java.util.ArrayList;
import java.util.List;

public class Tags {
    private static Tags instance;
    private List<String> tags;

    private Tags() {
        tags.add("art");
        tags.add("business");
        tags.add("culture");
        tags.add("education");
        tags.add("entertainment");
        tags.add("fashion");
        tags.add("food");
        tags.add("health");
        tags.add("music");
        tags.add("news");
        tags.add("politics");
        tags.add("sports");
        tags.add("technology");
        tags.add("travel");
        tags.add("weather");
    }

    public static Tags getInstance() {
        if (instance == null) {
            instance = new Tags();
        }
        return instance;
    }

    public List<String> getMyList() {
        return tags;
    }
}
