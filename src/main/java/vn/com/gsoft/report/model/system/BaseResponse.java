package vn.com.gsoft.report.model.system;

import lombok.Data;

@Data
public class BaseResponse {
	Object data;
	Object otherData;
	int statusCode;//0: succ <>0: fail
	String message;
	Object included;
}