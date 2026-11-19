package com.restaurant.restaurant_manager.service;

import com.restaurant.restaurant_manager.model.Reserva;
import com.restaurant.restaurant_manager.repository.ReservaRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository repository;

    public ReservaService(ReservaRepository repository) {
        this.repository = repository;
    }

    // Consultar todas las reservas
    public List<Reserva> listar() {
        return repository.findAll();
    }

    // Buscar una reserva por ID
    public Reserva buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Reserva no encontrada"
                    )
                );
    }

    // Registrar una reserva
    public Reserva guardar(Reserva reserva) {

        if (reserva.getCantidadPersonas() == null
                || reserva.getCantidadPersonas() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La cantidad de personas debe ser mayor que cero"
            );
        }

        reserva.setEstado("PENDIENTE");

        return repository.save(reserva);
    }

    // Actualizar una reserva
    public Reserva actualizar(Long id, Reserva datos) {

        Reserva reserva = buscarPorId(id);

        reserva.setNombreCliente(datos.getNombreCliente());
        reserva.setTelefono(datos.getTelefono());
        reserva.setFecha(datos.getFecha());
        reserva.setHora(datos.getHora());
        reserva.setCantidadPersonas(datos.getCantidadPersonas());

        return repository.save(reserva);
    }

    // Cancelar una reserva
    public Reserva cancelar(Long id) {

        Reserva reserva = buscarPorId(id);

        reserva.setEstado("CANCELADA");

        return repository.save(reserva);
    }
}