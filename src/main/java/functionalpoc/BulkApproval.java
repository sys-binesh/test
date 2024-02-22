package functionalpoc;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BulkApproval {
	long id;
	Date weekStartDate;
	Date weekEndDate;
	String gfsid;
	int branchId;
	int routine;
	int version;
	String nextApprovar;
	double amount;
	double accumulatedAmount;

}
