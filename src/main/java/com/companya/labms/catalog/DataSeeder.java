package com.companya.labms.catalog;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {

    private final EquipmentRepository equipmentRepository;
    private final LabRepository labRepository;

    public DataSeeder(EquipmentRepository equipmentRepository, LabRepository labRepository) {
        this.equipmentRepository = equipmentRepository;
        this.labRepository = labRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (equipmentRepository.count() == 0) {
            Equipment eq1 = new Equipment();
            eq1.setName("Digital Oscilloscope");
            eq1.setDescription("Measurement — 200MHz, 2 Channels");
            eq1.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            eq1.setCreatedAt(LocalDateTime.now());
            eq1.setUpdatedAt(LocalDateTime.now());
            equipmentRepository.save(eq1);

            Equipment eq2 = new Equipment();
            eq2.setName("Microscope");
            eq2.setDescription("Optics — Binocular Compound");
            eq2.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            eq2.setCreatedAt(LocalDateTime.now());
            eq2.setUpdatedAt(LocalDateTime.now());
            equipmentRepository.save(eq2);

            Equipment eq3 = new Equipment();
            eq3.setName("3D Printer");
            eq3.setDescription("Prototyping — PLA/ABS Supported");
            eq3.setStatus(Equipment.EquipmentStatus.MAINTENANCE);
            eq3.setCreatedAt(LocalDateTime.now());
            eq3.setUpdatedAt(LocalDateTime.now());
            equipmentRepository.save(eq3);

            Equipment eq4 = new Equipment();
            eq4.setName("Multimeter");
            eq4.setDescription("Electronics — Fluke 87V");
            eq4.setStatus(Equipment.EquipmentStatus.AVAILABLE);
            eq4.setCreatedAt(LocalDateTime.now());
            eq4.setUpdatedAt(LocalDateTime.now());
            equipmentRepository.save(eq4);
        }

        if (labRepository.count() == 0) {
            Lab lab1 = new Lab();
            lab1.setLabName("Electronics Lab A");
            lab1.setLocation("Building 1, Room 101");
            lab1.setStatus(Lab.LabStatus.AVAILABLE);
            lab1.setCreatedAt(LocalDateTime.now());
            lab1.setUpdatedAt(LocalDateTime.now());
            labRepository.save(lab1);

            Lab lab2 = new Lab();
            lab2.setLabName("Hardware Prototyping Lab");
            lab2.setLocation("Building 2, Room 205");
            lab2.setStatus(Lab.LabStatus.AVAILABLE);
            lab2.setCreatedAt(LocalDateTime.now());
            lab2.setUpdatedAt(LocalDateTime.now());
            labRepository.save(lab2);

            Lab lab3 = new Lab();
            lab3.setLabName("Chemistry Lab");
            lab3.setLocation("Building 3, Room 301");
            lab3.setStatus(Lab.LabStatus.MAINTENANCE);
            lab3.setCreatedAt(LocalDateTime.now());
            lab3.setUpdatedAt(LocalDateTime.now());
            labRepository.save(lab3);
        }
    }
}
