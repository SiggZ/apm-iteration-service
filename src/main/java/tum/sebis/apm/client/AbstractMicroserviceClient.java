package tum.sebis.apm.client;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import tum.sebis.apm.security.SecurityUtils;
import tum.sebis.apm.web.rest.errors.ServiceNotReachableException;

import java.net.URI;
import java.util.List;

@Component
public abstract class AbstractMicroserviceClient<E> {

    private String serviceName;
    protected String serviceUrl;
    protected DiscoveryClient discoveryClient;
    protected RestOperations restTemplate;

    public AbstractMicroserviceClient(DiscoveryClient discoveryClient, RestOperations restTemplate, String serviceName) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
        this.serviceName = serviceName.toUpperCase();
    }

    // add abstract methods that should be implemented by the subclasses here

    private String retrieveServiceUrl() {
        List<ServiceInstance> instanceList = discoveryClient.getInstances(serviceName);
        if (instanceList == null || instanceList.size() < 1) {
            throw new ServiceNotReachableException(serviceName);
        }
        return instanceList.get(0).getUri().toString();
    }

    /**
     * Creates a URL
     *
     * @param path the path to the resource
     * @return a URL
     */
    protected String generateUrl(String path) {
        if (serviceUrl == null) {
            serviceUrl = retrieveServiceUrl();
        }
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

    /**
     *  Send an authorized request to the given URL. For authorization the current JWT from the security context
     *  is used.
     *
     * @param url the URL to send the request to
     * @param method the HTTP method to use
     * @param requestBody the request body to send
     * @param responseType the type of the data in the response body
     * @return a response entity containing the response from the service and/or the according status code
     */
    protected ResponseEntity<E> sendAuthorizedRequest(String url, HttpMethod method, E requestBody, Class<E> responseType) {
        HttpHeaders headers = new HttpHeaders();
        String jwt = SecurityUtils.getCurrentUserJWT();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        HttpEntity<E> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<E> response;
        try {
            response = restTemplate.exchange(URI.create(url), method, requestEntity, responseType);
        } catch (HttpClientErrorException e) {
            response = ResponseEntity.status(e.getStatusCode()).build();
        } catch (ResourceAccessException e) {
            throw new ServiceNotReachableException(serviceName);
        }
        return response;
    }
}
