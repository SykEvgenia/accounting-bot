package com.github.zhenya.accountingbot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "history")
@Getter
@Setter
@NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "sum")
    private Long sum;

    @Column(name = "account_id")
    private Long accountId;

    public History(Long accountId) {
        this.accountId = accountId;
    }
}
