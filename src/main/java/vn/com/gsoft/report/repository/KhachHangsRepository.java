package vn.com.gsoft.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.gsoft.report.entity.KhachHangs;

@Repository
public interface KhachHangsRepository extends JpaRepository<KhachHangs, Long> {

}
