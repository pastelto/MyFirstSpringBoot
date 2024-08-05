package com.haribo.myfirstspringboot.repository;

import com.haribo.myfirstspringboot.entity.Memo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.List;
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

    // Pagination
    @Test
    public void testPageDefault() {
        // Pagination uses findAll() method for pagination and sorting

        // page 1, get 10
        // of(int page, int size): page no and size (sorting is not defined)
        Pageable pageable = PageRequest.of(0, 10);

        // findAll(): method of PagingAndSortRepository
        // return type is Page
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);

        // Result
        // Hibernate:
        // select
        //      m1_0.mno,
        //      m1_0.memo_text
        // from memo m1_0
        // limit ?, ? // for pagination
        // Hibernate:
        // select count(m1_0.mno) // get total count
        // from memo m1_0

        System.out.println("-------------------------------");

        System.out.println("Total Pages: " + result.getTotalPages());
        System.out.println("Total Count: " + result.getTotalElements());
        System.out.println("Page Number: " + result.getNumber());
        System.out.println("Page Size: " + result.getSize());
        System.out.println("has next page?: " + result.hasNext());
        System.out.println("first page?: " + result.isFirst());

        // Result
        // -------------------------------
        // Total Pages: 10
        // Total Count: 99
        // Page Number: 0
        // Page Size: 10
        // has next page?: true
        // first page?: true

        System.out.println("-------------------------------");

        for (Memo memo : result.getContent()) { // getContent() returns List<Memo>
            System.out.println(memo);
        }
        // result
        // -------------------------------
        // Memo(mno=1, memoText=Sample...1)
        // Memo(mno=2, memoText=Sample...2)
        // Memo(mno=3, memoText=Sample...3)
        // Memo(mno=4, memoText=Sample...4)
        // Memo(mno=5, memoText=Sample...5)
        // Memo(mno=6, memoText=Sample...6)
        // Memo(mno=7, memoText=Sample...7)
        // Memo(mno=8, memoText=Sample...8)
        // Memo(mno=9, memoText=Sample...9)
        // Memo(mno=10, memoText=Sample...10)
    }

    // Sorting
    @Test
    public void testSort() {

        Sort sort1 = Sort.by("mno").descending();

        Pageable pageable = PageRequest.of(0, 10, sort1);

        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(System.out::println);

        // Result
        // Hibernate:
        // select
        //      m1_0.mno,
        //      m1_0.memo_text
        // from memo m1_0
        // order by m1_0.mno desc // added sorting
        // limit ?, ?
        // Hibernate:
        // select count(m1_0.mno)
        // from memo m1_0
        // Memo(mno=99, memoText=Sample...99)
        // Memo(mno=98, memoText=Sample...98)
        // Memo(mno=97, memoText=Sample...97)
        // Memo(mno=96, memoText=Sample...96)
        // Memo(mno=95, memoText=Sample...95)
        // Memo(mno=94, memoText=Sample...94)
        // Memo(mno=93, memoText=Sample...93)
        // Memo(mno=92, memoText=Sample...92)
        // Memo(mno=91, memoText=Sample...91)
        // Memo(mno=90, memoText=Sample...90)

        Sort sort2 = Sort.by("memoText").ascending();
        Sort sortAll = sort1.and(sort2); // connects with and

        Pageable pageable2 = PageRequest.of(0, 10, sortAll); // using combined sorting
    }

    // Query Method
    @Test
    public void testQueryMethods() {

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);

        for (Memo memo : list) {
            System.out.println(memo);
        }

        // Result
        // Hibernate:
        // select
        //      m1_0.mno,
        //      m1_0.memo_text
        // from memo m1_0
        // where
        // m1_0.mno between ? and ?
        // order by m1_0.mno desc
        // Memo(mno=80, memoText=Sample...80)
        // Memo(mno=79, memoText=Sample...79)
        // Memo(mno=78, memoText=Sample...78)
        // Memo(mno=77, memoText=Sample...77)
        // Memo(mno=76, memoText=Sample...76)
        // Memo(mno=75, memoText=Sample...75)
        // Memo(mno=74, memoText=Sample...74)
        // Memo(mno=73, memoText=Sample...73)
        // Memo(mno=72, memoText=Sample...72)
        // Memo(mno=71, memoText=Sample...71)
        // Memo(mno=70, memoText=Sample...70)
    }

    @Test
    public void testQueryMethodWithPageable() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());

        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(System.out::println);

        // Result
        // Hibernate:
        // select
        //      m1_0.mno,
        //      m1_0.memo_text
        // from memo m1_0
        // where
        // m1_0.mno between ? and ?
        // order by m1_0.mno desc // Sorting
        // limit ?
        // Hibernate:
        // select count(m1_0.mno)
        // from memo m1_0
        // where
        // m1_0.mno between ? and ?
        // Memo(mno=50, memoText=Sample...50)
        // Memo(mno=49, memoText=Sample...49)
        // Memo(mno=48, memoText=Sample...48)
        // Memo(mno=47, memoText=Sample...47)
        // Memo(mno=46, memoText=Sample...46)
        // Memo(mno=45, memoText=Sample...45)
        // Memo(mno=44, memoText=Sample...44)
        // Memo(mno=43, memoText=Sample...43)
        // Memo(mno=42, memoText=Sample...42)
        // Memo(mno=41, memoText=Sample...41)
    }

    @Commit
    @Transactional // @Commit & @Transactional is needed to use delete query
    @Test
    public void testDeleteQueryMethods() {
        memoRepository.deleteMemoByMnoLessThan(10L);
    }
}
