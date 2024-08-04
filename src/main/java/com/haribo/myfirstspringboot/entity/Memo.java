package com.haribo.myfirstspringboot.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "Memo")
@ToString
@Getter // To create Getter Method
@Builder // To create Object
@AllArgsConstructor // @AllArgsConstructor and @NoArgsConstructor should go as pair to avoid compile error
@NoArgsConstructor
public class Memo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // To create PK by automatically
    private Long mno;

    // @Column means column in database table
    // @Transient means field which is not created in the database table
    // @Column(columnDefinition = "varchar(255) default 'Yes'") // columnDefinition is for setting a default value
    @Column(length = 200, nullable = false)
    private String memoText;
}