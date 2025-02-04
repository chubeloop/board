package com.boardproject.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseEntitiy {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationTime;


    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updateTime;
}
