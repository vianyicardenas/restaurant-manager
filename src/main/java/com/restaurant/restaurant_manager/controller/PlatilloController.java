package com.restaurant.restaurant_manager.controller;

import com.restaurant.restaurant_manager.model.Platillo;
import com.restaurant.restaurant_manager.service.PlatilloService;

import lombok.NoArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/platillos")
public class PlatilloController {

    private final PlatilloService service;

    public PlatilloController(PlatilloService service) {
        this.service = service;
    }

    @GetMapping
    public List<Platillo> listar() {
        return service.listar();
    }

    @PostMapping
    public Platillo guardar(@RequestBody Platillo platillo) {
        return service.guardar(platillo);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @GetMapping("/{id}")
public Platillo buscarPorId(@PathVariable Long id) {
    return service.buscarPorId(id);
}

@PutMapping("/{id}")
public Platillo actualizar(
        @PathVariable Long id,
        @RequestBody Platillo platillo) {
    platillo.setId(id);
    return service.guardar(platillo);
}
}