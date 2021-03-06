package com.asiainfo.controller.weeklyreport;

import java.util.ArrayList;
import java.util.List;

import com.asiainfo.domain.entity.user.User;
import com.asiainfo.domain.entity.weeklyreport.WeeklyReportForWeb;
import com.asiainfo.domain.kara.KaraRequestObject;
import com.asiainfo.domain.kara.response.KaraAttachment;
import com.asiainfo.domain.kara.response.KaraField;
import com.asiainfo.domain.kara.response.KaraMessage;
import com.asiainfo.service.weeklyreport.interfaces.IPlanRecordService;
import com.asiainfo.service.weeklyreport.interfaces.IUserService;
import com.asiainfo.util.consts.CommonConst;
import com.asiainfo.util.kara.MessageConstructor;
import com.asiainfo.util.time.TimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.asiainfo.domain.entity.weeklyreport.Plan;
import com.asiainfo.domain.entity.weeklyreport.ReportRecord;
import com.asiainfo.domain.entity.weeklyreport.WeeklyReport;
import com.asiainfo.service.weeklyreport.interfaces.IWeeklyReportService;

/**
 * Created by eason on 2017/1/6.
 */
@RestController
public class WeeklyReportController {
	private Log logger = LogFactory.getLog(WeeklyReportController.class);
	@Autowired
	private IWeeklyReportService weeklyReportService;
    @Value("${kara.web.url.viewWeeklyReport}")
    private String viewWeeklReportUrl;
    @Autowired
    private IPlanRecordService planService;
    @Autowired
    private IUserService userService;
	@RequestMapping(path = "/createWeeklyReport", method = RequestMethod.POST)
	public WeeklyReport createWeeklyReport(@RequestParam("userId") String reportUserId) {
		return weeklyReportService.createWeeklyReport(reportUserId);
	}
	@RequestMapping(path = "/queryWeeklyRecordOrderByCreateDate", method = RequestMethod.POST)
	public List<ReportRecord> queryWeeklyRecordOrderByCreateDate(@RequestBody KaraRequestObject requestObject){
		/*
		1、取当前时间，获取周数据
		2、调用方法获取数据
		3、取其中的ReportRecord内容返回
		 */
		int weeklyNumber = TimeUtil.getWeekOfYear(System.currentTimeMillis());
		WeeklyReport weeklyReport = weeklyReportService.queryWeeklyReportByUserIdAndWeekly(requestObject.getUserId(),weeklyNumber);
		return weeklyReport.getReportRecord();
	}

	@RequestMapping(path = "/queryWeeklyRecord", method = RequestMethod.POST)
	public KaraMessage queryReportRecrodsByWeek(@RequestBody KaraRequestObject request){
		/*
		2017-1-12:Create by Zhaojl
		1、解析传入的对象获取到需要的值
		2、调用Repository层的方法查询数据
		3、组合成返回格式并返回
		 */
		//获取数据
		String userId=request.getUserId();
		long currentTime = 0;
		//如果不传值，则取当前时间
		if("".equals(request.getText())){
			currentTime = System.currentTimeMillis();
		}else{
			currentTime = Long.valueOf(request.getText());
		}

		//定义返回结构
		List<KaraField> list=new ArrayList<KaraField>();

		//处理userId为空的情况
		if(userId == null || "".equals(userId)){
			return null;
		}

		//调用方法
		int weeklyNumber = TimeUtil.getWeekOfYear(currentTime);
		WeeklyReport weeklyReport = weeklyReportService.queryWeeklyReportByUserIdAndWeekly(userId,weeklyNumber);

		//将数据写入到返回结构中
		for (ReportRecord reportRecord : weeklyReport.getReportRecord()) {
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTimeInMillis(reportRecord.getCreateDate());
			KaraField field=new KaraField();
			field.setTitle(CommonConst.KaraInfo.recordElement);
			field.setValue(reportRecord.getContent());
			field.setId(reportRecord.getReportRecordId());
			list.add(field);
		}
		KaraMessage karaMessage = MessageConstructor.constructMessageWithFields(CommonConst.KaraInfo.weeklyWork,list);
		return karaMessage;
	}

