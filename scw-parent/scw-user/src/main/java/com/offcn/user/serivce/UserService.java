package com.offcn.user.serivce;


import com.offcn.user.pojo.TMember;

public interface UserService {

    public void registerUser(TMember member);

    public TMember login(String username,String password);

    public TMember findTmemberById(Integer id);
}
