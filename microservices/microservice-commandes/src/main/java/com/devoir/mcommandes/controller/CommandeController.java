package com.devoir.mcommandes.controller;

import com.devoir.mcommandes.configurations.ApplicationPropertiesConfiguration;
import com.devoir.mcommandes.dao.CommandeRepository;
import com.devoir.mcommandes.model.Commande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CommandeController implements HealthIndicator {
    @Autowired
    CommandeRepository commandeRepository;

    @Autowired
    ApplicationPropertiesConfiguration appProperties;

    // Affiche la liste de toutes les commandes des 10 / 20 derniers jours
    @GetMapping(value = "/Commandes")
    public List<Commande> listeDesCommandes() throws Exception {
        System.out.println(" ********* CommandeController Liste des commandes ");
        List<Commande> commandes = commandeRepository.findAll();
        if (commandes.isEmpty())
            throw new Exception("Aucune commande actuellement");

        LocalDate dateLimite = LocalDate.now().minusDays(appProperties.getCommandesLast());

        // Filtrer les commandes reçues au cours des 10 / 20 derniers jours
        List<Commande> commandesRecentes = commandes.stream()
                .filter(commande -> commande.getDate().isAfter(dateLimite))
                .collect(Collectors.toList());

        return commandesRecentes;

    }
    // Récuperer une commande par son id
    @GetMapping(value = "/Commandes/{id}")
    public Optional<Commande> recupererUneCommande(@PathVariable int id)throws Exception {
        System.out.println(" ********* CommandeController recupererUneCommande(@PathVariable int id) ");
        Optional<Commande> commande = commandeRepository.findById(id);
        if (!commande.isPresent())
            throw new Exception("La commande correspondante à l'id " + id + " n'existe pas");
        return commande;
    }
    //Créer une commande
    @PostMapping("/CreateCommandes")
    public ResponseEntity<String> createCommande(@RequestBody Commande nouvelleCommande) {
        if (nouvelleCommande == null || nouvelleCommande.getDescription() == null || nouvelleCommande.getQuantite() <= 0) {
            return new ResponseEntity<>("Les données de la commande sont invalides", HttpStatus.BAD_REQUEST);
        }

        try {
            Commande savedCommande = commandeRepository.save(nouvelleCommande);

            return new ResponseEntity<>("Commande créée avec succès. ID de la commande : " + savedCommande.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la création de la commande", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Mettre à jour une commande
    @PutMapping("/commandes/{id}")
    public ResponseEntity<String> updateCommande(@PathVariable Long id, @RequestBody Commande commandeMiseAJour) {

        if (commandeMiseAJour == null || commandeMiseAJour.getDescription() == null || commandeMiseAJour.getQuantite() <= 0) {
            return new ResponseEntity<>("Les données de la commande à mettre à jour sont invalides", HttpStatus.BAD_REQUEST);
        }

        if (!commandeRepository.existsById(Math.toIntExact(id))) {
            return new ResponseEntity<>("La commande à mettre à jour n'existe pas", HttpStatus.NOT_FOUND);
        }
        try {
            commandeMiseAJour.setId(Math.toIntExact(id));
            commandeRepository.save(commandeMiseAJour);

            return new ResponseEntity<>("Commande mise à jour avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la mise à jour de la commande", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //Supprimer une commande
    @DeleteMapping("/commandes/{id}")
    public ResponseEntity<String> deleteCommande(@PathVariable Long id) {
        if (!commandeRepository.existsById(Math.toIntExact(id))) {
            return new ResponseEntity<>("La commande à supprimer n'existe pas", HttpStatus.NOT_FOUND);
        }
        try {
            commandeRepository.deleteById(Math.toIntExact(id));

            return new ResponseEntity<>("Commande supprimée avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur lors de la suppression de la commande", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Health health() {
        System.out.println("****** Actuator : CommandeController health() ");
        List<Commande> commandes = commandeRepository.findAll();
        if (commandes.isEmpty()) {
            return Health.down().build();
        }
        return Health.up().build();
    }
}