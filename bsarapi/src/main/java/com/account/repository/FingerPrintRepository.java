package com.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.model.FingerPrint;

public interface FingerPrintRepository extends JpaRepository<FingerPrint, Long> {

}
