package functionalpoc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.microsoft.azure.functions.ExecutionContext;

public class AzureTableStorage {
	
	public void saveToStorage(Product product,ExecutionContext context) {
		
		try {
			//Properties prop = readPropertiesFile("application.properties");
			context.getLogger().info("Received message : Inside Save Storage s" );
			// Create a TableClient with a connection string and a table name.
			//TableClient tableClient = new TableClientBuilder().connectionString("DefaultEndpointsProtocol=https;AccountName=nttdatapocstorage;AccountKey=fzdMxNvNSdUDRX1JG43ADvxAkG7f78nl/HH5CAzzw+uJHf/Vkc2OmBrWLMZtbEpw4hm61EncLF1n+AStkli90A==;EndpointSuffix=core.windows.net").tableName("Product")
					//.buildClient();
			
			TableClient tableClient = new TableClientBuilder().connectionString("DefaultEndpointsProtocol=https;AccountName=aznttdatapocsa;AccountKey=rYQioYhwJZIz4P4eicdDdbSh2nbm85Mpfj3XwI/C4QhfBrVMMxB8uDOJ6q/k185bjRwFDLkhRBpt+AStACgpTA==;EndpointSuffix=core.windows.net").tableName("Product")
					.buildClient();

			// Create a new employee TableEntity.
			String partitionKey = "AsynProduct";
			String rowKey = generateUUID();
			Map<String, Object> productInfo = new HashMap<>();
			productInfo.put("name", product.getName());
			productInfo.put("type", product.getType());
			productInfo.put("manufacturer", product.getManufacturer());
			TableEntity productTable = new TableEntity(partitionKey, rowKey).setProperties(productInfo);

			// Upsert the entity into the table
			tableClient.upsertEntity(productTable);
			product.setKey(rowKey);
		} catch (Exception e) {
			// Output the stack trace.
			e.printStackTrace();
			context.getLogger().info("Received message : Inside Save Storage" +e);
		}
		
	}
	
	public void whenPostJsonUsingHttpClient(String message) 
			  throws ClientProtocolException, IOException {
			    CloseableHttpClient client = HttpClients.createDefault();
			    HttpPost httpPost = new HttpPost("https://prod-02.southeastasia.logic.azure.com:443/workflows/5133785f2dd743c6845afa969f320153/triggers/manual/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=drzGKip-cZYtdvcGPXQwuHjYK3yHgPSbuh8RvyyYk38");
			    //HttpPost httpPost = new HttpPost("https://poc-ntt-logic-app-async.azurewebsites.net:443/api/poc-nnt-sync/triggers/manual/invoke?api-version=2022-05-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=twuU-qLf2pMTzmONp7I7oLjfn3A9yUGmJxEN53B4yDM");
		    StringEntity entity = new StringEntity(message);
			    httpPost.setEntity(entity);
			    httpPost.setHeader("Accept", "application/json");
			    httpPost.setHeader("Content-type", "application/json");

			   client.execute(httpPost);
			   
			    client.close();
			}
	
	public String generateUUID() {

		UUID uuid = UUID.randomUUID();
		String randomUUIDString = uuid.toString();
		return randomUUIDString.replaceAll("-", "");

	}
	
	public Properties readPropertiesFile(String fileName) throws IOException {
	      FileInputStream fis = null;
	      Properties prop = null;
	      try {
	         fis = new FileInputStream(fileName);
	         prop = new Properties();
	         prop.load(fis);
	      } catch(FileNotFoundException fnfe) {
	         fnfe.printStackTrace();
	      } catch(IOException ioe) {
	         ioe.printStackTrace();
	      } finally {
	         fis.close();
	      }
	      return prop;
	   }

}
