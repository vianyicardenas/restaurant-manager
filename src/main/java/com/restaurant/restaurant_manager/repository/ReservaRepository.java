package com.restaurant.restaurant_manager.repository;

import com.restaurant.restaurant_manager.model.Reserva;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository
        extends JpaRepository<Reserva, Long> {

}