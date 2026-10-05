package com.digitinary.customercare.usecase.customer;

import com.digitinary.customercare.repository.CustomerRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CustomerCleanup {

    private final CustomerRepo customerRepo;

    public CustomerCleanup(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    // يعمل يومياً الساعة 3:00 صباحاً
    // Cron Format: second, minute, hour, day-of-month, month, day-of-week
    // برضه ai ساعدني فيها لكن الموضوع جميل وفاهم الفكرة

    @Scheduled(cron = "0 0 3 * * *")
    public void execute() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = customerRepo.deleteExpiredCustomers(now);

        System.out.println("Cleanup Job Executed: Deleted " + deletedCount + " expired customers at " + now);
    }
}