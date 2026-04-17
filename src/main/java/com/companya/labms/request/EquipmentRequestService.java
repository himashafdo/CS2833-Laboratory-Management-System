package com.companya.labms.request;

import com.companya.labms.auth.User;
import com.companya.labms.auth.UserRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class EquipmentRequestService {

    private static final Logger logger = LoggerFactory.getLogger(EquipmentRequestService.class);

    private final EquipmentRequestRepository repository;
    private final UserRepository userRepository;

    public EquipmentRequestService(EquipmentRequestRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<EquipmentRequest> getAllRequests() {
        return repository.findAll();
    }

    public List<EquipmentRequest> getUserRequests(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return repository.findByUserId(user.getId());
    }

    public EquipmentRequest createRequest(String username, String equipmentDetails, String justification, Integer quantity, EquipmentRequest.UrgencyLevel urgencyLevel) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        EquipmentRequest request = new EquipmentRequest();
        request.setUser(user);
        request.setEquipmentDetails(equipmentDetails);
        request.setJustification(justification);
        request.setQuantity(quantity);
        request.setUrgencyLevel(urgencyLevel);
        request.setStatus(EquipmentRequest.RequestStatus.PENDING);
        
        return repository.save(request);
    }

    public EquipmentRequest updateStatus(Long requestId, EquipmentRequest.RequestStatus status) {
        EquipmentRequest request = repository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(status);
        
        // Notifications shall be sent to users regarding request status.
        sendNotification(request.getUser(), "Your equipment request for '" + request.getEquipmentDetails() + "' has been " + status.name());

        // Integration with procurement module for approved requests.
        if (status == EquipmentRequest.RequestStatus.APPROVED) {
            triggerProcurementIntegration(request);
        }

        return repository.save(request);
    }

    public void deleteRequest(Long requestId) {
        repository.deleteById(requestId);
    }

    private void sendNotification(User user, String message) {
        // TODO: Implement actual email/SMS notification logic
        logger.info("NOTIFICATION sent to user " + user.getUsername() + ": " + message);
    }

    private void triggerProcurementIntegration(EquipmentRequest request) {
        // TODO: Implement actual integration with Procurement Module
        logger.info("PROCUREMENT INTEGRATION triggered for creating order: " + request.getQuantity() + "x " + request.getEquipmentDetails());
    }
}
