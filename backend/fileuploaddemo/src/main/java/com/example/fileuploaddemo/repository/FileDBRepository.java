package com.example.fileuploaddemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fileuploaddemo.entity.FileDB;

@Repository
public interface FileDBRepository extends JpaRepository<FileDB,Long>{
    
}
