package com.example.weed.repository;

import com.example.weed.entity.Member;
import com.example.weed.entity.Working;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface W2002_WorkingRepository extends JpaRepository<Working, Long> {

    Optional<Working> findTopByMemberOrderByDateDescCheckInTimeDesc(Member foundMember);

//    Optional<Working> findFirstByDateAndCheckInTimeIsNotNullOrderByCheckInTimeDesc(LocalDate currentDate);
//    Optional<Working> findFirstByDateAndCheckOutTimeIsNotNullOrderByCheckOutTimeDesc(LocalDate currentDate);

    Optional<Working> findFirstByMemberAndCheckInTimeIsNotNullOrderByCheckInTimeDesc(Member foundMember);

    Optional<Working> findFirstByMemberAndCheckOutTimeIsNotNullOrderByCheckOutTimeDesc(Member foundMember);
}