package vn.com.gsoft.report.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.report.model.dto.NhanVienNhaThuocsReq;
import vn.com.gsoft.report.entity.NhanVienNhaThuocs;

import java.util.List;

@Repository
public interface NhanVienNhaThuocsRepository extends JpaRepository<NhanVienNhaThuocs, Long> {

    @Query("SELECT c FROM NhanVienNhaThuocs c " +
            "WHERE 1=1 "
            + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR c.nhaThuocMaNhaThuoc = :#{#param.nhaThuocMaNhaThuoc}) "
            + " ORDER BY c.id desc"
    )
    Page<NhanVienNhaThuocs> searchPage(@Param("param") NhanVienNhaThuocsReq param, Pageable pageable);

    @Query("SELECT c FROM NhanVienNhaThuocs c " +
            "WHERE 1=1 "
            + " AND (:#{#param.nhaThuocMaNhaThuoc} IS NULL OR c.nhaThuocMaNhaThuoc = :#{#param.nhaThuocMaNhaThuoc}) "
            + " ORDER BY c.id desc"
    )
    List<NhanVienNhaThuocs> searchList(@Param("param") NhanVienNhaThuocsReq param);

}
