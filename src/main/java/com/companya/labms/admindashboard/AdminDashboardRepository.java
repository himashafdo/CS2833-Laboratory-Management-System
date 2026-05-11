package com.companya.labms.admindashboard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDashboardRepository extends JpaRepository<AdminDashboard, Long> {
}
