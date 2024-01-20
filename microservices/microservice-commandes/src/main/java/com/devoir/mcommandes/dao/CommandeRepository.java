package com.devoir.mcommandes.dao;

import com.devoir.mcommandes.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Integer> {
}
