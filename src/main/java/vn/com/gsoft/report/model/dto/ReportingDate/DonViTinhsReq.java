package vn.com.gsoft.report.model.dto.ReportingDate;

import lombok.Data;
import vn.com.gsoft.report.model.system.BaseRequest;


@Data
public class DonViTinhsReq extends BaseRequest {
    private String tenDonViTinh;
    private String maNhaThuoc;
    private Long referenceId;
    private Long archivedId;
    private Long storeId;
}

