package vn.com.gsoft.report.service;

import vn.com.gsoft.report.model.system.Profile;

public interface BaseService {
    Profile getLoggedUser() throws Exception;

}
