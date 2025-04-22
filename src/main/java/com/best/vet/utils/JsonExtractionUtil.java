package com.best.vet.utils;

import com.best.vet.dto.IngredientInfo;
import com.best.vet.dto.NutrientsDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonExtractionUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static List<String> extractAll100gValues(String json) throws IOException {
        JsonNode root       = MAPPER.readTree(json);
        JsonNode nutriments = root.path("product").path("nutriments");

        List<String> out = new ArrayList<>();

        // Iterate over every field in nutriments
        for (Map.Entry<String, JsonNode> field : iterable(nutriments.fields())) {
            String name = field.getKey();

            // We're only interested in the `*_100g` entries
            if (!name.endsWith("_100g")) continue;

            // Base key is everything before "_100g"
            String base = name.substring(0, name.length() - "_100g".length());

            // Grab the number, the matching _value and _unit
            String amount100g = field.getValue().asText();                  // e.g. "25"
            String value      = nutriments.path(base + "_value").asText();  // e.g. "25"
            String unit       = nutriments.path(base + "_unit").asText();   // e.g. "%"

            // Only add if all three exist
            if (!amount100g.isEmpty() && !value.isEmpty() && !unit.isEmpty()) {
                out.add(String.format(
                        "%s_100g:%s %s",
                        base,
                        value,
                        unit
                ));
            }
        }

        return out;
    }

    // Helper to turn an Iterator into an Iterable for the for‑each
    private static <T> Iterable<T> iterable(final java.util.Iterator<T> i) {
        return () -> i;
    }

    /**
     * Parses the OpenPetFoodFacts JSON and returns a list of IngredientInfo
     * containing text + percent_estimate.
     */
    public static List<IngredientInfo> extractIngredients(String json) throws IOException {
        JsonNode root = MAPPER.readTree(json);
        JsonNode ingredients = root.at("/product/ingredients");
        List<IngredientInfo> list = new ArrayList<>();
        if (ingredients.isArray()) {
            for (JsonNode ing : ingredients) {
                String text = ing.path("text").asText("");
                double pct = ing.path("percent_estimate").asDouble(0);
                list.add(new IngredientInfo(text, pct));
            }
        }
        return list;
    }

    /**
     * Extracts a handful of crude-... nutriments and concatenates as:
     * key_100g:value unit
     */
    public static List<NutrientsDTO> extractCrudeNutriments(String json) throws IOException {
        JsonNode root = MAPPER.readTree(json);
        JsonNode nutriments = root.at("/product/nutriments");

        String[] keys = {
                "crude-protein",
                "crude-fat",
                "crude-fibre",
                "crude-ash"
        };

        List<NutrientsDTO> results = new ArrayList<>();
        for (String key : keys) {
            String qg = nutriments.path(key + "_100g").asText(null);
            String value = nutriments.path(key + "_value").asText(null);
            String unit = nutriments.path(key + "_unit").asText(null);
            if (qg != null && value != null && unit != null) {
                NutrientsDTO n = new NutrientsDTO();
                n.setText(String.format("%s 100g", key));
                n.setValue(Double.parseDouble(String.format(" %s", value)));
                n.setUnit(String.format(" %s", unit));
                results.add(n);
            }
        }
        return results;
    }
}
