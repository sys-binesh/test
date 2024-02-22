package functionalpoc;

import java.util.List;

import lombok.Data;

@Data
public class BulkStatus {
	
	int status;
	List<BulkApproval> bulkApproval;

}
