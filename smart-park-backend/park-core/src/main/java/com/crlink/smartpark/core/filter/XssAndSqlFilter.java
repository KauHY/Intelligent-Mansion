package com.crlink.smartpark.core.filter;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class XssAndSqlFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String method = "GET";
        String param = "";
        XssAndSqlHttpServletRequestWrapper xssRequest = null;
        if (request instanceof HttpServletRequest) {
            method = ((HttpServletRequest) request).getMethod();
            xssRequest = new XssAndSqlHttpServletRequestWrapper((HttpServletRequest) request);
        }
        if ("POST".equalsIgnoreCase(method)) {
            param = this.getBodyString(xssRequest.getReader());
            if (StringUtils.isNotBlank(param)) {
                if (xssRequest.checkXSSAndSql(param)) {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=UTF-8");
                    PrintWriter out = response.getWriter();
//                    out.write(JSONResponseUtil.getWrappedERRString("您所访问的页面请求中有违反安全规则元素存在，拒绝访问!"));
                    out.write("您所访问的页面请求/提交的参数中有违反安全规则元素存在，拒绝访问!");
                    return;
                }
            }
        }
        if (xssRequest.checkParameter()) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
//            out.write(JSONResponseUtil.getWrappedERRString("您所访问的页面请求中有违反安全规则元素存在，拒绝访问!"));
            out.write("您所访问的页面请求/提交的参数中有违反安全规则元素存在，拒绝访问!");
            return;
        }
        chain.doFilter(xssRequest, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    // 获取request请求body中参数
    public static String getBodyString(BufferedReader br) throws IOException {
        List<String> strings = IOUtils.readLines(br);
        String str = StringUtils.join(strings, " ");
        br.close();
        return str;

    }
}