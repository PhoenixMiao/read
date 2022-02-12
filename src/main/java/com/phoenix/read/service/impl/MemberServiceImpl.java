package com.phoenix.read.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.phoenix.read.common.CommonErrorCode;
import com.phoenix.read.common.CommonException;
import com.phoenix.read.common.Page;
import com.phoenix.read.common.PageParam;
import com.phoenix.read.dto.Order;
import com.phoenix.read.entity.Activity;
import com.phoenix.read.entity.Member;
import com.phoenix.read.entity.User;
import com.phoenix.read.mapper.ActivityMapper;
import com.phoenix.read.mapper.MemberMapper;
import com.phoenix.read.mapper.OrganizerMapper;
import com.phoenix.read.mapper.UserMapper;
import com.phoenix.read.service.MemberService;
import com.phoenix.read.util.TimeUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private OrganizerMapper organizerMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long makeOrder(Long activityId, Long userId) {
        Activity activity = activityMapper.selectByPrimaryKey(activityId);
        if(activity.getStatus()==-1) throw new CommonException(CommonErrorCode.ACTIVITY_HAS_END);
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getDepartment()==null||user.getMajor()==null||user.getName()==null||user.getStudentId()==null||user.getGrade()==null) throw new CommonException(CommonErrorCode.SELF_INFORMATION_UNWRITTEN);
        if(memberMapper.getMemberByUserIdAndActivityId(userId,activity.getId())!=null) throw  new CommonException(CommonErrorCode.ORDER_HAS_MADE);
        //todo 乐观锁
        activityMapper.makeOrder(activity.getPeople()+1,activityId);
        return memberMapper.makeOrder(activityId,userId, TimeUtil.getCurrentTimestamp(),0);
    }

    @Override
    public void check(Long id) {
        Member member = memberMapper.selectByPrimaryKey(id);
        Activity activity = activityMapper.selectByPrimaryKey(member.getAcivityId());
        if(activity.getType()==-1) throw new CommonException(CommonErrorCode.ACTIVITY_HAS_END);
        memberMapper.updateStatus(1,id);
    }

    @Override
    public Page<Order> getMemberListByUserId(int pageSize, int pageNum, Long userId) {
        PageHelper.startPage(pageNum,pageSize);
        List<Member> memberList = memberMapper.getMemberListByUserId(userId);
        Page page = new Page(new PageInfo<>(memberList));
        ArrayList<Order> orderArrayList = new ArrayList<>();
        for(Member ele:memberList){
            Activity activity = activityMapper.selectByPrimaryKey(ele.getAcivityId());
            orderArrayList.add(new Order(ele.getId(),activity.getName(),organizerMapper.selectByPrimaryKey(activity.getOrganizerId()).getName(),userMapper.selectByPrimaryKey(activity.getPublisherId()).getName(),activity.getStartTime(),activity.getStatus()));
        }
        orderArrayList.sort(new Comparator<Order>(){
            @Override
            public int compare(Order a,Order b) {
                return a.getStartTime().compareTo(b.getStartTime()); //这是顺序
            }
        });
        return new Page(new PageParam(pageSize,pageNum,"startTime desc"),page.getTotal(),page.getPages(),orderArrayList);
    }

    @Override
    public Member getMemberById(Long id) {
        return memberMapper.selectByPrimaryKey(id);
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Builder
class MemberThead {
    private String timeStamp;
    private int type;
    private Long activityId;

    public void updateStatus() {
        try {
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = mDateFormat.parse(timeStamp);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            Date time = calendar.getTime();
            Timer timer = new Timer();
            if(type==0) timer.schedule(new OrderStart(activityId), time);
            else if(type==1) timer.schedule(new ActivityStart(activityId),time);
            else if(type==-1) timer.schedule(new ActivityEnd(activityId),time);
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
}


class OrderStart extends TimerTask {

    @Autowired
    private ActivityMapper activityMapper;

    private Long id;

    public OrderStart(Long id){
        this.id = id;
    }

    public void run() {
        activityMapper.updateStatus(0,id);
    }
}

class ActivityStart extends TimerTask {

    @Autowired
    private ActivityMapper activityMapper;

    private Long id;

    public ActivityStart(Long id){
        this.id = id;
    }

    public void run() {
        activityMapper.updateStatus(1,id);
    }
}

class ActivityEnd extends TimerTask {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private MemberMapper memberMapper;

    private Long id;

    public ActivityEnd(Long id){
        this.id = id;
    }

    public void run() {
        activityMapper.updateStatus(-1,id);
        if(activityMapper.selectByPrimaryKey(id).getIsCheck()==1){
            List<Member> memberList = memberMapper.getUncommittedMemberList(id,0);
            for(Member ele:memberList){
                memberMapper.updateStatus(-1,ele.getId());
            }
        }
    }
}
