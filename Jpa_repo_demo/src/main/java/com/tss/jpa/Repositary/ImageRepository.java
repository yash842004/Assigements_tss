package com.tss.jpa.Repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tss.jpa.entity.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long>{

}
