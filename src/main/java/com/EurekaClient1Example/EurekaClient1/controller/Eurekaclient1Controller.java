package com.EurekaClient1Example.EurekaClient1.controller;

import com.EurekaClient1Example.EurekaClient1.model.*;
//import com.sun.jersey.core.header.MediaTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
@RequestMapping("/employee")
public class Eurekaclient1Controller {
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    Environment environment;
    @GetMapping(value = "/calleurekaclient1")
    public ResponseEntity callEurekaClient1(){
        return new ResponseEntity("Hello From Client 1 From Wasim", HttpStatus.OK);
    }

    @GetMapping("/callEurekaClient2viaClient1")
    public ResponseEntity callClient2(){
        try {
            return new ResponseEntity(
                    restTemplate.getForObject(getEurkaClient2BaseUri() + "/consumer/calleurekaclient2", String.class), HttpStatus.OK);
        }catch (Exception exp) {
            return new ResponseEntity(
                    restTemplate.getForObject(getEurkaClient2BaseUri() + "/consumer/calleurekaclient2", String.class), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getallproducts")
    public ResponseEntity getAllProductsFromClient2(){
        try {
            return new ResponseEntity(
                    restTemplate.getForObject(getEurkaClient2BaseUri() + "/consumer/allproducts", String.class), HttpStatus.OK);
        }catch (Exception exp) {
            return new ResponseEntity(
                    restTemplate.getForObject(getEurkaClient2BaseUri() + "/consumer/allproducts", String.class), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value = "/getproductbyid/{id}")
    public ResponseEntity getProducByIDFromClient2(@PathVariable UUID id){
        try {
            return new ResponseEntity(
                    restTemplate.getForObject(getEurkaClient2BaseUri() + "/consumer/product/"+id, String.class), HttpStatus.OK);
        }catch (Exception exp) {
            return new ResponseEntity(
                    restTemplate.getForObject(getEurkaClient2BaseUri() + "/consumer/product/"+id, String.class), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
// employee/getproductbyid/id
    @DeleteMapping("/deleteproductbyid/{id}")
    public void deleteProductByIDFromClient2(@PathVariable UUID id){
        try {
            restTemplate.delete(getEurkaClient2BaseUri() + "/product/delete/"+id);
        }catch (Exception exp) {
            //log the error
        }
    }

    @PostMapping(value = "/updteproduct/")
    public ResponseEntity updateProductFromClient2(){
        try {
            return new ResponseEntity(
                    restTemplate.postForObject(getEurkaClient2BaseUri() + "/product/update/",new Product(UUID.randomUUID(), "Soap" , 99.99, "Body"),Product.class ), HttpStatus.OK);
        }catch (Exception exp) {
            return new ResponseEntity(
                    restTemplate.postForObject(getEurkaClient2BaseUri() + "/product/update/",new Product(UUID.randomUUID(), "Soap" , 99.99, "Body"),Product.class ), HttpStatus.OK);
        }
    }

    @GetMapping("/calleurekaclient1")
    public ResponseEntity callEurekaClient2(){
        return new ResponseEntity("Hello From Client 1 From port No. "+environment.getProperty("local.server.port")+" This is First Client service.", HttpStatus.OK);
    }

    private String getEurkaClient2BaseUri(){
        ServiceInstance serviceInstance =  loadBalancerClient.choose("EUREKA-CLIENT2");
        return serviceInstance.getUri().toString();
    }
}