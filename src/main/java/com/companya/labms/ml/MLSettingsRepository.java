package com.companya.labms.ml;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MLSettingsRepository extends JpaRepository<MLSettings, Long> {
    Optional<MLSettings> findByFeatureName(String featureName);
}
