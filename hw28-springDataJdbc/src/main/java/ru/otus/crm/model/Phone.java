package ru.otus.crm.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table("phone")
public class Phone {

    @Id
    @Column("id")
    private Long id;

    @Column("number")
    private String number;

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }
}
