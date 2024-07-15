package com.network.SocialNetwork.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.network.SocialNetwork.entity.Statistic;

public interface StatisticRepository extends JpaRepository<Statistic,Long>{
    List<Statistic> findAllByVisitAtBetween(LocalDate startDate, LocalDate endDate);
}
