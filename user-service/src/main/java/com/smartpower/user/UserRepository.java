package com.smartpower.user;

import com.smartpower.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Transactional
    void deleteByEmail(String email);

    List<User> findByPincodeAndRole(String pincode, Role role);

    List<User> findByPincode(String pincode);
}
