
package com.restaurant.restaurant_manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.restaurant.restaurant_manager.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
