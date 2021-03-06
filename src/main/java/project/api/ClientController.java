package project.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import project.data.OrderRequest;
import project.data.ResponseJson;
import project.service.ClientService;

import javax.json.JsonPatch;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "KAFKA_EXEMPEL";

    @GetMapping(value = "/api/client/{available}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ResponseJson>> getAvailabelCustomers(
            @NotBlank @PathVariable(name = "available") String available
    ) {
        try {
            return new ResponseEntity<>(clientService.getCustomers(available), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/api/kafka/{message}")
    @ResponseStatus(HttpStatus.OK)
    public String getKafkaMessage(
            @NotBlank @PathVariable("message") String message) {
        kafkaTemplate.send(TOPIC, message);
        return "publish successfully";
    }

    @GetMapping(value = "/api/database")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getDataFromdatabase() {
        try {
            return new ResponseEntity<>(clientService.getData(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/api/order")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrderRequest> registerOrder(@RequestBody OrderRequest orderRequest) {
        try {
            return new ResponseEntity<OrderRequest>(clientService.storeOrder(orderRequest), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/api/modifyInfo", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getpatch(
            @RequestBody JsonPatch jsonPatch) {
        try {
            return new ResponseEntity<String>(clientService.changeInfo(jsonPatch), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

