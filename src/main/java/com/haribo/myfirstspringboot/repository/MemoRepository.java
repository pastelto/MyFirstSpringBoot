package com.haribo.myfirstspringboot.repository;

import com.haribo.myfirstspringboot.entity.Memo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> { // <Class type, @Id type>

    // Query Method
    // Return type
    // 1) Select: List<>, Array[]
    // 2) Pageable type is in parameter: Page<E>
    List<Memo> findByMnoBetweenOrderByMnoDesc(Long from, Long to);

    // Simple then above
    Page<Memo> findByMnoBetween(Long from, Long to, Pageable pageable);

    void deleteMemoByMnoLessThan(Long num);
}
