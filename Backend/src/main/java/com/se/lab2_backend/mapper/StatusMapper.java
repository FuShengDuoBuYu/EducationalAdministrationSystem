package com.se.lab2_backend.mapper;

import com.se.lab2_backend.entity.Status;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StatusMapper extends JpaRepository<Status, Long> {
    @NotNull("没有状态信息！") List<Status> findAll();
    @NotNull("没有状态信息！") List<Status> findAllByIdentity(String identity);
    Optional<Status> findStatusByName(String statusName);
    Optional<Status> findStatusByStatusId(String statusId);
}