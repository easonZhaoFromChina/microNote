package com.asiainfo.domain.entity.weeklyreport;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "plan_record")
@PrimaryKeyJoinColumn(referencedColumnName = "reportRecordId")
public class PlanRecord extends ReportRecord {
	@Column(name = "content")
	private String planContent;
	private int state;
	@Column(name = "start_date")
	private Time startDate;
	@Column(name = "end_date")
	private Time endDate;

	public PlanRecord(Long reportRecordId, Time createDate, String planContent, int state, Time startDate,
			Time endDate) {
		super(reportRecordId, createDate);
		this.planContent = planContent;
		this.state = state;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public PlanRecord(Long reportRecordId, Time createDate) {
		super(reportRecordId, createDate);
	}

	public String getPlanContent() {
		return planContent;
	}

	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Time getStartDate() {
		return startDate;
	}

	public void setStartDate(Time startDate) {
		this.startDate = startDate;
	}

	public Time getEndDate() {
		return endDate;
	}

	public void setEndDate(Time endDate) {
		this.endDate = endDate;
	}
}