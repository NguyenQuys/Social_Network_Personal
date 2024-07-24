package com.network.SocialNetwork.repository;

import com.network.SocialNetwork.eenum.Role;
import com.network.SocialNetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.role.id = :roleId")
    List<User> findUsersByRoleId(@Param("roleId") Long roleId);

    // @Query("SELECT u FROM User u WHERE u.username or u.fullName = :keyword")
    List<User> findByFullNameContainingIgnoreCase(String keyword);

    List<User> findByRole(Role role);

    List<User> findByDateOfBirth(LocalDate dayOfBirth);
}
