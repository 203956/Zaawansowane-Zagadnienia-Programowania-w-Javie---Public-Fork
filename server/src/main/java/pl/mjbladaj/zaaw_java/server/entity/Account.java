package pl.mjbladaj.zaaw_java.server.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 3)
    @Column(unique = true)
    private String login;

    @NotBlank
    @Size(min = 3)
    private String password;

    private String mail;

    @OneToMany(mappedBy = "account")
    private List<AccountState> accountStates;
}