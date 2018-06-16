package pl.mjbladaj.zaaw_java.server.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Data
@EqualsAndHashCode(exclude = {"account", "availableCurrency"})
@ToString(exclude = {"account", "availableCurrency"})
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "account_state",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"account_id", "available_currency_id"}))
public class AccountState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "available_currency_id", nullable = false)
    private AvailableCurrency availableCurrency;
}
