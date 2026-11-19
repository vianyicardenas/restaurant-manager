package com.restaurant.restaurant_manager.repository;

import com.restaurant.restaurant_manager.model.Platillo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatilloRepository
        extends JpaRepository<Platillo, Long> {

}