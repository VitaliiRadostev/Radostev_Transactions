package ru.sber.sber_tech.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import java.math.BigDecimal;

/**
 * Счет клиента.
 *
 * @author Kiselev_Mikhail
 */
@Data
@Entity
@Accessors(chain = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_generator")
    @SequenceGenerator(name = "account_id_generator", sequenceName = "account_id_seq", allocationSize = 1)
    private Long id;

    private BigDecimal amount;

    @Version
    @JsonIgnore
    private Integer version;

    public Account(Long id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public Account() {
    }
}
