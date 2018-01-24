package tum.sebis.apm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import tum.sebis.apm.domain.Person;

@Component
public class PersonServiceClient extends AbstractMicroserviceClient<Person> {

    @Autowired
    public PersonServiceClient(DiscoveryClient discoveryClient, RestOperations restTemplate) {
        super(discoveryClient, restTemplate, "PersonService");
    }

    public boolean isPersonExisting(String id) {
        if (id == null || id.isEmpty()){
            return false;
        }
        ResponseEntity<Person> response =
            sendAuthorizedRequest(generateUrl("people", id), HttpMethod.GET, null, Person.class);
        return response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null;
    }
}
