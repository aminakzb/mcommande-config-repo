package com.devoir.mcommandes.model;

import com.mproduits.microserviceproduits.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private int id;
    private String description;
    private int quantite;
    private LocalDate date;
    private float montant;

    private int produit_id;


}
