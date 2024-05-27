package vn.com.gsoft.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.report.entity.PhieuKiemKeChiTiets;

import java.util.List;

@Repository
public interface PhieuKiemKeChiTietsRepository extends CrudRepository<PhieuKiemKeChiTiets, Long> {
//  @Query("SELECT c FROM PhieuKiemKeChiTiets c " +
//         "WHERE 1=1 "
//          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
//          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId})"
//          + " AND (:#{#param.thuocThuocId} IS NULL OR c.thuocThuocId = :#{#param.thuocThuocId}) "
//          + " AND (:#{#param.tonKho} IS NULL OR c.tonKho = :#{#param.tonKho}) "
//          + " AND (:#{#param.thucTe} IS NULL OR c.thucTe = :#{#param.thucTe}) "
//          + " AND (:#{#param.phieuKiemKeMaPhieuKiemKe} IS NULL OR c.phieuKiemKeMaPhieuKiemKe = :#{#param.phieuKiemKeMaPhieuKiemKe}) "
//          + " AND (:#{#param.donGia} IS NULL OR c.donGia = :#{#param.donGia}) "
//          + " AND (:#{#param.soLo} IS NULL OR lower(c.soLo) LIKE lower(concat('%',CONCAT(:#{#param.soLo},'%'))))"
//          + " AND (:#{#param.archiveDrugId} IS NULL OR c.archiveDrugId = :#{#param.archiveDrugId}) "
//          + " AND (:#{#param.archivedId} IS NULL OR c.archivedId = :#{#param.archivedId}) "
//          + " AND (:#{#param.referenceId} IS NULL OR c.referenceId = :#{#param.referenceId}) "
//          + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
//          + " ORDER BY c.id desc"
//  )
//  Page<PhieuKiemKeChiTiets> searchPage(@Param("param") PhieuKiemKeChiTietsReq param, Pageable pageable);
  
//
//  @Query("SELECT c FROM PhieuKiemKeChiTiets c " +
//         "WHERE 1=1 "
//          + " AND (:#{#param.id} IS NULL OR c.id = :#{#param.id}) "
//          + " AND (:#{#param.recordStatusId} IS NULL OR c.recordStatusId = :#{#param.recordStatusId})"
//          + " AND (:#{#param.thuocThuocId} IS NULL OR c.thuocThuocId = :#{#param.thuocThuocId}) "
//          + " AND (:#{#param.tonKho} IS NULL OR c.tonKho = :#{#param.tonKho}) "
//          + " AND (:#{#param.thucTe} IS NULL OR c.thucTe = :#{#param.thucTe}) "
//          + " AND (:#{#param.phieuKiemKeMaPhieuKiemKe} IS NULL OR c.phieuKiemKeMaPhieuKiemKe = :#{#param.phieuKiemKeMaPhieuKiemKe}) "
//          + " AND (:#{#param.donGia} IS NULL OR c.donGia = :#{#param.donGia}) "
//          + " AND (:#{#param.soLo} IS NULL OR lower(c.soLo) LIKE lower(concat('%',CONCAT(:#{#param.soLo},'%'))))"
//          + " AND (:#{#param.archiveDrugId} IS NULL OR c.archiveDrugId = :#{#param.archiveDrugId}) "
//          + " AND (:#{#param.archivedId} IS NULL OR c.archivedId = :#{#param.archivedId}) "
//          + " AND (:#{#param.referenceId} IS NULL OR c.referenceId = :#{#param.referenceId}) "
//          + " AND (:#{#param.storeId} IS NULL OR c.storeId = :#{#param.storeId}) "
//          + " ORDER BY c.id desc"
//  )
//  List<PhieuKiemKeChiTiets> searchList(@Param("param") PhieuKiemKeChiTietsReq param);

  List<PhieuKiemKeChiTiets> findByPhieuKiemKeMaPhieuKiemKe(Long phieuKiemKeMaPhieuKiemKe);

    void deleteByPhieuKiemKeMaPhieuKiemKe(Long id);
}
