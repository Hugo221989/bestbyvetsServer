package com.best.vet.service;

import com.best.vet.dto.AnalysisDTO;
import com.best.vet.dto.IngredientInfo;
import com.best.vet.dto.NutrientsDTO;
import com.best.vet.utils.JsonExtractionUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class ProductAnalysisService {
    private final WebClient webClient;

    public ProductAnalysisService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://world.openpetfoodfacts.org")
                .build();
    }

    public Mono<AnalysisDTO> analyzeByBarcode(String barcode) {
        return webClient.get()
                .uri("/api/v2/product/{code}.json", barcode)
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(json -> {
                    try {
                        List<IngredientInfo> ings = JsonExtractionUtil.extractIngredients(json);
                        List<NutrientsDTO> nutriments = JsonExtractionUtil.extractCrudeNutriments(json);
                        AnalysisDTO analysisDTO = new AnalysisDTO();
                        analysisDTO.setIngredients(ings);
                        analysisDTO.setNutriments(nutriments);
                        return Mono.just(analysisDTO);
//                        ings.forEach(System.out::println);
//                        nutriments.forEach(System.out::println);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Mono.empty();
                });
    }

    private AnalysisDTO computeAnalysis(Map<String,Object> root) {
        Map<String, Object> product = (Map<String,Object>)root.get("product");
        Map<String, Object> ingredients = (Map<String,Object>)product.get("ingredients");
        Map<String, Object> nutriments = (Map<String,Object>)product.get("nutriments");

        // extract values safely
        double carbs = toDouble(nutriments.get("carbohydrates_100g"));
        double sugars = toDouble(nutriments.get("sugars_100g"));
        double fat = toDouble(nutriments.get("fat_100g"));
        double satFat = toDouble(nutriments.get("saturated-fat_100g"));
        double protein = toDouble(nutriments.get("proteins_100g"));
        double fiber = toDouble(nutriments.get("fiber_100g"));
        double salt = toDouble(nutriments.get("salt_100g"));

        // build NutrientsDTO
        NutrientsDTO n = new NutrientsDTO();
//        n.setCarbohydrates(carbs);
//        n.setSugars(sugars);
//        n.setFat(fat);
//        n.setSaturatedFat(satFat);
//        n.setProtein(protein);
//        n.setFiber(fiber);
//        n.setSalt(salt);

        // compute health score (from 0–100)
        double score = 100;
        if (carbs > 40)    score -= (carbs - 40) * 0.5;
        if (sugars > 5)    score -= (sugars - 5) * 1;
        if (fat > 3)       score -= (fat - 3) * 1;
        if (protein < 8)   score -= (8 - protein) * 2;
        if (fiber < 3)     score -= (3 - fiber) * 2;
        if (salt > 0.9)    score -= (salt - 0.9) * 10;
        score = Math.max(0, Math.min(100, score));

        String label;
        if (score >= 80)      label = "Very healthy";
        else if (score >= 60) label = "Healthy";
        else if (score >= 40) label = "Moderately healthy";
        else                  label = "Unhealthy";

        AnalysisDTO analysis = new AnalysisDTO();
//        analysis.setNutrients(n);
//        analysis.setHealthScore(Math.round(score * 100.0)/100.0);  // two decimals
//        analysis.setHealthLabel(label);
        return analysis;
    }

    private double toDouble(Object o) {
        if (o instanceof Number) return ((Number)o).doubleValue();
        try { return Double.parseDouble(o.toString()); }
        catch (Exception e) { return 0; }
    }
}
