
package com.restaurant.restaurant_manager.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.restaurant.restaurant_manager.model.Pedido;
import com.restaurant.restaurant_manager.service.PedidoService;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    // Consultar todos los pedidos
    @GetMapping
    public List<Pedido> listar() {
        return service.listar();
    }

    // Consultar un pedido por ID
    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // Registrar un pedido
    @PostMapping
    public Pedido guardar(@RequestBody Pedido pedido) {
        return service.guardar(pedido);
    }

    // Actualizar el estado de un pedido
    @PutMapping("/{id}/estado")
    public Pedido actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> datos) {

        return service.actualizarEstado(
                id,
                datos.get("estado")
        );
    }

    // Eliminar un pedido
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Pedido eliminado correctamente";
    }
}
