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
@Table(name = "KhachHangs")
public class KhachHangs extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TenKhachHang")
    private String tenKhachHang; // Tên khách hàng
    @Column(name = "DiaChi")
    private String diaChi; // Địa chỉ
    @Column(name = "SoDienThoai")
    private String soDienThoai; // Số điện thoại
    @Column(name = "NoDauKy" , columnDefinition = "decimal default 0")
    private BigDecimal noDauKy; // Nợ đầu kỳ
    @Column(name = "DonViCongTac")
    private String donViCongTac; // Đơn vị công tác
    @Column(name = "Email")
    private String email; // Email
    @Column(name = "GhiChu")
    private String ghiChu; // Ghi chú
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc; // Mã nhà thuốc
    @Column(name = "MaNhomKhachHang")
    private Long maNhomKhachHang; // Mã nhóm khách hàng
    @Column(name = "Active")
    private Boolean active; // Hoạt động
    @Column(name = "CustomerTypeId" , columnDefinition = "integer default 0")
    private Integer customerTypeId; // Loại khách hàng
    @Column(name = "BarCode")
    private String barCode; // Mã vạch
    @Column(name = "BirthDate")
    private Date birthDate; // Ngày sinh
    @Column(name = "Code")
    private String code; // Mã
    @Column(name = "Score", columnDefinition = "decimal default 0")
    private BigDecimal score; // Điểm
    @Column(name = "InitScore", columnDefinition = "decimal default 0")
    private BigDecimal initScore; // Điểm khởi tạo
    @Column(name = "ArchivedId", columnDefinition = "integer default 0")
    private Integer archivedId; // ID đã lưu trữ
    @Column(name = "ReferenceId", columnDefinition = "integer default 0")
    private Integer referenceId; // ID tham chiếu
    @Column(name = "StoreId", columnDefinition = "integer default 0")
    private Integer storeId; // ID cửa hàng
    @Column(name = "RegionId", columnDefinition = "integer default 0" )
    private Integer regionId; // ID khu vực
    @Column(name = "CityId",columnDefinition = "integer default 0")
    private Integer cityId; // ID thành phố
    @Column(name = "WardId", columnDefinition = "integer default 0")
    private Integer wardId; // ID phường
    @Column(name = "MasterId", columnDefinition = "integer default 0")
    private Integer masterId; // ID chính
    @Column(name = "MetadataHash", columnDefinition = "integer default 0")
    private Integer metadataHash; // Metadata Hash
    @Column(name = "PreMetadataHash", columnDefinition = "integer default 0")
    private Integer preMetadataHash; // Metadata Hash trước
    @Column(name = "NationalFacilityCode")
    private String nationalFacilityCode; // Mã cơ sở quốc gia
    @Column(name = "MappingStoreId", columnDefinition = "integer default 0")
    private Integer mappingStoreId; // ID ánh xạ cửa hàng
    @Column(name = "TotalScore" , columnDefinition = "decimal default 0")
    private BigDecimal totalScore; // Tổng điểm
    @Column(name = "SexId", columnDefinition = "integer default 0")
    private Integer sexId; // ID giới tính
    @Column(name = "NameContacter")
    private String nameContacter; // Tên người liên hệ
    @Column(name = "PhoneContacter")
    private String phoneContacter; // Số điện thoại người liên hệ
    @Column(name = "RefCus")
    private String refCus; // Tham chiếu khách hàng
    @Column(name = "CusType", columnDefinition = "boolean default 0")
    private Boolean cusType; // Loại khách hàng
    @Column(name = "TaxCode")
    private String taxCode; // Mã số thuế
    @Column(name = "MedicalIdentifier")
    private String medicalIdentifier; // Mã số bảo hiểm y tế
    @Column(name = "CitizenIdentification")
    private String citizenIdentification; // Số CMND
    @Column(name = "HealthInsuranceNumber")
    private String healthInsuranceNumber; // Số bảo hiểm y tế
    @Column(name = "Job")
    private String job; // Nghề nghiệp
    @Column(name = "AbilityToPay")
    private String abilityToPay; // Khả năng thanh toán
    @Column(name = "ZaloId")
    private String zaloId; // Zalo ID
}