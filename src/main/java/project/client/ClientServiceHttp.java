package project.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import project.data.OrderRequest;

import java.util.List;

@Service
@Slf4j
public class ClientServiceHttp {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${client.petstore.url}")
    private String petstoreUrl;

    private final static String STATUS = "status";

    public <T> List<T> makeRequest(String status, ParameterizedTypeReference<List<T>> responseType) {
        ResponseEntity<List<T>> response;
        try {
            response = restTemplate.exchange(
                    petstoreUrl + "?"
                            + STATUS + "=" + status,
                    HttpMethod.GET,
                    null,
                    responseType);
        } catch (RestClientException e) {
            throw new RestClientException("error happend calling endpoint");
        }
        return response.getBody();
    }

    public OrderRequest registerOrder(OrderRequest request) {
        ResponseEntity<OrderRequest> responseEntity;

        HttpEntity httpEntity = new HttpEntity(request);

        try {
           responseEntity = restTemplate.exchange("https://petstore.swagger.io/v2/store/order",
                    HttpMethod.POST,
                   httpEntity,
                    OrderRequest.class);
        }catch (RestClientException e){
            throw new RestClientException("error happend store order");
        }
        return responseEntity.getBody();
    }
}
