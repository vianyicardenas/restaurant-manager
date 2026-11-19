package com.restaurant.restaurant_manager.controller;

import com.restaurant.restaurant_manager.model.Reserva;
import com.restaurant.restaurant_manager.service.ReservaService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    // Consultar todas las reservas
    @GetMapping
    public List<Reserva> listar() {
        return service.listar();
    }

    // Buscar una reserva por ID
    @GetMapping("/{id}")
    public Reserva buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // Registrar una reserva
    @PostMapping
    public Reserva guardar(@RequestBody Reserva reserva) {
        return service.guardar(reserva);
    }

    // Actualizar una reserva
    @PutMapping("/{id}")
    public Reserva actualizar(
            @PathVariable Long id,
            @RequestBody Reserva reserva) {

        return service.actualizar(id, reserva);
    }

    // Cancelar una reserva
    @PatchMapping("/{id}/cancelar")
    public Reserva cancelar(@PathVariable Long id) {
        return service.cancelar(id);
    }
}