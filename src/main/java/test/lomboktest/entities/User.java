package test.lomboktest.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {

    @Id
    @Column(name = "user_id")
    @GeneratedValue
    public Long id;

    @Column
    public String name;

    @Column
    public String password;

    @Column
    public String email;


}