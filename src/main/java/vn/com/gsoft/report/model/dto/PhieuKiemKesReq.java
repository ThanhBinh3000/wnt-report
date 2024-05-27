package vn.com.gsoft.report.model.dto;

import lombok.Data;
import vn.com.gsoft.report.model.system.BaseRequest;

import java.util.Date;
import java.util.List;

@Data
public class PhieuKiemKesReq extends BaseRequest {
    private Long phieuNhapMaPhieuNhap;
    private Long phieuXuatMaPhieuXuat;
    private String nhaThuocMaNhaThuoc;
    private Long userProfileUserId;
    private Boolean daCanKho;
    private Boolean active;
    private Integer soPhieu;
    private Long archivedId;
    private Long storeId;
    private Date archivedDate;

    private Long thuocThuocId;

    private Date fromDate;
    private Date toDate;
    private String soLo;
    private Date hanDung;
}
