package functionalpoc;

import java.io.IOException;

import com.google.gson.Gson;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.ServiceBusTopicTrigger;



/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {
   
	@FunctionName("sbtopicprocessor")
    public void run(
        @ServiceBusTopicTrigger(
            name = "pocnttdata",
            topicName = "poctopic",
            subscriptionName = "pocsubscription",
            connection = "SBConnection"
            
        ) String message,
        final ExecutionContext context
    ) {
		Gson gson = new Gson();
		Product product = gson.fromJson(message, Product.class);
		new AzureTableStorage().saveToStorage(product,context);
		try {
			new AzureTableStorage().whenPostJsonUsingHttpClient(message) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			context.getLogger().info("Exception in calling logicapp "+e);
			e.printStackTrace();
		}
        context.getLogger().info("Received message : " + message);
    }
}
