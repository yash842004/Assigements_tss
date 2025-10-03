package com.tss.banking.entity;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import com.tss.banking.entity.eums.AdminRole;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "admins")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "first_name", nullable = false, length = 60)
	private String firstName;
	@Column(name = "last_name", nullable = false, length = 60)
	private String lastName;
	@Column(nullable = false, length = 180, unique = true)
	private String email;
	@Column(name = "password_hash", nullable = false, length = 255)
	private String passwordHash;
	@Builder.Default
	@Column(name = "is_active", nullable = false)
	private boolean active = true;
	@Column(name = "created_date", nullable = false)
	private LocalDateTime createdDate;
	@Column(name = "last_updated")
	private LocalDateTime lastUpdated;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "admin_roles", joinColumns = @JoinColumn(name = "admin_id"))
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 30)
	@Builder.Default
	private Set<AdminRole> roles = new LinkedHashSet<>();
	@PrePersist
	protected void onCreate() {
		if (createdDate == null) {
			createdDate = LocalDateTime.now();
		}
		if (lastUpdated == null) {
			lastUpdated = LocalDateTime.now();
		}
	}
}
