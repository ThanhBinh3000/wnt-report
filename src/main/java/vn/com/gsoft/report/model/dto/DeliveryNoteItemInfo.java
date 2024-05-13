package vn.com.gsoft.report.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryNoteItemInfo {

    public Long nodeId;

    public Long noteItemId;
    public Long drugId;
    public String drugName;
    public String drugCode;

}
