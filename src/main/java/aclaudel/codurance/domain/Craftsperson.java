package aclaudel.codurance.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Craftsperson.
 */
@Entity
@Table(name = "craftsperson")
public class Craftsperson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "mentor")
    private Set<Craftsperson> mentees = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("mentees")
    private Craftsperson mentor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Craftsperson firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Craftsperson lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public Craftsperson email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Craftsperson> getMentees() {
        return mentees;
    }

    public Craftsperson mentees(Set<Craftsperson> craftspeople) {
        this.mentees = craftspeople;
        return this;
    }

    public Craftsperson addMentees(Craftsperson craftsperson) {
        this.mentees.add(craftsperson);
        craftsperson.setMentor(this);
        return this;
    }

    public Craftsperson removeMentees(Craftsperson craftsperson) {
        this.mentees.remove(craftsperson);
        craftsperson.setMentor(null);
        return this;
    }

    public void setMentees(Set<Craftsperson> craftspeople) {
        this.mentees = craftspeople;
    }

    public Craftsperson getMentor() {
        return mentor;
    }

    public Craftsperson mentor(Craftsperson craftsperson) {
        this.mentor = craftsperson;
        return this;
    }

    public void setMentor(Craftsperson craftsperson) {
        this.mentor = craftsperson;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Craftsperson)) {
            return false;
        }
        return id != null && id.equals(((Craftsperson) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Craftsperson{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
