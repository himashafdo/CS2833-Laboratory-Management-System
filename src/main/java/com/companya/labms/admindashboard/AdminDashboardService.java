package com.companya.labms.admindashboard;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.companya.labms.auth.UserRepository;
import com.companya.labms.catalog.EquipmentRepository;
import com.companya.labms.catalog.Equipment;
import com.companya.labms.reservation.ReservationRepository;
import com.companya.labms.reservation.Reservation;
import com.companya.labms.ticketing.IssueRepository;
import com.companya.labms.ticketing.Issue;

@Service
public class AdminDashboardService {

    private final AdminDashboardRepository repository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final ReservationRepository reservationRepository;
    private final IssueRepository issueRepository;

    public AdminDashboardService(
            AdminDashboardRepository repository, 
            UserRepository userRepository, 
            EquipmentRepository equipmentRepository,
            ReservationRepository reservationRepository,
            IssueRepository issueRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.reservationRepository = reservationRepository;
        this.issueRepository = issueRepository;
    }

    public List<AdminDashboard> getAllNotices() {
        return repository.findAll();
    }

    public AdminDashboard createNotice(AdminDashboard notice) {
        return repository.save(notice);
    }

    public Map<String, Long> getDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("users", userRepository.count());
        stats.put("equipment", equipmentRepository.count());
        stats.put("notices", repository.count());
        return stats;
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }
}
