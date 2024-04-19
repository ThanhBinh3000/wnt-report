package vn.com.gsoft.report.model.dto.ReportingDate;

import lombok.Data;
import vn.com.gsoft.report.model.system.BaseRequest;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PhieuThuChisReq extends BaseRequest {
    private Integer soPhieu;
    private String dienGiai;
    private Date ngayTao;
    private Integer loaiPhieu;
    private String nhaThuocMaNhaThuoc;
    private Long khachHangMaKhachHang;
    private Long nhaCungCapMaNhaCungCap;
    private Integer userProfileUserId;
    private BigDecimal amount;
    private String nguoiNhan;
    private String diaChi;
    private Integer loaiThuChiMaLoaiPhieu;
    private Boolean active;
    private Integer customerId;
    private Integer supplierId;
    private Integer archivedId;
    private Integer storeId;
    private Integer paymentTypeId;
    private String maCoSo;
    private Integer nhanVienId;
    private Integer rewardProgramId;
    private Date fromDate;
    private Date toDate;
}
