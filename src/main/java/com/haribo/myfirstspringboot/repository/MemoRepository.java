package com.haribo.myfirstspringboot.repository;

import com.haribo.myfirstspringboot.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {

}
