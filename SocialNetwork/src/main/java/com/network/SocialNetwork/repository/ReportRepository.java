package com.network.SocialNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.network.SocialNetwork.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository <Report,Long>{}
    
