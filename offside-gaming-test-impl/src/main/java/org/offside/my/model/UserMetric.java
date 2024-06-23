package org.offside.my.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user_metrics")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_entity")
    @SequenceGenerator(name = "seq_entity", sequenceName = "seq_entity_id", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "gas_metric")
    private Double gasMetric;

    @Column(name = "hot_water_metric")
    private Double hotWaterMetric;

    @Column(name = "gold_water_metric")
    private Double coldWaterMetric;

    @CreatedDate
    @Column(name = "date_of_metric")
    private Date dateOfMetric;
    
}
