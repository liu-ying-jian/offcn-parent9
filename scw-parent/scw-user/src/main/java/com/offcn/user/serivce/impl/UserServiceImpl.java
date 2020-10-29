package com.offcn.user.serivce.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberExample;
import com.offcn.user.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private TMemberMapper memberMapper;

    @Override
    public void registerUser(TMember member) {
        // 获取是否有账号重复的操作
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(member.getLoginacct());
        long l = memberMapper.countByExample(example);
        if(l > 0){
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        // 获取邮箱是否重复 （判断时注意大小写区分）

        // 完成注册功能
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 密码加密
        member.setUserpswd(encoder.encode(member.getUserpswd()));
        System.out.println(member.getUserpswd());
        // 认证
        member.setAuthstatus("0"); // 未认证
        member.setUsertype("0");  // 个人用户
        member.setAccttype("2"); // 个人
        System.out.println("插入数据:" + member.getLoginacct());
        // 记录插入
        memberMapper.insert(member);

    }

    @Override
    public TMember login(String username, String password) {
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(username);
        List<TMember> members = memberMapper.selectByExample(example);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (members !=  null && members.size() == 1){
            TMember tMember = members.get(0);

            boolean b = encoder.matches(password, tMember.getUserpswd());
            return b?tMember:null;
        }
        return null;
    }

    @Override
    public TMember findTmemberById(Integer id) {
        return memberMapper.selectByPrimaryKey(id);
    }
}
