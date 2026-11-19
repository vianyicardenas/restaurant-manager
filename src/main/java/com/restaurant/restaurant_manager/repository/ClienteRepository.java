
package com.restaurant.restaurant_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restaurant.restaurant_manager.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
