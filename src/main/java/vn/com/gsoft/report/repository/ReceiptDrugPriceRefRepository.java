package vn.com.gsoft.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.report.entity.ReceiptDrugPriceRef;

@Repository
public interface ReceiptDrugPriceRefRepository extends JpaRepository<ReceiptDrugPriceRef, Long> {

}
