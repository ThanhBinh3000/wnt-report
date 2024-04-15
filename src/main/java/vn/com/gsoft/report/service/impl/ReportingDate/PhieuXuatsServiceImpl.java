package vn.com.gsoft.report.service.impl.ReportingDate;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.report.entity.ReportingDate.KhachHangs;
import vn.com.gsoft.report.entity.ReportingDate.PhieuXuats;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuXuatsReq;
import vn.com.gsoft.report.model.system.Profile;
import vn.com.gsoft.report.repository.ReportingDate.KhachHangsRepository;
import vn.com.gsoft.report.repository.ReportingDate.PhieuXuatsRepository;
import vn.com.gsoft.report.service.ReportingDate.PhieuXuatsService;
import vn.com.gsoft.report.service.impl.BaseServiceImpl;

import java.util.Optional;

@Service
@Log4j2
public class PhieuXuatsServiceImpl extends BaseServiceImpl<PhieuXuats, PhieuXuatsReq, Long> implements PhieuXuatsService {

    private PhieuXuatsRepository hdrRepo;

    private KhachHangsRepository khachHangsRepository;

    @Autowired
    public PhieuXuatsServiceImpl(PhieuXuatsRepository hdrRepo, KhachHangsRepository khachHangsRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.khachHangsRepository = khachHangsRepository;
    }

    @Override
    public Page<PhieuXuats> searchReportingDate(PhieuXuatsReq req) throws Exception {
        Profile userInfo = this.getLoggedUser();
        if (userInfo == null)
            throw new Exception("Bad request.");
        Pageable pageable = PageRequest.of(req.getPaggingReq().getPage(), req.getPaggingReq().getLimit());
        Page<PhieuXuats> phieuXuats = hdrRepo.searchReportingDate(req, pageable);
        phieuXuats.getContent().forEach(item -> {
            if (item.getKhachHangMaKhachHang() != null) {
                Optional<KhachHangs> byIdNt = khachHangsRepository.findById(item.getKhachHangMaKhachHang());
                byIdNt.ifPresent(khachHangs -> item.setTenKhachHang(khachHangs.getTenKhachHang()));
            }
        });
        return phieuXuats;
    }
}