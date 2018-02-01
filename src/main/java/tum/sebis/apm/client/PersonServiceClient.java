package tum.sebis.apm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import tum.sebis.apm.domain.Person;
import tum.sebis.apm.web.rest.errors.PersonNotFoundException;
import tum.sebis.apm.web.rest.errors.PersonServiceException;

@Component
public class PersonServiceClient extends AbstractMicroserviceClient<Person> {

    @Autowired
    public PersonServiceClient(DiscoveryClient discoveryClient, RestOperations restTemplate) {
        super(discoveryClient, restTemplate, "PersonService");
    }

    public boolean isPersonExisting(String id) {
        try {
            Person person = getPersonById(id);
            return person != null;
        } catch (IllegalArgumentException|PersonNotFoundException e) {
            return false;
        }
    }

    public Person getPersonById(String id) {
        if (id == null || id.isEmpty()){
            throw new IllegalArgumentException("Id must not be null or empty");
        }
        ResponseEntity<Person> response =
            sendAuthorizedRequest(generateUrl("people", id), HttpMethod.GET, null, Person.class);
        if (response.getStatusCode().equals(HttpStatus.OK) && response.getBody() != null) {
            return response.getBody();
        }
        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new PersonNotFoundException(id);
        }
        throw new PersonServiceException("Request for retrieving a person failed with status "
            + response.getStatusCode() + " (" + response.getStatusCode().getReasonPhrase() + ")");
    }
}
