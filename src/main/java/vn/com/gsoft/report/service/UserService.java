package vn.com.gsoft.report.service;

import vn.com.gsoft.report.model.system.Profile;

import java.util.Optional;

public interface UserService extends BaseService {
    Optional<Profile> findUserByToken(String token);

    Optional<Profile> findUserByUsername(String token);

}
