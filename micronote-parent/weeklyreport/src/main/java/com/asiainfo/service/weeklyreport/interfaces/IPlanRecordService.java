package com.asiainfo.service.weeklyreport.interfaces;

import java.util.List;

import com.asiainfo.domain.entity.weeklyreport.Plan;
import com.asiainfo.domain.entity.weeklyreport.PlanRel;
import com.asiainfo.domain.entity.weeklyreport.ReportRecord;

/**
 * 处理工作计划条目的业务逻辑
 * @author yi
 */
public interface IPlanRecordService{
	
	/**
	 * 取消计划
	 * @param planRecordId
	 * @return
	 */
	boolean canelPlan(long planId, String reason) throws Exception;
	
	/**
	 * 恢复取消计划
	 * @param planId
	 * @return
	 * @throws Exception
	 */
	public boolean recoverPlan(long planId) throws Exception;
	
	/**
	 * 确认完成计划
	 * @param planRecordId
	 * @return
	 */
	ReportRecord confirmePlan(long planId, long worklyReportId) throws Exception;
	
	/**
	 * 延迟计划
	 * @param planRecordId
	 * @return
	 */
	PlanRel delayPlan(long planId) throws Exception;

	/**
	 * 创建计划
	 * @param plan
	 * @return
	 */
	Plan createWeeklyPlan(Plan plan, int week);

	/**
	 * 修改计划
	 * @param plan
	 * @return
	 */

	boolean modifyWeeklyPlan(Plan plan);

	/**
	 * 删除计划
	 * @param planId
	 * @return
	 */
	Plan deleteWeeklyPlan(long planId);

	/**
	 * 获取下周计划
	 * @param lastTimeThisWeek
	 * @return
	 */
	List<Plan> queryNextWeekPlan(String userId);
    /**
     * 获取本周计划
     * @param userId
     * @return
     */
    List<Plan> queryThisWeekPlan(String userId);

}
