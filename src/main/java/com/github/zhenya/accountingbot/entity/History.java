package com.github.zhenya.accountingbot.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "sum")
    private Long sum;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch= FetchType.LAZY)
    private Account account;

    public History(Account account) {
        this.account = account;
    }
}
