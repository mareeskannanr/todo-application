package com.todo.app.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.todo.app.utils.AppConstants;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "todo")
@Table(name = "todos")
public class Todo implements Serializable {

    private static final Long serialversionUID = -1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    public Long id;

    @Column(updatable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime createdOn = LocalDateTime.now();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime lastUpdatedOn =  LocalDateTime.now();

    @Column(unique = true, nullable = false)
    @NotBlank(message = AppConstants.CONTENT_ERROR_BLANK)
    public String content;

    public Todo() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    /*public void setCreated(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }*/

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(LocalDateTime lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Todo)) return false;
        Todo todo = (Todo) o;
        return hashCode() == todo.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCreatedOn(), getContent(), getLastUpdatedOn());
    }

    @Override
    public String toString() {
        return "Todo [id=" + id + ", createdOn=" + createdOn + ", lastUpdatedOn=" + lastUpdatedOn + ", content='" + content + ']';
    }
}
