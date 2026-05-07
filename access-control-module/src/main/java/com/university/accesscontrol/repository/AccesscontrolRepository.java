package com.university.accesscontrol.repository;

import com.university.accesscontrol.entity.AccesscontrolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccesscontrolRepository extends JpaRepository<AccesscontrolEntity, Long> {
}
