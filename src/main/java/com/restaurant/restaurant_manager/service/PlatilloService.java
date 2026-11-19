package com.restaurant.restaurant_manager.service;

import com.restaurant.restaurant_manager.model.Platillo;
import com.restaurant.restaurant_manager.repository.PlatilloRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlatilloService {

    private final PlatilloRepository repository;

    public PlatilloService(PlatilloRepository repository) {
        this.repository = repository;
    }

    public List<Platillo> listar() {
        return repository.findAll();
    }

    public Platillo guardar(Platillo platillo) {
        return repository.save(platillo);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public Platillo buscarPorId(Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Platillo no encontrado"));
}
}