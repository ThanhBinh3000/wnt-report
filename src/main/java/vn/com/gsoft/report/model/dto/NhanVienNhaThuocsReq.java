package vn.com.gsoft.report.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NhanVienNhaThuocsReq {

    private Long id;

    private String role;
    private String nhaThuocMaNhaThuoc;
    private Integer userUserId;
    private Integer archivedId;
    private Integer storeId;
}

