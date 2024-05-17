package com.github.zhenya.accountingbot.entity;

import lombok.*;

import javax.persistence.*;
import javax.transaction.Transactional;

@Entity
@Table(name = "account")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "name")
    private String name;

    @Column(name = "currency")
    private String currency;

    @Column(name = "sum")
    private Long sum;

    public Account(Long chatId) {
        this.chatId = chatId;
    }
}
