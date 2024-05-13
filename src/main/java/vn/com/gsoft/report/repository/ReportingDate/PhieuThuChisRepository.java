package vn.com.gsoft.report.repository.ReportingDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.report.entity.ReportingDate.PhieuThuChis;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuThuChisReq;
import vn.com.gsoft.report.repository.BaseRepository;

import java.util.List;


@Repository
public interface PhieuThuChisRepository extends BaseRepository<PhieuThuChis, PhieuThuChisReq, Long> {
    @Query("SELECT c FROM PhieuThuChis c " +
            "WHERE 1=1 "
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR lower(c.nhaThuocMaNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.nhaThuocMaNhaThuoc},'%'))))"
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
            + " AND (:#{#param.archivedId} IS NULL OR c.archivedId = :#{#param.archivedId}) "
            + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
            + " ORDER BY c.id desc"
    )
    Page<PhieuThuChis> searchPage(@Param("param") PhieuThuChisReq param, Pageable pageable);


    @Query("SELECT c FROM PhieuThuChis c " +
            " WHERE c.nhaThuocMaNhaThuoc = :#{#param.nhaThuocMaNhaThuoc}"
            + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
            + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR lower(c.nhaThuocMaNhaThuoc) LIKE lower(concat('%',CONCAT(:#{#param.nhaThuocMaNhaThuoc},'%'))))"
            + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId}) "
            + " AND (:#{#param.archivedId} IS NULL OR c.archivedId = :#{#param.archivedId}) "
            + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
            + " AND (:#{#param.fromDate} IS NULL OR c.ngayTao >= :#{#param.fromDate}) "
            + " AND (:#{#param.toDate} IS NULL OR c.ngayTao <= :#{#param.toDate}) "
            + " ORDER BY c.id desc"
    )
    List<PhieuThuChis> searchList(@Param("param") PhieuThuChisReq param);
}
