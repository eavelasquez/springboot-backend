package com.dev.springboot.backend.models.entity;

import java.util.Date;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;

@Entity
@Table(name = "clients")
public class Client implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "Missing name in body of request")
	@Size(min = 3, max = 36, message = "Minimum 3 characters - Maximum 36 characters")
	@Column(name = "name", length = 36, nullable = false)
	private String name;
	
	@NotEmpty(message = "Missing surname in body of request")
	@Size(min = 3, max = 36, message = "Minimum 3 characters - Maximum 36 characters")
	@Column(name = "surname", length = 36, nullable = false)
	private String surname;
	
	@NotEmpty(message = "Missing email in body of request")
	@Email(message = "Invalid email")
	@Column(name = "email", length = 72, unique = true, nullable = false)
	private String email;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.DATE)
	private Date createAt;
	
	@Column(name = "img", length = 128)
	private String img;
	
	@NotNull(message = "Missing region in body of request")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id", referencedColumnName = "id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Region region;
	
	@PrePersist
	public void prePersistCreateAt() {
		createAt = new Date();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Date getCreateAt() {
		return createAt;
	}
	
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	/**
	 * Attribute required when use Serializable
	 */
	private static final long serialVersionUID = 1L;

}
