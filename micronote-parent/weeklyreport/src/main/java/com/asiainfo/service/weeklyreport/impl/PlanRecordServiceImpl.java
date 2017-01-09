package com.asiainfo.service.weeklyreport.impl;

import com.asiainfo.domain.entity.weeklyreport.Plan;
import com.asiainfo.repository.weeklyreport.PlanRepository;
import com.asiainfo.util.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.asiainfo.domain.entity.weeklyreport.ReportRecord;
import com.asiainfo.repository.weeklyreport.ReportRecordRepository;
import com.asiainfo.service.weeklyreport.interfaces.IPlanRecordService;
import com.asiainfo.util.consts.CommonConst.PlanRecordState;
import com.asiainfo.util.consts.CommonConst.WorkRecordState;
import com.asiainfo.util.time.TimeUtil;
import org.springframework.stereotype.Service;

import static com.asiainfo.util.CommonUtils.getNullPropertyNames;

/**
 * 简单的计划操作实现
 * 
 * @author yi
 *
 */
@Service
public class PlanRecordServiceImpl implements IPlanRecordService {

	@Autowired
	ReportRecordRepository reportRecordRepository;
	@Autowired
	private PlanRepository planRepository;
	@Override
	public boolean canelPlan(long planRecordId) throws Exception{
		// TODO 修改计划状态到取消 (canceled)
		Plan plan = planRepository.findOne(planRecordId);
		plan.setState(PlanRecordState.CANCELED);
		plan.setStartDate(System.currentTimeMillis());
		planRepository.save(plan);
		return true;
	}

	@Override
	public boolean confirmePlan(long planRecordId, long worklyReportId) throws Exception {
		 //  TODO 确认计划完成
		 //  查询现在要完成的计划 变成确认状态confirmed
		Plan plan = planRepository.findOne(planRecordId);
		plan.setState(PlanRecordState.CONFIRMED);
		plan.setStartDate(System.currentTimeMillis());
		
		//TODO 复制一个条目为新的完成工作
		ReportRecord workRecord = new ReportRecord();
		workRecord.setCreateDate(System.currentTimeMillis());
		workRecord.setContent(plan.getContent());
		workRecord.setState(WorkRecordState.WORKED);
		workRecord.setRecordAttachments(plan.getRecordAttachments());
		
		//TODO 保存計劃工作和工作的修改
		planRepository.save(plan);
		reportRecordRepository.save(workRecord);
			
		return true;
	}

	@Override
	public boolean delayPlan(long planRecordId) throws Exception{
		
		// TODO 延遲計劃
		Plan plan = planRepository.findOne(planRecordId);
		//取得下周五的日期
		long nextWeekEndDate = TimeUtil.getNextWeekEndDate();
		//判断一下现在的结束时间是否大于下周周末的时间
		if(plan.getEndDate() < nextWeekEndDate){
			//修改结束日期到下周五
			plan.setEndDate(nextWeekEndDate);
			planRepository.save(plan);
		}
		
		return true;
	}
	
	@Override
	public Plan createWeeklyPlan(Plan plan) {
		return planRepository.save(plan);
	}

	@Override
	public boolean modifyWeeklyPlan(Plan plan) {
		Plan oldPlan=planRepository.findOne(plan.getPlanId());
		String[] nullProperties=CommonUtils.getNullPropertyNames(plan);
		BeanUtils.copyProperties(plan,oldPlan,nullProperties);
		planRepository.save(plan);
		return true;
	}

	@Override
	public boolean deleteWeeklyPlan(long planId) {
		planRepository.delete(planId);
		return true;
	}
}
