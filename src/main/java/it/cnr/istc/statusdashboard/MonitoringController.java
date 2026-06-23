package it.cnr.istc.statusdashboard;

import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/servicesmonitoring")
public class MonitoringController {

    private final ServicesRepository repository;
    private final RestTemplate restTemplate; // Tool to make server-to-server HTTP requests

    public MonitoringController(ServicesRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate(); // Initialize RestTemplate
    }

    @PostConstruct
    public void insertSampleServices() {
        // Clear previous data to avoid duplicates upon restart
        repository.deleteAll();

        // Adding sample services based on the tutor's requests
        Services s1 = new Services();
        s1.setDomain("https://ticket.iadmin.istc.cnr.it/actuator/health");
        s1.setStatus(false); // Default false, will be updated dynamically

        Services s2 = new Services();
        s2.setDomain("https://auth.iadmin.istc.cnr.it/actuator/health");
        s2.setStatus(false);

        Services s3 = new Services();
        s3.setDomain("https://progetti.iadmin.istc.cnr.it/actuator/health");
        s3.setStatus(false);

        repository.save(s1);
        repository.save(s2);
        repository.save(s3);

        System.out.println(">>> Monitoring services successfully initialized! <<<");
    }

    // CRUD - READ: Get all monitored services with real-time health check updates
    @GetMapping
    public List<Services> getAllServices() {
        List<Services> servicesList = repository.findAll();

        // Loop through each service and check its live status using RestTemplate
        for (Services service : servicesList) {
            try {
                // Send GET request to the service URL and parse response as a Map
                Map<String, Object> response = restTemplate.getForObject(service.getDomain(), Map.class);

                if (response != null && "UP".equals(response.get("status"))) {
                    service.setStatus(true);  // Service is alive and running
                } else {
                    service.setStatus(false); // Service returned a response but status is not UP
                }
            } catch (Exception e) {
                // If the server is offline or unreachable, an exception occurs
                service.setStatus(false);
            }
            // Save the updated status back to the database
            repository.save(service);
        }

        return repository.findAll();
    }

    // CRUD - CREATE: Add a new service to monitor
    @PostMapping
    public Services createService(@RequestBody Services newService) {
        return repository.save(newService);
    }

    // CRUD - UPDATE: Update monitored service data
    @PutMapping("/{id}")
    public Services updateService(@PathVariable Long id, @RequestBody Services updatedServiceData) {
        return repository.findById(id)
                .map(existingService -> {
                    existingService.setDomain(updatedServiceData.getDomain());
                    existingService.setStatus(updatedServiceData.getStatus());
                    return repository.save(existingService);
                })
                .orElseGet(() -> repository.save(updatedServiceData));
    }

    // CRUD - DELETE: Remove a service from monitoring
    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        repository.deleteById(id);
    }
}