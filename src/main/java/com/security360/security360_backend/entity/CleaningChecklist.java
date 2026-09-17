package com.security360.security360_backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cleaning_checklists")
public class CleaningChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private HousekeepingTask task;

    @Column(name = "checklist_item", nullable = false)
    private String checklistItem;

    @Column(name = "is_completed")
    private boolean isCompleted = false;

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public HousekeepingTask getTask() { return task; }
    public void setTask(HousekeepingTask task) { this.task = task; }
    public String getChecklistItem() { return checklistItem; }
    public void setChecklistItem(String checklistItem) { this.checklistItem = checklistItem; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}