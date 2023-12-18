package com.example.weed.repository;

import com.example.weed.entity.Working;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface W1007_WorkingRepository extends JpaRepository<Working, Long> {
    List<Working> findAll();

}
