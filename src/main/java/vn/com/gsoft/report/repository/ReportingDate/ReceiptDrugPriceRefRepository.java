package vn.com.gsoft.report.repository.ReportingDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.report.entity.ReportingDate.KhachHangs;
import vn.com.gsoft.report.entity.ReportingDate.ReceiptDrugPriceRef;

@Repository
public interface ReceiptDrugPriceRefRepository extends JpaRepository<ReceiptDrugPriceRef, Long> {

}
