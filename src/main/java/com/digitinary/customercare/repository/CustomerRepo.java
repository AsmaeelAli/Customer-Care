package com.digitinary.customercare.repository;

import com.digitinary.customercare.model.entities.customer.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity, Long>, JpaSpecificationExecutor<CustomerEntity> {
    Optional<CustomerEntity> findByUsername(String username);


    /**
     *
     * هون انا عرفت كويري بتحذف الكستمر حسب تاريخ الحذف يعني hard delete
     * <p>
     * انا بعرف في الانظمة الكبيرة لازم يكون الحذف على باتشات يعني مثلا عندك 1000 او اكثر عميل لازم تحذفهم فرضااا
     * <p>
     * الفكرة انه لازم ترسل كويري كويري يعني احذف 500 كل مرة لحتى تخلصهم
     * <p>
     * معلومة قرات عنها ومهمة عشان نا تعمل لوود او ضغط على الداتا بيسز !
     *
     */

    @Transactional
    @Modifying
    @Query("DELETE FROM CustomerEntity c WHERE c.deletedAt <= :now")
    int deleteExpiredCustomers(LocalDateTime now);
}
