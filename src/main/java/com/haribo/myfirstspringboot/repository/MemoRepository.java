package com.haribo.myfirstspringboot.repository;

import com.haribo.myfirstspringboot.entity.Memo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // @Query
    // ':' for simple param, ':#' for complex param
    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :memoText where m.mno = :mno ")
    int updateMemoText(@Param("mno") Long mno, @Param("memoText") String memoText);

    @Transactional
    @Modifying
    @Query("update Memo m set m.memoText = :#{#param.memoText} where m.mno = :#{#param.mno}")
    int updateMemo(@Param("param") Memo memo);

    // @Query with pagination
    @Query(value = "select m from Memo m where m.mno > :mno",
            countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Memo> getListWithQuery(Long mno, Pageable pageable);

    // Pros about using @Query
    // Able to select necessary data as Object[]
    @Query(value = "select m.mno, m.memoText, CURRENT_DATE from Memo m where m.mno > :mno",
            countQuery = "select count(m) from Memo m where m.mno > :mno")
    Page<Object[]> getListWithQueryObject(Long mno, Pageable pageable);

    // Native SQL
    @Query(value = "select * from memo where mno > 0", nativeQuery = true)
    List<Object[]> getNativeResult();
}
