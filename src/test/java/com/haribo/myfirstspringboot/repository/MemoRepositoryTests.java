package com.haribo.myfirstspringboot.repository;

import com.haribo.myfirstspringboot.entity.Memo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemoRepositoryTests {

    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass() {
        System.out.println(memoRepository.getClass().getName());
    }

    @Test
    public void testInsertDummies() {
        // Create new 100 memo object and insert using MemoRepository
        IntStream.rangeClosed(1,100).forEach(i -> {
            Memo memo = Memo.builder().memoText("Sample..."+i).build(); //.memoText should be not null
            memoRepository.save(memo); // save: Insert
        });
    }

    @Test
    public void testSelect() {
        // mno exists in the database
        Long mno = 100L;

        Optional<Memo> result = memoRepository.findById(mno); // SQL already executed.
        // .findById returns Optional type of java.util
        System.out.println("===============================");

        // Need to check whether result exists or not
        if (result.isPresent()) {
            Memo memo = result.get();
            System.out.println(memo);
        }

        // RESULT
        // Hibernate:
        // select
        //      m1_0.mno,
        //      m1_0.memo_text
        // from
        //      memo m1_0
        // where
        //      m1_0.mno=?
        // ===============================
        // Memo(mno=100, memoText=Sample...100)
    }

    @Transactional // Need @Transactional for using getOne() method
    @Test
    public void testSelectUsingGetOne() {
        // mno exists in the database
        Long mno = 100L;

        Memo memo = memoRepository.getOne(mno);
        System.out.println("===============================");

        System.out.println(memo); // SQL is executed when object really needed

        // RESULT
        // ===============================
        // Hibernate:
        // select
        //      m1_0.mno,
        //      m1_0.memo_text
        // from
        //      memo m1_0
        // where
        //      m1_0.mno=?
        // Memo(mno=100, memoText=Sample...100)
    }

    @Test
    public void testUpdate() {

        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
        System.out.println(memoRepository.save(memo));

        // RESULT
        // Hibernate: // select before update to check Memo object
        // select
        //      m1_0.mno
        //      m1_0.memo_text
        // from
        //      memo m1_0
        // where
        //      m1_0.mno=?
        // Hibernate: // Update
        // update
        //      memo
        // set
        //      memo_text=?
        // where
        //      mno=?
        // Memo(mno=100, memoText=Update Text)
    }

    @Test
    public void testDelete() {

        Long mno = 100L;
        memoRepository.deleteById(mno); // return type - void
        // if there isn't mno, org.spring.framework.dao.EmptyResultDataAccessException occurs

        // RESULT
        // Hibernate:
        // select
        //      m1_0.mno,
        //      m1_0.memo_text
        // from
        //      memo m1_0
        // where
        //      m1_0.mno=?
        // Hibernate:
        // delete
        //      from memo
        // where
        //      mno=?
    }
}
