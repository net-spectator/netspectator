package users.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "username")
    private String username;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @OneToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.REMOVE})
    @JoinColumn(name = "user_details")
    private UserDetails userDetails;

    public User(final UUID uuid, final String username, final Collection<Role> roles, final UserDetails userDetails) {
        this.uuid = uuid;
        this.username = username;
        this.roles = roles;
        this.userDetails = userDetails;
    }
}
