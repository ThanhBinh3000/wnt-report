package vn.com.gsoft.report.service.ReportingDate;

import org.springframework.data.domain.Page;
import vn.com.gsoft.report.entity.PhieuXuats;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuXuatsReq;
import vn.com.gsoft.report.service.BaseService;

public interface PhieuXuatsService extends BaseService<PhieuXuats, PhieuXuatsReq, Long> {

    Page<PhieuXuats> searchReportingDate(PhieuXuatsReq req) throws Exception;
}