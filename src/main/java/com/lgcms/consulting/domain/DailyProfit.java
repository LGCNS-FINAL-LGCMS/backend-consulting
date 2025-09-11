package com.lgcms.consulting.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "uk_daily_profit_member_day_title",
                columnNames = {"memberId", "day", "title"})
})
public class DailyProfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private LocalDateTime day;

    private String title;

    @Column(name = "profit")
    private Long dailyProfit;
}
