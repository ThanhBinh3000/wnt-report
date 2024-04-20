package vn.com.gsoft.report.service.impl.ReportingDate;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.com.gsoft.report.entity.ReportingDate.PhieuXuatChiTiets;
import vn.com.gsoft.report.entity.ReportingDate.PhieuXuats;
import vn.com.gsoft.report.model.dto.ReportingDate.PhieuXuatsReq;
import vn.com.gsoft.report.model.system.Profile;
import vn.com.gsoft.report.repository.ReportingDate.*;
import vn.com.gsoft.report.service.ReportingDate.PhieuXuatsService;
import vn.com.gsoft.report.service.impl.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.List;

@Service
@Log4j2
public class PhieuXuatsServiceImpl extends BaseServiceImpl<PhieuXuats, PhieuXuatsReq, Long> implements PhieuXuatsService {

    private PhieuXuatsRepository hdrRepo;

    private PhieuXuatChiTietsRepository phieuXuatChiTietsRepository;

    private PhieuNhapsRepository phieuNhapsRepository;

    private PhieuNhapChiTietsRepository phieuNhapChiTietsRepository;

    private KhachHangsRepository khachHangsRepository;

    private ThuocsRepository thuocsRepository;

    private DonViTinhsRepository donViTinhsRepository;

    @Autowired
    public PhieuXuatsServiceImpl(PhieuXuatsRepository hdrRepo,
                                 PhieuXuatChiTietsRepository phieuXuatChiTietsRepository,
                                 KhachHangsRepository khachHangsRepository,
                                 ThuocsRepository thuocsRepository,
                                 DonViTinhsRepository donViTinhsRepository,
                                 PhieuNhapsRepository phieuNhapsRepository,
                                 PhieuNhapChiTietsRepository phieuNhapChiTietsRepository) {
        super(hdrRepo);
        this.hdrRepo = hdrRepo;
        this.phieuXuatChiTietsRepository = phieuXuatChiTietsRepository;
        this.phieuNhapsRepository = phieuNhapsRepository;
        this.phieuNhapChiTietsRepository = phieuNhapChiTietsRepository;
        this.khachHangsRepository = khachHangsRepository;
        this.thuocsRepository = thuocsRepository;
        this.donViTinhsRepository = donViTinhsRepository;
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
                khachHangsRepository.findById(item.getKhachHangMaKhachHang()).ifPresent(khachHangs -> item.setTenKhachHang(khachHangs.getTenKhachHang()));
            }
            if (item.getSoPhieuXuat() != null) {
                List<PhieuXuatChiTiets> listDtl = phieuXuatChiTietsRepository.findAllByPhieuXuatMaPhieuXuat(item.getSoPhieuXuat().intValue());
                listDtl.forEach(child -> {
                    if (child.getThuocThuocId() != null) {
                        thuocsRepository.findById(child.getThuocThuocId()).ifPresent(thuoc -> {
                            item.setMaThuoc(thuoc.getMaThuoc());
                            item.setTenThuoc(thuoc.getTenThuoc());
                        });
                    }
                    if (child.getDonViTinhMaDonViTinh() != null) {
                        donViTinhsRepository.findById(Long.valueOf(child.getDonViTinhMaDonViTinh())).ifPresent(donViTinhs -> item.setTenDonViTinh(donViTinhs.getTenDonViTinh()));
                    }
                    BigDecimal totalSoLuong = listDtl.stream()
                            .map(PhieuXuatChiTiets::getSoLuong)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal totalRevenue = listDtl.stream()
                            .map(PhieuXuatChiTiets::getRevenue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    item.setGiaXuat(child.getGiaXuat());
                    item.setSoLuong(totalSoLuong);
                    item.setRevenue(totalRevenue);
                    item.setTongTien(child.getGiaXuat().multiply(totalSoLuong));
                });

            }
        });
        return phieuXuats;
    }
}