	@RequestMapping(path = "/queryPlans", method = RequestMethod.GET)
	public List<Plan> queryReportPlansByWeek(Long currentTime) {
		return null;
	}

	@PostMapping(path = "/submitWeeklyReport/{weeklyReportId}")
	public boolean submitWeeklyReport(@PathVariable("weeklyReportId") long weeklyReportId) throws Exception {
		 return weeklyReportService.submitWeeklyReport(weeklyReportId);
	}

    /**
     * 响应查看周报命令
     * @param request
     * @return
     */
	@PostMapping(path="/viewWeeklpReport")
	public KaraMessage viewWeeklyReport(@RequestBody KaraRequestObject request){
        logger.debug("request:"+request.toString());
        KaraAttachment attach=new KaraAttachment();
        String tmp=String.format(viewWeeklReportUrl, request.getUserId(),CommonConst.KaraInfo.urlSplit,CommonConst.KaraInfo.weeklyReportDetail);
        attach.setTitle(tmp);
//        attach.setTitle("<"+viewWeeklReportUrl+request.getUserId()+CommonConst.KaraInfo.urlSplit+CommonConst.KaraInfo.weeklyReportDetail+">");
        List<KaraAttachment> list=new ArrayList<>();
        list.add(attach);
        KaraMessage message=new KaraMessage();
        message.setText(CommonConst.KaraInfo.clickUrlToViewWeeklyReport);
        message.setAttachments(list.toArray(new KaraAttachment[0]));
        return message;
	}

    /**
     * 提供给web的查看周报详情接口
     * @param userId
     * @return
     */
    @GetMapping(path="/viewWeeklyReportForWeb/{user_id}")
    public WeeklyReportForWeb viewWeeklyReportForWeb(@PathVariable(name = "user_id",required = true) String userId){
        WeeklyReportForWeb weeklyReportForWeb=new WeeklyReportForWeb();
        WeeklyReport weeklyReport=weeklyReportService.queryWeeklyReportByUserIdAndWeekly(userId, TimeUtil.getWeekOfYear());
        BeanUtils.copyProperties(weeklyReport,weeklyReportForWeb);//copy properties
        //get UserInfo
        List<User> userList=new ArrayList<User>();
        if(!StringUtils.isEmpty(weeklyReport.getReportUserId())){
            User user=userService.queryUserById(weeklyReport.getReportUserId());
            if(user!=null){
                userList.add(user);
            }
        }
        if(!StringUtils.isEmpty(weeklyReport.getAuditingUserId())){
            User user=userService.queryUserById(weeklyReport.getAuditingUserId());
            if(user!=null){
                userList.add(user);
            }
        }
        //get reportRecord
        weeklyReportForWeb.setReportRecords(weeklyReport.getReportRecord());
        //query last week plan
        List<Plan> lastWeekplans=planService.queryThisWeekPlan(userId);
        //query next week plan
        List<Plan> nextWeekPlans=planService.queryNextWeekPlan(userId);
        weeklyReportForWeb.setLastWeekPlan(lastWeekplans);
        weeklyReportForWeb.setNextWeekPlan(nextWeekPlans);
        weeklyReportForWeb.setUser(userList);
        return weeklyReportForWeb;
    }

	@GetMapping(path="/queryReportUsers/{userId}")
	public Page<User> queryReportUsers(@PathVariable("userId") String authorId, Pageable pageable){
		return weeklyReportService.getReportUsers(authorId, pageable);
	}
    @GetMapping(path = "/queryCurrentWeekly")
    public List<Integer> queryCurrentWeekly(){
        List<Integer> result=new ArrayList<Integer>();
        int weekOfYear=TimeUtil.getWeekOfYear();
        for (int i=1;i<=weekOfYear;i++){
            result.add(i);
        }
        return result;
    }
}

