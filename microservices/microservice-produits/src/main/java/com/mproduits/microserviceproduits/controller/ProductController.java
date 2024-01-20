package com.mproduits.microserviceproduits.controller;

import com.mproduits.microserviceproduits.configurations.ApplicationPropertiesConfiguration;
import com.mproduits.microserviceproduits.dao.ProductDao;
import com.mproduits.microserviceproduits.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Configuration
//@EnableHystrixDashboard
//@EnableHystrix
@RestController
public class ProductController implements HealthIndicator {

    @Autowired
    ProductDao productDao;

    @Autowired
    ApplicationPropertiesConfiguration appProperties;

    // Affiche la liste de tous les produits disponibles
    @GetMapping(value = "/Produits")
    public List<Product> listeDesProduits() throws Exception {
        System.out.println(" ********* ProductController listeDesProduits() ");
        List<Product> products = productDao.findAll();
        if (products.isEmpty())
            throw new Exception("Aucun produit n'est disponible à la vente");
        List<Product> listeLimitee = products.subList(0,
                            appProperties.getLimitDeProduits());
        return listeLimitee;
    }
    // Récuperer un produit par son id
    @GetMapping(value = "/Produits/{id}")
    public Optional<Product> recupererUnProduit(@PathVariable int id) throws Exception {
        System.out.println(" ********* ProductController recupererUnProduit(@PathVariable int id) ");
        Optional<Product> product = productDao.findById(id);
        if (!product.isPresent())
            throw new Exception("Le produit correspondant à l'id " + id + " n'existe pas");
        return product;
    }

    @DeleteMapping("/Produits/{id}")
    public void deleteProduits(@PathVariable("id") final Long id) {
        productDao.deleteById(Math.toIntExact(id));
    }
    @PostMapping("/CreateProduits")
    public Product createEmployee(@RequestBody Product product) {
        return productDao.save(product);
    }
    @PutMapping("/UpdateProduit/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) throws Exception {
        Product existingProduct = productDao.findById(Math.toIntExact(id))
                .orElseThrow(() -> new Exception("Product not found with id: " + id));

        existingProduct.setId(updatedProduct.getId());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setName(updatedProduct.getName());

        return productDao.save(existingProduct);
    }
   /* @GetMapping("/myMessage")

    @HystrixCommand(fallbackMethod = "myHistrixbuildFallbackMessage",
            commandProperties ={@HystrixProperty(name =
                    "execution.isolation.thread.timeoutInMilliseconds", value = "1000")},
            threadPoolKey = "messageThreadPool")

    public String getMessage() throws InterruptedException {
        try {
            System.out.println("Message from EmployeeController.getMessage(): Begin To sleep for 3 seconds ");
            Thread.sleep(3000);
            return "Message from EmployeeController.getMessage(): End from sleep for 3 seconds ";
        } catch (Exception e) {
            System.out.println("Error in getMessage(): " + e.getMessage());
            throw e; // Rethrow the exception if needed
        }
    }
    private String myHistrixbuildFallbackMessage() {
        return "Message from myHistrixbuildFallbackMessage() : Hystrix Fallback message ( after timeout : 1 second )";
    }
*/
    @Override
    public Health health() {
        System.out.println("****** Actuator : ProductController health() ");
        List<Product> products = productDao.findAll();
        if (products.isEmpty()) {
            return Health.down().build();
        }
        return Health.up().build();
    }
}

