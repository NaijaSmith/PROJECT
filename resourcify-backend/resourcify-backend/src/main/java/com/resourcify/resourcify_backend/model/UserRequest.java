package com.resourcify.resourcify_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data // Lombok generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: useful for creating objects fluently
public class UserRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "resource_id", nullable = false)
    private int resourceId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

}
