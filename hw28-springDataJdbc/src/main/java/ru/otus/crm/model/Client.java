package ru.otus.crm.model;

import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("client")
public class Client {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @MappedCollection(idColumn = "id")
    private Address address;

    @MappedCollection(idColumn = "id", keyColumn = "id")
    private Set<Phone> phones;

    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
