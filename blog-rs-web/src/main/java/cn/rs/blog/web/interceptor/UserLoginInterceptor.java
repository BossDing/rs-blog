package cn.rs.blog.web.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import cn.rs.blog.bean.member.Member;
import cn.rs.blog.commoms.utils.ConfigUtil;
import cn.rs.blog.commoms.utils.MemberUtil;
import cn.rs.blog.core.utils.SpringContextHolder;
import cn.rs.blog.interceptor.RsInterceptor;
import cn.rs.blog.service.member.IMemberService;

/**
 * Created by rs
 */
public class UserLoginInterceptor implements RsInterceptor {

    @Override
    public boolean interceptor(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        try {
            Member loginUser = MemberUtil.getLoginMember(request);
            if (loginUser == null || loginUser.getId() == null) {
                if (!isAjaxRequest(request)){
                    response.sendRedirect(request.getContextPath() + "/member/login");
                }else {
                    response.setCharacterEncoding("utf-8");
                    out(response,"未登录");
                }
                return false;
            }else {
                IMemberService memberService = SpringContextHolder.getBean("memberService");
                Member findMember = memberService.findById(loginUser.getId());
                if(1 == Integer.parseInt((String) request.getServletContext().getAttribute(ConfigUtil.MEMBER_EMAIL_VALID.toUpperCase()))){
                    if(findMember.getIsActive() == 0){
                        if (!isAjaxRequest(request)){
                            response.sendRedirect(request.getContextPath() + "/member/active");
                        }else {
                            out(response,"未激活");
                        }
                        return false;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean isAjaxRequest(HttpServletRequest request){
        String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(header) ? true : false;
    }

    private void out(HttpServletResponse response, String msg){
        PrintWriter out = null;
        try {
            out = response.getWriter();
            JSONObject json = new JSONObject();
            json.put("code",-1);
            json.put("message",msg);
            out.print(json.toString());
            out.flush();
        } catch (IOException e1) {
            e1.printStackTrace();
        }finally {
            if (out != null){
                out.close();
            }
        }
    }
}
