
package com.restaurant.restaurant_manager.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.restaurant_manager.model.Pedido;
import com.restaurant.restaurant_manager.model.DetallePedido;
import com.restaurant.restaurant_manager.model.Platillo;
import com.restaurant.restaurant_manager.repository.PedidoRepository;
import com.restaurant.restaurant_manager.repository.PlatilloRepository;
import java.time.LocalDateTime;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PlatilloRepository platilloRepository;

    public PedidoService(
            PedidoRepository pedidoRepository,
            PlatilloRepository platilloRepository) {

        this.pedidoRepository = pedidoRepository;
        this.platilloRepository = platilloRepository;
    }

    // Registrar un pedido
    @Transactional
    public Pedido guardar(Pedido pedido) {

        if (pedido.getDetalles() == null ||
                pedido.getDetalles().isEmpty()) {
            throw new RuntimeException(
                    "El pedido debe contener al menos un platillo");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (DetallePedido detalle : pedido.getDetalles()) {

            if (detalle.getCantidad() == null ||
                    detalle.getCantidad() <= 0) {
                throw new RuntimeException(
                        "La cantidad debe ser mayor que cero");
            }

            Long platilloId = detalle.getPlatillo().getId();

            Platillo platillo = platilloRepository
                    .findById(platilloId)
                    .orElseThrow(() -> new RuntimeException(
                            "Platillo no encontrado: " + platilloId));

            // Obtener el precio real desde la base de datos
            BigDecimal precio = BigDecimal.valueOf(
                    platillo.getPrecio());

            detalle.setPlatillo(platillo);
            detalle.setPrecioUnitario(precio);
            detalle.setPedido(pedido);

            BigDecimal subtotal = precio.multiply(
                    BigDecimal.valueOf(detalle.getCantidad()));

            total = total.add(subtotal);
        }

        pedido.setTotal(total);

        if (pedido.getFecha() == null) {
            pedido.setFecha(LocalDateTime.now());
        }

        if (pedido.getEstado() == null ||
                pedido.getEstado().isBlank()) {
            pedido.setEstado("PENDIENTE");
        }

        return pedidoRepository.save(pedido);
    }

    // Consultar todos los pedidos
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    // Buscar pedido por ID
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Pedido no encontrado"));
    }

    // Actualizar estado del pedido
    public Pedido actualizarEstado(Long id, String estado) {

        Pedido pedido = buscarPorId(id);

        List<String> estadosValidos = List.of(
                "PENDIENTE",
                "EN_PREPARACION",
                "LISTO",
                "ENTREGADO",
                "CANCELADO"
        );

        if (!estadosValidos.contains(estado)) {
            throw new RuntimeException("Estado no válido");
        }

        pedido.setEstado(estado);

        return pedidoRepository.save(pedido);
    }

    // Eliminar pedido
    public void eliminar(Long id) {

        Pedido pedido = buscarPorId(id);

        pedidoRepository.delete(pedido);
    }
}
