package com.companya.labms.ml;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class MLSettingsService {

    private final MLSettingsRepository repository;

    public MLSettingsService(MLSettingsRepository repository) {
        this.repository = repository;
    }

    // Seed default ML features on startup if they don't exist
    @PostConstruct
    public void seedDefaults() {
        seedIfMissing("demand_forecast",
            "Predict busy equipment and time slots from reservation history.");
        seedIfMissing("failure_prediction",
            "Flag equipment at risk of failure based on issue report patterns.");
        seedIfMissing("recommendation",
            "Suggest related equipment when a student books a reservation.");
    }

    private void seedIfMissing(String name, String description) {
        repository.findByFeatureName(name).orElseGet(() -> {
            MLSettings s = new MLSettings(name, false, description);
            return repository.save(s);
        });
    }

    public List<MLSettings> getAllSettings() {
        return repository.findAll();
    }

    public MLSettings toggle(String featureName, String updatedBy) {
        MLSettings settings = repository.findByFeatureName(featureName)
            .orElseThrow(() -> new RuntimeException("ML feature not found: " + featureName));
        settings.setEnabled(!settings.isEnabled());
        settings.setUpdatedBy(updatedBy);
        return repository.save(settings);
    }

    public MLSettings setEnabled(String featureName, boolean enabled, String updatedBy) {
        MLSettings settings = repository.findByFeatureName(featureName)
            .orElseThrow(() -> new RuntimeException("ML feature not found: " + featureName));
        settings.setEnabled(enabled);
        settings.setUpdatedBy(updatedBy);
        return repository.save(settings);
    }

    public boolean isEnabled(String featureName) {
        return repository.findByFeatureName(featureName)
            .map(MLSettings::isEnabled)
            .orElse(false);
    }

    public Map<String, Boolean> getAllEnabledMap() {
        List<MLSettings> all = repository.findAll();
        Map<String, Boolean> map = new java.util.HashMap<>();
        for (MLSettings s : all) {
            map.put(s.getFeatureName(), s.isEnabled());
        }
        return map;
    }
}
