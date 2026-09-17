package com.security360.security360_backend.repository;

import com.security360.security360_backend.entity.GstReturn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GstReturnRepository
        extends JpaRepository<GstReturn, Long> {

    List<GstReturn> findByStatusIgnoreCase(String status);

    List<GstReturn> findByReturnTypeIgnoreCase(String returnType);
}