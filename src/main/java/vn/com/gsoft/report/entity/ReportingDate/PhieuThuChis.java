package vn.com.gsoft.report.entity.ReportingDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.gsoft.report.entity.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PhieuThuChis")
public class PhieuThuChis extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "SoPhieu")
    private Integer soPhieu; // Số phiếu
    @Column(name = "DienGiai")
    private String dienGiai; // Diễn giải
    @Column(name = "NgayTao")
    private Date ngayTao; // Ngày tạo
    @Column(name = "LoaiPhieu")
    private Long loaiPhieu; // Loại phiếu
    @Column(name = "NhaThuoc_MaNhaThuoc")
    private String nhaThuocMaNhaThuoc; // Mã nhà thuốc
    @Column(name = "KhachHang_MaKhachHang")
    private Long khachHangMaKhachHang; // Mã khách hàng
    @Column(name = "NhaCungCap_MaNhaCungCap")
    private Long nhaCungCapMaNhaCungCap; // Mã nhà cung cấp
    @Column(name = "UserProfile_UserId")
    private Integer userProfileUserId; // ID người dùng
    @Column(name = "Amount")
    private BigDecimal amount; // Số tiền
    @Column(name = "NguoiNhan")
    private String nguoiNhan; // Người nhận
    @Column(name = "DiaChi")
    private String diaChi; // Địa chỉ
    @Column(name = "LoaiThuChi_MaLoaiPhieu")
    private Integer loaiThuChiMaLoaiPhieu; // Mã loại phiếu thu chi
    @Column(name = "Active")
    private Boolean active; // Đang hoạt động
    @Column(name = "CustomerId")
    private Integer customerId; // ID khách hàng
    @Column(name = "SupplierId")
    private Integer supplierId; // ID nhà cung cấp
    @Column(name = "ArchivedId")
    private Integer archivedId; // ID được lưu trữ
    @Column(name = "StoreId")
    private Integer storeId; // ID cửa hàng
    @Column(name = "PaymentTypeId")
    private Integer paymentTypeId; // ID loại thanh toán
    @Column(name = "MaCoSo")
    private String maCoSo; // Mã cơ sở
    @Column(name = "NhanVienId")
    private Integer nhanVienId; // ID nhân viên
    @Column(name = "RewardProgramId")
    private Integer rewardProgramId; // ID chương trình khuyến mãi
    @Column(name = "FromDate")
    private Date fromDate; // Từ ngày
    @Column(name = "ToDate")
    private Date toDate; // Đến ngày
}