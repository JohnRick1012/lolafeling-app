package com.lolafelingcatering.application.data.service;

import com.lolafelingcatering.application.data.entity.Reservations;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationsRepository extends JpaRepository<Reservations, UUID> {

}