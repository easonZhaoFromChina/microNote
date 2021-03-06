package com.asiainfo.repository.weeklyreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.asiainfo.domain.entity.weeklyreport.ReportRecord;

import java.util.List;

import javax.transaction.Transactional;

/**
 * Created by eason on 2017/1/5.
 */
@RepositoryRestResource(collectionResourceRel = "reportRecords",path = "reportRecords")
public interface ReportRecordRepository extends JpaRepository<ReportRecord,Long> {
//    public List<ReportRecord> findByCreateDateBetweenOrderByCreateDateDesc(@Param("startDate") long startDate, @Param("endDate") long endDate);
    List<ReportRecord> findByReportUserIdAndCreateDateBetweenOrderByCreateDateDesc(@Param("reportUserId") String reportUserId,@Param("startDate") long startDate, @Param("endDate") long endDate);
    @Transactional
    void deleteByReportRecordId(long reportRecordId);
}
