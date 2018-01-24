package tum.sebis.apm.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestOperations;
import tum.sebis.apm.security.SecurityUtils;

import java.net.URI;
import java.util.List;

@Component
public abstract class AbstractMicroserviceClient<E> {

    private String serviceName;
    protected String serviceUrl;
    protected DiscoveryClient discoveryClient;
    protected RestOperations restTemplate;
    protected ObjectMapper mapper;

    public AbstractMicroserviceClient(DiscoveryClient discoveryClient, RestOperations restTemplate, String serviceName) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
        this.serviceName = serviceName.toUpperCase();
        this.serviceUrl = retrieveServiceUrl();
    }

    // add abstract methods that should be implemented by the subclasses here

    private String retrieveServiceUrl() {
        List<ServiceInstance> instanceList = discoveryClient.getInstances(serviceName);
        if (instanceList != null && instanceList.size() > 0) {
            return instanceList.get(0).getUri().toString();
        }
        return null;
    }

    /**
     * Creates a URL
     *
     * @param path the path to the resource
     * @return a URL
     */
    protected String generateUrl(String path) {
        String url = serviceUrl + "/api/" + path;
        return url;
    }

    /**
     * Helper method for creating a URL to a specific resource identified by an identifier
     *
     * @param path the path to the resource entities
     * @param id identifier for a specific resource
     * @return a URL to a specific resource
     */
    protected String generateUrl(String path, String id) {
        return generateUrl(path + "/" + id);
    }

    protected ResponseEntity<E> sendAuthorizedRequest(String url, HttpMethod method, E requestBody, Class<E> responseType) {
        HttpHeaders headers = new HttpHeaders();
        String jwt = SecurityUtils.getCurrentUserJWT();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        HttpEntity<E> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<E> response;
        try {
             response = restTemplate.exchange(URI.create(url), method, requestEntity, responseType);
        } catch (HttpStatusCodeException e) {
            response = ResponseEntity.status(e.getStatusCode()).build();
        }
        return response;
    }
}
