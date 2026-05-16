package com.companya.labms.request;

import com.companya.labms.auth.User;
import com.companya.labms.auth.UserRepository;
import com.companya.labms.shared.EmailService;
import com.companya.labms.shared.Role;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EquipmentRequestService {

    private final EquipmentRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public EquipmentRequestService(EquipmentRequestRepository requestRepository,
                                   UserRepository userRepository,
                                   EmailService emailService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    /** Student: submit a new equipment request */
    public EquipmentRequestDTO submitRequest(String username, EquipmentRequestDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        EquipmentRequest request = new EquipmentRequest();
        request.setUser(user);
        request.setItemName(dto.getItemName());
        request.setDescription(dto.getDescription());
        request.setQuantity(dto.getQuantity() > 0 ? dto.getQuantity() : 1);
        request.setUrgencyLevel(
                dto.getUrgencyLevel() != null
                        ? EquipmentRequest.UrgencyLevel.valueOf(dto.getUrgencyLevel())
                        : EquipmentRequest.UrgencyLevel.MEDIUM
        );

        EquipmentRequest savedRequest = requestRepository.save(request);

        // Send notifications to lab technicians
        List<User> technicians = userRepository.findByRole(Role.LAB_TECHNICIAN);
        for (User tech : technicians) {
            try {
                emailService.sendEquipmentRequestNotification(
                    tech.getEmail(),
                    user.getUsername(),
                    savedRequest.getItemName(),
                    savedRequest.getQuantity()
                );
            } catch (Exception e) {
                // Log exception and continue so submission doesn't fail if email fails
                System.err.println("Failed to send notification to " + tech.getEmail() + ": " + e.getMessage());
            }
        }

        return toDTO(savedRequest);
    }

    /** Student: view own requests */
    public List<EquipmentRequestDTO> getMyRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return requestRepository.findByUserOrderByCreatedAtDesc(user)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Admin/Technician: view all requests */
    public List<EquipmentRequestDTO> getAllRequests() {
        return requestRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    /** Admin/Technician: approve a request */
    public EquipmentRequestDTO approveRequest(Long id, String adminNote) {
        EquipmentRequest request = findById(id);
        request.setStatus(EquipmentRequest.RequestStatus.APPROVED);
        if (adminNote != null && !adminNote.isBlank()) request.setAdminNote(adminNote);
        return toDTO(requestRepository.save(request));
    }

    /** Admin/Technician: reject a request */
    public EquipmentRequestDTO rejectRequest(Long id, String adminNote) {
        EquipmentRequest request = findById(id);
        request.setStatus(EquipmentRequest.RequestStatus.REJECTED);
        if (adminNote != null && !adminNote.isBlank()) request.setAdminNote(adminNote);
        return toDTO(requestRepository.save(request));
    }

    /** Student: edit a pending request */
    public EquipmentRequestDTO editRequest(Long id, String username, EquipmentRequestDTO dto) {
        EquipmentRequest request = findById(id);
        if (!request.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only edit your own requests");
        }
        if (request.getStatus() != EquipmentRequest.RequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be edited");
        }
        request.setItemName(dto.getItemName());
        request.setDescription(dto.getDescription());
        request.setQuantity(dto.getQuantity() > 0 ? dto.getQuantity() : 1);
        if (dto.getUrgencyLevel() != null) {
            request.setUrgencyLevel(EquipmentRequest.UrgencyLevel.valueOf(dto.getUrgencyLevel()));
        }
        return toDTO(requestRepository.save(request));
    }

    /** Student: delete a pending request */
    public void deleteRequest(Long id, String username) {
        EquipmentRequest request = findById(id);
        if (!request.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only delete your own requests");
        }
        if (request.getStatus() != EquipmentRequest.RequestStatus.PENDING) {
            throw new RuntimeException("Only pending requests can be deleted");
        }
        requestRepository.delete(request);
    }

    /** Summary stats for charts */
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total",    requestRepository.count());
        stats.put("pending",  requestRepository.countByStatus(EquipmentRequest.RequestStatus.PENDING));
        stats.put("approved", requestRepository.countByStatus(EquipmentRequest.RequestStatus.APPROVED));
        stats.put("rejected", requestRepository.countByStatus(EquipmentRequest.RequestStatus.REJECTED));

        // Monthly counts (for bar chart)
        Map<Integer, Long> monthly = new LinkedHashMap<>();
        for (int m = 1; m <= 12; m++) monthly.put(m, 0L);
        for (Object[] row : requestRepository.countByMonth()) {
            monthly.put(((Number) row[0]).intValue(), ((Number) row[1]).longValue());
        }
        stats.put("monthly", monthly);
        return stats;
    }

    private EquipmentRequest findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found: " + id));
    }

    private EquipmentRequestDTO toDTO(EquipmentRequest r) {
        EquipmentRequestDTO dto = new EquipmentRequestDTO();
        dto.setId(r.getId());
        dto.setItemName(r.getItemName());
        dto.setDescription(r.getDescription());
        dto.setQuantity(r.getQuantity());
        dto.setUrgencyLevel(r.getUrgencyLevel() != null ? r.getUrgencyLevel().name() : "MEDIUM");
        dto.setStatus(r.getStatus() != null ? r.getStatus().name() : "PENDING");
        dto.setAdminNote(r.getAdminNote());
        dto.setSubmittedBy(r.getUser() != null ? r.getUser().getUsername() : "");
        dto.setSubmittedByRole(r.getUser() != null ? r.getUser().getRole().name() : "");
        dto.setCreatedAt(r.getCreatedAt() != null ? r.getCreatedAt().toString() : "");
        return dto;
    }
}
