package com.lolafelingcatering.application.data.service;

import com.lolafelingcatering.application.data.entity.Reservations;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReservationsService {

    private final ReservationsRepository repository;

    @Autowired
    public ReservationsService(ReservationsRepository repository) {
        this.repository = repository;
    }

    public Optional<Reservations> get(UUID id) {
        return repository.findById(id);
    }

    public Reservations update(Reservations entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Reservations> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
