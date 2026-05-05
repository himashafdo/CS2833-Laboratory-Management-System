package com.companya.labms.request;

import com.companya.labms.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EquipmentRequestRepository extends JpaRepository<EquipmentRequest, Long> {

    List<EquipmentRequest> findByUserOrderByCreatedAtDesc(User user);

    List<EquipmentRequest> findAllByOrderByCreatedAtDesc();

    long countByStatus(EquipmentRequest.RequestStatus status);

    @Query("SELECT r.status as status, COUNT(r) as count FROM EquipmentRequest r GROUP BY r.status")
    List<Object[]> countGroupByStatus();

    @Query("SELECT FUNCTION('MONTH', r.createdAt) as month, COUNT(r) as count " +
           "FROM EquipmentRequest r " +
           "WHERE FUNCTION('YEAR', r.createdAt) = FUNCTION('YEAR', CURRENT_DATE) " +
           "GROUP BY FUNCTION('MONTH', r.createdAt) " +
           "ORDER BY FUNCTION('MONTH', r.createdAt)")
    List<Object[]> countByMonth();
}
