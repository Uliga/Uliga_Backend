package com.uliga.uliga_backend.domain.RecordComment.model;

import com.uliga.uliga_backend.domain.Common.BaseTimeEntity;
import com.uliga.uliga_backend.domain.Member.model.Member;
import com.uliga.uliga_backend.domain.Record.model.Record;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class RecordComment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "recordComment_id")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private Record record;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member creator;
}
