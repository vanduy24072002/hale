package imt.duy.borrow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReport {
    List<Integer> total ;
    List<Integer> lastWeek ;
}
