package com.smartpower.outage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutageRepository extends JpaRepository<OutageMessage, Long> {
    List<OutageMessage> findByPincode(String pincode);
}
