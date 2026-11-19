
package com.restaurant.restaurant_manager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.restaurant.restaurant_manager.model.Cliente;
import com.restaurant.restaurant_manager.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cliente> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public Cliente guardar(@RequestBody Cliente cliente) {
        return service.guardar(cliente);
    }

    @PutMapping("/{id}")
    public Cliente actualizar(
            @PathVariable Long id,
            @RequestBody Cliente cliente) {
        return service.actualizar(id, cliente);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Cliente eliminado correctamente";
    }
}
