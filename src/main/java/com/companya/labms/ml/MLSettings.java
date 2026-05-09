package com.companya.labms.ml;

import com.companya.labms.shared.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "ml_settings")
public class MLSettings extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String featureName;

    @Column(nullable = false)
    private boolean enabled = false;

    private String description;
    private String updatedBy;

    public MLSettings() {}

    public MLSettings(String featureName, boolean enabled, String description) {
        this.featureName = featureName;
        this.enabled = enabled;
        this.description = description;
    }

    public String getFeatureName() { return featureName; }
    public void setFeatureName(String featureName) { this.featureName = featureName; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(String updatedBy) { this.updatedBy = updatedBy; }
}
