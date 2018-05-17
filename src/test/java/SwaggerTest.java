import org.junit.Test;



import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.PetApi;

import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


public class SwaggerTest {
    @Test
    public void testFindPetsByStatus() throws ApiException {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        // Configure OAuth2 access token for authorization: petstore_auth
        OAuth petstore_auth = (OAuth) defaultClient.getAuthentication("petstore_auth");
        petstore_auth.setAccessToken("YOUR ACCESS TOKEN");

        PetApi apiInstance = new PetApi();
        Pet body = new Pet(); // Pet | Pet object that needs to be added to the store
        try {
            apiInstance.addPet(body);
        } catch (ApiException e) {
            System.err.println("Exception when calling PetApi#addPet");
            e.printStackTrace();
        }

        List<String> supplierNames1 = new ArrayList<String>();
        supplierNames1.add("sold");
        List<Pet> pets=apiInstance.findPetsByStatus(supplierNames1);
        assertEquals(pets.get(0).getStatus(), "sold");

    }


}
