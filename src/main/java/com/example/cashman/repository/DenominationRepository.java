package com.example.cashman.repository;

import com.example.cashman.domain.Denomination;
import org.springframework.data.repository.CrudRepository;

public interface DenominationRepository extends CrudRepository<Denomination, Long> {
}
