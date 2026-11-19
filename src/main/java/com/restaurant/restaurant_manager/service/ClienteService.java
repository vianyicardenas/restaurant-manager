
package com.restaurant.restaurant_manager.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.restaurant.restaurant_manager.model.Cliente;
import com.restaurant.restaurant_manager.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public Cliente actualizar(Long id, Cliente datos) {
        Cliente cliente = buscarPorId(id);

        cliente.setNombre(datos.getNombre());
        cliente.setTelefono(datos.getTelefono());

        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}
