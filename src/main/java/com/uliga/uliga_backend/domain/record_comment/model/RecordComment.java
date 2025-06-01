package com.uliga.uliga_backend.domain.record_comment.model;

import com.uliga.uliga_backend.domain.common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.member.model.Member;
import com.uliga.uliga_backend.domain.record.model.Record;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "record_comment", catalog = "uliga_db")
public class RecordComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_comment_id")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private Record record;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;
}
