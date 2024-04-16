package vn.com.gsoft.report.entity.ReportingDate;

import jakarta.persistence.*;
import lombok.*;
import vn.com.gsoft.report.entity.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "DonViTinhs")
public class DonViTinhs extends BaseEntity {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name = "TenDonViTinh")
    private String tenDonViTinh; // Tên đơn vị tính
    @Column(name = "MaNhaThuoc")
    private String maNhaThuoc; // Mã nhà thuốc
    @Column(name = "ReferenceId")
    private Long referenceId; // ID tham chiếu
    @Column(name = "ArchivedId")
    private Long archivedId; // ID đã lưu trữ
    @Column(name = "StoreId")
    private Long storeId; // ID cửa hàng
}